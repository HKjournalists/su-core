package com.gettec.fsnip.fsn.dao.trace.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.trace.TraceDataProductNameDao;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.trace.TraceData;
import com.gettec.fsnip.fsn.model.trace.TraceDataProductName;
@Repository
public class TraceDataProductNameDaoImpl extends BaseDAOImpl<TraceDataProductName> implements
		TraceDataProductNameDao {
	public TraceDataProductName findByProductName(String productName){
		try {
			List<TraceDataProductName> TraceDataProductNameList=this.getListByCondition("where e.productName=?1",new Object[]{productName});
			return TraceDataProductNameList.get(0);
		} catch (JPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


}
