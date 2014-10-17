import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

/**
 * Handles all the fiddly graphical stuff and ties everything together.
 * It also handles game logic
 * I used the Eclipse plugin WindowBuilder (or something like that) to generate this class and the components of the GUI
 * @author Zach
 *
 */
public class ChessApp {

	private boolean singlePlayerGame;
	private boolean easyMode;
	private boolean playersMove = true; //true = player 1, false = player 2 or computer
	private boolean playerGoFirst = true;

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
		choosePlayerCount();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
	}

	private void choosePlayerCount(){		
		JButton singlePlayerButton = new JButton();
		singlePlayerButton.setFont(new Font("Arial Black", Font.PLAIN, 24));
		singlePlayerButton.setText("Single Player");
		singlePlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				singlePlayerGame = true;
				easyOrAdvanced();
			}
		});
		frame.getContentPane().add(singlePlayerButton);
		singlePlayerButton.setBounds(frame.getWidth()/4, frame.getHeight()/2 - frame.getHeight()/3, frame.getWidth()/2, frame.getHeight()/6);

		JButton twoPlayerButton = new JButton();
		twoPlayerButton.setFont(new Font("Arial Black", Font.PLAIN, 24));
		twoPlayerButton.setText("Two Player");
		twoPlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				singlePlayerGame = false;
				easyOrAdvanced();
			}
		});
		frame.getContentPane().add(twoPlayerButton);
		twoPlayerButton.setBounds(frame.getWidth()/4, frame.getHeight()/2 + frame.getHeight()/10, frame.getWidth()/2, frame.getHeight()/6);

	}

	private void easyOrAdvanced(){
		//clear the frame
		frame.getContentPane().removeAll(); //clear the frame
		frame.revalidate();
		frame.repaint();

		JButton easyButton = new JButton();
		easyButton.setFont(new Font("Arial Black", Font.PLAIN, 24));
		easyButton.setText("Beginner mode");
		easyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				easyMode = true;
				if(singlePlayerGame){
					whoGoesFirst();;
				}
				else{ startTwoPlayerGame();	}
			}
		});
		frame.getContentPane().add(easyButton);
		easyButton.setBounds(frame.getWidth()/4, frame.getHeight()/2 - frame.getHeight()/3, frame.getWidth()/2, frame.getHeight()/6);

		JButton advancedButton = new JButton();
		advancedButton.setFont(new Font("Arial Black", Font.PLAIN, 24));
		advancedButton.setText("Advanced Mode");
		advancedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				easyMode = false;
				if(singlePlayerGame){
					whoGoesFirst();
				}
				else{ startTwoPlayerGame();}
			}
		});
		frame.getContentPane().add(advancedButton);
		advancedButton.setBounds(frame.getWidth()/4, frame.getHeight()/2 + frame.getHeight()/10, frame.getWidth()/2, frame.getHeight()/6);
	}

	private void startSinglePlayerGame(){
		//clear the frame
		frame.getContentPane().removeAll(); //clear the frame
		frame.revalidate();
		frame.repaint();

		//TODO the rest of the single player logic
	}

	private void whoGoesFirst(){
		//clear the frame
		frame.getContentPane().removeAll(); //clear the frame
		frame.revalidate();
		frame.repaint();

		//replace buttons
		JButton player1Button = new JButton();
		player1Button.setFont(new Font("Arial Black", Font.PLAIN, 24));
		player1Button.setText("Player");
		player1Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				playerGoFirst = true;
				playersMove = true;
				startSinglePlayerGame();
			}
		});
		frame.getContentPane().add(player1Button);
		player1Button.setBounds(frame.getWidth()/4, frame.getHeight()/2 - frame.getHeight()/3, frame.getWidth()/2, frame.getHeight()/6);

		JButton player2Button = new JButton();
		player2Button.setFont(new Font("Arial Black", Font.PLAIN, 24));
		player2Button.setText("Computer");
		player2Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playerGoFirst = false;
				playersMove = false;
				startSinglePlayerGame();
			}
		});
		frame.getContentPane().add(player2Button);
		player2Button.setBounds(frame.getWidth()/4, frame.getHeight()/2 + frame.getHeight()/10, frame.getWidth()/2, frame.getHeight()/6);
	}

	private void startTwoPlayerGame(){
		//clear the frame
		frame.getContentPane().removeAll(); //clear the frame
		frame.revalidate();
		frame.repaint();
		//set relevant constants
		singlePlayerGame = false;
		//create board and wire up listeners
		Board board = new Board(true, easyMode);
		BoardDisplay graphicalBoard = new BoardDisplay(frame.getContentPane().getWidth(), frame.getContentPane().getHeight(), board);
		frame.getContentPane().add(graphicalBoard);
	}

}
