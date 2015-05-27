package com.zhangjie.myscore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;

public class MyScore extends ActionBarActivity {

	private String Score_Info;
	private ListView score;
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_score);
		Intent intent = getIntent();
		Score_Info = intent.getStringExtra("Score_Info");
		score = (ListView) findViewById(R.id.score);
		MyAdapter adapter=new MyAdapter(this, data);
		
		Document doc = Jsoup.parse(Score_Info);
		String info = doc.getElementById("Label1").text();
		
		String title = info.replaceAll("学号.{20}|性别.*专业.|.{2}班级.|.{2}当前.*", "");
		getSupportActionBar().setTitle(title);
		Elements elements = doc.getElementsByAttributeValue("align", "left");
		for (Element e : elements) {
			String t = e.getElementsByTag("td").text();
			Map<String, Object> map = new HashMap<String, Object>();
			String[] s = t.split("\\s");
			Log.i("length", ""+s.length);
			switch (s.length) {
			case 10:
				map.put("lesson", s[3] + " " + s[4] + "学分" + " " + s[5]);
				map.put("teacher", s[7]);
				map.put("my", s[8]);
				map.put("sum", "总评：" + s[9]);
				data.add(map);
				break;
			case 11:
				map.put("lesson", s[3] + " " + s[4] + "学分" + " " + s[5]);
				map.put("teacher", s[7]);
				map.put("my", s[8]);
				map.put("sum", "总评：" + s[9]);
				map.put("real", "期末：" + s[10]);
				data.add(map);
				break;
			case 12:
				map.put("lesson", s[3] + " " + s[4] + "学分" + " " + s[5]);
				map.put("teacher", s[7]);
				map.put("my", s[8]);
				map.put("sum", "总评：" + s[9]);
				map.put("real", "期末：" + s[10]);
				map.put("everyday", "平时：" + s[11]);
				data.add(map);
				break;
			}
		}
		score.setAdapter(adapter);
	}

	/*@Override
	protected View getSwipeView() {
		return findViewById(R.id.my_score);
	}*/
}
