package kb;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

import java.lang.Math;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class maze extends JPanel {
	private final int NUM_IMAGES = 5;
    private final int CELL_SIZE = 15;

    private final int TILES = 20;
    private final int N_ROWS = 16;
    private final int N_COLS = 16;
    
    private final int EMPTY = 0;
    private final int TILE = 1;
    private final int START = 2;
    private final int END = 3;
    private final int PATH = 4;

    private final int BOARD_WIDTH = 500;
    private final int BOARD_HEIGHT = 241;

    private int[] field;
    private int tileLeft;
    private int score;
    private Image[] img;

    private int allCells;
    private final JLabel statusBar;
    private final JLabel northBar;
    
    private int startX;
    private int startY;
    private int finalX;
    private int finalY;

    public maze(JLabel statusBar,JLabel northBar) {
        this.statusBar = statusBar;
        this.northBar = northBar;
        initBoard();
    }

    private void initBoard() {

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        img = new Image[NUM_IMAGES];

        for (int i = 0; i < NUM_IMAGES; i++) {
            var path = "src/kb/resources/" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }

        addMouseListener(new MinesAdapter());
        newGame();
    }

    private void newGame() {
        
        tileLeft = TILES;
        score = 0;
        
        allCells = N_ROWS * N_COLS;
        field = new int[allCells];

        for (int i = 0; i < allCells; i++) {
            field[i] = EMPTY;
        }
        
        startX = ThreadLocalRandom.current().nextInt(0, N_ROWS);
        startY = ThreadLocalRandom.current().nextInt(0, N_COLS);
        finalX = ThreadLocalRandom.current().nextInt(0, N_ROWS);
        finalY = ThreadLocalRandom.current().nextInt(0, N_COLS);
        
        while(Math.abs(startX - finalX) + Math.abs(startY - finalY) < 10) {
        	startX = ThreadLocalRandom.current().nextInt(0, N_ROWS);
            startY = ThreadLocalRandom.current().nextInt(0, N_COLS);
            finalX = ThreadLocalRandom.current().nextInt(0, N_ROWS);
            finalY = ThreadLocalRandom.current().nextInt(0, N_COLS);
        }
        
        field[(startX * N_COLS) + startY] = START;
        field[(finalX * N_COLS) + finalY] = END;

        statusBar.setText(" ");
        northBar.setText(" ");
    }
    
    private void AStar() {
        Node start = new Node(startX, startY);
        Node end = new Node(finalX, finalY);

        ArrayList<Node> res = AStarHelper(start, end);

        System.out.println("size " + res.size());

        for (Node n : res) {

            if (n.getX() == startX && n.getY() == startY) {
                continue;
            }
            if (n.getX() == finalX && n.getY() == finalY) {
                continue;
            }

            field[(n.getX() * N_COLS) + n.getY()] = PATH;
            System.out.println(n.getX() + ", " + n.getY());
        }
    }

    private boolean isValid(int x, int y) {
        if (x < 0 || y < 0 || x >= N_ROWS || y >= N_COLS) {
            return false;
        }
        int tile = field[(x * N_COLS) + y];
        if (tile == EMPTY || tile == END) {
            return true;
        }
        return false;
    }
    private boolean isDestination(int x, int y) {
        if (x == finalX && y == finalY) {
            return true;
        }
        return false;
    }
    private int calculateH(int x, int y) {
        int H = Math.abs(x-finalX) + Math.abs(y-finalY);

        return H;
    }

    private ArrayList<Node> AStarHelper(Node start, Node end) {
        ArrayList<Node> empty = new ArrayList<Node>();

        boolean[][] closedList = new boolean[N_ROWS][N_COLS];

        Node[][] allMap = new Node[N_ROWS][N_COLS];

        for (int x = 0; x < N_ROWS; x++) {
            for (int y = 0; y < N_COLS; y++) {
                allMap[x][y] = new Node(x, y);

                closedList[x][y] = false;
            }
        }

        int x = start.getX();
        int y = start.getY();

        allMap[x][y].setFCost(0);
        allMap[x][y].setGCost(0);
        allMap[x][y].setHCost(0);
        allMap[x][y].setParentX(x);
        allMap[x][y].setParentY(y);

        PriorityQueue<Node> openList = new PriorityQueue<Node>(300,new NodeComparator());
        openList.add(allMap[x][y]);

        boolean destinationFound = false;

        while (!openList.isEmpty() && openList.size() < N_COLS * N_ROWS) {
            Node node = openList.poll();
            
            x = node.getX();
            y = node.getY();

            System.out.println("processed : " + Integer.toString(x) + " " +
            					Integer.toString(y)+ " " +
            					Float.toString(node.getFCost()));
            
            closedList[x][y] = true;

            for (int newX = -1; newX <= 1; newX++) {
                for (int newY = -1; newY <= 1; newY++) {
                	if (newX == newY) {
                		continue;
                	}
                	if((newX == 1 && newY == -1) ||
                			(newX == -1 && newY == 1)) {
                		continue;
                	}
                    int gNew, hNew, fNew;

                    if (isValid(x + newX, y + newY)) {
                        if (isDestination(x + newX, y + newY)) {
                            allMap[x + newX][y + newY].setParentX(x);
                            allMap[x + newX][y + newY].setParentY(y);
                            destinationFound = true;
                            return makePath(allMap);
                        }
                        else if (!closedList[x + newX][y + newY]) {
                            gNew = node.getGCost() + 1;
                            hNew = calculateH(x + newX, y + newY);
                            fNew = gNew + hNew;

                            if (allMap[x + newX][y + newY].getFCost() == Integer.MAX_VALUE ||
                                    allMap[x + newX][y + newY].getFCost() > fNew)
                            {
                            	System.out.println("added : " + Integer.toString(x + newX) + " "
                            						+ Integer.toString(y + newY) + " " + 
                            							Integer.toString(fNew));
                                allMap[x + newX][y + newY].setFCost(fNew);
                                allMap[x + newX][y + newY].setGCost(gNew);
                                allMap[x + newX][y + newY].setHCost(hNew);
                                allMap[x + newX][y + newY].setParentX(x);
                                allMap[x + newX][y + newY].setParentY(y);
                                openList.add(allMap[x + newX][y + newY]);
                            }
                        }
                    }
                }
            }
        }
        return empty;
    }

    private ArrayList<Node> makePath(Node[][] map) {
        System.out.println("in make path");

        int x = finalX;
        int y = finalY;
        Stack<Node> path = new Stack<Node>();
        ArrayList<Node> useablePath = new ArrayList<Node>();

        while (!(map[x][y].getParentX() == x && map[x][y].getParentY() == y)
                && map[x][y].getX() != -1 && map[x][y].getY() != -1) {
            path.push(map[x][y]);
            int tempX = map[x][y].getParentX();
            int tempY = map[x][y].getParentY();
            x = tempX;
            y = tempY;
        }
        path.push(map[x][y]);

        while (!path.isEmpty()) {
            Node top = path.peek();
            path.pop();
            useablePath.add(top);
        }
        return useablePath;
    }

    private void reset() {
    	tileLeft = TILES;
    	allCells = N_ROWS * N_COLS;
    	
    	for (int i = 0; i < allCells; i++) {
            field[i] = EMPTY;
        }
    	
    	field[(startX * N_COLS) + startY] = START;
        field[(finalX * N_COLS) + finalY] = END;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0,0,BOARD_WIDTH,BOARD_HEIGHT);
        g.setColor(java.awt.Color.black);
        
        g.drawString("Tiles left : " + Integer.toString(tileLeft), 20,40);
        g.drawString("Start", 20,120);
        g.drawString("Score : " + Integer.toString(score), 20,80);
        g.drawString("Reset", 20, 160);
        g.drawString("New Game", 20, 200);
        
        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {
                int cell = field[(i * N_COLS) + j];
                g.drawImage(img[cell], (j * CELL_SIZE + 240),
                        (i * CELL_SIZE ), this);
            }
        }
    }
    
    private class MinesAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            
            boolean doRepaint = false;
            
            if(x > 20 && x < 20+75) {
            	if( y > 120-10 && y < 120+10) {
            		AStar();
            		doRepaint = true;
            	}
            	if( y > 160-10 && y < 160+10) {
            		reset();
            		doRepaint = true;
            	}
            	if( y > 200-10 && y < 200+100) {
            		newGame();
            		doRepaint = true;
            	}
            }
            if(x - 240 > 0) {
            	x = x-240;
            	int cCol = x / CELL_SIZE;
                int cRow = y / CELL_SIZE;

                if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                    	
                    	
                    } else {
                    	if (field[(cRow * N_COLS) + cCol] == EMPTY) {
                    		if(tileLeft>0) {
                    			field[(cRow * N_COLS) + cCol] = TILE;
                        		doRepaint = true;
                    			tileLeft--;
                    		}
                        }
                    	else if(field[(cRow * N_COLS) + cCol] == TILE) {
                    		field[(cRow * N_COLS) + cCol] = EMPTY;
                    		doRepaint = true;
                    		tileLeft++;
                    	}
                    }
                }
            }  
            
            if (doRepaint) {
                repaint();
            }
        }
    }
}
