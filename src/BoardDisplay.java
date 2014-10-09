import java.awt.Graphics;

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
	
	public void update(Board b){
		this.board = b;
		repaint();
	}
	
	/**
	 * This method overrides the JComponent paintComponent method and will be called every time java refreshes the frame
	 * The method repaint() also schedules a call to this method.
	 */
	public void paintComponent(Graphics g){
		//TODO painting instructions for the board
	}
}
