package com.gettec.fsnip.fsn.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.google.zxing.common.BitMatrix;
 
 
 public final class MatrixToImageWriter {
 
   private static final int BLACK = 0xFF000000;
   private static final int WHITE = 0xFFFFFFFF;
 
   private MatrixToImageWriter() {}
 
   
   public static BufferedImage toBufferedImage(BitMatrix matrix) {
     int width = matrix.getWidth();
     int height = matrix.getHeight();
     BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
     for (int x = 0; x < width; x++) {
       for (int y = 0; y < height; y++) {
         image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
       }
     }
     return image;
   }
 
   
   public static void writeToFile(BitMatrix matrix, String format, File file)
       throws IOException {
     BufferedImage image = toBufferedImage(matrix);
     if (!ImageIO.write(image, format, file)) {
       throw new IOException("Could not write an image of format " + format + " to " + file);
     }
   }
 
   
   public static void writeToStream(BitMatrix matrix, String format, OutputStream stream)
       throws IOException {
     BufferedImage image = toBufferedImage(matrix);
     if (!ImageIO.write(image, format, stream)) {
       throw new IOException("Could not write an image of format " + format);
     }
   }
 
   /** 
    *  
    * @param matrix 二维码矩阵相关 
    * @param format 二维码图片格式 
    * @param file 二维码图片文件 
    * @param logoPath logo路径 
    * @throws IOException 
    */  
   public static void writeToFile(BitMatrix matrix,String format,File file,InputStream logoIs) throws IOException {  
       BufferedImage image = toBufferedImage(matrix);  
       Graphics2D gs = image.createGraphics();  
       //载入logo  
       BufferedImage logo = ImageIO.read(logoIs);  
       int widthLogo = logo.getWidth(), heightLogo = logo.getHeight();
       // 计算图片放置位置
       int x = (image.getWidth() - widthLogo) / 2;
       int y = (image.getHeight() - heightLogo) / 2;
       gs.drawImage(logo, x, y, widthLogo, heightLogo, null);
       gs.dispose();
       logo.flush();
       if(!ImageIO.write(image, format, file)){  
           throw new IOException("Could not write an image of format " + format + " to " + file);    
       }  
   }  
   
 }