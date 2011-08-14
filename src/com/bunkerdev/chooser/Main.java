package com.bunkerdev.chooser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class Main extends TabActivity {
	
	private TabHost tabHost;
	private ArrayList<Choice> aux;
	public static GoogleAnalyticsTracker tracker;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tabhost);
	    
	    tracker = GoogleAnalyticsTracker.getInstance();
	    Properties gitSecrets = new Properties();
		try {
			gitSecrets.load(this.getClass().getClassLoader().getResourceAsStream("assets/GitSecrets"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    tracker.startNewSession(gitSecrets.getProperty("ANALYTICS"), this);
	    
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
	    
	    intent = new Intent().setClass(this, WheelPosition.class);
	    spec = tabHost.newTabSpec("wheel").setIndicator("Wheel",
	    				res.getDrawable(R.drawable.silver_star))
	    				.setContent(intent);
	    tabHost.addTab(spec);
	    
	    
	    tabHost.setCurrentTab(0);
	    
	}
	
	@Override
	  protected void onDestroy() {
	    super.onDestroy();
	    tracker.dispatch();
	    tracker.stopSession();
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
	
	public static void debug(String format, Object... args){
		String s = String.format(format, args);
		Log.i("TAG", s);
	}
}

