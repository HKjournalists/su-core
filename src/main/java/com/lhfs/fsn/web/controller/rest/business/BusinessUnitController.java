package com.lhfs.fsn.web.controller.rest.business;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_SUCCESS;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_HOT_BUSINESSUNIT;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_HOT_BUSUNIT_TOTAL;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_RENHUAI_BAIJIU;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_RENHUAI_BAIJIU_TOTAL;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Element;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.Certification;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.service.market.MkCategoryService;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.product.ProductStdCertificationService;
import com.gettec.fsnip.fsn.service.test.TestPropertyService;
import com.gettec.fsnip.fsn.service.test.TestResultService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.business.BusinessUnitLIMSVO;
import com.gettec.fsnip.fsn.vo.product.EnterpriseVo;
import com.gettec.fsnip.fsn.vo.product.ProductReportVo;
import com.lhfs.fsn.cache.EhCacheFactory;
import com.lhfs.fsn.service.business.BusinessUnitService;
import com.lhfs.fsn.service.product.ProductService;
import com.lhfs.fsn.util.CharUtil;
import com.lhfs.fsn.vo.PlusInfoVO;
import com.lhfs.fsn.vo.ScoreInfoVO;
import com.lhfs.fsn.vo.business.Business2PortalVO;
import com.lhfs.fsn.vo.business.BusinessAndProVO;
import com.lhfs.fsn.vo.business.BusinessAndProductVO;
import com.lhfs.fsn.vo.business.BusinessAndPros2PortalVO;
import com.lhfs.fsn.vo.business.BusinessResultVO;
import com.lhfs.fsn.vo.business.BusinessVOWda;
import com.lhfs.fsn.vo.business.BussinessBaseInfoVO;
import com.lhfs.fsn.vo.business.EnterpriseColumn2PortalVO;
import com.lhfs.fsn.vo.product.Product2EnterpriseColumnVO;
import com.lhfs.fsn.vo.product.ProductInfoVO;
import com.lhfs.fsn.vo.product.ProductInfosVO;
import com.lhfs.fsn.vo.product.ProductSimpleVO;
import com.lhfs.fsn.web.controller.RESTResult;

@Controller
@RequestMapping("/portal/businessunit")
public class BusinessUnitController {
	@Autowired BusinessUnitService businessUnitLFService;
	@Autowired ProductService productService;
	@Autowired private TestResultService testResultService;
	@Autowired private ResourceService resourceService;
	@Autowired private ProductStdCertificationService productStdCertificationService;
	@Autowired private TestPropertyService testPropertyService;
	@Autowired private MkCategoryService mkCategoryService; // 一级，二级产品分类
	
	/**
     * 获取热门企业
     * @param page
     * @param pageSize
     * @param req
     * @param resp
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/getHotBusinessUnit/{page}/{pageSize}")
       public View getListOfBusinessUnit(@PathVariable(value="page") int page,@PathVariable(value="pageSize") int pageSize,
               @RequestParam(value="busId",required=false) Long busId, //获取指定id的企业
               @RequestParam(value="id",required=false) String busIds, //去除包含此id的企业
               HttpServletRequest req, HttpServletResponse resp, Model model) {
           ResultVO resultVO = new ResultVO();
           try{
        	   long total = 0L;
        	   List<Long> listOfHotBusinessUnit = null;
        	   Element hotBusiness_element = (Element) EhCacheFactory.getCacheElement(CACHE_HOT_BUSINESSUNIT + "_" +page +
        			   (busId==null?"":busId)+ busIds);
	           if(hotBusiness_element == null){
	        	   if(busId==null){
	        		   total = businessUnitLFService.countOfHotBusinessUnit();
	        	   }
	               listOfHotBusinessUnit = businessUnitLFService.getListOfHotBusinessUnitWithPage(page, pageSize, busId, busIds);
	               EhCacheFactory.getInstance().getCache().put(new Element(CACHE_HOT_BUSINESSUNIT + "_" + page + 
	            		   (busId==null?"":busId)+ busIds, listOfHotBusinessUnit));
	               EhCacheFactory.put(CACHE_HOT_BUSUNIT_TOTAL, total);
	           } else {
	        	   Element total_element = (Element) EhCacheFactory.getCacheElement(CACHE_HOT_BUSUNIT_TOTAL);
	        	   total = Long.parseLong(total_element.getObjectValue().toString());
	        	   listOfHotBusinessUnit = (List<Long>) hotBusiness_element.getObjectValue();
	           }
               model.addAttribute("data", listOfHotBusinessUnit);
               model.addAttribute("total", total);
           } catch (ServiceException sex) {
               resultVO.setErrorMessage(sex.getMessage());
               resultVO.setStatus(SERVER_STATUS_FAILED);
               ((Throwable) sex.getException()).printStackTrace();
           } catch (Exception e) {
               resultVO.setErrorMessage(e.getMessage());
               resultVO.setStatus(SERVER_STATUS_FAILED);
           }
           model.addAttribute("result", resultVO);
           return JSON;
     }
    
    /**
     * 保存来自lims的企业信息
     * @param busUnitVO
     * @param model
     * @return
     */
    @SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST ,value = "/saveBusUnitOfLims")
	public View saveBusUnitOfLims(HttpServletRequest req, HttpServletResponse resp, Model model){
    	ResultVO resultVO = new ResultVO();
		try {	
		    StringBuffer jsonResult = new StringBuffer();         
            InputStream in = req.getInputStream();         //req 为HttpServletRequest
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = rd.readLine();
            while (line != null) {
                jsonResult.append(line);
                line = rd.readLine();
            }
            rd.close();
            in.close();
            JSONObject jsonVO = JSONObject.fromObject(jsonResult.toString());
            BusinessUnitLIMSVO busUnitLimsVO =(BusinessUnitLIMSVO) jsonVO.toBean(jsonVO, BusinessUnitLIMSVO.class);
			resultVO = businessUnitLFService.saveLimsBusUnitInfo(busUnitLimsVO);
		} catch (ServiceException sex) {
            resultVO.setErrorMessage(sex.getMessage());
            resultVO.setSuccess(false);
            ((Throwable)sex.getException()).printStackTrace();
        } catch (Exception e) {
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setSuccess(false);
            e.printStackTrace();
        }
        model.addAttribute("result", resultVO);
		return JSON;
	}
    
    /**********提供给fdams监管系统的《移动巡查》接口**********
     * 根据企业名字动态加载企业名称，模糊查询
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getBusinessUnitListForName")
    public View getBusinessUnitListForName(@RequestParam(value="name") String name,
            HttpServletRequest req, HttpServletResponse resp, Model model) {
        ResultVO resultVO = new ResultVO();
        try{
            List<String> businessUnitName = businessUnitLFService.loadBusinessUnitListForName(name);
            model.addAttribute("data", businessUnitName);
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
    
    /**********提供个fdams监管系统的《移动巡查》接口**********
     * 根据企业营养执照号动态加载全营业执照号，模糊查询
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getBusinessUnitListForlicenseNo")
    public View getBusinessUnitListForlicenseNo(@RequestParam(value="licenseNo") String licenseNo,
            HttpServletRequest req, HttpServletResponse resp, Model model) {
        ResultVO resultVO = new ResultVO();
        try{
            List<String> businessUnitlicenseNo = businessUnitLFService.loadBusinessUnitListForlicenseNo(licenseNo);
            model.addAttribute("data", businessUnitlicenseNo);
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
     *  监管系统获取来源于超市没有证照的企业信息
     * @param busId
     * @param page
     * @param pageSize
     * @param model
     * @return
     */
  	@RequestMapping(method = RequestMethod.GET ,value = "/getMarketBusiness/{busId}/{page}/{pageSize}")
  	public View getMarketBusiness(@PathVariable("busId") Long busId,@PathVariable("page") int page,
  			@PathVariable("pageSize") int pageSize,Model model){
  		ResultVO resultVO = new ResultVO();
  		try {			
  			List<BusinessVOWda> listBusVO = businessUnitLFService.getListOfMarketByMarketIdWithPage(busId, page, pageSize);
  			model.addAttribute("data", listBusVO);
  		} catch (ServiceException sex) {
  			resultVO.setStatus(SERVER_STATUS_FAILED);
  			resultVO.setErrorMessage(sex.getMessage());
  		}
  		model.addAttribute("result", resultVO);
  		return JSON;
  	}
  	
  	/**********提供个fdams监管系统的《移动巡查》接口**********
     * 根据经营主体或(和)营业执照号模糊查询商品
     * Author:cxl
     * @param name
     * @param licenseNo
     * @param page
     * @param pageSize
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getBusinessUnitListForlicenseNo/{page}/{pageSize}")
    public View getProductListByNameAndLicenseNo(@PathVariable(value="page") int page,@PathVariable(value="pageSize") int pageSize,
    		@RequestParam(value="name") String name,@RequestParam(value="licenseNo") String licenseNo,HttpServletRequest req, HttpServletResponse resp, Model model) {
        ResultVO resultVO = new ResultVO();
        try{
            List<ProductInfoVO> list= businessUnitLFService.getBusinessUnitListByName(page,pageSize,name,licenseNo);
            long totalSize=productService.getCountByBusNameOrLisNo(name,licenseNo);
            model.addAttribute("totalSize", totalSize);
            model.addAttribute("data", list==null?null:list);
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
	 * 查询指定类型的企业类别
	 * @param organization
	 * @param page
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET ,value = "/getListEnterpriseByTypeWithPage/{type}/{page}/{pageSize}")
	public View getListEnterpriseByTypeWithPage(@PathVariable String type, @PathVariable int page, @PathVariable int pageSize,
			@RequestParam("name") String name,@RequestParam("organization") String organization,Model model){
		ResultVO resultVO = new ResultVO();
		try{
			long counts = 0L;
			List<BusinessVOWda> listBusVO = null;
			name = URLDecoder.decode(name, "UTF-8");
			if(type!=null){
				if(type.equals("ltqy")) type="流通企业";
				else if(type.equals("scqy")) type="生产企业";
				counts = businessUnitLFService.countByType(type,name,organization);
				listBusVO = businessUnitLFService.getListEnterpriseByTypeWithPage(type, page, pageSize,name,organization);
			}
			model.addAttribute("counts",counts);
			model.addAttribute("data", listBusVO== null ? new ArrayList<BusinessVOWda>() : listBusVO);
		}catch(Exception sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setMessage(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	} 
	
	/**
	 * 加载相关企业类型下的企业和产品，按报告数量多到少排序
	 * @param productCount 需要加载的产品个数
	 * @param page 
	 * @param pagesize
	 * @param type 企业类型
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 * 
	 * @author HuangYog
	 */
	 @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.GET, value = "/loadBusinessUnitAndProduct/{productCount}/{page}/{pagesize}")
	    public View loadBusinessUnitAndProduct(@PathVariable Long productCount,@PathVariable int page,@PathVariable int pagesize, 
	    		@RequestParam("type") String type,HttpServletRequest req, HttpServletResponse resp, Model model) {
	        ResultVO resultVO = new ResultVO();
	        try{
	            long count = 0L;
	            List<Business2PortalVO> business2PortalVOs = null;
	            Element business2Portal_element = (Element) EhCacheFactory.getCacheElement(CACHE_RENHUAI_BAIJIU + "_" +page + pagesize + productCount+type);
	            if(business2Portal_element == null){
	                count = businessUnitLFService.loadBusinessUnitCount(type,null);
	                List<Business2PortalVO> bussinessUnitVO = businessUnitLFService.loadBusinessUnit(type,page,pagesize,null);
	                business2PortalVOs = new ArrayList<Business2PortalVO>();
	                for(Business2PortalVO bus: bussinessUnitVO){
	                    List<ProductSimpleVO> productInfoVOList = businessUnitLFService.loadProductInfoByOrganization(productCount,bus.getOrganization());
	                    bus.setProductList(productInfoVOList);
	                    business2PortalVOs.add(bus);
	                }
	                EhCacheFactory.getInstance().getCache().put(new Element(CACHE_RENHUAI_BAIJIU + "_" +page + pagesize + productCount+type, business2PortalVOs));
	                EhCacheFactory.put(CACHE_RENHUAI_BAIJIU_TOTAL, count);
	            }else{
	                Element total_element = (Element) EhCacheFactory.getCacheElement(CACHE_RENHUAI_BAIJIU_TOTAL);
	                count = Long.parseLong(total_element.getObjectValue().toString());
	                business2PortalVOs = (List<Business2PortalVO>) business2Portal_element.getObjectValue();
	            }
	            model.addAttribute("data", business2PortalVOs);
	            model.addAttribute("count", count);
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
	  * 通过关键字搜索企业
	  * @param keyword
	  * @return
	  */
	 @RequestMapping(method = RequestMethod.GET ,value = "/businessunitlists")
		public RESTResult<BusinessUnit> lists2(@RequestParam("queryParam") String keyword){
			try {			keyword = CharUtil.changeURLCode(keyword, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return businessUnitLFService.uploadSearch2(keyword, 01);
		}
	 /**
	  * portal接口：名特优食品专栏接口
	  *  @author longxianzhen 2015/06/25
	  */
	 @RequestMapping(method = RequestMethod.GET, value = "/enterpriseColumn")
		public View getEnterpriseColumn( 
				@RequestParam int pageSize,
				@RequestParam int page,
				@RequestParam int proSize,
				@RequestParam String type,
				@RequestParam(required=false) String keyword,
				HttpServletRequest request,Model model) {
			ResultVO resultVO = new ResultVO();
			try {
				List<EnterpriseColumn2PortalVO> businessList = new ArrayList<EnterpriseColumn2PortalVO>();
				Long count=0L;
				if("仁怀市白酒生产企业".equals(type)){//处理仁怀白酒企业类型
					count = businessUnitLFService.loadBusinessUnitCount(type,keyword);
	                List<Business2PortalVO> bussinessUnitVO = businessUnitLFService.loadBusinessUnit(type,page,pageSize,keyword);
	                for(Business2PortalVO bus: bussinessUnitVO){
	                    List<Product2EnterpriseColumnVO> productInfoVOList = businessUnitLFService.getProductInfoByOrganization(new Long(proSize),bus.getOrganization());
	                    /* 封装返回的企业数据 */
	                    EnterpriseColumn2PortalVO en2Vos=new EnterpriseColumn2PortalVO(bus.getId(),bus.getName(),bus.getLogo(),bus.getAbout(),
	                    		bus.getWebsite(),bus.getCountOfReport(),bus.getCountOfProduct(),productInfoVOList);
	                    businessList.add(en2Vos);
	                }
				}else{//处理茶专栏
					List<EnterpriseVo> businessTeaUnit = testResultService.getBusinessTeaUnit(pageSize, page, proSize,keyword);
					count= new Long(testResultService.countBusinessTeaUnit(keyword));
					for(EnterpriseVo bus: businessTeaUnit){
						List<Product2EnterpriseColumnVO> pro2EnVOs=new ArrayList<Product2EnterpriseColumnVO>();
						for(ProductReportVo pVo:bus.getProductList()){
							String proImg=null;
							 /*处理产品图片*/
					        List<Resource> imgList = resourceService.getProductImgListByproId(pVo.getId());//查找产品图片集合
							//为了兼容老数据如果资源表里没有产品图片则从产品表imgUrl字段中取值
							if(imgList==null||imgList.size()==0){
								if(pVo.getImgUrl() != null){
									String[] imgUrlListArray = pVo.getImgUrl().split("\\|");
									for(String url:imgUrlListArray){
										Resource re=new Resource();
										re.setUrl(url);
										imgList.add(re);
									}
								}
							}
							if(imgList.size()>0){
								proImg=imgList.get(0).getUrl();
							}
							List<Certification> proCerts=productStdCertificationService.getListOfStandCertificationByProductId(pVo.getId());
							/* 封装返回的产品数据 */
							Product2EnterpriseColumnVO p2EnVO=new Product2EnterpriseColumnVO(
	                    			pVo.getId(),pVo.getName(),proImg,new Long(pVo.getCountProReport()),proCerts);
	                    	pro2EnVOs.add(p2EnVO);
	                    }
						 /* 封装返回的企业数据 */
						EnterpriseColumn2PortalVO en2Vos=new EnterpriseColumn2PortalVO(bus.getId(),bus.getName(),bus.getLogo(),bus.getAbout(),
	                    		bus.getWebsite(),bus.getCountOfReport(),bus.getCountOfProduct(),pro2EnVOs);
	                    businessList.add(en2Vos);
					}
				}
				model.addAttribute("data", businessList);
				model.addAttribute("count", count);
				resultVO.setStatus(SERVER_STATUS_SUCCESS);
			} catch (Exception sex) {
				resultVO.setErrorMessage(sex.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				resultVO.setSuccess(false);
				sex.printStackTrace();
			}
			model.addAttribute("result", resultVO);
			return JSON;
		}
	 
		/**
		 * 
		 * @author lxz
		 */
	    @RequestMapping(method = RequestMethod.POST, value = "/getFarmingBusiness")
		    public View getBusinessAndProductByBuPros(@RequestBody BusinessAndProVO  buProVO ,HttpServletRequest req, 
		    										HttpServletResponse resp, Model model) {
		        ResultVO resultVO = new ResultVO();
		        try{
		        	List<BusinessAndProductVO> buPros=buProVO.getBuPros();
		            List<BusinessAndPros2PortalVO> business2PortalVOs = businessUnitLFService.getBusinessAndProductByBuPros(buPros);
		            model.addAttribute("data", business2PortalVOs);
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
	     * 政府专看的获取报告信息接口
	     * @author chenxiaolin 2015-09-28 
	     * @param id
	     * @return
	     */
		@RequestMapping(method = RequestMethod.GET, value = "getreport/{id}")
		public TestResult get(@PathVariable Long id) {
			try {
				TestResult testResult = testResultService.findByTestResultId(id);
				if(testResult.getSample()!=null && testResult.getSample().getProduct()!=null &&
						testResult.getSample().getProduct().getCategory()!=null &&
						testResult.getSample().getProduct().getCategory().getCategory() !=null) {

					String fristCategoryCode = testResult.getSample().getProduct().getCategory().getCategory().getCode();
					//保存一级菜单
					fristCategoryCode = fristCategoryCode.substring(0,2);
					ProductCategory fristCategory = mkCategoryService.findCategoryByCode(fristCategoryCode);
					testResult.getSample().getProduct().getCategory().getCategory().setFristCategory(fristCategory);
				}
				List<TestProperty> testPropertyList = testPropertyService.findByReportId(testResult.getId());
				if(testPropertyList!=null){
					testResult.setTestProperties(testPropertyList);
				}
				return testResult;
			} catch (ServiceException sex) {
				return null;
			}
		}

		/**
		 * 更加组织机构代码查询企业基本信息，资质信息(获取企业及资质信息)
		 * @param organization
		 * @param req
		 * @param resp
		 * @param model
		 * @return
		 */
		@RequestMapping(method = RequestMethod.GET, value = "get_enterprise_info")
		public View getEnterpriseInfo(@RequestParam(value="organization",required=false) Long organization,HttpServletRequest req, 
				HttpServletResponse resp, Model model) {
			try {
				BusinessResultVO  data = businessUnitLFService.getOrganizationBussinessData(organization);;
				model.addAttribute("data", data);
				model.addAttribute("flag", true);
			} catch (Exception e) {
				model.addAttribute("flag", false);
				e.printStackTrace();
			}
			return JSON;
		}

		/**
		 * 更加组织机构代码查询(获取企业产品及报告信息)
		 * @param organization
		 * @param req
		 * @param resp
		 * @param model
		 * @return
		 */
		@RequestMapping(method = RequestMethod.GET, value = "get_product_info")
		public View getProductInfo(@RequestParam(value="organization",required=false) String organization,
				@RequestParam(value="product_name",required=false) String product_name,
				@RequestParam(value="product_barcode",required=false) String product_barcode,
				HttpServletRequest req, 
				HttpServletResponse resp, Model model) {
			try {
				List<ProductInfosVO>  data = businessUnitLFService.getLikeProductInfosVO(organization,product_name,product_barcode);
				model.addAttribute("data", data);
				model.addAttribute("flag", true);
			} catch (Exception e) {
				model.addAttribute("flag", false);
				e.printStackTrace();
			}
			return JSON;
		}
		/**
		 * 获取评分指标数据
		 * @param organization
		 * @param req
		 * @param resp
		 * @param model
		 * @return
		 */
		@RequestMapping(method = RequestMethod.GET, value = "get_score_info")
		public View getScoreInfo(@RequestParam(value="product_barcode",required=false) String product_barcode,
				@RequestParam(value="start_date",required=false) String start_date,
				@RequestParam(value="end_date",required=false) String end_date,
				HttpServletRequest req, 
				HttpServletResponse resp, Model model) {
			try {
				ScoreInfoVO  scoreInfo = businessUnitLFService.getScoreInfoData(product_barcode,start_date,end_date);
				PlusInfoVO  plusInfo = businessUnitLFService.getPlusInfoData(product_barcode,start_date,end_date);
				model.addAttribute("score_info", scoreInfo);
				model.addAttribute("plus_info", plusInfo);
				model.addAttribute("flag", true);
			} catch (Exception e) {
				model.addAttribute("flag", false);
				e.printStackTrace();
			}
			return JSON;
		}

		
}
