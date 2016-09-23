package com.gettec.fsnip.fsn.dao.product.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.ProductStdCertificationDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.base.Certification;

@Repository(value="productStdCertificationDAO")
public class ProductStdCertificationDAOImpl extends BaseDAOImpl<Certification>
		implements ProductStdCertificationDAO {

	/**
	 * 获取标准的认证信息类别
	 * @throws DaoException 
	 */
	@Override
	public List<Certification> getListOfStandCertification() throws DaoException {
		try {
			String condition = " WHERE e.stdStatus = 0 ";
			return getListByCondition(condition, null);
		} catch (JPAException jpae) {
			throw new DaoException("【dao-error】获取标准的认证信息类别，出现异常！", jpae.getException());
		}
	}

	/**
	 * 按名称查找一条标准的认证信息
	 * @throws DaoException 
	 */
	@Override
	public Certification findByName(String name) throws DaoException {
		try {
			String condition = " WHERE e.name = ?1";
			Object[] params = new Object[]{name};
			List<Certification> result = this.getListByCondition(condition, params);
			if(result.size() > 0){
				return result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("【dao-error】获取标准的认证信息类别，出现异常！", jpae.getException());
		}
	}

	/**
	 * 按产品id查找产品的认证信息集合
	 * @throws DaoException 
	 */
	@Override
	public List<Certification> getListOfStandCertificationByProductId(
			Long productId) throws DaoException {
		List<Object> listCer=null;
		try{
			String sql = "SELECT cer.id,cer.imgurl,cer.message,cer.`name`,res.URL  FROM certification cer RIGHT JOIN " + 
					"(SELECT cert_id,resource_id FROM business_certification WHERE id IN " + 
					"(SELECT business_cert_id FROM business_certification_to_product where product_id=?1)) " + 
					"tmp1 ON cer.id=tmp1.cert_id LEFT JOIN t_test_resource res ON " + 
					"tmp1.resource_id=res.RESOURCE_ID";
			listCer=this.getListBySQLWithoutType(null, sql, new Object[]{productId});
			List<Certification> listCerts = new ArrayList<Certification>();
			if (listCer.size()>0) {
                for(int i=0;i<listCer.size();i++){
                    Object[] obj = (Object[]) listCer.get(i);
                    Certification vo = new Certification();
                    vo.setId(obj[0]!=null?Long.valueOf(obj[0].toString()):null);
                    vo.setImgUrl(obj[1]!=null?obj[1].toString():"");
                    vo.setMessage(obj[2]!=null?obj[2].toString():"");
                    vo.setName(obj[3]!=null?obj[3].toString():"");
                    vo.setDocumentUrl(obj[4]!=null?obj[4].toString():"");
                    listCerts.add(vo);
                }
            }
			return listCerts;
		}catch (JPAException jpae) {
			throw new DaoException("【dao-error】按产品id查找产品的认证信息集合时，出现异常！", jpae.getException());
		}
	}
	
}
