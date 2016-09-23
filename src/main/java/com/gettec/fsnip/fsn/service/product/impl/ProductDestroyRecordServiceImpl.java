package com.gettec.fsnip.fsn.service.product.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.product.ProductDestroyRecordDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.ProductDestroyRecord;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.product.ProductDestroyRecordService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.SystemDefaultInterface;
import com.gettec.fsnip.fsn.util.UploadUtil;
@Service
public class ProductDestroyRecordServiceImpl extends BaseServiceImpl<ProductDestroyRecord, ProductDestroyRecordDAO>
		implements ProductDestroyRecordService {
	@Autowired
	private ProductDestroyRecordDAO productDestroyRecordDAO;
	@Autowired private ResourceService resourceService;
	@Autowired protected ResourceService testResourceService;
	@Override
	public ProductDestroyRecordDAO getDAO() {
		// TODO Auto-generated method stub
		return productDestroyRecordDAO;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ProductDestroyRecord update(ProductDestroyRecord productDestroyRecord){
				
					try {
						try {
					//		Set<Resource> r=productDestroyRecord.getRecAttachments();
							productDestroyRecord.addrecResources(testResourceService.saverecResource(productDestroyRecord.getRecAttachments(), productDestroyRecord));
						} catch (ServiceException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(productDestroyRecord.getId()!=null){
							this.productDestroyRecordDAO.merge(productDestroyRecord);
						}else{
							this.productDestroyRecordDAO.persistent(productDestroyRecord);
						}
						
					} catch (JPAException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			return productDestroyRecord;
		
	}
	@Override
	public List<ProductDestroyRecord> getbyOrgId(String orgname, int page,
			int pageSize,String keyword) {
		// TODO Auto-generated method stub
		return productDestroyRecordDAO.getbyOrgId(orgname, page, pageSize,keyword);
	}

	@Override
	public long countbyOrg(String orgname,String keyword) {
		// TODO Auto-generated method stub
		return productDestroyRecordDAO.countbyOrg(orgname,keyword);
	}
}
