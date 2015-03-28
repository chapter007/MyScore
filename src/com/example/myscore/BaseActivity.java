package com.example.myscore;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseActivity extends ActionBarActivity{
	
	protected int mLayout=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mLayout);
		swipeInit();
	}
	
	protected View getSwipeView() {
		return ((ViewGroup) getWindow().getDecorView()).getChildAt(0);
	}
	
	@TargetApi(21)
	private void swipeInit() {
		// Replace the view first
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		SlidingPaneLayout v = (SlidingPaneLayout) inflater.inflate(R.layout.swipe_decor_wrapper, null);
		final ViewGroup frame = (ViewGroup) v.findViewById(R.id.swipe_container);
		View swipeView = getSwipeView();
		ViewGroup decor = (ViewGroup) swipeView.getParent();
		ViewGroup.LayoutParams params = swipeView.getLayoutParams();
		decor.removeView(swipeView);
		frame.addView(swipeView);
		decor.addView(v, params);
		//decor.setBackgroundColor(0);
		
		// Elevation
		if (Build.VERSION.SDK_INT >= 21) {
			frame.setElevation(11.8f);
		} else {
			v.setShadowResource(R.drawable.panel_shadow);
		}
		
		// Swipe gesture configurations
		v.setSliderFadeColor(0);
		v.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {

			@Override
			public void onPanelSlide(View v, float percent) {
				getWindow().getDecorView().setAlpha(1.0f - percent);
			}

			@Override
			public void onPanelOpened(View p1) {
				finish();
			}

			@Override
			public void onPanelClosed(View p1) {
			}
		});
		
		// Adjust window color
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
	}
}
