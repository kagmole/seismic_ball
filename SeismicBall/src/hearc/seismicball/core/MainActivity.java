package hearc.seismicball.core;

import hearc.seismicball.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.Window;


public class MainActivity extends Activity {
	
	private GamePanel gamePanel;
	private Handler gameHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initActivity();
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
}
