package com.gettec.fsnip.fsn.service.operate.impl;

import com.gettec.fsnip.fsn.dao.operate.OperateInfoDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.operate.OperateInfo;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.operate.OperateInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by wb on 2016/9/17.
 *
 */
@Service("operateInfoService")
public class OperateInfoServiceImpl extends BaseServiceImpl<OperateInfo,OperateInfoDAO> implements OperateInfoService{

   @Autowired
   private  OperateInfoDAO operateInfoDAO;

    @Override
    public OperateInfoDAO getDAO() {
        return operateInfoDAO;
    }

    /**
     * 根据ID获取规模信息
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OperateInfo getFindByIdOperateInfo(Long id) {
            OperateInfo operateInfo = operateInfoDAO.findBusinessId(id);
            return operateInfo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OperateInfo saveOrEditOperateInfo(OperateInfo operateInfo) {

        try {
            if (operateInfo != null){
                if(operateInfo.getId() == null || "".equals(operateInfo.getId())){
                    operateInfo.setCreateTime(new Date());
                    operateInfoDAO.persistent(operateInfo);
                    return operateInfoDAO.merge(operateInfo);
                }else{
                    OperateInfo operate = operateInfoDAO.findById(operateInfo.getId()) ;
                    operate.setFloorArea(operateInfo.getFloorArea());
                    operate.setOperateScope(operateInfo.getOperateScope());
                    operate.setPersonCount(operateInfo.getPersonCount());
                    operate.setOperateType(operateInfo.getOperateType());
                    operate.setUpdateTime(new Date());
                    return operateInfoDAO.merge(operate);
                }
            }
        } catch (JPAException e) {
            e.printStackTrace();
        }
        return operateInfo;
    }
}
