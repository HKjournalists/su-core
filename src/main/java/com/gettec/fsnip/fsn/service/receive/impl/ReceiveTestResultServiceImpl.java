package com.gettec.fsnip.fsn.service.receive.impl;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.receive.ReceiveTestResultDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.receive.ReceiveTestResult;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.receive.ReceiveSpecimenService;
import com.gettec.fsnip.fsn.service.receive.ReceiveTestPropertyService;
import com.gettec.fsnip.fsn.service.receive.ReceiveTestResultService;
import com.gettec.fsnip.fsn.vo.receive.ResultVO;
import com.lhfs.fsn.util.FtpUtil;

/**
 * 
 * @author ZhangHui 2015/4/24
 */
@Service(value="receiveTestResultService")
public class ReceiveTestResultServiceImpl 
			extends BaseServiceImpl<ReceiveTestResult, ReceiveTestResultDAO>
			implements ReceiveTestResultService{
	@Autowired private ReceiveTestResultDAO receiveTestResultDAO;
	@Autowired private ReceiveSpecimenService receiveSpecimenService;
	@Autowired private ReceiveTestPropertyService receiveTestPropertyService;
	
	private static Logger log = Logger.getLogger(ReceiveTestResultServiceImpl.class.getName());
	
	@Override
	public ReceiveTestResultDAO getDAO() {
		return receiveTestResultDAO;
	}

	/**
	 * 保存样品检测数据
	 * @author ZhangHui 2015/4/24
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(ReceiveTestResult recResult, ResultVO resultVO) throws ServiceException {
		if(recResult == null){
			throw new ServiceException();
		}
		String receive_id = recResult.getReceive_id();
		String edition = recResult.getEdition();
		if(receive_id==null || "".equals(receive_id) || edition==null || "".equals(edition)){
			throw new ServiceException();
		}
		
		try {
			// 判断该报告是否已经存在
			long count = getDAO().countByReceiveIdAndEdition(receive_id, edition);
			if(count > 0){
				resultVO.setSuccess(false);
				resultVO.setMessage("该报告已经存在！（edition: " + edition + ", id: "+ receive_id +"）");
				log.error("该报告已经存在！（edition: " + edition + ", id: "+ receive_id +"）");
				return;
			}
			
			/**
			 * 第一步：新增样品数据
			 */
			recResult.setCreate_time(new Date());
			create(recResult);
			/**
			 * 第二步：新增检测数据
			 */
			receiveSpecimenService.create(recResult.getRecSpecimens(), recResult.getId());
			/**
			 * 第三步：新增检测项目和结论
			 */
			receiveTestPropertyService.create(recResult.getRecTestProperties(), recResult.getId());
			log.info("样品检测数据保存成功！");
		} catch (Exception e) {
			log.error("样品检测数据保存失败！");
			throw new ServiceException("ReceiveTestResultServiceImpl.save()-->" + e.getMessage(), e);
		}
	}

}
