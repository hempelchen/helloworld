package com.chb.addon.light;

/**
 * Created by renen-inc_hempel on 14-3-19.
 */

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class FlashlightSurface extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder mHolder;
	private Camera mCameraDevices;
	private Camera.Parameters mParameters;

	public FlashlightSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i("CHB", "FlashlightSurface");
		mHolder = this.getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
	                           int height) {
		Log.i("CHB", "surfaceChanged");
		mParameters = mCameraDevices.getParameters();
		if (mParameters != null)
			mParameters.setPictureFormat(PixelFormat.JPEG);
//		mParameters.setPreviewSize(320, 480);
//		mParameters.setPictureSize(320, 480);
		mCameraDevices.setParameters(mParameters);
		mCameraDevices.startPreview();

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
//		Contants.LogI("surfaceCreated");
		try {
			mCameraDevices = Camera.open();
			mCameraDevices.setPreviewDisplay(mHolder);
		} catch (Exception e) {
			if (mCameraDevices != null)
				mCameraDevices.release();
			mCameraDevices = null;
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
//		Contants.LogI("surfaceDestroyed");
		if (mCameraDevices == null) return;
		mCameraDevices.stopPreview();
		mCameraDevices.release();
		mCameraDevices = null;
	}

	/**
	 * 设置手电筒的开关状态
	 *
	 * @param on ： true则打开，false则关闭
	 */
	public void setFlashlightSwitch(boolean on) {
		if (mCameraDevices == null) return;
		if (mParameters == null) {
			mParameters = mCameraDevices.getParameters();
		}
		if (on) {
			mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
		} else {
			mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
		}
//		Contants.LogI("setFlashlightSwitch-----------------" + on);
		mCameraDevices.setParameters(mParameters);
	}

}

