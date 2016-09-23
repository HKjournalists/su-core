package com.gettec.fsnip.fsn.dao.business;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.ProductionLicenseInfo;

public interface ProductionLicenseDAO extends BaseDAO<ProductionLicenseInfo>{

	long countRelationshipByResIdAndQsNo(Long resId, Long qsId) throws DaoException;

	/**
	 * 功能描述：获取当前企业的所有qs号
	 * @author ZhangHui 2015/5/21
	 */
	public List<ProductionLicenseInfo> getListByBusId(Long bussinessId) throws DaoException;

	/**
	 * 功能描述：根据qs号获取一条生产企业信息
	 * @author ZhangHui 2015/5/21
	 */
	public ProductionLicenseInfo findByQsno(String qsNo) throws DaoException;

	/**
	 * 功能描述：根据生产许可证编号，获取qs id
	 * @author ZhangHui 2015/6/4
	 * @throws DaoException 
	 */
	public Long findIdByQsno(String qsno) throws DaoException;

	/**
	 * 根据产品id查找其所有已绑定且没过期的生产许可证信息
	 * @author longxianzhen  2015/06/26
	 */
	List<ProductionLicenseInfo> getListByProId(Long proId)throws DaoException;
}