package com.gettec.fsnip.fsn.dao.business.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.business.BusinessMarketDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.business.BusinessMarket;
import com.gettec.fsnip.fsn.vo.business.BusinessMarketVO;

@Repository(value="businessMarketDAO")
public class BusinessMarketDAOImpl extends BaseDAOImpl<BusinessMarket>
implements BusinessMarketDAO{

	/**
	 * 根据组织结构查找交易市场信息
	 */
	@Override
	public BusinessMarket findByOrg(Long organization) throws DaoException {
		try {
			String condition = " WHERE e.business.organization = ?1 ";
			List<BusinessMarket> result = this.getListByCondition(condition, new Object[]{organization});
			if(result.size()>0){
				return result.get(0);
			}
			return null;	
		} catch (JPAException jpae) {
			throw new DaoException("BusniessMarketDAOImpl-->findByOrg():按组织结构查询交易市场，出现异常！"+jpae.getMessage(), jpae);
		}
	}

	/**
	 *根据商户ID和交易市场ID查询当前商户是否存在
	 */
	@Override
	public long getCountByBusidAndOrg(Long businessId, Long organization) throws DaoException {
		try{
			String sql = "SELECT COUNT(*) FROM business_market_to_business WHERE marketBusiness_id = "+
							  "(SELECT id FROM business_market WHERE business_id = "+
							  "(SELECT id FROM business_unit WHERE organization = ?1)) AND business_id = ?2 ";
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1, organization);
			query.setParameter(2, businessId);
			return Long.parseLong(query.getSingleResult().toString());
		}catch(Exception e){
			throw new DaoException("BusniessMarketDAOImpl.getCountByBusidAndOrg() 查询当前商户是否存在，出现异常！",e);
		}
	}

	/**
	 * 根据经营主体id 获取所选交易市场
	 */
	@Override
	public BusinessMarketVO findByBusinessId(Long businessId)
			throws DaoException {
		try{
			BusinessMarketVO  busMarketVO= null;
			String sql = "SELECT bm.id,bu.`name`,bu.organization FROM business_market bm LEFT JOIN business_unit bu " +
					"ON bm.business_id = bu.id LEFT JOIN business_market_to_business bm2b " +
					"ON bu.organization = bm2b.organization WHERE bm2b.business_id= ?1";
			List<Object[]> objs = this.getListBySQLWithoutType(null, sql, new Object[]{businessId});
			if(objs!=null&&objs.size()>0){
				Object[] obj = objs.get(0);
				busMarketVO = new BusinessMarketVO();
				busMarketVO.setId(Long.parseLong(obj[0]!=null?obj[0].toString():"-1"));
				busMarketVO.setName(obj[1]!=null?obj[1].toString():"");
				busMarketVO.setOrganization(Long.parseLong(obj[2]!=null?obj[2].toString():"-1"));
			}
			return busMarketVO;
		}catch(Exception e){
			throw new DaoException("BusniessMarketDAOImpl.findByBusinessId() "+e.getMessage(),e);
		}
	}
	
}
