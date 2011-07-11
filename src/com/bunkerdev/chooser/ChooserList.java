package com.bunkerdev.chooser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bunkerdev.chooser.wheel.WheelView;
import com.bunkerdev.chooser.wheel.adapters.NumericWheelAdapter;

public class ChooserList extends Activity{
	
	private AlertDialog wheelDialog;
	private AlertDialog createOptionDialog;
	private LayoutInflater inflater;
	private ExpLAdapter expAdapter;
	
	private static String tag = "TAG";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_list_tab);
        
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        
        ExpandableListView rows = (ExpandableListView) this.findViewById(R.id.rows);
        expAdapter = new ExpLAdapter(inflater);
        rows.setAdapter(expAdapter);
        
        initializeWheel();
        initializeCreateOption();
        
        Button chooseBtn = (Button) findViewById(R.id.chooseButton);
        chooseBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//TODO
			}
		});
    }
    
    private void initializeWheel(){
    	 TextView numChoicesView = (TextView)findViewById(R.id.numberChoices);
         numChoicesView.setOnClickListener(new OnClickListener() { 
 			
 			public void onClick(View v) {
 				wheelDialog.show();	
 			}
 		});
    	
        View wheelLayout = inflater.inflate(R.layout.wheel, null);
        AlertDialog.Builder builder = new Builder(this);
        
        final WheelView numChoices = (WheelView) wheelLayout.findViewById(R.id.numChoices);
        final NumericWheelAdapter numChoicesAdapter = new NumericWheelAdapter(this);
        numChoices.setViewAdapter(numChoicesAdapter);
        numChoices.setCyclic(true);
        numChoices.setVisibleItems(3);
        
        builder.setTitle(R.string.numChoicesDialogTitle);
        builder.setView(wheelLayout);
        builder.setPositiveButton(R.string.confirmButton, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				TextView numChoicesView = (TextView)findViewById(R.id.numberChoices);
				Integer chosen = numChoices.getCurrentItem();
				numChoicesView.setText(chosen.toString());
				wheelDialog.dismiss();				
			}
		});
       
        wheelDialog = builder.create();

    }
    
    private void initializeCreateOption(){
    	TextView addOptionView = (TextView)findViewById(R.id.addOption);
    	ImageView addOptionIView = (ImageView)findViewById(R.id.addOptionI);
    	OnClickListener addOptionClickListener = new OnClickListener() {
			
			public void onClick(View v) {
				createOptionDialog.show();
			}
		};
		
    	addOptionView.setOnClickListener(addOptionClickListener);
    	addOptionIView.setOnClickListener(addOptionClickListener);
    	
    	AlertDialog.Builder builder = new Builder(this);
    	builder.setTitle(R.string.nameOptionDialogTitle);
    	builder.setPositiveButton(R.string.confirmButton, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				EditText opNameView = (EditText) createOptionDialog.findViewById(R.id.optionName);
				Editable name = opNameView.getText();
				name.clearSpans();
				//expAdapter.addChoiceSeekBar(name);
				expAdapter.addChoiceRadioGroup(name);
				createOptionDialog.dismiss();
				opNameView.setText("");
			}
		});
    	View optionName = (View) inflater.inflate(R.layout.new_option, null);
    	builder.setView(optionName);
    	createOptionDialog = builder.create();
    }
    
   


}