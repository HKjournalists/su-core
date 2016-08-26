package com.gettec.fsnip.fsn.service.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.business.LiquorSalesLicenseDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.LiquorSalesLicenseInfo;
import com.gettec.fsnip.fsn.service.business.LiquorSalesLicenseService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;

/**
 * LiquorSalesLicenseService service implementation
 * @author xianzhen long
 */
@Service(value="liquorSalesLicenseService")
public class LiquorSalesLicenseServiceImpl extends BaseServiceImpl<LiquorSalesLicenseInfo, LiquorSalesLicenseDAO> 
		implements LiquorSalesLicenseService{
	@Autowired private LiquorSalesLicenseDAO liquorSalesLicenseDAO;

	/**
	 * 保存酒类销售信息
	 * @param liquorSalesLicense
	 * @return void
	 * @throws ServiceException 
	 * @author ZhangHui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(LiquorSalesLicenseInfo liquorSalesLicense) throws ServiceException {
		try {
			if(liquorSalesLicense == null){ return; }
			if(liquorSalesLicense.getId() == null){
				create(liquorSalesLicense);
			}else{
				LiquorSalesLicenseInfo orig_liquorSalesLicense = findById(liquorSalesLicense.getId());
				orig_liquorSalesLicense.setCertificateNo(liquorSalesLicense.getCertificateNo());
				orig_liquorSalesLicense.setLegalName(liquorSalesLicense.getLegalName());
				orig_liquorSalesLicense.setAddress(liquorSalesLicense.getLegalName());
				orig_liquorSalesLicense.setBusinessType(liquorSalesLicense.getBusinessType());
				orig_liquorSalesLicense.setBusinessScope(liquorSalesLicense.getBusinessScope());
				orig_liquorSalesLicense.setStartTime(liquorSalesLicense.getStartTime());
				orig_liquorSalesLicense.setEndTime(liquorSalesLicense.getEndTime());
				update(orig_liquorSalesLicense);
			}
		} catch (ServiceException sex) {
			throw new ServiceException("LiquorSalesLicenseServiceImpl.save()-->" + sex.getMessage(), sex.getException());
		}
	}

	@Override
	public LiquorSalesLicenseDAO getDAO() {
		return liquorSalesLicenseDAO;
	}
}