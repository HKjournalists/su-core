package com.gettec.fsnip.fsn.web.controller.rest.video;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.video.Enterprise_video;
import com.gettec.fsnip.fsn.service.video.EnterpriseVideoService;
import com.gettec.fsnip.fsn.vo.business.BusinessVideoVo;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

/**
 * Created by xuetaoyang on 2016/10/25.
 */
@Controller
@RequestMapping("/video")
public class VideoRESTService extends BaseRESTService {
    @Autowired
    private  EnterpriseVideoService enterpriseVideoService;
    /**
     * 获取拥有视频监控的企业
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getbusinessByvideo/{page}/{pageSize}")
    public View getBusinessByVideo(Model model, @PathVariable(value="page") int page,
                                   @PathVariable(value="pageSize") int pageSize,
                                   @RequestParam(value="name",required = false) String name,
                                   @RequestParam (value="province",required = false) String province,
                                   @RequestParam (value="address",required = false) String address,
                                   @RequestParam (value="type",required = false) String type,

                                   HttpServletRequest req, HttpServletResponse resp) {
        List<BusinessVideoVo> businessUnitList= null;
        String count="";
        try {
            businessUnitList = enterpriseVideoService.getbusinessByvideo(page,pageSize,name,province,address,type);
            count=enterpriseVideoService.countbusinessByvideo(page,pageSize,name,province,address,type);
            model.addAttribute("businessUnitList",businessUnitList);
            model.addAttribute("count",count);
        } catch (ServiceException e) {
            e.printStackTrace();
            model.addAttribute("status",false);
        }
        //List<Enterprise_video> enterprise_videoList=enterpriseVideoService.getVideoByOrgid(page,page_size,orgid);

        return JSON;
    }
    /**
     * 获取拥有视频监控的企业
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getVideoByOrgid/{page}/{pageSize}")
    public View getVideoByOrgid(Model model, @PathVariable(value="page") int page,
                                   @PathVariable(value="pageSize") int pageSize,
                                   @RequestParam(value="id") String id,
                                   HttpServletRequest req, HttpServletResponse resp) {
        Long orgid=Long.valueOf(id);
        List<Enterprise_video> enterpriseVideoList= null;
        try {
            enterpriseVideoList = enterpriseVideoService.getVideoByOrgid(page,pageSize,orgid);
            model.addAttribute("enterpriseVideoList",enterpriseVideoList);
            model.addAttribute("count",enterpriseVideoList.size());
        } catch (ServiceException e) {
            e.printStackTrace();
            model.addAttribute("status",false);
        }
        return JSON;
    }
}
