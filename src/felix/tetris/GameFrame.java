package felix.tetris;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {

	public GameFrame(String playerName, int difficulty) {
		this.setTitle("Tetris");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(new MyContentPane(this, playerName, difficulty));	
		this.setResizable(false);
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}
}
