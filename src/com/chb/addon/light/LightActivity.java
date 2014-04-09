package com.chb.addon.light;

/**
 * Created by renen-inc_hempel on 14-3-19.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.chb.addon.R;

public class LightActivity extends Activity {

	private FlashlightSurface mSurface;
	private ImageView mImageView;
	private boolean isFlashlightOn = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.light);
//		Contants.LogI("MainActivity: onCreate()");

		mSurface = (FlashlightSurface) findViewById(R.id.surfaceview);
		mImageView = (ImageView) findViewById(R.id.image);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (MotionEvent.ACTION_UP == event.getAction()) {
//			Contants.LogI("MainActivity: onTouchEvent() : ACTION_UP");
			if (isFlashlightOn) {
				mSurface.setFlashlightSwitch(false);
				isFlashlightOn = false;
//				mImageView.setImageResource(R.drawable.flashlight_off);
			} else {
				mSurface.setFlashlightSwitch(true);
				isFlashlightOn = true;
//				mImageView.setImageResource(R.drawable.flashlight_on);
			}
		}
		return super.onTouchEvent(event);
	}

}
