package com.gettec.fsnip.fsn.service.ledger.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.ledger.LedgerPrepackNoDao;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.ledger.LedgerPrepackNo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.ledger.LedgerPrepackNoService;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.SystemDefaultInterface;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.vo.ledger.LedgerPrepackNoVO;

@Service(value = "ledgerPrepackNoService")
public class LedgerPrepackNoServiceImpl extends BaseServiceImpl<LedgerPrepackNo, LedgerPrepackNoDao>
implements LedgerPrepackNoService{
	

	@Autowired 
	private LedgerPrepackNoDao ledgerPrepackNoDao;
	@Autowired private ResourceService  resourceService;
	@Override
	public List<LedgerPrepackNoVO> loadLedgerPrepackNo(int page, int pageSize,
			String productName, String companyName, String companyPhone,long qiyeId)
			throws DaoException {
		return getDAO().loadLedgerPrepackNo(page, pageSize, productName, companyName, companyPhone,qiyeId);
	}

	@Override
	public long getLedgerPrepackNoTotal(String productName, String companyName,
			String companyPhone,long qiyeId) throws DaoException {
		return getDAO().getLedgerPrepackNoTotal(productName, companyName, companyPhone,qiyeId);
	}

	@Override
	public LedgerPrepackNoDao getDAO() {
		return ledgerPrepackNoDao;
	}

	@Override
	public List<LedgerPrepackNoVO> getListLedgerPrepackNo(String orgId,String date)throws DaoException {
		return getDAO().getListLedgerPrepackNo(orgId, date);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveledgerPrepackNo(
			TreeMap<String, TreeMap<String, TreeMap<String, Object>>> sheetMap,
			Long fromBusId) {
		
		List<LedgerPrepackNo> lefgerList = new ArrayList<LedgerPrepackNo>();
		 Iterator<String> sheetIt = sheetMap.keySet().iterator();
  	     while (sheetIt.hasNext()) {
  		   String key = sheetIt.next();
  		   TreeMap <String,TreeMap <String,Object>>  rowMap = sheetMap.get(key);
  		   Iterator<String> rowIt = rowMap.keySet().iterator();
	  	   if(rowMap.size()==0){
	  			  break;
	  	   }
  		   while (rowIt.hasNext()) {
  			   String k = rowIt.next();
  			   TreeMap <String,Object> colMap = rowMap.get(k);
  			   Iterator<String> colIt = colMap.keySet().iterator();
  			   LedgerPrepackNo vo = null;
  			   vo = new LedgerPrepackNo();
  			   vo.setQiyeId(fromBusId);
  			   while (colIt.hasNext()) {
  				String _k = colIt.next(); 
  				if("产品名称".equals(_k)){
  					vo.setProductName((String)colMap.get(_k));	
  				}else if("别名".equals(_k)){
  					vo.setAlias((String)colMap.get(_k));
  				}else if("规格".equals(_k)){
  					vo.setStandard((String)colMap.get(_k));
  				}else if("采购时间".equals(_k)){
  					vo.setPurchaseTime((Date)colMap.get(_k));
  				}else if("数量".equals(_k)){
  					vo.setNumber((String)colMap.get(_k));
  				}else if("供货商名称".equals(_k)){
  					vo.setCompanyName((String)colMap.get(_k));
  				}else if("供货商电话".equals(_k)){
  					vo.setCompanyPhone((String)colMap.get(_k));
  				}else if("供货商联系人".equals(_k)){
  					vo.setSupplier((String)colMap.get(_k));
  				}else if("供货商地址".equals(_k)){
  					vo.setCompanyAddress((String)colMap.get(_k));
  				}
			}
  			lefgerList.add(vo);
  	   }
	 }
    try {
		for (LedgerPrepackNo entity : lefgerList) {
			ledgerPrepackNoDao.persistent(entity);
		}
	} catch (JPAException e) {
		e.printStackTrace();
	} 
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void setResourceData(LedgerPrepackNo vo) {
					try {
						//保存营业执照,生产许可,流通许可
						UploadUtil uploadUtil = new UploadUtil();
						String ftpPath = PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_PRODUCT_PATH) + "/" + vo.getQiyeId();
						String webUrl = PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_PRODUCT_PATH) + "/" + vo.getQiyeId();
						LedgerPrepackNo entity = null;
						if(vo.getId()!=null&&vo.getId()>0){
							entity = ledgerPrepackNoDao.findById(vo.getId());
						} 
						if(vo.getLicResource()!=null){
							String fileLicName=uploadUtil.createFileNameByDate(vo.getLicResource().getFileName());
							boolean isLicSuccess = uploadUtil.uploadFile(vo.getLicResource().getFile(), ftpPath, fileLicName);
							if(isLicSuccess){
								vo.getLicResource().setUrl(uploadUtil.getOssSignUrl(ftpPath+"/"+fileLicName));
							}else if(vo.getLicResource().getId()==null){
								vo.getLicResource().setUrl(webUrl + "/" + fileLicName);
							}
							vo.getLicResource().setName(fileLicName);
						}else{
							if(entity != null&&entity.getLicResource()!=null&&entity.getLicResource().getId()!=-1){
								ledgerPrepackNoDao.deleteResource(entity.getLicResource().getId());
							}
						}
						if(vo.getDisResource()!=null){
							String fileDisName=uploadUtil.createFileNameByDate(vo.getDisResource().getFileName());
							boolean isDisSuccess = uploadUtil.uploadFile(vo.getDisResource().getFile(), ftpPath, fileDisName);
							if(isDisSuccess){
								vo.getDisResource().setUrl(uploadUtil.getOssSignUrl(ftpPath+"/"+fileDisName));
							}else  if(vo.getDisResource().getId()==null){
								vo.getDisResource().setUrl(webUrl + "/" + fileDisName);
							}
							vo.getDisResource().setName(fileDisName);
						}else{
						    if(entity != null&&entity.getDisResource()!=null&&entity.getDisResource().getId()!=-1){
						    	ledgerPrepackNoDao.deleteResource(entity.getDisResource().getId());
							}
						}
					} catch (JPAException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
	}
}
