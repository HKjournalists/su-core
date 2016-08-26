package com.gettec.fsnip.fsn.dao.market;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.market.MkTestTemplate;


public interface MkTestTemplateDAO extends BaseDAO<MkTestTemplate>{

	List<String> getListOfBarCode(Long orignizatonId) throws DaoException;

	MkTestTemplate findByBarCode(String barCode, Long organization, String userName) throws DaoException;
	
	List<MkTestTemplate> findByBarCode(String barcode, Long organization) throws DaoException;

	MkTestTemplate findByReportId(Long reportId) throws DaoException;

	MkTestTemplate findByBarCode(String barcode) throws DaoException;

	/**
	 * 根据 barcode 和 organization 和 userName 查找一条template模板信息id
	 * @author tangxin 2015/6/8
	 * @throws DaoException
	 */
	Long findIdByBarCodeAndOrganizationAndUserName(String barcode, Long organization, String userName) throws DaoException;

	/**
	 * 根据 id 更新 template 中的 barcode 和 reportid 字段 
	 * @author tangxin 2015/6/8
	 * @throws DaoException
	 */
	boolean updateById(Long id, String barcode, Long reportId) throws DaoException;

	/**
	 * 创建 template 信息 
	 * @author tangxin 2015/6/8
	 * @throws DaoException
	 */
	boolean createBySql(String barcode, Long reportId, Long organization, String userName) throws DaoException;
    
	MkTestTemplate findByTemplateBarCode(String barcode,Long currentUserOrganization, String userName);
	MkTestTemplate findByTemplateBarCode(String barcode,Long currentUserOrganization);
	MkTestTemplate findByTemplateBarCode(String barcode);

}
