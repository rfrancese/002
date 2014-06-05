package com.archeotour;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class MainSiteFragment extends Fragment {

	private WebView meteoww;

	public MainSiteFragment() {
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	int id_site;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		id_site = getActivity().getIntent().getIntExtra("id_sito", 0);
		View rootView = inflater.inflate(R.layout.fragment_main_site,
				container, false);
		try {
			meteoww = (WebView) rootView.findViewById(R.id.MeteowebView);
			Log.v("mainsitefragment", "ww creata");
			new MeteoParser(711315, meteoww).execute();
		} catch (Exception e) {
			LinearLayout ll = (LinearLayout) meteoww.getParent();
			ll.removeView(meteoww);
		}
		return rootView;
	}

	public class MeteoParser extends AsyncTask<Void, Void, Void> {
		private Document doc;
		private int woeid;
		private String toRet;

		public MeteoParser(String cityName) {

		}

		public MeteoParser(int w, WebView meteoww) {
			woeid = w;
			Log.v("info meteo", getMeteoInfo());
		}

		public String getMeteoInfo() {

			String address = "http://weather.yahooapis.com/forecastrss?u=c&w="
					+ woeid;
			URL url = null;
			try {
				url = new URL(address);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			toRet = "";

			try {
				doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
						.parse(url.openStream());
				Element root = doc.getDocumentElement();
				NodeList channels = root.getElementsByTagName("channel");

				for (int i = 0; i < channels.getLength(); i++) {
					Element channel = (Element) channels.item(i);
					NodeList items = channel.getElementsByTagName("item");

					for (int j = 0; j < items.getLength(); j++) {
						Element item = (Element) items.item(j);
						NodeList descriptions = item
								.getElementsByTagName("description");

						for (int k = 0; k < descriptions.getLength(); k++) {
							Element description = (Element) descriptions
									.item(k);
							toRet = description.getFirstChild().getNodeValue();
						}
					}
				}
			} catch (SAXException e) {
				Log.e("ERROR", e.getMessage());
			} catch (IOException e) {
				Log.e("ERROR", e.getMessage());
			} catch (ParserConfigurationException e) {
				Log.e("ERROR", e.getMessage());
			} catch (FactoryConfigurationError e) {
				Log.e("ERROR", e.getMessage());
			}

			return toRet;
		}

		public String getWOEID(InputStream dbStream, String name) {
			String toRet = "";

			try {
				doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
						.parse(dbStream);
				Element root = doc.getDocumentElement();
				NodeList cities = root.getElementsByTagName("city");

				for (int i = 0; i < cities.getLength(); i++) {
					Element city = (Element) cities.item(i);

					if (city.getFirstChild().getNodeValue().equals(name)) {
						toRet = city.getAttribute("WOEID");
						break;
					}
				}

			} catch (SAXException e) {
				Log.e("ERROR", e.getMessage());
			} catch (IOException e) {
				Log.e("ERROR", e.getMessage());
			} catch (ParserConfigurationException e) {
				Log.e("ERROR", e.getMessage());
			} catch (FactoryConfigurationError e) {
				Log.e("ERROR", e.getMessage());
			}

			return toRet;
		}

		@Override
		protected Void doInBackground(Void... params) {
			toRet.replaceAll("\n", "&#13");
			toRet.replaceAll("\r", "&#10");

			try {
				meteoww.loadData(toRet, "text/html", "utf-8");
			} catch (Exception e){
				meteoww.loadDataWithBaseURL(null, toRet, "text/html", "utf-8", null);
			// meteoww.loadDataWithBaseURL(null, "aaa", "text/html", "utf-8", null);
			}
			return null;
		}
	}

}