package com.chb.helloworld.net;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.chb.helloworld.R;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2014-05-30
 *
 * @author hempel.chen@hotmail.com
 */

public class GetPostActivity extends Activity {
	private static final String TAG = "CHB-GET/POST";
	private Button btnTest1;
	private Button btnTest2;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getpost);
		((Button) findViewById(R.id.getpost_btn1)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				postTest("html");
			}
		});

		((Button) findViewById(R.id.getpost_btn2)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				postTest("json");
			}
		});
		((Button) findViewById(R.id.getpost_btn3)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getTest("html");
			}
		});
	}

	private void postTest(String type){
		try {
			final String SERVER_URL = "http://10.2.44.171:8080/J2EEDemo2/Greeting2"; // 定义需要获取的内容来源地址
			HttpPost request = new HttpPost(SERVER_URL); // 根据内容来源地址创建一个Http请求
			List params = new ArrayList();
			params.add(new BasicNameValuePair("type", type)); // 添加必须的参数
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); // 设置参数的编码
			HttpResponse httpResponse = new DefaultHttpClient().execute(request); // 发送请求并获取反馈
// 解析返回的内容
			if (httpResponse.getStatusLine().getStatusCode() != 404)
			{
				String result = EntityUtils.toString(httpResponse.getEntity());
				Header[] header = httpResponse.getHeaders("Content-Type");
				Log.d("CHB", header[0].toString());

				if( header[0].toString().contains("application/json")) {
					JSONArray jsonArray = new JSONArray(result);
					try {
						for(int i=0;i<jsonArray.length();i++) {
							JSONObject tempJson = jsonArray.optJSONObject(i);
							Log.d(TAG, "Id=" + tempJson.getString("Id"));
							Log.d(TAG, "number=" + tempJson.getString("number"));
						}
						Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
					} catch ( Exception e) {
						e.printStackTrace();
					}
				} else if ( header[0].toString().contains("text/html")) {
					Log.d("CHB", result);
					Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getTest(String type){
		try {
			final String SERVER_URL = "http://10.2.44.171:8080/J2EEDemo2/Greeting2"; // 定义需要获取的内容来源地址
			HttpGet request = new HttpGet(SERVER_URL); // 根据内容来源地址创建一个Http请求

			HttpResponse httpResponse = new DefaultHttpClient().execute(request); // 发送请求并获取反馈
// 解析返回的内容
			if (httpResponse.getStatusLine().getStatusCode() != 404)
			{
				String result = EntityUtils.toString(httpResponse.getEntity());
				Header[] header = httpResponse.getHeaders("Content-Type");
				Log.d("CHB", header[0].toString());

				if( header[0].toString().contains("application/json")) {
					JSONArray jsonArray = new JSONArray(result);
					try {
						for(int i=0;i<jsonArray.length();i++) {
							JSONObject tempJson = jsonArray.optJSONObject(i);
							Log.d(TAG, "Id=" + tempJson.getString("Id"));
							Log.d(TAG, "number=" + tempJson.getString("number"));
						}
						Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
					} catch ( Exception e) {
						e.printStackTrace();
					}
				} else if ( header[0].toString().contains("text/html")) {
					Log.d("CHB", result);
					Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}