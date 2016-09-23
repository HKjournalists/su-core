package com.gettec.fsnip.fsn.web.controller.rest.sales;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.service.sales.SalesCaseService;
import com.gettec.fsnip.fsn.service.sales.SalesResourceService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.AlbumVO;
import com.gettec.fsnip.fsn.vo.sales.SalesCaseVO;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 销售案例
 * @author tangxin 2015/04/24
 *
 */
@Controller
@RequestMapping("/sales/case")
public class CaseRESTService {
    @Autowired SalesCaseService salesCaseService;
    @Autowired SalesResourceService salesResourceService;

    /**
     * Create Date 2015-05-04
     * 在销售案例管理中获取销售案例的List
     * @author HY
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getListSalesCase/{configure}/{page}/{pageSize}")
    public View getListSalesCase(@PathVariable String configure, @PathVariable Integer page, @PathVariable Integer pageSize,
                                 HttpServletRequest req, HttpServletResponse resp, Model model) {
        ResultVO resultVO = new ResultVO();
        try{
            AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
            long totals = salesCaseService.countByConfigure(info.getOrganization(), configure);
            List<SalesCaseVO> listSalesCaseVO = salesCaseService.getListByOrganizationWithPage(info.getOrganization(), configure, page, pageSize);
            model.addAttribute("data", listSalesCaseVO);
            model.addAttribute("totals", totals);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    /**
     * 保持销售案例实例
     * @author
     * Created Date 2015-05-04
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public View createSalesCase(@RequestBody SalesCaseVO salesCaseVO,
                                HttpServletRequest req, HttpServletResponse resp, Model model){
        ResultVO resultVO = new ResultVO();
        try{
            AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
            salesCaseVO = salesCaseService.save(salesCaseVO, info, true);
            model.addAttribute("data", null);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    /**
     * 更新销售案例实例
     * @author HY
     * Created Date 2015-05-04
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public View updateSalesCase(@RequestBody SalesCaseVO salesCaseVO, HttpServletRequest req, HttpServletResponse resp, Model model) {
        ResultVO resultVO = new ResultVO();
        try{
            AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
            salesCaseVO = salesCaseService.save(salesCaseVO, info, false);
            model.addAttribute("data", null);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    /**
     * 验证销售案例名称是否重复
     * @author HY
     * Created Date 2015-05-05
     */
    @RequestMapping(method = RequestMethod.GET, value = "/countName/{name}/{id}")
    public View countContractName(@PathVariable String name, @PathVariable Long id,
                                  HttpServletRequest req, HttpServletResponse resp, Model model) {
        ResultVO resultVO = new ResultVO();
        try{
            AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
            long count = salesCaseService.countByName(name, info.getOrganization(), id);
            model.addAttribute("count", count);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    /**
     * 根据id获取销售案例信息
     * @author HY
     * Created Date 2015-05-03
     */
    @RequestMapping(method = RequestMethod.GET, value = "/findByid/{id}")
    public View findByid(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp, Model model) {
        ResultVO resultVO = new ResultVO();
        try{
            SalesCaseVO salesCaseVO = new SalesCaseVO(salesCaseService.findById(id));
            if(salesCaseVO != null) {
                salesCaseVO.setResource(salesResourceService.getListResourceByGUID(salesCaseVO.getGuid()));
            }
            model.addAttribute("data", salesCaseVO);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    /**
     * 删除销售案例信息(假删除)
     * @author HY
     * CReated Date 2015-05-03
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/delById/{id}")
    public View delBranchById(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp, Model model) {
        ResultVO resultVO = null;
        try{
            AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
            resultVO = salesCaseService.removeById(id, info);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    /**
     * 销售案例相册信息
     * @author HY
     * CReated Date 2015-05-03
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getcasealbum/{id}")
    public View getSalesCaseAlbum(@PathVariable Long id,@RequestParam(value = "cut",required = false) String cut,
                                  HttpServletRequest req, HttpServletResponse resp, Model model) {
        ResultVO resultVO = new ResultVO();
        try{
            //AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
            AlbumVO caseAlbum = salesCaseService.getSalesCaseAlbum(id, cut);
            model.addAttribute("data", caseAlbum);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }



}
