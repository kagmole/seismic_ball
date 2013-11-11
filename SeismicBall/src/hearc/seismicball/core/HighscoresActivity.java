package hearc.seismicball.core;

import java.util.List;

import hearc.seismicball.R;
import hearc.seismicball.core.adapters.HighscoresArrayAdapter;
import hearc.seismicball.core.database.HighscoresDB;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ListView;

public class HighscoresActivity extends Activity{
	
	HighscoresDB highscoresDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.highscore_lay);	
		
		highscoresDB = new HighscoresDB(getApplicationContext());
		List<String[]>values = highscoresDB.getHighscores();
		
		// fill the listview with a 2D string array
		ListView lv = (ListView) findViewById(R.id.highscoreLayLvHighscores);
		HighscoresArrayAdapter adapter = new HighscoresArrayAdapter(this,values); 
		lv.setAdapter(adapter);
		
		// set listener on the back button
		Button btnBack = (Button)findViewById(R.id.highscoreLayBtnBack);
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// set listener on the reset button
		Button btnReset = (Button)findViewById(R.id.highscoreLayBtnReset);
		btnReset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(HighscoresActivity.this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("Closing Activity")
		        .setMessage("Are you sure you want to reset the database?")
		        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
			    {
			        @Override
			        public void onClick(DialogInterface dialog, int which) {
			        	highscoresDB.resetTable();
			        	finish();
			        }
	
			    })
			    .setNegativeButton("No", null)
			    .show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
