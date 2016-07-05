package com.poorgroupproject.thrumaniamapeditor.form.element;

import com.poorgroupproject.thrumaniamapeditor.form.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by ahmad on 7/4/16.
 */
public class NaviPanel extends Canvas {
    private Image[][] loadedImage;
    private Image[] shownImage;
    private boolean[] status;
    private Rectangle[] boundry;
    private Main main;

    public NaviPanel(int x, int y,int width, int height, Main main){
        setLocation(x,y);
        setSize(width,height);
        loadedImage = new Image[14][2];
        shownImage = new Image[14];
        boundry = new Rectangle[14];
        status = new boolean[14];
        loadImage();
        setBoundry();
        setListener();
        addRepaint();
        this.main = main;
        setBackground(Color.LIGHT_GRAY);
    }

    private void addRepaint() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    setImages();
                    repaint();
                    try {
                        Thread.sleep(33);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        })).start();
    }

    public void loadImage(){
        try {
            loadedImage[0][0] = ImageIO.read(new File("resource/image/me_panel/left.png"));
            loadedImage[0][1] = ImageIO.read(new File("resource/image/me_panel/left2.png"));
            loadedImage[1][0] = ImageIO.read(new File("resource/image/me_panel/up.png"));
            loadedImage[1][1] = ImageIO.read(new File("resource/image/me_panel/up2.png"));
            loadedImage[2][0] = ImageIO.read(new File("resource/image/me_panel/down.png"));
            loadedImage[2][1] = ImageIO.read(new File("resource/image/me_panel/down2.png"));
            loadedImage[3][0] = ImageIO.read(new File("resource/image/me_panel/right.png"));
            loadedImage[3][1] = ImageIO.read(new File("resource/image/me_panel/right2.png"));
            loadedImage[4][0] = ImageIO.read(new File("resource/image/me_panel/zoomin.png"));
            loadedImage[4][1] = ImageIO.read(new File("resource/image/me_panel/zoomin2.png"));
            loadedImage[5][0] = ImageIO.read(new File("resource/image/me_panel/zoomout.png"));
            loadedImage[5][1] = ImageIO.read(new File("resource/image/me_panel/zoomout2.png"));
            loadedImage[6][0] = ImageIO.read(new File("resource/image/me_panel/add_column.png"));
            loadedImage[6][1] = ImageIO.read(new File("resource/image/me_panel/add_column2.png"));
            loadedImage[7][0] = ImageIO.read(new File("resource/image/me_panel/add_row.png"));
            loadedImage[7][1] = ImageIO.read(new File("resource/image/me_panel/add_row2.png"));
            loadedImage[8][0] = ImageIO.read(new File("resource/image/me_panel/remove_column.png"));
            loadedImage[8][1] = ImageIO.read(new File("resource/image/me_panel/remove_column2.png"));
            loadedImage[9][0] = ImageIO.read(new File("resource/image/me_panel/remove_row.png"));
            loadedImage[9][1] = ImageIO.read(new File("resource/image/me_panel/remove_row2.png"));
            loadedImage[10][0] = ImageIO.read(new File("resource/image/me_panel/undo.png"));
            loadedImage[10][1] = ImageIO.read(new File("resource/image/me_panel/undo2.png"));
            loadedImage[11][0] = ImageIO.read(new File("resource/image/me_panel/redo.png"));
            loadedImage[11][1] = ImageIO.read(new File("resource/image/me_panel/redo2.png"));
            loadedImage[12][0] = ImageIO.read(new File("resource/image/me_panel/save.png"));
            loadedImage[12][1] = ImageIO.read(new File("resource/image/me_panel/save2.png"));
            loadedImage[13][0] = ImageIO.read(new File("resource/image/me_panel/load.png"));
            loadedImage[13][1] = ImageIO.read(new File("resource/image/me_panel/load2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBoundry(){
        boundry[0] = new Rectangle(new Point(20,60), new Dimension(40,40));
        boundry[1] = new Rectangle(new Point(60,20), new Dimension(40,40));
        boundry[2] = new Rectangle(new Point(60,100), new Dimension(40,40));
        boundry[3] = new Rectangle(new Point(100,60), new Dimension(40,40));
        boundry[4] = new Rectangle(new Point(250,20), new Dimension(70,70));
        boundry[5] = new Rectangle(new Point(250,100), new Dimension(70,70));
        boundry[6] = new Rectangle(new Point(350,30), new Dimension(40,40));
        boundry[7] = new Rectangle(new Point(400,30), new Dimension(40,40));
        boundry[8] = new Rectangle(new Point(350,80), new Dimension(40,40));
        boundry[9] = new Rectangle(new Point(400,80), new Dimension(40,40));
        boundry[10] = new Rectangle(new Point(480,60), new Dimension(70,70));
        boundry[11] = new Rectangle(new Point(580,60), new Dimension(70,70));
        boundry[12] = new Rectangle(new Point(700,60), new Dimension(70,70));
        boundry[13] = new Rectangle(new Point(790,60), new Dimension(70,70));
    }

    private void setImages(){
        for (int i = 0; i < 14; i++) {
            if (!status[i])
                shownImage[i] = loadedImage[i][1];
            else
                shownImage[i] = loadedImage[i][0];
        }
    }
    private void setListener(){
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {

            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Rectangle r = new Rectangle(mouseEvent.getPoint(),new Dimension(1,1));
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                Rectangle r = new Rectangle(mouseEvent.getPoint(),new Dimension(1,1));
                for (int i = 0; i < 14; i++) {
                    if (r.intersects(boundry[i])) {
                        status[i] = true;
                        break;
                    }
                }
                if (r.intersects(boundry[0])){
                    main.moveViewportLeft();
                }else if (r.intersects(boundry[1])){
                    main.moveViewportUp();
                } else if (r.intersects(boundry[2])){
                    main.moveViewportDown();
                } else if (r.intersects(boundry[3])){
                    main.moveViewportRight();
                } else if (r.intersects(boundry[4])){
                    main.zoomIn();
                } else if (r.intersects(boundry[5])){
                    main.zoomOut();
                } else if (r.intersects(boundry[6])){
                    main.addCol();
                } else if (r.intersects(boundry[7])){
                    main.addRow();
                } else if (r.intersects(boundry[8])){
                    main.removeCol();
                } else if (r.intersects(boundry[9])){
                    main.removeRow();
                } else if (r.intersects(boundry[10])){
                    main.undo();
                } else if (r.intersects(boundry[11])){
                    main.redo();
                } else if (r.intersects(boundry[12])){
                    main.saveMap();
                } else if (r.intersects(boundry[13])){
                    main.openMap();
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                for (int i = 0; i < 14; i++) {
                    status[i] = false;
                }
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                main.setIsViewPortMoving(false);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
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
    }

    @Override
    public void paint(Graphics graphics) {
        update(graphics);
    }

    @Override
    public void update(Graphics graphics) {
        BufferedImage offscreen = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics2D offscreenGraphics = offscreen.createGraphics();
        for (int i = 0; i < 14; i++) {
            offscreenGraphics.drawImage(shownImage[i], ((int) boundry[i].getX()),((int) boundry[i].getY()),((int) boundry[i].getWidth()),((int) boundry[i].getHeight()),null);
        }

        graphics.drawImage(offscreen,0,0,null);
    }
}
