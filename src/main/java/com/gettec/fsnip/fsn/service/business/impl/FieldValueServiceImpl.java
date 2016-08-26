package com.gettec.fsnip.fsn.service.business.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.gettec.fsnip.fsn.dao.business.FieldValueDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.Field;
import com.gettec.fsnip.fsn.model.business.FieldValue;
import com.gettec.fsnip.fsn.service.business.FieldService;
import com.gettec.fsnip.fsn.service.business.FieldValueService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;

/**
 * @author Hui Zhang
 */
@Service(value="fieldValueService")
public class FieldValueServiceImpl extends BaseServiceImpl<FieldValue, FieldValueDAO> 
		implements FieldValueService{
	@Autowired private FieldValueDAO fieldValueDAO;
	@Autowired private FieldService fieldService;

	/**
	 * 保存生产企业的扩展字段的值
	 * @param fieldValues
	 * @param busunitId
	 * @return void
	 * @throws ServiceException 
	 * @author ZhangHui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(List<FieldValue> fieldValues, long busunitId) throws ServiceException {
		try {
			if(fieldValues==null || fieldValues.size()<1){return;}
			List<FieldValue> shareholders_now = new ArrayList<FieldValue>();  // 现在的股东列表
			List<FieldValue> shareholders_old = getDAO().getListByBusunitIdAndFieldId(32, busunitId);  // 原有的股东列表
			List<FieldValue> adds = new ArrayList<FieldValue>();
			for(FieldValue fieldValue : fieldValues){
				if(fieldValue.getField().getId() != null){
					/* 1. 除了股东信息外，其他扩展信息保存  */
					Field orig_field = fieldService.findByFieldId(fieldValue.getField().getId());
					fieldValue.setField(orig_field);
					List<FieldValue> orig_fieldValues = getDAO().getListByBusunitIdAndFieldId(fieldValue.getField().getId(), busunitId);
					if(orig_fieldValues.size()==0){
						create(fieldValue);
					}else{
						FieldValue orig_fieldValue = orig_fieldValues.get(0);
						orig_fieldValue.setValue(fieldValue.getValue());
						update(orig_fieldValue);
					}
				}else{
					/* 2. 股东信息保存 */
					if(fieldValue.getId()==null){
						adds.add(fieldValue);
					}else{
						shareholders_now.add(fieldValue);
					}
				}
			}
			/* 3. 删除股东  */
			List<FieldValue> removes = getListOfRemoves(shareholders_old,shareholders_now); // 获取被删除的股东列表
			if(!CollectionUtils.isEmpty(removes)){
				for(FieldValue fieldVal : removes){
					FieldValue orgFv = findById(fieldVal.getId());
					delete(orgFv);
				}
			}
			/* 4. 新增股东 */
			if(!CollectionUtils.isEmpty(adds)){
				Field orig_field = fieldService.findByFieldId(32L);
				for(FieldValue fieldVal : adds){
					fieldVal.setField(orig_field);
					create(fieldVal);
				}
			}
			/* 5. 更新现有的股东 */
			if(!CollectionUtils.isEmpty(shareholders_now)){
				for(FieldValue fieldVal : shareholders_now){
					FieldValue orgFv = findById(fieldVal.getId());
					orgFv.setValue(fieldVal.getValue());
					update(orgFv);
				}
			}
		}catch (DaoException dex) {
			throw new ServiceException("fieldValueServiceImpl.save()-->" + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 对比前后的资源列表，获取删除的集合
	 */
	private List<FieldValue> getListOfRemoves(List<FieldValue> origAttachments, List<FieldValue> nowAttachments) {
		List<FieldValue> removes = new ArrayList<FieldValue>();
		List<Long> currentId = new ArrayList<Long>();
		
		for (FieldValue fieldVal : nowAttachments) {
			if(fieldVal.getId()!=null){
				currentId.add(fieldVal.getId());
			}
		}
		for (FieldValue fieldVal : origAttachments) {
			if (!currentId.contains(fieldVal.getId())) {
				removes.add(fieldVal);
			}
		}
		return removes;
	}
	
	/**
	 * 按busunitId查找扩展信息列表。
	 * @throws ServiceException 
	 */
	@Override
	public List<FieldValue> getListByBusunitId(long busunitId) throws ServiceException {
		try {
			return fieldValueDAO.getListByBusunitId(busunitId);
		} catch (DaoException dex) {
			throw new ServiceException("【service-error】按busunitId查找扩展信息列表,出现异常。", dex);
		}
	}

	@Override
	public FieldValueDAO getDAO() {
		return fieldValueDAO;
	}
}