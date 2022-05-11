package felix.tetris;
/**
 * 
 * diese klasse repräsentiert ein einzelnes Feld in der Spielfeldmatrix
 *
 */
public class Zelle {
	
	private int xPos = 0;
	private int yPos = 0;
	private boolean belegt = false;
	private boolean boden = false;
	private int vollgemalt = 0; 
	
	public Zelle(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.belegt = false;
	}
	
	public Zelle(int xPos, int yPos, boolean boden) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.boden = boden;
	}
	
	public Zelle() {
	}
	
	public void setVollgemalt(int vollgemalt) {
		this.vollgemalt = vollgemalt;
	}
	
	public int getVollgemalt() {
		return this.vollgemalt;
	}
	
	public void setBoden(boolean boden) {
		this.boden = boden;
	}
	
	public boolean getBoden() {
		return this.boden;
	}
	
	public void setPos(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public int getXPos() {
		return this.xPos;
	}
	
	public int getYPos() {
		return this.yPos;
	}
	
	public void setBelegt(boolean belegt) {
		this.belegt = belegt;
	}
	
	public boolean getBelegt() {
		return this.belegt;
	}
}
