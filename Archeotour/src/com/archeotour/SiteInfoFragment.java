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
import android.widget.EditText;

public class SiteInfoFragment extends Fragment {

	private int id_site;
	private static EditText et;

	public SiteInfoFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		id_site = getActivity().getIntent().getIntExtra("id_sito", 0);
		View rootView = inflater.inflate(R.layout.fragment_site_info,
				container, false);
		et= (EditText) rootView.findViewById(R.id.text_site_info);
		new getJsonClass(id_site).execute();
		return rootView;
	}

	private class getJsonClass extends AsyncTask<Void, Void, Void> {
		private int id;
		private String testo; 
		public getJsonClass(int i){
			id = i;
			testo = "";
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
			et.setText(testo);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
	
			
			String urlDatabase = getResources().getString(R.string.url_root_sito) + "/interrogate.php?siteid="+id+"&request=getinfo";
			
			try {
				JSONObject obj = getJSONObject(urlDatabase);
				JSONArray jsonArray = obj.getJSONArray("sito");
				JSONObject jsonob;
				
				for (int i=0; i<jsonArray.length(); i++){
					jsonob = jsonArray.getJSONObject(i);
					double apertura = jsonob.getDouble("apertura");
					double chiusura = jsonob.getDouble("chiusura");
					testo += apertura + " " + chiusura + "\n";
					
					Log.v("apertura", String.valueOf(apertura));
					Log.v("chiusura", String.valueOf(chiusura));
				}
	
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("JSON Error", "JSON non scaricato");
			}
			return null;
		}
		
		 
	}
}