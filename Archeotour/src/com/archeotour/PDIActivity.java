package com.archeotour;

import java.util.List;

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
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class PDIActivity extends FragmentActivity implements
		OnMarkerClickListener, OnMapLongClickListener {

	private static GoogleMap myMap;
	double[] coords;
	PolylineOptions rectOptions;
	Polyline polyline;
	boolean markerClicked;

	Geocoder geocoder;
	String bestProvider;
	List<Address> user = null;
	double lat;
	double lng;
	Activity activity = this;

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		coords = getLocation(this);

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
				double la, lo;
				lo = coords[0];
				la = coords[1];
				myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
						la, lo), 14.0f));
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
		private JSONArray jsonArrayGoogle;
		private JSONArray jsonArrayMyDB;

		protected void onPostExecute(Void result) {
			Log.v("onpostexecute", "called");
			JSONObject jsonob = null;
			String name;
			String address;
			double lat;
			double lng;
			try {
				// aggiungo i segnalini dei POI Google alla mappa --- BEGIN
				for (int i = 0; i < jsonArrayGoogle.length(); i++) {
					jsonob = jsonArrayGoogle.getJSONObject(i);
					JSONObject geometry = jsonob.getJSONObject("geometry");
					JSONObject location = geometry.getJSONObject("location");

					lat = location.getDouble("lat");
					lng = location.getDouble("lng");
					name = jsonob.getString("name");
					address = jsonob.getString("vicinity");

					myMap.addMarker(new MarkerOptions()
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.mappin))
							.anchor(0.0f, 1.0f).position(new LatLng(lat, lng))
							.title(name).snippet(address));

					Log.v("postexecute", name + " lat " + lat + "lng " + lng
							+ " " + address);
				}// aggiungo i segnalini dei POI Google alla mappa --- END

				// aggiungo i miei segnalini alla mappa ---- BEGIN
				for (int i = 0; i < jsonArrayMyDB.length(); i++) {
					jsonob = jsonArrayMyDB.getJSONObject(i);
					lat = jsonob.getDouble("lat");
					lng = jsonob.getDouble("lon");
					name = jsonob.getString("nome");
					address = jsonob.getString("citta");
					myMap.addMarker(new MarkerOptions()
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.mappintemple))
							.anchor(0.0f, 1.0f).position(new LatLng(lat, lng))
							.title(name).snippet(address));

					Log.v("postexecute", name + " lat " + lat + "lng " + lng
							+ " " + address);

				}
				// aggiungo i miei segnalini alla mappa ---- END
			} catch (JSONException e) {
				Log.e("onpostexecute pdi", "error nel for");
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			if (coords==null){
				coords = getLocation(activity);
			}
			
			double la, lo;
			lo = coords[0];
			la = coords[1];
			String lat = String.valueOf(la);
			String lon = String.valueOf(lo);
			String radius = "1000";
			String types = "food";
			String urlRequestGoogle = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
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
			String urlRequestMyDB = "http://archeotour.altervista.org/DB_interface/interrogate.php?request=getmapinfo";

			try {
				obj = super.getJSONObject(urlRequestGoogle);
				jsonArrayGoogle = obj.getJSONArray("results");

				obj = super.getJSONObject(urlRequestMyDB);
				jsonArrayMyDB = obj.getJSONArray("sito");

			} catch (Exception e) {
				e.printStackTrace();
				Log.e("JSON Error", obj.toString());
			}
			return null;

		}

	}

	static double[] getLocation(Activity a) {

		double longitude, latitude;
		Activity activity = a;

		Log.i("getLocation", "getLocation");
		LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = lm.getBestProvider(criteria, false);
		Log.v("getLocation", "il provider selezionato è: " + provider);
		Location location = lm.getLastKnownLocation(provider);

		Log.i("getLocation", "location created");
		if (location == null) {
			Log.e("getLocation", "la location è null");
			return null;
		}
		longitude = (double) location.getLongitude();
		latitude = (double) location.getLatitude();
		Log.v("getLocation", longitude + " " + latitude);
		double[] toret = new double[2];
		toret[0] = longitude;
		toret[1] = latitude;

		// TODO: gestire gli spostamenti.
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				if (location == null) {
					Log.e("onLocationChanged", "la location è null");
					return;
				}
				double longitude, latitude;
				longitude = location.getLongitude();
				latitude = location.getLatitude();
				Log.v("spostato", longitude + " " + latitude);

			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			@Override
			public void onProviderEnabled(String provider) {
			}

			@Override
			public void onProviderDisabled(String provider) {
			}
		};

		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10,
				locationListener);
		return toret;
	}

}
