package felix.tetris;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener {
	public static void main(final String... args) {
		new MainFrame();
	}
	
	private String name = "";

	private JTextField txtName;

	private JButton btnLeicht;
	private JButton btnMittel;
	private JButton btnSchwer;

	public MainFrame() {
		this("");
	}
	
	public MainFrame(String name) {
		this.name = name;
		
		this.setTitle("Willkommen");
		this.setSize(350, 250);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);

		content();

		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private void content() {
		JLabel lblName = new JLabel("Bitte Namen eingeben und Schwierigkeit wählen!");
		lblName.setBounds(25, 5, 300, 30);
		this.add(lblName);
		
		// BILD ----------------------------
		JLabel bild = new JLabel();
		bild.setBounds(150, 35, 155, 151);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("tetris_bild.png")); // pfad für export ändern
		} catch (IOException e) {
			// nix
		}
		
		Image dimg = img.getScaledInstance(bild.getWidth(), bild.getHeight(), Image.SCALE_SMOOTH);				
		bild.setIcon(new ImageIcon(dimg));
		bild.setBorder(BorderFactory.createRaisedBevelBorder());
		this.add(bild);
		// -------------------------------
		
		txtName = new JTextField(this.name);
		txtName.setBounds(25, 35, 100, 30);
		this.add(txtName);

		btnLeicht = new JButton("Leicht");
		btnLeicht.setBounds(25, 75, 100, 30);
		btnLeicht.addActionListener(this);
		this.add(btnLeicht);

		btnMittel = new JButton("Mittel");
		btnMittel.setBounds(25, 115, 100, 30);
		btnMittel.addActionListener(this);
		this.add(btnMittel);

		btnSchwer = new JButton("Schwer");
		btnSchwer.setBounds(25, 155, 100, 30);
		btnSchwer.addActionListener(this);
		this.add(btnSchwer);
		
		this.getContentPane().setBackground(new Color(255, 200, 200));
				
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (txtName.getText().length() > 0 && !txtName.getText().equals("name pls")) {
			if (e.getSource() == btnLeicht) {
				this.dispose();
				new GameFrame(txtName.getText(), 1);
			}
			if (e.getSource() == btnMittel) {
				this.dispose();
				new GameFrame(txtName.getText(), 2);
			}
			if (e.getSource() == btnSchwer) {
				this.dispose();
				new GameFrame(txtName.getText(), 3);
			}
		} else {
			txtName.setText("name pls");
			txtName.requestFocus();
			txtName.selectAll();
		}
	}
}
