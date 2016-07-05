package com.poorgroupproject.thrumaniamapeditor.form;


import com.poorgroupproject.thrumaniamapeditor.Path;
import com.poorgroupproject.thrumaniamapeditor.form.element.ItemPanel;
import com.poorgroupproject.thrumaniamapeditor.form.element.NaviPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.Location;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * @author ahmad
 * @version 1.0.0
 */
public class Main extends Frame {

    private int rows;
    private int cols;
    private int mapWidth;
    private int mapHeight;

    private int mapViewportX;
    private int mapViewportY;
    private int mapViewPortWidth;
    private int mapViewPortHeight;

    private int screenWidth;
    private int screenHeight;


    private final int CELL_DEFAULT_WIDTH = 75;
    private final int CELL_DEFAULT_HEIGHT = 75;
    private final int MAP_DEFAULT_ROW = 50;
    private final int MAP_DEFAULT_COL = 50;
    private final int MAP_MIN_ROW = 15;
    private final int MAP_MIN_COL = 26;
    private final int MAP_MAX_ROW = 120;
    private final int MAP_MAX_COL = 208;
    private final int BORDER_LEN_MOVE_VIEWPORT = 100;
    private double zoomScale;

    private final int MINI_MAP_WIDTH = 320;
    private final int MINI_MAP_HEIGHT = 180;
    private int miniMapX;
    private int miniMapY;

    private Cell[][] mapMatrix;
    private BufferedImage miniMap;
    private BufferedImage tempMiniMap;

    private BufferedImage map;
    private BufferedImage tempMap;

    private Image waterImage;
    private Image[] landImages;
    private Image treeImage;
    private Image mountImage;
    private Image farmImage;
    private Image fishImage;
    private Image goldMineImage;
    private Image ironMineImage;


    private boolean isViewPortMoving;

    private Point cursorLastLocation;

    private Stack<Action> undo;
    private Stack<Action> redo;

    public enum PointerMode {
        WATER, LAND, MOUNTAIN, TREE, FARM, GOLD_MINE, IRON_MINE, MAP_MOVING,FISH
    };

    public PointerMode pointerMode = null;


    public enum Cell {
        WATER, LAND, MOUNTAIN, TREE, FARM, GOLD_MINE, IRON_MINE,FISH
    };

    public Main() {
        super();
        isViewPortMoving = false;
        mapViewPortWidth = ((int) getScreenDimension().getWidth()) - MINI_MAP_WIDTH;
        mapViewPortHeight = ((int) getScreenDimension().getHeight()) - MINI_MAP_HEIGHT;
        undo = new Stack<>();
        redo = new Stack<>();

        screenWidth = ((int) getScreenDimension().getWidth());
        screenHeight = ((int) getScreenDimension().getHeight());
        miniMapX = screenWidth - MINI_MAP_WIDTH;
        miniMapY = screenHeight - MINI_MAP_HEIGHT;

        initMap();
        loadImages();
        setMapDimension();
        makeMap();
        drawMiniMap();

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                MousePositionType mousePositionType = detectMousePosition(mouseEvent.getX(), mouseEvent.getY());
                if (mousePositionType == MousePositionType.IN_MAP)
                    clickOnMap(mouseEvent.getX(), mouseEvent.getY());
                else if (mousePositionType == MousePositionType.IN_MINIMAP)
                    clickOnMiniMap(mouseEvent.getX(), mouseEvent.getY());
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                cursorLastLocation = new Point(mouseEvent.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                MousePositionType mousePositionType = detectMousePosition(mouseEvent.getX(), mouseEvent.getY());
                if (mousePositionType == MousePositionType.IN_MAP)
                    clickOnMap(mouseEvent.getX(), mouseEvent.getY());
                else if (mousePositionType == MousePositionType.IN_MINIMAP)
                    clickOnMiniMap(mouseEvent.getX(), mouseEvent.getY());
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                Runnable r = null;
                if (mouseEvent.getY() < BORDER_LEN_MOVE_VIEWPORT) {
                    r = new Runnable() {
                        @Override
                        public void run() {
                            isViewPortMoving = false;
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            isViewPortMoving = true;
                            while (isViewPortMoving) {
                                moveViewportUp();
                            }
                        }
                    };
                } else if ((mouseEvent.getY() > mapViewPortHeight - BORDER_LEN_MOVE_VIEWPORT) && (mouseEvent.getY() < mapViewPortHeight)) {
                    r = new Runnable() {
                        @Override
                        public void run() {
                            isViewPortMoving = false;
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            isViewPortMoving = true;
                            while (isViewPortMoving) {
                                moveViewportDown();
                            }
                        }
                    };
                } else if (mouseEvent.getX() < BORDER_LEN_MOVE_VIEWPORT) {
                    r = new Runnable() {
                        @Override
                        public void run() {
                            isViewPortMoving = false;
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            isViewPortMoving = true;
                            while (isViewPortMoving) {
                                moveViewportLeft();
                            }
                        }
                    };
                } else if ((mouseEvent.getX() > mapViewPortWidth - BORDER_LEN_MOVE_VIEWPORT) && (mouseEvent.getX() < mapViewPortWidth)) {
                    r = new Runnable() {
                        @Override
                        public void run() {
                            isViewPortMoving = false;
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            isViewPortMoving = true;
                            while (isViewPortMoving) {
                                moveViewportRight();
                            }
                        }
                    };
                } else {
                    isViewPortMoving = false;
                }
                if (r != null) {
                    (new Thread(r)).start();
                }
            }
        });
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent){
                handleKeyboardInput(keyEvent);
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
                switch (mouseWheelEvent.getWheelRotation()) {
                    case -1:
                        zoomIn();
                        break;
                    case 1:
                        zoomOut();
                        break;
                }
            }
        });

        Canvas c = new ItemPanel(this,new Point(miniMapX,0),new Dimension(MINI_MAP_WIDTH,mapViewPortHeight));
        add(c);
        add(new NaviPanel(0,miniMapY,mapViewPortWidth,MINI_MAP_HEIGHT, this));
    }

    public void setIsViewPortMoving(boolean value){
        isViewPortMoving = value;
    }

    private enum MousePositionType {
        IN_MINIMAP, IN_MAP, IN_LEFT_PANEL, IN_BOTTOM_PANEL
    }

    ;

    public void undo(){

    }

    public void redo(){

    }

    public void handleKeyboardInput(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
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
            case KeyEvent.VK_Z:
                addRow();
                break;
            case KeyEvent.VK_X:
                addCol();
                break;
            case KeyEvent.VK_C:
                removeRow();
                break;
            case KeyEvent.VK_V:
                removeCol();
                break;

            case KeyEvent.VK_S:
                saveMap();
                break;
            case KeyEvent.VK_O:
                openMap();
                break;

            case KeyEvent.VK_0:
                pointerMode = PointerMode.LAND;
                break;
            case KeyEvent.VK_1:
                pointerMode = PointerMode.MOUNTAIN;
                break;
            case KeyEvent.VK_2:
                pointerMode = PointerMode.WATER;
                break;
            case KeyEvent.VK_3:
                pointerMode = PointerMode.TREE;
                break;
            case KeyEvent.VK_4:
                pointerMode = PointerMode.FARM;
                break;
            case KeyEvent.VK_5:
                pointerMode = PointerMode.GOLD_MINE;
                break;
            case KeyEvent.VK_6:
                pointerMode = PointerMode.IRON_MINE;
                break;
            case KeyEvent.VK_EQUALS:
                pointerMode = PointerMode.MAP_MOVING;
                break;
        }
    }

    private MousePositionType detectMousePosition(int x, int y) {
        if ((x < miniMapX) && (y < miniMapY))
            return MousePositionType.IN_MAP;
        else if ((x > miniMapX) && (y < miniMapY))
            return MousePositionType.IN_LEFT_PANEL;
        else if ((x < miniMapX) && (y > miniMapY))
            return MousePositionType.IN_BOTTOM_PANEL;
        else
            return MousePositionType.IN_MINIMAP;
    }

    private void clickOnMap(int x, int y) {
        if (pointerMode == null)
            return;
        x += mapViewportX;
        y += mapViewportY;
        x /= zoomScale;
        y /= zoomScale;
        int row = y / CELL_DEFAULT_HEIGHT;
        int col = x / CELL_DEFAULT_WIDTH;
        try {
            if (pointerMode == PointerMode.LAND)
                changeItem(col, row, Cell.LAND);
            else if (pointerMode == PointerMode.WATER)
                changeItem(col, row, Cell.WATER);
            else if (pointerMode == PointerMode.TREE)
                changeItem(col, row, Cell.TREE);
            else if (pointerMode == PointerMode.MOUNTAIN)
                changeItem(col, row, Cell.MOUNTAIN);
            else if (pointerMode == PointerMode.GOLD_MINE)
                changeItem(col, row, Cell.GOLD_MINE);
            else if (pointerMode == PointerMode.FARM)
                changeItem(col, row, Cell.FARM);
            else if (pointerMode == PointerMode.FISH)
                changeItem(col, row, Cell.FISH);
            else if (pointerMode == PointerMode.IRON_MINE)
                changeItem(col,row,Cell.IRON_MINE);
            else if (pointerMode == PointerMode.MAP_MOVING){
                mapViewportX -= x - cursorLastLocation.getX();
                mapViewportY -= y - cursorLastLocation.getY();
                cursorLastLocation.setLocation(x,y);
                drawMiniMap();
                repaint();
                System.out.println("some loooooooooooooooooooooooooo" + (x - cursorLastLocation.getX()));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("array out of bound");
        }
    }

    public void saveMap() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Thrumania Map File", "tmf");
        fileChooser.addChoosableFileFilter(fileFilter);
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String selectedFilePath = fileChooser.getSelectedFile().toString();
            if (!selectedFilePath.endsWith(".tmf")){
                selectedFilePath += ".tmf";
            }
            PrintWriter fileSaver = null;
            try {
                fileSaver = new PrintWriter(selectedFilePath);
            } catch (FileNotFoundException e) {
                System.err.println("file not found");
                return;
            }
            fileSaver.println(rows);
            fileSaver.println(cols);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    fileSaver.print(mapMatrix[i][j] + " ");
                }
                fileSaver.println();
            }

            
            fileSaver.close();
        }
    }

    public void openMap() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Thrumania Map File", "tmf");
        fileChooser.addChoosableFileFilter(fileFilter);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            Scanner s = null;
            try {
                s = new Scanner(selectedFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            rows = s.nextInt();
            cols = s.nextInt();
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    switch (s.next()){
                        case "LAND":
                            mapMatrix[i][j] = Cell.LAND;
                            break;
                        case "MOUNTAIN":
                            mapMatrix[i][j] = Cell.MOUNTAIN;
                            break;
                        case "FARM":
                            mapMatrix[i][j] = Cell.FARM;
                            break;
                        case "TREE":
                            mapMatrix[i][j] = Cell.TREE;
                            break;
                        case "FISH":
                            mapMatrix[i][j] = Cell.FISH;
                            break;
                        case "WATER":
                            mapMatrix[i][j] = Cell.WATER;
                            break;
                        case "GOLD_MINE":
                            mapMatrix[i][j] = Cell.GOLD_MINE;
                            break;
                        case "IRON_MINE":
                            mapMatrix[i][j] = Cell.IRON_MINE;
                            break;
                    }
                }
            }
            makeMap();
            drawMiniMap();
            repaint();

        }

    }

    /**
     * It assumes that the position of the mouse is in the mini map but the x and y parameter passes to the
     * method is still the position of mouse with respect to the origin of the screen(upper left corner)
     *
     * @param x
     * @param y
     */
    private void clickOnMiniMap(int x, int y) {
        x -= miniMapX;
        y -= miniMapY;
        mapViewportX = mapWidth * x / MINI_MAP_WIDTH;
        mapViewportY = mapHeight * y / MINI_MAP_HEIGHT;
        if (mapViewportX > mapWidth + mapViewPortWidth)
            mapViewportX = mapWidth + mapViewPortWidth;
        if (mapViewportY > mapHeight + mapViewPortHeight)
            mapViewportY = mapHeight + mapViewPortHeight;
        drawMiniMap();
        repaint();
    }

    public void zoomIn() {
        if (zoomScale < 3) {
            zoomScale += 0.25;
            repaint();
        }
        drawMiniMap();
    }

    public void zoomOut() {
        if (zoomScale > 0.1) {
            zoomScale -= 0.25;
            repaint();
        }
        drawMiniMap();
    }

    public void addCol() {
        if (cols < MAP_MAX_COL) {
            cols++;
            setMapDimension();
        }
    }

    public void addRow() {
        if (rows < MAP_MAX_ROW) {
            rows++;
            setMapDimension();
        }
    }

    public void removeCol() {
        if (cols > MAP_MIN_COL) {
            cols--;
            setMapDimension();
        }
    }

    public void removeRow() {
        if (rows > MAP_MIN_ROW) {
            rows--;
            setMapDimension();
        }
    }

    public void moveViewportLeft() {
        if (mapViewportX > 10) {
            mapViewportX -= 10;
            drawMiniMap();
            repaint();
        }
    }

    public void moveViewportRight() {
        if (mapViewportX + mapViewPortWidth < mapHeight - 10) {
            mapViewportX += 10;
            drawMiniMap();
            repaint();
        }
    }

    public void moveViewportUp() {
        if (mapViewportY > 10) {
            mapViewportY -= 10;
            drawMiniMap();
            repaint();
        }
    }

    public void moveViewportDown() {
        if (mapViewportY + mapViewPortHeight < mapWidth - 10) {
            mapViewportY += 10;
            drawMiniMap();
            repaint();
        }
    }

    private Image getLandImage(int row, int col) {
        /**
         * U1U
         * 8X2
         * U4U
         */
        int counter = 0;

        if ((row != 0) && (mapMatrix[row - 1][col] == Cell.LAND || mapMatrix[row - 1][col] == Cell.TREE || mapMatrix[row - 1][col] == Cell.FARM || mapMatrix[row - 1][col] == Cell.MOUNTAIN || mapMatrix[row - 1][col] == Cell.GOLD_MINE || mapMatrix[row - 1][col] == Cell.IRON_MINE)) {
            counter += 1;
        }
        if ((row != rows - 1) && (mapMatrix[row + 1][col] == Cell.LAND || mapMatrix[row + 1][col] == Cell.TREE || mapMatrix[row + 1][col] == Cell.FARM || mapMatrix[row + 1][col] == Cell.MOUNTAIN || mapMatrix[row + 1][col] == Cell.GOLD_MINE || mapMatrix[row + 1][col] == Cell.IRON_MINE)) {
            counter += 4;
        }
        if ((col != 0) && (mapMatrix[row][col - 1] == Cell.LAND || mapMatrix[row][col - 1] == Cell.TREE || mapMatrix[row][col - 1] == Cell.FARM || mapMatrix[row][col - 1] == Cell.MOUNTAIN || mapMatrix[row][col - 1] == Cell.GOLD_MINE || mapMatrix[row][col - 1] == Cell.IRON_MINE)) {
            counter += 8;
        }
        if ((col != cols - 1) && (mapMatrix[row][col + 1] == Cell.LAND || mapMatrix[row][col + 1] == Cell.TREE || mapMatrix[row][col + 1] == Cell.LAND || mapMatrix[row][col + 1] == Cell.MOUNTAIN || mapMatrix[row][col + 1] == Cell.GOLD_MINE || mapMatrix[row][col + 1] == Cell.IRON_MINE)) {
            counter += 2;
        }
        return landImages[counter];
    }

    private void loadImages() {
        landImages = new Image[16];
        try {
            waterImage = ImageIO.read(new File(Path.tileImagePath + "water.png"));
            landImages[0] = ImageIO.read(new File(Path.tileImagePath + "no.png"));
            landImages[1] = ImageIO.read(new File(Path.tileImagePath + "od.png"));
            landImages[2] = ImageIO.read(new File(Path.tileImagePath + "or.png"));
            landImages[3] = ImageIO.read(new File(Path.tileImagePath + "sld.png"));
            landImages[4] = ImageIO.read(new File(Path.tileImagePath + "ou.png"));
            landImages[5] = ImageIO.read(new File(Path.tileImagePath + "ocntrud.png"));
            landImages[6] = ImageIO.read(new File(Path.tileImagePath + "slu.png"));
            landImages[7] = ImageIO.read(new File(Path.tileImagePath + "sl.png"));
            landImages[8] = ImageIO.read(new File(Path.tileImagePath + "ol.png"));
            landImages[9] = ImageIO.read(new File(Path.tileImagePath + "srd.png"));
            landImages[10] = ImageIO.read(new File(Path.tileImagePath + "ocntrlr.png"));
            landImages[11] = ImageIO.read(new File(Path.tileImagePath + "sd.png"));
            landImages[12] = ImageIO.read(new File(Path.tileImagePath + "sru.png"));
            landImages[13] = ImageIO.read(new File(Path.tileImagePath + "sr.png"));
            landImages[14] = ImageIO.read(new File(Path.tileImagePath + "su.png"));
            landImages[15] = ImageIO.read(new File(Path.tileImagePath + "cntr.png"));


            treeImage = ImageIO.read(new File("resource/image/item/spring.png"));
            mountImage = ImageIO.read(new File("resource/image/tile/mountain.png"));
            fishImage = ImageIO.read(new File("resource/image/tile/fish.png"));
            goldMineImage = ImageIO.read(new File("resource/image/tile/goldmine.png"));
            ironMineImage = ImageIO.read(new File("resource/image/tile/ironmine.png"));
            farmImage = ImageIO.read(new File("resource/image/tile/farm.png"));

            //Scaling images
            waterImage = waterImage.getScaledInstance(CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, Image.SCALE_DEFAULT);
            for (int i = 0; i < landImages.length; i++) {
                landImages[i] = landImages[i].getScaledInstance(CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, Image.SCALE_DEFAULT);
            }
            treeImage = treeImage.getScaledInstance(CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, Image.SCALE_DEFAULT);
            mountImage = mountImage.getScaledInstance(CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, Image.SCALE_DEFAULT);
            fishImage = fishImage.getScaledInstance(CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, Image.SCALE_DEFAULT);
            goldMineImage = goldMineImage.getScaledInstance(CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, Image.SCALE_DEFAULT);
            ironMineImage = ironMineImage.getScaledInstance(CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, Image.SCALE_DEFAULT);
            farmImage = farmImage.getScaledInstance(CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, Image.SCALE_DEFAULT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMapDimension() {
        mapWidth = cols * CELL_DEFAULT_WIDTH;
        mapHeight = rows * CELL_DEFAULT_HEIGHT;

        Cell[][] mapMatrixTemp = new Cell[rows][cols];
        tempMap = new BufferedImage(mapWidth,mapHeight,BufferedImage.TYPE_INT_RGB);
        tempMiniMap = new BufferedImage(mapWidth,mapHeight,BufferedImage.TYPE_INT_RGB);

        Graphics tempMapGraphics = tempMap.createGraphics();
        Graphics tempMiniMapGraphics = tempMiniMap.createGraphics();

        int lastRows = mapMatrix.length;
        int lastCols = mapMatrix[0].length;

        if (lastCols > cols) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    mapMatrixTemp[i][j] = mapMatrix[i][j];
                }
            }
            tempMapGraphics.drawImage(map.getSubimage(0,0,cols * CELL_DEFAULT_WIDTH, rows * CELL_DEFAULT_HEIGHT),0,0,null);
            tempMiniMapGraphics.drawImage(miniMap.getSubimage(0,0,cols * CELL_DEFAULT_WIDTH, rows * CELL_DEFAULT_HEIGHT),0,0,null);
        }
        else if (lastRows > rows) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    mapMatrixTemp[i][j] = mapMatrix[i][j];
                }
            }
            tempMapGraphics.drawImage(map.getSubimage(0,0,lastCols * CELL_DEFAULT_WIDTH, lastRows * CELL_DEFAULT_HEIGHT),0,0,null);
            tempMiniMapGraphics.drawImage(miniMap.getSubimage(0,0,cols * CELL_DEFAULT_WIDTH, rows * CELL_DEFAULT_HEIGHT),0,0,null);
        }
        else if (lastCols < cols) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < lastCols; j++) {
                    mapMatrixTemp[i][j] = mapMatrix[i][j];
                }
                tempMapGraphics.drawImage(waterImage,(cols - 1) * CELL_DEFAULT_WIDTH, i * CELL_DEFAULT_HEIGHT,CELL_DEFAULT_WIDTH,CELL_DEFAULT_HEIGHT,null);
                tempMiniMapGraphics.drawImage(waterImage,(cols - 1) * CELL_DEFAULT_WIDTH, i * CELL_DEFAULT_HEIGHT, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
                mapMatrixTemp[i][cols - 1] = Cell.WATER;
            }
            tempMapGraphics.drawImage(map.getSubimage(0,0,lastCols * CELL_DEFAULT_WIDTH, lastRows * CELL_DEFAULT_HEIGHT),0,0,null);
            tempMiniMapGraphics.drawImage(miniMap.getSubimage(0,0,lastCols * CELL_DEFAULT_WIDTH, lastRows * CELL_DEFAULT_HEIGHT),0,0,null);
        }
        else if (lastRows < rows) {
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < lastRows; j++) {
                    mapMatrixTemp[j][i] = mapMatrix[j][i];
                }
                tempMapGraphics.drawImage(waterImage,i * CELL_DEFAULT_WIDTH, (rows - 1) * CELL_DEFAULT_HEIGHT,CELL_DEFAULT_WIDTH,CELL_DEFAULT_HEIGHT,null);
                tempMiniMapGraphics.drawImage(waterImage,i * CELL_DEFAULT_WIDTH, (rows - 1) * CELL_DEFAULT_HEIGHT,CELL_DEFAULT_WIDTH,CELL_DEFAULT_HEIGHT,null);
                mapMatrixTemp[rows - 1][i] = Cell.WATER;
            }
            tempMapGraphics.drawImage(map.getSubimage(0,0,lastCols * CELL_DEFAULT_WIDTH, lastRows * CELL_DEFAULT_HEIGHT),0,0,null);
            tempMiniMapGraphics.drawImage(miniMap.getSubimage(0,0,lastCols * CELL_DEFAULT_WIDTH, lastRows * CELL_DEFAULT_HEIGHT),0,0,null);
        }

        map = tempMap;
        miniMap = tempMiniMap;
        repaint();
    }

    private void initMap() {
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


    }

    private void drawMiniMap() {
        miniMap = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = miniMap.getGraphics();
        graphics.drawImage(map, 0, 0, null);
        int viewPortRectWidth = ((int) (mapViewPortWidth / zoomScale));
        int viewPortRectHeight = ((int) (mapViewPortHeight / zoomScale));
        graphics.setColor(new Color((float) 0.9, (float) 0.2, (float) 0.2, (float) 0.1));
        graphics.fillRect(mapViewportX, mapViewportY, viewPortRectWidth, viewPortRectHeight);
        graphics.dispose();
    }

    private void changeItem(int col, int row, Cell content) {
        Graphics graphics = map.createGraphics();
        Graphics graphics1 = miniMap.createGraphics();

        Image image = null;
        if (content == Cell.WATER) {
            image = waterImage;
            mapMatrix[row][col] = content;
        } else if (content == Cell.LAND) {
            image = getLandImage(row, col);
            mapMatrix[row][col] = content;
        } else if (content == Cell.TREE) {
            if (mapMatrix[row][col] == Cell.LAND) {
                image = treeImage;
                graphics.drawImage(image, col * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
                graphics1.drawImage(image, col * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
                repaint();
            }
            mapMatrix[row][col] = content;
            return;
        } else if (content == Cell.MOUNTAIN){
            if (mapMatrix[row][col] == Cell.LAND){
                image = mountImage;
                graphics.drawImage(image, col * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
                graphics1.drawImage(image, col * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
                repaint();
            }
            mapMatrix[row][col] = content;
            return;
        } else if (content == Cell.FARM){
            if (mapMatrix[row][col] == Cell.LAND){
                image = farmImage;
                graphics.drawImage(image, col * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
                graphics1.drawImage(image, col * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
                repaint();
            }
            mapMatrix[row][col] = content;
            return;
        } else if (content == Cell.IRON_MINE){
            if (mapMatrix[row][col] == Cell.MOUNTAIN){
                image = ironMineImage;
                graphics.drawImage(image, col * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
                graphics1.drawImage(image, col * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
                repaint();
            }
            mapMatrix[row][col] = content;
            return;
        } else if (content == Cell.GOLD_MINE){
            if (mapMatrix[row][col] == Cell.MOUNTAIN){
                image = goldMineImage;
                graphics.drawImage(image, col * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
                graphics1.drawImage(image, col * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
                repaint();
            }
            mapMatrix[row][col] = content;
            return;
        } else if (content == Cell.FISH) {
            if (mapMatrix[row][col] == Cell.WATER) {
                image = fishImage;
                graphics.drawImage(image, col * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
                graphics1.drawImage(image, col * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
                repaint();
            }
            mapMatrix[row][col] = content;
            return;
        }


        // To drawing the image of the neighbour cells
        if ((col != 0) && (mapMatrix[row][col - 1] == Cell.LAND)) {
            graphics.drawImage(getLandImage(row, col - 1), (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row, col - 1), (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((col != 0) && (mapMatrix[row][col - 1] == Cell.TREE)) {
            graphics.drawImage(getLandImage(row, col - 1), (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row, col - 1), (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(treeImage, (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(treeImage, (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((col != 0) && (mapMatrix[row][col - 1] == Cell.MOUNTAIN)) {
            graphics.drawImage(getLandImage(row, col - 1), (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row, col - 1), (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(mountImage, (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(mountImage, (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((col != 0) && (mapMatrix[row][col - 1] == Cell.GOLD_MINE)) {
            graphics.drawImage(getLandImage(row, col - 1), (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row, col - 1), (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(mountImage, (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(mountImage, (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(goldMineImage, (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(goldMineImage, (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((col != 0) && (mapMatrix[row][col - 1] == Cell.IRON_MINE)) {
            graphics.drawImage(getLandImage(row, col - 1), (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row, col - 1), (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(mountImage, (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(mountImage, (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(ironMineImage, (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(ironMineImage, (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }


        if ((col != cols - 1) && (mapMatrix[row][col + 1] == Cell.LAND)) {
            graphics.drawImage(getLandImage(row, col + 1), (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row, col + 1), (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((col != cols - 1) && (mapMatrix[row][col + 1] == Cell.TREE)) {
            graphics.drawImage(getLandImage(row, col + 1), (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row, col + 1), (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(treeImage, (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(treeImage, (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((col != cols - 1) && (mapMatrix[row][col + 1] == Cell.MOUNTAIN)) {
            graphics.drawImage(getLandImage(row, col + 1), (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row, col + 1), (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(mountImage, (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(mountImage, (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((col != cols - 1) && (mapMatrix[row][col + 1] == Cell.GOLD_MINE)) {
            graphics.drawImage(getLandImage(row, col + 1), (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row, col + 1), (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(mountImage, (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(mountImage, (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(goldMineImage, (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(goldMineImage, (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((col != cols - 1) && (mapMatrix[row][col + 1] == Cell.IRON_MINE)) {
            graphics.drawImage(getLandImage(row, col + 1), (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row, col + 1), (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(mountImage, (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(mountImage, (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(ironMineImage, (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(ironMineImage, (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }

        if ((row != 0) && (mapMatrix[row - 1][col] == Cell.LAND)) {
            graphics.drawImage(getLandImage(row - 1, col), col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row - 1, col), col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((row != 0) && (mapMatrix[row - 1][col] == Cell.TREE)) {
            graphics.drawImage(getLandImage(row - 1, col), col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row - 1, col), col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(treeImage, col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(treeImage, col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((row != 0) && (mapMatrix[row - 1][col] == Cell.MOUNTAIN)) {
            graphics.drawImage(getLandImage(row - 1, col), col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row - 1, col), col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(mountImage, col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(mountImage, col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((row != 0) && (mapMatrix[row - 1][col] == Cell.GOLD_MINE)) {
            graphics.drawImage(getLandImage(row - 1, col), col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row - 1, col), col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(mountImage, col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(mountImage, col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(goldMineImage, col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(goldMineImage, col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((row != 0) && (mapMatrix[row - 1][col] == Cell.IRON_MINE)) {
            graphics.drawImage(getLandImage(row - 1, col), col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row - 1, col), col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(mountImage, col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(mountImage, col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(ironMineImage, col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(ironMineImage, col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }


        if ((row != rows - 1) && (mapMatrix[row + 1][col] == Cell.LAND)) {
            graphics.drawImage(getLandImage(row + 1, col), col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row + 1, col), col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((row != rows - 1) && (mapMatrix[row + 1][col] == Cell.TREE)) {
            graphics.drawImage(getLandImage(row + 1, col), col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row + 1, col), col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(treeImage, col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(treeImage, col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((row != rows - 1) && (mapMatrix[row + 1][col] == Cell.MOUNTAIN)) {
            graphics.drawImage(getLandImage(row + 1, col), col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row + 1, col), col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(mountImage, col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(mountImage, col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((row != rows - 1) && (mapMatrix[row + 1][col] == Cell.GOLD_MINE)) {
            graphics.drawImage(getLandImage(row + 1, col), col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row + 1, col), col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(mountImage, col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(mountImage, col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(goldMineImage, col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(goldMineImage, col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((row != rows - 1) && (mapMatrix[row + 1][col] == Cell.IRON_MINE)) {
            graphics.drawImage(getLandImage(row + 1, col), col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row + 1, col), col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(mountImage, col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(mountImage, col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics.drawImage(ironMineImage, col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(ironMineImage, col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }


        graphics.drawImage(image, col * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        graphics1.drawImage(image, col * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);

        repaint();
    }

    private void makeMap() {
        map = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = map.createGraphics();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Image image = null;
                switch (mapMatrix[i][j]) {
                    case WATER:
                        image = waterImage;
                        break;
                    case LAND:
                        changeItem(j, i, Cell.LAND);
                        break;
                    case FARM:
                        changeItem(j,i,Cell.FARM);
                        break;
                    case MOUNTAIN:
                        changeItem(j,i,Cell.MOUNTAIN);
                        break;
                    case TREE:
                        changeItem(j,i,Cell.TREE);
                        break;
                    case FISH:
                        changeItem(j,i,Cell.FISH);
                        break;
                    case GOLD_MINE:
                        changeItem(j,i,Cell.GOLD_MINE);
                        break;
                    case IRON_MINE:
                        changeItem(j,i,Cell.IRON_MINE);
                        break;

                }

                graphics.drawImage(image, j * CELL_DEFAULT_HEIGHT, i * CELL_DEFAULT_WIDTH, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            }
        }
        repaint();
    }

    @Override
    public void putElements() {


    }

    @Override
    public void paint(Graphics graphics) {
        int width = ((int) (mapWidth * zoomScale));
        int height = ((int) (mapHeight * zoomScale));
        int x = -1 * mapViewportX;
        int y = -1 * mapViewportY;
        graphics.drawImage(map, x, y, width, height, null);
        graphics.drawImage(map.getSubimage(mapViewportX,mapViewportY,mapViewPortWidth,mapViewPortHeight), 0, 0, mapViewPortWidth,mapViewPortHeight, null);
        graphics.drawImage(miniMap,
                mapViewPortWidth,
                mapViewPortHeight,
                MINI_MAP_WIDTH
                , MINI_MAP_HEIGHT
                , null);
    }
}

class Action {

    private enum ActionType {
        ADD_ROW, RM_ROW, ADD_COL, RM_COL, CHG_MAP
    }

    ;

    ActionType actionType;
    Main.Cell[][] matrix;

    public Action(ActionType actionType) {
        this.actionType = actionType;
        if (actionType == ActionType.CHG_MAP)
            System.err.println("Invalid action type");
    }

    public Action(Main.Cell[][] matrix, int col, int row) {
        actionType = ActionType.CHG_MAP;
    }

}