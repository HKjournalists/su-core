package com.gettec.fsnip.fsn.service.base;

import java.util.List;

import org.springframework.ui.Model;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.vo.home.NewsColumnVO;
import com.gettec.fsnip.fsn.vo.home.NewsInformationVO;

public interface HomeService {

    /**
     * 获取食安云独家新闻
     * @param fsc 需要获取的条数
     * @return Model
     * @throws ServiceException
     * 
     * @author HuangYog
     */
    Model getFscNews(int fsc,Model model)throws ServiceException;

    /**
     * 获取滚动专区信息
     * @param roll 获取的条数
     * @return List<NewsInformationVO>
     * @throws ServiceException
     * 
     * @author HuangYog
     */
    List<NewsInformationVO> getRollNews(int roll)throws ServiceException;

    /**
     * 获取专栏模块的数据
     * @param message
     * @param columnId 专栏id
     * @return NewsColumnVO
     * @throws ServiceException
     * 
     * @author HuangYog
     */
    NewsColumnVO getMessageNews(long columnId, int message)throws ServiceException;

    /**
     * 根据新闻id加载该新闻的所有内容
     * @param newId 新闻id
     * @return NewsInformationVO
     * @throws ServiceException
     * 
     * @author HuangYog
     */
    NewsInformationVO getDefiniteNew(Long newId)throws ServiceException;

    
}
