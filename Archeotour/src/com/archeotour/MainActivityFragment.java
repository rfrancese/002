package com.archeotour;

import com.archeotour.db.MyDatabase;
import com.archeotour.db.RecentMetaData;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivityFragment extends Fragment {

	public MainActivityFragment() {
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		MyDatabase mDb = new MyDatabase(getActivity().getApplicationContext());
		mDb.open();
		Cursor c = mDb.fetchRecentSearch();
		LinearLayout v = (LinearLayout) rootView
				.findViewById(R.id.linear_layout_recent);
		int i = 0;
		if (c.moveToLast()) {

			int colid = c.getColumnIndex(RecentMetaData.SITEID);
			int colname = c.getColumnIndex(RecentMetaData.SITENAME);
			
			do{
				String searchrslt = c.getString(colname);
				int idsito = c.getInt(colid);
	
				View rsltview = LayoutInflater.from(
						getActivity().getApplicationContext()).inflate(
						R.layout.searchresult, null);
				rsltview.setId(idsito);
				rsltview.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.v("click listener", "id pulsante " + v.getId());
						Intent intent = new Intent(getActivity(),
								MainSiteActivity.class);
						intent.putExtra("id_sito", v.getId());
						startActivity(intent);
					}
				});
				TextView tv = (TextView) rsltview.findViewById(R.id.srchrslt);
				tv.setText(searchrslt);
				v.addView(rsltview, i);
				i++;
			}while (c.moveToPrevious());
		}

		return rootView;
	}

}
