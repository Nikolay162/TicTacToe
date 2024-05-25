package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Controller {
    private static final int WIDTH = 5;
    private static final int HEIGHT = 5;
    private static final int SQUARE_SIZE = 100;
    private static final int WIDTH_FIELD = SQUARE_SIZE * WIDTH;
    private static final int HEIGHT_FIELD = SQUARE_SIZE * HEIGHT;
    private static final int OFFSET = 20;
    private static final int WIN_VALUE = 3;

    private View view;
    private Graphics graphics;
    private Shape[][] shapes = new Shape[WIDTH][HEIGHT];
    private boolean isCrossTurn = true;
    private Set<Point> points = new HashSet<>();


    public void start() {
        view.create(WIDTH_FIELD, HEIGHT_FIELD);
        renderFrame();
    }

    public void setView(View view) {
        this.view = view;
    }

    private void renderFrame() {
        BufferedImage bufferedImage = new BufferedImage(WIDTH_FIELD, HEIGHT_FIELD, BufferedImage.TYPE_INT_RGB);
        graphics = bufferedImage.getGraphics();
        drawField();
        drawShapes();
        view.setImage(bufferedImage);

    }

    private BufferedImage createSquare(boolean isWin) {
        BufferedImage image = new BufferedImage(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        if (isWin) {
            graphics.setColor(Color.GREEN);
        }
        graphics.drawRect(0, 0, SQUARE_SIZE - 1, SQUARE_SIZE - 1);
        return image;
    }

    private void draw(BufferedImage image, int x, int y) {
        graphics.drawImage(image, x * SQUARE_SIZE, y * SQUARE_SIZE, null);
    }

    private void drawField() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                draw(createSquare(points.contains(new Point(x, y))), x, y);
            }
        }
    }

    public void handleMousePress(int mouseX, int mouseY) {
        if (!points.isEmpty()) {
            return;
        }
        int x = mouseX / SQUARE_SIZE;
        int y = mouseY / SQUARE_SIZE;
        if (shapes[x][y] != null) {
            return;
        }
        shapes[x][y] = isCrossTurn ? Shape.CROSS : Shape.CIRCLE;
        isCrossTurn = !isCrossTurn;
        check();
        renderFrame();
    }

    private BufferedImage createCross() {
        BufferedImage image = new BufferedImage(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        graphics.drawLine(OFFSET, OFFSET, SQUARE_SIZE - OFFSET, SQUARE_SIZE - OFFSET);
        graphics.drawLine(SQUARE_SIZE - OFFSET, OFFSET, OFFSET, SQUARE_SIZE - OFFSET);
        return image;
    }

    private BufferedImage createCircle() {
        BufferedImage image = new BufferedImage(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        graphics.drawOval(OFFSET, OFFSET, SQUARE_SIZE - OFFSET * 2, SQUARE_SIZE - OFFSET * 2);
        return image;
    }

    private void drawShapes() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (shapes[x][y] == Shape.CIRCLE) {
                    draw(createCircle(), x, y);
                } else if (shapes[x][y] == Shape.CROSS) {
                    draw(createCross(), x, y);
                }
            }
        }
    }

    private void check() {
        for (int x = 0; x <= WIDTH - WIN_VALUE; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                lineWin(x, y, 1, 0);
            }
        }
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y <= HEIGHT - WIN_VALUE; y++) {
                lineWin(x, y, 0, 1);
            }
        }
        for (int x = 0; x <= WIDTH - WIN_VALUE; x++) {
            for (int y = 0; y <= HEIGHT - WIN_VALUE; y++) {
                lineWin(x, y, 1, 1);
            }
        }
        for (int x = 0; x <= WIDTH - WIN_VALUE; x++) {
            for (int y = HEIGHT - 1; y >= WIN_VALUE - 1; y--) {
                 lineWin(x, y, 1, -1);
            }
        }
    }

    private void lineWin(int x, int y, int dx, int dy) {
        Shape shape = shapes[x][y];
        if (shape == null) {
            return;
        }
        for (int i = 1; i < WIN_VALUE; i++) {
            if (shapes[x + dx * i][y + dy * i] != shape) {
                return;
            }
        }
        for (int i = 0; i < WIN_VALUE; i++) {
            Point point = new Point(x + dx * i, y + dy * i);
            points.add(point);
        }
    }
}
