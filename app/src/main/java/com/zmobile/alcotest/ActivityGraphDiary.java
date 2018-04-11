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
import com.google.android.gms.ads.*;

//import com.zmobile.foodtest.Favorites.UpdateWeightRequest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityGraphDiary extends ActivityTemplate implements OnClickListener, Updatable {
	
	MenuHelper menuHelp;
	ImageButton bPrevMonth;
	ImageButton bNextMonth;
	TextView tvMonth;
	TextView tvTotal;
	TextView tvAvg;
	TextView tvNumberDays;
	TextView bUnit;
	TextView tvStats;
	SharedPreferences sharedData;
	String filename = "SharedData";
	
	Favorites favorites;
	PersonInfo nutrInfo;
	
	String consumerKey;				
	String consumerSecret;	
	
	Map<String,String> parameters;
	Calendar cal;
	LinearLayout graph1;
	LinearLayout graph2;
	
	long currentDate;
	long today;
	long todayHrs;
	int BMR;
	boolean chartMode = false;
	int ads;
	//HandlerPurchase handlerIAP;
	
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
		adView.resume();
	}	
	  
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//removeAdView();    
		adView.destroy();
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
		setContentView(R.layout.activity_diary_graph);
		setUpAdLayouts();
		super.onCreate(savedInstanceState);
		classTAG = this.getClass().getSimpleName();
		mTracker = getDefaultTracker(this);
		ThemeUtils.onActivityCreateSetTheme(this);

		//adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);
		//adView = (AdView) findViewById(R.id.month_adView);
		faceNativeLayoutId = R.layout.ad_unit_row;

        sharedData = getSharedPreferences(filename, 0);
		//adRequest = new AdRequest();		
		//interstitial = new InterstitialAd(this, "ca-app-pub-4402674240600002/4272753379");
		//MobileCore.getSlider().setContentViewWithSlider(this, R.layout.activity_graph_view, R.raw.slider_1);
		
        bPrevMonth = (ImageButton) findViewById(R.id.btPrevMonth);
        bNextMonth = (ImageButton) findViewById(R.id.btNextMonth);
        tvMonth = (TextView) findViewById(R.id.txtMonth);
        tvStats = (TextView) findViewById(R.id.tvStats);
        tvTotal = (TextView) findViewById(R.id.tvTotalCons);
        tvAvg = (TextView) findViewById(R.id.tvAvgCons);
        tvNumberDays = (TextView) findViewById(R.id.tvNumberOfDays);

        graph1 = (LinearLayout) findViewById(R.id.graph1);
        graph2 = (LinearLayout) findViewById(R.id.graph2);
        bUnit = (Button) findViewById(R.id.bUnit2);
        
        bPrevMonth.setOnClickListener(this);
        bNextMonth.setOnClickListener(this);		
        bUnit.setOnClickListener(this);
		
        cal = Calendar.getInstance();
                		
		currentDate = cal.getTimeInMillis()/(1000*60*60*24);
		
		todayHrs = Calendar.getInstance().getTimeInMillis()/(1000*60*60);
		
		SharedPreferences settings = getApplicationContext().getSharedPreferences("Settings", 0);		
		
		int alc = settings.getInt("alc", 2);
		int lbs = settings.getInt("lbs", 150);
		String sex = settings.getString("sex", "Female");	
		boolean sexMale = true;
		if (sex.equals("Female"))
			sexMale = false;
		
		favorites = Favorites.getInstance(this);
				//new Favorites(this, sexMale, lbs, alc/100);
		
		//nutrInfo = new PersonInfo(this, sex);
		nutrInfo = PersonInfo.getInstance(this);
		menuHelp = new MenuHelper(this);
		//menuHelp = MenuHelper.getInstance(this);
		
		getMonth(currentDate);
				
	}
	
	void drawGraph(List<BloodRecord> weightMonth, long first, long last){	
		
		HashMap<Long,Double> dailyAmount = new HashMap<Long, Double>(); 
		
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
		final SimpleDateFormat yearFormat = new SimpleDateFormat("MMMMM yyyy");
		CustomLabelFormatter formater = new CustomLabelFormatter() {
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
		
		long firstDay = first/(60*60*24);
		long lastDay = last/(60*60*24);
		long daysInMonth = lastDay - firstDay;
		
		int size = weightMonth.size();
		int drinkDays = 0;
		
		if (size == 0){
			String no_data = getResources().getString(R.string.no_data);
			//Toast.makeText(this, no_data, Toast.LENGTH_LONG).show();
			tvStats.setText(no_data);
			toastMsg(R.string.no_data);
			if (currentDate > today)
				currentDate = today;
			return;
		}
		
		int min = 0;//10000;
		int max = 2;
		int minB = 0;//10000;
		int maxB = 2;
		int range = Math.min(size, 9);
		int labelsNum = 10;//Math.min(size, 10);
		int textSize = (int) getResources().getDimension(R.dimen.text_xs);
		
		//Map<Long, Double> data = new HashMap<Long, Double>();
		List<Long> axisX = new ArrayList<Long>();
		List<Double> axisY = new ArrayList<Double>();
		
		/*
		GraphViewData[] bmr = new GraphViewData[num]; 
		GraphViewData[] carbohydrate = new GraphViewData[num];
		GraphViewData[] protein = new GraphViewData[num];
		GraphViewData[] fat = new GraphViewData[num];
		*/
		int nr = 0;
		double prev = 0;
		for(BloodRecord drink: weightMonth){
			//int date = Integer.parseInt(day.get("date_int"));//-from_date_int;
			//String date_str = cal.setTimeInMillis(drink.getTime()*1000);
			//long date_long = nutrInfo.strToLong(date_str, currentDate);//Long.parseLong(date_str);
			//double date_dbl = Double.parseDouble(date_str);					
			long timeSecs = drink.getTime();
			long timeMil = drink.getTime()*1000;
			long date = timeSecs/(60*60*24);
			double alcUnits = drink.getPureAlcMl()/10;
			if (dailyAmount.containsKey(date))
				prev = dailyAmount.get(date);
			else if (date >= firstDay & date <= lastDay){
				drinkDays++;
			}
			dailyAmount.put(date, prev+alcUnits);
		}
		double monthTotal = 0;
		double dailyTotal = 0;
		//for(Long date: dailyAmount.keySet()){
		for(long date = firstDay; date<=lastDay; date++){
			if (dailyAmount.containsKey(date))
				dailyTotal = dailyAmount.get(date);
			else
				dailyTotal = 0;
			monthTotal += dailyTotal;
			long timeMil = date*24*60*60*1000;
			//calories[nr] = new GraphViewData(timeMil, (double)dailyTotal);
			double valY = (double)dailyTotal; // calories[nr].getY(); 
			axisX.add(timeMil);
			axisY.add((double)dailyTotal);
			//data.put(timeMil, (double)dailyTotal);
			/*
			bmr[nr] = new GraphViewData(date, BMR);
			carbohydrate[nr] = new GraphViewData(date, todayCarbs);
			protein[nr] = new GraphViewData(date, todayProtein);
			fat[nr] = new GraphViewData(date, todayFat);
			*/
			min = (int)Math.floor(Math.min(BMR, Math.min(min, dailyTotal)));
			max = (int)Math.ceil(Math.max(BMR, Math.max(max, dailyTotal))); 
			minB = min;//(int)Math.floor(Math.min(Math.min(minB, todayCarbs), Math.min(todayProtein, todayFat)));
			maxB = max;//(int)Math.ceil(Math.max(Math.max(maxB, todayCarbs), Math.max(todayProtein, todayFat)));
			/*
			double valB = carbohydrate[nr].getY(); 
			if (valY < min) min = (int)(Math.floor(valY/5))*5;
			if (valY > max) max = (int)(Math.ceil(valY/5))*5;
			if (valB < minB) minB = (int)(Math.floor(valB/5))*5;
			if (valB > maxB) maxB = (int)(Math.ceil(valB/5))*5;*/
			nr++;
		}
		double monthAvg = monthTotal/daysInMonth;
		GraphViewData[] calories = new GraphViewData[nr];
		for(int i=0; i<nr; i++)
			calories[i] = new GraphViewData(axisX.get(i), axisY.get(i));
		/*
		min = (int) (Math.floor(min/5)*5);
		max = (int) (Math.ceil(max/5)*5);
		minB = (int) (Math.floor(minB/5)*5);
		maxB = (int) (Math.ceil(maxB/5)*5);
		*/
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
		String month = yearFormater.formatLabel(calories[0].getX(), true);
		tvMonth.setText(month);
		String currentMonth = "("+month+")";
		String bac_label = getResources().getString(R.string.consumption);
		String total = getResources().getString(R.string.total_consumption);
		String avg = getResources().getString(R.string.avg_consumption);
		String numDays = getResources().getString(R.string.num_days);
		/*
		tvTotal.setText(total+"\n"+String.format("%.2f", monthTotal));
		tvAvg.setText(avg+"\n"+String.format("%.2f",monthAvg));
		tvNumDays.setText(numDays+"\n"+dailyAmount.size());
		*/
		tvTotal.setText(String.format("%.2f", monthTotal));
		tvAvg.setText(String.format("%.2f",monthAvg));
		//int daysNum = dailyAmount.size();
		tvNumberDays.setText(String.format("%d",drinkDays));
		
		
		GraphView graphView = new LineGraphView(this, bac_label);
		GraphView intakeGraphView = new BarGraphView(this, bac_label);
		
		//((LineGraphView) graphView).setDrawDataPoints(true);
		//((LineGraphView) graphView).setDataPointsRadius(5f);		
		graphView.setCustomLabelFormatter(formater);
		
		// set view port, start=2, size=10
		//graphView.setViewPort(1, range);
		//graphView.setScalable(true);
		graphView.setScrollable(true);
		graphView.getGraphViewStyle().setLegendSpacing(20);
		graphView.getGraphViewStyle().setLegendWidth(20);
		graphView.getGraphViewStyle().setLegendMarginBottom(20);
		graphView.getGraphViewStyle().setLegendBorder(20);
		
		graphView.getGraphViewStyle().setTextSize(textSize);
		graphView.getGraphViewStyle().setGridColor(Color.GRAY);
		int verticalLabelsNum = (int)(1+(max-min));
		//verticalLabelsNum = Math.min(verticalLabelsNum, 10);
		if (verticalLabelsNum>10){
			int max5 = (int)Math.ceil(max/5);
			double m = max5+1;
			verticalLabelsNum = (int)m+1;
			graphView.setManualYAxisBounds(m*5, 0);
			intakeGraphView.setManualYAxisBounds(m*5, 0);
		}else{
			graphView.setManualYAxisBounds(max, 0);
			intakeGraphView.setManualYAxisBounds(maxB, 0);
		}
		graphView.getGraphViewStyle().setNumVerticalLabels(verticalLabelsNum);
		
		graphView.getGraphViewStyle().setNumHorizontalLabels(labelsNum);
		graphView.getGraphViewStyle().setVerticalLabelsWidth(30);
		graphView.getGraphViewStyle().setVerticalLabelsAlign(Align.CENTER);
		// set manual Y axis bounds
				
		// set legend
		graphView.setShowLegend(false);
		graphView.setLegendAlign(LegendAlign.BOTTOM);
		graphView.getGraphViewStyle().setLegendBorder(20);
		graphView.getGraphViewStyle().setLegendSpacing(30);
		graphView.getGraphViewStyle().setLegendWidth(300);
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
		layout.removeAllViews();
		layout.addView(graphView);
		/*
		layout = (LinearLayout) findViewById(R.id.graph2);
		layout.addView(graphView);
		*/		
		//String consumption = getResources().getString(R.string.consumption);
		
		GraphViewSeriesStyle seriesStyle = new GraphViewSeriesStyle();
		GraphViewSeriesStyle seriesStyleBMR = new GraphViewSeriesStyle();
		GraphViewSeriesStyle seriesStyleCarbo = new GraphViewSeriesStyle();
		GraphViewSeriesStyle seriesStyleProtein = new GraphViewSeriesStyle();
		GraphViewSeriesStyle seriesStyleFat = new GraphViewSeriesStyle();
		seriesStyleBMR.color = Color.GREEN;
		seriesStyleCarbo.color = Color.BLUE;
		seriesStyleProtein.color = Color.MAGENTA;
		seriesStyleFat.color = Color.RED;
		seriesStyle.setValueDependentColor(new ValueDependentColor() {
			@Override
			public int get(GraphViewDataInterface data) {
				// the higher the more red
				return nutrInfo.getColor(data.getY());
						//Color.rgb((int)(150+((data.getY()/3)*100)), (int)(150-((data.getY()/3)*150)), (int)(150-((data.getY()/3)*150)));
			}
		});
		// add data
		String consumption = getResources().getString(R.string.consumption);
		//String your_rdi = getResources().getString(R.string.your_rdi);
		graphView.addSeries(new GraphViewSeries(consumption, seriesStyle, calories));
		
		//graphView.addSeries(new GraphViewSeries(your_rdi, seriesStyleBMR, bmr));
		
		// add data
		
		intakeGraphView.addSeries(new GraphViewSeries("carbohydrate", seriesStyleCarbo, calories));
		/*
		intakeGraphView.addSeries(new GraphViewSeries("protein", seriesStyleProtein, protein));
		intakeGraphView.addSeries(new GraphViewSeries("fat", seriesStyleFat, fat));
		*/
		intakeGraphView.setCustomLabelFormatter(formater);
		
		// set view port, start=2, size=10
		//bmiGraphView.setViewPort(1, range);
		//bmiGraphView.setScalable(true);
		intakeGraphView.setScrollable(false);
		intakeGraphView.getGraphViewStyle().setTextSize(textSize);
		intakeGraphView.getGraphViewStyle().setGridColor(Color.GRAY);
		/*
		if (verticalLabelsNum>10){
			int max5 = (int)Math.ceil(maxB/5);
			verticalLabelsNum = max5+1;
			intakeGraphView.setManualYAxisBounds(max5*5, 0);
		}else{
			intakeGraphView.setManualYAxisBounds(maxB, 0);	
		}*/
		intakeGraphView.getGraphViewStyle().setNumVerticalLabels(verticalLabelsNum);
		intakeGraphView.getGraphViewStyle().setNumHorizontalLabels(labelsNum);
		// set legend
		intakeGraphView.setShowLegend(false);
		intakeGraphView.setLegendAlign(LegendAlign.BOTTOM);
		intakeGraphView.getGraphViewStyle().setLegendBorder(20);
		intakeGraphView.getGraphViewStyle().setLegendSpacing(30);
		intakeGraphView.getGraphViewStyle().setLegendWidth(300);
		intakeGraphView.getGraphViewStyle().setVerticalLabelsWidth(30);
		intakeGraphView.getGraphViewStyle().setVerticalLabelsAlign(Align.CENTER);
		// set manual Y axis bounds
		
		LinearLayout layout2 = (LinearLayout) findViewById(R.id.graph2);
		layout2.removeAllViews();
		layout2.addView(intakeGraphView);
		
		//adView.loadAd(adRequest);  
		//adView.setVisibility(View.VISIBLE);
	}
	
	void getMonth(long date){
		// current_weight_kg
		Map<String,String> params = new HashMap<String,String>();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date*24*60*60*1000);
		
		try{
			
			favorites.getHistoryList();
			
			//todayHrs
			
			int min = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
	        calendar.set(Calendar.DAY_OF_MONTH, min);
	        long first = calendar.getTimeInMillis()/1000;
	        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	        calendar.set(Calendar.DAY_OF_MONTH, max);
			long last = calendar.getTimeInMillis()/1000;
			
			List<BloodRecord> month = favorites.getHistoryforDates(first, last);
			
			drawGraph(month, first, last);
						
			//FoodSearchRequest request = new FoodSearchRequest();		
			
			//new GetMonthRequest().execute(url, reqMethod, paramsSigned);
		
		}catch(Exception e){
			String err = e.toString();
			toastMsg(err);
		}
	}
	

	@Override
	public void onClick(View button) {
		// TODO Auto-generated method stub
		switch(button.getId()){
		case R.id.btPrevMonth:
			currentDate -= 28;
			getMonth(currentDate);
			break;
		case R.id.btNextMonth:			
			/*if (currentDate + 30 > today){
				Toast.makeText(this, "No data available for this month", Toast.LENGTH_SHORT).show();
			}else{*/
				currentDate += 31;
				getMonth(currentDate);
			//}
			break;
		case R.id.bUnit2:
			if (chartMode == true){
				chartMode = false;
				String line = getResources().getString(R.string.line_chart);
				bUnit.setText(line);
				graph1.setVisibility(View.GONE);
				graph2.setVisibility(View.VISIBLE);
			}else{
				chartMode = true;
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
		getMenuInflater().inflate(R.menu.history, menu);
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
		
		//String msg = getResources().getString(stringId);
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
/*
	public void setAdVisible(boolean show){
		if (show)
			adView.setVisibility(View.VISIBLE);
		else
			adView.setVisibility(View.GONE);
	}
*/
}
