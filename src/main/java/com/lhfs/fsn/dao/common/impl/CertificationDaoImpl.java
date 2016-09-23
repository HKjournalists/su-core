package com.lhfs.fsn.dao.common.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.model.base.Certification;
import com.lhfs.fsn.dao.common.CertificationDao;
import com.lhfs.fsn.vo.product.CertificationVO;

@Repository
public class CertificationDaoImpl extends BaseDAOImpl<Certification> implements CertificationDao{

	@Override
	public int getRecordCount(String keyword) {
		BigInteger count = (BigInteger) entityManager.createNativeQuery("select count(*) from certification where name like ?1")
				.setParameter(1, "%"+keyword+"%").getSingleResult();
		return count.intValue();
	}

	@Override
	public List<Certification> getCertificationByKeyword(String keyword, int startindex, int pagesize) {
		List<Certification> result = entityManager
				.createQuery(
						"from certification where name like ?1",
						Certification.class).setParameter(1,"%"+keyword+"%")
						.setFirstResult(startindex)
						.setMaxResults(pagesize)
						.getResultList();
		if(result != null && result.size() > 0){
			return result;
		}
		return null;
	}

	@Override
	public Certification getCertByID(Long id) {
		List<Certification> result = entityManager
				.createQuery(
						"from certification where id=?1",
						Certification.class).setParameter(1,id)
						.setMaxResults(1)
						.getResultList();
		if(result != null && result.size() > 0){
			return result.get(0);
		}
		return null;
	}
	
    /**
     * 根据产品id查找该产品的相关认证信息
     * @param id 产品id
     * @return List<CertificationVO> 
     * @author HuangYog 
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<CertificationVO> findByPuroductId(Long id) {
        String sql = "SELECT cert.id,cert.name,cert.imgurl FROM business_certification_to_product bctp " + 
        		"LEFT JOIN business_certification buc ON bctp.business_cert_id = buc.id " + 
        		"LEFT JOIN certification cert ON buc.cert_id = cert.id WHERE bctp.product_id = ?1";
        List<Object[]> objs = entityManager.createNativeQuery(sql).setParameter(1,id).getResultList();
        List<CertificationVO> result = null;
        if(objs !=null && objs.size()>0) {
            result = new ArrayList<CertificationVO>();
            for(int i = 0; i < objs.size(); i++) {
                Object[] obj = (Object[]) objs.get(i);
                Long cid = obj[0] != null ? Long.valueOf(obj[0].toString()) : -1;
                String name = obj[1] != null ? obj[1].toString() : "";
                String imgUrl = obj[2] != null ? obj[2].toString() : "";
                result.add(new CertificationVO(cid, name, imgUrl));
            }
            return result;
        }
        return null;
    }

}
