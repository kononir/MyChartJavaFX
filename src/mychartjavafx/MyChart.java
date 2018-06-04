/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mychartjavafx;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 *
 * @author Vlad
 */
    public class MyChart extends Region {
    private Canvas graphicCanvas;
    private ScrollPane scroll;
    private final double coordinateXIndent = 300;
    private final double coordinateYIndent = 200;
    private final double border = 50;
    private double coordinatesOriginX;
    private double coordinatesOriginY;
    private List<CoordinateClass> coordinatesList;
    
    public final Canvas getGraphicCanvas(){
        return this.graphicCanvas;
    }
    
    public final ScrollPane getScroll(){
        return this.scroll;
    }
    
    public MyChart(double height, double width){
        super.setHeight(height);
        super.setWidth(width);
    }
    
    public final void createWorkingSpace(double leftLimit, double rightLimit){
        double leftLimitLength = Math.abs(Math.pow(2, leftLimit)) + 1;
        double rightLimitLength = Math.pow(2, rightLimit) + 1;
        
        graphicCanvas = new Canvas();
        
        if(leftLimitLength > rightLimitLength)
            graphicCanvas.setHeight(2 * leftLimitLength * coordinateYIndent);
        else
            graphicCanvas.setHeight(2 * rightLimitLength * coordinateYIndent);
            
        graphicCanvas.setWidth(this.getWidth() - 10);
        
        Group graphicGroup = new Group(graphicCanvas);
        
        HBox graphicHBox = new HBox(graphicGroup);
        graphicHBox.setAlignment(Pos.CENTER);
        
        scroll = new ScrollPane(graphicHBox);
        scroll.setPrefHeight(this.getHeight());
        scroll.setPrefWidth(this.getWidth());
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        scroll.setPannable(true);
        
        this.getChildren().add(scroll);
        
        drawBothAxis(leftLimit, rightLimit);
    }
    
    private void drawBothAxis(double leftLimit, double rightLimit){
        double inaccuracy = 5;
        double arrowXIndent = 15;
        
        double YAxisWidth = graphicCanvas.getHeight() - (2 * border)
                          - inaccuracy - arrowXIndent;
        double YAxisWidthHalf = YAxisWidth / 2;
        final double verticalLimit = YAxisWidthHalf / coordinateYIndent;
        final double nullX = 0.0001;
        
        coordinatesList = new ArrayList();
        
        CoordinateClass nullCoordinate = new CoordinateClass(nullX, verticalLimit);
        coordinatesList.add(nullCoordinate);
        
        drawXAxis(leftLimit, rightLimit);       
        drawYAxis(verticalLimit);
    }
    
    private void drawXAxis(double leftLimit, double rightLimit){
        double inaccuracy = 5;
        double arrowXIndent = 15;
        double arrowYIndent = 5;
               
        if(Math.abs(leftLimit) > rightLimit){
            double coordinatesLeftLimit = leftLimit * coordinateXIndent;
            double canvasWidth = 2 * Math.abs(coordinatesLeftLimit + border)
                               + arrowXIndent + inaccuracy;
            graphicCanvas.setWidth(canvasWidth);
        }
        else{
            double coordinatesRightLimit = rightLimit * coordinateXIndent;
            double canvasWidth = 2 * (coordinatesRightLimit + border)
                               + arrowXIndent + inaccuracy;
            graphicCanvas.setWidth(canvasWidth);
        }
        
        double coordinatesLeftEndX = border;
        double coordinatesRightEndX = graphicCanvas.getWidth() - border;
        double XAxisWidth = coordinatesRightEndX - coordinatesLeftEndX
                          - arrowXIndent - inaccuracy;
        coordinatesOriginX = (XAxisWidth / 2) + border;
        coordinatesOriginY = graphicCanvas.getHeight() / 2;
     
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
        double scrollVvalue = scroll.getVmax() / 2;
        scroll.setHvalue(scrollHvalue);
        scroll.setVvalue(scrollVvalue);
    }
    
    private void drawYAxis(double verticalLimit){
        double inaccuracy = 5;
        double arrowYIndent = 15;
        double arrowXIndent = 5;
        
        double coordinatesVerticalLimit = verticalLimit * coordinateYIndent;
        
        double newCanvasHeight = 2 * (coordinatesVerticalLimit + border)
                            + arrowXIndent + inaccuracy;
        double oldCanvasHeight = graphicCanvas.getHeight();
        
        if(newCanvasHeight > oldCanvasHeight)
            graphicCanvas.setHeight(newCanvasHeight);
        
        double coordinatesUpperEndY = border;
        double coordinatesLowerEndY = newCanvasHeight - border;
        double YAxisWidth = coordinatesLowerEndY - coordinatesUpperEndY
                          - arrowXIndent - inaccuracy;
        
        double oldCoordinatesOriginY = coordinatesOriginY;
        coordinatesOriginY =(YAxisWidth / 2) + border + arrowXIndent + inaccuracy;
        
        if(oldCoordinatesOriginY <= coordinatesOriginY){
            double translationValue = Math.abs(oldCoordinatesOriginY - coordinatesOriginY);
            graphicCanvas.setTranslateX(translationValue);
        }
        
        setCenterScrollPane();
        
        GraphicsContext graphicsContext = graphicCanvas.getGraphicsContext2D();
        
        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.setLineWidth(1);
        
        graphicsContext.strokeLine(
                coordinatesOriginX,
                coordinatesUpperEndY,
                coordinatesOriginX,
                coordinatesLowerEndY
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
            
            double deltaYCoordinate = coordinatesOriginY - yCoordinate;
            double negativeYCoordinate = coordinatesOriginY + deltaYCoordinate;
            
            graphicsContext.strokeLine(
                coordinateDivisionYAxisRightX,
                negativeYCoordinate,
                coordinateDivisionYAxisLeftX,
                negativeYCoordinate
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
            
            double divisionTextNegativeYCoordinate = negativeYCoordinate + axisIndent;
            double divisionTextNegativeXCoordinate = coordinatesOriginX + axisIndent;
            double currentNegativeCoordinate = -1 * (coordinatesOriginY - yCoordinate) 
                                             / coordinateYIndent;
            String negativeCoordinateText = String.valueOf(currentNegativeCoordinate);
            graphicsContext.strokeText(
                    negativeCoordinateText,
                    divisionTextNegativeXCoordinate,
                    divisionTextNegativeYCoordinate
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
        double currentCoordinateX = currentX * coordinateXIndent;
        double currentCoordinateY = currentY * coordinateYIndent;
        CoordinateClass coordinate = new CoordinateClass(
                currentCoordinateX, currentCoordinateY
        );
        coordinatesList.add(coordinate);
        
        //сместить с помощью setTranslateX (Y) -> сместили в drawYAxis
        //удалить верхушку, начиная с последнего деления до буквы "y"
        //добавить сверху и снизу необходимое количество делений
        //добавить верхушку
        //прорисовать точки
        //прорисовать линию от предыдущей точки
              
        GraphicsContext graphicsContext = graphicCanvas.getGraphicsContext2D();
        
        int listSize = coordinatesList.size();
        CoordinateClass prevCoordinate = coordinatesList.get(listSize - 2);
        double prevCoordinateX = prevCoordinate.getX();
        double prevCoordinateY = prevCoordinate.getY();
    }
}
