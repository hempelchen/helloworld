package com.chb.addon.launchmode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by renen-inc_hempel on 14-4-2.
 */
public class ActB extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("actB");
		TextView textView = new TextView(this);
		textView.setText(this + "");
		TextView textView2 = new TextView(this);
		textView2.setText("task id: "+this.getTaskId());
		Button button=new Button(this);
		button.setText("go actA");
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(ActB.this, ActA.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(textView);
		layout.addView(textView2);
		layout.addView(button);
		this.setContentView(layout);
	}

	@Override
	protected void onDestroy(){
//		Toast.makeText(this, this + "", Toast.LENGTH_SHORT).show();
		Log.e("CHB", this+"");
	}
}