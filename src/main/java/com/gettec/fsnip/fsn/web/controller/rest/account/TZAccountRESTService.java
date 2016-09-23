package com.gettec.fsnip.fsn.web.controller.rest.account;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.service.account.TZAccountService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.account.AccountOutVO;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;


 /**
 * 进货台账 、批发台账、 收货台账  这三个模块的controller层
 * @author chenxiaolin
 */
@Controller
@RequestMapping("/tzAccount")
public class TZAccountRESTService extends BaseRESTService{
	
	@Autowired TZAccountService tZAccountService;
	
	/**********************************************************进货台账**************************************************************/
    /**
     *查询进货台账首页列表 
     *status 1: 表示政府查看进货台账 0：表示生产企业或供应商进货台账首页数据
     */
    @RequestMapping(method = RequestMethod.GET, value = "/checkpurchase/list/{page}/{pageSize}")
    public View loadTZReturnProduct(Model model,@PathVariable(value="page") int page,
            @PathVariable(value="pageSize") int pageSize,
            @RequestParam(value = "number", required = false) String number,
            @RequestParam(value = "licOrName", required = false) String licOrName,
            @RequestParam(value = "status", required = false) int status,
            HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
        	Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
            model = tZAccountService.loadTZReturnProduct(myOrg, page, pageSize,number,licOrName,model,status);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }

    /**
     * 新增进货时选择商品
     */
    @RequestMapping(method = RequestMethod.GET, value = "/selectBuyGoods/{page}/{pageSize}")
    public View selectBuyGoods(Model model,@PathVariable(value="page") int page,
            @PathVariable(value="pageSize") int pageSize,
            @RequestParam (value="name",required = false) String name,
            @RequestParam (value="barcode",required = false) String barcode,
            HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
        	Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
            model = tZAccountService.selectBuyGoods(myOrg, page, pageSize,name,barcode, model);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    /**
     * 查看进货和批发时加载对方商家信息
     * 
     */
    @RequestMapping(method = RequestMethod.GET, value = "/lookpurchase/{id}")
    public View lookTZPurchaseProduct(Model model,@PathVariable(value="id") long id,
    		@RequestParam(value = "type") String type,
           HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
            model = tZAccountService.lookTZPurchaseProduct(id,type,model);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    /**
     * 查看进货时加载产品详情
     * 
     */
    @RequestMapping(method = RequestMethod.GET, value = "/viewpurchse/list/{id}/{page}/{pageSize}")
    public View viewPurchaseNotSubmit(Model model,@PathVariable(value="id") long id,
    		@PathVariable(value="page") int page,
            @PathVariable(value="pageSize") int pageSize) {
        ResultVO resultVO = new ResultVO();
        try {
        	Long curOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
            model = tZAccountService.viewPurchaseNotSubmit(id,curOrg,page,pageSize,model);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    /**********************************************************批发台账**************************************************************/
    /**
     * 显示批发台账首页数据
     * status 1: 表示政府查看批发台账 0：表示生产企业或供应商批发台账首页数据
     */
    @RequestMapping(method = RequestMethod.GET, value = "/view/list/{page}/{pageSize}")
    public View viewWholeSale(Model model,@PathVariable(value="page") int page,
            @PathVariable(value="pageSize") int pageSize,
            @RequestParam(value = "number", required = false) String number,
            @RequestParam(value = "licOrName", required = false) String licOrName,
            @RequestParam(value = "status", required = false) int status,
            HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
        	Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
             model = tZAccountService.viewWholeSale(myOrg, page, pageSize,number,licOrName, model,status);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    /**
     * 新增批发台账时选择商品
     */
    @RequestMapping(method = RequestMethod.GET, value = "/selectSaleGoods/{page}/{pageSize}")
    public View selectProduct(Model model,@PathVariable(value="page") int page,
            @PathVariable(value="pageSize") int pageSize,
            @RequestParam(value="name",required=false) String name,
            @RequestParam(value="barcode",required=false) String barcode,
            HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
        	Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
            model = tZAccountService.selectSaleGoods(myOrg,name,barcode,page, pageSize, model);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    /**
     * 添加进货台账商品及信息
     * 添加批发台账商品及信息
     * type 1进货，0批发
     * status subimt 确定提交，save 保存
     */
    @RequestMapping(method = RequestMethod.POST, value = "/addwholesale/submit/{status}/{type}")
    public View submitTZWholeSaleProduct(@RequestBody AccountOutVO accountOut,
    		@PathVariable(value="status") String status,Model model,
    		@PathVariable(value="type") Integer type,
    HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
            if(accountOut.getInBusId()!=null){
            	Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
                model = tZAccountService.submitTZWholeSaleProduct(myOrg,accountOut,model,status,type);
            }
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    
    /**
     * 查看批发台账时加载产品详情
     */
    @RequestMapping(method = RequestMethod.GET, value = "/loadingSaleGoods/{page}/{pageSize}")
    public View loadingDetailGoods(Model model,@PathVariable(value="page") int page,
            @PathVariable(value="pageSize") int pageSize,
            @RequestParam(value="tzId") Long tzId,
            HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
        	Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
            model = tZAccountService.loadingDetailGoods(tzId,myOrg,page, pageSize, model);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    /**********************************************************收货台账**************************************************************/
    
    /**
     * 查询显示收货台账首页数据
     */
    @RequestMapping(method = RequestMethod.GET, value = "/viewReceipt/list/{page}/{pageSize}")
    public View viewReceiptGoods(Model model,@PathVariable(value="page") int page,
            @PathVariable(value="pageSize") int pageSize,
            @RequestParam(value = "number", required = false) String number,
            @RequestParam(value = "licOrName", required = false) String licOrName,
            HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
        	 Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
             model = tZAccountService.viewReceiptGoods(myOrg, page, pageSize,number,licOrName, model);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    /**
     * 确认收货
     */
    @RequestMapping(method = RequestMethod.POST, value = "/submitReceipt/{id}")
    public View submitReceiptGoods(Model model,@PathVariable(value="id") long tZId,@RequestBody List<Map<String,Object>> rvo,
    		HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
        	Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
            tZAccountService.submitReceiptGoods(tZId,myOrg,rvo);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    /**
     * 查看收货信息
     * 
     */
    @RequestMapping(method = RequestMethod.GET, value = "/lookReceipt/{id}")
    public View lookTZReceiptGoods(Model model,@PathVariable(value="id") long accountId) {
        ResultVO resultVO = new ResultVO();
        try {
            model = tZAccountService.lookTZReceiptGoods(accountId,model);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    /**
     * 收货查看时加载产品详情列表
     * 
     */
    @RequestMapping(method = RequestMethod.GET, value = "/loadingReceiptGoods/list/{page}/{pageSize}")
    public View viewNoReceiptGoods(Model model,@PathVariable(value="page") int page,
            @PathVariable(value="pageSize") int pageSize,@RequestParam(value="tzId",required = false) Long tzId) {
    	
        ResultVO resultVO = new ResultVO();
        try {
            model = tZAccountService.viewNoReceiptGoods(tzId,page,pageSize,model);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    /**
     * 确认删除
     */
    @RequestMapping(method = RequestMethod.GET, value = "/deleteRow/{id}/{type}")
    public View deleteGoods(Model model,@PathVariable(value="id") long tZId,@PathVariable(value="type") String type) {
        ResultVO resultVO = new ResultVO();
        try {
            tZAccountService.deleteGoods(tZId,type);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    /**
     * 确认退货
     * chenxiaolin 2015-09-18
     */
    @RequestMapping(method = RequestMethod.GET, value = "/returnOfGoods/{id}")
    public View returnOfGoods(Model model,@PathVariable(value="id") long tZId) {
        ResultVO resultVO = new ResultVO();
        try {
        	Long myOrg = Long.valueOf(AccessUtils.getUserRealOrg().toString());
            tZAccountService.returnOfGoods(tZId,myOrg,null);//null是为了让台账App调用这个方法而加的参数
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    
    
/**********************************************************监管系统首页数据**************************************************************/
    
    /**
     * 加载监管系统首页数据
     */
    @RequestMapping(method = RequestMethod.GET, value = "/fdamsDate")
    public View loadingFDAMSDate(Model model) {
        ResultVO resultVO = new ResultVO();
        try {
        	model = tZAccountService.loadingFDAMSDate(model);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    /*********************************************************显示政府专看信息中购货商和供应商************************************************************************/
    /**
     * 加载企业的基本信息
     * 进货时：购货商信息 1
     * 批发时：供应商信息 2
     * 
     */
    @RequestMapping(method = RequestMethod.GET, value = "/basicInfo/{type}")
    public View loadBusinessUnitBasicInfo(Model model,HttpServletRequest req,HttpServletResponse resp,
    		@PathVariable(value = "type") int type,
    		@RequestParam(value="tzId",required = false) Long tzId){
        ResultVO resultVO = new ResultVO();
        try {
            model = tZAccountService.loadBustBasicInfo(tzId,type, model);
        } catch (ServiceException sex) {
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            sex.printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    
    /**
     * 
     *政府查看企业发布的报告列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/zhengfu/list/{page}/{pageSize}/{busId}")
    public View loadZFReportList(Model model,@PathVariable(value="page") int page,
            @PathVariable(value="pageSize") int pageSize,
            @PathVariable(value="busId") Long busId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "batch", required = false) String batch,
            HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
            model = tZAccountService.loadZFReportList(page, pageSize,busId,name,batch,model);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    /**
     * 
     *政府查看企业注册列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/zhengfuEnter/list/{page}/{pageSize}")
    public View loadZFEnterList(Model model,@PathVariable(value="page") int page,
            @PathVariable(value="pageSize") int pageSize,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) int type,
            HttpServletRequest req,HttpServletResponse resp) {
        ResultVO resultVO = new ResultVO();
        try {
            model = tZAccountService.loadZFEnterList(page, pageSize,name,type,model);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;    
    }


     /**
      * 根据生产日期检验该生产日期所对应的报告是否已经存在且不过期
      * @param model
      * @param prodate
      * @return
      */
    @RequestMapping(method = RequestMethod.GET, value = "/checkReport")
    public View checkReport(Model model,@RequestParam(value = "prodate") String prodate,
                                        @RequestParam(value = "proId") Long proId){

        ResultVO resultVO = new ResultVO();
        try {
            model = tZAccountService.checkReport(model,prodate,proId);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }

}