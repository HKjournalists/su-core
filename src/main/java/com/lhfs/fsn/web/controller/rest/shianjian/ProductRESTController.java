package com.lhfs.fsn.web.controller.rest.shianjian;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNote;
import com.gettec.fsnip.fsn.service.business.EnterpriseRegisteService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.lhfs.fsn.service.business.BusinessUnitService;
import com.lhfs.fsn.service.product.ProductInstanceService;
import com.lhfs.fsn.service.product.ProductService;
import com.lhfs.fsn.service.testReport.TestReportService;
import com.lhfs.fsn.vo.TraceabilityVO;
import com.lhfs.fsn.vo.business.BussinessUnitVO;
import com.lhfs.fsn.vo.product.ProductInfoVO;
import com.lhfs.fsn.vo.product.ProductSimpleVO;
import com.lhfs.fsn.vo.product.SajBatchAndTraceabilityVO;
import com.lhfs.fsn.vo.product.SajTraceabilityVO;
import com.lhfs.fsn.vo.report.ResultToShianjianVO;

/**
 * 以下均为提供给食安监使用的接口
 * @author YongHuang
 */
@Controller
@RequestMapping("/shianjian/product")
public class ProductRESTController {
	@Autowired ProductService productService;
	@Autowired ProductInstanceService productInstanceLFService;
	@Autowired EnterpriseRegisteService enterpriseRegisteService;
	@Autowired BusinessUnitService  businessUnitService;
	@Autowired TestReportService testReportService;
	
	/**
	 * 根据产品id获取product的信息
	 * @param id
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
    @RequestMapping(method = RequestMethod.GET, value = "proload/{id}")
    public View loadProductInfoById(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp, Model model) {
        ResultVO resultVO = new ResultVO();
        try{
            ProductInfoVO productInfoVO = productService.getProductInfoAndCertById(id);
            model.addAttribute("data", productInfoVO);
        } catch (ServiceException sex) {
            resultVO.setErrorMessage(sex.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        } catch (Exception e) {
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    
    /**
     * 根据传来的产品barcode 获取product的 商品 id,名称、商标 和 图片
     * @param barcode
     * @param req
     * @param resp
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "someproduct/{barcode}")
    public View loadProductInfoByBarcode(@PathVariable String barcode, HttpServletRequest req, HttpServletResponse resp, Model model) {
        ResultVO resultVO = new ResultVO();
        try{
            Map<String,String> productInfo = productService.getProductInfoSomeByBarcode(barcode);
            List<Object> batchs = productService.getBatchForBarcode(barcode);//根据产品条形码查找相关的批次（batch）
            model.addAttribute("batchs", batchs);
            model.addAttribute("data", productInfo);
        } catch (ServiceException sex) {
            resultVO.setErrorMessage(sex.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        } catch (Exception e) {
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    
    /**
     * 根据企业id加载企业信息  包括：企业名称、企业法人、企业证照、主营商品id,name
     * @param name
     * @param req
     * @param resp
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "simplebusunit/{name}")
    public View loadBusinessUnit(@PathVariable String name, HttpServletRequest req, HttpServletResponse resp, Model model) {
        ResultVO resultVO = new ResultVO();
        try{
        	BussinessUnitVO bussinessUnitVO = enterpriseRegisteService.loadBusinessUnit(name);
        	List<ProductSimpleVO> productInfoVOList = businessUnitService.loadProductInfoByName(name);
            bussinessUnitVO.setProductList(productInfoVOList);
            model.addAttribute("data", bussinessUnitVO);
        } catch (ServiceException sex) {
            resultVO.setErrorMessage(sex.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        } catch (Exception e) {
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    
    /**
     * 通过产品id获取距离指定批次（生产日期）最近的报告
     * @param id
     * @param batch
     * @param req
     * @param resp
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "getReportByBatch/{id}/{batch}")
    public View getReportByProIdAndProDate(@PathVariable Long id,@PathVariable String batch,HttpServletRequest req, HttpServletResponse resp, Model model) {
        ResultVO resultVO = new ResultVO();
        try{
            ResultToShianjianVO  resultShianjianVO = testReportService.findByProductIdAndproductionDate(id, batch);
            model.addAttribute("data", resultShianjianVO==null?"":resultShianjianVO);
        } catch (ServiceException sex) {
            resultVO.setErrorMessage(sex.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        } catch (Exception e) {
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

    /**
     * 产品溯源接口
     * @param req
     * @param resp
     * @param batch
     * @param barcode
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = { "/getBusinessUnitTraceabilityByBatch/{barcode}" })
    public View getDetailTraceability(@PathVariable(value = "barcode") String barcode,@RequestParam("batch") String batch,HttpServletRequest req,
            HttpServletResponse resp, Model model) {
        ResultVO resultVO = new ResultVO();
        try{
    		 List<List<SajTraceabilityVO>> traceability = businessUnitService.sajTraceability(barcode, batch);//为食安监提供的 溯源
    		 List<SajBatchAndTraceabilityVO> sajBatchAndTraceabilityVO = new ArrayList<SajBatchAndTraceabilityVO>();
    		 if(traceability != null){
    			 for(List<SajTraceabilityVO> tVOlist : traceability){
    			     SajBatchAndTraceabilityVO suyuan = new SajBatchAndTraceabilityVO();
    			     List<SajTraceabilityVO> VoList = new ArrayList<SajTraceabilityVO>();
    			     suyuan.setBatch(batch);
    				 for(int i = tVOlist.size() - 1 ; i>=0 ; i--){
    					 VoList.add(tVOlist.get(i));
    					 suyuan.setSuyuan(VoList);
    				 }
    				 sajBatchAndTraceabilityVO.add(suyuan);
    			 }
    		 }
    		 model.addAttribute("data",sajBatchAndTraceabilityVO);
        }catch (Exception e) {
          e.printStackTrace();
          resultVO.setErrorMessage(e.getMessage());
          resultVO.setStatus(SERVER_STATUS_FAILED);
      }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    
    /**
     * 模糊得到时间范围的产品溯源（一个星期；一个月）
     * @param req
     * @param resp
     * @param barcode
     * @param startTime
     * @param endTime
     * @param flag 标志位 1 代表手动输入时间范围； 2代表最近一个礼拜； 3 代表最近一个月
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = { "/getTraceabilityByProductDate/{barcode}/{flag}" })
    public View getTraceabilityProductDate(HttpServletRequest req,HttpServletResponse resp,
            @PathVariable(value = "barcode") String barcode,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            @PathVariable(value = "flag") int flag, Model model) {
        ResultVO resultVO = new ResultVO();
        try {
            //1、根据时间范围和barcode 查处所有的batch
            List<String> batchList = new ArrayList<String>();
            if(flag == 1 && !startTime.equals("") && !endTime.equals("")) {
                batchList = productInstanceLFService.getProductBacth4ProductDateAndBarcode(barcode,startTime,endTime); 
            }
            if(flag == 2) {
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();        
                calendar.setTime(date);     
                calendar.set(Calendar.DATE,calendar.get(Calendar.DATE) - 7); 
                batchList = productInstanceLFService.getProductBacth4ProductDateAndBarcode(barcode,changeDate(calendar.getTime()),changeDate(date)); 
            }
            if(flag == 3) {
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();        
                calendar.setTime(date);     
                calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH) - 1); 
                batchList = productInstanceLFService.getProductBacth4ProductDateAndBarcode(barcode,changeDate(calendar.getTime()),changeDate(date)); 
            }
            //一个产品可能有多个bacth ，一个batch 对应的 产品可能有条个溯源 so...
            List<SajBatchAndTraceabilityVO> list = new ArrayList<SajBatchAndTraceabilityVO>();
            if (batchList != null) {
                for (String batch : batchList) {
                	 List<List<SajTraceabilityVO>> traceability = businessUnitService.sajTraceability(barcode, batch);//为食安监提供的 溯源
	           		 if(traceability != null){
	           			// 将查出来的溯源信息排序（倒序）
	           			 for(List<SajTraceabilityVO> tVOlist : traceability){
	           			     SajBatchAndTraceabilityVO suyuan = new SajBatchAndTraceabilityVO();
	           			     suyuan.setBatch(batch);
	           				 List<SajTraceabilityVO> voList = new ArrayList<SajTraceabilityVO>();
	           				 for(int i = tVOlist.size() - 1 ; i>=0 ; i--){
	           				     voList.add(tVOlist.get(i));
	           					suyuan.setSuyuan(voList);
	           				 }
	           				 list.add(suyuan);
	           			 }
	           		 }
                }
            }
            
            model.addAttribute("data",list);
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    /**
     * 产品溯源接口
     * @param req
     * @param resp
     * @param batch
     * @param barcode
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = { "/getBusinessUnitTraceabilityByBatchNew/{batch}/{barcode}" })
    public View getTraceabilityNew(HttpServletRequest req,
            HttpServletResponse resp,
            @PathVariable(value = "batch") String batch,
            @PathVariable(value = "barcode") String barcode, Model model) {
        ResultVO resultVO = new ResultVO();
        try{ 	
        	List<TraceabilityVO> traceability = new ArrayList<TraceabilityVO>();
        	/*1.获取交易该商品batch和该barcode的收货单*/
        	List<ReceivingNote> list =businessUnitService.getReceivingNoteByBatchAndBarcode(batch,barcode);   	
        	for(ReceivingNote receivingNote : list){
        		TraceabilityVO vo = new  TraceabilityVO();
        		/*2.获取收货企业信息*/
        		BusinessUnit directionBusiness = businessUnitService.findBusinessByOrg(receivingNote.getOrganization());
        		/*3.获取交易数量*/
        		Long count = businessUnitService.getTransactionNum(receivingNote.getRe_num(),receivingNote.getOrganization(),batch,barcode);
        		/*4.构建TraceabilityVO*/
        		vo.setResourceBusName(receivingNote.getRe_provide_num().getName());
        		vo.setDirectionBusName(directionBusiness.getName());
        		vo.setReceiveDate(receivingNote.getRe_date());
        		vo.setReceiveNum(count);       		
        		traceability.add(vo);
        	}
        	model.addAttribute("data",traceability);
        }catch (Exception e) {
          e.printStackTrace();
          resultVO.setErrorMessage(e.getMessage());
          resultVO.setStatus(SERVER_STATUS_FAILED);
      }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    /**
     * 获取所有的企业类型（行业分类）
     * @param req
     * @param resp
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getbuseinssunittype")
    public View getBuseinssUnitType(HttpServletRequest req, HttpServletResponse resp, Model model) {
        ResultVO resultVO = new ResultVO();
        try{
            List<String> busUnitType = enterpriseRegisteService.getBuseinssUnitType();
            model.addAttribute("data", busUnitType);
        } catch (ServiceException sex) {
            resultVO.setErrorMessage(sex.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        } catch (Exception e) {
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    
    /**
     * 根据企业类型 返回企业名称
     * @param page 当前页
     * @param pageSize 每页显示的条数
     * @param type 企业类型
     * @param req
     * @param resp
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getbuseinssunitbytype/{page}/{pageSize}/{type}")
    public View getBuseinssUnitByType(@PathVariable(value = "page") int page,@PathVariable(value = "pageSize") int pageSize,
            @PathVariable(value = "type") String type, HttpServletRequest req, HttpServletResponse resp, Model model) {
        ResultVO resultVO = new ResultVO();
        try{
            List<String> busUnit = enterpriseRegisteService.getBuseinssUnitByType(page,pageSize,type);
            Object total = enterpriseRegisteService.getBuseinssUnitCountByType(type);
            model.addAttribute("data", busUnit);
            model.addAttribute("total", total);
        } catch (ServiceException sex) {
            resultVO.setErrorMessage(sex.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        } catch (Exception e) {
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus(SERVER_STATUS_FAILED);
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    
    /**
     * 将时间格式的数据转为String
     * @param date
     * @return
     */
    private String changeDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(date);
        return time;
    }
   
   /**
    * 根据企业名字动态加载企业名称，模糊查询 并分页显示
    * @param page
    * @param pageSize
    * @param name
    * @param req
    * @param resp
    * @param model
    * @return
    */
   @RequestMapping(method = RequestMethod.GET, value = "/getBusinessUnitListForName/{page}/{pageSize}")
   public View getBusinessUnitListForName(@PathVariable(value = "page") int page,@PathVariable(value = "pageSize") int pageSize,
           @RequestParam(value="name") String name,HttpServletRequest req, HttpServletResponse resp, Model model) {
       ResultVO resultVO = new ResultVO();
       try{
           List<String> businessUnitName = businessUnitService.loadBusinessUnitListForName(name,page,pageSize);
           Object total = businessUnitService.loadBusinessUnitListForNameCount(name);
           model.addAttribute("data", businessUnitName);
           model.addAttribute("total", total);
       } catch (ServiceException sex) {
           resultVO.setErrorMessage(sex.getMessage());
           resultVO.setStatus(SERVER_STATUS_FAILED);
       } catch (Exception e) {
           resultVO.setErrorMessage(e.getMessage());
           resultVO.setStatus(SERVER_STATUS_FAILED);
       }
       model.addAttribute("result", resultVO);
       return JSON;
   }
   
   /**
    * 查询相关企业类型下面的 与模糊名称匹配的企业名称 ，并分页显示
    * @param page
    * @param pageSize
    * @param type
    * @param name
    * @param req
    * @param resp
    * @param model
    * @return
    */
   @RequestMapping(method = RequestMethod.GET, value = "/getbusUnitNamebytypeAndName/{page}/{pageSize}/{type}/{name}")
   public View getBuseinssUnitNameByTypeAndName(@PathVariable(value = "page") int page,@PathVariable(value = "pageSize") int pageSize,
           @PathVariable(value = "type") String type,@PathVariable(value = "name") String name,
           HttpServletRequest req, HttpServletResponse resp, Model model) {
       ResultVO resultVO = new ResultVO();
       try{
           List<String> busUnitName = enterpriseRegisteService.getBuseinssUnitNameByTypeAndName(page,pageSize,type,name);//查询相关企业类型下面的 与模糊名称匹配的企业名称
           Object total = enterpriseRegisteService.getBuseinssUnitNameCountByType(type,name);//查询相关企业类型下面的 与模糊名称匹配的企业名称 的总数
           model.addAttribute("data", busUnitName);
           model.addAttribute("total", total);
       } catch (ServiceException sex) {
           resultVO.setErrorMessage(sex.getMessage());
           resultVO.setStatus(SERVER_STATUS_FAILED);
       } catch (Exception e) {
           resultVO.setErrorMessage(e.getMessage());
           resultVO.setStatus(SERVER_STATUS_FAILED);
       }
       model.addAttribute("result", resultVO);
       return JSON;
   }
}
