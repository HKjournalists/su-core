package com.gettec.fsnip.fsn.service.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.business.LicenceFormatDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.LicenceFormat;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.business.LicenceFormatService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.AddressService;
import com.lhfs.fsn.vo.business.LightBusUnitVO;
/**
 * @author HuangYog
 */
@Service(value="licenceFormatService")
public class LicenceFormatServiceImpl extends BaseServiceImpl<LicenceFormat, LicenceFormatDAO> 
implements LicenceFormatService{
    
    @Autowired 
    private LicenceFormatDAO licenceFormatDAO;
    @Autowired
    private BusinessUnitService businessUnitService;
    @Autowired
    private AddressService addressService;
    
    @Override
    public LicenceFormatDAO getDAO() {
        return licenceFormatDAO;
    }
    
    /**
     * 根据该企业的组织机构找到该企业的地址中的省份，关联的简称
     * 分装到QSFormatVO中
     * @param orgnizationId 组织机构id
     * @return List<LicenceFormat>
     * @author HuangYog
     */
    @Override
    public List<LicenceFormat> loadlistFormatqs(Long orgnizationId)
            throws ServiceException {
        try {
        	/* 1. 获取当前登录用户所属企业的省份简称 */
           // BusinessUnit businessUnit = businessUnitService.findByOrganization(orgnizationId);
            LightBusUnitVO  businessUnit=businessUnitService.findBusVOByOrg(orgnizationId);
            List<LicenceFormat> licenceFormats = licenceFormatDAO.findAll();
            if(businessUnit == null){
            	return licenceFormats;
            }
            String adress = businessUnit!=null?businessUnit.getOtherAddress():null;//将企业保存的地址截取地址中的省份
            String pShort = null;
            String busType = (businessUnit.getType()!=null&&businessUnit.getType().contains("生产企业"))?"生产企业":"流通企业";
            /* 是生产企业才去查询地址的简称 */
            if(busType.equals("生产企业")&&adress!=null&&!adress.equals("")) {
                pShort = addressService.getProJianChengByPro(adress.split("-")[0]);//根据省份名称获取省份简称
            }
            pShort = (pShort!=null&&!pShort.equals(""))?pShort:"?";
            /* 改变特殊的输入规则（带有当前省份的） */
            for(LicenceFormat format:licenceFormats) {
            	/* (?)XK(格式：(?)XKxx-xxx-xxxxx) 将？替换为pShort */
            	if(format.getFormetName().contains("?")){
            		String name = format.getFormetName().replace("?",pShort);
                    String value = format.getFormetValue().replace("?",pShort);
                    format.setFormetName(name);
                    format.setFormetValue(value);
                    break;
            	}
            }
            return licenceFormats.subList(0, 5);
        } catch (JPAException jpae) {
            throw new ServiceException("BusinessUnitDAOImpl.loadlistFormatqs,出现异常！", jpae.getException());
        }
    }
}
