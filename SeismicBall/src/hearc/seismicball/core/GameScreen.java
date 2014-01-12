package hearc.seismicball.core;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender.SendIntentException;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import hearc.seismicball.core.database.HighscoresDB;
import hearc.seismicball.elements.Ball;
import hearc.seismicball.elements.Level;
import hearc.seismicball.elements.MatrixPosition;
import hearc.seismicball.elements.Tile;
import hearc.seismicball.framework.Game;
import hearc.seismicball.framework.Graphics;
import hearc.seismicball.framework.Screen;
import hearc.seismicball.framework.implementation.AndroidGame;

public class GameScreen extends Screen implements SensorEventListener {

	private static final float STANDARD_BALL_FACTOR = 2.0f;
	private static final float STANDARD_ACCELEROMETER_LIMIT = 0.0f;

	private Ball ball;
	public Ball ballMultiplayer;

	private float accelerometerLimit;
	private float[] accelerometerValues;

	private int currentLevelID;

	private Level currentLevel;

	private SensorManager sensorManager;

	public static GameScreen instance = null;
	
	private long startTime = 0;
	private boolean wait = true;

	public GameScreen(Game game) {
		super(game);

		instance = GameScreen.this;

		ball = new Ball(false);
		ball.setSpeedFactor(STANDARD_BALL_FACTOR);

		ballMultiplayer = new Ball(true);
		

		accelerometerLimit = STANDARD_ACCELEROMETER_LIMIT;
		accelerometerValues = new float[2];

		currentLevelID = -1;

		goToNextLevel();

		sensorManager = (SensorManager) ((AndroidGame) game)
				.getSystemService(Context.SENSOR_SERVICE);

		if (sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
			Sensor rotationSensor = sensorManager.getSensorList(
					Sensor.TYPE_ACCELEROMETER).get(0);

			sensorManager.registerListener(this, rotationSensor,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		
		startTime = System.currentTimeMillis();
	}

	protected void goToNextLevel() {

		currentLevelID++;

		/* Look if there is a next level */
		if (currentLevelID < Assets.levels.size()) {
			currentLevel = Assets.levels.get(currentLevelID);

			// TODO MULTI - gérer ici le départ des joueurs
			int tileStartID = 0;

			MatrixPosition startMatrixPosition = currentLevel
					.getStartMatrixPositions().get(tileStartID);

			ball.resetMatrixPosition(startMatrixPosition.getI(),
					startMatrixPosition.getJ());
			ballMultiplayer.resetMatrixPosition(startMatrixPosition.getI(),
					startMatrixPosition.getJ());
	
		} else {
			// TODO Fin du jeu
			// XXX On boucle temporairement sur les niveaux
			currentLevelID = 0;

			instance = null;
			getNameDialog();
			
			// goToNextLevel();
		}
	}

	private void getNameDialog() {

		final AlertDialog.Builder builder = new AlertDialog.Builder((AndroidGame)game);
		
		((AndroidGame)game).runOnUiThread(new Runnable() {
		    public void run() {
		    	
				builder.setTitle("What's your name ?");

				// Set up the input
				final EditText input = new EditText((AndroidGame)game);
				// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//				input.setInputType(InputType.TYPE_CLASS_TEXT);
				builder.setView(input);
				
				startTime = System.currentTimeMillis()-startTime;
				
				// Set up the buttons
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
						HighscoresDB database = new HighscoresDB(((AndroidGame)game).getApplicationContext());
						database.addHighscore((int)(startTime)/10, input.getText().toString());
						((AndroidGame) game).finish();
						wait = false;
				    }
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        dialog.cancel();
				        ((AndroidGame) game).finish();
				        wait = false;
				    }
				});
		    	builder.show();
		    }
		});
		
		while(wait){}
		
		return;
	}

	private void restartLevel() {
		// TODO MULTI - gérer ici le départ des joueurs
		int tileStartID = 0;

		MatrixPosition startMatrixPosition = currentLevel
				.getStartMatrixPositions().get(tileStartID);

		ball.resetMatrixPosition(startMatrixPosition.getI(),
				startMatrixPosition.getJ());
	}

	@Override
	public void update(float deltaTime) {
		/* Move ball with accelerator values */
		float modifiersFactors = ball.getSpeedFactor() * deltaTime;

		float horizontalModifier = accelerometerValues[1] * modifiersFactors;
		float verticalModifier = accelerometerValues[0] * modifiersFactors;

		int deltaX = ball.getBounds().left;
		int deltaY = ball.getBounds().top;

		ball.translate((int) horizontalModifier, (int) verticalModifier);

		StartMenuActivity.instance.sendMessage("l"
				+ String.valueOf(ball.getBounds().left));
		StartMenuActivity.instance.sendMessage("t"
				+ String.valueOf(ball.getBounds().top));

		deltaX -= ball.getBounds().left;
		deltaY -= ball.getBounds().top;

		for (ArrayList<Tile> tilesRow : currentLevel.getTiles()) {
			for (Tile tile : tilesRow) {
				if (Rect.intersects(tile.getBounds(), ball.getBounds())) {
					switch (tile.getType()) {
					case Tile.TYPE_END:
						/* Collision with ending tiles */
						StartMenuActivity.instance.sendMessage("next");
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
							xAdjustment = tile.getBounds().left
									- ball.getBounds().right;
						} else if (deltaX > 0) {
							/* Left collision */
							xAdjustment = tile.getBounds().right
									- ball.getBounds().left;
						}

						if (Math.abs(xAdjustment) > Math.abs(deltaX)) {
							xAdjustment = 0;
						}

						if (deltaY > 0) {
							/* Top collision */
							yAdjustment = tile.getBounds().bottom
									- ball.getBounds().top;
						} else if (deltaY < 0) {
							/* Bottom collision */
							yAdjustment = tile.getBounds().top
									- ball.getBounds().bottom;
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
				g.drawImage(tile.getImage(), tile.getBounds().left,
						tile.getBounds().top);
			}
		}
		g.drawImage(ball.getImage(), ball.getBounds().left,
				ball.getBounds().top);
		
		// if multiplayer then draw the ball
		// work still there isn't collision with the other ball
		if(!StartMenuActivity.instance.isMultiplayer()){
			g.drawImage(ballMultiplayer.getImage(),
					ballMultiplayer.getBounds().left,
					ballMultiplayer.getBounds().top);
		}
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

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			if (event.values[0] > accelerometerLimit
					|| event.values[0] < -accelerometerLimit) {
				accelerometerValues[0] = event.values[0];
			} else {
				accelerometerValues[0] = 0.0f;
			}

			if (event.values[1] > accelerometerLimit
					|| event.values[1] < -accelerometerLimit) {
				accelerometerValues[1] = event.values[1];
			} else {
				accelerometerValues[1] = 0.0f;
			}
		}
	}

}
