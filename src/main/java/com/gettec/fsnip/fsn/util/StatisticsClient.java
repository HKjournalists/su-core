package com.gettec.fsnip.fsn.util;
import java.lang.Thread.State;

import org.apache.commons.lang.ObjectUtils.Null;
import org.springframework.stereotype.Component;
import com.gettec.logsystem.client.utils.DateUtil;

/**
 * 
 * @author licw
 * 
 */
@Component(value="staClient")
public class StatisticsClient {

	private DoubleQueue<Object> queue;
	private long diffTime = 0L;
	private SendTask sendTask = null;
	private Thread taskThread =null;
	
	/**
	 * 初始化
	 * 
	
	 */
	public StatisticsClient(){
		build();
	}
	private void build() {
		if (queue==null) {
			queue = new DoubleQueue<Object>();
		}
		if (sendTask==null) {
			sendTask = new SendTask(queue);
		}
		
		// 开启队列处理线程
		if (taskThread==null) {
			taskThread = new Thread(sendTask);
			taskThread.start();
		}else{
			if(taskThread.getState()==State.TERMINATED)
			{
				taskThread = new Thread(sendTask);
				taskThread.start();
			}
		}
	}

	/**
	 * 记录日志信息
	 * 
	 * @param logData
	 */
	public void offer(Object logData) {	
		build();
		this.queue.offer(logData);
	}

	/**
	 * 获取远程服务器当前时间
	 * 
	 * @return
	 */
	public long getCurrentTime() {
		return this.diffTime + DateUtil.getLocalTime();
	}

	/**
	 * 关闭函数 用于释放logclient占用的资源
	 */
	public void close() {
		// 将内存中的日志或立即发送至远程服务器 或序列化在本地
		if (sendTask != null) {
			sendTask.close();
		}
	}

}
