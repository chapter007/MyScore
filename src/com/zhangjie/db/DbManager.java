package com.zhangjie.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class DbManager {
	
	private Context context;
	private SQLiteDatabase database;
	private DbOpenHelper helper;
	private final int BUFFER_SIZE = 400000;
	public static final String PACKAGE_NAME = "com.zhangjie.myscore";
	public static final String DB_NAME = "score.db";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME; // 在手机里存放数据库的位置
	
	public DbManager(Context context) {
		this.context=context;
		helper=new DbOpenHelper(context, DB_NAME, null, 1);
		database=helper.getWritableDatabase();
	}
	
	public void openDatabase() {
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }
	
	public void add(List<score> scores) {  
        database.beginTransaction();  //开始事务  
        try {  
            for (score score : scores) {  
                database.execSQL("INSERT INTO score VALUES(NULL,?,?, ?, ?, ?,?,?,?)", 
                		new Object[]{score.term,score.lesson, score.teacher, score.myScore,
                		score.sumScore,score.realScore,score.eveScore,score.reScore});  
            }
            database.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            database.endTransaction();    //结束事务  
        }  
    }  
	
	 public void updatemyScore(score score) {  
	        ContentValues cv = new ContentValues();  
	        cv.put("myScore", score.myScore);  
	        database.update("score", cv, "Lesson = ?", new String[]{score.lesson});  
	    }
	 
	 public void deleteOldScore(score score) {  
	        database.delete("score", "id >= ?", new String[]{String.valueOf(score.id)});  
	    }
	 
	private SQLiteDatabase openDatabase(String dbfile) {
		if (!(new File(dbfile).exists())) {
			//判断数据库文件是否存在，若不存在则返回空，否则直接打开数据库
		    return null;
		}
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
		        null);
		return db;
	}
	
	public List<score> query() {
		ArrayList<score> data=new ArrayList<score>();
		Cursor cursor=queryTheCursor();
		while (cursor.moveToNext()) {
			score mScore=new score();
			mScore.lesson=cursor.getString(cursor.getColumnIndex("Lesson"));
			data.add(mScore);
		}
		return data;
	}
	
	public Cursor queryTheCursor() {  
        Cursor c = database.rawQuery("SELECT * FROM SCORE", null);  
        return c;  
    }
	
	public void closeDatabase() {
        this.database.close();
    }
}
