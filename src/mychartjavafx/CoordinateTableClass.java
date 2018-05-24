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
    private final int x;
    private final int y;
    
    CoordinateTableClass(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    int getX(){
        return x;
    }
    
    int getY(){
        return y;
    }
}
