package com.zhangjie.myscore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;


/**
 * ��Ҫʵ�����һ���ɾ��ActivityЧ��ֻ��Ҫ�̳�SwipeBackActivity���ɣ������ǰҳ�溬��ViewPager
 * ֻ��Ҫ����SwipeBackLayout��setViewPager()��������
 * �̳�������onKeyDown    ��ǰactivity���ذ�ťֻ��ҪActivity.this.finish();����
 *
 */
public class SwipeBackActivity extends ActionBarActivity {
	protected SwipeBackLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
				R.layout.base, null);
		layout.attachToActivity(this);
	}
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
	}
	 

}
