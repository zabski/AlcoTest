package com.zmobile.alcotest;

import java.util.Calendar;
import java.util.Date;

import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.DigitalClock;
import android.widget.TextView;

// A Fragment is sort of a mini Activity which you can add
// or remove from another Activity.

// You can use them to create reusable pieces you can add 
// to your interface.

// They are often used so that depending on your screen size 
// you can add more or less fragments

public class SimpleFragment extends Fragment implements OnChronometerTickListener {

	// onCreateView is called when the Fragment is first called for
	
	// The LayoutInflater inflates an xml file into the view
	
	// A ViewGroup is an abstract class that all ViewGroups like 
	// LinearLayout extends
	
	// A Bundle is used to pass data between Activities such as
	// variable data you'd want saved if the orientation of the
	// device changes
	TextView tvBAC;
	TextView tvNow;
	TextView tvLastBAC;
	TextView tvLastBACrest;
	TextView tvPromile;
	TextView tvSoberTime;
	TextView tvLegalTime;	
	TextView tvLegalHours;
	TextView tvSoberHours;
	TextView circle;
	Favorites favs;
	ActivityGraphLevel act;
	
	DigitalClock clock;
	Chronometer chrono;		

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the Fragment, but don't attach it to the main 
		// activity LinearLayout because attachToRoot is marked
		// as false		
		
		View view = inflater.inflate(R.layout.fragment_alc_content, container, false);
		
		//simpleFragmentView.findViewById(id)
		
		clock = (DigitalClock) view.findViewById(R.id.digitalClock);
        chrono = (Chronometer) view.findViewById(R.id.chronometer);	      
        
        tvBAC = (TextView) view.findViewById(R.id.tvBAC);
        tvNow = (TextView) view.findViewById(R.id.tvNow);
        tvLastBAC = (TextView) view.findViewById(R.id.tvLastBAC);
        tvLastBACrest = (TextView) view.findViewById(R.id.tvLastBACrest);
        tvPromile = (TextView) view.findViewById(R.id.tvPromile);   
        tvLegalTime = (TextView) view.findViewById(R.id.tvLegalTime);
        tvSoberTime = (TextView) view.findViewById(R.id.tvSoberTime);
        tvLegalHours = (TextView) view.findViewById(R.id.tvLegalHours);
        tvSoberHours = (TextView) view.findViewById(R.id.tvSoberHours);
        circle = (TextView) view.findViewById(R.id.circle);

		act = (ActivityGraphLevel) getActivity();
		favs = act.favorites;

		chrono.setOnChronometerTickListener(this);
        //onChronometerTick(chrono);
        chrono.start();

		return view;
	}
	
	String hoursSplit(double hoursFull){//, int hours, int mins, int secs){
		String format;
		int hours = (int) hoursFull;
		double minsFull = (hoursFull-Math.floor(hoursFull))*60;
		int mins = (int) minsFull;
		double secsFull = (minsFull-Math.floor(minsFull))*60;
		int secs = (int) secsFull;		
		format = String.format(" %02d:%02d:%02d ", hours, mins, secs);
		return format;
	}
	
	String hoursSplit(Calendar cal){
		Date date = cal.getTime();
		int hour = date.getHours();
		int min = date.getMinutes();
		int sec = date.getSeconds();		
		
		String timeSoberStr = String.format(" %02d:%02d:%02d ", hour, min, sec);
		return timeSoberStr;
	}
	
	@Override
	public void onChronometerTick(Chronometer chronometer) {
		// TODO Auto-generated method stub

		double bac = favs.calcTotalBacNow();
		Calendar cal = Calendar.getInstance();
		
		long now = cal.getTimeInMillis();
		long nowSecs = (long)(now/1000);
		
		double lastBacNow = favs.calcLastBAC(nowSecs);
		double lastBac = favs.calcLastBacNow();
		double lastBACpromile = lastBac*10;
		int lastBACrest = (int)((lastBACpromile*100-Math.floor(lastBACpromile*100))*100)%100;
		double lastBACpromileFloor = Math.floor(lastBac*1000)/100;//*10;
		
		double MR = 0.015;
		if (favs.sexMale == false){			
			MR = 0.017;
		}
		
		String nowStr = cal.getTime().toLocaleString();
		
		double timeSoberHrs = lastBac/MR;
		double timeLegalHrs = (lastBac-favs.getLegalAlc())/MR;
		
		long timeSoberMil = (long) (timeSoberHrs*1000*60*60);
		long timeLegalMil = (long) (timeLegalHrs*1000*60*60);
		
		cal.setTimeInMillis(now+timeSoberMil);
		
		Date date = cal.getTime();
		 
		//toLocaleString();
		
		String timeSoberStr = hoursSplit(cal);
		
		cal.setTimeInMillis(now+timeLegalMil);
				
		String timeLegalStr = hoursSplit(cal);
				
		//cal.getTime().toLocaleString();				
		
		String fully_sober = getResources().getString(R.string.fully_sober);
		String legal_sober = getResources().getString(R.string.legal_sober);
		String current_alc = getResources().getString(R.string.current_alc);
		String sober = getResources().getString(R.string.sober_time)+"\n";
		String legal = getResources().getString(R.string.legal_time)+"\n";
		String that_is = getResources().getString(R.string.that_is);
		String bacStr = String.format("%.5f", bac);
		String lastBacStr = String.format("%.5f", lastBac);
		String lastBacPromileStr = String.format("%.2f", lastBACpromileFloor);
		String lastBacRestStr = String.format("%02d", lastBACrest);
		
		
		String hrsSoberStr = sober+timeSoberStr+that_is+hoursSplit(timeSoberHrs); // String.format("%.5f", timeSoberHrs);
		String hrsLegalStr = legal+timeLegalStr+that_is+hoursSplit(timeLegalHrs); //String.format("%.5f", timeLegalHrs); 
				
		tvNow.setText(nowStr);//String.format("%d", now));
		tvBAC.setText(current_alc);		
		//tvLastBAC.setText(lastBacStr+" %");
		tvLastBAC.setText(lastBacPromileStr);
		tvLastBACrest.setText(lastBacRestStr);
		String promile = getResources().getString(R.string.promile);
		tvPromile.setText(" ‰ ("+promile+")");
		int color = act.GetColor(lastBac);
		circle.setBackgroundColor(color);
		ShapeDrawable circleShape = new ShapeDrawable( new  OvalShape() );
		
		GradientDrawable gradient = act.nutrInfo.getGradient(color,1,2);
		circleShape.getPaint().setColor(color);
		//Shape shape = new ShapeDrawable();
		//gradient.setShape(R.layout.shape_circle);
		//gradient.setShape(new  OvalShape());
		//circle.setBackgroundDrawable(gradient);
		circle.setBackgroundDrawable(circleShape);
		//tvLegalTime.setText(that_is+timeLegalStr);//String.format("%d" , timeSoberMil));//
		//tvSoberTime.setText(that_is+timeSoberStr);//String.format("%f",  timeSoberHrs));//
		if (lastBac > 0)
			tvLegalHours.setText(hrsSoberStr);
		else
			tvLegalHours.setText(fully_sober);
		if (lastBac > favs.getLegalAlc())
			tvSoberHours.setText(hrsLegalStr);
		else
			tvSoberHours.setText(legal_sober);
	}	

}