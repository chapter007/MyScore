package com.zhangjie.myscore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.zhangjie.db.DbManager;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.R.integer;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	protected static final int GET = 0x11;
	private String Score_Info;
	private EditText EditText1, EditText2;
	private CheckBox remeber;
	private Button getScore;
	private Spinner year;
	private static String viewstate, viewstategenerator, eventValidation,
			years;
	private List<String> list;
	private ArrayAdapter adapter;
	private SharedPreferences service;
	private String url = "http://211.70.149.134:8080/stud_score/brow_stud_score.aspx";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		EditText1 = (EditText) findViewById(R.id.TextBox1);
		EditText2 = (EditText) findViewById(R.id.TextBox2);
		year = (Spinner) findViewById(R.id.year);
		remeber = (CheckBox) findViewById(R.id.remeber);
		getScore = (Button) findViewById(R.id.getScore);
		adapter = ArrayAdapter.createFromResource(MainActivity.this,
				R.array.xn, R.layout.myspinner);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		year.setAdapter(adapter);
		year.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
		
		service = PreferenceManager.getDefaultSharedPreferences(this);
		if (service.getBoolean("check", true)) {
			remeber.setChecked(true);
			EditText1.setText(service.getString("xh", ""));
			EditText2.setText(service.getString("sf", ""));
		}

		EditText1.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable s) {
				remeber.setChecked(false);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
			}
		});
		
		remeber.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String xh, sf;
				xh = EditText1.getText().toString();
				sf = EditText2.getText().toString();
				service.edit().putBoolean("check", remeber.isChecked())
						.commit();
				if (xh.contains("1")) {
					service.edit().putString("xh", xh).commit();
					service.edit().putString("sf", sf).commit();
				} else {
					Toast.makeText(MainActivity.this, "请把信息补完整", 3000).show();
				}
			}
		});

		new Thread(new Runnable() {
			@Override
			public void run() {
				getViewState(url);
			}
		}).start();

		getScore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(EditText1.getText())
						|| TextUtils.isEmpty(EditText2.getText())) {
					Toast.makeText(MainActivity.this, "请把信息补完整",
							Toast.LENGTH_SHORT).show();
					;
				} else {
					new getScore().execute();
				}
			}
		});

	}

	class SpinnerXMLSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			years = (String) adapter.getItem(arg2);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}

	}

	public void getViewState(String url) {
		try {
			Document document=Jsoup.connect(url).get();
			viewstate=document.getElementById("__VIEWSTATE").attr("value");
			eventValidation=document.getElementById("__EVENTVALIDATION").attr("value");
			Element xn=document.getElementById("drop_xn");
			/*Elements options=xn.getElementsByTag("option");
			for (Element option:options) {
				if (option.attr("value")!="") {
					String sxn=option.attr("value");
					Log.i("xn", sxn);
					list.add(sxn);
				}
			}*/
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public String queryStringForPost(String url, String TextBox1,
			String TextBox2) throws IOException {
		new HttpPost(url);
		Log.i("text", TextBox1 + TextBox2);
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
		parmas.add(new BasicNameValuePair("drop_xn", years));
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

	/*public void getFileFromBytes(String name, String path) {
		byte[] b = name.getBytes();
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(path);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}*/

	private ProgressDialog progressDialog;

	private class getScore extends AsyncTask<integer, Integer, String> {

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(MainActivity.this, "正在查询成绩",
					"如果出现问题请返回重试一下");
			super.onPreExecute();
		}

		// 执行后台数据更新，不能在这里执行任何ui有关的操作
		protected String doInBackground(integer... params) {

			String TextBox1 = EditText1.getText().toString();
			String TextBox2 = EditText2.getText().toString();

			try {
				Score_Info = queryStringForPost(url, TextBox1, TextBox2);
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
				Toast.makeText(MainActivity.this, "有点问题，重试一下吧",
						Toast.LENGTH_SHORT).show();
			} else {
				Intent intent = new Intent(MainActivity.this, MyScore.class);
				intent.putExtra("Score_Info", Score_Info);
				intent.putExtra("year", years);
				startActivity(intent);
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
}
