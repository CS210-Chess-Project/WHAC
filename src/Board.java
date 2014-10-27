import java.util.ArrayList;

/**
 * Class to represent the game board or various lookahead board states.
 * @author Zach
 *
 */
public class Board{

	public static final boolean BLACK =false;
	public static final boolean WHITE = true;

	public Piece[][] boardArray;
	private int score;  // the minimax score for this board.  Computed and returned by evaluateSelf()

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
		return isStalemateForColor(Board.WHITE) || isStalemateForColor(Board.BLACK);
	}

	public boolean isStalemateForColor(boolean color){
		int pieceCount = 0;
		if(color==Board.WHITE){
			pieceCount = whiteCount();
		}
		else{
			pieceCount = blackCount();
		}
		if (pieceCount == 0){ //then it's not a stalemate, it's a win!
			return false;
		}
		else{ //else check stalemate conditions 
			 //if color has no moves:
			if(this.nextTurnStates(color).size()==0){ 
				return true;
			}
			else return false;
		}
	}

	public boolean isGameOver(boolean whosTurn){
		if (this.isStalemateForColor(whosTurn)){
			return true;
		}
		else if (this.whiteCount() == 0 || this.blackCount()==0){
			return true;
		}
		return false;
	}

	public boolean determineWinningColor(){
		int whiteCount = this.whiteCount();
		int blackCount = this.blackCount();
		System.out.println("Determining winning color: \nWhite count: "+ whiteCount + "\nBlack Count: " + blackCount);
		return this.whiteCount() < this.blackCount(); //returns true (WHITE) if white count is less than black count
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

	/**
	 * Method that calls minimax on the current board and chooses the best future board state.
	 * I found that it's a heck of a lot easier to return a Board object than a Move object, and the Board object is just as useful.
	 * @param forColor the color (white = true, black = false) of the player getting the move
	 * @param lookAheadNumber the amount to look ahead
	 * @return the Board object representing the best chosen move
	 */
	public Board getNextMove(boolean forColor, int lookAheadNumber){

		//What I have here reads kind of strangely:
		//it uses the minimax method to get back the index of the best choice from the list of next board states.

		ArrayList<Board> potentialMoves = this.nextTurnStates(forColor);
		if(potentialMoves.size()==1){
			return potentialMoves.get(0);
		}
		else{
			//call minimax, maximizing the score of the chosen color
			int indexOfBestMove = this.minimax(forColor, true, lookAheadNumber, Integer.MIN_VALUE, Integer.MAX_VALUE)[1];
			if (indexOfBestMove >= 0){
				return potentialMoves.get(indexOfBestMove); 
			}
			else{
				//no moves were available
				//return null;
				System.out.println("No moves available");
				return this;
			}
		}

	}

	/**
	 * Utility method to help with the minimax search. 
	 * The return is sorta convoluted.  It returns an integer array whose 0th element is the best score found, and whose 1st element is the index 
	 * of the chosen move from nextMoveStates().
	 *  
	 * @param maximizing true if the caller is maximizing.  False if the caller is minimizig
	 * @param lookahead how many moves to look ahead
	 * @param alpha the current alpha value
	 * @param beta the current beta value
	 * @return an integer array.  The first value is the best score.  The second value is the index of the chosen move
	 */
	private int[] minimax(boolean alignment, boolean maximizing, int lookahead, int alpha, int beta){
		//pull down alpha-beta values from parent:
		int thisNodesAlpha = alpha;
		int thisNodesBeta = beta;
		int currentValue; // the currentValue of the node - will be initialized momentarily based on whether we are maximizing or minimizing
		int indexToReturn = -1;
		
		int nextLookahead = lookahead-1; //this will change if we need to lookahead "faster" (fewer moves)

		//generate list of potential moves:
		ArrayList<Board> potentialMoves = this.nextTurnStates(alignment);
		//DEBUG:
		if(lookahead == 4){
			System.out.println("Options: " + potentialMoves.size());
		}
		//reduce lookahead to satisfy time reqs if needed:
		if(potentialMoves.size()>7){
			nextLookahead = lookahead - 2; //don't lookahead as far
			if (nextLookahead < 0){
				nextLookahead = 0; //guard against negative lookahead
			}
		}
		
		//if we are at a leaf, just return the current board's score
		if (potentialMoves.size() == 0 || lookahead == 0){ //two stopping conditions
			if(potentialMoves.size()==0){
			}
			return new int[]{this.evaluateSelf(alignment), indexToReturn};			
		}
		else{ //do minimax
			if(maximizing){
				currentValue = Integer.MIN_VALUE;
				//go through the moves, pruning if possible:
				for (int i = 0; i < potentialMoves.size(); i++){
					if (thisNodesAlpha > thisNodesBeta){ //pruning condition
						break;//prune the rest of the children (don't evaluate them)
					}
					else{
						int score;
						Board potentialMove = potentialMoves.get(i);
						if (potentialMove.isGameOver(alignment)){ //if it's a game over, score takes on a max or min val
							if (potentialMove.determineWinningColor()==alignment){
								score = Integer.MAX_VALUE;
							}
							else score= Integer.MIN_VALUE;
						}
						else{ //otherwise we do a min search (other players turn)
							score = potentialMoves.get(i).minimax(alignment, false, nextLookahead, thisNodesAlpha, thisNodesBeta)[0];
						}
						//if we aren't pruning, then we check for value to pull up						
						
						if (score >= currentValue){ //pull up condition
							currentValue = score;
							thisNodesAlpha = score;
							indexToReturn = i;
						}
					}
				}
			}
			else{ //minimizing
				currentValue = Integer.MAX_VALUE;
				//go through moves, pruning if possible:
				for (int i = 0; i < potentialMoves.size(); i++){
					if (thisNodesAlpha > thisNodesBeta){ //pruning condition
						break; //prune the rest of the children
					}
					else{
						int score;
						Board potentialMove = potentialMoves.get(i);
						if (potentialMove.isGameOver(!alignment)){ //if it's a game over, score takes on a max or min val
							if (potentialMove.determineWinningColor()==!alignment){
								score = Integer.MIN_VALUE;
							}
							else score= Integer.MAX_VALUE;
						}
						else{ //otherwise we do a min search (other players turn)
							score = potentialMoves.get(i).minimax(alignment, false, nextLookahead, thisNodesAlpha, thisNodesBeta)[0];
						}
						if (score <= currentValue){ //pull up condition
							currentValue = score;
							thisNodesBeta = score;
							indexToReturn = i;
						}
					}
				}
			}
		}

		return new int[]{currentValue, indexToReturn};
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
	public int evaluateSelf(boolean playerColor){
		if(this.isGameOver(playerColor)){
			if (this.determineWinningColor() == playerColor){
				return Integer.MAX_VALUE;
			}
			else{
				return Integer.MIN_VALUE;
			}
		}

		//TODO: modify this to include number of moves in the score
		//sum up individual piece scores
		int boardScore = 0;
		for(int row = 0; row < 5; row++){
			for(int col = 0; col < 5; col++){
				boardScore += boardArray[row][col].getHeuristicScore(playerColor);
			}
		}
		//factor in how many pieces each player has
//		if (playerColor== Board.WHITE){
//			boardScore -= this.whiteCount();
//			boardScore += this.blackCount();
//		}
//		else{
//			boardScore += this.whiteCount();
//			boardScore -= this.blackCount();
//		}
		this.score = boardScore;
		return this.score;
	}

	// ------------------------- End Minimax related methods --------------------------------

	//Utilities:
	public int whiteCount(){
		int counter = 0;
		for(int col = 0; col < 5; col++){
			for (int row = 0; row < 5; row++){
				if (boardArray[row][col].alignment == Board.WHITE){
					counter++;
				}
			}
		}

		return counter;
	}
	public int blackCount(){
		int counter = 0;
		for(int col = 0; col < 5; col++){
			for (int row = 0; row < 5; row++){
				if (boardArray[row][col].alignment == Board.BLACK && !(boardArray[row][col] instanceof EmptySpace)){
					counter++;
				}
			}
		}

		return counter;
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
	public static void main(String[] args){
		/*Board b = new Board(true);
		System.out.println(b.toString());
		System.out.println("White score: " + b.evaluateSelf(Board.WHITE));
		System.out.println("Black score: " + b.evaluateSelf(Board.BLACK));
		//make move bad for white:
		//b.makeMove(b.getPieceAt(3, 2),2,2);
		b.makeMove(b.getPieceAt(4, 3),2,2);
		System.out.println("White score: " + b.evaluateSelf(Board.WHITE));
		System.out.println("Black score: " + b.evaluateSelf(Board.BLACK));*/
		Board b = new Board();


		Piece[][] newBoard = new Piece[5][5];
		for(int row = 0 ; row < 5; row++){
			for(int col = 0; col< 5; col++){
				newBoard[row][col] = new EmptySpace(new int[]{row, col});
			}
		}
		b.setBoardArray(newBoard);
		newBoard[0][0] = new King(new int[]{0,0,}, Board.BLACK, b);
		newBoard[1][0] = new Pawn(new int[]{1,0}, Board.BLACK, b);
		newBoard[1][2] = new Pawn(new int[]{1,2}, Board.BLACK, b);
		newBoard[3][3] = new Pawn(new int[]{1,0}, Board.BLACK, b);
		newBoard[2][2] = new Pawn(new int[]{2,2}, Board.WHITE, b);
		newBoard[3][0] = new Pawn(new int[]{3,0}, Board.WHITE, b);
		b.setBoardArray(newBoard);

		System.out.println(b.nextTurnStates(Board.BLACK).size());

	}
}
