/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mychartjavafx;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;
import static mychartjavafx.MyMath.fact;

/**
 *
 * @author Vlad
 */
public class Calculation extends Thread {
    private final Exchanger<String> exchanger;
    private final double a;
    private final double xLowerLimit;
    private final double xUpperLimit;
    private final double stepH = 0.1;
    private final double inaccuracyE = 0.00001;
    private double rezult;
    
    public Calculation(Exchanger<String> exchanger,
            double a,
            double xLowerLimit,
            double xUpperLimit){
        this.exchanger = exchanger;
        this.a = a;
        this.xLowerLimit = xLowerLimit;
        this.xUpperLimit = xUpperLimit;
    }
    
    public final double getRezult(){
        return this.rezult;
    }
    
    @Override
    public final void run(){
        for(double currentX = xLowerLimit; 
                currentX <= xUpperLimit;
                currentX = MyMath.roundDouble(currentX + stepH, 1)){
            try {

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

                exchanger.exchange(String.valueOf(rezult));
            } catch(InterruptedException e){
                System.out.println("Some problems in Calculation!");
            }
        }
    }
    
    
}
