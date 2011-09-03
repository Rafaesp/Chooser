package com.bunkerdev.chooser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Favorites extends ListActivity {
	
	private HashMap<String, ArrayList<Choice>> favorites;
	private ArrayAdapter<String> adapter;
	private AlertDialog editDialog;
	private AlertDialog selectionDialog;
	private String keyClicked;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		favorites = new HashMap<String, ArrayList<Choice>>();
		adapter = new ArrayAdapter<String>(this, R.layout.simple_textview);
		keyClicked = "";
		
		AlertDialog.Builder builder = new Builder(this);
		builder.setItems(R.array.favoritesSelectionDialog, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					if(editDialog == null)
						createEditNameDialog();
					editDialog.show();
					break;
				case 1:
					removeFavorite();
					break;
				default:
					break;
				}
			}
		});
		
		selectionDialog = builder.create();
		
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int arg2, long arg3) {
				keyClicked = ((TextView)v).getText().toString();
				selectionDialog.show();
				return false;
			}
		});
		getListView().setAdapter(adapter);
	}
	

	protected void removeFavorite() {
		favorites.remove(keyClicked);
		adapter.remove(keyClicked);
		adapter.notifyDataSetChanged();
		saveFavorites();
	}

	protected void createEditNameDialog() {
		View editNameView = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE))
								.inflate(R.layout.edit_name, null);
		AlertDialog.Builder builder = new Builder(this);
		builder.setView(editNameView);
		builder.setTitle(R.string.editNameTitle);
		builder.setNegativeButton(R.string.btnCancel, new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				editDialog.dismiss();
			}
		});
		builder.setPositiveButton(R.string.confirmButton, new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				String editedName = ((EditText)editDialog.findViewById(R.id.editName)).getText().toString();
				
				int contains = 1;
				String nameBackup = editedName;
				while(favorites.containsKey(editedName)){
					editedName = nameBackup + " ("+contains+")";
					contains++;
				}
				
				favorites.put(editedName, favorites.get(keyClicked));
				favorites.remove(keyClicked);
				adapter.remove(keyClicked);
				adapter.add(editedName);
				adapter.notifyDataSetChanged();
				saveFavorites();
			}
		});
		
		editDialog = builder.create();
	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter.clear();
		for(String s :readFavorites())
			adapter.add(s);
		adapter.notifyDataSetChanged();
		Main.tracker.trackPageView("/Favorites");
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<String> readFavorites(){
		try{
			//use buffering
			InputStream file = openFileInput(ChooserList.PATH);
			BufferedInputStream buffer = new BufferedInputStream( file );
			ObjectInput input = new ObjectInputStream ( buffer );
			try{
				favorites = (HashMap<String, ArrayList<Choice>>)input.readObject();
			}
			finally{
				input.close();
			}
		}
		catch(ClassNotFoundException ex){
		}
		catch(IOException ex){
		}
		
		ArrayList<String> keyList = new ArrayList<String>();
		keyList.addAll(favorites.keySet());
		return keyList;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ArrayList<Choice> list = favorites.get(((TextView)v).getText().toString());
		getSharedPreferences(ChooserList.PREFS, MODE_PRIVATE).edit()
		.putBoolean(ChooserList.FROM_FAVORITES, true).commit();
		((Main)this.getParent()).setAux(list);
		((Main)this.getParent()).switchTab(0);
		Main.tracker.trackEvent("Click", "Buttons", "readFavorite", favorites.size());
	}
	
	private void saveFavorites(){
		
		FileOutputStream fos;
		try {
			fos = openFileOutput(ChooserList.PATH, MODE_PRIVATE);
			BufferedOutputStream buffer = new BufferedOutputStream( fos );
			ObjectOutput output = new ObjectOutputStream( buffer );
			try{
				output.writeObject(favorites);
			}finally{
				output.close();
			}
		}catch(IOException ex){
		}
	}
	
	
	
	
}
