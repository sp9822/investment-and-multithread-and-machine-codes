package org.example;

import org.example.dp.Dp;
import org.example.dp.Philosopher;
import org.example.sb.Barber;
import org.example.sb.Customer;
import org.example.sb.Sb;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {

        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.

        //oddEvenInTT();
        //diningPhilosopher();
        sleepingBarber();
    }

    private static void sleepingBarber() {
        Scanner sc =new Scanner(System.in);

        Sb sb=new Sb(2);

        /*System.out.println("Total barbers.");
        int tb=sc.nextInt();
        for(int i=0;i<tb;i++){
            sb.addBarber(new Barber((i+1)));
        }*/

        sb.barberArrive();

        System.out.println("Total customers.");
        int tc=sc.nextInt();
        for(int i=0;i<tc;i++){
            sb.addCustomer(i);
        }
    }

    private static void diningPhilosopher() {
        Scanner sc =new Scanner(System.in);
        Dp dp=new Dp();

        System.out.println("Total Forks.");
        int tf=sc.nextInt();
        List<Semaphore> forks=new ArrayList<>();
        for(int i=0;i<tf;i++){
            forks.add(new Semaphore(1, true));
        }

        Semaphore permission=new Semaphore(tf/2, true);

        List<Philosopher> philosophers=new ArrayList<>();
        System.out.println("Total philosophers no.");
        int tp=sc.nextInt();
        for(int i=0;i<tp;i++){
            philosophers.add(new Philosopher(i
                    , forks.get(i%tf)
                    , forks.get((i+1)%tf)
                    , permission
                    , ((i+1)*17%7)));
        }

        Dp dpo=new Dp();
        dpo.startMeet(philosophers, forks);
    }

    private static void oddEvenInTT() {
        Scanner sc=new Scanner(System.in);
        System.out.println("Provide no.");
        ThreadTest test=new ThreadTest(sc.nextInt());

        Thread et = new Thread(() -> {
            test.printEven();
        });
        et.setName("Even thread");

        Thread ot = new Thread(() -> {
            test.printOdd();
        });
        ot.setName("Odd thread");

        et.start();
        ot.start();
    }
}