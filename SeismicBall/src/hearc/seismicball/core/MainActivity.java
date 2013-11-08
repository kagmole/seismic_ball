package hearc.seismicball.core;

import hearc.seismicball.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;


public class MainActivity extends Activity {
	
	private GamePanel gamePanel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initActivity();
		createComponents();
	}
	
	private void initActivity() {
		// Landscape screen orientation
		// FIXME The screen is stuck on landscape after this
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		// Hide the window title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	private void createComponents() {
		gamePanel = new GamePanel(this);
		
		setContentView(gamePanel);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
}
