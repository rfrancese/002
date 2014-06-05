package com.archeotour;

import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import com.archeotour.db.JsonSearchSuper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SiteInfoFragment extends Fragment {

	private int id_site;
	private static TextView et;

	public SiteInfoFragment() {
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

		id_site = getActivity().getIntent().getIntExtra("id_sito", 0);
		View rootView = inflater.inflate(R.layout.fragment_site_info,
				container, false);
		et = (TextView) rootView.findViewById(R.id.text_site_info);
		new getJsonClass(id_site, rootView).execute();
		return rootView;
	}

	private class getJsonClass extends JsonSearchSuper {
		private int id;
		private String testo;
		private View rootview;

		public getJsonClass(int i, View rw) {
			rootview = rw;
			id = i;
			testo = "";
		}

		protected void onPostExecute(Void result) {

			ProgressBar progress;
			progress = (ProgressBar) rootview
					.findViewById(R.id.infoprogressbar);
			RelativeLayout r = (RelativeLayout) progress.getParent();
			r.removeView(progress);
			et.setText(testo);
		}

		@Override
		protected Void doInBackground(Void... params) {

			String urlDatabase = getResources().getString(
					R.string.url_root_sito)
					+ "/interrogate.php?siteid=" + id + "&request=getinfo";

			try {
				JSONObject obj = getJSONObject(urlDatabase);
				JSONArray jsonArray = obj.getJSONArray("orari");
				JSONObject jsonob;
				DecimalFormat df = new DecimalFormat("#.00");
				testo = "Orari apertura sito: \n";

				for (int i = 0; i < jsonArray.length(); i++) {

					Log.v("get json", "primo for");
					jsonob = jsonArray.getJSONObject(i);
					double apertura = jsonob.getDouble("apertura");
					double chiusura = jsonob.getDouble("chiusura");

					testo += "  " + df.format(apertura) + " - "
							+ df.format(chiusura) + "\n";

					Log.v("apertura", String.valueOf(apertura));
					Log.v("chiusura", String.valueOf(chiusura));
				}
				jsonArray = obj.getJSONArray("tariffe");
				testo += "\n\n Tariffe : \n";

				for (int i = 0; i < jsonArray.length(); i++) {

					Log.v("get json", "primo for");
					jsonob = jsonArray.getJSONObject(i);
					String percorso = jsonob.getString("tipo_percorso");
					double prezzo = jsonob.getDouble("prezzo");
					testo += "  " + percorso + ": " + df.format(prezzo) + "€\n";

				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("JSON Error", "JSON non scaricato");
			}
			return null;
		}

	}
}