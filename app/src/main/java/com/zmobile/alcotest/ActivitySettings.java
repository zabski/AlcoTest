package com.zmobile.alcotest;

//import com.zabski.loancalc.R;

//import com.zabski.loancalc.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableRow;
import android.widget.TextView;
//import com.google.ads.*;
import com.facebook.ads.NativeAdView;
import com.google.android.gms.ads.*;
//import com.google.analytics.tracking.android.EasyTracker;
//import com.ironsource.mobilcore.MobileCore;
//import com.zmobile.foodtest.Favorites.FavoriteRequest;


public class ActivitySettings extends ActivityTemplate implements OnClickListener,
	OnSeekBarChangeListener, OnCheckedChangeListener, OnItemSelectedListener, Updatable {

	MenuHelper menuHelp;
	EditText editWzrost;
	EditText editWaga;
	TextView textBMI;
	//TextView tvMeters;
	TextView tvKilos;
	TextView tvAlc;
	//TextView tvFeet;
	TextView tvLbs;
	//TextView tvHeight;
	TextView tvWeight;
	//TextView tvBMR;	
	//TextView tvBMI;
	//TextView tvBMIdesc;
	Button btSave;
	Button btExit;
	ImageButton tbMetric;
	ImageButton btAlcMin;
	ImageButton btAlcPlus;
	ImageButton btHgtMin;
	ImageButton btHgtPlus;
	ImageButton btWgtMin;
	ImageButton btWgtPlus;
	TableRow tvLegend1;
	TableRow tvLegend2;
	TableRow tvLegend3;
	TableRow tvLegend4;	
	SeekBar seekAlc;
	//SeekBar seekHeight;
	//SeekBar seekWeight;
	SeekBar seekWeightLbs;
	RadioGroup rGroupSex;
	RadioGroup rGroupExc;
	RadioButton rbMale;
	RadioButton rbFemale;
	//Spinner spinExe;
	Spinner spinUnit;
	SharedPreferences sharedData;
	String filename = "Settings";
	
	//String consumerKey;
	//String consumerSecret;
	String[] mUnitList;
	
	PersonInfo nutrInfo;	
	
	//private InterstitialAd interstitial;
	//AdRequest adRequest;

	//HandlerPurchase handlerIAP;
	
	int minAlc = 0;
	int maxAlc = 10;
	int minHgt = 130;
	int maxHgt = 210;
	int minWgt = 30;	
	int maxWgt = 180;
	int minWgtLbs = 66;
	int maxWgtLbs = 500;
	int mAlc = 2;
	int mHeight = 160;
	int mFeet;
	int mInch;
	int mUnit; 
	//int mWeight = 60;
	double mWeightKg;
	final double LbsRatio = 0.45359237;
	int mLbs;
	String mSex;
	short isMale = 1;
	int mExercise = 0;
	double mBMR;
	double mBMI;
	String mAuthToken = "";

	int clBlue = Color.rgb(100, 100, 255);
	int clGreen = Color.rgb(0, 200, 0);
	int clOrange = Color.rgb(255, 200, 0);
	int clRed = Color.rgb(255, 32, 32);
	boolean metric = true;
	int ads;
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		//adView.pause();
		super.onPause();		
		ThemeUtils.saveTheme(sharedData);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();	
		//adView.resume();
	}	
	  
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//removeAdView();    
		//adView.destroy();
		super.onDestroy();		              
	}	
	
	@Override
	public void onStart() {
	    super.onStart();
	    // The rest of your onStart() code.
	    //EasyTracker.getInstance(this).activityStart(this);  // Add this method.
		/*
		if (ads<5 || handlerIAP.mHasRemovedAds)
			adView.setVisibility(View.GONE);
		else
			adView.loadAd(adRequest);
			*/
        //interstitial.loadAd(adRequest);
	}

	@Override
	public void onStop() {
		save();
	    super.onStop();
	    // The rest of your onStop() code.
	    //EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	}	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_settings);
		setUpAdLayouts();
		super.onCreate(savedInstanceState);
		classTAG = this.getClass().getSimpleName();
		mTracker = getDefaultTracker(this);
		ThemeUtils.onActivityCreateSetTheme(this);

		sharedData = getSharedPreferences(filename, 0);
		//TextView txView = (TextView) findViewById(R.id.sets_adView);

		statusText = (TextView) findViewById(R.id.status);
		viewType = NativeAdView.Type.HEIGHT_300;

		LinearLayout layout = (LinearLayout) findViewById(R.id.setsTopLay);
	    //layout.addView(adView);
	    //ads = getResources().getInteger(R.integer.ads);

		mUnitList = getResources().getStringArray(R.array.units);
		
		btSave = (Button) findViewById(R.id.btAdd);
		btExit = (Button) findViewById(R.id.btClose);
		btAlcMin = (ImageButton) findViewById(R.id.btAlcMin);
		btAlcPlus = (ImageButton) findViewById(R.id.btAlcPlus);
		//btHgtMin = (ImageButton) findViewById(R.id.btHgtMin);
		//btHgtPlus = (ImageButton) findViewById(R.id.btHgtPlus);
		btWgtMin = (ImageButton) findViewById(R.id.btWgtMin);
		btWgtPlus = (ImageButton) findViewById(R.id.btWgtPlus);
		//tbMetric = (Button) findViewById(R.id.tbMetric);
		tvAlc = (TextView) findViewById(R.id.tvAlc);
		//tvFeet = (TextView) findViewById(R.id.tvFeet);

		//tvMeters = (TextView) findViewById(R.id.tvCm);
		tvKilos = (TextView) findViewById(R.id.tvKgs);
		tvLbs = (TextView) findViewById(R.id.tvLbs);				
		
		// Get from the SharedPreferences
		SharedPreferences settings = sharedData;
				//getApplicationContext().getSharedPreferences("Settings", 0);
		
		mSex = settings.getString("sex", "Male");
		isMale = (short) settings.getInt("isMale", 1);
		mUnit = settings.getInt("unit", 0);	
		int BMR = settings.getInt("kcal", 2000);		
		
		//nutrInfo = new PersonInfo(this, mSex);
		nutrInfo = PersonInfo.getInstance(this);
		mWeightKg = nutrInfo.lbsToKg(mLbs);
		
        seekAlc = (SeekBar)findViewById(R.id.seekAlc); // make seekbar object
        seekAlc.setOnSeekBarChangeListener(this); // set seekbar listener.
        seekAlc.setMax(maxAlc-minAlc);
        //seekHeight = (SeekBar)findViewById(R.id.seekHeight); // make seekbar object
        //seekHeight.setMax(maxHgt-minHgt);
        //seekHeight.setOnSeekBarChangeListener(this); // set seekbar listener.
        //seekWeight = (SeekBar)findViewById(R.id.seekWeight); // make seekbar object
        //seekWeight.setOnSeekBarChangeListener(this); // set seekbar listener.
        //seekWeight.setMax(maxWgt-minWgt);
        seekWeightLbs = (SeekBar)findViewById(R.id.seekWeightLbs); // make seekbar object
        seekWeightLbs.setOnSeekBarChangeListener(this); // set seekbar listener.
        seekWeightLbs.setMax(maxWgtLbs-minWgtLbs);		
        
        spinUnit = (Spinner) findViewById(R.id.spinUnit);
		/*
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.units, android.R.layout.simple_spinner_dropdown_item);
        */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.units, R.layout.spinner);
        
        spinUnit.setAdapter(adapter);
        		//new SpinTypesAdapter(this, R.layout.spinner_type_item, mUnitList, image));				
		        	
        spinUnit.setSelection(mUnit);
        spinUnit.setOnItemSelectedListener(this);
		
		mAlc = settings.getInt("alc", 2);
		mHeight = settings.getInt("height", 160);
		//mWeight = settings.getInt("weight", 50);
		mLbs = settings.getInt("lbs", 100);
		
		mExercise = settings.getInt("exercise", 1);
		mAuthToken = settings.getString("token", ""); 
		
		// Read stored data		

		/*
		editWzrost = (EditText) findViewById(R.id.editWzrost);
		editWaga = (EditText) findViewById(R.id.editWaga);
		*/


		/*
		tvLegend1 = (TableRow) findViewById(R.id.tableRow5);
		tvLegend2 = (TableRow) findViewById(R.id.tableRow6);
		tvLegend3 = (TableRow) findViewById(R.id.tableRow7);
		tvLegend4 = (TableRow) findViewById(R.id.tableRow8);
		*/

		btSave.setOnClickListener(this);
		btExit.setOnClickListener(this);
		btAlcMin.setOnClickListener(this);
		btAlcPlus.setOnClickListener(this);
		//btHgtMin.setOnClickListener(this);
		//btHgtPlus.setOnClickListener(this);
		btWgtMin.setOnClickListener(this);
		btWgtPlus.setOnClickListener(this);
		rGroupSex = (RadioGroup) findViewById(R.id.radioGroupSex);
		rGroupSex.setOnCheckedChangeListener(this);
		rbMale = (RadioButton) findViewById(R.id.radioMale);
		rbFemale = (RadioButton) findViewById(R.id.radioFemale);
		//rGroupExc = (RadioGroup) findViewById(R.id.radioGroup2);
		//rGroupExc.setOnCheckedChangeListener(this);
		
		//spinExe = (Spinner) findViewById(R.id.spinExcercise);
		// Create an ArrayAdapter using the string array and a default spinner layout
		//ArrayAdapter<CharSequence> adapterExc = ArrayAdapter.createFromResource(this,
		//        R.array.exercise, android.R.layout.simple_spinner_item);
		ArrayAdapter<CharSequence> adapterExc = ArrayAdapter.createFromResource(this,
		        R.array.beer_amounts, android.R.layout.simple_spinner_item);
		        //.simple_selectable_list_item);
		// Specify the layout to use when the list of choices appears
		adapterExc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		
		//spinExe.setAdapter(adapterExc);
		
		//spinExe.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner, spinnerItems));
		
		//spinExe.setOnItemSelectedListener(this);
		
		/*tbMetric.setOnClickListener(this);
		tvLegend1.setBackgroundColor(clBlue);
		tvLegend2.setBackgroundColor(clGreen);
		tvLegend3.setBackgroundColor(clOrange);
		tvLegend4.setBackgroundColor(clRed);
		NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker1);
		
		np.setMinValue(140);
	    np.setMaxValue(200);
	    np.setWrapSelectorWheel(true);
	    //np.setDisplayedValues(nums);
	    np.setValue(160);
	    */		
		menuHelp = new MenuHelper(this);
		//menuHelp = MenuHelper.getInstance(this);
		displayValues();
		//CountPublish();
		//fbNativeAds.createAndLoadNativeAd(this, nativeAdContainer, R.layout.ad_unit);
	}


	// Display all input values
	void displayValues()
	{
		seekAlc.setProgress(mAlc-minAlc);
		//seekHeight.setProgress(mHeight-minHgt);
		//seekWeight.setProgress(mWeight-minWgt);
		seekWeightLbs.setProgress(mLbs-minWgtLbs);
		//spinExe.setSelection(mExercise);		
		if (mSex.equals("Male"))
			rGroupSex.check(R.id.radioMale);
		else
			rGroupSex.check(R.id.radioFemale);
	}
	
	
	/// Buttons click
	@Override
	public void onClick(View arg0) {		
		

/*
Men	BMR = 88.362 + (13.397 x weight in kg) + (4.799 x height in cm) - (5.677 x age in years)
Women	BMR = 447.593 + (9.247 x weight in kg) + (3.098 x height in cm) - (4.330 x age in years)

Little to no exercise	Daily kilocalories needed = BMR x 1.2
Light exercise (1-3 days per week)	Daily kilocalories needed = BMR x 1.375
Moderate exercise (3-5 days per week)	Daily kilocalories needed = BMR x 1.55
Heavy exercise (6-7 days per week)	Daily kilocalories needed = BMR x 1.725
Very heavy exercise (twice per day, extra heavy workouts)	Daily kilocalories needed = BMR x 1.9
*/
		int id = arg0.getId();
		
		switch(id){
		case (R.id.btAlcMin):
			if (mAlc>minAlc){ 
				mAlc--;
				seekAlc.setProgress(mAlc-minAlc);
			}			
			break;
		case (R.id.btAlcPlus):
			if (mAlc<maxAlc){ 
				mAlc++;
				seekAlc.setProgress(mAlc-minAlc);
			}	
			break;
		case (R.id.btWgtMin):
			/*
			if (mWeight>minWgt){ 
				mWeight--;
				seekWeight.setProgress(mWeight-minWgt);
			}*/
			// Lbs
			if (mLbs>minWgtLbs){ 
				mLbs--;
				seekWeightLbs.setProgress(mLbs-minWgtLbs);
			}	
			break;
		case (R.id.btWgtPlus):
			/*
			if (mWeight<maxWgt){ 
				mWeight++;
				seekWeight.setProgress(mWeight-minWgt);
			}*/
			// Lbs
			if (mLbs<maxWgtLbs){ 
				mLbs++;
				seekWeightLbs.setProgress(mLbs-minWgtLbs);
			}	
			break;		
		case (R.id.btAdd):
			save();
			finish();
			break;
		case (R.id.btClose):
			finish();
			break;				
		}
	}

	private void save(){
		//SharedPreferences settings = getApplicationContext().getSharedPreferences("Settings", 0);
		SharedPreferences settings = sharedData;
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("alc", mAlc);
		editor.putInt("height", mHeight);
		//editor.putInt("weight", mWeight);
		editor.putInt("lbs", mLbs);
		editor.putInt("isMale", isMale);
		editor.putString("sex", mSex);
		editor.putInt("exercise", mExercise);
		editor.putInt("kcal", (int)Math.round(mBMR));
		editor.putInt("unit", mUnit);
		// Commit the edits!
		editor.commit();
		double weightKg = nutrInfo.lbsToKg(mLbs);
		try{
			boolean sexMale = (isMale==1);
			Favorites.getInstance(this,sexMale,mLbs,(double)mAlc/100);
			//favorites.updateWeight(weightKg, mHeight, weightKg-5, false);
		}catch(Exception e){
			String no_internet = "Turn on internet connection";
			Toast.makeText(ActivitySettings.this, no_internet, Toast.LENGTH_SHORT).show();
		}
		//favorites.updateWeight(mLbs, mHeight, mLbs-10, true);
	}

	@Override
	public void onBackPressed(){
		//save();
		super.onBackPressed();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		
		int id = seekBar.getId();
		switch (id) {
		case (R.id.seekAlc):
			mAlc = progress+minAlc;
			break;
			/*
		case (R.id.seekWeight):
			mWeight = progress+minWgt;
			mLbs = (int)Math.round(mWeight/LbsRatio);		    
			break;*/		
		case (R.id.seekWeightLbs):
			mLbs = progress+minWgtLbs;			
			//mWeight = (int)Math.round(mLbs*LbsRatio);
			mWeightKg = (double)Math.round(mLbs*LbsRatio*2)/2;
			break;
		}
		/*
		mAge = seekAge.getProgress()+minAge;
		mHeight = seekHeight.getProgress()+minHgt;
		mWeight = seekWeight.getProgress()+minWgt;
		*/
		
		int feet = (int) (mHeight/(12*2.54));
		int inches = (int)(Math.round(mHeight%(feet*12*2.54))/2.54);
		int lbs = (int)Math.round(mWeightKg/0.45359237); 
		String promile = getResources().getString(R.string.promile);
		double alcPercent = (double)mAlc/100;
		double alcPromile = (double)mAlc/10;
		tvAlc.setText(String.format("%.1f",alcPromile)+" ‰  ("+promile+")");		
		//tvMeters.setText(mHeight+" cm = "+feet+" feet "+inches+" inches");
		//tvFeet.setText(feet+" feet "+inches+" inches");		
		//tvKilos.setText(mWeight+" kg = "+lbs+" lbs");
		tvKilos.setText(String.format("%.1f", mWeightKg)+" kg = "+mLbs+" lbs");
		//tvLbs.setText(lbs+" lbs");
		//CountPublish();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheckedChanged(RadioGroup rGroup, int id) {
		// TODO Auto-generated method stub
		Drawable btn = getResources().getDrawable(R.drawable.radio_btn);
		
		rGroup.getChildAt(0).setBackgroundColor(Color.TRANSPARENT);		
		rGroup.getChildAt(1).setBackgroundColor(Color.TRANSPARENT);
		
		switch(id){
		case R.id.radioMale:
			mSex = "Male";
			isMale = 1;
			break;
		case R.id.radioFemale:
			mSex = "Female";
			isMale = 0;			
			break;
		default:
			break;
		}
		
		/*
		rGroup.getChildAt(0).setBackgroundDrawable(btn);
		rGroup.getChildAt(1).setBackgroundDrawable(btn);
		*/
		RadioButton rb = (RadioButton) findViewById(id);
		/*
		if (rGroup == rGroupExc){
			mExercise = rGroup.indexOfChild(rb);
			rGroup.getChildAt(2).setBackgroundColor(Color.WHITE);
			rGroup.getChildAt(3).setBackgroundColor(Color.WHITE);
		}*/
		if (rGroupSex == rGroup){						
			//mSex = (String) rb.getText();
		}
			//Drawable gold = getResources().getDrawable(R.layout.gold_gradient);
			//rb.setBackground(btn);
			rb.setBackgroundDrawable(btn);
		//}
		//CountPublish();
	}
	
	// Spinner select
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		
		mUnit = pos;
		// TODO Auto-generated method stub
		/*
		switch (view.getId()) {
		case R.id.ddownKapital:*/
			switch(pos){
			case 0:
				mExercise = 0;
				//compoundsPerYear = 365;
				
				break;
			case 1:
				mExercise = 1;
				//compoundsPerYear = 12;
				break;
			case 2:
				mExercise = 2;
				//compoundsPerYear = 1;
				break;				
			case 3:
				mExercise = 3;
				//compoundsPerYear = 1;
				break;								
			case 4:
				mExercise = 4;
				//compoundsPerYear = 1;
				break;					
			}			
			//CountPublish();
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		mExercise = 0;
		//CountPublish();
	}
	
	int GetColor(double bmi) {
		float[] hsv = new float[3];
		int color = Color.GRAY;		
		Color.colorToHSV(color, hsv);
		//hsv[parameter] = hsv[parameter]/factor;
		//hsv[0] = (float)(-bmi/10 + 7.5)/92*360+360;
		//hsv[0] = (float)(300/((bmi-0.5)/7)-23);
		hsv[0] = (float)(360-bmi*12);
		hsv[1] = 1;
		hsv[2] = 0.75f;
		int newCol = Color.HSVToColor(hsv);
		return newCol;
	}
	
	int GetColorCal(double cal) {
		float[] hsv = new float[3];
		int color = Color.GRAY;		
		Color.colorToHSV(color, hsv);
		//hsv[parameter] = hsv[parameter]/factor;
		//hsv[0] = (float)(-bmi/10 + 7.5)/92*360+360;
		//hsv[0] = (float)(300/((bmi-0.5)/7)-23);
		hsv[0] = (float)(360-cal*12);
		hsv[1] = 1;
		hsv[2] = 0.75f;
		int newCol = Color.HSVToColor(hsv);
		return newCol;
	}
	
	int ModifyColor(int color, int parameter, float factor) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[parameter] = hsv[parameter]/factor;
		int newCol = Color.HSVToColor(hsv);
		return newCol;
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		menuHelp.handleOnItemSelected(this, item);
		return true;

    } 	
	
  	public void Update() {
		// TODO Auto-generated method stub
		int themeId = menuHelp.dialogSymbol.getThemeId();
		int selectionId = menuHelp.dialogSymbol.getSelectionId();
		//getApplication().setTheme(themeId);
		ThemeUtils.changeToTheme(this, selectionId);

	}
/*
	public void setAdVisible(boolean show){
		if (show)
			adView.setVisibility(View.VISIBLE);
		else
			adView.setVisibility(View.GONE);
	}
*/
}