package com.gettec.fsnip.fsn.dao.member;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.member.Member;
import com.gettec.fsnip.fsn.vo.member.MemberManageViewVO;
import com.gettec.fsnip.fsn.vo.sales.DetailAlbumVO;

public interface MemberDAO extends BaseDAO<Member>{
	public List<Member> findMembers(String name, Long businessUnitId, Long businessBrandId, List<Long> producerId, Long memberGroupId, int pageSize, int page);

	public Member findByBarcode(String barcode) throws DaoException;

	/**
	 * 根据条件获取条形码集合有分页处理
	 * @param condition 查询条件 为all时查询所有
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws DaoException
	 */
	public List<String> getBarcodeListByCondition(String condition,int page,int pageSize) throws DaoException;

	public long countByCondition(Map<String, Object> map) throws DaoException;

	public List<Member> getListOfMemberByConditionWithPage(
			Map<String, Object> map, int page, int pageSize) throws DaoException;

	public List<Member> getListOfMemberByConditionOfSonWithPage(
			Map<String, Object> map, int page, int pageSize) throws DaoException;

	public List<Member> searchMemberListByName(String name)throws DaoException;

	public long getCountByName(String name)throws DaoException;
	/**
	 * 根据条件（人员名称或条形码）分页查询某个企业的人员列表
	 * @param bu 生产企业
	 * @param page 第几页
	 * @param pageSize 每页显示条数
	 * @param memberName 人员名称
	 * @param barcode 条形码
	 * @return List<Member>
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	public List<Member> getProListByBusiness(BusinessUnit bu, int page,
			int pageSize, String memberName, String barcode)throws DaoException;
	/**
	 * 根据条件查询某个企业下人员总数
	 * @param organizationId 企业组织机构ID
	 * @param memberName 人员名称
	 * @param barcode 条形码
	 * @return Long
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	public Long getMemberStaCountByConfigure(Long organizationId,
			String memberName, String barcode)throws DaoException;

	public List<Member> getListByNameAndFormat(String name, String format) throws DaoException;

	public List<Member> getListByOtherNameAndFormat(String otherName,
			String format) throws DaoException;

	public List<Member> getListByOtherNameAndPDFformat(String otherName,
			String pdfFormat, boolean isObscure) throws DaoException;

	public long countByHotWord(Long organization, String hotWord) throws DaoException;

	public List<Member> getListByHotWordWithPage(Long organization,
			String hotWord, int page, int pageSize) throws DaoException;
	
	public List<Member> getListByStorageInfo(Long organization) throws DaoException;
	
	public List<Member> getListMemberByIds(List<Long> ids);

	public List<Member> getAllNotLocalMember(int page, int size,Long organization) throws DaoException;

	public List<Member> getAllLocalMember(int page, int size,Long organization) throws DaoException;

	public Long getCountOfAllLocalMember(Long organization) throws DaoException;

	public Long getCountOfAllNotLocalMember(Long organization) throws DaoException;

	public Long findByBarcodeAndBrandNameAndBusunitId(String barcode,
			String brandName, Long bussunitId) throws DaoException;

	List<Member> getListByBarcode(String barcode) throws DaoException;

	/**
	 * 查询所有条形码
	 * @return List<String>
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	public List<String> getAllBarcode()throws DaoException;
	public Member findByName(String sampleName) throws DaoException;
	
	/**
	 * 根据组织机构ID查询一个企业下的所有人员数量
	 * @param organization 企业组织机构ID
	 * @return Long
	 * @author LongXianZhen
	 */
	public Long getAllProCountByOrganization(Long organization)throws DaoException;

	
	public Long memberCount() throws DaoException;
	
//	public List<MemberListVo> getMemberList(Long type,int pageSize,int page) throws DaoException;
//	
//	public List<MemberRiskVo> riskBillboard(String type,int pageSize,int page) throws DaoException;
	
	public String memberCode(String name) throws DaoException;
	
	public int countriskBill(String code) throws DaoException ;


	/**
	 * 根据人员条形码获取人员id
	 * @author ZhangHui 2015/4/10
	 */
	public Long getIdByBarcode(String barcode) throws DaoException;

	public List<MemberManageViewVO> getLightMemberVOsByPage(int page,int pageSize, Long orgId) throws DaoException;
	
	/**
	 * 经销商只能加载出自己的人员条形码
	 * @author HuangYog
	 * Create date 2015/04/13
	 */
	public List<String> getAllBarcode(Long myOrg)throws DaoException ;

	/**
	 * 获取轻量级人员信息(包括引进人员)
	 * @author ZhangHui 2015/4/14
	 */
//	List<MemberManageViewVO> getAllLightMemberVOsByPage(int page,
//			int pageSize, String condition, String condition_barnd,
//			Long organization, Long fromBusId, boolean isDel)
//			throws DaoException;

	/**
	 * 获取轻量级人员数量(包括引进人员)
	 * @author ZhangHui 2015/4/14
	 */
	long countAllMember(String condition, String condition_barnd,
			Long organization) throws DaoException;

	boolean checkExistBarcode(String barcode) throws DaoException;
	
	public List<Member> getmemberList(int pageSize,int page) throws DaoException;

	List<DetailAlbumVO> getMemberAlbums(Long organization, int page, int pageSize, String cut) throws DaoException;

	/**
	 * 判断人员组织机构是否能修改
     * @author LongXianZhen	2015/05/06
	 */
	public boolean judgeMemberOrgModify(Long organization)throws DaoException;

	/**
	 * 功能描述：根据人员条形码查找一条已经被生产企业认领的人员信息（如果没有被认领，则返回null）
     * @author ZhangHui 2015/06/04
	 * @throws DaoException 
	 */
	public Member findByBarcodeOfHasClaim(String barCode) throws DaoException;

	/**
	 * 功能描述：检查人员有无被生产企业认领
	 * @return  true  代表已经被生产企业认领
	 * 			false 代表没有没生产企业认领
	 * @throws DaoException 
     * @author ZhangHui 2015/06/05
	 */
	public boolean checkClaimOfMember(Long id) throws DaoException;
	
	/**
	 * 根据人员id查询人员轻量级分装
	 * @param memberId
	 * @author longxianzhen 2015/08/03
	 */
//	public MemberManageViewVO findByMemberManageViewVOByProId(Long memberId)throws DaoException;
	
	/**
	 * 根据人员id集合查找人员集合 
	 * @author longxianzhen 2015/08/07
	 */
	public List<Member> getProListByCondition(String configure)throws DaoException;
	
	
	/**
	 * 根据相应属性验证对象是否存在
	 * @param businessBrand
	 * @return
	 */
	public Member checkMember(Member member) throws ServiceException;
	/**
	 * 查询企业下的人员信息
	 * @param businessId 企业ID
	 * @param memberName 人员名称
	 * @param barcode   人员条形码
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @param page     
	 * @param pageSize
	 * author:wubiao
	 * date : 2015.12.17 16:05
	 * @return
	 */
//	public List<MemberStaVO> getMemberStaListByConfigureData(Long businessId,
//			String memberName, String barcode, String startDate,
//			String endDate, int page, int pageSize);
	/**
	 * 查询企业下的人员信息
	 * @param businessId 企业ID
	 * @param memberName 人员名称
	 * @param barcode   人员条形码
	 * author:wubiao
	 * date : 2015.12.17 16:05
	 * @return
	 */
	public Long getMemberStaCountByConfigureData(Long businessId,
			String memberName, String barcode);
	
	
	/**
	 * 建立人员条形码和二维码id对应关系
	 * @param barcode
	 * @param QRNum
	 * @author liuyuanjing
	 * date:2015-12-26
	 */
	public boolean setBarcodeToQRcode(String barcode ,Long memberID,String QRStart,String QREnd);
	
	
	/**
	 * 删除人员条形码与二维码的对应关系
	 * @param id
	 * @return
	 * @throws DaoException
	 * @author lyj  2016-01-07
	 */
	public boolean deleteBarcodeToQRcode(Long id)throws DaoException;
	
	/**
	 * 删除人员
	 * @param id
	 * @return
	 * @throws DaoException
	 * @author HCJ  2016-5-20
	 */
	public boolean deleteMemberById(Long id)throws DaoException;
	
	
}

