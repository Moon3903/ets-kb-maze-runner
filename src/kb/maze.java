package kb;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class maze extends JPanel {
	private final int NUM_IMAGES = 13;
    private final int CELL_SIZE = 15;

    private final int TILES = 20;
    private final int N_ROWS = 16;
    private final int N_COLS = 16;
    
    private final int EMPTY = 0;
    private final int TILE = 10;
    private final int FLAG = 11;
    private final int PATH = 12;

    private final int BOARD_WIDTH = 500;
    private final int BOARD_HEIGHT = 241;

    private int[] field;
    private int tileLeft;
    private int score;
    private Image[] img;

    private int allCells;
    private final JLabel statusbar;
    private final JLabel northbar;
    
    private int startx;
    private int starty;
    private int finalx;
    private int finaly;

    public maze(JLabel statusbar,JLabel northbar) {

        this.statusbar = statusbar;
        this.northbar = northbar;
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
        
        startx = ThreadLocalRandom.current().nextInt(0, N_ROWS);
        starty = ThreadLocalRandom.current().nextInt(0, N_COLS);
        finalx = ThreadLocalRandom.current().nextInt(0, N_ROWS);
        finaly = ThreadLocalRandom.current().nextInt(0, N_COLS);
        
        while(Math.abs(startx-finalx) + Math.abs(starty-finaly) < 10) {
        	startx = ThreadLocalRandom.current().nextInt(0, N_ROWS);
            starty = ThreadLocalRandom.current().nextInt(0, N_COLS);
            finalx = ThreadLocalRandom.current().nextInt(0, N_ROWS);
            finaly = ThreadLocalRandom.current().nextInt(0, N_COLS);
        }
        
        field[(startx * N_COLS) + starty] = FLAG;
        field[(finalx * N_COLS) + finaly] = FLAG;

        statusbar.setText(" ");
        northbar.setText(" ");
     
    }
    private void reset() {
    	
    	tileLeft = TILES;
    	
    	allCells = N_ROWS * N_COLS;
    	
    	for (int i = 0; i < allCells; i++) {
            field[i] = EMPTY;
        }
    	
    	field[(startx * N_COLS) + starty] = FLAG;
        field[(finalx * N_COLS) + finaly] = FLAG;
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
            		//start A*
            		System.out.println("start\n");
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
