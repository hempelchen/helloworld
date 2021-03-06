package com.chb.helloworld.addonfragment;

import com.chb.helloworld.InnerAppActivity;

import android.graphics.Color;
import android.os.*;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AddonFragment extends Fragment {
	private String strDes = "你看见我，说明你是通过别的应用启动我的 \n\n 我是一个新的进程\nPID = ";


	@Override
	public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle) {
		View view = layoutinflater.inflate(R.layout.test, null);
		TextView textview = (TextView)view.findViewById(R.id.addonfragment_tx1);
        String txt = getResources().getString(R.string.app_name);
        String innerActivityName = InnerAppActivity.class.getSimpleName();
		textview.setText(strDes + Process.myPid() + "\n Inner Activity name: " + innerActivityName + "\ntask id: " + this.getActivity()
                                                                                                 .getTaskId() + " \n" + txt );
		textview.setTextColor(Color.RED);
		view.setBackgroundColor(Color.GRAY);
		return view;
	}
}
