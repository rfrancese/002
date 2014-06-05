package com.archeotour.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper { //classe che ci aiuta nella creazione del db
	
	private final String NEWS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "  //codice sql di creazione della tabella
            + NewsMetaData.NEWS_TABLE + " (" 
            + NewsMetaData.TITLE + " text not null,"
            + NewsMetaData.TEXT + " text not null);";
	
	private final String MASTER_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "  //codice sql di creazione della tabella
            + MasterMetaData.TABLE + " (" 
            + MasterMetaData.TNAME + " text not null,"
            + MasterMetaData.CREATIONDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP);";
	
	private final String RECENT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "  //codice sql di creazione della tabella
            + RecentMetaData.TABLE + " (" 
            + RecentMetaData.SITENAME + " text not null,"
            + RecentMetaData.SITEID + " text not null);";
	
	private static final String DB_NAME = "archeotourdb";
    private static final int DB_VERSION = 1;

    public DbHelper (Context context, CursorFactory factory) {
            super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase _db) { //solo quando il db viene creato, creiamo la tabella
            _db.execSQL(NEWS_TABLE_CREATE);
            _db.execSQL(MASTER_TABLE_CREATE);
            _db.execSQL(RECENT_TABLE_CREATE);
            Log.d("stringa creazione master", MASTER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            //qui mettiamo eventuali modifiche al db, se nella nostra nuova versione della app, il db cambia numero di versione
    }
    
   
}


