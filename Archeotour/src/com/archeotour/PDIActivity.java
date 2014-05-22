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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class PDIActivity extends FragmentActivity implements
		OnMarkerClickListener, OnMapLongClickListener {

	private static GoogleMap myMap;
	PolylineOptions rectOptions;
	Polyline polyline;
	boolean markerClicked;
	private String api_key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api_key = getString(R.string.google_places_api_key);

		Log.v("map", "started");
		setContentView(R.layout.activity_gpsmap);
		FragmentManager myFragmentManager = getSupportFragmentManager();
		SupportMapFragment mySupportMapFragment = (SupportMapFragment) myFragmentManager
				.findFragmentById(R.id.map);
		myMap = mySupportMapFragment.getMap();
		myMap.setMyLocationEnabled(true);
		
		myMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
			
			@Override
			public void onMapLoaded() {
				Log.v("map", "ciao");
				myMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(40.7738125,14.7932548) , 14.0f) );

				 
			}
		});

		
		new getJsonClass().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapLongClick(LatLng point) {
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		return true;
	}

	private class getJsonClass extends AsyncTask<Void, Void, Void> {
		private JSONArray jsonArray;

		public getJsonClass() {
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

		protected void onPostExecute(Void result) {
			Log.v("onpostexecute", "called");
			JSONObject jsonob = null;
			try {
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonob = jsonArray.getJSONObject(i);

					JSONObject geometry = jsonob.getJSONObject("geometry");
					JSONObject location = geometry.getJSONObject("location");

					Double lat = location.getDouble("lat");
					Double lng = location.getDouble("lng");
					String name = jsonob.getString("name");
					String address = jsonob.getString("vicinity");
					
					myMap.addMarker(new MarkerOptions()
							.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.mappin))
							.anchor(0.0f, 1.0f)
							.position(new LatLng(lat, lng))
							.title(name)
							.snippet(address)
							);

					Log.v("postexecute", name + " lat " + lat + "lng " + lng
							+ " " + address);
				}
			} catch (JSONException e) {
				Log.e("onpostexecute pdi", "error nel for");
				e.printStackTrace();
			}

		}

		@Override
		protected Void doInBackground(Void... params) {

			String lat = "40.7738125";
			String lon = "14.7932548";
			String radius = "500";
			String types = "food";
			String urlRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
					+ lat
					+ ","
					+ lon
					+ "&radius="
					+ radius
					+ "&types="
					+ types
					+ "&sensor=false&key="
					+ getString(R.string.google_places_api_key);
			JSONObject obj = null;

			try {
				obj = getJSONObject(urlRequest);
				jsonArray = obj.getJSONArray("results");

			} catch (Exception e) {
				e.printStackTrace();
				Log.e("JSON Error", obj.toString());
			}
			return null;
		}

	}
}