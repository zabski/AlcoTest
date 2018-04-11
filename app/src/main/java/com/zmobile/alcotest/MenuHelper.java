package com.zmobile.alcotest;

import com.suredigit.inappfeedback.FeedbackDialog;
import com.suredigit.inappfeedback.FeedbackSettings;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;

class MenuHelper {

	Activity ctx;
	DialogSymbol dialogSymbol;
	private FeedbackDialog feedBack;
	
	private static MenuHelper instance = null;

	public static MenuHelper getInstance(Activity ctx){
	      if(instance == null) {
	    	  instance = new MenuHelper(ctx);
	      }
		instance.ctx = ctx;
	      return instance;
	}	

	public MenuHelper(Activity ctx) {
		this.ctx = ctx;
		// TODO Auto-generated constructor stub	
	
		dialogSymbol = new DialogSymbol(ctx);
										    		 
		FeedbackSettings feedbackSettings = new FeedbackSettings();
		String feedbackPrompt = ctx.getString(R.string.feedback_prompt);
		String opinion = ctx.getString(R.string.opinion);
		feedbackSettings.setText(feedbackPrompt);
		feedbackSettings.setIdeaLabel(opinion);
		feedBack = new FeedbackDialog((Activity) ctx, "AF-5ED0923D1C0B-AD", feedbackSettings);
	}
	

	   public boolean handleOnItemSelected(Activity ctx, MenuItem item) {
	          // do something..
		   Intent i;
	        switch (item.getItemId()) {
			case android.R.id.home:
				ctx.finish();
				return true;
	        case R.id.action_search:
				i = new Intent(ctx, ActivityAddDrink.class);
				ctx.startActivity(i);
				return true;
			case R.id.action_favorites:
				i = new Intent(ctx, ActivityFavorites.class);
				ctx.startActivity(i);
				return true;
			case R.id.action_diary:
				i = new Intent(ctx, ActivityDiaryToday.class);
				ctx.startActivity(i);
				return true;
			case R.id.action_chart:
				i = new Intent(ctx, ActivityGraphLevel.class);
				ctx.startActivity(i);
				return true;
			case R.id.action_history:
				i = new Intent(ctx, ActivityGraphDiary.class);
				ctx.startActivity(i);
				return true;
			case R.id.action_settings:
				  i = new Intent(ctx, ActivitySettings.class);
				  ctx.startActivity(i);
				  return true;
			case R.id.action_info:
				  i = new Intent(ctx, ActivityInfo.class);
				  ctx.startActivity(i);
				  return true;
			case R.id.action_rate_app:
				Uri uri = Uri.parse("market://details?id=" + ctx.getPackageName());
				Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        	try {
        		ctx.startActivity(goToMarket);
        	} catch (ActivityNotFoundException e) {
        		ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + ctx.getPackageName())));
        	}
        	return true;
        case R.id.action_feedback:
        	feedBack.show();
			/*
			final Intent emailIntent = new Intent( android.content.Intent.ACTION_SEND);

			emailIntent.setType("plain/text");

			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { "zmobapp@gmail.com" });

			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Email Subject");

			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					"Email Body");

			ctx.startActivity(Intent.createChooser(
					emailIntent, "Send mail..."));*/
        	return true;
        case R.id.action_theme:
        	dialogSymbol.setAct((Updatable) ctx);
        	return true;
		/*
		case R.id.action_remove_ads:
			HandlerPurchase.getInstance(ctx).onRemoveAdsButtonClicked(new View(ctx));
			return true;
			*/
        	/*
        case R.id.action_translate:
        	Intent intent = new Intent(ctx, TransCommuActivity.class);
        	intent.putExtra(TransCommuActivity.APPLICATION_CODE_EXTRA, "UZvMgTNLhy");
        	ctx.startActivity(intent);
	          
	          
	        case R.id.action_info:
	          i = new Intent(this, ActivityInfo.class);
	          startActivity(i);
	          return true;
	          */
	        default:
	          //return super.onOptionsItemSelected(item);
	          return false;
	        } 
	   }

	}