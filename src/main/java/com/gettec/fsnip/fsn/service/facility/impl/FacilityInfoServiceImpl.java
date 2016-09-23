package com.gettec.fsnip.fsn.service.facility.impl;

import com.gettec.fsnip.fsn.dao.facility.FacilityInfoDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.facility.FacilityInfo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.facility.FacilityInfoService;
import com.gettec.fsnip.fsn.service.facility.FacilityMaintenanceRecordService;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_PRODUCT_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_PRODUCT_PATH;

/**
 * Created by wb on 2016/9/13.
 *
 */
@Service("facilityInfoService")
public class FacilityInfoServiceImpl extends BaseServiceImpl<FacilityInfo,FacilityInfoDAO> implements FacilityInfoService {
    private  static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private FacilityInfoDAO facilityInfoDAO;

    @Autowired
    private FacilityMaintenanceRecordService facilityMaintenanceRecordService;

    @Autowired
    private ResourceService resourceService;

    public FacilityInfoDAO getDAO() {
        return facilityInfoDAO;
    }

   /**
    * 更加条件获取设备信息集合
    * @author wb
    * @date 2016.9.14
    * @param facilityName 查询参数
    * @param page 页码
    * @param pageSize  每页显示条数
    * @return 返回List集合
    */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<FacilityInfo> getFacilityInfoList(Long busniessId,int page, int pageSize, String facilityName) {
        List<FacilityInfo> facilityInfoList = null;
        try {
            facilityInfoList = facilityInfoDAO.getFacilityInfoList(busniessId,page,pageSize,facilityName);
        } catch (JPAException e) {
            e.printStackTrace();
        }
        return facilityInfoList;
    }

    /**
     * 更加条件获取设备信息集合
     * @author wb
     * @date 2016.9.14
     * @param facilityName 查询参数
     * @return 返回数据总条数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Long getFacilityCount(Long busniessId,String facilityName) {
        Long total = null;
        try {
            total = facilityInfoDAO.getFacilityCount(busniessId,facilityName);
        } catch (JPAException e) {
            e.printStackTrace();
        }
        return total;
    }

    /**
     * 保存或者修改设备信息
     * @author wb
     * @date 2016.9.16
     * @param facilityInfo
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean facilitySaveOrEdit(FacilityInfo facilityInfo) {

        try {
            if(facilityInfo.getId()==null || "".equals(facilityInfo.getId())){
                Resource rs = this.getImgResource(facilityInfo.getResource());
                facilityInfo.setCreateTime( new Date());
                facilityInfo.setResource(rs);
                facilityInfoDAO.persistent(facilityInfo);
            }else {
                Resource rs = null;
                if (facilityInfo.getResource() != null && facilityInfo.getResource().getId() != null) {
                    rs = resourceService.findById(facilityInfo.getResource().getId());
                }
                if (rs == null) {
                    rs = this.getImgResource(facilityInfo.getResource());
                }
                FacilityInfo e = facilityInfoDAO.findById(facilityInfo.getId());
                e.setFacilityName(facilityInfo.getFacilityName());
                e.setManufacturer(facilityInfo.getManufacturer());
                e.setFacilityType(facilityInfo.getFacilityType());
                e.setFacilityCount(facilityInfo.getFacilityCount());
                e.setApplication(facilityInfo.getApplication());
                e.setBuyingTime(facilityInfo.getBuyingTime());
                e.setResource(rs);
                e.setRemark(facilityInfo.getRemark());
            }
            return true;
        } catch (JPAException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return false;
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void deleteResource(Resource resource) {
        try {
            if(resource!=null){
                resourceService.delete(resource);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传图片
     * @param resource
     * @return
     */
    private Resource getImgResource(Resource resource) {

        String code = Math.random()+"";
               code = code.substring(2,code.length());
        UploadUtil uploadUtil = new UploadUtil();
        String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_PRODUCT_PATH) + "/" + code;
        String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PRODUCT_PATH) + "/" + code;

        if (resource.getFile() != null) {
            String randomStr = SDF.format(new Date()) + (new Random().nextInt(1000) + 1);
            String name = code + "-" + randomStr + "." + resource.getType().getRtDesc();
            boolean isSuccess = uploadUtil.uploadFile(resource.getFile(), ftpPath, name);
            if (isSuccess) {
                String url;
                if (UploadUtil.IsOss()) {
                    url = uploadUtil.getOssSignUrl(ftpPath + "/" + name);
                } else {
                    url = webUrl + "/" + name;
                }
                resource.setUrl(url);
                resource.setName(name);
            }
        }else{
            return null;
        }
        return resource;
    }

    /**
     * 删除设备信息以及相关的养护信息
     * @author wb
     * @date 2016.9.16
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean deleteFacilityInfo(Long id) {
        try {
            FacilityInfo es = facilityInfoDAO.findById(id);
            facilityInfoDAO.remove(es);
            facilityMaintenanceRecordService.deleteMaintenance(id);
            return true;
        } catch (JPAException e) {
            e.printStackTrace();
        }
        return false;
    }
}
