package com.chb.helloworld.memoryinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.chb.helloworld.R;

import java.util.List;

public class BrowseProcessInfoAdapter extends BaseAdapter {

	private List<ProcessInfo> mlistProcessInfo = null;

	LayoutInflater infater = null;

	public BrowseProcessInfoAdapter(Context context, List<ProcessInfo> apps) {
		infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mlistProcessInfo = apps;
	}

	@Override
	public int getCount() {
		System.out.println("size" + mlistProcessInfo.size());
		return mlistProcessInfo.size();
	}

	@Override
	public Object getItem(int position) {
		return mlistProcessInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup arg2) {
		System.out.println("getView at " + position);
		View view = null;
		ViewHolder holder = null;
		if (convertview == null || convertview.getTag() == null) {
			view = infater.inflate(R.layout.meminfo_process_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertview;
			holder = (ViewHolder) convertview.getTag();
		}
		ProcessInfo processInfo = (ProcessInfo) getItem(position);
		holder.tvPID.setText(processInfo.getPid() + "");
		holder.tvUID.setText(processInfo.getUid() + "");
		holder.tvProcessMemSize.setText(processInfo.getMemSize() + "KB");
		holder.tvProcessName.setText(processInfo.getProcessName());
		holder.tvApplicationName.setText(processInfo.getApplicationName());
		holder.tvIsSystemApp.setText((processInfo.getIsSystemApp()?"是":"否"));
		holder.tvIsDebugable.setText((processInfo.getIsDebugable()?"是":"否"));
		holder.ivApplicationIcon.setImageDrawable(processInfo.getApplicationIcon());

		return view;
	}

	class ViewHolder {
		TextView tvPID;
		TextView tvUID;
		TextView tvProcessMemSize;
		TextView tvProcessName;
		TextView tvApplicationName;
		TextView tvIsSystemApp;
		TextView tvIsDebugable;
		ImageView ivApplicationIcon;

		public ViewHolder(View view) {
			this.tvPID = (TextView) view.findViewById(R.id.tvProcessPID);
			this.tvUID = (TextView) view.findViewById(R.id.tvProcessUID);
			this.tvProcessMemSize = (TextView) view.findViewById(R.id.tvProcessMemSize);
			this.tvProcessName = (TextView) view.findViewById(R.id.tvProcessName);
			this.tvApplicationName = (TextView) view.findViewById(R.id.tvApplicationName);
			this.tvIsSystemApp = (TextView) view.findViewById(R.id.tvIsSystemApp);
			this.tvIsDebugable = (TextView) view.findViewById(R.id.tvIsDebugable);
			this.ivApplicationIcon = (ImageView) view.findViewById(R.id.ivApplicationIcon);
		}
	}

}