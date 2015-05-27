package com.zhangjie.myscore;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, Object>> data;

	public MyAdapter(Context mContext, List<Map<String, Object>> mdata) {
		context = mContext;
		data = mdata;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView[] text = new TextView[8];
		//Log.i("position", "���ǵ�"+position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.score_info, null);
		}
		text[0] = (TextView) convertView.findViewById(R.id.Lesson_info);
		text[1] = (TextView) convertView.findViewById(R.id.teacher);
		text[2] = (TextView) convertView.findViewById(R.id.My_Score);
		text[3] = (TextView) convertView.findViewById(R.id.Sum_Score);
		text[4] = (TextView) convertView.findViewById(R.id.Real_Score);
		text[5] = (TextView) convertView.findViewById(R.id.Everyday_Score);
		text[6] = (TextView) convertView.findViewById(R.id.Exp_Score);
		text[7] = (TextView) convertView.findViewById(R.id.Reget_Score);

		text[0].setText((CharSequence) data.get(position).get("lesson"));
		text[1].setText((CharSequence) data.get(position).get("teacher"));
		text[2].setText((CharSequence) data.get(position).get("my"));
		text[3].setText((CharSequence) data.get(position).get("sum"));
		text[4].setText((CharSequence) data.get(position).get("real"));
		text[5].setText((CharSequence) data.get(position).get("everyday"));
		text[6].setText((CharSequence) data.get(position).get("exp"));
		text[7].setText((CharSequence) data.get(position).get("reget"));
		return convertView;
	}

}
