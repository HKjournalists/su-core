package com.gettec.fsnip.fsn.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

/**
 * Created by Sam Zhou on 2016/3/10.
 */
public class DataOpenPlatformUtils {

    private final static String SUPPORT_EMAIL = "wenxian@fsnip.com";

    private final static String APP_KEY         = "f91f0d0e-ebce-4724-87f6-aadf49d4c74a";
    private final static String SECRET_KEY      = "c9935f22-c751-45e1-9de8-c4d5a75ad5bb";
    private final static String ACCESS_TOKEN    = "8d41642455afd5de76c6c0735575e7a6";

    private static final Logger LOG = Logger.getLogger(DataOpenPlatformUtils.class);

    /**
     * @param message 包含发送的消息以及接收的手机号码
     * @return
     */
    public static boolean sendMessage(String message){

        boolean success = false;
        byte[] result = null;
        HttpClient client = new HttpClient();
        URI uri = null;
        try {
            uri = new URI("http://api.fsnip.com/fop/rest/router?method=sms.fsnip.sms.service.sms.sendsms" +
                    "&version=1&format=json&accesstoken=" + ACCESS_TOKEN +
                    "&appkey=" + APP_KEY +
                    "&secretkey=" + SECRET_KEY,false,"UTF-8");
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        PostMethod method = new PostMethod(uri.toString());
        method.setRequestBody(message);
        method.addRequestHeader("Content-Type", "text/html; charset=UTF-8");
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8");
        try {
            int returnCode = client.executeMethod(method);
            if(returnCode == HttpStatus.SC_OK){
                success = true;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }finally {
            method.releaseConnection();
        }
        return success;
    }

}
