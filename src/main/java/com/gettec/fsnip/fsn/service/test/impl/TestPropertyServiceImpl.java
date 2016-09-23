package com.gettec.fsnip.fsn.service.test.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.gettec.fsnip.fsn.dao.market.impl.MKTempItemDAOImpl;
import com.gettec.fsnip.fsn.dao.test.TestPropertyDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.MkTempReportItem;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.test.TestPropertyService;

/**
 * TestProperty service implementation
 * 
 * @author Ryan Wang
 */
@Service(value="testPropertyService")
public class TestPropertyServiceImpl extends BaseServiceImpl<TestProperty, TestPropertyDAO> 
		implements TestPropertyService{
	@Autowired protected TestPropertyDAO testPropertyDAO;
	@Autowired private MKTempItemDAOImpl tempItemDAO;
	
	@Override
	public TestPropertyDAO getDAO() {
		return testPropertyDAO;
	}
	
	/**
	 * 根据报告id获取当前报告的检测项目总条数
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public Long getCountByReportId(Long reportId) throws ServiceException {
		try {
			return getDAO().countByReportId(reportId);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 根据报告id获取当前报告的检测项目列表（需分页）
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<TestProperty> getListByReportIdWithPage(Long reportId, int page, int pageSize) throws ServiceException {
		try {
			return getDAO().getListByReportIdWithPage(reportId, page, pageSize);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 按报告id查找检测项目列表
	 * @throws ServiceException 
	 */
	@Override
	public List<TestProperty> findByReportId(Long reportId) throws ServiceException{
		try {
			return getDAO().findByReportId(reportId);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleByTRID(Long id) throws ServiceException{
		try {
			List<TestProperty>  testPropertys= getDAO().findByReportId(id);
			if(testPropertys!=null){
				for(TestProperty tp:testPropertys){
					getDAO().remove(tp);
				}
			}
		} catch (JPAException jpae) {
			throw new ServiceException(jpae.getMessage(), jpae.getException());
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}
	
	/**
	 * 保存检测项目
	 * @param origReport 原来的报告
	 * @param nowReport 现在的报告
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(Long report_id, List<TestProperty> properties, boolean isNew) throws ServiceException{
		try {
			if(!isNew){
				getDAO().deleByTestResultId(report_id);	
			}
			create(report_id, properties);
//			if(isNew){
//				create(report_id, properties);
//			}else{
//				update(report_id, properties);
//			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("保存检测项目，出现异常", e);
		}
	}

	/**
	 * 更新报告时，更新报告的检测项目
	 * @throws ServiceException 
	 */
	private void update(Long report_id, List<TestProperty> nowProperties) throws ServiceException {
		try {
			if(report_id == null){
				throw new Exception("参数为空");
			}
			
			List<TestProperty> origProperties = getDAO().findByReportId(report_id);
			Set<TestProperty> removes = getRemoves(origProperties, nowProperties);
			if(!CollectionUtils.isEmpty(removes)){
				for(TestProperty item : removes){
					delete(item.getId());
				}
			}
			for(TestProperty item : nowProperties){
//				TestProperty orig_item = findById(item.getId());
//				if(orig_item == null){
				if(item.getId() == null){
					item.setId(null);
					item.setTestResultId(report_id);
					create(item);
				}else{
					TestProperty orig_item = findById(item.getId());	
					orig_item.setName(item.getName());
					orig_item.setUnit(item.getUnit());
					orig_item.setTechIndicator(item.getTechIndicator());
					orig_item.setResult(item.getResult());
					orig_item.setStandard(item.getStandard());
					orig_item.setAssessment(item.getAssessment());
					update(orig_item);
				}
			}
		} catch (Exception e) {
			throw new ServiceException("TestPropertyService.update()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 新建报告时，给报告新建检测项目
	 * @throws ServiceException 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void create(Long report_id, List<TestProperty> properties) throws ServiceException {
		try {
			if(report_id == null){
				throw new Exception("参数为空");
			}
			
			for(TestProperty item : properties){
				if(item.getId() != null){
					item.setId(null);
				}
				item.setTestResultId(report_id);
				create(item);
			}
		} catch (Exception e) {
			throw new ServiceException("TestPropertyService.create()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 获取删除的检测项目
	 * @param origItems 原来的检测项目列表
	 * @param nowItems 现在的检测项目列表
	 * @return
	 */
	private Set<TestProperty> getRemoves(List<TestProperty> origItems, List<TestProperty> nowItems) {
		Set<TestProperty> removes = new HashSet<TestProperty>();
		List<Long> currentId = new ArrayList<Long>();
		for(TestProperty item : nowItems){
			if(item.getId() != null){
				currentId.add(item.getId());
			}
		}
		if(origItems!=null){
			for(TestProperty item : origItems){
				if(!currentId.contains(item.getId())){
					removes.add(item);
				}
			}
		}
		return removes;
	}
	
	/**
	 * 按检测项目不同的列名查找不同列的集合信息
	 * @throws ServiceException  
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<String> getListOfColumeValue(int columeId,String keyword,int page,int pageSize) throws ServiceException{
		try {
			return getDAO().getListOfColumeValue(columeId,keyword,page,pageSize);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 获取所有报告（不包括被商超退回的报告）
	 * @author ZhangHui 2015/5/7
	 */
	@Override
	public List<TestProperty> findByReportIdWithoutId(long reportId) throws ServiceException {
		try {
			return getDAO().findByReportIdWithoutId(reportId);
		} catch (DaoException e) {
			throw new ServiceException("TestPropertyServiceImpl.findByReportIdWithoutId()-->" + e.getMessage(), e.getException());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteTestProperty(long id) {
		boolean flag = false;
		try {
			TestProperty entity =  testPropertyDAO.findById(id);
            if(entity!=null){
            	testPropertyDAO.remove(entity);
            	flag = true;
            }else{
            	flag = tempItemDAO.deleteById(id);
//            	MkTempReportItem item = tempItemDAO.findById(id);
//            	if(item!=null){
//            		tempItemDAO.remove(item);
//            		flag = true;
//            	}
            }
		} catch (JPAException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveTestProperty(List<TestProperty> items) {
		try {
			for(TestProperty vo : items) {
				if(vo.getId()==null){
					testPropertyDAO.persistent(vo);
				}else{
					TestProperty e = testPropertyDAO.findById(vo.getId());
					e.setName(vo.getName());
					e.setUnit(vo.getUnit());
					e.setTechIndicator(vo.getTechIndicator());
					e.setResult(vo.getResult());
					e.setAssessment(vo.getAssessment());
					e.setStandard(vo.getStandard());
				}
			}
		} catch (JPAException e) {
			e.printStackTrace();
		}
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<TestProperty> getTestPropertyListByProductId(long productId) {
		String sql="select tp.* from test_property tp inner join test_result tr on tr.id=tp.test_result_id inner join product_instance pi on pi.id=tr.sample_id where pi.product_id=?1 and tr.test_type=?2";
		try {
			return testPropertyDAO.getListBySQL(TestProperty.class,sql,new Object[]{productId,"第三方检测"});
		} catch (JPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}