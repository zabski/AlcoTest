package com.zmobile.alcotest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import com.google.analytics.tracking.android.EasyTracker;
//import com.ironsource.mobilcore.MobileCore;

//import com.zmobile.foodtest.Favorites.UpdateWeightRequest;
/*
import fatsecret.platform.FatSecretAPI;
import fatsecret.platform.FatSecretAuth;
import fatsecret.platform.FatSecretException;
*/
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.DigitalClock;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;
import android.widget.TextView;
import com.google.android.gms.ads.*;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.*;

public class ActivityDiaryToday extends ActivityTemplate implements OnClickListener, OnChronometerTickListener,
	Updatable {
	
	String[] mTypesList;
	//ImageButton bPrevDay;
	//ImageButton bNextDay;
	Button bClearAll;
	TableLayout diaryTable;
	TableLayout tblTotals;
	//TextView tvDay;
	TextView tvBAC;
	TextView tvLastBAC;
	DigitalClock clock;
	Chronometer chrono;
	AlertDialog.Builder builder;	
	AlertDialog clearAllDialog;
	AlertDialog delItemDialog;
	
	SharedPreferences sharedData;
	String filename = "SharedData";
	
	/*
	TextView tvTotalKcals;
	TextView tvTotalCarbos;
	TextView tvTotalProtein;
	TextView tvTotalFat;*/
	TextView tvDiaryEntry;
	PersonInfo personInfo;
	
	//FatSecretAPI fatSecretApi;
	//FatSecretAuth fatSecretAuth;
	Favorites favorites;	
	
	String consumerKey;
	String consumerSecret;	
	
	String mClickedItemSign;
	Map<String,String> parameters;
	
	Map<String,Double> consumedTotals;
	
	List<BloodRecord> diaryList;
	
	List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	
	MenuHelper menuHelp;
	
	String[] itemNamesToView = {"calories", "carbohydrate",	"protein", "fat"};
	//String[] itemNames = {"food_entry_id", "food_entry_description", "meal", "food_id", "serving_id", "number_of_units", "food_entry_name"};
	
	long currentDate;
	double totalKcals = 0;
	double totalFat = 0;
	double totalCarbos = 0;
	double totalProtein = 0;
	int mUnit;
	int ads;
	//HandlerPurchase handlerIAP;

	@Override
	public void onStart() {
	    super.onStart();
	    // The rest of your onStart() code.
	    //EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	    chrono.start();
		/*
		if (ads<4 || handlerIAP.mHasRemovedAds) {
			adView.setVisibility(View.GONE);
			faceAdView.setVisibility(View.GONE);
		}else {
			//adView.loadAd(adRequest);
			faceAdView.loadAd();
		}*/
        //interstitial.loadAd(adRequest);
	    //onChronometerTick(chrono);
	}

	@Override
	public void onStop() {
	    super.onStop();
	    // The rest of your onStop() code.
	    //EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	    chrono.stop();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		//adView.pause();
		super.onPause();		
		ThemeUtils.saveTheme(sharedData);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_diary);
		setUpAdLayouts();
		super.onCreate(savedInstanceState);
		classTAG = this.getClass().getSimpleName();
		mTracker = getDefaultTracker(this);
		ThemeUtils.onActivityCreateSetTheme(this);

		//facebook native
		faceNativeLayoutId = R.layout.ad_unit_row;
		admobNativeLayoutId = R.layout.ad_app_install;
		//nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container);
		adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);
		statusText = (TextView) findViewById(R.id.status);
		viewType = NativeAdView.Type.HEIGHT_100;
		// Facebook Audeince Network
		RelativeLayout adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);
		String facebook_banner_id = getString(R.string.facebook_banner_id);

		sharedData = getSharedPreferences(filename, 0);
		mTypesList = getResources().getStringArray(R.array.drink_types);
		
		//MobileCore.getSlider().setContentViewWithSlider(this, R.layout.activity_graph_view, R.raw.slider_1);
		
		//bPrevDay = (ImageButton) findViewById(R.id.bPrevDay);
		//bNextDay = (ImageButton) findViewById(R.id.bNextDay);
		//tvDay = (TextView) findViewById(R.id.tvDay);
        tvBAC = (TextView) findViewById(R.id.tvBAC);
        tvLastBAC = (TextView) findViewById(R.id.tvLastBAC);
        clock = (DigitalClock) findViewById(R.id.digitalClock1);
        chrono = (Chronometer) findViewById(R.id.chronometer1);
        tblTotals = (TableLayout) findViewById(R.id.tblTotals);
        		
        bClearAll = (Button) findViewById(R.id.bClearAll); 
        /*
        tvTotalKcals = (TextView) findViewById(R.id.tvTotalKcals);
        tvTotalCarbos = (TextView) findViewById(R.id.tvTotalCarbos);
        tvTotalProtein = (TextView) findViewById(R.id.tvTotalProtein);
        tvTotalFat = (TextView) findViewById(R.id.tvTotalFat);
        */                
        
        diaryTable = (TableLayout) findViewById(R.id.diaryTableLayout);        
        
        //bPrevDay.setOnClickListener(this);
        //bNextDay.setOnClickListener(this);
        bClearAll.setOnClickListener(this);
        
        chrono.setOnChronometerTickListener(this);
		
		//consumerKey = getString(R.string.consumerKey);				
		//consumerSecret = getString(R.string.consumerSecret);
		/*
		SharedPreferences settings = getApplicationContext().getSharedPreferences("Settings", 0);
		
		String sex = settings.getString("sex", "Female");
		int alc = settings.getInt("alc", 2);
		mUnit = settings.getInt("unit", 0);
		int lbs = settings.getInt("lbs", 150);
		*/
		//personInfo = new PersonInfo(this, sex);  
		personInfo = PersonInfo.getInstance(this);
		menuHelp = new MenuHelper(this);
		//menuHelp = MenuHelper.getInstance(this);
		/*
		double weightKg = personInfo.lbsToKg(lbs);
		boolean sexMale = true;
		if (sex.equals("Female"))
			sexMale = false;
		*/
		consumedTotals = new HashMap<String, Double>();		
				      
		
		try{
			//fatSecretApi = new FatSecretAPI(consumerKey,consumerSecret);
			
			favorites = Favorites.getInstance(this);
			//, sexMale, lbs, alc/10);
			//new Favorites(this, sexMale, lbs, alc/100);
			
			//fatSecretAuth = favorites.getFatSecretAuth();
		}catch(Exception e){
			String err = e.toString();
			//String no_internet = getResources().getString(R.string.no_internet);
			//Toast.makeText(this, no_internet, Toast.LENGTH_SHORT).show();
		}
		
		long nowMils = Calendar.getInstance().getTimeInMillis();
		long nowSecs = nowMils/1000;
		
		currentDate = nowMils/(1000*60*60*24);	
		
		long lastDrinkTime = favorites.getLastDrinkTime();
		
		double hoursSinceLastDrink = (nowSecs-lastDrinkTime)/(60*60); 
		
		if (hoursSinceLastDrink > 12 && favorites.calcLastBacNow() <= 0)
			favorites.clearDiary();
		
		createClearAllDialog();
		createDelDiaryDialog();
		getDiary(currentDate);
		//fbNativeAds.createAndLoadNativeAd(this, nativeAdContainer, R.layout.ad_unit_no_img);
	}
	
	void createClearAllDialog(){
		builder = new AlertDialog.Builder(this);
		// Add the buttons
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User clicked OK button
		        	   //dialog.dismiss();
		        	   favorites.clearDiary();
		        	   getDiary(currentDate);
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
		builder.setTitle(R.string.new_record);
		/*
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.dialog_type_name, null);
		builder.setView(v);
		etLabel = (EditText) v.findViewById(R.id.etLabel);
		// Create the AlertDialog*/
		clearAllDialog = builder.create();		
	}
	
	void createDelDiaryDialog(){
		builder = new AlertDialog.Builder(this);
		// Add the buttons
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User clicked OK button
		        	   //dialog.dismiss();		        	   
		        	   //favorites.delDiaryItem(diary_sign);
		        	   deleteDiaryItem(mClickedItemSign);
		        	   getDiary(currentDate);
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
		builder.setTitle(R.string.del_item);
		/*
		LayoutInflater inflater = getLayoutInflater();
		View v = inflater.inflate(R.layout.dialog_type_name, null);
		builder.setView(v);
		etLabel = (EditText) v.findViewById(R.id.etLabel);
		// Create the AlertDialog*/
		delItemDialog = builder.create();		
	}
	
	private void clearTable(){
		
		diaryTable.removeAllViews();
		totalKcals = 0;
		totalFat = 0;
		totalCarbos = 0;
		totalProtein = 0;
	}		
	
	private void insertDiaryItem(BloodRecord drink, int arrayIndex){
		
		int id = drink.getId();
		int typeId = drink.getmTypeId();
		int amountId = drink.getmAmountId();
		int amountMl = drink.getmAmount();
		long time = drink.getTime();
		//Calendar cal = Calendar.
		Date d = new Date(time*1000);
		int hour = d.getHours();
		int mins = d.getMinutes();
		String timeForm = String.format(" %02d:%02d", hour, mins); 
				//String.valueOf(hour)+":"+String.valueOf(mins);
		double percent = drink.getmPercent();	
		String drinkId = String.valueOf(id);
		String label = drink.getmLabel();
		String type = mTypesList[typeId];
		String amount = drink.getVolumeStr(mUnit);
				//String.valueOf(amountMl)+" ml";
		String perc = String.format("%.1f ",percent) + "%";
		String details = type+" "+perc+", "+amount+", "+timeForm;	
		int percentInt = (int)(percent*100);
		//String signature = String.format("%d-%d-%.1f", typeId, amountId, percent);
		String signature = String.format("%d-%d-%d", typeId, amountId, percentInt);
		//perc = perc.concat(" %");
		
		// Get the LayoutInflator service
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// Use the inflater to inflate a stock row from stock_quote_row.xml
		View newFoodRow = inflater.inflate(R.layout.diary_list_row, null);
		
		// Create the TextView for the ScrollView Row
		TextView tvLabel = (TextView) newFoodRow.findViewById(R.id.tvDiaryName);
		//TextView tvDiaryServing = (TextView) newFoodRow.findViewById(R.id.tvDiaryServing);
		TextView tvDetails = (TextView) newFoodRow.findViewById(R.id.tvDiaryUnits);
		TextView tvDiaryId = (TextView) newFoodRow.findViewById(R.id.idDiaryEntry);
		TextView tvDrinkId = (TextView) newFoodRow.findViewById(R.id.idFood);
		ImageButton btDelDiary = (ImageButton) newFoodRow.findViewById(R.id.btDelDiary);
		ImageButton foodInfoButton = (ImageButton) newFoodRow.findViewById(R.id.foodInfoButton);
		ImageView ballImage = (ImageView) newFoodRow.findViewById(R.id.diaryImg);
					
		int drink_img = R.drawable.beer;
		
		switch(typeId){
			case 0:	drink_img = R.drawable.beer; break;
			case 1:	drink_img = R.drawable.wine; break;
			case 2:	drink_img = R.drawable.liquor; break;
			case 3:	drink_img = R.drawable.cocktail1; break;
		}
		
		//Drawable ball = getResources().getDrawable(ball_img);
		//ballImage.setBackgroundDrawable(ball);			
		ballImage.setImageResource(drink_img);
		
		// Add the stock symbol to the TextView
		tvLabel.setText(label);		
		tvDrinkId.setText(signature);
		tvDiaryId.setText(signature);
		tvDetails.setText(details);	
				
		foodInfoButton.setOnClickListener(getFoodInfoListener);
		btDelDiary.setOnClickListener(getDelEntryListener);
		/*
		Button quoteFromWebButton = (Button) newStockRow.findViewById(R.id.quoteFromWebButton);
		quoteFromWebButton.setOnClickListener(getStockFromWebsiteListener);
		*/
		// Add the new components for the stock to the TableLayout
		arrayIndex = -1;
		diaryTable.addView(newFoodRow, arrayIndex);
		//tblTotals.addView(newFoodRow, arrayIndex);
				
		
	}
	/*
	private void updateTotalsTable(){
		
		TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
		TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		//LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		//llp.width=android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		rowParams.width=android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		//tableParams.width=android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		llp.gravity = Gravity.RIGHT;
		int margin_s = (int) getResources().getDimension(R.dimen.margin_s);
		int margin_m = (int) getResources().getDimension(R.dimen.margin_m);
		
		TableRow tableRowLabels = new TableRow(this);
		tableParams.setMargins(0, 0, 0, 0);
		llp.setMargins(5, 0, 0, 0);
		double rdiRatio;
		String rdiText;
		double value;
		int intRatio;
		int left_pad = 15;
		
		tblTotals.setLayoutParams(llp);
		tblTotals.removeAllViews();
		
		consumedTotals.put("calories", totalKcals);
		consumedTotals.put("carbohydrate", totalCarbos);
		consumedTotals.put("protein", totalProtein);
		consumedTotals.put("fat", totalFat);
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//View tableLabels = inflater.inflate(R.layout.diary_table_labels, null);
		View tableLabels = inflater.inflate(R.layout.totals_table_row, null);
		int colorDark = getResources().getColor(R.color.ewhite);
		
		tblTotals.addView(tableLabels, -1);	
		
		Typeface style = Typeface.DEFAULT;
		String[] keySet = itemNamesToView;
		int rowCount = 1;
		//List<String> keySet = new ArrayList<String>(Arrays.asList(itemNamesToView)); 
		//keySet.add("kcals_from_fat");
		for(String key: keySet){
			TableRow tableRow = new TableRow(this);
			LinearLayout lineLayout = new LinearLayout(this);
			LinearLayout lineLayoutRDI = new LinearLayout(this);
			lineLayout.setLayoutParams(rowParams);
			lineLayoutRDI.setLayoutParams(rowParams);
			
			LinearLayout.LayoutParams bar_lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);					
			
			//TextView title = new TextView(this);
			
			View newTotalRow = inflater.inflate(R.layout.totals_table_row, null);
			
			// Create the TextView for the ScrollView Row
			TextView title = (TextView) newTotalRow.findViewById(R.id.tvFoodType);
			TextView tv = (TextView) newTotalRow.findViewById(R.id.tvAmount);
			TextView tvRDI = (TextView) newTotalRow.findViewById(R.id.tvRDI);
			TextView tvBar = (TextView) newTotalRow.findViewById(R.id.tvBar);
			//title.setText(itemNames[infoNr]);
			//title.setTextSize(R.dimen.legend_text_size);

			int textSize = (int) getResources().getDimension(R.dimen.text_xs);
			int chartSize = (int) getResources().getDimension(R.dimen.month_width)*2;
			//title.setTextSize(textSize);
			int bkgColor = 0;
			if (rowCount%2==1) 
				bkgColor = colorDark;
			else 
				bkgColor = Color.WHITE;						
			 
			title.setBackgroundColor(bkgColor);
			int id = getResources().getIdentifier(key, "string", getPackageName());
			String label = getResources().getString(id);			
			
			title.setText(label);
			title.setTypeface(style);
			//title.setLayoutParams(llp);
			title.setPadding(margin_s, margin_s, margin_s, margin_s);
			//lineLayout.addView(title, llp);
			//tableRow.addView(newTotalRow, rowParams);					
			
			//for(int servingNr=1; servingNr<2; servingNr++){ //foodInfo.size()
				//TextView tv = new TextView(this);				
				//String text = dataTable[infoNr][servingNr];

				double amount = consumedTotals.get(key);
				
				//tv.setTextSize(textSize);
				tv.setTypeface(style);
				tv.setGravity(Gravity.CENTER);
				tv.setPadding(margin_s, margin_s, margin_s, margin_s);
				tv.setBackgroundColor(bkgColor);
				//tv.setTextSize(R.dimen.legend_text_size);
				String unitKey = "_"+key;									
				int unitId = getResources().getIdentifier(unitKey, "string", getPackageName());
				String unitLabel = getResources().getString(unitId);
				String formated = String.format("%.0f",amount);
				if (unitLabel.equals("g"))
					formated = String.format("%.1f",amount);
				tv.setText(formated+" "+unitLabel);
				//tv.setLayoutParams(rowParams);
				//lineLayout.addView(tv, llp);
			//}
			// RDI column
				
				//TextView tvRDI = new TextView(this);				
				//String text = dataTable[infoNr][servingNr];	
						//valueOf(rdiRatio)+" %";								
				rdiText = personInfo.getPercentStr(key, consumedTotals.get(key));
				double rdi = personInfo.getPercent(key, consumedTotals.get(key));
				int width = Math.min((int)rdi*2,chartSize); 
				bar_lp.width=width;
				bar_lp.height=20;
				
				//tvRDI.setLayoutParams(llp);
				
				//tvRDI.setTextSize(textSize);
				tvRDI.setTypeface(style);	
							
				tvRDI.setGravity(Gravity.RIGHT);
				tvRDI.setPadding(margin_s, margin_s, margin_s, margin_s);
				tvRDI.setBackgroundColor(bkgColor);				
				tvRDI.setText(rdiText);
				
				tvBar.setLayoutParams(bar_lp);
				int color = personInfo.getColorBar(rdi);
				GradientDrawable gradient = personInfo.getGradient(color,1,2);
				//tvBar.setBackgroundColor(color);
				tvBar.setBackgroundDrawable(gradient);	
				//lineLayoutRDI.addView(tvRDI, rowParams);				
				
				rowCount++;
				//tableRow.setLayoutParams(rowParams);
			//tableRow.addView(lineLayout, rowParams);
			//tableRow.addView(lineLayoutRDI, rowParams);
			//tableRow.addView(newTotalRow, tableParams);
			//tblTotals.addView(tableRow, tableParams);
			//tblTotals.addView(tableRow, -1);
			//tblTotals.addView(newTotalRow, -1);
			tblTotals.addView(newTotalRow, -1);	
			//tblTotals.addView(title);
			//diaryTable.addView(tableRow, -1);
									
		}
	}*/
	
	private BloodRecord getDiaryForId(String strId){
		
        //List<FavoriteDrink> diaryList = favorites.getDiaryList();
        
        //FavoriteDrink result;
        
        for (BloodRecord drink : diaryList){
        	int drinkId = drink.getId();
        	if (strId.equals(String.valueOf(drinkId))){
        		return drink;
        	}
        }
        return null;
	}	
	
	public OnClickListener getFoodInfoListener = new OnClickListener(){

		public void onClick(View v) {
			
			// Get the text saved in the TextView next to the clicked button
			// with the id stockSymbolTextView

			// TableRow tableRow = (TableRow) v.getParent().getParent();
			LinearLayout layoutRow = (LinearLayout) v.getParent(); 
            //TextView stockTextView = (TextView) layoutRow.findViewById(R.id.nameListTextView);
            TextView tvIdFood = (TextView) layoutRow.findViewById(R.id.idFood);
            //String stockSymbol = stockTextView.getText().toString();
            TextView tvLabel = (TextView) layoutRow.findViewById(R.id.tvDiaryName);            
            String label = tvLabel.getText().toString();
            String id = tvIdFood.getText().toString();                        
            
            // An intent is an object that can be used to start another activity
            Intent intent = new Intent(ActivityDiaryToday.this, ActivityAddDrink.class);
            
            BloodRecord drink = favorites.getDrinkForId(id, label, personInfo);
            //FavoriteDrink drink = getDiaryForId(id);
            // Add the stock symbol to the intent
            //intent.putExtra(STOCK_SYMBOL, stockSymbol);
            if (drink != null){
	            intent.putExtra("id", id);
	            intent.putExtra("type", drink.getmTypeId());
	            intent.putExtra("amount", drink.getmAmountId());
	            intent.putExtra("percent", drink.getmPercent());
	            intent.putExtra("name", drink.getmLabel());
	            /* Add the stock symbol to the intent
	            intent.putExtra(STOCK_SYMBOL, stockSymbol);
	            
	            intent.putExtra("latitude", lastLoc.getLatitude());
	            intent.putExtra("longitude", lastLoc.getLongitude());
	            
	            Bundle bundle = new Bundle();
	            
	            HashMap<String,String> place = getItemForId(id);
	            
	            if (place != null){
	            	            	            
		            for(String key : place.keySet()){
		            	String value =  place.get(key);
		            	bundle.putString(key, value);
		            
		            }            
	            }
	            bundle.putString("id", id);
	            
	            intent.putExtra("place", bundle);
	            
	            //intent.putExtra("place", place);
	            */
	            startActivity(intent);
            }
		}
		
	};
	
	public OnClickListener getDelEntryListener = new OnClickListener(){

		public void onClick(View v) {
			
			// Get the text saved in the TextView next to the clicked button
			// with the id stockSymbolTextView

			// TableRow tableRow = (TableRow) v.getParent().getParent();
			LinearLayout layoutRow = (LinearLayout) v.getParent(); 
            //TextView stockTextView = (TextView) layoutRow.findViewById(R.id.nameListTextView);
            TextView idEntry = (TextView) layoutRow.findViewById(R.id.idDiaryEntry);
            //String stockSymbol = stockTextView.getText().toString();
            String entry_id = idEntry.getText().toString();    
            
            mClickedItemSign = entry_id;
            delItemDialog.show();
            //deleteDiaryItem(entry_id);                                         
			
		}
		
	};
	
	void deleteDiaryItem(String diary_id){
		
		//diaryTable.removeAllViews();
		//favorites.delDiary(personInfo.strToInt(diary_id,0));
		favorites.delDiaryItem(diary_id); 
		getDiary(currentDate);
	}
	
	/*
	public void deleteEntry(String entry_id){
	
		Map<String,String> params = new HashMap<String,String>();
		//String token = fatSecretAuth.getToken();
		//String secret = fatSecretAuth.getSecret();
							
		params.put("food_entry_id", entry_id);
		//params.put("oauth_token", token);
		
		try{
			
			Object[] urlParams = fatSecretApi.GetProfileUrl("food_entry.delete", params, token, secret);
			//FoodSearchRequest request = new FoodSearchRequest();

			String url = (String)urlParams[0];
			String reqMethod = (String)urlParams[1];
			String paramsSigned = (String)urlParams[2];
			//Map<String,String> 
			parameters = (Map<String,String>)urlParams[3];
			
			new DelEntryRequest().execute(url, reqMethod, paramsSigned);
		
		}catch(Exception e){
			
		}
			
	}*/
	
	public HashMap<String,String> getItemForId(String id){
		
		Iterator<HashMap<String,String>> iter = list.iterator();
		HashMap<String,String> item;
		
		while (iter.hasNext()){
			item = iter.next();
			if (item.get("id").equals(id)){
				return item;
			}
		}
		return null;
	}
	
	String getStringDate(long date){
		
		String result = "";
		
		long timeInMilis = date*24*60*60*1000;
		
		Calendar cal = Calendar.getInstance();
		
		cal.setTimeInMillis(timeInMilis);
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		Date dt = new Date(timeInMilis);
		
		DateFormat df = DateFormat.getDateInstance(); 
		
		SimpleDateFormat sdf = new SimpleDateFormat("EE"); 			
		
		result = sdf.format(dt)+", "+df.format(dt);
		
		return result;
	}
	
	void getDiary(long date){
		// current_weight_kg
		Map<String,String> params = new HashMap<String,String>();
		//String token = fatSecretAuth.getToken();
		//String secret = fatSecretAuth.getSecret();					
		//Map<String,String> params = new HashMap<String,String>();	
		diaryList = favorites.getDiaryList();
		diaryTable.removeAllViews();				
		
		for(BloodRecord drink: diaryList){
			//updateFavoritesList(drink);
			insertDiaryItem(drink, 0);
		}
		
		double bac = favorites.calcTotalBacNow();
		
		String bacStr = String.valueOf(bac); 
		
		tvBAC.setText("Current alc is: "+bacStr);
				
	}
	
	@Override
	public void onClick(View button) {
		// TODO Auto-generated method stub
		switch(button.getId()){
		/*
		case R.id.bPrevDay:
			currentDate -= 1;
			getDiary(currentDate);
			break;
		case R.id.bNextDay:
			currentDate += 1;
			getDiary(currentDate);
			break;*/
		case R.id.bClearAll:
			clearAllDialog.show();
			//currentDate += 1;
			//getDiary(currentDate);
			break;			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.diary, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		menuHelp.handleOnItemSelected(this, item);
		return true;
		/*
		Intent i;
        switch (item.getItemId()) {
        case R.id.action_search:
            i = new Intent(this, ActivityAddDrink.class);
            startActivity(i);
            return true;        
        case R.id.action_favorites:
            i = new Intent(this, ActivityFavorites.class);
            startActivity(i);
            return true;        
        case R.id.action_diary:
            i = new Intent(this, ActivityDiaryToday.class);
            startActivity(i);
            return true;                   
        case R.id.action_chart:
            i = new Intent(this, ActivityGraphDiary.class);
            startActivity(i);
            return true;        
        case R.id.action_history:
            i = new Intent(this, ActivityGraphLevel.class);
            startActivity(i);
            return true;                     
        case R.id.action_settings:
          i = new Intent(this, ActivitySettings.class);
          startActivity(i);
          return true;
        case R.id.action_info:
          i = new Intent(this, ActivityInfo.class);
          startActivity(i);
          return true;
        default:
          return super.onOptionsItemSelected(item);
        } */
    } 	
	/*
	private void toastMsg(int stringId){
		
		String msg = getResources().getString(stringId);
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}*/

	@Override
	public void onChronometerTick(Chronometer chronometer) {
		// TODO Auto-generated method stub
		double bac = favorites.calcTotalBacNow();
		
		Calendar cal = Calendar.getInstance();
		long now = cal.getTimeInMillis();
		long nowSecs = (long)(now/1000);
		
		double lastBacNow = favorites.calcLastBAC(nowSecs);
		double lastBac = favorites.calcLastBacNow();
		
		String bacStr = String.format("%.5f", bac);
		String lastBacStr = String.format("%.5f", lastBac); 
		
		tvBAC.setText("Current alc is: "+bacStr);
		
		tvLastBAC.setText("Last alc is: " + lastBacStr);
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
