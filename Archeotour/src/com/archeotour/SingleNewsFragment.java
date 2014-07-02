package com.archeotour;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SingleNewsFragment extends Fragment {

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	private String news;

	private OnFragmentInteractionListener mListener;

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	public static SingleNewsFragment newInstance(String param1, String param2) {
		SingleNewsFragment fragment = new SingleNewsFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public SingleNewsFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (getArguments() != null) {
			news = getArguments().getString("notizia");
			Log.v("single news fragment", news);
		} else {
			news = "nessuna notizia selezionata o errori durante il recupero della notizia";
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_single_news, container,
				false);
		TextView text = (TextView) root.findViewById(R.id.singlenews);
		text.setText(Html.fromHtml(news));
		text.setTextColor(Color.parseColor("#FFFFFF"));
		
		Log.v ("news", news);
		//text.loadData(news,  "text/html", "utf-8");
		return root;
	}

	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}


	public interface OnFragmentInteractionListener {
		public void onFragmentInteraction(Uri uri);
	}

}
