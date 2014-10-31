import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	
	//handy device-specific variables
	private static final double _W = java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private static final double _H = java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	//variables related to game type/state
	private static boolean singlePlayerGame;
	private boolean easyMode;
	private static boolean playersMove = true; //true = player 1, false = player 2 or computer
	private static boolean playerColor = WHITE;
	private static boolean winningColor;

	Stack<Board> moveHistory = new Stack<Board>();
	private static Board boardBeforeAIMove;

	//variables related to click events:
	private int pressedRow;
	private int pressedCol;
	private int releasedRow;
	private int releasedCol;


	Piece selectedPiece;

	static int maxLookAhead = 5;

	//variables for global graphical components
	private static BoardDisplay graphicalBoard;

	private static JFrame frame;
	private static JPanel frameContent;
	private JPanel infoPane;
	private BoardDisplay overlayPane;
	private JPanel controlPane;
	private static JButton undoMoveBtn;
	private JButton newGameBtn;
	private static JButton boardBeforeAIMoveBtn;
	
	private static JLabel infoLabel;
	private static String CPUMoveText = "<html>Status: CPU turn.</html>";
	private static String PlayersMoveText = "<html>Status: Your turn.</html>";
	private static String player1Text = "<html>Status: Player 1's turn.</html>";
	private static String player2Text = "<html>Status: Player 2's turn.</html>";
	private static String gameOverText = "<html>Status: Game Over!</html>";
	
	private static JLabel notificationLabel;

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
					//save board:

					//pre-move tasks:
					boardBeforeAIMove = graphicalBoard.getBoard();
					graphicalBoard.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					//compute and make move::
					graphicalBoard.setBoard(graphicalBoard.getBoard().getNextMove(!playerColor, maxLookAhead));
					//do after-move tasks:
					playersMove = true;										
					graphicalBoard.setCursor(Cursor.getDefaultCursor());
					boardBeforeAIMoveBtn.setEnabled(true);
					infoLabel.setText(PlayersMoveText);
					frame.repaint();

					//check for game over states:
					if (graphicalBoard.getBoard().isGameOver(playerColor)){
						winningColor = graphicalBoard.getBoard().determineWinningColor();
						gameOver();
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

	/**
	 * Show screen to choose single or multiplayer
	 */
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
	
	/**
	 * Show screen to choose beginner mode or advanced mode
	 */
	private void easyOrAdvanced(){
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
		setupInfoPane();

		//start AI movement:
		AITimer.start();
	}

	/**
	 * Show screen to choose who goes first
	 */
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
				//playerGoFirst = true;
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
				//playerGoFirst = false;
				playersMove = false;
				startSinglePlayerGame();
			}
		});
		frameContent.add(compButton);
		compButton.setBounds(frameContent.getWidth()/4, frameContent.getHeight()/2 + frameContent.getHeight()/10, frameContent.getWidth()/2, frameContent.getHeight()/6);
	}

	/**
	 * Show screen to choose what color the player will control
	 */
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
		graphicalBoard = new BoardDisplay(frameContent.getWidth(), frameContent.getHeight(), board);
		frameContent.add(graphicalBoard);

		//wire stuff up:
		wireUpMouseListener(graphicalBoard);
		setupControlPane();
		setupInfoPane();
	}

	//helper method to add mouse listener to the board
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
							//boardBeforePlayerMove = new Board(bd.getBoard().getBoardArray());
							moveHistory.push(new Board(bd.getBoard().getBoardArray()));

							bd.getBoard().makeMove(new Move(selectedPiece, releasedRow, releasedCol, bd.getBoard()));
							bd.repaint();
							playersMove = !playersMove;
							undoMoveBtn.setEnabled(true);
							if (graphicalBoard.getBoard().isGameOver(!playerColor)){
								//gameOver= true;

								winningColor = graphicalBoard.getBoard().determineWinningColor();
								gameOver();
							}
							
							//set status label to appropriate message:
							if(singlePlayerGame){
								infoLabel.setText(CPUMoveText);
							}
							else if(playersMove){
								infoLabel.setText(player1Text);
							}
							else{
								infoLabel.setText(player2Text);
							}		
							notificationLabel.setText("");

						}
						else{
							notificationLabel.setText("<html>Illegal move. You have capture moves available.</html>");
						}
						
					}

					//always un-highlight moves
					bd.getHighlightedMoves().clear();
					frame.repaint();
				}
			}
		});
	}

	/**
	 * Helper method to populate the control panel
	 */
	private void setupControlPane(){
		//Add new game button:
		newGameBtn = new JButton("New Game");
		controlPane.add(newGameBtn);
		newGameBtn.setFont(new Font("Arial Black", Font.PLAIN, 15));
		newGameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newGame();
			}
		});
		newGameBtn.setBounds(controlPane.getWidth()/10, 40, controlPane.getWidth()*4/5, 80);

		//Add undo move button:
		undoMoveBtn = new JButton("<html><center>Undo Last Move</center></html>");
		controlPane.add(undoMoveBtn);
		undoMoveBtn.setFont(new Font("Arial Black", Font.PLAIN, 15));
		undoMoveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				undoLatestMove();
			}
		});
		undoMoveBtn.setBounds(controlPane.getWidth()/10, 130, controlPane.getWidth()*4/5, 80);
		undoMoveBtn.setEnabled(false); //disable button until move is made

		//add "board before AI move" button:
		if(singlePlayerGame){
			boardBeforeAIMoveBtn = new JButton("<html><center>See Board <br>Before AI Move</center></html>");
			controlPane.add(boardBeforeAIMoveBtn);
			boardBeforeAIMoveBtn.setFont(new Font("Arial Black", Font.PLAIN, 15));
			boardBeforeAIMoveBtn.setAlignmentX((float) 0.5);
			boardBeforeAIMoveBtn.addMouseListener(new MouseAdapter(){
				public void mousePressed(MouseEvent e){
					if (overlayPane == null){
						overlayPane = new BoardDisplay(frameContent.getWidth(), frameContent.getHeight(), boardBeforeAIMove);
					}
					else{
						overlayPane.setBoard(boardBeforeAIMove);
					}
					frame.getContentPane().add(overlayPane);
					overlayPane.setBounds(frameContent.getBounds());
					frameContent.setVisible(false);
					overlayPane.setVisible(true);
				}

				public void mouseReleased(MouseEvent e){
					overlayPane.setVisible(false);
					frameContent.setVisible(true);
				}
			});
			boardBeforeAIMoveBtn.setBounds(controlPane.getWidth()/10, 220, controlPane.getWidth()*4/5, 80);
			boardBeforeAIMoveBtn.setEnabled(false); //disable button until move is made
		}
	}

	/**
	 * Helper method to populate the info panel
	 */
	private void setupInfoPane(){
		JButton helpBtn = new JButton("Help");
		infoPane.add(helpBtn);
		helpBtn.setFont(new Font("Arial Black", Font.PLAIN, 15));
		helpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Math.random()<=0.3){ //30% of the time, just display some silly message
					JOptionPane.showMessageDialog(frame, "WHAC help services are currently closed.  \nPlease call sometime between the hours of 11:00 and 11:30 Monday through Friday, except not Fridays or Mondays, \nand only on the 3rd Wednesday of every 5th month and Tuesdays that happen to fall on the 13th day of September.  \nFeel Free to try on Thursday though.");
				}
				else{
					JFrame popup = new JFrame();
					popup.setBounds((int)_W/4, (int)_H/4, (int)_W/2, (int)_H/2);
					popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					popup.setVisible(true);
					JLabel instructionLabel = new JLabel();
					instructionLabel.setText("<html><div style=\"text-align: center;font-size: 12px;\">The Game of WHAC was invented by an shrimp-loving Indonesian sherpa around 1800 B.C.  It has since gained national appeal, attracting the attention of some of the best players and logisticians in the world, including Vince Gilligan, Gilligan from Gilligan's Island, the Beastie Boys, Nathan Fillion, Tony the Tiger, and the Monty Python Troupe.  The game is often played in a setting of utter tranquility, with Enya music playing in the background accompanied by a live harpist.  <br>The rules are as follows:"
							+ "<ulstyle=\"text-align: left;font-size: 12px;\">"
							+ "<li>Masochistic war: the first player to lose all his pieces wins.</li>"
							+ "<li>Masochistic stalemates: In the event of a stalemate, the player with the fewest pieces wins.</li>"
							+ "<li>Stalemated Stalemates: If a stalemate is reached and both players have the same number of pieces, the player with the most advanced nuclear weapon wins</li>"
							+ "<li>Sadistic Movement: If a capture is a available, it must be taken.  If two or more such captures are avaiable, the player is to chose one and give the opponent's piece a quick and brutal death as an example to the others</li>"
							+ "<li>?????????????????</li>"
							+ "<li>Philandering, horseplay, fidgeting, tomfoolery, extortion, and money laundering are strictly prohibited.</li>"
							+ "</ul>"
							+ "</div></html>");
					popup.add(instructionLabel);
				}
				
			}
		});
		helpBtn.setBounds(infoPane.getWidth()/10, 160, infoPane.getWidth()*4/5, 80);
		
		//setup info box:
		String initialText = "";
		if(singlePlayerGame && playersMove){
			 initialText = PlayersMoveText;
		}
		else if(singlePlayerGame && !playersMove){
			initialText = CPUMoveText;
		}
		else if(!singlePlayerGame && playersMove){
			initialText = player1Text;
		}
		else{
			initialText = player2Text;
		}
		
		infoLabel = new JLabel(initialText);
		infoLabel.setBounds(20, 20, infoPane.getWidth() - 40, infoPane.getHeight()/8);
		infoLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
		infoPane.add(infoLabel);
		
		JLabel notificationTitleLabel = new JLabel("Notifications:");
		notificationTitleLabel.setBounds(20, 15 + infoPane.getHeight()/8, infoPane.getWidth() - 40, 30);
		notificationTitleLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
		infoPane.add(notificationTitleLabel);
		
		
		notificationLabel = new JLabel("");
		notificationLabel.setBounds(20,  + 45 + infoPane.getHeight()/8, infoPane.getWidth() - 40, infoPane.getHeight()/5);
		notificationLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
		infoPane.add(notificationLabel);
		
	}

	/**
	 * method to handle things when the game ends
	 */
	private static void gameOver(){
		AITimer.stop();
		frameContent.removeAll();
		infoLabel.setText(gameOverText);
		//if(singlePlayerGame){
		if(winningColor==!playerColor){ //if CPU wins
			JLabel winLabel = new JLabel("Computer Wins!", SwingConstants.CENTER);//"<html><div style=\"font-size:40px;\">Computer Wins!</div></html>");
			winLabel.setFont(new Font("Standard", Font.BOLD, 32));
			winLabel.setBounds(0,50, frameContent.getWidth(), 150);
			frameContent.add(winLabel);
			ImageIcon icon = new ImageIcon("resources" + File.separator + "yzma-i-win.gif");
			JLabel label = new JLabel(icon);
			frameContent.add(label);
			label.setBounds(10, 100, frameContent.getWidth(), frameContent.getHeight()-150);
		}
		else{
			JLabel loseLabel = new JLabel("Player Wins!", SwingConstants.CENTER);//"<html><div style=\"font-size:40px;\">Computer Loses...</div></html>");
			loseLabel.setFont(new Font("Standard", Font.BOLD, 32));
			loseLabel.setBounds(0,100, frameContent.getWidth(), 150);
			frameContent.add(loseLabel);
			ImageIcon icon = new ImageIcon("resources" + File.separator + "yzma-lost.gif");
			JLabel label = new JLabel(icon);
			frameContent.add(label);
			label.setBounds(10, 100, frameContent.getWidth(), frameContent.getHeight()-150);	

		}

		undoMoveBtn.setEnabled(false);
		if(boardBeforeAIMoveBtn!= null){
			boardBeforeAIMoveBtn.setEnabled(false);
		}
		//}

	}

	//methods for control panel functionality:	
	/**
	 * Starts new game
	 */
	private void newGame(){
		AITimer.stop();

		//frame.dispose();
		frame.getContentPane().removeAll();
		initialize(false);
	}

	/**
	 * Undoes the latest player-made move
	 */
	private void undoLatestMove(){
		if(singlePlayerGame){
			if(!moveHistory.isEmpty()){
				graphicalBoard.setBoard(moveHistory.pop());
				graphicalBoard.repaint();
				if(!graphicalBoard.isAncestorOf(frameContent)){ //usually this would only happen if the game ended
					frameContent.add(graphicalBoard);	
					AITimer.start();
				}
				playersMove = true;
				boardBeforeAIMoveBtn.setEnabled(false); //disable undo AI makes a move again
			}
		}
		else{
			if(!moveHistory.isEmpty()){
				playersMove = !playersMove;
				graphicalBoard.setBoard(moveHistory.pop());
				graphicalBoard.repaint();
			}
		}

		if(moveHistory.isEmpty()){
			undoMoveBtn.setEnabled(false);
		}
		//undoMoveBtn.setEnabled(false); //disable button until another move is made
	}

	//utilities:
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
		//releasedY = clickY;
		//releasedX = clickX;
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
