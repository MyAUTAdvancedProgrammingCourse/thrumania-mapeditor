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
                switch (keyEvent.getKeyCode()){
                    case KeyEvent.VK_0:
                        selectItem(4);
                        break;
                    case KeyEvent.VK_1:
                        selectItem(5);
                        break;
                    case KeyEvent.VK_2:
                        selectItem(6);
                        break;
                    case KeyEvent.VK_3:
                        selectItem(7);
                        break;
                    case KeyEvent.VK_4:
                        selectItem(0);
                        break;
                    case KeyEvent.VK_5:
                        selectItem(2);
                        break;
                    case KeyEvent.VK_6:
                        selectItem(3);
                        break;
                    case KeyEvent.VK_EQUALS:
                        selectItem(1);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                makeItemDefault();
                int index = (int)(mouseEvent.getY() / itemDimension.getHeight());
                selectItem(index);
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

    private void selectItem(int index){
        if (selectedId == index){       //      User wants to deselect the selected item
            selectedId = -1;
            itemStatus[index] = 0;
        }else {
            if (selectedId != -1)
                itemStatus[selectedId] = 0;
            selectedId = index;
            itemStatus[index] = 2;
        }
        setItemImages();
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
