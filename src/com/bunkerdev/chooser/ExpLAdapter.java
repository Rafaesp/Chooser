package com.bunkerdev.chooser;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bunkerdev.chooser.Choice.Weighing;

public class ExpLAdapter extends BaseExpandableListAdapter {
	
	private ArrayList<LinearLayout> groups;
	private ArrayList<LinearLayout> children;
	private LayoutInflater inflater;
	private static String tag = "TAG";
	
	
	public ExpLAdapter(ArrayList<Choice> choices, LayoutInflater inf){
		groups = new ArrayList<LinearLayout>();
		children = new ArrayList<LinearLayout>();
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
	
	public void addChoiceRadioGroup(Choice c){
		LinearLayout adapterGroup = (LinearLayout) inflater.inflate(R.layout.adapter_group, null);
		LinearLayout adapterChild = (LinearLayout) inflater.inflate(R.layout.adapter_child_radiobuttons, null);
		
		adapterGroup.setTag(c);
		
		RadioGroup rg = (RadioGroup) adapterChild.findViewById(R.id.rgWeighing);
		
		switch (c.getWeight()) {
		case NEVER:
			((RadioButton)adapterChild.findViewById(R.id.rbnever)).setChecked(true);
			break;
		case ALMOSTIMPOSSIBLE:
			((RadioButton)adapterChild.findViewById(R.id.rbalmostimpossible)).setChecked(true);
			break;
		case NORMAL:
			((RadioButton)adapterChild.findViewById(R.id.rbnormal)).setChecked(true);
			break;
		case ABOVENORMAL:
			((RadioButton)adapterChild.findViewById(R.id.rbabovenormal)).setChecked(true);
			break;
		default:
			break;
		}
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

}