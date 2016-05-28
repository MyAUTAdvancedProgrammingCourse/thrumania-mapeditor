package com.poorgroupproject.thrumaniamapeditor.form.element;

import com.poorgroupproject.thrumaniamapeditor.Path;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author yahya
 * @version 1.0.0
 */
public class MyFrame extends JFrame implements ActionListener{
    int w,h;
    int waterCheck=0,landCheck=0,mountainCheck=0,fishCheck=0,goldCheck=0,ironCheck=0,treeCheck=0,agriCheck=0;
    JLabel lWater,lLand,lMountain,lFish,lGold,lIron,lTree,lAgri;
    ImageIcon [] water=new ImageIcon[3];
    ImageIcon [] land=new ImageIcon[3];
    ImageIcon [] mountain=new ImageIcon[3];
    ImageIcon [] fish=new ImageIcon[3];
    ImageIcon [] gold=new ImageIcon[3];
    ImageIcon [] iron=new ImageIcon[3];
    ImageIcon [] tree=new ImageIcon[3];
    ImageIcon [] agri =new ImageIcon[3];



    public MyFrame(int w,int h) {
        this.h=h;
        this.w=w;


        setLayout(null);
        Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
        setSize(w, h);
        setLocation((d.width-w)/2,(d.height-h)/2);
        setTitle("map editor");
        getContentPane().setBackground(Color.black);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //setResizable(false);

        tree[0]=new ImageIcon(Path.panelImagePath + "tree0.png");
        tree[1]=new ImageIcon(Path.panelImagePath + "tree1.png");
        tree[2]=new ImageIcon(Path.panelImagePath + "tree2.png");
        water[0]=new ImageIcon(Path.panelImagePath + "sea0.png");
        water[1]=new ImageIcon(Path.panelImagePath + "sea1.png");
        water[2]=new ImageIcon(Path.panelImagePath + "sea2.png");
        land[0]=new ImageIcon(Path.panelImagePath + "land0.png");
        land[1]=new ImageIcon(Path.panelImagePath + "land1.png");
        land[2]=new ImageIcon(Path.panelImagePath + "land2.png");
        mountain[0]=new ImageIcon(Path.panelImagePath + "mountain0.png");
        mountain[1]=new ImageIcon(Path.panelImagePath + "mountain1.png");
        mountain[2]=new ImageIcon(Path.panelImagePath + "mountain2.png");
        fish[0]=new ImageIcon(Path.panelImagePath + "fish0.png");
        fish[1]=new ImageIcon("fish1.png");
        fish[2]=new ImageIcon("fish2.png");
        gold[0]=new ImageIcon("gold0.png");
        gold[1]=new ImageIcon("gold1.png");
        gold[2]=new ImageIcon("gold2.png");
        iron[0]=new ImageIcon("iron0.png");
        iron[1]=new ImageIcon("iron1.png");
        iron[2]=new ImageIcon("iron2.png");
        agri[0]=new ImageIcon("agri0.png");
        agri[1]=new ImageIcon("agri1.png");
        agri[2]=new ImageIcon("agri2.png");







        lWater=new JLabel();
        lWater.setIcon(water[0]);
        lWater.setLocation(w-250,h-1880);
        lWater.setSize(200,200);
        getContentPane().add(lWater);


        lWater.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("clicked");
                lWater.setIcon(water[2]);
                waterCheck=1;
                if(waterCheck==0){
                    lWater.setIcon(water[0]);
                }

                if(landCheck==1){
                    lLand.setIcon(land[0]);
                }
                if(mountainCheck==1){
                    lMountain.setIcon(mountain[0]);
                }
                if(fishCheck==1){
                    lFish.setIcon(fish[0]);
                }
                if(goldCheck==1){
                    lGold.setIcon(gold[0]);
                }
                if(ironCheck==1){
                    lIron.setIcon(iron[0]);
                }
                if(treeCheck==1){
                    lTree.setIcon(tree[0]);
                }
                if(agriCheck==1){
                    lAgri.setIcon(agri[0]);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("entered");
                if(waterCheck!=1) {
                    lWater.setIcon(water[1]);
                }

            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("exit");
                if(waterCheck!=1) {
                    lWater.setIcon(water[0]);
                }

            }
        });



        lLand=new JLabel();
        lLand.setIcon(land[0]);
        lLand.setLocation(w-250,h-1680);
        lLand.setSize(200,200);
        getContentPane().add(lLand);


        lLand.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("clicked");
                lLand.setIcon(land[2]);
                landCheck=1;

                if(waterCheck==1){
                    lWater.setIcon(water[0]);
                }

                if(landCheck==0){
                    lLand.setIcon(land[0]);
                }
                if(mountainCheck==1){
                    lMountain.setIcon(mountain[0]);
                }
                if(fishCheck==1){
                    lFish.setIcon(fish[0]);
                }
                if(goldCheck==1){
                    lGold.setIcon(gold[0]);
                }
                if(ironCheck==1){
                    lIron.setIcon(iron[0]);
                }
                if(treeCheck==1){
                    lTree.setIcon(tree[0]);
                }
                if(agriCheck==1){
                    lAgri.setIcon(agri[0]);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("entered");
                if(landCheck!=1) {
                    lLand.setIcon(land[1]);
                }

            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("exit");
                if(landCheck!=1) {
                    lLand.setIcon(land[0]);
                }

            }
        });

        lMountain=new JLabel();
        lMountain.setIcon(mountain[0]);
        lMountain.setLocation(w-250,h-1480);
        lMountain.setSize(200,200);
        getContentPane().add(lMountain);


        lMountain.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("clicked");
                lMountain.setIcon(mountain[2]);
                mountainCheck=1;

                if(waterCheck==1){
                    lWater.setIcon(water[0]);
                }

                if(landCheck==1){
                    lLand.setIcon(land[0]);
                }
                if(mountainCheck==0){
                    lMountain.setIcon(mountain[0]);
                }
                if(fishCheck==1){
                    lFish.setIcon(fish[0]);
                }
                if(goldCheck==1){
                    lGold.setIcon(gold[0]);
                }
                if(ironCheck==1){
                    lIron.setIcon(iron[0]);
                }
                if(treeCheck==1){
                    lTree.setIcon(tree[0]);
                }
                if(agriCheck==1){
                    lAgri.setIcon(agri[0]);
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("entered");
                if(mountainCheck!=1) {
                    lMountain.setIcon(mountain[0b1]);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("exit");
                if(mountainCheck!=1) {
                    lMountain.setIcon(mountain[0]);
                }

            }
        });

        lFish=new JLabel();
        lFish.setIcon(fish[0]);
        lFish.setLocation(w-250,h-1280);
        lFish.setSize(200,200);
        getContentPane().add(lFish);


        lFish.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("clicked");
                lFish.setIcon(fish[2]);
                fishCheck=1;

                if(waterCheck==1){
                    lWater.setIcon(water[0]);
                }

                if(landCheck==1){
                    lLand.setIcon(land[0]);
                }
                if(mountainCheck==1){
                    lMountain.setIcon(mountain[0]);
                }
                if(fishCheck==0){
                    lFish.setIcon(fish[0]);
                }
                if(goldCheck==1){
                    lGold.setIcon(gold[0]);
                }
                if(ironCheck==1){
                    lIron.setIcon(iron[0]);
                }
                if(treeCheck==1){
                    lTree.setIcon(tree[0]);
                }
                if(agriCheck==1){
                    lAgri.setIcon(agri[0]);
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("entered");
                if(fishCheck!=1) {
                    lFish.setIcon(fish[1]);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("exit");
                if(fishCheck!=1) {

                    lFish.setIcon(fish[0]);
                }
            }
        });


        lGold=new JLabel();
        lGold.setIcon(gold[0]);
        lGold.setLocation(w-250,h-1080);
        lGold.setSize(200,200);
        getContentPane().add(lGold);


        lGold.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("clicked");
                lGold.setIcon(gold[2]);
                goldCheck=1;

                if(waterCheck==1){
                    lWater.setIcon(water[0]);
                }

                if(landCheck==1){
                    lLand.setIcon(land[0]);
                }
                if(mountainCheck==1){
                    lMountain.setIcon(mountain[0]);
                }
                if(fishCheck==1){
                    lFish.setIcon(fish[0]);
                }
                if(goldCheck==0){
                    lGold.setIcon(gold[0]);
                }
                if(ironCheck==1){
                    lIron.setIcon(iron[0]);
                }
                if(treeCheck==1){
                    lTree.setIcon(tree[0]);
                }
                if(agriCheck==1){
                    lAgri.setIcon(agri[0]);
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("entered");
                if(goldCheck!=1) {
                    lGold.setIcon(gold[1]);
                }

            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("exit");
                if(goldCheck!=1) {
                    lGold.setIcon(gold[0]);
                }
            }
        });

        lIron=new JLabel();
        lIron.setIcon(iron[0]);
        lIron.setLocation(w-250,h-880);
        lIron.setSize(200,200);
        getContentPane().add(lIron);


        lIron.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("clicked");
                lIron.setIcon(iron[2]);
                ironCheck=1;

                if(waterCheck==1){
                    lWater.setIcon(water[0]);
                }

                if(landCheck==1){
                    lLand.setIcon(land[0]);
                }
                if(mountainCheck==1){
                    lMountain.setIcon(mountain[0]);
                }
                if(fishCheck==1){
                    lFish.setIcon(fish[0]);
                }
                if(goldCheck==1){
                    lGold.setIcon(gold[0]);
                }
                if(ironCheck==0){
                    lIron.setIcon(iron[0]);
                }
                if(treeCheck==1){
                    lTree.setIcon(tree[0]);
                }
                if(agriCheck==1){
                    lAgri.setIcon(agri[0]);
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("entered");
                if(ironCheck!=1) {
                    lIron.setIcon(iron[1]);
                }

            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("exit");
                if(ironCheck!=1) {
                    lIron.setIcon(iron[0]);
                }

            }
        });

        lAgri=new JLabel();
        lAgri.setIcon(agri[0]);
        lAgri.setLocation(w-250,h-680);
        lAgri.setSize(200,200);
        getContentPane().add(lAgri);


        lAgri.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("clicked");
                lAgri.setIcon(agri[2]);
                agriCheck=1;

                if(waterCheck==1){
                    lWater.setIcon(water[0]);
                }

                if(landCheck==1){
                    lLand.setIcon(land[0]);
                }
                if(mountainCheck==1){
                    lMountain.setIcon(mountain[0]);
                }
                if(fishCheck==1){
                    lFish.setIcon(fish[0]);
                }
                if(goldCheck==1){
                    lGold.setIcon(gold[0]);
                }
                if(ironCheck==1){
                    lIron.setIcon(iron[0]);
                }
                if(treeCheck==1){
                    lTree.setIcon(tree[0]);
                }
                if(agriCheck==0){
                    lAgri.setIcon(agri[0]);
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("entered");
                if(agriCheck!=1) {
                    lAgri.setIcon(agri[1]);
                }

            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("exit");
                if(agriCheck!=1) {
                    lAgri.setIcon(agri[0]);
                }

            }
        });

        lTree=new JLabel();
        lTree.setIcon(tree[0]);
        lTree.setLocation(w-250,h-480);
        lTree.setSize(200,200);
        getContentPane().add(lTree);


        lTree.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("clicked");
                lTree.setIcon(tree[2]);
                treeCheck=1;

                if(waterCheck==1){
                    lWater.setIcon(water[0]);
                }

                if(landCheck==1){
                    lLand.setIcon(land[0]);
                }
                if(mountainCheck==1){
                    lMountain.setIcon(mountain[0]);
                }
                if(fishCheck==1){
                    lFish.setIcon(fish[0]);
                }
                if(goldCheck==1){
                    lGold.setIcon(gold[0]);
                }
                if(ironCheck==1){
                    lIron.setIcon(iron[0]);
                }
                if(treeCheck==0){
                    lTree.setIcon(tree[0]);
                }
                if(agriCheck==1){
                    lAgri.setIcon(agri[0]);
                }


            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("entered");
                if(treeCheck!=1) {
                    lTree.setIcon(tree[1]);
                }

            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("exit");
                if (treeCheck!=1) {
                    lTree.setIcon(tree[0]);
                }

            }
        });






        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
