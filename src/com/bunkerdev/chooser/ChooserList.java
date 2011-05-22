package com.bunkerdev.chooser;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.bunkerdev.chooser.wheel.WheelView;
import com.bunkerdev.chooser.wheel.adapters.NumericWheelAdapter;

public class ChooserList extends Activity{
	
	ArrayList<SeekBar> bars;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        bars = new ArrayList<SeekBar>();
        
        setContentView(R.layout.listtab);
        
        final WheelView numChoices = (WheelView) findViewById(R.id.numChoices);
        final NumericWheelAdapter numChoicesAdapter = new NumericWheelAdapter(this);
        numChoices.setViewAdapter(numChoicesAdapter);
        numChoices.setCyclic(true);
        numChoices.setVisibleItems(3);

    }

}