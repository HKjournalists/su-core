package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;

public interface CustomerDAO extends BaseDAO<BusinessUnit> {

	public List<BusinessUnit> getfilter(String filter,String name,String fieldName, List<Long> listId);

	public List<BusinessUnit> getListByConditionAndOrgId(String configure,
			Long organization, int page, int pageSize) throws DaoException;

	Long getCountByConditionAndOrgId(String configure, Long organization)
			throws DaoException;

	public long count(Long organization, Long customerId) throws DaoException;

	public void addProviderRelation(Long busId, Long providerId) throws DaoException;

	public long countProviderRelation(Long busId, Long providerId) throws DaoException;

	List<BusinessUnit> getListByCustomerType(Long organization,
			int customerType, int page, int pageSize) throws DaoException;

	/**
	 * authar ：wubiao
	 * 日期： 2015、9、23
	 * 查询入住客户
	 * @param keyword 查询关键字
	 * @param organization 当前机构
	 * @param page  当前页
	 * @param pageSize
	 * @return
	 */
	public List<BusinessUnit> searchCustomerList(String keyword,
			Long organization, int page, int pageSize);

	/**
	 *  * authar ：wubiao
	 * 日期： 2015、9、23
	 * 入住客户总数
	 * @param keyword 查询关键字
	 * @param organization
	 * @return
	 */
	public Long searchCustomerCount(String keyword, Long organization);
    /**
      * authar ：wubiao
	 * 日期： 2015、9、23
     * 查询来源客户或销往客户
     * @param type 1,表示销往客户;2,表示来源客户
     * @param configure  条件查询
     * @param organization
     * @param page 当前页
     * @param pageSize 每页显示总数
     * @return
     */
	public List<BusinessUnit> sourceOrsoldCustomer(int type,String configure,
			Long organization, int page, int pageSize);

	/**
	 * authar ：wubiao
	 * 日期： 2015、9、23
	 * 查询来源客户或销往客户 总条数
	 * @param type
	 * @param configure
	 * @param organization
	 * @return
	 */
	public Long sourceOrsoldCustomer(int type,String configure, Long organization);
}
