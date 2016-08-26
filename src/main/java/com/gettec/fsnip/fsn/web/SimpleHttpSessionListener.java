package com.gettec.fsnip.fsn.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The secret here is we are still inside a web application, not a real REST
 * framework, so that we can simply use the SESSION terminology to deal with the
 * security problem to a user.
 * 
 * @author Yun-Long Xi (Cloud): shallowlong@gmail.com
 */
public class SimpleHttpSessionListener implements HttpSessionListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(SimpleHttpSessionListener.class);

	private static int count = 0;
	private static Map<String, HttpSession> sessionMap = new HashMap<String, HttpSession>();
	private static Map<String, Long> userSessionMap = new HashMap<String, Long>();

	/**
	 * @return the count
	 */
	public static final int getCount() {
		return count;
	}

	/**
	 * @return the sessionMap
	 */
	public static Map<String, HttpSession> getSessionMap() {
		return sessionMap;
	}

	/**
	 * @return the userSessionMap
	 */
	public static Map<String, Long> getUserSessionMap() {
		return userSessionMap;
	}

	/**
	 * @param session
	 *            current session
	 * @param userId
	 *            the user id
	 */
	public static void createSessionManually(HttpSession session, Long userId) {
		if (null != session) {
			String sessionId = session.getId();
			if (null == getSessionMap().get(sessionId)) {
				// if removed before, add again when successfully login
				getSessionMap().put(sessionId, session);
			}
			if (null != getUserSessionMap().get(sessionId)) {
				getUserSessionMap().remove(sessionId);
			}
			getUserSessionMap().put(sessionId, userId);
		}
	}

	/**
	 * @param session
	 *            current session
	 */
	public static void removeSessionManually(HttpSession session) {
		if (null != session) {
			String sessionId = session.getId();
			if (null != getSessionMap().get(sessionId)) {
				// if removed before, add again when successfully login
				getSessionMap().remove(sessionId);
			}
			getUserSessionMap().remove(sessionId);
		}
	}

	/**
	 * This method will be called when the client call the LIMS application, one
	 * session will be created and installed inside the session map. If the use
	 * entered the error username/password, this session will be temporarily
	 * removed from the map until correct information provided.
	 */
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		String sessionId = session.getId();
		sessionMap.put(sessionId, session);
		++count;
		LOG.info(
				"-- sessionCreated: New session (Client) entered, sessionId is '{}'",
				sessionId);
		LOG.info(
				"-- sessionCreated: Total {} sessions remained in current container",
				count);
	}

	/**
	 * Session will be automatically removed from the map when timeout.
	 */
	public void sessionDestroyed(HttpSessionEvent se) {
		--count;
		String sessionId = se.getSession().getId();
		sessionMap.remove(sessionId);
		userSessionMap.remove(sessionId);
		LOG.info("-- sessionDestroyed: sessionId '{}' is being destroyed",
				sessionId);
		LOG.info(
				"-- sessionDestroyed: Total {} sessions remained in current container",
				count);
	}

}
