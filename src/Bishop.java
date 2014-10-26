import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Class to represent Bishop
 * @author Zach
 *
 */
public class Bishop extends Piece {

	public Bishop(int[] location) {
		super(location);
	}

	public Bishop(int[] location, boolean alignment, Board parentBoard) {
		super(location, alignment, parentBoard);
	}

	@Override
	protected void generateAvailableMoves() {
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
				this.image = ImageIO.read(new File("resources" + File.separator + "white-bishop.png"));
			}
			else{
				this.image = ImageIO.read(new File("resources" + File.separator + "black-bishop.png"));
			}
		} catch (IOException e) {
			System.out.println("Exception thrown");
			image = null;
		}
	}

	@Override
	public int getHeuristicScore(boolean playerAlignment){   
		int pieceScore = -3;
		if (this.alignment != playerAlignment){
			pieceScore = -pieceScore;
		}

		return pieceScore + getLocationScore(playerAlignment);
	}

}
