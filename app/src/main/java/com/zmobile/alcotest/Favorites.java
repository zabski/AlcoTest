package com.zmobile.alcotest;

import java.io.*;
import java.io.FileInputStream.*;
import java.io.FileOutputStream;
import java.io.FileOutputStream.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.zmobile.foodtest.FoodInfoActivity.GetFoodRequest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/*
import fatsecret.platform.FatSecretAPI;
import fatsecret.platform.FatSecretAuth;
import fatsecret.platform.FatSecretException;
*/
public class Favorites {

	private static Favorites instance;

	static String filename = "Settings";
	static SharedPreferences settings;

	Map<String,String> parameters;
	
	Context ctx;
	
	String consumerKey;				
	String consumerSecret;	
	
	//FatSecretAPI fatSecretApi;
	//FatSecretAuth fatSecretAuth;
	//Profile profile;
	
	String mFoodId;
	boolean sexMale;
	double weightKg;

	private double legalAlc;
	int Lbs;
	static final double LbsRatio = 0.45359237;
	
	String favFileName = "favoriteDrinks.list";
	String diaryFileName = "diaryItems.list";
	String historyFileName = "history.list";
	
	//Map<Integer,FavoriteDrink> favList = new HashMap<Integer,FavoriteDrink>();
	
	ArrayList<BloodRecord> favList = new ArrayList<BloodRecord>();
	ArrayList<BloodRecord> diaryList = new ArrayList<BloodRecord>();
	ArrayList<BloodRecord> historyList = new ArrayList<BloodRecord>();

	public double getLegalAlc() {
		return legalAlc;
	}

	public static Favorites getInstance(Context ctx){
		if (instance == null){
			settings = ctx.getSharedPreferences(filename, 0);

			int isMale = (short) settings.getInt("isMale", 1);
			int mAlc = settings.getInt("alc", 2);
			int mHeight = settings.getInt("height", 160);
			//mWeight = settings.getInt("weight", 50);
			int mLbs = settings.getInt("lbs", 100);
			double legAlc= (double)mAlc/100;
			boolean sexMale = true;
			if (isMale==1) sexMale = true;
			else sexMale = false;
			instance = new Favorites(ctx, sexMale, mLbs, legAlc);
					//new Favorites(ctx);
		}else {
			instance.ctx = ctx;
		}
		return instance;
	}

	public static Favorites getInstance(Context ctx, boolean sexMale, int lbs, double legAlc){
		if (instance == null){
			instance = new Favorites(ctx, sexMale, lbs, legAlc);
		}else {
			instance.ctx = ctx;
			instance.sexMale = sexMale;
			instance.Lbs = lbs;
			instance.weightKg = lbsToKg(lbs);
			instance.legalAlc = legAlc;
		}
		return instance;
	}
	/*
	private Favorites(Context ctx){

		this(ctx, sexMale, mHeight, legAlc);
	}*/
	
	private Favorites(Context ctx, boolean sexMale, int lbs, double legAlc)
	{
		this.ctx = ctx;
		this.sexMale = sexMale;
		this.Lbs = lbs;
		this.legalAlc = legAlc;
		this.weightKg = lbsToKg(lbs);
		
		getFavoritesList();
		getDiaryList();
		
		//consumerKey = ctx.getString(R.string.consumerKey);				
		//consumerSecret = ctx.getString(R.string.consumerSecret);
		
		/*
		 fatSecretApi = new FatSecretAPI(consumerKey,consumerSecret);
		 
		
		fatSecretApi = api;
		
		profile = new Profile(fatSecretApi);
		
		fatSecretAuth = loadAuth();		
		
		if (fatSecretAuth == null){
			try{
				profile.create();
				//fatSecretAuth = fatSecretApi.ProfileCreate();
				//Thread.sleep(10000);
				synchronized(profile){
					//Thread.currentThread().wait();
					//this.wait();
					profile.wait(5000);
				}
				fatSecretAuth = profile.getAuth();
				saveAuth(fatSecretAuth);				
				
			}catch(Exception e){
				String err = e.toString();
				int a = 1;
				
			}
		}*/
	}

	/*
	public Favorites(FatSecretAPI api, FatSecretAuth auth)
	{	
		this.fatSecretApi = api;
		this.fatSecretAuth = auth;					
	}
	*/
	
	private ArrayList<BloodRecord> loadListFromFile(String filename, ArrayList<BloodRecord> list) {
		
		ArrayList<BloodRecord> masterlistrev = new ArrayList<BloodRecord>();
		try {			
		    FileInputStream fis = new FileInputStream(ctx.getFilesDir().getPath().toString() + "/" +filename); 
		    		//openFileInput(serfilename);
		    ObjectInputStream ois = new ObjectInputStream(fis);
		    masterlistrev = (ArrayList<BloodRecord>) ois.readObject();
		} catch (Exception e) {
			String err = e.toString();
		    e.printStackTrace();
		}
		list = masterlistrev;
		return masterlistrev;
	}
	
	private void writeListToFile(ArrayList<BloodRecord> masterlistrev, String filename){

	    File myfile = new File(ctx.getFilesDir().getPath().toString() + "/" +filename); 
	    		//getFileStreamPath(serfilename);
	    try {
	        if(myfile.exists() || myfile.createNewFile()){
	            FileOutputStream fos = new FileOutputStream(myfile);//, Context.MODE_PRIVATE);
	            		//openFileOutput(serfilename, MODE_PRIVATE);
	            ObjectOutputStream oos = new ObjectOutputStream(fos);
	            oos.writeObject(masterlistrev);
	        }
		} catch (Exception e) {
			String err = e.toString();
		     e.printStackTrace();
		}
	}	
	
	public List<BloodRecord> getFavoritesList(){
		
		if (favList == null || favList.size() == 0)
			favList = loadListFromFile(favFileName, favList);
		return favList;
	}
	
	public List<BloodRecord> getDiaryList(){
		
		if (diaryList == null || diaryList.size() == 0)
			//diaryList = (ArrayList<DiaryItem>) 
			diaryList = loadListFromFile(diaryFileName, diaryList);
		return diaryList;
	}
	
	public List<BloodRecord> getHistoryList(){
		
		if (historyList == null || historyList.size() == 0)
			//diaryList = (ArrayList<DiaryItem>) 
			historyList = loadListFromFile(historyFileName, historyList);
		return historyList;
	}	
	
	public List<BloodRecord> getHistoryforDates(long begin, long finish){
		int start = 0;
		int end = historyList.size();
		int id = 0;
		for(BloodRecord item: historyList){
			long time = item.getTime();
			if (id == 0 && time > begin)
				start = id;
			if (time > finish)
				end = id;
			id++;
		}	
		List<BloodRecord> subList = historyList.subList(start, end); 
		return subList;
	}
	
	public void clearDiary(){
		getHistoryList();
		historyList.addAll(diaryList);
		saveHistory();
		diaryList.clear();
		saveDiary();
	}
	
	public void clearFavorites(){						
		favList.clear();
		saveFavorites();
	}	

	private void saveFavorites(){
		
		writeListToFile(favList, favFileName);

	}
	
	private void saveDiary(){
		
		writeListToFile(diaryList, diaryFileName);

	}
	
	private void saveHistory(){
		
		writeListToFile(historyList, historyFileName);

	}	
	
	public void addFavorite(BloodRecord drink){
		
		favList.add(drink);		
		saveFavorites();		
		
	}
	
	public void addDiary(BloodRecord drink){
		
		diaryList.add(drink);		
		saveDiary();		
		
	}	
	
	public boolean delFavorite(String favor_sign){
		
		for(BloodRecord drink: favList){
			/*
			int id = drink.getId();
			if (id == favor_id){
				favList.remove(drink);
				saveFavorites();
				return true;
			}		*/
			String rowSign = drink.getSignature();
			if (rowSign == null || rowSign.equals(""))
				rowSign = drink.generateSignature();
			if (favor_sign.equals(rowSign)){
				favList.remove(drink);
				saveFavorites();
				return true;
			}
		}	
		return false;
	}
	
	public boolean delDiaryItem(String diary_sign){
		
		for(BloodRecord drink: diaryList){
			int id = drink.getId();
			String rowSign = drink.getSignature();
			if (rowSign == null || rowSign.equals(""))
				rowSign = drink.generateSignature();
			if (diary_sign.equals(rowSign)){
				diaryList.remove(drink);
				saveDiary();
				return true;
			}
		}	
		return false;
	}	
	
	public long getLastDrinkTime(){
		long lastTime = 0;
		for(BloodRecord drink: diaryList){
			if (drink.getTime() > lastTime)
				lastTime = drink.getTime(); 
		}
		return lastTime;
	}
	
	public BloodRecord getDrinkForId(String strId, String label, PersonInfo personInfo){
		
        //List<FavoriteDrink> favList = favorites.getFavoritesList();
        
        String[] vals = strId.split("-");
        int type = personInfo.strToInt(vals[0], 0);
        int amount = personInfo.strToInt(vals[1], 0);
        int percentInt = personInfo.strToInt(vals[2], 500);
        double percent = ((double)percentInt)/100;//personInfo.strToDbl(vals[2], 5);        
        
        BloodRecord drink = new BloodRecord(0, percent, type, amount, label, 0); 
        
        return drink;	
	}
	
	public BloodRecord findFavorite(String sign, String label){
		for(BloodRecord drink: favList){
			if (sign.equals(drink.getSignature()) && label.equals(drink.getmLabel())){
				return drink;
			}
		}
		return null;
	}
	
	public void addFoodDiary(String food_id, String serving_id, String entry_name){
				
		
	}
	
	private double calcBAC(double Wt, double SD, double BW, double MR, double DP){
		// Wt - weight in kg
		// SD - Standard Drink = 10g of pure alcohol
		// BW - body water constant (0.58 for men and 0.49 for women)
		// MR - metabolism constant (women 0.017, men 0.015)
		// DP - drinking period in hours
		double bac = 0;
		bac = 0.806*SD*1.2/(BW*Wt)-(MR*DP);
		return Math.max(bac,0);
	}
	
	public double calcTotalBacNow(){
		long nowSecs = Calendar.getInstance().getTimeInMillis()/(1000);
		return calcTotalBAC(nowSecs);
	}
	
	public double calcTotalBAC(long forTimeSecs){
		double totalBAC = 0;
		//long now = Calendar.getInstance().getTimeInMillis()/(1000*60);
		double waterRatio = 0.58; // for males
		double MR = 0.015;
		if (sexMale == false){
			waterRatio = 0.49;
			MR = 0.017;
		}
		long startTime = forTimeSecs;
		long lastTime = 0;
		
		for(BloodRecord drink : diaryList){
			long time = drink.getTime();//("time");
			startTime = Math.min(startTime, time);
			lastTime = Math.max(lastTime, time);
			double amount = drink.getmAmount();//("ant");
			double percent = drink.getmPercent();//("perc");
			long drinkPeriodSecs = forTimeSecs - time;
			double drinkPeriodHours = (double)drinkPeriodSecs/(60*60);
			double standardDrinks = drink.getStandardDrinks();
					//amount*percent/1000;
			double bac = 0;
			double lastBAC = 0;
			if (forTimeSecs > time){
				bac = calcBAC(waterRatio, standardDrinks, weightKg, MR, 0);
				lastBAC = calcBAC(waterRatio, standardDrinks, weightKg, MR, drinkPeriodHours);
			}
			totalBAC += bac;
		}
		long drinkPeriodSecs = forTimeSecs - startTime;
		double drinkPeriodHours = (double)drinkPeriodSecs/(60*60);
		totalBAC = totalBAC - (MR*drinkPeriodHours);
		return totalBAC;
	}
	
	public double calcLastBacNow(){
		long nowSecs = Calendar.getInstance().getTimeInMillis()/1000;
		return calcLastBAC(nowSecs);
	}	
	
	public double calcLastBAC(long forTimeSecs){
		double totalBAC = 0;
		//long now = Calendar.getInstance().getTimeInMillis()/(1000*60);
		double waterRatio = 0.58; // for males
		double MR = 0.015;
		if (sexMale == false){
			waterRatio = 0.49;
			MR = 0.017;
		}
		long startTime = Long.MAX_VALUE;
		long lastTime = 0;
		double bac = 0;
		double lastBAC = 0;		
		
		for(BloodRecord drink : diaryList){
			long time = drink.getTime();//("time");
			startTime = Math.min(startTime, time); // set drinking start time to time of first drink
			long startPeriodSecs = 0;
			if (time > startTime)					// 
			   startPeriodSecs = time - startTime;							
			double startPeriodHours = (double)startPeriodSecs/(60*60); //period from first to this drink
			double currentBAC = totalBAC - (MR*startPeriodHours); // currentBAC for this drinks time
			if (currentBAC <= 0){	// if current BAC reached 0 reset start time to current drink and totaBAC to 0 
				startTime = time;
				totalBAC = 0;
			}						
			/*
			lastTime = Math.max(lastTime, time);
			long drinkPeriodSecs = forTimeSecs - time;
			double drinkPeriodHours = (double)drinkPeriodSecs/(60*60);
			*/
			double amount = drink.getmAmount();//("ant");
			double percent = drink.getmPercent();//("perc");
			double standardDrinks = amount*percent/1000; // number of 10g alcohol units		
			if (forTimeSecs > time){					// if drink time in past calulate BAC of this drink
				bac = calcBAC(waterRatio, standardDrinks, weightKg, MR, 0);
				//lastBAC = calcBAC(waterRatio, standardDrinks, weightKg, MR, drinkPeriodHours);
				totalBAC += bac;
			}
		}
		if (forTimeSecs > startTime){
			long drinkPeriodSecs = forTimeSecs - startTime;
			double drinkPeriodHours = (double)drinkPeriodSecs/(60*60);
			totalBAC = totalBAC - (MR*drinkPeriodHours);
		}else{
			totalBAC = 0;
		}
		return Math.max(totalBAC,0);
	}	
	
	public static double lbsToKg(int lbs){
		
		double weightKg = (double)Math.round(lbs*LbsRatio*2)/2;
		return weightKg;
	}			

}
