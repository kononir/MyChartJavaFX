/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mychartjavafx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 *
 * @author Vlad
 */
public class MyChart extends Region {
    
    
    private double currentX;
    private double currentY;
    private final double coordinatesOriginX;
    private final double coordinatesEndX;
    private double coordinatesOriginY;
    private final double coordinatesEndY = 0;
    private final double borderIndent = 20;
    private final double coordinateIndent = 300;
    private final double arrowXIndent = 15;
    private final double arrowYIndent = 5;
    private final double inaccuracy = 5;
    
    public MyChart(double xUpperLimit){
        this.coordinatesOriginX = borderIndent;
        this.coordinatesEndX = (xUpperLimit * coordinateIndent)
                             + borderIndent + arrowXIndent + inaccuracy;
        this.coordinatesOriginY = arrowYIndent + inaccuracy;
        
        createWidgetWithXAxis();
    }
    
    public void repaint(){
        
    }
    
    private void createWidgetWithXAxis(){
        this.resize(coordinatesEndX, 465);
        
        double canvasHeight = borderIndent + arrowYIndent + inaccuracy;
        Canvas graphicCanvas = new Canvas(coordinatesEndX, canvasHeight);
        this.getChildren().add(graphicCanvas);
        graphicCanvas.relocate(0, this.getHeight());
        
        GraphicsContext graphicsContext = graphicCanvas.getGraphicsContext2D();
        drawXAxis(graphicsContext);
    }
    
    private void drawXAxis(GraphicsContext graphicsContext){
        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.setLineWidth(1);
        
        graphicsContext.strokeLine(
                coordinatesOriginX,
                coordinatesOriginY,
                coordinatesEndX,
                coordinatesOriginY
        );
        
        double coordinateArrowXAxisX = coordinatesEndX - arrowXIndent;
        double coordinateArrowXAxisTopY = coordinatesOriginY - arrowYIndent;
        graphicsContext.strokeLine(
                coordinatesEndX,
                coordinatesOriginY,
                coordinateArrowXAxisX,
                coordinateArrowXAxisTopY
        );
        
        double coordinateArrowXAxisBottomY = coordinatesOriginY + arrowYIndent;
        graphicsContext.strokeLine(
                coordinatesEndX,
                coordinatesOriginY,
                coordinateArrowXAxisX,
                coordinateArrowXAxisBottomY
        );
        
        double lastXCoordinate = coordinatesEndX - arrowXIndent - inaccuracy;
        double stepXCoordinate = coordinateIndent * 0.1;
        double secondXCoordinate = coordinatesOriginX + stepXCoordinate;
        for(double xCoordinate = secondXCoordinate;
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
            
            graphicsContext.setStroke(Color.GREEN);
        
            double axisIndent = 15;
            double divisionTextXCoordinate = xCoordinate - axisIndent;
            double divisionTextYCoordinate = coordinatesOriginY + axisIndent;
            String coordinateText = String.valueOf(
                    (xCoordinate - borderIndent) / coordinateIndent
            );
            graphicsContext.strokeText(
                    coordinateText,
                    divisionTextXCoordinate,
                    divisionTextYCoordinate
            );
        }
        
        graphicsContext.setStroke(Color.GREEN);
        
        double xAxisIndent = 3;
        double axisTextXCoordinate = coordinatesEndX - arrowXIndent - 7.5;
        double axisTextYCoordinate = coordinatesOriginY - xAxisIndent;
        graphicsContext.strokeText(
                "x",
                axisTextXCoordinate,
                axisTextYCoordinate
        );
    }
}
