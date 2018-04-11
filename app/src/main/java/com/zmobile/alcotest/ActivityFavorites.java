package com.zmobile.alcotest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//import com.google.ads.AdRequest;
//import com.google.ads.AdView;
import com.facebook.ads.NativeAdView;
import com.google.android.gms.ads.*;
import com.google.android.gms.analytics.HitBuilders;
//import com.google.analytics.tracking.android.EasyTracker;

//import com.newthinktank.stockquotes.gpsLocate.MyLocationListener;

//import com.newthinktank.stockquotes.StockInfoActivity.MyAsyncTask;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
/*
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMInterstitial;
import com.millennialmedia.android.MMAd;
import com.millennialmedia.android.MMBroadcastReceiver;
import com.millennialmedia.android.MMRequest;
import com.millennialmedia.android.MMSDK;
import com.millennialmedia.android.RequestListener.RequestListenerImpl;
//import com.zmobile.foodtest.Favorites.GetFavoriteRequest;
import com.zmobile.foodtest.LocationValet.ILocationValetListener;
import com.zmobile.foodtest.R.id;
import com.zmobile.foodtest.R.layout;
import com.zmobile.foodtest.R.menu;
import com.zmobile.foodtest.R.string;

import fatsecret.platform.FatSecretAPI;
import fatsecret.platform.FatSecretException;
//import fatsecret.platform.FatSecretAPI.NetworkRequest;
//import fatsecret.platform.FatSecretAPI.OAuthBase;
*/
public class ActivityFavorites extends ActivityTemplate implements OnItemClickListener,
	OnClickListener, Updatable{
	
	// Store a unique message using your package name to avoid conflicts
	// with other apps. This stores the stock symbol I plan on displaying
	public final static String STOCK_SYMBOL = "com.example.myfirstapp.STOCK";
	public final static String TAG = "ActivityFavorites";
	
	public String[] paramNames = {"id", "Title", "Address", "City", "State", "Phone", "Latitude",
		"Longitude", "Distance", "Url", "MapUrl",
		"BusinessUrl"};
	
	String[] array = { "One", "Two", "Three", "Four", "Five", "Six", "Seven",
            "Eight", "Nine", "Ten" };
	
	Map<String,String> parameters;
	
	List<Map<String,String>> _favoritesList;
	SharedPreferences favoritesPrefs;
	MenuHelper menuHelp;
	
	AlertDialog.Builder builder;	
	AlertDialog clearAllDialog;
	AlertDialog delItemDialog;
	String mClickedItemSign;
	int mUnit;
	
	SharedPreferences sharedData;
	String filename = "SharedData";
	
	// "Rating.AverageRating",
	// , "Categories"
	// "LastReviewIntro", 
	
	String consumerKey;				
	String consumerSecret;			
	
	//FatSecretAPI fatSecretApi;
	Favorites favorites;
	PersonInfo personInfo;
	
	AutoCompleteTextView autoCompView;
	/*
	Location lastLoc;
    LocationManager locMan;
    LocationListener locationListener;
    Criteria criteria;
    Set<String> keywords;

	private InterstitialAd interstitial;
	*/
	
	List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	
	// Manages key valued pairs associated with stock symbols
	private SharedPreferences stockSymbolsEntered;
	
	// Table inside the scroll view that holds stock symbols
	// and buttons
	private TableLayout favoritesTable;
	
	// Where the user enters a new stock symbol
	//private EditText stockSymbolEditText;
	
	// Button that enters a new stock and another that
	// deletes all of them
	ImageButton searchButton;
	Button btDelFavor;
	Button bClearAll;
		
	TextView resultsTitleTxt;
	LinearLayout btnParent;
	String[] mTypesList;
	int ads;
	//HandlerPurchase handlerIAP;
	/*
	LocationValet locationValet;
	MMAdView adViewFromXml;
	MMRequest MMrequest;*/
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		adView.pause();
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
        //interstitial.loadAd(adRequest);
		  /*
		  if (ads<3 || handlerIAP.mHasRemovedAds)
			  adView.setVisibility(View.GONE);
		  else
			  adView.loadAd(adRequest);
			  */
	  }

	  @Override
	  public void onStop() {
	    super.onStop();
	    // The rest of your onStop() code.
	    //EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	  }
	
	
	// Set up the activity
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_favorites);
		setUpAdLayouts();
		super.onCreate(savedInstanceState);
		classTAG = this.getClass().getSimpleName();
		mTracker = getDefaultTracker(this);
		ThemeUtils.onActivityCreateSetTheme(this);


		//adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);
		//adViewExpress.setAdSize(AdSize.MEDIUM_RECTANGLE);
		//adViewExpress.setAdUnitId(getString(R.string.ad_unit_id));
		//adViewExpress.loadAd(new AdRequest.Builder().build());

		//facebook native
		faceNativeLayoutId = R.layout.ad_unit_row;
		admobNativeLayoutId = R.layout.ad_app_install;
		//nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container);
		statusText = (TextView) findViewById(R.id.status);
		viewType = NativeAdView.Type.HEIGHT_100;
		//-----

		mTypesList = getResources().getStringArray(R.array.drink_types);
		
		favoritesPrefs = getApplicationContext().getSharedPreferences("Favorites", 0);	
		
		favorites = Favorites.getInstance(this);
				//new Favorites(this);
		
		sharedData = getSharedPreferences(filename, 0);

        // --------------------- Autocomplete -------------------------
        /*
        array = getResources().getStringArray(R.array.foodList);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, array);
        */
	    autoCompView = (AutoCompleteTextView) findViewById(R.id.fav_autocomplete);
	    //autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
	    //autoCompView.setAdapter(adapter);
	    autoCompView.setThreshold(1);
	    autoCompView.setOnItemClickListener(this);
	    //autoCompView.setOnKeyListener(getOnKeyListner); 
	    //autoCompView.setOnEditorActionListener(getOnEditActionListener);
	    autoCompView.addTextChangedListener(getTextWatcher);

		// Initialize Components
		favoritesTable = (TableLayout) findViewById(R.id.fav_tableLayout);

		//adView = (AdView) findViewById(R.id.fav_adView);
		btnParent = (LinearLayout) findViewById(R.id.fav_layoutScroll);
		
		bClearAll = (Button) findViewById(R.id.bClearAllFavs); 
		bClearAll.setOnClickListener(this);
		
		// Add ClickListeners to the buttons
		//searchButton.setOnClickListener(typeFavoriteListener);
		//deleteStocksButton.setOnClickListener(deleteStocksButtonListener);
		
        SharedPreferences settings = getApplicationContext().getSharedPreferences("Settings", 0);
		
		String sex = settings.getString("sex", "Female");
		mUnit = settings.getInt("unit", 0);
		int BMR = settings.getInt("kcal", 2000);
		
		//personInfo = new PersonInfo(this, sex); 
		personInfo = PersonInfo.getInstance(this);
		
		menuHelp = new MenuHelper(this);
		//menuHelp = MenuHelper.getInstance(this);
        
        createDelDiaryDialog();
        showFavorites();
		//fbNativeAds.createAndLoadNativeAd(this, nativeAdContainer, R.layout.ad_unit_no_img);

	}
	
	void createDelDiaryDialog(){
		builder = new AlertDialog.Builder(this);
		// Add the buttons
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User clicked OK button
		        	   //dialog.dismiss();		        	   
		        	   //favorites.delDiaryItem(diary_sign);		        	   
		        	   deleteFavor(mClickedItemSign);      
		        	   showFavorites();
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
			
	public void showFavorites(){
		
		//Map<String,String> params = new HashMap<String,String>();
		
		favoritesTable.removeAllViews();
		
		List<BloodRecord> favList = favorites.getFavoritesList();			
		
		for(BloodRecord drink: favList){
			//updateFavoritesList(drink);
			insertFavoriteInTable(drink, 0);
		}
		/*
		if (nativeAdContainer!=null) {
			ViewGroup parent = ((ViewGroup) nativeAdContainer.getParent());
			parent.removeAllViews();
		}
		favoritesTable.addView(nativeAdContainer);
		*/
		if (favList.size() == 0){
			return;
		}
		//adView.loadAd(adRequest);
			
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
	    setIntent(intent);
	    handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		//displaySearchFromDialog();
		/*
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      displaySearchFromDialog();
	    }*/
	}
	
	// filter out favorites table list with given string
	private void FilterFavorites(String text){
		favoritesTable.removeAllViews();
		for(BloodRecord drink : favorites.getFavoritesList()){
			String name = drink.getmLabel();//item.get("food_name");
			if (name.toLowerCase().contains(text))
				//updateFavoritesList(drii);
				insertFavoriteInTable(drink, 0);
		}
		
	}		
	
	private void insertFavoriteInTable(BloodRecord drink, int arrayIndex){
		//Map<String,String> favoriteItem, int arrayIndex){		

		int id = drink.getId();
		int typeId = drink.getmTypeId();
		int amountId = drink.getmAmountId();
		int amountMl = drink.getmAmount();
		double percent = drink.getmPercent();	
		String drinkId = String.valueOf(id);
		String label = drink.getmLabel();
		String type = mTypesList[typeId];
		//String amount = String.valueOf(amountMl)+" ml";
		String amount = drink.getVolumeStr(mUnit);
		String perc = String.format("%.1f",percent)+"%";
		String details = type+" "+perc+" "+amount;
		int percentInt = (int)(percent*100);
		//String signature = String.format("%d-%d-%.1f", typeId, amountId, percent);
		String signature = String.format("%d-%d-%d", typeId, amountId, percentInt);
		//perc = perc.concat(" %");
		/*
		String desc = String.valueOf(drink.getmAmountId());////favoriteItem.get("food_description");
		String type = String.valueOf(drink.getmTypeId());//favoriteItem.get("food_type");
		//String city = place.get("brand_name");
		String amount = String.valueOf(drink.getmAmount());//favoriteItem.get("food_url");
		//String avgRate = place.get("AvarageRating"); 
		String id = String.valueOf(drink.getId());//favoriteItem.get("food_id");
		String servId = String.valueOf(drink.getmPercent());
		//getfavoriteItem.get("serving_id");
		*/
		// Get the LayoutInflator service
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// Use the inflater to inflate a stock row from stock_quote_row.xml
		View newFavoriteRow = inflater.inflate(R.layout.favor_list_row, null);
		
		// Create the TextView for the ScrollView Row
		TextView tvLabel = (TextView) newFavoriteRow.findViewById(R.id.tvLabel);
		//TextView addressTextView = (TextView) newFavoriteRow.findViewById(R.id.addressListTextView);
		TextView tvDetails = (TextView) newFavoriteRow.findViewById(R.id.tvDetails);
		TextView tvDrinkId = (TextView) newFavoriteRow.findViewById(R.id.idDrink);
		TextView tvDiaryId = (TextView) newFavoriteRow.findViewById(R.id.idDiary);
		ImageView ballImage = (ImageView) newFavoriteRow.findViewById(R.id.favDrinkImg);
		ImageButton btDelFavor = (ImageButton) newFavoriteRow.findViewById(R.id.btDelFavor);
		ImageButton btAddItem = (ImageButton) newFavoriteRow.findViewById(R.id.btAddDiaryItem);
		btDelFavor.setOnClickListener(getDelFavorListener);
		btAddItem.setOnClickListener(getAddDrinkListener);
		//double energy = personInfo.getEnergy(desc);
		
		//desc = personInfo.trimDescription(desc);
		
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
		tvDrinkId.setText(signature); // 1-2-10.5
		tvDiaryId.setText(signature);
		tvDetails.setText(details);
		
		ImageButton favInfoButton = (ImageButton) newFavoriteRow.findViewById(R.id.foodInfoButton);
		favInfoButton.setOnClickListener(getDrinkInfoListener);
		
		btDelFavor.setOnClickListener(getDelFavorListener);
		/*
		Button quoteFromWebButton = (Button) newStockRow.findViewById(R.id.quoteFromWebButton);
		quoteFromWebButton.setOnClickListener(getStockFromWebsiteListener);
		*/
		// Add the new components for the stock to the TableLayout
		arrayIndex = -1;
		favoritesTable.addView(newFavoriteRow, arrayIndex);
				
		
	}
	
	public OnClickListener getDelFavorListener = new OnClickListener(){

		public void onClick(View v) {
			
			// Get the text saved in the TextView next to the clicked button
			// with the id stockSymbolTextView

			// TableRow tableRow = (TableRow) v.getParent().getParent();
			LinearLayout layoutRow = (LinearLayout) v.getParent(); 
            //TextView stockTextView = (TextView) layoutRow.findViewById(R.id.nameListTextView);
            TextView tvDrinkId = (TextView) layoutRow.findViewById(R.id.idDrink);
            TextView tvDiaryId = (TextView) layoutRow.findViewById(R.id.idDiary);
            //Button btDelFavor = (Button) layoutRow.findViewById(R.id.btDelFavor);
            //String stockSymbol = stockTextView.getText().toString();
            String drink_id = tvDrinkId.getText().toString();
            String diary_id = tvDiaryId.getText().toString();    
            
            mClickedItemSign = diary_id; 
            delItemDialog.show();
            //deleteFavor(drink_id);                                         			
		}
		
	};
	
	void deleteFavor(String food_id){
		
		//favorites.delFavorite(personInfo.strToInt(food_id,0)); 
		favorites.delFavorite(food_id); 
		showFavorites();
		
		//String food_id = fullResponse.get(0).get("food_id");		
		//updateFavorButton(false);		
		/*
		if (serv_id.equals("") || serv_id == null)	
			favorites.delFavorite(food_id, null);
		else
			favorites.delFavorite(food_id, serv_id);
		
		SharedPreferences.Editor editor = favoritesPrefs.edit();
		//editor.putBoolean(food_id, true);
		editor.remove(food_id);
		//putString(food_id, currentServingId);
		// Commit the edits!
		editor.commit();				
		
		favoritesTable.removeAllViews();
		synchronized(_favoritesList){
		
		for (Map<String, String> favItem : _favoritesList){
			
			if (food_id.equals(favItem.get("food_id")) && serv_id.equals(favItem.get("serving_id"))){
				_favoritesList.remove(favItem);
			}else{
				updateFavoritesList(favItem);
			}
			
		}
		}*/
	}
	/*
	public OnItemClickListener getOnItemClickListener = new OnItemClickLisetner(){

		@Override
		
	};*/
	
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }  
    
    // handler for typing in the search favorites edit view
	public OnClickListener typeFavoriteListener = new OnClickListener(){			
		
		@Override
		public void onClick(View theView) {
			String text = autoCompView.getText().toString();
			FilterFavorites(text);
			//displaySearchResults();
			
		}
		
	};
	
	private void deleteAllStocks(){
		
		// Delete all the stocks stored in the TableLayout
		favoritesTable.removeAllViews();
		list.removeAll(list);
		
		
	}
	
	public OnClickListener deleteStocksButtonListener = new OnClickListener(){


		public void onClick(View v) {
			
			deleteAllStocks();
			
			autoCompView.setText("");
			
			// Editor is used to store a key / value pairs
			
			SharedPreferences.Editor preferencesEditor = stockSymbolsEntered.edit();
			
			// Here I'm deleting the key / value pairs
			preferencesEditor.clear();
			preferencesEditor.apply();
			
		}
		
	};
	
	
	void deleteFavorite(String food_id){
		
		//favorites.delDiary(personInfo.strToInt(food_id,0));
		favorites.delFavorite(food_id); 
		showFavorites();
	}
	
	TextWatcher getTextWatcher = new TextWatcher(){
		
	
	    public void afterTextChanged(Editable s) 
	    {
	    	String text = s.toString();
			// TODO Auto-generated method stub
			FilterFavorites(text); 
	    }
	    public void beforeTextChanged(CharSequence s, int start, int count, int after) 
	    {
	        /*This method is called to notify you that, within s, the count characters beginning at start are about to be replaced by new text with length after. It is an error to attempt to make changes to s from this callback.*/ 
	    }
	    public void onTextChanged(CharSequence s, int start, int before, int count) 
	    {
	    }
	};
	
	
	public OnClickListener getDrinkInfoListener = new OnClickListener(){

		public void onClick(View v) {
			
			// Get the text saved in the TextView next to the clicked button
			// with the id stockSymbolTextView

			// TableRow tableRow = (TableRow) v.getParent().getParent();
			LinearLayout layoutRow = (LinearLayout) v.getParent(); 
            TextView tvLabel = (TextView) layoutRow.findViewById(R.id.tvLabel);
            TextView tvDrinkId = (TextView) layoutRow.findViewById(R.id.idDrink);
            String label = tvLabel.getText().toString();
            String id = tvDrinkId.getText().toString();                    
            
            // An intent is an object that can be used to start another activity
            Intent intent = new Intent(ActivityFavorites.this, ActivityAddDrink.class);
            
            BloodRecord drink = favorites.getDrinkForId(id, label, personInfo);
            // Add the stock symbol to the intent
            //intent.putExtra(STOCK_SYMBOL, stockSymbol);
            int type = drink.getmTypeId();
            int amount = drink.getmAmountId();
            double percent = drink.getmPercent();
            int alc2 = (int)(drink.getmPercent()*2);
            String name = drink.getmLabel();
            if (drink != null){
	            intent.putExtra("id", id);
	            intent.putExtra("type", type);
	            intent.putExtra("amount", amount);
	            intent.putExtra("percent", percent);
	            intent.putExtra("alc2", alc2);
	            intent.putExtra("name", name);	       	            	            
	            
	            startActivity(intent);
            }
			
		}
		
	};
	
	public OnClickListener getAddDrinkListener = new OnClickListener(){

		public void onClick(View v) {
			
			// Get the text saved in the TextView next to the clicked button
			// with the id stockSymbolTextView

			// TableRow tableRow = (TableRow) v.getParent().getParent();
			LinearLayout layoutRow = (LinearLayout) v.getParent(); 
            TextView tvLabel = (TextView) layoutRow.findViewById(R.id.tvLabel);
            TextView tvDrinkId = (TextView) layoutRow.findViewById(R.id.idDrink);
            String label = tvLabel.getText().toString();
            String id = tvDrinkId.getText().toString();                    
            
            // An intent is an object that can be used to start another activity
            Intent intent = new Intent(ActivityFavorites.this, ActivityAddDrink.class);
            
            
			//FavoriteDrink drink = favorites.getDrinkForId(id, label, personInfo);
			BloodRecord drink = favorites.findFavorite(id, label);
			
			Calendar calendar = Calendar.getInstance();
            long startTime = calendar.getTimeInMillis();			
			long timeSecs = startTime/1000;
			
			if (drink != null){
				drink.setTime(timeSecs);
	                                   
	            //FavoriteDrink diaryDrink = new FavoriteDrink(drink.getmAmount(), drink.getmPercent(), drink.getmTypeId(), drink.getmAmountId(), label, timeSecs);	
				//favorites.addDiary(diaryDrink);
	             
				favorites.addDiary(drink);
				toastMsg(R.string.diary_added); 
			}else{
				toastMsg("Drink not found!"); 
			}
			
		}
		
	};
	
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.favorites, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		menuHelp.handleOnItemSelected(this, item);
		return true;

    } 	

	/*
	private void toastMsg(int stringId){
		
		String msg = getResources().getString(stringId);
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}		

	private void toastMsg(String msg){
		
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
*/
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		favorites.clearFavorites();
		showFavorites();
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
	

	public OnKeyListener getOnKeyListner = new OnKeyListener(){

		@Override
		public boolean onKey(View arg0, int keyCode, KeyEvent event) {
			String text = autoCompView.getText().toString();
			// TODO Auto-generated method stub
			FilterFavorites(text); 
			if (event.getAction() == KeyEvent.ACTION_DOWN){
	            if (keyCode == KeyEvent.KEYCODE_ENTER) {
				
				//Intent intent = new Intent(FoodSearch.this, GetFood.class);
				//startActivity(intent);
				//displaySearchResults();
				}
	            //if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z)
	            //FilterFavorites(text);    
			}
			
			return false;
		}
		
	};
	
	public OnEditorActionListener getOnEditActionListener = new OnEditorActionListener(){

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			String text = autoCompView.getText().toString();
			// TODO Auto-generated method stub
			FilterFavorites(text); 
			
			if (event.getAction() == KeyEvent.ACTION_DOWN){
	            //if (keyCode == KeyEvent.KEYCODE_ENTER) {
				
				//Intent intent = new Intent(FoodSearch.this, GetFood.class);
				//startActivity(intent);
				//displaySearchResults();
				//}
	            //if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z)
	            //FilterFavorites(text);    
			}
			return false;
		}
		
	};*/	
	
	/*
	public void clearSearchOnClick() {
		
		deleteAllStocks();
		
		autoCompView.setText("");
		
		// Editor is used to store a key / value pairs
		
		SharedPreferences.Editor preferencesEditor = stockSymbolsEntered.edit();
		
		// Here I'm deleting the key / value pairs
		preferencesEditor.clear();
		preferencesEditor.apply();
		
	}
	
	public OnClickListener getStockFromWebsiteListener = new OnClickListener(){

		public void onClick(View v) {
			
			// Get the text saved in the TextView next to the clicked button
			// with the id stockSymbolTextView

			//TableRow tableRow = (TableRow) v.getParent();
            //TextView stockTextView = (TextView) tableRow.findViewById(R.id.findViewById(R.id.);
            //String stockSymbol = stockTextView.getText().toString();
            
            // The URL specific for the stock symbol
            //String stockURL = getString(R.string.yahoo_stock_url) + stockSymbol;
            
            //Intent getStockWebPage = new Intent(Intent.ACTION_VIEW, Uri.parse(stockURL));
            
            //startActivity(getStockWebPage);
			
		}
		
	};
	
	
	private FavoriteDrink getFavoriteForId(String strId){
		
        List<FavoriteDrink> favList = favorites.getFavoritesList();
        
        String[] vals = strId.split("-");
        int type = personInfo.strToInt(vals[0], 0);
        int amount = personInfo.strToInt(vals[1], 0);
        double percent = personInfo.strToDbl(vals[2], 5);        
        
        FavoriteDrink drink = new FavoriteDrink(0, percent, type, amount, strId, 0); 
        
        return drink;
        /*FavoriteDrink result;
        
        for (FavoriteDrink drink : favList){
        	int drinkId = drink.getId();
        	if (strId.equals(String.valueOf(drinkId))){
        		return drink;
        	}
        }
        return null;
	}
	*/	

}
