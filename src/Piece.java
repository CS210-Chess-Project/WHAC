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
	protected int score;
	
	/**
	 * Constructs a piece with the specified location
	 * @param location the 2D array {row, col}
	 */
	public Piece(int[] location){
		this.location = location;
		loadImage();
	}
	
	/**
	 * Constructs a piece with the specified location, color, and parentBoard
	 * @param location the 2D array {row, col}
	 * @param alignment the boolean value indicating white or black
	 * @param parentBd the Board object that this piece belongs to
	 */
	public Piece(int[] location, boolean alignment, Board parentBd){
		this.location = location;
		this.alignment = alignment;
		this.parentBoard = parentBd;
		loadImage();		
	}
	
	/**
	 * Generates and returns a list of available moves for this piece
	 * @return the list of available moves for this piece
	 */
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

	/**
	 * loads the resource image for the piece
	 */
	protected abstract void loadImage();

	//  Getters/Setters

	public int[] getLocation() {
		return location;
	}
	
	public abstract int getHeuristicScore(boolean playerColor);
	
	//location score is shared for any piece
	protected int getLocationScore(boolean playerColor){
		int locationScore = 0;
    	//corners:
    	if ((location[0] == 0 && location[1] == 0) || (location[0] == 4 && location[1]==4) || (location[0] == 0 && location[1]==4) || (location[0]==4 && location[1]==0)){
    		locationScore = 2;
    	}
    	//edges
    	else if(location[0] ==0 || location[1] == 0 || location[0]==4 || location[1]==4){
    		locationScore = 1;
    	}
    	//inner edges:
    	else if(location[0]==1 || location[1]==1 || location[0]==3 || location[1]==3){
    		locationScore = -1;
    	}
    	else if(location[0]==2 && location[1]==2){
    		locationScore = -2;
    	}
    	
    	//if its an opponent piece we're talking about, then the location score should be reversed
    	if (this.alignment != playerColor){
    		locationScore = -locationScore;
    	}
    	return locationScore;
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
