package com.gettec.fsnip.fsn.service.market.impl;

import com.gettec.fsnip.fsn.dao.market.UpdataReportDao;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.UpdataReport;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.UpdataReportService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.util.HttpUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.lhfs.fsn.vo.report.ReportUpdateMsgVO;
import com.lhfs.fsn.vo.report.UpdataReportVO;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create Time 2015-03-25
 * @author HuangYog 
 * @email huangyong@fsnip.com
 * 
 */
@Service(value="updataReportService")
public class UpdataReportServiceImpl extends BaseServiceImpl<UpdataReport, UpdataReportDao> implements UpdataReportService {

	@Autowired private UpdataReportDao updataReportDao;
	@Autowired private ProductService productService;
	
	@Override
    public UpdataReportDao getDAO() {
        return updataReportDao;
    }
	
	/**
	 * 统计申请跟新报告对象总数
	 * @author HuangYog
	 */
    @Override
    public Long getApplyReportTimesCount(Long org,Integer status,String configure) throws ServiceException {
        try {
            return updataReportDao.getApplyReportTimesCount(getConfigure(org,status,configure));
        } catch (DaoException daoe) {
            throw new ServiceException("UpdataReportServiceImpl.getApplyReportTimesCount()-->" + daoe.getMessage(),daoe.getException());
        }
    }
    
    /**
     * @param org 组织机构
     * @param status 当前需要统计的对象的状态  0 待处理状态  1 待处理中   2 已处理状态
     * 统计申请跟新报告对象
     * @author HuangYog
     */
	@Override
	public List<UpdataReportVO> findAllForCondition(Long org,Integer status,String configure, Integer page, Integer pageSize) throws ServiceException {
		try {
		    List<UpdataReport> lists =  updataReportDao.findAllForCondition(page,pageSize,getConfigure(org,status,configure));
		    List<UpdataReportVO> updataReportVOs =  lists != null && lists.size() > 0 ? encapsulationUpdataReportInfo(lists) : null;
            return updataReportVOs;
        } catch (DaoException daoe) {
            throw new ServiceException("UpdataReportServiceImpl.findAllForCondition()-->" + daoe.getMessage(),daoe.getException());
        }
	}

	/**
	 * 将查出来的UpdataReport 信息封装到VO中
	 * @author HuangYog
	 */
	private List<UpdataReportVO> encapsulationUpdataReportInfo(List<UpdataReport> lists) {
	    List<UpdataReportVO> listVO= null;
	    if (lists != null && lists.size() > 0) {
	        listVO = new ArrayList<UpdataReportVO>();
            for (UpdataReport report : lists) {
                UpdataReportVO vo= new UpdataReportVO();
                vo.setId(report.getId());
                vo.setApplyDate(report.getApplyDate());
                vo.setApplyTimes(report.getApplyTimes());
                vo.setProductBarcode(report.getProductBarcode());
                vo.setProductName(report.getProductName());
                vo.setReportType(report.getReportType());
				vo.setProductId(report.getProductId());
                listVO.add(vo);
            }
        }
        return listVO;
    }
	
    /**
     * 封装前台grid传来的过滤信息
     * @author HuangYog
     */
	private Map<String, Object> getConfigure( Long org,Integer status,String condition){
		String new_configure = " WHERE e.organization = ?1 AND e.handleStatus = ?2";
		if(condition != null && !"null".equals(condition) && !"".equals(condition)){
			String filter[] = condition.split("@@");
			for(int i=0;i<filter.length;i++){
				String filters[] = filter[i].split("@");
				try {
					String config = splitJointConfigure(filters[0],filters[1],filters[2]);
					if(config==null){
						continue;
					}
					if(i==0){
						new_configure = new_configure + " AND " + config;
					}else{
						new_configure = new_configure +" AND " + config;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("condition", new_configure);
		map.put("params", new Object[]{org,status});
		return map;
	}
	
	/**
	 * 封装grid的过滤条件
	 * @author HuangYog
	 */
	private String splitJointConfigure(String field,String mark,String value) throws ServiceException{
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException("UpdataReportServiceImpl.splitJointConfigure()-->，利用utf-8解码筛选条件时出现异常！",e);
		}
		if(field.equals("id")){
			return FilterUtils.getConditionStr("id", mark, value);
		}
		if(field.equals("productName")){
			return FilterUtils.getConditionStr("productName",mark,value);
		}
		if(field.equals("productBarcode")){
			return FilterUtils.getConditionStr("productBarcode",mark,value);
		}
		if(field.equals("reportType")){
			return FilterUtils.getConditionStr("reportType",mark,value);
		}
		if(field.equals("applyDate")){
		    return FilterUtils.getConditionStr("applyDate",mark,value);
		}
		if(field.equals("applyTimes")){
		    return FilterUtils.getConditionStr("applyTimes",mark,value);
		}
		return null;
	}
	
    /**
     * 将状态改为正在处理中，根据id
     * @param status 状态值  0代表未处理；1代表处理中；2代表已处理
     * @author HuangYog
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void changeApplyReportStatus(Long id,Integer status) throws ServiceException {
            try {
                UpdataReport orig_reportApply = this.findById(id);
                if(orig_reportApply != null) {
                    orig_reportApply.setHandleStatus(status);
                    orig_reportApply.setApplyDate(new Date());
                    update(orig_reportApply);
                }
            } catch (ServiceException sex) {
                throw new ServiceException("UpdataReportServiceImpl.changeApplyReportStatus()-->"+sex.getMessage(),sex.getException());
            }
    }
    
    /**
     * 根据barcode和报告类型查找是否有portal申请跟新记录
     * @author HuangYog
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void changeApplyReportStatus(String barcode, String testType,Integer status)throws ServiceException {
        try {
            UpdataReport  orig_reportApply = updataReportDao.findByBarcodeAndReportType(barcode,testType);
            if (orig_reportApply != null) {
                changeApplyReportStatus(orig_reportApply.getId(),status);
            }
        } catch (DaoException e) {
        	e.printStackTrace();
            throw new ServiceException("UpdataReportServiceImpl.changeApplyReportStatus()-->"+e.getMessage(),e.getException());
        }
        
    }

    /**
     * 当产品被更新时应同步更新报告申请更新表中的产品信息
     * @author HuangYog
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void changeApplyReportProductInfo(Product product)throws ServiceException {
        try {
        	if(product==null || product.getId()==null){
        		throw new Exception("参数为空");
        	}
        	
            /* 1.查找是否有对应的记录 */
            List<UpdataReport> orig_updataReport = updataReportDao.findByProductId(product.getId());
            /* 2.更新所有存在的相关记录 */
            if(orig_updataReport != null && orig_updataReport.size() > 0) {
               for(UpdataReport updataReport: orig_updataReport) {
                   updataReport.setProductName(product.getName());
                   updataReport.setProductBarcode(product.getBarcode());
                   update(updataReport);
               }
            }
        } catch (DaoException e) {
            throw new ServiceException("[DaoException]UpdataReportServiceImpl.changeApplyReportProductInfo()-->" + e.getMessage(), e.getException());
        } catch (Exception e) {
            throw new ServiceException("[Exception]UpdataReportServiceImpl.changeApplyReportProductInfo()-->" + e.getMessage(), e);
        }
    }

    /**
     * 当报告成功发送到portal后需要向protal推送相关的报告更新消息 
     * @author HuangYog
     */
    @SuppressWarnings("unused")
    @Override
    public void sendMessageToPortal(String barcode, String testType)throws ServiceException {
        try {
            UpdataReport orig_reportApply = updataReportDao.findByBarcodeAndReportType(barcode,testType);
            if (orig_reportApply != null && orig_reportApply.getHandleStatus()==2) {
                ReportUpdateMsgVO updataReportVO = new ReportUpdateMsgVO();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String updateTime = sdf.format(orig_reportApply.getApplyDate());
                updataReportVO.setReportType(orig_reportApply.getReportType());
                updataReportVO.setProductId(orig_reportApply.getProductId());
                updataReportVO.setUpdateTime(updateTime);
                JSONObject updataReportVOJson = JSONObject.fromObject(updataReportVO);
                String url = HttpUtils.getPortalHostname()+"/fsn-portal/service/report/updateReport";
                String response = SSOClientUtil.send(url, SSOClientUtil.POST, updataReportVOJson);
                //JSONObject result = (JSONObject) JSONSerializer.toJSON(response);
            }
        } catch (DaoException e) {
            throw new ServiceException("UpdataReportServiceImpl.sendMessageToPortal()-->"+e.getMessage(),e.getException());
        } 
        
    }

    /**
     * 保存portal发来的报告更新请求
     * @author HuangYog
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void save(UpdataReportVO updataReportVO) throws ServiceException {
        try {
            Long pid = updataReportVO.getProductId();
            String reportType = updataReportVO.getReportType();
            UpdataReport updataReport = new UpdataReport();
            /* 判断update_report表中是否已存在产品id和报告类型 都匹配的对象 */
            UpdataReport orig_updataReport = updataReportDao.checkToProIdAandreportType(pid,reportType);
            //如果存在则更新
            if (orig_updataReport != null) {
                Date applyDate = new Date();
                Date orig_Date = orig_updataReport.getApplyDate();
                applyDate =  applyDate.after(orig_Date) ? applyDate:orig_Date;
                updataReport = orig_updataReport;
                updataReport.setApplyTimes(orig_updataReport.getApplyTimes()+1);
                updataReport.setApplyDate(applyDate);
                updataReport.setHandleStatus(0);//改变处理状态
                
                update(updataReport);
            }else{//如果不存在则新增
                Product orig_product =  productService.findById(pid);
                /* 判断该产品id是否有对应的产品 */
                if (orig_product == null) {
                    throw new  ServiceException("UpdataReportServiceImpl.save()--> 产品id为"+ pid +"没有对应的产品 异常",null);
                }
                updataReport.setProductId(pid);
                updataReport.setApplyDate(new Date());
                updataReport.setReportType(reportType);
                updataReport.setProductBarcode(orig_product.getBarcode());
                updataReport.setOrganization(orig_product.getOrganization()!=null?orig_product.getOrganization():-1);
                updataReport.setApplyTimes(1);
                updataReport.setProductName(orig_product.getName());
                updataReport.setHandleStatus(0);//改变处理状态
                create(updataReport);
            }
        }catch(DaoException daoe){
            throw new  ServiceException("UpdataReportServiceImpl.save()-->"+daoe.getMessage(),daoe.getException());
        }
    }
}