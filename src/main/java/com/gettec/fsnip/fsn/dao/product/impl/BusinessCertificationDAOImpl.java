package com.gettec.fsnip.fsn.dao.product.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.BusinessCertificationDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.product.BusinessCertification;
import com.gettec.fsnip.fsn.vo.ProductCertificationVO;

@Repository(value="productCertificationDAO")
public class BusinessCertificationDAOImpl extends BaseDAOImpl<BusinessCertification>
	implements BusinessCertificationDAO {

	/**
	 * 按产品id获取认证信息产品列表
	 */
	@Override
	public List<BusinessCertification> getListOfCertificationByProductId(
			Long productId) throws DaoException {
		try {
			String sql = "SELECT * FROM business_certification WHERE id IN " +
					"(SELECT DISTINCT business_cert_id FROM business_certification_to_product WHERE product_id = ?1)";
			Object[] params = new Object[]{productId};
			return this.getListBySQL(BusinessCertification.class, sql, params);
		} catch (JPAException jpae) {
			throw new DaoException("ProductCertificationDAOImpl.getListOfCertificationByProductId() Exception:"+jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 按企业id获取认证信息产品列表,返回VO
	 */
	@Override
	public List<ProductCertificationVO> getListOfCertificationVOByBusinessId(
			Long businessId) throws DaoException {
		List<ProductCertificationVO> resultVO=null;
		try {
			String condition = " WHERE e.businessId = ?1";
			Object[] params = new Object[]{businessId};
			List<BusinessCertification> listResult = this.getListByCondition(condition, params);
			if(listResult!=null&&listResult.size()>0){
				resultVO=new ArrayList<ProductCertificationVO>();
				for(BusinessCertification pc:listResult){
					ProductCertificationVO pcVO = new ProductCertificationVO();
					pcVO.setId(pc.getId());
					pcVO.setName(pc.getCert().getName());
					pcVO.setUrl(pc.getCertResource().getUrl());
					pcVO.setEndDate(pc.getEndDate());
					pcVO.setFileName(pc.getCertResource().getName());
					resultVO.add(pcVO);
				}
			}
			return resultVO;
		} catch (JPAException jpae) {
			throw new DaoException("ProductCertificationDAOImpl.getListOfCertificationByBsuinessId() Exception:"+jpae.getMessage(), jpae.getException());
		}
	}
	
	/**
	 * 按企业id获取认证信息产品列表，返回ProductCertification实体
	 */
	@Override
	public List<BusinessCertification> getListOfCertificationByBusinessId(
			Long businessId) throws DaoException {
		try {
			String condition = " WHERE e.businessId = ?1";
			Object[] params = new Object[]{businessId};
			return this.getListByCondition(condition, params);
		} catch (JPAException jpae) {
			throw new DaoException("ProductCertificationDAOImpl.getListOfCertificationByBsuinessId() Exception:"+jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 根据企业id和认证类型分页查询认证信息
	 * @author tangxin 2015-05-04
	 */
	@Override
	public List<BusinessCertification> getListOfCertificationByBusIdAndType(Long businessId,int type,int page,int pageSize) throws DaoException {
		try {
			String condition = " WHERE e.businessId = ?1 and e.cert.stdStatus = ?2";
			Object[] params = new Object[]{businessId,type};
			return this.getListByPage(page, pageSize, condition, params);
		} catch (JPAException jpae) {
			throw new DaoException(jpae.getMessage(), jpae.getException());
		}
	}
	
	/**
	 * 根据企业id和认证类型统计数量
	 * @author tangxin 2015-05-04
	 */
	@Override
	public long countByBusIdAndType(Long businessId, int type) throws DaoException {
		try {
			String condition = " WHERE e.businessId = ?1 and e.cert.stdStatus = ?2";
			Object[] params = new Object[]{businessId,type};
			return this.count(condition, params);
		} catch (JPAException jpae) {
			throw new DaoException(jpae.getMessage(), jpae.getException());
		}
	}
	
	/**
	 * 根据认证id统计关联的产品数量
	 */
	@Override
	public Long countProductByBusinessCertificationId(Long busCertId)
			throws DaoException {
		try {
			return this.countBySql("business_certification_to_product", " where business_cert_id= ?1 ", new Object[]{busCertId});
		} catch (JPAException jpae) {
			throw new DaoException("ProductCertificationDAOImpl.countProductByBusinessCertificationId() Exception:"+jpae.getMessage(), jpae.getException());
		}
	}
	
}
