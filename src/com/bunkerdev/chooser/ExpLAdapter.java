package com.bunkerdev.chooser;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExpLAdapter extends BaseExpandableListAdapter {
	
	ArrayList<LinearLayout> groups;
	ArrayList<LinearLayout> children;
	LayoutInflater inflater;
	
	public ExpLAdapter(LayoutInflater inf){
		groups = new ArrayList<LinearLayout>();
		children = new ArrayList<LinearLayout>();
		inflater = inf;
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
	
	public void addChoice(CharSequence name){
		LinearLayout adapterGroup = (LinearLayout) inflater.inflate(R.layout.adaptergroup, null);
		LinearLayout adapterChild = (LinearLayout) inflater.inflate(R.layout.adapterchild, null);
		
		TextView choiceNameView = (TextView) adapterGroup.findViewById(R.id.choice); 
		choiceNameView.setText(name);
		
		groups.add(adapterGroup);
		children.add(adapterChild);
		notifyDataSetChanged();
	}
	
	

}