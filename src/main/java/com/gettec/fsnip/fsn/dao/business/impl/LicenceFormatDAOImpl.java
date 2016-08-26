package com.gettec.fsnip.fsn.dao.business.impl;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.business.LicenceFormatDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.model.business.LicenceFormat;

/**
 * @author HuangYog
 *
 */
@Repository(value="licenceFormatDAO")
public class LicenceFormatDAOImpl  extends BaseDAOImpl<LicenceFormat>
implements LicenceFormatDAO {

}
