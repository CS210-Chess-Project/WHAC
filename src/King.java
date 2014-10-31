import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Class to represent Kings
 * See superclass for method documentation
 * @author Zach
 *
 */
public class King extends Piece {

	public King(int[] location) {
		super(location);
	}

	public King(int[] location, boolean alignment, Board parent) {
		super(location, alignment, parent);
	}

	@Override
	protected void generateAvailableMoves() {
		ArrayList<Move> others = new ArrayList<Move>();
		ArrayList<Move> captures = new ArrayList<Move>();

		Piece[][] board = this.parentBoard.getBoardArray();
		boolean captureFound = false;

		//check up
		int checkedRow = this.location[0] - 1;
		int checkedCol = this.location[1];
		if (checkedRow < 5 && checkedCol < 5 && checkedRow >= 0 && checkedCol >= 0){ //check bounds
			if (board[checkedRow][checkedCol] instanceof EmptySpace){
				others.add(new Move(this, checkedRow, checkedCol, parentBoard));
			}
			else if(board[checkedRow][checkedCol].alignment != this.alignment){
				captures.add(new Move(this, checkedRow, checkedCol, parentBoard));
				captureFound = true;
			}
		}

		//check up-right
		checkedRow = this.location[0] - 1;
		checkedCol = this.location[1] + 1;
		if (checkedRow < 5 && checkedCol < 5 && checkedRow >= 0 && checkedCol >= 0){ //check bounds
			if (board[checkedRow][checkedCol] instanceof EmptySpace){
				others.add(new Move(this, checkedRow, checkedCol, parentBoard));
			}
			else if(board[checkedRow][checkedCol].alignment != this.alignment){
				captures.add(new Move(this, checkedRow, checkedCol, parentBoard));
				captureFound = true;
			}
		}

		//check up-left
		checkedRow = this.location[0] - 1;
		checkedCol = this.location[1] - 1;
		if (checkedRow < 5 && checkedCol < 5 && checkedRow >= 0 && checkedCol >= 0){ //check bounds
			if (board[checkedRow][checkedCol] instanceof EmptySpace){
				others.add(new Move(this, checkedRow, checkedCol, parentBoard));
			}
			else if(board[checkedRow][checkedCol].alignment != this.alignment){
				captures.add(new Move(this, checkedRow, checkedCol, parentBoard));
				captureFound = true;
			}
		}

		//check down
		checkedRow = this.location[0] + 1;
		checkedCol = this.location[1];
		if (checkedRow < 5 && checkedCol < 5 && checkedRow >= 0 && checkedCol >= 0){ //check bounds
			if (board[checkedRow][checkedCol] instanceof EmptySpace){
				others.add(new Move(this, checkedRow, checkedCol, parentBoard));
			}
			else if(board[checkedRow][checkedCol].alignment != this.alignment){
				captures.add(new Move(this, checkedRow, checkedCol, parentBoard));
				captureFound = true;
			}
		}

		//check down - right
		checkedRow = this.location[0] + 1;
		checkedCol = this.location[1] + 1;
		if (checkedRow < 5 && checkedCol < 5 && checkedRow >= 0 && checkedCol >= 0){ //check bounds
			if (board[checkedRow][checkedCol] instanceof EmptySpace){
				others.add(new Move(this, checkedRow, checkedCol, parentBoard));
			}
			else if(board[checkedRow][checkedCol].alignment != this.alignment){
				captures.add(new Move(this, checkedRow, checkedCol, parentBoard));
				captureFound = true;
			}
		}

		//check down - left
		checkedRow = this.location[0] + 1;
		checkedCol = this.location[1] - 1;
		if (checkedRow < 5 && checkedCol < 5 && checkedRow >= 0 && checkedCol >= 0){ //check bounds
			if (board[checkedRow][checkedCol] instanceof EmptySpace){
				others.add(new Move(this, checkedRow, checkedCol, parentBoard));
			}
			else if(board[checkedRow][checkedCol].alignment != this.alignment){
				captures.add(new Move(this, checkedRow, checkedCol, parentBoard));
				captureFound = true;
			}
		}

		//check right
		checkedRow = this.location[0];
		checkedCol = this.location[1] + 1;
		if (checkedRow < 5 && checkedCol < 5 && checkedRow >= 0 && checkedCol >= 0){ //check bounds
			if (board[checkedRow][checkedCol] instanceof EmptySpace){
				others.add(new Move(this, checkedRow, checkedCol, parentBoard));
			}
			else if(board[checkedRow][checkedCol].alignment != this.alignment){
				captures.add(new Move(this, checkedRow, checkedCol, parentBoard));
				captureFound = true;
			}
		}

		//check left
		checkedRow = this.location[0];
		checkedCol = this.location[1] - 1;
		if (checkedRow < 5 && checkedCol < 5 && checkedRow >= 0 && checkedCol >= 0){ //check bounds
			if (board[checkedRow][checkedCol] instanceof EmptySpace){
				others.add(new Move(this, checkedRow, checkedCol, parentBoard));
			}
			else if(board[checkedRow][checkedCol].alignment != this.alignment){
				captures.add(new Move(this, checkedRow, checkedCol, parentBoard));
				captureFound = true;
			}
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
		// TODO Auto-generated method stub
		try {
			if (this.alignment){ //i.e. if white piece then
				this.image = ImageIO.read(new File("resources" + File.separator + "white-king.png"));
			}
			else{
				this.image = ImageIO.read(new File("resources" + File.separator + "black-king.png"));
			}
		} catch (IOException e) {
			System.out.println("Exception thrown");
			image = null;
		}
	}
	@Override
	public int getHeuristicScore(boolean playerAlignment){
		int pieceScore = 0;
		if (this.alignment != playerAlignment){
			pieceScore = -pieceScore;
		}

		return pieceScore + getLocationScore(playerAlignment);
	}


}
