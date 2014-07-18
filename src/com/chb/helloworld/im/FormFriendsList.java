package com.chb.helloworld.im;

import android.app.Activity;
import android.os.Bundle;
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new Thread(new Runnable() {
			@Override
			public void run() {
				getData();
			}
		}).start();
	}


	public List<RosterEntry> getData() {
		Roster roster = XmppTool.getConnection().getRoster();
		List<RosterEntry> list = new ArrayList<RosterEntry>();
		Collection<RosterEntry> rosterEntries = roster.getEntries();
		Iterator<RosterEntry> iterator = rosterEntries.iterator();
		while (iterator.hasNext()){
			list.add(iterator.next());
		}

		return list;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		if(XmppTool.getConnection().isConnected()){
			XmppTool.closeConnection();
		}

	}
}