package com.gettec.fsnip.fsn.web.controller.rest.wdn;

import com.gettec.fsnip.fsn.model.wdn.WdnOrderInfo;
import com.gettec.fsnip.fsn.service.wdn.WdnService;
import com.gettec.fsnip.fsn.util.DataOpenPlatformUtils;
import com.gettec.fsnip.fsn.web.util.SSOUtil;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

/**
 * Created by hughxiang on 2016/1/18.
 */

@Controller
@RequestMapping(value="/wdn")
public class WdnRestService {


    private final static String SUPPORT_PHONE = "18985507103";
    private final static String SUPPORT_EMAIL = "wenxian@fsnip.com";
    private final static String SEARCH_ALL_TYPE = "全部类型";

    @Autowired
    private WdnService wdnService;
    private static final Logger LOG = Logger.getLogger(WdnRestService.class);

    @RequestMapping(method = RequestMethod.GET, value="/{typeSelectName}/{selectCode}/{keywords}/{page}/{pageSize}/{sortField}")
    public View search(HttpServletRequest request, HttpServletResponse resp, Model model,
                       @PathVariable String typeSelectName,
                       @PathVariable String selectCode,
                       @PathVariable String keywords,
                       @PathVariable int page,
                       @PathVariable int pageSize,
                       @PathVariable String sortField){
        try {
            typeSelectName = URLEncoder.encode(typeSelectName, "UTF-8");
            keywords = URLEncoder.encode(keywords, "UTF-8");
            String results = "";
            if(null != typeSelectName&&!typeSelectName.equals("0")){
                StringBuffer urlBuffer = new StringBuffer();
                urlBuffer.append("http://discovery.las.ac.cn/jsoninterface.do?action=expertSearch&expression=");
                if(!typeSelectName.equals(URLEncoder.encode(SEARCH_ALL_TYPE, "UTF-8"))){
                    urlBuffer.append("class:").append(typeSelectName).append("+");
                }
                if (selectCode.equals("title-all") && keywords.equals("0")){
                    urlBuffer.append("&start=").append(page).append("&size=").append(pageSize)
                            .append("&sortField=").append(sortField);
                } else {
                    urlBuffer.append(selectCode).append(":").append(keywords)
                            .append("&start=").append(page).append("&size=").append(pageSize)
                            .append("&sortField=").append(sortField);
                }

                results = connect(urlBuffer.toString(), "GET", "");
                results = results.replaceAll("data-source","datasource");
                JSONObject obj = JSONObject.fromObject(results);
                model.addAttribute("result", obj);
            }

        }catch (Exception e){
            LOG.error(e.getMessage());
        }

        return JSON;
    }

    @RequestMapping(method = RequestMethod.GET, value="/getUserInfo")
    public View getUserInfo(HttpServletRequest req, HttpServletResponse resp, Model model){
        try {
            AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
            String email = AccessUtils.getUserEmail().toString();
            Object phone = AccessUtils.getUserPhone().toString();
            String addr = AccessUtils.getUserOrgAddr().toString();
            model.addAttribute("user", info);
            model.addAttribute("email", email);
            model.addAttribute("phone", phone);
            model.addAttribute("addr", addr);
            model.addAttribute("status", true);
        }catch (Exception e){
            LOG.error(e.getMessage());
        }
        return JSON;
    }
  
	@RequestMapping(method = RequestMethod.GET, value = "/submitOrder")
	public View submitOrder(
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "journal", required = false) String journal,
			@RequestParam(value = "author", required = false) String author,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "volume", required = false) String volume,
			@RequestParam(value = "issue", required = false) String issue,
			@RequestParam(value = "issn", required = false) String issn,
			@RequestParam(value = "startPage", required = false) String startPage,
			@RequestParam(value = "endPage", required = false) String endPage,
			@RequestParam(value = "dataSource", required = false) String dataSource) {
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			String url = "http://dds.las.ac.cn/Reader/guizhou_fill_bill.jsp?";
			url += "username=gzytest&password=gzy";
			String email = SUPPORT_EMAIL;// AccessUtils.getUserEmail().toString();
			String phone = SUPPORT_PHONE;// AccessUtils.getUserPhone().toString();
			url += "&email=" + email + "&libcode=311001";
			String result = "";
			String str = "&title=" + enCodeUTF8(title) + "&journal="
					+ enCodeUTF8(journal) + "&author=" + enCodeUTF8(author)
					+ "&year=" + enCodeUTF8(year) + "&volume="
					+ enCodeUTF8(volume) + "&issue=" + enCodeUTF8(issue)
					+ "&issn=" + enCodeUTF8(issn) + "&startPage="
					+ enCodeUTF8(startPage) + "&endPage=" + enCodeUTF8(endPage)
					+ "&dataSource=" + enCodeUTF8(dataSource);
			String p_ = (str).replace("__", "/")
					.split("&dataSource")[0];
			result = connect(url + p_ + "&remark=''", "GET", "");
			JSONObject obj = JSONObject.fromObject(result);
			String orderId = obj.getString("orderId");

			WdnOrderInfo order = new WdnOrderInfo();
			order.setOrderId(orderId);
			order.setTitle(URLDecoder.decode(title, "UTF-8"));
			order.setJournal(URLDecoder.decode(journal.replace("None", ""),"UTF-8"));
			order.setAuthors(URLDecoder.decode(author, "UTF-8"));
			order.setYear(year);
			order.setVolume(volume);
			order.setIssue(issue);
			order.setIssn(URLDecoder.decode(issn, "UTF-8"));
			order.setDataSource(URLDecoder.decode(dataSource, "UTF-8"));
			order.setUserId(info.getUserId());
			order.setUserRealName(info.getRealUserName());
			order.setPhone(phone);
			order.setEmail(email);
			order.setApplyDate(new Date());
			order.setStatus("等待处理！");
			wdnService.saveWdnOrder(order);
			model.addAttribute("status", obj.get("illReturn"));
			boolean hasEmail = (null == AccessUtils.getUserEmail() ? false : true);
			boolean hasPhone = (null == AccessUtils.getUserPhone() ? false : true);
			// 由于大部分用户不存在电话和邮箱, 程序反向推理
			String draftMessage = null;
			String content = null;
			if (!hasEmail && !hasPhone) {
				draftMessage = "{0} 用户 {1}提交了文献申请, 国科图将发送到邮箱 " + SUPPORT_EMAIL
						+ ",收到后请转发给用户,系统暂无联系信息";
			} else if (hasEmail && !hasPhone) {
				draftMessage = "{0} 用户 {1}提交了文献申请, 国科图将发送到邮箱 " + SUPPORT_EMAIL
						+ ",收到后请转发到用户邮箱{2}";
			} else if (!hasEmail && hasPhone) {
				draftMessage = "{0} 用户 {1}提交了文献申请, 国科图将发送到邮箱 " + SUPPORT_EMAIL
						+ ",收到后请电话联系用户{2}";
			} else if (hasEmail && hasPhone) {
				draftMessage = "{0} 用户 {1}提交了文献申请, 国科图将发送到邮箱 " + SUPPORT_EMAIL
						+ ",收到后请转发到用户邮箱{2}, 联系电话{3}";
			}

			if (!hasEmail && hasPhone) {
				content = MessageFormat.format(draftMessage,
						info.getUserOrgName(), info.getUserName(),
						AccessUtils.getUserPhone());
			} else {
				content = MessageFormat.format(draftMessage,
						info.getUserOrgName(), info.getUserName(),
						AccessUtils.getUserEmail(), AccessUtils.getUserPhone());
			}
			// send message
			String message = "{\"data\":["
					+ "{\"OPERATOR_NAME\":\"超级管理员\",\"OPERATOR_ORG\":1,"
					+ "\"OPERATOR_ORG_NAME\":\"贵州省分析测试院\",\"OPERATOR_SERVICE\":\"qiye.fsnip.com\","
					+ "\"TRANSMIT_SERVICE\":\"192.168.1.1\","
					+ "\"CONTENT\":\"" + content + "\","
					+ "\"TOPHONENUMBER\":\"" + SUPPORT_PHONE + "\"" + "}]"
					+ "}";
			boolean results = DataOpenPlatformUtils.sendMessage(message);
			if (!results) {
				LOG.error("通知短信发送失败!");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
			model.addAttribute("status", false);
		}
		return JSON;
	}


    @RequestMapping(method = RequestMethod.GET, value="/getOrdersInfo")
    public View getOrdersInfo(HttpServletRequest req, HttpServletResponse resp, Model model){
        try {
            AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
            List<WdnOrderInfo> list = wdnService.getOrdersByUserId(info.getUserId());

            if(list.size()>0){
                for(WdnOrderInfo order:list){
                    String url = "http://dds.las.ac.cn/Reader/guizhou_query_order.jsp?orderId=";
                    url += order.getOrderId();
                    String results = connect(url, "GET", "");
                    if(!"null".equals(results)){
                        JSONObject obj = JSONObject.fromObject(results);
                        String transStatus = obj.getString("transStatus");
                        if(transStatus.equals("001")){
                            order.setStatus("已接收未处理");
                        }else if(transStatus.equals("002")){
                            order.setStatus("正在处理");
                        }else if(transStatus.equals("900")){
                            order.setStatus("满足");
                        }else if(transStatus.equals("800")){
                            order.setStatus("无法满足");
                        }else{
                            order.setStatus("未知状态");
                        }
                    }
                }
            }
            model.addAttribute("results", list);
            model.addAttribute("status", true);
        }catch (Exception e){
            LOG.error(e.getMessage());
        }

        return JSON;
    }
    public String enCodeUTF8(String str) throws UnsupportedEncodingException{
    	return URLEncoder.encode(str.replace("None", "''"), "UTF-8")  ;
    }
    private static String connect(String url, String method, Object param) {
        URL targetUrl = null;
        HttpURLConnection urlCon = null;
        if (param != null && !param.equals("") && param instanceof String) {
            url = (!url.endsWith("?")) ? (url + "?" + param) : (url + param);
        }
        try {
            System.out.println(url);
            targetUrl = new URL(url);
            urlCon = (HttpURLConnection) targetUrl.openConnection();
            urlCon.setRequestMethod(method);
            if (param != null && !param.equals("") && (param instanceof JSONObject || param instanceof net.sf.json.JSON)) {
                urlCon.setDoOutput(true);
                urlCon.setRequestProperty("Content-type", "application/json");
                urlCon.getOutputStream().write(param.toString().getBytes("UTF-8"));
                urlCon.getOutputStream().flush();
                urlCon.getOutputStream().close();
            }
            InputStream in = urlCon.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = rd.readLine();
            StringBuffer jsonResult = new StringBuffer();
            while (line != null) {
                jsonResult.append(line);
                line = rd.readLine();
            }
            rd.close();
            in.close();
            return jsonResult.toString();

        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            if (urlCon != null)
                urlCon.disconnect();
        }
        return null;
    }
}
