/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mychartjavafx;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import mymath.MyMath;

/**
 *
 * @author Vlad
 */
public class MyChartWidget extends AnchorPane {
    
    private Canvas graphicCanvas;
    private ScrollPane scroll;
    private final double coordinateXIndent = 300;
    private final double coordinateYIndent = 200;
    private final double border = 50;
    private final double inaccuracy = 15;
    private double coordinatesOriginX;
    private double coordinatesOriginY;
    private final List<Point> pointsList;
    private double personalX;

    public MyChartWidget() {
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
        super.getChildren().add(coordinateTable);

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
            boolean answerCheck = checkInput(
                    aTextField,
                    xLowerLimitTextField,
                    xUpperLimitTextField
            );

            if (!answerCheck) {
                Alert wrongInputAllert = new Alert(Alert.AlertType.ERROR);
                wrongInputAllert.setTitle("Wrong input!");
                wrongInputAllert.setHeaderText(
                        "Неверный ввод! Пожалуйста проверьте введённые данные и повторите попытку."
                );
                wrongInputAllert.showAndWait();
                return;
            }

            String aString = aTextField.getText();
            double a = Double.valueOf(aString);
            String xLowerLimitString = xLowerLimitTextField.getText();
            double xLowerLimit = Double.valueOf(xLowerLimitString);
            String xUpperLimitString = xUpperLimitTextField.getText();
            double xUpperLimit = Double.valueOf(xUpperLimitString);

            ObservableList<CoordinateTableClass> tableCoordinatesList
                    = FXCollections.observableArrayList();

            coordinateTable.setItems(tableCoordinatesList);
            
            scroll = new ScrollPane();
            graphicCanvas = new Canvas();
            makeWorkingSpace(xLowerLimit, xUpperLimit);

            AnchorPane.setBottomAnchor(scroll, 35.0);
            AnchorPane.setLeftAnchor(scroll, 170.0);
            AnchorPane.setRightAnchor(scroll, 15.0);
            super.getChildren().add(scroll);

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
                
            personalX = xLowerLimit;

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run(){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                if(personalX <= xUpperLimit){
                                    double currentY = Double.valueOf(exchanger.exchange(""));
                                    String currentXString = String.valueOf(personalX);
                                    String currentYString = String.valueOf(currentY);

                                    CoordinateTableClass point = new CoordinateTableClass(
                                            currentXString, currentYString
                                    );

                                    tableCoordinatesList.add(point);
                                    repaint(personalX, currentY);
                                    updatePersonalX();
                                }
                                else{
                                    scheduler.shutdown();
                                }
                            } catch (InterruptedException ex) {
                                System.out.println("Some problems in Drawing!");
                            }

                        }

                        public void updatePersonalX(){
                            personalX = MyMath.roundDouble(personalX + stepH, 1);
                        }

                    });
                }
            }, 0, 1, TimeUnit.MILLISECONDS);

            setOnGraphicScrolling(scaleLabel);
        });

        HBox functionalItemsHBox = new HBox(
                scaleLabel,
                aInputHBox,
                xInputHBox,
                createChartButton
        );
        functionalItemsHBox.setSpacing(7);

        AnchorPane.setBottomAnchor(functionalItemsHBox, 5.0);
        AnchorPane.setLeftAnchor(functionalItemsHBox, 170.0);
        
        super.getChildren().add(functionalItemsHBox);
        
        pointsList = new ArrayList();
    }
    
    
    
    private void makeWorkingSpace(double leftLimit, double rightLimit){
        double arrowIndent = 15;
        double yAxisHeight = Math.pow(2, rightLimit) + 1;
        double canvasHeight = yAxisHeight * coordinateYIndent + border
                            + arrowIndent + inaccuracy;
        graphicCanvas.setHeight(canvasHeight);
        
        if(Math.abs(leftLimit) > rightLimit){
            double coordinatesLeftLimit = leftLimit * coordinateXIndent;
            double canvasWidth = 2 * (Math.abs(coordinatesLeftLimit) + border)
                               + arrowIndent + inaccuracy;
            graphicCanvas.setWidth(canvasWidth);
        }
        else{
            double coordinatesRightLimit = rightLimit * coordinateXIndent;
            double canvasWidth = 2 * (coordinatesRightLimit + border)
                               + arrowIndent + inaccuracy;
            graphicCanvas.setWidth(canvasWidth);
        }
        
        Group graphicGroup = new Group(graphicCanvas);
        
        HBox graphicHBox = new HBox(graphicGroup);
        graphicHBox.setAlignment(Pos.CENTER);
        
        scroll.setContent(graphicHBox);
        scroll.setPrefHeight(460); 
        scroll.setPrefWidth(775);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        scroll.setPannable(true);
        
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(() -> {
                    drawXAxis();
                    drawYAxis();
                });
                scheduler.shutdown();
            }
        }, 0, 1, TimeUnit.NANOSECONDS);
    }
    
    private boolean checkInput(TextField aTextField,
            TextField xLowerLimitTextField,
            TextField xUpperLimitTextField) {

        String aString = aTextField.getText();
        String xLowerLimitString = xLowerLimitTextField.getText();
        String xUpperLimitString = xUpperLimitTextField.getText();

        if (aString.isEmpty()
                || xLowerLimitString.isEmpty()
                || xUpperLimitString.isEmpty()) {
            return false;
        }

        Pattern digitPatern = Pattern.compile("^[0-9]+");

        Matcher matcherA = digitPatern.matcher(aString);
        Matcher matcherXLowerLimit = digitPatern.matcher(xLowerLimitString);
        Matcher matcherXUpperLimit = digitPatern.matcher(xUpperLimitString);

        return matcherA.matches() && matcherXLowerLimit.matches()
                && matcherXUpperLimit.matches();
    }

    private void setOnGraphicScrolling(Label scaleLabel) {
        scroll.setOnKeyPressed(ctrlKey -> {
            KeyCode keyCode = ctrlKey.getCode();
            if (keyCode.equals(KeyCode.CONTROL)) {
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
            if (keyCode.equals(KeyCode.CONTROL)) {
                graphicCanvas.setOnScroll(null);
            }
        });
    }
    
    private void drawXAxis(){
        double arrowXIndent = 15;
        double arrowYIndent = 5;
        
        double coordinatesLeftEndX = border;
        double coordinatesRightEndX = graphicCanvas.getWidth() - border;
        double XAxisWidth = coordinatesRightEndX - coordinatesLeftEndX
                          - arrowXIndent - inaccuracy;
        coordinatesOriginX = (XAxisWidth / 2) + border;
        coordinatesOriginY = graphicCanvas.getHeight() - border;
     
        GraphicsContext graphicsContext = graphicCanvas.getGraphicsContext2D();
        
        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.setLineWidth(1);
        
        graphicsContext.strokeLine(
                coordinatesLeftEndX,
                coordinatesOriginY,
                coordinatesRightEndX,
                coordinatesOriginY
        );
        
        double coordinateArrowXAxisX = coordinatesRightEndX - arrowXIndent;
        double coordinateArrowXAxisTopY = coordinatesOriginY - arrowYIndent;
        graphicsContext.strokeLine(
                coordinatesRightEndX,
                coordinatesOriginY,
                coordinateArrowXAxisX,
                coordinateArrowXAxisTopY
        );
        
        double coordinateArrowXAxisBottomY = coordinatesOriginY + arrowYIndent;
        graphicsContext.strokeLine(
                coordinatesRightEndX,
                coordinatesOriginY,
                coordinateArrowXAxisX,
                coordinateArrowXAxisBottomY
        );
        
        double lastXCoordinate = coordinatesRightEndX - arrowXIndent - inaccuracy;
        double stepXCoordinate = coordinateXIndent * 0.1;
        double secondRightXCoordinate = coordinatesOriginX + stepXCoordinate;
        
        for(double xCoordinate = secondRightXCoordinate;
                xCoordinate <= lastXCoordinate;
                xCoordinate += stepXCoordinate){
            graphicsContext.setStroke(Color.BLUE);
            
            double coordinateDivisionXAxisTopY = coordinateArrowXAxisTopY;
            double coordinateDivisionXAxisBottomY = coordinateArrowXAxisBottomY;
            
            graphicsContext.strokeLine(
                xCoordinate,
                coordinateDivisionXAxisTopY,
                xCoordinate,
                coordinateDivisionXAxisBottomY
            );
            
            double deltaXCoordinate = xCoordinate - coordinatesOriginX;
            double negativeXCoordinate = coordinatesOriginX - deltaXCoordinate;
            
            graphicsContext.strokeLine(
                negativeXCoordinate,
                coordinateDivisionXAxisTopY,
                negativeXCoordinate,
                coordinateDivisionXAxisBottomY
            );
            
            graphicsContext.setStroke(Color.GREEN);
        
            double axisIndent = 15;
            double divisionTextXCoordinate = xCoordinate - axisIndent;
            double divisionTextYCoordinate = coordinatesOriginY + axisIndent;
            double currentCoordinate = (xCoordinate - coordinatesOriginX) 
                                     / coordinateXIndent;
            String coordinateText = String.valueOf(currentCoordinate);
            graphicsContext.strokeText(
                    coordinateText,
                    divisionTextXCoordinate,
                    divisionTextYCoordinate
            );
            
            double divisionTextNegativeXCoordinate = negativeXCoordinate - axisIndent;
            double divisionTextNegativeYCoordinate = coordinatesOriginY + axisIndent;
            double currentNegativeCoordinate = -1 * (xCoordinate - coordinatesOriginX) 
                                             / coordinateXIndent;
            String negativeCoordinateText = String.valueOf(currentNegativeCoordinate);
            graphicsContext.strokeText(
                    negativeCoordinateText,
                    divisionTextNegativeXCoordinate,
                    divisionTextNegativeYCoordinate
            );
        }
        
        graphicsContext.setStroke(Color.GREEN);
        
        double axisIndent = 15;
        double divisionTextXCoordinate = coordinatesOriginX - axisIndent;
        double divisionTextYCoordinate = coordinatesOriginY + axisIndent;
        graphicsContext.strokeText(
                "0",
                divisionTextXCoordinate,
                divisionTextYCoordinate
        );
        
        axisIndent = 10;
        double axisTextXCoordinate = coordinatesRightEndX + axisIndent;
        double axisTextYCoordinate = coordinatesOriginY + axisIndent;
        graphicsContext.strokeText(
                "x",
                axisTextXCoordinate,
                axisTextYCoordinate
        );
    }
    
    private void setCenterScrollPane(){
        double scrollHvalue = scroll.getHmax() / 2;
        double scrollVvalue = scroll.getVmax();
        scroll.setHvalue(scrollHvalue);
        scroll.setVvalue(scrollVvalue);
    }
    
    private void drawYAxis(){
        double arrowYIndent = 15;
        double arrowXIndent = 5;
        
        double coordinatesUpperEndY = border;
        
        setCenterScrollPane();
        
        GraphicsContext graphicsContext = graphicCanvas.getGraphicsContext2D();
        
        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.setLineWidth(1);
        
        graphicsContext.strokeLine(
                coordinatesOriginX,
                coordinatesUpperEndY,
                coordinatesOriginX,
                coordinatesOriginY
        );
        
        double coordinateArrowYAxisY = coordinatesUpperEndY + arrowYIndent;
        double coordinateArrowYAxisLeftX = coordinatesOriginX - arrowXIndent;
        graphicsContext.strokeLine(
                coordinatesOriginX,
                coordinatesUpperEndY,
                coordinateArrowYAxisLeftX,
                coordinateArrowYAxisY
        );
        
        double coordinateArrowYAxisRightX = coordinatesOriginX + arrowXIndent;
        graphicsContext.strokeLine(
                coordinatesOriginX,
                coordinatesUpperEndY,
                coordinateArrowYAxisRightX,
                coordinateArrowYAxisY
        );
        
        double lastYCoordinate = coordinatesUpperEndY + arrowXIndent + inaccuracy;
        double stepYCoordinate = coordinateYIndent * 0.1;
        double secondUpperYCoordinate = coordinatesOriginY - stepYCoordinate;
        
        for(double yCoordinate = secondUpperYCoordinate;
                yCoordinate >= lastYCoordinate;
                yCoordinate -= stepYCoordinate){
            graphicsContext.setStroke(Color.BLUE);
            
            double coordinateDivisionYAxisRightX = coordinateArrowYAxisRightX;
            double coordinateDivisionYAxisLeftX = coordinateArrowYAxisLeftX;
            
            graphicsContext.strokeLine(
                coordinateDivisionYAxisRightX,
                yCoordinate,
                coordinateDivisionYAxisLeftX,
                yCoordinate
            );
            
            graphicsContext.setStroke(Color.GREEN);
        
            double axisIndent = 8;
            double divisionTextYCoordinate = yCoordinate + axisIndent;
            double divisionTextXCoordinate = coordinatesOriginX + axisIndent;
            double currentCoordinate = (coordinatesOriginY - yCoordinate) 
                                     / coordinateYIndent;
            String coordinateText = String.valueOf(currentCoordinate);
            graphicsContext.strokeText(
                    coordinateText,
                    divisionTextXCoordinate,
                    divisionTextYCoordinate
            );
        }
        
        double axisIndent = 5;
        double axisTextYCoordinate = coordinatesUpperEndY - axisIndent;
        double axisTextXCoordinate = coordinatesOriginX + axisIndent;
        graphicsContext.strokeText(
                "y",
                axisTextXCoordinate,
                axisTextYCoordinate
        );
    }
    
    public void repaint(double currentX, double currentY){
        GraphicsContext graphicsContext = graphicCanvas.getGraphicsContext2D();
        
        graphicsContext.setFill(Color.ORANGE);
        
        double currentCoordinateX = currentX * coordinateXIndent;
        double currentCoordinateY = currentY * coordinateYIndent;
        double pointWidth = 10;
        double pointHeight = 10;
        double currentPointX = coordinatesOriginX - (pointWidth / 2)
                      + currentCoordinateX;
        double currentPointY = coordinatesOriginY - (pointHeight / 2)
                      - currentCoordinateY;
        graphicsContext.fillOval(
                currentPointX,
                currentPointY,
                pointWidth,
                pointHeight
        );
        
        Point currentPoint = new Point(
                currentPointX, currentPointY
        );
        pointsList.add(currentPoint);
        
        int listSize = pointsList.size();
        
        if(listSize == 1)
            return;
        
        Point prevPoint = pointsList.get(listSize - 2);
        double prevPointX = prevPoint.getX();
        double prevPointY = prevPoint.getY();
        
        double prevPointCenterX = prevPointX + (pointWidth / 2);
        double prevPointCenterY = prevPointY + (pointHeight / 2);
        double currentPointCenterX = currentPointX + (pointWidth / 2);
        double currentPointCenterY = currentPointY + (pointHeight / 2);
        
        graphicsContext.setStroke(Color.ORANGE);
        
        graphicsContext.strokeLine(
                prevPointCenterX,
                prevPointCenterY,
                currentPointCenterX,
                currentPointCenterY
        );
    }
}
