package com.zmobile.alcotest;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.device.ads.AdLayout;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdViewAttributes;
//import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.zmobile.ads.AdsGeneralHandler;
import com.zmobile.ads.AdviewsContainer;

import java.lang.reflect.Method;

/**
 * Created by lukasz on 2015-06-16.
 */
public class ActivityTemplate extends Activity implements Updatable { //}, AdListener {

    public String TAG;// = ActivityTemplate.class.getSimpleName();
    public String classTAG = ActivityTemplate.class.getSimpleName();
    ActivityTemplate act;
    //private static final String ADMOB_APP_ID = "ca-app-pub-4402674240600002~2053780578";
    //private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110";
    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-4402674240600002/9771423379";
    //private static final String ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713";
    private static String ADMOB_APP_ID;// = "ca-app-pub-4402674240600002~2053780578";
    static final long millisPerDay = 24 * 60 * 60 * 1000;
    Tracker mTracker;

    MenuHelper menuHelp;
    SharedPreferences sharedData;
    String filename = "SharedData";

    //------------------------ showing ads -----------------------------

    public boolean showNativeAds = false;
    public boolean showExpressAds = true;
    public boolean showFaceAds = true;
    public boolean showAmazonAds = true;

    public boolean showFaceNative = true;
    public boolean showFaceBanner = true;
    public boolean showAdmobBanner = true;
    public boolean showAdmobExpress = true;
    public boolean showAmazonBanner = true;

    public com.google.android.gms.ads.AdView adView;
    public com.amazon.device.ads.AdLayout amazonAd;
    public NativeExpressAdView adViewExpress;
    //public FrameLayout nativeAdContainer;
    public LinearLayout nativeAdContainer;
    public FrameLayout fbBannerContainer;

    AdsGeneralHandler adsHandle;

    public int faceNativeLayoutId = R.layout.ad_unit;
    int admobNativeLayoutId = 3;

    int ads, fb_ads, log;

    String fbNativeId;
    //double adShowBias = 0.35;
    static boolean adShown = false;

    //private InterstitialAd interstitial;
    //com.facebook.ads.InterstitialAd faceInterstitial;
    String interAdId;

    String admobNativeId;
    String faceInterId;
    static int faceShown = 0;
    static int admobShown = 0;
    static int interShown = 0;
    //AdRequest adRequest;
    //HandlerPurchase handlerIAP;

    //facebook native ads

    public NativeAdView.Type viewType = NativeAdView.Type.HEIGHT_100;
    public NativeAdViewAttributes adViewAttributes = new NativeAdViewAttributes();

    public TextView statusText;
    private com.facebook.ads.AdView fb_adView;

    String fb_adNative_id;

    //AdMob Native ad


    //FacebookNativeAds fbNativeAds;
    //AdmobNativeAds admobNativeAds;

    // Color set
    private int backgroundColor;
    private int rowBackgroundColor;
    private int titleColor;
    private int linkColor;
    private int contentColor;
    private int borderColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mTracker = ActivityMenu.mTracker;
        mTracker = getDefaultTracker(this);
        act = this;
        fbNativeId = getResources().getString(R.string.facebook_native_id);
        menuHelp = new MenuHelper(this);
        sharedData = getSharedPreferences(filename, 0);
        act = this;
        if (TAG==null || (!TAG.equals("ActivityMenu2"))) {
            ActionBar actionBar = getActionBar();
            actionBar.setHomeButtonEnabled(true);
            actionBar.setIcon(R.drawable.prev_2);
        }
        /*
        AdRequestBuilder AdBuilder = AdRequestBuilder.getInstance();
        adRequest = AdBuilder.build();
        ADMOB_APP_ID = getString(R.string.admob_app_id);
        MobileAds.initialize(this, ADMOB_APP_ID);

        interAdId = getString(R.string.admob_inter_id2);
        faceInterId = getString(R.string.facebook_inter_id);

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(interAdId);
        interstitial.setAdListener(getAdMobInterListener);
        */

        ads = getResources().getInteger(R.integer.ads);
        fb_ads = getResources().getInteger(R.integer.fb_ads);
        log = getResources().getInteger(R.integer.log);
        //adShown = false;
        //handlerIAP = HandlerPurchase.getInstance(this);
        int theme = sharedData.getInt("theme", 0);
        ThemeUtils.setTheme(theme);
        ThemeUtils.onActivityCreateSetTheme(this);

        //facebook native ads
        //nativeAdContainer = (LinearLayout) findViewById(R.id.native_ad_container);
        //statusText = (TextView) findViewById(R.id.status);

        if (faceNativeLayoutId == 0)
            faceNativeLayoutId = R.layout.ad_unit;

        if (admobNativeLayoutId == 0)
            admobNativeLayoutId = R.layout.ad_app_install;

        int cl_bkg = getResources().getColor(R.color.bkg_lt);
        backgroundColor = Color.LTGRAY;
        rowBackgroundColor = cl_bkg;//Color.WHITE;
        titleColor = Color.WHITE;//Color.argb(0xff, 0x4e, 0x56, 0x65);
        linkColor = Color.WHITE;//Color.argb(0xff, 0x3b, 0x59, 0x98);
        contentColor = Color.WHITE;//;Color.argb(0xff, 0x4e, 0x56, 0x65);
        borderColor = Color.YELLOW;//Color.GRAY;

        /*
        loadFacebookFullAd();

        //createAndLoadNativeAd();
        fbNativeAds = new FacebookNativeAds();

        //AdMob Native Ad
        admobNativeAds = new AdmobNativeAds(this);

        */
        AdviewsContainer adsContain = new AdviewsContainer();
        adsContain.adView = adView;
        //adsContain.admobNativeContainer = admobNativeContainer;
        adsContain.adViewExpress = adViewExpress;
        adsContain.fbBannerContainer  = fbBannerContainer;
        adsContain.amazonAd = amazonAd;
        adsContain.nativeAdContainer = nativeAdContainer;

        adsContain.showAdmobBanner = showAdmobBanner;
        adsContain.showAdmobExpress = showAdmobExpress;
        adsContain.showAmazonBanner = showAmazonBanner;
        adsContain.showFaceBanner = showFaceBanner;
        adsContain.showFaceNative = showFaceNative;

        adsHandle = new AdsGeneralHandler(this, adsContain);

    }
	
	@Override
    public void onStart() {
        super.onStart();
        // The rest of your onStart() code.
        if (statusText!=null) statusText.setVisibility(View.GONE);
        adShown = false;
        adsHandle.loadAds();
        adsHandle.showAds();
        //loadAds();
        /*

        try {
            String id = interstitial.getAdUnitId();
            if (id == null) {
                interstitial.setAdUnitId(interAdId);
            }
            if (!interstitial.isLoaded())
                interstitial.loadAd(adRequest);

            //ActivityMenu.fbFullAd.loadAd();

            if (showFaceAds)
                if (!faceInterstitial.isAdLoaded())
                    faceInterstitial.loadAd();

        }catch (Exception e){
            toastMsg("Start error: "+e.getMessage(),2);
        }*/

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //ThemeUtils.onActivityCreateSetTheme(this);
        //setContentView(R.layout.main);
        if (mTracker!=null) {
            mTracker.setScreenName(TAG);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
        //admobNativeAds.refreshAd(nativeAdContainer,true,true);
        /*
        if (showFaceAds)
            showFacebookNativeAd(faceNativeLayoutId);
        else
            showAdmobNativeAd();
        //showAdMobExpressNativeAd();
        */
    }

	    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        if (adView != null) adView.pause();
        super.onPause();
        ThemeUtils.saveTheme(sharedData);
        try {
            //showInterstitial(adShowBias);
            //showInterstitial2();
            adsHandle.showInterstitial4();
        } catch (Exception e) {
            toastMsg("Pause error: " + e.getMessage(), 3);
        }
    }
	
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        //removeAdView();
        if (adView!=null) adView.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        //showInterstitial();
        super.onBackPressed();
    }

    // --------------------- show ads general ----------------------------------------

    /*
    public void loadAds(){
        if (handlerIAP!=null)
            if (handlerIAP.mHasRemovedAds)
                return;
        if (adView==null)
            adView = (AdView) findViewById(R.id.adView);
        if (ads > 0 ) { //&& !handlerIAP.mHasRemovedAds) {
            if (ads > 1 && adView!=null) {
                setAdsVisible(adView);
                adView.loadAd(adRequest);
                adView.resume();
            }
            if (interstitial!=null)
                if (!interstitial.isLoaded() && !interstitial.isLoading())
                    interstitial.loadAd(adRequest);
        }
        if (adView != null) {
            if (ads < 1) { // || handlerIAP.mHasRemovedAds) {
                adView.setVisibility(View.GONE);
            }else{
                adView.setVisibility(View.VISIBLE);
            }
        }
        if (fb_ads == 0 && ads == 0) { //|| handlerIAP.mHasRemovedAds) {
            if (nativeAdContainer != null) nativeAdContainer.setVisibility(View.GONE);
        }
    }

    private void showInterstitial() {
        //if (interstitial.isReady()) {
        double rand = Math.random();
        if (handlerIAP!=null)
            if (handlerIAP.mHasRemovedAds)
                return;
        if (interstitial!=null) {
            boolean loaded = interstitial.isLoaded();
            if (loaded && rand < adShowBias && ads > 0 ) { //&& adShown == false) {
                interstitial.show();
                interstitial.loadAd(adRequest);
                adShown = true;
            }
        }

    }

    protected void showInterstitial2(){
        double rand = Math.random();

        if (ads == 0 || rand > adShowBias) // || adShown)
            return;
        boolean admobLoaded = interstitial.isLoaded();
        boolean faceLoaded = faceInterstitial.isAdLoaded();
        if (interShown++ % 2 == 1) {
            if (admobLoaded) {
                interstitial.show();
                admobShown++;
                //interstitial.loadAd(adRequest);
                //toastMsg("Admob shown: "+admobShown+" face: "+faceShown, 3);
            }
            interstitial.loadAd(adRequest);
            //ActivityMenu.fbFullAd.show();
        }else {
            if (faceLoaded) {
                faceInterstitial.show();
                faceShown++;
                //toastMsg("Face shown: "+faceShown+" admob "+admobShown, 3);
            } else
                faceInterstitial.loadAd();
        }
        toastMsg("Admob shown: "+admobShown+" face: "+faceShown, 2);
    }


    public void setAdVisible(boolean show) {
        if (adView != null) {
	        if (show) {
	        	adView.setVisibility(View.VISIBLE);
	            nativeAdContainer.setVisibility(View.VISIBLE);
	        }else {
	            adView.setVisibility(View.GONE);
	            nativeAdContainer.setVisibility(View.GONE);
	        }
        }
    }

    // -------------------- facebook inter ------------------------

    private void loadFacebookFullAd() {
        String fbFullAdId = getString(R.string.facebook_inter_id);
        faceInterstitial = new com.facebook.ads.InterstitialAd(this, fbFullAdId);
        faceInterstitial.setAdListener(getFacebookFullAdListener);
        faceInterstitial.loadAd();
    }

    InterstitialAdListener getFacebookFullAdListener = new InterstitialAdListener() {
        public void onInterstitialDisplayed(Ad ad) { }

        public void onInterstitialDismissed(Ad ad) {
            //ActivityMenu.fbFullAd.loadAd();
            faceInterstitial.loadAd();
        }

        public void onError(Ad ad, AdError adError) {
            toastMsg("Face inter error: "+adError.getErrorMessage(), 3);
        }

        public void onAdLoaded(Ad ad) {
            toastMsg("Face inter loaded", 3);
        }

        public void onAdClicked(Ad ad) {}
    };
    */

    // ----------------- facebook native ----------------------------
    /*
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

    private void showFacebookNativeAd(int layoutId){

        if (nativeAdContainer==null)
            nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container);
        if (nativeAdContainer==null)
            return;

        LinearLayout ll = new LinearLayout(this);
        //FrameLayout ll = new FrameLayout(this);
        //LinearLayout ll = nativeAdContainer;
        //LinearLayout ll2 = ll;
        //FrameLayout ll2 = ll;

        fbNativeAds = new FacebookNativeAds();
        //fbNativeAds.createAndLoadNativeAd(this, ll, R.layout.ad_unit_grid);
        fbNativeAds.createAndLoadNativeAd(this, ll, layoutId);
        //fbNativeAds.createAndLoadNativeAd(this, nativeAdContainer, layoutId);

        //nativeAdContainer.addView(ll);

        //v = vi.inflate(R.layout.ad_container, null);
        //ll2 = (LinearLayout) ll.getChildAt(0);
        //ll2 = (FrameLayout) ll.getChildAt(0);
        //ll2 = ll;

        //View adview = (View) ll2.getChildAt(1);
        //View adview = (View) ll2.getChildAt(0);
        //View adview = (View) ll.getChildAt(1);
        //ViewGroup.LayoutParams lp = v.getLayoutParams();
        //AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ll2.getLayoutParams());
        //ll2.setLayoutParams(lp);
        //ListView lv = new ListView(c);
        //lv.addView(v,0);
        //View v2 = lv.getChildAt(0);
        //AbsListView.
        //LinearLayout.
        //ViewGroup.LayoutParams lp = convertView.getLayoutParams();
        //adview.setLayoutParams(lp);
        //((ViewGroup)ll2.getParent()).removeAllViews();
        if (ll.getChildCount()<1) {
            showAdmobNativeAd();
        }else {
            setAdsVisible(nativeAdContainer);
            nativeAdContainer.removeAllViews();
            nativeAdContainer.addView(ll);
        }

    }



    public void onFbNativeAdError(){
        showAdmobNativeAd();


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

    protected void createAndLoadNativeAd() {
        // Create a native ad request with a unique placement ID
        // (generate your own on the Facebook app settings).
        // Use different ID for each ad placement in your app.
        nativeAd = new NativeAd(ActivityTemplate.this, fb_adNative_id);

        // Set a listener to get notified when the ad was loaded.
        nativeAd.setAdListener(getFbNativeAdListener);

        // Initiate a request to load an ad.
        nativeAd.loadAd();

        //statusText.setText("Loading an ad...");
    }

    AdListener getFbNativeAdListener = new AdListener() {

        @Override
        public void onAdLoaded(Ad ad) {
            if (nativeAd == null || nativeAd != ad) {
                // Race condition, load() called again before last ad was displayed
                return;
            }

            if (statusText != null) statusText.setText("Fb native Ad loaded.");
            reloadAdContainer();
        }

        @Override
        public void onError(Ad ad, AdError error) {
            if (statusText != null)
                statusText.setText("Fb native Ad failed to load: " + error.getErrorMessage());
        }

        @Override
        public void onAdClicked(Ad ad) {
            statusText.setText("Fb native Ad Clicked");
        }

    };
    */

    // -------------------------- AdMob Native -------------------------------

    /*
    public void showAdmobNativeAd(){
        //LinearLayout ll = new LinearLayout(this);
        FrameLayout ll = new FrameLayout(this);

        //LinearLayout ll = nativeAdContainer;
        admobNativeAds = new AdmobNativeAds((Activity) this);
        admobNativeAds.refreshAd(ll, true, true, false);
        //LinearLayout l;
        //ll2 = ll;
        ViewGroup parent = ((ViewGroup)ll.getParent());
        if (parent!=null)
            parent.removeAllViews();

        if (nativeAdContainer==null)
            nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container);
        if (nativeAdContainer!=null){
            setAdsVisible(nativeAdContainer);
            nativeAdContainer.removeAllViews();
            nativeAdContainer.addView(ll);
        }
    }

    */

    // ---------------------------- Ad Mob Native Express ------------------------------------------

    public void showAdMobExpressNativeAd(){
        nativeAdContainer.setVisibility(View.GONE);
        adViewExpress.setVisibility(View.VISIBLE);
        adViewExpress.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                //loadError = true;
                if (log>1) Toast.makeText(act, "Express Failed to load native ad: " + errorCode, Toast.LENGTH_SHORT).show();
                //((ActivityTemplate)act).showAdMobExpressNativeAd();
                //((ActivityTemplate)act).loadAds();
            }

            public void onAdLoaded(){
                //loadError = true;//false;
                if (log>1) Toast.makeText(act, "Express native loaded!", Toast.LENGTH_SHORT).show();
                //ActivityTemplate ctxTemp = ((ActivityTemplate) ctx);
                //ctxTemp.nativeAdContainer.setVisibility(View.VISIBLE);
            }
        });
        if (adViewExpress!=null) {
            if (adViewExpress.getAdSize() == null)
                adViewExpress.setAdSize(AdSize.MEDIUM_RECTANGLE);
            if (adViewExpress.getAdUnitId() == null)
                adViewExpress.setAdUnitId(getString(R.string.admob_express_small_id));
            adViewExpress.loadAd(new AdRequest.Builder().build());
        }
    }

    // ------------------------------ AdMob inter --------------------------

    com.google.android.gms.ads.AdListener getAdMobInterListener = new com.google.android.gms.ads.AdListener(){

        @Override
        public void onAdFailedToLoad(int errorCode){
            toastMsg("AdMob inter load error: "+errorCode, 3);
        }

        @Override
        public void onAdLoaded(){
            toastMsg("AdMob inter loaded", 3);
        }

    };

    com.google.android.gms.ads.AdListener getAdMobBannerListener = new com.google.android.gms.ads.AdListener(){

        @Override
        public void onAdFailedToLoad(int errorCode){
            toastMsg("AdMob banner load error: "+errorCode, 3);
        }

        @Override
        public void onAdLoaded(){
            toastMsg("AdMob banner loaded", 3);
        }

    };

    public void setAdsVisible(View viewToShow){
        if (adView!=null)
            //adView.setVisibility(View.GONE);
            adView.setVisibility(View.VISIBLE);
        if (nativeAdContainer!=null)
            //nativeAdContainer.setVisibility(View.GONE);
            nativeAdContainer.setVisibility(View.VISIBLE);
        if (adViewExpress!=null)
            //adViewExpress.setVisibility(View.GONE);
            adViewExpress.setVisibility(View.VISIBLE);
        if (fbBannerContainer!=null)
            //nativeAdContainer.setVisibility(View.GONE);
            fbBannerContainer.setVisibility(View.VISIBLE);
        if (viewToShow!=null)
            viewToShow.setVisibility(View.VISIBLE);
    }

    public void setAdsVisible(boolean show){
        if (adView!=null)
            if (!show) adView.setVisibility(View.GONE);
            else adView.setVisibility(View.VISIBLE);
        if (nativeAdContainer!=null)
            if (!show) nativeAdContainer.setVisibility(View.GONE);
            else nativeAdContainer.setVisibility(View.VISIBLE);
        if (adViewExpress!=null)
            if (!show) adViewExpress.setVisibility(View.GONE);
            else adViewExpress.setVisibility(View.VISIBLE);
        if (fbBannerContainer!=null)
            if (!show) nativeAdContainer.setVisibility(View.GONE);
            else fbBannerContainer.setVisibility(View.VISIBLE);
        /*
        if (viewToShow!=null)
            viewToShow.setVisibility(View.VISIBLE);
            */
    }

    protected void setUpAdLayouts(){
        amazonAd = (AdLayout) findViewById(R.id.amazonAdView);
        //nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container_end);
        nativeAdContainer = (LinearLayout) findViewById(R.id.native_ad_container_end);
        adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);
        //adView = (com.google.android.gms.ads.AdView) findViewById(R.id.disc_adView);
        adView = (com.google.android.gms.ads.AdView) findViewById(R.id.adView);
        fbBannerContainer = (FrameLayout) findViewById(R.id.fb_banner_ad);
    }

    // ---------------------- general stuff, not related to showing ads --------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the activity_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        menuHelp.handleOnItemSelected(this, item);
        return true;

    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
        if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(NoSuchMethodException e){
                    Log.e(TAG, "onMenuOpened", e);
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    public void toastMsg(int stringId){

        String msg = getResources().getString(stringId);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void toastMsg(String str){
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    private void sendEmail(String msg){
        //nutrInfo.sendEmail(this, msg);
    }

    public void updateTheme(Activity activity) {
        // TODO Auto-generated method stub
        int themeId = menuHelp.dialogSymbol.getThemeId();
        int selectionId = menuHelp.dialogSymbol.getSelectionId();
        //getApplication().setTheme(themeId);
        //ThemeUtils.changeToTheme(this, selectionId);
        ThemeUtils.changeToTheme(activity, selectionId);
    }

    // This is for Google Analytics
    synchronized public Tracker getDefaultTracker(Activity activity) {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(activity);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            String ga_tracker = getString(R.string.ga_trackingId);
            mTracker = analytics.newTracker(ga_tracker);
            //R.xml.global_tracker);
            //"UA-45019697-11");
            //R.xml.global_tracker);
        }
        return mTracker;
    }

    public void toastMsg(int stringId, int level){

        String msg = getResources().getString(stringId);
        if (level<log)
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void toastMsg(String str, int level){
        if (level<log)
            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    public void Update(){

    }

}
