package felix.tetris;
import java.util.Random;

public class Tetri {

	int whichShape;
	
	public Tetri() {
		Random random = new Random();
		this.whichShape = random.nextInt(7);
		
		switch(this.whichShape) {
		case 0:
			squareForm();
			break;
		case 1:
			iForm();
			break;
		case 2:
			lForm();
			break;
		case 3:
			lReverseForm();
			break;
		case 4:
			zForm();
			break;
		case 5:
			zReverseForm();
			break;
		case 6:
			iForm();
			break;		
		}		
	}

	private void zReverseForm() {
		// TODO Auto-generated method stub
		
	}

	private void zForm() {
		// TODO Auto-generated method stub
		
	}

	private void lReverseForm() {
		// TODO Auto-generated method stub
		
	}

	private void lForm() {
		// TODO Auto-generated method stub
		
	}

	private void iForm() {
		// TODO Auto-generated method stub
		
	}

	private void squareForm() {
		// TODO Auto-generated method stub
		
	}
}
