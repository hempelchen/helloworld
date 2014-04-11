package com.harlan.demo.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.harlan.demo.aidl.ExecuteServiceAIDL;
import com.harlan.demo.aidl.HarlanInfo;
import com.harlan.demo.aidl.ICallBack;

public class ClientActivity extends Activity
{
    public static final String TAG = "ClientActivity";
    
    private static final String BIND_ACTION = "com.harlan.demo.aidl.service";
    
    private EditText mEditTextName;
    
    private EditText mEditTextAge;
    
    private EditText mEditTextPlace;
    
    private Button mButtonCommit;
    
    private ExecuteServiceAIDL executeService;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        getView();
        mButtonCommit.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                getServiceConnect();
                
            }
        });
    }
        
    private void getView()
    {
        mEditTextName = (EditText)findViewById(R.id.editText_name);
        mEditTextAge = (EditText)findViewById(R.id.editText_age);
        mEditTextPlace = (EditText)findViewById(R.id.editText_place);
        mButtonCommit = (Button)findViewById(R.id.button_commit);
    }
    
    private void getServiceConnect()
    {
        Intent it = new Intent();
        it.setAction(BIND_ACTION);
        startService(it);
        bindService(it, mserviceConnection, BIND_AUTO_CREATE);
    }
    
    ServiceConnection mserviceConnection = new ServiceConnection()
    {
        
        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            Log.d(TAG, "onServiceDisconnected");
            
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            Log.d(TAG, "onServiceConnected");
            executeService = ExecuteServiceAIDL.Stub.asInterface(service);
            if (executeService != null)
            {
                handlerInfo();
            }
        }
    };
    
    private void handlerInfo()
    {
        String mName;
        int mAge;
        String mPlace;
        if (mEditTextName.getText().toString().equals(""))
        {
            mEditTextName.setText("Harlan");
            mName = "Harlan";
        }
        else
        {
            mName = mEditTextName.getText().toString();
        }
        if (mEditTextAge.getText().toString().equals(""))
        {
            mAge = 22;
        }
        else
        {
            mAge = Integer.parseInt(mEditTextAge.getText().toString());
        }
        if (mEditTextPlace.getText().toString().equals(""))
        {
            mPlace = "Nanjing";
        }
        else
        {
            mPlace = mEditTextPlace.getText().toString();
        }
        HarlanInfo mInfo = new HarlanInfo();
        mInfo.setName(mName);
        mInfo.setAge(mAge);
        mInfo.setPlace(mPlace);
        try
        {
            HarlanInfo serverInfo = new HarlanInfo();
            serverInfo = executeService.getServerHarlanInfo(mInfo, mCallBack);
            mEditTextName.setText(serverInfo.getName());
            mEditTextAge.setText(String.valueOf(serverInfo.getAge()));
            mEditTextPlace.setText(serverInfo.getPlace());
            unbindService(mserviceConnection);
        }
        catch (RemoteException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    ICallBack.Stub mCallBack = new ICallBack.Stub()
    {
        
        @Override
        public void handleByServer(String param)
            throws RemoteException
        {
            Toast.makeText(getApplicationContext(), param, Toast.LENGTH_LONG).show();
            
        }
    };
    
}
