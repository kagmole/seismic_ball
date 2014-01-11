package hearc.seismicball.elements;

import android.graphics.Rect;

import hearc.seismicball.core.Assets;
import hearc.seismicball.framework.Image;

public class Tile {
	
	public static final int TILE_WIDTH = 64;
	public static final int TILE_HEIGHT = 64;
	
	public static final int TYPE_GROUND = 0;
	public static final int TYPE_WALL = 1;
	public static final int TYPE_HOLE = 2;
	public static final int TYPE_START = 3;
	public static final int TYPE_END = 4;
	
	private Image tileImage;
	
	private int tileType;
	
	private Rect tileBounds;

	public Tile(int i, int j, int type) {
		tileBounds = new Rect(
				i * TILE_WIDTH, 
				j * TILE_HEIGHT,
				(i + 1) * TILE_WIDTH,
				(j + 1) * TILE_HEIGHT);
		
		tileType = type;
		
		switch(tileType) {
		case TYPE_GROUND:
			tileImage = Assets.tileGround;
			break;
		case TYPE_WALL:
			tileImage = Assets.tileWall;
			break;
		case TYPE_HOLE:
			tileImage = Assets.tileHole;
			break;
		case TYPE_START:
			tileImage = Assets.tileStart;
			break;
		case TYPE_END:
			tileImage = Assets.tileEnd;
			break;
		default:
			tileImage = Assets.tileGround;
			tileType = TYPE_GROUND;
			break;
		}
	}
	
	public Rect getBounds() {
		return tileBounds;
	}
	
	public Image getImage() {
		return tileImage;
	}
	
	public int getType() {
		return tileType;
	}
}