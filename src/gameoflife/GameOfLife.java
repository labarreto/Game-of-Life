package gameoflife;

import acm.program.*;
import acm.graphics.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GameOfLife extends GraphicsProgram {

    private static final int WORLD_SIZE = 50;
    private static final int CELL_SIZE = 16;
    private static final int CELL_PADDING = 2;
    private static final int SPARSITY = 5;
    private static final int OFFSET = 50;
    private static final int DELAY = 64;
    private static final Color COLOR0 = Color.WHITE;
    private static final Color COLOR1 = Color.RED;
    private static final Color COLOR2 = Color.GREEN;
    private static final Color COLOR3 = Color.BLUE;
    private static final Color COLOR4 = Color.YELLOW;
    private static GRect background;
    private static GOval[][] world;
    private static boolean[][] alive;
    private static boolean running = false;
    private static boolean exit = false;

    public void run() {
        addMouseListeners();
        setUpButtons();
        setUpBackground();
        setUpWorld();
        while (!exit) {
            if (running) {
                nextGeneration();
            }
            pause(DELAY);
        }
        System.exit(0);
    }

    protected void setUpButtons() {
        add(new JButton("Randomize"), NORTH);
        add(new JButton("Clear"), NORTH);
        add(new JButton("Next"), NORTH);
        add(new JButton("Start"), NORTH);
        add(new JButton("Stop"), NORTH);
        add(new JButton("Exit"), NORTH);
        addActionListeners();
    }

    protected void setUpBackground() {
        int backgroundDimension = CELL_SIZE * WORLD_SIZE;
        background = new GRect(OFFSET, OFFSET,
                backgroundDimension, backgroundDimension);
        background.setColor(Color.BLACK);
        background.setFilled(true);
        add(background);
    }

    protected void setUpWorld() {
        world = new GOval[WORLD_SIZE][WORLD_SIZE];
        alive = new boolean[WORLD_SIZE][WORLD_SIZE];
        for (int i = 0; i < WORLD_SIZE; i++) {
            for (int j = 0; j < WORLD_SIZE; j++) {
                world[i][j] = newCell(i, j);
            }
        }
        for (int i = 0; i < WORLD_SIZE; i++) {
            for (int j = 0; j < WORLD_SIZE; j++) {
                alive[i][j] = false;
            }
        }
    }

    protected GOval newCell(int i, int j) {
        GOval cell = new GOval(OFFSET + i * CELL_SIZE + CELL_PADDING,
                OFFSET + j * CELL_SIZE + CELL_PADDING,
                CELL_SIZE - 2 * CELL_PADDING,
                CELL_SIZE - 2 * CELL_PADDING);
        cell.setColor(Color.BLACK);
        cell.setFilled(true);
        add(cell);
        return cell;
    }

    protected void randomize() {
        for (int i = 0; i < WORLD_SIZE; i++) {
            for (int j = 0; j < WORLD_SIZE; j++) {
                alive[i][j] = Math.random() < (1.0 / SPARSITY);
            }
        }
        updateColors();
    }

    protected void nextGeneration() {
        updateLife();
        updateColors();
    }

    protected void updateLife() {
        boolean space[][] = new boolean[WORLD_SIZE][WORLD_SIZE];
        for (int i = 0; i < WORLD_SIZE; i++) {
            for (int j = 0; j < WORLD_SIZE; j++) {
                if (alive[i][j] == true && numNeighbors(i, j) == 2 || numNeighbors(i, j) == 3) {
                    space[i][j] = true;
                } else if (alive[i][j] == false && numNeighbors(i, j) == 3) {
                    space[i][j] = true;
                } else {
                    space[i][j] = false;
                }
            }
        }
        alive = space;
    }

    protected int numNeighbors(int i, int j) {
        int yay = 0;
        int n = WORLD_SIZE;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <=1; dc++) {
                if (alive [ (i + dr + n) % n][ (j + dc + n) % n] == true && (dr != 0 || dc != 0)) {
                    yay++;
                }
            }
        }
        return yay;
    }


    protected void updateColors() {
                for (int i = 0; i < WORLD_SIZE; i++) {
            for (int j = 0; j < WORLD_SIZE; j++) {
                if (alive[i][j] == false) {
                    world[i][j].setColor(Color.BLACK);
                } else if (numNeighbors(i, j) == 0) {
                    world[i][j].setColor(COLOR0);
                } else if (numNeighbors(i, j) == 1) {
                    world[i][j].setColor(COLOR1);
                } else if (numNeighbors(i, j) == 2) {
                    world[i][j].setColor(COLOR2);
                } else if (numNeighbors(i, j) == 3) {
                    world[i][j].setColor(COLOR3);
                } else if (numNeighbors(i, j) == 4) {
                    world[i][j].setColor(COLOR4);
                }
            }
        }
    
    }

    protected void clear() {
        for (int i = 0; i < WORLD_SIZE; i++) {
            for (int j = 0; j < WORLD_SIZE; j++) {
                alive[i][j] = false;
            }
        }
        updateColors();
    }


    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int i = (x - OFFSET) / CELL_SIZE;
        int j = (y - OFFSET) / CELL_SIZE;
        alive[i][j] = !alive[i][j];
        updateColors();
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == "Randomize") {
            randomize();
        }
        if (cmd == "Clear") {
            clear();
        } else if (cmd == "Next") {
            nextGeneration();
        } else if (cmd == "Start") {
            running = true;
        } else if (cmd == "Stop") {
            running = false;
        } else if (cmd == "Exit") {
            exit = true;
        }
    }

    public static void main(String[] args) {
        new GameOfLife().start();
    }

}
