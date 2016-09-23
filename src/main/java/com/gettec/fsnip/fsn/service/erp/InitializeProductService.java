package com.gettec.fsnip.fsn.service.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.erp.InitializeProductDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.InitializeProduct;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface InitializeProductService extends  BaseService<InitializeProduct, InitializeProductDAO>{

	public PagingSimpleModelVO<InitializeProduct> getAllInitializeProduct(int page,int pageSize,Long organization);

	public List<InitializeProduct> getAllOutInitializeProduct(Long organization);

	void save(InitializeProduct initializeProduct, Long organization) throws ServiceException;

	public List<InitializeProduct> getInitializeProduct(Long organization);

	InitializeProduct findByProIdAndOrgId(Long productId, Long organization) throws ServiceException;

	public InitializeProduct findByBarcodeAndOrgId(String no, Long organization) throws ServiceException;
	
	long countByOrganizationAndLocal(Long organization, boolean local, String configure) throws ServiceException;

	List<InitializeProduct> getByOrganizationAndLocal(Long organization, boolean local, String configure, 
			int page, int pageSize) throws ServiceException;

	/**
	 * 删除/恢复 产品
	 * @param proId 产品id
	 * @param organization 组织机构id
	 * @param isDel<br>
	 * true: 已删除<br>
	 * false:未删除<br>
	 * @author ZhangHui 2015/4/14
	 */
	public void updateDelFlag(Long proId, Long organization, boolean isDel) throws ServiceException;

	/**
	 * 根据产品id,组织机构id,删除标记,引进标记，查找产品数量
	 * @param proId 产品id
	 * @param organization 组织机构id
	 * @param isDel<br>
	 * 			true: 已删除<br>
	 * 			false:未删除<br>
	 * @author ZhangHui 2015/4/14
	 */
	public long count(Long proId, Long organization, boolean isDel) throws ServiceException;

	/**
	 * 检查企业是否引进该产品
	 * @author ZhangHui 2015/4/15
	 */
	public boolean checkLeadProduct(String barcode, Long organization) throws ServiceException;
	
	/**
	 * 引进已存在的产品
	 * @author ZhangHui 2015/4/15
	 */
	public void leadProduct(String barcode,Long organization)throws ServiceException;
}
