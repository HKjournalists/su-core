package com.gettec.fsnip.fsn.dao.facility.impl;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.facility.FacilityInfoDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.facility.FacilityInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wb on 2016/9/13.
 * 设备信息接口实现类
 */
@Repository("facilityInfoDAO")
public class FacilityInfoDAOImpl extends BaseDAOImpl<FacilityInfo> implements FacilityInfoDAO{
    /**
     *
     * 更加条件获取设备信息集合
     * @author wb
     * @date 2016.9.14
     * @param facilityName 查询参数
     * @param page 页码
     * @param pageSize  每页显示条数
     * @return 返回List集合
     */
    @Override
    public List<FacilityInfo> getFacilityInfoList(int page, int pageSize, String facilityName) throws JPAException {
        String condition = "";
        if(facilityName !=null&&!"".equals(facilityName)){
            condition = " where e.facilityName like '%"+facilityName+"%' ";
        }
        condition+=" ORDER BY e.createTime desc";
        return this.getListByPage(page,pageSize,condition);
    }

    /**
     *
     * 更加条件获取设备信息集合
     * @author wb
     * @date 2016.9.14
     * @param facilityName 查询参数
     * @return 返回数据总条数
     */
    @Override
    public Long getFacilityCount(String facilityName) throws JPAException {
        String condition = null;
        if(facilityName !=null&&!"".equals(facilityName)){
            condition = " where e.facilityName like '%"+facilityName+"%' ";
        }
        return this.count(condition);
    }
}
