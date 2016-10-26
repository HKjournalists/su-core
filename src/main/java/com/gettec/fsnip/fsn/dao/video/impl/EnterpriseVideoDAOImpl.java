package com.gettec.fsnip.fsn.dao.video.impl;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.video.EnterpriseVideoDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.video.Enterprise_video;
import com.gettec.fsnip.fsn.vo.business.BusinessVideoVo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * @author litg
 */
@Repository(value="enterpriseVideoDAO")
public class EnterpriseVideoDAOImpl extends BaseDAOImpl<Enterprise_video>
implements EnterpriseVideoDAO {

    @Override
    public List<BusinessVideoVo> getbusinessByvideo(int page, int page_size, String name, String keyword) throws ServiceException {
        String jpql = "SELECT * FROM business_unit b RIGHT JOIN enterprise_video e ON b.id=e.org_id ";
        if(name!=null&&!"".equals(name)){
            jpql+="WHERE b.name LIKE '%"+name+"%' ";
            if(keyword!=null&&!"".equals(keyword)){
                jpql+="AND b.address LIKE '"+keyword+"%' ";
            }
        }else if(keyword!=null&&!"".equals(keyword)){
            jpql+="WHERE b.address LIKE '"+keyword+"%' ";
        }
        jpql+="GROUP BY b.id";
        Query query = entityManager.createNativeQuery(jpql);
        query.setFirstResult((page-1)*page_size);
        query.setMaxResults(page_size);
        try{
           List<Object[]> objectList=query.getResultList();
           List<BusinessVideoVo> businessVoList=new ArrayList<BusinessVideoVo>();
            if(objectList.size()>0){
                for(Object[] obj : objectList){
                    BusinessVideoVo businessVo=new BusinessVideoVo();
                    Long id=Long.valueOf(obj[0].toString());
                    String sql="SELECT COUNT(*) FROM enterprise_video WHERE org_id="+id;
                    Query query2 = entityManager.createNativeQuery(sql);
                    Number result=(Number)query2.getSingleResult();
                    int count=result.intValue();
                    businessVo.setId(id);
                    businessVo.setName(obj[1].toString());
                    businessVo.setAddress(obj[2].toString());
                    businessVo.setBusType(obj[4].toString());
                    businessVo.setCount(count);
                    businessVoList.add(businessVo);
                }
            }
            return businessVoList;
        }catch(Exception e){
            return null;
        }

    }

    @Override
    public String countbusinessByvideo(int page, int page_size, String name, String keyword) throws ServiceException {
        String jpql="SELECT COUNT(DISTINCT (b.id)) FROM business_unit b INNER JOIN enterprise_video e ON b.id=e.org_id ";
        if(name!=null&&!"".equals(name)){
            jpql+="WHERE b.name LIKE '%"+name+"%' ";
            if(keyword!=null&&!"".equals(keyword)){
                jpql+="AND b.address LIKE '"+keyword+"%' ";
            }
        }else if(keyword!=null&&!"".equals(keyword)){
            jpql+="WHERE b.address LIKE '"+keyword+"%' ";
        }
        Query query = entityManager.createNativeQuery(jpql);
        String result=query.getSingleResult().toString();
        return result;
    }

    @Override
    public List<Enterprise_video> getVideoByOrgid(int page, int page_size, Long orgid) throws ServiceException {
        String jpql = "SELECT * FROM enterprise_video e RIGHT JOIN business_unit b ON b.id=e.org_id WHERE b.id=?1";
        Query query = entityManager.createNativeQuery(jpql);
        query.setParameter(1,orgid);
        query.setFirstResult((page-1)* page_size);
        query.setMaxResults(page_size);
        try{
            List<Object[]> objectList=query.getResultList();
            List<Enterprise_video> enterpriseVideoList=new ArrayList<Enterprise_video>();
            if(objectList.size()>0){
                for(Object[] obj:objectList){
                    Enterprise_video enterprise_video=new Enterprise_video();
                    enterprise_video.setId(obj[0].toString());
                    enterprise_video.setOrg_id(obj[1].toString());
                    enterprise_video.setVideo_url(obj[2].toString());
                    enterprise_video.setVideo_type(obj[3].toString());
                    enterprise_video.setVideo_desc(obj[4].toString());
                    enterprise_video.setSort(Integer.parseInt(obj[5].toString()));
                    enterprise_video.setIs_show(obj[6].toString());
                    enterprise_video.setCreate_user(obj[7].toString());
                    enterprise_video.setCreate_time(obj[8].toString());
                    enterprise_video.setLast_modify_user(obj[9].toString());
                    enterprise_video.setLast_modify_time(obj[10].toString());
                    enterpriseVideoList.add(enterprise_video);
                }
            }
            return enterpriseVideoList;
        }catch(Exception e){
            return null;
        }
    }
}