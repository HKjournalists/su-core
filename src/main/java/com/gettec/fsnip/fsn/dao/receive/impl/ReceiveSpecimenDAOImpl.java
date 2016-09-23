package com.gettec.fsnip.fsn.dao.receive.impl;

import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.receive.ReceiveSpecimenDAO;
import com.gettec.fsnip.fsn.model.receive.ReceiveSpecimendata;

/**
 * 
 * @author ZhangHui 2015/4/24
 */
@Repository(value="receiveSpecimenDAO")
public class ReceiveSpecimenDAOImpl extends BaseDAOImpl<ReceiveSpecimendata> 
		implements ReceiveSpecimenDAO{
	
}
