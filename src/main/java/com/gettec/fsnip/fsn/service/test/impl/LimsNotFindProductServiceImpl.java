package com.gettec.fsnip.fsn.service.test.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.test.LimsNotFindProductDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.test.LimsNotFindProduct;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.test.LimsNotFindProductService;

/**
 * LimsNotFindProduct service implementation
 * 
 * @author Ryan Wang
 */
@Service(value="limsNotFindProductService")
public class LimsNotFindProductServiceImpl extends BaseServiceImpl<LimsNotFindProduct, LimsNotFindProductDAO> 
		implements LimsNotFindProductService{
	@Autowired protected LimsNotFindProductDAO limsNotFindProductDAO;
	
	@Override
	public LimsNotFindProductDAO getDAO() {
		return limsNotFindProductDAO;
	}

	/**
	 * 保存lims发布的匹配不上产品的报告
	 * @author LongXianZhen
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(String data) {
		try {
			String[] d=data.split("&&&");
			if(d.length==3){
				LimsNotFindProduct li=new LimsNotFindProduct();
				li.setProName(d[0]);
				li.setBarcode(d[1]);
				li.setJsonURL(d[2]);
				limsNotFindProductDAO.persistent(li);
			}
		} catch (JPAException e) {
			((Throwable) e.getException()).printStackTrace();
		}
	}
}