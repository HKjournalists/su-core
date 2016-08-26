package com.gettec.fsnip.fsn.web.controller.rest.account;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.service.account.TZStockInfoService;
import com.gettec.fsnip.fsn.service.account.TZStockService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.account.AccountOutVO;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

/**
 * 台账系统  企业的销售关系
 * @author HuangYog 
 * @email HuangYong@fsnip.com
 */
@Controller
@RequestMapping("/account/store")
public class TZStoreRESTService extends BaseRESTService{

    @Autowired TZStockService tzStockService;
    @Autowired
    TZStockInfoService tzStockInfoService;

    /**
     * 添加库存
     * @author HY
     */
    @RequestMapping(method = RequestMethod.GET, value = "/addstore/{barcode}")
    public View createStore(@PathVariable(value = "barcode") String barcode,
                            @RequestParam(value = "num",required = false) Integer num,
                            @RequestParam(value = "qs",required = false) String qs,
                            @RequestParam(value = "stackid",required = false) Long stackid,
                            Model model,HttpServletRequest req,HttpServletResponse resp){
        ResultVO resultVO = new ResultVO();
        Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
        try {
            if(qs!=null){
                model = tzStockService.createStore(myOrg, barcode, num,qs,stackid,model);
            }else{
                resultVO.setMessage("产品qs号不能为空!");
                resultVO.setStatus(SERVER_STATUS_FAILED);
            }
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
     * 按条形码查找库存中是否已存在相关信息
     * @author HY
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getProduct")
    public View getProductByBarcode(@RequestParam(value = "barcode",required = false) String barcode,Model model,HttpServletRequest req,HttpServletResponse resp){
        ResultVO resultVO = new ResultVO();
        try {
            model = tzStockService.getStoreProductByBarcode(barcode, model);
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
     * 按条形码查找库存中是否已存在相关信息
     * @author HY
     */
    @RequestMapping(method = RequestMethod.GET, value = "/validatebarcode/{barcode}")
    public View validatebarcode(@PathVariable(value = "barcode") String barcode,
                                @RequestParam(value = "qs",required = false) String qs,
                                Model model,HttpServletRequest req,HttpServletResponse resp){
        ResultVO resultVO = new ResultVO();
        Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
        try {
            model = tzStockService.validatebarcode(myOrg, barcode, qs, model);
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
     * 加载产品库存数据List
     * @author HY
     */
    @RequestMapping(method = RequestMethod.GET, value = "/loadStoreInfoList/{page}/{pageSize}")
    public View loadStoreInfoList(@PathVariable(value = "page") Integer page,
                                @PathVariable(value = "pageSize") Integer pageSize,
                                @RequestParam(value = "condition", required = false) String condition,
                                Model model,HttpServletRequest req,HttpServletResponse resp){
        ResultVO resultVO = new ResultVO();
        Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
        try {
            model = tzStockInfoService.loadStoreInfoList(myOrg, page, pageSize, condition, model);
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
     * 根据库存ID 加载产品库存明细数据List
     * @author HY
     */
    @RequestMapping(method = RequestMethod.GET, value = "/loadStoreDetailList/{page}/{pageSize}/{sid}")
    public View loadStoreDetailList(@PathVariable(value = "page") Integer page,
                                @PathVariable(value = "pageSize") Integer pageSize,
                                @PathVariable(value = "sid") Long sid,
                                Model model,HttpServletRequest req,HttpServletResponse resp){
        ResultVO resultVO = new ResultVO();
        try {
            model = tzStockInfoService.loadStoreDetailList(page, pageSize, sid, model);
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
     * 确认收到退货信息
     * @author HY
     */
    @RequestMapping(method = RequestMethod.POST, value = "/receiptIn")
    public View receiptReturnGoods(@RequestBody Long outId,
                                Model model,HttpServletRequest req,HttpServletResponse resp){
        ResultVO resultVO = new ResultVO();
        try {
            model = tzStockInfoService.receiptReturnGoods(outId, model);
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
     * 台帐 生产企业添加自己的产品到库存中（生产企业）
     *
     * @author HY
     */
    @RequestMapping(method = RequestMethod.GET, value = "/selfproductlist/{page}/{pageSize}")
    public View loadSelfProductList(@PathVariable(value = "page") int page,
                                    @PathVariable(value = "pageSize") int pageSize,
                                    @RequestParam(value = "name", required = false) String pname,
                                    @RequestParam(value = "bar", required = false) String pbar,
                                    Model model,HttpServletRequest req,HttpServletResponse resp){
        ResultVO resultVO = new ResultVO();
        try {
            Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
            model = tzStockService.loadSelfProductList(myOrg, pname, pbar, page, pageSize, model);
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
     * 确认企业自己将自己的产品添加到库存
     * @author HY
     */
    @RequestMapping(method = RequestMethod.POST, value = "/selfAddStore")
    public View addSelfProductToStore(@RequestBody AccountOutVO accountOut,
                                   Model model,HttpServletRequest req,HttpServletResponse resp){
        ResultVO resultVO = new ResultVO();
        try {
            if(accountOut == null){
                resultVO.setStatus(SERVER_STATUS_FAILED);
                resultVO.setSuccess(false);
            }else{
                Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
                String userName = (String)AccessUtils.getUserName();
                model = tzStockService.addSelfProductToStore(myOrg,userName, accountOut, model);
            }
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
     * 台帐 在商品入库页面加载已经添加商品入库了的产品
     * @author HY
     */
    @RequestMapping(method = RequestMethod.GET, value = "/intaked/{page}/{pageSize}")
    public View loadIntakeProductList(@PathVariable(value = "page") int page,
                                    @PathVariable(value = "pageSize") int pageSize,
                                    @RequestParam(value = "name", required = false) String pname,
                                    @RequestParam(value = "bar", required = false) String pbar,
                                    Model model,HttpServletRequest req,HttpServletResponse resp){
        ResultVO resultVO = new ResultVO();
        try {
            Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
            model = tzStockService.loadIntakeProductList(myOrg,pname,pbar, page, pageSize, model);
        } catch (ServiceException sex) {
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            sex.printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

}
