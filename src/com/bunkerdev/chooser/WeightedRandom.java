package com.bunkerdev.chooser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.util.Log;

import com.bunkerdev.chooser.Choice.Weighing;


public class WeightedRandom {
	
	String tag = "TAG";
    private HashMap<Choice, Double> choiceValues;
    private ArrayList<Choice> choices;
    public enum WeighingCase {ALLAI, ALLN, ALLAN, AIN, NAN, AIAN, AINAN, NOCASE};
    
    
    public WeightedRandom(ArrayList<Choice> list){
        choices =  (ArrayList<Choice>)list.clone();
        choiceValues = new HashMap<Choice, Double>();
    }
    
    private WeighingCase getWeighingCase(){
        boolean AI = false;
        boolean N = false;
        boolean AN = false;
        boolean AIN = false;
        boolean NAN = false;
        boolean AIAN = false;
        boolean AINAN = false;
        
        for(int i = 0; i<choices.size(); i++){
            Choice c = choices.get(i);
            
            switch(c.getWeight()){
                
                case ALMOSTIMPOSSIBLE:
                    AI = true;
                    if(N) AIN = true;
                    if(AN) AIAN = true;
                break;
                case NORMAL:
                    N = true;
                    if(AI) AIN = true;
                    if(AN) NAN = true;
                break;
                case ABOVENORMAL:
                    AN = true;
                    if(AI) AIAN = true;
                    if(N) NAN = true;
                break;
            
                default:
                //case NEVER
                choices.remove(i);
                break;
            }
            
            if(AI && N && AN){
             AINAN = true;
             break;
            }
            
        }
        
        if(AINAN)
           return WeighingCase.AINAN;
        else if(AIAN)
           return WeighingCase.AIAN;
        else if(NAN)
           return WeighingCase.NAN;
        else if(AIN)
           return WeighingCase.AIN;
        else if(AI)
        	return WeighingCase.ALLAI;
        else if(N)
        	return WeighingCase.ALLN;
        else if(AN)
        	return WeighingCase.ALLAN;
        else
           return WeighingCase.NOCASE;
    }
    
    private void updateWeighingValues(){
        HashMap<Weighing, Double> weighingValues = new HashMap<Weighing, Double>();
    	WeighingCase wc = getWeighingCase();
    	
        //TODO: get these values from SharedPreferences
    	Double ALLSAME = 1.0;
        Double AIN_AI = 0.05;
        Double AIN_N = 0.95;
        Double AIAN_AI = 0.01;
        Double AIAN_AN = 0.99;
        Double NAN_N = 0.4;
        Double NAN_AN = 0.6;
        Double AINAN_AI = 0.04;
        Double AINAN_N = 0.38;
        Double AINAN_AN = 0.58;

        switch(wc){
	        case ALLAI:
	    		weighingValues.put(Weighing.ALMOSTIMPOSSIBLE, ALLSAME);
	        break;
	        case ALLN:
	            weighingValues.put(Weighing.NORMAL, ALLSAME);
	        break;
	        case ALLAN:
	            weighingValues.put(Weighing.ABOVENORMAL, ALLSAME);
	        break;
	        case AIN:
                weighingValues.put(Weighing.ALMOSTIMPOSSIBLE, AIN_AI);
                weighingValues.put(Weighing.NORMAL, AIN_N);
            break;
            case  AIAN:
                weighingValues.put(Weighing.ALMOSTIMPOSSIBLE, AIAN_AI);
                weighingValues.put(Weighing.ABOVENORMAL, AIAN_AN);
            break;
            case  NAN:
                weighingValues.put(Weighing.NORMAL, NAN_N);
                weighingValues.put(Weighing.ABOVENORMAL, NAN_AN);
            break;
            case  AINAN:
                weighingValues.put(Weighing.ALMOSTIMPOSSIBLE, AINAN_AI);
                weighingValues.put(Weighing.NORMAL, AINAN_N);
                weighingValues.put(Weighing.ABOVENORMAL, AINAN_AN);
            break;
            case  NOCASE:
            	Log.i(tag, "NOCASE on switch of method updateWeighing");
            break;
            default:
            break;
        }
        
        weighingValues.put(Weighing.NEVER, 0.0);
        
        for(Choice c : choices)
        	choiceValues.put(c, weighingValues.get(c.getWeight()));
    }
    
    
    //http://w-shadow.com/blog/2008/12/10/fast-weighted-random-choice-in-php/
    public ArrayList<Choice> getChoice(Integer nChoices){
    	ArrayList<Choice> resList = new ArrayList<Choice>();

    	updateWeighingValues();
    	
    	while(resList.size() < nChoices){
	        Double counter = 0.0;
	        Double totalWeight = getTotalWeight();
	        Double rand = Math.random() * totalWeight;
	        
	        for(Choice c : choiceValues.keySet()){
	        	counter += choiceValues.get(c);
//	        	Log.i(tag, "counter:" +counter+" rand: "+rand+" total: "+totalWeight);
	        	if(counter >= rand){
	        		if(!resList.contains(c)){
	    	        	if(c.isRange())
	    	        		c.setChosen(getChosenFromRange(c));
	        			resList.add(c);
	        		}
	        		break;
	        	}
	        }
	        //TODO Check if choices can be repeated
	        
    	}
    
        return resList;
    }
    
    private Integer getChosenFromRange(Choice c){
    	int min = c.getRangeIni();
    	int max = c.getRangeEnd()-c.getRangeIni();
    	Double randDouble = min + Math.random()*max;
    	Integer rand = ((Double)Math.rint(randDouble)).intValue();
    	return rand;
    }

	private Double getTotalWeight() {
		Double total = 0.0;
		
		for(Double w : choiceValues.values()){
			total+=w;
		}
		
		//total = Math.round(total*100)/100.0;
		
		return total;
	}
	
	public String doStatistics(Integer times){
		HashMap<Choice, Integer> counters = new HashMap<Choice, Integer>();
		
		for(Choice c : choices)
			counters.put(c, 0);
		
		for(int i = 0; i<times; i++){
			Choice c = getChoice(1).get(0);
			counters.put(c, counters.get(c)+1);
		}
		
//		Log.i(tag, choiceValues.toString());
		
		return counters.toString();
		
	}
    
    
}