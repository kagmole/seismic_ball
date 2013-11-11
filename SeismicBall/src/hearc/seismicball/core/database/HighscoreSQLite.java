// class who manage the connection to the SQLLite
// create the highscore table if there is no table

package hearc.seismicball.core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class HighscoreSQLite  extends SQLiteOpenHelper{
	
	private static final String TABLE_HIGHSCORE = "table_highscores";
	private static final String COL_ID = "id";
	private static final String COL_SCORE = "score";
	private static final String COL_NAME = "name";
 
	private static final String CREATE_DB = "CREATE TABLE " + TABLE_HIGHSCORE + " ("
	+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_SCORE + " INTEGER NOT NULL, "
	+ COL_NAME + " TEXT NOT NULL);";

	public HighscoreSQLite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DB);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	

}
