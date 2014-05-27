package com.chb.helloworld.memoryinfo;


import android.graphics.drawable.Drawable;

public class ProcessInfo {

	private int pid; //
	private int uid; //
	private int memSize;
	private String processName;
	private String applicationName;
	private boolean isSystemApp;
	private Drawable applicationIcon;

	public ProcessInfo() {
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getMemSize() {
		return memSize;
	}

	public void setMemSize(int memSize) {
		this.memSize = memSize;
	}

	public String getProcessName() {
		return processName;
	}

	public void setPocessName(String processName) {
		this.processName = processName;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public boolean getIsSystemApp() {
		return isSystemApp;
	}

	public void setIsSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}

	public Drawable getApplicationIcon() {
		return applicationIcon;
	}

	public void setApplicationIcon(Drawable applicationIcon) {
		this.applicationIcon = applicationIcon;
	}

}
