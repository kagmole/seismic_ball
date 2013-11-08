package hearc.seismic.readfiletest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class LevelLoader {
	public static List<List<Integer>> getLevel(String _fileName, Context _context){
		List<List<Integer>> levelGrid = new ArrayList<List<Integer>>();
		try {
			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(_context.getAssets().open(_fileName)));	
						 
			String mLine = reader.readLine();
			while (mLine != null) {
				List<Integer> currentLevelGrid = new ArrayList<Integer>();
				for(String myChar:mLine.split("")){
					//check if it's numeric
					if(myChar.matches("[0-9]")){
						currentLevelGrid.add(Integer.parseInt(myChar));					
					}
				}
				levelGrid.add(currentLevelGrid);
				mLine = reader.readLine();
			}			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return levelGrid;
	}
}
