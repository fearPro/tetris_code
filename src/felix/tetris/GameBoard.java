package felix.tetris;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameBoard extends JPanel implements KeyListener, Runnable {

	MyContentPane myContentPane;

	int width = 400 + 2;
	int height = 800 - 2;

	int UNIT_SIZE = 25;

	int drawSpeed = 64;

	double fallSpeedBasis = 0.0;
	double fallSpeedModifier;

	int score = 0;

	Thread thread;

	// einzelner tetri
	Shape shape;
	int[] tetriX = new int[4];
	int[] tetriY = new int[4];
	Shape.Tetri tetri;
	int orientation;
	int xCollModi = 0;

	// koordinatensystem
	Zelle[][] matrix = new Zelle[31][16];

	boolean running = false; // läuft oder pause

	char direction = 'D';

	Graphics2D g;

	public GameBoard(MyContentPane myContentPane, double num) {
		this.myContentPane = myContentPane;

		this.setBackground(Color.LIGHT_GRAY);
		this.setFocusable(true);

		this.fallSpeedModifier = num; // geschwindigkeit 0 - 1 // 1 am schnellsten

		shape = new Shape();
		tetriX = shape.getTetriX();
		tetriY = shape.getTetriY();
		tetri = shape.getTetri();
		orientation = 12;

		this.addKeyListener(this);
		koordinaten();
	}

	public int getScore() {
		return this.score;
	}

	public boolean getRunning() {
		return this.running;
	}

	/**
	 * diese Methode baut ein 2dimensionales array vom typ zelle jede Zelle bekommt
	 * x und y koordinaten, und die information ob sie gerade gefüllt ist
	 */
	private void koordinaten() {
		for (int j = 0; j < 16; j++) {
			for (int i = 0; i < 31; i++) {
				if (i == 30) {
					matrix[i][j] = new Zelle(j * UNIT_SIZE, i * UNIT_SIZE, true);
				} else {
					matrix[i][j] = new Zelle(j * UNIT_SIZE, i * UNIT_SIZE);
				}
			}
		}
	}

	public void stopGame() {
		running = false;

		thread.stop();
	}

	public void startGame() {
		running = true;

		thread = new Thread(this);
		thread.start();
	}

	public void paintComponent(final Graphics graphics) {
		g = ((Graphics2D) graphics);
		super.paintComponent(g);
		draw(g);
	}

	private void draw(final Graphics g) {
		// Spielfeldraster -------------------------------
		for (int i = 0; i < height / UNIT_SIZE; i++) {
			g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, height);
			g.drawLine(0, i * UNIT_SIZE, width, i * UNIT_SIZE);
		}
		// fallende objekte --------------------------------

		vollgemaltWertAbfragenUndDannAusmalenOderNicht();

		drawShape();

	}

	private void vollgemaltWertAbfragenUndDannAusmalenOderNicht() {
		for (Zelle[] reihe : matrix) {
			for (Zelle zelle : reihe) {
				if (zelle.getVollgemalt() != 0) {
					g.fillRect(zelle.getXPos(), zelle.getYPos(), UNIT_SIZE, UNIT_SIZE);
				}
			}
		}
	}

	private void drawShape() {
		g.setColor(shape.getTetriColor());
		for (int i = 0; i < 4; i++) {
			g.fillRect(matrix[tetriY[i]][tetriX[i]].getXPos(), matrix[tetriY[i]][tetriX[i]].getYPos(), UNIT_SIZE,
					UNIT_SIZE);
		}
	}

	private void nachLinks() {
		if ((tetriX[0] > 0 && tetriX[1] > 0 && tetriX[2] > 0 && tetriX[3] > 0)) { // wandcollision
			if (((matrix[tetriY[0]][tetriX[0] - 1].getBelegt()) && (((tetriX[0] - 1) != tetriX[1]) // teilecollision
					&& ((tetriX[0] - 1) != tetriX[2]) && ((tetriX[0] - 1) != tetriX[3])))
					|| ((matrix[tetriY[1]][tetriX[1] - 1].getBelegt()) && (((tetriX[1] - 1) != tetriX[0])
							&& ((tetriX[1] - 1) != tetriX[2]) && ((tetriX[1] - 1) != tetriX[3])))
					|| ((matrix[tetriY[2]][tetriX[2] - 1].getBelegt()) && (((tetriX[2] - 1) != tetriX[0])
							&& ((tetriX[2] - 1) != tetriX[1]) && ((tetriX[2] - 1) != tetriX[3])))
					|| ((matrix[tetriY[3]][tetriX[3] - 1].getBelegt()) && (((tetriX[3] - 1) != tetriX[0])
							&& ((tetriX[3] - 1) != tetriX[1]) && ((tetriX[3] - 1) != tetriX[2])))) {
				// nix
			} else {
				for (int i = 0; i < 4; i++) {
					tetriX[i] = tetriX[i] - 1;
				}
			}
		}
	}

	private void nachRechts() {
		if (tetriX[0] < 15 && tetriX[1] < 15 && tetriX[2] < 15 && tetriX[3] < 15) {
			if (((matrix[tetriY[0]][tetriX[0] + 1].getBelegt()) && (((tetriX[0] + 1) != tetriX[1])
					&& ((tetriX[0] + 1) != tetriX[2]) && ((tetriX[0] + 1) != tetriX[3])))
					|| ((matrix[tetriY[1]][tetriX[1] + 1].getBelegt()) && (((tetriX[1] + 1) != tetriX[0])
							&& ((tetriX[1] + 1) != tetriX[2]) && ((tetriX[1] + 1) != tetriX[3])))
					|| ((matrix[tetriY[2]][tetriX[2] + 1].getBelegt()) && (((tetriX[2] + 1) != tetriX[0])
							&& ((tetriX[2] + 1) != tetriX[1]) && ((tetriX[2] + 1) != tetriX[3])))
					|| ((matrix[tetriY[3]][tetriX[3] + 1].getBelegt()) && (((tetriX[3] + 1) != tetriX[0])
							&& ((tetriX[3] + 1) != tetriX[1]) && ((tetriX[3] + 1) != tetriX[2])))) {
				// hauptsache wir sind alle gesund
			} else {
				for (int i = 0; i < 4; i++) {
					tetriX[i] = tetriX[i] + 1;
				}
			}
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void drehosaurusRex() {
		switch (this.tetri) {
		case L_SHAPE:
			switch (this.orientation) {
			case 12:
				this.orientation = 3;
				drehHilfe(-1, 1, 0, 0, 1, -1, 2, 0);
				break;
			case 3:
				if ((trueIfWandLinks() || isTrueIfLinksAndereSteine())
						&& (!isTrueIfRechtsAndereSteine() && !trueIfWandRechts())) {
					this.orientation = 6;
					this.xCollModi = 1;
					drehHilfe(1, 1, 0, 0, -1, -1, 0, -2);
				} else {
					if (!trueIfWandLinks() && !isTrueIfLinksAndereSteine()) {
						this.orientation = 6;
						drehHilfe(1, 1, 0, 0, -1, -1, 0, -2);
					}
				}
				break;
			case 6:
				this.orientation = 9;
				drehHilfe(1, -1, 0, 0, -1, +1, -2, 0);
				break;
			case 9:
				if ((trueIfWandRechts() || isTrueIfRechtsAndereSteine())
						&& (!isTrueIfLinksAndereSteine() && !trueIfWandLinks() && !isTrueIfLinksAndereSteine2())) {
					this.orientation = 12;
					this.xCollModi = -1;
					drehHilfe(-1, -1, 0, 0, 1, 1, 0, 2);
				} else {
					if (!trueIfWandRechts() && !isTrueIfRechtsAndereSteine()) {
						this.orientation = 12;
						drehHilfe(-1, -1, 0, 0, 1, 1, 0, 2);
					}
				}
				break;
			}
			break;
		case L_REVERSE_SHAPE:
			switch (this.orientation) {
			case 12:
				this.orientation = 3;
				drehHilfe(0, 2, -1, 1, 0, 0, 1, -1);
				break;
			case 3:
				if ((trueIfWandLinks() || isTrueIfLinksAndereSteine())
						&& (!isTrueIfRechtsAndereSteine() && !trueIfWandRechts())) {
					this.orientation = 6;
					this.xCollModi = 1;
					drehHilfe(2, 0, 1, 1, 0, 0, -1, -1);
				} else {
					if (!trueIfWandLinks() && !isTrueIfLinksAndereSteine()) {
						this.orientation = 6;
						drehHilfe(2, 0, 1, 1, 0, 0, -1, -1);
					}
				}
				break;
			case 6:
				this.orientation = 9;
				drehHilfe(0, -2, 1, -1, 0, 0, -1, 1);
				break;
			case 9:
				if ((trueIfWandRechts() || isTrueIfRechtsAndereSteine())
						&& (!isTrueIfLinksAndereSteine() && !trueIfWandLinks() && !isTrueIfLinksAndereSteine2())) {
					this.orientation = 12;
					this.xCollModi = -1;
					drehHilfe(-2, 0, -1, -1, 0, 0, 1, 1);
				} else {
					if (!trueIfWandRechts() && !isTrueIfRechtsAndereSteine()) {
						this.orientation = 12;
						drehHilfe(-2, 0, -1, -1, 0, 0, 1, 1);
					}
				}
				break;
			}
			break;
		case Z_SHAPE:
			switch (this.orientation) {
			case 12:
				this.orientation = 3;
				drehHilfe(0, 2, 1, 1, 0, 0, 1, -1);
				break;
			case 3:
				if ((trueIfWandLinks() || isTrueIfLinksAndereSteine())
						&& (!isTrueIfRechtsAndereSteine() && !trueIfWandRechts())) {
					this.orientation = 6;
					this.xCollModi = 1;
					drehHilfe(2, 0, 1, -1, 0, 0, -1, -1);
				} else {
					if (!trueIfWandLinks() && !isTrueIfLinksAndereSteine()) {
						this.orientation = 6;
						drehHilfe(2, 0, 1, -1, 0, 0, -1, -1);
					}
				}
				break;
			case 6:
				this.orientation = 9;
				drehHilfe(0, -2, -1, -1, 0, 0, -1, 1);
				break;
			case 9:
				if ((trueIfWandRechts() || isTrueIfRechtsAndereSteine())
						&& (!isTrueIfLinksAndereSteine() && !trueIfWandLinks() && !isTrueIfLinksAndereSteine2())) {
					this.orientation = 12;
					this.xCollModi = -1;
					drehHilfe(-2, 0, -1, 1, 0, 0, 1, 1);
				} else {
					if (!trueIfWandRechts() && !isTrueIfRechtsAndereSteine()) {
						this.orientation = 12;
						drehHilfe(-2, 0, -1, 1, 0, 0, 1, 1);
					}
				}
				break;
			}
			break;
		case Z_REVERSE_SHAPE:
			switch (this.orientation) {
			case 12:
				this.orientation = 3;
				drehHilfe(-1, 1, 0, 0, 1, 1, 2, 0);
				break;
			case 3:
				if ((trueIfWandLinks() || isTrueIfLinksAndereSteine())
						&& (!isTrueIfRechtsAndereSteine() && !trueIfWandRechts())) {
					this.orientation = 6;
					this.xCollModi = 1;
					drehHilfe(1, 1, 0, 0, 1, -1, 0, -2);
				} else {
					if (!trueIfWandLinks() && !isTrueIfLinksAndereSteine()) {
						this.orientation = 6;
						drehHilfe(1, 1, 0, 0, 1, -1, 0, -2);
					}
				}
				break;
			case 6:
				this.orientation = 9;
				drehHilfe(1, -1, 0, 0, -1, -1, -2, 0);
				break;
			case 9:
				if ((trueIfWandRechts() || isTrueIfRechtsAndereSteine())
						&& (!isTrueIfLinksAndereSteine() && !trueIfWandLinks() && !isTrueIfLinksAndereSteine2())) {
					this.orientation = 12;
					this.xCollModi = -1;
					drehHilfe(-1, -1, 0, 0, -1, 1, 0, 2);
				} else {
					if (!trueIfWandRechts() && !isTrueIfRechtsAndereSteine()) {
						this.orientation = 12;
						drehHilfe(-1, -1, 0, 0, -1, 1, 0, 2);
					}
				}
				break;
			}
			break;
		case I_SHAPE:
			switch (this.orientation) {
			case 12:
				this.orientation = 3;
				drehHilfe(-1, 2, 0, 1, 1, 0, 2, -1);
				break;
			case 3:
				if ((trueIfWandLinks() || isTrueIfLinksAndereSteine()) && (!trueIfWandRechts() && !trueIfWandRechts2()
						&& !trueIfWandRechts3() && !isTrueIfRechtsAndereSteine() && !isTrueIfRechtsAndereSteine2()
						&& !isTrueIfRechtsAndereSteine3())) {
					this.orientation = 6;
					this.xCollModi = 2;
					drehHilfe(2, 1, 1, 0, 0, -1, -1, -2);
				} else {
					if ((trueIfWandLinks2() || isTrueIfLinksAndereSteine2())
							&& (!trueIfWandRechts() && !trueIfWandRechts2() && !isTrueIfRechtsAndereSteine()
									&& !isTrueIfRechtsAndereSteine2())) {
						this.orientation = 6;
						this.xCollModi = 1;
						drehHilfe(2, 1, 1, 0, 0, -1, -1, -2);
					} else {
						if (!trueIfWandLinks() && !trueIfWandLinks2() && !isTrueIfLinksAndereSteine()
								&& !isTrueIfLinksAndereSteine2() && !trueIfWandRechts() && !trueIfWandRechts2()
								&& !isTrueIfRechtsAndereSteine() && !isTrueIfRechtsAndereSteine2()) {
							this.orientation = 6;
							this.xCollModi = 0;
							drehHilfe(2, 1, 1, 0, 0, -1, -1, -2);
						} else {
							if ((trueIfWandRechts() || isTrueIfRechtsAndereSteine()) && (!trueIfWandLinks()
									&& !trueIfWandLinks2() && !trueIfWandLinks3() && !isTrueIfLinksAndereSteine()
									&& !isTrueIfLinksAndereSteine2() && !isTrueIfLinksAndereSteine3())) {
								this.orientation = 6;
								this.xCollModi = -1;
								drehHilfe(2, 1, 1, 0, 0, -1, -1, -2);
							} else {
								if ((trueIfWandRechts2() || isTrueIfRechtsAndereSteine2())
										&& (!trueIfWandLinks() && !trueIfWandLinks2() && !isTrueIfLinksAndereSteine()
												&& !isTrueIfLinksAndereSteine2())) {
									this.orientation = 6;
									this.xCollModi = 0;
									drehHilfe(2, 1, 1, 0, 0, -1, -1, -2);
								}
							}
						}
					}
				}
				break;
			case 6:
				this.orientation = 9;
				drehHilfe(1, -2, 0, -1, -1, 0, -2, 1);
				break;
			case 9:
				if ((trueIfWandLinks() || isTrueIfLinksAndereSteine()) && (!trueIfWandRechts() && !trueIfWandRechts2()
						&& !trueIfWandRechts3() && !isTrueIfRechtsAndereSteine() && !isTrueIfRechtsAndereSteine2()
						&& !isTrueIfRechtsAndereSteine3())) {
					this.orientation = 12;
					this.xCollModi = 1;
					drehHilfe(-2, -1, -1, 0, 0, 1, 1, 2);
				} else {
					if ((trueIfWandLinks2() || isTrueIfLinksAndereSteine2())
							&& (!trueIfWandRechts() && !trueIfWandRechts2() && !isTrueIfRechtsAndereSteine()
									&& !isTrueIfRechtsAndereSteine2())) {
						this.orientation = 12;
						this.xCollModi = 0;
						drehHilfe(-2, -1, -1, 0, 0, 1, 1, 2);
					} else {
						if (!trueIfWandLinks() && !trueIfWandLinks2() && !isTrueIfLinksAndereSteine()
								&& !isTrueIfLinksAndereSteine2() && !trueIfWandRechts() && !trueIfWandRechts2()
								&& !isTrueIfRechtsAndereSteine() && !isTrueIfRechtsAndereSteine2()) {
							this.orientation = 12;
							this.xCollModi = 0;
							drehHilfe(-2, -1, -1, 0, 0, 1, 1, 2);
						} else {
							if ((trueIfWandRechts() || isTrueIfRechtsAndereSteine()) && (!trueIfWandLinks()
									&& !trueIfWandLinks2() && !trueIfWandLinks3() && !isTrueIfLinksAndereSteine()
									&& !isTrueIfLinksAndereSteine2() && !isTrueIfLinksAndereSteine3())) {
								this.orientation = 12;
								this.xCollModi = -2;
								drehHilfe(-2, -1, -1, 0, 0, 1, 1, 2);
							} else {
								if ((trueIfWandRechts2() || isTrueIfRechtsAndereSteine2())
										&& (!trueIfWandLinks() && !trueIfWandLinks2() && !isTrueIfLinksAndereSteine()
												&& !isTrueIfLinksAndereSteine2())) {
									this.orientation = 12;
									this.xCollModi = -1;
									drehHilfe(-2, -1, -1, 0, 0, 1, 1, 2);
								}
							}
						}
					}
				}
				break;

			}
			break;
		case THREE_SHAPE:
			switch (this.orientation) {
			case 12:
				this.orientation = 3;
				drehHilfe(-1, 1, 1, 1, 1, -1, 0, 0);
				break;
			case 3:
				if ((trueIfWandLinks() || isTrueIfLinksAndereSteine())
						&& (!isTrueIfRechtsAndereSteine() && !trueIfWandRechts())) {
					this.orientation = 6;
					this.xCollModi = 1;
					drehHilfe(1, 1, 1, -1, -1, -1, 0, 0);
				} else {
					if (!trueIfWandLinks() && !isTrueIfLinksAndereSteine()) {
						this.orientation = 6;
						drehHilfe(1, 1, 1, -1, -1, -1, 0, 0);
					}
				}
				break;
			case 6:
				this.orientation = 9;
				drehHilfe(1, -1, -1, -1, -1, 1, 0, 0);
				break;
			case 9:
				if ((trueIfWandRechts() || isTrueIfRechtsAndereSteine())
						&& (!isTrueIfLinksAndereSteine() && !trueIfWandLinks() && !isTrueIfLinksAndereSteine2())) {
					this.orientation = 12;
					this.xCollModi = -1;
					drehHilfe(-1, -1, -1, 1, 1, 1, 0, 0);
				} else {
					if (!trueIfWandRechts() && !isTrueIfRechtsAndereSteine()) {
						this.orientation = 12;
						drehHilfe(-1, -1, -1, 1, 1, 1, 0, 0);
					}
				}
				break;
			}
			break;
		}
	}

	private boolean isTrueIfLinksAndereSteine() {
		if ((tetriX[0] > 0 && tetriX[1] > 0 && tetriX[2] > 0 && tetriX[3] > 0)) { // wandcollision
			if (((matrix[tetriY[0]][tetriX[0] - 1].getBelegt()) && (((tetriX[0] - 1) != tetriX[1]) // teilecollision
					&& ((tetriX[0] - 1) != tetriX[2]) && ((tetriX[0] - 1) != tetriX[3])))
					|| ((matrix[tetriY[1]][tetriX[1] - 1].getBelegt()) && (((tetriX[1] - 1) != tetriX[0])
							&& ((tetriX[1] - 1) != tetriX[2]) && ((tetriX[1] - 1) != tetriX[3])))
					|| ((matrix[tetriY[2]][tetriX[2] - 1].getBelegt()) && (((tetriX[2] - 1) != tetriX[0])
							&& ((tetriX[2] - 1) != tetriX[1]) && ((tetriX[2] - 1) != tetriX[3])))
					|| ((matrix[tetriY[3]][tetriX[3] - 1].getBelegt()) && (((tetriX[3] - 1) != tetriX[0])
							&& ((tetriX[3] - 1) != tetriX[1]) && ((tetriX[3] - 1) != tetriX[2])))) {
				// nix
				return true;

			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private boolean isTrueIfLinksAndereSteine2() {
		if ((tetriX[0] > 1 && tetriX[1] > 1 && tetriX[2] > 1 && tetriX[3] > 1)) { // wandcollision
			if (((matrix[tetriY[0]][tetriX[0] - 2].getBelegt()) && (((tetriX[0] - 2) != tetriX[1]) // teilecollision
					&& ((tetriX[0] - 2) != tetriX[2]) && ((tetriX[0] - 2) != tetriX[3])))
					|| ((matrix[tetriY[1]][tetriX[1] - 2].getBelegt()) && (((tetriX[1] - 2) != tetriX[0])
							&& ((tetriX[1] - 2) != tetriX[2]) && ((tetriX[1] - 2) != tetriX[3])))
					|| ((matrix[tetriY[2]][tetriX[2] - 2].getBelegt()) && (((tetriX[2] - 2) != tetriX[0])
							&& ((tetriX[2] - 2) != tetriX[1]) && ((tetriX[2] - 2) != tetriX[3])))
					|| ((matrix[tetriY[3]][tetriX[3] - 2].getBelegt()) && (((tetriX[3] - 2) != tetriX[0])
							&& ((tetriX[3] - 2) != tetriX[1]) && ((tetriX[3] - 2) != tetriX[2])))) {
				// nix
				return true;

			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private boolean isTrueIfLinksAndereSteine3() {
		if ((tetriX[0] > 2 && tetriX[1] > 2 && tetriX[2] > 2 && tetriX[3] > 2)) { // wandcollision
			if (((matrix[tetriY[0]][tetriX[0] - 3].getBelegt()) && (((tetriX[0] - 3) != tetriX[1]) // teilecollision
					&& ((tetriX[0] - 3) != tetriX[2]) && ((tetriX[0] - 3) != tetriX[3])))
					|| ((matrix[tetriY[1]][tetriX[1] - 3].getBelegt()) && (((tetriX[1] - 3) != tetriX[0])
							&& ((tetriX[1] - 3) != tetriX[2]) && ((tetriX[1] - 3) != tetriX[3])))
					|| ((matrix[tetriY[2]][tetriX[2] - 3].getBelegt()) && (((tetriX[2] - 3) != tetriX[0])
							&& ((tetriX[2] - 3) != tetriX[1]) && ((tetriX[2] - 3) != tetriX[3])))
					|| ((matrix[tetriY[3]][tetriX[3] - 3].getBelegt()) && (((tetriX[3] - 3) != tetriX[0])
							&& ((tetriX[3] - 3) != tetriX[1]) && ((tetriX[3] - 3) != tetriX[2])))) {
				// nix
				return true;

			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private boolean isTrueIfRechtsAndereSteine() {
		if (tetriX[0] < 15 && tetriX[1] < 15 && tetriX[2] < 15 && tetriX[3] < 15) {

			// wenn rechts belegt ist soll bei einer drehung nach links verschoben werden,
			// außer wenn links auch belegt ist
			if (((matrix[tetriY[0]][tetriX[0] + 1].getBelegt()) && (((tetriX[0] + 1) != tetriX[1])
					&& ((tetriX[0] + 1) != tetriX[2]) && ((tetriX[0] + 1) != tetriX[3])))
					|| ((matrix[tetriY[1]][tetriX[1] + 1].getBelegt()) && (((tetriX[1] + 1) != tetriX[0])
							&& ((tetriX[1] + 1) != tetriX[2]) && ((tetriX[1] + 1) != tetriX[3])))
					|| ((matrix[tetriY[2]][tetriX[2] + 1].getBelegt()) && (((tetriX[2] + 1) != tetriX[0])
							&& ((tetriX[2] + 1) != tetriX[1]) && ((tetriX[2] + 1) != tetriX[3])))
					|| ((matrix[tetriY[3]][tetriX[3] + 1].getBelegt()) && (((tetriX[3] + 1) != tetriX[0])
							&& ((tetriX[3] + 1) != tetriX[1]) && ((tetriX[3] + 1) != tetriX[2])))) {
				// nix
				return true;

			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private boolean isTrueIfRechtsAndereSteine2() {
		if (tetriX[0] < 14 && tetriX[1] < 14 && tetriX[2] < 14 && tetriX[3] < 14) {

			// wenn rechts belegt ist soll bei einer drehung nach links verschoben werden,
			// außer wenn links auch belegt ist
			if (((matrix[tetriY[0]][tetriX[0] + 2].getBelegt()) && (((tetriX[0] + 2) != tetriX[1])
					&& ((tetriX[0] + 2) != tetriX[2]) && ((tetriX[0] + 2) != tetriX[3])))
					|| ((matrix[tetriY[1]][tetriX[1] + 2].getBelegt()) && (((tetriX[1] + 2) != tetriX[0])
							&& ((tetriX[1] + 2) != tetriX[2]) && ((tetriX[1] + 2) != tetriX[3])))
					|| ((matrix[tetriY[2]][tetriX[2] + 2].getBelegt()) && (((tetriX[2] + 2) != tetriX[0])
							&& ((tetriX[2] + 2) != tetriX[1]) && ((tetriX[2] + 2) != tetriX[3])))
					|| ((matrix[tetriY[3]][tetriX[3] + 2].getBelegt()) && (((tetriX[3] + 2) != tetriX[0])
							&& ((tetriX[3] + 2) != tetriX[1]) && ((tetriX[3] + 2) != tetriX[2])))) {
				// nix
				return true;

			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private boolean isTrueIfRechtsAndereSteine3() {
		if (tetriX[0] < 13 && tetriX[1] < 13 && tetriX[2] < 13 && tetriX[3] < 13) {

			// wenn rechts belegt ist soll bei einer drehung nach links verschoben werden,
			// außer wenn links auch belegt ist
			if (((matrix[tetriY[0]][tetriX[0] + 3].getBelegt()) && (((tetriX[0] + 3) != tetriX[1])
					&& ((tetriX[0] + 3) != tetriX[2]) && ((tetriX[0] + 3) != tetriX[3])))
					|| ((matrix[tetriY[1]][tetriX[1] + 3].getBelegt()) && (((tetriX[1] + 3) != tetriX[0])
							&& ((tetriX[1] + 3) != tetriX[2]) && ((tetriX[1] + 3) != tetriX[3])))
					|| ((matrix[tetriY[2]][tetriX[2] + 3].getBelegt()) && (((tetriX[2] + 3) != tetriX[0])
							&& ((tetriX[2] + 3) != tetriX[1]) && ((tetriX[2] + 3) != tetriX[3])))
					|| ((matrix[tetriY[3]][tetriX[3] + 3].getBelegt()) && (((tetriX[3] + 3) != tetriX[0])
							&& ((tetriX[3] + 3) != tetriX[1]) && ((tetriX[3] + 3) != tetriX[2])))) {
				// nix
				return true;

			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private boolean trueIfWandLinks() { // +1 // +2
		if ((tetriX[0] == 0 || tetriX[1] == 0 || tetriX[2] == 0 || tetriX[3] == 0)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean trueIfWandLinks2() { // +1 // +2
		if ((tetriX[0] == 1 || tetriX[1] == 1 || tetriX[2] == 1 || tetriX[3] == 1)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean trueIfWandLinks3() { // +1 // +2
		if ((tetriX[0] == 2 || tetriX[1] == 2 || tetriX[2] == 2 || tetriX[3] == 2)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean trueIfWandRechts() { // -1 -2
		if (tetriX[0] == 15 || tetriX[1] == 15 || tetriX[2] == 15 || tetriX[3] == 15) {
			return true;
		} else {
			return false;
		}
	}

	private boolean trueIfWandRechts2() { // -1 -2
		if (tetriX[0] == 14 || tetriX[1] == 14 || tetriX[2] == 14 || tetriX[3] == 14) {
			return true;
		} else {
			return false;
		}
	}

	private boolean trueIfWandRechts3() { // -1 -2
		if (tetriX[0] == 13 || tetriX[1] == 13 || tetriX[2] == 13 || tetriX[3] == 13) {
			return true;
		} else {
			return false;
		}
	}

	// -----------------------------------------------------------------------------------------

	private void drehHilfe(int y0, int x0, int y1, int x1, int y2, int x2, int y3, int x3) {
		tetriY[0] = tetriY[0] + y0;
		tetriX[0] = tetriX[0] + x0 + xCollModi;

		tetriY[1] = tetriY[1] + y1;
		tetriX[1] = tetriX[1] + x1 + xCollModi;

		tetriY[2] = tetriY[2] + y2;
		tetriX[2] = tetriX[2] + x2 + xCollModi;

		tetriY[3] = tetriY[3] + y3;
		tetriX[3] = tetriX[3] + x3 + xCollModi;
		this.xCollModi = 0;
	}

	private void volleReiheCheck() {
		int scoreMultiplikator = 0;
		int volleReiheCount = 16;
		for (int i = 0; i < 31; i++) {
			volleReiheCount = 16;
			for (int j = 0; j < 16; j++) {
				if (matrix[i][j].getVollgemalt() != 0) {
					volleReiheCount--;
					if (volleReiheCount == 0) {
						scoreMultiplikator++;
						reiheEntfernen(i);
					}
				}
			}
		}
		this.score = this.score + (10 * scoreMultiplikator);
		this.myContentPane.setTxtScore(this.score);
		this.fallSpeedModifier = this.fallSpeedModifier + (0.02 * scoreMultiplikator);
		System.out.println(this.fallSpeedModifier);
//		System.out.println(score);
	}

	private void reiheEntfernen(int welcheReihe) {
		for (int i = 0; i < 16; i++) {
			matrix[welcheReihe][i].setVollgemalt(0);
			matrix[welcheReihe][i].setBelegt(false);
		}

		reihenVerschieben(welcheReihe);
	}

	// 0 -30
	private void reihenVerschieben(int welcheReihe) {
		for (int i = welcheReihe; i > 0; i--) {
			for (int j = 0; j < 16; j++) {
				matrix[i][j].setVollgemalt(matrix[i - 1][j].getVollgemalt());
				matrix[i][j].setBelegt(matrix[i - 1][j].getBelegt());
				matrix[i][j].setBoden(matrix[i - 1][j].getBoden()); // fehler beim boden bestimmen bei

			}
		}
		// hohlraum fix
		for (int i = welcheReihe; i > 0; i--) {
			for (int j = 0; j < 16; j++) {
				if (i != 30) {
					if (!matrix[i + 1][j].getBelegt()) {
						matrix[i][j].setBoden(false);
					}
				}
			}
		}
	}

	private void fallenUndAblegen() {

		if (matrix[tetriY[0]][tetriX[0]].getBoden() != true && matrix[tetriY[1]][tetriX[1]].getBoden() != true
				&& matrix[tetriY[2]][tetriX[2]].getBoden() != true && matrix[tetriY[3]][tetriX[3]].getBoden() != true) {

			if (fallSpeedBasis >= 1) { // fallGeschwindigkeit relativ zur gesamtgeschwindigkeit
				for (int i = 0; i < 4; i++) {
					matrix[tetriY[i]][tetriX[i]].setBelegt(false);
					tetriY[i]++;
					fallSpeedBasis = 0;
				}
			}
		} else {
//			System.out.println("boden true");
			for (int i = 0; i < 4; i++) {
				matrix[tetriY[i]][tetriX[i]].setVollgemalt(1);
				matrix[tetriY[i]][tetriX[i]].setBelegt(true);
				try {
					if (matrix[tetriY[i] - 1][tetriX[i]].getBelegt() == false) {

						matrix[tetriY[i] - 1][tetriX[i]].setBoden(true);

					}
				} catch (ArrayIndexOutOfBoundsException aie) {
					new GameOver(myContentPane.getPlayerName(), this.score);
					stopGame();
				}
			}

			this.score++;
			volleReiheCheck();

			// next tetri
			shape = new Shape();
			tetriX = shape.getTetriX();
			tetriY = shape.getTetriY();
			tetri = shape.getTetri();
			orientation = 12;

		}
	}

	private void movement() {
		switch (direction) {
		case 'D':
			// horizontal stillstehen
			break;
		case 'L':
			nachLinks();
			direction = 'D';
			break;
		case 'R':
			nachRechts();
			direction = 'D';
			break;
		case 'U':
			if ((tetriY[0] < 30) && (tetriY[1] < 30) && (tetriY[2] < 30) && (tetriY[3] < 30)) {
				if ((matrix[tetriY[0] + 1][tetriX[0]].getBoden() == true)
						|| (matrix[tetriY[1] + 1][tetriX[1]].getBoden() == true)
						|| (matrix[tetriY[2] + 1][tetriX[2]].getBoden() == true)
						|| (matrix[tetriY[3] + 1][tetriX[3]].getBoden() == true)) {
					System.out.println("boden");
				} else {
					drehosaurusRex();
				}
			}

			direction = 'D';
			break;
		}
	}

// KeyListener -----------------------------------------
	@Override
	public void keyTyped(KeyEvent e) {
		// nix
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			direction = 'L';
			movement();
			break;
		case KeyEvent.VK_RIGHT:
			direction = 'R';
			movement();
			break;
		case KeyEvent.VK_UP:
			direction = 'U';
			movement();
			break;
		case KeyEvent.VK_DOWN:
			direction = 'S';
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		direction = 'D';
	}

	// thread ----------------------------------
	@Override
	public void run() {
		while (running) {
			try {
//				movement();

				fallenUndAblegen();

				repaint();
				fallSpeedBasis += fallSpeedModifier; // fallenUndAblegen()

				if (direction == 'S') {
					if (fallSpeedModifier < 0.4) {
						Thread.sleep(3);
					} else {
						if (fallSpeedModifier >= 0.4 && fallSpeedModifier < 0.7) {
							Thread.sleep(10);
						} else {
							if (fallSpeedModifier >= 0.7) {
								Thread.sleep(20);
							}
						}
					}
				} else {
					Thread.sleep(drawSpeed);
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
