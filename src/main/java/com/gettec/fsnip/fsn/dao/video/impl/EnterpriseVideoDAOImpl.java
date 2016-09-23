package com.gettec.fsnip.fsn.dao.video.impl;

import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.video.EnterpriseVideoDAO;
import com.gettec.fsnip.fsn.model.video.Enterprise_video;

/**
 * @author litg
 */
@Repository(value="enterpriseVideoDAO")
public class EnterpriseVideoDAOImpl extends BaseDAOImpl<Enterprise_video>
implements EnterpriseVideoDAO {
	
}