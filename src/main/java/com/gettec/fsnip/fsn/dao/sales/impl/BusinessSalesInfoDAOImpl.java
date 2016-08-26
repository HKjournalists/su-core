package com.gettec.fsnip.fsn.dao.sales.impl;

import java.util.List;

import com.gettec.fsnip.fsn.exception.JPAException;
import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.sales.BusinessSalesInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.sales.BusinessSalesInfo;
import com.lhfs.fsn.vo.sales.EnterpriseIntroductionVO;
import com.lhfs.fsn.vo.sales.EnterpriseViewImgVO;

/**
 *Create Date 2015-04-24
 * @author HY
 */
@Repository(value="businessSalesInfoDAO")
public class BusinessSalesInfoDAOImpl extends BaseDAOImpl<BusinessSalesInfo> implements BusinessSalesInfoDAO{
	
	/**
	 * 根据组织企业机构获取企业宣传照和logo图片
	 * @author tangxin 2015-04-28
	 */
	@Override
	@SuppressWarnings("unchecked")
	public EnterpriseViewImgVO findEnterpriseViewImgByOrganization(Long organization, boolean description) throws DaoException{
    	try{
    		if(organization == null) {
        		return null;
        	}
    		String cond = "";
    		if(description){
    			cond = ",bus.about";
    		}
        	String sql = " SELECT bus.id,bus.organization,bus.`name`,res.URL,bsi.pub_ptotos_url" + cond + " FROM business_unit bus " +
        			"LEFT JOIN enterprise_registe er ON bus.`name` = er.enterpriteName " +
        			"LEFT JOIN t_business_logo_to_resource b2r ON er.id = b2r.ENTERPRISE_REGISTE_ID " +
        			"LEFT JOIN t_test_resource res ON b2r.RESOURCE_ID = res.RESOURCE_ID " +
        			"LEFT JOIN t_bus_sales_info bsi ON bus.organization = bsi.organization " +
        			"WHERE bus.organization = ?1 order by bus.id desc";
        	List<Object[]> listObjs = this.entityManager.createNativeQuery(sql)
        	 					.setParameter(1, organization).getResultList();
        	EnterpriseViewImgVO viewImgList = null;
        	if(listObjs != null && listObjs.size() > 0) {
        		Object[] obj = listObjs.get(0);
        		String abt = null;
        		if(description) {
        			abt = (obj[5] != null ? obj[5].toString() : null);
        		}
        		viewImgList = new EnterpriseViewImgVO(Long.parseLong(obj[0] != null ? obj[0].toString() : "0"),
        				Long.parseLong(obj[1] != null ? obj[1].toString() : "0"),
        				obj[2] != null ? obj[2].toString() : "",
        				obj[3] !=null ? obj[3].toString() : "",
        				obj[4] != null ? obj[4].toString() : "",abt);
        	}
        	return viewImgList;
    	}catch(Exception e) {
    		throw new DaoException(e.getMessage(), e);
    	}
    } 
    
	
	/**
	 * 根据企业组织机构获取企业信息 (企业门户App接口)
	 * @author tangxin 2015-04-28
	 */
	@Override
	@SuppressWarnings("unchecked")
	public EnterpriseIntroductionVO findEnterpriseIntroductionByOrganization(Long organization) throws DaoException{
    	try{
    		if(organization == null) {
        		return null;
        	}
        	String sql = "SELECT bus.id,bus.organization,bus.about,bus.contact,bus.telephone,bus.email,bus.address," +
        			"bus.website FROM business_unit bus WHERE bus.organization = ?1 ORDER BY bus.id DESC ";
        	List<Object[]> objList = this.entityManager.createNativeQuery(sql)
        	 										.setParameter(1, organization).getResultList();
        	EnterpriseIntroductionVO introduction = null;
        	if(objList != null && objList.size() > 0) {
        		Object[] obj = objList.get(0);
        		introduction = new EnterpriseIntroductionVO(Long.parseLong(obj[0] != null ? obj[0].toString() : "0"),
        				Long.parseLong(obj[1] != null ? obj[1].toString() : "0"),
        				obj[2] != null ? obj[2].toString() : "",
        				obj[3] !=null ? obj[3].toString() : "",
        				obj[4] !=null ? obj[4].toString() : "",
        				obj[5] !=null ? obj[5].toString() : "",
        				obj[6] !=null ? obj[6].toString() : "",
        				obj[7] != null ? obj[7].toString() : "");
        	}
        	return introduction;
    	}catch(Exception e) {
    		throw new DaoException(e.getMessage(), e);
    	}
    }

	/**
	 * 据据业业id查找相关销售信息
	 * HY
	 */
	@Override
	public BusinessSalesInfo findByBusId(Long id) throws DaoException {
		String condition = " WHERE e.businessId = ?1 ORDER BY e.id DESC ";
		try {
			List<BusinessSalesInfo> reslut = this.getListByCondition(condition,new Object[]{id});
			return reslut != null && reslut.size() >0 ? reslut.get(0):null;
		} catch (JPAException jpae) {
			throw new DaoException(jpae.getMessage(), jpae);
		}
	}
}
