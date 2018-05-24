/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mychartjavafx;

/**
 *
 * @author Vlad
 */
public class CreateChartButtonController {
    private final double a;
    private final double xLowerLimit;
    private final double xUpperLimit;
    private final double stepH = 0.1;
    private final double inaccuracyE = 0.00001;
    private double currentX;
    
    public CreateChartButtonController(double a, double xLowerLimit, double xUpperLimit){
        this.a = a;
        this.xLowerLimit = xLowerLimit;
        this.xUpperLimit = xUpperLimit;
        currentX = xLowerLimit;
    }
    
    public final double controll(){
        if(currentX > xUpperLimit){
            double noX = -1;
            return noX;
        }
        else{
            double summand;
            double currentY = 0;
            
            for(double i = 0; ; i++){
                summand = Math.pow(currentX * Math.log(a), i) / fact(i);
                if(summand < inaccuracyE){
                    break;
                }
                currentY += summand;
            }
            
            currentX += stepH;
            
            return currentY;
        }
    }
    
    private double fact(double x){
        if(x > 0)
            return x * fact(x - 1);
        else if(x == 0)
            return 1;
        else
            return -1;
    }
}
