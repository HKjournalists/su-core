package com.gettec.fsnip.fsn.web.controller.rest.account;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.service.account.TZProductTrailService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.account.TZOriginVO;
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

import java.util.List;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

/**
 * Created by HY on 2015/6/4.
 * desc: 台账溯源
 */
@Controller
@RequestMapping("/account/origin")
public class TZOriginRESTService {

    @Autowired
    TZProductTrailService tzProductTrailService;

    /**
     * 查询产品溯源
     *
     * @author HY
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{barcode}")
    @ResponseBody
    public List<TZOriginVO> loadGoodsOrigin(@PathVariable(value = "barcode") String barcode,
                                @RequestParam(value = "id",required = false) String idAndBatch,
                                @RequestParam(value = "batch",required = false) String batch,
                                Model model, HttpServletRequest req, HttpServletResponse resp) {
        List<TZOriginVO> lists = null;
            try {
                /* 如果轨迹已存在则直接加载下级轨迹 */
                if(idAndBatch!=null){
                    lists = tzProductTrailService.loadGoodsOrigin(barcode,idAndBatch,batch);
                }else{
                    /* 如果轨迹不存在则查找根轨迹 */
                    lists = tzProductTrailService.loadGoodsOrigin(barcode,batch);
                }
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            return lists;
    }

    /**
     * 加载产品详情信息
     * HY
     */
    @RequestMapping(method = RequestMethod.GET, value = "/goodsList/{page}/{pageSize}")
    public View loadAllGoodsList(@PathVariable(value = "page") int page,
                                 @PathVariable(value = "pageSize") int pageSize,
                                 @RequestParam(value = "condition",required = false) String condition,
                                 Model model, HttpServletRequest req, HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
        try {
                model = tzProductTrailService.loadAllGoodsList(myOrg, page, pageSize,condition, model);
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
