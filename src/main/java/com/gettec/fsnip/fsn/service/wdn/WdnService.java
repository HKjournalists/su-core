package com.gettec.fsnip.fsn.service.wdn;


import com.gettec.fsnip.fsn.model.wdn.WdnOrderInfo;

import java.util.List;

/**
 * Created by hughxiang on 2016/1/22.
 */
public interface WdnService {

    boolean saveWdnOrder(WdnOrderInfo order);

    List<WdnOrderInfo> getOrdersByUserId(Long userId);
}
