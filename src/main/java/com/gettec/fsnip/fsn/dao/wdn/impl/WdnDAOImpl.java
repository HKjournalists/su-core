package com.gettec.fsnip.fsn.dao.wdn.impl;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.wdn.WdnDAO;
import com.gettec.fsnip.fsn.model.wdn.WdnOrderInfo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by hughxiang on 2016/1/22.
 */
@Repository("wdnDAO")
public class WdnDAOImpl extends BaseDAOImpl<WdnOrderInfo> implements WdnDAO{

    @Override
    public List<WdnOrderInfo> getOrdersByUserId(Long userId) {
        String sql = "SELECT e FROM " + entityClass.getName() + " e WHERE e.userId =:userId";
        Query query = entityManager.createQuery(sql, WdnOrderInfo.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
