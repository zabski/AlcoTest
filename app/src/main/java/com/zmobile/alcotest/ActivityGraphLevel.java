package com.zmobile.alcotest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.google.ads.AdRequest;
//import com.google.ads.InterstitialAd;
//import com.google.analytics.tracking.android.EasyTracker;
//import com.ironsource.mobilcore.MobileCore;
import com.jjoe64.graphview.GraphView.*;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.*;

/*
import com.zmobile.foodtest.Favorites.UpdateWeightRequest;

import fatsecret.platform.FatSecretAPI;
import fatsecret.platform.FatSecretAuth;
import fatsecret.platform.FatSecretException;
*/
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint.Align;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.*;

//import com.mobfox.sdk.Banner;
//import com.mobfox.sdk.BannerListener;

public class ActivityGraphLevel extends ActivityTemplate implements OnClickListener,
	OnChronometerTickListener, Updatable {
	
	ImageButton bPrevMonth;
	ImageButton bNextMonth;
	TextView tvMonth;
	Button bUnit;
	TextView tvBAC;
	TextView tvLastBAC;	
	LinearLayout graph1;
	LinearLayout graph2;
	
	DigitalClock clock;
	Chronometer chrono;		
	/*
	FatSecretAPI fatSecretApi;
	FatSecretAuth fatSecretAuth;
	*/
	SharedPreferences sharedData;
	String filename = "Settings";
	
	public Favorites favorites;	
	PersonInfo nutrInfo;
	
	String consumerKey;				
	String consumerSecret;	
	
	Map<String,String> parameters;
	List<Map<String,String>> parsedArray;
	
	// The Fragment that will be added to the main Activity
	
	//SimpleFragment simpleFragment;
	MenuHelper menuHelp;
	
	boolean lbsMode = true;
	long currentDate;
	long today;
	int mHeight;
	int ads;
	//HandlerPurchase handlerIAP;
	//private Banner mobfoxBanner;
	
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
	    //interstitial.loadAd(adRequest);
		/*
		if (ads==0 || handlerIAP.mHasRemovedAds)
			adView.setVisibility(View.GONE);
		else
			adView.loadAd(adRequest);
			*/
	    // 3354423372
	}

	@Override
	public void onStop() {
	    super.onStop();
	    // The rest of your onStop() code.
	    //EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	}	
	
	  public void onBackPressed() {

		super.onBackPressed();
	  }	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_graph_view);
		setUpAdLayouts();
		super.onCreate(savedInstanceState);
		classTAG = this.getClass().getSimpleName();
		mTracker = getDefaultTracker(this);
		ThemeUtils.onActivityCreateSetTheme(this);

		sharedData = getSharedPreferences(filename, 0);
		/*
		adView = (AdView) findViewById(R.id.chart_adView);
		nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container);
		adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);*/

		/* mobFox
		mobfoxBanner = (Banner) findViewById(R.id.mobfox_banner);
		String mobfoxHash = getString(R.string.mobfox_hash);
		mobfoxBanner.setInventoryHash(mobfoxHash);

		mobfoxBanner.load();
		*/
		final Activity self = this;

		//adRequest = new AdRequest();		
		//interstitial = new InterstitialAd(this, "ca-app-pub-4402674240600002/");
		//MobileCore.getSlider().setContentViewWithSlider(this, R.layout.activity_graph_view, R.raw.slider_1);
		
		// The FragmentManager provides methods for interacting
		// with Fragments in this Activity		
		FragmentManager fragmentManager = getFragmentManager();
		
		// The FragmentTransaction adds, removes, replaces and 
		// defines animations for Fragments
		
		// beginTransaction() is used to begin any edits of Fragments		
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		// Create our Fragment object		
		SimpleFragment simpleFragment = new SimpleFragment();
		
		// Add the Fragment to the Activity		
		fragmentTransaction.add(R.id.simplefragment, simpleFragment);
		
		// Schedules for the addition of the Fragment to occur		
		fragmentTransaction.commit();
		/*
        bPrevMonth = (ImageButton) findViewById(R.id.bPrevMonth);
        bNextMonth = (ImageButton) findViewById(R.id.bNextMonth);
        tvMonth = (TextView) findViewById(R.id.tvMonth);
        */
        View v = simpleFragment.getView();
        bUnit = (Button) findViewById(R.id.bUnit);
        graph1 = (LinearLayout) findViewById(R.id.graph1);
        graph2 = (LinearLayout) findViewById(R.id.graph2);
        /*
        clock = (DigitalClock) v.findViewById(R.id.digitalClock);
        chrono = (Chronometer) v.findViewById(R.id.chronometer);	      
        
        tvBAC = (TextView) findViewById(R.id.tvBAC);
        tvLastBAC = (TextView) findViewById(R.id.tvLastBAC);        
        
        bPrevMonth.setOnClickListener(this);
        bNextMonth.setOnClickListener(this);
        */
        bUnit.setOnClickListener(this);
        
        //chrono.setOnChronometerTickListener(this);
                
		/*
		consumerKey = getString(R.string.consumerKey);				
		consumerSecret = getString(R.string.consumerSecret);
				
		try{
			fatSecretApi = new FatSecretAPI(consumerKey,consumerSecret);
		
			favorites = new Favorites(this, fatSecretApi);		
		
			fatSecretAuth = favorites.getFatSecretAuth();
		}catch(Exception e){
			String err = e.toString();
			//String no_internet = getResources().getString(R.string.no_internet);
			//Toast.makeText(this, no_internet, Toast.LENGTH_SHORT).show();
			toastMsg(R.string.no_internet);
		}
		*/
		today = Calendar.getInstance().getTimeInMillis()/(1000*60*60*24);
		
		SharedPreferences settings = getApplicationContext().getSharedPreferences("Settings", 0);		
		
		int alc = settings.getInt("alc", 5);
		int lbs = settings.getInt("lbs", 150);
		String sex = settings.getString("sex", "Female");	
		boolean sexMale = true;
		if (sex.equals("Female"))
			sexMale = false;
		
		favorites = Favorites.getInstance(this);
		//new Favorites(this, sexMale, lbs, (double)alc/100);
		
		//nutrInfo = new PersonInfo(this, sex);
		nutrInfo = PersonInfo.getInstance(this);
		
		mHeight = settings.getInt("height", 160);
		
		menuHelp = new MenuHelper(this);
		//menuHelp = MenuHelper.getInstance(this);
		/*
		*/
		drawGraph();
	}
	
	void drawGraph(){
		//List<Map<String,String>> weightMonth, long from_date_int){
		
		final SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
		final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd");
		final SimpleDateFormat yearFormat = new SimpleDateFormat("MMM yyyy");
		CustomLabelFormatter formater = new CustomLabelFormatter() {
			@Override
			public String formatLabel(double value, boolean isValueX) {
				if (isValueX) {
					Date d = new Date((long) value);
					return hourFormat.format(d);
				}
				return null; // let graphview generate Y-axis label for us
			}
		};		
		CustomLabelFormatter dateFormater = new CustomLabelFormatter() {
			@Override
			public String formatLabel(double value, boolean isValueX) {
				if (isValueX) {
					Date d = new Date((long) value);
					return dateFormat.format(d);
				}
				return null; // let graphview generate Y-axis label for us
			}
		};
		CustomLabelFormatter yearFormater = new CustomLabelFormatter() {
			@Override
			public String formatLabel(double value, boolean isValueX) {
				if (isValueX) {
					Date d = new Date((long) value);
					return yearFormat.format(d);
				}
				return null; // let graphview generate Y-axis label for us
			}
		}; 
		
		int num = 41;
		
		if (num == 0){
			String no_data = getResources().getString(R.string.no_data);
			Toast.makeText(this, no_data, Toast.LENGTH_SHORT).show();
			if (currentDate > today)
				currentDate = today;
			return;
		}
		double min = 1000;
		double max = 0;
		double minB = 1000;
		double maxB = 0;
		double range = Math.min(num, 9);
		int labelsNum = Math.min(num, 11); 
		int textSize = (int) getResources().getDimension(R.dimen.text_xs);
		long nowHour = Calendar.getInstance().getTimeInMillis()/(1000*60*60);
		GraphViewData[] data = new GraphViewData[num];
		GraphViewData[] data2 = new GraphViewData[num];
		GraphViewData[] bmi = new GraphViewData[num];
		int nr = 0;
		//for(Map<String,String> day: weightMonth){
		for(double hour=nowHour-3; hour<=nowHour+7; hour+=0.25){
			//int date = Integer.parseInt(day.get("date_int"));//-from_date_int;
			double bac = 10*favorites.calcLastBAC((long)(hour*60*60));
			/*
			String date_str = day.get("date_int");
			long date_long = Long.parseLong(date_str);
			double date_dbl = Double.parseDouble(date_str);					
			long date = 1000*60*60*24*date_long;//-from_date_int;
			double weightKg = Double.parseDouble(day.get("weight_kg"));			
			double weight = weightKg;			
			if (lbsMode == true){
				weight = nutrInfo.kgToLbs(weightKg);
			}*/
			data[nr] = new GraphViewData(hour*60*60*1000,bac);
			data2[nr] = new GraphViewData(hour*60*60*1000,bac);
			double valY = data[nr].getY(); 
			//bmi[nr] = new GraphViewData(date, nutrInfo.calcBMI(mHeight,weightKg));//valY/Math.pow(mHeight,2));
			bmi[nr] = new GraphViewData(hour*60*60*1000,favorites.getLegalAlc()*10);
			double valB = bmi[nr].getY(); 
			if (valY < min) min = floor100(valY);//(int)(Math.floor(valY*10)/10);
			if (valY > max) max = ceil100(valY);//(int)(Math.ceil(valY*10)/10);
			if (valB < minB) minB = floor100(valB);//(int)(Math.floor(valB*10)/10);
			if (valB > maxB) maxB = ceil100(valB);//(int)(Math.ceil(valB*10)/10);
			nr++;
		}
		/*
		if (min == max){
			min = (int) (Math.floor(min-1));
			max = (int) (Math.ceil(max+1));
		}
		if (minB == maxB){
			minB = (int) (Math.floor(minB-1));
			maxB = (int) (Math.ceil(maxB+1));
		}*/
		/*
		double v=0;
		for (int i=0; i<num; i++) {
			v += 0.2;
			data[i] = new GraphViewData(i, 70+10*Math.sin(Math.random()*v));			
			double valY = data[i].getY(); 
			bmi[i] = new GraphViewData(i, valY/Math.pow(1.75,2));
			double valB = bmi[i].getY(); 
			if (valY < min) min = (int)(Math.floor(valY/5))*5;
			if (valY > max) max = (int)(Math.ceil(valY/5))*5;
			if (valB < minB) minB = (int)(Math.floor(valB/5))*5;
			if (valB > maxB) maxB = (int)(Math.ceil(valB/5))*5;
		}
		/*
		 * date as label formatter
		 */
		String month = yearFormater.formatLabel(data[0].getX(), true);
		//tvMonth.setText(month);
		String currentMonth = "("+month+")";
		String label = getResources().getString(R.string.bac_graph);
		
		if (lbsMode == true)
			label = label.concat(" ( ‰ )");
		else
			label = label.concat(" ( ‰ )");
			
		GraphView graphView = new LineGraphView(this, label);
		((LineGraphView) graphView).setDrawDataPoints(false);
		((LineGraphView) graphView).setDataPointsRadius(3f);
		
		String bac_graph = getResources().getString(R.string.bac_graph);	
		GraphView bmiGraphView = new BarGraphView(this, label);
		GraphViewSeriesStyle seriesStyleBMR = new GraphViewSeriesStyle();
		seriesStyleBMR.color = Color.RED;
		
		graphView.setCustomLabelFormatter(formater);
		//String legal_level = getResources().getString(R.string.legal_level);
		// add data
		graphView.addSeries(new GraphViewSeries(data));
		graphView.addSeries(new GraphViewSeries("legal level", seriesStyleBMR, bmi));
		// set view port, start=2, size=10
		//graphView.setViewPort(1, range);
		//graphView.setScalable(true);
		graphView.setScrollable(true);
		
		graphView.getGraphViewStyle().setGridColor(Color.GRAY);
		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.BLACK);
		graphView.getGraphViewStyle().setTextSize(textSize);
		int verticalLabelsNum = (int)((0.01+max-min)*100);
		//verticalLabelsNum = Math.min(verticalLabelsNum, 10);
		if (verticalLabelsNum>20) {
			int max20 = (int) Math.ceil(max * 100 / 20);
			verticalLabelsNum = max20 + 1;
			double maxY = (double) max20 * 20 / 100;
			graphView.setManualYAxisBounds(maxY, 0);
			bmiGraphView.setManualYAxisBounds(maxY, 0);
		}else if (verticalLabelsNum>10){
			int max10 = (int)Math.ceil(max*100/10);
			verticalLabelsNum = max10+1;
			double maxY = (double)max10*10/100;
			graphView.setManualYAxisBounds(maxY, 0);
			bmiGraphView.setManualYAxisBounds(maxY, 0);
		}else if (verticalLabelsNum>5){
			int max5 = (int)Math.ceil(max*100/5);
			verticalLabelsNum = max5+1;
			double maxY = (double)max5*5/100;
			graphView.setManualYAxisBounds(maxY, 0);			
			bmiGraphView.setManualYAxisBounds(maxY, 0);
		}else{
			graphView.setManualYAxisBounds(max, 0);
			bmiGraphView.setManualYAxisBounds(max, 0);
		}
		//graphView.setManualYAxisBounds(0.5, 0);
		//graphView.setManualYAxis(true);
		graphView.getGraphViewStyle().setNumVerticalLabels(verticalLabelsNum);
		graphView.getGraphViewStyle().setNumHorizontalLabels(labelsNum);
		graphView.getGraphViewStyle().setVerticalLabelsWidth(50);
		graphView.getGraphViewStyle().setVerticalLabelsAlign(Align.CENTER);
		// set manual Y axis bounds
		//graphView.setManualYAxisBounds(Math.ceil(max), Math.floor(min));
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
		layout.removeAllViews();
		layout.addView(graphView);
		/*
		layout = (LinearLayout) findViewById(R.id.graph2);
		layout.addView(graphView);
		*/			
		
		GraphViewSeriesStyle seriesStyle = new GraphViewSeriesStyle();
		seriesStyle.setValueDependentColor(new ValueDependentColor() {
			@Override
			public int get(GraphViewDataInterface data) {
				// the higher the more red
				return GetColor(data.getY()/10);
						//Color.rgb((int)(150+((data.getY()/3)*100)), (int)(150-((data.getY()/3)*150)), (int)(150-((data.getY()/3)*150)));
			}
		});
		
		// add data
		bmiGraphView.addSeries(new GraphViewSeries("aaa", seriesStyle, data2)); 
		//bmiGraphView.addSeries(new GraphViewSeries(bmi));
		bmiGraphView.setCustomLabelFormatter(formater);
		// set view port, start=2, size=10
		//bmiGraphView.setViewPort(1, range);
		//bmiGraphView.setScalable(true);
		bmiGraphView.setScrollable(false);
		bmiGraphView.getGraphViewStyle().setGridColor(Color.GRAY);
		bmiGraphView.getGraphViewStyle().setTextSize(textSize);
		bmiGraphView.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);
		bmiGraphView.getGraphViewStyle().setHorizontalLabelsColor(Color.BLACK);
		bmiGraphView.getGraphViewStyle().setNumVerticalLabels(verticalLabelsNum);
		bmiGraphView.getGraphViewStyle().setNumHorizontalLabels(labelsNum);
		bmiGraphView.getGraphViewStyle().setVerticalLabelsWidth(50);
		bmiGraphView.getGraphViewStyle().setVerticalLabelsAlign(Align.CENTER);
		// set manual Y axis bounds
		//bmiGraphView.setManualYAxisBounds(Math.ceil(maxB), Math.floor(minB));
		
		LinearLayout layout2 = (LinearLayout) findViewById(R.id.graph2);
		layout2.removeAllViews();
		layout2.addView(bmiGraphView);
		
		//adView.loadAd(adRequest);  
		//adView.setVisibility(View.VISIBLE);
	}
	
	double ceil100(double x){		
		return Math.ceil(x*100)/100;
	}
	
	double floor100(double x){		
		return Math.floor(x*100)/100;
	}
	
	/*
	void getWeight(long date){
		// current_weight_kg
		Map<String,String> params = new HashMap<String,String>();
		//String token = fatSecretAuth.getToken();
		//String secret = fatSecretAuth.getSecret();
		
		//params.put("current_weight_kg", String.valueOf(weight));
		if (date > 0)
			params.put("date", String.valueOf(date));
		//params.put("oauth_token", token);
		
		try{
			
			Object[] urlParams = fatSecretApi.GetProfileUrl("weights.get_month", params, token, secret);
			//FoodSearchRequest request = new FoodSearchRequest();

			String url = (String)urlParams[0];
			String reqMethod = (String)urlParams[1];
			String paramsSigned = (String)urlParams[2];
			//Map<String,String> 
			parameters = (Map<String,String>)urlParams[3];
			
			//new GetWeightRequest().execute(url, reqMethod, paramsSigned);
		
		}catch(Exception e){
			
		}
	}
	
	
	private class GetWeightRequest extends AsyncTask<String, String, String> {
		
		
	    protected void onPostExecute(String response) {
	        // TODO: check this.exception 
	        // TODO: do something with the feed
	    	List<String> rootPath = new ArrayList<String>();
	    	
	    	rootPath.add("month");
	    	//rootPath.add("day");
	    	
	    	String[] itemNames = {"date_int", "weight_kg"};
	    	//{"brand_name",
	    	//<String, String, String>
	    	ParseJSON parser = new ParseJSON(response, rootPath, itemNames);
	    	
	    	parsedArray = parser.getArrayResponse("day");
	    	
	    	String[] dateItems = {"from_date_int", "to_date_int"};
	    	//{"brand_name",
	    	//<String, String, String>
	    	ParseJSON parser2 = new ParseJSON(response, rootPath, dateItems);
	    	
	    	//parsedArray = parser.getArrayResponse("serving");
	    	Map<String,String> dates = parser2.getResponse();
	    	
	    	currentDate = nutrInfo.strToLong(dates.get("from_date_int"), today);
	    	//Integer.valueOf(dates.get("from_date_int")); 
	    	
	    	if (parsedArray.size() > 0)
	    		drawGraph(parsedArray, currentDate);
	    	else{    	
		    	//String no_data = getResources().getString(R.string.no_data);
				//Toast.makeText(ActivityGraphLevel.this, no_data, Toast.LENGTH_SHORT).show();
				toastMsg(R.string.no_data);
				//finish();
	    	}			
			
			//String retUrl = urlBase + "&" + result.getSignature();
	    }

		//protected String doInBackground(String url, String method, String paramsStr, Map<String,String> params) {
	    
	    protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// If use POST, must use this
			String response = "";
			String urlStr = (String)params[0];	
			String requestMethod = (String)params[1];
			String paramStr = (String)params[2];
			//Map<String, String> header = params;//(Map<String, String>)params[3];
			//HttpURLConnection conn = (HttpURLConnection)params[0];				
			//String paramStr = (String)params[2];
			//StringBuffer sb = new StringBuffer();
			
			try {
				response = fatSecretApi.doHttpMethodReq(urlStr, requestMethod, paramStr, parameters);
			} catch (FatSecretException e) {
				// TODO Auto-generated catch block
				String err = e.toString();
				e.printStackTrace();
			}

			return response;
		}

	}*/
	
	int GetColor(double bmi) {
		float[] hsv = new float[3];
		int color = Color.GRAY;		
		Color.colorToHSV(color, hsv);
		//hsv[parameter] = hsv[parameter]/factor;
		//hsv[0] = (float)(-bmi/10 + 7.5)/92*360+360;
		//hsv[0] = (float)(300/((bmi-0.5)/7)-23);
		double legal = favorites.getLegalAlc();
		hsv[0] = (float)(120.0-bmi*60.0/legal);
		hsv[1] = 1;
		hsv[2] = 0.75f;
		int newCol = Color.HSVToColor(hsv);
		return newCol;
	}

	@Override
	public void onClick(View button) {
		// TODO Auto-generated method stub
		switch(button.getId()){

		case R.id.bUnit:
			if (lbsMode == true){
				lbsMode = false;
				String line = getResources().getString(R.string.line_chart);
				bUnit.setText(line);
				graph1.setVisibility(View.GONE);
				graph2.setVisibility(View.VISIBLE);
			}else{
				lbsMode = true;
				String bar = getResources().getString(R.string.bar_chart);
				bUnit.setText(bar);
				graph2.setVisibility(View.GONE);
				graph1.setVisibility(View.VISIBLE);
			}
			//drawGraph(parsedArray, currentDate);
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chart, menu);
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
	*/
	@Override
	public void onChronometerTick(Chronometer chronometer) {
		// TODO Auto-generated method stub
		double bac = favorites.calcTotalBacNow();
		double lastBac = favorites.calcLastBacNow();
		double lastBACpromile = lastBac*10;
		double lastBACrest = (lastBACpromile*100-Math.ceil(lastBACpromile*100))*100;
		
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
