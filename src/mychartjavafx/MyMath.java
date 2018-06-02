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
public class MyMath {
    public static double roundDouble(double number, double numberOfDigits){
        double decades = Math.pow(10, numberOfDigits);
        double intPart = number * decades;
        double intPartRound = Math.round(intPart);
        double roundNumber = intPartRound / decades;
                
        return roundNumber;
    }
}
