/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mychartjavafx;

import java.util.concurrent.Exchanger;

/**
 *
 * @author Vlad
 */
public class CreateChartButtonController {

    private final double a;
    private final double xLowerLimit;
    private final double xUpperLimit;
    private final Exchanger<String> exchanger;

    public CreateChartButtonController(double a,
            double xLowerLimit,
            double xUpperLimit,
            Exchanger<String> exchanger) {
        this.a = a;
        this.xLowerLimit = xLowerLimit;
        this.xUpperLimit = xUpperLimit;
        this.exchanger = exchanger;
    }

    public final void controll() {
        Calculation calculation = new Calculation(
                exchanger,
                a,
                xLowerLimit,
                xUpperLimit
        );

        Thread calculationThread = new Thread(calculation);
        calculationThread.setDaemon(true);
        calculationThread.start();
    }
}
