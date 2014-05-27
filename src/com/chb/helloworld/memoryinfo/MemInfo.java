package com.chb.helloworld.memoryinfo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.chb.helloworld.R;

/**
 * Created by renen-inc_hempel on 14-5-27.
 */
public class MemInfo extends Activity {
	private static String TAG = "AM_MEMORYIPROCESS";

	private ActivityManager mActivityManager = null;

	private TextView tvAvailMem;
	private Button btProcessInfo;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memory_info);

		tvAvailMem = (TextView) findViewById(R.id.meminfo_tx1);
		btProcessInfo = (Button) findViewById(R.id.meminfo_btn1);
		//跳转到显示进程信息界面
		btProcessInfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MemInfo.this, BrowseProcessInfoActivity.class);
				startActivity(intent);
			}
		});

		//获得ActivityManager服务的对象
		mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		//获得可用内存信息
		String availMemStr = getSystemAvaialbeMemorySize();
		Log.i(TAG, "The Availabel Memory Size is " + availMemStr);
		//显示
		tvAvailMem.setText(availMemStr);

	}

	//获得系统可用内存信息
	private String getSystemAvaialbeMemorySize() {
		//获得MemoryInfo对象
		MemoryInfo memoryInfo = new MemoryInfo();
		//获得系统可用内存，保存在MemoryInfo对象上
		mActivityManager.getMemoryInfo(memoryInfo);
		long memSize = memoryInfo.availMem;

		//字符类型转换
		String availMemStr = formateFileSize(memSize);

		return availMemStr;
	}

	//调用系统函数，字符串转换 long -String KB/MB
	private String formateFileSize(long size) {
		return Formatter.formatFileSize(MemInfo.this, size);
	}
}