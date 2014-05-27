package com.chb.helloworld.memoryinfo;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.text.format.Formatter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.chb.helloworld.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by renen-inc_hempel on 14-5-27.
 */

public class BrowseProcessInfoActivity extends Activity implements OnItemClickListener {

	private static String TAG = "ProcessInfo";
	private static final int KILL_PORCESS = 1;
	private static final int SEARCH_RUNNING_APP = 2;

	private ActivityManager mActivityManager = null;
	private Activity mActivity = null;
	// ProcessInfo Model类 用来保存所有进程信息
	private List<ProcessInfo> processInfoList = null;

	private ListView listviewProcess;
	private TextView tvTotalProcessNo;
	private TextView tvTotalAvailableMem;

	private static String availMemStr = "";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meminfo_process_list);
		mActivity = this;

		listviewProcess = (ListView) findViewById(R.id.listviewProcess);
		listviewProcess.setOnItemClickListener(this);

		tvTotalProcessNo = (TextView) findViewById(R.id.tvTotalProcessNo);
		tvTotalProcessNo.setText("当前系统进程共有：");
		tvTotalAvailableMem = (TextView) findViewById(R.id.tvTotalAvailableMem);
		tvTotalAvailableMem.setText("当前系统可用内存共有：");

		this.registerForContextMenu(listviewProcess);


		new Thread(new Runnable() {
			@Override
			public void run() {

				//获得ActivityManager服务的对象
				mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
				ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
				//获得系统可用内存，保存在MemoryInfo对象上
				mActivityManager.getMemoryInfo(memoryInfo);
				availMemStr = Formatter.formatFileSize(BrowseProcessInfoActivity.this, memoryInfo.availMem);


						// 获得ActivityManager服务的对象
				mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
				// 获得系统进程信息
				getRunningAppProcessInfo();

				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// 为ListView构建适配器对象
						BrowseProcessInfoAdapter mprocessInfoAdapter = new BrowseProcessInfoAdapter(getApplicationContext(), processInfoList);
						listviewProcess.setAdapter(mprocessInfoAdapter);

						tvTotalAvailableMem.setText("当前系统可用内存共有：" + availMemStr);
						tvTotalProcessNo.setText("当前系统进程共有：" + processInfoList.size());
					}
				});

			}
		}).start();


	}

	//杀死该进程，并且刷新
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
//		new AlertDialog.Builder(this).setMessage("是否杀死该进程")
//				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						//杀死该进程，释放进程占用的空间
//						mActivityManager.killBackgroundProcesses(processInfoList.get(position).getProcessName());
//						//刷新界面
//						getRunningAppProcessInfo();
//						BrowseProcessInfoAdapter mprocessInfoAdapter = new BrowseProcessInfoAdapter(BrowseProcessInfoActivity.this, processInfoList);
//						listviewProcess.setAdapter(mprocessInfoAdapter);
//						tvTotalProcessNo.setText("当前系统进程共有：" + processInfoList.size());
//
//					}
//				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.cancel();
//			}
//		}).create().show();
		Bundle bundle = new Bundle();
		bundle.putInt("pid", processInfoList.get(position).getPid());
		bundle.putInt("uid", processInfoList.get(position).getUid());
		bundle.putInt("memSize", processInfoList.get(position).getMemSize());
		bundle.putString("processName", processInfoList.get(position).getProcessName());

		Intent intent = new Intent(this, ProcessDetailInfoActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);

	}

	// 获得系统进程信息
	private void getRunningAppProcessInfo() {
		// ProcessInfo Model类   用来保存所有进程信息
		processInfoList = new ArrayList<ProcessInfo>();

		// 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
		List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
				                                                             .getRunningAppProcesses();

		for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
			// 进程ID号
			int pid = appProcessInfo.pid;
			// 用户ID 类似于Linux的权限不同，ID也就不同 比如 root等
			int uid = appProcessInfo.uid;
			// 进程名，默认是包名或者由属性android：process=""指定
			String processName = appProcessInfo.processName;
			// 获得该进程占用的内存
			int[] myMempid = new int[]{pid};
			// 此MemoryInfo位于android.os.Debug.MemoryInfo包中，用来统计进程的内存信息
			Debug.MemoryInfo[] memoryInfo = mActivityManager
					                                .getProcessMemoryInfo(myMempid);
			// 获取进程占内存用信息 kb单位
			int memSize = memoryInfo[0].dalvikPrivateDirty;

			Log.i(TAG, "processName: " + processName + "  pid: " + pid
					           + " uid:" + uid + " memorySize is -->" + memSize + "kb");
			// 构造一个ProcessInfo对象
			ProcessInfo processInfo = new ProcessInfo();
			processInfo.setPid(pid);
			processInfo.setUid(uid);
			processInfo.setMemSize(memSize);
			processInfo.setPocessName(processName);
			processInfoList.add(processInfo);

			// 获得每个进程里运行的应用程序(包),即每个应用程序的包名
			String[] packageList = appProcessInfo.pkgList;
			Log.i(TAG, "process id is " + pid + "has " + packageList.length);
			for (String pkg : packageList) {
				Log.i(TAG, "packageName " + pkg + " in process id is -->" + pid);
			}
		}
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
		menu.add(0, 0, KILL_PORCESS, "杀死该进程");
		menu.add(0, 0, SEARCH_RUNNING_APP, "运行在该进程的应用程序");
		super.onCreateContextMenu(menu, v, menuInfo);

	}

	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case KILL_PORCESS: // 杀死该进程 ， 重新加载界面
				new AlertDialog.Builder(this).setMessage("是否杀死该进程")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create().show();
				break;
			case SEARCH_RUNNING_APP: // 查看运行在该进程的应用程序信息
				break;
			default:
				break;
		}
		return super.onContextItemSelected(item);
	}

}
