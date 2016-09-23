package com.gettec.fsnip.fsn.service.member;

import java.util.List;

import net.sf.json.JSONObject;

import com.gettec.fsnip.fsn.dao.member.MemberDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.member.Member;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.member.MemberManageViewVO;
import com.gettec.fsnip.fsn.vo.sales.DetailAlbumVO;
import com.lhfs.fsn.vo.SampleVO;


public interface MemberService extends BaseService<Member, MemberDAO>{
	public boolean checkExistBarcode(String barcode) throws ServiceException;
	
	public Member eagerFindById(Long id) throws ServiceException;

	/**
	 * 根据条件获取条形码集合有分页处理
	 * @param condition
	 * @param page
	 * @param pageSize
	 * @return List<String>
	 * @throws ServiceException
	 * @author longxianzhen
	 */
	public List<String> getBarcodeListByCondition(String condition,int page,int pageSize)throws ServiceException;

	public long count(Long organization, String configure) throws ServiceException;
	public long count(String configure) throws ServiceException;
	
	public Member findByMemberId(Long id,String identify) throws ServiceException;
	
	public boolean deleteMemberById(Long id) throws ServiceException;

	public List<Member> getMemberListByConfigWithPage(String configure,
			int page, int pageSize)throws ServiceException;

	public long allCountByConfig(String configure)throws ServiceException;

	public List<Member> searchMemberListByName(String name)throws ServiceException;

	public long getCountByName(String name)throws ServiceException;

	public List<Member> getListByNameAndFormat(String name, String format) throws ServiceException;

	public List<Member> getListByOtherNameAndFormat(String name, String format) throws ServiceException;

	public List<Member> getListByOtherNameAndPDFformat(String name, String format, boolean isObscure) throws ServiceException;

	public long countByHotWord(Long organization, String hotWord) throws ServiceException;

	public List<Member> getListByHotWordWithPage(Long organization,
			int page, int pageSize, String hotWord) throws ServiceException;

	public Member getMemberByBarcode(String barcode) throws ServiceException;

	public List<Member> getAllLocalMember(int page, int size,Long organization) throws ServiceException;

	public List<Member> getAllNotLocalMember(int page, int size,Long organization) throws ServiceException;

	public Long getCountOfAllLocalMember(Long organization) throws ServiceException;

	public Long getCountOfAllNotLocalMember(Long organization) throws ServiceException;

	/**
	 * 背景：人员新增/编辑页面
	 * 功能描述：保存人员
	 * @param current_business_name 当前正在执行人员新增操作的企业名称
	 * @param organization          当前正在执行人员新增操作的企业组织机构id
	 * @param isNew 
	 * 			true  代表 新增
	 * 			false 代表 更新
	 * @throws ServiceException
	 * @author ZhangHui 2015/6/3
	 */
	public void saveMember(Member member, String current_business_name, Long organization, boolean isNew)
			throws ServiceException;

	public Long findByBarcodeAndBrandNameAndBusunitId(String barcode,
			String name, Long bussunitId) throws ServiceException;

	boolean createMember(JSONObject memberVO, Long organization);

	
	/**
	 * 查询所有的条形码
	 * @return List<String>
	 * @throws ServiceException
	 */
	public List<String> getAllBarcode()throws ServiceException;
		
	public Member findByBarcode(String barcode) throws ServiceException;
		
	public Member findByName(String sampleName) throws ServiceException;
	
	public ResultVO recalculatedNutri(Long memberID) throws ServiceException;
	
	public Long memberCount() throws ServiceException;
	
 	
	public String memberCode(String name) throws ServiceException;
	
	public int countriskBill(String code) throws ServiceException ;
	
	/**
	 * 根据人员条形码查询人员id
	 * @author ZhangHui 2015/4/10
	 */
	public Long getIdByBarcode(String barcode) throws ServiceException;

	/**
	 * 根据LIMS传过来的样品信息 新增人员
	 * @param sample
	 * @param organizationID
	 * @return Member
	 * @author LongXianZhen
	 */
	public Member saveMember(SampleVO sample, Long organizationID);


	/**
	 * 按条件查找人员
	 * @author ZhangHui 2015/4/10
	 */
	public List<Member> getListByConfigure(String configure, Object[] params) throws ServiceException;

	/**
	 * 获取轻量级人员信息
	 * @author ZhangHui 2015/4/11
	 */
	List<MemberManageViewVO> getLightMemberVOsByPage(int page, int pageSize, Long orgId) throws ServiceException;
	
	
	/**
	 * 获取人员数量(包括引进人员)
	 * @author ZhangHui 2015/4/14
	 */
	public long countAllMember(Long currentUserOrganization, String configure) throws ServiceException;


	
	/**
	 * 根据相应属性验证对象是否存在
	 * @param businessBrand
	 * @return
	 */
	public Member checkMember(Member member) throws ServiceException;
	
	public void updateSpeicalMember(long memberId,boolean isSpeicalMember);

	/**
	 *根据关键字查询人员
	 * @param orgId
	 * @param key
	 * @return
	 */
	public List<Member> getLightMemberVOsByPage(Long orgId, String key)  throws ServiceException;
	
}