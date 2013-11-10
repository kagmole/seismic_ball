// manage adding highscores, getting highscores and reset the table

package hearc.seismicball.core.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HighscoresDB {
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "highscores.db";
 
	private static final String TABLE_HIGHSCORES = "table_highscores";
	private static final String COL_ID = "id";
	private static final int NUM_COL_ID = 0;
	private static final String COL_SCORE = "score";
	private static final int NUM_COL_SCORE = 1;
	private static final String COL_NAME = "name";
	private static final int NUM_COL_NAME = 2;
	
	private SQLiteDatabase database;
	 
	private HighscoreSQLite highscoreSQLite;
	
	public HighscoresDB(Context context){
		highscoreSQLite = new HighscoreSQLite(context, DB_NAME, null, DB_VERSION);
	}
	
	private void open(){
		database = highscoreSQLite.getWritableDatabase();
	}
 
	private void close(){
		database.close();
	}
	
	public long addHighscore(int score, String name){
		open();
		ContentValues values = new ContentValues();
		values.put(COL_SCORE, score);
		values.put(COL_NAME, name);
		long result = database.insert(TABLE_HIGHSCORES, null, values); 
		close();
		return result;
	}
	
	public List<String[]> getHighscores(){
		// open the DB
		open();
		//query give all the highscores order by desc
		Cursor c = database.query(TABLE_HIGHSCORES, new String[] {COL_ID, COL_SCORE, COL_NAME}, null , null,  null, null, COL_SCORE+" DESC");
		// list with String[3] {rank,score,name}
		List<String[]> highscores = new ArrayList<String[]>();
		// the rank value who be increment on each movenext()
		int rank = 1;
		
		// if there is no row, return empty list
		if (c.getCount() == 0)
			return highscores;
		
		// move to first row of the query result
		c.moveToFirst();
		while(!c.isAfterLast()){
			String[] highscore = new String[3];
			highscore[0] = String.valueOf(rank);
			highscore[1] = String.valueOf(c.getInt(NUM_COL_SCORE));
			highscore[2] = c.getString(NUM_COL_NAME);
			highscores.add(highscore);
			rank++;
			c.moveToNext();
		}		
		//close the DB
		close();
		return highscores;
	}
	
	public void resetTable(){
		open();
		database.delete(TABLE_HIGHSCORES, null, null);
		close();
	}
}
