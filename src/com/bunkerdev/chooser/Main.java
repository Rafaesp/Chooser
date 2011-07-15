package com.bunkerdev.chooser;

import java.util.ArrayList;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class Main extends TabActivity {
	
	private Intent intent;
	private TabHost tabHost;
	private ArrayList<Choice> aux;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tabhost);

	    aux = new ArrayList<Choice>();
	    
	    Resources res = getResources(); // Resource object to get Drawables
	    tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, ChooserList.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("list").setIndicator("List",
	                      res.getDrawable(R.drawable.list))
	                      .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, Favorites.class);
	    spec = tabHost.newTabSpec("favorites").setIndicator("Favorites",
	    				res.getDrawable(R.drawable.gold_star_little))
	    				.setContent(intent);
	    tabHost.addTab(spec);
	    
	    
	    tabHost.setCurrentTab(0);
	    
	}
	
	public void switchTab(int tab){
        tabHost.setCurrentTab(tab);
	}

	public ArrayList<Choice> getAux() {
		return aux;
	}

	public void setAux(ArrayList<Choice> aux) {
		this.aux = aux;
	}
}

