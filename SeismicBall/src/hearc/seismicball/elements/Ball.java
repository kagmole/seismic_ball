package hearc.seismicball.elements;

import android.graphics.Bitmap;

public class Ball extends Tile {

	public Ball(int left, int top, int right, int bottom, Bitmap image) {
		super(left, top, right, bottom, image);
	}
	
	public void translate(int x, int y) {
		bounds.left += x;
		bounds.right += x;
		
		bounds.top += y;
		bounds.bottom += y;
	}
}
