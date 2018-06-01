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
    private Canvas graphicCanvas;
    private final double coordinateIndent = 300;
    private final double arrowXIndent = 15;
    private final double arrowYIndent = 5;
    
    public MyChart(double xUpperLimit, double height, double width){
        super.setPrefSize(width, height);
        createCanvas();
    }
    
    public void repaint(double currentX, double currentY){
        double currentCanvasX = currentX * coordinateIndent;
        double currentCanvasY = currentY * coordinateIndent;
                
        GraphicsContext graphicsContext = graphicCanvas.getGraphicsContext2D();
        /*if(currentCanvasY > prevCanvasY){
            
        }*/
    }
    
    private void createCanvas(){
        ScrollPane scroll = new ScrollPane();
        scroll.setMinSize(this.getWidth(), this.getHeight());
        scroll.setMaxSize(this.getWidth(), this.getHeight());
        
        graphicCanvas = new Canvas(this.getWidth(), this.getHeight());
        this.getChildren().add(scroll);
        scroll.setContent(graphicCanvas);
    }
    
    public final void drawXAxis(double leftLimit, double rightLimit){
        double coordinatesLeftLimit = (leftLimit * coordinateIndent);
        
        double inaccuracy = 5;
        double coordinatesRightLimit = (rightLimit * coordinateIndent)
                                     + arrowXIndent + inaccuracy;
        
        double coordinatesOriginX = this.getWidth() / 2;
        double coordinatesOriginY = this.getHeight() / 2;
        
        double coordinatesLeftEndX;
        if(coordinatesLeftLimit > 0){
            coordinatesLeftEndX = coordinatesOriginX - coordinatesLeftLimit;
        }
        else{
            coordinatesLeftEndX = coordinatesOriginX + coordinatesLeftLimit;            
        }
        
        double coordinatesRightEndX;
        if(coordinatesRightLimit > 0){
            coordinatesRightEndX = coordinatesOriginX + coordinatesRightLimit;
        }
        else{
            coordinatesRightEndX = coordinatesOriginX - coordinatesRightLimit; 
        }
        
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
                    (xCoordinate - stepXCoordinate) / coordinateIndent
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
        double axisTextXCoordinate = coordinatesRightEndX - arrowXIndent - 7.5;
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
        
        
    }
}
