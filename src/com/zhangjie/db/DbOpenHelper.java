package com.zhangjie.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper{

	public DbOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {//第一次调用的时候就会用它来创建数据库
		db.execSQL("CREATE TABLE IF NOT EXISTS score" +  
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT,Term VARCHAR, Lesson VARCHAR,Teacher VARCHAR, "
                + "myScore VARCHAR, sumScore VARCHAR,realScore VARCHAR,eveScore VARCHAR,reScore VARCHAR)");  
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("ALTER TABLE score ADD COLUMN other STRING");
	}

}
