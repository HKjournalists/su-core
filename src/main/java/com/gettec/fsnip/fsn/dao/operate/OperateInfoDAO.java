package com.gettec.fsnip.fsn.dao.operate;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.operate.OperateInfo;

/**
 * Created by wb on 2016/9/17.
 */
public interface OperateInfoDAO extends BaseDAO<OperateInfo>{
    /**
     * 根据企业ID获取规模信息
     * @param businessId
     * @return
     */
    OperateInfo findBusinessId(Long businessId);

}
