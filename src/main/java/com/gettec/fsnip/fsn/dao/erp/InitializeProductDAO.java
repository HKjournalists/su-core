package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.InitializeProduct;

public interface InitializeProductDAO extends BaseDAO<InitializeProduct>{

	public List<InitializeProduct> getAllOutInitializeProduct(Long organization);

	public List<InitializeProduct> getAllInitializeProduct(int page,
			int pageSize, Long organization);

	public Long getCountInitializeProduct(Long organization);

	public List<InitializeProduct> getInitializeProducts(Long organization);

	public InitializeProduct findByProIdAndOrgId(Long productId,
			Long organization) throws DaoException;

	public InitializeProduct findByBarcodeAndOrgId(String barcode,
			Long organization) throws DaoException;
	
	long countConditon(Map<String, Object> map) throws DaoException;
	
	List<InitializeProduct> getByConditon(Map<String, Object> map, int page, int pageSize) throws DaoException;

	/**
	 * 删除/恢复 产品
	 * @param proId 产品id
	 * @param organization 组织机构id
	 * @param isDel<br>
	 * true: 已删除<br>
	 * false:未删除<br>
	 * @author ZhangHui 2015/4/14
	 */
	public void updateDelFlag(Long proId, Long organization, boolean isDel) throws DaoException;
	
}
