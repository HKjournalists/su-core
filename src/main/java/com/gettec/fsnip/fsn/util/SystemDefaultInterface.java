package com.gettec.fsnip.fsn.util;

public interface SystemDefaultInterface {
	String DEFAULT_ZONE_STRING = "0";
	
	// FTP 参数配置
	String FSN_FTP_HOST = "ftp.host";
	String FSN_FTP_PORT = "ftp.port";
	String FSN_FTP_USENAME = "ftp.username";
	String FSN_FTP_PASSWORD = "ftp.password";
	String FSN_FTP_ISPASSIVEMODE = "ftp.isPassiveMode";
	
	String FSN_FTP_UPLOAD_REPORT_PATH = "ftp.upload.report.path";
	String FSN_FTP_UPLOAD_REPORT_BACK_PATH = "ftp.upload.report.back.path";
	String FSN_FTP_UPLOAD_PRODUCT_PATH = "ftp.upload.product.path";
	String FSN_FTP_UPLOAD_MEMBER_PATH = "ftp.upload.member.path";
	String FSN_FTP_UPLOAD_ENTERPRISE_PATH = "ftp.upload.enterprise.path";
	String FSN_FTP_UPLOAD_LICENSE_PATH = "ftp.upload.license.path";
	String FSN_FTP_UPLOAD_ORG_PATH = "ftp.upload.org.path";
	String FSN_FTP_UPLOAD_DIS_PATH = "ftp.upload.dis.path";
	String FSN_FTP_UPLOAD_QS_PATH = "ftp.upload.qs.path";
	String FSN_FTP_UPLOAD_LOGO_PATH = "ftp.upload.logo.path";
	String FSN_FTP_UPLOAD_LIQUOR_PATH = "ftp.upload.liquor.path";
	String FSN_FTP_UPLOAD_PRODEP_PATH = "ftp.upload.prodep.path";
	String FSN_FTP_UPLOAD_CERT_PATH = "ftp.upload.cert.path";
	String FSN_FTP_UPLOAD_BRAND_PATH = "ftp.upload.brand.path";
	String FSN_FTP_UPLOAD_WASTE_PATH = "ftp.upload.waste.path";
	String FSN_FTP_UPLOAD_DISHSNO_PATH ="ftp.upload.dishsno.path";
	//销毁记录图片上传
	String FSN_FTP_UPLOAD_DESTROY_PATH = "ftp.upload.destroy.path";
	String FSN_FTP_UPLOAD_WEB_DESTROY_PATH = "ftp.upload.web.destroy.path";
	
	String FSN_FTP_UPLOAD_WEB_PATH = "ftp.upload.web.path";
	String FSN_FTP_UPLOAD_WEB_REPORT_PATH = "ftp.upload.web.report.path";
	String FSN_FTP_UPLOAD_WEB_REPORT_BACK_PATH = "ftp.upload.web.report.back.path";
	String FSN_FTP_UPLOAD_WEB_PRODUCT_PATH = "ftp.upload.web.product.path";
	String FSN_FTP_UPLOAD_WEB_MEMBER_PATH = "ftp.upload.web.member.path";
	String FSN_FTP_UPLOAD_WEB_ENTERPRISE_PATH = "ftp.upload.web.enterprise.path";
	String FSN_FTP_UPLOAD_WEB_LICENSE_PATH = "ftp.upload.web.license.path";
	String FSN_FTP_UPLOAD_WEB_ORG_PATH = "ftp.upload.web.org.path";
	String FSN_FTP_UPLOAD_WEB_DIS_PATH = "ftp.upload.web.dis.path";
	String FSN_FTP_UPLOAD_WEB_QS_PATH = "ftp.upload.web.qs.path";
	String FSN_FTP_UPLOAD_WEB_LOGO_PATH = "ftp.upload.web.logo.path";
	String FSN_FTP_UPLOAD_WEB_LIQUOR_PATH = "ftp.upload.web.liquor.path";
	String FSN_FTP_UPLOAD_WEB_PRODEP_PATH = "ftp.upload.web.prodep.path";
	String FSN_FTP_UPLOAD_WEB_CERT_PATH = "ftp.upload.web.cert.path";
	String FSN_FTP_UPLOAD_WEB_BRAND_PATH = "ftp.upload.web.brand.path";
	String FSN_FTP_UPLOAD_WEB_WASTE_PATH = "ftp.upload.web.waste.path";
	String FSN_FTP_UPLOAD_WEB_DISHSNO_PATH ="ftp.upload.web.dishsno.path";
	
	// 销售系统上传路径
	String FSN_FTP_UPLOAD_SALES_CONTRACT_PATH = "ftp.upload.sales.contract.path";
	String FSN_FTP_UPLOAD_SALES_PROMOTION_PATH = "ftp.upload.sales.promotion.path";
	String FSN_FTP_UPLOAD_SALES_SALESCASE_PATH = "ftp.upload.sales.salescase.path";
	String FSN_FTP_UPLOAD_SALES_ALBUMS_PATH = "ftp.upload.sales.albums.path";
	String FSN_FTP_UPLOAD_SALES_DATA_PATH = "ftp.upload.sales.data.path";
	
	
	String FSN_GET_USERS_FROM_SSO_URL ="fsn.get.users.from.sso.url"; 
	String FSN_GET_NHH_FROM_SSO_URL="fsn.get.nhh.from.sso.url";
	
	//信用登记 url
    String FSN_GET_JG_CREDIT_LEVEL_URL="jg.credit.level.url"; 
	
	// 阿里云 参数配置
	String FSN_ALIYUN_KEY = "oss.key";
	String FSN_ALIYUN_KEYSECRET = "oss.secret";
	String FSN_ALIYUN_BUCKETNAME = "oss.bucketName";
	String FSN_ALIYUN_ENDPOINT = "oss.endpoint";
	
	// FTP/阿里云 模式转换标识
	String FSN_STORAGE_SWITCH = "storage.switch";
	
	//爱特购调用大众门户产品详情的地址
	final String PORTAL_PRODUCCT_DETAIL_URL = "protal.product.detail.url"; 
	
	String YIYANG_KEY="yy.key";
	String YIYANG_LOGINURL="yy.loginurl";
	
	//2016年货会
	String NIANHUOHUI_CUSTOMER_ID="nhh.customerId";
	
	final static String OPEN_API_KEY="open.api.key";
	final static String OPEN_API_SECRETKEY="open.api.secretkey";
	final static String OPEN_API_ACCESSTOKEN="open.api.accesstoken";
	final static String OPEN_API_URL="open.api.url";
	
	//rabbit mq 配置相关
	final static String RABBIT_MQ_HOST="rabbit.mq.host";
	final static String RABBIT_MQ_PORT="rabbit.mq.port";
	final static String RABBIT_MQ_USERNAME="rabbit.mq.username";
	final static String RABBIT_MQ_PASSWORD="rabbit.mq.password";
	final static String RABBIT_MQ_QUEUE="rabbit.mq.queue";
	

	//监管设置相关的值
	final static String RABBIT_MQ_SUPERVISE_JG="rabbit.mq.supervise.jg";
	final static String RABBIT_MQ_SUPERVISE_KEY="rabbit.mq.supervise.key";
	final static String RABBIT_MQ_SUPERVISE_DIRECT="rabbit.mq.supervise.direct";
	final static String RABBIT_MQ_SUPERVISE_JG_FEN="rabbit.mq.supervise.jgfsn";
	
}
