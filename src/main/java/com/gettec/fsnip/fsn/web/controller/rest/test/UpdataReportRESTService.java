package com.gettec.fsnip.fsn.web.controller.rest.test;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.service.market.UpdataReportService;
import com.gettec.fsnip.fsn.service.test.TestResultService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.report.ReportVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.vo.report.UpdataReportVO;

/**
 * Create Time 2015-03-25
 * @author HuangYog 
 * @email huangyong@fsnip.com
 */
@Controller
@RequestMapping("/test/applyTimes")
public class UpdataReportRESTService {
    @Autowired private UpdataReportService updataReportService;
    @Autowired private TestResultService testResultService;
    /**
     * 加载申请报告跟新所有对象
     * @param status 消息的状态
     * @param configure 过滤条件
     * @author HuangYog
     */
    @RequestMapping(method=RequestMethod.GET, value="/getApplyReportTimes/{status}")
    public View getApplyReportTimesAll(HttpServletRequest req, HttpServletResponse resp,
                                   @PathVariable("status") Integer status,
                                   @RequestParam("page") Integer page,
                                   @RequestParam("pageSize") Integer pageSize,
                                   @RequestParam(required = false) String configure,Model model){
        ResultVO resultVO = new ResultVO();
        try{
            AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
            List<UpdataReportVO> lists = updataReportService.findAllForCondition(info.getOrganization(),status,configure, page, pageSize);
            Long count = updataReportService.getApplyReportTimesCount(info.getOrganization(),status,configure);
            model.addAttribute("data", lists);
            model.addAttribute("count", count);
        }catch (Exception e) {
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
            e.printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    
    /**
     * 查看相关产品是否存在某种类型的最新报告
     * @param proId 产品id
     * @param reportType 报告类型
     * @param status 0表示是在未处理中查找是否最新报告，1,表示处理中
     * @author HuangYog
     */
    @SuppressWarnings({ "deprecation", "static-access", "rawtypes", "unchecked" })
    @RequestMapping(method=RequestMethod.GET, value="/handleApply/{proId}/{reportType}/{status}")
    public View handleReportUpApply(HttpServletRequest req, HttpServletResponse resp,
                                   @PathVariable("proId") Long proId,@PathVariable("reportType") String reportType,
                                    @PathVariable("status") Integer status,Model model){
        ResultVO resultVO = new ResultVO();
        try{
            AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
            /* 1 判断该用户是否有权限录入报告*/
            Object obj =  AccessUtils.getUriList();
            JSONArray array = new JSONArray();
            array.element(obj);
            List arrayList = array.toList(array);
            boolean isPower = false;//是否有权限  true 有  false 没有 
            String pageName = "";
            for( int i = 0 ; i <arrayList.size() ; i++) {
               List<String> url = (List<String>) (arrayList.get(i)!=null?arrayList.get(i):null);
               if (url !=null && url.size() > 0) {
                for(String str:url) {
                    String str1 = str.substring(str.lastIndexOf("/")+1);
                    if (str1.equals("add_testreport.html")||str1.equals("subBusiness_addReport.html")) {
                        isPower = true;
                        pageName = str1;
                        break;
                    }
                }
                if (isPower) {
                    break;
                }
               }
            }
            /* 2 用户点击处理后查找是否已有新报告 */
            ReportVO reportVO = testResultService.getNewestReportForPIdAndReportType(info.getOrganization(),proId,reportType,status);
            model.addAttribute("data", reportVO);
            model.addAttribute("isPower", isPower);
            model.addAttribute("pageName", pageName);
        } catch (ServiceException sex) {
            resultVO.setErrorMessage(sex.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
            sex.printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    
    /**
     * 根据id修改该条报告申请更新记录的状态值
     * @author HuangYog
     */
    @RequestMapping(method=RequestMethod.GET, value="/{id}")
    public View setApplyReportStatus(HttpServletRequest req, HttpServletResponse resp,
                                   @PathVariable("id") Long id,Model model){
        ResultVO resultVO = new ResultVO();
        try{
            updataReportService.changeApplyReportStatus(id,1);
        } catch (ServiceException sex) {
            resultVO.setErrorMessage(sex.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
            sex.printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

}