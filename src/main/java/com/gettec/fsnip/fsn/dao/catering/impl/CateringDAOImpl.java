package com.gettec.fsnip.fsn.dao.catering.impl;


import com.gettec.fsnip.fsn.dao.catering.CateringDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.business.Catering;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class CateringDAOImpl extends BaseDAOImpl<Catering> implements CateringDAO {


    @Override
    public Catering findByBusinessId(Long businessId) {
        try {
            String condition = " WHERE e.businessId = ?1 ";
            List<Catering> resultList = this.getListByCondition(condition, new Object[]{businessId});
            if(resultList.size() > 0){
                return resultList.get(0);
            }
        } catch (JPAException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
