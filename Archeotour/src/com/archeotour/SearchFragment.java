package com.archeotour;

import org.json.JSONArray;
import org.json.JSONObject;

import com.archeotour.db.JsonSearchSuper;
import com.archeotour.db.MyDatabase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("NewApi")
public class SearchFragment extends Fragment {

	private String input;
	private EditText et;
	private View rootView;

	public SearchFragment() {
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

		rootView = inflater.inflate(R.layout.fragment_search, container, false);
		et = (EditText) rootView.findViewById(R.id.searchsitetext);

		et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				input = et.getText().toString();
				new AsyncSearch(input, rootView).execute();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

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

	private class AsyncSearch extends JsonSearchSuper {

		private String searchrslt;
		private int idsito;
		private String urlDatabase;
		private TextView tv;
		private LinearLayout v;

		private JSONArray jsonArray;
		private JSONObject jsonob;

		private ProgressBar progress;

		private AsyncSearch(String s, View r) {
			v = (LinearLayout) r.findViewById(R.id.llayoutsrchrsltshow);
			progress = (ProgressBar) r.findViewById(R.id.progressBarsearch);

			urlDatabase = getResources().getString(R.string.url_root_sito)
					+ "/interrogate.php?partname=" + s + "&request=getlist";
		}

		@Override
		protected void onPreExecute() {
			progress.setVisibility(View.VISIBLE);
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

			progress.setVisibility(View.INVISIBLE);
			try {
				v.removeAllViews();
				for (int i = 0; i < jsonArray.length(); i++) {
					Log.v("search onpostexecute", "for iterazione " + i);
					jsonob = jsonArray.getJSONObject(i);
					searchrslt = jsonob.getString("nomerslt");
					idsito = jsonob.getInt("id");
					View rsltview = LayoutInflater.from(
							getActivity().getApplicationContext()).inflate(
							R.layout.searchresult, null);
					rsltview.setId(idsito);
					rsltview.setTag(searchrslt);
					rsltview.setOnClickListener(new OnClickListener() {
						private String name;
						@Override
						public void onClick(View v) {
							
							Log.v("click listener", "id pulsante " + v.getId());
							name = (String) v.getTag();
							//prendo i dati della ricerca
							//li aggiungo alle ricerche recenti
							
							MyDatabase mDb = new MyDatabase(getActivity().getApplicationContext());
							mDb.open();
							//-----------------------------------------
							mDb.insertRecent(name, String.valueOf(v.getId()));
							Log.v("Search Fragment", "inseriti: " + name + " " + v.getId());
							//------------------------------------------
							Intent intent = new Intent(getActivity(),
									MainSiteActivity.class);
							intent.putExtra("id_sito", v.getId());
							startActivity(intent);
						}
					});
					tv = (TextView) rsltview.findViewById(R.id.srchrslt);
					tv.setText(searchrslt);
					v.addView(rsltview, i);
				}
			} catch (Exception e) {
				Log.e("search onpostexecute", "err");
				e.printStackTrace();
			}

		}

	}
}