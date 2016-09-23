package com.gettec.fsnip.fsn.service.operate;

import com.gettec.fsnip.fsn.dao.operate.OperateInfoDAO;
import com.gettec.fsnip.fsn.model.operate.OperateInfo;
import com.gettec.fsnip.fsn.service.common.BaseService;

/**
 * Created by wb on 2016/9/17.
 */
public interface OperateInfoService extends BaseService<OperateInfo,OperateInfoDAO> {
    /**
     * 根据ID获取规模信息
     * @param id
     * @return
     */
    OperateInfo getFindByIdOperateInfo(Long id);

    /**
     * 保存或者修改规模信息
     * @param operateInfo
     * @return
     */
    OperateInfo saveOrEditOperateInfo(OperateInfo operateInfo);

}
