package com.zhangjie.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class DbManager {
	
	private Context context;
	private SQLiteDatabase database;
	private DbOpenHelper helper;
	private final int BUFFER_SIZE = 400000;
	public static final String PACKAGE_NAME = "com.zhangjie.myscore";
	public static final String DB_NAME = "score.db";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME; // ���ֻ��������ݿ��λ��
	
	public DbManager(Context context) {
		this.context=context;
		helper=new DbOpenHelper(context, DB_NAME, null, 1);
		database=helper.getWritableDatabase();
	}
	
	public void openDatabase() {
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }
	
	public void add(List<score> scores) {  
        database.beginTransaction();  //��ʼ����  
        try {  
            for (score score : scores) {  
                database.execSQL("INSERT INTO score VALUES(NULL,?,?,?,?, ?, ?, ?,?,?,?)", 
                		new Object[]{score.xh,score.term,score.lesson,score.lessonId,score.teacher, score.myScore,
                		score.sumScore,score.realScore,score.eveScore,score.reScore});  
            }
            database.setTransactionSuccessful();  //��������ɹ����  
        } finally {  
            database.endTransaction();    //��������  
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
			//�ж����ݿ��ļ��Ƿ���ڣ����������򷵻ؿգ�����ֱ�Ӵ����ݿ�
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
			mScore.teacher=cursor.getString(cursor.getColumnIndex("Teacher"));
			mScore.myScore=cursor.getString(cursor.getColumnIndex("myScore"));
			mScore.sumScore=cursor.getString(cursor.getColumnIndex("sumScore"));
			mScore.realScore=cursor.getString(cursor.getColumnIndex("realScore"));
			mScore.eveScore=cursor.getString(cursor.getColumnIndex("eveScore"));
			mScore.reScore=cursor.getString(cursor.getColumnIndex("reScore"));
			data.add(mScore);
		}
		return data;
	}
	
	public ArrayList<score> query(String Zterm,String xh) {
		ArrayList<score> data=new ArrayList<score>();
		String cmd=String.format("SELECT * FROM SCORE where Term = '%s' and xh = '%s'", Zterm,xh);
		Log.i("cmd", cmd);
		Cursor cursor=database.rawQuery(cmd, null);
		while (cursor.moveToNext()) {
			score mScore=new score();
			mScore.xh=cursor.getString(cursor.getColumnIndex("xh"));
			mScore.lessonId=cursor.getString(cursor.getColumnIndex("LessonID"));
			mScore.lesson=cursor.getString(cursor.getColumnIndex("Lesson"));
			mScore.teacher=cursor.getString(cursor.getColumnIndex("Teacher"));
			mScore.myScore=cursor.getString(cursor.getColumnIndex("myScore"));
			mScore.sumScore=cursor.getString(cursor.getColumnIndex("sumScore"));
			mScore.realScore=cursor.getString(cursor.getColumnIndex("realScore"));
			mScore.eveScore=cursor.getString(cursor.getColumnIndex("eveScore"));
			mScore.reScore=cursor.getString(cursor.getColumnIndex("reScore"));
			data.add(mScore);
		}
		return data;
	}
	
	public ArrayList<String> queryLessonID() {
		ArrayList<String> LessonsID=new ArrayList<String>();
		Cursor cursor=database.rawQuery("SELECT LessonID FROM SCORE", null);
		while (cursor.moveToNext()) {
			String LessonID=cursor.getString(cursor.getColumnIndex("LessonID"));
			LessonsID.add(LessonID);
		}
		return LessonsID;
	}
	
	public ArrayList<String> queryXH() {
		ArrayList<String> XHs=new ArrayList<String>();
		Cursor cursor=database.rawQuery("SELECT xh FROM SCORE", null);
		while (cursor.moveToNext()) {
			String xh=cursor.getString(cursor.getColumnIndex("xh"));
			XHs.add(xh);
		}
		return XHs;
	}
	
	public boolean queryTerm(String Zterm){
		Cursor cursor=queryTheCursor();
		String term = null;
		while (cursor.moveToNext()) {
			term=term+cursor.getString(cursor.getColumnIndex("Term"));
		}
		if (term!=null) {
			if (!term.contains(Zterm)) {//��һ�����������������
				Log.i("��һ����getwebInfo",Zterm);
				return true;
			}else {//һ����bu�������������,����ֱ�Ӷ�ȡ����
				Log.i("һ��������ֱ�Ӷ�ȡ����",Zterm);
				return false;
			}
		}
		return true;
	}
	
	public Cursor queryTheCursor() {  
        Cursor c = database.rawQuery("SELECT * FROM SCORE", null);  
        return c;
    }
	
	public void closeDatabase() {
        this.database.close();
    }
}
