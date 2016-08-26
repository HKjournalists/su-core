package com.gettec.fsnip.fsn.service.trace.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.trace.TraceDataDao;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.trace.TraceData;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.trace.TraceDataService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.SystemDefaultInterface;
import com.gettec.fsnip.fsn.util.UploadUtil;

@Service
public class TraceDataServiceImpl extends
BaseServiceImpl<TraceData, TraceDataDao> implements TraceDataService {
	@Autowired
	private TraceDataDao traceDataDao;
	@Autowired private ResourceService resourceService;

	@Override
	public boolean checkbyproductIDandbatchCode(Long productID, String batchCode) {
		return this.traceDataDao.check(productID, batchCode);
	}

	@Override
	public TraceDataDao getDAO() {
		return traceDataDao;
	}

	@Override
	public TraceData findPagebyproductID(Long productID, int page) {
		// TODO Auto-generated method stub
		return this.traceDataDao.findPagebyproductID(productID, page);

	}

	@Override
	public long count(Long productID) {
		return this.traceDataDao.count(productID);
	}

	@Override
	public TraceData findPagebyproductIDandproductDate(Long productID,
			int page, String productDate) {
		return this.traceDataDao.findPagebyproductIDandproductDate(productID,
				page, productDate);
	}
	/**
	 *生成食安唯一码最后一位校验码
	 * 
	 * @author xuetaoyang 2016/05/17
	 */
	@Override
	public String CheckCode(String fsnCode) {
		int OuShu = 0;
		int JiShu = 0;
		int sum = 0;
		int checkcode=0;
		if (fsnCode != null) {
			int a = 0;
			for (int i = fsnCode.length() - 1; i >= 0;) {
				if (a % 2 == 0) {
					OuShu = OuShu+ Integer.parseInt(String.valueOf(fsnCode.charAt(i)));
				} else {
					JiShu = JiShu+ Integer.parseInt(String.valueOf(fsnCode.charAt(i)));
				}
				a++;
				i--;
			}
			sum = 3 * OuShu + JiShu;
		}
		for (int i = 0; i < 10; i++) {
			if ((sum + i) % 10 == 0) {
				checkcode= i;
			}
		}
		return String.valueOf(checkcode);
	}

	@Override
	public List<TraceData> getbyOrgId(Long org,int page,int pageSize) {
		return this.traceDataDao.getbyOrgId(org,page,pageSize);
	}

	public long countbyOrg(long org){
		return this.traceDataDao.countbyOrg(org);
	}

	public Resource addResource(TraceData traceData){
		if(traceData.getSourceCertify()!=null){
			try {
				resourceService.create(traceData.getSourceCertify());
				UploadUtil uploadUtil=new UploadUtil();
				String fileName=uploadUtil.createFileNameByDate(traceData.getSourceCertify().getFileName());
				uploadUtil.uploadFile(traceData.getSourceCertify().getFile(),PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_REPORT_PATH),fileName);
				if(UploadUtil.IsOss()){
					traceData.getSourceCertify().setUrl(uploadUtil.getOssSignUrl(PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_REPORT_PATH)+"/"+fileName));
				}else{
					traceData.getSourceCertify().setUrl(PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_REPORT_PATH+"/"+fileName));
				}
				return traceData.getSourceCertify();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return traceData.getSourceCertify();
	}
	public Resource addResource1(TraceData traceData){
		if(traceData.getGrowEnvironmentResource()!=null){
			try {
				resourceService.create(traceData.getGrowEnvironmentResource());
				UploadUtil uploadUtil=new UploadUtil();
				String fileName=uploadUtil.createFileNameByDate(traceData.getGrowEnvironmentResource().getFileName());
				uploadUtil.uploadFile(traceData.getGrowEnvironmentResource().getFile(),PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_REPORT_PATH),fileName);
				if(UploadUtil.IsOss()){
					traceData.getGrowEnvironmentResource().setUrl(uploadUtil.getOssSignUrl(PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_REPORT_PATH)+"/"+fileName));
				}else{
					traceData.getGrowEnvironmentResource().setUrl(PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_REPORT_PATH+"/"+fileName));
				}
				return traceData.getGrowEnvironmentResource();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return traceData.getGrowEnvironmentResource();
	}
	public Resource addResource2(TraceData traceData){
		if(traceData.getBuyLink()!=null){
			try {
				resourceService.create(traceData.getBuyLink());
				UploadUtil uploadUtil=new UploadUtil();
				String fileName=uploadUtil.createFileNameByDate(traceData.getBuyLink().getFileName());
				uploadUtil.uploadFile(traceData.getBuyLink().getFile(),PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_REPORT_PATH),fileName);
				if(UploadUtil.IsOss()){
					traceData.getBuyLink().setUrl(uploadUtil.getOssSignUrl(PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_REPORT_PATH)+"/"+fileName));
				}else{
					traceData.getBuyLink().setUrl(PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_REPORT_PATH+"/"+fileName));
				}
				return traceData.getBuyLink();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return traceData.getBuyLink();
	}
	
	public Resource addResource3(TraceData traceData){
		if(traceData.getBusinessPromiseResource()!=null){
			try {
				resourceService.create(traceData.getBusinessPromiseResource());
				UploadUtil uploadUtil=new UploadUtil();
				String fileName=uploadUtil.createFileNameByDate(traceData.getBusinessPromiseResource().getFileName());
				uploadUtil.uploadFile(traceData.getBusinessPromiseResource().getFile(),PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_REPORT_PATH),fileName);
				if(UploadUtil.IsOss()){
					traceData.getBusinessPromiseResource().setUrl(uploadUtil.getOssSignUrl(PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_REPORT_PATH)+"/"+fileName));
				}else{
					traceData.getBusinessPromiseResource().setUrl(PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_REPORT_PATH+"/"+fileName));
				}
				return traceData.getBusinessPromiseResource();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return traceData.getBusinessPromiseResource();
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public TraceData update(TraceData traceData){
		try{
			if(traceData.getId()==null){
				this.addResource(traceData);
				this.addResource1(traceData);
				this.addResource2(traceData);
				this.addResource3(traceData);
			}else{
				if(traceData.getSourceCertify()!=null){
					if(traceData.getSourceCertify().getId()==null){
						try{
							TraceData _traceData=this.findById(traceData.getId());
							if(_traceData.getSourceCertify()!=null){
								this.resourceService.delete(_traceData.getSourceCertify());
							}
						}catch(Exception e){
							e.printStackTrace();
						}
						this.addResource(traceData);
					}
				}else{
					try{
						TraceData _traceData=this.findById(traceData.getId());
						if(_traceData.getSourceCertify()!=null){
							this.resourceService.delete(_traceData.getSourceCertify());
						}
						traceData.setSourceCertify(null);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				if(traceData.getGrowEnvironmentResource()!=null){
					if(traceData.getGrowEnvironmentResource().getId()==null){
						try{
							TraceData _traceData=this.findById(traceData.getId());
							if(_traceData.getGrowEnvironmentResource()!=null){
								this.resourceService.delete(_traceData.getGrowEnvironmentResource());
							}
						}catch(Exception e){
							e.printStackTrace();
						}
						this.addResource1(traceData);
					}
				}else{
					try{
						TraceData _traceData=this.findById(traceData.getId());
						if(_traceData.getGrowEnvironmentResource()!=null){
							this.resourceService.delete(_traceData.getGrowEnvironmentResource());
						}
						traceData.setGrowEnvironmentResource(null);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				if(traceData.getBuyLink()!=null){
					if(traceData.getBuyLink().getId()==null){
						try{
							TraceData _traceData=this.findById(traceData.getId());
							if(_traceData.getBuyLink()!=null){
								this.resourceService.delete(_traceData.getBuyLink());
							}
						}catch(Exception e){
							e.printStackTrace();
						}
						this.addResource2(traceData);
					}
				}else{
					try{
						TraceData _traceData=this.findById(traceData.getId());
						if(_traceData.getBuyLink()!=null){
							this.resourceService.delete(_traceData.getBuyLink());
						}
						traceData.setBuyLink(null);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
				if(traceData.getBusinessPromiseResource()!=null){
					if(traceData.getBusinessPromiseResource().getId()==null){
						try{
							TraceData _traceData=this.findById(traceData.getId());
							if(_traceData.getBusinessPromiseResource()!=null){
								this.resourceService.delete(_traceData.getBusinessPromiseResource());
							}
						}catch(Exception e){
							e.printStackTrace();
						}
						this.addResource3(traceData);
					}
				}else{
					try{
						TraceData _traceData=this.findById(traceData.getId());
						if(_traceData.getBusinessPromiseResource()!=null){
							this.resourceService.delete(_traceData.getBusinessPromiseResource());
						}
						traceData.setBusinessPromiseResource(null);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			JSONArray timeTrace=new JSONArray();
			timeTrace.add(sdf.format(traceData.getSourceDate())+"@原材料入仓时间");
			if(traceData.getWarehouseDate()!=null){
				timeTrace.add(sdf.format(traceData.getWarehouseDate())+"@入库时间");
			}
			timeTrace.add(sdf.format(traceData.getProductDate())+"@生产日期");
			if(traceData.getLeaveDate()!=null){
				timeTrace.add(sdf.format(traceData.getLeaveDate())+"@出厂日期");
			}
			traceData.setTimeTrack(timeTrace.toString());
			this.traceDataDao.merge(traceData);
			return traceData;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
