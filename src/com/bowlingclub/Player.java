package com.bowlingclub;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Player implements Comparable<Player>{
	private int id;
	private String name;
	private int status;
	private List<Integer> scores = new ArrayList<Integer>();

	
	public Player ()
	{
		super();
		this.name = null;
		this.status = 0;
		this.scores.add(0);
	}
	public Player (String n)
	{
		this.name = n;
		this.status = 0;
		this.scores.add(0);
	}
	
	//Gettings///
	public String getName()
	{
		return this.name;
	}
	
	public List<Integer> getScoresArray()
	{
		return this.scores;
	}

	public String getScoresStr()
	{ 
		String strScore = " ";
		
		for (int i = 0; i < scores.size(); i++)
		{
			strScore += scores.get(i).toString() + ",";
		}
		
		if (strScore.endsWith(",")) {
			strScore = strScore.substring(0, strScore.length() - 1);
		}
		
		return strScore.replaceAll("\\s+", "");
	}
	
	public int getStatus ()
	{
		return this.status;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	
	///Settings///
	public void setStatus(int st)
	{
		this.status = st;
	}
	
	public void addScore(int x)
	{
		this.scores.add(x);
	}
	
	public void setScore (String xs)
	{
		this.scores.clear();
		String[] sets = xs.split(",");
		for (int i = 0; i < sets.length; i++)
		{
			String temp = sets[i].replaceAll("\\s+", "");
			this.scores.add(Integer.parseInt(temp));			
		}
	}

	public void setName(String n)
	{
		this.name = n; 
	}
	
	public void setId (int a)
	{
		this.id = a;
	}
	@Override
	public int compareTo(Player another) {
		// TODO Auto-generated method stub
		if (this.name != null)
		{
			return this.name.toLowerCase().compareTo(another.getName().toLowerCase());			
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
	

}
