package com.gettec.fsnip.fsn.service.sales.impl;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_ENTERPRISE_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_PATH;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.sales.ElectronicDataDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sales.ElectronicData;
import com.gettec.fsnip.fsn.model.sales.SalesResource;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.ResourceTypeService;
import com.gettec.fsnip.fsn.service.sales.BusinessSalesInfoService;
import com.gettec.fsnip.fsn.service.sales.ElectronicDataService;
import com.gettec.fsnip.fsn.service.sales.EmailLogService;
import com.gettec.fsnip.fsn.service.sales.SalesBranchService;
import com.gettec.fsnip.fsn.service.sales.SalesDataSortService;
import com.gettec.fsnip.fsn.service.sales.SalesResourceService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.util.sales.AnnexDownLoad;
import com.gettec.fsnip.fsn.util.sales.BaseMailDefined;
import com.gettec.fsnip.fsn.util.sales.SalesUtil;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.DataSortVO;
import com.gettec.fsnip.fsn.vo.sales.MaterialsVO;
import com.gettec.fsnip.fsn.vo.sales.SalesBranchVO;
import com.gettec.fsnip.fsn.vo.sales.SortFieldValueVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.vo.sales.EnterpriseViewImgVO;

/**
 * 电子资料service层
 * @author tangxin 2015/04/24
 *
 */
@Service(value = "electronicDataService")
public class ElectronicDataServiceImpl extends
		BaseServiceImpl<ElectronicData, ElectronicDataDAO> implements
		ElectronicDataService {
	
	@Autowired private ElectronicDataDAO electronicDataDAO;
	@Autowired private SalesResourceService salesResourceService;
	@Autowired private JavaMailSender javaMailSender;
	@Autowired private EmailLogService emailLogService;
	@Autowired private ServletContext servletContext;
	@Autowired private BusinessSalesInfoService businessSalesInfoService;
	@Autowired private SalesBranchService salesBranchService;
	@Autowired private SalesDataSortService salesDataSortService;
	@Autowired private ResourceTypeService resourceTypeService;
	@Autowired private BusinessUnitService businessUnitService;

	/**
	 * 邮件发送
	 * @author tangxin 2015-05-10
	 */
	@Override
	public ResultVO sendMail(BaseMailDefined baseMail, AuthenticateInfo info) throws ServiceException{
		try{
			ResultVO resultVO = new ResultVO();
			if(baseMail == null){
				resultVO.setStatus("false");
				resultVO.setErrorMessage("无效的邮件信息，无法发送！");
				return resultVO;
			}
			List<Long> attachments = baseMail.getAttachments();
			String idStr = "";
			for(Long resId : attachments){
				idStr += resId.toString() + ",";
			}
			File file = null;
			String enterName = businessUnitService.findNameByOrganization(info.getOrganization());
			if(!"".equals(idStr)){
				file = salesResourceService.downLoadElectData(idStr,enterName);
			}
			final String enterNameFinal = enterName;
			final BaseMailDefined bMail = baseMail;
			final File ffile = file;//new File("E:/150420-1.zip");
			//将用户提交来的收信人统一分割（,  ;  包括中文的，英文的，全角的，半角的）；
			String emailTo =  bMail.getTo().replace("；",";").replace(",",";").replace("，",";").replace("；",";").replace("，",";");
			bMail.setTo(emailTo);
			final String []tos = emailTo.split(";");//收件人的数组
			/* 发送邮件 */
			final MimeMessagePreparator preparator = new MimeMessagePreparator() {
		           public void prepare(MimeMessage mimeMessage) throws Exception {
		               MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true,"UTF-8");
		               message.setTo(tos);
		               message.setText(bMail.getText());
		               
		               
		               String mailenterName="";  
		               try {  
		            	   mailenterName=javax.mail.internet.MimeUtility.encodeText(enterNameFinal);  
		               } catch (UnsupportedEncodingException e) {  
		                   e.printStackTrace();  
		               }   
		               message.setFrom(new InternetAddress(mailenterName+" <10000@fsnip.com>"));  
		               
		              // message.setFrom("10000@fsnip.com");
		               message.setSubject(bMail.getSubject());
		               message.setSentDate(new Date());
		               /* 附件不为空  */
		               if(ffile != null){
			               FileSystemResource fsr = new FileSystemResource(ffile);
			               message.addAttachment(MimeUtility.encodeWord(ffile.getName()), fsr);
		               }
		           }
		       };
		       try{
		        Thread thread = new Thread(){//异步发送邮件
		    		public void run(){
		    			javaMailSender.send(preparator);
		    			if(ffile != null) {
			            	  AnnexDownLoad.delFolder(ffile.getParentFile().getPath());
		    			}
		    		}
		    	};
		    	thread.start();
		    	emailLogService.recordMailLog(baseMail, info);
		       }catch(MailException e) {  
		           e.printStackTrace();  
		       }
		    return resultVO;
		}catch(Exception e){
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	/**
	 * 获取指定组织机构下的电子资料信息
	 * @author tangxin 2015-05-10
	 */
	@Override
	public List<MaterialsVO> getListMaterials(Long organization) throws ServiceException{
		try{
			return getDAO().getListMaterials(organization);
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}
	
	private String formatString(String value){
		return value == null ? "" : value.replace("&", "﹠");
	} 
	
	/**
	 * 获取企业认证信息或企业宣传信息的Items
	 * @author tangxin 2015-05-16
	 */
	private String getItemStr(List<SortFieldValueVO> listSort, int sort){
		if(listSort == null){
			return null;
		}
		StringBuilder items = new StringBuilder();
		int index = 1;
		if(sort > 0){
			for(int i=0;i<listSort.size();i++){
				if(listSort.get(i).getSort() > 0){
					items.append("<名称"+index+">"+formatString(listSort.get(i).getName())+"</名称"+index+">");
					items.append("<图片"+index+">"+formatString(listSort.get(i).getUrl())+"</图片"+index+">");
					index += 1;
				}
			}
		}else{
			for(int i=0;i<listSort.size();i++){
				if(i < 8){
					items.append("<名称"+(i+1)+">"+formatString(listSort.get(i).getName())+"</名称"+(i+1)+">");
					items.append("<图片"+(i+1)+">"+formatString(listSort.get(i).getUrl())+"</图片"+(i+1)+">");
				}
			}
		}
		return items.toString();
	}
	
	/**
	 * 创建产品的Items
	 * @author tangxin 2015-05-16
	 */
	private String createProductItems(List<SortFieldValueVO> listSort, int sort){
		if(listSort == null){
			return null;
		}
		StringBuilder items = new StringBuilder();
		if(sort > 0){
			for(int i=0;i<listSort.size();i++){
				SortFieldValueVO vo = listSort.get(i);
				if(vo.getSort() > 0){
					String url="http://fsnrec.com:8080/portal/img/product/temp/temp.jpg";
					if(vo.getUrl()!=null&&!"".equals(vo.getUrl())){
						url=vo.getUrl();
					}
					items.append("<项目><图片>"+formatString(url)+"</图片>");
					items.append("<产品名称>"+formatString(vo.getName())+"</产品名称>");
					items.append("<规格>"+formatString(vo.getFormat())+"</规格>");
					items.append("<产品介绍>"+formatString(vo.getDesc())+"</产品介绍></项目>");
				}
			}
		}else{
			for(int i=0;i<listSort.size();i++){
				if(i < 8){
					SortFieldValueVO vo = listSort.get(i);
					String url="http://fsnrec.com:8080/portal/img/product/temp/temp.jpg";
					if(vo.getUrl()!=null&&!"".equals(vo.getUrl())){
						url=vo.getUrl();
					}
					items.append("<项目><图片>"+formatString(url)+"</图片>");
					items.append("<产品名称>"+formatString(vo.getName())+"</产品名称>");
					items.append("<规格>"+formatString(vo.getFormat())+"</规格>");
					items.append("<产品介绍>"+formatString(vo.getDesc())+"</产品介绍></项目>");
				}
			}
		}
		return items.toString();
	}
	
	/**
	 * 创建销售案例的Items
	 * @author tangxin 2015-05-16
	 */
	private String createSalesCaseItems(List<SortFieldValueVO> listSort, int sort){
		if(listSort == null || listSort.size() < 1){
			return null;
		}
		StringBuilder items = new StringBuilder();
		if(sort > 0){
			for(int i=0;i<listSort.size();i++){
				SortFieldValueVO vo = listSort.get(i);
				if(vo.getSort() > 0){
					items.append("<项目><图片>"+formatString(vo.getUrl())+"</图片>");
					items.append("<案例名称>"+formatString(vo.getName())+"</案例名称>");
					items.append("<案例介绍>"+formatString(vo.getDesc())+"</案例介绍></项目>");
				}
			}
		}else{
			for(int i=0;i<listSort.size();i++){
				if(i < 8){
					SortFieldValueVO vo = listSort.get(i);
					items.append("<项目><图片>"+formatString(vo.getUrl())+"</图片>");
					items.append("<案例名称>"+formatString(vo.getName())+"</案例名称>");
					items.append("<案例介绍>"+formatString(vo.getDesc())+"</案例介绍></项目>");
				}
			}
		}
		return items.toString();
	}
	
	
	public String getXmlData(DataSortVO dataSortVO, EnterpriseViewImgVO enterprise, AuthenticateInfo info)
	{
		if(dataSortVO == null || info == null)return null;
		StringBuilder busUnitStr=new StringBuilder();
		busUnitStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		busUnitStr.append("<企业电子资料>");
		
		String url = enterprise.getEnterpriseImgUrl();
		if(url==null || url.equals(""))
		    url = "http://fsnrec.com:8080/portal/img/FSC/BUS_XC.png";
		String urls[] = url.split("\\|");
		url = urls[0];
		
		/* 企业基本信息 */
		if(enterprise != null){
			busUnitStr.append("<企业情况介绍><表头>");
			busUnitStr.append("<企业名称>" + formatString(enterprise.getEnterpriseName()) + "</企业名称>");
			busUnitStr.append("<企业名称背景>http://fsnrec.com:8080/portal/img/FSC/TITLE_BK.png</企业名称背景>");
			busUnitStr.append("<企业宣传照>" + formatString(url) + "</企业宣传照>");
			busUnitStr.append("<企业LOGO>" + formatString(enterprise.getLogo()) + "</企业LOGO>");
			busUnitStr.append("<企业简介>" + formatString(enterprise.getDescription()) + "</企业简介>");
			busUnitStr.append("</表头></企业情况介绍>");
		}
		/* 企业认证信息 */
		List<SortFieldValueVO> listSortCertVO = dataSortVO.getListSortCertVO();
		if(listSortCertVO != null){
			busUnitStr.append("<企业认证信息><表头>");
			busUnitStr.append("<企业资质>企业资质</企业资质>");
			busUnitStr.append("<企业资质背景>http://fsnrec.com:8080/portal/img/FSC/TITLE_BK.png</企业资质背景>");
			busUnitStr.append("<企业认证>企业认证</企业认证>");
			busUnitStr.append("<企业认证背景>http://fsnrec.com:8080/portal/img/FSC/BUS_CERT_PUB.png</企业认证背景>");
			busUnitStr.append("</表头><表体>");
			int sort = listSortCertVO.get(0).getSort();
			String items = getItemStr(listSortCertVO,sort);
			busUnitStr.append(items != null ? items : "");
			busUnitStr.append("</表体></企业认证信息>");
		}
		/* 企业掠影信息 */
		List<SortFieldValueVO> listSortAlbumVO = dataSortVO.getListSortAlbumVO();
		busUnitStr.append("<企业掠影信息>");
		busUnitStr.append("<表头>");
		busUnitStr.append("<企业风采背景>http://fsnrec.com:8080/portal/img/FSC/TITLE_BK.png</企业风采背景>");
		busUnitStr.append("<企业风采>企业风采</企业风采></表头>");
		if(listSortAlbumVO != null && listSortAlbumVO.size() > 0) {
			busUnitStr.append("<表体>");
			int sort = listSortAlbumVO.get(0).getSort();
			if(sort < 1){
				int size = listSortAlbumVO.size();
				sort = listSortAlbumVO.get((size > 0) ? (size-1) : 0).getSort();
			}
			String items = getItemStr(listSortAlbumVO,sort);
			busUnitStr.append(items != null ? items : "");
			busUnitStr.append("</表体>");
		}
		busUnitStr.append("</企业掠影信息>");
		
		/* 产品信息 */
		List<SortFieldValueVO> listSortProductVO = dataSortVO.getListSortProductVO();
		busUnitStr.append("<产品信息><表头>");
		busUnitStr.append("<产品展示背景>http://fsnrec.com:8080/portal/img/FSC/TITLE_BK.png</产品展示背景>");
		busUnitStr.append("<产品展示>产品展示</产品展示></表头>");
		if(listSortProductVO != null && listSortProductVO.size() > 0){
			busUnitStr.append("<表体>");
			int sort = listSortProductVO.get(0).getSort();
			String proItems = createProductItems(listSortProductVO, sort);
			busUnitStr.append(proItems != null ? proItems : "");
			busUnitStr.append("</表体>");
		}
		busUnitStr.append("</产品信息>");
		
		/* 销售案例信息 */
		List<SortFieldValueVO> listSortSalesCasetVO = dataSortVO.getListSortSalesCaseVO();
		busUnitStr.append("<销售案例信息><表头>");
		busUnitStr.append("<销售案例背景>http://fsnrec.com:8080/portal/img/FSC/TITLE_BK.png</销售案例背景>");
		busUnitStr.append("<销售案例>销售案例</销售案例></表头>");
		if(listSortSalesCasetVO != null && listSortSalesCasetVO.size() > 0) {
			busUnitStr.append("<表体>");
			int sortCase = listSortSalesCasetVO.get(0).getSort();
			if(sortCase < 1){
				int size = listSortSalesCasetVO.size();
				sortCase = listSortSalesCasetVO.get((size > 0) ? (size-1) : 0).getSort();
			}
			String caseItems = createSalesCaseItems(listSortSalesCasetVO, sortCase);
			busUnitStr.append(caseItems != null ? caseItems : "");
			busUnitStr.append("</表体>");
		}
		busUnitStr.append("</销售案例信息>");
		
		/* 销售网点信息 */
		List<SalesBranchVO> branchList = null;
		try {
			branchList = salesBranchService.getListByOrganizationWithPage(info.getOrganization(), null, -1, 0);
			if(branchList == null)return null;
		} catch (ServiceException e) {
			return null;
		}
		busUnitStr.append("<销售网点信息><表头>");
		busUnitStr.append("<销售网点背景>http://fsnrec.com:8080/portal/img/FSC/TITLE_BK.png</销售网点背景>");
		busUnitStr.append("<销售网点>销售网点</销售网点></表头>");
		if(branchList != null && branchList.size() > 0){
			busUnitStr.append("<表体>");
			for(SalesBranchVO vo : branchList){
				busUnitStr.append("<项目><销售网点名称>"+formatString(vo.getName())+"</销售网点名称>");
				busUnitStr.append("<地址>"+formatString(vo.getAddress())+"</地址>");
				busUnitStr.append("<联系电话>"+formatString(vo.getTelephone())+"</联系电话></项目>");
			}
			busUnitStr.append("</表体>");
		}
		busUnitStr.append("</销售网点信息>");
		
		/* 食品安全与营养宣传信息 */
		busUnitStr.append("<贵州食品安全与营养>");
		busUnitStr.append("<公司标题背景>http://fsnrec.com:8080/portal/img/FSC/TITLE_BK.png</公司标题背景>");
		busUnitStr.append("<示范企业>http://fsnrec.com:8080/portal/img/FSC/SF_BUS.png</示范企业>");
		busUnitStr.append("<公司二维码>http://fsnrec.com:8080/portal/img/FSC/SAGS.png</公司二维码>");
		busUnitStr.append("</贵州食品安全与营养>");
		/* xml end */
		busUnitStr.append("</企业电子资料>");
		return busUnitStr.toString();
	}
	
	/**
	 * 创建pdf通过xml数据源
	 * @param dataSource
	 * @return
	 */
	public SalesResource createPdf(String xml,String templatePath,long orgId)throws Exception
	{
		File f = new File(servletContext.getRealPath(templatePath));
		try {
			InputStream JRxml = new FileInputStream(f);
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			InputStream in_nocode;
			in_nocode = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			JRXmlDataSource xmlDataSource = new JRXmlDataSource(in_nocode);
			JasperReport jasperReport = JasperCompileManager.compileReport(JRxml);
			byte[] bytes = JasperRunManager.runReportToPdf(jasperReport, parameterMap, xmlDataSource);
			String webPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PATH);
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_ENTERPRISE_PATH);
			ftpPath = ftpPath + "/" + orgId;
			String fileName = System.currentTimeMillis()+".pdf";
			UploadUtil uploadUtil = new UploadUtil();
			boolean bRet = uploadUtil.uploadFile(bytes, ftpPath, fileName);
			if(!bRet)throw new Exception("上传pdf文件失败");
			SalesResource res = new SalesResource();
			if(UploadUtil.IsOss()){
				res.setUrl(uploadUtil.getOssSignUrl(ftpPath + "/" + fileName));
			}else{
				res.setUrl(webPath + ftpPath.substring(5) + "/" + fileName);
			}
			res.setCreateTime(new Date());
			res.setUpdateTime(new Date());
			res.setSort(-1);
			res.setCover(0);
			res.setDelStatus(0);
			res.setType(resourceTypeService.findById(3L));
			return res;
		} catch (FileNotFoundException e) {
			throw new Exception("找不到对应的报表模版");
		} catch (UnsupportedEncodingException e) {
			throw new Exception("数据源编码不正确");
		}catch (JRException e) {
			throw new Exception("生成pdf失败");
		}
	}
	
	@Override
	public void savePdfData(SalesResource res,long orgId,long enterpriseId,AuthenticateInfo info,int type)throws ServiceException
	{
		ElectronicData electDate;
		try {
			electDate = getDAO().findByOrganization(orgId,type);
			if(electDate == null){
				/* 新增电子资料 */
				electDate = new ElectronicData();
				electDate.setCreateUser(res.getCreateUser());
				electDate.setCreateTime(new Date());
				electDate.setUpdateTime(new Date());
				electDate.setUpdateUser(res.getCreateUser());
				electDate.setGuid(SalesUtil.createGUID());
				electDate.setDelStatus(0);
				electDate.setBusinessId(enterpriseId);
				electDate.setOrganization(orgId);
				electDate.setName(res.getFileName());
				electDate.setType(type);
				create(electDate);
			} else {
				/* 更新原有的电子资料 */
				electDate.setUpdateTime(new Date());
				electDate.setUpdateUser(res.getCreateUser());
				electDate.setName(res.getFileName());
				update(electDate);
				salesResourceService.removeResourceByGUID(electDate.getGuid(), info);
			}
			res.setGuid(electDate.getGuid());
			salesResourceService.create(res);
		} catch (ServiceException e) {
			throw new ServiceException(e.getMessage(), e);
		}catch (DaoException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	
	
	/**
	 * 保存企业电子资料，生产pdf。
	 * @author tangxin 2015-05-16
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultVO save(DataSortVO dataSortVO, AuthenticateInfo info) throws ServiceException{
		try{
			ResultVO result = new ResultVO();
			EnterpriseViewImgVO enterprise = businessSalesInfoService.findEnterpriseViewImgByOrganization(info.getOrganization(), true);
			String xml = getXmlData(dataSortVO,enterprise,info);
			long orgId = info.getOrganization();
			long enterpriseId = enterprise.getId();
			SalesResource res1 = createPdf(xml,"/WEB-INF/enterprise_introduce.jrxml",orgId);
			
			SalesResource res2  = createPdf(xml,"/WEB-INF/enterprise_lueying.jrxml",orgId);
			SalesResource res3  = createPdf(xml,"/WEB-INF/zhengshu.jrxml",orgId);
			SalesResource res4  = createPdf(xml,"/WEB-INF/product.jrxml",orgId);
			SalesResource res5  = createPdf(xml,"/WEB-INF/salescase.jrxml",orgId);
			SalesResource res6  = createPdf(xml,"/WEB-INF/salesbranch.jrxml",orgId);
			SalesResource res7  = createPdf(xml,"/WEB-INF/bus_sales.jrxml",orgId);
			
			res1.setFileName(enterprise.getEnterpriseName()+"企业简介.pdf");
			res1.setCreateUser(info.getUserName());
			res1.setUpdateUser(info.getUserName());
			savePdfData(res1,orgId,enterpriseId,info,2);
			
			res2.setFileName(enterprise.getEnterpriseName()+"企业相册.pdf");
			res2.setCreateUser(info.getUserName());
			res2.setUpdateUser(info.getUserName());
			savePdfData(res2,orgId,enterpriseId,info,4);
			
			res3.setFileName(enterprise.getEnterpriseName()+"企业认证.pdf");
			res3.setCreateUser(info.getUserName());
			res3.setUpdateUser(info.getUserName());
			savePdfData(res3,orgId,enterpriseId,info,3);

			res4.setFileName(enterprise.getEnterpriseName()+"产品图集.pdf");
			res4.setCreateUser(info.getUserName());
			res4.setUpdateUser(info.getUserName());
			savePdfData(res4,orgId,enterpriseId,info,5);
			
			res5.setFileName(enterprise.getEnterpriseName()+"销售案例展示.pdf");
			res5.setCreateUser(info.getUserName());
			res5.setUpdateUser(info.getUserName());
			savePdfData(res5,orgId,enterpriseId,info,6);
			
			res6.setFileName(enterprise.getEnterpriseName()+"销售网点展示.pdf");
			res6.setCreateUser(info.getUserName());
			res6.setUpdateUser(info.getUserName());
			savePdfData(res6,orgId,enterpriseId,info,7);
			
			res7.setFileName(enterprise.getEnterpriseName()+"宣传资料.pdf");
			res7.setCreateUser(info.getUserName());
			res7.setUpdateUser(info.getUserName());
			savePdfData(res7,orgId,enterpriseId,info,1);
			return result;
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}catch(Exception e){
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	/**
	 * 创建企业电子资料信息
	 * @author tangxin 2015-05-16
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultVO createElectDate(DataSortVO dataSortVO, AuthenticateInfo info) throws ServiceException{
		ResultVO result = new ResultVO();
		if(dataSortVO == null || info == null){
			return result;
		}
		if(dataSortVO.getListSortAlbumVO() == null){
			dataSortVO.setListSortAlbumVO(salesDataSortService.getSortBusinesAlbum(info.getOrganization(), -1, 0));
		}
		if(dataSortVO.getListSortProductVO() == null){
			List<SortFieldValueVO> productFiledVO = salesDataSortService.getSortProductAlbum(info.getOrganization(), true, -1, 0);
			if(productFiledVO == null || productFiledVO.size() < 1){
				productFiledVO = salesDataSortService.getSortProductAlbum(info.getOrganization(), false, 1, 8);
			}
			dataSortVO.setListSortProductVO(productFiledVO);
		}
		if(dataSortVO.getListSortSalesCaseVO() == null){
			dataSortVO.setListSortSalesCaseVO(salesDataSortService.getSortSalesCase(info.getOrganization(), -1, 0));
		}
		result = save(dataSortVO, info);
		return result;
	}
	
	@Override
	public ElectronicDataDAO getDAO() {
		return electronicDataDAO;
	}

}
