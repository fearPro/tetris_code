package felix.tetris;
import java.awt.Color;
import java.util.Random;

public class Shape {

	enum Tetri {
		SQUARE_SHAPE, L_SHAPE, L_REVERSE_SHAPE, Z_SHAPE, Z_REVERSE_SHAPE, I_SHAPE, THREE_SHAPE;
	}

	Tetri tetri;

	int[] tetriX = new int[4];
	int[] tetriY = new int[4];

	Color tetriColor;

	int orientation = 12; // F?
	int startPosVerschieber = 5;

	public Shape() {
		Random random = new Random();
		this.tetriColor = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
		int num = random.nextInt(7);
//		num = 5;

		switch (num) {
		case 0:
			squareShape();
			break;
		case 1:
			lShape();
			break;
		case 2:
			lReverseShape();
			break;
		case 3:
			zShape();
			break;
		case 4:
			zReverseShape();
			break;
		case 5:
			iShape();
			break;
		case 6:
			threeShape();
			break;
		}
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public int getOrientation() {
		return this.orientation;
	}

	public Tetri getTetri() {
		return this.tetri;
	}

	public int[] getTetriX() {
		return tetriX;
	}

	public int[] getTetriY() {
		return tetriY;
	}

	public Color getTetriColor() {
		return tetriColor;
	}

	private void squareShape() {
		this.tetri = Tetri.SQUARE_SHAPE;
		tetriY[0] = 0;
		tetriX[0] = 0 + this.startPosVerschieber;

		tetriY[1] = 0;
		tetriX[1] = 1 + this.startPosVerschieber;

		tetriY[2] = 1;
		tetriX[2] = 0 + this.startPosVerschieber;

		tetriY[3] = 1;
		tetriX[3] = 1 + this.startPosVerschieber;
	}

	private void lShape() {
		this.tetri = Tetri.L_SHAPE;
		tetriY[0] = 1;
		tetriX[0] = 0 + this.startPosVerschieber;

		tetriY[1] = 1;
		tetriX[1] = 1 + this.startPosVerschieber;

		tetriY[2] = 1;
		tetriX[2] = 2 + this.startPosVerschieber;

		tetriY[3] = 0;
		tetriX[3] = 2 + this.startPosVerschieber;
	}

	private void lReverseShape() {
		this.tetri = Tetri.L_REVERSE_SHAPE;
		tetriY[0] = 0;
		tetriX[0] = 0 + this.startPosVerschieber;

		tetriY[1] = 1;
		tetriX[1] = 0 + this.startPosVerschieber;

		tetriY[2] = 1;
		tetriX[2] = 1 + this.startPosVerschieber;

		tetriY[3] = 1;
		tetriX[3] = 2 + this.startPosVerschieber;
	}

	private void zShape() {
		this.tetri = Tetri.Z_SHAPE;
		tetriY[0] = 0;
		tetriX[0] = 0 + this.startPosVerschieber;

		tetriY[1] = 0;
		tetriX[1] = 1 + this.startPosVerschieber;

		tetriY[2] = 1;
		tetriX[2] = 1 + this.startPosVerschieber;

		tetriY[3] = 1;
		tetriX[3] = 2 + this.startPosVerschieber;
	}

	private void zReverseShape() {
		this.tetri = Tetri.Z_REVERSE_SHAPE;
		tetriY[0] = 1;
		tetriX[0] = 0 + this.startPosVerschieber;

		tetriY[1] = 1;
		tetriX[1] = 1 + this.startPosVerschieber;

		tetriY[2] = 0;
		tetriX[2] = 1 + this.startPosVerschieber;

		tetriY[3] = 0;
		tetriX[3] = 2 + this.startPosVerschieber;
	}

	private void iShape() {
		this.tetri = Tetri.I_SHAPE;
		tetriY[0] = 1;
		tetriX[0] = 0 + this.startPosVerschieber;

		tetriY[1] = 1;
		tetriX[1] = 1 + this.startPosVerschieber;

		tetriY[2] = 1;
		tetriX[2] = 2 + this.startPosVerschieber;

		tetriY[3] = 1;
		tetriX[3] = 3 + this.startPosVerschieber;
	}

	private void threeShape() {
		this.tetri = Tetri.THREE_SHAPE;
		tetriY[0] = 1;
		tetriX[0] = 0 + this.startPosVerschieber;

		tetriY[1] = 0;
		tetriX[1] = 1 + this.startPosVerschieber;

		tetriY[2] = 1;
		tetriX[2] = 2 + this.startPosVerschieber;

		tetriY[3] = 1;
		tetriX[3] = 1 + this.startPosVerschieber;
	}
}
