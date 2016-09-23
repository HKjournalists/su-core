package com.gettec.fsnip.fsn.service.business.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.business.LiutongToProduceLicenseDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.LicenceFormat;
import com.gettec.fsnip.fsn.model.business.LiutongToProduceLicense;
import com.gettec.fsnip.fsn.model.business.ProductionLicenseInfo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.service.business.LicenceFormatService;
import com.gettec.fsnip.fsn.service.business.LiutongToProduceLicenseService;
import com.gettec.fsnip.fsn.service.business.ProductionLicenseService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.product.ProductTobusinessUnitService;

@Service(value = "liutongToProduceLicenseService")
public class LiutongToProduceLicenseServiceImpl extends
		BaseServiceImpl<LiutongToProduceLicense, LiutongToProduceLicenseDAO>
		implements LiutongToProduceLicenseService {

	@Autowired private LiutongToProduceLicenseDAO liutongToProduceLicenseDAO;
	@Autowired private ProductionLicenseService productionLicenseService;
	@Autowired private ProductTobusinessUnitService productToBusinessUnitService;
	@Autowired private ResourceService testResourceService;
	@Autowired private  LicenceFormatService licenceFormatService;
	
	
	/**
	 * 跟流通企业orgid和生产企业id获取qs列表
	 */
	@Override
	public List<LiutongToProduceLicense> getByOrganizationAndProducerId(
			Long organization, Long producerId) throws ServiceException {
		try{
			String condition = " where e.organization = ?1 and e.producerId = ?2";
			List<LiutongToProduceLicense> lists = liutongToProduceLicenseDAO.getListByCondition(condition, new Object[]{organization,producerId});
			if(lists == null || lists.size()<1 ) {
			    return lists;
			}
			/* 检查qs输入规则为空的则默认匹配第一种 */
			LicenceFormat orig_licenceFormat = licenceFormatService.findById(1);
			for(LiutongToProduceLicense ltpl : lists) {
			    if(ltpl.getQsInstance().getQsnoFormat()==null) {
			        ltpl.getQsInstance().setQsnoFormat(orig_licenceFormat);
			    }
			}
			return lists;
		}catch(JPAException jpae){
			throw new ServiceException("LiutongToProduceLicenseServiceImpl.getByOrganizationAndProducerId()"+jpae.getMessage(),jpae);
		}
	}

	/**
	 * 根据流通企业组织机构、生产企业id以及qs号，获取一条流通企业-生产企业 关系。
	 * @param organization 流通企业组织机构
	 * @param producerId 生产企业id
	 * @param qsNo 
	 * @return LiutongToProduceLicense
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	public LiutongToProduceLicense findByOrgIdAndProducerIdAndQs(
			Long organization, Long producerId, String qsNo)
			throws ServiceException {
		try{
			String condition = " where e.organization = ?1 and e.producerId = ?2 and e.qsInstance.qsNo = ?3";
			List<LiutongToProduceLicense> listProduceLicense = liutongToProduceLicenseDAO
					.getListByCondition(condition, new Object[]{organization,producerId,qsNo});
			if(listProduceLicense==null||listProduceLicense.size()<1) return null;
			return listProduceLicense.get(0);
		}catch(JPAException jpae){
			throw new ServiceException("LiutongToProduceLicenseServiceImpl.findByOrgIdAndProducerIdAndQs()"+jpae.getMessage(),jpae);
		}
	}

	/**
	 * 保存[流通企业-生产企业-qs号]关系
	 * @param listQSInfo
	 * @param organizaton
	 * @return void
	 * @throws ServiceException
	 * @author ZhangHui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(List<LiutongToProduceLicense> listQS, Long organizaton)
			throws ServiceException {
		/*try{
			if(listQS==null || listQS.size()<1) return;
			for(LiutongToProduceLicense qs : listQS){
			    LicenceFormat licenceFormat = licenceFormatService.findbyId(qs.getQsInstance().getQsnoFormat().getId());
			    qs.getQsInstance().setQsnoFormat(licenceFormat);
				LiutongToProduceLicense proLicen = new LiutongToProduceLicense(qs);
				proLicen.setOrganization(organizaton);
				save(proLicen);
			}
		}catch(ServiceException sex){
			throw new ServiceException("LiutongToProduceLicenseServiceImpl.save()-->" + sex.getMessage(), sex.getException());
		}*/
	}
	
	/**
	 * 保存[流通企业-生产企业-qs号]关系
	 * @param produceLicense
	 * @return void
	 * @throws ServiceException
	 * @author ZhangHui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(LiutongToProduceLicense liu2prolice) throws ServiceException {
		try{
			if(liu2prolice == null) { return; }
			/* 1.保存生产许可证（包括图片上传） */
			/*ProductionLicenseInfo orig_proLicense = productionLicenseService.save(liu2prolice.getQsInstance());
			liu2prolice.setQsInstance(orig_proLicense);*/
			// 保存qs基本信息
			boolean isHaveUpdateRight = false;//productionLicenseService.save(liu2prolice.getQsInstance());
			if(isHaveUpdateRight){
				// 保存qs图片
				testResourceService.saveQsResource(liu2prolice.getQsInstance());
			}
			/* 3.保存[流通企业-生产企业-qs号]关系 */
			if(liu2prolice.getId() == null){
				/* 3.1 新增[流通企业-生产企业-qs号]关系 */
				create(liu2prolice);
			}else{
				/* 3.2.2 更新[流通企业-生产企业-qs号]关系 */
				LiutongToProduceLicense orig_liu2prolice = findById(liu2prolice.getId());
				setLiu2prolicenseValue(orig_liu2prolice, liu2prolice);
				update(orig_liu2prolice);
				/* 3.2.1 更新[生产企业-产品-qs号]关系 */
				ProductionLicenseInfo orig_qs = orig_liu2prolice.getQsInstance(); // 原来的qs号
				ProductionLicenseInfo qs = liu2prolice.getQsInstance(); // 修改后的qs号
				if(orig_qs!=null && qs!=null && !qs.getQsNo().equals(orig_qs.getQsNo())){
					//productToBusinessUnitService.updateQsNo(orig_qs.getQsNo(), qs.getQsNo(), orig_liu2prolice.getProducerId());
				}
			}
		}catch(ServiceException sex){
			throw new ServiceException("LiutongToProduceLicenseServiceImpl.save()-->" + sex.getMessage(), sex.getException());
		}
	}
	
	/**
	 * 更新前的赋值操作
	 * @param orig_liu2prolice
	 * @param liu2prolice
	 * @return void
	 * @author ZhangHui
	 */
	private void setLiu2prolicenseValue(LiutongToProduceLicense orig_liu2prolice, LiutongToProduceLicense liu2prolice){
		orig_liu2prolice.setMsg(liu2prolice.getMsg());
		orig_liu2prolice.setOrganization(liu2prolice.getOrganization());
		orig_liu2prolice.setPassFlag(liu2prolice.getPassFlag());
		orig_liu2prolice.setProducerId(liu2prolice.getProducerId());
		orig_liu2prolice.setQsInstance(liu2prolice.getQsInstance());
		orig_liu2prolice.setFullFlag(liu2prolice.isFullFlag());
	}
	
	/**
	 * 审核生产企业的QS信息
	 * @param listQs
	 * @return void
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void approved(List<LiutongToProduceLicense> listQs) throws ServiceException {
		try{
			if(listQs==null||listQs.size()<1){return;}
			for(LiutongToProduceLicense qs : listQs){
				/*将该生产企业下该QS号全部设置为审核通过*/
				List<LiutongToProduceLicense> orig_qsList = findByProducerIdAndQs(qs.getProducerId(),qs.getQsInstance().getQsNo());
				for(LiutongToProduceLicense orig_qs : orig_qsList){
					Set<Resource> attachments = qs.getQsInstance().getQsAttachments(); // 获取页面未被删除的图片
					Set<Resource> removes = testResourceService.getListOfRemoves(
							orig_qs.getQsInstance().getQsAttachments(), attachments);
					orig_qs.getQsInstance().removeQsResources(removes);
					orig_qs.setMsg(qs.getMsg());
					orig_qs.setPassFlag(qs.getPassFlag());
					update(orig_qs);
				}
			}
		}catch(Exception e){
			throw new ServiceException("",e);
		}
	}

	
	/**
	 * 审核通过或退回QS信息
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void approvedByOrgIdAndProduceId(Long organization, Long producerId, boolean isPass)
			throws ServiceException {
		try{
			String condition = " where e.organization = ?1 and e.producerId = ?2";
			List<LiutongToProduceLicense> orig_Qs = liutongToProduceLicenseDAO.getListByCondition(condition, new Object[]{organization,producerId});
			if(orig_Qs==null || orig_Qs.size()<1) return;
			for(LiutongToProduceLicense qs : orig_Qs){
				qs.setMsg(isPass?"审核通过":"审核退回");
				qs.setPassFlag(isPass?"审核通过":"审核退回");
				liutongToProduceLicenseDAO.merge(qs);
			}
		}catch(Exception e){
			throw new ServiceException("",e);
		}
		
	}
	
	/**
	 * 更新生产许可证
	 * @param origQsNo
	 * @param oldQsNo
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateQsNoByOldQsNo(ProductionLicenseInfo origQsNo, String oldQsNo) throws ServiceException {
		try{
			String condition = " where e.qsInstance.qsNo = ?1 ";
			List<LiutongToProduceLicense> listL2P = this.getDAO().getListByCondition(condition, new Object[]{oldQsNo});
			if(listL2P == null) return;
			for(LiutongToProduceLicense l2p : listL2P){
				if(!l2p.getPassFlag().equals("审核通过")){
					l2p.setQsInstance(origQsNo);
					this.getDAO().merge(l2p);
				}
			}
		}catch(Exception e){
			throw new ServiceException(e.getMessage(),e);
		}
	}
	
	/**
	 * 根据qs号统计关联的企业的数量
	 * @param qsNo
	 * @return
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	public long countByQSNo(String qsNo) throws ServiceException{
		try{
			String condition = " where e.qsInstance.qsNo = ?1";
			return getDAO().count(condition, new Object[]{qsNo});
		}catch(JPAException jpae){
			throw new ServiceException(jpae.getMessage(),jpae);
		}
	}
	
	@Override
	public LiutongToProduceLicenseDAO getDAO() {
		return liutongToProduceLicenseDAO;
	}

	/**
	 * 根据生产企业ID和QS号验证该信息是否已经通过审核
	 * @author 郝圆彬
	 */
	@Override
	public boolean  ValidateByProducerIdAndQs(Long producerId, String qsNo)
			throws ServiceException {
		try{
			String condition = " where e.passFlag = '审核通过' and e.producerId = ?1 and e.qsInstance.qsNo = ?2";
			List<LiutongToProduceLicense> listProduceLicense = liutongToProduceLicenseDAO
					.getListByCondition(condition, new Object[]{producerId,qsNo});
			if(listProduceLicense==null||listProduceLicense.size()<1) return false;
			return true;
		}catch(JPAException jpae){
			throw new ServiceException("LiutongToProduceLicenseServiceImpl.ValidateByProducerIdAndQs()"+jpae.getMessage(),jpae);
		}
	}
	
	/**
	 * 根据生产企业ID和QS号验证该信息是否已经通过审核
	 * @author 郝圆彬
	 */
	@Override
	public List<LiutongToProduceLicense> findByProducerIdAndQs(Long producerId, String qsNo)
			throws ServiceException {
		try{
			String condition = " where e.producerId = ?1 and e.qsInstance.qsNo = ?2";
			List<LiutongToProduceLicense> listProduceLicense = liutongToProduceLicenseDAO
					.getListByCondition(condition, new Object[]{producerId,qsNo});
			if(listProduceLicense==null||listProduceLicense.size()<1) return null;
			return listProduceLicense;
		}catch(JPAException jpae){
			throw new ServiceException("LiutongToProduceLicenseServiceImpl.findByProducerIdAndQs()"+jpae.getMessage(),jpae);
		}
	}
}
