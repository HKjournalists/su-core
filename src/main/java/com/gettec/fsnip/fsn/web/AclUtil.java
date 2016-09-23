package com.gettec.fsnip.fsn.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.gettec.fsnip.fsn.util.PropertyLoader;
import com.gettec.fsnip.fsn.util.RoleEnum;

public class AclUtil {
	
	private static final Logger logger = Logger.getLogger(AclUtil.class);
	
	private static final String ACL_PROPERTIES = "acl.properties";
	
	private static final String MENU_CSV = "menu.txt";

	private static final Properties acl = PropertyLoader.loadProperties(ACL_PROPERTIES);
	
	private static final Map<String, UIMenuBar> menus = new HashMap<String, UIMenuBar>();
	
	private static final Map<RoleEnum, List<UIMenuBar>> roleMenus = new HashMap<RoleEnum, List<UIMenuBar>>();
	
	static{
		try {
			//initilize menus
			List<String> lines = FileUtils.readLines(new File(AclUtil.class.getClassLoader().getResource(MENU_CSV).getFile()), "utf-8");
			if(lines != null && lines.size() > 0){
				boolean skip = true;
				UIMenuBar menu = null;
				for(String line :  lines){
					if(skip){
						skip = false;
						continue;
					}
					menu = new UIMenuBar();
					//Menu Id,Parent Menu Id,CSS Class,Text,URL,Sprite CSS Class,Content,Image URL,Encoded
					logger.info("Parsing line[" + line + "]");
					String[] colVals = StringUtils.commaDelimitedListToStringArray(line);
					if(colVals != null){
						menu.setMenuId(colVals[0]);
						if(menus.containsKey(colVals[1])){
							menu.setParent(menus.get(colVals[1]));
						}
						menu.setCssClass(colVals[2]);
						menu.setText(colVals[3]);
						menu.setUrl(colVals[4]);
						menus.put(menu.getMenuId(), menu);
					}
				}
			}
			
			//assign menus to roles according to acl
			for(RoleEnum role : RoleEnum.values()){
				String[] ac = StringUtils.commaDelimitedListToStringArray(acl.getProperty(role.getAlias()));
				roleMenus.put(role, new ArrayList<UIMenuBar>());
				UIMenuBar pm = null;
				UIMenuBar m = null;
				for(String mId : ac){
					m = new UIMenuBar();
					//BeanUtils.copyProperties(m, menus.get(mId));
					m.setMenuId(menus.get(mId).getMenuId());
					m.setParent(menus.get(mId).getParent());
					m.setCssClass(menus.get(mId).getCssClass());
					m.setText(menus.get(mId).getText());
					m.setUrl(menus.get(mId).getUrl());
					
					if(m.getParent() == null){
						roleMenus.get(role).add(m);
						pm = m;
					}else{
						if(pm.getItems() == null){
							pm.setItems(new ArrayList<UIMenuBar>());
						}
						pm.getItems().add(m);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error occurs on parsing csv file:" + MENU_CSV, e);
		}
	}
	
	public static List<UIMenuBar> getMenus(RoleEnum role){
		return roleMenus.get(role);
	}
	
	public static void main(String[] args){
		for(RoleEnum role : RoleEnum.values()){
			List<UIMenuBar> menus = getMenus(role);
			logger.info("Menus of role:" + role.getName());
			for(UIMenuBar m : menus){
				logger.info(m.getMenuId());
				for(UIMenuBar it : m.getItems()){
					logger.info(it.getMenuId());
				}
			}
		}
	}
}
