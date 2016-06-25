package com.poorgroupproject.thrumaniamapeditor.form.element;

import com.poorgroupproject.thrumaniamapeditor.Path;
import com.poorgroupproject.thrumaniamapeditor.form.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 */
public class ItemPanel extends Canvas {

    private Image[][] loadedImage;
    private Image [] shownItem;
    private byte[] itemStatus;
    private int selectedId;
    Main main;

    private Dimension itemDimension;

    @Override
    public void paint(Graphics graphics) {
        update(graphics);
    }

    public void update(Graphics graphics){
        BufferedImage offscreen;
        offscreen = ((BufferedImage) createImage(getWidth(), getHeight()));
        Graphics2D offscreenGraphics = offscreen.createGraphics();
        for (int i = 0; i < 8; i++) {
            offscreenGraphics.drawImage(shownItem[i], ((int) (getSize().getWidth() - itemDimension.getWidth())) / 2, i * ((int) itemDimension.getHeight()), ((int) itemDimension.getWidth()), ((int) itemDimension.getHeight()), null);
        }
        graphics.drawImage(offscreen,0,0,null);
    }

    public void loadImage() {
        try {
            for (int i = 0; i < 3; i++) {
                loadedImage[0][i] = ImageIO.read(new File(Path.panelImagePath + "agri" + i + ".png"));
            }
            for (int i = 0; i < 3; i++) {
                loadedImage[1][i] = ImageIO.read(new File(Path.panelImagePath + "fish" + i + ".png"));
            }
            for (int i = 0; i < 3; i++) {
                loadedImage[2][i] = ImageIO.read(new File(Path.panelImagePath + "gold" + i + ".png"));
            }
            for (int i = 0; i < 3; i++) {
                loadedImage[3][i] = ImageIO.read(new File(Path.panelImagePath + "iron" + i + ".png"));
            }
            for (int i = 0; i < 3; i++) {
                loadedImage[4][i] = ImageIO.read(new File(Path.panelImagePath + "land" + i + ".png"));
            }
            for (int i = 0; i < 3; i++) {
                loadedImage[5][i] = ImageIO.read(new File(Path.panelImagePath + "mountain" + i + ".png"));
            }
            for (int i = 0; i < 3; i++) {
                loadedImage[6][i] = ImageIO.read(new File(Path.panelImagePath + "sea" + i + ".png"));
            }
            for (int i = 0; i < 3; i++) {
                loadedImage[7][i] = ImageIO.read(new File(Path.panelImagePath + "tree" + i + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ItemPanel(Main main,Point location, Dimension size) {
        selectedId = -1;
        this.main = main;
        setSize(size);
        setLocation(location);
        setBackground(Color.GRAY);
        loadedImage = new Image[8][3];
        shownItem = new Image[8];
        itemStatus = new byte[8];
        itemDimension = new Dimension((int) (size.getHeight() / 8), (int) (size.getHeight() / 8));
        loadImage();
        for (int i = 0; i < 8; i++) {
            itemStatus[i] = 0;
        }
        setItemImages();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        },10,20);
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                main.handleKeyboardInput(keyEvent);
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                makeItemDefault();
                itemStatus[(int)(mouseEvent.getY() / itemDimension.getHeight())] = 2;
                setItemImages();
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                makeItemDefault();
                setItemImages();
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                makeItemDefault();
                if (itemStatus[(int)(mouseEvent.getY() / itemDimension.getHeight())] != 2)
                    itemStatus[(int)(mouseEvent.getY() / itemDimension.getHeight())] = 1;
                setItemImages();
            }
        });
    }

    private void makeItemDefault(){
        for (int i = 0; i < 8; i++) {
            if (itemStatus[i] == 1)
                itemStatus[i] = 0;
        }
    }
    private void setItemImages(){
        for (int i = 0; i < 8; i++) {
            shownItem[i] = loadedImage[i][itemStatus[i]];
        }
    }
}
