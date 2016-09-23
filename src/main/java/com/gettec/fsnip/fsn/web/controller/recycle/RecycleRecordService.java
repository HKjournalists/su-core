package com.gettec.fsnip.fsn.web.controller.recycle;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.ProductDestroyRecord;
import com.gettec.fsnip.fsn.recycle.Process_mode;
import com.gettec.fsnip.fsn.recycle.Recycle_reason;
import com.gettec.fsnip.fsn.service.product.ProductDestroyRecordService;
import com.gettec.fsnip.fsn.service.recycle.RecycleService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

/**
 * 产品回收/销毁记录查询接口
 */
@Controller
@RequestMapping("/recycle")
public class RecycleRecordService {
	
    @Autowired
    RecycleService recycleService;
    @Autowired
    ProductDestroyRecordService productDestroyRecordService;
    
    /**
     * 查询产品回收/销毁记录
     * @param model
     * @param req
     * @param resp
     * @return
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.POST, value = "/getRecords")
    public View getRecords(Model model,HttpServletRequest req,HttpServletResponse resp){
    	String type = req.getParameter("type");
    	String page = req.getParameter("page");
    	String page_number = req.getParameter("page_number");
    	String batch = req.getParameter("batch");
    	String product_name = req.getParameter("product_name");
    	String product_code = req.getParameter("product_code");
    	String problem_describe = req.getParameter("problem_describe");
    	String start_date = req.getParameter("start_date");
    	String end_date = req.getParameter("end_date");
    	String enterprise_name = req.getParameter("enterprise_name");
    	if (type == null || "".equals(type)) {
            model.addAttribute("success", 0);
            model.addAttribute("msg", "查询类别不能为空");
            return JSON;
    	}
    	int i_type;
    	try {
    		i_type = Integer.parseInt(type);
    	} catch (NumberFormatException e) {
            model.addAttribute("success", 0);
            model.addAttribute("msg", "查询类别不正确");
            return JSON;
    	}
    	if (i_type < 1 || i_type > 3) {
            model.addAttribute("success", 0);
            model.addAttribute("msg", "查询类别不正确");
            return JSON;
    	}
    	int i_page = 1;
    	if (page != null && !"".equals(page)) {
        	try {
        		i_page = Integer.parseInt(page);
        	} catch (NumberFormatException e) {
                model.addAttribute("success", 0);
                model.addAttribute("msg", "查询页码不正确");
                return JSON;
        	}
    	}
    	int i_page_number = 10;
    	if (page_number != null && !"".equals(page_number)) {
        	try {
        		i_page_number = Integer.parseInt(page_number);
        	} catch (NumberFormatException e) {
                model.addAttribute("success", 0);
                model.addAttribute("msg", "每页数据条数不正确");
                return JSON;
        	}
    	}
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	if (start_date != null && !"".equals(start_date)) {
    		try {
        		format.parse(start_date);
    		} catch (ParseException e) {
                model.addAttribute("success", 0);
                model.addAttribute("msg", "起始日期不正确");
                return JSON;
        	}
    	}
    	if (end_date != null && !"".equals(end_date)) {
    		try {
        		format.parse(end_date);
    		} catch (ParseException e) {
                model.addAttribute("success", 0);
                model.addAttribute("msg", "截止日期不正确");
                return JSON;
        	}
    	}
    	HashMap<String,String> param = new HashMap<String,String>();
    	param.put("type", type);
    	param.put("page", page);
    	param.put("page_number", page_number);
    	param.put("batch", batch);
    	param.put("product_name", product_name);
    	param.put("product_code", product_code);
    	param.put("problem_describe", problem_describe);
    	param.put("start_date", start_date);
    	param.put("end_date", end_date);
    	param.put("enterprise_name", enterprise_name);
    	Integer count = 0;
    	try {
    		count = recycleService.getRecords_count(param);
    	} catch (Exception e) {
    		e.printStackTrace();
            model.addAttribute("success", 0);
            model.addAttribute("msg", "查询数据总数时发生异常，请稍后再试");
            return JSON;
    	}
    	JSONObject data_jo = new JSONObject();
		data_jo.put("page", i_page);
		data_jo.put("page_number", i_page_number);
    	if (count == null || count == 0) {
    		//没有数据
    		data_jo.put("total_page", 0);
    		data_jo.put("total_number", 0);
            model.addAttribute("success", 1);
            model.addAttribute("data", data_jo.toString());
            return JSON;
    	}
		data_jo.put("total_page", count % i_page_number == 0 ? count/i_page_number : count/i_page_number + 1);
		data_jo.put("total_number", count);
		List datas = null;
    	try {
    		datas = recycleService.getRecords(param);
    	} catch (Exception e) {
    		e.printStackTrace();
            model.addAttribute("success", 0);
            model.addAttribute("msg", "查询数据时发生异常，请稍后再试");
            return JSON;
    	}
    	if (datas != null && !datas.isEmpty()) {
    		JSONArray data_ja = new JSONArray();
    		for (Object o : datas) {
    			Object[] data = (Object[])o;
    			BigInteger id = (BigInteger)data[0];
    			String product_name1 = (String)data[1];
    			String product_code1 = (String)data[2];
    			String batch1 = (String)data[3];
    			String number1 = (String)data[4];
    			String problem_describe1 = (String)data[5];
    			for (Recycle_reason recycle_reason : Recycle_reason.values()) {
    				if (recycle_reason.getValue().equals(problem_describe1)) {
    	    			problem_describe1 = recycle_reason.getName();
    					break;
    				}
    			}
    			Timestamp process_time = (Timestamp)data[6];
    			String process_mode = (String)data[7];
    			for (Process_mode Process_mode_e : Process_mode.values()) {
    				if (Process_mode_e.getValue().equals(process_mode)) {
    					process_mode = Process_mode_e.getName();
    					break;
    				}
    			}
    			String handle_name = (String)data[9];
    			JSONObject jo = new JSONObject();
    			jo.put("id", id);
    			jo.put("product_name", product_name1);
    			jo.put("product_code", product_code1);
    			jo.put("batch", batch1);
    			jo.put("number", number1);
    			jo.put("problem_describe", problem_describe1);
    			jo.put("process_time", format.format(process_time.clone()));
    			jo.put("process_mode", process_mode);
    			jo.put("handle_name", handle_name);
    	    	try {
    				ProductDestroyRecord productDestroyRecord = productDestroyRecordService.findById(id.longValue());
    				if(productDestroyRecord==null){
//    		    		return null;
    		    	}else{
    		    		Set<Resource> list=productDestroyRecord.getRecAttachments();
//    		    		 model.addAttribute("list", list);
    		    		 jo.put("photos", list);
    		    	}
    			} catch (ServiceException e) {
    				e.printStackTrace();
    			}
    			
    			data_ja.add(jo);
    		}
    		data_jo.put("data_list", data_ja.toString());
    	}
        model.addAttribute("success", 1);
        model.addAttribute("data", data_jo.toString());
        return JSON;
    }
    //监管接口，通过销毁记录id获取销毁证明图片
    @RequestMapping(method = RequestMethod.GET, value = "/getRecoureceByRecordId")
    public View getRecoureceByRecordId(Model model,HttpServletRequest req,HttpServletResponse resp,@RequestParam("id") long id){
    	try {
			ProductDestroyRecord productDestroyRecord=this.productDestroyRecordService.findById(id);
			if(productDestroyRecord==null){
	    		return null;
	    	}else{
	    		Set<Resource> list=productDestroyRecord.getRecAttachments();
	    		 model.addAttribute("list", list);
	    	}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return JSON;
    }
}