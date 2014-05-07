package com.chb.helloworld.net;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.chb.helloworld.R;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by renen-inc_hempel on 14-4-17.
 */
public class DNSActivity extends Activity {
	private TextView net_tx1;
	private Button net_btn1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.net);

		net_btn1 = (Button) findViewById(R.id.net_btn1);
		net_btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				testDNS();
			}
		});
	}

	//获得www.taobao.com对应的IP地址，并通过Toast的方式打印出来
	private void testDNS() {
		try {
			InetAddress inetAddress = InetAddress.getByName("www.taobao.com");
			Toast.makeText(DNSActivity.this, "\"www.taobao.com\" Address is " + inetAddress.getHostAddress(), Toast.LENGTH_LONG).show();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}


}