package com.chb.helloworld.im;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.chb.helloworld.R;
import com.chb.helloworld.im.util.XmppTool;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created on 2014-07-18
 *
 * @author hempel.chen@hotmail.com
 */

public class FormFriendsList extends Activity {
	private List<RosterEntry> rosterList;
	private ListView listView;
	private IMFriendListAdapter adapter;

	private final int MSG_TO_GETDATA = 1;
	private final int MSG_TO_SETUI = 2;
	private final int MSG_TO_UPDATEUI = 3;



	Handler handler = new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
				case MSG_TO_GETDATA:
					getData();
					break;

				case MSG_TO_SETUI:
					setUI();
					break;

				case MSG_TO_UPDATEUI:
					break;
			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.im_formfriendslist);
		listView = (ListView)findViewById(R.id.friendslist);

//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				getData();
//			}
//		}).start();
		handler.sendEmptyMessage(MSG_TO_GETDATA);
	}


	public List<RosterEntry> getData() {
		Roster roster = XmppTool.getConnection().getRoster();
		rosterList = new ArrayList<RosterEntry>();
		Collection<RosterEntry> rosterEntries = roster.getEntries();
		Iterator<RosterEntry> iterator = rosterEntries.iterator();
		while (iterator.hasNext()){
			rosterList.add(iterator.next());
		}
		handler.sendEmptyMessage(MSG_TO_SETUI);
		return rosterList;
	}

	public void setUI(){
		adapter = new IMFriendListAdapter(this);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		if(XmppTool.getConnection().isConnected()){
			XmppTool.closeConnection();
		}

	}

	class IMFriendListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		IMFriendListAdapter(Context context){
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount(){
			return rosterList.size();
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent){

			ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = (View) mInflater.inflate(R.layout.im_friendslist_item, null);

				holder.header = (ImageView)convertView.findViewById(R.id.friendslist_header);
				holder.jid = (TextView)convertView.findViewById(R.id.friendslist_jid);
				holder.nickName = (TextView)convertView.findViewById(R.id.friendslist_name);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder)convertView.getTag();
			}
			holder.header.setImageDrawable(getResources().getDrawable(R.drawable.default_header));
			holder.jid.setText(rosterList.get(position).getUser());
			holder.nickName.setText(rosterList.get(position).getName());
			System.out.println("getView position=" + position + ", view="+convertView);

			return convertView;
		}

	}

	class ViewHolder {
		public ImageView header;
		public TextView jid;
		public TextView nickName;

	}
}

