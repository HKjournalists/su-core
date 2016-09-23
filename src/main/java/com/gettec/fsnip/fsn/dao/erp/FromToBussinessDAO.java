package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.buss.FromToBussiness;
import com.gettec.fsnip.fsn.model.product.Product;

public interface FromToBussinessDAO extends BaseDAO<FromToBussiness> {

	public List<Long> finAllToBusIds(Long proId, Long fromBusId, boolean isDel) throws DaoException;

	public FromToBussiness findById(Long proId, Long fromBusId, Long toBusId) throws DaoException;

	List<Long> finAllToBusIds(Long proId, Long fromBusId) throws DaoException;

	public String findNamestrs(Long proId, Long fromBusId, boolean isDel) throws DaoException;

	public List<Long> finAllProIdsByPage(Long fromBusId, Long toBusId, boolean isDel, int page, int pageSize,String configure) throws DaoException;

	public void updateDelFlag(Long proId, Long fromBusId, Long toBusId, boolean isDel) throws DaoException;

	public long counts(Long fromBusId, Long toBusId, boolean isDel) throws DaoException;
	public Product getProbyId(Long fromBusId, Long toBusId, Long proId,boolean isDel) throws DaoException;
	
	/**
	 * 获取某供应商提供给当前登录的商超的待处理报告的产品数量
	 * @author ZhangHui 2015/5/4
	 */
	public long countsOfOnHandle(Long fromBusId, Long toBusId, boolean isDel,String configure) throws DaoException;

	/**
	 * 获取某供应商提供给当前登录的商超的待处理报告的产品idStrs
	 * @author ZhangHui 2015/5/4
	 */
	public List<Long> findAllProIdStrsOfOnHandle(Long fromBusId, Long toBusId,
			boolean isDel, int page, int pageSize,String configure) throws DaoException;

	/**
	 * 根据产品id和商超ID获取供应商
	 * @author LongXianZhen 2015/5/6
	 */
	public FromToBussiness getFromBuByproIdAndToBuId(Long productId, Long toBusId)throws DaoException;

	/**
	 * 获取某供应商提供给当前登录的商超的所有产品总数<br>
	 * isDel:true  已删除<br>
	 * isDel:false 未删除
	 * @author Zhanghui 2015/6/17
	 */
	public long countsOfProduct2Super(Long fromBusId, Long toBusId, boolean isDel,String configure)
			throws DaoException;

	/**
	 * 功能描述：获取某供应商提供给某商超的产品
	 * @author Zhanghui 2015/6/23
	 * @throws DaoException 
	 */
	public long count(Long productId, Long fromBusId, Long toBusId) throws DaoException;

	/**
	 * 功能描述：根据产品id，商超企业id查找供应商企业id
	 * @author Zhanghui 2015/7/1
	 * @throws DaoException 
	 */
	public Long findFromBusId(Long productId, Long toBusId) throws DaoException;

	/**
	 * 功能描述：获取供货给当前商超的供应商数量
	 * @author Zhanghui 2015/7/7
	 * @throws DaoException 
	 */
	public long count(Long productId, Long toBusId) throws DaoException;

	/**
	 * 功能描述：获取供货给当前商超的供应商名称
	 * @author Zhanghui 2015/7/7
	 * @throws DaoException 
	 */
	public List<String> getListOfFromBusName(Long productId, Long toBusId) throws DaoException;
}
