import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 * Handles all the fiddly graphical stuff and ties everything together.
 * It also handles game logic
 * @author Zach
 *
 */
public class ChessApp {
	//arbitrary constants
	private static final boolean WHITE = true;
	private static final boolean BLACK = false;

	//variables related to game type/state
	private static boolean singlePlayerGame;
	private boolean easyMode;
	private static boolean playersMove = true; //true = player 1, false = player 2 or computer
	private static boolean playerColor = WHITE;
	private boolean playerGoFirst = true;
	private static boolean gameOver = false;
	private static boolean AIinAction = false;
	
	private Board boardBeforePlayerMove;  //stores the board before player's latest move, in case we need to undo

	//variables related to click events:
	private int pressedX;
	private int pressedY;
	private int pressedRow;
	private int pressedCol;
	private int releasedRow;
	private int releasedCol;
	private int releasedX;
	private int releasedY;


	Piece selectedPiece;

	static int maxLookAhead = 4;

	//variables for global graphical components
	private static BoardDisplay graphicalBoard;

	private static JFrame frame;
	private JPanel frameContent;
	private JPanel infoPane;
	private JPanel controlPane;
	private JButton undoMoveBtn;
	private JButton newGameBtn;
	
	static Timer AITimer;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//This timer makes the AI move if the correct flag has been set
		AITimer = new Timer(400,new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!playersMove){
					AIinAction = true;
					graphicalBoard.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					graphicalBoard.setBoard(graphicalBoard.getBoard().getNextMove(!playerColor, maxLookAhead));
					playersMove = true;
					frame.repaint();
					System.out.println("Score after CPU move: " + graphicalBoard.getBoard().evaluateSelf(!playerColor));
					graphicalBoard.setCursor(Cursor.getDefaultCursor());
					AIinAction = false;
					//check for game over states:
					if (graphicalBoard.getBoard().isGameOver(playerColor)){
						System.out.println("GAME OVER!");
						gameOver = true;
					}
				}
			}			
		});

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChessApp window = new ChessApp();
					frame.setVisible(true);
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
		initialize(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(boolean newFrame) {
		if(newFrame){
			frame = new JFrame();
		}
		frame.setBounds(0, 0, 1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		Border panelBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		
		
		frameContent = new JPanel();
		frame.getContentPane().add(frameContent);
		frameContent.setBounds(10, 10, 700, 640);
		frameContent.setLayout(null);
		frameContent.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		infoPane = new JPanel();
		frame.getContentPane().add(infoPane);
		infoPane.setBounds(720, 10, 250, 310);
		infoPane.setLayout(null);
		infoPane.setBorder(BorderFactory.createTitledBorder(panelBorder, "Info"));
		
		controlPane = new JPanel();
		frame.getContentPane().add(controlPane);
		controlPane.setBounds(720, 340, 250, 310);
		controlPane.setLayout(null);
		controlPane.setBorder(BorderFactory.createTitledBorder(panelBorder, "Controls"));
		
		choosePlayerCount();
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
		graphicalBoard = new BoardDisplay(frameContent.getWidth(), frameContent.getHeight(), board);
		frameContent.add(graphicalBoard);
		frame.repaint();

		//wire up stuff:
		wireUpMouseListener(graphicalBoard);
		setupControlPane();
		
		//start AI movement:
		AITimer.start();
	}

	private void whoGoesFirst(){
		clearContentPane();

		//add label
		JLabel title = new JLabel("Who goes first?", SwingConstants.CENTER);
		frameContent.add(title);
		title.setFont(new Font("Arial Black", Font.PLAIN, 24));
		title.setBounds(0, 35, frameContent.getWidth(), frameContent.getHeight()/12);

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
		JLabel title = new JLabel("Choose the player's color:", SwingConstants.CENTER);
		frameContent.add(title);
		title.setFont(new Font("Arial Black", Font.PLAIN, 24));
		title.setBounds(0, 35, frameContent.getWidth(), frame.getHeight()/12);

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
		setupControlPane();
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
							//make deep copy of board so we can revert if we have to:
							boardBeforePlayerMove = new Board(bd.getBoard().getBoardArray());
							
							bd.getBoard().makeMove(new Move(selectedPiece, releasedRow, releasedCol, bd.getBoard()));
							bd.repaint();
							playersMove = !playersMove;
							System.out.println("PlayersMove: " + playersMove);
							undoMoveBtn.setEnabled(true);
							//TODO: notify user that AI is making move
							//DEBUG
							System.out.println("Score after player's turn: " + bd.getBoard().evaluateSelf(!playerColor));
							if (graphicalBoard.getBoard().isGameOver(!playerColor)){
								System.out.println("GAME OVER!");
								gameOver= true;
							}

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
	
	private void setupControlPane(){
		//Add new game button:
		newGameBtn = new JButton("New Game");
		controlPane.add(newGameBtn);
		newGameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newGame();
			}
		});
		newGameBtn.setBounds(controlPane.getWidth()/10, 40, controlPane.getWidth()*4/5, 80);
		//Add undo move button:
		undoMoveBtn = new JButton("Undo Last Move");
		controlPane.add(undoMoveBtn);
		undoMoveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				undoLatestMove();
			}
		});
		undoMoveBtn.setBounds(controlPane.getWidth()/10, 130, controlPane.getWidth()*4/5, 80);
		undoMoveBtn.setEnabled(false); //disable button until move is made
	}
	
	private void newGame(){
		AITimer.stop();
		
		//frame.dispose();
		frame.getContentPane().removeAll();
		initialize(false);
	}
	
	//undoes the latest player-made move
	private void undoLatestMove(){
		if(singlePlayerGame){
			if(AIinAction){ //force player to wait until AI makes move
				//TODO notify user to wait until AI move is done
			}
			else{
				graphicalBoard.setBoard(boardBeforePlayerMove);
				graphicalBoard.repaint();
				playersMove = true;
			}
		}
		else{
			playersMove = !playersMove;
			graphicalBoard.setBoard(boardBeforePlayerMove);
			graphicalBoard.repaint();
		}
		
		undoMoveBtn.setEnabled(false); //disable button until another move is made
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
