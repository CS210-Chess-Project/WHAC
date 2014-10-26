import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Class to represent Queens.  Queens suck.
 * @author Zach
 *
 */
public class Queen extends Piece {

	public Queen(int[] location) {
		super(location);
	}

	public Queen(int[] location, boolean alignment, Board parent) {
		super(location, alignment, parent);
	}

	@Override
	protected void generateAvailableMoves() {
		//The Queens moves are a union of the Rooks and the Bishops.  good ole copy'n'paste		
		ArrayList<Move> captures = new ArrayList<Move>();
		ArrayList<Move> others = new ArrayList<Move>();

		Piece[][] board = parentBoard.getBoardArray();
		boolean captureFound = false;

		//lower right checking:
		int checkedRow = this.location[0] + 1;
		int checkedCol = this.location[1] + 1;
		while (checkedRow < 5 && checkedCol < 5 && checkedRow >= 0 && checkedCol >= 0){ //check bounds conditions
			Piece checkedPiece = board[checkedRow][checkedCol];
			if(checkedPiece instanceof EmptySpace){
				others.add(new Move(this, checkedRow, checkedCol, parentBoard));
			}
			else if (checkedPiece.alignment == !this.alignment){ // if its an opponent piece
				captures.add(new Move(this, checkedRow, checkedCol, parentBoard));
				captureFound = true;
				break;
			}
			else{
				break;
			}
			checkedRow++;
			checkedCol++;
		}

		//lower left checking
		checkedRow = this.location[0] + 1;
		checkedCol = this.location[1] - 1;
		while (checkedRow < 5 && checkedCol < 5 && checkedRow >= 0 && checkedCol >= 0){ //check bounds conditions
			Piece checkedPiece = board[checkedRow][checkedCol];
			if(checkedPiece instanceof EmptySpace){
				others.add(new Move(this, checkedRow, checkedCol, parentBoard));
			}
			else if (checkedPiece.alignment == !this.alignment && !(checkedPiece instanceof EmptySpace)){ // if its an opponent piece
				captures.add(new Move(this, checkedRow, checkedCol, parentBoard));
				captureFound = true;
				break;
			}
			else{
				break;
			}
			checkedRow++;
			checkedCol--;
		}

		//upper right checking
		checkedRow = this.location[0] - 1;
		checkedCol = this.location[1] + 1;
		while (checkedRow < 5 && checkedCol < 5 && checkedRow >= 0 && checkedCol >= 0){ //check bounds conditions
			Piece checkedPiece = board[checkedRow][checkedCol];
			if(checkedPiece instanceof EmptySpace){
				others.add(new Move(this, checkedRow, checkedCol, parentBoard));
			}
			else if (checkedPiece.alignment == !this.alignment && !(checkedPiece instanceof EmptySpace)){ // if its an opponent piece
				captures.add(new Move(this, checkedRow, checkedCol, parentBoard));
				captureFound = true;
				break;
			}
			else{
				break;
			}
			checkedRow--;
			checkedCol++;
		}

		//upper left checking
		checkedRow = this.location[0] - 1;
		checkedCol = this.location[1] - 1;
		while (checkedRow < 5 && checkedCol < 5 && checkedRow >= 0 && checkedCol >= 0){ //check bounds conditions
			Piece checkedPiece = board[checkedRow][checkedCol];
			if(checkedPiece instanceof EmptySpace){
				others.add(new Move(this, checkedRow, checkedCol, parentBoard));
			}
			else if (checkedPiece.alignment == !this.alignment && !(checkedPiece instanceof EmptySpace)){ // if its an opponent piece
				captures.add(new Move(this, checkedRow, checkedCol, parentBoard));
				captureFound = true;
				break;
			}
			else{
				break;
			}
			checkedRow--;
			checkedCol--;
		}

		//check upwards
		checkedRow = this.location[0] - 1;
		checkedCol = this.location[1];
		while(checkedRow < 5 && checkedCol < 5 && checkedRow >= 0 && checkedCol >= 0){
			if (board[checkedRow][checkedCol] instanceof EmptySpace){
				others.add( new Move(this, checkedRow, checkedCol, parentBoard));
			}
			else if(board[checkedRow][checkedCol].alignment != this.alignment){ //enemy piece
				captures.add(new Move(this, checkedRow, checkedCol, parentBoard));
				captureFound = true;
				break;				
			}
			else{ //friendly piece blocks
				break;
			}

			checkedRow--;
		}

		//check downwards
		checkedRow = this.location[0] + 1;
		checkedCol = this.location[1];
		while(checkedRow < 5 && checkedCol < 5 && checkedRow >= 0 && checkedCol >= 0){
			if (board[checkedRow][checkedCol] instanceof EmptySpace){
				others.add( new Move(this, checkedRow, checkedCol, parentBoard));
			}
			else if(board[checkedRow][checkedCol].alignment != this.alignment){ //enemy piece
				captures.add(new Move(this, checkedRow, checkedCol, parentBoard));
				captureFound = true;
				break;				
			}
			else{ //friendly piece blocks
				break;
			}

			checkedRow++;
		}

		//check to right
		checkedRow = this.location[0];
		checkedCol = this.location[1] + 1;
		while(checkedRow < 5 && checkedCol < 5 && checkedRow >= 0 && checkedCol >= 0){
			if (board[checkedRow][checkedCol] instanceof EmptySpace){
				others.add( new Move(this, checkedRow, checkedCol, parentBoard));
			}
			else if(board[checkedRow][checkedCol].alignment != this.alignment){ //enemy piece
				captures.add(new Move(this, checkedRow, checkedCol, parentBoard));
				captureFound = true;
				break;				
			}
			else{ //friendly piece blocks
				break;
			}

			checkedCol++;
		}

		//check to left
		checkedRow = this.location[0];
		checkedCol = this.location[1] - 1;
		while(checkedRow < 5 && checkedCol < 5 && checkedRow >= 0 && checkedCol >= 0){
			if (board[checkedRow][checkedCol] instanceof EmptySpace){
				others.add( new Move(this, checkedRow, checkedCol, parentBoard));
			}
			else if(board[checkedRow][checkedCol].alignment != this.alignment){ //enemy piece
				captures.add(new Move(this, checkedRow, checkedCol, parentBoard));
				captureFound = true;
				break;				
			}
			else{ //friendly piece blocks
				break;
			}

			checkedCol--;
		}

		if (captureFound){
			this.availableMoves = captures;
		}
		else{
			this.availableMoves = others;
		}
	}

	@Override
	protected void loadImage() {
		try {
			if (this.alignment){ //i.e. if white piece then
				this.image = ImageIO.read(new File("resources" + File.separator + "white-queen.png"));
			}
			else{
				this.image = ImageIO.read(new File("resources" + File.separator + "black-queen.png"));
			}
		} catch (IOException e) {
			System.out.println("Exception thrown");
			image = null;
		}
	}
	@Override
	public int getHeuristicScore(boolean playerAlignment){
		int pieceScore = -12;
		if (this.alignment != playerAlignment){
			pieceScore = -pieceScore;
		}

		return pieceScore + getLocationScore(playerAlignment);
	}


}
