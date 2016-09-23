package com.gettec.fsnip.fsn.util.sales;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 销售系统共用工具类
 * @author tangxin 2015/04/24
 *
 */
public class SalesUtil {
	
	/* 相册ID */
	public static final String SANZHENG_ALBUM_ID = "1"; //三证相册
	public static final String HONOR_ALBUM_ID = "2"; //荣誉证书
	public static final String CERTIFICATION_ALBUM_ID = "3"; //其他认证相册
	public static final String PRODUCT_ALBUM_ID = "4"; //产品相册
	public static final String BUSPHOTO_ALBUM_ID = "5"; //企业掠影
	public static final String BUSSALESCASE_ALBUM_ID = "6"; //企业销售案例相册

	/* 相册名称 */
	public static final String SANZHENG_ALBUM_NAME = "三证";
	public static final String HONOR_ALBUM_NAME = "荣誉证书";
	public static final String CERTIFICATION_ALBUM_NAME = "认证证照";
	public static final String PRODUCT_ALBUM_NAME = "产品图片集 ";
	public static final String BUSPHOTO_ALBUM_NAME = "企业掠影";
	
	/* 认证信息类型 */
	public static final int CERTIFICATION_TYPE_STD = 0; //标准认证信息类型
	public static final int CERTIFICATION_TYPE_SLF = 1; //用户上传的荣誉认证类型
	
	/* 相册图片展示字段类型 */
	public static final int PHOTO_FIELD_TYPE_ORGANIZATION = 1; //组织机构类型
	public static final int PHOTO_FIELD_TYPE_LICENCE = 2; //营业执照类型
	public static final int PHOTO_FIELD_TYPE_TAXREGISTER = 3; //税务登记证类型
	public static final int PHOTO_FIELD_TYPE_HONORCERT = 4; //荣誉证书类型
	public static final int PHOTO_FIELD_TYPE_CERTIFICATE = 5; //企业认证类型
	public static final int PHOTO_FIELD_TYPE_PRODUCT = 6; //产品类型
	public static final int PHOTO_FIELD_TYPE_BUSALBUM = 7; //企业掠影
	
	// 默认图片路径
	public static final String DEFAULT_IMG_URL = "http://fsnrec.com:8080/portal/img/temp/temp.jpg";
	
	// 相冊封面图片
	// 三证封面
	public static final String SANZHENG_COVER_IMG = "http://fsnrec.com:8080/portal/img/FSC/SANZHENG_ALBUM_COVER.png";
	// 荣誉证书封面
	public static final String HONOR_COVER_IMG = "http://fsnrec.com:8080/portal/img/FSC/HONOR_ALBUM_COVER.png";
	// 认证证照封面
	public static final String CERTIFICATION_COVER_IMG = "http://fsnrec.com:8080/portal/img/FSC/CERTIFICATION_ALBUM_COVER.png";
	// 产品图片集 封面
	public static final String PRODUCTS_COVER_IMG = "http://fsnrec.com:8080/portal/img/FSC/PRODUCTS_ALBUM_COVER.png";
	// 企业掠影相册封面
	public static final String BUS_ALBUM_COVER_IMG = "http://fsnrec.com:8080/portal/img/FSC/BUS_ALBUM_COVER.png";
	
	/**
	 * 生成全局变量GUID
	 * @author tangxin 2015/04/24
	 */
	public static synchronized String createGUID() {
		try{
			UUID uuid = UUID.randomUUID();   
			//return uuid != null ? uuid.toString().replace("-", "") : null; 
			return uuid != null ? uuid.toString() : null; 
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解析图片的切割参数
	 * @param cut
	 * @return
	 * @author tangxin 2015/04/24
	 */
	public static Map<String,Integer> getCutWidthAndHeight(String cut){
		Map<String,Integer> resultMap = null;
		try{
			if(cut == null || !(cut.contains("x") || cut.contains("X"))) {
				return null;
			}
			String[] wh = cut.split("x");
			if(wh.length > 2) {
				return null;
			}
			if(wh.length <= 1) {
				wh = cut.split("X");
			}
			if(wh.length > 2 || wh.length <= 1) {
				return null;
			}
			int w = Integer.parseInt(wh[0]);
			int h = Integer.parseInt(wh[1]);
			resultMap = new HashMap<String,Integer>();
			resultMap.put("width", w);
			resultMap.put("height", h);
			return resultMap;
		}catch(Exception e){
			return null;
		}
	}
}
