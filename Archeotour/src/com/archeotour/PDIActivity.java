package com.archeotour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.archeotour.db.JsonSearchSuper;
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
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
				myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
						40.760735,14.473559), 14.0f));

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

	private class getJsonClass extends JsonSearchSuper {
		private JSONArray jsonArray;

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
							.anchor(0.0f, 1.0f).position(new LatLng(lat, lng))
							.title(name).snippet(address));

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

			
			String lat = "40.760735";
			String lon = "14.473559";
			String radius = "1000";
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
				obj = super.getJSONObject(urlRequest);
				jsonArray = obj.getJSONArray("results");

			} catch (Exception e) {
				e.printStackTrace();
				Log.e("JSON Error", obj.toString());
			}
			return null;

		}

	}

}
