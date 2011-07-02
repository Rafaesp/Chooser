package com.bunkerdev.chooser;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bunkerdev.chooser.wheel.WheelView;
import com.bunkerdev.chooser.wheel.adapters.NumericWheelAdapter;

public class ChooserList extends Activity{
	
	ArrayList<SeekBar> bars;
	Dialog dialog;
	
	private static String tag = "TAG";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        bars = new ArrayList<SeekBar>();
        
        setContentView(R.layout.listtab);
        
        initializeDialog();
        TextView numChoicesView = (TextView)findViewById(R.id.numberChoices);
        numChoicesView.setOnClickListener(new OnClickListener() { 
			
			public void onClick(View v) {
				dialog.show();				
			}
		});
        


    }
    
    private void initializeDialog(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        
        View wheelLayout = inflater.inflate(R.layout.wheel, null);
        
        final WheelView numChoices = (WheelView) wheelLayout.findViewById(R.id.numChoices);
        final NumericWheelAdapter numChoicesAdapter = new NumericWheelAdapter(this);
        numChoices.setViewAdapter(numChoicesAdapter);
        numChoices.setCyclic(true);
        numChoices.setVisibleItems(3);
        
        dialog = new Dialog(this);
        dialog.setTitle(R.string.numChoicesDialogTitle);
        dialog.setContentView(R.layout.wheel);
        dialog.setContentView(wheelLayout);

    }
    
    

}