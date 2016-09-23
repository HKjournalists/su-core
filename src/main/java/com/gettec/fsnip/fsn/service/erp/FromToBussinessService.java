package com.gettec.fsnip.fsn.service.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.erp.FromToBussinessDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.buss.FromToBussiness;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.ResultVO;

public interface FromToBussinessService extends BaseService<FromToBussiness, FromToBussinessDAO> {

	public void save(ResultVO resultVO, Long proId, Long fromBusId, String[] customers) throws ServiceException;

	public String findIdstrs(Long proId, Long fromBusId, boolean isDel) throws ServiceException;

	public String findNamestrs(Long proId, Long fromBusId, boolean isDel)
			throws ServiceException;

	public String findAllProIdStrsByPage(Long fromBusId, Long toBusId, boolean isDel,
			int page, int pageSize,String configure) throws ServiceException;

	/**
	 * 更新销往企业的删除标记
	 * @author ZhangHui 2015/4/14
	 */
	public void updateDelFlag(Long proId, Long fromBusId, Long toBusId, boolean isDel)
			throws ServiceException;

	public Long counts(Long fromBusId, Long toBusId, boolean isDel) throws ServiceException;
	public Product getProbyId(Long fromBusId, Long toBusId, Long proId, boolean isDel) throws ServiceException;
	/**
	 * 获取某供应商提供给当前登录的商超的待处理报告的产品idStrs
	 * @author ZhangHui 2015/5/4
	 */
	public String findAllProIdStrsOfOnHandle(Long fromBusId, Long toBusId,
			boolean idDel, int page, int pageSize,String configure) throws ServiceException;

	/**
	 * 获取某供应商提供给当前登录的商超的待处理报告的产品数量
	 * @author ZhangHui 2015/5/5
	 */
	public Long countsOfOnHandle(Long fromBusId, Long toBusId, boolean idDel,String configure) throws ServiceException;

	/**
	 * 根据产品id和商超ID获取供应商
	 * @author LongXianZhen 2015/5/6
	 */
	public FromToBussiness getFromBuByproIdAndToBuId(Long productId, Long toBusId)throws ServiceException;

	/**
	 * 获取某供应商提供给当前登录的商超的所有产品总数<br>
	 * isDel:true  已删除<br>
	 * isDel:false 未删除
	 * @author Zhanghui 2015/6/17
	 */
	public Long countsOfProduct2Super(Long fromBusId, Long toBusId, boolean isDel,String configure)
			throws ServiceException;

	/**
	 * 功能描述：获取某供应商提供给某商超的产品的记录
	 * @author Zhanghui 2015/6/23
	 * @throws ServiceException 
	 */
	public long count(Long productId, Long fromBusId, Long toBusId) throws ServiceException;

	/**
	 * 功能描述：根据产品id，商超企业id查找供应商企业id
	 * @author Zhanghui 2015/7/1
	 * @throws ServiceException 
	 */
	public Long findFromBusId(Long productId, Long toBusId) throws ServiceException;

	/**
	 * 功能描述：获取供货给当前商超的供应商数量
	 * @author Zhanghui 2015/7/7
	 * @throws ServiceException 
	 */
	public long count(Long productId, Long toBusId) throws ServiceException;

	/**
	 * 功能描述：获取供货给当前商超的供应商名称
	 * @author Zhanghui 2015/7/7
	 * @throws ServiceException 
	 */
	public List<String> getListOfFromBusName(Long productId, Long toBusId) throws ServiceException;
}
