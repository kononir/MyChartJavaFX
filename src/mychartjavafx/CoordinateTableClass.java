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
public class CoordinateTableClass {
    private final String x;
    private final String y;
    
    CoordinateTableClass(String x, String y){
        this.x = x;
        this.y = y;
    }
    
    public String getX(){
        return x;
    }
    
    public String getY(){
        return y;
    }
}
