import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Class to represent the horsey.  I for one think we should name him Secretariat.
 * @author Zach
 *
 */
public class Knight extends Piece {

	public Knight(int[] location) {
		super(location);
	}

	public Knight(int[] location, boolean alignment, Board parent) {
		super(location, alignment, parent);
	}

	/**
	 * This is pretty long, ugly, and brute-forcy but I couldn't think of a better way to check knight moves
	 */
	protected void generateAvailableMoves() {
		ArrayList<Move> captureMoves = new ArrayList<Move>();
		ArrayList<Move> otherMoves = new ArrayList<Move>();

		int proposedCol1 = location[1] - 1;
		int proposedCol2 = location[1] + 1;		

		//check two-up, one over moves:
		int proposedRow = location[0] - 2;
		if (proposedRow >= 0 && proposedRow <5){//bounds checking
			if (proposedCol1 >= 0 && proposedCol1 <5){ //bounds checking
				if (parentBoard.getBoardArray()[proposedRow][proposedCol1] instanceof EmptySpace){ //can move to empty spaces
					otherMoves.add(new Move(this, proposedRow, proposedCol1, parentBoard));
				}
				else if(parentBoard.getBoardArray()[proposedRow][proposedCol1].alignment == !this.alignment){ //can also capture opposite pieces
					captureMoves.add(new Move(this, proposedRow, proposedCol1, parentBoard));
				}
			}
			if (proposedCol2 >= 0 && proposedCol2 <5){ //bounds checking
				if (parentBoard.getBoardArray()[proposedRow][proposedCol2] instanceof EmptySpace){ //can move to empty spaces
					otherMoves.add(new Move(this, proposedRow, proposedCol2, parentBoard));
				}
				else if(parentBoard.getBoardArray()[proposedRow][proposedCol2].alignment == !this.alignment){ //can also capture opposite pieces
					captureMoves.add(new Move(this, proposedRow, proposedCol2, parentBoard));
				}
			}
		}
		//check two down, one over
		proposedRow = location[0] + 2;
		if (proposedRow >= 0 && proposedRow <5){//bounds checking
			if (proposedCol1 >= 0 && proposedCol1 <5){ //bounds checking
				if (parentBoard.getBoardArray()[proposedRow][proposedCol1] instanceof EmptySpace){ //can move to empty spaces
					otherMoves.add(new Move(this, proposedRow, proposedCol1, parentBoard));
				}
				else if(parentBoard.getBoardArray()[proposedRow][proposedCol1].alignment == !this.alignment){ //can also capture opposite pieces
					captureMoves.add(new Move(this, proposedRow, proposedCol1, parentBoard));
				}
			}
			if (proposedCol2 >= 0 && proposedCol2 <5){ //bounds checking
				if (parentBoard.getBoardArray()[proposedRow][proposedCol2] instanceof EmptySpace){ //can move to empty spaces
					otherMoves.add(new Move(this, proposedRow, proposedCol2, parentBoard));
				}
				else if(parentBoard.getBoardArray()[proposedRow][proposedCol2].alignment == !this.alignment){ //can also capture opposite pieces
					captureMoves.add(new Move(this, proposedRow, proposedCol2, parentBoard));
				}
			}
		}


		proposedCol1 = location[1] + 2;
		proposedCol2 = location[1] - 2;
		//check one up, two over
		proposedRow = location[0] - 1;
		if (proposedRow >= 0 && proposedRow <5){//bounds checking
			if (proposedCol1 >= 0 && proposedCol1 <5){ //bounds checking
				if (parentBoard.getBoardArray()[proposedRow][proposedCol1] instanceof EmptySpace){ //can move to empty spaces
					otherMoves.add(new Move(this, proposedRow, proposedCol1, parentBoard));
				}
				else if(parentBoard.getBoardArray()[proposedRow][proposedCol1].alignment == !this.alignment){ //can also capture opposite pieces
					captureMoves.add(new Move(this, proposedRow, proposedCol1, parentBoard));
				}
			}
			if (proposedCol2 >= 0 && proposedCol2 <5){ //bounds checking
				if (parentBoard.getBoardArray()[proposedRow][proposedCol2] instanceof EmptySpace){ //can move to empty spaces
					otherMoves.add(new Move(this, proposedRow, proposedCol2, parentBoard));
				}
				else if(parentBoard.getBoardArray()[proposedRow][proposedCol2].alignment == !this.alignment){ //can also capture opposite pieces
					captureMoves.add(new Move(this, proposedRow, proposedCol2, parentBoard));
				}
			}
		}

		//check one down, two over
		proposedRow = location[0] + 1;
		if (proposedRow >= 0 && proposedRow <5){//bounds checking
			if (proposedCol1 >= 0 && proposedCol1 <5){ //bounds checking
				if (parentBoard.getBoardArray()[proposedRow][proposedCol1] instanceof EmptySpace){ //can move to empty spaces
					otherMoves.add(new Move(this, proposedRow, proposedCol1, parentBoard));
				}
				else if(parentBoard.getBoardArray()[proposedRow][proposedCol1].alignment == !this.alignment){ //can also capture opposite pieces
					captureMoves.add(new Move(this, proposedRow, proposedCol1, parentBoard));
				}
			}
			if (proposedCol2 >= 0 && proposedCol2 <5){ //bounds checking
				if (parentBoard.getBoardArray()[proposedRow][proposedCol2] instanceof EmptySpace){ //can move to empty spaces
					otherMoves.add(new Move(this, proposedRow, proposedCol2, parentBoard));
				}
				else if(parentBoard.getBoardArray()[proposedRow][proposedCol2].alignment == !this.alignment){ //can also capture opposite pieces
					captureMoves.add(new Move(this, proposedRow, proposedCol2, parentBoard));
				}
			}
		}

		if (captureMoves.isEmpty()){
			this.availableMoves = otherMoves;
		}
		else this.availableMoves = captureMoves;

	}

	@Override
	protected void loadImage() {
		try {
			if (this.alignment){ //i.e. if white piece then
				this.image = ImageIO.read(new File("resources" + File.separator + "white-knight.png"));
			}
			else{
				this.image = ImageIO.read(new File("resources" + File.separator + "black-knight.png"));
			}
		} catch (IOException e) {
			System.out.println("Exception thrown");
			image = null;
		}
	}
	@Override
	public int getHeuristicScore(boolean playerAlignment){
		int pieceScore = 1;
    	if (this.alignment != playerAlignment){
    		pieceScore = -pieceScore;
    	}
       
    	return pieceScore + getLocationScore(playerAlignment);
	}

}
