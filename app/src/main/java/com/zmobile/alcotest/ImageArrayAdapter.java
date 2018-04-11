package com.zmobile.alcotest;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

public class ImageArrayAdapter extends ArrayAdapter<ListItem> {
	

	private ArrayList<ListItem> items;
	Context c;
	int viewItemId;

	//ads
	FacebookNativeAds fbNativeAds;
	AdmobNativeAds admobNativeAds;
	public NativeAd nativeAd;
	int ads, fb_ads;
	boolean showFb = true;
	AdRequest adRequest;
	HandlerPurchase handlerIAP;
	String fb_adNative_id;

	public ImageArrayAdapter(Context context, int textViewResourceId, ArrayList<ListItem> items, int viewItemId) {
		super(context, textViewResourceId, items);
		this.c = context;
		this.items = items;
		this.viewItemId = viewItemId;

		//context.nativeAd;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;

		LayoutInflater vi = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (v == null) {
			//v = vi.inflate(R.layout.list_item, null);
			v = vi.inflate(viewItemId, null);

		}

		//lz
		//v = parent.getRootView().findViewById(R.id.native_ad_container);

		if (position > 6){
			LinearLayout ll = new LinearLayout(c);
			FrameLayout fl = new FrameLayout(c);

			LinearLayout ll2 = ll;
			//FrameLayout ll2 = ll;
			fbNativeAds = new FacebookNativeAds();
			admobNativeAds = new AdmobNativeAds((Activity) c);
			ActivityTemplate ctx = (ActivityTemplate)c;
			if (showFb) {

				fbNativeAds.createAndLoadNativeAd(c, ll, R.layout.ad_unit_grid);
				//fbNativeAds.createAndLoadNativeAd(c, ll, R.layout.ad_unit_no_img);

				//v = vi.inflate(R.layout.ad_container, null);
				if (ll.getChildAt(0)!=null)
					ll2 = (LinearLayout) ll.getChildAt(0);
				//ll2 = (FrameLayout) ll.getChildAt(0);

				//View adview = (View) ll2.getChildAt(1);
				//ViewGroup.LayoutParams lp = v.getLayoutParams();
				//AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ll2.getLayoutParams());
				//ll2.setLayoutParams(lp);

			}
			/*else {

				admobNativeAds.refreshAd(fl, true, true, true);
				//ll2 = ll;
				ll2.addView(fl);
			}*/

			//if (ll2.getChildCount()<1){
			if (!showFb || fbNativeAds.loadError) {

				admobNativeAds.refreshAd(fl, true, true, true);
				//admobNativeAds.loadError = true;
				//ll2 = ll;

				if (!admobNativeAds.loadError) {
					ll2.addView(fl);
				} else {
					ll2.setBackgroundColor(ctx.getResources().getColor(R.color.bkg_lt));
					//ll2.setLayoutParams(new LinearLayout.LayoutParams(ctx,));
					ViewGroup parent2 = (ViewGroup) ctx.adViewExpress.getParent();
					//NativeExpressAdView adViewExpress = ctx.adViewExpress;
					//adViewExpress.loadAd(new AdRequest.Builder().build());
					parent2.removeView(ctx.adViewExpress);
					ll2.addView(ctx.adViewExpress);

					ctx.showAdMobExpressNativeAd();
					//ll2.addView(adViewExpress);
					//TextView tv = new TextView(ctx);
					//tv.setText("qwerty");
					//ll2.addView(tv);

				}
			}
			int a = 1;
			int b = a + 2;
			a = b + 1;
			View ret = (View) ll2;
			v = ret;
			//return ret;//adview;
			return v;
		}

		ListItem item = items.get(position);
		
		if (item != null) {
			TextView title = (TextView) v.findViewById(R.id.title);
			//TextView text = (TextView) v.findViewById(R.id.text);
			ImageView img = (ImageView) v.findViewById(R.id.img);
			
			if (title != null) {
				title.setText(item.title);
			}

			//if(text != null) {	text.setText("text: " + item.text ); }
			
			if(img != null) {
				
				int id = c.getResources().getIdentifier(item.img, "id", c.getPackageName());
				img.setImageResource(id);
			}
		}
		return v;

	}
}