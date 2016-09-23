package com.gettec.fsnip.fsn.service.sales.impl;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_SALES_CONTRACT_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_SALES_SALESCASE_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_PATH;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.tools.ant.types.FileList.FileName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.gettec.fsnip.fsn.dao.sales.SalesResourceDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sales.SalesResource;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.sales.SalesResourceService;
import com.gettec.fsnip.fsn.util.FileUtils;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.util.sales.AnnexDownLoad;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.ContractResVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

@Service(value="salesResourceService")
public class SalesResourceServiceImpl extends BaseServiceImpl<SalesResource,SalesResourceDAO> implements SalesResourceService {
    
    @Autowired private SalesResourceDAO salesResourceDAO;

    /**
	 * 通过全局guid 获取资源列表
	 * @author tangxin 2015-04-30
	 */
    @Override
    public List<SalesResource> getListResourceByGUID(String guid) throws ServiceException{
    	try{
    		return getDAO().getListByGUID(guid);
    	}catch(DaoException daoe){
    		throw new ServiceException(daoe.getMessage(), daoe);
    	}
    }
    
    /**
	 * 根据id删除资源（假删除）
	 * @author tangxin 2015-04-30
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultVO removeResourceById(Long id, AuthenticateInfo info) throws ServiceException{
    	ResultVO resultVO = new ResultVO();
    	try{
    		if(id == null) {
    			resultVO.setStatus("false");
    			resultVO.setErrorMessage("删除的资源时，指定的资源id为空。");
    			return resultVO;
    		}
    		SalesResource orig_res = getDAO().findById(id);
    		if(orig_res != null) {
    			orig_res.setUpdateTime(new Date());
    			orig_res.setUpdateUser(info.getUserName());
    			orig_res.setDelStatus(1);
    			update(orig_res);
    			resultVO.setErrorMessage("删除成功！");
    		} else {
    			resultVO.setStatus("false");
    			resultVO.setErrorMessage("删除的资源时，指定id的资源不存在。");
    			return resultVO;
    		}
    		return resultVO;
    	}catch(Exception e){
    		throw new ServiceException(e.getMessage(), e);
    	}
    }
    
    /**
	 * 根据guid删除资源（假删除）
	 * @author tangxin 2015-04-30
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultVO removeResourceByGUID(String guid, AuthenticateInfo info) throws ServiceException{
    	ResultVO resultVO = new ResultVO();
    	try{
    		if(guid == null || "".equals(guid)) {
    			resultVO.setStatus("false");
    			resultVO.setErrorMessage("被删除的资源id为空。");
    			return resultVO;
    		}
    		List<SalesResource> orig_res_list = getDAO().getListByGUID(guid);
    		if(orig_res_list != null && orig_res_list.size() > 0) {
    			for(SalesResource orig_sres : orig_res_list){
    				orig_sres.setUpdateTime(new Date());
    				orig_sres.setUpdateUser(info.getUserName());
    				orig_sres.setDelStatus(1);
    				orig_sres.setCover(0);
    				orig_sres.setSort(-1);
        			update(orig_sres);
    			}
    		}
    		resultVO.setErrorMessage("删除成功！");
    		return resultVO;
    	}catch(Exception e){
    		throw new ServiceException(e.getMessage(), e);
    	}
    }

	/**
	 * 保存销售案例上传的图片资源
	 * @author HY
	 * Created Date 2015-05-05
	 */
	@Override
	public ResultVO saveSalesCaseResource(List<SalesResource> salesResList, String guid,
										  AuthenticateInfo info) throws ServiceException {
		ResultVO resultVO = new ResultVO();
		if(salesResList == null || salesResList.size() < 1) {
			return resultVO;
		}
		String webPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PATH);
		String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_SALES_SALESCASE_PATH);
		ftpPath +=  "/" + info.getOrganization();
		UploadUtil uploadUtil = new UploadUtil();
		for(SalesResource salesRes : salesResList) {
			if(salesRes.getFile() != null) {
				Long curTime = System.currentTimeMillis();
				String uploadfileName = curTime + "." + salesRes.getType().getRtDesc();
				boolean isSuccess = uploadUtil.uploadFile(salesRes.getFile(), ftpPath, uploadfileName);
				if(!isSuccess) {
					resultVO.setStatus("false");
					resultVO.setErrorMessage("FTP 异常！");
					return resultVO;
				}
				if(UploadUtil.IsOss()){
					salesRes.setUrl(uploadUtil.getOssSignUrl(ftpPath+"/"+uploadfileName));
				}else{
					salesRes.setUrl(webPath + ftpPath.substring(5) + "/" + uploadfileName);
				}
				salesRes.setCreateTime(new Date());
				salesRes.setCreateUser(info.getUserName());
				salesRes.setUpdateTime(new Date());
				salesRes.setUpdateUser(info.getUserName());
				salesRes.setSort(-1);
				salesRes.setDelStatus(0);
				salesRes.setGuid(guid);
				create(salesRes);
			} else if(salesRes.getId() != null && salesRes.isUpdate()) {
				try {
					SalesResource orig_salesRes = getDAO().findById(salesRes.getId());
					orig_salesRes.setDelStatus(salesRes.getDelStatus());
					orig_salesRes.setSort(salesRes.getSort());
					orig_salesRes.setCover(salesRes.getCover());
					salesRes.setUpdateTime(new Date());
					salesRes.setUpdateUser(info.getUserName());
					update(orig_salesRes);
				} catch (JPAException e) {
					resultVO.setStatus("false");
					resultVO.setErrorMessage(e.getMessage());
				}
			}
		}
		return resultVO;
	}

	/**
     * 上传并保存企业合格附件
     * @author tangxin 2015/04/29
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SalesResource saveContractResource(SalesResource salesRes, AuthenticateInfo info) 
    		throws ServiceException{
    	boolean mark = (salesRes.getId() != null && !"".equals(salesRes.getUrl()));
    	/**
    	 * 编辑合同信息时，用户没有重新上传合同附件，更新资源名称后返回
    	 */
    	if(salesRes == null|| salesRes.getFile() == null || mark){
    		try {
				SalesResource orig_res = getDAO().findById(salesRes.getId());
				if(orig_res != null){
					/* 同步更新资源名称 */
					orig_res.setFileName(salesRes.getFileName());
					update(orig_res);
				}
			} catch (JPAException e) {
				e.printStackTrace();
			}
    		return salesRes;
    	}
    	String webPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PATH);
    	String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_SALES_CONTRACT_PATH);
    	ftpPath +=  "/" + info.getOrganization();
    	Long curTime = System.currentTimeMillis();
    	String uploadfileName = curTime + "." + FileUtils.getExtension(salesRes.getFileName());
    	UploadUtil uploadUtil = new UploadUtil();
    	boolean isSuccess = uploadUtil.uploadFile(salesRes.getFile(), ftpPath, uploadfileName);
    	if(!isSuccess) {
    		return null;
    	}
    	if(UploadUtil.IsOss()){
        	salesRes.setUrl(uploadUtil.getOssSignUrl(ftpPath + "/" + uploadfileName));
    	}else{
    		ftpPath = ftpPath.substring(5);
        	salesRes.setUrl(webPath + ftpPath + "/" + uploadfileName);
    	}
    	List<SalesResource> orig_salesRes_list = null;
    	try {
    		orig_salesRes_list = getDAO().getListByGUID(salesRes.getGuid());
		} catch (DaoException e) {
			e.printStackTrace();
		}
    	if(orig_salesRes_list != null && orig_salesRes_list.size() > 0) {
    		SalesResource orig_res = orig_salesRes_list.get(0);
    		orig_res.setFileName(salesRes.getFileName());
    		orig_res.setUrl(salesRes.getUrl());
    		orig_res.setUpdateTime(new Date());
    		orig_res.setUpdateUser(info.getUserName());
    		update(orig_res);
    		salesRes = orig_res;
    	} else{
    		salesRes.setCreateTime(new Date());
    		salesRes.setCreateUser(info.getUserName());
    		salesRes.setUpdateTime(new Date());
    		salesRes.setUpdateUser(info.getUserName());
    		salesRes.setSort(-1);
    		salesRes.setDelStatus(0);
    		create(salesRes);
    	}
    	return salesRes;
    }
    
    /**
     * 上传照片集合，指定ftp路径
     * @author tangxin 2015/04/29
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultVO saveListResourceByFtppath(List<SalesResource> listSalesRes, String guid, AuthenticateInfo info, String ftpPath) 
    		throws ServiceException{
    	ResultVO resultVO = new ResultVO();
    	if(listSalesRes == null || listSalesRes.size() < 1) {
    		return resultVO;
    	}
    	if(ftpPath == null || "".equals(ftpPath)){
    		resultVO.setStatus("false");
    		resultVO.setErrorMessage("FTP 路径为空！");
    		return resultVO;
    	}
    	String webPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PATH);
    	ftpPath +=  "/" + info.getOrganization();
    	UploadUtil uploadUtil = new UploadUtil();
    	for(SalesResource salesRes : listSalesRes) {
    		if(salesRes.getFile() != null) {
    			Long curTime = System.currentTimeMillis();
            	String uploadfileName = curTime + "." + salesRes.getType().getRtDesc();
            	boolean isSuccess = uploadUtil.uploadFile(salesRes.getFile(), ftpPath, uploadfileName);
            	if(!isSuccess) {
            		resultVO.setStatus("false");
            		resultVO.setErrorMessage("FTP 异常！");
            		return resultVO;
            	}
            	salesRes.setFile(null);
            	if(UploadUtil.IsOss()){
            		salesRes.setUrl(uploadUtil.getOssSignUrl(ftpPath+"/"+uploadfileName));
            	}else{
            		salesRes.setUrl(webPath + ftpPath.substring(5) + "/" + uploadfileName);
            	}
            	salesRes.setCreateTime(new Date());
        		salesRes.setCreateUser(info.getUserName());
        		salesRes.setUpdateTime(new Date());
        		salesRes.setUpdateUser(info.getUserName());
        		//salesRes.setSort(-1);
        		salesRes.setDelStatus(0);
        		salesRes.setGuid(guid);
        		create(salesRes);
    		} else if(salesRes.getId() != null && salesRes.isUpdate()) {
    			try {
					SalesResource orig_salesRes = getDAO().findById(salesRes.getId());
					orig_salesRes.setDelStatus(salesRes.getDelStatus());
					orig_salesRes.setSort(salesRes.getSort());
					orig_salesRes.setCover(salesRes.getCover());
					salesRes.setUpdateTime(new Date());
	        		salesRes.setUpdateUser(info.getUserName());
	        		update(orig_salesRes);
				} catch (JPAException e) {
					resultVO.setStatus("false");
            		resultVO.setErrorMessage(e.getMessage());
				}
    		}
    	}
    	return resultVO;
    }

	/**
	 * 根据企业获取所有的电子合同和电子资料
	 * @author HY 2015-04-30
	 */
	@Override
	public Model getListContractsAndElectMater(AuthenticateInfo info, Model model) throws ServiceException {
		try {
			/* 获取电子合同资源id和合同名称 */
			List<ContractResVO> contResIdLists = salesResourceDAO.getContResIdList(info.getOrganization());
			/* 获取电子资料名称和资源id */
			ContractResVO contractResVO = salesResourceDAO.getElectDataAndRes(info.getOrganization());
			model.addAttribute("contracts",contResIdLists);
			model.addAttribute("electData",contractResVO);
			return model;
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(), daoe.getException());
		}
	}
	/**
	 * 打包下载用户勾选的电子合同和电子资料
	 * @param resId 用户勾选的内容
	 * @author HY 2015-04-30
	 */
	@Override
	public File downLoadElectData(String resId, String enterName) throws ServiceException {
		try {
			/*1 根据资源id获取资源 */
			List<SalesResource> resLists = salesResourceDAO.getListByResIds(resId);
			File file = null;
			if(resLists != null && resLists.size() > 0){
				/*2 将资源打成zip包 */
				file = AnnexDownLoad.downLoadFiles(resLists, enterName);
			}
			return file;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	/**
	 * 批量更新排序资源
	 * @author tangxin 2015-05-10
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultVO updateSortResource(List<SalesResource> listResource) throws ServiceException{
		try{
			ResultVO resultVO = new ResultVO();
			if(listResource == null || listResource.size() < 1){
				return resultVO;
			}
			for(SalesResource srs : listResource){
				if(srs == null) continue;
				SalesResource origRes = findById(srs.getId());
				if(origRes == null) continue;
				origRes.setSort(srs.getSort());
				update(origRes);
			}
			return resultVO;
		}catch(Exception e){
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	@Override
    public SalesResourceDAO getDAO() {
        return salesResourceDAO;
    }
}
