package com.gettec.fsnip.fsn.service.product.impl;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.product.Business2QsRelationDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.service.product.Business2QsRelationService;
import com.gettec.fsnip.fsn.service.product.ProductLicenseOfApplicantClaimHandleService;
import com.gettec.fsnip.fsn.service.product.ProductTobusinessUnitService;
import com.gettec.fsnip.fsn.vo.product.ProductQSVO;
import com.gettec.fsnip.fsn.vo.product.ProductionLicenseApplicantClaimHandleVO;

/**
 * 企业-qs号关系  service层 实现类
 * @author ZhangHui 2015/5/15
 */
@Service(value="business2QsRelationService")
public class Business2QsRelationServiceImpl implements Business2QsRelationService{
	@Autowired private Business2QsRelationDAO business2QsRelationDAO;
	@Autowired private ProductLicenseOfApplicantClaimHandleService productLicenseOfApplicantClaimHandleService;
	@Autowired private ProductTobusinessUnitService productToBusinessUnitService;
	
	public Business2QsRelationDAO getDAO() {
		return business2QsRelationDAO;
	}

	/**
	 * 功能描述：分页查询当前登录企业下的qs号，用于在qs权限设置页面展示
	 * @author ZhangHui 2015/5/15
	 */
	@Override
	public List<ProductQSVO> getListOfQsByPage(long organization, int local, int page, int pageSize) throws ServiceException {
		try {
			return getDAO().getListOfQsByPage(organization, local, page, pageSize);
		} catch (DaoException e) {
			throw new ServiceException("Business2QsRelationServiceImpl.getListOfQsByPage()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 功能描述：根据企业组织机构查找企业自己的qs号总数量
	 * @author ZhangHui 2015/5/15
	 */
	@Override
	public long countOfMyOwn(long organization) {
		return getDAO().countOfMyOwn(organization);
	}

	/**
	 * 功能描述：查找当前qs号被授权的企业
	 * @param local
	 * 			0 代表qs号为不是当前企业自己的qs号
	 *          1 代表qs号是当前企业自己的qs号
	 * @author ZhangHui 2015/5/15
	 */
	@Override
	public List<ProductQSVO> getListOfQs(Long qsId, int local) throws ServiceException {
		try {
			return getDAO().getListOfQs(qsId, local);
		} catch (DaoException e) {
			throw new ServiceException("Business2QsRelationServiceImpl.getListOfQsByPage()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 功能描述：新增一条 企业-qs 关系
	 * @param owerBusName 当前qs号真正的主企业名称
	 * @author Zhanghui 2015/5/28
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean create(ProductQSVO vo, String owerBusName) throws ServiceException {
		try {
			if(vo.getBusinessId()==null || "".equals(vo.getBusinessName()) || "".equals(owerBusName)){
				throw new Exception("参数为空");
			}
			
			ProductQSVO orig_vo = getDAO().getVOWithoutDel(vo.getQsId(), vo.getBusinessId());
			if(orig_vo == null){
				// 背景： 系统中不存在这条 企业-qs 记录
				long count = getDAO().count(vo.getQsId(), 1);
				if(count > 0){
					// 1.1 已经存在 主企业-qs 记录，则新增一条 被授权企业-qs 关系(当前企业为被授权企业)
					return getDAO().create(vo.getBusinessId(), vo.getQsId(), 0, 1, vo.isCan_eidt()?1:2);
				}else{
					// 1.2 不存在 主企业-qs 记录
					if(owerBusName.equals(vo.getBusinessName())){
						// 1.2.1 当前企业为主企业，新增一条 主企业-qs 关系
						boolean isSuccess = getDAO().create(vo.getBusinessId(), vo.getQsId(), 1, 1, 1);
						if(isSuccess){
							// 收回其他被授权企业对该qs号的编辑权
							return getDAO().updateOfReclaimRights(vo.getQsId()); 
						}
						return false;
					}else{
						// 1.2.2 当前企业为被授权企业，新增一条 被授权企业-qs 关系
						// 判断有无其他被授权企业对该qs号有编辑权
						count = getDAO().countOfBeAuthorizedCanEdit(vo.getQsId());
						if(count > 0){
							return getDAO().create(vo.getBusinessId(), vo.getQsId(), 0, 1, 2);
						}else{
							return getDAO().create(vo.getBusinessId(), vo.getQsId(), 0, 1, 1);
						}
					}
				}
			}else{
				vo.setId(orig_vo.getId()); // 设置id
				
				// 背景： 系统中存在这条 企业-qs 记录
				// 获取当前qs的主企业id
				String orig_ownerBusName = getDAO().getOwnerBusNameOfQs(vo.getQsId());
				
				if(orig_ownerBusName!=null && !owerBusName.equals(orig_ownerBusName)){
					throw new Exception("拥有当前qs号的所有权企业信息有误，请核对！");
				}
				
				if(owerBusName.equals(vo.getBusinessName())){
					// 2.1  当前企业为主企业，恢复 主企业-qs 关系
					boolean isSuccess = getDAO().updateOfRight(orig_vo.getId(), 1, 1, 1);
					if(isSuccess){
						// 收回其他企业对该qs号的编辑权
						return getDAO().updateOfReclaimRights(vo.getQsId()); 
					}
					return false;
				}else{
					// 2.2 当前企业为被授权企业，恢复 被授权企业-qs 关系
					if(orig_ownerBusName == null){
						// 当前qs号未被认领，查看其他被授权企业有无该qs号的编辑权
						long count = getDAO().countOfBeAuthorizedCanEdit(vo.getQsId());
						if(count > 0){
							return getDAO().updateOfRight(orig_vo.getId(), 0, 1, 2);
						}else{
							return getDAO().updateOfRight(orig_vo.getId(), 0, 1, 1);
						}
					}
					return getDAO().updateOfRight(orig_vo.getId(), 0, 1, 2);
				}
			}
		} catch (DaoException e) {
			throw new ServiceException("[DaoException]Business2QsRelationServiceImpl.create()-->" + e.getMessage(), e.getException());
		} catch (Exception e){
			throw new ServiceException("[Exception]Business2QsRelationServiceImpl.create()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：新增一条 企业-qs 关系
	 * @author Zhanghui 2015/5/18
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean create(Long bussinessId, Long qs_id, String ownerBusName, String currentBusName) throws ServiceException {
		try {
			if(bussinessId==null || qs_id==null || ownerBusName==null || currentBusName==null || 
					"".equals(ownerBusName) || "".equals(currentBusName)){
				throw new Exception("参数为空");
			}
			
			ProductQSVO vo = new ProductQSVO();
			vo.setBusinessId(bussinessId);
			vo.setQsId(qs_id);
			vo.setBusinessName(ownerBusName);
			return create(vo, currentBusName);
		} catch (ServiceException e) {
			throw new ServiceException("Business2QsRelationServiceImpl.create()-->" + e.getMessage(), e.getException());
		} catch (Exception e) {
			throw new ServiceException("Business2QsRelationServiceImpl.create()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：编辑一条qs授权许可
	 * @author Zhanghui 2015/5/19
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateOfRight(Long id, int can_use, int can_edit) {
		return getDAO().updateOfRight(id, can_use, can_edit);
	}

	/**
	 * 功能描述：查找一条qs授权许可
	 * @author Zhanghui 2015/5/18
	 */
	@Override
	public Long findIdByVO(ProductQSVO vo) {
		return getDAO().findIdByVO(vo);
	}

	/**
	 * 功能描述：删除一条qs授权许可
	 * @author Zhanghui 2015/5/19
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(ProductQSVO vo) throws ServiceException {
		try {
			// 删除授权
			getDAO().updateByDel(vo.getId(), 1);
			
			// 将 被授权企业-产品-qs号  设置为qs无效（该企业无法在继续使用qs号录报告）
			productToBusinessUnitService.updateByEffect(vo.getQsId(), vo.getBusinessId(), 0);
		} catch (DaoException e) {
			throw new ServiceException("Business2QsRelationServiceImpl.delete()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 功能描述：删除 企业-qs 记录
	 * @author Zhanghui 2015/5/22
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(Long bussinessId, Long qsId) throws DaoException {
		try {
			ProductQSVO vo = getDAO().find(bussinessId, qsId);
			
			// 1. 删除 当前企业-qs 记录
			getDAO().updateByDel(bussinessId, qsId, vo.isLocal(), 1);
			
			if(vo.isLocal()){
				// 将再次之前的认领申请设置为 过期，当该企业需要再次认领时，还是需要通过审核环节
				ProductionLicenseApplicantClaimHandleVO proLic_handle_vo = productLicenseOfApplicantClaimHandleService.findOfNotOverdue(qsId, bussinessId);
				if(proLic_handle_vo!=null && proLic_handle_vo.getId()!=null){
					productLicenseOfApplicantClaimHandleService.setApplicantOverdue(proLic_handle_vo.getId());
				}
			}
			
			if(vo.isLocal() || (!vo.isLocal() && vo.isCan_eidt())){
				// 当qs号未被认领时，删除有编辑权限的 企业-qs号 关系后，需要将编辑权转移到另外一家
				Long targetId = getDAO().findTargetIdOfMoveRight(qsId);
				if(targetId != null){
					getDAO().moveRightOfCanEdit(targetId);
				}
			}
			
			// 2. 将 qs号-当前企业-产品 设置为无效
			productToBusinessUnitService.updateByEffect(qsId, bussinessId, 0);
			
		} catch (DaoException e) {
			throw new DaoException("Business2QsRelationServiceImpl.delete()-->" + e.getMessage(), e.getException());
		} catch (ServiceException e) {
			throw new DaoException("Business2QsRelationServiceImpl.delete()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 功能描述：批量 删除  被授权企业-qs 关系
	 * @author Zhanghui 2015/5/20
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteOfBeAuthorized(Long qsId, Long owner_bus_id) throws ServiceException{
		try {
			// 批量 删除  被授权企业-qs 关系
			getDAO().updateByDel(qsId, 0, 1);
			
			// 将 被授权企业-产品-qs号  设置为qs无效（该企业无法在继续使用qs号录报告）
			productToBusinessUnitService.updateExOwnerbusByEffect(qsId, owner_bus_id, 0);
		} catch (DaoException e) {
			throw new ServiceException("[DaoException]Business2QsRelationServiceImpl.deleteOfBeAuthorized()-->" + e.getMessage(), e.getException());
		} catch (ServiceException e) {
			throw new ServiceException("[ServiceException]Business2QsRelationServiceImpl.deleteOfBeAuthorized()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 功能描述：查找授权qs数量(排除del为1的记录)
	 * @author Zhanghui 2015/5/19
	 */
	@Override
	public long count(Long qsId, Long businessId) {
		return getDAO().count(qsId, businessId);
	}
	
	/**
	 * 功能描述：根据企业id获取所有qs号
	 * @author ZhangHui 2015/5/20
	 */
	@Override
	public List<ProductQSVO> getListOfQsByPage(Long organization, int page, int pageSize) throws DaoException {
		try {
			return getDAO().getListOfQsByPage(organization, page, pageSize);
		} catch (DaoException e) {
			throw new DaoException("Business2QsRelationServiceImpl.getListOfQsByPage()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 功能描述：通过对比更新前后的qs号，如果：
	 *         a. 前后相同，则不需要更新
	 *         b. 前后不同，则执行更新操作
	 * @param ownerBusName   qs号的主企业
	 * @param currentBusName 当前正在操作该qs号的企业名称
	 * @author Zhanghui 2015/5/21
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateByContrast(Long bussinessId, Long qsId, Long old_edit_qsId, String ownerBusName, 
			String currentBusName) throws DaoException {
		try {
			if(ownerBusName==null || currentBusName==null || "".equals(ownerBusName) || "".equals(currentBusName)){
				throw new Exception("参数为空");
			}
			
			if(ownerBusName.equals(currentBusName)){
				return getDAO().updateByContrast(bussinessId, qsId, old_edit_qsId, 1);
			}
			
			return getDAO().updateByContrast(bussinessId, qsId, old_edit_qsId, 0);
		} catch (DaoException e) {
			throw new DaoException("[DaoException]Business2QsRelationServiceImpl.updateByContrast()-->" + e.getMessage(), e.getException());
		} catch (Exception e) {
			throw new DaoException("[Exception]Business2QsRelationServiceImpl.updateByContrast()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：判断该企业有无编辑当前qs号的权限
	 * @author Zhanghui 2015/5/21
	 */
	@Override
	public boolean isHaveUpdateRightOfQs(String currentBusName, Long now_edit_qsId) throws DaoException {
		try {
			if(currentBusName==null || "".equals(currentBusName) || now_edit_qsId==null){
				throw new Exception("参数为空");
			}
			
			String orig_ownerBusName = getDAO().getOwnerBusNameOfQs(now_edit_qsId);
			if(orig_ownerBusName==null || orig_ownerBusName.equals(currentBusName)){
				// 1 该qs号没有被任何企业认领   或者   当前企业为该qs号的主企业
				return true;
			}else{
				// 2 该qs号已经被其他企业认领，再判断有无编辑权利
				ProductQSVO vo = getDAO().findByBusname(currentBusName, now_edit_qsId);
				if(vo == null){
					throw new Exception("当前操作为不合法操作");
				}
				return vo.isCan_eidt();
			}
		} catch (DaoException e) {
			throw new DaoException("[DaoException]Business2QsRelationServiceImpl.isHaveUpdateRightOfQs()-->" + e.getMessage(), e.getException());
		} catch (Exception e) {
			throw new DaoException("[Exception]Business2QsRelationServiceImpl.isHaveUpdateRightOfQs()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：根据企业组织机构查找企业所有可以使用的qs号总数量
	 * @author Zhanghui 2015/5/21
	 */
	@Override
	public long countOfMyAll(Long organization) throws DaoException {
		try {
			return getDAO().countOfMyAll(organization);
		} catch (DaoException e) {
			throw new DaoException("Business2QsRelationServiceImpl.countOfMyAll()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 功能描述：查找授权qs数量
	 * @author Zhanghui 2015/5/22
	 */
	@Override
	public long count(Long qsId, int local) throws ServiceException {
		try {
			return getDAO().count(qsId, local);
		} catch (DaoException e) {
			throw new ServiceException("Business2QsRelationServiceImpl.count()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 背景：企业基本信息界面，input #qsno 失去焦点后，验证当前qs能否使用和编辑
	 * 功能描述：获取一条 企业-qs 关系
	 * @author Zhanghui 2015/5/22
	 */
	@Override
	public ProductQSVO getRightOfQs(String qsno, Long organization) throws ServiceException {
		try {
			if(organization == null){
				throw new Exception("参数为空");
			}
			
			ProductQSVO vo = new ProductQSVO(); // 默认：local = false
			vo.setCan_use(true);   // 默认：可以使用
			vo.setCan_eidt(true);  // 默认：可以编辑
			vo.setClaimed(true);   // 默认：已经被认领
			
			ProductQSVO ownerBusVo = getDAO().getVOOfOwnerBus(qsno);
			Long ownerBusOrgId = ownerBusVo==null?null:ownerBusVo.getOrganization();
			if(ownerBusOrgId == null){
				/**
				 * vo!=null && vo.isClaimed==false
				 * 			代表  该qs号没有被任何企业认领 
				 */
				vo.setClaimed(false);
				
				// 判断当前登录企业有无该qs号的使用权
				long count = getDAO().count(qsno, organization);
				if(count > 0){
					vo.setLocal(true);
				}else{
					// 当前登录企业没有qs号的使用权，判断当前企业有无提交该qs号的认领申请
					ProductionLicenseApplicantClaimHandleVO applicant_vo = productLicenseOfApplicantClaimHandleService.findOfNotOverdue(
							qsno, organization);
					
					vo.setApplicant_vo(applicant_vo);
				}
				
			}else if(ownerBusOrgId.equals(organization)){
				/**
				 * vo!=null && vo.isClaimed==true && vo.local==true
				 * 			代表 该qs号被当前企业认领
				 */
				vo.setLocal(true);
			}else{
				// 3 该qs号已经被其他企业认领（当前登录企业为被授权企业）
				vo.setBusinessName(ownerBusVo.getBusinessName());
				vo.setClaimed(true);
				
				ProductQSVO pro_qs_vo = getDAO().find(qsno, organization);
				/**
				 * vo!=null && vo.isClaimed==true && vo.local==false && vo.id==null
				 *  		代表  该qs号已经被其他企业认领，且不存在 当前登录企业-qs号  关系
				 * vo!=null && vo.isClaimed==true && vo.local==false && vo.id!=null
				 * 			代表  该qs号已经被其他企业认领，且存在     当前登录企业-qs号 关系 
				 */
				if(pro_qs_vo != null){
					vo.setId(-1L);
					vo.setCan_use(pro_qs_vo.isCan_use());
					vo.setCan_eidt(pro_qs_vo.isCan_eidt());
				}
			}
			
			return vo;
		} catch (DaoException e) {
			throw new ServiceException("[DaoException]Business2QsRelationServiceImpl.findVO()-->" + e.getMessage(), e.getException());
		} catch (Exception e) {
			throw new ServiceException("[Exception]Business2QsRelationServiceImpl.findVO()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：查看当前qs号有无被认领
	 * @author Zhanghui 2015/5/28
	 */
	@Override
	public boolean getClaimedOfQs(Long qsId) throws ServiceException {
		try {
			if(qsId == null){
				throw new Exception("参数为空");
			}
			
			String orig_ownerBusName = getDAO().getOwnerBusNameOfQs(qsId);
			
			if(orig_ownerBusName==null || "".equals(orig_ownerBusName)){
				return false;
			}
			
			return true;
		} catch (ServiceException e) {
			throw new ServiceException("Business2QsRelationServiceImpl.getClaimedOfQs()-->" + e.getMessage(), e.getException());
		} catch (Exception e) {
			throw new ServiceException("Business2QsRelationServiceImpl.getClaimedOfQs()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 获取指定企业下可以使用的QS号
	 * @author tanxin 2015-06-01
	 */
	@Override
	public List<ProductQSVO> getListCanUseByOrganization(Long organization) throws ServiceException{
		try{
			return getDAO().getListCanUseByOrganization(organization);
		}catch(DaoException e){
			throw new ServiceException("Business2QsRelationServiceImpl.getListCanUseByOrganization()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 授予/取消企业该qs号的所有权
	 * @param confer
	 * 			true  代表授予
	 * 			false 代表取消
	 * @author ZhangHui 2015/6/2
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void conferOwnership(Long business_id, Long qs_id, boolean confer) throws ServiceException {
		try {
			long count = getDAO().count(qs_id, business_id);
			if(count > 0){
				getDAO().updateByLocal(business_id, qs_id, confer?1:0);
			}else if(confer){
				getDAO().create(business_id, qs_id, 1, 4, 4);
			}
		} catch (DaoException e) {
			throw new ServiceException("Business2QsRelationServiceImpl.conferOwnership()-->" + e.getMessage(), e.getException());
		}
	}
}
