package com.poorgroupproject.thrumaniamapeditor.form;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ahmad on 5/16/16.
 */
public class Frame extends JFrame {
    private Dimension screenDimension;
    private Dimension mousePointerDimension;

    private GraphicsDevice device;
    public Frame(){
        setUpGraphicFullscreen();
        setLayout(null);
        setTitle("Thrumania Map Editor");
        setMouseCursor();
    }

    private void setUpGraphicFullscreen(){
        screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenDimension);
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        setUndecorated(true);
        device.setFullScreenWindow(this);
    }
    private void setMouseCursor(){
        mousePointerDimension = new Dimension(60,60);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("resource/image/cursor/default.png");
        image = image.getScaledInstance((int)mousePointerDimension.getWidth(),(int)mousePointerDimension.getHeight(),Image.SCALE_DEFAULT);
        /**
         * ***********IMPORTANT***********
         * Change the (20,0) point if the mouse cursor change. This is the anchor point.
         */
        Cursor c = toolkit.createCustomCursor(image , new Point(20,
                0), "img");
        this.setCursor (c);
    }

    public void showFrame(){
        setVisible(true);
    }
}
