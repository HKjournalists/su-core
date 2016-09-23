package com.gettec.fsnip.fsn.util;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_REPORT_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_REPORT_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_STORAGE_SWITCH;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import com.gettec.fsnip.fsn.model.market.Resource;
import com.lhfs.fsn.util.FtpUtil;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;

public class UploadUtil {
	private FtpUtil ftpUtil;
	private OssUtil ossUtil;
	
	
	public String getOssSignUrl(String fileName){
		return OssUtil.getSignUrl(fileName);
	}
	
	/**
	 * 判断存储文件方式是用oss还是ftp
	 * @return
	 */
	public static boolean IsOss(){
		if(PropertiesUtil.getProperty(FSN_STORAGE_SWITCH).equals("oss")){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 上传文件
	 * @param file
	 * @param ftpPath
	 * @return	fileName
	 */
	public String uploadFile(MultipartFile file,String ftpPath,String fileName) {
		if(fileName==null){
			fileName=createFileNameByDate(file.getOriginalFilename());
		}
		try {
			createConnection();
			if(IsOss()){
				if(ossUtil.uploadFile(file.getInputStream(),ftpPath, fileName)){
					return fileName;
				}else{
					return null;
				}
			}else{
				ftpUtil.uploadFile(file.getInputStream(), ftpPath, fileName);
				return fileName;
			}
		} catch (Exception e) {
			return null;
		} finally {
			closeConnection();
		}
	}
	
	/**
	 * 上传 
	 * @param fileBytes
	 * @param ftpPath
	 * @param fileName
	 * @return boolean
	 * @author ZhangHui
	 */
	public boolean uploadFile(byte[] fileBytes,String ftpPath,String fileName) {
		try {
			createConnection();
			if(IsOss()){
				return ossUtil.uploadFile(new ByteArrayInputStream(fileBytes),ftpPath, fileName);
			}else{
				return ftpUtil.uploadFile(new ByteArrayInputStream(fileBytes), ftpPath, fileName);
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 上传
	 * @param in
	 * @param ftpPath
	 * @param fileName
	 * @return
	 */
	public boolean uploadFile(InputStream in,String ftpPath,String fileName) {
		try {
			createConnection();
			if(IsOss()){
				return ossUtil.uploadFile(in,ftpPath, fileName);
			}else{
				return ftpUtil.uploadFile(in, ftpPath, fileName);
			}
		} catch (Exception e) {
			return false;
		} finally {
			if(in != null){
				try {
					in.close();
					in=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 下载
	 * @param path
	 * @return InputStream
	 * @author ZhangHui
	 */
	public InputStream downloadFileStream(String path){
		try {
			createConnection();
			if(IsOss()){
				return ossUtil.downloadFileStream(path);
			}else{
				return ftpUtil.getInputStreamByPath(path);
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 删除
	 * @param removePath
	 * @return boolean
	 * @author ZhangHui
	 */
	public boolean deleteFile(String removePath){
		try {
			createConnection();
			if(IsOss()){
				return ossUtil.deleteFile(removePath);
			}else{
				return ftpUtil.deleteFile(removePath);
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 根据当前时间生成一个文件名字
	 * @param String fileName	文件名称
	 * @return	String
	 * @author ZhangHui
	 */
	public String createFileNameByDate(String fileName){
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyyMMddhhmmss");
		Random random=new Random();
		return myFormatter.format(new Date()) + random.nextInt(999)%1000 + 
				fileName.substring(fileName.lastIndexOf("."),fileName.length());
	}
	
	/**
	 * 获取json文件
	 * @param urlPath
	 * @return JSONObject
	 * @author ZhangHui
	 */
	public JSONObject getJSON(String urlPath){
		try {
			createConnection();
			/*String rootPath = "/lims/BusinessUnits";
			if(!urlPath.contains(rootPath)){
				urlPath = rootPath + urlPath;
			}*/
			InputStream is = downloadFileStream(urlPath);
			if(is==null){ return null; }
			int i = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((i = is.read()) != -1) {
			    baos.write(i);
			}
			String content = new String(baos.toByteArray(),"UTF-8");
            return JSONObject.fromObject(content);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * LIMS报告上传
	 * @param pdfURL
	 * @param serviceOrder
	 * @param sampleNO
	 * @param testType
	 * @return Map<String,String>
	 * @throws IOException
	 * @author ZhangHui
	 */
	public Map<String,String> uploadReportPdf(String edition, String pdfUrl, String serviceOrder, String testType){
		try {
			/* 1. 上传原pdf */
			createConnection();
			Map<String,String> map = new HashMap<String, String>();
			Date date = new Date();
			String uploadPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_REPORT_PATH) + "/" + edition + "/" + serviceOrder;
			String fielname = date.getTime() + ".pdf";
			boolean success_upload = false; 
			String webUrl = "";
			if(IsOss()){
				success_upload = coypOssFile(pdfUrl,uploadPath,  fielname);
				webUrl =this.getOssSignUrl(uploadPath+"/"+fielname);
			}else{
				InputStream in_full = downloadFileStream(pdfUrl);
				if(in_full==null){
					map.put("msg", "nullPdf");
					return map;
				}
				success_upload = uploadFile(in_full, uploadPath,  fielname);
				webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_REPORT_PATH) + "/" + edition + "/" + serviceOrder;
				closeConnection();
			}
			map.put("pdfPath", webUrl);
			map.put("interceptionPdf", webUrl);
			if(!success_upload){
				return null;
			}
			System.out.println(webUrl+"\n");
			/* 2. 上传截取前两页后的pdf */
			if(testType.equals("政府抽检")){
				InputStream in = downloadFileStream(pdfUrl);
				int numberOfPages = new PdfReader(in).getNumberOfPages();
				if(numberOfPages>2){
					success_upload = uploadCutReportPdf(pdfUrl, uploadPath + "/cut", fielname);
					if(success_upload){
						webUrl = "";
						webUrl =this.getOssSignUrl(uploadPath+"/cut/"+fielname);
						map.put("interceptionPdf", webUrl);
					}
				}
				closeConnection();
			}
			map.put("msg", "success");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	private boolean coypOssFile(String pdfUrl, String uploadPath,
			String fielname) {
		boolean flag = ossUtil.coypOssFile(pdfUrl,uploadPath,fielname);
		return flag;
	}


	/**
	 * 报告上传
	 * @param serviceOrder
	 * @param testType
	 * @return Map<String,String>
	 * @throws IOException
	 * @author ZhangHui
	 */
	public Map<String,String> uploadReportPdf(InputStream in_full, ByteArrayInputStream in_cut,
				String ftpPath, String fileName, String reportNo, String testType) throws IOException {
		if(in_full == null){return null;}
		try {
			/* 1. 上传原pdf */
			createConnection();
			Map<String,String> map = new HashMap<String, String>();
			boolean success_upload = uploadFile(in_full, ftpPath, fileName);
			String webUrl = "";
			if(success_upload){
				webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_REPORT_PATH) + "/" + reportNo;
				if(UploadUtil.IsOss()){
					map.put("fullPdfPath", this.getOssSignUrl(ftpPath+"/"+fileName));
				}else{
					map.put("fullPdfPath", webUrl + "/" +fileName);
				}
				map.put("interceptionPdfPath", map.get("fullPdfPath"));
			}
			closeConnection();
			/* 2. 上传截取前两页后的pdf */
			createConnection();
			if(in_cut!=null && testType.equals("政府抽检")){
				success_upload = uploadCutReportPdf(in_cut, ftpPath + "/cut", fileName);
				if(success_upload){
					map.put("interceptionPdfPath", webUrl + "/cut/" + fileName);
				}
			}
			return map;
		} catch (Exception e) {
			throw null;
		}
	}

	/**
	 * 上传截取前两页后的pdf至ftp
	 * @param in_cut
	 * @param uploadPath
	 * @param fileName
	 * @return boolean
	 * @author ZhangHui
	 */
	private boolean uploadCutReportPdf(InputStream in_cut, String uploadPath, String fileName) {
		try {
			createConnection();
			/* 得到截取前两页后的pdf的临时路径*/
			String pdfFile = partitionPdfFile(in_cut, 1, 2);
			/* 将截取的pdf读取转换为InputStream*/
			File file = new File(pdfFile);
            BufferedInputStream bufferedInputStream=new BufferedInputStream(new FileInputStream(file));
            InputStream in = bufferedInputStream;
		    boolean uploadFile = uploadFile(in, uploadPath, fileName);
		    /* 删除临时截取的pdf*/
		    if(uploadFile){
		    	if(file.exists()){
		    		file.delete();
		    	}
		    }
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 上传截取前两页后的pdf
	 * @param pdfUrl
	 * @return String
	 * @author ZhangHui
	 */
	private boolean uploadCutReportPdf(String pdfUrl, String uploadPath, String fileName) {
		InputStream in = downloadFileStream(pdfUrl);
		return uploadCutReportPdf(in, uploadPath, fileName);
	}
	
	
	public String uploadReportImg(JSONArray imgPath,String ftpPath, String fileName){
		createConnection();
		String webUrl;
		if(IsOss()){
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			if(ossUtil==null){
				ossUtil=new OssUtil();
			}
			ToPdfUtil.toPdfByJSON(imgPath, out);
			InputStream in =new ByteArrayInputStream(out.toByteArray());
			ossUtil.uploadFile(in, ftpPath, fileName);
			webUrl = OssUtil.getSignUrl(ftpPath+"/"+fileName);
		}else{
			if(ftpUtil==null){
				ftpUtil = new FtpUtil();
			}
			OutputStream out = ftpUtil.getOutputStream(ftpPath, fileName);
			ToPdfUtil.toPdfByJSON(imgPath, out);
			webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_REPORT_PATH) + "/threecode/" + fileName;
		}
		return webUrl;
	}
	
	
	/**
	 * 多张图片合成pdf，并上传
	 * @param rs
	 * @param uploadPath
	 * @param uploadFileName
	 * @param fTP_UPLOAD_PATH
	 * @param testType
	 * @return Map<String,String>
	 * @throws IOException
	 * @author ZhangHui
	 */
	public Map<String,String> uploadReportPdf(Collection<Resource> rs, 
			String ftpPath, String fileName, String reportNo) throws IOException {
		Map<String,String> map = new HashMap<String, String>();
		try {
			createConnection();
			String webUrl;
			if(IsOss()){
				ByteArrayOutputStream out=new ByteArrayOutputStream();
				if(ossUtil==null){
					ossUtil=new OssUtil();
				}
				ToPdfUtil.toPdf(rs, out);
				InputStream in =new ByteArrayInputStream(out.toByteArray());
				ossUtil.uploadFile(in, ftpPath, fileName);
				webUrl = OssUtil.getSignUrl(ftpPath+"/"+fileName);
			}else{
				if(ftpUtil==null){
					ftpUtil = new FtpUtil();
				}
				OutputStream out = ftpUtil.getOutputStream(ftpPath, fileName);
				ToPdfUtil.toPdf(rs, out);
				webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_REPORT_PATH) + "/" + reportNo+"/"+fileName;
			}
			map.put("fullPdfPath", webUrl);
			map.put("interceptionPdfPath",webUrl);
		    return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 创建连接
	 */
	private void createConnection(){
		if(IsOss()){
			if(ossUtil == null){
				ossUtil = new OssUtil();
			}
		}else {
			if(ftpUtil == null){
				ftpUtil = new FtpUtil();
			}
		}
	}
	
	/**
	 * 关闭连接
	 */
	private void closeConnection(){
		if(ftpUtil!=null){
			ftpUtil.closeConnection();
			ftpUtil = null;
		}else if(ossUtil!=null){
			ossUtil.close();
		}
	}
	
	public void close(){
		if(IsOss()){
			if(ossUtil!=null){
				ossUtil.close();
			}
		}else{
			if(ftpUtil!=null){
				ftpUtil.closeConnection();
			}
		}
	}
	/** 
	 * 
     * 截取pdf的第from页至第end页，组成一个新的文件名 
     * @param in 
     * @param subfileName 
     * @param from 
     * @param end 
     * @return 返回截取后的ftp临时路径
     * @author Zhawanneng
     */ 
    public  String partitionPdfFile(InputStream in, int from, int end) {  
        Document document = null;  
        PdfCopy copy = null; 
        String savepath = null;
        String newFile=""+System.currentTimeMillis()+".pdf";
        try {  
            PdfReader reader = new PdfReader(in);            
            int n = reader.getNumberOfPages();            
            if(end==0){  
                end = n;  
            }  
            /* 判断pdfFile文件夹是否存在不存在就创建*/
            File file1 = new File("/pdfFile");
            if (!file1.exists()) {
            	file1.mkdirs();
            }
            ArrayList<String> savepaths = new ArrayList<String>();  
            savepath = file1+ "/"+newFile;  
            savepaths.add(savepath); 
            /* 截取pdf文件输出到临时文件中，savepath是临时文件路径*/ 
            document = new Document(reader.getPageSize(1));  
            copy = new PdfCopy(document, new FileOutputStream(savepaths.get(0)));  
            document.open();
            for(int j=from; j<=end; j++) {  
                document.newPage();   
                PdfImportedPage page = copy.getImportedPage(reader, j);  
                copy.addPage(page);  
            }
            document.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch(DocumentException e) {  
            e.printStackTrace();  
        }
		return savepath;  
    } 
}
