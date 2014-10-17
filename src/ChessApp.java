import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
		
		//wire stuff up:
		wireUpMouseListener(graphicalBoard);
	}
	
	private void wireUpMouseListener(BoardDisplay bd){
		System.out.println("Wiring up stuff");
		bd.addMouseListener(new MouseListener(){
			
			Piece selectedPiece;
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// do nothing
			}
			public void mouseEntered(MouseEvent arg0) {
				//do nothing
			}
			public void mouseExited(MouseEvent arg0) {
				//do nothing
			}
			
			//click down event.  Get and highlight available moves.
			public void mousePressed(MouseEvent arg0) {
				Piece pieceClicked = getPieceClicked(arg0.getX(), arg0.getY(), bd.getBoard());
				String color = "White";
				if (!pieceClicked.getAlignment()){
					color = "Black";
				}
				System.out.println("Clicked Piece: " + color + " " + pieceClicked.toString());
				
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
	}
	
	private Piece getPieceClicked(int clickX, int clickY, Board bd){
		int xClicked = clickX;
		int yClicked = clickY;
		int colNum = 0;
		int rowNum = 0;
		//determine colNum from x coordinate clicked
		if (between(xClicked, 0, frame.getWidth()/5)){ colNum = 0;}
		else if (between(xClicked, frame.getWidth()/5, frame.getWidth()*2/5)){ colNum = 1;}
		else if (between(xClicked, frame.getWidth()*2/5, frame.getWidth()*3/5)){ colNum = 2;}
		else if (between(xClicked, frame.getWidth()*3/5, frame.getWidth()*4/5)){ colNum = 3;}
		else if (between(xClicked, frame.getWidth()*4/5, frame.getWidth()*5/5)){ colNum = 4;}
		//determine rowNum from y coord
		if (between(yClicked, 0, frame.getHeight()/5)){ rowNum = 0;}
		else if(between(yClicked, frame.getHeight()/5, frame.getHeight()*2/5)){ rowNum = 1;}
		else if(between(yClicked, frame.getHeight()*2/5, frame.getHeight()*3/5)){ rowNum = 2;}
		else if(between(yClicked, frame.getHeight()*3/5, frame.getHeight()*4/5)){ rowNum = 3;}
		else if(between(yClicked, frame.getHeight()*4/5, frame.getHeight()*5/5)){ rowNum = 4;}
		
		//grab the corresponding piece from board:
		return bd.getBoardArray()[rowNum][colNum];
	}
	

	private boolean between(int num, int lower, int higher){
		return (lower < num && higher > num);
	}

}
