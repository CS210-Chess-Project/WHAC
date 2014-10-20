import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class to represent the horsey.  I for one think we should name him Secretariat.
 * @author Zach
 *
 */
public class Knight extends Piece {

	public Knight(int[] location) {
		super(location);
		// TODO Auto-generated constructor stub
	}

	public Knight(int[] location, boolean alignment, Board parent) {
		super(location, alignment, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void generateAvailableMoves() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadImage() {
		// TODO Auto-generated method stub
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

}
