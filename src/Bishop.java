import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class to represent Bishop
 * @author Zach
 *
 */
public class Bishop extends Piece {

	public Bishop(int[] location) {
		super(location);
		// TODO Auto-generated constructor stub
	}

	public Bishop(int[] location, boolean alignment) {
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

}
