package com.archeotour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		url_description = getResources().getString(R.string.url_root_sito);
		int id_site = getActivity().getIntent().getIntExtra("id_sito",0);
		
		new getJsonClass(id_site).execute();
		
		View rootView = inflater.inflate(R.layout.fragment_site_description,
				container, false);
		
		rw = (WebView) rootView.findViewById(R.id.description_webview);
		
		return rootView;
	}
	
	private class getJsonClass extends AsyncTask<Void, Void, Void> {
		private int id;
		public getJsonClass(int i){
			id = i;
		}
		private JSONObject getJSONObject(String url) throws IOException,
				MalformedURLException, JSONException {
			
			HttpURLConnection conn = (HttpURLConnection) new URL(url)
					.openConnection();

			InputStream in = conn.getInputStream();

			try {
				StringBuilder sb = new StringBuilder();
				BufferedReader r = new BufferedReader(new InputStreamReader(
						new DoneHandlerInputStream(in)));
				for (String line = r.readLine(); line != null; line = r
						.readLine()) {
					sb.append(line);
				}

				return new JSONObject(sb.toString());
			} finally {
				in.close();
			}
		}

		protected void onPostExecute(Void result){
			rw.loadUrl(url_description + "/descrizione.html");
		}
		
		@Override
		protected Void doInBackground(Void... params) {

			
			String urlDatabase = getResources().getString(R.string.url_root_sito) + "/interrogate.php?siteid="+id+"&request=geturl";
			
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