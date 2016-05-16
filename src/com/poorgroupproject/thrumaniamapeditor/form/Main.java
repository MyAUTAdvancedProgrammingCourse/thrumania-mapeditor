package com.poorgroupproject.thrumaniamapeditor.form;

import com.poorgroupproject.thrumaniamapeditor.form.element.MapPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PipedReader;
import java.util.Map;

/**
 * @author ahmad
 * @version 1.0.0
 */
public class Main extends Frame {

    private int rows;
    private int cols;
    private int mapX;
    private int mapY;
    private final int CELL_WIDTH = 50;
    private final int CELL_HEIGHT = 50;
    private final int MAP_DEFAULT_ROW = 500;
    private final int MAP_DEFAULT_COL = 400;


    private enum Cell{
       WATER, LAND
    } ;

    private Cell [][] mapMatrix;
    private BufferedImage map;

    private BufferedImage []images;

    public Main(){
        super();
        putElements();
    }

    private void initMap(){
        rows = MAP_DEFAULT_ROW;
        cols = MAP_DEFAULT_COL;

        mapMatrix = new Cell[rows][cols];

        for (int i = 0; i < mapMatrix.length; i++) {
            for (int j = 0; j < mapMatrix[0].length; j++) {
                mapMatrix[i][j] = Cell.WATER;
            }
        }
        map = new BufferedImage(rows * CELL_WIDTH, cols * CELL_HEIGHT ,BufferedImage.TYPE_INT_ARGB);

    }
    @Override
    public void putElements() {

        Graphics graphics = bufferedImage.createGraphics();
        images = new BufferedImage[40];
        try {
            for (int i = 0; i < images.length; i++) {
                images[i] = ImageIO.read(new File("resource/image/simerion/tiles/world/ground/000" + 1 + ".gif"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int j = 0; j < images.length / 10; j++) {
            for (int i = 1; i <= 10; i++) {
                graphics.drawImage(images[i-1], i * (images[i-1].getWidth()/2), j * i * (images[i-1].getHeight()/2 - 20),null);
            }
        }
        System.out.println("this is in");
        repaint();

    }

    @Override
    public void paint(Graphics graphics) {
        System.out.println("this is run");
        if (bufferedImage == null)
            System.out.println("some probliem");
        graphics.drawImage(bufferedImage,20,20,null);
    }


}
