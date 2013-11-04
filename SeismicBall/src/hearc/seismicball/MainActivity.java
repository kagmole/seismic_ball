package hearc.seismicball;

import hearc.seismicball.R;

import java.util.List;





import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;


public class MainActivity extends Activity implements SensorEventListener {
	
	private SensorManager sm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//screen orientation 
		//TODO Screen stuck on landscape after this
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		//needed to activation the sensor listener
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(sm.getSensorList(Sensor.TYPE_ROTATION_VECTOR).size()!=0){
        	Sensor s = sm.getSensorList(Sensor.TYPE_ROTATION_VECTOR).get(0);
        	sm.registerListener(this,s, SensorManager.SENSOR_DELAY_NORMAL);
        }

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		
		TextView tbxTop = (TextView) findViewById(R.id.tbxTop);
		if(event.values[0]>0.1){
			tbxTop.setText("up");
		}else if(event.values[0]<-0.1){
			tbxTop.setText("down");
		}
		else{
			tbxTop.setText("nothing");
		}
		
		
		TextView tbxRight = (TextView) findViewById(R.id.tbxRight);
		if(event.values[1]>0.1){
			tbxRight.setText("left");
		}else if(event.values[1]<-0.1){
			tbxRight.setText("right");
		}
		else{
			tbxRight.setText("nothing");
		}
		
//		TextView tbxLeft = (TextView) findViewById(R.id.tbxLeft);
//		tbxLeft.setText(String.valueOf(event.values[2]));
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
	
	
}
