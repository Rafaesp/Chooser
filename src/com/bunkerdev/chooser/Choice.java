package com.bunkerdev.chooser;

import java.io.Serializable;

public class Choice implements Serializable, Cloneable {

	private static final long serialVersionUID = -2045169531698578334L;
	public enum Weighing {NEVER, ALMOSTIMPOSSIBLE, NORMAL, ABOVENORMAL};
	
	private Weighing weight;
	private String name;
	private Integer rangeIni, rangeEnd;
	private boolean range;
	
	public Choice(String name) {
		this.name = name;
		range = false;
	}
	
	public Choice(String name, Integer ini, Integer end) {
		this.name = name;
		rangeIni = ini;
		rangeEnd = end;
		range = true;
	}

	public Weighing getWeight() {
		return weight;
	}

	public void setWeight(Weighing weight) {
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRange() {
		return range;
	}

	public void setRange(boolean range) {
		this.range = range;
	}

	public Integer getRangeIni() {
		return rangeIni;
	}

	public void setRangeIni(Integer rangeIni) {
		this.rangeIni = rangeIni;
	}

	public Integer getRangeEnd() {
		return rangeEnd;
	}

	public void setRangeEnd(Integer rangeEnd) {
		this.rangeEnd = rangeEnd;
	}

	@Override
	public String toString() {
		if(!range)
			return name;
		else
			return rangeIni+" - "+rangeEnd; 
	}

	@Override
	protected Object clone() {
		Choice c = null;
		try{
			c = (Choice)super.clone();
			
		}catch(CloneNotSupportedException e){
			e.printStackTrace();
		}
		return c;
	}
	
}
