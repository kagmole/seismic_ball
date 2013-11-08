package hearc.seismicball.core;

import hearc.seismicball.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;

public class GamePanel extends View {
	
	private static final int GROUND_TILE = 0;
	private static final int WALL_TILE = 1;
	private static final int HOLE_TILE = 2;
	private static final int START_TILE = 3;
	private static final int END_TILE = 4;
	
	private static final int GRID_WIDTH = 16;
	private static final int GRID_HEIGHT = 9;
	
	private static final int TILE_WIDTH = 64;
	private static final int TILE_HEIGHT = 64;
	
	private Bitmap groundTile = BitmapFactory.decodeResource(getResources(), R.drawable.ground_tile);
	private Bitmap wallTile = BitmapFactory.decodeResource(getResources(), R.drawable.wall_tile);
	private Bitmap holeTile = BitmapFactory.decodeResource(getResources(), R.drawable.hole_tile);
	private Bitmap startTile = BitmapFactory.decodeResource(getResources(), R.drawable.start_tile);
	private Bitmap endTile = BitmapFactory.decodeResource(getResources(), R.drawable.end_tile);
	
	private int[][] grid;
	
	private Rect tileBounds;

	public GamePanel(Context context) {
		super(context);
		
		tileBounds = new Rect();
		
		initGrid();
	}
	
	private void initGrid() {
		grid = new int[GRID_WIDTH][GRID_HEIGHT];
		
		// XXX Change for a resource's dynamic loading
		for (int j = 0; j < GRID_HEIGHT; ++j) {
			for (int i = 0; i < GRID_WIDTH; ++i) {
				if (i == 0 || i == GRID_WIDTH - 1 || j == 0 || j == GRID_HEIGHT - 1) {
					grid[i][j] = WALL_TILE;
				} else if ((i == 1 && j == 1) || (i == GRID_WIDTH - 2 && j == GRID_HEIGHT - 2)
						|| (i == 1 && j == GRID_HEIGHT - 2) || (i == GRID_WIDTH - 2 && j == 1)) {
					grid[i][j] = HOLE_TILE;
				} else if (i == 1 && j == 3) {
					grid[i][j] = START_TILE;
				} else if(i == GRID_WIDTH - 2 && j == GRID_HEIGHT - 4) {
					grid[i][j] = END_TILE;
				} else {
					grid[i][j] = GROUND_TILE;
				}
			}
		}
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		
		for (int j = 0; j < GRID_HEIGHT; ++j) {
			for (int i = 0; i < GRID_WIDTH; ++i) {
				tileBounds.left = i * TILE_WIDTH;
				tileBounds.top = j * TILE_HEIGHT;
				tileBounds.right = (i + 1) * TILE_WIDTH;
				tileBounds.bottom = (j + 1) * TILE_HEIGHT;
				
				switch(grid[i][j]) {
				case GROUND_TILE:
					canvas.drawBitmap(groundTile, null, tileBounds, null);
					break;
				case WALL_TILE:
					canvas.drawBitmap(wallTile, null, tileBounds, null);
					break;
				case HOLE_TILE:
					canvas.drawBitmap(holeTile, null, tileBounds, null);
					break;
				case START_TILE:
					canvas.drawBitmap(startTile, null, tileBounds, null);
					break;
				case END_TILE:
					canvas.drawBitmap(endTile, null, tileBounds, null);
					break;
				default:
					canvas.drawBitmap(groundTile, null, tileBounds, null);
					break;
				}
			}
		}
	}
}