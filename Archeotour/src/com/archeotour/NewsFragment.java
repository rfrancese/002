package com.archeotour;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xml.sax.SAXException;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsFragment extends Fragment {
	
	private LinearLayout layout;

	public NewsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_news, container,
				false);
		layout = (LinearLayout)rootView.findViewById(R.id.linearlayoutnews);

		new RSSParse(layout).execute();
		
		return rootView;
	}

	private class RSSParse extends AsyncTask<Void, Void, Void> {
		private LinearLayout ll;
		private EditText et;
		private ArrayList<RssItem> rssItems;
		public RSSParse(LinearLayout l)
		{
			ll = l;
		}
		
		protected void onPostExecute(Void result){
			
			for (RssItem rssItem : rssItems) {
				Log.i("RSS Reader", rssItem.getTitle());
				et = new EditText(getActivity());
				et.setKeyListener(null);
				et.setOnClickListener(new NewsClickListener(rssItem.getDescription(), getFragmentManager()));     
				et.setText(rssItem.getTitle());
				ll.addView(et);
				
			}
			
			


		}
		
		@Override
		protected Void doInBackground(Void... params) {
			URL url;
			try {
				url = new URL(
						"https://news.google.de/news/feeds?pz=1&cf=all&ned=it&hl=it&q=pompei&output=rss");

				RssFeed feed = RssReader.read(url);

				 rssItems = feed.getRssItems();
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}
	}

}