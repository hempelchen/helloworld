package com.chb.helloworld;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.chb.helloworld.addon.AddonLoader;

public class InnerAppActivity extends FragmentActivity {
	private String mAddonLunchFragment = "com.chb.helloworld.addonfragment.AddonFragment";
	private String mAddonApkPath = "AddonFragment.apk";

	private AssetManager asm;
	private Resources res;
	private Resources.Theme thm;
	private ClassLoader classLoader = null;
	private AddonLoader loader = new AddonLoader(this);
	private boolean keepTheme = true;


	public void onCreate(Bundle savedInstanceState) {
		try {
			// 从asset加载子进程包
			if (null == classLoader) {
				classLoader = loader.loadAssetAddon(mAddonApkPath, getClassLoader());
			}

			AssetManager am = AssetManager.class.newInstance();
            System.out.println("CHB1");
			am.getClass().getMethod("addAssetPath", String.class).invoke(am, loader.getApkLoader().getApkFilePath());
            System.out.println("CHB2");
			asm = am;
			Resources superRes = super.getResources();
            System.out.println("CHB3");
			res = new Resources(am, superRes.getDisplayMetrics(), superRes.getConfiguration());
			thm = res.newTheme();
			thm.setTo(super.getTheme());
            System.out.println("CHB4");
		} catch (Exception e) {
			e.printStackTrace();
		}

        System.out.println("CHB5");
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		FrameLayout rootView = new FrameLayout(this);
		rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		rootView.setId(android.R.id.primary);
		setContentView(rootView);
		keepTheme = false;
        System.out.println("CHB6");

		try {
			String fragmentClass = mAddonLunchFragment;
			Fragment f = (Fragment) classLoader.loadClass(fragmentClass).newInstance();
            System.out.println("CHB7");

			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.add(android.R.id.primary, f);
			ft.commit();
            System.out.println("CHB8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public AssetManager getAssets() {
		return asm == null ? super.getAssets() : asm;
//        return super.getAssets();
	}

	@Override
	public Resources getResources() {
		return res == null ? super.getResources() : res;
//		return super.getResources();
	}

	@Override
	public Resources.Theme getTheme() {
		return keepTheme || thm == null ? super.getTheme() : thm;
//		return super.getTheme();
	}

	@Override
	public ClassLoader getClassLoader() {
		return classLoader == null ? super.getClassLoader() : classLoader;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public String getPackageResourcePath() {
		return (null == loader) ? super.getPackageResourcePath() : loader.getApkLoader().getApkFilePath();
	}
}