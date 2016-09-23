package com.gettec.fsnip.fsn.dao.market;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.sys.SystemAttribute;


public interface SystemAttributeDAO extends BaseDAO<SystemAttribute> {

	List<SystemAttribute> getSortedAttrs();
}
