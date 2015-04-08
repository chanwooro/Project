package com.bowlingclub;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Playerlist extends Activity{

	private PlayerDbByFile db = new PlayerDbByFile();
	private List<Player> players;

	private ArrayAdapter<String> adapter;
	private int itemIndex = 0;
	private ListView listv;
	private ArrayList<String> names = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playerlist);

		players = Main.players;
		
		listv = (ListView) findViewById(R.id.managelist);
		
		for (int i = 0; i < players.size(); i++)
		{
			names.add(players.get(i).getName());
		}
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice,names);
		
		listv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listv.setAdapter(adapter);
        registerForContextMenu(listv);
        listv.setOnItemClickListener(itemClickListener);
        
        //Based on the status, check the checkboxes
    	for (int i = 0; i < players.size(); i++)
		{
			if (players.get(i).getStatus() == 1)
			{
				listv.setItemChecked(i, true);
			}
		}
	}
	
	
	private OnItemClickListener itemClickListener = new OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos, long id)
        {
        	CheckedTextView check = (CheckedTextView)clickedView;
        	check.setChecked(!check.isChecked());
        	boolean click = !check.isChecked();
        	check.setChecked(click);
        	
            if (click) {
            	players.get(pos).setStatus(1);
        		Log.d("PlayerList", "sel" + pos + ", " + id);
        	} else {
        		players.get(pos).setStatus(0);
        		Log.d("PlayerList", "unsel" + pos + ", " + id);
        	} 
        }
    };

	private void addPlayer (String name) {
		Player player = new Player(name);
		players.add(player);
		adapter.add(name);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onBackPressed() {
		db.updateAllPlayer(players);
		Intent toMain = new Intent(Playerlist.this, Main.class);
		startActivity(toMain);	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.playerlist, menu);
        return true;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 switch (item.getItemId())
	        {
	        case R.id.menu_edit:
	        	final Dialog dialog = new Dialog(Playerlist.this);
				dialog.setContentView(R.layout.customdialog);
				dialog.setTitle("Provide a New Name");
				
				final EditText EntryName = (EditText) dialog.findViewById(R.id.entryname);
				
				Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButtonOK);
				Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButtonNO);
				
				// if 'OK' button is clicked, 		
				dialogButton1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {				
						String name = EntryName.getText().toString();
							if (name.matches(""))
							{
								Toast.makeText(getApplicationContext(), "A name cannot be null", Toast.LENGTH_SHORT).show();
							}
							else
							{
								List<Player> players = db.getAllPlayers();
						    	Player player = new Player();
						    	player = players.get(itemIndex);
						    	//get previous player position and delete
						    	int pos = adapter.getPosition(player.getName());
						    	adapter.remove(player.getName());
						    	//change the player name to the new value and update the database
								player.setName(name);
								//db.updatePlayer(player);
								//finally insert the new name into the previous posision
								adapter.insert(player.getName(), pos);								
								adapter.notifyDataSetChanged();
								Log.d("PlayerList", "player is edited");
								dialog.dismiss();
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
	            
	        case R.id.menu_delete:
	        	Player player = new Player();
	        	player = players.get(itemIndex);
	        	
	        	SparseBooleanArray checkedItemPositions = listv.getCheckedItemPositions();
	        	adapter.remove(player.getName());
	        	db.deletePlayer(player);
	        	checkedItemPositions.clear();
	        	
	        	players = db.getAllPlayers();       	
	        	for (int i = 0; i < players.size(); i++)
	    		{
	    			if (players.get(i).getStatus() == 1)
	    			{
	    				listv.setItemChecked(i, true);
	    			}
	    		}
	        	
	        	adapter.notifyDataSetChanged();
	        	
	        	
	           	Log.d("PlayerList", "player is deleted");
	        	return true;
	      	
	        default:
	        	return super.onContextItemSelected(item);
	        }
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflator = getMenuInflater();
		inflator.inflate(R.menu.playerlistctxt, menu);
		
		ListView lv = (ListView) v;
		AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) menuInfo;
		itemIndex = (int) (long) lv.getItemIdAtPosition(acmi.position);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
        switch (item.getItemId())
        {
        case R.id.addnew:
        	final Dialog dialog = new Dialog(Playerlist.this);
			dialog.setContentView(R.layout.customdialog);
			dialog.setTitle("Adding A New Player");
			
			final EditText EntryName = (EditText) dialog.findViewById(R.id.entryname);
			
			Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButtonOK);
			Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButtonNO);
			
			// if 'OK' button is clicked, 		
			dialogButton1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {				
					String name = EntryName.getText().toString();
						if (name.matches(""))
						{
							Toast.makeText(getApplicationContext(), "A name cannot be null", Toast.LENGTH_SHORT).show();
						}
						else
						{
							addPlayer(name);
							Log.d("PlayerList", name + " player is added");
							dialog.dismiss();
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
            return super.onOptionsItemSelected(item);
        }
	}
}


