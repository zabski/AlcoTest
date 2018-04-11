package com.zmobile.alcotest;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdViewAttributes;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.zmobile.foodendpoint.customerApi.model.OfyCustomer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.Tracker;
//import com.ironsource.mobilcore.MobileCore;
//import com.ironsource.mobilcore.MobileCore.*;
//import com.appigniter.android.AppIgniterAdLayout;

/*
import com.millennialmedia.android.MMSDK;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMInterstitial;
import com.millennialmedia.android.MMAd;
import com.millennialmedia.android.MMBroadcastReceiver;
import com.millennialmedia.android.MMRequest;
import com.millennialmedia.android.MMSDK;
import com.millennialmedia.android.RequestListener.RequestListenerImpl;
*/
public class ActivityMenu extends ActivityTemplate implements Updatable {

	//static final String TAG = ActivityMenu2.class.getSimpleName();
	public static Tracker mTracker;

	//private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110";
	//private static final String ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713";

	ListView mlist;
	GridView mGrid;

	String classes[] = {"ActivityAddDrink", "ActivityFavorites", "ActivityDiaryToday", "ActivityGraphLevel", "ActivityGraphDiary", "ActivitySettings", "ActivityInfo", "ActivityMenu2"};

	String menuImages[] = {"wine_bent_plus", "star_gold_128", "diary_128", "line_128", "stats_bar_128",
			"settings_128", "info_128", "add_64"};

	String[] inputWords = {"contraception", "pregnancy",
			"love", "relationship", "weight loss", "fashion", "diet"};

	ArrayList<ListItem> menuItems;
	//MMAdView MMadViewFromXml;
	SharedPreferences sharedData;
	String filename = "SharedData";
	MenuHelper menuHelp;

	//facebook native ads
	//FacebookNativeAds fbNativeAds;
	//public	LinearLayout nativeAdContainer;
	private NativeAdView.Type viewType = NativeAdView.Type.HEIGHT_100;
	private NativeAdViewAttributes adViewAttributes = new NativeAdViewAttributes();

	//AdmobNativeAds admobNativeAds;
	InterstitialAd interstitial;
	AdRequest adRequest;

	private TextView statusText;
	//private AdView adView;
	//com.google.android.gms.ads.AdView adView;
	//public NativeAd nativeAd;
	//int ads, fb_ads;
	//AdRequest adRequest;
	//HandlerPurchase handlerIAP;
	//String fb_adNative_id;

	// Color set
	private int backgroundColor;
	private int rowBackgroundColor;
	private int titleColor;
	private int linkColor;
	private int contentColor;
	private int borderColor;

	SharedPreferences settings;// = getApplicationContext().getSharedPreferences("Settings", 0);
	Boolean remarket;
	private FirebaseAnalytics mFirebaseAnalytics;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v,
								int position, long id) {
			String activityName = classes[position];
			//super.onListItemClick(l, v, position, id);
			//MobileCore.showOfferWall(this, null);
			Class ourClass = null;
			try {
				ourClass = Class.forName("com.zmobile.alcotest." + activityName);
				Intent ourIntent = new Intent(ActivityMenu.this, ourClass);

				startActivity(ourIntent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

	//@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		String activityName = classes[position];
		//super.onListItemClick(l, v, position, id);
		//MobileCore.showOfferWall(this, null);
		Class ourClass = null;
		try {
			ourClass = Class.forName("com.zmobile.alcotest." + activityName);
			Intent ourIntent = new Intent(ActivityMenu.this, ourClass);

			startActivity(ourIntent);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
		setContentView(R.layout.activity_menu2);
		setUpAdLayouts();
		// TODO Auto-generated method stub
		TAG = ActivityMenu.class.getSimpleName();
		mTracker = getDefaultTracker();
		sharedData = getSharedPreferences(filename, 0);
		int theme = sharedData.getInt("theme", 0);
		ThemeUtils.setTheme(theme);
		ThemeUtils.onActivityCreateSetTheme(this);
		menuHelp = new MenuHelper(this);


		/*
        TextView privacyText = (TextView) findViewById(R.id.privacyPolicy);
        privacyText.setClickable(true);
        privacyText.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='https://docs.google.com/document/d/1KU_5kCrjkvKUBFU790Az9KCPuqTEUPAc9J739mziMi8'> Privacy Policy </a>";
        privacyText.setText(Html.fromHtml(text));
		*/
		//adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);
		mlist = (ListView) findViewById(R.id.list);
		mGrid = (GridView) findViewById(R.id.gridView1);
		//mlist.setDividerHeight(0);

		faceNativeLayoutId = R.layout.ad_unit_no_img;

		String menuTitles[] = {getString(R.string.add_drink), getString(R.string.favorites),
				getString(R.string.diary), getString(R.string.alc_level), getString(R.string.diary_graph),
				getString(R.string.settings), getString(R.string.info), "Ad"};
		menuItems = new ArrayList<ListItem>();

		for (int i = 0; i < 8; i++) {
			menuItems.add(i, new ListItem(menuTitles[i], "", "@drawable/" + menuImages[i]));
			//[i].title = menuTitles[i];
			//menuItems[i].img = "@drawable/ic_launcher";
		}

		//full screen
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		//setListAdapter(new ArrayAdapter<String>(ActivityMenu.this, android.R.layout.simple_list_item_1, menuTitles));

		mlist.setAdapter(new ImageArrayAdapter(ActivityMenu.this, android.R.layout.simple_list_item_1, menuItems, R.layout.list_item));
		//setListAdapter(new ImageArrayAdapter(ActivityMenu2.this, android.R.layout.simple_list_item_1, menuItems));
		mlist.setOnItemClickListener(listener);

		mGrid.setAdapter(new ImageArrayAdapter(ActivityMenu.this, android.R.layout.simple_list_item_1, menuItems, R.layout.grid_item));
		mGrid.setOnItemClickListener(listener);
		//ViewGroup parent = (ViewGroup) nativeAdContainer.getParent();
		//parent.removeAllViews();
		//nativeAdContainer = new LinearLayout(this);
		//mGrid.addView(nativeAdContainer,7);

		//nativeAdContainer = (LinearLayout) mGrid.getChildAt(7);//.findViewById(R.id.native_ad_container);
		/*
		View container = new View(this);
		mGrid.getAdapter().getView(7,container, null);
		ViewParent vp = container.getParent();
		LinearLayout ll = (LinearLayout) vp;
		//nativeAdContainer.addView(container);
		//nativeAdContainer = (LinearLayout) container;
		*/

		adView = (com.google.android.gms.ads.AdView) findViewById(R.id.adView);
		//facebook native ads
		//nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container_end);
		statusText = (TextView) findViewById(R.id.status);

		int cl_bkg = getResources().getColor(R.color.bkg_lt);
		backgroundColor = Color.LTGRAY;
		rowBackgroundColor = cl_bkg;//Color.WHITE;
		titleColor = Color.WHITE;//Color.argb(0xff, 0x4e, 0x56, 0x65);
		linkColor = Color.WHITE;//Color.argb(0xff, 0x3b, 0x59, 0x98);
		contentColor = Color.WHITE;//;Color.argb(0xff, 0x4e, 0x56, 0x65);
		borderColor = Color.YELLOW;//Color.GRAY;

		//createAndLoadNativeAd();
		//fbNativeAds = new FacebookNativeAds();
		//fbNativeAds.createAndLoadNativeAd(this, nativeAdContainer, R.layout.ad_unit_no_img);
		//fbNativeAds.createAndLoadNativeAd(this, container, R.layout.ad_unit_no_img);

		//AdMob Native ads

		//AdMob Native ads

		//MobileAds.initialize(this, ADMOB_APP_ID);

		//admobNativeAds = new AdmobNativeAds(this);
		//admobNativeAds.refreshAd(nativeAdContainer,true,true);

		settings = getApplicationContext().getSharedPreferences("Settings", 0);
		remarket = settings.getBoolean("remarket", false);
		String googleMail = "zmobapp@gmail.com";
		boolean userSaved = settings.getBoolean("userDataSaved", false);
		//if (!userSaved)
			//googleMail = saveCustomerData();

		if (!remarket)
			RemarketHttpCall.get();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

		super.onCreate(savedInstanceState);
		loadAds();
	}

	void loadAds(){
		adView = (AdView) findViewById(R.id.adView);

		AdRequestBuilder AdBuilder = AdRequestBuilder.getInstance();
		adRequest = AdBuilder.build();
		adView.setAdListener(getAdMobBannerListener);
		adView.loadAd(adRequest);

		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId(getResources().getString(R.string.admob_inter_id));
		interstitial.setAdListener(getAdMobInterListener);
		interstitial.loadAd(adRequest);
	}


	/*
	public void onFbNativeAdError(){
		nativeAdContainer.setVisibility(View.GONE);
		if (adView != null) {
			if (ads > 3 && !handlerIAP.mHasRemovedAds) {
				adView.setVisibility(View.VISIBLE);
				adView.loadAd(adRequest);
			}else{
				adView.setVisibility(View.GONE);
			}
		}
	}

	public void onFbNativeAdLoaded(){
		if (adView != null)
			adView.setVisibility(View.GONE);
		if (nativeAdContainer!=null)
			nativeAdContainer.setVisibility(View.VISIBLE);
	}
	*/
	/*
	protected void createAndLoadNativeAd() {
		// Create a native ad request with a unique placement ID
		// (generate your own on the Facebook app settings).
		// Use different ID for each ad placement in your app.
		nativeAd = new NativeAd(ActivityMenu.this, fb_adNative_id);

		// Set a listener to get notified when the ad was loaded.
		nativeAd.setAdListener(ActivityMenu.this);

		// Initiate a request to load an ad.
		nativeAd.loadAd();

		//statusText.setText("Loading an ad...");
	}

	@Override
	public void onAdLoaded(Ad ad) {
		if (nativeAd == null || nativeAd != ad) {
			// Race condition, load() called again before last ad was displayed
			return;
		}

		statusText.setText("Ad loaded.");
		reloadAdContainer();
	}

	@Override
	public void onError(Ad ad, AdError error) {
		statusText.setText("Ad failed to load: " + error.getErrorMessage());
	}

	@Override
	public void onAdClicked(Ad ad) {
		statusText.setText("Ad Clicked");
	}

	private void reloadAdContainer() {
		if (nativeAd != null && nativeAd.isAdLoaded()) {
			nativeAdContainer.removeAllViews();

			// Create a NativeAdViewAttributes object (e.g. adViewAttributes)
			//   to render the native ads
			// Set the attributes
			adViewAttributes.setBackgroundColor(rowBackgroundColor);
			adViewAttributes.setTitleTextColor(titleColor);
			adViewAttributes.setDescriptionTextColor(contentColor);
			adViewAttributes.setButtonBorderColor(linkColor);
			adViewAttributes.setButtonTextColor(linkColor);

			// Use NativeAdView.render to generate the ad View
			// NativeAdViewType viewType = NativeAdViewType.HEIGHT_100;
			View adView = NativeAdView.render(this, nativeAd, viewType, adViewAttributes);

			// Add adView to the container showing Ads
			nativeAdContainer.addView(adView, 0);
			nativeAdContainer.setBackgroundColor(Color.TRANSPARENT);

			//showCodeButton.setText(R.string.show_code);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		menuHelp.handleOnItemSelected(this, item);
		return true;

	}
*/

	public void onStart() {
		super.onStart();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		//MobileCore.showStickee(this);
		// Get from the SharedPreferences
		//EasyTracker.getInstance(this).activityStart(this);  // Add this method.
		mTracker.setScreenName(TAG);
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		//SharedPreferences settings = getApplicationContext().getSharedPreferences("Settings", 0);
		int lbs = settings.getInt("lbs", -1);
		//mHeight = settings.getInt("height", 160);
		//mWeight = settings.getInt("weight", 50);
		//mSex = settings.getString("sex", "Female");
		//mExercise = settings.getInt("exercise", 1);
		if (lbs < 0) {
			Intent ourIntent = new Intent(ActivityMenu.this, ActivitySettings.class);
			startActivity(ourIntent);
		}

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Alcohol content in blood", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://z-mobile-apps.blogspot.com/alcohol"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://com.zmobile.alcotest/http/z-mobile-apps.blogspot.com/alcohol")
		);
		AppIndex.AppIndexApi.start(client, viewAction);
	}

	@Override
	public void onStop() {
		super.onStop();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Alcohol content in blood", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://z-mobile-apps.blogspot.com/alcohol"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://com.zmobile.alcotest/http/z-mobile-apps.blogspot.com/alcohol")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		// The rest of your onStop() code.
		//EasyTracker.getInstance(this).activityStop(this);  // Add this method.
		int sta = RemarketHttpCall.status;
		if (!remarket && sta == 200) {
			remarket = true;
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("remarket", true);
			editor.commit();
		}
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.disconnect();
	}

	protected void onDestroy() {
		super.onDestroy();
		//AppIgniterAdLayout.clean();
	}

	public void setAdVisible(boolean show) {
		if (adView != null) {
			if (show)
				adView.setVisibility(View.VISIBLE);
			else
				adView.setVisibility(View.GONE);
		}
	}

	public void Update() {
		// TODO Auto-generated method stub
		int themeId = menuHelp.dialogSymbol.getThemeId();
		int selectionId = menuHelp.dialogSymbol.getSelectionId();
		//getApplication().setTheme(themeId);
		ThemeUtils.changeToTheme(this, selectionId);

	}

	synchronized public Tracker getDefaultTracker() {
		if (mTracker == null) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			// To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
			String ga_tracker = getString(R.string.ga_trackingId);
			mTracker = analytics.newTracker(ga_tracker);
			//R.xml.global_tracker);
			//"UA-45019697-11");
			//R.xml.global_tracker);
		}
		return mTracker;
	}

	String saveCustomerData() {

		long todayMillis = Calendar.getInstance().getTimeInMillis();

		long today = todayMillis / (1000 * 60 * 60 * 24);

		String possibleEmail = "";

		PackageManager pm = getPackageManager();
		int hasPerm = pm.checkPermission(android.Manifest.permission.GET_ACCOUNTS, getPackageName());
		if (hasPerm <= PackageManager.PERMISSION_GRANTED) {
		//if (0 == hasPerm) {
			if (Build.VERSION.SDK_INT >= 23) {
				requestPermissions(new String[]{android.Manifest.permission.GET_ACCOUNTS}, 0);
			}
			Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
			Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
			//.get(context).getAccounts();
			for (Account account : accounts) {
				if (emailPattern.matcher(account.name).matches()) {
					possibleEmail = encodeEmail(account.name);
					OfyCustomer customer = new OfyCustomer();
					customer.setAddr(possibleEmail);
					customer.setAppName("AlcoTest");
					customer.setProducer(android.os.Build.MANUFACTURER);
					customer.setModel(android.os.Build.MODEL);
					customer.setDevice(android.os.Build.DEVICE);
					customer.setCountry(Locale.getDefault().getCountry());
					customer.setLang(Locale.getDefault().getLanguage());
					customer.setDate(today);
					new AddCustomerAsyncTask().execute(new Pair<Context, OfyCustomer>(this, customer));
				}
			}
		}
		return possibleEmail;
	}

    String encodeEmail(String email){
        String encoded = "";
        String encoded2 = "";
        for(int i=0; i<email.length(); i++){
            char c = email.charAt(i);
            char p = ++c;
            c--;
            char m = --c;
            encoded += p;
            encoded2 += m;
        }
        return encoded;
    }

}
