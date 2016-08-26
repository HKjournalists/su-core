package com.gettec.fsnip.fsn.dao.base.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.base.CountryDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.base.Country;
import com.gettec.fsnip.fsn.model.base.CountryToBarcode;
/**
 * countryDAO customized operation implementation
 * 
 * @author Longxianzhen 2015/05/22
 */
@Repository(value = "countryDAO")
public class CountryDAOImpl extends BaseDAOImpl<Country>
		implements CountryDAO {

	
	/**
     * 根据条形码前三位获取对应的国家
	 * @author longxianzhen 2015/05/22
     */
	@SuppressWarnings("unchecked")
	@Override
	public Country getCountryByBar3(int bar3) throws DaoException {
		try {
            String sql = "select * from country_to_barcode e where ?1>=e.bar3Int_start AND ?2<=e.bar3Int_end";
            Query query = entityManager.createNativeQuery(sql,CountryToBarcode.class);
            query.setParameter(1, bar3);
            query.setParameter(2, bar3);
            List<CountryToBarcode> countrys=query.getResultList();
            if(countrys.size()>0){
            	return countrys.get(0).getCountry();
            }
            return null;
		} catch (Exception e) {
	        throw new DaoException("CountryDAOImpl.getCountryByBar3()-->"+"dao层按条码前3为查询国家出错", e);
	    }
	}
	
}
