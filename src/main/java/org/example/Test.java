package org.example;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Test {
    volatile static int i;
    static int n;

    Test(int n){
        this.i=1;
        this.n=n;
    }

    public void printNums(int n) {
        this.i = 1;
        this.n=n;

        Thread et = new Thread(() -> {
            printEven();
        });
        et.setName("Even thread");

        Thread ot = new Thread(() -> {
            printOdd();
        });
        ot.setName("Odd thread");
        et.start();
        ot.start();
    }

    public synchronized void printEven() {
        while (i <= n) {
            if (i % 2 == 0) {
                System.out.println(Thread.currentThread().getName() + " " + i);
                i++;
                notifyAll();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized void printOdd() {
        while (i <= n) {
            if (i % 2 == 1) {
                System.out.println(Thread.currentThread().getName() + " " + + i);
                i++;
                notifyAll();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
