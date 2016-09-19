package com.gettec.fsnip.fsn.dao.operate.impl;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.operate.OperateInfoDAO;
import com.gettec.fsnip.fsn.model.operate.OperateInfo;
import org.springframework.stereotype.Repository;

/**
 * Created by wb on 2016/9/17.
 */
@Repository("operateInfoDAO")
public class OperateInfoDAOImpl extends BaseDAOImpl<OperateInfo> implements OperateInfoDAO{
}
