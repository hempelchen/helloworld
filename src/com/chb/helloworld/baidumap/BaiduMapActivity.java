package com.chb.helloworld.baidumap;

import android.app.Activity;
import android.os.Bundle;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.chb.helloworld.R;

/**
 * Created on 2014-06-23
 *
 * @author hempel.chen@hotmail.com
 */

public class BaiduMapActivity extends Activity {
	MapView mMapView = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//在使用SDK各组件之前初始化context信息，传入ApplicationContext
		//注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.baidumap);

		//获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		initMap();
	}

	protected void initMap(){
//		BaiduMap mBaiduMap = mMapView.getMap();
//		//定义Maker坐标点
		LatLng point = new LatLng(39.304127, 79.3928);
//		BaiduMapOptions bo = new BaiduMapOptions();
//		bo.mapStatus(new MapStatus.Builder().target(new LatLng(79.3928, 39.304127)).build());
//		BaiduMap baiduMap = mMapView.getMap();
//		baiduMap.setMapStatus();


//		//构建Marker图标
//		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
//		//构建MarkerOption，用于在地图上添加Marker
//		OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
//		mBaiduMap.addOverlay(option);

//		MyLocationData locData = new MyLocationData.Builder().latitude(39.304127)
//				                         .longitude(179.3928).build();
//		mMapView.setMyLocationData(locData);
		BaiduMap mBaiduMap = mMapView.getMap();
//		mBaiduMap.setMyLocationData(locData);


	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}
	@Override
	protected void onResume() {
		super.onResume();
		//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}
}