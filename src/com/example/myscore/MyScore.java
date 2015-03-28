package com.example.myscore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;

public class MyScore extends BaseActivity{
	
	private String Score_Info,Personal_info,Lesson_info;
	private WebView Simple_Info_View;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mLayout=R.layout.my_score;
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.my_score);
		Simple_Info_View=(WebView) findViewById(R.id.Simple_Info_View);
		
		Intent intent=getIntent();
		Score_Info=intent.getStringExtra("Score_Info");
		Pattern pattern=Pattern.compile("<span.id=\"Label1\".+</span>");
		Matcher matcher=pattern.matcher(Score_Info);
		if (matcher.find()) {
			Personal_info=matcher.group().replaceAll("<span.id=\"Label1\".+\">|&nbsp;|</font></span>", "");
			Log.i("personal_info:", ""+Personal_info);
			String title=Personal_info.replaceAll("ѧ��.{16}|�Ա�.{2}|ѧԺ.+?רҵ.|�༶.|��ǰ���ڼ�.", "");
			title=title+"��";
			getSupportActionBar().setTitle(title);
		}
		pattern=Pattern.compile("<td>.+?</td>");
		matcher=pattern.matcher(Score_Info);
		if (matcher.find()) {
			Lesson_info=matcher.group();
			Log.i("lesson_info:", Lesson_info);
		}
		pattern=Pattern.compile("<table.c[\\S\\s]+\\s{2}</table>\\s+</div>");
		matcher=pattern.matcher(Score_Info);
		
		
		if (matcher.find()) {
			String tem=matcher.group().replaceAll("<font.face=\".+?size=\"3\">|<font.face=\"΢���ź�\" color=\"White\".size=\"3\">|<td><font.face.+?\\d{8}</font></td>|<th.scope.+?�γ̺�</b>","");	
			Log.i("tem", tem);
			Simple_Info_View.loadDataWithBaseURL("", tem, "text/html", "utf-8", "");
		}
		
	}
	
	
	@Override
	protected View getSwipeView() {
		Log.i("Myscore_getSwipeview", "1");
		return findViewById(R.id.my_score);
	}
}
