package com.gettec.fsnip.fsn.dao.deal;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.deal.DealToProblem;

public interface DealToProblemDAO extends BaseDAO<DealToProblem>{

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
	 * 获取流水号获取问题信息，查看问题是否纯在
	 * @param pcode
	 * @return
	 */
	DealToProblem getFindPcodeOrScode(String pcode);

}
