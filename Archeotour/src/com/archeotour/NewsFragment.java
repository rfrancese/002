package com.archeotour;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.archeotour.db.JsonSearchSuper;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewsFragment extends Fragment {

	private int id_site;
	private RSSParse parser;
	public NewsFragment() {
	}

	@Override
	public void onDestroy() {
		//distruggo la connessione http per evitare crash
		parser.cancel(true);
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_news, container,
				false);
		id_site = getActivity().getIntent().getIntExtra("id_sito", 0);
		
		
		parser = new RSSParse(id_site, rootView);
		parser.execute();
		return rootView;
	}

	private class RSSParse extends JsonSearchSuper {
		private LinearLayout ll;
		private TextView et;
		private ArrayList<RssItem> rssItems;
		private int id_site;
		private ProgressBar progress;

		public RSSParse(int i, View rw) {
			id_site = i;
			ll = (LinearLayout) rw.findViewById(R.id.linearlayoutnews);
			progress = (ProgressBar) rw.findViewById(R.id.newsprogressbar);
		}

		protected void onPostExecute(Void result) {
			if (isCancelled()){
				return;
			}
			ll.removeView(progress);
			//rimuovo la progress bar

			//creo l'elenco delle notizie
			for (RssItem item : rssItems) {
				et = new TextView(getActivity());
				et.setClickable(true);
				et.setOnClickListener(new NewsClickListener(item.getDescription(),
						getFragmentManager(), getActivity()));
				et.setText(item.getTitle());
				et.setPadding(0, 7, 0, 7);
				ll.addView(et);
			}
		}

		@Override
		protected Void doInBackground(Void... params) {

			String chiave;
			String urlDatabase = getResources().getString(
					R.string.url_root_sito)
					+ "/interrogate.php?siteid="
					+ id_site
					+ "&request=getcitta";

			try {
				JSONObject obj = getJSONObject(urlDatabase);
				JSONArray jsonArray = obj.getJSONArray("sito");
				JSONObject jsonob;
				Log.v("get news", "response " + obj);
				jsonob = jsonArray.getJSONObject(0);
				chiave = jsonob.getString("citta");

			} catch (Exception e) {
				chiave = "archeologia";
			}

			URL url;

			try {
				url = new URL(
						"https://news.google.de/news/feeds?pz=1&cf=all&ned=it&hl=it&q="
								+ URLEncoder.encode(chiave, "UTF-8") + "&output=rss");
				Log.v("news fragment", "url utilizzato: " + url);
				RssFeed feed = RssReader.read(url);
				rssItems = feed.getRssItems();
				Log.v("notizie", rssItems.toString());
				if (rssItems == null) {
					Log.e("news background", "notizie non recuperate");
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}
	}

}