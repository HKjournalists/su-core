package com.gettec.fsnip.fsn.service.product;

import java.util.List;

import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.vo.product.ProductQSVO;

/**
 * 企业-qs号关系  service层 接口
 * @author ZhangHui 2015/5/15
 */
public interface Business2QsRelationService {

	/**
	 * 功能描述：分页查询当前登录企业下的qs号，用于在qs权限设置页面展示
	 * @param local
	 * 			0 代表qs号为不是当前企业自己的qs号
	 *          1 代表qs号是当前企业自己的qs号
	 * @author ZhangHui 2015/5/15
	 */
	public List<ProductQSVO> getListOfQsByPage(long organization, int local, int page, int pageSize) throws ServiceException;

	/**
	 * 功能描述：根据企业组织机构查找企业自己的qs号总数量
	 * @author ZhangHui 2015/5/15
	 */
	public long countOfMyOwn(long organization);

	/**
	 * 功能描述：查找当前qs号被授权的企业
	 * @param local
	 * 			0 代表qs号为不是当前企业自己的qs号
	 *          1 代表qs号是当前企业自己的qs号
	 * @author ZhangHui 2015/5/15
	 */
	public List<ProductQSVO> getListOfQs(Long qsId, int local) throws ServiceException;

	/**
	 * 功能描述：新增一条 企业-qs 关系
	 * @param owerBusName 当前qs号真正的主企业名称
	 * @author Zhanghui 2015/5/28
	 */
	public boolean create(ProductQSVO vo, String owerBusName) throws ServiceException;
	
	/**
	 * 功能描述：新增一条 企业-qs 关系
	 * @author Zhanghui 2015/5/22
	 */
	public boolean create(Long bussinessId, Long qs_id, String ownerBusName, String currentBusName) throws ServiceException;
	
	/**
	 * 功能描述：编辑一条qs授权许可
	 * @author Zhanghui 2015/5/19
	 */
	public boolean updateOfRight(Long id, int can_use, int can_edit);
	
	/**
	 * 功能描述：查找一条qs授权许可
	 * @author Zhanghui 2015/5/18
	 */
	public Long findIdByVO(ProductQSVO vo);
	
	/**
	 * 功能描述：删除一条qs授权许可
	 * @author Zhanghui 2015/5/19
	 * @throws ServiceException 
	 */
	public void delete(ProductQSVO vo) throws ServiceException;

	/**
	 * 功能描述：查找授权qs数量
	 * @author Zhanghui 2015/5/19
	 */
	public long count(Long qsId, Long businessId);


	/**
	 * 功能描述：根据企业id获取qs号
	 * @author ZhangHui 2015/5/20
	 */
	List<ProductQSVO> getListOfQsByPage(Long organization, int page, int pageSize) throws DaoException;

	/**
	 * 功能描述：通过对比更新前后的qs号，如果：
	 *         a. 前后相同，则不需要更新
	 *         b. 前后不同，则执行更新操作
	 * @param ownerBusName   qs号的主企业
	 * @param currentBusName 当前正在操作该qs号的企业名称
	 * @author Zhanghui 2015/5/21
	 */
	public boolean updateByContrast(Long bussinessId, Long qsId, Long old_edit_qsId, String ownerBusName, 
			String currentBusName) throws DaoException;

	/**
	 * 前提：当前无  当前企业和qs号 的关系记录
	 * 功能描述：判断该企业有无编辑当前qs号的权限
	 * @param currentBusName 当前在操作该qs号的企业名称
	 * @author Zhanghui 2015/5/21
	 */
	public boolean isHaveUpdateRightOfQs(String currentBusName, Long now_edit_qsId) throws DaoException;

	/**
	 * 功能描述：根据企业组织机构查找企业所有可以使用的qs号总数量
	 * @author Zhanghui 2015/5/21
	 */
	public long countOfMyAll(Long organization) throws DaoException;

	/**
	 * 批量 删除  被授权企业-qs 关系
	 * @author Zhanghui 2015/5/22
	 * @throws ServiceException 
	 */
	public void deleteOfBeAuthorized(Long qsId, Long owner_bus_id) throws ServiceException;

	/**
	 * 功能描述：查找授权qs数量
	 * @author Zhanghui 2015/5/22
	 */
	public long count(Long qsId, int local) throws ServiceException;

	/**
	 * 功能描述：删除一条qs授权许可
	 * @author Zhanghui 2015/5/22
	 */
	public void delete(Long bussinessId, Long qsId) throws DaoException;

	/**
	 * 功能描述：根据qs号和组织机构id，获取该qs号的操作权限
	 * @author Zhanghui 2015/5/25
	 */
	public ProductQSVO getRightOfQs(String qsno, Long currentUserOrganization) throws ServiceException;

	/**
	 * 功能描述：查看当前qs号有无被认领
	 * @author Zhanghui 2015/5/28
	 */
	public boolean getClaimedOfQs(Long qsId) throws ServiceException;

	/**
	 * 获取指定企业下可以使用的QS号
	 * @author tanxin 2015-06-01
	 */
	List<ProductQSVO> getListCanUseByOrganization(Long organization) throws ServiceException;

	/**
	 * 授予/取消企业该qs号的所有权
	 * @param business_id 企业id
	 * @param qs_id       qs id
	 * @param confer
	 * 			true  代表授予
	 * 			false 代表取消
	 * @author ZhangHui 2015/6/2
	 * @throws ServiceException 
	 */
	public void conferOwnership(Long business_id, Long qs_id, boolean confer) throws ServiceException;

}
