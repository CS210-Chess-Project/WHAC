import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
	
	int maxLookAhead =4;

	//variables for global graphical components
	private BoardDisplay graphicalBoard;

	private JFrame frame;
	private JPanel frameContent;
	private JPanel infoPane;

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
		frame.setBounds(0, 0, 1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		frameContent = new JPanel();
		frame.getContentPane().add(frameContent);
		frameContent.setBounds(10, 10, 700, 640);
		frameContent.setLayout(null);

		infoPane = new JPanel();
		frame.getContentPane().add(infoPane);
		infoPane.setBounds(710, 10, 300, 640);
		infoPane.setLayout(null);
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
		frameContent.add(singlePlayerButton);
		singlePlayerButton.setBounds(frameContent.getWidth()/4, frameContent.getHeight()/2 - frameContent.getHeight()/3, frameContent.getWidth()/2, frameContent.getHeight()/6);

		JButton twoPlayerButton = new JButton();
		twoPlayerButton.setFont(new Font("Arial Black", Font.PLAIN, 24));
		twoPlayerButton.setText("Two Player");
		twoPlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				singlePlayerGame = false;
				easyOrAdvanced();
			}
		});
		frameContent.add(twoPlayerButton);
		twoPlayerButton.setBounds(frameContent.getWidth()/4, frameContent.getHeight()/2 + frameContent.getHeight()/10, frameContent.getWidth()/2, frameContent.getHeight()/6);

	}

	private void easyOrAdvanced(){
		//clear the contentPane
		//		frame.getContentPane().removeAll();
		//		frame.revalidate();
		//		frame.repaint();
		clearContentPane();

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
		frameContent.add(easyButton);
		easyButton.setBounds(frameContent.getWidth()/4, frameContent.getHeight()/2 - frameContent.getHeight()/3, frameContent.getWidth()/2, frameContent.getHeight()/6);

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
		frameContent.add(advancedButton);
		advancedButton.setBounds(frameContent.getWidth()/4, frameContent.getHeight()/2 + frameContent.getHeight()/10, frameContent.getWidth()/2, frameContent.getHeight()/6);
	}

	private void startSinglePlayerGame(){
		clearContentPane();

		//set relevant constants
		singlePlayerGame = true;

		//create board and wire up listeners
		Board board = new Board(easyMode);
		this.graphicalBoard = new BoardDisplay(frameContent.getWidth(), frameContent.getHeight(), board);
		frameContent.add(graphicalBoard);
		frame.repaint();

		//wire stuff up:
		wireUpMouseListener(graphicalBoard);

		if (!playerGoFirst){
			//make AI move:
			graphicalBoard.setBoard(board.getNextMove(!playerColor, maxLookAhead));
			
			//change constants:
			playersMove = true;
		}
		//TODO the rest of the single player logic
	}

	private void whoGoesFirst(){
		clearContentPane();

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
		frameContent.add(playerButton);
		playerButton.setBounds(frameContent.getWidth()/4, frameContent.getHeight()/2 - frameContent.getHeight()/3, frameContent.getWidth()/2, frameContent.getHeight()/6);

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
		frameContent.add(compButton);
		compButton.setBounds(frameContent.getWidth()/4, frameContent.getHeight()/2 + frameContent.getHeight()/10, frameContent.getWidth()/2, frameContent.getHeight()/6);
	}

	private void decidePlayerColor(){
		clearContentPane();

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
		frameContent.add(whiteButton);
		whiteButton.setBounds(frameContent.getWidth()/4, frameContent.getHeight()/2 - frameContent.getHeight()/3, frameContent.getWidth()/2, frameContent.getHeight()/6);

		JButton blackButton = new JButton();
		blackButton.setFont(new Font("Arial Black", Font.PLAIN, 24));
		blackButton.setText("Black Pieces");
		blackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playerColor = BLACK;
				whoGoesFirst();
			}
		});
		frameContent.add(blackButton);
		blackButton.setBounds(frameContent.getWidth()/4, frameContent.getHeight()/2 + frameContent.getHeight()/10, frameContent.getWidth()/2, frameContent.getHeight()/6);
	}

	private void startTwoPlayerGame(){
		clearContentPane();

		//set relevant constants
		singlePlayerGame = false;

		//create board and wire up listeners
		Board board = new Board(easyMode);
		this.graphicalBoard = new BoardDisplay(frameContent.getWidth(), frameContent.getHeight(), board);
		frameContent.add(graphicalBoard);

		//wire stuff up:
		wireUpMouseListener(graphicalBoard);
	}

	private void wireUpMouseListener(BoardDisplay bd){
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
					boolean correctColor;
					if (playersMove){
						correctColor = playerColor;
					}
					else{
						correctColor = !playerColor;
					}
					getLocationReleased(arg0.getX(), arg0.getY(), bd.getBoard()); //sets constants releasedCol and releasedRow
					if(selectedPiece.alignment != correctColor){
						//disallow move: not your turn
					}
					else if (releasedCol == pressedCol && releasedRow == pressedRow ){ //failing conditions for a move
						//do nothing. attempted move to same location
					}
					else{
						//prevent illegal moves:
						boolean moveLegal = false;
						for (Move m:selectedPiece.getAvailableMoves()){
							if (m.getRow() == releasedRow && m.getCol() == releasedCol){ // is the proposed move a member of available moves?
								//is the proposed move a capture move? then it is always legal
								if (m.isCaptureMove()){
									moveLegal = true;
								}
								// is it not a capture move? then it is only legal if there are no other capture moves on the board for this color
								else{
									if (!bd.getBoard().captureMovesAvailable(currentPlayersColor())){
										moveLegal = true;
									}
								}
							}
						}
						if (moveLegal){
							bd.getBoard().makeMove(new Move(selectedPiece, releasedRow, releasedCol, bd.getBoard()));
							bd.repaint();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							playersMove = !playersMove; //make it the other persons move
							
							//if its singleplayer, make AI move:
							//TODO: notify user that AI is making move
							//make AI move:
							graphicalBoard.setBoard(graphicalBoard.getBoard().getNextMove(!playerColor, maxLookAhead));
							
							//change constants:
							playersMove = true;
							
						}
						else{
							//TODO: notify the user that this move isn't legal
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
		if (between(xClicked, 0, frameContent.getWidth()/5)){ colNum = 0;}
		else if (between(xClicked, frameContent.getWidth()/5, frameContent.getWidth()*2/5)){ colNum = 1;}
		else if (between(xClicked, frameContent.getWidth()*2/5, frameContent.getWidth()*3/5)){ colNum = 2;}
		else if (between(xClicked, frameContent.getWidth()*3/5, frameContent.getWidth()*4/5)){ colNum = 3;}
		else if (between(xClicked, frameContent.getWidth()*4/5, frameContent.getWidth()*5/5)){ colNum = 4;}
		//determine rowNum from y coord
		if (between(yClicked, 0, frameContent.getHeight()/5)){ rowNum = 0;}
		else if(between(yClicked, frameContent.getHeight()/5, frameContent.getHeight()*2/5)){ rowNum = 1;}
		else if(between(yClicked, frameContent.getHeight()*2/5, frameContent.getHeight()*3/5)){ rowNum = 2;}
		else if(between(yClicked, frameContent.getHeight()*3/5, frameContent.getHeight()*4/5)){ rowNum = 3;}
		else if(between(yClicked, frameContent.getHeight()*4/5, frameContent.getHeight()*5/5)){ rowNum = 4;}


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
		if (between(xClicked, 0, frameContent.getWidth()/5)){ colNum = 0;}
		else if (between(xClicked, frameContent.getWidth()/5, frameContent.getWidth()*2/5)){ colNum = 1;}
		else if (between(xClicked, frameContent.getWidth()*2/5, frameContent.getWidth()*3/5)){ colNum = 2;}
		else if (between(xClicked, frameContent.getWidth()*3/5, frameContent.getWidth()*4/5)){ colNum = 3;}
		else if (between(xClicked, frameContent.getWidth()*4/5, frameContent.getWidth()*5/5)){ colNum = 4;}
		//determine rowNum from y coord
		if (between(yClicked, 0, frameContent.getHeight()/5)){ rowNum = 0;}
		else if(between(yClicked, frameContent.getHeight()/5, frameContent.getHeight()*2/5)){ rowNum = 1;}
		else if(between(yClicked, frameContent.getHeight()*2/5, frameContent.getHeight()*3/5)){ rowNum = 2;}
		else if(between(yClicked, frameContent.getHeight()*3/5, frameContent.getHeight()*4/5)){ rowNum = 3;}
		else if(between(yClicked, frameContent.getHeight()*4/5, frameContent.getHeight()*5/5)){ rowNum = 4;}

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

	private boolean currentPlayersColor(){
		if (playersMove){
			return playerColor;
		}
		else{
			return !playerColor;
		}
	}

	private void clearContentPane(){
		frameContent.removeAll();
		frameContent.revalidate();
		frameContent.repaint();		
	}
}
