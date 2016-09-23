package com.gettec.fsnip.fsn.service.member.impl;

import com.gettec.fsnip.fsn.dao.member.MemberDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.member.Member;
import com.gettec.fsnip.fsn.service.business.BusinessBrandService;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.UnitService;
import com.gettec.fsnip.fsn.service.market.MkCategoryService;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.member.MemberService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.member.MemberManageViewVO;
import com.lhfs.fsn.vo.SampleVO;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Set;


/**
 * Member service implementation
 * 
 * @author Ryan Wang
 */
@Service(value="memberService")
public class MemberServiceImpl extends BaseServiceImpl<Member, MemberDAO> 
		implements MemberService{
	@Autowired private MemberDAO memberDAO;
	@Autowired private ResourceService testResourceService;
	@Autowired private BusinessBrandService businessBrandService;
	@Autowired private UnitService unitService; 
	@Autowired private BusinessUnitService businessUnitServicee;
	@Autowired private MkCategoryService categoryService;
	@Autowired private ResourceService resourceService;
	
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public MemberDAO getDAO() {
		return memberDAO;
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public boolean checkExistBarcode(String barcode) throws ServiceException {
		try {
			return getDAO().checkExistBarcode(barcode);
		} catch (DaoException dex) {
			throw new ServiceException("", dex.getException());
		}
	}
	
	public Member eagerFindById(Long id) throws ServiceException {
		try {
			Member member = getDAO().findById(id);
			/*if(member != null){
				member.setMemberInstances(memberInstanceDAO.findMemberInstancesByPID(id));
			}*/
			return member;
		} catch (JPAException jpae) {
			throw new ServiceException("", jpae.getException());
		}
	}
	
	/**
	 * 根据条件获取条形码集合有分页处理
	 * @param condition
	 * @param page
	 * @param pageSize
	 * @return List<String>
	 * @throws ServiceException
	 * @author LongXianZhen
	 */
	@Override
	public List<String> getBarcodeListByCondition(String condition,int page ,int pageSize) throws ServiceException{
		try {
			return getDAO().getBarcodeListByCondition(condition,page,pageSize);
		} catch (DaoException dex) {
			throw new ServiceException("MemberServiceImpl.getBarcodeListByCondition()-->"+dex.getMessage(), dex.getException());
		}
	}
	
    @Override
	public Member getMemberByBarcode(String barcode) throws ServiceException {		
		try {
			return getDAO().findByBarcode(barcode);
		} catch (DaoException dex) {
			throw new ServiceException("", dex.getException());
		}
	}
	
	/**
	 * 按企业组织机构查找人员数量
	 * @author ZhangHui 2015/4/11
	 */
	@Override
	public long count(Long organization, String configure) throws ServiceException {
		try {
			if(organization == null){
				return 0;
			}
			String condition = "";
			if(configure == null || "null".equals(configure)){
				condition = " WHERE orgId = ?1";
				//如果等于configure = 0的情况下，是不需要查询条件的，因此在如下判断中，给你了一个1=1的条件来满足查询sql的常规书写
			}else{
				condition = getConfigure(configure) + " AND orgId = ?1";
			}
			/*if(condition != null) {
				condition += " and packageFlag = '0' ";
			}*/
			/* count方法是使用面向对象查询，模式给member实体使用别名e */
			condition = condition.replace("member", "e");
			return getDAO().count(condition, new Object[]{organization});
		} catch (JPAException e) {
			throw new ServiceException("MemberServiceImpl.count()-->" + e.getMessage(), e.getException());
		}
	}
	
	public long count(String configure) throws ServiceException {
		try {
			String condition = "";
			if(configure == null || "null".equals(configure)){
				//如果等于configure = 0的情况下，是不需要查询条件的，因此在如下判断中，给你了一个1=1的条件来满足查询sql的常规书写
			} else if(configure != null&&"0".equals(configure)){
			}else{
				condition = getConfigure(configure);
			}
			condition = condition.replace("member", "e");
			return getDAO().count(condition, new Object[]{});
		} catch (JPAException e) {
			throw new ServiceException("MemberServiceImpl.count()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 获取轻量级人员信息
	 * @author HCJ 2016-5-19
	 */
	@Override
	public List<MemberManageViewVO> getLightMemberVOsByPage(int page, int pageSize, Long orgId) throws ServiceException {
		try {
			return getDAO().getLightMemberVOsByPage(page, pageSize, orgId);
		} catch (DaoException e) {
			throw new ServiceException("MemberServiceImpl.getLightMemberVOsByPage()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 获取人员数量(包括引进人员)
	 * @author ZhangHui 2015/4/14
	 */
	@Override
	public long countAllMember(Long organization, String configure) throws ServiceException {
		try {
			String condition = getConfigureOfIgnoreBrand(configure);
			String condition_barnd = "";
			if(configure!=null && "".equals(condition)){
				/* 此时configure应该为品牌名称  */
				condition_barnd = getConfigure(configure);
			}
			
			return getDAO().countAllMember(condition, condition_barnd, organization);
		} catch (DaoException e) {
			throw new ServiceException("MemberServiceImpl.countAllMember()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 背景：人员新增/编辑页面
	 * 功能描述：新增人员
	 * @param organization          当前正在执行人员新增操作的父组织机构id
	 * @param current_business_name 当前正在执行人员新增操作的企业名称
	 * @param myOrganization        当前正在执行人员新增操作的企业组织机构id
	 * @param isNew 
	 * 			true  代表 新增
	 * 			false 代表 更新
	 * @throws ServiceException
	 * @author ZhangHui 2015/6/3
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveMember(Member member, String current_business_name, Long organization, boolean isNew)
				throws ServiceException {
		try {
			
			// 7. 保存人员信息
			if(isNew){
				create(member);
			}else{
				// 更新人员
				if(member.getId() == null){
					throw new Exception("参数 人员 id 为空");
				}
				Member orig_member = getDAO().findById(member.getId());
				setMemberValue(orig_member, member);
				update(orig_member);
			}
			
		} catch (ServiceException e) {
			throw new ServiceException("[ServiceException]MemberServiceImpl.createMember()-->" + e.getMessage(), e.getException());
		} catch (Exception e) {
			throw new ServiceException("[Exception]MemberServiceImpl.createMember()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 按人员id查找一条完整的人员信息
	 * @throws ServiceException 
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public Member findByMemberId(Long id,String identify) throws ServiceException {
		try {
			Member member = findById(id);
			return member;
		} catch (ServiceException sex) {
			throw sex;
		}
	}
	
	/**
	 * 按人员id删除人员信息
	 * @throws ServiceException 
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public boolean deleteMemberById(Long id) throws ServiceException {
		try {
			getDAO().deleteMemberById(id);
			return true;
		} catch (DaoException e) {
			throw new ServiceException(
					"MemberServiceImpl.deleteMemberById() 出现异常！", e);
		}
	}

	/**
	 * 更新之前，赋值操作
	 * @param orig_member
	 * @param member
	 * @throws Exception 
	 */
	private void setMemberValue(Member orig_member, Member member) throws Exception {
		if(orig_member==null || member==null){
			throw new Exception("参数为空");
		}
		
		orig_member.setName(member.getName());  // 人员姓名
		orig_member.setCredentialsType(member.getCredentialsType());//证件类型
		orig_member.setIdentificationNo(member.getIdentificationNo());  // 身份证号
		orig_member.setSex(member.getSex());//性别
		orig_member.setNation(member.getNation());//民族
		orig_member.setAddress(member.getAddress());  // 住址
		orig_member.setPosition(member.getPosition());  // 职务
		orig_member.setAppointUnit(member.getAppointUnit());//任免单位
		orig_member.setPersonType(member.getPersonType());//人员类型
		orig_member.setWorkType(member.getWorkType());//工种
		orig_member.setHealthNo(member.getHealthNo());//健康证编号
		orig_member.setIssueUnit(member.getIssueUnit());//发证单位
		orig_member.setMobilePhone(member.getMobilePhone());//手机
		orig_member.setDescription(member.getDescription());  // 简介
		orig_member.setTel(member.getTel());  // 联系电话
		orig_member.setEmail(member.getEmail());  // 邮箱
		
		/**
		 * 处理人员图片
		 */
		Set<Resource> removesHd = testResourceService.getListOfRemoves(orig_member.getHdAttachments(), member.getHdAttachments());
		orig_member.removeHdResources(removesHd);
		Set<Resource> adds = testResourceService.getListOfAdds(member.getHdAttachments());
		orig_member.addHdResources(adds);
		
		Set<Resource> removesHth = testResourceService.getListOfRemoves(orig_member.getHthAttachments(), member.getHthAttachments());
		orig_member.removeHthResources(removesHth);
		Set<Resource> addsHth = testResourceService.getListOfAdds(member.getHthAttachments());
		orig_member.addHthResources(addsHth);
		for (Resource resource : member.getHthAttachments()) {
			if(resource.getId() != null){
				testResourceService.update(resource);
			}
		}
		
		Set<Resource> removesQc = testResourceService.getListOfRemoves(orig_member.getQcAttachments(), member.getQcAttachments());
		orig_member.removeQcResources(removesQc);
		Set<Resource> addsQc = testResourceService.getListOfAdds(member.getQcAttachments());
		orig_member.addQcResources(addsQc);
		for (Resource resource : member.getQcAttachments()) {
			if(resource.getId() != null){
				testResourceService.update(resource);
			}
		}
		
		Set<Resource> removesHn = testResourceService.getListOfRemoves(orig_member.getHnAttachments(), member.getHnAttachments());
		orig_member.removeHnResources(removesHn);
		Set<Resource> addsHn = testResourceService.getListOfAdds(member.getHnAttachments());
		orig_member.addHnResources(addsHn);
		for (Resource resource : member.getHnAttachments()) {
			if(resource.getId() != null){
				testResourceService.update(resource);
			}
		}
	}
	
	
	/**
	 * 拼接sql语句
	 * @author ZhangHui 2015/4/11
	 */
	private String getConfigure(String configure) throws ServiceException{
		
		String new_configure = "";
		String filter[] = configure.split("@@");
		for(int i=0;i<filter.length;i++){
			String filters[] = filter[i].split("@");
			if(filters.length > 3){
				try {
					String config = splitJointConfigure(filters[0],filters[1],filters[2], false);
					if(config==null){
						continue;
					}
					if(i==0){
						new_configure = new_configure + " WHERE " + config;
					}else{
						new_configure = new_configure +" AND " + config;
					}
				} catch (Exception e) {
				e.printStackTrace();
				}
			}
		}
	    return new_configure;
	}
	
	/**
	 * 拼接sql语句（忽略品牌）
	 * @author ZhangHui 2015/4/11
	 */
	private String getConfigureOfIgnoreBrand(String configure) throws ServiceException{
		
		String new_configure = "";
		String filter[] = configure.split("@@");
		for(int i=0;i<filter.length;i++){
			String filters[] = filter[i].split("@");
			if(filters.length > 3){
				try {
					String config = splitJointConfigure(filters[0],filters[1],filters[2], true);
					if(config==null){
						continue;
					}
					if(i==0){
						new_configure = new_configure + " WHERE " + config;
					}else{
						new_configure = new_configure +" AND " + config;
					}
				} catch (Exception e) {
				e.printStackTrace();
				}
			}
		}
	    return new_configure;
	}
	
	/**
	 * 分割页面的过滤信息
	 * @param field
	 * @param mark
	 * @param value
	 * @param isSon 
	 * @return
	 * @throws ServiceException
	 */
	private String splitJointConfigure(String field, String mark, String value, boolean isIgnoreBrand) throws ServiceException{
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException("【Service-Error】数据筛选时，利用utf-8解码筛选条件时出现异常！",e);
		}
		if(field.equals("id")){
			return FilterUtils.getConditionStr("member.id",mark,value);
		}
		if(field.equals("name")){
			return FilterUtils.getConditionStr("member.name",mark,value);
		}
		if(!isIgnoreBrand && field.equals("brandName")){
			return FilterUtils.getConditionStr("businessBrand.name",mark,value);
		}
		if(field.equals("format")){
			return FilterUtils.getConditionStr("member.format",mark,value);
		}
		if(field.equals("barcode")){
			return FilterUtils.getConditionStr("member.barcode",mark,value);
		}
		if(field.equals("cstm")){
			return FilterUtils.getConditionStr("member.cstm",mark,value);
		}
		if(field.equals("ingredient")){
			return FilterUtils.getConditionStr("member.ingredient",mark,value);
		}
		if(field.equals("nutriStatus")){
			if("成功".indexOf(value) > -1){
				value = "1";
			} else if("失败".indexOf(value) > -1){
				value = "2";
			} else if("未计算".indexOf(value) > -1){
				value = "0";
			}
			return FilterUtils.getConditionStr("member.nutriStatus",mark,value);
		}
		return null;
	}
	
	/**
	 * 获取所有人员列表，有分页处理
	 */
	@Override
	public List<Member> getMemberListByConfigWithPage(String configure,
			int page, int pageSize) throws ServiceException {
		try {
			return getDAO().getListByPage(page, pageSize, configure.equals("null")?"":getConfigure(configure));
		} catch (JPAException jpae) {
			throw new ServiceException(jpae.getMessage(), jpae.getException());
		}
	}

	@Override
	public long allCountByConfig(String configure) throws ServiceException {
		try {
			return getDAO().count(configure.equals("null")?"":configure);
		} catch (JPAException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}

	@Override
	public List<Member> searchMemberListByName(String name)
			throws ServiceException {
				return null;
		/*try {

			return MemberTransfer.transfer(getDAO().searchMemberListByName(name));
		} catch (DaoException jpae) {
			throw new ServiceException(jpae.getMessage(), jpae.getException());
		}*/
	}

	@Override
	public long getCountByName(String name) throws ServiceException {
		try {
			return getDAO().getCountByName(name);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 按人员名称和规格查找人员集合。（外包装和内包装）
	 * @param name 人员名称
	 * @param format 人员规格
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public List<Member> getListByNameAndFormat(String name, String format) throws ServiceException {
		try {
			return getDAO().getListByNameAndFormat(name, format);
		} catch (DaoException jpae) {
			throw new ServiceException("【service-error】按人员名称和规格查找人员集合，出现异常！", jpae.getException());
		}
	}
	
	/**
	 * 按人员别名和规格查找人员集合。（外包装和内包装）
	 * @param name 人员名称
	 * @param format 人员规格
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public List<Member> getListByOtherNameAndFormat(String otherName, String format) throws ServiceException {
		try {
			return getDAO().getListByOtherNameAndFormat(otherName, format);
		} catch (DaoException jpae) {
			throw new ServiceException("【service-error】按人员别名和规格查找人员集合，出现异常！", jpae.getException());
		}
	}
	
	/**
	 * 按人员别名和规格查找人员集合。（外包装和内包装）
	 * @param name 人员名称
	 * @param format 人员规格
	 * @param isObscure
	 *        true: 人员名称模糊查询
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public List<Member> getListByOtherNameAndPDFformat(String otherName, String pdfFormat, 
			boolean isObscure) throws ServiceException {
		try {
			return getDAO().getListByOtherNameAndPDFformat(otherName, pdfFormat, isObscure);
		} catch (DaoException jpae) {
			throw new ServiceException("【service-error】按人员别名和规格查找人员集合，出现异常！", jpae.getException());
		}
	}

	/**
	 * 按热词查找人员条数。
	 * @param organization 用户组织机构id
	 * @param hotWords  热词集合
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public long countByHotWord(Long organization, String hotWord) throws ServiceException {
		String condition = " WHERE e.orgId = ?1 ";
		Object[] param = new Object[]{organization};
		if(StringUtils.isNotBlank(hotWord)&&!"null".equals(hotWord)){
			param = new Object[]{organization,hotWord};
			condition += " and (e.name like concat('%',concat(?2,'%')) or e.position like concat('%',concat(?2,'%')) or e.identificationNo like concat('%',concat(?2,'%')) ) ";
		}
		try {
			return getDAO().count(condition,param);
			//return getDAO().countByHotWord(organization, hotWord);
		} catch (JPAException jpae) {
			throw new ServiceException("【service-error】 按热词查找人员条数，出现异常！", jpae.getException());
		}
	}

	/**
	 * 按热词查找人员集合。
	 * @param organization 用户组织机构id
	 * @param hotWord  热词集合
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<Member> getListByHotWordWithPage(Long organization,
			int page, int pageSize, String hotWord) throws ServiceException {
		String condition = " WHERE e.orgId = ?1 ";
		Object[] param = new Object[]{organization};
		if(StringUtils.isNotBlank(hotWord)&&!"null".equals(hotWord)){
			param = new Object[]{organization,hotWord};
			condition += " and (e.name like concat('%',concat(?2,'%')) or e.position like concat('%',concat(?2,'%')) or e.identificationNo like concat('%',concat(?2,'%')) ) ";
		}
		try {
			List<Member> list =this.getDAO().getListByPage(page,pageSize,condition,param); //this.getDAO().getListByCondition(condition, param);
			return list;
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据组织机构查找企业本地人员
	 * @param page 
	 * @param size 
	 * @param organization 
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public List<Member> getAllLocalMember(int page, int size,Long organization) throws ServiceException{
		return null;
		/*try {
			return MemberTransfer.transfer(getDAO().getAllLocalMember(page,size,organization));
		} catch (DaoException dex) {
			throw new ServiceException("MemberServiceImpl.getAllLocalMember()-->", dex);
		}*/
	}
	
	/**
	 * 根据组织机构查找企业引进人员列表
	 * @param page 
	 * @param size 
	 * @param organization 
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public List<Member> getAllNotLocalMember(int page, int size,Long organization) throws ServiceException{
		return null;
		/*try {
			return MemberTransfer.transfer(memberDAO.getAllNotLocalMember(page,size,organization));
		} catch (DaoException dex) {
			throw new ServiceException("MemberServiceImpl.getAllNotLocalMember()-->", dex);
		}*/
	}

	/**
	 * 根据组织机构查找企业本地人员总数
	 * @param page 
	 * @param size 
	 * @param organization 
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public Long getCountOfAllLocalMember(Long organization) throws ServiceException {
		try {
			return getDAO().getCountOfAllLocalMember(organization);
		} catch (DaoException dex) {
			throw new ServiceException("MemberServiceImpl.getCountOfAllLocalMember()-->", dex);
		}
	}

	/**
	 * 根据组织机构查找企业引进人员总数
	 * @param page 
	 * @param size 
	 * @param organization 
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public Long getCountOfAllNotLocalMember(Long organization) throws ServiceException {
		try {
			return getDAO().getCountOfAllNotLocalMember(organization);
		} catch (DaoException dex) {
			throw new ServiceException("MemberServiceImpl.getCountOfAllNotLocalMember()-->" + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 根据barcode、品牌名称、企业id查找品牌id
	 * @param barcode 
	 * @param brandName 
	 * @param bussunitId 
	 * @return Member
	 * @throws ServiceException 
	 */
	@Override
	public Long findByBarcodeAndBrandNameAndBusunitId(String barcode,
			String brandName, Long bussunitId) throws ServiceException {
		try {
			return getDAO().findByBarcodeAndBrandNameAndBusunitId(barcode, brandName, bussunitId);
		} catch (DaoException dex) {
			throw new ServiceException("MemberServiceImpl.findByBarcodeAndBrandNameAndBusunitId()-->" + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 根据lims传来的JSON来保存Member
	 * @param memberVO
	 * @param organization
	 * 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean createMember(JSONObject memberVO, Long organization) {
			String brandName = memberVO.getString("businessBrand");
			Member member = new Member();
			String barcode = memberVO.getString("barcode");
			try {
				/* 人员图片信息 */
				boolean isMemberImg = memberVO.containsKey("memberImg");
				/*if(!isMemberImg){
					String memberImg = memberVO.getString("memberImg");
					if(!memberImg.equals("")||memberImg != null){
						member = testResourceService.setImgToMember(memberImg, barcode);
					}
				}
				 人员条形码
				member.setBarcode(memberVO.getString("barcode").replaceAll(" ", ""));
				 品牌 
				BusinessBrand orig_bussBrand = null;
				if(brandName==null || brandName.equals("") || brandName.equals("null")){
					orig_bussBrand = businessBrandService.findByName("--");
				}else{
					orig_bussBrand = businessBrandService.findByName(brandName);
				}
				if(orig_bussBrand == null){
					 BusinessBrand bb = new BusinessBrand();
					 bb.setName(brandName);
					 bb.setBusinessUnit(businessUnitServicee.findByName("--"));					 
					 businessBrandService.create(bb);
					 orig_bussBrand = bb;
				}
				member.setBusinessBrand(orig_bussBrand);
				 组织机构
				member.setOrganization(organization);
				 其他默认值 
				member.setCharacteristic("");
				member.setQscoreSelf(Float.valueOf("5"));
				member.setQscoreSample(Float.valueOf("5"));
				member.setQscoreCensor(Float.valueOf("5"));
				 人员类别 
				MemberCategoryInfo orig_proCategory = memberCategoryInfoService.findByNameAndCategoryIdAndFlag("其他", 400L, true);
				if(orig_proCategory == null){
					MemberCategoryInfo new_proCategory = new MemberCategoryInfo();
					new_proCategory.setName("其他");
					MemberCategory category = categoryService.findById(400L);
					new_proCategory.setCategory(category);
					new_proCategory.setCategoryFlag(true);
					new_proCategory.setDisplay("其他");
					new_proCategory.setAddition(true);
					orig_proCategory = memberCategoryInfoService.create(new_proCategory);
				}else if(orig_proCategory.isDel()){
					orig_proCategory.setDel(false);
					memberCategoryInfoService.update(orig_proCategory);
				}
				member.setCategory(orig_proCategory);
				MemberCategory pc = orig_proCategory != null ? orig_proCategory.getCategory() : null;
				member.setSecondCategoryCode(pc != null ? pc.getCode() : "1607");
				 执行标准 
				String regularity = memberVO.getString("regularity");
				member.setRegularity(memberCategoryInfoService.getRegularityByString(regularity, orig_proCategory.getCategory()));
				人员名字
				member.setName(memberVO.getString("name"));
				人员规格
				member.setFormat(memberVO.getString("format"));*/
				create(member);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
			return true;
	}
	


	/**
	 * 查询所有的条形码
	 * @return List<String>
	 * @throws ServiceException
	 * @author LongXianZhen
	 */
	@Override
	public List<String> getAllBarcode() throws ServiceException {
		try {
			return getDAO().getAllBarcode();
		} catch (DaoException dex) {
			throw new ServiceException("MemberServiceImpl.getAllBarcode()-->" + dex.getMessage(), dex.getException());
		}
		
		
	}
	/**
	 * 根据人员条形码查找人员
	 * @param barcode 条形码
	 * @author ZhaWanNeng
	 * 更新时间2015/3/17
	 */
	@Override
	public Member findByBarcode(String barcode) throws ServiceException {
		return null;
		/*try {
			return MemberTransfer.transfer(getDAO().findByBarcode(barcode));
		} catch (DaoException dex) {
			throw new ServiceException("", dex.getException());
		}*/
	}
	/**
	 * 根据名字查找人员
	 * @param sampleName 名字
	 * @author ZhaWanNeng
	 * 更新时间2015/3/17
	 */
	@Override
	public Member findByName(String sampleName) throws ServiceException {
		try {
		return getDAO().findByName(sampleName);
		} catch (DaoException dex) {
			throw new ServiceException("", dex.getException());
		}
	}

	/**
	 * 重新计算营养指标
	 * @author TangXin
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultVO recalculatedNutri(Long memberID) throws ServiceException {
		try{
			ResultVO resultVO = new ResultVO();
			if(memberID == null) {
				return resultVO;
			}
			Member orig_member = getDAO().findById(memberID);
			/*List<MemberNutrition> orit_nutris = memberNutriService.getListOfNutrisByMemberId(memberID);
			if(orit_nutris != null && orit_nutris.size() == 0){
				resultVO.setStatus("false");
				resultVO.setMessage("计算失败，当前人员无营养报告信息！");
			} else {
				orig_member.setListOfNutrition(orit_nutris);
				 计算人员营养标签指数  
				saveNutriLabel(orig_member);
				update(orig_member);
				if(orig_member.getNutriStatus() != '1'){
					resultVO.setStatus("false");
					resultVO.setMessage("计算失败，当前人员营养报告的计算结果没有对应的营养指数！");
				}
			}*/
			return resultVO;
		}catch(Exception e){
			throw new ServiceException("[service-error]:计算商品营养指标时出现异常！" + e.getMessage(), e);
		}
	}
	/**
	 * 人员总数
	 * @return Long
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@Override
	public Long memberCount() throws ServiceException {
		try {
			return getDAO().memberCount();
			} catch (DaoException dex) {
				throw new ServiceException("", dex.getException());
			}
	}
	/**
	 * 人员的营养指数排行
	 * @return Long
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	/*@Override
	public List<MemberListVo> getMemberList(Long type, int pageSize, int page)
			throws ServiceException {
		try {
			return getDAO().getMemberList(type, pageSize, page);
			} catch (DaoException dex) {
				throw new ServiceException("", dex.getException());
			}
	}*/
	/**
	 * 风险排行接口
	 * @return Long
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	/*@Override
	public List<MemberRiskVo> riskBillboard(String type, int pageSize, int page)
			throws ServiceException {
		try {
			return getDAO().riskBillboard(type, pageSize, page);
			} catch (DaoException dex) {
				throw new ServiceException("MemberServiceImpl.riskBillboard()-->"+dex.getMessage(), dex.getException());
			}
	}*/
	/**
	 * 获取人员的的一级分类的code
	 * @param name 一级分类的名称
	 * @return
	 * @throws DaoException
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@Override
	public String memberCode(String name) throws ServiceException {
		try {
			return getDAO().memberCode(name);
			} catch (DaoException dex) {
				throw new ServiceException("MemberServiceImpl.memberCode()-->"+dex.getMessage(), dex.getException());
			}
	}
	/**
	 * 获取人员的风险排行的数量
	 * @param code 一级分类的code
	 * @return
	 * @throws DaoException
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@Override
	public int countriskBill(String code) throws ServiceException {
		try {
			return getDAO().countriskBill(code);
			} catch (DaoException dex) {
				throw new ServiceException("MemberServiceImpl.countriskBill()-->"+dex.getMessage(), dex.getException());
			}
	}

	/**
	 * 根据LIMS传过来的样品信息 新增人员
	 * @param sample
	 * @param organizationID
	 * @return Member
	 * @author LongXianZhen
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public Member saveMember(SampleVO sample, Long organizationID) {
		String brandName = sample.getBusinessBrand().trim();
		String businessName=sample.getProducer().getName();
		Member member = new Member();
		String barcode = sample.getBarCode().replaceAll(" ", "");
		/*try {
			 人员图片信息 
			String memberImg = sample.getMemberImg();
			if(memberImg != null&&!memberImg.equals("")){
				member = testResourceService.setImgToMember(memberImg, barcode);
			}
			 人员条形码
			member.setBarcode(barcode);
			
			 企业 
			BusinessUnit bu=null;
			if(businessName==null||businessName.equals("")||businessName.equals("null")){
				bu=businessUnitServicee.findByName("--");
			}else{
				bu=businessUnitServicee.findByName(businessName);
			}
			if(bu==null){
				Map<String,Object> testeeMap=businessUnitServicee.saveBusinessUnit(sample.getProducer());
				bu=(BusinessUnit)testeeMap.get("business");
			}
			 品牌 
			BusinessBrand orig_bussBrand = null;
			if(brandName==null || brandName.equals("") || brandName.equals("null")){
				orig_bussBrand = businessBrandService.findByName("--");
			}else{
				orig_bussBrand = businessBrandService.findByName(brandName);
			}
			if(orig_bussBrand == null){
				 BusinessBrand bb = new BusinessBrand();
				 bb.setName(brandName);
				 if(bu==null){
					 bb.setBusinessUnit(businessUnitServicee.findByName("--"));					 
				 }else{
					 bb.setBusinessUnit(bu);
				 }
				 businessBrandService.create(bb);
				 orig_bussBrand = bb;
			}
			member.setBusinessBrand(orig_bussBrand);
			
			 组织机构
			member.setOrganization(organizationID);
			 其他默认值 
			member.setCharacteristic("");
			member.setQscoreSelf(Float.valueOf("5"));
			member.setQscoreSample(Float.valueOf("5"));
			member.setQscoreCensor(Float.valueOf("5"));
			 人员类别 
			MemberCategoryInfo orig_proCategory = memberCategoryInfoService.findByNameAndCategoryIdAndFlag("其他", 400L, true);
			if(orig_proCategory == null){
				MemberCategoryInfo new_proCategory = new MemberCategoryInfo();
				new_proCategory.setName("其他");
				MemberCategory category = categoryService.findById(400L);
				new_proCategory.setCategory(category);
				new_proCategory.setCategoryFlag(true);
				new_proCategory.setDisplay("其他");
				new_proCategory.setAddition(true);
				memberCategoryInfoService.create(new_proCategory);
			}else if(orig_proCategory.isDel()){
				orig_proCategory.setDel(false);
				memberCategoryInfoService.update(orig_proCategory);
			}
			member.setCategory(orig_proCategory);
			 执行标准 
			String regularity = sample.getRegularity();
			member.setRegularity(memberCategoryInfoService.getRegularityByString(regularity, orig_proCategory.getCategory()));
			人员名字
			member.setName(sample.getName().trim());
			人员规格
			member.setFormat(sample.getFormat());
			memberDAO.persistent(member);
			return member;
		}catch(ServiceException e){
			((Throwable) e.getException()).printStackTrace();
			return null;
		}catch(JPAException e){
			((Throwable) e.getException()).printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}*/
		return null;
	}


	/**
	 * 根据人员barcode获取人员id
	 * @author Zhanghui 2015/4/9
	 */
	@Override
	public Long getIdByBarcode(String barcode) throws ServiceException {
		try {
			return getDAO().getIdByBarcode(barcode);
		} catch (DaoException dex) {
			throw new ServiceException("MemberServiceImpl.getIdByBarcode()-->" + dex.getMessage(), dex);
		}
	}

	/**
	 * 
	 * @author Zhanghui 2015/4/9
	 */
	@Override
	public List<Member> getListByConfigure(String configure, Object[] params) throws ServiceException {
		try {
			//return getDAO().getListByCondition(configure, params);
			return getDAO().getProListByCondition(configure);
		} catch (DaoException jpae) {
			throw new ServiceException("MemberServiceImpl.getListByConfigure()-->" + jpae.getMessage(), jpae);
		}
	}


	@Override
	@Transactional
	public Member checkMember(Member member) throws ServiceException {
		return memberDAO.checkMember(member);
	}
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateSpeicalMember(long memberId,boolean isSpeicalMember){
		try {
			Member member=memberDAO.findById(memberId);
//			member.setSpecialMember(isSpeicalMember);
			memberDAO.merge(member);
		} catch (JPAException e) {
			e.printStackTrace();
		}
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean setBarcodeToQRcode(String barcode ,Long memberID,String QRStart,String QREnd){
		return this.getDAO().setBarcodeToQRcode(barcode,memberID,QRStart,QREnd );
	}

	@Override
	public List<Member> getLightMemberVOsByPage(Long orgId, String key)  throws ServiceException{
		String condition = " WHERE e.orgId = ?1 ";
		Object[] param = new Object[]{orgId};
		if(StringUtils.isNotBlank(key)){
			param = new Object[]{orgId,key};
			condition += " and (e.name like concat('%',concat(?2,'%')) or e.position like concat('%',concat(?2,'%')) or e.identificationNo like concat('%',concat(?2,'%')) ) ";
		}
		try {
			List<Member> list = this.getDAO().getListByCondition(condition, param);
			return list;
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return null;
	}


}