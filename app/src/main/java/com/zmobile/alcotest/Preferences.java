package com.zmobile.alcotest;

import android.content.SharedPreferences;

public class Preferences {
	
	static SharedPreferences sharedData;
	static String filename = "SharedData";
	static Preferences instance;
	
	public static Preferences getInstance(){
	      if(instance == null) {
	    	  instance = new Preferences();
	      }		    	  		      
	      return instance;
	}			
	
	void Preferences(){
		//sharedData = getSharedPreferences(filename, 0);
	}
	
	//public static 

}
