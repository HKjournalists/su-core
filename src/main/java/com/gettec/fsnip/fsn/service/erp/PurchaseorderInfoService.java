package com.gettec.fsnip.fsn.service.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.erp.PurchaseorderInfoDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.PurchaseorderInfo;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNote;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.lhfs.fsn.vo.product.ProductInfoVO;

public interface PurchaseorderInfoService extends BaseService<PurchaseorderInfo, PurchaseorderInfoDAO>{
	List<PurchaseorderInfo> getListByNo(String no) throws ServiceException;
	void save(ReceivingNote receivNote, Long organization) throws ServiceException;
	List<PurchaseorderInfo> getListByNoPage(String no, int page, int pageSize) throws ServiceException;
	long countByNo(String no) throws ServiceException;
	
	/**
	 * 查询 erp中的对应产品实例 总数
	 * @param proId 产品id
	 * @param batch 产品批次
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return long
	 * @throws ServiceException
	 */
	long countGetErpProductInstance(Long proId, String batch,String startTime,String endTime )throws ServiceException;
	/**
	 * 查询 erp中的对应产品实例
	 * @param page
	 * @param pageSize 
	 * @param proId 产品id
	 * @param batch 产品批次
	 * @param startTime 产品批次
	 * @param endTime 产品批次
	 * @return List<ProductVOWda>
	 * @throws ServiceException
	 */
	List<ProductInfoVO> getErpProductInstanceByProductId(int page, int pageSize,Long proId, String batch,String startTime,String endTime) throws ServiceException;
}
