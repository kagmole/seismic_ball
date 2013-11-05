package hearc.seismicball.elements;

public abstract class AbstractBox {
	
	public static final int GROUND_BOX = 0;
	public static final int WALL_BOX = 1;
	public static final int HOLE_BOX = 2;
	public static final int START_BOX = 3;
	public static final int END_BOX = 4;
	
	protected int i;
	protected int j;
	
	public AbstractBox(int i, int j) {
		this.i = i;
		this.j = j;
	}
	
	public abstract void drawElement();
	
	public int getI() {
		return i;
	}
	
	public int getJ() {
		return j;
	}
	
	public abstract int getId();
}
