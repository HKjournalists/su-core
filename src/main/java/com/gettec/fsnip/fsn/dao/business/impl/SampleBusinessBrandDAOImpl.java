package com.gettec.fsnip.fsn.dao.business.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.business.SampleBusinessBrandDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.SampleBusinessBrand;
import com.lhfs.fsn.util.StringUtil;

/**
 * BusinessBrand customized operation implementation
 * 
 * @author Ryan Wang
 */
@Repository(value="sampleBusinessBrandDAO")
public class SampleBusinessBrandDAOImpl extends BaseDAOImpl<SampleBusinessBrand>
		implements SampleBusinessBrandDAO {

	
	@Override
	public SampleBusinessBrand checkSampleBusinessBrand(SampleBusinessBrand sampleBusinessBrand)
			throws ServiceException {
		Criteria criteria=this.getSession().createCriteria(SampleBusinessBrand.class);
		
		BusinessUnit businessUnit = sampleBusinessBrand.getBusinessUnit();
		if(null!=businessUnit&&!StringUtil.isBlank(businessUnit.getName())){
			criteria.createAlias("businessUnit", "businessUnit");
			criteria.add(Restrictions.eq("businessUnit.name",businessUnit.getName()));
		}
		if(!StringUtil.isBlank(sampleBusinessBrand.getName())){
			criteria.add(Restrictions.eq("name",sampleBusinessBrand.getName()));
		}
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);// ROOT_ENTITY
		if(criteria.list().size()>0){
			return (SampleBusinessBrand) criteria.list().get(0);
		}
		return null;
	}
}
