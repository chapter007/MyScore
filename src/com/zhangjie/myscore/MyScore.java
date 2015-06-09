package com.zhangjie.myscore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zhangjie.db.DbManager;
import com.zhangjie.db.score;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyScore extends SwipeBackActivity {

	private String Score_Info, zTerm, viewstate, viewstategenerator,
			eventValidation, xh,sf;
	private ListView score;
	private TextView notify;
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	private DbManager manager;
	private SharedPreferences preference;
	private String url = "http://211.70.149.134:8080/stud_score/brow_stud_score.aspx";
	private Document doc ;
	private MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_score);
		Intent intent = getIntent();
		Score_Info = intent.getStringExtra("Score_Info");
		zTerm = intent.getStringExtra("year");
		xh=intent.getStringExtra("xh");
		sf=intent.getStringExtra("sf");
		eventValidation=intent.getStringExtra("eventValidation");
		viewstate=intent.getStringExtra("viewstate");
		viewstategenerator=intent.getStringExtra("viewstategenerator");
		
		score = (ListView) findViewById(R.id.score);
		notify=(TextView) findViewById(R.id.notify);
		manager = new DbManager(MyScore.this);
		preference = PreferenceManager
				.getDefaultSharedPreferences(MyScore.this);
		adapter = new MyAdapter(this, data);
		boolean checkDB=manager.queryTerm(zTerm);
		Log.i("checkDb", ""+!checkDB);
		if (!checkDB) {
			//从数据库读取数据
			getDbInfo();
			adapter.notifyDataSetChanged();
		}else {
			//从网上找数据
			Log.i("从网上找数据", "");
			new getScore().execute();
			
		}
		score.setAdapter(adapter);
	}

	public void add() {
		ArrayList<score> scores = new ArrayList<score>();
		for (int i = 0; i < data.size(); i++) {
			score score = new score();
			score.term = (String) data.get(i).get("term");
			score.lesson = (String) data.get(i).get("lesson");
			score.teacher = (String) data.get(i).get("teacher");
			score.myScore = (String) data.get(i).get("my");
			score.sumScore = (String) data.get(i).get("sum");
			score.realScore = (String) data.get(i).get("real");
			score.eveScore = (String) data.get(i).get("everyday");
			// score.reScore=Integer.parseInt(data.get(i).get("re").toString());
			scores.add(score);
		}

		manager.add(scores);
	}

	public void getWebInfo(Document doc) {
		String info = doc.getElementById("Label1").text();
		String title = info.replaceAll("学号.{20}|性别.*专业.|.{2}班级.|.{2}当前.*", "");
		getSupportActionBar().setTitle(title);
		Elements elements = doc.getElementsByAttributeValue("align", "left");
		for (Element e : elements) {
			String t = e.getElementsByTag("td").text();
			Map<String, Object> map = new HashMap<String, Object>();
			String[] s = t.split("\\s");

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
		adapter.notifyDataSetChanged();
		Log.i("data", ""+data.isEmpty());
		if (data.isEmpty()) {
			notify.setVisibility(View.VISIBLE);
		}
		if (manager.queryTerm(zTerm)&&!data.isEmpty()) {
			Log.i("可以向数据库添加数据", zTerm);
			add();
		}
	}

	public void getDbInfo() {
		ArrayList<score> scores=new ArrayList<score>();
		scores=manager.query(zTerm);
		for (score score:scores) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("term", score.term);
			map.put("lesson", score.lesson);
			map.put("teacher", score.teacher);
			map.put("my", score.myScore);
			map.put("sum", score.sumScore);
			map.put("real", score.realScore);
			map.put("everyday", score.eveScore);
			data.add(map);
		}
		Log.i("从数据库读取数据", "DbInfo"+scores);
	}
	
	public String queryStringForPost(String url, String TextBox1,
			String TextBox2) throws IOException {
		new HttpPost(url);
		Log.i("viewstate", viewstate);
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> parmas = new ArrayList<NameValuePair>();
		parmas.add(new BasicNameValuePair("__EVENTTARGET", ""));
		parmas.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		parmas.add(new BasicNameValuePair("__VIEWSTATE", viewstate));
		parmas.add(new BasicNameValuePair("__EVENTVALIDATION", eventValidation));
		parmas.add(new BasicNameValuePair("__VIEWSTATEGENERATOR",
				viewstategenerator));
		parmas.add(new BasicNameValuePair("TextBox1", TextBox1));
		parmas.add(new BasicNameValuePair("TextBox2", TextBox2));
		parmas.add(new BasicNameValuePair("drop_xn", zTerm));
		parmas.add(new BasicNameValuePair("drop_xq", ""));
		parmas.add(new BasicNameValuePair("drop_type", "全部成绩"));
		parmas.add(new BasicNameValuePair("Button_cjcx", "查询"));
		parmas.add(new BasicNameValuePair("hid_dqszj", ""));

		httpPost.setEntity(new UrlEncodedFormEntity(parmas, HTTP.UTF_8));
		HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);

		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String strResult = EntityUtils.toString(httpResponse.getEntity());
			return strResult;
		} else {
			Log.i("httpresponse", "wrong"
					+ httpResponse.getStatusLine().toString());
		}

		return null;
	}

	private ProgressDialog progressDialog;

	private class getScore extends AsyncTask<integer, Integer, String> {

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(MyScore.this, "正在查询成绩",
					"如果出现问题请返回重试一下");
			super.onPreExecute();
		}

		// 执行后台数据更新，不能在这里执行任何ui有关的操作
		protected String doInBackground(integer... params) {

			try {
				getViewState(url);
				Score_Info = queryStringForPost(url, xh, sf);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		// 执行完后台任务更新ui
		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			progressDialog = null;
			// Log.i("score",Score_Info);
			if (Score_Info == null) {
				Toast.makeText(MyScore.this, "出了一些问题，可以重试一下",
						Toast.LENGTH_SHORT).show();
			}else {
				doc = Jsoup.parse(Score_Info);
				getWebInfo(doc);
			}

			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			progressDialog.dismiss();
			progressDialog = null;
			super.onCancelled();
		}

	}
	
	public void getViewState(String url) {
		try {
			Document document = Jsoup.connect(url).get();
			viewstate = document.getElementById("__VIEWSTATE").attr("value");
			eventValidation = document.getElementById("__EVENTVALIDATION")
					.attr("value");
			document.getElementById("drop_xn");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		manager.closeDatabase();
	}

}
