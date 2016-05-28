package com.poorgroupproject.thrumaniamapeditor.form;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

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

    private BufferedImage map;

    private Image waterImage;
    private Image[] landImages;
    private Image treeImage;

    private boolean isViewPortMoving;

    private Stack<Action> undo;
    private Stack<Action> redo;

    private enum PointerMode {
        WATER, LAND, MOUNTAIN, TREE, FARM, GOLD_MINE, IRON_MINE,
    }

    ;

    private PointerMode pointerMode = null;


    public enum Cell {
        WATER, LAND, MOUNTAIN, TREE, FARM, GOLD_MINE, IRON_MINE,
    }

    ;

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
                clickOnMap(mouseEvent.getX(), mouseEvent.getY());
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

            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {

                clickOnMap(mouseEvent.getX(), mouseEvent.getY());
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
            public void keyPressed(KeyEvent keyEvent) {
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
                    case KeyEvent.VK_A:
                        addRow();
                        break;
                    case KeyEvent.VK_S:
                        addCol();
                        break;
                    case KeyEvent.VK_D:
                        removeRow();
                        break;
                    case KeyEvent.VK_F:
                        removeCol();
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
                }
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
    }

    private final byte IN_MINIMAP = 1;
    private final byte IN_MAP = 2;
    private final byte IN_LEFT_PANEL = 3;
    private final byte IN_BUTTOM_PANEL = 4;

    private byte detectMousePosition(int x, int y) {
        return 0;
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
    }

    private void zoomIn() {
        if (zoomScale < 3) {
            zoomScale += 0.25;
            repaint();
        }
        drawMiniMap();
    }

    private void zoomOut() {
        if (zoomScale > 0.1) {
            zoomScale -= 0.25;
            repaint();
        }
        drawMiniMap();
    }

    private void addCol() {
        if (cols < MAP_MAX_COL) {
            cols++;
            setMapDimension();
        }
    }

    private void addRow() {
        if (rows < MAP_MAX_ROW) {
            rows++;
            setMapDimension();
        }
    }

    private void removeCol() {
        if (cols > MAP_MIN_COL) {
            cols--;
            setMapDimension();
        }
    }

    private void removeRow() {
        if (rows < MAP_MAX_ROW) {
            rows--;
            setMapDimension();
        }
    }

    private void moveViewportLeft() {
        if (mapViewportX > 0) {
            mapViewportX -= 10;
            drawMiniMap();
            repaint();
        }
    }

    private void moveViewportRight() {
        if (mapViewportX + mapViewPortWidth < mapHeight) {
            mapViewportX += 10;
            drawMiniMap();
            repaint();
        }
    }

    private void moveViewportUp() {
        if (mapViewportY > 0) {
            mapViewportY -= 10;
            drawMiniMap();
            repaint();
        }
    }

    private void moveViewportDown() {
        if (mapViewportY + mapViewPortHeight < mapWidth) {
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

        if ((row != 0) && (mapMatrix[row - 1][col] == Cell.LAND || mapMatrix[row - 1][col] == Cell.TREE || mapMatrix[row - 1][col] == Cell.FARM)) {
            counter += 1;
        }
        if ((row != rows - 1) && (mapMatrix[row + 1][col] == Cell.LAND || mapMatrix[row + 1][col] == Cell.TREE || mapMatrix[row + 1][col] == Cell.FARM)) {
            counter += 4;
        }
        if ((col != 0) && (mapMatrix[row][col - 1] == Cell.LAND || mapMatrix[row][col - 1] == Cell.TREE || mapMatrix[row][col - 1] == Cell.FARM)) {
            counter += 8;
        }
        if ((col != cols - 1) && (mapMatrix[row][col + 1] == Cell.LAND || mapMatrix[row][col + 1] == Cell.TREE || mapMatrix[row][col + 1] == Cell.LAND)) {
            counter += 2;
        }
        return landImages[counter];
    }

    private void loadImages() {
        landImages = new Image[16];
        try {
            waterImage = ImageIO.read(new File("resource/image/tile/water.png"));
            landImages[0] = ImageIO.read(new File("resource/image/tile/no.png"));
            landImages[1] = ImageIO.read(new File("resource/image/tile/od.png"));
            landImages[2] = ImageIO.read(new File("resource/image/tile/or.png"));
            landImages[3] = ImageIO.read(new File("resource/image/tile/sld.png"));
            landImages[4] = ImageIO.read(new File("resource/image/tile/ou.png"));
            landImages[5] = ImageIO.read(new File("resource/image/tile/ocntrud.png"));
            landImages[6] = ImageIO.read(new File("resource/image/tile/slu.png"));
            landImages[7] = ImageIO.read(new File("resource/image/tile/sl.png"));
            landImages[8] = ImageIO.read(new File("resource/image/tile/ol.png"));
            landImages[9] = ImageIO.read(new File("resource/image/tile/srd.png"));
            landImages[10] = ImageIO.read(new File("resource/image/tile/ocntrlr.png"));
            landImages[11] = ImageIO.read(new File("resource/image/tile/sd.png"));
            landImages[12] = ImageIO.read(new File("resource/image/tile/sru.png"));
            landImages[13] = ImageIO.read(new File("resource/image/tile/sr.png"));
            landImages[14] = ImageIO.read(new File("resource/image/tile/su.png"));
            landImages[15] = ImageIO.read(new File("resource/image/tile/cntr.png"));

            treeImage = ImageIO.read(new File("resource/image/item/spring.png"));


            //Scaling images
            waterImage = waterImage.getScaledInstance(CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, Image.SCALE_DEFAULT);
            for (int i = 0; i < landImages.length; i++) {
                landImages[i] = landImages[i].getScaledInstance(CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, Image.SCALE_DEFAULT);
            }
            treeImage = treeImage.getScaledInstance(CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, Image.SCALE_DEFAULT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMapDimension() {
        mapWidth = cols * CELL_DEFAULT_WIDTH;
        mapHeight = rows * CELL_DEFAULT_HEIGHT;
        Cell[][] mapMatrixTemp = new Cell[rows][cols];
        int lastRows = mapMatrix.length;
        int lastCols = mapMatrix[0].length;
        if (lastCols > cols) {
            for (int i = 0; i < lastRows; i++) {
                for (int j = 0; j < cols; j++) {
                    //mapMatrixTemp  mapMatrix[i][j]
                }
            }
        }
        if (lastRows > rows) {
            for (int i = 0; i < lastRows; i++) {
                for (int j = 0; j < lastCols; j++) {

                }
            }
        }
        if (lastCols < cols) {

        }
        if (lastRows < rows) {

        }
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
        } else if (content == Cell.LAND) {
            image = getLandImage(row, col);
        } else if (content == Cell.TREE) {
            if (mapMatrix[row][col] == Cell.LAND) {
                image = treeImage;
                graphics.drawImage(image, col * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
                graphics1.drawImage(image, col * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT, CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
                repaint();
            }
            return;
        }

        mapMatrix[row][col] = content;

        // To drawing the image of the neighbour cells
        if ((col != 0) && (mapMatrix[row][col - 1] == Cell.LAND)) {
            graphics.drawImage(getLandImage(row, col - 1), (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row, col - 1), (col - 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((col != cols - 1) && (mapMatrix[row][col + 1] == Cell.LAND)) {
            graphics.drawImage(getLandImage(row, col + 1), (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row, col + 1), (col + 1) * CELL_DEFAULT_WIDTH, row * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((row != 0) && (mapMatrix[row - 1][col] == Cell.LAND)) {
            graphics.drawImage(getLandImage(row - 1, col), col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row - 1, col), col * CELL_DEFAULT_WIDTH, (row - 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
        }
        if ((row != rows - 1) && (mapMatrix[row + 1][col] == Cell.LAND)) {
            graphics.drawImage(getLandImage(row + 1, col), col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
                    CELL_DEFAULT_WIDTH, CELL_DEFAULT_HEIGHT, null);
            graphics1.drawImage(getLandImage(row + 1, col), col * CELL_DEFAULT_WIDTH, (row + 1) * CELL_DEFAULT_HEIGHT,
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