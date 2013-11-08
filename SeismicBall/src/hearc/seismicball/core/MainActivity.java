package hearc.seismicball.core;

import hearc.seismicball.R;
import android.annotation.SuppressLint;
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


public class MainActivity extends Activity implements SensorEventListener {
	
	private GamePanel gamePanel;
	private Handler gameHandler;
	private SensorManager sensorManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initActivity();
		initSensorManager();
		createComponents();
		startGameLoop();
	}
	
	private void initActivity() {
		// Landscape screen orientation
		// FIXME The screen is stuck on landscape after this
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		// Hide the window title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	@SuppressLint("InlinedApi")
	private void initSensorManager() {
		// Sensor listener's activation
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
        if(sensorManager.getSensorList(Sensor.TYPE_ROTATION_VECTOR).size() != 0) {
        	Sensor rotationSensor = sensorManager.getSensorList(Sensor.TYPE_ROTATION_VECTOR).get(0);
        	
        	sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// Nothing to do here for the moment
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO
		/*
		 * event.values[0] > 0: up
		 * event.values[0] < 0: down
		 * event.values[1] > 0: left
		 * event.values[1] < 0: right
		 */
	}
}
