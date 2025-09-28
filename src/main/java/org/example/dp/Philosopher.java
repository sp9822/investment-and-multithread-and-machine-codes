package org.example.dp;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Philosopher extends Thread {
    int id;
    Semaphore leftF;
    Semaphore rightF;
    Semaphore permission;
    int greed;

    public Philosopher(int id, Semaphore leftF, Semaphore rightF, Semaphore permission, int greed) {
        this.id = id;
        this.leftF = leftF;
        this.rightF = rightF;
        this.permission=permission;
        this.greed=greed;
    }

    @Override
    public void run() {
        Scanner sc=new Scanner(System.in);
        int i=0;
        while(i<=greed) {
            think();

            try {
                permission.acquire();
                leftF.acquire();
                rightF.acquire();
                eat(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            rightF.release();
            leftF.release();
            permission.release();

            think();
            i++;
        }
        System.out.println("Philosopher " + id + " stopped after eating: "+i+" times.");
    }

    private void eat(int i) {
        //System.out.println("Philosopher: "+id+" is eating ");
        System.out.println("Philosopher: "+id+" is eating "+(i+1)+"th time ");
        sleepRandom();
    }

    private void think() {
        System.out.println("Philosopher: "+id+" is thinking");
        sleepRandom();
    }

    private void sleepRandom() {
        try {
            Thread.sleep((long) (500+Math.random()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
