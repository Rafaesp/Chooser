package com.bunkerdev.chooser;

import java.util.ArrayList;
import java.util.LinkedList;

import com.bunkerdev.chooser.Choice.Weighing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ExpLAdapter extends BaseExpandableListAdapter {
	
	private ArrayList<SeekBar> bars;
	private ArrayList<LinearLayout> groups;
	private ArrayList<LinearLayout> children;
	private LayoutInflater inflater;
	private static String tag = "TAG";
	
	
	public ExpLAdapter(ArrayList<Choice> choices, LayoutInflater inf){
		groups = new ArrayList<LinearLayout>();
		children = new ArrayList<LinearLayout>();
		bars = new ArrayList<SeekBar>();
		inflater = inf;
		refreshChoices(choices);
	}
	
	public Object getChild(int groupPosition, int childPosition) {
		return children.get(groupPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		return children.get(groupPosition);
	}

	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	public int getGroupCount() {
		return groups.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		return groups.get(groupPosition);
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	public void addChoiceSeekBar(String name){
		LinearLayout adapterGroup = (LinearLayout) inflater.inflate(R.layout.adapter_group, null);
		LinearLayout adapterChild = (LinearLayout) inflater.inflate(R.layout.adapter_child_seekbar, null);
		
		TextView choiceNameView = (TextView) adapterGroup.findViewById(R.id.choice); 
		choiceNameView.setText(name);
		
		TextView weight = (TextView) adapterChild.findViewById(R.id.choiceWeight);
		weight.setText(2+"");
		
		SeekBar sb = (SeekBar) adapterChild.findViewById(R.id.seekbar);
		sb.setMax(3);
		sb.setProgress(2);
		bars.add(sb);
		
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			public void onStopTrackingTouch(SeekBar seekBar) {
				//refreshProgress(seekBar);
				notifyDataSetChanged();
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				TextView weight = (TextView) ((View)seekBar.getParent()).findViewById(R.id.choiceWeight);
				weight.setText(seekBar.getProgress()+"");
			}
		});
		
		groups.add(adapterGroup);
		children.add(adapterChild);
		notifyDataSetChanged();
	}
	
	public void addChoiceRadioGroup(Choice c){
		LinearLayout adapterGroup = (LinearLayout) inflater.inflate(R.layout.adapter_group, null);
		LinearLayout adapterChild = (LinearLayout) inflater.inflate(R.layout.adapter_child_radiobuttons, null);
		
		adapterGroup.setTag(c);
		
		RadioGroup rg = (RadioGroup) adapterChild.findViewById(R.id.rgWeighing);
		adapterChild.setTag(rg);
		
		TextView choiceNameView = (TextView) adapterGroup.findViewById(R.id.choice); 
		choiceNameView.setText(c.getName());
		
		groups.add(adapterGroup);
		children.add(adapterChild);
		notifyDataSetChanged();
		
	}
	
    public void refreshWeighings(){
    	for(int i=0; i<groups.size();i++){
    		Choice c = (Choice) groups.get(i).getTag();
    		RadioGroup rgWeighing = (RadioGroup) children.get(i).getTag();
    		Weighing weight;
    		switch (rgWeighing.getCheckedRadioButtonId()) {
    		case R.id.rbnever:
				weight = Weighing.NEVER;
				break;
    		case R.id.rbalmostimpossible:
    			weight = Weighing.ALMOSTIMPOSSIBLE;
				break;
    		case R.id.rbnormal:
    			weight = Weighing.NORMAL;
				break;
    		case R.id.rbabovenormal:
    			weight = Weighing.ABOVENORMAL;
				break;
			default:
				weight = Weighing.NORMAL;
				break;
			}
    		
    		c.setWeight(weight);
    		
    	}
    }
    
    public void refreshChoices(ArrayList<Choice> list){
    	groups.clear();
    	children.clear();
		for(Choice c : list){
			addChoiceRadioGroup(c);
		}
    }

	
	
	/*
	   private void refreshProgress(SeekBar bar){
		Integer nProgress = bar.getProgress();
		Integer oldProgress = (Integer) bar.getTag();
		
		boolean increased = nProgress>=oldProgress? true:false;
		Integer progress = Math.abs(nProgress - oldProgress);
		
		Integer change = 0;
		
		Integer progressPerBar = ((Double)Math.floor(progress/(bars.size()-1))).intValue();
		Integer rest = progress%(bars.size()-1);
		Log.i(tag, "nProg: "+nProgress+" oldProg: "+oldProgress+" incres: "+increased+" progPerBar: "+progressPerBar+" rest: "+rest);
		if(increased){
			for(int i = 0; i<bars.size(); i++){
				SeekBar ibar = bars.get(i);
				if(!bar.equals(ibar)){
					if(i<rest){
						change = ibar.getProgress()-progressPerBar-1;
						ibar.setProgress(change);
					}else{
						change = ibar.getProgress()-progressPerBar;
						ibar.setProgress(change);
					}
					
					ibar.setTag(change);
				}
			}
		}else{
			for(int i = 0; i<bars.size(); i++){
				SeekBar ibar = bars.get(i);
				if(!bar.equals(ibar)){
					if(i<rest){
						change = ibar.getProgress()+progressPerBar+1;
						ibar.setProgress(change);
					}else{
						change = ibar.getProgress()+progressPerBar;
						ibar.setProgress(change);
					}
					ibar.setTag(change);
				}
			}	
		}
	}
	*/
}