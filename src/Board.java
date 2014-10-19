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
			int[] locationBlack = {1,x};
			boardArray[1][x] = new Pawn(locationBlack, !playerIsWhite); //black pawns
			int[] locationWhite = {3,x};
			boardArray[3][x] = new Pawn(locationWhite, playerIsWhite); //white pawns
			
			//empty spaces:
			boardArray[2][x] = new EmptySpace(new int[]{x,2});
		}
		
		//setup white - white pieces are the same in both easy and advanced mode
		//white pieces:
		boardArray[4][0] = new Rook(new int[]{4,0}, playerIsWhite);
		boardArray[4][1] = new Knight(new int[]{4,1}, playerIsWhite);
		boardArray[4][2] = new Bishop(new int[]{4,2}, playerIsWhite);
		boardArray[4][3] = new Queen(new int[]{4,3}, playerIsWhite);
		boardArray[4][4] = new King(new int[]{4,4}, playerIsWhite);
		
		//easy mode does NOT have the pieces mirrored
		if (isEasyMode){
			//black pieces:
			boardArray[0][0] = new King(new int[]{0,0}, !playerIsWhite);
			boardArray[0][1] = new Queen(new int[]{0,1}, !playerIsWhite);
			boardArray[0][2] = new Bishop(new int[]{0,2}, !playerIsWhite);
			boardArray[0][3] = new Knight(new int[]{0,3}, !playerIsWhite);
			boardArray[0][4] = new Rook(new int[]{0,4}, !playerIsWhite);
		}
		//advanced mode has mirrored pieces
		else{
			boardArray[0][4] = new King(new int[]{0,4}, !playerIsWhite);
			boardArray[0][3] = new Queen(new int[]{0,3}, !playerIsWhite);
			boardArray[0][2] = new Bishop(new int[]{0,2}, !playerIsWhite);
			boardArray[0][1] = new Knight(new int[]{0,1}, !playerIsWhite);
			boardArray[0][0] = new Rook(new int[]{0,0}, !playerIsWhite);
		}
	}

	public Board(Piece[][] boardState){
		this.boardArray = boardState;
	}

	//moves a piece to a specified row and column
	public void makeMove(Piece p, int row, int col){
		int[] location = p.getLocation(); //get current location
		this.boardArray[location[0]][location[1]] = new EmptySpace(location);
		p.setLocation(new int[]{row, col});
		
		boardArray[row][col] = p;
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
				returned = returned + boardArray[y][x].toString() + "|";
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
	
	public Piece getPieceAt(int row, int col){
		return boardArray[row][col];
	}
	
	//test method
	/*public static void main(String[] args){
		Board board = new Board(true, false);
		board.makeMove(board.getPieceAt(3,2), 1, 2);
		System.out.println(board.toString());
	}*/

}
