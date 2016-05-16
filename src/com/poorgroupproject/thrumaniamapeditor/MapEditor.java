package com.poorgroupproject.thrumaniamapeditor;


import com.poorgroupproject.thrumaniamapeditor.form.Frame;
import com.poorgroupproject.thrumaniamapeditor.form.Main;

import java.awt.*;

/**
 * @author ahmad
 * @version 1.0.0
 */
public class MapEditor {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Frame frame = new Main();
                frame.showFrame();
            }
        });
    }
}
