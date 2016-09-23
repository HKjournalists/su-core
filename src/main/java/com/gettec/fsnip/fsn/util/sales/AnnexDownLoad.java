package com.gettec.fsnip.fsn.util.sales;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_PATH;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.gettec.fsnip.fsn.model.sales.SalesResource;
import com.gettec.fsnip.fsn.util.FileUtils;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;

/**
 * Created by HY on 2015/5/9.
 * desc: 附件下载
 */
public class AnnexDownLoad {
    static final File buffPath = new File("./"+System.currentTimeMillis());
    /**
     * @return
     * @throws IOException
     */
    private File inputstreamtofile(UploadUtil uploadUtil,SalesResource res) throws IOException {
        String path = "/http" + res.getUrl().replace(PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PATH), "");
        InputStream in = uploadUtil.downloadFileStream(path);
        //File bufferDir = new File(buffPath);
        if (!buffPath.exists() && !buffPath .isDirectory()){
            buffPath .mkdir();
        }
        File file = new File(buffPath.getPath() +"/"+res.getFileName());
        Integer indx = 1;
        while(file.exists()){
        	String fileName = file.getName();
        	String exp = FileUtils.getExtension(file.getName());
        	fileName = fileName.replace("."+exp, "");
        	file = new File(buffPath.getPath() +"/" + fileName + indx + "." + exp);
        	indx += 1;
        }
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        in.close();
        return file;
    }

    //文件打包下载
    public static File downLoadFiles(List<SalesResource> resLists, String enterName) throws Exception {
        //String dirpath = ;
        //String buffPath = "D://56256";
    	File returnFiel = null;
        try {
        	enterName = (enterName == null ? "企业" : enterName);
            UploadUtil uploadUtil = new UploadUtil();
            List<File> files = new ArrayList<File>();
            AnnexDownLoad getFile= new AnnexDownLoad();
			/*2 将资源下载封装到List<File> 中*/
            for(SalesResource res : resLists){
                File file = getFile.inputstreamtofile(uploadUtil,res);
                files.add(file);
            }
            /**这个集合就是你想要打包的所有文件，
             * 这里假设已经准备好了所要打包的文件*/
            //List<File> files = new ArrayList<File>();

            /**创建一个临时压缩文件，
             * 我们会把文件流全部注入到这个文件中
             * 这里的文件你可以自定义是.rar还是.zip*/

            returnFiel = new File(buffPath.getPath()+"/《"+enterName+"》相关资料.zip");
            if (!returnFiel.exists()){
            	returnFiel.createNewFile();
            }
            //response.getWriter()
            //创建文件输出流
            FileOutputStream fous = new FileOutputStream(returnFiel);
            /**打包的方法我们会用到ZipOutputStream这样一个输出流,
             * 所以这里我们把输出流转换一下*/
            ZipOutputStream zipOut = new ZipOutputStream(fous);
            zipOut.setEncoding("GBK");
            /**这个方法接受的就是一个所要打包文件的集合，
             * 还有一个ZipOutputStream*/
            zipFile(files, zipOut);
            zipOut.closeEntry();
            zipOut.close();
            fous.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        /**直到文件的打包已经成功了，
         * 文件的打包过程被我封装在FileUtil.zipFile这个静态方法中，
         * 稍后会呈现出来，接下来的就是往客户端写数据了*/
        return returnFiel ;
    }

    /**
     * 把接受的全部文件打成压缩包
     */
    public static void zipFile(List<File> files,ZipOutputStream outputStream) {
        int size = files.size();
        for(int i = 0; i < size; i++) {
            File file = files.get(i);
            zipFile(file, outputStream);
        }
    }

    public static HttpServletResponse downloadZip(File file,HttpServletResponse response) {
        try {
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            // 清空response
            response.reset();
            response.setContentLength(fis.available());
            fis.read(buffer);
            fis.close();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");

            //如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
            String name = new String(file.getName().getBytes("utf-8"),"ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + name);
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }

    /**
     * 根据输入的文件与输出流对文件进行打包
     */
    public static void zipFile(File inputFile,ZipOutputStream ouputStream) {
        try {
            if(inputFile.exists()) {
                /**如果是目录的话这里是不采取操作的，
                 * 至于目录的打包正在研究中*/
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 1024);
                    //org.apache.tools.zip.ZipEntry
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    ouputStream.putNextEntry(entry);
                    // 向压缩文件中输出数据
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        ouputStream.write(buffer, 0, nNumber);
                    }
                    // 关闭创建的流对象
                    bins.close();
                    IN.close();
                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            zipFile(files[i], ouputStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除文件夹
    //param folderPath 文件夹完整绝对路径
    public static void delFolder(String folderPath) {
        try {
        	if(folderPath == null || "".equals(folderPath)) {
        		return;
        	}
            delAllFile(folderPath); //删除完里面所有内容
            java.io.File myFilePath = new java.io.File(folderPath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除指定文件夹下所有文件
    //param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        if(path == null || "".equals(path)){
        	return true;
        }
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }
}
