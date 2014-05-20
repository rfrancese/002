package com.archeotour;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainSiteActivity extends ActionBarActivity {
	
	private static int id_sito;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_site);
		id_sito = getIntent().getIntExtra("id_sito",0);
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
		Intent intent = new Intent(this, SiteDescriptionActivity.class);
		intent.putExtra("id_sito", id_sito);
		startActivity(intent);
	}

	public void startPDIActivity(View view) {
		Intent intent = new Intent(this, PDIActivity.class);
		startActivity(intent);
	}

	public void startNewsActivity(View view) {
		Intent intent = new Intent(this, NewsActivity.class);
		startActivity(intent);
	}

	public void startInfoActivity(View view) {
		Intent intent = new Intent(this, SiteInfoActivity.class);
		intent.putExtra("id_sito", id_sito);
		startActivity(intent);
	}

	
}
