package com.archeotour;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainSiteFragment extends Fragment {

	public MainSiteFragment() {
	}
	
	int id_site;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		id_site = getActivity().getIntent().getIntExtra("id_sito",0);
		View rootView = inflater.inflate(R.layout.fragment_main_site,
				container, false);
		return rootView;
	}
}