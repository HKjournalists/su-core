package com.gettec.fsnip.fsn.web.controller.rest;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_SUCCESS;
import static com.gettec.fsnip.fsn.web.IWebUtils.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.market.ResourceType;
import com.gettec.fsnip.fsn.util.RoleEnum;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.UserSafetyVO;
import com.gettec.fsnip.fsn.web.AclUtil;
import com.gettec.fsnip.fsn.web.UIMenuBar;
import com.gettec.fsnip.fsn.web.util.SSOUtil;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;


@Controller
@RequestMapping("/user")
public class UserRESTService {

	/*
	@Autowired
	OrganizationService organizationService;

	@Autowired
	RoleService roleService;*/

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(UserRESTService.class);
	private static final String HAVE_BEEN_SIGNED_OUT ="您已登出或登录超时间";

	/**
	 * Verify user by username and password
	 * 
	 * @param username
	 * @param password
	 * @param model
	 * @param session
	 * @return JSON VIEW
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/login")
	public View login(@RequestParam("user") String username,
			@RequestParam("pwd") String password, Model model,
			HttpSession session) {
		/*User matchUser = userService.verifyUser(username, password);
		if (null != matchUser) {
			// update the session manually in case previously removed
			SimpleHttpSessionListener.createSessionManually(session,
					matchUser.getId());
			// update lastLoginDate
			matchUser.setLastLoginDate(new Date());
			userService.update(matchUser);*/
//			model.addAttribute("url", "/fsn-core/home.html");
//			model.addAttribute("status", SERVER_STATUS_SUCCESS);
		/*} else {
			// remove session is the username/password is incorrect
			SimpleHttpSessionListener.removeSessionManually(session);
			model.addAttribute("status", SERVER_STATUS_FAILED);
			model.addAttribute("error", SYS_ERROR_CODE_USER_NAME_NOT_FOUND);
		}*/
		return JSON;
	}

	/**
	 * @param req
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/logout")
	public View logout(HttpServletRequest req, HttpServletResponse response, Model model) throws IOException {
		String ssoLogout = SSOUtil.getPropety(SSOUtil.LOGOUT);
		String application = SSOUtil.getPropety(SSOUtil.APPLICATION);
		model.addAttribute("status", SERVER_STATUS_SUCCESS);
		model.addAttribute("url", ssoLogout+"?service="+application+"?t="+Math.random());
		return JSON;
	}

	/**
	 * @param req
	 * @param response
	 * @param model
	 * @param locale
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/menubar")
	public View getUserMenubar(HttpServletRequest req,
			HttpServletResponse response, Model model, Locale locale)
			throws IOException {
		JSONArray menuList = (JSONArray) AccessUtils.getMenuList();
		if (null != menuList) {
			try {
				JSONObject menuObj = new JSONObject();
				menuObj.put("menubar", menuList.toString());
				menuObj.put("loginUser", AccessUtils.getUserName());
				response.setContentType("application/json");
				response.setCharacterEncoding("utf-8");
				response.getWriter().println(menuObj);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		return JSON;
	}
	
	/**
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/menubar/{roleId}")
	public View getUserMenubar(HttpSession session, Model model, @PathVariable int roleId) {
		/*Long userId = ValidatorRESTService.innerValidator(session);
		if (null != userId) {*/
			model.addAttribute("status", SERVER_STATUS_SUCCESS);
			List<UIMenuBar> menus = new ArrayList<UIMenuBar>();
			UIMenuBar menu = new UIMenuBar();
			menu.setCssClass("menu_item");
			menu.setText("Business");
			menu.setUrl("#");
			List<UIMenuBar> items = new ArrayList<UIMenuBar>();
			menu.setItems(items);
			UIMenuBar item = new UIMenuBar();
			item.setText("Business Unit");
			item.setUrl("/fsn-core/views/business/management.html");
			items.add(item);
			
			
			/*item = new UIMenuBar();
			item.setText("Business Brand");
			item.setUrl("/fsn-core/views/business/business-brand.html");
			items.add(item);*/
			
			menus.add(menu);
			
			menu = new UIMenuBar();
			menu.setCssClass("menu_item");
			menu.setText("Product");
			menu.setUrl("#");
			items = new ArrayList<UIMenuBar>();
			menu.setItems(items);
			item = new UIMenuBar();
			item.setText("Product");
			item.setUrl("/fsn-core/views/product/management.html");
			items.add(item);
			
			
			/*item = new UIMenuBar();
			item.setText("Business Brand");
			item.setUrl("/fsn-core/views/business/business-brand.html");
			items.add(item);*/
			
			menus.add(menu);
			
			RoleEnum role = RoleEnum.getRole(roleId);
			menus = AclUtil.getMenus(role);
			
			model.addAttribute("menubar", menus/*userService.getUserMenuBar(userId)*/);
			String url = StringUtils.EMPTY;
			if(menus != null && menus.size() > 0){
				if(menus.get(0) != null && menus.get(0).getItems() != null && menus.get(0).getItems().size() > 0){
					url = menus.get(0).getItems().get(0).getUrl();
				}else if(menus.get(0) != null){
					url = menus.get(0).getUrl();
				}
			}
			model.addAttribute("url", url);
		/*} else {
			model.addAttribute("status", SERVER_STATUS_FAILED);
			model.addAttribute("url", LOGIN);
		}*/
		return JSON;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/modifyPassword")
	public View modifyPassword(@RequestBody UserSafetyVO userVO, HttpServletRequest req, Model model) {
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, null);
			if (info != null && info.getUserId().equals(userVO.getId())) {
				JSONObject userJson = (JSONObject) JSONSerializer.toJSON(userVO);
				String response = SSOClientUtil.send(SSOClientUtil.getServiceURLOfCurrentCAS()+"/user/modifyPassword", SSOClientUtil.POST, userJson);
				JSONObject result = (JSONObject) JSONSerializer.toJSON(response);
				model.addAttribute("status", result.get("status"));
				model.addAttribute("message", result.get("message"));
			} else {
				model.addAttribute("status", SERVER_STATUS_FAILED);
				model.addAttribute("message", HAVE_BEEN_SIGNED_OUT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/myId")
	public View getPresentUserId(HttpServletRequest request, Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			model.addAttribute("id", AccessUtils.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/kendoUI/addResources/{flag}") // for kendo ui upload
	public View addTestResources(@RequestParam List<MultipartFile> attachments, @PathVariable String flag,
				 Model model) {
		try{
			Collection<Resource> rs  = getTestResources(attachments);
			String f_type="jpegjpgbmppngpdf";
			if(flag.equals("brand")){
				f_type="jpegjpgbmppng";
			}
			if(flag.equals("report")){
				f_type="pdf";
			}
			if(flag.equals("xls")){
				f_type="xls";
			}
			
			for(Resource ts : rs){
				String rs_type = null;
				int i = ts.getFileName().lastIndexOf('.');
				if (i > 0 &&  i < ts.getFileName().length() - 1) {
					rs_type = ts.getFileName().substring(i+1).toLowerCase();
			    }
				if(f_type.indexOf(rs_type)==-1){
					model.addAttribute("status", SERVER_STATUS_FAILED);
					model.addAttribute("typeEror", "true");
					return JSON;
				}
				if(ts.getFile().length>10485760){
					model.addAttribute("status", SERVER_STATUS_FAILED);
					model.addAttribute("morSize", "true");
					return JSON;
				}
			}
			List<Resource> list  = this.addTestResources(rs);
			if(list.size() != rs.size()){
				throw new Exception("File Type unsupported");
			}
			model.addAttribute("results", list);
			model.addAttribute("result", list);
			model.addAttribute("status", SERVER_STATUS_SUCCESS);
		}catch(Exception e){
			model.addAttribute("status", SERVER_STATUS_FAILED);
			e.printStackTrace();
			return null;
		}
		return JSON;
	}
	
	protected static Collection<Resource> getTestResources(List<MultipartFile> files){
		List<Resource> attachments = new ArrayList<Resource>();
        for(MultipartFile mpf : files ){
        	Resource resource = new Resource();
            try {
            	/*byte[] fileISO8559 = mpf.getOriginalFilename().getBytes("ISO-8859-1");
            	String fileUTF8 = new String(fileISO8559, "utf-8");*/
            	String fileUTF8=mpf.getOriginalFilename();
            	resource.setFileName(fileUTF8);
                resource.setName(fileUTF8);
				resource.setFile(mpf.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
            if(resource.getFile() == null){
            	continue;
            }
            attachments.add(resource);
        }        
        return attachments;
	}

	public List<Resource> addTestResources(Collection<Resource> resources){
		List<Resource> resource = new ArrayList<Resource>();
		if(resources != null){
			for(Resource rs : resources){
				this.updateResource(rs);
				if(rs.getType() == null){
					continue;
				}
				resource.add(rs);
			}
		}
		return resource;
	}
	private void updateResource( Resource rs){
		
			if(rs == null) return;
			String rs_type = null;
			int i = rs.getFileName().lastIndexOf('.');
			if (i > 0 &&  i < rs.getFileName().length() - 1) {
				rs_type = rs.getFileName().substring(i+1).toLowerCase();
		    }
			ResourceType type =new ResourceType();// this.resourceTypeDAO.findByName(FileUtils.getExtension(rs.getFileName()));
			if(rs_type.equals("jpeg")){
				type.setRtId(10L);
				type.setRtName("image/jpeg");
				type.setRtDesc("jpeg");
				type.setContentType("jpeg");
			}
			if(rs_type.equals("jpg")){
				type.setRtId(8L);
				type.setRtName("image/jpg");
				type.setRtDesc("jpg");
				type.setContentType("jpg");
			}
			if(rs_type.equals("bmp")){
				type.setRtId(11L);
				type.setRtName("image/bmp");
				type.setRtDesc("bmp");
				type.setContentType("bmp");
			}
			if(rs_type.equals("png")){
				type.setRtId(12L);
				type.setRtName("image/png");
				type.setRtDesc("png");
				type.setContentType("png");
			}if(rs_type.equals("gif")){
				type.setRtId(1L);
				type.setRtName("image/gif");
				type.setRtDesc("gif");
				type.setContentType("gif");
			}
			if(type != null){
				rs.setType(type);
			}
	}
	@RequestMapping(value = "/kendoUI/removeResources", method = RequestMethod.POST) // for kendo ui upload
    public View removeResourcesKendoUI(@RequestParam String[] fileNames,  Model model) {
		model.addAttribute("result", fileNames);
		model.addAttribute("status", SERVER_STATUS_SUCCESS);
		return JSON;
    }
}
