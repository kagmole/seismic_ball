package hearc.seismicball.framework.implementation;

import android.graphics.Bitmap;

import hearc.seismicball.framework.Image;
import hearc.seismicball.framework.Graphics.ImageFormat;

public class AndroidImage implements Image {
	Bitmap bitmap;
	ImageFormat format;

	public AndroidImage(Bitmap bitmap, ImageFormat format) {
		this.bitmap = bitmap;
		this.format = format;
	}

	@Override
	public int getWidth() {
		return bitmap.getWidth();
	}

	@Override
	public int getHeight() {
		return bitmap.getHeight();
	}

	@Override
	public ImageFormat getFormat() {
		return format;
	}

	@Override
	public void dispose() {
		bitmap.recycle();
	}
}