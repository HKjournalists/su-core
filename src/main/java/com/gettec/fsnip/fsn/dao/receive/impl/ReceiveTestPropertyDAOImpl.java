package com.gettec.fsnip.fsn.dao.receive.impl;

import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.receive.ReceiveTestPropertyDAO;
import com.gettec.fsnip.fsn.model.receive.ReceiveTestProperty;

/**
 * 
 * @author ZhangHui 2015/4/24
 */
@Repository(value="receiveTestPropertyDAO")
public class ReceiveTestPropertyDAOImpl extends BaseDAOImpl<ReceiveTestProperty> 
		implements ReceiveTestPropertyDAO{
	
}
