package hearc.seismicball.elements;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Tile {
	
	private Bitmap image;
	
	// XXX private asap
	protected Rect bounds;

	public Tile(int left, int top, int right, int bottom, Bitmap image) {
		bounds = new Rect(left, top, right, bottom);
		
		this.image = image;
	}
	
	public void drawElement(Canvas canvas) {
		canvas.drawBitmap(image, null, bounds, null);
	}
	
	public Rect getBounds() {
		return bounds;
	}
}
