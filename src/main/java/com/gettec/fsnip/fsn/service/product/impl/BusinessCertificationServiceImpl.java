package com.gettec.fsnip.fsn.service.product.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.gettec.fsnip.fsn.dao.product.BusinessCertificationDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.Certification;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.BusinessCertification;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.product.BusinessCertificationService;
import com.gettec.fsnip.fsn.service.product.ProductStdCertificationService;
import com.gettec.fsnip.fsn.vo.ProductCertificationVO;

@Service(value="businessCertificationService")
public class BusinessCertificationServiceImpl extends BaseServiceImpl<BusinessCertification, BusinessCertificationDAO>
			implements BusinessCertificationService{

	@Autowired private BusinessCertificationDAO bsuinessCertificationDAO;
	@Autowired private ResourceService testResourceService;
	@Autowired private ProductStdCertificationService productStdCertificationService;
	
	@Override
	public BusinessCertificationDAO getDAO() {
		return bsuinessCertificationDAO;
	}

	/**
	 * 按产品id获取认证信息图片列表
	 * @throws ServiceException 
	 */
	@Override
	public List<BusinessCertification> getListOfCertificationByProductId(Long productId) throws ServiceException {
		try {
			return bsuinessCertificationDAO.getListOfCertificationByProductId(productId);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 按企业id获取认证信息图片列表，返回VO
	 * @throws ServiceException
	 */
	@Override
	public List<ProductCertificationVO> getListOfCertificationVOByBusinessId(Long businessId) throws ServiceException {
		try {
			return bsuinessCertificationDAO.getListOfCertificationVOByBusinessId(businessId);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 按企业id获取认证信息 ,返回ProductCertification实体
	 * @throws ServiceException 
	 */
	@Override
	public List<BusinessCertification> getListOfCertificationByBusinessId(Long businessId) throws ServiceException {
		try {
			return bsuinessCertificationDAO.getListOfCertificationByBusinessId(businessId);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 根据企业id和认证类型分页查询认证信息
	 * @author tangxin 2015-05-04
	 */
	@Override
	public List<BusinessCertification> getListOfCertificationByBusIdAndType(Long businessId,int type,int page,int pageSize) throws ServiceException {
		try {
			return bsuinessCertificationDAO.getListOfCertificationByBusIdAndType(businessId, type, page, pageSize);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 根据企业id和认证类型统计数量
	 * @author tangxin 2015-05-04
	 */
	@Override
	public long countByBusIdAndType(Long businessId, int type) throws ServiceException {
		try {
			return bsuinessCertificationDAO.countByBusIdAndType(businessId, type);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 保存企业的其他认证信息
	 * @param business
	 * @return void
	 * @throws ServiceException
	 * @author ZhangHui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(BusinessUnit business) throws ServiceException {
		try {
			if(business.getListOfCertification()==null){return;}
			/* 1. 上传新增的图片 */
			testResourceService.saveCertificationResources(business);
			/* 2. 新增和更新认证信息  */
			List<BusinessCertification> listOfCertification = business.getListOfCertification();
			for(BusinessCertification certification : listOfCertification){
				if(certification.getCert() == null) {continue;}
				if("".equals(certification.getCert().getName()) || certification.getCert().getName()==null){continue;}
				if("".equals(certification.getCertResource().getName())||certification.getCertResource().getName()==null){continue;}
				if("".equals(certification.getEndDate())){continue;}
				if(certification.getCert().getStdStatus() != null && certification.getCert().getStdStatus() == 1){ /* 处理荣誉证书 */
					if(certification.getId()!=null && certification.getCert().getId() != null){
					/* 2.1 更新已有的荣誉认证小图标 */
						BusinessCertification orig_certification = bsuinessCertificationDAO.findById(certification.getId());
						Certification certIcon = orig_certification.getCert();
						certIcon.setStdStatus(certification.getCert().getStdStatus());
						certIcon.setName(certification.getCert().getName());
						Resource certIconRes = certification.getCert().getCertIconResource();
						// 如果页面编辑时重新上传了荣誉认证图片
						if(certIconRes != null) {
							certIcon.setImgUrl(certIconRes.getUrl());
						}
						orig_certification.setEndDate(certification.getEndDate());
						update(orig_certification);
					}else if(certification.getCert().getCertIconResource() != null){
					/* 2.2 新增荣誉认证小图标 */
						Certification newCert = new Certification();
						newCert.setImgUrl(certification.getCert().getCertIconResource().getUrl());
						newCert.setStdStatus(certification.getCert().getStdStatus());
						newCert.setName(certification.getCert().getName());
						certification.setBusinessId(business.getId());
						certification.setCert(newCert);
						create(certification);
					}
				}
				/* 处理普通认证信息 */
				Certification certification1 = productStdCertificationService.findByName(certification.getCert().getName());
				if(certification1 !=null){
					certification1.setStdStatus(certification.getCert().getStdStatus());
				}
				if(certification.getId()!=null){
					/* 2.1 更新已有的认证信息 */
					BusinessCertification orig_certification = bsuinessCertificationDAO.findById(certification.getId());
					orig_certification.setCertResource(certification.getCertResource());
					orig_certification.setCert(certification1);
					orig_certification.setEndDate(certification.getEndDate());
					update(orig_certification);
				}else{
					/* 2.2 新增认证信息 */
					certification.setBusinessId(business.getId());
					certification.setCert(certification1);
					//certification.setCertResource(certification.getCertResource());
					create(certification);
				}


			}
			/* 3. 处理删除项   */
			List<BusinessCertification> origCertifications = bsuinessCertificationDAO.getListOfCertificationByBusinessId(business.getId());
			Set<BusinessCertification> removes = getRemoves(origCertifications, listOfCertification);
			if(!CollectionUtils.isEmpty(removes)){
				for(BusinessCertification certification : removes){
					bsuinessCertificationDAO.remove(bsuinessCertificationDAO.findById(certification.getId()));
				}
			}
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]ProductCertificationServiceImpl.save()-->"+jpae.getMessage(), jpae.getException());
		} catch (DaoException dex) {
			throw new ServiceException("[DaoException]ProductCertificationServiceImpl.save()-->"+dex.getMessage(), dex.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]ProductCertificationServiceImpl.save()-->"+sex.getMessage(), sex.getException());
		}
	}
	
	/**
	 * 获取删除的检测项目
	 * @param origItems 原来的检测项目列表
	 * @param nowItems 现在的检测项目列表
	 * @return
	 */
	private Set<BusinessCertification> getRemoves(Collection<BusinessCertification> origCertifications, Collection<BusinessCertification> nowCertifications) {
		Set<BusinessCertification> removes = new HashSet<BusinessCertification>();
		List<Long> currentId = new ArrayList<Long>();
		for(BusinessCertification certification : nowCertifications){
			if(certification.getId() != null){
				currentId.add(certification.getId());
			}
		}
		for(BusinessCertification certification : origCertifications){
			if(!currentId.contains(certification.getId()) && certification.getId()!=null){
				removes.add(certification);
			}
		}
		return removes;
	}

	/**
	 * 根据认证信息id统计关联的产品数量
	 */
	@Override
	public Long countProductByBusinessCertificationId(Long busCertId)
			throws ServiceException {
		try{
			return bsuinessCertificationDAO.countProductByBusinessCertificationId(busCertId);
		}catch(DaoException daoe){
			throw new ServiceException("BusinessCertificationServiceImpl.countProductByBusinessCertificationId() "+daoe.getMessage(),daoe.getException());
		}
	}
}
