package hearc.seismicball.elements;

import java.util.ArrayList;

public class Level {

	private ArrayList<ArrayList<Tile>> tilesArray;

	private ArrayList<MatrixPosition> startMatrixPositions;

	private int levelMatrixWidth;
	private int levelMatrixHeight;
	
	private String levelName;

	public Level(String levelName, ArrayList<ArrayList<Tile>> tilesArray,
			ArrayList<MatrixPosition> startMatrixPositions) {
		this.levelName = levelName;

		this.tilesArray = tilesArray;

		this.startMatrixPositions = startMatrixPositions;
		
		levelMatrixWidth = tilesArray.size();
		
		if (levelMatrixWidth > 0) {
			levelMatrixHeight = tilesArray.get(0).size();
		} else {
			levelMatrixHeight = 0;
		}
	}

	public ArrayList<ArrayList<Tile>> getTiles() {
		return tilesArray;
	}

	public ArrayList<MatrixPosition> getStartMatrixPositions() {
		return startMatrixPositions;
	}
	
	public int getLevelMatrixWidth() {
		return levelMatrixWidth;
	}
	
	public int getLevelMatrixHeight() {
		return levelMatrixHeight;
	}

	public String getName() {
		return levelName;
	}
}
