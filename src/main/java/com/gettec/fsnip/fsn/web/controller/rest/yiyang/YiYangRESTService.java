package com.gettec.fsnip.fsn.web.controller.rest.yiyang;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.util.Decript;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.SystemDefaultInterface;
import com.gettec.fsnip.sso.client.util.AccessUtils;

@Controller
@RequestMapping("/yiyang")
public class YiYangRESTService {
	@RequestMapping(method = RequestMethod.GET, value = "/loginInfo")
	public View loginInfo(Model model){
		String key=PropertiesUtil.getProperty(SystemDefaultInterface.YIYANG_KEY);
		String enterpriseName=AccessUtils.getUserOrgName().toString();
		String username=AccessUtils.getUserName().toString();
		String organization=AccessUtils.getUserOrg().toString();
		long time=System.currentTimeMillis()/1000;
		String sign=Decript.SHA1(key+enterpriseName+organization+username+time).toUpperCase();
		model.addAttribute("url",PropertiesUtil.getProperty(SystemDefaultInterface.YIYANG_LOGINURL)+"?"
			+"enterpriseName="+enterpriseName
			+"&organization="+organization
			+"&username="+username
			+"&time="+time
			+"&sign="+sign);
		return JSON;
	}
}
