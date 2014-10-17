import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Superclass of anything that can occupy a grid on the board, including empty spaces
 * Implemented as an abstract class to that subclasses are forced to implement custom methods for generateAvailableMoves()
 * We will never need an instance of Piece, just instances of the subclasses
 * @author Zach
 *
 */
public abstract class Piece {
	private int[] location; //the 2D location of the piece on the board.
	protected ArrayList<int[]> availableMoves;
	protected ArrayList<int[]> captureMoves;
	private boolean alignment;  //this will let us know if the piece is controlled by us or the enemy;  True = our piece; false = enemy piece
	
	public Piece(int[] location){
		this.location = location;
		generateAvailableMoves();
	}
	
	public Piece(int[] location, boolean alignment){
		this.location = location;
		generateAvailableMoves();
		this.alignment = alignment;
	}
	
	
	//generates list of available moves and returns the list.  We generate each time to make sure we get an up-to-date list
	public ArrayList<int[]> getAvailableMoves(){
		this.generateAvailableMoves();
		return availableMoves;
	}	
	
	public ArrayList<int[]> getCaptureMoves(){
		this.generateCaptureMoves();
		return captureMoves;
	}

	/**
	 * This method is implemented in subclasses and should always cause the field availableMoves to be instantiated
	 * It will be called anytime the list of available moves needs to be updated or retrieved
	 */
	protected abstract void generateAvailableMoves();
	
	
	/**
	 * This method is also implemented in subclasses and generates a list of available capture moves
	 * It should also be called anytime the list of capture moves needs to be updated or retrieved
	 */
	protected abstract void generateCaptureMoves();
	
	/**
	 * Painting instructions for the GUI;  Paints onto the G2D context g
	 */
	protected abstract void paintSelf(int width, int height, Graphics2D g);


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
