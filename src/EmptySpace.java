import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Class to represent an empty space on the board
 * @author Zach
 *
 */
public class EmptySpace extends Piece {

	public EmptySpace(int[] location) {
		super(location);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void generateAvailableMoves() {
		// TODO Auto-generated method stub
		this.availableMoves = new ArrayList<int[]>(0); //returns empty list (size 0)
	}

	@Override
	protected void generateCaptureMoves() {
		// TODO Auto-generated method stub
		this.captureMoves = new ArrayList<int[]>(0); //returns empty list (size 0)
	}

	@Override
	protected void paintSelf(int width, int height, Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

}
