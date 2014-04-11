package com.harlan.demo.aidl;
import com.harlan.demo.aidl.HarlanInfo;
import com.harlan.demo.aidl.ICallBack;
interface ExecuteServiceAIDL
{
	/**
	 *get info from server and 
	 *Transfer a callback methods handle;
	 *if occur error ,will be return null
	 *对于非基本数据类型和String和CharSequence类型,要加上方向指示
	 *包括in、out和inout，in表示由客户端设置，out表示由服务端设置，inout是两者均可设置。
     */
	HarlanInfo getServerHarlanInfo(in HarlanInfo info,ICallBack icallback);
}