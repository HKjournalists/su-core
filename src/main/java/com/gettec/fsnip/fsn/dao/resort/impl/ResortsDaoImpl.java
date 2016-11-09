package com.gettec.fsnip.fsn.dao.resort.impl;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.resort.ResortsDao;
import com.gettec.fsnip.fsn.model.resort.Resorts;

@Repository(value = "ResortsDao")
public class ResortsDaoImpl extends BaseDAOImpl<Resorts> implements ResortsDao{

}
