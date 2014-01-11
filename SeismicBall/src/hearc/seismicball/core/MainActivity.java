//package hearc.seismicball.core;
//
//import hearc.seismicball.R;
//import android.annotation.SuppressLint;
//import hearc.seismicball.core.database.HighscoresDB;
//import android.app.Activity;
//import android.content.pm.ActivityInfo;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.Menu;
//import android.view.Window;
//
//
//public class MainActivity extends Activity implements SensorEventListener {
//	
//	private GamePanel gamePanel;
//	private Handler gameHandler;
//	private SensorManager sensorManager;
//	
//	public boolean isVictory;
//	private long startTime;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		startTime = System.currentTimeMillis();
//		isVictory = false;
//		
//		initActivity();
//		initSensorManager();
//		createComponents();
//	}
//	
//	private void initActivity() {
//		// Landscape screen orientation
//		// FIXME The screen is stuck on landscape after this
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		
//		// Hide the window title
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//	}
//	
//	@SuppressLint("InlinedApi")
//	private void initSensorManager() {
//		// Sensor listener's activation
//		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//		
//        if(sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
//        	Sensor rotationSensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
//        	
//        	sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
//        }
//	}
//	
//	private void createComponents() {
//		gamePanel = new GamePanel(this);
//		
//		setContentView(gamePanel);
//	}
//	
////	private void startGameLoop() {
////		gameHandler = new Handler();
////		
////		Runnable task = new Runnable() {
////			
////		    @Override
////		    public void run() {
////		    	// TODO Change to dynamic value
////		        gamePanel.updateGame(10, 0);
////		        
////		        Log.i("BLA", "TEST");
////		        
////		        gameHandler.postDelayed(this, 100);
////		    }
////		};
////		gameHandler.removeCallbacks(task);
////		gameHandler.post(task);
////	}
//	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		
//		return true;
//	}
//
//	@Override
//	public void onAccuracyChanged(Sensor sensor, int accuracy) {
//		 // nothing to do		
//	}
//
//	@Override
//	public void onSensorChanged(SensorEvent event) {
//		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
//			float deadzone = 2.5f;
//			int speed = 10;//should be in the GamePanel class
//			if(event.values[0] > deadzone){
//				 gamePanel.updateGame(0, speed);
//			}
//			if(event.values[0] < -deadzone){
//				gamePanel.updateGame(0, -speed);
//			}
//			if(event.values[1] > deadzone){
//				gamePanel.updateGame(speed, 0);
//			}
//			if(event.values[1] < -deadzone){
//				gamePanel.updateGame(-speed, 0);
//			}
//		}
//	}
//	
//	@Override
//	protected void onDestroy(){
//		if(isVictory){
//			HighscoresDB database = new HighscoresDB(getApplicationContext());
//			database.addHighscore((int)(System.currentTimeMillis()-startTime)/1000, "DEV");
//		}
//		super.onDestroy();
//	}
//}
