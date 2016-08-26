package com.gettec.fsnip.fsn.dao.product.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.AbstractModelDAOImpl;
import com.gettec.fsnip.fsn.dao.product.ProductDAO;
import com.gettec.fsnip.fsn.dao.product.SampleProductDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.SampleProduct;
import com.gettec.fsnip.fsn.util.ImgUtils;
import com.gettec.fsnip.fsn.util.sales.SalesUtil;
import com.gettec.fsnip.fsn.vo.product.ProductManageViewVO;
import com.gettec.fsnip.fsn.vo.sales.DetailAlbumVO;
import com.gettec.fsnip.fsn.vo.sales.PhotoFieldVO;
import com.lhfs.fsn.util.StringUtil;
import com.lhfs.fsn.vo.product.ProductListVo;
import com.lhfs.fsn.vo.product.ProductNutritionVO;
import com.lhfs.fsn.vo.product.ProductRiskVo;


/**
 * Product customized operation implementation
 * 
 * @author Ryan Wang
 */
@Repository(value="sampleProductDAO")
public class SampleProductDAOImpl extends AbstractModelDAOImpl<SampleProduct>
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