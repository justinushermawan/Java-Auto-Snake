/**
 * Created by Justinus_JW on 10/01/2016.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Main extends JFrame implements ActionListener {

    /**
     * CONSTANTS
     */
    private final int FRAME_WIDTH = 1042;
    private final int FRAME_HEIGHT = 584;
    private final double GAME_HEARTZ = 30.0;
    private final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HEARTZ;
    private final int MAX_UPDATES_BEFORE_RENDER = 5;
    private final double TARGET_FPS = 60;
    private final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

    private Border btnStartBorder = new LineBorder(new Color(213, 15, 37), 2);
    private Border btnPauseBorder = new LineBorder(new Color(238, 178, 17), 2);
    private Border btnQuitBorder = new LineBorder(new Color(0, 153, 37), 2);

    private JButton startButton = new JButton("Start");
    private JButton quitButton = new JButton("Quit");
    private JButton pauseButton = new JButton("Pause");
    private boolean running = false;
    private boolean paused = false;

    private GamePanel gamePanel = new GamePanel();

    public Main() {
        super("Auto-Snake");

        startButton.setBackground(Color.WHITE);
        startButton.setForeground(Color.BLACK);
        startButton.setBorder(btnStartBorder);

        pauseButton.setBackground(Color.WHITE);
        pauseButton.setForeground(Color.BLACK);
        pauseButton.setBorder(btnPauseBorder);

        quitButton.setBackground(Color.WHITE);
        quitButton.setForeground(Color.BLACK);
        quitButton.setBorder(btnQuitBorder);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 2));
        p.add(startButton);
        p.add(pauseButton);
        p.add(quitButton);
        c.add(gamePanel, BorderLayout.CENTER);
        c.add(p, BorderLayout.SOUTH);

        startButton.addActionListener(this);
        pauseButton.addActionListener(this);
        quitButton.addActionListener(this);

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }

    public void actionPerformed(ActionEvent e) {
        Object s = e.getSource();
        if (s == startButton) {
            running = !running;
            if (running) {
                startButton.setText("Stop");
                runGameLoop();
            } else {
                startButton.setText("Start");
            }
        } else if (s == pauseButton) {
            paused = !paused;
            if (paused) {
                pauseButton.setText("Resume");
            } else {
                pauseButton.setText("Pause");
            }
        } else if (s == quitButton) {
            System.exit(0);
        }
    }

    public void runGameLoop() {
        Thread loop = new Thread()
        {
            public void run() {
                gameLoop();
            }
        };
        loop.start();
    }

    private void gameLoop() {
        double lastUpdateTime = System.nanoTime();
        double lastRenderTime = System.nanoTime();
        int lastSecondTime = (int) (lastUpdateTime / 1000000000);
        while (running) {
            double now = System.nanoTime();
            int updateCount = 0;
            if (!paused) {
                while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
                    lastUpdateTime += TIME_BETWEEN_UPDATES;
                    updateCount++;
                }
                if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
                    lastUpdateTime = now - TIME_BETWEEN_UPDATES;
                }
                float interpolation = Math.min(1.0f, (float)((now - lastUpdateTime) / TIME_BETWEEN_UPDATES));
                drawGame(interpolation);
                lastRenderTime = now;
                int thisSecond = (int) (lastUpdateTime / 1000000000);
                if (thisSecond > lastSecondTime) {
                    updateGame();
                    System.out.println("NEW SECOND " + thisSecond + " " + gamePanel.getFrameCount());
                    gamePanel.setFPS(gamePanel.getFrameCount());
                    gamePanel.setFrameCount(0);
                    lastSecondTime = thisSecond;
                }
                while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                    Thread.yield();
                    try {
                        Thread.sleep(30);
                    } catch (Exception e) {}
                    now = System.nanoTime();
                }
            }
        }
    }

    private void updateGame() {
        gamePanel.update();
    }

    private void drawGame(float interpolation) {
        gamePanel.setInterpolation(interpolation);
        gamePanel.repaint();
    }

}