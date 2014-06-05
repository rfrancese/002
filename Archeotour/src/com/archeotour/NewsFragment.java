package com.archeotour;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.archeotour.db.JsonSearchSuper;
import com.archeotour.db.MasterMetaData;
import com.archeotour.db.MyDatabase;
import com.archeotour.db.NewsMetaData;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;
import android.database.Cursor;
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

	public NewsFragment() {
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
		View rootView = inflater.inflate(R.layout.fragment_news, container,
				false);
		id_site = getActivity().getIntent().getIntExtra("id_sito", 0);

		new RSSParse(id_site, rootView).execute();

		return rootView;
	}

	private class RSSParse extends JsonSearchSuper {
		private LinearLayout ll;
		private TextView et;
		private ArrayList<RssItem> rssItems;
		private int id_site;
		private ProgressBar progress;
		private MyDatabase mDb;
		private Cursor c;

		public RSSParse(int i, View rw) {
			id_site = i;
			ll = (LinearLayout) rw.findViewById(R.id.linearlayoutnews);
			progress = (ProgressBar) rw.findViewById(R.id.newsprogressbar);
			mDb = new MyDatabase(getActivity().getApplicationContext());
			mDb.open();
			c = mDb.fetchNews(); // query
		}

		@SuppressWarnings("deprecation")
		protected void onPostExecute(Void result) {
			ll.removeView(progress);

			c = mDb.fetchNews(); // query
			getActivity().startManagingCursor(c);
			int nameTi = c.getColumnIndex(NewsMetaData.TITLE);
			int nameTe = c.getColumnIndex(NewsMetaData.TEXT);

			if (c.moveToFirst()) {
				// se va alla prima entry, il cursore non è vuoto
				Log.v("news", "nell if");
				do {
					// Log.v("news estratta ", c.getString(nameTe));
					et = new TextView(getActivity());
					et.setClickable(true);
					et.setOnClickListener(new NewsClickListener(c
							.getString(nameTe), getFragmentManager(), getActivity()));
					et.setText(c.getString(nameTi));
					et.setPadding(0, 7, 0, 7);
					ll.addView(et);
				} while (c.moveToNext());// iteriamo al prossimo elemento
			}

			mDb.close();
		}

		@SuppressWarnings("deprecation")
		private int getTimeDifference() {

			try {
				Cursor cursor_master = mDb.fetchTimeDifference();
				getActivity().startManagingCursor(cursor_master);
				int master_created_date = cursor_master
						.getColumnIndex(MasterMetaData.CREATIONDATE);

				if (cursor_master.moveToFirst()) {
					String created_date = cursor_master.getString(master_created_date);
					Log.v("getTimeDiffernce", "risultato fetch " + created_date);
				}
			} catch (Exception e) {
				Log.e("getTimeDifference", "errore nella lettura dell'orario");
				return 0;
			}
			/*
			 * Cursor cursor; String query; query = "select * from " +
			 * MasterMetaData.TABLE + " where " + MasterMetaData.TNAME + " = '"
			 * + NewsMetaData.NEWS_TABLE + "'";
			 * 
			 * cursor = mDb.rawQuery(query, null); Log.e("getTimeDifference",
			 * "query1: " + query); int ci =
			 * cursor.getColumnIndex(MasterMetaData.CREATIONDATE);
			 * 
			 * query = "SELECT (strftime('%s','now') - strftime('%s','" +
			 * cursor.getString(ci) + "')) / 60;"; Log.e("getTimeDifference",
			 * "query2: " + query); cursor = mDb.rawQuery(query, null);
			 * Log.e("getTimeDifference", cursor.getString(0));
			 */
			return 0;
		}

		@Override
		protected Void doInBackground(Void... params) {

			if (c.moveToFirst()) {
				Log.v("database", "il database non è vuoto");
				getTimeDifference();
			}

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
								+ chiave + "&output=rss");
				Log.v("news fragment", "url utilizzato: " + url);
				RssFeed feed = RssReader.read(url);

				rssItems = feed.getRssItems();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//TODO svuotare il database
			for (RssItem rssItem : rssItems) {
				mDb.insertNews(rssItem.getTitle(), rssItem.getDescription());
			}
			return null;
		}
	}

}