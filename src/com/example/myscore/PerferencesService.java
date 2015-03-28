package com.example.myscore;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PerferencesService {
	private Context context;
	
	public PerferencesService(Context mContext){
		context=mContext;
	}
	
	public void save(int xh,String sf,String check) {
		SharedPreferences preferences = context.getSharedPreferences("info", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt("xh", xh);
		editor.putString("sf", sf);
		editor.putString("check", check);
		editor.commit();
	}
	
	public Map<String, String> getpreferences() {
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences("info", Context.MODE_PRIVATE);
		params.put("sf",preferences.getString("sf", ""));
		params.put("check", preferences.getString("check", "false"));
		params.put("xh",String.valueOf(preferences.getInt("xh",0)));
		return params;
		
	}
}
