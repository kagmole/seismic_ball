package hearc.seismicball.core;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class RotationSensorActivity extends Activity implements SensorEventListener {
	
	private SensorManager sm;
	
	public RotationSensorActivity() {		
		// Sensor listener's activation
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		
        if(sm.getSensorList(Sensor.TYPE_ROTATION_VECTOR).size() != 0) {
        	Sensor s = sm.getSensorList(Sensor.TYPE_ROTATION_VECTOR).get(0);
        	
        	sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
        }
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Nothing to do here
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		/*
		 * event.values[0] > 0: up
		 * event.values[0] < 0: down
		 * event.values[1] > 0: left
		 * event.values[1] < 0: right
		 */
	}

}
