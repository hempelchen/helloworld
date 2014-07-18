package com.harlan.demo.aidl;
interface ICallBack{
	/**
	*callback of AIDLClient
	*handle by server
	**/
	void handleByServer(String param);
}