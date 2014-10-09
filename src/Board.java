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
	
	// Getters/ Setters
	
	public Piece[][] getBoardArray() {
		return boardArray;
	}

	public void setBoardArray(Piece[][] boardArray) {
		this.boardArray = boardArray;
	}

}
