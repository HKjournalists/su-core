package com.gettec.fsnip.fsn.web.controller.rest;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_SUCCESS;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.web.SimpleHttpSessionListener;

/**
 * 
 * @author Yun-Long Xi (Cloud): shallowlong@gmail.com
 */
@Controller
@RequestMapping("/validator")
public class ValidatorRESTService {

	/**
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "")
	public View validator(HttpSession session, Model model) {
		String sessionId = session == null ? "" : session.getId();
		Long userId = SimpleHttpSessionListener.getUserSessionMap().get(
				sessionId);
		if (null == userId) {
			model.addAttribute("status", SERVER_STATUS_FAILED);
			model.addAttribute("url", "/lims-core/login.html");
		} else {
			model.addAttribute("status", SERVER_STATUS_SUCCESS);
		}

		return JSON;
	}

	/**
	 * @param session
	 * @return the user id, null if failed to validate
	 */
	public static Long innerValidator(HttpSession session) {
		String sessionId = session == null ? "" : session.getId();
		Long userId = SimpleHttpSessionListener.getUserSessionMap().get(
				sessionId);
		return userId;
	}
}
