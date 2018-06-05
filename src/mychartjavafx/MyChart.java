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
    private final double inaccuracy = 15;
    private double coordinatesOriginX;
    private double coordinatesOriginY;
    private final List<Point> pointsList;
    
    public final Canvas getGraphicCanvas(){
        return this.graphicCanvas;
    }
    
    public final ScrollPane getScroll(){
        return this.scroll;
    }
    
    public MyChart(double height, double width){
        super.setHeight(height);
        super.setWidth(width);
        pointsList = new ArrayList();
    }
    
    public final void createWorkingSpace(double leftLimit, double rightLimit){
        graphicCanvas = new Canvas();
        
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
        
        scroll = new ScrollPane(graphicHBox);
        scroll.setPrefHeight(this.getHeight());
        scroll.setPrefWidth(this.getWidth());
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        scroll.setPannable(true);
        
        this.getChildren().add(scroll);
        
        drawXAxis();       
        drawYAxis();
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
