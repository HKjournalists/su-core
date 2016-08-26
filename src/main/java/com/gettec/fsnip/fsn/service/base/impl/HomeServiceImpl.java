package com.gettec.fsnip.fsn.service.base.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.service.base.HomeService;
import com.gettec.fsnip.fsn.util.HttpUtils;
import com.gettec.fsnip.fsn.vo.home.NewsColumnVO;
import com.gettec.fsnip.fsn.vo.home.NewsInformationVO;

@Service(value = "HomeService")
public class HomeServiceImpl implements HomeService {

    /**
     * 获取食安云独家新闻
     * @param fsc 需要获取的条数
     * @return List<NewsInformationVO>
     */
    @Override
    public Model getFscNews(int fsc,Model model)throws ServiceException {
        try {
            String url = HttpUtils.getPortalHostname()+"/fsn-portal/service/load/loadnews?newsType=4&pageSize="+fsc; //newsType=4表示食安独家
            String result = HttpUtils.send(url, "GET",null );
            JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result);
            String status =  jsonResult.getJSONObject("RESTResult").getString("status");
            if (status.equals("1")) {  //判断portal返回过来的数据是否正常
                JSONArray jsonArray = jsonResult.getJSONObject("RESTResult").getJSONArray("data");
                JSONObject news = jsonArray.getJSONObject(0);
                List<NewsInformationVO> listFsc = new ArrayList<NewsInformationVO>();
                JSONArray jsonNewsArray = news.getJSONArray("newslist");
                for (int i = 0; i < jsonNewsArray.size(); i++) {
                    NewsInformationVO nVo = new NewsInformationVO();
                    JSONObject massage = jsonNewsArray.getJSONObject(i);
                    nVo.setId(massage.getLong("id"));
                    nVo.setTitle(massage.getString("newsTitle"));
                    listFsc.add(nVo);
                }
                model.addAttribute("onlyFsc",listFsc);
            }else {
                throw new ServiceException("HomeServiceImpl-->getFscNews   portal 返回数据无效",null);
            }
            return model;
        } catch (Exception e) {
            throw new ServiceException("HomeServiceImpl-->getFscNews"+e.getMessage(),e);
        }
    }
    
    /**
     * 获取滚动专区信息
     * @param roll 获取的条数
     * @return List<NewsInformationVO>
     */
    @Override
    public List<NewsInformationVO> getRollNews(int roll)throws ServiceException {
        try {
            String url = HttpUtils.getPortalHostname()+"/fsn-portal/service/fsnservice/recommendnews/?position="+roll;
            //String url = "http://192.168.1.47:8080/fsn-portal/service/fsnservice/recommendnews/?position="+roll;
            String result = HttpUtils.send(url, "GET",null );
            JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result);
            String status =  jsonResult.getString("status");
            List<NewsInformationVO> listRolls = new ArrayList<NewsInformationVO>();
            if (status.equals("1")) {  //判断portal返回过来的数据是否正常
                JSONArray jsonArray = jsonResult.getJSONArray("recommendNewsList");
                for (int i = 0; i < jsonArray.size(); i++) {
                    NewsInformationVO nVo = new NewsInformationVO();
                    JSONObject news = jsonArray.getJSONObject(i);
                    JSONArray jsonNewsArray = news.getJSONArray("newsList");
                    JSONObject massage = jsonNewsArray.getJSONObject(0);
                    nVo.setId(massage.getLong("id"));
                    nVo.setTitle(massage.getString("newsTitle") == null ? "": massage.getString("newsTitle"));
                    nVo.setOutline(massage.getString("newsAbstract") == null ? "": massage.getString("newsAbstract"));
                    nVo.setContent(massage.getString("content") == null ? "": massage.getString("content"));
                    nVo.setImageUrls(news.getString("imgurl") == null ? "": news.getString("imgurl"));
                    listRolls.add(nVo);
                }
            }
            else {
                throw new ServiceException("HomeServiceImpl-->getRollNews ####   portal 返回数据无效",null);
            }
            return listRolls;
        } catch (Exception e) {
            throw new ServiceException("HomeServiceImpl-->getRollNews"+e.getMessage(),e);
        }
    }

    //加载专栏中的新闻
    @Override
    public NewsColumnVO getMessageNews(long columnId,int message)throws ServiceException {
        try {
            String url = HttpUtils.getPortalHostname()+"/fsn-portal/service/fsnservice/recommendnews/?position="+columnId+"&p=1&pn="+message;
            //String url = "http://192.168.1.47:8080/fsn-portal/service/fsnservice/recommendnews/?position="+columnId+"&p=1&pn="+message;
            String result = HttpUtils.send(url, "GET",null );
            JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result);
            String status =  jsonResult.getString("status");
            NewsColumnVO newColumnVO = new NewsColumnVO();
            if (status.equals("1")) { //判断portal返回过来的数据是否正常
                JSONArray jsonArray = jsonResult.getJSONArray("recommendNewsList");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject news = jsonArray.getJSONObject(i);
                    JSONArray jsonNewsArray = news.getJSONArray("newsList");
                    newColumnVO.setId(news.getLong("id"));
                    newColumnVO.setName(news.getString("title") != null ? news.getString("title") : "");
                    newColumnVO.setImageUrl(news.getString("imgurl") != null ? news.getString("imgurl") : "");
                    List<NewsInformationVO> listRolls = new ArrayList<NewsInformationVO>();
                    for (int j = 0; j < jsonNewsArray.size(); j++) {
                        NewsInformationVO nVo = new NewsInformationVO();
                        JSONObject massage = jsonNewsArray.getJSONObject(j);
                        nVo.setId(massage.getLong("id"));
                        nVo.setTitle(massage.getString("newsTitle") == null ? "": massage.getString("newsTitle"));
                        nVo.setDate(massage.getString("publishDate").equals("null")? "": publishDateFormat(Long.valueOf(massage.getString("publishDate"))));
                        listRolls.add(nVo);
                        newColumnVO.setNewsLists(listRolls);
                    }
                }
            }else {
                throw new ServiceException("HomeServiceImpl-->getMessageNews ######  portal 返回数据无效",null);
            }
            return newColumnVO;
        } catch (Exception e) {
            throw new ServiceException("HomeServiceImpl-->getMessageNews"+e.getMessage(),e);
        }
    }
    
    //格式化日期
    private String publishDateFormat(long sd){
        Date dat=new Date(sd);  
        GregorianCalendar gc = new GregorianCalendar();   
        gc.setTime(dat);  
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");  
        String sb=format.format(gc.getTime());  
        return sb;
    }
    /**
     * 根据新闻id加载该新闻的所有内容
     * @param newId 新闻id
     * @return NewsColumnVO
     * @author HuangYog
     */
    @Override
    public NewsInformationVO getDefiniteNew(Long newId) throws ServiceException {
        try {
            String url = HttpUtils.getPortalHostname()+"/fsn-portal/service/news/find/"+newId;
            String result = HttpUtils.send(url, "GET",null );
            JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result);
            String status =  jsonResult.getJSONObject("RESTResult").getString("status");
            NewsInformationVO nVo = new NewsInformationVO();
            if (status.equals("1")) {  //判断portal返回过来的数据是否正常
                JSONObject  jsonNew = jsonResult.getJSONObject("RESTResult").getJSONObject("data");
                nVo.setId(jsonNew.getLong("id"));
                nVo.setTitle(jsonNew.getString("newsTitle")==null? "":jsonNew.getString("newsTitle"));
                nVo.setOutline(jsonNew.getString("newsAbstract")==null?"":jsonNew.getString("newsAbstract"));
                nVo.setContent(jsonNew.getString("newsContent")==null?"":jsonNew.getString("newsContent"));
                nVo.setImageUrls(jsonNew.getString("newsIcon")==null?"":jsonNew.getString("newsIcon"));
            }else {
                throw new ServiceException("HomeServiceImpl-->getDefiniteNew  ######  portal 返回数据无效",null);
            }
            
            return nVo;
        } catch (Exception e) {
            throw new ServiceException("HomeServiceImpl-->getDefiniteNew"+e.getMessage(),e);
        }
    }
    
    

}
