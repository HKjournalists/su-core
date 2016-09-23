package com.gettec.fsnip.fsn.dao.facility;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.facility.FacilityInfo;

import java.util.List;

/**
 * Created by wb on 2016/9/13.
 * 设备信息dao接口类
 */
public interface FacilityInfoDAO extends BaseDAO<FacilityInfo> {
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
    List<FacilityInfo> getFacilityInfoList(Long busniessId,int page, int pageSize, String facilityName) throws JPAException;

    /**
     *
     * 更加条件获取设备信息集合
     * @author wb
     * @date 2016.9.14
     * @param facilityName 查询参数
     * @return 返回数据总条数
     */
    Long getFacilityCount(Long busniessId,String facilityName) throws JPAException;
}
