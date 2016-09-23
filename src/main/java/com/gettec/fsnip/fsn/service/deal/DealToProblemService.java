package com.gettec.fsnip.fsn.service.deal;

import java.util.List;

import net.sf.json.JSONObject;

import com.gettec.fsnip.fsn.dao.deal.DealToProblemDAO;
import com.gettec.fsnip.fsn.model.deal.DealProblem;
import com.gettec.fsnip.fsn.model.deal.DealToProblem;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.deal.DealProblemBean;
import com.gettec.fsnip.fsn.vo.deal.DealProblemLogBean;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

public interface DealToProblemService extends BaseService<DealToProblem,DealToProblemDAO>{

	/**
	 * 根据条形码获取需要处理的问题总数
	 * @param barcode
	 * @return
	 */
	long getdealToProblemTotal(String barcode,long businessId);
	/**
	 * 根据条形码获取需要处理问题相关信息
	 * @param barcode
	 * @return
	 */
	List<DealToProblem> getdealToProblemList(int page, int pageSize, String barcode,long businessId);
	/**
	 * 实体对象转换为json格式
	 * @param dtpvo
	 * @return
	 */
	JSONObject getDealProblemLogVO(long id,AuthenticateInfo info);
	
	/**
	 * 实体对象转换为json格式
	 * @param dtpvo并且通知监管
	 * @return
	 */
	JSONObject getJsonDealToProblem(DealProblem sendproblem,long status);
	
	/**
	 * 通知企业
	 * @param sendproblem
	 */
	void getNoticeDealToProblem(DealProblem sendproblem);
	
	/**
	 * 获取RabbitMQ传递过来的json数据
	 * @param obj
	 */
	void setDealToProblem(JSONObject obj);

}
