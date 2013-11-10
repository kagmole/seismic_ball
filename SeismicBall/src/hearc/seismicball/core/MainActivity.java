package hearc.seismicball.core;

import hearc.seismicball.R;
import hearc.seismicball.core.database.HighscoresDB;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.Window;


public class MainActivity extends Activity implements SensorEventListener{
	
	private GamePanel gamePanel;
	private Handler gameHandler;
	private SensorManager sm;
	private long startTime;
	public boolean isVictory;//moche ce flag...
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		startTime = System.currentTimeMillis();
		isVictory = false;
		
		initActivity();
		createComponents();
//		startGameLoop();
	}
	
	private void initActivity() {
		// Landscape screen orientation
		// FIXME The screen is stuck on landscape after this
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		// Hide the window title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// Sensor initialisation
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		
        if(sm.getSensorList(Sensor.TYPE_ROTATION_VECTOR).size() != 0) {
        	Sensor s = sm.getSensorList(Sensor.TYPE_ROTATION_VECTOR).get(0);
        	
        	sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
        }
	}
	
	private void createComponents() {
		gamePanel = new GamePanel(this);
		
		setContentView(gamePanel);
	}
	
	private void startGameLoop() {
		gameHandler = new Handler();
		
		Runnable task = new Runnable() {
			
		    @Override
		    public void run() {
		    	// TODO Change to dynamic value
		        gamePanel.updateGame(10, 0);
		        gamePanel.invalidate();

		        
		        gameHandler.postDelayed(this, 100);
		    }
		};
		gameHandler.removeCallbacks(task);
		gameHandler.post(task);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		 // nothing to do		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float deadzone = 0.1f;
		int speed = 10;//should be in the GamePanel class
		if(event.values[0] > deadzone){
			 gamePanel.updateGame(0, -speed);
		}
		if(event.values[0] < -deadzone){
			gamePanel.updateGame(0, speed);
		}
		if(event.values[1] > deadzone){
			gamePanel.updateGame(-speed, 0);
		}
		if(event.values[1] < -deadzone){
			gamePanel.updateGame(speed, 0);
		}
	}
	
	@Override
	protected void onDestroy(){
		if(isVictory){
			HighscoresDB database = new HighscoresDB(getApplicationContext());
			database.addHighscore((int)(System.currentTimeMillis()-startTime)/1000, "DEV");
		}
		super.onDestroy();
	}
}
