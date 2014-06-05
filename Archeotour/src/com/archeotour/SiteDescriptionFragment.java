package com.archeotour;

import org.json.JSONArray;
import org.json.JSONObject;

import com.archeotour.db.JsonSearchSuper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class SiteDescriptionFragment extends Fragment {

	private static String url_description;
	private static WebView rw;

	public SiteDescriptionFragment() {
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

		url_description = getResources().getString(R.string.url_root_sito);
		int id_site = getActivity().getIntent().getIntExtra("id_sito", 0);

		new getJsonClass(id_site).execute();

		View rootView = inflater.inflate(R.layout.fragment_site_description,
				container, false);

		rw = (WebView) rootView.findViewById(R.id.description_webview);

		return rootView;
	}

	private class getJsonClass extends JsonSearchSuper {
		private int id;

		public getJsonClass(int i) {
			id = i;
		}

		protected void onPostExecute(Void result) {
			 rw.postDelayed(new Runnable() {
			        @Override
			        public void run() {
						rw.loadUrl(url_description + "/descrizione.html");
			        }
			    }, 100);
		}

		@Override
		protected Void doInBackground(Void... params) {

			String urlDatabase = getResources().getString(
					R.string.url_root_sito)
					+ "/interrogate.php?siteid=" + id + "&request=geturl";

			try {
				JSONObject obj = getJSONObject(urlDatabase);
				JSONArray jsonArray = obj.getJSONArray("sito");
				JSONObject jsonob = jsonArray.getJSONObject(0);
				url_description += "//" + jsonob.getString("url_descr");
				Log.v("JSON Parse", url_description);

			} catch (Exception e) {
				e.printStackTrace();
				Log.e("JSON Error", "JSON non scaricato");
			}
			return null;
		}

	}
}