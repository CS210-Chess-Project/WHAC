import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class to represent the puny pawn
 * @author Zach
 *
 */
public class Pawn extends Piece {

	public Pawn(int[] location) {
		super(location);
		// TODO Auto-generated constructor stub
	}

	public Pawn(int[] location, boolean alignment, Board parent) {
		super(location, alignment, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void generateAvailableMoves() {		
		//clear the list and regenerate
		this.availableMoves.clear();

		boolean captureMovesFound = false;

		//check for capture moves first:
		int potentialCol1 = this.location[1] - 1;
		int potentialCol2 = this.location[1] + 1;
		//white pieces:
		if (this.alignment == Board.WHITE){
			int newRow = this.location[0] - 1;
			if (newRow >= 0 && newRow < 5){
				if (potentialCol1 >= 0){ //check left diagonal capture
					Piece leftDiagPiece = parentBoard.getPieceAt(newRow, potentialCol1);
					if (leftDiagPiece.alignment == Board.BLACK && !(leftDiagPiece instanceof EmptySpace)){ //white pieces can only capture black pieces
						availableMoves.add(new Move(this, newRow, potentialCol1, parentBoard));
						captureMovesFound = true;
					}
				}
				if (potentialCol2 < 5){ //check right diagonal capture
					Piece rightDiagPiece = parentBoard.getPieceAt(newRow, potentialCol2);
					if (rightDiagPiece.alignment == Board.BLACK && !(rightDiagPiece instanceof EmptySpace)){ //white pieces can only capture black pieces
						availableMoves.add(new Move(this, newRow, potentialCol2, parentBoard));
						captureMovesFound = true;
					}
				}
			}
		}
		//black pieces:
		else{
			int newRow = this.location[0] + 1;
			if (newRow >= 0 && newRow < 5){
				if (potentialCol1 >= 0){ //check left diagonal capture
					Piece leftDiagPiece = parentBoard.getPieceAt(newRow, potentialCol1);
					if (leftDiagPiece.alignment == Board.WHITE && !(leftDiagPiece instanceof EmptySpace)){
						availableMoves.add(new Move(this, newRow, potentialCol1, parentBoard));
						captureMovesFound = true;
					}
				}
				if (potentialCol2 < 5){ //check right diagonal capture
					Piece rightDiagPiece = parentBoard.getPieceAt(newRow, potentialCol2);
					if (rightDiagPiece.alignment == Board.WHITE && !(rightDiagPiece instanceof EmptySpace)){
						availableMoves.add(new Move(this, newRow, potentialCol2, parentBoard));
						captureMovesFound = true;
					}
				}
			}
		}

		// check for other moves, if there are no capture moves
		if (!captureMovesFound){
			int currentCol = location[1];
			if (this.alignment == Board.WHITE){ //if its white then it can move one direction up the board if the space is clear
				int newRow = this.location[0] -1;
				if (newRow >= 0){
					if (parentBoard.getBoardArray()[newRow][currentCol] instanceof EmptySpace){
						availableMoves.add(new Move(this, newRow, currentCol, parentBoard));
					}
				}
			}
			else{ //if it's black then it can move one direction down the board if the space is clear
				int newRow = this.location[0] + 1;
				if(newRow < 5){

					if (parentBoard.getBoardArray()[newRow][currentCol] instanceof EmptySpace){
						availableMoves.add(new Move(this, newRow, currentCol, parentBoard));
					}
				}
			}
		}
	}

	@Override
	protected void loadImage() {
		// TODO Auto-generated method stub
		try {
			if (this.alignment){ //i.e. if white piece then
				this.image = ImageIO.read(new File("resources" + File.separator + "white-pawn.png"));
			}
			else{
				this.image = ImageIO.read(new File("resources" + File.separator + "black-pawn.png"));
			}
		} catch (IOException e) {
			System.out.println("Unable to find resource Image for Pawn");
			image = null;
		}
	}
	@Override
	public int getHeuristicScore(boolean playerAlignment){
		int pieceScore = 2;
    	if (this.alignment != playerAlignment){
    		pieceScore = -pieceScore;
    	}
       
    	return pieceScore + getLocationScore(playerAlignment);
	}

}
