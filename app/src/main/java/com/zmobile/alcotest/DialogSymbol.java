package com.zmobile.alcotest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

public class DialogSymbol extends AlertDialog implements OnCheckedChangeListener {
	
	AlertDialog.Builder builder;
	static String mSymbol;
	int themeId;
	int positionSelected;
	Spinner ddSymbol;
	RadioGroup radioTheme;
	AlertDialog dialog;
	Updatable act;
	//static DialogSymbol onlyCopy;
	/*
	public static DialogSymbol getInstance(Context context){
		if (onlyCopy == null){
			onlyCopy = new DialogSymbol(context);
			return onlyCopy;
		}else
			return onlyCopy;
	}
	 */
	
	private static DialogSymbol instance = null;
	/*
	public static DialogSymbol getInstance(Context ctx){
	      if(instance == null) {
	    	  instance = new DialogSymbol(ctx);
	      }		    	  		      
	      return instance;
	}
	*/
	public DialogSymbol(Context context) {
		super(context);
		//this.act = act;
		// TODO Auto-generated constructor stub
		builder = new AlertDialog.Builder(context);
		// Add the buttons
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User clicked OK button
		        	   //dialog.dismiss();
		        	   //mSymbol = ddSymbol.getSelectedItem().toString();		
		        	   
		        	   //positionSelected = ddSymbol.getSelectedItemPosition();
		        	   switch (positionSelected){
		        	   case 0:
		        		   themeId = R.style.ThemeLight; break;
		        	   case 1:
		        		   themeId = R.style.ThemeDark; break;
		        	   default:		        		   
		        	   case 2:
		        		   themeId = R.style.ThemeWine; break;
		        	   		        		   
		        	   }
		        	   //getApplication().setTheme(themeId);
		        	     
		        	   act.Update();
		        	   //Activity act = getActivity();  
		           }
		       });
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User cancelled the dialog
		        	   //dialog.cancel();
		        	   //dialog.dismiss();
		           }
		       });
		
		// Set other dialog properties	
		builder.setTitle(R.string.choose_theme);
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.dialog_themes, null);
		builder.setView(v);
		//ddSymbol = (Spinner) v.findViewById(R.id.ddCurrSymbol);
		radioTheme = (RadioGroup) v.findViewById(R.id.radioTheme);
		radioTheme.setOnCheckedChangeListener(this);
		
		// spinner symbol
		//ddSymbol = (Spinner) findViewById(R.id.ddSymbol);
		// Create an ArrayAdapter using the string array and a default spinner layout
		/*
		ArrayAdapter<CharSequence> adapterSymbol = ArrayAdapter.createFromResource(context,
		        R.array.themes, R.layout.spinner);
		// Specify the layout to use when the list of choices appears
		adapterSymbol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		ddSymbol.setAdapter(adapterSymbol);		
		//ddSymbol.setOnItemSelectedListener(symbolListner);
		ddSymbol.setSelection(1);	
		*/
		// Create the AlertDialog
		dialog = builder.create();
	}
	
	public AlertDialog getDialog(){
		return dialog;
	}
	
	public String getSymbol(){
		return mSymbol;
	}
	
	public int getThemeId(){
		return themeId;
	}

	public int getSelectionId(){
		return positionSelected;
	}	
	
	public void setAct(Updatable act){
		this.act = act;
		dialog.show();
	}

	public void onCheckedChanged(RadioGroup arg0, int id) {
		// TODO Auto-generated method stub
		RadioButton rb = (RadioButton) findViewById(id);
		
		switch(id){
		case R.id.radioButton1:
			positionSelected = 0;
			break;
		case R.id.radioButton2:
			positionSelected = 1;
			break;
		case R.id.radioButton3:
			positionSelected = 2;
			break;			
		}
	}

}
