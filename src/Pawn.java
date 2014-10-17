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

	public Pawn(int[] location, boolean alignment) {
		super(location, alignment);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void generateAvailableMoves() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void generateCaptureMoves() {
		// TODO Auto-generated method stub
		
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
			System.out.println("Exception thrown");
			image = null;
		}
	}

}
