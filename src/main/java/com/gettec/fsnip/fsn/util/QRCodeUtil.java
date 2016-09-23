package com.gettec.fsnip.fsn.util;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_PRODUCT_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_PRODUCT_PATH;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Hashtable;

import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.QRCodeProductInfo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * 提供二维码产品相关的操作，当前有生成条形码信息，生产二维码图片信息
 * @author TangXin
 */
public class QRCodeUtil {

	/**
	 * 动态生产条形码
	 * @param busId 入驻企业ID
	 * @param cityCode 省市县代码
	 * @param productAreaCode 产品所属区域
	 * @param productLP 产品自动流水码
	 * @return barcode 返回barcode
	 * @author TangXin
	 */
	public static String createBarCode(Long busId, String cityCode, String productAreaCode, long productLP){
		String barcode = null;
		try{
			if(busId==null||cityCode==null||productLP<=0) return null;
			if(productAreaCode==null||productAreaCode.equals("")) productAreaCode="00";
			/*1.拼接企业ID，长度共6位,不足的在前面补0*/
			String busIdStr = busId.toString();
			String slNo = String.valueOf(productLP); //产品流水号
			while(busIdStr.length()<5){
				busIdStr="0"+busIdStr;
			}
			/*2.拼接产品流水码，长度共5位,不足的在前面补0*/
			while(slNo.length()<5){
				slNo="0"+slNo;
			}
			barcode = busIdStr + cityCode + productAreaCode + slNo;
		}catch(Exception e){
			barcode = null;
		}
		return barcode;
	}
	
	/**
	 * 生成二维码
	 * @param content
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage createQRCode(String content,int width,int height){
		try {
			Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();  
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
			hints.put(EncodeHintType.MARGIN,0);
			BitMatrix matrix = new MultiFormatWriter().encode(content,  
					BarcodeFormat.QR_CODE, width, height, hints); 
			return MatrixToImageWriter.toBufferedImage(matrix); 
		} catch (WriterException e) {  
			e.printStackTrace();  
			return null;
		}
	}
	
	public static BitMatrix createBitMatrix(String content,int width,int height){
		try {
			Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();  
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
			hints.put(EncodeHintType.MARGIN,0);
			BitMatrix matrix = new MultiFormatWriter().encode(content,  
					BarcodeFormat.QR_CODE, width, height, hints); 
			return matrix;
		} catch (WriterException e) {  
			e.printStackTrace();  
			return null;
		}
	}

	/**
	 * 通过qrcodeVO 生产二维码图片
	 * @param qrcodeVO
	 * @return
	 * @author TangXin
	 */
	public static Resource createQRCode(QRCodeProductInfo qrcodeProductInfo, String logoPath){
		Resource resource = null;
		File qrcodeFile=null;
		try{
			Product product = qrcodeProductInfo.getProduct();
			StringBuffer params = new StringBuffer(); 
			params.append("产品名称：").append(product.getName()).append("\r\n");
			params.append("产地：").append(product.getProducer().getAddress()).append("\r\n");  
			params.append("供应商：").append(product.getProducer().getName()).append("\r\n");  
			params.append("分类：").append(qrcodeProductInfo.getProAreaName()).append("\r\n");  
			params.append("流水码：").append(product.getBarcode()).append("\r\n"); 
			params.append("【欲知该产品更多安全与营养信息，请至各应用商城下载【食安测】APP】"); 
			String content = params.toString(); //二维码内容
			String format = "png"; //二维码图片的格式
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();   
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
			String fileName = product.getBarcode()+".png";
			qrcodeFile = new File("./"+fileName);
			InputStream logoIs = new FileInputStream(logoPath);
			MatrixToImageWriter.writeToFile(bitMatrix, format, qrcodeFile, logoIs);
			/*上传ftp*/
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_PRODUCT_PATH) + "/" + product.getBarcode();
			UploadUtil uploadUtil = new UploadUtil();
			boolean success_upload = uploadUtil.uploadFile(new FileInputStream(qrcodeFile), ftpPath, fileName);
			if(success_upload){
				String webPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PRODUCT_PATH) + "/" + product.getBarcode();
				/*构造MkTestresource 对象并返回*/
				resource = new Resource();
				resource.setFileName(fileName);
				resource.setName(fileName);
				if(UploadUtil.IsOss()){
					resource.setUrl(uploadUtil.getOssSignUrl(ftpPath + "/" + fileName));
				}else{
					resource.setUrl(webPath + "/" + fileName);
				}
			}
			if(logoIs!=null) logoIs.close();
		}catch(Exception e){
			resource = null;
			e.printStackTrace();
		}finally{
			try{
				if(qrcodeFile!=null&&qrcodeFile.exists()){
					qrcodeFile.delete();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return resource;
	}
}
