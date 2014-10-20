/**
 * THis class encapsulates the data of a move.  That is, the target piece and the row and column of where that piece will be moved.
 * It also optionally includes a Board that represents the board state after the move has been made
 * @author Zach
 *
 */
public class Move {
	private int row;
	private int col;
	private Piece targetPiece;
	private Board resultBoard;
	
	/**
	 * Constructor with the basic parameters
	 * @param p the target piece
	 * @param row the row where the piece will be moved
	 * @param col the column where the piece will be moved
	 */
	public Move(Piece p, int row, int col){
		this.row = row; this.col = col;
		this.targetPiece = p;		
	}
	
	/**
	 * Contructor that will also create the resulting board state
	 * @param p the target piece
	 * @param row the row where the piece will be moved
	 * @param col the column where the piece will be moved
	 */
	public Move(Piece p, int row, int col, Board boardBeforeMove){
		this.row = row; this.col = col;
		this.targetPiece = p;
		boardBeforeMove.makeMove(p, row, col);
		this.resultBoard = boardBeforeMove;
	}

	//Getters/Setters
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Piece getTargetPiece() {
		return targetPiece;
	}

	public void setTargetPiece(Piece targetPiece) {
		this.targetPiece = targetPiece;
	}

	public Board getResultBoard() {
		return resultBoard;
	}
	
		
}
