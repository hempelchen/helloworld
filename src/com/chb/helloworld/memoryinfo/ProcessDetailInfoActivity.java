package com.chb.helloworld.memoryinfo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.chb.helloworld.R;

/**
 * Created by renen-inc_hempel on 14-5-27.
 */
public class ProcessDetailInfoActivity extends Activity {
	ProcessInfo processInfo = null;

	TextView tvPID;
	TextView tvUID;
	TextView tvProcessMemSize;
	TextView tvProcessName;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meminfo_process_item_detail);

		getExtras();
		initView();

	}

	public void getExtras(){
		Bundle args = this.getIntent().getExtras();
		if(args != null) {
			if(processInfo == null) {
				processInfo = new ProcessInfo();
			}
			processInfo.setPid(args.getInt("pid"));
			processInfo.setUid(args.getInt("uid"));
			processInfo.setMemSize(args.getInt("memSize"));
			processInfo.setPocessName(args.getString("processName"));

		}
	}

	public void initView() {
		tvPID = (TextView) findViewById(R.id.tvProcessPID);
		tvUID = (TextView) findViewById(R.id.tvProcessUID);
		tvProcessMemSize = (TextView) findViewById(R.id.tvProcessMemSize);
		tvProcessName = (TextView) findViewById(R.id.tvProcessName);

		tvPID.setText(processInfo.getPid() + "");
		tvUID.setText(processInfo.getUid() + "");
		tvProcessMemSize.setText(processInfo.getMemSize() + "KB");
		tvProcessName.setText(processInfo.getProcessName());
	}
}