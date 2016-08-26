package com.gettec.fsnip.fsn.dao.sales;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.sales.EmailLog;

/**
 * 邮件发送日志DAO
 * @author tangxin 2015/04/24
 *
 */
public interface EmailLogDAO extends BaseDAO<EmailLog> {

	Map<String, Long> getAttachmentsId(Long organization, List<Long> listId)
			throws DaoException;

}
