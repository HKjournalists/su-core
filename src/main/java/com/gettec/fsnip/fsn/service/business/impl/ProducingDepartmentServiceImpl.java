package com.gettec.fsnip.fsn.service.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.gettec.fsnip.fsn.dao.business.ProducingDepartmentDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.ProducingDepartment;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.service.business.ProducingDepartmentService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;

/**
 * producingDepartmentService service implementation
 * @author xianzhen long
 */
@Service(value="producingDepartmentService")
public class ProducingDepartmentServiceImpl extends BaseServiceImpl<ProducingDepartment, ProducingDepartmentDAO> 
		implements ProducingDepartmentService{
	@Autowired private ProducingDepartmentDAO producingDepartmentDAO;

	/**
	 * 保存生产车间布点信息
	 * @param busunit
	 * @param step
	 * @return void
	 * @throws ServiceException 
	 * @author TangXin
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(BusinessUnit busunit, String step) throws ServiceException {
		try {
			if(busunit==null||step==null){ return; }
			/* 1. 从数据库中删除已经在页面删除的记录  */
			List<ProducingDepartment> nowDepartments = null;
			
			boolean flag=false;  // flag 为true时是生产车间  为false时是挂靠酒厂
			if(step.equals("step1")){
				flag = true;
				nowDepartments = busunit.getProDepartments();
			}else if(step.equals("step3")){
				nowDepartments = busunit.getSubDepartments();
			}else{
				return;
			}
			List<ProducingDepartment> origDepartments = getDAO().getListByBusunitIdAndDepartFlag(busunit.getId(), flag);
			List<ProducingDepartment> removes = getRemoves(origDepartments, nowDepartments);
			if(!CollectionUtils.isEmpty(removes)){
				for(ProducingDepartment department : removes){
					delete(department.getId());
				}
			}
			for(ProducingDepartment department : nowDepartments){
				if(department.getId() == null){
					/* 2. 从数据库中新增在页面新添加的记录  */
					department.setBusinessId(busunit.getId());
					Set<Resource> depAttachments=department.getDepAttachments();
					department.setDepAttachments(null);
					create(department);
					department.setDepAttachments(depAttachments);
				}else{
					/* 3. 从数据库中更新在页面更改的记录  */
					ProducingDepartment orig_department = findById(department.getId());
					orig_department.setName(department.getName());
					orig_department.setLegalName(department.getLegalName());
					orig_department.setAddress(department.getAddress());
					orig_department.setTelephone(department.getTelephone());
					orig_department.setInCommissionNum(department.getInCommissionNum());
					orig_department.setYear(department.getYear());
					Set<Resource> depAttachments=department.getDepAttachments();
					department.setDepAttachments(null);
					update(orig_department);
					department.setDepAttachments(depAttachments);
				}
			}
		} catch (DaoException daoe) {
			throw new ServiceException("ProducingDepartmentServiceImpl.save()-->" + daoe.getMessage(), daoe.getException());
		}
	}
	
	/**
	 * 对比前后的生产车间布点列表，获取删除的集合
	 */
	private List<ProducingDepartment> getRemoves(List<ProducingDepartment> origDepartments, List<ProducingDepartment> nowDepartments) {
		List<ProducingDepartment> removes = new ArrayList<ProducingDepartment>();
		List<Long> currentId = new ArrayList<Long>();
		
		for (ProducingDepartment resource : nowDepartments) {
			Long id = resource.getId();
			if (id != null) {
				currentId.add(id);
			}
		}
		for (ProducingDepartment resource : origDepartments) {
			if (resource.getId()!=null && !currentId.contains(resource.getId())) {
				removes.add(resource);
			}
		}
		return removes;
	}

	@Override
	public ProducingDepartmentDAO getDAO() {
		return producingDepartmentDAO;
	}
}