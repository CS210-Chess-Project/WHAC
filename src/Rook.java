import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class to represent a rook
 * @author Zach
 *
 */
public class Rook extends Piece {

	public Rook(int[] location) {
		super(location);
		// TODO Auto-generated constructor stub
	}

	public Rook(int[] location, boolean alignment) {
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

}
