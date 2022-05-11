package felix.tetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class GameOver extends JFrame implements ActionListener {

	DBAccess dbAccess;

	JButton btnClose;
	JButton btnClear;

	public GameOver(String name, int score) {
		this.setTitle("GAME OVER");
		this.setSize(275, 360);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);

		JLabel lblName = new JLabel("Gut gespielt " + name + ".");
		lblName.setBounds(10, 10, 150, 25);
		this.add(lblName);

		JLabel lblScore = new JLabel("Deine Punktzahl ist " + score + ".");
		lblScore.setBounds(10, 35, 150, 25);
		this.add(lblScore);

		// dbaccess -> highscore.db -> tabelle

		try {
			dbAccess = new DBAccess();
			dbAccess.insert(name, score);
			dbAccess.selectAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		fillTable();

		btnClose = new JButton("Schließen");
		btnClose.setBounds(10, 280, 90, 30);
		btnClose.addActionListener(this);
		this.add(btnClose);

		btnClear = new JButton("delete Highscores");
		btnClear.setBounds(110, 280, 140, 30);
		btnClear.addActionListener(this);
		this.add(btnClear);

		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.requestFocus();
	}

	private void fillTable() {
		String[] columns = { "Name", "Score" };
		// daten sortieren
		Object[][] data = dbAccess.getData();
		Arrays.sort(data, (b, a) -> Integer.compare(Integer.parseInt((String) a[1]), Integer.parseInt((String) b[1])));

		// tabelle
		DefaultTableModel dtf = new DefaultTableModel(data, columns);
		JTable table = new JTable(dtf);
		table.setEnabled(false);

		JScrollPane sp = new JScrollPane(table);
		sp.setBounds(10, 70, 240, 200);
		this.add(sp);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnClose) {
			try {
				dbAccess.closeConnection();
			} catch (SQLException e1) {

			}
			this.dispose();
		}
		if (e.getSource() == btnClear) {
			try {
				dbAccess.deleteEverything();
				dbAccess.selectAll();
				fillTable();
			} catch (SQLException e1) {

			}
		}

	}
}
