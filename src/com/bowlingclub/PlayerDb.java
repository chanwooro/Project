package com.bowlingclub;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlayerDb extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "BowlingPlayers";
	private static final String TABLE_PLAYERS = "players";
	
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_STATUS = "status";
	private static final String KEY_SCORE = "bowlscores";
	
	public PlayerDb (Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate (SQLiteDatabase db) {	
		String sql = "CREATE TABLE " + TABLE_PLAYERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_NAME + " TEXT,"
				+ KEY_STATUS + " INTEGER,"
                + KEY_SCORE + " TEXT)";
		db.execSQL(sql);
	}
	
	public void onUpgrade (SQLiteDatabase db, int oldV, int newV){
		Log.d("db", "Database is updated");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
		onCreate(db);
	}

	public void addPlayer(Player player){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, player.getName());
		values.put(KEY_STATUS, player.getStatus()); //status = 1 if this person is on the list already, if not, 0
		values.put(KEY_SCORE, player.getScoresStr());
		db.insert(TABLE_PLAYERS, null, values);
		db.close();
		Log.d("db", "Added player: " + player.toString());
	}
	
	public List<Player> getAllPlayers() {
		Log.d("db", "getAllPlayers is triggered");
		List<Player> playerList = new ArrayList<Player>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_PLAYERS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		Log.d("db", "Column number is : " + cursor.getColumnCount());
	
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Player player = new Player();
				player.setId(cursor.getInt(0));
				player.setName(cursor.getString(1));
				player.setStatus(cursor.getInt(2));
				player.setScore(cursor.getString(3));
				
				playerList.add(player);
			} while (cursor.moveToNext());
		}
		// return task list
		return playerList;
	}
	
	public void updatePlayer(Player player) {
		// updating row
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, player.getName());
		values.put(KEY_STATUS, player.getStatus());
		values.put(KEY_SCORE, player.getScoresStr());
		
		db.update(TABLE_PLAYERS, values, KEY_ID + " = ?",new String[] {String.valueOf(player.getId())});
		db.close();
		Log.d("db", "Updated player: " + player.toString());
	}
	
	public void deletePlayer(Player player){
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_PLAYERS, KEY_ID+" = ?", new String[]{String.valueOf(player.getId())});
		
		db.close();
		Log.d("db", "Deleted player: " + player.toString());
	}
	
	public void clearAllScores() {
		Log.d("db", "All scores are going to be erased");
		List<Player> playerList = new ArrayList<Player>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_PLAYERS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
	
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Player player = new Player();
				player.setId(cursor.getInt(0));
				player.setName(cursor.getString(1));
				player.setStatus(cursor.getInt(2));
				player.setScore(cursor.getString(3));
				playerList.add(player);
			} while (cursor.moveToNext());
		}
		// return task list
		
		for (int i=0; i< cursor.getCount(); i++)
		{
			Player player = new Player();
			player = playerList.get(i);
			ContentValues values = new ContentValues();
			
			values.put(KEY_NAME, player.getName());
			values.put(KEY_STATUS, player.getStatus());
			values.put(KEY_SCORE, "0");
			db.update(TABLE_PLAYERS, values, KEY_ID + " = ?",new String[] {String.valueOf(player.getId())});
		}

		db.close();
	}	
}
