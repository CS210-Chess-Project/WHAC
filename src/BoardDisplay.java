import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

/**
 * Class to help display our board.  I figured we'll be using Swing/AWT so I had it extend a JPanel to make things easy.
 * @author Zach
 *
 */
public class BoardDisplay extends JPanel {
	private Board board;

	public BoardDisplay(Board b){
		this.board = b;
	}

	public BoardDisplay(int width, int height, Board b){
		super();
		this.setSize(width, height);
		this.setPreferredSize(new Dimension(width, height));
		this.board = b;
	}

	public void update(Board b){
		this.board = b;
		repaint();
	}

	/**
	 * This method overrides the JComponent paintComponent method and will be called every time java refreshes the frame
	 * The method repaint() also schedules a call to this method.
	 */
	public void paintComponent(Graphics g){
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
	}

	public Board getBoard(){
		return this.board;

	}
}
