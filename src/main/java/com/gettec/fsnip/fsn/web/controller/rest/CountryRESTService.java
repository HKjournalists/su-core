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
import com.gettec.fsnip.fsn.model.base.Country;
import com.gettec.fsnip.fsn.service.base.CountryService;
import com.gettec.fsnip.fsn.vo.ResultVO;

/**
 * 国家 rest
 * @author longxianzhen 2015/05/22
 */
@Controller
@RequestMapping("/country")
public class CountryRESTService {
    
    @Autowired private CountryService countryService;
    
    /**
     * 获取所有的国家
	 * @author longxianzhen 2015/05/22
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getAllCountry")
    public View getAllCountry( HttpServletRequest req, 
            HttpServletResponse response, Model model) throws IOException {
        ResultVO resultVO = new ResultVO();
        try {
            List<Country> countrys= countryService.findAll();//获取所有国家
            model.addAttribute("data", countrys);
        } catch (ServiceException e) {
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
            ((Throwable) e.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    
    /**
     * 根据条形码前三位获取对应的国家
	 * @author longxianzhen 2015/05/22
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getCountryByBar3/{bar3}")
    public View getCountryByBar3(@PathVariable("bar3") int bar3 , HttpServletRequest req, 
            HttpServletResponse response, Model model) throws IOException {
        ResultVO resultVO = new ResultVO();
        try {
            Country country = countryService.getCountryByBar3(bar3);//
            model.addAttribute("data", country);
        } catch (ServiceException e) {
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
            ((Throwable) e.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
}
