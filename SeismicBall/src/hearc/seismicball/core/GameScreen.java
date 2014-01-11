package hearc.seismicball.core;

import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.ArrayList;

import hearc.seismicball.elements.Ball;
import hearc.seismicball.elements.Level;
import hearc.seismicball.elements.MatrixPosition;
import hearc.seismicball.elements.Tile;
import hearc.seismicball.framework.Game;
import hearc.seismicball.framework.Graphics;
import hearc.seismicball.framework.Screen;

public class GameScreen extends Screen implements SensorEventListener {
	
	private static final float STANDARD_BALL_FACTOR = 0.5f;
	private static final float STANDARD_ACCELEROMETER_LIMIT = 2.5f;
	
	private Ball ball;
	
	private float accelerometerLimit;
	private float[] accelerometerValues;
	
	private int currentLevelID;
	
	private Level currentLevel;
	
	public GameScreen(Game game) {
		super(game);
		
		ball = new Ball();
		ball.setSpeedFactor(STANDARD_BALL_FACTOR);
		
		accelerometerLimit = STANDARD_ACCELEROMETER_LIMIT;
		accelerometerValues = new float[2];
		
		currentLevelID = -1;
		
		goToNextLevel();
	}
	
	private void goToNextLevel() {
		currentLevelID++;
		
		/* Look if there is a next level */
		if (currentLevelID < Assets.levels.size()) {
			currentLevel = Assets.levels.get(currentLevelID);
			
			// TODO MULTI - gérer ici le départ des joueurs
			int tileStartID = 0;
			
			MatrixPosition startMatrixPosition = currentLevel.getStartMatrixPositions().get(tileStartID);
			
			ball.resetMatrixPosition(startMatrixPosition.getI(), startMatrixPosition.getJ());
		} else {
			// TODO Fin du jeu
			// XXX On boucle temporairement sur les niveaux
			currentLevelID = -1;
			goToNextLevel();
		}
	}
	
	private void restartLevel() {
		// TODO MULTI - gérer ici le départ des joueurs
		int tileStartID = 0;
		
		MatrixPosition startMatrixPosition = currentLevel.getStartMatrixPositions().get(tileStartID);
		
		ball.resetMatrixPosition(startMatrixPosition.getI(), startMatrixPosition.getJ());
	}

	@Override
	public void update(float deltaTime) {
		/* Move ball with accelerator values */
		float modifiersFactors = ball.getSpeedFactor() * deltaTime;
		
		float horizontalModifier = accelerometerValues[0] * modifiersFactors;
		float verticalModifier = accelerometerValues[1] * modifiersFactors;
		
		int deltaX = ball.getBounds().left;
		int deltaY = ball.getBounds().top;
		
		ball.translate((int) horizontalModifier, (int) verticalModifier);
		
		deltaX -= ball.getBounds().left;
		deltaY -= ball.getBounds().top;
		
		for (ArrayList<Tile> tilesRow : currentLevel.getTiles()) {
			for (Tile tile : tilesRow) {
				if (Rect.intersects(tile.getBounds(), ball.getBounds())) {
					switch (tile.getType()) {
					case Tile.TYPE_END:
						/* Collision with ending tiles */
						goToNextLevel();
						break;
					case Tile.TYPE_HOLE:
						/* Collision with holes */
						restartLevel();
						break;
					case Tile.TYPE_WALL:
						/* Collision with walls */
						int xAdjustment = 0;
						int yAdjustment = 0;
						
						if (deltaX < 0) {
							/* Right collision */
							xAdjustment = tile.getBounds().left - ball.getBounds().right;
						} else if (deltaX > 0) {
							/* Left collision */
							xAdjustment = tile.getBounds().right - ball.getBounds().left;
						}
						
						if (Math.abs(xAdjustment) > Math.abs(deltaX)) {
							xAdjustment = 0;
						}
						
						if (deltaY > 0) {
							/* Top collision */
							yAdjustment = tile.getBounds().bottom - ball.getBounds().top;
						} else if (deltaY < 0) {
							/* Bottom collision */
							yAdjustment = tile.getBounds().top - ball.getBounds().bottom;
						}
						
						if (Math.abs(yAdjustment) > Math.abs(deltaY)) {
							yAdjustment = 0;
						}
						
						ball.translate(xAdjustment, yAdjustment);
						break;
					default:
						/* Nothing to do here */
						break;
					}
				}
			}
		}
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.clearScreen(0);
		
		for (ArrayList<Tile> tilesRow : currentLevel.getTiles()) {
			for (Tile tile : tilesRow) {
				g.drawImage(tile.getImage(), tile.getBounds().left, tile.getBounds().top);
			}
		}
		g.drawImage(ball.getImage(), ball.getBounds().left, ball.getBounds().top);
	}

	@Override
	public void pause() {
		// TODO mettre le jeu en pause
	}

	@Override
	public void resume() {
		// TODO relancer le jeu
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void backButton() {
		// TODO mettre le jeu en pause
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int arg) {
		// Nothing to do
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			if (event.values[0] > accelerometerLimit || event.values[0] < -accelerometerLimit) {
				accelerometerValues[0] = event.values[0];
			} else {
				accelerometerValues[0] = 0.0f;
			}

			if (event.values[1] > accelerometerLimit || event.values[1] < -accelerometerLimit) {
				accelerometerValues[1] = event.values[1];
			} else {
				accelerometerValues[1] = 0.0f;
			}
		}
	}

}
