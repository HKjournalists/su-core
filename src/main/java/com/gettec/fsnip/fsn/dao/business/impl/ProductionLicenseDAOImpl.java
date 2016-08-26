package com.gettec.fsnip.fsn.dao.business.impl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.business.ProductionLicenseDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.business.ProductionLicenseInfo;

/**
 * ProductionLicenseDAO dao implementation
 * @author Hui Zhang
 */
@Repository(value="productionLicenseDAO")
public class ProductionLicenseDAOImpl extends BaseDAOImpl<ProductionLicenseInfo> implements ProductionLicenseDAO {

	/**
	 * 根据资源id和qs号统计该资源是否关联其他qs信息
	 */
	@Override
	public long countRelationshipByResIdAndQsNo(Long resId, Long qsId)
			throws DaoException {
		try{
			String sql="SELECT count(*) FROM productionLicenseInfo_to_resource where resource_id= ?1 AND qs_id != ?2";
			return this.countBySql(sql, new Object[]{resId,qsId});
		}catch(JPAException jpae){
			throw new DaoException("ProductionLicenseDAOImpl.countRelationshipByResIdAndQsNo() "+jpae.getMessage(),jpae);
		}
	}
	
	/**
	 * 功能描述：获取当前企业的所有qs号
	 * @author ZhangHui 2015/5/21
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductionLicenseInfo> getListByBusId(Long bussinessId) throws DaoException {
		 try{
			if(bussinessId == null){
				return null;
			}
			 
	    	String sql = "SELECT pli.* FROM production_license_info pli " +
	    				 "INNER JOIN businessunit_to_prolicinfo b2p ON b2p.qs_id = pli.id AND b2p.del = 0 AND b2p.business_id = ?1";
	    	
	        Query query = entityManager.createNativeQuery(sql, ProductionLicenseInfo.class);
	        query.setParameter(1, bussinessId);
	        
	        return query.getResultList();
        }catch(Exception e){
        	e.printStackTrace();
			throw new DaoException("ProductionLicenseDAOImpl.getListByBusId() 出现异常！",e);
		}
	}

	/**
	 * 功能描述：根据qs号获取一条生产企业信息
	 * @author ZhangHui 2015/5/21
	 */
	@Override
	public ProductionLicenseInfo findByQsno(String qsNo) throws DaoException {
		try {
			String condition = " WHERE e.qsNo = ?1";
			List<ProductionLicenseInfo> result = this.getListByCondition(condition, new Object[]{qsNo});
			if(result.size() > 0){
				return result.get(0);
			}
			return null;
		} catch (JPAException e) {
			e.printStackTrace();
			throw new DaoException("ProductToBusinessUnitDAOImpl.findByQsno() 出现异常", e.getException());
		}
	}

	/**
	 * 功能描述：根据生产许可证编号，获取qs id
	 * @author ZhangHui 2015/6/4
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long findIdByQsno(String qsno) throws DaoException {
		try{
			if(qsno==null || "".equals(qsno)){
				throw new Exception("参数为空");
			}
			 
	    	String sql = "SELECT id FROM production_license_info WHERE qs_no = ?1";
	    	
	    	Query query = entityManager.createNativeQuery(sql);
	        query.setParameter(1, qsno);
	        
	        List<Object> result = query.getResultList();
	        if(result.size() > 0){
	        	Object obj = result.get(0);
	        	return ((BigInteger)obj).longValue();
	        }
	        
	        return null;
        }catch(Exception e){
        	e.printStackTrace();
			throw new DaoException("ProductionLicenseDAOImpl.findIdByQsno() 出现异常！",e);
		}
	}

	/**
	 * 根据产品id查找其所有已绑定且没过期的生产许可证信息
	 * @author longxianzhen  2015/06/26
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductionLicenseInfo> getListByProId(Long proId) throws DaoException{
		try{
	    	String sql = "SELECT pl.* FROM production_license_info pl LEFT JOIN product_to_businessunit ptb ON pl.id=ptb.qs_id WHERE " +
	    			"ptb.PRODUCT_ID=?1 AND ptb.bind=1 AND ptb.effect=1 GROUP BY pl.id";
	    	Query query = entityManager.createNativeQuery(sql,ProductionLicenseInfo.class);
	        query.setParameter(1, proId);
	        List<ProductionLicenseInfo> result = query.getResultList();
	        return result;
        }catch(Exception e){
        	e.printStackTrace();
			throw new DaoException("ProductionLicenseDAOImpl.getListByProId() 出现异常！",e);
		}
	}
}