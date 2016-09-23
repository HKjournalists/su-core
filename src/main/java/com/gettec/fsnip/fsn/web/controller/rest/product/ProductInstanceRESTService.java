package com.gettec.fsnip.fsn.web.controller.rest.product;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import java.io.File;
import java.io.FileOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.View;
import com.gettec.fsnip.fsn.dao.business.BusinessUnitDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.service.product.ProductInstanceService;
import com.gettec.fsnip.fsn.web.controller.RESTResult;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;

 /**
 * ProductInstance REST Service API
 * 
 * @author Ryan Wang
 */
@Controller
@RequestMapping("/product")
public class ProductInstanceRESTService extends BaseRESTService{
	@Autowired protected BusinessUnitDAO businessUnitDAO;

	@RequestMapping(method = RequestMethod.GET, value = "product-instance/{id}")
	public ProductInstance get(@PathVariable Long id) {
		try {
			ProductInstance productInstance = productInstanceService.findById(id);
			return productInstance;
		} catch (ServiceException sex) {
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "product-instance")
	public RESTResult<ProductInstance> create(@RequestParam("model") ProductInstance productInstance) {
		try {
			RESTResult<ProductInstance> result = null;
			productInstanceService.create(productInstance);
			result = new RESTResult<ProductInstance>(RESTResult.SUCCESS, productInstance);
			result.setSuccess(true);
			return result;
		} catch (ServiceException sex) {
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	@RequestMapping(method = RequestMethod.POST, value = "product-instance/image/{id}")
	public ProductInstance upload(MultipartHttpServletRequest request,
			@PathVariable Long id) throws Exception {
		String path = request.getRealPath("/image/product");
		MultipartFile file = request.getFile("imageFile");
		byte[] bytes =  file.getBytes();
		String fileName = file.getOriginalFilename();
		/*fileName = fileName.substring(fileName.indexOf("."));
		fileName = id + fileName;*/
		fileName = id + ".JPG";
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
		fos.write(bytes);
		fos.close();
		return null;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "product-instance")
	public RESTResult<ProductInstance> update(@RequestParam("model") ProductInstance productInstance) {
		try {
			RESTResult<ProductInstance> result = null;
			productInstanceService.update(productInstance);
			result = new RESTResult<ProductInstance>(RESTResult.SUCCESS, productInstance);
			result.setSuccess(true);
			return result;
		} catch (ServiceException sex) {
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "product-instance/{id}")
	public RESTResult<ProductInstance> delte(@PathVariable("id") Long id) {
		try {
			RESTResult<ProductInstance> result = null;
			ProductInstance productInstance = productInstanceService.findById(id);
			productInstanceService.delete(id);
			result = new RESTResult<ProductInstance>(RESTResult.SUCCESS, productInstance);
			return result;
		} catch (ServiceException sex) {
			return null;
		}
	}
	
	@Autowired
	protected ProductInstanceService productInstanceService;
	
	/**
	 * 根据产品barcode和当前登录企业ID查找是否有质检报告
	 * @param barcode 条形码
	 * @param producerId 企业ID
	 * @return 产品实例
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/isHasReport/{barcode}")
	public View hasReport(@PathVariable(value="barcode") String productId,HttpServletRequest req, HttpServletResponse resp, Model model) {
		try{
			Long userOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			Long producerId = businessUnitDAO.getIdByOrganization(userOrganization);//通过用户组织机构查找企业id
			ProductInstance instance = productInstanceService.findByBarcodeAndProducerId(productId,producerId);
			if(instance==null){
				model.addAttribute("result", false);
			}else{
				model.addAttribute("result", true);
			}	     
		}catch(Exception e){
			e.printStackTrace();
		}
		return JSON;
	}
}
