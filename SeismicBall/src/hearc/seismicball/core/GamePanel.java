//package hearc.seismicball.core;
//
//import java.sql.Date;
//import java.util.LinkedList;
//import java.util.List;
//
//import hearc.seismicball.R;
//import hearc.seismicball.core.database.HighscoresDB;
//import hearc.seismicball.elements.Ball;
//import hearc.seismicball.elements.Tile;
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Rect;
//import android.text.format.Time;
//import android.view.View;
//
//public class GamePanel extends View {
//	
//	private static final int GROUND_TILE = 0;
//	private static final int WALL_TILE = 1;
//	private static final int HOLE_TILE = 2;
//	private static final int START_TILE = 3;
//	private static final int END_TILE = 4;
//	
//	private static final int TILE_WIDTH = 64;
//	private static final int TILE_HEIGHT = 64;
//	
//	private Ball ball;
//	
//	private Bitmap ballImage = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
//	private Bitmap groundTileImage = BitmapFactory.decodeResource(getResources(), R.drawable.ground_tile);
//	private Bitmap wallTileImage = BitmapFactory.decodeResource(getResources(), R.drawable.wall_tile);
//	private Bitmap holeTileImage = BitmapFactory.decodeResource(getResources(), R.drawable.hole_tile);
//	private Bitmap startTileImage = BitmapFactory.decodeResource(getResources(), R.drawable.start_tile);
//	private Bitmap endTileImage = BitmapFactory.decodeResource(getResources(), R.drawable.end_tile);
//	
//	private LinkedList<Tile> gridTiles;
//	private LinkedList<Tile> wallTiles;
//	
//	private Tile endTile;
//	MainActivity mainActivity;
//
//
//	public GamePanel(MainActivity mainActivity) {
//		super(mainActivity.getApplicationContext());
//		this.mainActivity = mainActivity;
//		
//		initTilesAndBalls();
//	}
//	
//	private void initTilesAndBalls() {		
//		int i;
//		int j = 0;
//		
//		int left;
//		int top;
//		int right;
//		int bottom;
//		
//		List<List<Integer>> grid = LevelLoader.getLevel("levels/001.lvl", getContext());
//		
//		gridTiles = new LinkedList<Tile>();
//		wallTiles = new LinkedList<Tile>();
//		
//		for (List<Integer> gridRow : grid) {
//			i = 0;
//			
//			for (Integer gridValue : gridRow) {
//				left = i * TILE_WIDTH;
//				top = j * TILE_HEIGHT;
//				right = left + TILE_WIDTH;
//				bottom = top + TILE_HEIGHT;
//				
//				switch(gridValue) {
//				case GROUND_TILE:
//					gridTiles.add(new Tile(left, top, right, bottom, groundTileImage));
//					break;
//				case WALL_TILE:
//					Tile tile = new Tile(left, top, right, bottom, wallTileImage);
//					
//					gridTiles.add(tile);
//					wallTiles.add(tile);
//					break;
//				case HOLE_TILE:
//					gridTiles.add(new Tile(left, top, right, bottom, holeTileImage));
//					break;
//				case START_TILE:
//					gridTiles.add(new Tile(left, top, right, bottom, startTileImage));
//					
//					ball = new Ball(left, top, right, bottom, ballImage);
//					break;
//				case END_TILE:
//					endTile = new Tile(left, top, right, bottom, endTileImage);
//					gridTiles.add(endTile);
//					break;
//				default:
//					gridTiles.add(new Tile(left, top, right, bottom, groundTileImage));
//					break;
//				}
//				++i;
//			}
//			++j;
//		}
//	}
//	
//	public void updateGame(int x, int y) {
//		ball.translate(x, y);
//		
//		for (Tile tile : wallTiles) {
//			if (Rect.intersects(ball.getBounds(), tile.getBounds())) {
//				ball.translate(-x, -y);
//			}
//		}
//		
//		if(Rect.intersects(ball.getBounds(), endTile.getBounds())){
//			mainActivity.isVictory = true;
//			mainActivity.finish();
//		}
//		invalidate(ball.getBounds());
//	}
//	
//	@Override
//	public void onDraw(Canvas canvas) {
//		canvas.drawColor(Color.BLACK);
//		
//		for (Tile tile : gridTiles) {
//			tile.drawElement(canvas);
//		}
//		ball.drawElement(canvas);
//	}
//}