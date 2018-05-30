/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mychartjavafx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 *
 * @author Vlad
 */
    public class MyChart extends Region {
    private final Canvas graphicCanvas;
    private final double coordinatesOriginX;
    private final double coordinatesEndX;
    private double coordinatesOriginY;
    private final double coordinatesEndY = 0;
    private final double borderIndent = 50;
    private final double coordinateIndent = 300;
    private final double arrowXIndent = 15;
    private final double arrowYIndent = 5;
    private final double inaccuracy = 5;
    private double prevCanvasX;
    private double prevCanvasY;
    
    public MyChart(double xUpperLimit, double height, double width){
        this.coordinatesOriginX = borderIndent;
        this.coordinatesEndX = (xUpperLimit * coordinateIndent)
                             + (2 * borderIndent) + arrowXIndent + inaccuracy;
        this.coordinatesOriginY = arrowYIndent + inaccuracy;
        this.prevCanvasX = coordinatesOriginX;
        this.prevCanvasY = arrowYIndent + inaccuracy;
        
        super.setHeight(height);
        super.setWidth(width);
        
        graphicCanvas = createCanvasWithXAxis();
    }
    
    public void repaint(double currentX, double currentY){
        double currentCanvasX = currentX * coordinateIndent + borderIndent;
        double currentCanvasY = currentY * coordinateIndent;
                
        GraphicsContext graphicsContext = graphicCanvas.getGraphicsContext2D();
        if(currentCanvasY > prevCanvasY){
            /*graphicCanvas.setHeight(borderIndent + currentY * coordinateIndent
                                         + arrowYIndent + inaccuracy);*/
            coordinatesOriginY = currentY * coordinateIndent + arrowYIndent + inaccuracy;
            drawYAxis(graphicsContext);
            //graphicCanvas.setTranslateY(graphicCanvas.getHeight() - borderIndent); //смещение?
            //this.getChildren().get(0).
        }
    }
    
    private Canvas createCanvasWithXAxis(){
        ScrollPane scroll = new ScrollPane();
        scroll.setMinSize(this.getWidth(), this.getHeight());
        scroll.setMaxSize(this.getWidth(), this.getHeight());
        
        double canvasHeight = borderIndent + arrowYIndent + inaccuracy;
        Canvas canvas = new Canvas(coordinatesEndX, canvasHeight);
        this.getChildren().add(scroll);
        scroll.setContent(canvas);
        
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        drawXAxis(graphicsContext);
        
        canvas.setTranslateY(this.getHeight() - borderIndent);
        
        return canvas;
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
        
        double axisIndent = 15;
            double divisionTextXCoordinate = coordinatesOriginX - axisIndent;
            double divisionTextYCoordinate = coordinatesOriginY + axisIndent;
            graphicsContext.strokeText(
                    "0",
                    divisionTextXCoordinate,
                    divisionTextYCoordinate
            );
        
        double xAxisIndent = 3;
        double axisTextXCoordinate = coordinatesEndX - arrowXIndent - 7.5;
        double axisTextYCoordinate = coordinatesOriginY - xAxisIndent;
        graphicsContext.strokeText(
                "x",
                axisTextXCoordinate,
                axisTextYCoordinate
        );
    }
    
    private void drawYAxis(GraphicsContext graphicsContext){
        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.setLineWidth(1);
        
        //graphicsContext.get //попробовать получить объекты из контекста
        
        graphicsContext.strokeLine(
                coordinatesOriginX,
                -200,
                coordinatesOriginX,
                10
        );
    }
}
