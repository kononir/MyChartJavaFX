/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mychartjavafx;

import java.util.concurrent.Exchanger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author Vlad
 */
public class MyChartJavaFX extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        AnchorPane root = new AnchorPane();
        
        TableView<CoordinateTableClass> coordinateTable = new TableView();
        TableColumn<CoordinateTableClass, String> xColumn = new TableColumn("x");
        TableColumn<CoordinateTableClass, String> yColumn = new TableColumn("y");
        coordinateTable.getColumns().addAll(xColumn, yColumn);
        
        xColumn.setCellValueFactory(new PropertyValueFactory<>("x"));
        yColumn.setCellValueFactory(new PropertyValueFactory<>("y"));
        
        coordinateTable.setMaxWidth(163);
                
        AnchorPane.setLeftAnchor(coordinateTable, 0.0);
        AnchorPane.setTopAnchor(coordinateTable, 0.0);
        AnchorPane.setBottomAnchor(coordinateTable, 0.0);
        root.getChildren().add(coordinateTable);
        
        Label scaleLabel = new Label("Масштаб - 1:1");
        scaleLabel.setFont(new Font("Arial", 15));
        
        Label aLabel = new Label("a: ");
        aLabel.setFont(new Font("Arial", 15));
        TextField aTextField = new TextField();
        HBox aInputHBox = new HBox(aLabel, aTextField);
        
        Label xLabel = new Label("x: [");
        xLabel.setFont(new Font("Arial", 15));
        TextField xLowerLimitTextField = new TextField();
        Label xSemicolonLabel = new Label(";");
        xSemicolonLabel.setFont(new Font("Arial", 15));
        TextField xUpperLimitTextField = new TextField();
        Label xBracketLabel = new Label("]");
        xBracketLabel.setFont(new Font("Arial", 15));
        HBox xInputHBox = new HBox(
                xLabel,
                xLowerLimitTextField,
                xSemicolonLabel,
                xUpperLimitTextField,
                xBracketLabel
        );
        
        Button createChartButton = new Button("Построить график");
        createChartButton.setOnAction(createChart -> {
            String aString = aTextField.getText();
            double a = Double.valueOf(aString);
            String xLowerLimitString = xLowerLimitTextField.getText();
            double xLowerLimit = Double.valueOf(xLowerLimitString);
            String xUpperLimitString = xUpperLimitTextField.getText();
            double xUpperLimit = Double.valueOf(xUpperLimitString);
            
            ObservableList<CoordinateTableClass> tableCoordinatesList 
                    = FXCollections.observableArrayList(); 
        
            coordinateTable.setItems(tableCoordinatesList);
            
            MyChart functionChart = new MyChart(460, 775);
            functionChart.createWorkingSpace(xLowerLimit, xUpperLimit);
            
            AnchorPane.setBottomAnchor(functionChart, 35.0);
            AnchorPane.setLeftAnchor(functionChart, 170.0);
            AnchorPane.setRightAnchor(functionChart, 15.0);
            root.getChildren().add(functionChart);
            
            Exchanger<String> exchanger = new Exchanger();
            
            CreateChartButtonController buttonController
                    = new CreateChartButtonController(
                            a,
                            xLowerLimit,
                            xUpperLimit,
                            exchanger
                    );
            
            double stepH = 0.1;
            
            buttonController.controll();
            for(double currentX = xLowerLimit;
                    currentX <= xUpperLimit;
                    currentX = MyMath.roundDouble(currentX + stepH, 1)){
                updateAll(currentX, exchanger, tableCoordinatesList, functionChart);
                
            }
            
            setOnGraphicScrolling(functionChart, scaleLabel);
        });
        
        HBox functionalItemsHBox = new HBox(
                scaleLabel,
                aInputHBox,
                xInputHBox,
                createChartButton
        );
        functionalItemsHBox.setSpacing(7);

        AnchorPane.setBottomAnchor(functionalItemsHBox,  5.0);
        AnchorPane.setLeftAnchor(functionalItemsHBox,  170.0);
        root.getChildren().add(functionalItemsHBox);
        
        Scene scene = new Scene(root, 950, 500);
        
        primaryStage.setTitle("MyChart");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    void updateAll(double currentX,
            Exchanger<String> exchanger,
            ObservableList<CoordinateTableClass> tableCoordinatesList,
            MyChart functionChart){
        Platform.runLater(() -> {
            try {
                double currentY = Double.valueOf(exchanger.exchange(""));

                String currentXString = String.valueOf(currentX);
                String currentYString = String.valueOf(currentY);

                CoordinateTableClass point = new CoordinateTableClass(
                        currentXString, currentYString
                );
                tableCoordinatesList.add(point);

                functionChart.repaint(currentX, currentY);

            } catch (InterruptedException ex) {
                System.out.println("Some problems in Drawing!");
            }
        });
    }
    
    void setOnGraphicScrolling(MyChart functionChart, Label scaleLabel){
        ScrollPane scroll = functionChart.getScroll();
        Canvas graphicCanvas = functionChart.getGraphicCanvas();
        
        scroll.setOnKeyPressed(ctrlKey -> { 
            KeyCode keyCode = ctrlKey.getCode();
            if(keyCode.equals(KeyCode.CONTROL)){
                graphicCanvas.setOnScroll(scrolling -> {
                    scrolling.consume();

                    double zoomIntensity = 0.05;
                    double zoomDelta = scrolling.getTextDeltaY();
                    double zoomFactor = Math.exp(zoomDelta * zoomIntensity);

                    double prevScale = graphicCanvas.getScaleX();
                    double newScale = prevScale * zoomFactor;
                    
                    double roundNewScale = MyMath.roundDouble(newScale, 2);
                    String newScaleString = String.valueOf(roundNewScale);
                    String scaleLabelText = "Масштаб - " + newScaleString 
                                          + ":" + newScaleString;
                    scaleLabel.setText(scaleLabelText);

                    graphicCanvas.setScaleX(newScale);
                    graphicCanvas.setScaleY(newScale);

                    scroll.layout();
                });
            }
        });

        scroll.setOnKeyReleased(ctrlKey -> {
            KeyCode keyCode = ctrlKey.getCode();
            if(keyCode.equals(KeyCode.CONTROL)){
                graphicCanvas.setOnScroll(null);
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
