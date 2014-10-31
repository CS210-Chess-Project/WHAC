import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * Class to help display the game board.
 * @author Zach
 *
 */
@SuppressWarnings("serial")
public class BoardDisplay extends JPanel {
	public boolean makeAIMove = false;
	private Board board;
	private ArrayList<Move> highlightedMoves = new ArrayList<Move>();

	/**
	 * Constructor mostly for test boards
	 * @param b the board to represent
	 */
	public BoardDisplay(Board b){
		this.board = b;
	}

	/**
	 * Constructor that should be used
	 * @param width the width that the board with occupy
	 * @param height the height that the board with occupy
	 * @param b the board to represent
	 */
	public BoardDisplay(int width, int height, Board b){
		super();
		this.setSize(width, height);
		this.setPreferredSize(new Dimension(width, height));
		this.board = b;
	}

	/**
	 * This method overrides the JComponent paintComponent method and will be called every time java refreshes the frame
	 * The method repaint() also schedules a call to this method.
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D myG = (Graphics2D)g;
		//paint grid:
		myG.setColor(Color.BLACK);
		int xSpacing = this.getWidth()/5;
		int ySpacing = this.getHeight()/5;

		//paint each piece and the BG
		Piece[][] boardArray = board.getBoardArray();
		for(int y = 0; y < 5; y++){
			for (int x = 0; x < 5; x++){
				Rectangle2D.Double thisBGCell = new Rectangle2D.Double(y*xSpacing,x*ySpacing, xSpacing, ySpacing);
				if((x+y)%2 == 0){ //even spaces are dark
					myG.setColor(new Color(139,69,19)); //dark brown
				}
				else{
					myG.setColor(new Color(255,228,196)); //light brown
				}
				myG.fill(thisBGCell);

				boardArray[x][y].paintSelf(this.getWidth()/5, this.getHeight()/5, myG); //paint each piece
			}
		}
		
		//paint highlighted cells if there are any
		myG.setColor(new Color(0,255,0,125));
		for(Move move:highlightedMoves){
			
			Rectangle2D.Double thisBGCell = new Rectangle2D.Double(move.getCol()*xSpacing,move.getRow()*ySpacing, xSpacing, ySpacing);
			myG.fill(thisBGCell);
		}
		
	}

	//Getters/Setters and Utilities
	public Board getBoard(){
		return this.board;

	}
	
	public ArrayList<Move> getHighlightedMoves(){
		return this.highlightedMoves;
	}
	
	public void setHighlightedMoves(ArrayList<Move> moves){
		this.highlightedMoves  = moves;
	}

	public void setBoard(Board b) {
		this.board = b;
	}
}
