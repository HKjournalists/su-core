package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.PurchaseorderInfo;

public interface PurchaseorderInfoDAO extends BaseDAO<PurchaseorderInfo> {
	List<PurchaseorderInfo> getListByNo(String no) throws DaoException;

	List<PurchaseorderInfo> getListByNoPage(String no, int page, int pageSize) throws DaoException;

	long countByNo(String no) throws DaoException;

	/**
	 * 查询 erp中的对应产品实例
	 * @param page
	 * @param pageSize
	 * @param proId 产品id
	 * @param batch 批次
	 * @param startTime 开始时间
	 * @param batch 结束时间
	 * @return List<PurchaseorderInfo>
	 * @throws DaoException
	 */
	List<PurchaseorderInfo> getErpProductInstanceByProductId(int page,int pageSize, Long proId,
			String batch,String startTime,String endTime)throws DaoException;

	/**
	 * 查询 erp中的对应产品实例 总数
	 * @param proId 产品id
	 * @param batch 批次
	 * @param startTime 开始时间
	 * @param batch 结束时间
	 * @return long
	 */
	long countGetErpProductInstance(Long proId, String batch,String startTime,String endTime)throws DaoException;
}
