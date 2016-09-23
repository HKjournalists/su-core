package com.gettec.fsnip.fsn.common;


import java.util.Map;

import org.springframework.ui.Model;

import com.gettec.fsnip.sso.client.util.AccessUtils;


public class BaseController{
	
	
	private static final boolean SERVER_STATUS_SUCCESS=true;
	private static final boolean SERVER_STATUS_FAILED=false;
	
	public static final String  SERVER_CREATE_SUCCESS="添加成功";
	public static final String  SERVER_CREATE_ERROR="添加失败";
	
	public static final String  SERVER_UPDATE_SUCCESS="更新成功";
	public static final String  SERVER_UPDATE_ERROR="更新失败";
	
	
	public static final String  SERVER_DELETE_SUCCESS="删除成功";
	public static final String  SERVER_DELETE_ERROR="删除失败";
	
	
	public void readResult(Map<String,Object> map, Model model){
		model.addAttribute("list", map.get("list"));
		model.addAttribute("type", "read");
		model.addAttribute("counts", map.get("total"));
	}
	
	
	public Long getUserOrganizationId(){
		return Long.valueOf( AccessUtils.getUserOrg().toString());
	}
	
	
	

	public void createResult(boolean flag,Model model){
		if(flag){
			model.addAttribute("status", SERVER_STATUS_SUCCESS);
			model.addAttribute("message", SERVER_CREATE_SUCCESS);
		}else{
			model.addAttribute("status", SERVER_STATUS_FAILED);
			model.addAttribute("message", SERVER_CREATE_ERROR);
		}
		model.addAttribute("type", "create");
	}
	
	
	public void updateResult(boolean flag,Model model){
		if(flag){
			model.addAttribute("status", SERVER_STATUS_SUCCESS);
			model.addAttribute("message", SERVER_UPDATE_SUCCESS);
		}else{
			model.addAttribute("status", SERVER_STATUS_FAILED);
			model.addAttribute("message", SERVER_UPDATE_ERROR);
		}
		model.addAttribute("type", "update");
	}
	
	
	public void deleteResult(boolean flag,Model model){
		if(flag){
			model.addAttribute("status", SERVER_STATUS_SUCCESS);
			model.addAttribute("message", SERVER_DELETE_SUCCESS);
		}else{
			model.addAttribute("status", SERVER_STATUS_FAILED);
			model.addAttribute("message", SERVER_DELETE_ERROR);
		}
		model.addAttribute("type", "delete");
	}
	
	
	public void deleteResult(Map<String,Object> map,Model model){
		if((Boolean) map.get("status")){
			model.addAttribute("status", SERVER_STATUS_SUCCESS);
			model.addAttribute("message", SERVER_DELETE_SUCCESS);
		}else{
			model.addAttribute("status", SERVER_STATUS_FAILED);
			model.addAttribute("message", map.get("message"));
		}
		model.addAttribute("type", "delete");
	}
	
	
	public static String getServerCreateSuccess() {
		return SERVER_CREATE_SUCCESS;
	}
	public static String getServerCreateError() {
		return SERVER_CREATE_ERROR;
	}
	public static String getServerUpdateSuccess() {
		return SERVER_UPDATE_SUCCESS;
	}
	public static String getServerUpdateError() {
		return SERVER_UPDATE_ERROR;
	}
	public static String getServerDeleteSuccess() {
		return SERVER_DELETE_SUCCESS;
	}
	public static String getServerDeleteError() {
		return SERVER_DELETE_ERROR;
	}
	
	

	
	
	
	
	
	
}
