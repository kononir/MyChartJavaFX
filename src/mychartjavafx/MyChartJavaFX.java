/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mychartjavafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Vlad
 */
public class MyChartJavaFX extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        MyChartWidget chartWidget = new MyChartWidget();
        StackPane root = new StackPane(chartWidget);
        Scene scene = new Scene(root, 950, 500);
        
        primaryStage.setTitle("MyChart");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
