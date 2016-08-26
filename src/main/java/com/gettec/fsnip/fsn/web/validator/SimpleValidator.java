package com.gettec.fsnip.fsn.web.validator;

import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_SUCCESS;
//import static com.gettec.fsnip.fsn.web.IWebUtils.SYS_ERROR_NO_ACCESS_PERMISSION;
import static com.gettec.fsnip.fsn.web.IWebUtils.SYS_ERROR_NO_PERMISSION;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import com.gettec.fsnip.fsn.web.SimpleHttpSessionListener;

/**
 * Simple validator
 * 
 * @author Yun-Long Xi (Cloud): shallowlong@gmail.com
 */
public class SimpleValidator {

	private static final Logger LOG = LoggerFactory
			.getLogger(SimpleValidator.class);

//	private static PermissionService permissionService;
	private static String resourcePattern;
	private static String homePagePattern;
	private static String contextPathPattern;
	private static String htmlPattern = ".*\\.html$";
	private static String redirectLoginUrl = "/login.html?login=required";
	@SuppressWarnings("unused")
	private static String redirectHomeUrl = "/home.html?permission=required";
	private static String servicePattern;

	static {
		// Properties prop = new Properties();
		// @SuppressWarnings("resource")
		// InputStream propStream =
		// Thread.currentThread().getContextClassLoader()
		// .getResourceAsStream("ignore.pattern.properties");
		// try {
		// prop.load(propStream);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// System.out.println(prop.stringPropertyNames());
		// System.out.println(prop.get("resourcePattern"));
	}

	/**
	 * Call to check user login status and url permission status<br/>
	 * if Model is null, HttpServletResponse is required to execute redirect,
	 * else, HttpServletResponse is not need because model will have 'url'
	 * parameter to show the redirect URL <br/>
	 * <code>
	 * 1, if call by filter:<br/>
	 * 	&nbsp;&nbsp;1) check user login<br/>
	 * 	&nbsp;&nbsp;2) check user has permission to a html URL<br/>
	 * 	&nbsp;&nbsp;3) HttpServletResponse.redirect (sometime for say in a page, this will fail to execute<br/>
	 * 2, if call by ajax methods<br/>
	 * 	&nbsp;&nbsp;1) check user login<br/>
	 * 	&nbsp;&nbsp;2) check user has permission to a html URL<br/>
	 * 	&nbsp;&nbsp;3) setup Model (status, error, url)<br/>
	 * </code>
	 * 
	 * @param req
	 * @param resp
	 * @param model
	 * @throws IOException
	 * @return the user id if passed the login and permission check, null if not
	 *         pass both of them
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private static Long validateUser(HttpServletRequest req,
			HttpServletResponse resp, Model model) throws IOException {
		Long userId = null;
		//
		userId = validateUserHasLogin(req, resp, model);
		//
		//validateUserHasPermissionToURL(req, resp, model, userId);
		//
		//validateUserHasPermissionToAPI(req, resp, model, userId);

		return userId;
	}

	/**
	 * Partial method for check the login permission
	 * 
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 * @throws IOException
	 */
	public static Long validateUserHasLogin(HttpServletRequest req,
			HttpServletResponse resp, Model model) throws IOException {
		// 0, check whether the session id is bundled with the user id
		Long userId = SimpleHttpSessionListener.getUserSessionMap().get(
				req.getRequestedSessionId());
		if (null == userId) {
			userId = Long.parseLong(req.getSession(false).getAttribute("SSO_USERID").toString());
			if (null != userId) {
				return userId;
			}
		}
		if (!ignore(req)) {
			// 1, situation is user is not even login
			if (null == userId) {
				LOG.warn(
						"-- validateUserHasLogin(): request session id '{}' has not bounded with validated user yet, the requestURI is '{}', it will now go to the /login.html",
						req.getRequestedSessionId(), req.getRequestURI());
				StringBuilder urlSb = new StringBuilder();
				urlSb.append(req.getContextPath());
				urlSb.append(redirectLoginUrl);
				// if the URI is a target html, then add the redirect info
				if (req.getRequestURI().matches(htmlPattern)) {
					urlSb.append("&redirect=");
					urlSb.append(req.getRequestURI());
				}
				fillModelOrResponse(model, resp, SERVER_STATUS_FAILED,
						SYS_ERROR_NO_PERMISSION, urlSb.toString());
			} else {
				fillModelOrResponse(model, resp, SERVER_STATUS_SUCCESS, null,
						null);
			}
		}
		return userId;
	}

	/*public static AuthenticateInfo validUser(HttpServletRequest req,
			HttpServletResponse resp, Model model) throws IOException {
		Long userid = validateUserHasLogin(req, resp, model);

		if (userid.intValue() > 0) {
			return new AuthenticateInfo(userid);
		}
		return null;
	}*/

	/**
	 * Partial method for check the URL access permission, with .html pattern at
	 * least
	 * 
	 * @param req
	 * @param resp
	 * @param model
	 * @param userId
	 * @throws IOException
	 */
	/*private static void validateUserHasPermissionToURL(HttpServletRequest req,
			HttpServletResponse resp, Model model, Long userId)
			throws IOException {
		if (!ignore(req) && null != permissionService) {
			// 2, situation is user has no permission to visit the requested URI
			if (null != userId && req.getRequestURI().matches(htmlPattern)
					&& !req.getRequestURI().matches(homePagePattern)) {
				if (!permissionService.checkUrlVisitPermission(userId,
						req.getRequestURI())) {
					LOG.warn(
							"-- validateUserHasPermissionToURL(): the user who's id is '{}' has no permission to visit the requestURI is '{}', it will now go to the /home.html",
							userId, req.getRequestURI());
					StringBuilder urlSb = new StringBuilder();
					urlSb.append(req.getContextPath());
					urlSb.append(redirectHomeUrl);
					fillModelOrResponse(model, resp, SERVER_STATUS_FAILED,
							SYS_ERROR_NO_ACCESS_PERMISSION, urlSb.toString());
				} else {
					fillModelOrResponse(model, resp, SERVER_STATUS_SUCCESS,
							null, null);
				}
			}
		}
		if (null == permissionService) { // permissionService is null
			LOG.error("-- validateUserHasPermissionToURL(): PermissionService is null, failed to initialize by the default filter");
			if (null != model) {
				StringBuilder urlSb = new StringBuilder();
				urlSb.append(req.getContextPath());
				urlSb.append(redirectLoginUrl);
				fillModelOrResponse(
						model,
						resp,
						SERVER_STATUS_FAILED,
						"System Error, Please Close Your Browser and Try Again!",
						urlSb.toString());
			} else {
				resp.sendError(500,
						"System Error, Please Close Your Browser and Try Again!");
				resp.getOutputStream().flush();
			}
		}
	}*/

	/**
	 * @param req
	 * @param resp
	 * @param model
	 * @param userId
	 * @throws IOException
	 */
	/*public static void validateUserHasPermissionToAPI(HttpServletRequest req,
			HttpServletResponse resp, Model model, Long userId)
			throws IOException {
		if (!ignore(req) && null != permissionService) {
			if (null != userId && req.getRequestURI().matches(servicePattern)) {
				LOG.debug(
						"-- validateUserHasPermissionToAPI(): validate for the API {}",
						req.getRequestURI());
				if (null != model) {
					fillModelOrResponse(model, null, SERVER_STATUS_FAILED,
							"No Permission", null);
				} else {
					// TODO add API check
					// resp.sendError(401, "No Permission");
					// resp.getOutputStream().flush();
				}
			}
		}
		if (null == permissionService) { // permissionService is null
			LOG.error("-- validateUserHasPermissionToAPI(): PermissionService is null, failed to initialize by the default filter");
			resp.sendError(500,
					"System Error, Please Close Your Browser and Try Again!");
			resp.getOutputStream().flush();
		}
	}*/

	/**
	 * Call to setup for the pattern and permission service
	 * 
	 * @param req
	 * @param sc
	 */
	public static void initOnce(HttpServletRequest req, ServletContext sc) {
		if (null != sc) {
			/*if (null == permissionService) {
				ApplicationContext ac = WebApplicationContextUtils
						.getWebApplicationContext(sc);
				permissionService = (PermissionService) ac
						.getBean("permissionService");
				LOG.info("-- initOnce(): get the permissionService bean from spring context...");
			}*/
		}
		if (null != req) {
			if (null == resourcePattern) {
				resourcePattern = "^" + req.getContextPath() + "/resource/.*";
				LOG.info("-- initOnce(): ignore pattern for resource: '{}'",
						resourcePattern);
			}
			if (null == contextPathPattern) {
				contextPathPattern = req.getContextPath() + "/";
				LOG.info(
						"-- initOnce(): ignore pattern for context path: '{}'",
						contextPathPattern);
			}
			if (null == homePagePattern) {
				homePagePattern = req.getContextPath() + "/home.html";
				LOG.info("-- initOnce(): ignore pattern for home page: '{}'",
						homePagePattern);
			}
			if (null == servicePattern) {
				servicePattern = "^" + req.getContextPath() + "/service/.*";
				LOG.info(
						"-- initOnce(): ignore pattern for service pattern: '{}'",
						servicePattern);
			}
		}
	}

	private static boolean ignore(HttpServletRequest req) {
		initOnce(req, null);
		if (req.getRequestURI().equals(contextPathPattern)
				|| req.getRequestURI().matches(resourcePattern)
				|| req.getRequestURI().matches(".*login.html$")
				|| req.getRequestURI().matches(".*menu.html$")
				|| req.getRequestURI().matches(".*user/login$")
				|| req.getRequestURI().matches(".*user/logout$")) {
			return true; // ignore = true
		}
		return false; // ignore = false
	}

	/**
	 * Fill for the response or the model<br/>
	 * 1, model and response required one of them<br/>
	 * 2, status is required, error and url is optional
	 * 
	 * @param model
	 * @param resp
	 * @param status
	 * @param error
	 * @param url
	 * @throws IOException
	 */
	private static void fillModelOrResponse(Model model,
			HttpServletResponse resp, String status, String error, String url)
			throws IOException {
		if (null != model) { // ajax call
			model.addAttribute("status", status);
			if (null != error) {
				model.addAttribute("error", error);
			}
			if (null != url) {
				model.addAttribute("url", url);
			}
		} else { // filter call
			if (null != url) { // in case the normal URL access
				resp.sendRedirect(url);
				resp.getOutputStream().flush();
			}
		}
	}
}
