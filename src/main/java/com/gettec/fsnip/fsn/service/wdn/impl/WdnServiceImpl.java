package com.gettec.fsnip.fsn.service.wdn.impl;

import com.gettec.fsnip.fsn.dao.wdn.WdnDAO;
import com.gettec.fsnip.fsn.model.wdn.WdnOrderInfo;
import com.gettec.fsnip.fsn.service.wdn.WdnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by hughxiang on 2016/1/22.
 */

@Service("wdnService")
public class WdnServiceImpl implements WdnService{

    @Autowired
    private WdnDAO wdnDAO;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean saveWdnOrder(WdnOrderInfo order) {
        try {
            wdnDAO.persistent(order);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<WdnOrderInfo> getOrdersByUserId(Long userId) {
        return wdnDAO.getOrdersByUserId(userId);
    }
}
