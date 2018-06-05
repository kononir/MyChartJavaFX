/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mychartjavafx;

import java.util.concurrent.Semaphore;
import static mychartjavafx.MyMath.fact;

/**
 *
 * @author Vlad
 */
public class Calculation extends Thread {
    private final Semaphore semaphore;
    private final double a;
    private final double xLowerLimit;
    private final double xUpperLimit;
    private final double stepH = 0.1;
    private final double inaccuracyE = 0.00001;
    private double rezult;
    
    public Calculation(Semaphore semaphore,
            double a,
            double xLowerLimit,
            double xUpperLimit){
        this.semaphore = semaphore;
        this.a = a;
        this.xLowerLimit = xLowerLimit;
        this.xUpperLimit = xUpperLimit;
    }
    
    public final double getRezult(){
        return this.rezult;
    }
    
    @Override
    public final void run(){
        try{
            for(double currentX = xLowerLimit; 
                    currentX <= xUpperLimit;
                    currentX = MyMath.roundDouble(currentX + stepH, 1)){
                semaphore.acquire();

                double summand;
                double currentY = 0;

                for(double i = 0; ; i++){
                    summand = Math.pow(currentX * Math.log(a), i) / fact(i);
                    if(summand < inaccuracyE){
                        break;
                    }
                    currentY += summand;
                }

                rezult = MyMath.roundDouble(currentY, 4);

                semaphore.release();

                long ms = 0;
                int ns = 20;
                Thread.sleep(ms, ns);
            }
        } catch(InterruptedException e){
            System.out.println("Some problems in Calculation!");
        }
    }
    
    
}
