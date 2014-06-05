package com.archeotour.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MyDatabase {
	private SQLiteDatabase mDb;
	private DbHelper helper;

	public MyDatabase(Context ctx) {
		helper = new DbHelper(ctx, null);
		// quando istanziamo questa classe, istanziamo anche l'helper

	}

	public void open() { // il database su cui agiamo è leggibile/scrivibile
		mDb = helper.getWritableDatabase();
	}

	public void close() { // chiudiamo il database su cui agiamo
		mDb.close();
	}

	// i seguenti 2 metodi servono per la lettura/scrittura del db. aggiungete e
	// modificate a discrezione

	public void insertNews(String title, String text) {
		// metodo per inserire i dati
		ContentValues cv = new ContentValues();
		cv.put(NewsMetaData.TITLE, title);
		cv.put(NewsMetaData.TEXT, text);
		mDb.insert(NewsMetaData.NEWS_TABLE, null, cv);
		Log.v("MyDatabase", "news inserita" + title);

		ContentValues cv2 = new ContentValues();
		cv2.put(MasterMetaData.TNAME, NewsMetaData.NEWS_TABLE);
		Log.v("inserisco", cv2.toString());

		if (Exists(NewsMetaData.NEWS_TABLE)) {
			mDb.delete(MasterMetaData.TABLE, MasterMetaData.TNAME + "=?",
					new String[] { NewsMetaData.NEWS_TABLE });
		}

		mDb.insert(MasterMetaData.TABLE, null, cv2);

	}

	public void insertRecent(String name, String id) {
		// metodo per inserire i dati
		ContentValues cv = new ContentValues();
		cv.put(RecentMetaData.SITENAME, name);
		cv.put(RecentMetaData.SITEID, id);
		mDb.insert(RecentMetaData.TABLE, null, cv);
		Log.v("MyDatabase", "ricerca recente aggiunta");

	}

	private boolean Exists(String name) {
		Cursor cursor = mDb.rawQuery("select * from " + MasterMetaData.TABLE
				+ " where " + MasterMetaData.TNAME + " = '"
				+ NewsMetaData.NEWS_TABLE + "'", null);
		// new String[] { name });
		Log.v("MyDatabase ",
				"numero di risultati " + String.valueOf(cursor.getCount()));
		boolean exists = (cursor.getCount() > 0);
		cursor.close();
		return exists;
	}

	public Cursor fetchNews() {
		// metodo per fare la query di tutti i dati delle news
		return mDb.query(NewsMetaData.NEWS_TABLE, null, null, null, null, null,
				null);
	}

	public Cursor fetchRecentSearch() {
		// metodo per fare la query di tutti i dati delle ricerche recenti
		return mDb.query(RecentMetaData.TABLE, null, null, null, null, null,
				null);
	}

	public Cursor fetchMaster() {
		// metodo per fare la query di tutti i dati della master table
		return mDb.query(MasterMetaData.TABLE, null, null, null, null, null,
				null);
	}

	public Cursor fetchTimeDifference() {
		// String query =
		// "SELECT (strftime('%s','now') - strftime('%s','')) / 60;";

		String query = "SELECT (strftime('%s','now') - strftime('%s',"
				+ " SELECT " + MasterMetaData.CREATIONDATE + " FROM "
				+ MasterMetaData.TABLE + ")) / 60;";
		Log.v("fetchTimeDifference", "query: " + query);
		return mDb.rawQuery(query, null);
	}

}
