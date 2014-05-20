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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment {

	private String input;
	private EditText et;
	View rootView;

	public SearchFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_search, container, false);
		et = (EditText) rootView.findViewById(R.id.searchsitetext);

		ImageButton ib = (ImageButton) rootView.findViewById(R.id.searchbutton);
		ib.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				input = et.getText().toString();
				new AsyncSearch(input, rootView).execute();
			}
		});
		return rootView;
	}

	private class AsyncSearch extends AsyncTask<Void, Void, Void> {

		View rootv;

		String searchrslt;
		int idsito;
		String urlDatabase;
		TextView tv;
		
		JSONArray jsonArray;
		JSONObject jsonob;

		private AsyncSearch(String s, View r) {
			urlDatabase = getResources().getString(
					R.string.url_root_sito)
					+ "/interrogate.php?partname=" + s + "&request=getlist";
			rootv = r;
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				Log.v("search request", "get json");
				Log.v("search url", urlDatabase);
				JSONObject obj = getJSONObject(urlDatabase);

				Log.v("search request", "getArray");
				jsonArray = obj.getJSONArray("sito");

				Log.v("search request", "prima del for");
				Log.v("search result", jsonArray.toString());

			} catch (Exception e) {
				Log.e("search request", "errore nella richiesta");
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			try {
				LinearLayout v = (LinearLayout) rootv.findViewById(R.id.llayoutsrchrsltshow);
				for (int i = 0; i < jsonArray.length(); i++) {
					Log.v ("search onpostexecute", "for iterazione " + i);
					jsonob = jsonArray.getJSONObject(i);
					searchrslt = jsonob.getString("nomerslt");
					idsito = jsonob.getInt("id");
					View rsltview = LayoutInflater.from(
							getActivity().getApplicationContext()).inflate(
							R.layout.searchresult, null);
					rsltview.setId(idsito);
					rsltview.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Log.v("click listener", "id pulsante " + v.getId());
							Intent intent = new Intent(getActivity(), MainSiteActivity.class);
							intent.putExtra("id_sito", v.getId());
							startActivity(intent);
						}
					});
					tv = (TextView) rsltview.findViewById(R.id.srchrslt);
					tv.setText(searchrslt);
					v.addView(rsltview, i);
					// TODO: le view se cerchi 2 volte si sommano, fare in modo
					// che pulisca i risultati prima di mostrare i nuovi
				}
			} catch (Exception e){
				Log.e ("search onpostexecute", "err");
				e.printStackTrace();
			}

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

	}
}