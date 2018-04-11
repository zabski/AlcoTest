package com.zmobile.alcotest;

//import com.zabski.loancalc.R;

//import com.zabski.loancalc.R;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
//import com.google.ads.*;
import com.facebook.ads.NativeAdView;
import com.google.android.gms.ads.*;
import com.google.android.gms.analytics.HitBuilders;
//import com.google.analytics.tracking.android.EasyTracker;
//import com.ironsource.mobilcore.MobileCore;
//import com.zmobile.foodtest.Favorites.FavoriteRequest;

public class ActivityAddDrink extends ActivityTemplate implements OnClickListener, OnSeekBarChangeListener,
		OnDateChangedListener,	TimePicker.OnTimeChangedListener, OnTimeSetListener, Updatable {

	static final String TAG = ActivityAddDrink.class.getSimpleName();
	MenuHelper menuHelp;		
	
	Integer[] image = { R.drawable.beer, R.drawable.wine, R.drawable.liquor, R.drawable.cocktail1 };
	String[] mTypesList;
	String[] mAmountList;
	
	SharedPreferences sharedData;
	String filename = "SharedData";

	Calendar mToday;
	TimePicker timePicker;
	TextView tvDrinkName;
	Button bEditName;
	AlertDialog.Builder builder;
	TimePickerDialog.Builder tpBuilder;
	AlertDialog dialog;
	TimePickerDialog tpDialog;
	EditText etLabel;
	TextView tvTime;
	NumberPicker numberPicker1;
	/*
	DatePicker mDatePicker;	
	EditText editWzrost;
	EditText editWaga;
	TextView textBMI;
	TextView tvMeters;
	TextView tvKilos;	
	TextView tvFeet;
	TextView tvLbs;
	TextView tvHeight;
	TextView tvWeight;
	TextView tvBMR;	
	TextView tvBMI;
	TextView tvBMIdesc;
	*/
	TextView tvPercent;	
	TextView tvPureAlc;
	Button btAddDrink;
	Button btAddFavor;
	Button btExit;
	Button bSetTime;
	Button bSetDefName;
	Button bSetDefPerc;
	//ImageButton tbMetric;
	ImageButton btPercMin;
	ImageButton btPercPlus;
	/*
	ImageButton btHgtMin;
	ImageButton btHgtPlus;
	ImageButton btWgtMin;
	ImageButton btWgtPlus;
	TableRow tvLegend1;
	TableRow tvLegend2;
	TableRow tvLegend3;
	TableRow tvLegend4;	
	*/
	SeekBar seekPerc;
	/*
	SeekBar seekHeight;	
	SeekBar seekWeight;
	SeekBar seekWeightLbs;
	RadioGroup rGroupSex;
	RadioGroup rGroupExc;
	*/
	Spinner spinDrinkType;
	Spinner spinDrinkAmt;
	
	String consumerKey;				
	String consumerSecret;
	String mLabel;
	
	PersonInfo nutrInfo;
	Favorites favorites;
	
	boolean nameEdited = false;
	
	double beerPerc = 5;
	double winePerc = 11;
	double vodkaPerc = 40;
	int mAmountMl = 250;
	int mTypePos = 0;
	int mAmountPos = 0;
	int mSelectPos = 0;
	//int mPercentage = 40;
	int mYear = 2014;
	int minAge = 10;
	int maxAge = 60;
	int minHgt = 130;
	int maxHgt = 210;
	int minWgt = 30;	
	int maxWgt = 180;
	int minWgtLbs = 66;
	int maxWgtLbs = 500;
	int mAge = 30;
	int mHeight = 160;
	int mFeet;
	int mInch;
	int mUnit;
	//int mWeight = 60;
	double mWeightKg;
	final double LbsRatio = 0.45359237;
	int mLbs;
	String mSex = "Female";
	int mExercise = 0;
	double mBMR;
	double mBMI;
	String mAuthToken = "";

	int clBlue = Color.rgb(100, 100, 255);
	int clGreen = Color.rgb(0, 200, 0);
	int clOrange = Color.rgb(255, 200, 0);
	int clRed = Color.rgb(255, 32, 32);
	boolean metric = true;
	private int mHour;
	private int mMin;
	//private int mAmount;
	private double mPercent;
	private double mPureAlc;
	private double mPercMin = 3;
	String[] mBeerMl = new String[5];
	String[] mBeerUs = new String[5];
	String[] mBeerUk = new String[5];
	//int ads;
	//HandlerPurchase handlerIAP;
	
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
		//mTracker.setScreenName(classTAG);
		//mTracker.send(new HitBuilders.ScreenViewBuilder().build());
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
		int progress = getSeekProgress(mPercent);
		//progress = (int)Math.round((mPercent-mPercMin)*2);
		seekPerc.setProgress(progress);
	
		tvPercent.setText(String.valueOf(mPercent)+"%");
		
		tvDrinkName.setText(mLabel);	

        //interstitial.loadAd(adRequest);
	}

	@Override
	public void onStop() {
	    super.onStop();
	    // The rest of your onStop() code.
	    //EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	}	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_add_drink);
		setUpAdLayouts();
		super.onCreate(savedInstanceState);
		classTAG = this.getClass().getSimpleName();
		mTracker = getDefaultTracker(this);
		ThemeUtils.onActivityCreateSetTheme(this);

		sharedData = getSharedPreferences(filename, 0);
		//facebook native
		//nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container);
		//adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);
		//adViewExpress.setAdSize(AdSize.FLUID);
		statusText = (TextView) findViewById(R.id.status);
		viewType = NativeAdView.Type.HEIGHT_120;

		faceNativeLayoutId = R.layout.ad_unit_no_img;

		/*
	    LinearLayout layout = (LinearLayout) findViewById(R.id.addTopLay);
	    layout.addView(adView);
	    LinearLayout layout2 = (LinearLayout) findViewById(R.id.scroLay);
	    layout2.addView(adView2);
	    adView.loadAd(adRequest);
	    adView2.loadAd(adRequest);
	    new_adView.loadAd(adRequest);
	    new_adView2.loadAd(adRequest);*/
		mTypesList = getResources().getStringArray(R.array.drink_types);
		mBeerMl = getResources().getStringArray(R.array.beerml);
		mBeerUs = getResources().getStringArray(R.array.beermlus);
		mBeerUk = getResources().getStringArray(R.array.beermluk);
		//setContentView(R.layout.activity_settings);
		//MobileCore.getSlider().setContentViewWithSlider(this, R.layout.activity_settings, R.raw.slider_1);		
		//mDatePicker = (DatePicker)findViewById(R.id.datePicker1);
		btAddFavor = (Button) findViewById(R.id.btAddFavorite);
		btAddDrink = (Button) findViewById(R.id.btAdd);
		btExit = (Button) findViewById(R.id.btExit);
		btPercMin = (ImageButton) findViewById(R.id.btPercMin);
		btPercPlus = (ImageButton) findViewById(R.id.btPercPlus);
		tvDrinkName = (TextView) findViewById(R.id.tvDrinkName);
		bEditName = (Button) findViewById(R.id.bEditName);
		bSetTime = (Button) findViewById(R.id.bSetTime);
		bSetDefName = (Button) findViewById(R.id.bSetDefName);
		bSetDefPerc = (Button) findViewById(R.id.bSetDefPerc);
		numberPicker1 = (NumberPicker) findViewById(R.id.numberPicker1);
		/*
		btHgtMin = (ImageButton) findViewById(R.id.btHgtMin);
		btHgtPlus = (ImageButton) findViewById(R.id.btHgtPlus);
		btWgtMin = (ImageButton) findViewById(R.id.btWgtMin);
		btWgtPlus = (ImageButton) findViewById(R.id.btWgtPlus);
		*/
		//tbMetric = (Button) findViewById(R.id.tbMetric);
		tvPercent = (TextView) findViewById(R.id.tvPercent);
		tvPureAlc = (TextView) findViewById(R.id.tvPureAlc);
		//timePicker = (TimePicker) findViewById(R.id.timePicker);
		tvTime = (TextView) findViewById(R.id.tvTime);

		//int progress = (int)Math.round(mPercent*2-mPercMin); 
				//configSpinDrinkAmount(type);
		// Get from the SharedPreferences
		SharedPreferences settings = getApplicationContext().getSharedPreferences("Settings", 0);
		
		mSex = settings.getString("sex", "Female");
		int BMR = settings.getInt("kcal", 2000);
		mUnit = settings.getInt("unit", 0);
		
		//nutrInfo = new PersonInfo(this, mSex);
		nutrInfo = PersonInfo.getInstance(this);
        seekPerc = (SeekBar)findViewById(R.id.seekPercent); // make seekbar object
        seekPerc.setOnSeekBarChangeListener(this); // set seekbar listener.
        //seekPerc.setOnTouchListener(this);
        
		spinDrinkType = (Spinner) findViewById(R.id.spinDrinkType);
		spinDrinkAmt = (Spinner) findViewById(R.id.spinDrinkAmt);
		
		spinDrinkType.setAdapter(new SpinTypesAdapter(this, R.layout.spinner_type_item, mTypesList, image));				
		
		spinDrinkType.setOnItemSelectedListener(getOnTypeSelectedListener);		
		
		Intent intent = getIntent();
		
		String id = intent.getStringExtra("id");
		mLabel = intent.getStringExtra("name");
		int type = intent.getIntExtra("type", 0);
		int amount = intent.getIntExtra("amount", 0);
		double percent = intent.getDoubleExtra("percent", 5);
		int alc2 = intent.getIntExtra("alc2", 10);
		double alc = (double)alc2/2;
		
		if (mLabel != null)
		if (!mLabel.equals("")){
			nameEdited = true;
		}
						
		mAmountList = getAmountList(type, false);
		spinDrinkAmt.setAdapter(new SpinAmountAdapter(this, R.layout.spinner_amt_item, mAmountList, image));
		spinDrinkAmt.setOnItemSelectedListener(getOnAmountSelectedListener);
		
		
		mAmountPos = amount;
		mSelectPos = amount;		
					
		spinDrinkType.setSelection(type);		
		spinDrinkAmt.setSelection(amount);
		mPercent = percent;	
		
		favorites = Favorites.getInstance(this);
		//new Favorites(this);
		menuHelp = new MenuHelper(this);	
		//menuHelp = MenuHelper.getInstance(this);
		
		mToday = Calendar.getInstance();
				
		mHour = mToday.get(Calendar.HOUR_OF_DAY);
		mMin = mToday.get(Calendar.MINUTE);		
		tvTime.setText(String.format("%02d:%02d", mHour, mMin));
        
        if (mYear < 2000) mYear = 2013;
        /*
        mDatePicker.init(
          mToday.get(Calendar.YEAR), 
          mToday.get(Calendar.MONTH), 
          mToday.get(Calendar.DAY_OF_MONTH), this);
		*/		
				
		mWeightKg = nutrInfo.lbsToKg(mLbs);
				
		
        //seekPerc.setMax(maxPerc-minPerc);        
                               	
        //seekPerc.setProgress(progress);
        /*
        seekHeight = (SeekBar)findViewById(R.id.seekHeight); // make seekbar object
        seekHeight.setMax(maxHgt-minHgt);
        seekHeight.setOnSeekBarChangeListener(this); // set seekbar listener.
        //seekWeight = (SeekBar)findViewById(R.id.seekWeight); // make seekbar object
        //seekWeight.setOnSeekBarChangeListener(this); // set seekbar listener.
        //seekWeight.setMax(maxWgt-minWgt);
        seekWeightLbs = (SeekBar)findViewById(R.id.seekWeightLbs); // make seekbar object
        seekWeightLbs.setOnSeekBarChangeListener(this); // set seekbar listener.
        seekWeightLbs.setMax(maxWgtLbs-minWgtLbs);		
		
		mAge = settings.getInt("age", 30);
		mHeight = settings.getInt("height", 160);
		//mWeight = settings.getInt("weight", 50);
		mLbs = settings.getInt("lbs", 100);
		
		mExercise = settings.getInt("exercise", 1);
		mAuthToken = settings.getString("token", ""); 
		
		// Read stored data		
         */
		btAddDrink.setOnClickListener(this);
		btExit.setOnClickListener(this);
		btPercMin.setOnClickListener(this);
		btPercPlus.setOnClickListener(this);
		btAddFavor.setOnClickListener(this);
		bEditName.setOnClickListener(this);
		bSetTime.setOnClickListener(this);
		bSetDefName.setOnClickListener(this);
		bSetDefPerc.setOnClickListener(this);
		//btHgtMin.setOnClickListener(this);
		//btHgtPlus.setOnClickListener(this);
		//btWgtMin.setOnClickListener(this);
		//btWgtPlus.setOnClickListener(this);
		//rGroupSex = (RadioGroup) findViewById(R.id.radioGroupSex);
		//rGroupSex.setOnCheckedChangeListener(this);
		//rGroupExc = (RadioGroup) findViewById(R.id.radioGroup2);
		//rGroupExc.setOnCheckedChangeListener(this);				
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		//ArrayAdapter<CharSequence> adapterExc = ArrayAdapter.createFromResource(this,
		//        R.array.exercise, android.R.layout.simple_spinner_item);
		/*
		ArrayAdapter<CharSequence> adapterDrinkType = ArrayAdapter.createFromResource(this,
		        R.array.drink_types, R.layout.spinner_type_item); 
		        //android.R.layout.simple_spinner_item);
		        //.simple_selectable_list_item);
		// Specify the layout to use when the list of choices appears
		adapterDrinkType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		
		// Apply the adapter to the spinner
		spinDrinkType.setAdapter(adapterDrinkType);
		
		ArrayAdapter<CharSequence> adapterDrinkAmt = ArrayAdapter.createFromResource(this,
		        R.array.beer_amounts, android.R.layout.simple_spinner_item);
		        //.simple_selectable_list_item);
		// Specify the layout to use when the list of choices appears
		adapterDrinkAmt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		
		// Apply the adapter to the spinner
		spinDrinkAmt.setAdapter(adapterDrinkAmt);		
		*/
		//spinExe.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner, spinnerItems));
		

		/*
		displayValues();
		CountPublish();
		*/
		
		builder = new AlertDialog.Builder(this);
		// Add the buttons
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User clicked OK button
		        	   //dialog.dismiss();
		        	   nameEdited = true;
		        	   mLabel = nutrInfo.firstCapital(etLabel.getText().toString());
		        	   tvDrinkName.setText(mLabel);
		        	     
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
		builder.setTitle(R.string.type_name);
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.dialog_type_name, null);
		builder.setView(v);
		etLabel = (EditText) v.findViewById(R.id.etLabel);
		// Create the AlertDialog
		dialog = builder.create();
		//dialog.show();
		//fbNativeAds.createAndLoadNativeAd(this, nativeAdContainer, R.layout.ad_unit_no_img);
		
		// --------------- TimePicket Dialog
				
		tpDialog = new TimePickerDialog(this, this, mHour, mMin, true);
		//tpDialog.show();
	}
	
	public class SpinTypesAdapter extends ArrayAdapter<String> {

		public SpinTypesAdapter(Context context, int textViewResourceId,
		    String[] objects, Integer[] image) {
		   super(context, textViewResourceId, objects);
		   // TODO Auto-generated constructor stub
		}

		@Override
		public View getDropDownView(int position, View convertView,
		    ViewGroup parent) {
		   // TODO Auto-generated method stub
		   return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		   // TODO Auto-generated method stub
		   return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView,
		    ViewGroup parent) {
		   // TODO Auto-generated method stub
		   // return super.getView(position, convertView, parent);

		   LayoutInflater inflater = getLayoutInflater();
		   View row = inflater.inflate(R.layout.spinner_type_item, parent, false);
		   TextView label = (TextView) row.findViewById(R.id.type_name);
		   label.setText(mTypesList[position]);

		   ImageView icon = (ImageView) row.findViewById(R.id.type_img);
		   icon.setImageResource(image[position]);
		   return row;
		}
	}
	
	public class SpinAmountAdapter extends ArrayAdapter<String> {

		public SpinAmountAdapter(Context context, int textViewResourceId,
		    String[] objects, Integer[] image) {
		   super(context, textViewResourceId, objects);
		   // TODO Auto-generated constructor stub
		}

		@Override
		public View getDropDownView(int position, View convertView,
		    ViewGroup parent) {
		   // TODO Auto-generated method stub
		   return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		   // TODO Auto-generated method stub
		   return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView,
		    ViewGroup parent) {
		   // TODO Auto-generated method stub
		   // return super.getView(position, convertView, parent);
		   int typePos = spinDrinkType.getSelectedItemPosition();
		   String[] amountList = getAmountList(typePos, false);

		   LayoutInflater inflater = getLayoutInflater();
		   View row = inflater.inflate(R.layout.spinner_amt_item, parent, false);
		   TextView label = (TextView) row.findViewById(R.id.type_name);
		   int pos = Math.min(amountList.length-1, position);
		   label.setText(amountList[pos]+" = "+getDrinkAmountStr(pos));

		   ImageView icon = (ImageView) row.findViewById(R.id.type_img);
		   icon.setImageResource(image[typePos]);
		   icon.setScaleX((position+5f)/10);
		   icon.setScaleY((position+5f)/10);		  
		   return row;
		}
	}	
		
/*
	// Display all input values
	void displayValues()
	{
		seekPerc.setProgress(mAge-minAge);
		seekHeight.setProgress(mHeight-minHgt);
		//seekWeight.setProgress(mWeight-minWgt);
		seekWeightLbs.setProgress(mLbs-minWgtLbs);
		spinDrinkType.setSelection(mExercise);		
		if (mSex.equals("Male"))
			rGroupSex.check(R.id.radioMale);
		else
			rGroupSex.check(R.id.radioFemale);
	}
	*/
	
	String getDrinkAmountStr(int pos){
		
		double amountMl = nutrInfo.strToDbl(mAmountList[pos], 500);
		String res = "";
		switch(mUnit){
		case 0:
			res = mAmountList[pos]+" ml";
			break;
		case 1:
			res = String.format("%.1f fl oz", amountMl/28.4125);
			break;
		case 2:
			res = String.format("%.1f fl oz", amountMl/29.5735);
			break;
		}
		return res;
	}
	
	
	String setDrinkName(int pos){
		String label = "";
		String amountStr = getDrinkAmountStr(pos);
		if (nameEdited == true){
			label = tvDrinkName.getText().toString();
		}else{
			label = mTypesList[mTypePos]+" "+mPercent+"% alc, "+amountStr;
			tvDrinkName.setText(label);
		}
		return label;
	}
	
	
	/// Buttons click
	@Override
	public void onClick(View arg0) {		
		
		int pos = Math.min(mAmountList.length-1, mSelectPos);
		String label = mTypesList[mTypePos]+" "+mPercent+"%, "+mAmountList[pos];
		int id = arg0.getId();
		int progress = seekPerc.getProgress();
		
		switch(id){
		case (R.id.btPercMin):
			int prog = seekPerc.getProgress();
			if (prog>0)
				seekPerc.setProgress(prog-1);
			/*
			if (mPercent>mPercMin){
				
				mPercent -= 0.5;
				int prog = (int)Math.round(mPercent*2-mPercMin);
				seekPerc.setProgress(prog);
			}			*/
			break;
		case (R.id.btPercPlus):
			seekPerc.incrementProgressBy(1);
			/*
			if (mPercent<mPercMin+seekPerc.getMax()/2){
				mPercent += 0.5;
				int prog = (int)Math.round(mPercent*2-mPercMin);
				seekPerc.setProgress(prog);
			}*/	
			break;
		case (R.id.btAddFavorite):
			//seekPerc.incrementProgressBy(1);
			
			mPercent = mPercMin+(double)progress/2;
			label = setDrinkName(pos);
			//mTypesList[mTypePos]+" "+mPercent+"% alc, "+mAmountList[pos];
			BloodRecord favDrink = new BloodRecord(mAmountMl, mPercent, mTypePos, mSelectPos, label, -1);	
			favorites.addFavorite(favDrink);
			toastMsg(R.string.fav_added);
			break;
			
		case (R.id.bEditName):
			//builder.show();	
			dialog.show();
			break;
		case (R.id.bSetTime):
			//builder.show();	
			tpDialog.show();
			break;		
		case (R.id.bSetDefName):
			nameEdited = false;
			setDrinkName(mSelectPos);
			break;
		case (R.id.bSetDefPerc):
			mPercent = getDefaultPercent(mTypePos);
			progress = getSeekProgress(mPercent);
			seekPerc.setProgress(progress);						
			break;			
			/*
		case (R.id.btHgtPlus):
			if (mHeight<maxHgt){ 
				mHeight++;
				seekHeight.setProgress(mHeight-minHgt);
			}	
			break;
		case (R.id.btWgtMin):
			// Lbs
			if (mLbs>minWgtLbs){ 
				mLbs--;
				seekWeightLbs.setProgress(mLbs-minWgtLbs);
			}	
			break;
		case (R.id.btWgtPlus):
			// Lbs
			if (mLbs<maxWgtLbs){ 
				mLbs++;
				seekWeightLbs.setProgress(mLbs-minWgtLbs);
			}	
			break;	
			*/
		case (R.id.btAdd):
			/*
			SharedPreferences settings = getApplicationContext().getSharedPreferences("Settings", 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("age", mAge);
			editor.putInt("height", mHeight);
			//editor.putInt("weight", mWeight);
			editor.putInt("lbs", mLbs);
			editor.putString("sex", mSex);
			editor.putInt("exercise", mExercise);					
			editor.putInt("kcal", (int)Math.round(mBMR));
			// Commit the edits!
			editor.commit();
			double weightKg = nutrInfo.lbsToKg(mLbs);
			*/
			Calendar calendar = Calendar.getInstance();			
			Date today = calendar.getTime();
			int year = calendar.get(Calendar.YEAR);//today.getYear();
			int month = calendar.get(Calendar.MONTH);//today.getMonth();
			//int day = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			//today.getDay();
			//getDayOfMonth();
			int hour = mHour;//timePicker.getCurrentHour();
			int min = mMin;//timePicker.getCurrentMinute();
			calendar.set(year, month, day, hour, min, 0);
			long startTime = calendar.getTimeInMillis();			
			long timeSecs = startTime/1000; 
						
			mPercent = mPercMin+(double)progress/2;
			label = setDrinkName(pos);
			//mTypesList[mTypePos]+" "+mPercent+"% alc, "+mAmountList[pos];
			BloodRecord diaryDrink = new BloodRecord(mAmountMl, mPercent, mTypePos, mSelectPos, label, timeSecs);	
			favorites.addDiary(diaryDrink);			
			toastMsg(R.string.diary_added);
			try{
				//favorites.updateWeight(weightKg, mHeight, weightKg-5, false);
			}catch(Exception e){
				String no_internet = "Turn on internet connection";
				Toast.makeText(ActivityAddDrink.this, no_internet, Toast.LENGTH_SHORT).show();
			}
			//favorites.updateWeight(mLbs, mHeight, mLbs-10, true);
			//finish();
			break;
		case (R.id.btExit):
			finish();
			break;				
		}
			
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		double percent = 5;
		int id = seekBar.getId();
		switch (id) {
		case (R.id.seekPercent):			
			percent = mPercMin+(double)progress/2;
			tvPercent.setText(String.valueOf(percent)+" %");
			mPureAlc = percent*mAmountMl/100; 
			tvPureAlc.setText(String.valueOf(mPureAlc)+" ml");			
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
		
		int feet = (int) (mHeight/(12*2.54));
		int inches = (int)(Math.round(mHeight%(feet*12*2.54))/2.54);
		int lbs = (int)Math.round(mWeightKg/0.45359237); 
		String years = getResources().getString(R.string.years);
		tvPercent.setText(mAge+" "+years);		
		tvMeters.setText(mHeight+" cm = "+feet+" feet "+inches+" inches");
		//tvFeet.setText(feet+" feet "+inches+" inches");		
		//tvKilos.setText(mWeight+" kg = "+lbs+" lbs");
		tvKilos.setText(String.format("%.1f", mWeightKg)+" kg = "+mLbs+" lbs");
		*/
		//tvLbs.setText(lbs+" lbs");
		//CountPublish();
		mPercent = percent;
		if (nameEdited == false)
			setDrinkName(mSelectPos);		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	private void setAdapter(int arrayId){
		/*
		ArrayAdapter<CharSequence> adapterDrinkAmt = ArrayAdapter.createFromResource(this,
		        array, android.R.layout.simple_spinner_item);
		        //.simple_selectable_list_item);
		// Specify the layout to use when the list of choices appears
		adapterDrinkAmt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		
		// Apply the adapter to the spinner
		spinDrinkAmt.setAdapter(adapterDrinkAmt);*/
		String[] array = getResources().getStringArray(arrayId);
		spinDrinkAmt.setAdapter(new SpinAmountAdapter(this, R.layout.spinner_amt_item, array, image));			
	}
	
	int getSeekProgress(double percent){
		
		int progress = (int)Math.round((percent-mPercMin)*2);
		return progress;
	}
	
	double getDefaultPercent(int pos){
		double result = 5;
		switch(pos){
		case 0:
			result = beerPerc;
			break;
		case 1:
			result = winePerc;
			break;
		case 2:
			result = vodkaPerc;
			break;
		case 3:
			result = vodkaPerc;
			break;			
		}
		return result;
	}
	
	private void configSpinDrinkAmount(int pos){
		int progress = 10;
		mAmountList = getAmountList(pos, true);
		switch(pos){
		case 0:
			setAdapter(R.array.beer_amounts);
			//mPercMin = 3;
			//seekPerc.setMax(20);
			numberPicker1.setMaxValue(15);
			numberPicker1.setMinValue(3);
			numberPicker1.setValue(5);			
			//mPercent = beerPerc;			
			break;
		case 1:
			setAdapter(R.array.wine_amounts);
			//mPercMin = 7;
			//seekPerc.setMax(30);
			numberPicker1.setMaxValue(20);
			numberPicker1.setMinValue(5);
			numberPicker1.setValue(11);
			//mPercent = winePerc;
			//progress = getSeekProgress(winePerc);
			//progress = (int)Math.round((mPercent-mPercMin)*2);
			//seekPerc.setProgress(progress);			
			break;
		case 2:
			setAdapter(R.array.vodka_amounts);
			//mPercMin = 30;
			//seekPerc.setMax(50);
			numberPicker1.setMaxValue(55);
			numberPicker1.setMinValue(20);
			numberPicker1.setValue(40);
			//mPercent = vodkaPerc;
			//progress = getSeekProgress(vodkaPerc);
			//progress = (int)Math.round((mPercent-mPercMin)*2);
			//seekPerc.setProgress(progress);			
			break;
		case 3:
			setAdapter(R.array.cocktail_amounts);
			//mPercMin = 30;
			//seekPerc.setMax(50);		
			///mPercent = vodkaPerc; 
			//progress = getSeekProgress(vodkaPerc);
			//progress = (int)Math.round((mPercent-mPercMin)*2);
			//seekPerc.setProgress(progress);					
			break;			
		}
		progress = getSeekProgress(mPercent);
		//progress = (int)Math.round((mPercent-mPercMin)*2);
		//seekPerc.setProgress(progress);	
	}
	
	OnItemSelectedListener getOnTypeSelectedListener = 
			new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					configSpinDrinkAmount(position);
					mTypePos = position;			
					spinDrinkAmt.setSelection(mAmountPos);
					if (nameEdited == false){
						setDrinkName(mAmountPos); }
					int progress = 10;
					switch(position){
						case 0:
							progress = 4;
							break;
						case 1:
							progress = 16;
							break;
						case 2:
							progress = 74;
							break;
						case 3:
							progress = 74;
							break;
					}
					seekPerc.setProgress(progress);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
		
	};
	
	OnItemSelectedListener getOnAmountSelectedListener = 
			new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					int[] amountMl = new int[8]; // will be assigned a table according to type choice
					
					int typePos = spinDrinkType.getSelectedItemPosition();
					//int amtPos = spinDrinkAmt.getSelectedItemPosition();
					//mAmountList = getAmountList(typePos, true);
					//mAmountList = getAmountList(position, true);
					mSelectPos = position;
					for(int i=0; i<mAmountList.length; i++){
						amountMl[i] = nutrInfo.strToInt(mAmountList[i], 100);
					}
					//mAmountPos = position;
					//mAmount = amountMl[amtPos];
					mAmountMl = amountMl[position];
					mPureAlc = mAmountMl*mPercent/100;
					//onProgressChanged(seekPerc, seekPerc.getProgress(), true);	
					tvPureAlc.setText(String.valueOf(mPureAlc)+" ml");
					if (nameEdited == false)
						setDrinkName(position);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
		
	};
	
	String[] getAmountList(int typePos, boolean ml){
		String[] strArray = new String[8];
		String[] beerVols = new String[5];
		switch(mUnit){
		case 0:
			beerVols = mBeerMl;//getResources().getStringArray(R.array.beer_ml);
			break;
		case 1:
			beerVols = mBeerUs;//getResources().getStringArray(R.array.beer_ml_us);
			break;
		case 2:
			beerVols = mBeerUk;//getResources().getStringArray(R.array.beer_ml_uk);
			break;
		}
		
		switch(typePos){
		case 0:
			if (ml)
				strArray = beerVols;//getResources().getStringArray(R.array.beer_ml);
			else
				strArray = getResources().getStringArray(R.array.beer_amounts);				
			break;
		case 1:
			if (ml)
				strArray = getResources().getStringArray(R.array.wine_ml);
			else
				strArray = getResources().getStringArray(R.array.wine_amounts);				
			break;								
		case 2:
			if (ml)
				strArray = getResources().getStringArray(R.array.vodka_ml);
			else
				strArray = getResources().getStringArray(R.array.vodka_amounts);				
			break;
		case 3:
			if (ml)
				strArray = getResources().getStringArray(R.array.cocktail_ml);
			else
				strArray = getResources().getStringArray(R.array.cocktail_amounts);				
			break;
		}
		return strArray;
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
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	
		menuHelp.handleOnItemSelected(this, item);
		return true;

    }

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		mHour = hourOfDay;
		mMin = minute;
		mToday.set(Calendar.HOUR_OF_DAY, hourOfDay);
		mToday.set(Calendar.MINUTE, minute);
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		mHour = hourOfDay;
		mMin = minute;
		tvTime.setText(String.format("%02d:%02d", hourOfDay, minute));
	} 	
/*
	private void toastMsg(int stringId){
		
		String msg = getResources().getString(stringId);
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}	
*/
  	public void Update() {
		// TODO Auto-generated method stub
		int themeId = menuHelp.dialogSymbol.getThemeId();
		int selectionId = menuHelp.dialogSymbol.getSelectionId();
		//getApplication().setTheme(themeId);
		ThemeUtils.changeToTheme(this, selectionId);

	}

	
}