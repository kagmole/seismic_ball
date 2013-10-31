package com.example.seismicball;

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
        if(sm.getSensorList(Sensor.TYPE_GYROSCOPE).size()!=0){
        	Sensor s = sm.getSensorList(Sensor.TYPE_GYROSCOPE).get(0);
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
		TextView test = (TextView) findViewById(R.id.textView);
		test.setText(String.valueOf(event.values[0]));
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
	
	
}
