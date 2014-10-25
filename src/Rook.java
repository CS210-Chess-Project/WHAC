import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Class to represent a rook
 * @author Zach
 *
 */
public class Rook extends Piece {

	public Rook(int[] location) {
		super(location);
	}

	public Rook(int[] location, boolean alignment, Board parent) {
		super(location, alignment, parent);
	}

	@Override
	protected void generateAvailableMoves() {
		ArrayList<Move> others = new ArrayList<Move>();
		ArrayList<Move> captures = new ArrayList<Move>();

		Piece[][] board = this.parentBoard.getBoardArray();
		boolean captureFound = false;

		//check upwards
		int checkedRow = this.location[0] - 1;
		int checkedCol = this.location[1];
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
		// TODO Auto-generated method stub
		try {
			if (this.alignment){ //i.e. if white piece then
				this.image = ImageIO.read(new File("resources" + File.separator + "white-rook.png"));
			}
			else{
				this.image = ImageIO.read(new File("resources" + File.separator + "black-rook.png"));
			}
		} catch (IOException e) {
			System.out.println("Exception thrown");
			image = null;
		}
	}
	@Override
	public int getHeuristicScore(boolean playerAlignment){
		int pieceScore = -4;
		if (this.alignment != playerAlignment){
			pieceScore = -pieceScore;
		}

		return pieceScore + getLocationScore(playerAlignment);
	}

}
