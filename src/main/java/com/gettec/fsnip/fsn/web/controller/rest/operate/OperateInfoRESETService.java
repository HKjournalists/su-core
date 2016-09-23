package com.gettec.fsnip.fsn.web.controller.rest.operate;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.operate.OperateInfo;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.operate.OperateInfoService;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;


/**
 * Created by wb on 2016/9/17.
 * @author wb 2016.9.17
 */
@Controller
@RequestMapping("/operate")
public class OperateInfoRESETService  extends BaseRESTService{

    @Autowired
    private OperateInfoService operateInfoService;
    @Autowired
    private BusinessUnitService businessUnitService;
    /**
     * 根据ID获取规模信息
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/findById")
    public View getFindByIdOperateInfoI(Model model){

        try {
            Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
            Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
            OperateInfo operateInfo = operateInfoService.getFindByIdOperateInfo(fromBusId);
            model.addAttribute("data",operateInfo);
            model.addAttribute("status",true);
        } catch (ServiceException e) {
            model.addAttribute("status",false);
            e.printStackTrace();
        }
        return JSON;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/saveOrEdit")
    public View saveOrEdit(@RequestBody  OperateInfo operateInfo,Model model){
        try {
            Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
            Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
            operateInfo.setBusinessId(fromBusId);
            OperateInfo opInfo = operateInfoService.saveOrEditOperateInfo(operateInfo);
            model.addAttribute("data",opInfo);
            model.addAttribute("status",true);
        } catch (ServiceException e) {
            model.addAttribute("status",false);
            e.printStackTrace();
        }
        return JSON;
    }


}
