package com.bowlingclub;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class CustomList extends ArrayAdapter<String>{
	private final Activity context;
	public List<String> names;
	public List<String> scores;
	
	public CustomList(Activity context, List<String> names, List<String> scores) {
		super(context, R.layout.mainlistsingle, names);
		this.context = context;
		this.names = names;
		this.scores = scores;
	}
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.mainlistsingle, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.main_sub_name);
		TextView txtScores = (TextView) rowView.findViewById(R.id.main_sub_scores);
		
		txtTitle.setText(names.get(position));
		txtScores.setText(scoreShow(scores.get(position)));
		return rowView;
	}	
	
	public String scoreShow (String input)
	{
		boolean Zflag = false;
		String out = "";
		String[] sets = input.split(",");
		for (int i = 0; i < sets.length; i++)
		{
			String temp = sets[i].replaceAll("\\s+", "");
					
			if (Integer.parseInt(temp) != 0)
			{
				//Zflag = true;
				out += temp + "\t\t\t";
			}
			else if ((Integer.parseInt(temp) == 0) && (Zflag))
			{
				out += temp + "\t\t\t";
			}
		}
		return out;
	}
	
}
