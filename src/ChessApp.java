import java.awt.EventQueue;

import javax.swing.JFrame;

/**
 * Handles all the fiddly graphical stuff and ties everything together.
 * I used the Eclipse plugin WindowBuilder (or something like that) to generate this class and the components of the GUI
 * @author Zach
 *
 */
public class ChessApp {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChessApp window = new ChessApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChessApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Board board = new Board(true, false);
		BoardDisplay testBoard = new BoardDisplay(frame.getWidth(), frame.getHeight(), board);
		frame.add(testBoard);
	}

}
