package com.gettec.fsnip.fsn.service.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.business.CirculationPermitDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.CirculationPermitInfo;
import com.gettec.fsnip.fsn.service.business.CirculationPermitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;

/**
 * CirculationPermitServiceImpl service implementation
 * @author Hui Zhang
 */
@Service(value="circulationPermitService")
public class CirculationPermitServiceImpl extends BaseServiceImpl<CirculationPermitInfo,CirculationPermitDAO>
		implements CirculationPermitService{
	@Autowired private CirculationPermitDAO circulationPermitDAO;

	/**
	 * 按食品流通许可证编号查找一条食品流通许可证信息
	 * @param distributionNo
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public CirculationPermitInfo findByDistributionNo(String distributionNo) throws ServiceException {
		try {
			return circulationPermitDAO.findById(distributionNo);
		} catch (JPAException jpae) {
			throw new ServiceException("【service-error】按食品流通许可证编号查找一条食品流通许可证信息，出现异常。", jpae);
		}
	}

	/**
	 * 保存一条食品流通许可证信息
	 * @param distribution
	 * @param isNew
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(CirculationPermitInfo distribution, boolean isNew) throws ServiceException {
		if(isNew){
			create(distribution);
		}else{
			update(distribution);
		}
	}

	/**
	 * 保存食品流通许可证信息
	 * @param distribution
	 * @return void
	 * @throws ServiceException 
	 * @author ZhangHui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(CirculationPermitInfo distrib) throws ServiceException {
		try {
			if(distrib == null){ return; }
			CirculationPermitInfo orig_distrib = circulationPermitDAO.findById(distrib.getDistributionNo());
			if(orig_distrib == null){
				create(distrib);
			}else{
				setDistributionVal(orig_distrib, distrib);
				update(orig_distrib);
			}
		} catch (JPAException jpae) {
			throw new ServiceException("CirculationPermitServiceImpl.save()-->" + jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 更新前赋值操作
	 * @param orig_distrib
	 * @param distrib
	 * @return void
	 * @author ZhangHui
	 */
	private void setDistributionVal(CirculationPermitInfo orig_distrib,
			CirculationPermitInfo distrib) {
		orig_distrib.setLicensingAuthority(distrib.getLicensingAuthority());
		orig_distrib.setLegalName(distrib.getLegalName());
		orig_distrib.setLicenseName(distrib.getLicenseName());
		orig_distrib.setStartTime(distrib.getStartTime());
		orig_distrib.setEndTime(distrib.getEndTime());
		orig_distrib.setSubjectType(distrib.getSubjectType());
		orig_distrib.setBusinessAddress(distrib.getBusinessAddress());
		orig_distrib.setOtherAddress(distrib.getOtherAddress());
		orig_distrib.setToleranceRange(distrib.getToleranceRange());
		orig_distrib.setToleranceTime(distrib.getToleranceTime());
		orig_distrib.setManageType(distrib.getManageType());
		orig_distrib.setManageProject(distrib.getManageProject());
	}

	@Override
	public CirculationPermitDAO getDAO() {
		return circulationPermitDAO;
	}
}