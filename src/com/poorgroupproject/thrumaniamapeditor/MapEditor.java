package com.poorgroupproject.thrumaniamapeditor;


import com.poorgroupproject.thrumaniamapeditor.form.Frame;

import java.awt.*;

/**
 * Created by ahmad on 5/16/16.
 */
public class MapEditor {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Frame frame = new Frame();
                frame.showFrame();
            }
        });
    }
}
