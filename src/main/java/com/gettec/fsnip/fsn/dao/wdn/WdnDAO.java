package com.gettec.fsnip.fsn.dao.wdn;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.wdn.WdnOrderInfo;

import java.util.List;

/**
 * Created by hughxiang on 2016/1/22.
 */
public interface WdnDAO extends BaseDAO<WdnOrderInfo> {
    List<WdnOrderInfo> getOrdersByUserId(Long userId);
}
