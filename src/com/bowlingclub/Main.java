package com.bowlingclub;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class Main extends Activity {

	private PlayerDbByFile db = new PlayerDbByFile();
	
	public static List<Player> players;
	
	public static List<Player> SelectedPlayers = new ArrayList<Player>();
	public static List<String> names = new ArrayList<String>();
	public static List<String> scores = new ArrayList<String>();
	
	private static int teamNum = 0;
	private static int count = 0;
	private int itemIndex = 0;
	
	private TextView txtview;
	private static CustomList adapter;
	private static ListView listview;

	private SortingManager stm;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainlist);

		drawListScreen();
		
		listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            //Toast.makeText(getApplicationContext(), "pos: " + position + ", id: " + id, Toast.LENGTH_SHORT).show();
	            final int pos = position;
	            final Dialog dialog = new Dialog(Main.this);
				dialog.setContentView(R.layout.customdialog);
				dialog.setTitle("Enter Score of " + parent.getItemAtPosition(position));
				
				final EditText teamNo = (EditText) dialog.findViewById(R.id.entryname);
				
				Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButtonOK);
				Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButtonNO);
				
				// if 'OK' button is clicked, 		
				dialogButton1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {				
						String numberStr = teamNo.getText().toString();
							try {
							   int num = Integer.parseInt(numberStr);
							   if (num <= 0 || num > 300)
							   {
								   Toast.makeText(getApplicationContext(), "The number should be 1~300", Toast.LENGTH_SHORT).show();
							   }
							   else
							   {
								   Player player = SelectedPlayers.get(pos);
								   player.addScore(num);
								   //db.updatePlayer(player);
								   drawListScreen();
								   dialog.dismiss();
							   }
							   
							} catch (NumberFormatException e) {
								Toast.makeText(getApplicationContext(), "This should be a pure number", Toast.LENGTH_SHORT).show();
							}
						}
				});
				
				// if 'CANCEL' button is clicked, 	
				dialogButton2.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				
				dialog.show();
	        }
	    });
		registerForContextMenu(listview);
		
		Button btn = (Button) findViewById(R.id.btn_addplayer);
		btn.setOnClickListener(new Button.OnClickListener(){
		public void onClick(View v){
			Intent intent = new Intent(Main.this, Playerlist.class);
			startActivity(intent);
			}
		});
		
	}

	private void  drawListScreen ()
	{
		names.clear();
		scores.clear();
		SelectedPlayers.clear();
		
		players = db.getAllPlayers();
		
		count = 0;
		for (int i = 0; i < players.size(); i++)
		{
			if (players.get(i).getStatus() == 1)
			{
				names.add(players.get(i).getName());
				scores.add(players.get(i).getScoresStr());
				SelectedPlayers.add(players.get(i));
				count++;
			}
		}
		
		listview = (ListView) findViewById(R.id.list);
		adapter = new CustomList(Main.this, names, scores);
		listview.setAdapter(adapter);
		
		txtview = (TextView) findViewById(R.id.text_totalplayers);
		txtview.setText("Total Players: " + count + ", \t\t No. teams: " + teamNum);
	}
	


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
        switch (item.getItemId())
        {
        case R.id.menu_clearscores:
			AlertDialog.Builder ab = new AlertDialog.Builder(Main.this);
		 	ab.setMessage("Do you want to reset all the scores?");
		    ab.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					db.clearAllScores();
				}
			});
		    ab.setNegativeButton("NO", null);
		 	ab.show();
		 	       
            return true;

        case R.id.menu_teamdivider:
        	if (teamNum == 0)
        	{
        		final Dialog dialog = new Dialog(Main.this);
				dialog.setContentView(R.layout.customdialog);
				dialog.setTitle("Specify the number of team");
				
				final EditText teamNo = (EditText) dialog.findViewById(R.id.entryname);
				
				Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButtonOK);
				Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButtonNO);
				
				// if 'OK' button is clicked, 		
				dialogButton1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {				
						String numberStr = teamNo.getText().toString();
							try {
							   int num = Integer.parseInt(numberStr);
							   if (num <= 0)
							   {
								   Toast.makeText(getApplicationContext(), "The number should be positive and non-zero", Toast.LENGTH_SHORT).show();
							   }
							   else if (num > count) 
							   {
								   Toast.makeText(getApplicationContext(), "No.Team > No.Player", Toast.LENGTH_SHORT).show();
							   }
							   else
							   {
								   teamNum = num;
								   txtview.setText("Total Players: " + count + ", \t\t No. teams: " + teamNum);
								   dialog.dismiss();
							   }
							   
							} catch (NumberFormatException e) {
								Toast.makeText(getApplicationContext(), "This should be a pure number", Toast.LENGTH_SHORT).show();
							}
						}
				});
				
				// if 'CANCEL' button is clicked, 	
				dialogButton2.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				
				dialog.show();
        	}
        	return true;
        
        case R.id.FLPair:
			stm = new SortingManager(SelectedPlayers);
			toTeamView();
        	return true;
        	
        case R.id.LeadersPick:
			stm = new SortingManager(SelectedPlayers);
			toTeamView();
        	return true;
        	
        case R.id.BetaTest:
			stm = new SortingManager(SelectedPlayers);
			
			for (int a=0; a< stm.players.size(); a++)
			{
				Log.d("Main", stm.players.get(a).getName() + ": " + stm.playersAvg.get(a) + ","
						+ stm.playersMod.get(a) + ","
						+ stm.playersBest.get(a) + ","
						+ stm.playersWorst.get(a));
			}		
			toTeamView();
        	return true;
        	
        case R.id.menu_settings:
        	return true;
        	
        default:
            return super.onOptionsItemSelected(item);
        }
	}
	
	private void toTeamView ()
	{
		Intent intent = new Intent(Main.this, TeamView.class);
		//Intent intent = new Intent(Main.this, TeamFragView.class);
		//intent.putExtra("Pages", teamNum+1);		
		startActivity(intent);
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflator = getMenuInflater();
		inflator.inflate(R.menu.mainctxt, menu);
		
		ListView lv = (ListView) v;
		AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) menuInfo;
		itemIndex = (int) (long) lv.getItemIdAtPosition(acmi.position);
		Log.d("Main", "Context Menu is pressed: " + itemIndex);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 switch (item.getItemId())
	        {
	        case R.id.menu_edit:
	        	final Dialog dialog = new Dialog(Main.this);
				dialog.setContentView(R.layout.customdialog);
				dialog.setTitle("Edit Score Set");
				
				TextView text = (TextView) dialog.findViewById(R.id.dialogtext);
				text.setText("Please keep the format = number,number,number...");
				
				final Player player = SelectedPlayers.get(itemIndex);
				
				final SimpleMaths sm = new SimpleMaths();
								
				final EditText ScoreString = (EditText) dialog.findViewById(R.id.entryname);
				ScoreString.setText(sm.removeZero(player.getScoresStr()));
				
				
				Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButtonOK);
				Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButtonNO);
				
				// if 'OK' button is clicked, 		
				dialogButton1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {				
						String ScoreSet = ScoreString.getText().toString();
							if (sm.inspectCommaNum(ScoreSet))
							{
								if (ScoreSet.matches(""))
								{
									ScoreSet = "0";
								}
								player.setScore(ScoreSet);
								//db.updatePlayer(player);
								drawListScreen();
								dialog.dismiss();
							}
							else
							{
								Toast.makeText(getApplicationContext(), "Keep format and the number 1~300", Toast.LENGTH_SHORT).show();
							}
							
						}
				});
				
				// if 'CANCEL' button is clicked, 	
				dialogButton2.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				
				dialog.show();
	            return true;

	        default:
	        	return super.onContextItemSelected(item);
	        }
	}
}
