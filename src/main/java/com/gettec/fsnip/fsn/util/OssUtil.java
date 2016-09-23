package com.gettec.fsnip.fsn.util;

import static com.aliyun.oss.internal.OSSUtils.OSS_RESOURCE_MANAGER;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_ALIYUN_BUCKETNAME;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_ALIYUN_ENDPOINT;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_ALIYUN_KEY;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_ALIYUN_KEYSECRET;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.internal.OSSUtils;
import com.aliyun.oss.model.CopyObjectRequest;
import com.aliyun.oss.model.CopyObjectResult;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;

public class OssUtil {
	private static String key;
	private static String secret;
	private static String bucketName;
	private static String endpoint;
	private OSSClient client;
	static{
		key = PropertiesUtil.getProperty(FSN_ALIYUN_KEY);
		secret = PropertiesUtil.getProperty(FSN_ALIYUN_KEYSECRET);
		bucketName = PropertiesUtil.getProperty(FSN_ALIYUN_BUCKETNAME);
		endpoint=PropertiesUtil.getProperty(FSN_ALIYUN_ENDPOINT);
	}
	public OssUtil(){
		if(client==null){
			if(endpoint==null){
				client = new OSSClient(key, secret);
			}else{
				client = new OSSClient(endpoint, key, secret);
			}
		}
	}
	
	public void close(){
		if(client!=null){
			client.shutdown();
		}
	}
	
	/**
	 * 上传
	 * @param in
	 * @param remotepath
	 * @param filename
	 * @return
	 */
	public boolean uploadFile(InputStream in, String remotepath, String filename){
		try{
			//检测拼接的地址最后一个字符是不是斜杠，如果不是，则增加‘/’
			String slash = remotepath.substring(remotepath.length() - 1, remotepath.length());
			String remoteurl = "";
			if("/".equals(slash)){
				remoteurl = remotepath  + filename;
			}
			else{
				remoteurl = remotepath  + "/" + filename;
			}
			//检测remoteurl的第一个字符是不是斜杠，如是，则去掉‘/’，否则阿里云无法生成路径
			String check_slash = remoteurl.substring(0,1);
			if("/".equals(check_slash)){
				//把前面多出来的“/”去掉
				remoteurl = remoteurl.substring(1);
			}
			// 创建上传Object的Metadata
			ObjectMetadata meta = new ObjectMetadata();
			meta.setCacheControl("no-cache");  
			meta.setHeader("Pragma", "no-cache");  
			// 必须设置ContentLength
			meta.setContentLength(in.available());
			client.putObject(bucketName, remoteurl, in, meta);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	

	/**
	 * 下载
	 * @param localpath
	 * @param remoteurl
	 * @param filename
	 * @return String
	 * @author ZhangHui
	 */
	public String downloadFile(String localpath, String remoteurl, String filename){
		String path = null;
		try{
			//检测拼接的地址最后一个字符是不是斜杠，如果不是，则增加‘/’
			String slash = localpath.substring(localpath.length() - 1, localpath.length());
			if("/".equals(slash)){
				path = localpath + filename;
			}
			else{
				path = localpath + "/" + filename;
			}
			// 新建GetObjectRequest
			GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, remoteurl);
			// 下载Object到文件
			client.getObject(getObjectRequest, new File(path));
		}catch (Exception e){
			e.printStackTrace();
			return path;
		}
		return path;
	}

	/**
	 * 删除
	 * @param remotePath
	 * @return boolean
	 * @author ZhangHui
	 */
	public boolean deleteFile(String remotePath){
		boolean flag = true;
		try{
			String remoteurl = "";
			//检测remotePath的第一个字符是不是斜杠，如是，则去掉‘/’，否则阿里云无法生成路径
			String check_slash = remotePath.substring(0,1);
			if("/".equals(check_slash)){
				//去掉路径前面的“/”
				remoteurl = remotePath.substring(1);
			}
			client.deleteObject(bucketName, remoteurl);
		}catch(Exception e){
			e.printStackTrace();
			flag = false;
			return flag;
		}
		return flag;
	}

	/**
	 * 下载
	 * @param remotePath
	 * @return InputStream
	 * @author ZhangHui
	 */
	public InputStream downloadFileStream(String remotePath) {
		InputStream objectContent = null;
		try{
			//检测remotePath的第一个字符是不是斜杠，如是，则去掉‘/’，否则阿里云无法生成路径
			if("/".equals(remotePath.substring(0,1))){
				//去掉路径前面的“/”
				remotePath = remotePath.substring(1);
			}
			OSSObject object = client.getObject(bucketName, remotePath);
			objectContent = object.getObjectContent();
			return objectContent;
		}catch(Exception e){
			e.printStackTrace();
			return objectContent;
		}
	}
	
    /**
     * 从lims环境服务器上的文件拷贝到本服务器环境上
     * @param pdfUrl   lims环境路径
     * @param remotepath 本(目标)服务器环境路径
     * @param filename  文件名
     * @return
     */
	@SuppressWarnings("unused")
	public boolean coypOssFile(String pdfUrl, String remotepath, String filename) {
		boolean  flag = true;
		
		//检测拼接的地址最后一个字符是不是斜杠，如果不是，则增加‘/’
		String slash = remotepath.substring(remotepath.length() - 1, remotepath.length());
		String remoteurl = "";
		if("/".equals(slash)){
			remoteurl = remotepath  + filename;
		}else{
			remoteurl = remotepath  + "/" + filename;
		}
		//检测remoteurl的第一个字符是不是斜杠，如是，则去掉‘/’，否则阿里云无法生成路径
		String check_slash = remoteurl.substring(0,1);
		if("/".equals(check_slash)){
			//把前面多出来的“/”去掉
			remoteurl = remoteurl.substring(1);
		}
		//检测remoteurl的第一个字符是不是斜杠，如是，则去掉‘/’，否则阿里云无法生成路径
		String check_slash_y = pdfUrl.substring(0,1);
			if("/".equals(check_slash_y)){
				//把前面多出来的“/”去掉
				pdfUrl = pdfUrl.substring(1);
		}
		// 创建CopyObjectRequest对象
		CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName, pdfUrl, bucketName, remoteurl);
		// 设置新的Metadata
		ObjectMetadata meta = new ObjectMetadata();
		//meta.setContentType("application/xml");
		copyObjectRequest.setNewObjectMetadata(meta);
		
		// 必须设置ContentLength
		//meta.setContentLength(1024);
		// 复制Object
		CopyObjectResult result = client.copyObject(copyObjectRequest);
		
		return flag;
	}
	
	/**
	 * 获取签名url
	 * @param fileName
	 * @return
	 */
	public static String getSignUrl(String fileName){
		String slash = fileName.substring(0,1);
		if("/".equals(slash)){
			fileName=fileName.substring(1, fileName.length());
		}
		// 设置URL过期时间为100年
		String expires="4102416000";
		String canonicalString="GET\n\n\n"+expires+"\n/"+bucketName+"/"+fileName;
		String signature = computeSignature(secret, canonicalString);
		Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("Expires", expires);
        params.put("OSSAccessKeyId", key);
        params.put("Signature", signature);
        String queryString = paramToQueryString(params,DEFAULT_ENCODING);
        String url=PropertiesUtil.getProperty("oss.url")+"/"+OSSUtils.determineResourcePath(bucketName, fileName, false)+"?"+queryString;
        return url;
	}
	
	
	
	
	 /* The default encoding. */
    private static final String DEFAULT_ENCODING = "UTF-8";
    
    /* Signature method. */
    private static final String ALGORITHM = "HmacSHA1";
    
    private static final Object LOCK = new Object();
    
    /* Prototype of the Mac instance. */
    private static Mac macInstance;

    public String getAlgorithm() {
        return ALGORITHM;
    }

    private static String computeSignature(String key, String data) {
        try {
            byte[] signData = sign(key.getBytes(DEFAULT_ENCODING), data.getBytes(DEFAULT_ENCODING));
            return BinaryUtil.toBase64String(signData);
        }
        catch(UnsupportedEncodingException ex) {
            throw new RuntimeException("Unsupported algorithm: " + DEFAULT_ENCODING, ex);
        }
    }


    private static byte[] sign(byte[] key, byte[] data) {
        try {
            // Because Mac.getInstance(String) calls a synchronized method, it could block on 
            // invoked concurrently, so use prototype pattern to improve perf.
            if (macInstance == null) {
                synchronized (LOCK) {
                    if (macInstance == null) {
                        macInstance = Mac.getInstance(ALGORITHM);
                    }
                }
            }

            Mac mac = null;
            try {
                mac = (Mac)macInstance.clone();
            } catch (CloneNotSupportedException e) {
                // If it is not clonable, create a new one.
                mac = Mac.getInstance(ALGORITHM);
            }
            mac.init(new SecretKeySpec(key, ALGORITHM));
            return mac.doFinal(data);
        }
        catch(NoSuchAlgorithmException ex) {
            throw new RuntimeException("Unsupported algorithm: " + ALGORITHM, ex);
        }
        catch(InvalidKeyException ex) {
            throw new RuntimeException("Invalid key: " + key, ex);
        }
    }
    /**
     * Encode a URL segment with special chars replaced.
     */
    private static String urlEncode(String value, String encoding) {
        if (value == null) {
            return "";
        }
        
        try {
            String encoded = URLEncoder.encode(value, encoding);
            return encoded.replace("+", "%20").replace("*", "%2A")
                    .replace("%7E", "~").replace("%2F", "/");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(OSS_RESOURCE_MANAGER.getString("FailedToEncodeUri"), e);
        }
    }


    /**
     * Encode request parameters to URL segment.
     */
    private static String paramToQueryString(Map<String, String> params, String charset) {
        
        if (params == null || params.isEmpty()) {
            return null;
        }

        StringBuilder paramString = new StringBuilder();
        boolean first = true;
        for(Entry<String, String> p : params.entrySet()) {
            String key = p.getKey();
            String value = p.getValue();

            if (!first) {
                paramString.append("&");
            }

            // Urlencode each request parameter
            paramString.append(urlEncode(key, charset));
            if (value != null) {
                paramString.append("=").append(urlEncode(value, charset));
            }

            first = false;
        }

        return paramString.toString();
    }
}
