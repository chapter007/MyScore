package com.zhangjie.myscore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zhangjie.db.DbManager;
import com.zhangjie.db.score;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;

public class MyScore extends SwipeBackActivity {

	private String Score_Info,zTerm;
	private ListView score;
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	private DbManager manager;
	private SharedPreferences preference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_score);
		Intent intent = getIntent();
		Score_Info = intent.getStringExtra("Score_Info");
		score = (ListView) findViewById(R.id.score);
		manager=new DbManager(MyScore.this);
		preference=PreferenceManager.getDefaultSharedPreferences(MyScore.this);
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
			if (s.length>0) {
				zTerm=s[0];
				preference.edit().putString("term", zTerm).commit();
			}
			switch (s.length) {
			case 10:
				map.put("term", s[0]);
				map.put("lesson", s[3] + " " + s[4] + "学分" + " " + s[5]);
				map.put("teacher", s[7]);
				map.put("my", s[8]);
				map.put("sum", "总评：" + s[9]);
				data.add(map);
				break;
			case 11:
				map.put("term", s[0]);
				map.put("lesson", s[3] + " " + s[4] + "学分" + " " + s[5]);
				map.put("teacher", s[7]);
				map.put("my", s[8]);
				map.put("sum", "总评：" + s[9]);
				map.put("real", "期末：" + s[10]);
				data.add(map);
				break;
			case 12:
				map.put("term", s[0]);
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
		if (manager.query()!=null&&preference!=null&&!preference.getString("term", null).equals(zTerm)) {
			Log.i("选择了不同的学期", zTerm);
			add();
		}else {
			Log.i("第一次添加数据", zTerm);
			add();
		}
		
		score.setAdapter(adapter);
	}

	public void add(){
		ArrayList<score> scores=new ArrayList<score>();
		for (int i = 0; i < data.size(); i++) {
			score score=new score();
			score.term=(String) data.get(i).get("term");
			score.lesson=(String) data.get(i).get("lesson");
			score.teacher=(String) data.get(i).get("teacher");
			score.myScore=(String) data.get(i).get("my");
			score.sumScore=(String) data.get(i).get("sum");
			score.realScore=(String) data.get(i).get("real");
			score.eveScore=(String) data.get(i).get("everyday");
			//score.reScore=Integer.parseInt(data.get(i).get("re").toString());
			scores.add(score);
		}
		
		manager.add(scores);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		manager.closeDatabase();
	}
}
