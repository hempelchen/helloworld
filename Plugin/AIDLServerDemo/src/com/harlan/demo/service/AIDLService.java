package com.harlan.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.harlan.demo.aidl.ExecuteServiceAIDL;
import com.harlan.demo.aidl.HarlanInfo;
import com.harlan.demo.aidl.ICallBack;

public class AIDLService extends Service
{
    public static final String TAG = "AIDLService";
    
    private ICallBack mCallBack;
    
    /**
     * 绑定服务
     */
    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }
    
    /**
     * 创建服务
     */
    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    /**
     * 销毁服务
     */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    /**
     * 启动服务
     */
    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);
    }

    /**
     * 解绑服务
     */
    @Override
    public boolean onUnbind(Intent intent)
    {
        mCallBack = null;
        return super.onUnbind(intent);
    }
    
    ExecuteServiceAIDL.Stub mBinder = new ExecuteServiceAIDL.Stub()
    {
        @Override
        public HarlanInfo getServerHarlanInfo(HarlanInfo info, ICallBack icallback)
            throws RemoteException
        {
            Log.d(TAG,"getServerHarlanInfo");
            mCallBack = icallback;
            mCallBack.handleByServer("The msg is from server");
            HarlanInfo newInfo = new HarlanInfo();
            newInfo.setName(info.getName().toLowerCase());
            newInfo.setAge(info.getAge() + 10);
            newInfo.setPlace("Home");
            return newInfo;
        }
    };
    
}
