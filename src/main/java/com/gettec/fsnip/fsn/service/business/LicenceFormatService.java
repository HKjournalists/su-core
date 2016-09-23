package com.gettec.fsnip.fsn.service.business;

import java.util.List;

import com.gettec.fsnip.fsn.dao.business.LicenceFormatDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.LicenceFormat;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface LicenceFormatService extends BaseService<LicenceFormat, LicenceFormatDAO>{

    /**
     * 根据该企业的组织机构找到该企业的地址中的省份，关联的简称
     * 分装到QSFormatVO中
     * @param orgnizationId 组织机构id
     * @return List<LicenceFormat>
     * @throws ServiceException
     * @author HuangYog
     */
    List<LicenceFormat> loadlistFormatqs(Long orgnizationId)throws ServiceException;

}
