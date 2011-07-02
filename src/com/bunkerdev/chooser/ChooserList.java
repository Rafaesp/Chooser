package com.bunkerdev.chooser;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bunkerdev.chooser.wheel.WheelView;
import com.bunkerdev.chooser.wheel.adapters.NumericWheelAdapter;

public class ChooserList extends Activity{
	
	ArrayList<SeekBar> bars;
	Dialog wheelDialog;
	Dialog createOption;
	LayoutInflater inflater;
	
	private static String tag = "TAG";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        bars = new ArrayList<SeekBar>();
        
        setContentView(R.layout.listtab);
        
        initializeWheelDialog();
        TextView numChoicesView = (TextView)findViewById(R.id.numberChoices);
        numChoicesView.setOnClickListener(new OnClickListener() { 
			
			public void onClick(View v) {
				wheelDialog.show();				
			}
		});
        
        


    }
    
    private void initializeWheelDialog(){
        View wheelLayout = inflater.inflate(R.layout.wheel, null);
        
        final WheelView numChoices = (WheelView) wheelLayout.findViewById(R.id.numChoices);
        final NumericWheelAdapter numChoicesAdapter = new NumericWheelAdapter(this);
        numChoices.setViewAdapter(numChoicesAdapter);
        numChoices.setCyclic(true);
        numChoices.setVisibleItems(3);
        
        Button wheelButton = (Button) wheelLayout.findViewById(R.id.wheelButton);
        wheelButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				TextView numChoicesView = (TextView)findViewById(R.id.numberChoices);
				Integer chosen = numChoices.getCurrentItem();
				numChoicesView.setText(chosen.toString());
				wheelDialog.dismiss();
			}
		});
        
        wheelDialog = new Dialog(this);
        wheelDialog.setTitle(R.string.numChoicesDialogTitle);
        wheelDialog.setContentView(R.layout.wheel);
        wheelDialog.setContentView(wheelLayout);

    }
    
    private void initializecreateOptionDialog(){
    	
    
    }
    
    

}