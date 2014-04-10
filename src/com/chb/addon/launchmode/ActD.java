package com.chb.addon.launchmode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by renen-inc_hempel on 14-4-2.
 */
public class ActD extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("actD singleInstance");
		TextView textView = new TextView(this);
		textView.setText(this + "");
		TextView textView2 = new TextView(this);
		textView2.setText("task id: " + this.getTaskId() + "\n process id: " + android.os.Process.myPid());
		Button button = new Button(this);
		button.setText("go actA");
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActD.this, ActA.class);
				startActivity(intent);
			}
		});

		Button button2 = new Button(this);
		button2.setText("go actB");
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActD.this, ActB.class);
				startActivity(intent);
			}
		});

		Button button3 = new Button(this);
		button3.setText("go actC");
		button3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActD.this, ActC.class);
				startActivity(intent);
			}
		});

		Button button4 = new Button(this);
		button4.setText("go actD");
		button4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActD.this, ActD.class);
				startActivity(intent);
			}
		});

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(textView);
		layout.addView(textView2);
		layout.addView(button);
		layout.addView(button2);
		layout.addView(button3);
		layout.addView(button4);
		this.setContentView(layout);
	}


}