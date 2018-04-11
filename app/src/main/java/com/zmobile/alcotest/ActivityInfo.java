package com.zmobile.alcotest;

/*import com.appigniter.android.AppIgniterAdLayout;
import com.google.analytics.tracking.android.EasyTracker;
import com.ironsource.mobilcore.MobileCore;
*/
import com.facebook.ads.NativeAdView;
//import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.zmobile.zmoblib.SoftKeyboard;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityInfo extends ActivityTemplate implements OnClickListener, Updatable{
	
	Button bEnterData;

	MenuHelper menuHelp;
	SoftKeyboard sk;

	//HandlerPurchase handlerIAP;
	
	SharedPreferences sharedData;
	String filename = "SharedData";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_info);
		setUpAdLayouts();
		super.onCreate(savedInstanceState);
		classTAG = this.getClass().getSimpleName();
		mTracker = getDefaultTracker(this);
		ThemeUtils.onActivityCreateSetTheme(this);


		TextView privacyText = (TextView) findViewById(R.id.privacyPolicy);
		privacyText.setClickable(true);
		privacyText.setMovementMethod(LinkMovementMethod.getInstance());
		String text = "<a href='https://docs.google.com/document/d/1KU_5kCrjkvKUBFU790Az9KCPuqTEUPAc9J739mziMi8'> Privacy Policy </a>";
		privacyText.setText(Html.fromHtml(text));
		//adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);
		//adViewExpress.setAdSize(AdSize.MEDIUM_RECTANGLE);
		//adViewExpress.setAdUnitId(getString(R.string.ad_unit_id));
		//adViewExpress.loadAd(new AdRequest.Builder().build());
		sharedData = getSharedPreferences(filename, 0);
		//facebook native
		//nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container);

		statusText = (TextView) findViewById(R.id.status);
		viewType = NativeAdView.Type.HEIGHT_300;

	    //LinearLayout layout = (LinearLayout) findViewById(R.id.infoTopLay);
	    //layout.addView(adView);
	    LinearLayout layout2 = (LinearLayout) findViewById(R.id.infoScroLay);
	    //layout2.addView(adView2);
	    //adView.loadAd(adRequest);
	    //adView2.loadAd(adRequest);
	    //xml_adView.loadAd(adRequest);
	    //xml_adView2.loadAd(adRequest);
	    menuHelp = new MenuHelper(this);
	    //menuHelp = MenuHelper.getInstance(this);

		bEnterData = (Button)findViewById(R.id.bEnterData);   
		bEnterData.setOnClickListener(this);

		//fbNativeAds.createAndLoadNativeAd(this, nativeAdContainer, R.layout.ad_unit);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		menuHelp.handleOnItemSelected(this, item);
		return true;

    } 


	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		//startActivityForResult(new Intent(Intent.ACTION_MAIN).setDataAndType(null, ActivitySettings.MIME_TYPE), 100);
		//Intent intent = new Intent(this, ActivityMenu.class);
		//Intent intent = new Intent(Intent.ACTION_MAIN);
		//Intent intent = new Intent("com.zmobile.foodtest.ActivityMenu");
		//String action = "com.zmobile.foodtest.MAIN";//"android.intent.action.MAIN";
		//Intent intent = new Intent();//"android.intent.action.MAIN");
		//Intent intent = new Intent(ActivityInfo.this, ActivityMenu.class);
		//intent.setAction(action);
		//startActivity(intent);
		finish();
	}
	
	@Override
	public void onStart() {
	    super.onStart();
	    // The rest of your onStart() code.
	    //EasyTracker.getInstance(this).activityStart(this);  // Add this method.
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
	
    protected void onDestroy(){
        super.onDestroy();
        //AppIgniterAdLayout.clean();
    }	

	@Override
	public void onRestart() {
	    super.onRestart();
	    //MobileCore.showStickee(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		//adView.pause();
		super.onPause();		
		ThemeUtils.saveTheme(sharedData);
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
