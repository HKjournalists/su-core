package com.gettec.fsnip.fsn.web.controller.rest.facility;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.facility.FacilityInfo;
import com.gettec.fsnip.fsn.model.facility.FacilityMaintenanceRecord;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.facility.FacilityInfoService;
import com.gettec.fsnip.fsn.service.facility.FacilityMaintenanceRecordService;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

/**
 * Created by wb on 2016/9/14.
 * 设备信息接收层
 */
@Controller
@RequestMapping("/facility")
public class FacilityInfoRESTService extends BaseRESTService {

    @Autowired
    private BusinessUnitService businessUnitService;

    @Autowired
    private FacilityInfoService facilityInfoService;

    @Autowired
    private FacilityMaintenanceRecordService facilityMaintenanceRecordService;

    /**
     * 根据条件获取设备信息集合
     * @authar wb
     * @date 2016.9.14
     * @param page  页码
     * @param pageSize  每页显示总条数
     * @param facilityName  设备名称（查询参数）
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/facilityList/{page}/{pageSize}")
    public View getFacilityList(@PathVariable(value="page") int page,@PathVariable(value="pageSize") int pageSize,@RequestParam(value="facilityParam") String facilityName,
                                Model model){
        try {
            Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
            Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
            List<FacilityInfo> facilityList = facilityInfoService.getFacilityInfoList(fromBusId,page,pageSize,facilityName);
            Long total = facilityInfoService.getFacilityCount(fromBusId,facilityName);
            model.addAttribute("data",facilityList);
            model.addAttribute("total",total);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return JSON;
    }

    /**
     * 新增或者修改设备信息
     *  @authar wb
     * @date 2016.9.16
     * @param facilityInfo
     * @param model
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/facilitySaveOrEdit")
    public View facilitySaveOrEdit(@RequestBody FacilityInfo facilityInfo, Model model,HttpServletRequest req,HttpServletResponse resp){
        try {
            Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
            Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
            facilityInfo.setBusinessId(fromBusId);
            boolean flag = facilityInfoService.facilitySaveOrEdit(facilityInfo);

            model.addAttribute("status",flag);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return JSON;
    }
    /**
     * 删除设备信息同时删除关联的养护记录信息
     *  @authar wb
     * @date 2016.9.16
     * @param id
     * @param model
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/deleteFacilityInfo/{id}")
    public View deleteFacilityInfo(@PathVariable(value="id") Long id, Model model,HttpServletRequest req,HttpServletResponse resp){
            boolean flag = facilityInfoService.deleteFacilityInfo(id);
            model.addAttribute("status",flag);
        return JSON;
    }


    /**
     * 根据条件获取养护记录信息集合
     * @authar wb
     * @date 2016.9.14
     * @param page  页码
     * @param pageSize  每页显示总条数
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getByFacilityId/{facilityId}/{page}/{pageSize}")
    public View getByFacilityId(@PathVariable(value="facilityId") Long facilityId,@PathVariable(value="page") int page,@PathVariable(value="pageSize") int pageSize,@RequestParam(value="facilityParam") String facilityParam,
                                Model model,HttpServletRequest req,HttpServletResponse resp){

        List<FacilityMaintenanceRecord> facilityList = facilityMaintenanceRecordService.getByFacilityIdList(facilityId, page, pageSize, facilityParam);
        Long total = facilityMaintenanceRecordService.getByFacilityIdCount(facilityId, facilityParam);
        model.addAttribute("data",facilityList);
        model.addAttribute("total",total);
        return JSON;
    }


    /**
     * 新增或者修改养护记录信息
     *  @authar wb
     * @date 2016.9.16
     * @param model
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/maintenanceSaveOrEdit")
    public View maintenanceSaveOrEdit(@RequestBody FacilityMaintenanceRecord maintenance, Model model,HttpServletRequest req,HttpServletResponse resp){
            boolean flag = facilityMaintenanceRecordService.maintenanceSaveOrEdit(maintenance);
            model.addAttribute("status",flag);
        return JSON;
    }

    /**
     * 删除养护记录信息
     *  @authar wb
     * @date 2016.9.16
     * @param id
     * @param model
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/delMaintenanceInfo/{id}")
    public View delMaintenanceInfo(@PathVariable(value="id") Long id, Model model,HttpServletRequest req,HttpServletResponse resp){
        boolean flag = facilityMaintenanceRecordService.delMaintenanceInfo(id);
        model.addAttribute("status",flag);
        return JSON;
    }
}
