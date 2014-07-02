package com.archeotour;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class NewsClickListener implements OnClickListener {

	private String news;
	private FragmentManager fragmentmanager;
	private Activity activity;

	public NewsClickListener(String s, FragmentManager f, Activity activity) {
		news  =  s ;
		fragmentmanager = f;
		this.activity = activity;

		Log.v("notizia inserita", "inserita la notizia: \n " + news);
	}

	@Override
	public void onClick(View arg0) {

		Fragment obgfr = new SingleNewsFragment();
		FragmentTransaction transaction = fragmentmanager.beginTransaction();
		Bundle args = new Bundle();
		args.putCharSequence("notizia", news);
		obgfr.setArguments(args);

		if (isLandscape()) {
			if (isTablet()) {
				transaction.replace(R.id.single_news_container, obgfr);
			} else { // non � un tablet ma � orizzontale
				transaction.replace(R.id.right_container, obgfr);
			}

		} else { // se � in orizzontale
			transaction.replace(R.id.container, obgfr);
		}

		transaction.addToBackStack(null);
		transaction.commit();
	}

	private boolean isLandscape() {
		return (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
	}

	private boolean isTablet() {

		try {

			// Compute screen size

			DisplayMetrics dm = activity.getResources().getDisplayMetrics();
			float screenWidth = dm.widthPixels / dm.xdpi;
			float screenHeight = dm.heightPixels / dm.ydpi;
			double size = Math.sqrt(Math.pow(screenWidth, 2)
					+ Math.pow(screenHeight, 2));
			// Tablet devices should have a screen size greater than 6 inches
			return size >= 6;

		} catch (Throwable t) {
			Log.e("Click listener", "Failed to compute screen size", t);
			return false;
		}

	}
}
