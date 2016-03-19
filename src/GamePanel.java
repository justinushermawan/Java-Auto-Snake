/**
 * Created by Justinus_JW on 10/01/2016.
 */
import java.util.*;
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;

public class GamePanel extends JPanel {

    private class Body {
        Integer x;
        Integer y;
        Body next, prev;
        int oldX, oldY;
        public Body(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }
    }

    private class Vertex {
        Integer x;
        Integer y;
        Vertex parent;
        public Vertex(Integer x, Integer y, Vertex parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
        }
    }

    private float interpolation;
    private int fps = 60;
    private int frameCount = 0;

    private int map[][] = new int[][] {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    };

    private Random rand;
    private ArrayList<Vertex> path;
    private ArrayList<Vertex> queue;
    private Body head;
    private Body tail;
    private boolean visited[][];
    private boolean notFinished = true;
    private Vertex finishVertex = null;
    private Vertex foodtemp;
    private final int TILE_WIDTH = 25;
    private final int TILE_HEIGHT = 25;
    private int foodeaten = 0;

    private BufferedImage landImage, wallImage, headImage, bodyImage, foodImage;

    public GamePanel() {

        File landFileImage = new File("images/land.png");
        try {
            landImage = ImageIO.read(landFileImage);
        } catch (IOException e) { }

        File wallFileImage = new File("images/wall.png");
        try {
            wallImage = ImageIO.read(wallFileImage);
        } catch (IOException e) { }

        File headFileImage = new File("images/head.png");
        try {
            headImage = ImageIO.read(headFileImage);
        } catch (IOException e) { }

        File bodyFileImage = new File("images/body.png");
        try {
            bodyImage = ImageIO.read(bodyFileImage);
        } catch (IOException e) { }

        File foodFileImage = new File("images/food.png");
        try {
            foodImage = ImageIO.read(foodFileImage);
        } catch (IOException e) { }

        rand = new Random();
        head = new Body(12, 28);
        tail = new Body(12, 29);
        head.next = tail;
        tail.prev = head;
        head.prev = null;
        tail.next = null;
    }

    public void setFPS(int f) {
        fps = f;
    }

    public int getFPS() { return fps; }

    public void setFrameCount(int fc) {
        frameCount = fc;
    }

    public int getFrameCount() { return frameCount; }

    public void setInterpolation(float i) {
        interpolation = i;
    }

    private int getFoodEaten() {
        int body_length = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 2) {
                    body_length++;
                }
            }
        }
        return body_length - 2;
    }

    private void walk() {
        Collections.reverse(path);
        for (int i = 1; i < path.size(); i++) {
            Body curr = head;
            while (curr != null) {
                if (curr == head) {
                    Vertex v = path.get(i);
                    curr.oldX = curr.x;
                    curr.oldY = curr.y;
                    curr.x = v.x;
                    curr.y = v.y;
                } else {
                    curr.oldX = curr.x;
                    curr.oldY = curr.y;
                    curr.x = curr.prev.oldX;
                    curr.y = curr.prev.oldY;
                }
                swap(new Vertex(curr.oldX, curr.oldY, null), new Vertex(curr.x, curr.y, null));
                curr = curr.next;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            repaint();
        }
    }

    private void swap(Vertex _old, Vertex _new) {
        int temp = map[_old.x][_old.y];
        map[_old.x][_old.y] = map[_new.x][_new.y];
        map[_new.x][_new.y] = temp;
    }

    private void pushChild() {
        Body body = null;
        if (map[tail.x - 1][tail.y] == 0) {
            body = new Body(tail.x - 1, tail.y);
        } else if (map[tail.x + 1][tail.y] == 0) {
            body = new Body(tail.x + 1, tail.y);
        } else if (map[tail.x][tail.y - 1] == 0) {
            body = new Body(tail.x, tail.y - 1);
        } else if (map[tail.x][tail.y + 1] == 0) {
            body = new Body(tail.x, tail.y + 1);
        } else {
            System.exit(0);
        }
        tail.next = body;
        body.prev = tail;
        tail = body;
        map[body.x][body.y] = 2;
    }

    private void clearFood() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 3) {
                    map[i][j] = 0;
                }
            }
        }
    }

    private void backtrack() {
        Vertex curr = finishVertex;
        while (curr != null) {
            path.add(curr);
            curr = curr.parent;
        }
    }

    private void open(Vertex open) {
        if (notFinished) {
            if (map[open.x][open.y] != 0 && map[open.x][open.y] != 3 || visited[open.x][open.y]) {
                return;
            } else {
                visited[open.x][open.y] = true;
                queue.add(open);
                if (map[open.x][open.y] == 3) {
                    notFinished = false;
                    finishVertex = open;
                    return;
                }
            }
        }
    }

    private void generateFood() {
        int x;
        int y;
        while (true) {
            x = rand.nextInt(map.length);
            y = rand.nextInt(map[0].length);
            if (map[x][y] == 0 && map[x + 1][y] != 2 && map[x - 1][y] != 2 && map[x][y + 1] != 2 && map[x][y - 1] != 2) {
                break;
            }
        }
        map[x][y] = 3;
        foodtemp = new Vertex(x, y, null);
    }

    private void printMap(Graphics2D g2d) {
        // Draw Map
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 0) {
                    g2d.drawImage(landImage, j * TILE_WIDTH, i * TILE_WIDTH, TILE_WIDTH, TILE_HEIGHT, null);
                } else if (map[i][j] == 1) {
                    g2d.drawImage(wallImage, j * TILE_WIDTH, i * TILE_WIDTH, TILE_WIDTH, TILE_HEIGHT, null);
                } else if (map[i][j] == 2) {
                    g2d.drawImage(landImage, j * TILE_WIDTH, i * TILE_WIDTH, TILE_WIDTH, TILE_HEIGHT, null);
                    if (j == head.y && i == head.x) {
                        int diffy=head.y-head.next.y;
                        int diffx=head.x-head.next.x;
                        if(diffy == -1) { // Left
                            g2d.drawImage(headImage, j * TILE_WIDTH, i * TILE_WIDTH, TILE_WIDTH, TILE_HEIGHT, null);
                        } else if(diffy == 1) { // Right
                            AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(180), headImage.getWidth() / 2, headImage.getHeight() / 2);
                            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                            g2d.drawImage(op.filter(headImage, null), j * TILE_WIDTH, i * TILE_WIDTH, TILE_WIDTH, TILE_HEIGHT, null);
                        } else if(diffx == -1) { // Top
                            AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(90), headImage.getWidth() / 2, headImage.getHeight() / 2);
                            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                            g2d.drawImage(op.filter(headImage, null), j * TILE_WIDTH, i * TILE_WIDTH, TILE_WIDTH, TILE_HEIGHT, null);
                        } else { // Bottom
                            AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(270), headImage.getWidth() / 2, headImage.getHeight() / 2);
                            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                            g2d.drawImage(op.filter(headImage, null), j * TILE_WIDTH, i * TILE_WIDTH, TILE_WIDTH, TILE_HEIGHT, null);
                        }
                    } else {
                        g2d.drawImage(bodyImage, j * TILE_WIDTH, i * TILE_WIDTH, TILE_WIDTH, TILE_HEIGHT, null);
                    }
                }
                if(foodtemp != null) {
                    if(foodtemp.x == i && foodtemp.y == j) {
                        g2d.drawImage(foodImage, j * TILE_WIDTH, i * TILE_WIDTH, TILE_WIDTH, TILE_HEIGHT, null);
                    }
                }
            }
        }

        // Draw Food Eaten HUD
        g2d.setFont(new Font("Arial", Font.BOLD, 50));
        g2d.drawString("" + getFoodEaten(), 30, getHeight() - 30);
    }

    private void findPath() {
        try {
            queue = new ArrayList<Vertex>();
            path = new ArrayList<Vertex>();
            visited = new boolean[map.length][map[0].length];
            Vertex h = new Vertex(head.x, head.y, null);
            queue.add(h);
            notFinished = true;
            finishVertex = null;
            while (notFinished) {
                Vertex temp = queue.get(0);
                open(new Vertex(temp.x, temp.y - 1, temp));
                open(new Vertex(temp.x - 1, temp.y, temp));
                open(new Vertex(temp.x, temp.y + 1, temp));
                open(new Vertex(temp.x + 1, temp.y, temp));
                queue.remove(0);
            }
            backtrack();
        } catch (Exception e) {
            System.out.println("Path is closed.");
            System.exit(0);
        }
    }

    public void update() {
        generateFood();
        findPath();
        clearFood();
        walk();
        pushChild();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        printMap(g2d);
    }

}