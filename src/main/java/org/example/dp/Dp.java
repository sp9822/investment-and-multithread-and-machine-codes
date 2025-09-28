package org.example.dp;

import java.util.List;
import java.util.concurrent.Semaphore;

public class Dp {
    public void startMeet(List<Philosopher> philosophers, List<Semaphore> forks) {
        boolean cont=true;

        while (cont){
            for(Philosopher cp:philosophers){
                cp.start();
            }
        }
    }
}
