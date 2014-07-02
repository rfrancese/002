package com.archeotour.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public abstract class JsonSearchSuper extends AsyncTask<Void, Void, Void> {

	public JsonSearchSuper() {
	}

	protected JSONObject getJSONObject(String url) throws IOException,
			MalformedURLException, JSONException {

		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();

		InputStream in = conn.getInputStream();

		try {
			StringBuilder sb = new StringBuilder();
			@SuppressWarnings("resource")
			BufferedReader r = new BufferedReader(new InputStreamReader(
					new DoneHandlerInputStream(in)));
			for (String line = r.readLine(); line != null; line = r.readLine()) {
				sb.append(line);
			}

			return new JSONObject(sb.toString());
		} finally {
			in.close();
		}
	}
}
