package com.gettec.fsnip.fsn.service.sales;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.dao.sales.EmailLogDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sales.EmailLog;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.util.sales.BaseMailDefined;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 邮件发送记录service层
 * @author tangxin 2015/04/24
 *
 */
public interface EmailLogService extends BaseService<EmailLog, EmailLogDAO> {

	Map<String, Long> getAttachmentsId(Long organization, List<Long> listId)
			throws ServiceException;

	void recordMailLog(BaseMailDefined baseMail, AuthenticateInfo info) throws ServiceException;

}
