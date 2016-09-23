package com.gettec.fsnip.fsn.web.controller.rest.account;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.EnterpriseRegiste;
import com.gettec.fsnip.fsn.service.account.TZStockInfoService;
import com.gettec.fsnip.fsn.service.account.TZStockService;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.business.AccountBusinessVO;
import com.gettec.fsnip.fsn.vo.business.BusinessMarketVO;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

/**
 * 台账系统  企业的销售关系
 * @author HuangYog 
 * @email HuangYong@fsnip.com
 */
@Controller
@RequestMapping("/account/loadbus")
public class TZCheckBusinessRESTService extends BaseRESTService{

    @Autowired
    BusinessUnitService businessUnitService;
    @Autowired
    TZStockService tzStockService;

    /**
     * 台帐系统政府专员查看注册企业信息
     * @param province 省
     * @param city 市
     * @param area 县
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{page}/{pageSize}")
    public View getAccountEnRegisteList(@PathVariable(value="page")int page,
                                        @PathVariable(value="pageSize") int pageSize,
                                        @RequestParam(value="province" , required = false) String province,
                                        @RequestParam(value="city" , required = false) String city,
                                        @RequestParam(value="area" , required = false) String area,
                                        @RequestParam(value="nl" , required = false) String nameOrLicNo,
                                        @RequestParam(value="btype" , required = false) String btype,
                                        Model model,HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
            province = URLDecoder.decode(province,"UTF-8");
            city = URLDecoder.decode(city,"UTF-8");
            area = URLDecoder.decode(area,"UTF-8");
            nameOrLicNo = URLDecoder.decode(nameOrLicNo,"UTF-8");
            btype = URLDecoder.decode(btype,"UTF-8");
            List<AccountBusinessVO> enRegiste = businessUnitService.getAccountEnRegisteList(page, pageSize, province,city,area,nameOrLicNo,btype);
            Long totalCount = businessUnitService.getAccountEnRegisteListTotal( province, city, area,nameOrLicNo,btype);
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("enRegisteList", enRegiste);
            map.put("totalCount", totalCount);
            model.addAttribute("data", map);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }catch(Exception e){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{busId}")
    public View getAccountBusinessById(@PathVariable(value="busId")Long busId,
                                        Model model,HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
            AccountBusinessVO enRegiste = businessUnitService.getAccountBusinessById(busId);
            model.addAttribute("data", enRegiste);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }catch(Exception e){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public View getRolePermission(@RequestParam(value="page") String html,
            Model model,HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
            model.addAttribute("admin", false);
            int a = html.indexOf("admin_enRegiste.html");
            int b = html.indexOf("admin_test-result-manage.html");
            int c = html.indexOf("admin_test-result-preview1.html");
            if(a>-1){
                model.addAttribute("admin", true);
                return JSON;
            }
            if(b>-1){
                model.addAttribute("admin", true);
                return JSON;
            }
            if(c>-1){
                model.addAttribute("admin", true);
                return JSON;
            }
            List<String> obj = (List<String>) AccessUtils.getUriList();
            if(obj != null && obj.size() > 0){
                for(int i = 0; i < obj.size(); i++){
                    String origHtml = obj.get(i);
                    int x = origHtml.indexOf(html);
                    if(x > -1){
                        String []dir = origHtml.split(html);
                        if(dir!=null && dir.length > 0){
                            String addr = dir[0];
                            if("/fsn-core/views/account/admin/".equals(addr)){
                                model.addAttribute("admin", true);
                                break;
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    /**
     * 加载企业的所有类型
     * @author HY
     */
    @RequestMapping(method = RequestMethod.GET, value = "btype")
    public View loadBusinessType(Model model,HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
            List<BusinessMarketVO> type = tzStockService.loadBusinessType();
            model.addAttribute("type", type);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }catch(Exception e){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

}
