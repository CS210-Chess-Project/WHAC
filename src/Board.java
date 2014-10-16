import java.util.ArrayList;

/**
 * Class to represent the game board and the various lookahead board states.
 * @author Zach
 *
 */
public class Board {
	public Piece[][] boardArray;
	private double score;  // the minimax score for this board.  Computed and returned by evaluateSelf()

	public Board(){
		this.boardArray = new Piece[5][5];
	}

	/**
	 * This constructor initializes a board with pieces.  If the player is white, then the white piece are assigned to he player
	 * @param playerIsWhite tells if player controls white pieces or black pieces
	 */
	public Board(boolean playerIsWhite, boolean isEasyMode){
		this.boardArray = new Piece[5][5];
		//initialize pawns on 2nd and 4th rows
		for (int x = 0; x < 5; x++){
			int[] locationBlack = {x,1};
			boardArray[x][1] = new Pawn(locationBlack, !playerIsWhite); //black pawns
			int[] locationWhite = {x,3};
			boardArray[x][3] = new Pawn(locationWhite, playerIsWhite); //white pawns
			
			//empty spaces:
			boardArray[x][2] = new EmptySpace(new int[]{x,2});
		}
		
		//setup white - white pieces are the same in both easy and advanced mode
		//white pieces:
		boardArray[0][4] = new Rook(new int[]{0,4}, playerIsWhite);
		boardArray[1][4] = new Knight(new int[]{1,4}, playerIsWhite);
		boardArray[2][4] = new Bishop(new int[]{2,4}, playerIsWhite);
		boardArray[3][4] = new Queen(new int[]{3,4}, playerIsWhite);
		boardArray[4][4] = new King(new int[]{4,4}, playerIsWhite);
		
		//easy mode does NOT have the pieces mirrored
		if (isEasyMode){
			//black pieces:
			boardArray[0][0] = new King(new int[]{0,0}, !playerIsWhite);
			boardArray[1][0] = new Queen(new int[]{1,0}, !playerIsWhite);
			boardArray[2][0] = new Bishop(new int[]{2,0}, !playerIsWhite);
			boardArray[3][0] = new Knight(new int[]{3,0}, !playerIsWhite);
			boardArray[4][0] = new Rook(new int[]{4,0}, !playerIsWhite);
		}
		//advanced mode has mirrored pieces
		else{
			boardArray[4][0] = new King(new int[]{4,0}, !playerIsWhite);
			boardArray[3][0] = new Queen(new int[]{3,0}, !playerIsWhite);
			boardArray[2][0] = new Bishop(new int[]{2,0}, !playerIsWhite);
			boardArray[1][0] = new Knight(new int[]{1,0}, !playerIsWhite);
			boardArray[0][0] = new Rook(new int[]{0,0}, !playerIsWhite);
		}
	}

	public Board(Piece[][] boardState){
		this.boardArray = boardState;
	}

	public void makeOpponentMove(Piece p, int[][] toPosition){
		//TODO: should move the opponents piece p to the specified position and update the boardArray
	}

	public void makeFriendlyMove(int lookAheadNumber){
		this.boardArray = getNextMove(lookAheadNumber).getBoardArray();
		//TODO also need to repaint board
	}	

	public Board getNextMove(int lookAheadNumber){
		//TODO: This is where most of the work will be done.  We will do a minimax search for the best moves, looking ahead the specified number of turns
		//A Board state that represents our best move will be returned
		return null;
	}

	public ArrayList<Board> nextTurnStates(){
		//TODO : This method will return a list of all the possible board states at the next turn.
		return null;
	}

	public double evaluateSelf(){
		//TODO : this method should using our heuristic to score the board, set the score field to that score, and return the score
		this.score = 0;
		return this.score;
	}
	
	//Testing methods:
	public String toString(){
		String returned = "";
		for(int y = 0; y < boardArray.length; y++){
			for (int x = 0; x < boardArray[0].length; x++){
				returned = returned + boardArray[x][y].toString() + "|";
			}
			returned = returned + "\n";
		}
		return returned;
	}

	// Getters/ Setters

	public Piece[][] getBoardArray() {
		return boardArray;
	}

	public void setBoardArray(Piece[][] boardArray) {
		this.boardArray = boardArray;
	}
	
	//test method
	/*public static void main(String[] args){
		Board board = new Board(true, false);
		System.out.println(board.toString());
	}*/

}
