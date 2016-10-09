package com.gettec.fsnip.fsn.web.controller.rest.account;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.recycle.Recycle_reason;
import com.gettec.fsnip.fsn.service.account.TZBusAccountOutService;
import com.gettec.fsnip.fsn.service.account.TZBusinessRelationService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.account.AccountOutVO;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

/**
 * 台账系统  企业的销售关系
 * @author HuangYog 
 * @email HuangYong@fsnip.com
 */
@Controller
@RequestMapping("/account/relation")
public class TZBusRelationRESTService extends BaseRESTService{

    @Autowired TZBusinessRelationService tzBusRelationService;
    @Autowired TZBusAccountOutService tzBusAccountOutService;

    /**
     * 加载企业的基本信息
     * @author HY
     */
    @RequestMapping(method = RequestMethod.GET, value = "/basicInfo")
    public View loadBusinessUnitBasicInfo(Model model,HttpServletRequest req,HttpServletResponse resp){
        ResultVO resultVO = new ResultVO();
        Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
        try {
            model = tzBusRelationService.loadBusinessUnitBasicInfo(myOrg, model);
        } catch (ServiceException sex) {
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            sex.printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    /**
     * 加载企业退货单号信息List
     * @author HY
     */
    @RequestMapping(method = RequestMethod.GET, value = "/loadReturnAccount/{type}/{page}/{pageSize}")
    public View loadReturnAccountList(@PathVariable(value="page") int page,@PathVariable(value="pageSize") int pageSize,
                                      @RequestParam(value = "no",required = false) String accountNo,
                                      @RequestParam(value = "nameLic",required = false) String nameAndLic,
                                      @PathVariable(value="type") String type,
                                      Model model,HttpServletRequest req,HttpServletResponse resp){
        ResultVO resultVO = new ResultVO();
        Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
        try {
            nameAndLic = URLDecoder.decode(nameAndLic, "UTF-8");
            model = tzBusAccountOutService.loadReturnAccountList(type, myOrg, accountNo, nameAndLic, page, pageSize, model);
        } catch (ServiceException sex) {
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            sex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    /**
     * 根据企业关系加载对应的企业信息List
     * @param type 企业关系 0：供应，1：销售
     * Create Author HY Date 2015-05-17
     */
    @RequestMapping(method = RequestMethod.GET, value = "/returnBus/{type}/{page}/{pageSize}")
    public View loadTZBusRelation(@PathVariable(value="type") int type, Model model,
                                  @PathVariable(value="page") int page,
                                  @PathVariable(value="pageSize") int pageSize,
                                  @RequestParam(value = "name",required = false) String busName,
                                  @RequestParam(value = "lic",required = false) String busLic,
                                  HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
        	Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
            model = tzBusRelationService.loadTZBusRelation(myOrg, type, busName, busLic, page, pageSize, model);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    /**
     * 查询可以退货的产品
     * Create Author HY Date 2015-05-17
     */
    @RequestMapping(method = RequestMethod.GET, value = "/returnGoods/{page}/{pageSize}")
    public View loadTZReturnProduct(Model model,@PathVariable(value="page") int page,
                                    @PathVariable(value="pageSize") int pageSize,
                                    @RequestParam(value = "name",required = false) String proName,
                                    @RequestParam(value = "bar",required = false) String proBar,
                                    HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
        	Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
            model = tzBusRelationService.loadTZReturnProduct(myOrg, proName, proBar, page, pageSize, model);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }

    /**
     * 提交退货信息
     * Create Author HY Date 2015-05-17
     */
    @RequestMapping(method = RequestMethod.POST, value = "/submit")
    public View submitTZReturnProduct(Model model,@RequestBody AccountOutVO accountOut,
    HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
            if(accountOut.getInBusId()!=null){
            	Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
                model = tzBusRelationService.submitTZReturnProduct(myOrg,accountOut,model);
            }
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    /**
     * 根据退货代号初始化退货信息中的数据
     * Create Author HY Date 2015-05-17
     */
    @RequestMapping(method = RequestMethod.GET, value = "/initpage/{id}")
    public View initReturnGoods(Model model,@PathVariable(value="id") Long id,
    HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
                model = tzBusAccountOutService.initReturnGoods(id,model);
        } catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    /**
     * 根据退货代号初始化确认收到退货信息中的数据
     * Create Author HY Date 2015-05-17
     */
    @RequestMapping(method = RequestMethod.GET, value = "/initIn/{id}")
    public View initReturnInGoods(Model model,@PathVariable(value="id") Long id,
    HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
                model = tzBusAccountOutService.initReturnInGoods(id, model);
        } catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    /**
     * 加载当前企业退货的产品详情
     * Create Author HY Date 2015-05-17
     */
    @RequestMapping(method = RequestMethod.GET, value = "/loadDetail/{page}/{pageSize}")
    public View loadReturnGoodsDetail(Model model,@PathVariable(value="page") Integer page,
                                      @PathVariable(value="pageSize") Integer pageSize,
                                      @RequestParam(value="outId",required = false) Long outId,
                                      HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
            model = tzBusAccountOutService.loadReturnGoodsDetail(outId, page, pageSize, model);
        } catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    /**
     * 加载当前企业退货的产品详情
     * Create Author HY Date 2015-05-17
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
    public View deleteReturnInfo(Model model,@PathVariable(value="id") Long id,
                                      HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
        	Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
            model = tzBusAccountOutService.deleteReturnInfo(myOrg, id, model);
        } catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    /**
     * 加载企业退货单号信息List
     * @author HY
     */
    @RequestMapping(method = RequestMethod.GET, value = "/loadReturn/admin/{page}/{pageSize}")
    public View loadAllReturnAccount(@PathVariable(value="page") int page,@PathVariable(value="pageSize") int pageSize,
                                      @RequestParam(value = "nameLic",required = false) String nameOrLic,
                                      Model model,HttpServletRequest req,HttpServletResponse resp){
        ResultVO resultVO = new ResultVO();
        try {
            model = tzBusAccountOutService.loadAllReturnAccount(nameOrLic, page, pageSize, model);
        } catch (ServiceException sex) {
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            sex.printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    /**
     * 加载回收原因
     * @author LiTG
     */
    @RequestMapping(method = RequestMethod.GET, value = "/recycle_reason")
    public View recycle_reason(Model model,HttpServletRequest req,HttpServletResponse resp){
        JSONArray result_ja = new JSONArray();
        for(Recycle_reason recycle_reason : Recycle_reason.values()){
        	JSONObject jo = new JSONObject();
        	String name = recycle_reason.getName();
        	String value = recycle_reason.getValue();
        	jo.put("name", name);
        	jo.put("value", value);
        	result_ja.add(jo);
    	}
        model.addAttribute("flg", true);
        model.addAttribute("recycle_reason", result_ja.toString());
        return JSON;
    }

    /**
     * 获取当前登录企业的来源客户，只查询生产企业
     * Create Author lxz Date 2015-05-17
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getProBus/{page}/{pageSize}")
    public View loadTZBusRelation( Model model,
                                  @PathVariable(value="page") int page,
                                  @PathVariable(value="pageSize") int pageSize,
                                  @RequestParam(value = "name",required = false) String busName,
                                  @RequestParam(value = "lic",required = false) String busLic,
                                  HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
            Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
            model = tzBusRelationService.getProBus(myOrg, busName, busLic, page, pageSize, model);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    /**
     * 根据企业关系加载对应的企业信息List
     * Create Author lxz Date 2015-05-17
     */
    @RequestMapping(method = RequestMethod.GET, value = "/returnOwnBus//{page}/{pageSize}")
    public View loadTZOwnBusRelation(Model model,
                                  @PathVariable(value="page") int page,
                                  @PathVariable(value="pageSize") int pageSize,
                                  @RequestParam(value = "name",required = false) String busName,
                                  @RequestParam(value = "lic",required = false) String busLic,
                                  HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
            Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
            model = tzBusRelationService.loadTZOwnBusRelation(myOrg, busName, busLic, page, pageSize, model);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
}