package com.gettec.fsnip.fsn.dao.operate.impl;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.operate.OperateInfoDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.operate.OperateInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wb on 2016/9/17.
 */
@Repository("operateInfoDAO")
public class OperateInfoDAOImpl extends BaseDAOImpl<OperateInfo> implements OperateInfoDAO{
    @Override
    public OperateInfo findBusinessId(Long businessId) {
        try {
            String condition =" WHERE e.businessId = ?1 ";
            List<OperateInfo> opList =  this.getListByCondition(condition, new Object[]{businessId});
       return opList.size()>0?opList.get(0):null;
        } catch (JPAException e) {
            e.printStackTrace();
        }
        return null;
    }
}
