package com.archeotour;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat;
import android.view.View;
import android.view.View.OnClickListener;

public class NewsClickListener implements OnClickListener {

	private String news;
	private FragmentManager fragmentmanager;

	public NewsClickListener(String s, FragmentManager f) {
		news = s;
		fragmentmanager = f;
	}

	@Override
	public void onClick(View arg0) {
		Fragment obgfr = new SingleNewsFragment();
		FragmentTransaction transaction = fragmentmanager.beginTransaction();
		Bundle args = new Bundle();
		args.putCharSequence("notizia", news);
		obgfr.setArguments(args);
		transaction.replace(R.id.container, obgfr);
		transaction.addToBackStack(null);
		transaction.commit();
	}

}
