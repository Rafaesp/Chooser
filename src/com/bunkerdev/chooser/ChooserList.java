package com.bunkerdev.chooser;

import java.util.ArrayList;
import java.util.LinkedList;

import android.R.style;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bunkerdev.chooser.wheel.WheelView;
import com.bunkerdev.chooser.wheel.adapters.NumericWheelAdapter;

public class ChooserList extends Activity{
	
	private AlertDialog wheelDialog;
	private AlertDialog createOptionDialog;
	private LayoutInflater inflater;
	private ExpLAdapter expAdapter;
	private LinkedList<Choice> choices;
	private Integer numChoices;
	private WheelView wheel;
	
	private static String tag = "TAG";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_list_tab);
        
        choices = new LinkedList<Choice>();
        
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
				expAdapter.refreshWeighings();
				WeightedRandom randGenerator= new WeightedRandom(choices);
				
				ArrayList<Choice> chosen = randGenerator.getChoice(numChoices);
				//Log.i(tag, randGenerator.doStatistics(100));	
				
			}
		});
    }
    
    private void initializeWheel(){
        View wheelLayout = inflater.inflate(R.layout.wheel, null);
        AlertDialog.Builder builder = new Builder(this);
        
        wheel = (WheelView) wheelLayout.findViewById(R.id.wheel);
        
        final NumericWheelAdapter numChoicesAdapter = new NumericWheelAdapter(this);
        wheel.setViewAdapter(numChoicesAdapter);
        wheel.setCyclic(false);
        wheel.setVisibleItems(3);
        
        builder.setTitle(R.string.numChoicesDialogTitle);
        builder.setView(wheelLayout);
        builder.setPositiveButton(R.string.confirmButton, new DialogInterface.OnClickListener() {

		public void onClick(DialogInterface dialog, int which) {
				TextView numChoicesView = (TextView)findViewById(R.id.tvNumberChoices);
				Integer chosen = wheel.getCurrentItem();
				numChoices = chosen;
				numChoicesView.setText(chosen.toString());
				wheelDialog.dismiss();				
			}
		});
        
        TextView numChoicesView = (TextView)findViewById(R.id.tvNumberChoices);
		numChoices = 1;
		numChoicesView.setText(1+"");
        
		((NumericWheelAdapter)wheel.getViewAdapter()).setMaxValue(choices.size());
		
       numChoicesView.setOnClickListener(new OnClickListener() { 
			
			public void onClick(View v) {
				((NumericWheelAdapter)wheel.getViewAdapter()).setMaxValue(choices.size());
				wheelDialog.show();	
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
    	
    	builder.setPositiveButton(R.string.confirmButton, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				RadioGroup opType = (RadioGroup) createOptionDialog.findViewById(R.id.rgOptionType);
				
				int type = opType.getCheckedRadioButtonId();
				Choice c;
				
				switch (type) {
				case R.id.rbSimple:
					EditText opNameView = (EditText) createOptionDialog.findViewById(R.id.optionName);
					Editable nameEdit = opNameView.getText();
					nameEdit.clearSpans();
					c = new Choice(nameEdit.toString());
					choices.add(c);
					expAdapter.addChoiceRadioGroup(c);
					createOptionDialog.dismiss();
					opNameView.setText("");
					break;
				case R.id.rbRange:
					EditText etRangeIni = (EditText) createOptionDialog.findViewById(R.id.rangeIni);
					EditText etRangeEnd = (EditText) createOptionDialog.findViewById(R.id.rangeEnd);
					try {
						Integer rIni = new Integer(etRangeIni.getText().toString());
						Integer rEnd = new Integer(etRangeEnd.getText().toString());
						if(rIni>=rEnd){
							Toast t = Toast.makeText(getApplicationContext(), R.string.toastRangeError, 3000);
							t.show();
						}else{
							String name = getText(R.string.rangeName1)+" "+rIni+" "+getText(R.string.rangeName2)+" "+rEnd;
							c = new Choice(name, rIni, rEnd);
							choices.add(c);
							expAdapter.addChoiceRadioGroup(c);
							createOptionDialog.dismiss();
						}
					} catch (NumberFormatException e) {
						Toast t = Toast.makeText(getApplicationContext(), R.string.toastRangeError, 3000);
						t.show();
					}
					
					etRangeIni.setText("");
					etRangeEnd.setText("");
					
					break;
				default:
					break;
				}
				
			}
		});
    	View newOption = (View) inflater.inflate(R.layout.new_option, null);
    	
    	RadioButton rbSimple = (RadioButton) newOption.findViewById(R.id.rbSimple);
    	RadioButton rbRange = (RadioButton) newOption.findViewById(R.id.rbRange);
		EditText opNameView = (EditText) newOption.findViewById(R.id.optionName);
		RelativeLayout rangeLayout = (RelativeLayout) newOption.findViewById(R.id.rlRange);
    	
    	rbSimple.setTag(opNameView);
    	rbRange.setTag(rangeLayout);
    	
    	OnCheckedChangeListener typeChangeListener = new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Object obj = buttonView.getTag();
				
				if(obj instanceof EditText){
					if(isChecked)
						((EditText) obj).setVisibility(View.VISIBLE);
					else
						((EditText) obj).setVisibility(View.GONE);
				}else{
					if(isChecked)
						((RelativeLayout) obj).setVisibility(View.VISIBLE);
					else
						((RelativeLayout) obj).setVisibility(View.GONE);
				}
			}
		};
		
		rbSimple.setOnCheckedChangeListener(typeChangeListener);
		rbRange.setOnCheckedChangeListener(typeChangeListener);
		
    	builder.setView(newOption);
    	createOptionDialog = builder.create();
    }
    
}