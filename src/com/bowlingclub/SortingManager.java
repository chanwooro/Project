package com.bowlingclub;

import java.util.ArrayList;
import java.util.List;

import com.bowlingclub.Player;

public class SortingManager {
	
	public static List<Player> players = new ArrayList<Player>();
	public static List<Integer> playersAvg = new ArrayList<Integer>();
	public static List<Integer> playersMod = new ArrayList<Integer>();
	public static List<Integer> playersBest = new ArrayList<Integer>();
	public static List<Integer> playersWorst = new ArrayList<Integer>();
	
	public SortingManager (List<Player> players)
	{
		onClear();
		
		SortingManager.players = players;
		SimpleMaths sm = new SimpleMaths();
		for (int a = 0; a < players.size(); a++)
		{
			playersAvg.add(sm.getAverage(players.get(a).getScoresArray()));
			playersMod.add(sm.getMedian(players.get(a).getScoresArray()));
			playersBest.add(sm.getBest(players.get(a).getScoresArray()));
			playersWorst.add(sm.getWorst(players.get(a).getScoresArray()));
		}
		
	}
	
	public void performFLP ()
	{
		
	}
	
	public void onClear ()
	{
		try
		{
			players.clear();
			playersAvg.clear();
			playersBest.clear();
			playersMod.clear();
			playersWorst.clear();
		} catch (NullPointerException n)
		{
			
		}
		
	}
}
