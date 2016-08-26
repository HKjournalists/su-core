package com.gettec.fsnip.fsn.web.util;

import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

public interface IWebUtils {

	MappingJacksonJsonView JSON = new MappingJacksonJsonView();
	String HOME = "home";
	/* Fields */
	String ADD_FIELD = "fields/add";
	String UPDATE_FIELD = "fields/update";
	String MANAGE_FIELD = "fields/management";

	/* Template */
	String ADD_TEMPLATE = "template/add";
	String UPDATE_TEMPLATE = "template/update";
	String MANAGE_TEMPLATE = "template/management";

	String SERVER_STATUS_SUCCESS = "true";
	String SERVER_STATUS_FAILED = "false";

	String SYS_ERROR_NO_PERMISSION = "You have no permission to use the service";
	String SYS_ERROR_NO_ACCESS_PERMISSION = "You have no permission to use the service";

	/* Duplicated name */
	String SYS_ERROR_CODE_USER_NAME_EXISTING = "SYS_ERROR_CODE_USER_NAME_EXISTING";
	String SYS_ERROR_CODE_ORG_NAME_EXISTING = "SYS_ERROR_CODE_ORG_NAME_EXISTING";
	String SYS_ERROR_CODE_ROLE_NAME_EXISTING = "SYS_ERROR_CODE_ROLE_NAME_EXISTING";
	String SYS_ERROR_CODE_PERMISSION_NAME_EXISTING = "SYS_ERROR_CODE_PERMISSION_NAME_EXISTING";

	/* Not existing name */
	String SYS_ERROR_CODE_USER_NAME_NOT_FOUND = "SYS_ERROR_CODE_USER_NAME_NOT_FOUND";
	String SYS_ERROR_CODE_ORG_NAME_NOT_FOUND = "SYS_ERROR_CODE_ORG_NAME_NOT_FOUND";
	String SYS_ERROR_CODE_ROLE_NAME_NOT_FOUND = "SYS_ERROR_CODE_ROLE_NAME_NOT_FOUND";
	String SYS_ERROR_CODE_PERMISSION_NAME_NOT_FOUND = "SYS_ERROR_CODE_PERMISSION_NAME_NOT_FOUND";

	String SYS_ERROR_CODE_FOREIGN_KEY_REFERENCED = "SYS_ERROR_CODE_FOREIGN_KEY_REFERENCED";
	String SYS_ERROR_CODE_PARENT_PERMISSION = "SYS_ERROR_CODE_PARENT_PERMISSION";
}
