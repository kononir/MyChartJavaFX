/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mychartjavafx;

import java.util.concurrent.Semaphore;

/**
 *
 * @author Vlad
 */
public class CreateChartButtonController {
    private final double a;
    private final double xLowerLimit;
    private final double xUpperLimit;
    private double rezult;
    private Calculation calculation;
    private final Semaphore semaphore;
    
    public CreateChartButtonController(double a,
            double xLowerLimit,
            double xUpperLimit,
            Semaphore semaphore){
        this.a = a;
        this.xLowerLimit = xLowerLimit;
        this.xUpperLimit = xUpperLimit;
        this.semaphore = semaphore;
    }
    
    public final double getRezult(){
        rezult = calculation.getRezult();
        return this.rezult;
    }
    
    public final void controll(){
        calculation = new Calculation(semaphore, a, xLowerLimit, xUpperLimit);
        calculation.start();
    }
    
    
    
    
}
