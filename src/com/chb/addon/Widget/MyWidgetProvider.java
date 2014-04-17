package com.chb.addon.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.chb.addon.NetworkManager;
import com.chb.addon.R;

public class MyWidgetProvider extends AppWidgetProvider{
	private TextView tx1;

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		System.out.println(this.getClass().getSimpleName() + ":" + Thread.currentThread().getStackTrace()[2].getMethodName().toString());

		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		System.out.println(this.getClass().getSimpleName() + ":" + Thread.currentThread().getStackTrace()[2].getMethodName().toString());
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		System.out.println(this.getClass().getSimpleName() + ":" + Thread.currentThread().getStackTrace()[2].getMethodName().toString());
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		RemoteViews views=new RemoteViews(context.getPackageName(), R.layout.mobile_data);
		System.out.println(this.getClass().getSimpleName() + ":" + Thread.currentThread().getStackTrace()[2].getMethodName().toString());
		System.out.println(intent.getAction());
		try {
			if(NetworkManager.getInstance(context).isMobileConnected()) {
				views.setTextViewText(R.id.tx_mobile_data_status, "开");
			} else {
				views.setTextViewText(R.id.tx_mobile_data_status, "关");
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onReceive(context, intent);
		
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		System.out.println(this.getClass().getSimpleName() + ":" + Thread.currentThread().getStackTrace()[2].getMethodName().toString());

		for(int i=0;i<appWidgetIds.length;i++){
			
			Intent intent = new Intent("CHANGE_MAIN_BUTTON_CONTENT_HW");
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.mobile_data);
			PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, 0);
			remoteViews.setOnClickPendingIntent(R.id.btn_mobile_data_switcher, pending);

			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	public static RemoteViews getTimeView(Context context){
		RemoteViews views=new RemoteViews(context.getPackageName(), R.layout.mobile_data);

		try {
			//因为系统设置是异步的，比较慢，而用户点击后会立即同步调用这里，因此，这里的开关状态与运行是的网络开关状态相反，
			// 也就是说，这里的开关状态，是设置成大概1秒后的系统设置状态。
			if(NetworkManager.getInstance(context).isMobileConnected()) {
				views.setTextViewText(R.id.tx_mobile_data_status, "关");
			} else {
				views.setTextViewText(R.id.tx_mobile_data_status, "开");
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return views;
	}
	
}
