package com.gettec.fsnip.fsn.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.gettec.fsnip.fsn.model.market.Resource;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 
 * @author Administrator
 *
 */
public class ToPdfUtil {
	public static void toPdf(Collection<Resource> rs, OutputStream out){
		/* 1. 创建一个文档对象  */
		Document doc = new Document();
		try {
			/* 2. 定义输出文件的位置   */
			PdfWriter.getInstance(doc, out);
			/* 3. 开启文档 */
			doc.open();
			/* 4. 向文档中加入图片 */
			for(Resource resource : rs){
				/* 5. 取得图片 */
				Image jpg = Image.getInstance(resource.getFile()); //原来的图片的路径
				float heigth = jpg.getScaledHeight();
				float width = jpg.getScaledWidth();
				/* 6. 压缩 */
				//int percent=getPercent(heigth, width); // 合理压缩(h>w，按w压缩，否则按h压缩)
				int percent=getPercent2(heigth, width); // 统一按照宽度压缩
				/* 7. 设置图片居中显示 */
				jpg.setAlignment(Image.MIDDLE);
				/* 8. 按百分比显示图片的比例 */
				jpg.scalePercent(percent);//表示是原来图像的比例;
				doc.add(jpg);
			}
			/* 关闭文档并释放资源 */
			doc.close();
		}
		catch (FileNotFoundException e) {   
			e.printStackTrace();   
		} catch (DocumentException e) {   
			e.printStackTrace();   
		} 
		catch (IOException e) {   
			e.printStackTrace();   
		}
	}


	public static void toPdfByJSON(JSONArray imgPath,OutputStream out){
		/* 1. 创建一个文档对象  */
		Document doc = new Document();
		try {
			/* 2. 定义输出文件的位置   */
			PdfWriter.getInstance(doc, out);
			/* 3. 开启文档 */
			doc.open();
			/* 4. 向文档中加入图片 */
			/* 5. 取得图片 */
			for(int i=0;i<imgPath.size();i++){
				Image jpg = Image.getInstance(getImageFromURL(imgPath.getString(i))); //原来的图片的路径
				float heigth = jpg.getScaledHeight();
				float width = jpg.getScaledWidth();
				/* 6. 压缩 */
				int percent=getPercent2(heigth, width); // 统一按照宽度压缩
				/* 7. 设置图片居中显示 */
				jpg.setAlignment(Image.MIDDLE);
				/* 8. 按百分比显示图片的比例 */
				jpg.scalePercent(percent);//表示是原来图像的比例;
				doc.add(jpg);
			}
			/* 关闭文档并释放资源 */
			doc.close();
		}
		catch (FileNotFoundException e) {   
			e.printStackTrace();   
		} catch (DocumentException e) {   
			e.printStackTrace();   
		} 
		catch (IOException e) {   
			e.printStackTrace();   
		}
	}
	/**
	 * 第一种解决方案
	 * 在不改变图片形状的同时，判断，如果h>w，则按h压缩，否则在w>h或w=h的情况下，按宽度压缩
	 * @param h
	 * @param w
	 * @return
	 */
	public static int getPercent(float h,float w){
		int p=0;
		float p2=0.0f;
		if(h>w){
			p2=297/h*100;
		}else{
			p2=210/w*100;
		}
		p=Math.round(p2);
		return p;
	}
	/**
	 * 第二种解决方案，统一按照宽度压缩
	 * 这样来的效果是，所有图片的宽度是相等的，自我认为给客户的效果是最好的
	 * @param args
	 */
	public static int getPercent2(float h,float w){
		int p=0;
		float p2=0.0f;
		p2=530/w*100;
		p=Math.round(p2);
		return p;
	}

	/**
	 * 根据图片网络地址转byte
	 * @param urlPath
	 * @return
	 */
	public static byte[] getImageFromURL(String urlPath) { 
		byte[] data = null; 
		InputStream is = null; 
		HttpURLConnection conn = null; 
		try { 
			URL url = new URL(urlPath); 
			conn = (HttpURLConnection) url.openConnection(); 
			conn.setDoInput(true); 
			// conn.setDoOutput(true); 
			conn.setRequestMethod("GET"); 
			conn.setConnectTimeout(6000); 
			is = conn.getInputStream(); 
			if (conn.getResponseCode() == 200) { 
				data = readInputStream(is); 
			} else{ 
				data=null; 
			} 
		} catch (MalformedURLException e) { 
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} finally { 
			try { 
				if(is != null){ 
					is.close(); 
				}                
			} catch (IOException e) { 
				e.printStackTrace(); 
			} 
			conn.disconnect();           
		} 
		return data; 
	} 
	/**
	 * 读取InputStream数据，转为byte[]数据类型
	 * @param is
	 *            InputStream数据
	 * @return 返回byte[]数据
	 */

	private static byte[] readInputStream(InputStream is) { 
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		byte[] buffer = new byte[1024]; 
		int length = -1; 
		try { 
			while ((length = is.read(buffer)) != -1) { 
				baos.write(buffer, 0, length); 
			} 
			baos.flush(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
		byte[] data = baos.toByteArray(); 
		try { 
			is.close(); 
			baos.close(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
		return data; 
	} 

}
