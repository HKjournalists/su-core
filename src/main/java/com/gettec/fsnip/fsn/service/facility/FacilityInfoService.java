package com.gettec.fsnip.fsn.service.facility;


import com.gettec.fsnip.fsn.dao.facility.FacilityInfoDAO;
import com.gettec.fsnip.fsn.model.facility.FacilityInfo;
import com.gettec.fsnip.fsn.service.common.BaseService;

import java.util.List;

/**
 * Created by wb on 2016/9/16.
 */
public interface FacilityInfoService extends BaseService<FacilityInfo,FacilityInfoDAO> {
    /**
     *
     * 更加条件获取设备信息集合
     * @author wb
     * @date 2016.9.14
     * @param busniessId 企业ID
     * @param facilityName 查询参数
     * @param page 页码
     * @param pageSize  每页显示条数
     * @return 返回List集合
     */
    List<FacilityInfo> getFacilityInfoList(Long busniessId,int page, int pageSize, String facilityName);

    /**
     *
     * 更加条件获取设备信息集合
     * @author wb
     * @date 2016.9.14
     *  @param busniessId 企业ID
     * @param facilityName 查询参数
     * @return 返回数据总条数
     */
    Long getFacilityCount(Long busniessId,String facilityName);


    /**
     * 保存或者修改设备信息
     * @author wb
     * @date 2016.9.16
     * @param facilityInfo
     * @return
     */
    boolean facilitySaveOrEdit(FacilityInfo facilityInfo);

    /**
     * 删除设备信息同时删除相关联的养护信息
     * @author wb
     * @date 2016.9.16
     * @return
     */
    boolean deleteFacilityInfo(Long id);
}
