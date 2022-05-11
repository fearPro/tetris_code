package felix.tetris;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MyContentPane extends JPanel implements ActionListener {

	enum Difficulty {
		LEICHT(0.1), MITTEL(0.25), SCHWER(1.0);

		double difficultyValue;

		Difficulty(double difficultyValue) {
			this.difficultyValue = difficultyValue;
		}
	}

	GameFrame gameFrame;
	GameBoard gameBoard;

	JButton btnStart;
	JButton btnRestart;
	JButton btnNewGame;
	JButton btnExit;

	JTextField txtScore;
	String playerName;
	Difficulty difficulty;

	final int FRAME_WIDTH = 602;
	final int FRAME_HEIGHT = 798;

	final int MENU_SPACE = 200;

	public MyContentPane(GameFrame gameFrame, String playerName, int difficulty) {
		this.gameFrame = gameFrame;
		this.playerName = playerName;

		switch (difficulty) {
		case 1:
			this.difficulty = Difficulty.LEICHT;
			break;
		case 2:
			this.difficulty = Difficulty.MITTEL;
			break;
		case 3:
			this.difficulty = Difficulty.SCHWER;
			break;
		}

		this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		this.setLayout(null);

		gameBoard = new GameBoard(this, this.difficulty.difficultyValue);
		gameBoard.setBounds(10, 10, FRAME_WIDTH - MENU_SPACE, FRAME_HEIGHT - 20);
		gameBoard.setBorder(BorderFactory.createLoweredBevelBorder());
		this.add(gameBoard);
		
		new Font("Calibri", Font.BOLD, 30);
		
		JLabel lblScore = new JLabel("Score");
		lblScore.setFont(new Font("Arial", Font.BOLD, 30));
		lblScore.setBounds(465, 30, 150, 30);
		this.add(lblScore);
		
		txtScore = new JTextField("0");
		txtScore.setBounds(432, 70, 150, 35);
		txtScore.setHorizontalAlignment(JTextField.CENTER);
		txtScore.setEditable(false);
		txtScore.setFont(new Font("Arial", Font.PLAIN, 25));
		this.add(txtScore);
		
		btnStart = new JButton("Start");
		btnStart.setBounds(425, 120, 165, 100);
		btnStart.setFont(new Font(null, Font.BOLD, 20));
		btnStart.addActionListener(this);
		this.add(btnStart);
		
		btnRestart = new JButton("Restart");
		btnRestart.setBounds(425, 235, 165, 50);
		btnRestart.setFont(new Font(null, Font.BOLD, 20));
		btnRestart.addActionListener(this);
		this.add(btnRestart);		

		btnNewGame = new JButton("New Game");
		btnNewGame.setBounds(425, 300, 165, 50);
		btnNewGame.setFont(new Font(null, Font.BOLD, 20));
		btnNewGame.addActionListener(this);
		this.add(btnNewGame);
		
		btnExit = new JButton("Exit");
		btnExit.setBounds(425, 365, 165, 50);
		btnExit.setFont(new Font(null, Font.BOLD, 20));
		btnExit.addActionListener(this);
		this.add(btnExit);

	}
	
	public String getPlayerName() {
		return this.playerName;
	}

	public void setPlayerName(String name) {
		this.playerName = name;
	}

	public void setTxtScore(int score) {
		txtScore.setText(score + "");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnStart) {
			if (!gameBoard.getRunning()) {
				this.btnStart.setText("Pause");
				gameBoard.startGame();
				gameBoard.grabFocus();
			} else {
				this.btnStart.setText("Start");
				gameBoard.stopGame();
				gameBoard.grabFocus();
			}
		}
		if(e.getSource() == btnNewGame) {
			gameFrame.dispose();
			new MainFrame(this.playerName);			
		}
		if(e.getSource() == btnRestart) {
			this.remove(gameBoard);
			gameBoard.dispatchEvent(e); // ?
			gameBoard = new GameBoard(this, this.difficulty.difficultyValue);
			gameBoard.setBounds(10, 10, FRAME_WIDTH - MENU_SPACE, FRAME_HEIGHT - 20);
			gameBoard.setBorder(BorderFactory.createLoweredBevelBorder());
			this.add(gameBoard);
			gameBoard.startGame();
			gameBoard.grabFocus();
			gameBoard.score = 0;
			txtScore.setText("0");
		}
		if(e.getSource() == btnExit) {
			System.exit(0);
		}
	}
}
