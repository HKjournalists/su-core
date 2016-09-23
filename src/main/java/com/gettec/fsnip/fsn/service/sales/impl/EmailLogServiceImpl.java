package com.gettec.fsnip.fsn.service.sales.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.sales.EmailLogDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sales.EmailLog;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.sales.EmailLogService;
import com.gettec.fsnip.fsn.util.sales.BaseMailDefined;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 邮件发送记录service层
 * @author tangxin 2015/04/24
 *
 */
@Service(value = "emailLogService")
public class EmailLogServiceImpl extends BaseServiceImpl<EmailLog, EmailLogDAO> implements
		EmailLogService {

	@Autowired private EmailLogDAO emailLogDAO;
	@Autowired private BusinessUnitService businessUnitService;

	/**
	 * 根据资源id获取电子资料id
	 * @param listId 资源id集合
	 * @author tangxin 2015-05-11
	 */
	@Override
	public Map<String, Long> getAttachmentsId(Long organization, List<Long> listId) throws ServiceException{
		try{
			return getDAO().getAttachmentsId(organization, listId);
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}
	
	/**
	 * 邮件发送日志记录
	 * @throws ServiceException
	 * @author tangxin 2015-05-0-11
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void recordMailLog(BaseMailDefined baseMail, AuthenticateInfo info) throws ServiceException {
		try{
			if(baseMail == null || info == null || info.getOrganization() != null) {
				return;
			}
			EmailLog log = getModel(baseMail, info);
			if(log != null) {
				create(log);
			}
		}catch(ServiceException sexe){
			throw sexe;
		}
	}
	
	/**
	 * 创建一个 EmailLog 实体
	 * @param info
	 * @return
	 * @author tangxin 2015-05-11
	 */
	private EmailLog getModel(BaseMailDefined baseMail, AuthenticateInfo info){
		if(info == null || baseMail == null){
			return null;
		}
		EmailLog log = new EmailLog();
		Long busId = null, resId = null, dataId = null;
		Map<String, Long> map = null;
		try {
			busId = businessUnitService.findIdByOrg(info.getOrganization());
			/* 根据资源id获取电子资料id */
			map = getAttachmentsId(info.getOrganization(), baseMail.getAttachments());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		String ids = "";
		if(map != null) {
			/* 电子资料对应的资源id */
			resId = map.get("resourceId");
			/* 电子资料id */
			dataId = map.get("dataId");
		}
		if(baseMail.getAttachments() != null){
			for(Long id : baseMail.getAttachments()){
				/* 封装合同id，不报告资料id */
				if(resId != null && id.equals(resId)){
					continue;
				}
				ids += (id + ",");
			}
		}
		ids = (ids.length() > 0 ? ids.substring(0, (ids.length()-1)) : ids);
		log.setBusinessId(busId);
		log.setOrganization(info.getOrganization());
		log.setSendUser(info.getUserName());
		log.setDelStatus(0);
		log.setSendTime(new Date());
		/* 邮件附件名称，当前固定为：Attachments.zip */
		log.setAnnex_name("Attachments.zip");
		log.setAddressee(baseMail.getTo());
		log.setTitile(baseMail.getSubject());
		log.setElectDateId(dataId);
		log.setContartsIds(ids);
		log.setContent(baseMail.getText());
		return log;
	}
	
	@Override
	public EmailLogDAO getDAO() {
		return emailLogDAO;
	}

}
