package com.gettec.fsnip.fsn.dao.business.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.business.MarketToBusinessPKDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.business.MarketToBusiness;

@Repository(value="marketToBusinessPKDAO")
public class MarketToBusinessPKDAOImpl extends BaseDAOImpl<MarketToBusiness>
implements MarketToBusinessPKDAO{
	
	/**
	 * 根据营业执照号校验是否存在当前市场
	 */
	@Override
	public long getCountByLicenseAndOrg(String license, Long organization)
			throws DaoException {
		try{
			String condition = " WHERE e.license = ?1 and  e.organization = ?2 ";
			return this.count(condition, new Object[]{license,organization});
			
		}catch(Exception jpae){
			throw new DaoException("MarketToBusinessPKDAOImpl-->getCountByLicenseAndOrg：根据营业执照号校验是否存在当前市场！出现异常！"+jpae.getMessage(),jpae);
		}
	}


	/**
	 * 分页加载交易市场下商户
	 */
	@Override
	public List<MarketToBusiness> getByOrganization(
			Map<String, Object> map, int page, int pageSize)
			throws DaoException {
		try {
			String condition =(String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			return this.getListByPage(page, pageSize, condition, params);
		} catch (JPAException jpae) {
			throw new DaoException("MarketToBusinessPKDAOImpl-->getByOrganization：分页加载交易市场下商户！出现异常！", jpae.getException());
		}
	}


	/**
	 * 统计交易市场下商户数量
	 */
	@Override
	public long countByOrganization(Map<String, Object> map)
			throws DaoException {
		try {
			String condition =(String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			return this.count(condition, params);
		} catch (JPAException jpae) {
			throw new DaoException("MarketToBusinessPKDAOImpl-->countByOrganization：统计交易市场下商户数量", jpae.getException());
		}
	}


	/**
	 * 根据企业ID校验当前交易市场是否存在当前商户
	 */
	@Override
	public long getCountBybusIdAndOrg(Long businessId, Long organization)
			throws DaoException {
		try {
			String condition = " WHERE e.business.id = ?1 and  e.organization = ?2 ";
			return this.count(condition, new Object[]{businessId,organization});
		} catch (JPAException jpae) {
			throw new DaoException("MarketToBusinessPKDAOImpl-->getCountBybusIdAndOrg：根据企业ID校验当前交易市场是否存在当前商户", jpae.getException());
		}
	}

	/**
	 * 根据经营主体id获取所选交易市场
	 */
	@Override
	public MarketToBusiness findByBusinessId(Long businessId)
			throws DaoException {
		try{
			String condition = "  WHERE e.business.id = ?1";
			List<MarketToBusiness> listM2B = this.getListByCondition(condition, new Object[]{businessId});
			if(listM2B!=null&&listM2B.size()>0) return listM2B.get(0);
			return null;
		}catch(Exception e){
			throw new DaoException("MarketToBusinessPKDAOImpl-->findByBusinessId()"+e.getMessage(), e);
		}
	}
	
}
