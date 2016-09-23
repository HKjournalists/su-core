package com.gettec.fsnip.fsn.service.sales.impl;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_SALES_SALESCASE_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_PATH;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.sales.BusinessSalesInfoDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.sales.BusinessSalesInfo;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.sales.BusinessSalesInfoService;
import com.gettec.fsnip.fsn.service.sales.RecommendBuyService;
import com.gettec.fsnip.fsn.service.sales.SalesCaseService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.util.sales.SalesUtil;
import com.gettec.fsnip.fsn.vo.business.BusinessSalesVO;
import com.gettec.fsnip.fsn.vo.sales.RecommendBuyVO;
import com.gettec.fsnip.fsn.vo.sales.SalesCaseVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.vo.sales.EnterpriseIntroductionVO;
import com.lhfs.fsn.vo.sales.EnterpriseViewImgVO;

@Service(value="businessSalesInfoService")
public class BusinessSalesInfoServiceImpl extends BaseServiceImpl<BusinessSalesInfo, BusinessSalesInfoDAO> implements BusinessSalesInfoService {
   
    @Autowired private BusinessSalesInfoDAO businessSalesInfoDAO;
    @Autowired private RecommendBuyService recommendBuyService;
    @Autowired private BusinessUnitService businessUnitService;
    @Autowired private SalesCaseService salesCaseService;


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    
    /**
	 * 根据组织企业机构获取企业宣传照和logo图片 (企业门户App接口)
	 * @author tangxin 2015-04-28
	 */
    @Override
    public EnterpriseViewImgVO findEnterpriseViewImgByOrganization(Long organization, boolean description) throws ServiceException{
    	try{
    		return businessSalesInfoDAO.findEnterpriseViewImgByOrganization(organization, description);
    	}catch(DaoException daoe){
    		throw new ServiceException(daoe.getMessage(), daoe);
    	}
    }
    
    /**
	 * 根据企业组织机构获取企业信息 (企业门户App接口)
	 * @author tangxin 2015-04-28
	 */
    @Override
    public EnterpriseIntroductionVO findEnterpriseIntroductionByOrganization(Long organization) throws ServiceException{
    	try{
    		EnterpriseIntroductionVO  enterpriseVO = businessSalesInfoDAO.findEnterpriseIntroductionByOrganization(organization);
    		if(enterpriseVO != null){
    			List<RecommendBuyVO> buyWayList = recommendBuyService.getListByOrganizationWithPage(organization, null, -1, 0);
    			enterpriseVO.setRecommendInfoList(buyWayList);
    		}
    		return enterpriseVO;
    	}catch(DaoException daoe){
    		throw new ServiceException(daoe.getMessage(), daoe);
    	}
    }

    /**
     * 保存销售信息
     * HY
     * @update tangxin 2015-05-18
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void save(BusinessUnit businessUnit, AuthenticateInfo info) throws ServiceException {
        boolean upload_Propaganda = false; //企业宣传照图片上传到ftp成功的标志，true表示上传成功。
        boolean upload_qr = false; //企业宣二维码图片上传到ftp成功的标志，true表示上传成功。
        try {
            // 企业传传照 
        	List<Resource> listPubRes = businessUnit.getPropagandaAttachments();
        	// 企业二维码图片
        	List<Resource> listQRes = businessUnit.getQrAttachments();
        	// 非空处理
            //Resource reProPub = (listPubRes != null && listPubRes.size() > 0 ? listPubRes.get(0) : null);
            
            Resource reQR = (listQRes != null && listQRes.size() > 0 ? listQRes.get(0) : null);
            String name = "";
            String url = "";
            int len = listPubRes.size();
            if(listPubRes != null && len > 0)
            {
            	for(int i=0;i<len;i++)
            	{
            		Resource res = listPubRes.get(i);
            		upload_Propaganda=true;
            		if(res.getFile() != null){
            			upload_Propaganda = savePropagandaResource(res, businessUnit.getName());
            		}
            		name += res.getName();
        			url += res.getUrl();
        			if(i<len-1)
    				{
	    				name += "|";
	    				url += "|";
    				}
            	}
            }
            
            //当企业宣传照资源的 file 不为空时，说明改资源是新增的，需要上传到ftp
            //if(reProPub != null && reProPub.getFile() != null){
            	//upload_Propaganda = savePropagandaResource(reProPub, businessUnit.getName());
            //}

            //当企业宣二维码资源的 file 不为空时，说明改资源是新增的，需要上传到ftp
            if(reQR != null && reQR.getFile() != null){
                upload_qr = savePropagandaResource(reQR, businessUnit.getName());
            }
            //根据企业id查找一条企业宣传信息
            BusinessSalesInfo orig_salesInfo = businessSalesInfoDAO.findByBusId(businessUnit.getId());
            //非空处理
            orig_salesInfo = (orig_salesInfo != null ? orig_salesInfo : new BusinessSalesInfo());
            
            orig_salesInfo.setUpdateTime(new Date());
            orig_salesInfo.setUpdateUser(info.getUserName());
            if(upload_Propaganda && (!name.equals("") && !url.equals(""))){
            	//如果改宣传照片在之前已经有值了，需要添加原来的值
            	/*String tname = orig_salesInfo.getPubPtotosName();
            	String turl = orig_salesInfo.getPubPtotosUrl();
            	if(tname!=null && turl!=null && !tname.equals("")&& !turl.equals(""))
            	{
            		name = name+"|"+tname;
            		url = url+"|"+turl;
            	}*/
                orig_salesInfo.setPubPtotosName(name);
                orig_salesInfo.setPubPtotosUrl(url);
            } else{
            	 orig_salesInfo.setPubPtotosName(null);
                 orig_salesInfo.setPubPtotosUrl(null);
            }
            if(upload_qr){
                orig_salesInfo.setQrcodeImgName(reQR.getName());
                orig_salesInfo.setQrcodeImgUrl(reQR.getUrl());
            } else if(reQR == null){
            	orig_salesInfo.setQrcodeImgName(null);
                orig_salesInfo.setQrcodeImgUrl(null);
            }
            orig_salesInfo.setDelStatus(0);
            if(orig_salesInfo.getId() != null){
                update(orig_salesInfo);
            } else {
                orig_salesInfo.setCreateTime(new Date());
                orig_salesInfo.setCreateUser(info.getUserName());
                orig_salesInfo.setGuid(SalesUtil.createGUID());
                orig_salesInfo.setBusinessId(businessUnit.getId());
                orig_salesInfo.setOrganization(info.getOrganization());
                create(orig_salesInfo);
            }
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(), daoe);
        }
    }

    /**
     * 跟据企业 id找企业销售息息
     * @author HY
     */
    @Override
    public BusinessSalesInfo findByBusId(Long id) throws ServiceException {
        try {
            return businessSalesInfoDAO.findByBusId(id);
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(), daoe);
        }
    }

    /**
     * 加载销售首页的企业信息和销售信息
     */
    @Override
    public BusinessSalesVO findByOrgId(Long organization) throws ServiceException {
        try {
        	/* 加载企业基本信息 */
            BusinessUnit orig_businessUnit = businessUnitService.findByOrganization(organization);
            BusinessSalesVO businessSalesVO = null;
            if (orig_businessUnit != null) {
            	// 封装企业基本信息、企业销售信息、企业推荐购买
                businessSalesVO = getBusinessSalesInfoVO(orig_businessUnit);
                getBusinessSalesCase(businessSalesVO);
            }
            return businessSalesVO;
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(), daoe);
        }
    }

    /**
     * 封装企业基本信息、企业销售信息、企业推荐购买
     * @author HY
     * Create Date 2015-05-05
     */
    private BusinessSalesVO getBusinessSalesInfoVO(BusinessUnit orig_businessUnit) throws DaoException, ServiceException {
        BusinessSalesVO businessSalesVO;/* 加载销售信息 */
        BusinessSalesInfo orig_salesInfo = businessSalesInfoDAO.findByBusId(orig_businessUnit.getId());
                /* 加载企业推荐购买方式 */
        List<RecommendBuyVO> orig_recVOlists = recommendBuyService.getListByOrganizationWithPage(orig_businessUnit.getOrganization(), null, -1, 0);
        businessSalesVO = new BusinessSalesVO();
        businessSalesVO.setBusContact(orig_businessUnit.getContact());
        businessSalesVO.setBusId(orig_businessUnit.getId());
        businessSalesVO.setBusNote(orig_businessUnit.getAbout());
        businessSalesVO.setBusTel(orig_businessUnit.getTelephone());
        businessSalesVO.setBusEmail(orig_businessUnit.getEmail());
        businessSalesVO.setBusAddress(orig_businessUnit.getAddress());
        businessSalesVO.setBusName(orig_businessUnit.getName());
                /* 添加企业二维码 */
        if(orig_salesInfo != null){
            businessSalesVO.setQrCodeUrl(orig_salesInfo.getQrcodeImgUrl());
            businessSalesVO.setPublicityUrl(orig_salesInfo.getPubPtotosUrl());
        }
                /* 添加企业推荐购买方式 */
        if(orig_recVOlists != null && orig_recVOlists.size() > 0){
            businessSalesVO.setRecommendBuyVOs(orig_recVOlists);
        }
        return businessSalesVO;
    }

    /**
     * 封装企业销售案例
     * author HY
     * Create Date 2015-05-05
     */
    private void getBusinessSalesCase(BusinessSalesVO businessSalesVO) throws ServiceException {
        List<SalesCaseVO> salesCaseVOs = salesCaseService.getListEntityByBusId(businessSalesVO.getBusId());
        if(salesCaseVOs != null && salesCaseVOs.size() > 0){
            businessSalesVO.setSalesCaseVOs(salesCaseVOs);
        }
    }

    /**
     * 存存业业传传照片片
     * @author HY
     */
    private boolean savePropagandaResource(Resource resource, String name) throws ServiceException {
        String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_SALES_SALESCASE_PATH);
        String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PATH);
        boolean success_upload = false;
		/* 1.图片已存在 */
        if (resource.getFile() == null && resource.getName() != null && resource.getUrl() != null) {
            success_upload = true;
        }else if (resource.getFile() != null) { /* 3.图片是页面新增图片 */
            String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
            String urlFileName = randomStr + "." + resource.getType().getRtDesc();
            UploadUtil uploadUtil = new UploadUtil();
            success_upload = uploadUtil.uploadFile(resource.getFile(), ftpPath, urlFileName);
            if(success_upload){
            	if(UploadUtil.IsOss()){
            		resource.setUrl(uploadUtil.getOssSignUrl(ftpPath+"/" + urlFileName));
            	}else{
            		resource.setUrl(webUrl + ftpPath.substring(5)+"/" + urlFileName);
            	}
                resource.setFileName(resource.getFileName());
                resource.setName(resource.getFileName());
            }
        }
        return success_upload;
    }

    @Override
    public BusinessSalesInfoDAO getDAO() {
        return businessSalesInfoDAO;
    }
}
