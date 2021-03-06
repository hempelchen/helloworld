package com.chb.helloworld.im.util;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

/**
 * @author Sam.Io
 * @time 2011/11/18
 * @project AdXmpp
 */
public class XmppTool {

	private static XMPPConnection con = null;
	
	private static void openConnection() {
		try {
			ConnectionConfiguration connConfig = new ConnectionConfiguration(ImConst.ImSever, 5222);
//			connConfig.setReconnectionAllowed(true);
//			connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
//			connConfig.setSASLAuthenticationEnabled(true);
//			connConfig.setTruststorePath("/sdcard/Hempel/cacerts.bks");
//			connConfig.setTruststorePassword("chb123");
//			connConfig.setTruststoreType("bks");
			con = new XMPPConnection(connConfig);
			con.connect();
		} catch (XMPPException xe) {
			xe.printStackTrace();
		}
	}

	public static XMPPConnection getConnection() {
		if (con == null) {
			openConnection();
		}
		return con;
	}

	public static void closeConnection() {
		if(con != null) {
			con.disconnect();
			con = null;
		}
	}
}
