package com.gettec.fsnip.fsn.service.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.business.TaxRegisterDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.TaxRegisterInfo;
import com.gettec.fsnip.fsn.service.business.TaxRegisterService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;

/**
 * taxRegisterService service implementation
 * @author xianzhen long
 */
@Service(value="taxRegisterService")
public class TaxRegisterServiceImpl extends BaseServiceImpl<TaxRegisterInfo, TaxRegisterDAO>  
		implements TaxRegisterService{
	@Autowired private TaxRegisterDAO taxRegisterDAO;

	/**
	 * 保存税务登记信息
	 * @param taxRegister
	 * @return void
	 * @throws ServiceException 
	 * @author Zhanghui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(TaxRegisterInfo taxRegister) throws ServiceException {
		try {
			if(taxRegister == null){ return; }
			if(taxRegister.getId() == null){
				create(taxRegister);
			}else{
				TaxRegisterInfo orig_taxRegister = findById(taxRegister.getId());
				orig_taxRegister.setTaxerName(taxRegister.getTaxerName());
				orig_taxRegister.setLegalName(taxRegister.getLegalName());
				orig_taxRegister.setAddress(taxRegister.getAddress());
				orig_taxRegister.setRegisterType(taxRegister.getRegisterType());
				orig_taxRegister.setBusinessScope(taxRegister.getBusinessScope());
				orig_taxRegister.setApproveSetUpAuthority(taxRegister.getApproveSetUpAuthority());
				orig_taxRegister.setIssuingAuthority(taxRegister.getIssuingAuthority());
				orig_taxRegister.setWithholdingObligations(taxRegister.getWithholdingObligations());
				update(orig_taxRegister);
			}
		} catch (ServiceException sex) {
			throw new ServiceException("TaxRegisterServiceImpl.save()-->" + sex.getMessage(), sex.getException());
		}
	}

	@Override
	public TaxRegisterDAO getDAO() {
		return taxRegisterDAO;
	}
}