import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Font;
import java.util.ArrayList;

/**
 * Handles all the fiddly graphical stuff and ties everything together.
 * It also handles game logic
 * I used the Eclipse plugin WindowBuilder (or something like that) to generate this class and the components of the GUI
 * @author Zach
 *
 */
public class ChessApp {
	//arbitrary constants
	private final boolean WHITE = true;
	private final boolean BLACK = false;

	//variables related to game type/state
	private boolean singlePlayerGame;
	private boolean easyMode;
	private boolean playersMove = true; //true = player 1, false = player 2 or computer
	private boolean playerColor = WHITE;
	private boolean playerGoFirst = true;
	private boolean gameOver = false;

	//variabled related to click events:
	private int pressedX;
	private int pressedY;
	private int pressedRow;
	private int pressedCol;
	private int releasedRow;
	private int releasedCol;
	private int releasedX;
	private int releasedY;


	Piece selectedPiece;

	//variabled for global graphical components
	private BoardDisplay graphicalBoard;

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
		frame.getContentPane().removeAll();
		frame.revalidate();
		frame.repaint();

		JButton easyButton = new JButton();
		easyButton.setFont(new Font("Arial Black", Font.PLAIN, 24));
		easyButton.setText("Beginner mode");
		easyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				easyMode = true;
				if(singlePlayerGame){
					decidePlayerColor();
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
					decidePlayerColor();
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

		//add label
		JLabel title = new JLabel();
		frame.getContentPane().add(title);
		title.setFont(new Font("Arial Black", Font.PLAIN, 24));
		title.setText("Who goes first?");
		title.setBounds(frame.getWidth()/5, 35, frame.getWidth()*3/5, frame.getHeight()/12);

		//replace buttons
		JButton playerButton = new JButton();
		playerButton.setFont(new Font("Arial Black", Font.PLAIN, 24));
		playerButton.setText("Player");
		playerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				playerGoFirst = true;
				playersMove = true;
				startSinglePlayerGame();
			}
		});
		frame.getContentPane().add(playerButton);
		playerButton.setBounds(frame.getWidth()/4, frame.getHeight()/2 - frame.getHeight()/3, frame.getWidth()/2, frame.getHeight()/6);

		JButton compButton = new JButton();
		compButton.setFont(new Font("Arial Black", Font.PLAIN, 24));
		compButton.setText("Computer");
		compButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playerGoFirst = false;
				playersMove = false;
				startSinglePlayerGame();
			}
		});
		frame.getContentPane().add(compButton);
		compButton.setBounds(frame.getWidth()/4, frame.getHeight()/2 + frame.getHeight()/10, frame.getWidth()/2, frame.getHeight()/6);
	}

	private void decidePlayerColor(){
		//clear the frame
		frame.getContentPane().removeAll(); //clear the frame
		frame.revalidate();
		frame.repaint();

		//add label
		JLabel title = new JLabel();
		frame.getContentPane().add(title);
		title.setFont(new Font("Arial Black", Font.PLAIN, 24));
		title.setText("Choose the player's color:");
		title.setBounds(frame.getWidth()/5, 35, frame.getWidth()*3/5, frame.getHeight()/12);

		//replace buttons
		JButton whiteButton = new JButton();
		whiteButton.setFont(new Font("Arial Black", Font.PLAIN, 24));
		whiteButton.setText("White Pieces");
		whiteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				playerColor = WHITE;
				whoGoesFirst();
			}
		});
		frame.getContentPane().add(whiteButton);
		whiteButton.setBounds(frame.getWidth()/4, frame.getHeight()/2 - frame.getHeight()/3, frame.getWidth()/2, frame.getHeight()/6);

		JButton blackButton = new JButton();
		blackButton.setFont(new Font("Arial Black", Font.PLAIN, 24));
		blackButton.setText("Black Pieces");
		blackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playerColor = BLACK;
				whoGoesFirst();
			}
		});
		frame.getContentPane().add(blackButton);
		blackButton.setBounds(frame.getWidth()/4, frame.getHeight()/2 + frame.getHeight()/10, frame.getWidth()/2, frame.getHeight()/6);
	}

	private void startTwoPlayerGame(){
		//clear the frame
		frame.getContentPane().removeAll(); //clear the frame
		frame.revalidate();
		frame.repaint();
		//set relevant constants
		singlePlayerGame = false;
		//create board and wire up listeners
		Board board = new Board(easyMode);
		this.graphicalBoard = new BoardDisplay(frame.getContentPane().getWidth(), frame.getContentPane().getHeight(), board);
		frame.getContentPane().add(graphicalBoard);

		//wire stuff up:
		wireUpMouseListener(graphicalBoard);
	}

	private void wireUpMouseListener(BoardDisplay bd){
		System.out.println("Wiring up stuff");
		bd.addMouseListener(new MouseListener(){

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

				selectedPiece = getPieceClicked(arg0.getX(), arg0.getY(), bd.getBoard());
				boolean legalCondition1 = (playersMove && playerColor==selectedPiece.alignment );
				boolean legalCondition2 = (!playersMove && playerColor!=selectedPiece.alignment);
				if (legalCondition1 || legalCondition2 ){
					//highlight legal moves
					bd.setHighlightedMoves(selectedPiece.getAvailableMoves());
					frame.repaint();
				}
				else{
					selectedPiece = null;
				}

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {	
				if (selectedPiece!= null){
					getLocationReleased(arg0.getX(), arg0.getY(), bd.getBoard()); //sets constants releasedCol and releasedRow
					if (releasedCol == pressedCol && releasedRow == pressedRow){
						//do nothing.  attempted move to same spot
					}
					else{
						//prevent illegal moves:
						boolean moveLegal = false;
						for (Move m:selectedPiece.getAvailableMoves()){
							System.out.println("checking move");
							if (m.getRow() == releasedRow && m.getCol() == releasedCol){
								moveLegal = true;
							}
						}
						if (moveLegal){
							bd.getBoard().makeMove(new Move(selectedPiece, releasedRow, releasedCol));
							bd.repaint();

							playersMove = !playersMove; //make it the other persons move
						}
					}

					//always un-highlight moves
					bd.getHighlightedMoves().clear();
					frame.repaint();
				}
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


		//set corresponding constants
		pressedRow = rowNum;
		pressedCol = colNum;
		pressedX = clickX;
		pressedY = clickY;

		//grab the corresponding piece from board:
		return bd.getBoardArray()[rowNum][colNum];
	}

	private int[] getLocationReleased(int clickX, int clickY, Board bd){
		//translates the x,y coords of a mouse released event into using row, col numbers
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

		//set appropriate release constants
		releasedY = clickY;
		releasedX = clickX;
		releasedRow = rowNum;
		releasedCol = colNum;

		return new int[]{rowNum, colNum};
	}


	private boolean between(int num, int lower, int higher){
		return (lower < num && higher > num);
	}

}
