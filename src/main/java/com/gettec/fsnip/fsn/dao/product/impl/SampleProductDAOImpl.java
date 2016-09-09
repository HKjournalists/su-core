package com.gettec.fsnip.fsn.dao.product.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.SampleProductDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.product.SampleProduct;
import com.lhfs.fsn.util.StringUtil;


/**
 * Product customized operation implementation
 * 
 * @author Ryan Wang
 */
@Repository(value="sampleProductDAO")
public class SampleProductDAOImpl extends BaseDAOImpl<SampleProduct>
		implements SampleProductDAO {
	
	@Override
	public SampleProduct checkSampleProduct(SampleProduct sampleProduct) throws ServiceException {
		Criteria criteria=getSession().createCriteria(SampleProduct.class);
		BusinessUnit businessUnit =sampleProduct.getProducer();
		if(null!=businessUnit&&!StringUtil.isBlank(businessUnit.getName())){
			criteria.createAlias("producer", "producer");
			criteria.add(Restrictions.eq("producer.name",businessUnit.getName()));
		}
		if(!StringUtil.isBlank(sampleProduct.getName())){
			criteria.add(Restrictions.eq("name",sampleProduct.getName()));
		}
		if(!StringUtil.isBlank(sampleProduct.getFormat())){
			criteria.add(Restrictions.eq("format",sampleProduct.getFormat()));
		}
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);// ROOT_ENTITY
		
		if(criteria.list().size()>0){
			return (SampleProduct) criteria.list().get(0);
		}
		return null;
	}
}