package hearc.seismicball.elements;

import android.graphics.Rect;

import hearc.seismicball.core.Assets;
import hearc.seismicball.framework.Image;

public class Ball {
	
	private static final int BALL_WIDTH = 64;
	private static final int BALL_HEIGHT = 64;
	
	private float speedFactor;
	
	private Image ballImage;
	
	private Rect ballBounds;
	
	public Ball() {
		ballImage = Assets.ball;
	}
	
	public void resetMatrixPosition(int i, int j) {
		ballBounds = new Rect(
				i * BALL_WIDTH, 
				j * BALL_HEIGHT,
				(i + 1) * BALL_WIDTH,
				(j + 1) * BALL_HEIGHT);
	}
	
	public void translate(int x, int y) {
		ballBounds.left += x;
		ballBounds.right += x;
		
		ballBounds.top += y;
		ballBounds.bottom += y;
	}
	
	public Rect getBounds() {
		return ballBounds;
	}
	
	public Image getImage() {
		return ballImage;
	}
	
	public float getSpeedFactor() {
		return speedFactor;
	}
	
	public void setSpeedFactor(float speedFactor) {
		this.speedFactor = speedFactor;
	}
}
