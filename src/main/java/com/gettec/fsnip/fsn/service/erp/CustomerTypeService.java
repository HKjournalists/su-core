package com.gettec.fsnip.fsn.service.erp;


import com.gettec.fsnip.fsn.dao.erp.CustomerTypeDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.CustomerAndProviderType;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
import com.gettec.fsnip.fsn.vo.erp.SimpleModelVO;


public interface CustomerTypeService extends BaseService<CustomerAndProviderType, CustomerTypeDAO> {
	/* filter cgw
	 * describe:客户类型的筛选
	 * @param page int
	 * @param pageSize int 
	 * @param configure String */
	public PagingSimpleModelVO<CustomerAndProviderType> getCustomerTypefilter(int page, int pageSize,String configure, Long organization);
	/*filter cgw
	 * describe:获取筛选出来的总条数
	 * @param configure
	 * */
	long countCustomerTypefilter(String configure);
	
	public PagingSimpleModelVO<CustomerAndProviderType> getPaging(int page, int size, String keywords, Long organization) throws ServiceException;
	
	public PagingSimpleModelVO<CustomerAndProviderType> getAll(Long organization) throws ServiceException;
	
	/**
	 * 删除的时候，返回一个int类型的值，根据这个值在controller层判断状态
	 * @author Liang Zhou
	 * 2014-10-27
	 * @param id
	 * @return
	 * @throws ServiceException 
	 */
	public void remove(Long id, Long organization) throws ServiceException;
	public boolean updateCustomerType(SimpleModelVO vo, Long organization) throws ServiceException;
	public boolean add(SimpleModelVO vo, Long currentUserOrganization, int type) throws ServiceException;
	public boolean judgeIsUsed(Long id, Long currentUserOrganization) throws ServiceException;
}
