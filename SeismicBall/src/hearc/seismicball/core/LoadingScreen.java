package hearc.seismicball.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import hearc.seismicball.elements.Level;
import hearc.seismicball.elements.MatrixPosition;
import hearc.seismicball.elements.Tile;
import hearc.seismicball.framework.Game;
import hearc.seismicball.framework.Graphics;
import hearc.seismicball.framework.Graphics.ImageFormat;
import hearc.seismicball.framework.Screen;
import hearc.seismicball.framework.FileIO;

public class LoadingScreen extends Screen {
	
	private static String ballFilePath = "images/ball.png";
	private static String tileEndFilePath = "images/tile_end.png";
	private static String tileGroundFilePath = "images/tile_ground.png";
	private static String tileHoleFilePath = "images/tile_hole.png";
	private static String tileStartFilePath = "images/tile_start.png";
	private static String tileWallFilePath = "images/tile_wall.png";
	
	private static String level001Name = "Level 1";
	private static String level001FilePath = "levels/level001.txt";
	private static String level002Name = "Level 2";
	private static String level002FilePath = "levels/level002.txt";
	
	public LoadingScreen(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		
		Assets.ball = g.newImage(ballFilePath, ImageFormat.ARGB4444);
		Assets.tileEnd = g.newImage(tileEndFilePath, ImageFormat.ARGB4444);
		Assets.tileGround = g.newImage(tileGroundFilePath, ImageFormat.ARGB4444);
		Assets.tileHole = g.newImage(tileHoleFilePath, ImageFormat.ARGB4444);
		Assets.tileStart = g.newImage(tileStartFilePath, ImageFormat.ARGB4444);
		Assets.tileWall = g.newImage(tileWallFilePath, ImageFormat.ARGB4444);
		
		FileIO fileIO = game.getFileIO();
		
		Assets.levels = new ArrayList<Level>();
		
		try {
			Assets.levels.add(loadLevel(level001Name, fileIO.readAsset(level001FilePath)));
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load level '"
					+ level001Name + "' from asset '"
					+ level001FilePath + "'");
		}
		
		try {
			Assets.levels.add(loadLevel(level002Name, fileIO.readAsset(level002FilePath)));
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load level '"
					+ level002Name + "' from asset '"
					+ level002FilePath + "'");
		}
		
		game.setScreen(new GameScreen(game));
	}
	
	private Level loadLevel(String levelName, InputStream fileStream) throws IOException {
		ArrayList<ArrayList<Tile>> tilesArray = new ArrayList<ArrayList<Tile>>();
		
		ArrayList<MatrixPosition> startMatrixPositions = new ArrayList<MatrixPosition>();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
		
		String row = reader.readLine();
		
		int i;
		int j = 0;
		
		while (row != null) {
			ArrayList<Tile> tilesRow = new ArrayList<Tile>();
			
			i = 0;
			
			for (String rowChar : row.split("")) {
				if (rowChar.matches("[0-9]")) {
					Tile tile = new Tile(i, j, Integer.parseInt(rowChar));
					
					tilesRow.add(tile);
					
					if (tile.getType() == Tile.TYPE_START) {
						startMatrixPositions.add(new MatrixPosition(i, j));
					}
					
					i++;
				}
			}
			tilesArray.add(tilesRow);
			row = reader.readLine();
			
			j++;
		}
		reader.close();
		
		return new Level(levelName, tilesArray, startMatrixPositions);
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		// TODO g.drawImage(Assets.splash, 0, 0);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void backButton() {
		// TODO Auto-generated method stub

	}

}
