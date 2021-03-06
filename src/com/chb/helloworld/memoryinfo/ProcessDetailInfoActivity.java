package com.chb.helloworld.memoryinfo;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.chb.helloworld.R;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by renen-inc_hempel on 14-5-27.
 */
public class ProcessDetailInfoActivity extends Activity {
	ProcessInfo processInfo = null;

	TextView tvPID;
	TextView tvUID;
	TextView tvProcessMemSize;
	TextView tvProcessName;
	TextView tvApplicationName;
	TextView tvIsSystemApp;
	TextView tvIsDebugable;
	ImageView ivApplicationIcon;

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

			String processName = args.getString("processName");
			if(!TextUtils.isEmpty(processName)) {
				getProcessMoreInfo(processInfo.getProcessName());
			}
		}
	}

	public void initView() {
		tvPID = (TextView) findViewById(R.id.tvProcessPID);
		tvUID = (TextView) findViewById(R.id.tvProcessUID);
		tvProcessMemSize = (TextView) findViewById(R.id.tvProcessMemSize);
		tvProcessName = (TextView) findViewById(R.id.tvProcessName);
		tvApplicationName = (TextView) findViewById(R.id.tvApplicationName);
		tvIsSystemApp = (TextView) findViewById(R.id.tvIsSystemApp);
		tvIsDebugable = (TextView) findViewById(R.id.tvIsDebugable);
		ivApplicationIcon = (ImageView) findViewById(R.id.ivApplicationIcon);


		tvPID.setText(processInfo.getPid() + "");
		tvUID.setText(processInfo.getUid() + "");
		tvProcessMemSize.setText(processInfo.getMemSize() + "KB");
		tvProcessName.setText(processInfo.getProcessName());
		tvApplicationName.setText(processInfo.getApplicationName());
		tvIsSystemApp.setText((processInfo.getIsSystemApp()?"是":"否"));
		tvIsDebugable.setText((processInfo.getIsDebugable()?"是":"否"));
		ivApplicationIcon.setImageDrawable(processInfo.getApplicationIcon());

	}

	public void getProcessMoreInfo(String packageName) {
		try {
			PackageManager pm = getPackageManager();
			PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

			if(processInfo == null) {
				processInfo = new ProcessInfo();
			}
			processInfo.setApplicationName(packageInfo.applicationInfo.loadLabel(pm).toString());
			processInfo.setIsSystemApp(((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM) == 0 ? false:true));
			processInfo.setIsDebugable(((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_DEBUGGABLE) == 0 ? false:true));
			processInfo.setApplicationIcon( packageInfo.applicationInfo.loadIcon(pm));

			Signature[] signs = packageInfo.signatures;
			Signature sign = signs[0];
			parseSignature(sign.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parseSignature(byte[] signature) {
		try {
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signature));
			String pubKey = cert.getPublicKey().toString();
			String signNumber = cert.getSerialNumber().toString();
			Log.d("CHB", "pubKey  = " + pubKey);
			Log.d("CHB", "signNumber  = " + signNumber);
		} catch (CertificateException e) {
			e.printStackTrace();
		}
	}
}