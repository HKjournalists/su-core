package com.gettec.fsnip.fsn.other;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.sf.json.JSONObject;
import com.gettec.fsnip.fsn.service.deal.DealToProblemService;
import com.gettec.fsnip.fsn.vo.deal.DealProblemBean;
import com.gettec.fsnip.fsn.vo.deal.DealProblemLogBean;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class DealToProblemOther implements Consumer{
	private DealToProblemService dealToProblemService;

	public DealToProblemService getDealToProblemService() {
		return dealToProblemService;
	}

	public void setDealToProblemService(DealToProblemService dealToProblemService) {
		this.dealToProblemService = dealToProblemService;
	}

	@Override
	public void handleConsumeOk(String consumerTag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleCancelOk(String consumerTag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {

	}
	@Override
	public void handleDelivery(String arg0, Envelope arg1,
			BasicProperties arg2, byte[] msg) throws IOException {
		try {
			String content=new String(msg, "utf-8");
                
			JSONObject array=JSONObject.fromObject(content);
			JSONObject obj = JSONObject.fromObject(array);
			
			dealToProblemService.setDealToProblem(obj);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleShutdownSignal(String consumerTag,
			ShutdownSignalException sig) {


	}

	@Override
	public void handleRecoverOk(String consumerTag) {
		// TODO Auto-generated method stub

	}

}
