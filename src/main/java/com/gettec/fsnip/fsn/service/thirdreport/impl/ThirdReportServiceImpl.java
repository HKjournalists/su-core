package com.gettec.fsnip.fsn.service.thirdreport.impl;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_PRODUCT_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_PRODUCT_PATH;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.thirdreport.ThirdReportDao;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.thirdReport.Thirdreport;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.thirdreport.ThirdReportService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.vo.thirdreport.ThirdreportVo;

@Service
public class ThirdReportServiceImpl extends BaseServiceImpl<Thirdreport, ThirdReportDao> implements ThirdReportService{
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	@Autowired
	private ThirdReportDao thirdReportDao;
	
	@Override
	public ThirdReportDao getDAO() {
		return thirdReportDao;
	}

	@Override
	public List<ThirdreportVo> getReportNo(long currrntUserId) {
		return thirdReportDao.getReportNo(currrntUserId);
	}

	@Override
	public List<TestResult> getReportDetail(long currrntUserId, String testType) {
		
		return thirdReportDao.getReportDetail(currrntUserId,testType);
	}

	@Override
	public long getReportCount(long currrntUserId, String testType) {
		return thirdReportDao.getReportCount(currrntUserId,testType);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(Thirdreport thirdreport) throws Exception {
		//保存到云端资源图片中
		this.savePhoto(thirdreport.getReportArray(),thirdreport.getServiceOrder());
		this.savePhoto(thirdreport.getCheckArray(),thirdreport.getServiceOrder());
		this.savePhoto(thirdreport.getBuyArray(),thirdreport.getServiceOrder());
		thirdReportDao.persistent(thirdreport);
	}

	private Set<Resource> savePhoto(Set<Resource> resList,String orderNo) throws Exception {
		
		UploadUtil uploadUtil = new UploadUtil();
		String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_PRODUCT_PATH) + "/" + orderNo;
		String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PRODUCT_PATH) + "/" + orderNo;

		for (Resource resource : resList) {
			if (resource.getFile() != null) {
				String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
				String name = orderNo + "-" + randomStr + "." + resource.getType().getRtDesc();
				boolean isSuccess = uploadUtil.uploadFile(resource.getFile(), ftpPath, name);
				if(isSuccess){
					String url;
					if(UploadUtil.IsOss()){
						url=uploadUtil.getOssSignUrl(ftpPath+"/"+name);
					}else{
						url = webUrl + "/" + name;
					}
					resource.setUrl(url);
					resource.setName(name);
				}else{
					throw new Exception("产品图片上传失败");
				}
			}
		}
		return resList;
	}

	@Override
	public List<Long> getReportCounts(long currrntUserId, String testType) {
		
		return thirdReportDao.getReportCounts(currrntUserId, testType);
	}

	@Override
	public List<String> getStandards(long currrntUserId) {
		return thirdReportDao.getStandards(currrntUserId);
	}

	@Override
	public long getStandardCount(long productId, String testType) {
		return thirdReportDao.getStandardCount(productId,testType);
	}
	
	
	
	
}
