package com.gettec.fsnip.fsn.service.test.impl;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_GET_USERS_FROM_SSO_URL;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_GET_NHH_FROM_SSO_URL;

import java.util.Date;
import java.util.List;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.test.TestResultDAO;
import com.gettec.fsnip.fsn.dao.test.TestResultHandlerDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.test.TestResultHandler;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.test.TestResultHandlerService;
import com.gettec.fsnip.fsn.util.HttpUtils;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.vo.report.ToBeStructuredReportVO;

/**
 * testResultHandlerService service implementation
 * 
 * @author LongXianZhen
 * 2015-04-28
 */
@Service(value="testResultHandlerService")
public class TestResultHandlerServiceImpl extends BaseServiceImpl<TestResultHandler, TestResultHandlerDAO> 
		implements TestResultHandlerService{
	@Autowired protected TestResultHandlerDAO testResultHandlerDAO;
	@Autowired protected TestResultDAO testResultDAO;
	@Override
	public TestResultHandlerDAO getDAO() {
		return testResultHandlerDAO;
	}

	/**
	 * 根据报告ID新建一条需要格式化的报告，并根据算法自动分配
	 *  @author LongXianZhen 2015-04-28
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void createByReportId(long reportId) throws ServiceException {
		try {
			TestResult tr=testResultDAO.findById(reportId);
			//是否是年货产品,如果是年货产品就发给年货的角色
//			String url=tr.getSample().getProduct().isSpecialProduct()?PropertiesUtil.getProperty(FSN_GET_NHH_FROM_SSO_URL):PropertiesUtil.getProperty(FSN_GET_USERS_FROM_SSO_URL);
			String url=PropertiesUtil.getProperty(FSN_GET_USERS_FROM_SSO_URL);
			String userName=null;
			//掉sso接口获取所有的报告格式化人员
			String fsnResult = HttpUtils.send(url,"GET",null);
            JSONObject returnResult=JSONObject.fromObject(fsnResult);
            if("true".equals(returnResult.getString("status"))){
            	JSONArray users=returnResult.getJSONArray("users");
            	
            	String user_handler="";
//            	 String[] str = {"jgh21","jgh22","jgh23","jgh24","jgh25","jgh26","jgh27","jgh28","jgh29","jgh30","jgh31","jgh32","jgh33","jgh34","jgh35","jgh36","jgh37","jgh38","jgh39","jgh40"};
            	for (int k=0;k<users.size();k++) {
            		userName=users.getJSONObject(k).getString("userName");
//					boolean flag = false;
//            		for (int i = 0; i < str.length; i++) {
//            			if(str[i].equals(userName)){flag = true ;}
//					}
//            		if(flag){continue;};
            		user_handler+="'"+userName+"'";
            		if(k<users.size()-1){
            			user_handler +=",";
            		}
				}
            	 //根据当前结构化人员，查找报告最少的机构化用户，把报告最少的人员分配报告
            	TestResultHandler  handler = testResultHandlerDAO.getMinTestResultHandler(user_handler);
            	if(handler==null){
            		//条形码对用户数量取余来自动分配
            		Random random = new Random();
            		int index=Math.abs(random.nextInt()%users.size());
            		userName=users.getJSONObject(index).getString("userName");
            	}else{
                	userName =handler.getHandler(); 
                }
            }
			TestResultHandler trh=new TestResultHandler();
			trh.setCreationTime(new Date());
			trh.setStatus(1); // 状态：待结构化
			trh.setTestResult(tr);
			trh.setHandler(userName);
			getDAO().persistent(trh);
		} catch (JPAException e) {
			((Throwable) e.getException()).printStackTrace();
			throw new ServiceException("TestResultHandlerServiceImpl.createByReportId()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 更新结构化报告状态
	 * @author ZhangHui 2015/5/6
	 * @throws ServiceException 
	 */
	@Override
	public TestResultHandler findByTestResultIdCanEdit(Long test_result_id) throws ServiceException {
		try {
			return getDAO().findByTestResultIdCanEdit(test_result_id);
		} catch (DaoException e) {
			throw new ServiceException("TestResultHandlerServiceImpl.update()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 根据报告id，查找结构化报告数量
	 * @author ZhangHui 2015/5/6
	 */
	@Override
	public long countByCanEdit(Long test_result_id) throws ServiceException {
		try {
			return getDAO().countByCanEdit(test_result_id);
		} catch (DaoException e) {
			throw new ServiceException("TestResultHandlerServiceImpl.update()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 根据用户名查询已退回的结构化报告数量
	 * @author ZhangHui 2015/5/7
	 */
	@Override
	public long countOfBack(String handler, String configure) {
		return getDAO().countOfBack(handler, configure);
	}

	/**
	 * 根据用户名查询结构化报告数量
	 * @author ZhangHui 2015/5/7
	 */
	@Override
	public long count(String handler, int status, String configure) throws ServiceException {
		return getDAO().count(handler, status, configure);
	}

	/**
	 * 根据用户名查询结构化报告
	 * @author ZhangHui 2015/5/7
	 */
	@Override
	public List<ToBeStructuredReportVO> getStructuredsByPage(String handler,
			int status, int page, int pageSize, String configure) throws ServiceException {
		try {
			return getDAO().getStructuredsByPage(handler, status, page, pageSize, configure);
		} catch (DaoException e) {
			throw new ServiceException("TestResultHandlerServiceImpl.getStructuredsByPage()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 根据用户名查询已退回的结构化报告
	 * @author ZhangHui 2015/5/7
	 */
	@Override
	public List<ToBeStructuredReportVO> getBackStructuredsByPage(String handler,
			int page, int pageSize, String configure) throws ServiceException {
		try {
			return getDAO().getBackStructuredsByPage(handler, page, pageSize, configure);
		} catch (DaoException e) {
			throw new ServiceException("TestResultHandlerServiceImpl.getStructuredsByPage()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 根据用户名查询已结构化报告数量
	 * @author ZhangHui 2015/5/7
	 */
	@Override
	public long countOfStructured(String handler, String configure) {
		return getDAO().countOfStructured(handler, configure);
	}

	/**
	 * 根据用户名查询已结构化报告
	 * @author ZhangHui 2015/5/7
	 */
	@Override
	public List<ToBeStructuredReportVO> getHasStructuredsByPage(
			String handler, int page, int pageSize, String configure) throws ServiceException {
		try {
			return getDAO().getHasStructuredsByPage(handler, page, pageSize, configure);
		} catch (DaoException e) {
			throw new ServiceException("TestResultHandlerServiceImpl.getHasStructuredsByPage()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 根据报告id和状态查询结构化报告数量
	 * @author ZhangHui 2015/5/8
	 */
	@Override
	public boolean isCanViewAllInfo(long test_result_id) {
		return getDAO().isCanViewAllInfo(test_result_id);
	}
	
	/**
	 * 根据产品id查找最近一次已结构化报告id
	 * @author ZhangHui 2015/5/8
	 */
	@Override
	public long getTestResultIdOfHasStructed(long productId) {
		return getDAO().getTestResultIdOfHasStructed(productId);
	}

	/**
	 * 根据报告id和状态查找结构化报告数量
	 * @author ZhangHui 2015/5/8
	 */
	@Override
	public long count(long myReportId, int status) {
		return getDAO().count(myReportId, status);
	}
	

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void dealWithTheOldData() throws ServiceException {
		try {
			List<TestResult> trs=testResultDAO.getListByCondition(" WHERE e.publishFlag=?1 ", new Object[]{'6'});
			//掉sso接口获取所有的报告格式化人员
			String url = PropertiesUtil.getProperty(FSN_GET_USERS_FROM_SSO_URL);
			String fsnResult = HttpUtils.send(url,"GET",null);
            JSONObject returnResult=JSONObject.fromObject(fsnResult);
            JSONArray users= new JSONArray();
            if("true".equals(returnResult.getString("status"))){
            	users=returnResult.getJSONArray("users");
            }
			for(TestResult tr:trs){
	            String barcode=tr.getSample().getProduct().getBarcode();
	            Long barcodeInt=Long.parseLong(barcode);
	            String userName=null;
	            //条形码对用户数量取余来自动分配
    			int index=(int) (barcodeInt%users.size());
            	userName=users.getJSONObject(index).getString("userName");
				TestResultHandler trh=new TestResultHandler();
				trh.setCreationTime(new Date());
				trh.setStatus(1); // 状态：待结构化
				trh.setTestResult(tr);
				trh.setHandler(userName);
				getDAO().persistent(trh);
			}
		} catch (JPAException e) {
			throw new ServiceException("TestResultHandlerServiceImpl.dealWithTheOldData()-->" + e.getMessage(), e.getException());
		}
	}
}