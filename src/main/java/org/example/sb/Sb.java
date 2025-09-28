package org.example.sb;

import org.springframework.ui.context.Theme;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Sb extends Thread {
    Barber barber;
    List<Customer> customerList;


    Semaphore mutEx ;
    Semaphore customerStat;
    Semaphore barberStat;


    static int waitSeats;
    volatile static int occupiedWaitSeats;

    public Sb(int waitSeats){
        customerList=new ArrayList<>();
        mutEx = new Semaphore(1);
        customerStat = new Semaphore(0);
        barberStat = new Semaphore(0);
        barber=new Barber(mutEx, barberStat, customerStat);
        Sb.waitSeats=waitSeats;
        occupiedWaitSeats=0;
    }

    public static synchronized void increaseUccupiedWaitSeats(){
        occupiedWaitSeats++;
    }

    public static synchronized void decreaseUccupiedWaitSeats(){
        occupiedWaitSeats--;
    }

    public void barberArrive() {
        barber.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addCustomer(int id) {
        Customer customer=new Customer(id, mutEx, barberStat, customerStat);
        customerList.add(customer);
        customer.start();
        try {
            Thread.sleep(customer.hairLength);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*public void addBarber(Barber barber){
        barber.setIdolBarber(idolBarber);
        barber.setWaitingCustomer(waitingCustomer);
        barberList.add(barber);
        idolBarber.release();
        barber.start();
    }*/
}
