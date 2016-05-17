package com.poorgroupproject.thrumaniamapeditor.form;

import com.poorgroupproject.thrumaniamapeditor.form.element.MapPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
    private int mapViewportX;
    private int mapViewportY;
    private final int CELL_WIDTH = 50;
    private final int CELL_HEIGHT = 50;
    private final int MAP_DEFAULT_ROW = 500;
    private final int MAP_DEFAULT_COL = 400;
    private double zoomScale;


    private enum Cell{
       WATER, LAND
    } ;

    private Cell [][] mapMatrix;
    private BufferedImage map;

    private Image []itemImages;

    public Main(){
        super();
        initMap();
        loadImages();
        putElements();
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()){
                    case KeyEvent.VK_LEFT:
                        moveViewportLeft();
                        break;
                    case KeyEvent.VK_UP:
                        moveViewportUp();
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveViewportRight();
                        break;
                    case KeyEvent.VK_DOWN:
                        moveViewportDown();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
                switch (mouseWheelEvent.getWheelRotation()){
                    case 1:
                        System.out.println("zoom1");
                        break;
                    case -1:
                        System.out.println("zoom2");
                        break;
                }
            }
        });
    }

    private void zoomIn(){
        zoomScale += 0.25;
    }

    private void zoomOut(){
        zoomScale -= 0.25;
    }

    private void moveViewportLeft(){
        mapViewportX += 10;
        repaint();
    }

    private void moveViewportRight(){
        mapViewportX -= 10;
        repaint();
    }

    private void moveViewportUp(){
        mapViewportY -= 10;
        repaint();
    }

    private void moveViewportDown(){
        mapViewportY += 10;
        repaint();
    }

    private void loadImages(){
        itemImages = new Image[5];
        try {
            itemImages[0] = ImageIO.read(new File("resource/image/simerion/characters/0009.gif"));
            itemImages[1] = ImageIO.read(new File("resource/image/simerion/characters/0010.gif"));
            itemImages[2] = ImageIO.read(new File("resource/image/simerion/characters/0011.gif"));
            itemImages[3] = ImageIO.read(new File("resource/image/simerion/characters/0012.gif"));
            itemImages[4] = ImageIO.read(new File("resource/image/simerion/characters/0013.gif"));
            for (int i = 0; i < itemImages.length; i++) {
                itemImages[i] = itemImages[i].getScaledInstance(CELL_WIDTH,CELL_HEIGHT,Image.SCALE_DEFAULT);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initMap(){
        zoomScale = 1;
        mapViewportX = 0;
        mapViewportY = 0;
        rows = MAP_DEFAULT_ROW;
        cols = MAP_DEFAULT_COL;

        mapMatrix = new Cell[rows][cols];

        for (int i = 0; i < mapMatrix.length; i++) {
            for (int j = 0; j < mapMatrix[0].length; j++) {
                mapMatrix[i][j] = Cell.WATER;
            }
        }

        mapMatrix[3][2] = Cell.LAND;

    }

    private void makeMap(){
        map = new BufferedImage(rows * CELL_WIDTH, cols * CELL_HEIGHT ,BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = map.createGraphics();
        for (int i = 0; i < mapMatrix.length; i++) {
            for (int j = 0; j < mapMatrix[0].length; j++) {
                Image image = null;
                switch (mapMatrix[i][j]){
                    case LAND:
                        image = itemImages[0];
                        break;
                    case WATER:
                        image = itemImages[1];
                        break;
                }

                graphics.drawImage(image, i * CELL_WIDTH, j * CELL_HEIGHT,null);
            }
        }
        repaint();
    }
    @Override
    public void putElements() {
        makeMap();
    }

    @Override
    public void paint(Graphics graphics) {
        int width = ((int) (cols * CELL_WIDTH * zoomScale));
        int height = ((int) (rows * CELL_HEIGHT * zoomScale));
        int x = -1 * mapViewportX;
        int y = -1 * mapViewportY;
        graphics.drawImage(map,x,y,null);
    }


}
