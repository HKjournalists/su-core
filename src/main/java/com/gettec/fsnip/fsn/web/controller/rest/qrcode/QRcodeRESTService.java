package com.gettec.fsnip.fsn.web.controller.rest.qrcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gettec.fsnip.fsn.util.QRCodeUtil;

@Controller
@RequestMapping("/qrcode")
public class QRcodeRESTService {
	@RequestMapping(method = RequestMethod.GET, value = "/create")
	public void create(HttpServletResponse response,@RequestParam(value="content") String content,
			@RequestParam("width") int width,@RequestParam("height") int height) throws IOException{
		ImageIO.write(QRCodeUtil.createQRCode(content,width,height),"PNG", response.getOutputStream());
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/createImg")
	public void createImg(HttpServletResponse response,@RequestParam(value="QRstart") int QRstart,
			@RequestParam("QRend") int QRend) throws IOException{
		int size=85;
		String content="http://www.fsnip.com/fsn-portal/mobile/product/read.shtml?id=";
		BufferedImage imageResult = new BufferedImage(size * 14 , size*9,BufferedImage.TYPE_INT_RGB);
		for(int i=QRstart;i<=QRend;i++){
			int[] imgArray = new int[size*size];// 从图片中读取RGB
			BufferedImage bi=QRCodeUtil.createQRCode(content+i,85,85);
			imgArray = bi.getRGB(0, 0, size, size, imgArray, 0, size);
			System.out.println();
			for(int col=0;col<9;col++){
				for(int row=0;row<14;row++){
//					if(col==8&&row==13){
//						imageResult.setRGB(size*row, size*col, size, size, imageArraySecond, 0, size);
//					}else{
						imageResult.setRGB(size*row, size*col, size, size, imgArray, 0, size);	
//					}
				}
			}
			File outFile = new File("E:\\1.jpg");  
			ImageIO.write(imageResult, "jpg", outFile);// 写图片  
		}
	}
}
