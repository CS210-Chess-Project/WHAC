import java.util.ArrayList;

/**
 * Class to represent the game board or various lookahead board states.
 * @author Zach
 *
 */
public class Board {
	public static final boolean BLACK =false;
	public static final boolean WHITE = true;

	public Piece[][] boardArray;
	private double score;  // the minimax score for this board.  Computed and returned by evaluateSelf()

	public Board(){
		this.boardArray = new Piece[5][5];
	}

	/**
	 * This constructor initializes a board with pieces.  If the player is white, then the white piece are assigned to he player
	 * @param playerIsWhite tells if player controls white pieces or black pieces
	 */
	public Board(boolean isEasyMode){
		this.boardArray = new Piece[5][5];
		//initialize pawns on 2nd and 4th rows
		for (int x = 0; x < 5; x++){
			int[] locationBlack = {1,x};
			boardArray[1][x] = new Pawn(locationBlack, Board.BLACK, this); //black pawns
			int[] locationWhite = {3,x};
			boardArray[3][x] = new Pawn(locationWhite, Board.WHITE, this); //white pawns

			//empty spaces:
			boardArray[2][x] = new EmptySpace(new int[]{x,2});
		}

		//setup white - white pieces are the same in both easy and advanced mode
		//white pieces:
		boardArray[4][0] = new Rook(new int[]{4,0}, WHITE, this);
		boardArray[4][1] = new Knight(new int[]{4,1}, WHITE, this);
		boardArray[4][2] = new Bishop(new int[]{4,2}, WHITE, this);
		boardArray[4][3] = new Queen(new int[]{4,3}, WHITE, this);
		boardArray[4][4] = new King(new int[]{4,4}, WHITE, this);

		//easy mode does NOT have the pieces mirrored
		if (isEasyMode){
			//black pieces:
			boardArray[0][0] = new King(new int[]{0,0}, BLACK, this);
			boardArray[0][1] = new Queen(new int[]{0,1}, BLACK, this);
			boardArray[0][2] = new Bishop(new int[]{0,2}, BLACK, this);
			boardArray[0][3] = new Knight(new int[]{0,3}, BLACK, this);
			boardArray[0][4] = new Rook(new int[]{0,4}, BLACK, this);
		}
		//advanced mode has mirrored pieces
		else{
			boardArray[0][4] = new King(new int[]{0,4}, BLACK, this);
			boardArray[0][3] = new Queen(new int[]{0,3}, BLACK, this);
			boardArray[0][2] = new Bishop(new int[]{0,2}, BLACK, this);
			boardArray[0][1] = new Knight(new int[]{0,1}, BLACK, this);
			boardArray[0][0] = new Rook(new int[]{0,0}, BLACK, this);
		}
	}

	public Board(Piece[][] boardState){
		this.boardArray = boardState;
	}

	public boolean isStalemate(){
		//TODO: check for stalemate conditions
		return false;
	}

	//By nature, this isn't a terribly efficient method.  Try not to use it for anything performance-sensitive
	/**
	 * Checks if the current board has a capture move available for the selected Color
	 * @param targetAlignment the color (true = White, false = Black) of the color to search
	 * @return
	 */
	public boolean captureMovesAvailable(boolean targetAlignment){
		//add all available moves for every piece on the board
		for (int col = 0; col < this.boardArray[0].length; col++){
			for(int row = 0; row < this.boardArray.length; row++){
				Piece currentPiece = boardArray[row][col];
				if (currentPiece.alignment == targetAlignment){
					for(Move m : this.boardArray[row][col].getAvailableMoves()){
						if (m.isCaptureMove()){
							return true;
						}
					}
				}
			}
		}		

		return false;
	}

	//moves a piece to a specified row and column
	//really more of a helper method for makeMove that takes a move object
	public void makeMove(Piece p, int row, int col){
		int[] location = p.getLocation(); //get current location
		this.boardArray[location[0]][location[1]] = new EmptySpace(location);
		p.setLocation(new int[]{row, col});

		boardArray[row][col] = p;
		
		endOfMoveProcessing();
	}

	//nicer, more cleanly packaged makeMove method that uses the Move class
	public void makeMove(Move m){
		this.makeMove(m.getTargetPiece(), m.getRow(), m.getCol());		
	}
	
	/**
	 * This method performs end of move tasks such as checking for pawns that need to be "kinged"
	 * Later this might check for game over or stalemate states
	 */
	private void endOfMoveProcessing(){
		//check for pawns that need kinging
		for (int col = 0; col < 5; col++){
			if (boardArray[0][col] instanceof Pawn){
				boolean color = boardArray[0][col].alignment;
				boardArray[0][col] = new King(new int[]{0, col}, color, this);
				System.out.println("Found one");
			}
			if(boardArray[4][col] instanceof Pawn){
				boolean color = boardArray[4][col].alignment;
				boardArray[4][col] = new King(new int[]{4, col}, color, this);
				System.out.println("Found one");
			}
		}
	}


	//  ---------------------------MINIMAX search related methods ----------------------------------
	// I just kinda threw together framework for how I thought these might work.  If you need to modify them, feel free.

	public Move getNextMove(int lookAheadNumber){
		//TODO: This is where most of the work will be done.  We will do a minimax search for the best moves, looking ahead the specified number of turns
		//A Board state that represents our best move will be returned
		return null;
	}

	public ArrayList<Board> nextTurnStates(){
		//TODO : This method will return a list of all the possible board states at the next turn.
		return null;
	}

	public double evaluateSelf(boolean playerColor){
		double pieceMoves = 0;
		double boardScore = 0;
		//these should depend on who controls the piece.  It's bad if our piece is in the middle, but good if our opponent's is.  So I moved these to the individual classes
		// I also combined the scoring for a piece into one method, so we can just sum up the scores of all the pieces
		// This now gives a better indication of a good/bad board.  A completely neutral board will have a score of 0 (for example the starting board).
		// The score will change based on how we move.  For example, moving our piece to the center of the board will cause the score to fall below 0
		//this is demonstrated in the main method of this class
		for(int row = 0; row < 5; row++){
			for(int col = 0; col < 5; col++){
				/*if((row == 0 && col == 0)||(row == 0 && col == 4))
					pieceLocationScore = -4;
				else if((row == 4 && col == 0) || (row == 4 && col == 4))
					pieceLocationScore = 4;
				else if(row == 0 ||  col == 0)
					pieceLocationScore = -3;
				else if(row == 4 || col == 4)
					pieceLocationScore = 3;
				else if((row == 2 && (col != 0 || col != 4) || (col == 2 && (row != 0 || row != 4))))
					pieceLocationScore = -5;
				else
					pieceLocationScore = 1;
				ArrayList<Move> allMovesForPiece = boardArray[row][col].getAvailableMoves();
				pieceMoves = allMovesForPiece.size() + pieceMoves;
				pieceTypeScore = boardArray[row][col].getHeuristicScore() + pieceTypeScore;*/
				boardScore += boardArray[row][col].getHeuristicScore(playerColor);
			}
		}
		this.score = boardScore;
		return this.score;
	}

	// ------------------------- End Minimax related methods --------------------------------

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
		Board board = new Board(true);
		System.out.println(board.evaluateSelf(Board.WHITE));
		board.makeMove(board.getPieceAt(3,2), 1, 2);
		System.out.println(board.toString());
		System.out.println(board.evaluateSelf(Board.WHITE));
		board.makeMove(board.getPieceAt(4,2),1,2);
		System.out.println(board.evaluateSelf(Board.WHITE));
	}*/


}
