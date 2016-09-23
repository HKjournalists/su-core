package com.lhfs.fsn.util;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_HOST;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_ISPASSIVEMODE;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_PASSWORD;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_PORT;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_USENAME;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.gettec.fsnip.fsn.util.PropertiesUtil;

/**
 * 图片上传Util
 * @author Administrator
 */
public class FtpUtil {
	private static String FTP_HOST;
	private static Integer FTP_PORT;
	private static String FTP_USERNAME;
	private static String FTP_PASSWORD;
	private static boolean FTP_ISPASSIVEMODE;
	
    private FTPClient ftpClient;
    private static Logger log = Logger.getLogger(FtpUtil.class.getName());
    
    static{
		FTP_HOST = PropertiesUtil.getProperty(FSN_FTP_HOST);
		FTP_PORT = Integer.valueOf(PropertiesUtil.getProperty(FSN_FTP_PORT));
		FTP_USERNAME = PropertiesUtil.getProperty(FSN_FTP_USENAME);
		FTP_PASSWORD = PropertiesUtil.getProperty(FSN_FTP_PASSWORD);
		FTP_ISPASSIVEMODE = Boolean.parseBoolean(PropertiesUtil.getProperty(FSN_FTP_ISPASSIVEMODE));
	}
    
    public FtpUtil(){
		ftpClient= new FTPClient();
		try {
			//ftpClient.setControlEncoding("UTF-8");
			//设定为UTF-8以后，Editor中对刚上传的文件能预览成功，刷新后可以正常显示，但是通过普通FTP客户端，不能正确显示UTF-8文件名
			ftpClient.setControlEncoding("GBK");
			ftpClient.connect(FTP_HOST, FTP_PORT); // 连接
			ftpClient.login(FTP_USERNAME, FTP_PASSWORD); // 登录
			// 查看连接状态
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				log.error("FTP服务器拒绝连接。");
			}
			if(FTP_ISPASSIVEMODE){
				ftpClient.enterLocalPassiveMode();
			}
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		} catch (IOException e) {
			log.error("登录ftp服务器【" + FTP_HOST + "】失败");
			e.printStackTrace();
		}
		log.info("ftp连接成功");
	}
    
    /**
     * 关闭连接
     */
    public void closeConnection(){
    	try {
    		if(ftpClient!=null){
        		ftpClient.disconnect();
        	}
    		log.info("ftp连接成功关闭");
		} catch (IOException ioe) {
			log.info("ftp连接关闭失败");
			ioe.printStackTrace();
		}
    }
    
    
    /**
     * 上传
     * @param stream
     * @param ftpPath
     * @param fileName
     * @return boolean
     * @author ZhangHui
     */
    public boolean uploadFile(InputStream stream, String ftpPath,String fileName) {
		try {
			log.info("上传开始...");
			makeDirectory(ftpPath);
			return ftpClient.storeFile(ftpPath+"/"+fileName, stream);
		} catch (IOException e) {
			log.info("上传失败...");
			e.printStackTrace();
			return false;
		}
	}
    
    /**
     * 上传
     * @param stream
     * @param ftpPath
     * @param fileName
     * @return boolean
     * @author ZhangHui
     */
    public OutputStream getOutputStream(String ftpPath, String fileName) {
		try {
			log.info("上传开始...");
			makeDirectory(ftpPath);
			return ftpClient.storeFileStream(ftpPath + "/" + fileName);
		} catch (IOException e) {
			log.info("上传失败...");
			e.printStackTrace();
			return null;
		}
	}
    
    /**
	 * 在服务器上创建一个文件夹
	 * @param dir
	 *            文件夹名称，不能含有特殊字符，如 \  、: 、* 、?、 "、 <、>...
	 * @return boolean
	 * @author ZhangHui
	 */
	public boolean makeDirectory(String dir) {
		String[] pathList = dir.split("/");
		StringBuffer path=new StringBuffer();
		for(int i=0;i<pathList.length;i++){
			path.append("/"+pathList[i]);
			try{
				ftpClient.makeDirectory(path.toString());
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 下载（无则返回一张默认图片）
	 * @param upath
	 * @return InputStream
	 * @author ZhangHui
	 */
	/*public InputStream getInputStreamImgByPath(String upath) {
		log.info("下载开始...");
		InputStream ipts = null;
		try {
			InputStream ipt = ftpClient.retrieveFileStream(upath);
			if (ipt != null) {
				ipts = ipt;
			} else {
				ipts = getDefaultImage();
			}
		} catch (IOException e) {
			log.info("下载失败...");
			e.printStackTrace();
		}
		log.info("下载结束...");
		return ipts;
	}*/
	
	/**
	 * 下载(无则返回null)
	 * @param upath
	 * @return InputSream
	 * @author ZhangHui
	 */
	public InputStream getInputStreamByPath(String upath) {
		log.info("下载开始...");
		InputStream ipts = null;
		try {
			InputStream ipt = ftpClient.retrieveFileStream(upath);
			if (ipt != null) {
				ipts = ipt;
			}
		} catch (IOException e) {
			log.info("下载失败...");
			e.printStackTrace();
		}
		log.info("下载结束...");
		return ipts;
	}

	/**
	 * 获取默认图片
	 * @return InputStream
	 * @author ZhangHui
	 */
	/*private InputStream getDefaultImage() {
		InputStream defaultStream = null;
		// 从本地读取一个默认的图片
		try {
			String defaultPath = "/uploads/defaultimg.jpg";
			String rootPath = "";
			if (rootPath.equals("")) {
				File root = new File(".");
				rootPath = root.getCanonicalPath();
			}
			String filePath = rootPath + defaultPath;
			File imageFile = new File(filePath);
			FileInputStream inputStream = new FileInputStream(imageFile);
			defaultStream = inputStream;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return defaultStream;
	}*/

	/**
	 * 删除一个文件
	 * @param filename
	 * @return boolean
	 * @author ZhangHui
	 */
	public boolean deleteFile(String filename) {
		log.info("删除开始...");
		boolean flag = true;
		try {
			flag = ftpClient.deleteFile(filename);
		} catch (IOException ioe) {
			log.info("删除失败...");
			ioe.printStackTrace();
		}
		log.info("删除结束...");
		return flag;
	}
}