package com.gettec.fsnip.fsn.dao.product;

import java.util.List;

import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.vo.product.ProductQSVO;

/**
 * 企业-qs号关系  DAO层接口 
 * @author ZhangHui 2015/5/15
 */
public interface Business2QsRelationDAO{
	/**
	 * 功能描述：分页查询当前登录企业下的qs号，用于在qs权限设置页面展示
	 * @author ZhangHui 2015/5/15
	 * @param pageSize2 
	 */
	public List<ProductQSVO> getListOfQsByPage(long organization, int local, int page, int pageSize) throws DaoException;

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
	public List<ProductQSVO> getListOfQs(Long qsId, int local) throws DaoException;

	/**
	 * 功能描述：新增一条 企业-qs 关系
	 * @author Zhanghui 2015/5/18
	 */
	public boolean create(Long business_id, Long qs_id, int local, int can_use, int can_edit);

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
	 * 功能描述：删除/恢复 一条 企业-qs 记录
	 * @param del
	 *          0 代表未删除
	 *          1 代表已删除
	 * @author Zhanghui 2015/5/22
	 * @throws DaoException 
	 */
	public void updateByDel(long id, int del) throws DaoException;

	/**
	 * 功能描述：查找授权qs数量
	 * @author Zhanghui 2015/5/19
	 */
	public long count(Long qsId, Long businessId);

	/**
	 * 功能描述：根据企业id获取qs号
	 * @author ZhangHui 2015/5/20
	 */
	public List<ProductQSVO> getListOfQsByPage(Long organization, int page, int pageSize) throws DaoException;

	/**
	 * 功能描述：通过对比更新前后的qs号，如果：
	 *         a. 前后相同，则不需要更新
	 *         b. 前后不同，则执行更新操作
	 * @author Zhanghui 2015/5/21
	 */
	public boolean updateByContrast(Long bussinessId, Long qsId, Long old_edit_qsId, int local) throws DaoException;

	/**
	 * 功能描述：根据企业组织机构查找企业所有可以使用的qs号总数量
	 * @author Zhanghui 2015/5/21
	 */
	public long countOfMyAll(Long organization) throws DaoException;

	/**
	 * 功能描述：批量 删除/恢复  企业-qs 关系
	 * @param local
	 *         0 代表 批量 删除/恢复  被授权企业-qs 关系
	 *         1 代表 删除/恢复 主企业-qs 关系
	 * @param del
	 * 		   0 代表 还原为未删除状态
	 * 		   1 代表 删除 
	 * @author Zhanghui 2015/5/20
	 * @throws DaoException 
	 */
	public void updateByDel(Long qsId, int local, int del) throws DaoException;

	/**
	 * 功能描述：查找授权qs数量
	 * @author Zhanghui 2015/5/22
	 */
	public long count(Long qsId, int local) throws DaoException;
	
	/**
	 * 功能描述：查找可以对当前qs号有编辑权限的被授权企业数量
	 * @author Zhanghui 2015/5/28
	 */
	public long countOfBeAuthorizedCanEdit(Long qsId) throws DaoException;

	/**
	 * 功能描述：删除/恢复 企业-qs 关系
	 * @param local
	 *         0 代表当前企业是该qs号的主人
	 *         1 代表当前企业不是该qs号的主人
	 * @param del 
	 *         0 代表 还原为未删除状态
	 * 		   1 代表 删除
	 * @author Zhanghui 2015/5/22
	 */
	public void updateByDel(Long bussinessId, Long qsId, boolean local, int del) throws DaoException;

	/**
	 * 功能描述：验证当前企业是否为该qs号的主人，如果不是主人，是否有编辑权
	 * @author Zhanghui 2015/5/28
	 */
	public ProductQSVO find(Long bussinessId, Long qsId) throws DaoException;

	/**
	 * 功能描述：查找授权qs Id（不考虑del）
	 * @author Zhanghui 2015/5/22
	 */
	public ProductQSVO getVOWithoutDel(Long qsId, Long businessId) throws DaoException;

	/**
	 * 功能描述：获取当前qs的主企业名称
	 * @author Zhanghui 2015/5/22
	 */
	public String getOwnerBusNameOfQs(Long qsId) throws DaoException;
	
	/**
	 * 功能描述：获取当前qs的主企业信息
	 * @author Zhanghui 2015/5/22
	 */
	public ProductQSVO getVOOfOwnerBus(String qsno) throws DaoException;

	/**
	 * 功能描述：编辑一条 企业-qs 关系
	 * @author Zhanghui 2015/5/22
	 */
	public boolean updateOfRight(Long id, int local, int can_use, int can_edit);

	/**
	 * 功能描述：根据组织机构id和qs号，获取一条 企业-qs 关系
	 * @author Zhanghui 2015/5/22
	 */
	public ProductQSVO find(String qsno, Long organization) throws DaoException;

	/**
	 * 功能描述：收回不是该qs号的主企业的编辑权
	 * @author Zhanghui 2015/5/28
	 */
	public boolean updateOfReclaimRights(Long qsId);

	/**
	 * 功能描述：当qs号未被认领前，删除第一家企业-qs号关系后，需要将编辑权转移到另外一家
	 * @author Zhanghui 2015/5/28
	 */
	public void moveRightOfCanEdit(Long qsId) throws DaoException;

	/**
	 * 功能描述：当qs号未被认领前，删除第一家企业-qs号关系后，查找需要将编辑权转移到下一家的 企业-qs id
	 * @author Zhanghui 2015/5/28
	 */
	public Long findTargetIdOfMoveRight(Long qsId) throws DaoException;

	/**
	 * 获取指定企业下可以使用的QS号
	 * @author tanxin 2015-06-01
	 */
	List<ProductQSVO> getListCanUseByOrganization(Long organization) throws DaoException;

	/**
	 * 授予/取消企业该qs号的所有权
	 * @param business_id 企业id
	 * @param qs_id
	 * @param local
	 * 			0  代表授予
	 * 			1  代表取消
	 * @author ZhangHui 2015/6/2
	 * @throws DaoException 
	 */
	public void updateByLocal(Long business_id, Long qs_id, int local) throws DaoException;

	/**
	 * 功能描述：查看xx企业有无xxqs号的使用权
	 * @author Zhanghui 2015/6/19
	 * @throws DaoException 
	 */
	public long count(String qs_no, Long organization) throws DaoException;

	/**
	 * 功能描述：根据企业名称查找一条 该企业-qs 关系
	 * @author Zhanghui 2015/6/24
	 * @throws DaoException 
	 */
	public ProductQSVO findByBusname(String bus_name, Long qs_id) throws DaoException;
}
