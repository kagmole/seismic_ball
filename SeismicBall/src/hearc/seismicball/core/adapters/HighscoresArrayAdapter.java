package hearc.seismicball.core.adapters;

import java.util.List;

import hearc.seismicball.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HighscoresArrayAdapter extends ArrayAdapter<String[]>{
	private final Context context;
	private final List<String[]> values;

	public HighscoresArrayAdapter(Context context, List<String[]> values) {
		super(context, R.layout.highscore_item, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.highscore_item, parent, false);
		TextView lblRank = (TextView) rowView.findViewById(R.id.highscoreItemLblRank);
		TextView lblScore = (TextView) rowView.findViewById(R.id.highscoreItemLblScore);
		TextView lblName = (TextView) rowView.findViewById(R.id.highscoreItemLblName);
		lblRank.setText(values.get(position)[0]);
		lblScore.setText(values.get(position)[1]);
		lblName.setText(values.get(position)[2]);

		return rowView;
	}
	
}
