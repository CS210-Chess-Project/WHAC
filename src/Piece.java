import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Superclass of anything that can occupy a grid on the board, including empty spaces
 * Implemented as an abstract class to that subclasses are forced to implement custom methods for generateAvailableMoves()
 * We will never need an instance of Piece, just instances of the subclasses
 * @author Zach
 *
 */
public abstract class Piece {
	protected int[] location; //the 2D location of the piece on the board.
	protected ArrayList<Move> availableMoves = new ArrayList<Move>();
	//protected ArrayList<Move> captureMoves= new ArrayList<Move>(); decided against this
	protected boolean alignment;  //True = WHITE; false=BLACK
	protected BufferedImage image; //image to render when piece is drawn
	protected Board parentBoard;
	
	public Piece(int[] location){
		this.location = location;
		loadImage();
	}
	
	public Piece(int[] location, boolean alignment, Board parentBd){
		this.location = location;
		this.alignment = alignment;
		this.parentBoard = parentBd;
		loadImage();		
	}
	
	
	//generates list of available moves and returns the list.  We generate each time to make sure we get an up-to-date list
	public ArrayList<Move> getAvailableMoves(){
		this.generateAvailableMoves();
		return availableMoves;
	}

	/**
	 * This method is implemented in subclasses and should always cause the field availableMoves to be instantiated
	 * Remember not to ignore non-capture moves if capture moves are available
	 * It will be called anytime the list of available moves needs to be updated or retrieved
	 */
	protected abstract void generateAvailableMoves();
	
	/**
	 * Painting instructions for the GUI;  Paints onto the G2D context g
	 */
	protected void paintSelf(int width, int height, Graphics2D g){
		
		if (image!= null){
			g.drawImage(this.image,this.getLocation()[1]*width + 10, this.getLocation()[0]*height + 10, width-20, height-20, null);
		}
	}

	
	protected abstract void loadImage();

	//  Getters/Setters

	public int[] getLocation() {
		return location;
	}



	public void setLocation(int[] location) {
		this.location = location;
	}



	public boolean getAlignment() {
		return alignment;
	}



	public void setAlignment(boolean alignment) {
		this.alignment = alignment;
	}	
	
}
