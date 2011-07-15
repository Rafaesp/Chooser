package com.bunkerdev.chooser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

public class Favorites extends ListActivity {
	private HashMap<String, ArrayList<Choice>> favorites;
	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		favorites = new HashMap<String, ArrayList<Choice>>();
		adapter = new ArrayAdapter<String>(this, R.layout.simple_textview);
		getListView().setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter.clear();
		for(String s :readFavorites())
			adapter.add(s);
		adapter.notifyDataSetChanged();
	}
	
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
	}
	
	
}
