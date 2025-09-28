package org.example.sb;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Barber extends Thread {
    int id;
    Semaphore mutEx;

    Semaphore barberStat;

    Semaphore customerStat;

    public Barber(int id){
        this.id=id;
    }

    public Barber(Semaphore mutEx, Semaphore barberStat, Semaphore customerStat) {
        this.mutEx=mutEx;
        this.barberStat=barberStat;
        this.customerStat=customerStat;
    }

    @Override
    public void run() {
        Scanner sc=new Scanner(System.in);
        boolean shopOpen=true;
       while(shopOpen){
           System.out.println("Do u want to close shop(y/n)?");
           if("y".equals(sc.next())){
               break;
           }
           try {
               System.out.println("Barber is waiting for customer");
               customerStat.acquire();
               barberStat.release();
               System.out.println("Barber got a customer");

               mutEx.acquire();
               Sb.decreaseUccupiedWaitSeats();
               mutEx.release();

               cuttingHair();

           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
    }

    private void cuttingHair() {
        System.out.println("Barber is cutting hair.");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
