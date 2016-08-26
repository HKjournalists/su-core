package com.gettec.fsnip.fsn.web.controller.rest;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.service.base.HomeService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.home.NewsColumnVO;
import com.gettec.fsnip.fsn.vo.home.NewsInformationVO;

/**
 * 新门面加载界面数据
 * @author HuangYong
 */
@Controller
@RequestMapping("/home")
public class HomeRESTService {
    
    @Autowired private HomeService homeService;
    
    /**
     * 加载home-new页面基本数据 :加载食安云新闻
     * @param fsc 食安云独家栏需要显示的条数
     * @param req
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/initializeFsc/{fsc}")
    public View initializeFsc(@PathVariable("fsc") int fsc , HttpServletRequest req, 
            HttpServletResponse response, Model model) throws IOException {
        ResultVO resultVO = new ResultVO();
        try {
            model = homeService.getFscNews(fsc,model);//获取食安云独家
        } catch (ServiceException e) {
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
            e.printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    
    /**
     * 加载home-new页面基本数据 :加载滚动专区新闻信息
     * @param roll 滚动新闻条数
     * @param req
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/initializeRoll/{roll}")
    public View initializeRoll(@PathVariable("roll") int roll, HttpServletRequest req,
            HttpServletResponse response, Model model) throws IOException {
        ResultVO resultVO = new ResultVO();
        try {
            List<NewsInformationVO> rollNews = homeService.getRollNews(roll);//获取滚动专区信息
            model.addAttribute("rollNews",rollNews);
        } catch (ServiceException e) {
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
            e.printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    
    /**
     *  加载home-new页面基本数据 :加载所有新闻专栏信息
     * @param column 每个专栏显示的新闻条数
     * @param req
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/initializeColumn/{column}")
    public View initializeColumn(@PathVariable("column") int column,HttpServletRequest req,
             HttpServletResponse response, Model model) throws IOException {
        ResultVO resultVO = new ResultVO();
        try {
            NewsColumnVO messageNews = homeService.getMessageNews(5,column);//获取资讯
            NewsColumnVO subjects= homeService.getMessageNews(6,column);//获取专题
            NewsColumnVO services = homeService.getMessageNews(7,column);//获取服务
            model.addAttribute("messageNews",messageNews);
            model.addAttribute("subjects",subjects);
            model.addAttribute("services",services);
        } catch (ServiceException e) {
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
            e.printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    
    /**
     * 根据新闻id加载该新闻的所有内容
     * @param newId 新闻id
     * @param req
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getDefiniteNew/{newId}")
    public View getDefiniteNew(@PathVariable("newId") Long newId,HttpServletRequest req,
             HttpServletResponse response, Model model) throws IOException {
        ResultVO resultVO = new ResultVO();
        try {
            NewsInformationVO messageNew = homeService.getDefiniteNew(newId); // 根据新闻id加载该新闻的所有内容
            model.addAttribute("messageNews",messageNew);
        } catch (ServiceException e) {
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
            e.printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    
}
