package com.gettec.fsnip.fsn.web;




import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.gettec.logsystem.client.model.LogData;
import com.gettec.logsystem.client.model.PageEventLog;
/**
 * 
 * @author Rongshen Xie
 *
 */
public class JournalInterceptor extends HandlerInterceptorAdapter{  
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		LogData ldata = new LogData();
		PageEventLog plog = new PageEventLog();
//		plog.setCookieId(request.getRequestedSessionId());
		plog.setCurl(request.getHeader("Host")+request.getRequestURI());
		plog.setPurl(request.getHeader("Referer"));
		plog.setServerID(Integer.parseInt(request.getRemoteHost().split("\\.")[3]));
		plog.setStartTime(ThreadLocalUtil.getClient().getCurrentTime());
		plog.setiPAddress(request.getRemoteHost());
		plog.setReqeustMetohd(request.getMethod());
		plog.setUserAgent(request.getHeader("User-Agent"));
		plog.setUserAccount((request.getUserPrincipal()==null)?null:request.getUserPrincipal().toString());
		
//		int count = request.getInputStream().available();
//		System.out.println(count);
//		byte[] bs=new byte[count];
//		int readCount = 0; // 已经成功读取的字节的个数
//		while (readCount < count) {
//			readCount += request.getInputStream().read(bs, readCount, count - readCount);
//		}
//		plog.setRequestContent(new String(bs));
////		request.getInputStream().close();
//		System.out.println(plog.getRequestContent());
		
		ldata.setRequestGuid(java.util.UUID.randomUUID().toString());
		ldata.setPageEventLog(plog);
		ThreadLocalUtil.set(ldata);

		return super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
//		System.out.println("postHandle");
		ThreadLocalUtil.get().getPageEventLog().setEndTime(ThreadLocalUtil.getClient().getCurrentTime());
		ThreadLocalUtil.getClient().offer(ThreadLocalUtil.get());
		super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
//		System.out.println("afterCompletion");
		super.afterCompletion(request, response, handler, ex);
	}
}
