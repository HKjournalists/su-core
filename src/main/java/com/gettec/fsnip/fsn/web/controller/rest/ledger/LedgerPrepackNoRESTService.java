package com.gettec.fsnip.fsn.web.controller.rest.ledger;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.ledger.LedgerPrepackNo;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.ledger.LedgerPrepackNoService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.ledger.LedgerPrepackNoVO;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;


@Controller
@RequestMapping("/ledgerPrepackNo")
public class LedgerPrepackNoRESTService extends BaseRESTService {
	static SimpleDateFormat SDFTIME = new SimpleDateFormat("yyyy-MM-dd");
	 
	@Autowired
	private LedgerPrepackNoService ledgerPrepackNoService;
	
	@Autowired 
	private BusinessUnitService businessUnitService;
	
	/**
	 * 获得当前采购列表
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListLedgerPrepackNo/{page}/{pageSize}")
	public View getListProducer(@PathVariable(value="page")int page,
			@PathVariable(value="pageSize") int pageSize, 
			@RequestParam(value = "productName", required = false) String productName,
			@RequestParam(value = "companyName", required = false) String companyName,
			@RequestParam(value = "companyPhone", required = false) String companyPhone,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		try {
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
			long total = ledgerPrepackNoService.getLedgerPrepackNoTotal(productName, companyName, companyPhone,fromBusId);
			model.addAttribute("data",ledgerPrepackNoService.loadLedgerPrepackNo(page, pageSize, productName, companyName, companyPhone,fromBusId));
			model.addAttribute("total", total);
		} catch (DaoException e1) {
			e1.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return JSON;	
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/ledgerPrepackNoSave")
	public View save(@RequestBody LedgerPrepackNo ledgerPrepackNo,Model model,HttpServletRequest req,HttpServletResponse resp) throws ServiceException{
		ResultVO resultVO = new ResultVO();
		try {
			ledgerPrepackNoService.setResourceData(ledgerPrepackNo);
			if(ledgerPrepackNo.getId()!=null){
				ledgerPrepackNoService.update(ledgerPrepackNo);
			}else{
				Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
				Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
				ledgerPrepackNo.setQiyeId(fromBusId);
				ledgerPrepackNoService.create(ledgerPrepackNo);
			}
			model.addAttribute("data", ledgerPrepackNo);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
     * 确认删除
     */
    @RequestMapping(method = RequestMethod.GET, value = "/deleteRow/{id}")
    public View deleteGoods(Model model,@PathVariable(value="id") long tZId) {
        ResultVO resultVO = new ResultVO();
        try {
        	ledgerPrepackNoService.delete(tZId);
        	Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
        	long total = ledgerPrepackNoService.getLedgerPrepackNoTotal("", "", "",fromBusId);
        	model.addAttribute("total", total);
        }catch(ServiceException sex){
            resultVO.setStatus(SERVER_STATUS_FAILED);
            resultVO.setSuccess(false);
            resultVO.setErrorMessage(sex.getMessage());
            ((Throwable) sex.getException()).printStackTrace();
        } catch (DaoException e) {
			e.printStackTrace();
		}
        model.addAttribute("result", resultVO);
        return JSON;    
    }
    
    /**
     * 查看
     */
    @RequestMapping(method = RequestMethod.GET, value = "/findLedgerPrepackNo/{id}")
    public View findWasteDisposa(Model model,@PathVariable(value="id") long id) {
    	ResultVO resultVO = new ResultVO();
    	try {
    		LedgerPrepackNo ledgerPrepackNo = ledgerPrepackNoService.findById(id);
    		LedgerPrepackNoVO vo  = new LedgerPrepackNoVO();
    		vo.setId(ledgerPrepackNo.getId());
			vo.setProductName(ledgerPrepackNo.getProductName());
			vo.setQiyeId(ledgerPrepackNo.getQiyeId());
			try {
				vo.setPurchaseTime(ledgerPrepackNo.getPurchaseTime() != null ? SDFTIME.format(SDFTIME.parse(ledgerPrepackNo.getPurchaseTime().toString())) : "");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			vo.setAlias(ledgerPrepackNo.getAlias());
			vo.setStandard(ledgerPrepackNo.getStandard());
			vo.setNumber(ledgerPrepackNo.getNumber());
			vo.setCompanyName(ledgerPrepackNo.getCompanyName());
			vo.setCompanyPhone(ledgerPrepackNo.getCompanyPhone());
			vo.setCompanyAddress(ledgerPrepackNo.getCompanyAddress());
			vo.setSupplier(ledgerPrepackNo.getSupplier());
			vo.setLicResource(ledgerPrepackNo.getLicResource());
			vo.setQsResource(ledgerPrepackNo.getQsResource());
			vo.setDisResource(ledgerPrepackNo.getDisResource());
			model.addAttribute("data",vo);
			
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    	model.addAttribute("result", resultVO);
		return JSON;
    }
}
