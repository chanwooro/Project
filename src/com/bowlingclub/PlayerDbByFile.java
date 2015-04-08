package com.bowlingclub;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class PlayerDbByFile {
	
	static final String folderName = "BowlingPlayers";
	public static final File App_Folder = new File(Environment.getExternalStorageDirectory() + File.separator + folderName);
	public static final File Saved_File = new File(Environment.getExternalStorageDirectory() + File.separator + folderName + File.separator + "players.txt");
	
	private static final int KEY_ID = 0;
	private static final int KEY_NAME = 1;
	private static final int KEY_STATUS = 2;
	private static final int KEY_SCORE = 3;
	
	public PlayerDbByFile () {
		checkSaveFile();
	}
	
	public void checkSaveFile ()
	{
		if (!(App_Folder.exists()))
		{
			App_Folder.mkdirs();
			Log.i("App", "Folder is created");
		}
		
		if (!(Saved_File.isFile()))
		{
			//create it
			try {
				Saved_File.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void addPlayer(Player player){
		
		try {
			player.setId(countPlayers());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		player.setStatus(1);
		
		String write_to_file = player.getId() + "," + player.getName() + "," + player.getStatus() + "," + player.getScoresStr() + "\n";
				
		try {
			OutputStream playerFile = new FileOutputStream(Saved_File, true);
			playerFile.write(write_to_file.getBytes());
			playerFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		Log.d("db", "Added player: " + player.toString());
	}
	
	public int countPlayers() throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(Saved_File));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	
	public List<Player> getAllPlayers() {
		Log.d("db", "getAllPlayers is triggered");
		List<Player> playerList = new ArrayList<Player>();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(Saved_File)); 
			String s;
			while ((s=in.readLine()) != null)
			{
				List<String> tempList = Arrays.asList(s.split(","));
				Player player = new Player();
				String ss = null;
				
				if (tempList.size() < 3) continue; //if the file is corrupted
				
				try{
					int id = Integer.parseInt(tempList.get(KEY_ID).trim());
					int status = Integer.parseInt(tempList.get(KEY_STATUS).trim());
					
					player.setId(id);
					player.setName(tempList.get(KEY_NAME));
					player.setStatus(status);
					
					for (int i=KEY_SCORE; i<tempList.size(); i++)
					{
						ss += tempList.get(i) + ",";
					}
					ss = ss.replaceAll(",$", ""); //remove the last comma
					
					player.setScore(ss);
					playerList.add(player);
				} catch (NumberFormatException e){
					continue;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return playerList;
	}
	
	public void updateAllPlayer(List<Player> players) {

	}
	
	public void deletePlayer(Player player){
		Log.d("db", "Deleted player: " + player.toString());
	}
	
	public void clearAllScores() {
		Log.d("db", "All scores are going to be erased");
	}
}
