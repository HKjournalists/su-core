package com.gettec.fsnip.fsn.web;


import com.gettec.fsnip.sso.client.util.SSOPropertyUtils;
import com.gettec.logsystem.client.core.LogClient;
import com.gettec.logsystem.client.enums.ApplicationTypes;
import com.gettec.logsystem.client.model.LogData;
/**
 * 
 * @author Rongshen Xie
 *
 */
public class ThreadLocalUtil {
	public static LogClient client =null;
	static {
		try {
			client = new LogClient(SSOPropertyUtils.get("ENV"),"../webapps/lims-core/WEB-INF/classes/logsys.properties",ApplicationTypes.FSN);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			client = new LogClient("http://192.168.1.114:8080/logsystem-server/ServerServlet", "e:/testserialize",
					ApplicationTypes.FSN,false);
//			client = new LogClient("localhost:9099/logsystem-server/ServerServlet", "e:/testserialize",
//					ApplicationTypes.LIMS_CORE,true);
		}
	} 
	private static ThreadLocal<LogData> tl=new ThreadLocal<LogData>();
	
	public static void set(LogData logData){
		tl.set(logData); 
	}
	
	public static LogData get(){
		return tl.get();	
	}

	public static LogClient getClient() {
		return client;
	}

	public static void setClient(LogClient client) {
		ThreadLocalUtil.client = client;
	}

	public static ThreadLocal<LogData> getTl() {
		return tl;
	}

	public static void setTl(ThreadLocal<LogData> tl) {
		ThreadLocalUtil.tl = tl;
	}

}
