package com.chb.helloworld.js;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.webkit.*;
import com.chb.helloworld.R;

import java.io.File;

/**
 * Created by renen-inc_hempel on 14-5-4.
 */
public class MyWebviewActivity extends Activity {

	private WebView contentWebView = null;
	private ValueCallback<Uri> mUploadMessage = null;
	private static int RESULT_LOAD_IMAGE = 1;
	private String mCameraFilePath = "";

	private static int MINIMUM_FONT_SIZE = 8;
	private static int MINIMUM_LOGICAL_FONT_SIZE = 8;
	private static int DEFAULT_FONT_SIZE = 16;
	private static int DEFAULT_FIXED_FONT_SIZE = 13;
	protected String cacheUrl;
	protected String initUrl;

	// 0: default,loadUrl;  1:loadData
	private static int loadType = 0;
	private String webData = "";

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.js);
		getExtras();
		contentWebView = (WebView) findViewById(R.id.webview);
		setWebViewProperty();
		if(loadType == 1) {
			contentWebView.loadData(webData, "text/html", "utf-8");
		} else {
			contentWebView.loadUrl("http://el-lady.com.cn/cw201402/mobile/camera.html");
		}
	}

	public void getExtras(){
		Bundle args = this.getIntent().getExtras();
		if( args != null) {
			loadType = args.getInt("loadType", 0);
			webData = args.getString("webData");
		}
	}

	public void setWebViewProperty() {
		WebSettings settings = contentWebView.getSettings();
		settings.setLoadsImagesAutomatically(true);
//		settings.setPluginsEnabled(true);
		settings.setSaveFormData(false);
		// settings.setLightTouchEnabled(true);
		settings.setSavePassword(false);
		settings.setJavaScriptEnabled(true);
		settings.setCacheMode(WebSettings.LOAD_NORMAL);
		settings.setMinimumFontSize(MINIMUM_FONT_SIZE);
		settings.setMinimumLogicalFontSize(MINIMUM_LOGICAL_FONT_SIZE);
		settings.setDefaultFontSize(DEFAULT_FONT_SIZE);
		settings.setDefaultFixedFontSize(DEFAULT_FIXED_FONT_SIZE);
		settings.setTextSize(WebSettings.TextSize.NORMAL);
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		settings.setAllowFileAccess(true);
		settings.setBuiltInZoomControls(false);
		settings.setDomStorageEnabled(true);
		//web.addJavascriptInterface(new JavascriptInterface(mActivity), "mynamespace");
		contentWebView.addJavascriptInterface(this, "android");
		contentWebView.setWebViewClient(new LbsWebViewClient());
		contentWebView.setWebChromeClient(new LbsWebChromeClient());
	}

	public class LbsWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			initUrl = url;

			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			cacheUrl = url;
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
		                            String description, String failingUrl) {

			super.onReceivedError(view, errorCode, description, failingUrl);

		}

		@Override
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {


			return super.shouldOverrideKeyEvent(view, event);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

		/**
		 * webView�ں��?��ݣ��ж�ˢ���Ƿ��ظ��ύ�ķ���
		 */
		@Override
		public void onFormResubmission(WebView view, final Message dontResend,
		                               final Message resend) {

			resend.sendToTarget();
		}
	}

	public class LbsWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);


		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			setTitle(title);
		}

		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			openFileChooserLocal(uploadMsg, acceptType, capture);
		}
	}

	void openFileChooserLocal(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

		final String imageMimeType = "image/*";
		final String videoMimeType = "video/*";
		final String audioMimeType = "audio/*";
		final String mediaSourceKey = "capture";
		final String mediaSourceValueCamera = "camera";
		final String mediaSourceValueFileSystem = "filesystem";
		final String mediaSourceValueCamcorder = "camcorder";
		final String mediaSourceValueMicrophone = "microphone";

		// According to the spec, media source can be 'filesystem' or 'camera' or 'camcorder'
		// or 'microphone' and the default value should be 'filesystem'.
		String mediaSource = mediaSourceValueFileSystem;

        if (mUploadMessage != null) {
            // Already a file picker operation in progress.
            return;
        }

        mUploadMessage = uploadMsg;

		// Parse the accept type.
		String params[] = acceptType.split(";");
		String mimeType = params[0];

		if (capture.length() > 0) {
			mediaSource = capture;
		}

		if (capture.equals(mediaSourceValueFileSystem)) {
			// To maintain backwards compatibility with the previous implementation
			// of the media capture API, if the value of the 'capture' attribute is
			// "filesystem", we should examine the accept-type for a MIME type that
			// may specify a different capture value.
			for (String p : params) {
				String[] keyValue = p.split("=");
				if (keyValue.length == 2) {
					// Process key=value parameters.
					if (mediaSourceKey.equals(keyValue[0])) {
						mediaSource = keyValue[1];
					}
				}
			}
		}

		//Ensure it is not still set from a previous upload.
        mCameraFilePath = null;

		if (mimeType.equals(imageMimeType)) {
			if (mediaSource.equals(mediaSourceValueCamera)) {
				// Specified 'image/*' and requested the camera, so go ahead and launch the
				// camera directly.
				startActivityForResult(createCameraIntent(), RESULT_LOAD_IMAGE);
				return;
			} else {
				// Specified just 'image/*', capture=filesystem, or an invalid capture parameter.
				// In all these cases we show a traditional picker filetered on accept type
				// so launch an intent for both the Camera and image/* OPENABLE.
				Intent chooser = createChooserIntent(createCameraIntent());
				chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(imageMimeType));
				startActivityForResult(chooser, RESULT_LOAD_IMAGE);
				return;
			}
		} else if (mimeType.equals(videoMimeType)) {
			if (mediaSource.equals(mediaSourceValueCamcorder)) {
				// Specified 'video/*' and requested the camcorder, so go ahead and launch the
				// camcorder directly.
				startActivityForResult(createCamcorderIntent(), RESULT_LOAD_IMAGE);
				return;
			} else {
				// Specified just 'video/*', capture=filesystem or an invalid capture parameter.
				// In all these cases we show an intent for the traditional file picker, filtered
				// on accept type so launch an intent for both camcorder and video/* OPENABLE.
				Intent chooser = createChooserIntent(createCamcorderIntent());
				chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(videoMimeType));
				startActivityForResult(chooser, RESULT_LOAD_IMAGE);
				return;
			}
		} else if (mimeType.equals(audioMimeType)) {
			if (mediaSource.equals(mediaSourceValueMicrophone)) {
				// Specified 'audio/*' and requested microphone, so go ahead and launch the sound
				// recorder.
				startActivityForResult(createSoundRecorderIntent(), RESULT_LOAD_IMAGE);
				return;
			} else {
				// Specified just 'audio/*',  capture=filesystem of an invalid capture parameter.
				// In all these cases so go ahead and launch an intent for both the sound
				// recorder and audio/* OPENABLE.
				Intent chooser = createChooserIntent(createSoundRecorderIntent());
				chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(audioMimeType));
				startActivityForResult(chooser, RESULT_LOAD_IMAGE);
				return;
			}
		}

		// No special handling based on the accept type was necessary, so trigger the default
		// file upload chooser.
		startActivityForResult(createDefaultOpenableIntent(), RESULT_LOAD_IMAGE);
	}

	private Intent createDefaultOpenableIntent() {
		// Create and return a chooser with the default OPENABLE
		// actions including the camera, camcorder and sound
		// recorder where available.
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		i.setType("*/*");

		Intent chooser = createChooserIntent(createCameraIntent(), createCamcorderIntent(),
				                                    createSoundRecorderIntent());
		chooser.putExtra(Intent.EXTRA_INTENT, i);
		return chooser;
	}

	private Intent createChooserIntent(Intent... intents) {
		Intent chooser = new Intent(Intent.ACTION_CHOOSER);
		chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
		chooser.putExtra(Intent.EXTRA_TITLE,"选择应用");
		return chooser;
	}

	private Intent createOpenableIntent(String type) {
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		i.setType(type);
		return i;
	}

	private Intent createCameraIntent() {
		String datetime = "";
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File externalDataDir = Environment.getExternalStoragePublicDirectory(
				                                                                    Environment.DIRECTORY_DCIM);
		File cameraDataDir = new File(externalDataDir.getAbsolutePath() +
				                              File.separator + "Camera");
		cameraDataDir.mkdirs();
		long dateTaken = System.currentTimeMillis();
		if (dateTaken != 0) {
			datetime = DateFormat.format("yyyyMMdd_kkmmss", dateTaken).toString();
		}
		mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator + "IMG_" + datetime + ".jpg";
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFilePath)));
		return cameraIntent;
	}

	private Intent createCamcorderIntent() {
		return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	}

	private Intent createSoundRecorderIntent() {
		return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK ) {
			if(null != data) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						                                          filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();

				// String picturePath contains the path of selected Image
				mUploadMessage.onReceiveValue(Uri.fromFile(new File(picturePath)));
			} else if (!mCameraFilePath.isEmpty()) {
				mUploadMessage.onReceiveValue(Uri.fromFile(new File(mCameraFilePath)));
				mCameraFilePath = "";
			}
			mUploadMessage = null;
		}
	}

}