package com.chb.helloworld;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.chb.helloworld.baidumap.BaiduMapActivity;
import com.chb.helloworld.im.FormLogin;
import com.chb.helloworld.js.MyWebviewActivity;
import com.chb.helloworld.light.LightActivity;
import com.chb.helloworld.memoryinfo.BrowseProcessInfoActivity;
import com.chb.helloworld.net.GetPostActivity;
import com.chb.helloworld.player.MediaPlayerActivity;
import com.chb.helloworld.utils.CalendarUtil;
import com.chb.helloworld.utils.ViewServer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

	private boolean mIsExit = false;

	private double latitude = 0.0;
	private double longitude = 0.0;
	private TextView tx1;
	private Button btn1;
	private String dayofWeek[] = {"日", "一", "二", "三", "四", "五", "六"};


	public Handler handler = new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
				case 1:
					Log.d("CHB", "handler msg what =1");
					break;

				case 2:
					Log.d("CHB", "handler msg what =2");
					break;

				case 3:
					Log.d("CHB", "handler msg what =3");
					break;
			}
		}
	};


	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ViewServer.get(this).addWindow(this);

		tx1 = (TextView) findViewById(R.id.tx1);
		tx1.setTextSize(18);
		tx1.setText("我是主进程\nPID = " + Process.myPid() + "\ntask id: " + this.getTaskId() );
		tx1.setTextColor(Color.YELLOW);
		setTime();

//		final String btnTitle = "Get/Post测试";
//		btn1 = (Button) findViewById(R.id.main_btn1);
//		btn1.setText(btnTitle);
		((Button) findViewById(R.id.main_btn1)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startOtherApp();
			}
		});
		((Button) findViewById(R.id.main_btn2)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startLbs();
			}
		});
		((Button) findViewById(R.id.main_btn3)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startMyWebview();
			}
		});
		((Button) findViewById(R.id.main_btn4)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startLight();
			}
		});
		((Button) findViewById(R.id.main_btn5)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startMemInfo();
			}
		});
		((Button) findViewById(R.id.main_btn6)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startGetPost();
			}
		});
		((Button) findViewById(R.id.main_btn7)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startBaiduMap();
			}
		});
		((Button) findViewById(R.id.main_btn8)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startIm();
			}
		});
		((Button) findViewById(R.id.main_btn9)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startPlayer();
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ViewServer.get(this).removeWindow(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		ViewServer.get(this).setFocusedWindow(this);
	}

	@Override
	public void onBackPressed() {
		if (mIsExit) {
			this.finish();
			System.exit(0);
			return;
		}
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				mIsExit = false;
			}
		}, 3000);
		Toast.makeText(this, "连续点击退出", Toast.LENGTH_SHORT).show();
		mIsExit = true;

	}

	private void setTime(){
		StringBuilder buf = new StringBuilder();
		CalendarUtil cu = new CalendarUtil();
		Calendar cal = Calendar.getInstance();
		Date d = new Date();

		String h=d.getHours()+":"+d.getMinutes();
		String chineseMonth = cu.getChineseMonth(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		String chineseDay = cu.getChineseDay(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		buf.append("\n当前日期：").append(cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH));
		buf.append("  星期" + dayofWeek[d.getDay()]);
		buf.append("\n当前时间：").append(h);
		buf.append("\n农历：        ").append(chineseMonth).append(chineseDay);
		System.out.println(chineseDay + "==============" + chineseMonth);
		tx1.setText(tx1.getText() + "\n" + buf + "\n");
	}

	private void startOtherApp() {
		Context context = this.getApplicationContext();
		Intent intent = new Intent(context, InnerAppActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}


	private void startLocate() {
		Log.i("LBS", "startLocate");
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, new LocationListener() {
			public void onLocationChanged(Location location) { //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
				// log it when the location changes
				if (location != null) {
					Log.i("LBS", "Location changed : Lat: "
							             + location.getLatitude() + " Lon: "
							             + location.getLongitude());
					tx1.setText("定位中...\n经度： " + location.getLongitude() + "\n纬度： " + location.getLatitude());
				}
			}

			public void onProviderDisabled(String provider) {
				// Provider被disable时触发此函数，比如GPS被关闭
				Log.i("LBS", "onProviderDisabled");
			}

			public void onProviderEnabled(String provider) {
				//  Provider被enable时触发此函数，比如GPS被打开
				Log.i("LBS", "onProviderEnabled");
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
				// Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
				Log.i("LBS", "onStatusChanged");
			}
		});
	}

	private void startLbs() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Log.i("LBS", "LocationManager.GPS_PROVIDER is enable");
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				Log.i("LBS", "LocationManager.GPS_PROVIDER Location: Lat: "
						                  + location.getLatitude() + " Lon: "
						                  + location.getLongitude());
				tx1.setText("定位中...\n经度： " + location.getLongitude() + "\n纬度： " + location.getLatitude());
			} else {
				startLbsWithNetwork(locationManager);
			}
		} else {
			Log.i("LBS", "LocationManager.GPS_PROVIDER is disable");
			startLbsWithNetwork(locationManager);
		}
	}

	private void startLbsWithNetwork(LocationManager locationManager){
		LocationListener locationListener = new LocationListener() {

			// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {

			}

			// Provider被enable时触发此函数，比如GPS被打开
			@Override
			public void onProviderEnabled(String provider) {

			}

			// Provider被disable时触发此函数，比如GPS被关闭
			@Override
			public void onProviderDisabled(String provider) {

			}

			//当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					Log.i("LBS", "LocationManager.NETWORK_PROVIDER Location changed : Lat: "
							             + location.getLatitude() + " Lon: "
							             + location.getLongitude());
					tx1.setText("定位中...\n经度： " + location.getLongitude() + "\n纬度： " + location.getLatitude());
				}
			}
		};
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (location != null) {
			latitude = location.getLatitude(); //经度
			longitude = location.getLongitude(); //纬度
			Log.i("LBS", "LocationManager.NETWORK_PROVIDER Location changed : Lat: "
					             + location.getLatitude() + " Lon: "
					             + location.getLongitude());
			tx1.setText("定位中...\n经度： " + location.getLongitude() + "\n纬度： " + location.getLatitude());
		}
	}

	private void startMyWebview() {
		Context context = this.getApplicationContext();
		Intent intent = new Intent(context, MyWebviewActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	private void startLight() {
//		Context context = this.getApplicationContext();
		Intent intent = new Intent(this, LightActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	public void setMobileDataStatus(Context context, boolean enabled)

	{

		ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		// ConnectivityManager类
		Class<?> conMgrClass = null;
		// ConnectivityManager类中的字段
		Field iConMgrField = null;
		// IConnectivityManager类的引用
		Object iConMgr = null;
		// IConnectivityManager类
		Class<?> iConMgrClass = null;
		// setMobileDataEnabled方法
		Method setMobileDataEnabledMethod = null;
		try {
			// 取得ConnectivityManager类
			conMgrClass = Class.forName(conMgr.getClass().getName());
			// 取得ConnectivityManager类中的对象Mservice
			iConMgrField = conMgrClass.getDeclaredField("mService");
			// 设置mService可访问
			iConMgrField.setAccessible(true);
			// 取得mService的实例化类IConnectivityManager
			iConMgr = iConMgrField.get(conMgr);
			// 取得IConnectivityManager类
			iConMgrClass = Class.forName(iConMgr.getClass().getName());
			// 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
			setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			// 设置setMobileDataEnabled方法是否可访问
			setMobileDataEnabledMethod.setAccessible(true);
			// 调用setMobileDataEnabled方法
			setMobileDataEnabledMethod.invoke(iConMgr, enabled);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void startMemInfo() {
//		Context context = this.getApplicationContext();
		Intent intent = new Intent(this, BrowseProcessInfoActivity.class);
		startActivity(intent);
	}


	private void startGetPost() {
		Intent intent = new Intent(this, GetPostActivity.class);
		startActivity(intent);
	}

	private void startBaiduMap() {
		Intent intent = new Intent(this, BaiduMapActivity.class);
		startActivity(intent);
	}

	private void startIm() {
		Intent intent = new Intent(this, FormLogin.class);
		startActivity(intent);
	}

	private void startPlayer() {
		Intent intent = new Intent(this, MediaPlayerActivity.class);
		startActivity(intent);
	}

}
