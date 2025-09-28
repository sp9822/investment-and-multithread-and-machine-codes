package org.example.sb;

import java.util.concurrent.Semaphore;

public class Customer extends Thread{
    int id;
    int hairLength;

    Semaphore mutEx = new Semaphore(1);

    Semaphore barberStat = new Semaphore(0);

    Semaphore customerStat = new Semaphore(0);


    public Customer(int id, Semaphore mutEx, Semaphore barberStat, Semaphore customerStat){
        this.id=id;
        this.hairLength=((id+1)*17%8);
        this.mutEx=mutEx;
        this.barberStat=barberStat;
        this.customerStat=customerStat;
    }

    @Override
    public void run() {
        try {
            mutEx.acquire();
            if (Sb.occupiedWaitSeats < Sb.waitSeats) {
                Sb.increaseUccupiedWaitSeats();
                mutEx.release();
                System.out.println("customer: " + id + " is waiting");
                customerStat.release();

                barberStat.acquire();
                gettingHairCut();
                System.out.println("Customer: "+id+" got haircut.");
            } else {
                System.out.println("Customer id: " + id + " left due to no available seat");
                mutEx.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            hairLength--;
        }
    }


    private void gettingHairCut() {
        System.out.println("Customer: "+id+" is getting a haircut.");
        try {
            Thread.sleep(hairLength);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
