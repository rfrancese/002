package com.archeotour;

import com.archeotour.SingleNewsFragment.OnFragmentInteractionListener;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainSiteActivity extends ActionBarActivity implements
		OnFragmentInteractionListener {

	private static int id_sito;

	@Override
	public void onDestroy() {
		super.onDestroy();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_site);
		id_sito = getIntent().getIntExtra("id_sito", 0);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new MainSiteFragment()).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_site, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void startSiteDescriptionActivity(View view) {
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.right_container,
							new SiteDescriptionFragment()).commit();
		} else {
			Intent intent = new Intent(this, SiteDescriptionActivity.class);
			intent.putExtra("id_sito", id_sito);
			startActivity(intent);
		}
	}

	public void startPDIActivity(View view) {
		Intent intent = new Intent(this, PDIActivity.class);
		startActivity(intent);
	}

	public void startNewsActivity(View view) {
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.right_container, new NewsFragment()).commit();
		} else {
			Intent intent = new Intent(this, NewsActivity.class);
			intent.putExtra("id_sito", id_sito);
			startActivity(intent);
		}
	}

	public void startInfoActivity(View view) {
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.right_container, new SiteInfoFragment())
					.commit();
		} else {
			Intent intent = new Intent(this, SiteInfoActivity.class);
			intent.putExtra("id_sito", id_sito);
			startActivity(intent);
		}
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
	}

}
