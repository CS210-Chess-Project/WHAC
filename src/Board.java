import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class to represent the game board or various lookahead board states.
 * @author Zach
 *
 */
public class Board{
	//test:
	private static Piece focusPiece;

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

	/**
	 * Constructor that makes a deep copy of another board.
	 * @param boardState the Piece array representing the other board
	 */
	public Board(Piece[][] boardState){
		//arrays in Java are passed by reference(sort of), so we have to copy the passed array here
		Piece[][] newBoardState = new Piece[5][5];
		for(int col = 0; col < 5; col++){
			for(int row = 0; row < 5; row++){
				Piece oldPiece = boardState[row][col];
				Piece pieceToAdd = null;
				if (oldPiece instanceof Pawn){
					pieceToAdd = new Pawn(new int[]{row, col}, oldPiece.alignment, this);
				}
				else if (oldPiece instanceof King){
					pieceToAdd = new King(new int[]{row, col}, oldPiece.alignment, this);
				}
				else if (oldPiece instanceof Queen){
					pieceToAdd = new Queen(new int[]{row, col}, oldPiece.alignment, this);
				}
				else if (oldPiece instanceof Bishop){
					pieceToAdd = new Bishop(new int[]{row, col}, oldPiece.alignment, this);
				}
				else if (oldPiece instanceof Knight){
					pieceToAdd = new Knight(new int[]{row, col}, oldPiece.alignment, this);
				}
				else if (oldPiece instanceof Rook){
					pieceToAdd = new Rook(new int[]{row, col}, oldPiece.alignment, this);
				}
				else if (oldPiece instanceof EmptySpace){
					pieceToAdd = new EmptySpace(new int[]{row, col});
				}
				newBoardState[row][col] = pieceToAdd;
			}
		}

		this.boardArray = newBoardState;

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
		this.boardArray[location[0]][location[1]] = new EmptySpace(new int[]{location[0], location[1]});
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
			}
			if(boardArray[4][col] instanceof Pawn){
				boolean color = boardArray[4][col].alignment;
				boardArray[4][col] = new King(new int[]{4, col}, color, this);
			}
		}
	}


	//  ---------------------------MINIMAX search related methods ----------------------------------
	// I just kinda threw together framework for how I thought these might work.  If you need to modify them, feel free.

<<<<<<< HEAD
	/**
	 * Minimax lookahead to decide moves
	 * 
	 * @param lookAheadNumber the number of moves to look ahead
	 * @param alpha the alpha value for alpha-beta pruning
	 * @param beta the beta value for alpha-beta pruning
	 * @param board the starting board
	 * @param maximizing true if maximizing, false if minimizing
	 * @return the optimal move
	 */
	public Move getNextMove(int lookAheadNumber, int alpha, int beta, int board, boolean maximizing){
=======
	public Move getNextMove(int lookAheadNumber){
		double threshold;
                double alpha;
                double beta;
                if(lookAheadNumber == 0){
                    
                }
                else
                {
                    ArrayList<Board> next = nextTurnStates();
                    for(int i = 0; i < next.size(); i++)
                        next.get(i).getNextMove(lookAheadNumber-1);
                }
                //This is the basis for the recursion. Beginning with the current board, it will look ahead at all the boards inside each
                //board arraylist until the best move is found. There's still a lot of work to do, but this is a good starting point.
                //I feel bad about using a for-loop in a recursion, but I couldn't think of a different way to look inside each board in the array.
                //Once I start assigning alpha and beta values, I'll add code to prune (breaking out a for loop early, breaking the recursion, etc.).
                //The future idea is to take the board that scores the "best" value and determine what inital move needs to make that happen, and
                //then constructing a Move object with the proper variables. - Mark
                
>>>>>>> 32a4e2eb30b0059de11f528a2ca588170ec72e15
		//TODO: This is where most of the work will be done.  We will do a minimax search for the best moves, looking ahead the specified number of turns
		//A Move object that represents our best move will be returned
		return null;
	}

	
	/**
	 * Generates a list of all the possible board states (boards resulting from a move) for a particular board
	 * forColor specifies the color whose moves will be generated
	 * This method omits non-capture moves if capture moves are available
	 * @param forColor the alignment of the color we are generating moves for.  true = White, false = Black
	 * @return an ArrayList of Boards, each one representing a possible move that stems from the current board
	 */
	public ArrayList<Board> nextTurnStates(boolean forColor){
		ArrayList<Board> futureStates = new ArrayList<Board>();

		ArrayList<Move> captures = new ArrayList<Move>();
		ArrayList<Move> others = new ArrayList<Move>();
		for (int col = 0; col < boardArray[0].length; col++){ //cols first in the loop so memory locations are contiguous
			for (int row = 0; row < boardArray.length; row++){
				if (boardArray[row][col].alignment == forColor){
					ArrayList<Move> movesForPiece = boardArray[row][col].getAvailableMoves();
					if (movesForPiece.size()>0){
						if(movesForPiece.get(0).isCaptureMove()){
							captures.addAll(movesForPiece);
						}
						else{
							others.addAll(movesForPiece);
						}
					}
				}
			}
		}

		if (captures.size()>0){
			for (Move m:captures){
				Board toAdd = new Board(this.boardArray);
				int pieceRow = m.getTargetPiece().getLocation()[0];
				int pieceCol = m.getTargetPiece().getLocation()[1];
				toAdd.makeMove(toAdd.getBoardArray()[pieceRow][pieceCol], m.getRow(), m.getCol());
				futureStates.add(toAdd);
			}
		}
		else{
			for (Move m:others){				
				Board toAdd = new Board(this.boardArray);
				int pieceRow = m.getTargetPiece().getLocation()[0];
				int pieceCol = m.getTargetPiece().getLocation()[1];
				//I learned the hard way, It's extremely important NOT to use the target piece from the Move object.  That way we avoid passing the target piece by reference
				toAdd.makeMove(toAdd.getBoardArray()[pieceRow][pieceCol], m.getRow(), m.getCol());
				futureStates.add(toAdd);
			}
		}

		return futureStates;
	}

	/**
	 * Evalutes the board object and returns a heuristic score.  A higher score is better for the evaluting player (in this case, our AI)
	 * @param playerColor The player from whose perspective we score.
	 * @return a numeric score
	 */
	public double evaluateSelf(boolean playerColor){
		//TODO: modify this to include number of moves in the score
		double pieceMoves = 0;
		double boardScore = 0;
		//these should depend on who controls the piece.  It's bad if our piece is in the middle, but good if our opponent's is.  So I moved these to the individual classes
		// I also combined the scoring for a piece into one method, so we can just sum up the scores of all the pieces
		// This now gives a better indication of a good/bad board.  A completely neutral board will have a score of 0 (for example the starting board).
		// The score will change based on how we move.  For example, moving our piece to the center of the board will cause the score to fall below 0
		for(int row = 0; row < 5; row++){
			for(int col = 0; col < 5; col++){
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
		focusPiece = board.getPieceAt(3, 2);
		board.makeMove(board.getPieceAt(3,2), 2, 2);
		board.nextTurnStates(board.BLACK);


		//		System.out.println(board.evaluateSelf(Board.WHITE));
		//		board.makeMove(board.getPieceAt(3,2), 1, 2);
		//		System.out.println(board.toString());
		//		System.out.println(board.evaluateSelf(Board.WHITE));
		//		board.makeMove(board.getPieceAt(4,2),1,2);
		//		System.out.println(board.evaluateSelf(Board.WHITE));
	}*/


}
