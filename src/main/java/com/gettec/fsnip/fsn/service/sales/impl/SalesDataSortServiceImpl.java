package com.gettec.fsnip.fsn.service.sales.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.sales.SalesDataSortDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.Certification;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.EnterpriseRegiste;
import com.gettec.fsnip.fsn.model.business.ProductionLicenseInfo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.BusinessCertification;
import com.gettec.fsnip.fsn.model.sales.SalesCase;
import com.gettec.fsnip.fsn.model.sales.SalesDataSort;
import com.gettec.fsnip.fsn.model.sales.SalesResource;
import com.gettec.fsnip.fsn.model.sales.SortField;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.business.EnterpriseRegisteService;
import com.gettec.fsnip.fsn.service.business.ProductionLicenseService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.BusinessCertificationService;
import com.gettec.fsnip.fsn.service.sales.SalesCaseService;
import com.gettec.fsnip.fsn.service.sales.SalesDataSortService;
import com.gettec.fsnip.fsn.service.sales.SalesResourceService;
import com.gettec.fsnip.fsn.util.sales.SalesUtil;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.DataSortVO;
import com.gettec.fsnip.fsn.vo.sales.SortFieldValueVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 销售资料排序 serviceimpl
 * @author tangxin 2015-05-07
 */

@Service(value="salesDataSortService")
public class SalesDataSortServiceImpl extends
		BaseServiceImpl<SalesDataSort, SalesDataSortDAO> implements
		SalesDataSortService {
	
	@Autowired SalesDataSortDAO salesDataSortDAO;
	@Autowired EnterpriseRegisteService enterpriseRegisteService;
	@Autowired BusinessUnitService businessUnitService;
	@Autowired BusinessCertificationService businessCertificationService;
	@Autowired SalesResourceService salesResourceService;
	@Autowired SalesCaseService salesCaseService;
	@Autowired protected ProductionLicenseService productionLicenseService;

	/**
	 * 分页获取排序后或未排序的产品相册(备注:产品相册的sort_field_id=6)
	 * @param sort 值为 true 时获取已经排序的产品
	 * @author tangxin 2015-05-07
	 */
	@Override
	public List<SortFieldValueVO> getSortProductAlbum(Long organization, boolean sort, int page,int pageSize) 
			throws ServiceException {
		try{
			List<SortFieldValueVO> sortProductVO = getDAO().getSortProductAlbum(organization, sort, page, pageSize);
			return sortProductVO;
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}
	
	/**
	 * 分页获取排序后或未排序的企业掠影相册
	 * @author tangxin 2015-05-09
	 */
	@Override
	public List<SortFieldValueVO> getSortBusinesAlbum(Long organization, int page,int pageSize) 
			throws ServiceException {
		try{
			return getDAO().getBusAlbumsWithPage(organization, page, pageSize);
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}
	
	/**
	 * 分页获取排序后或未排序的企业销售案例相册
	 * @author tangxin 2015-05-09
	 */
	@Override
	public List<SortFieldValueVO> getSortSalesCase(Long organization, int page,int pageSize) 
			throws ServiceException {
		try{
			return getDAO().getSalesCaseWithPage(organization, page, pageSize);
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}
	
	/**
	 * 分页查询已排序企业三证、生产许可证、荣誉证 图片
	 * @author tangxin 2015-05-09
	 */
	@Override
	public List<SortFieldValueVO> getSanZhengWithPage(Long organization, int page, int pageSize) throws ServiceException {
		try{
			List<SortFieldValueVO> sanZhengVO = getDAO().getSanZhengWithPage(organization, page, pageSize);
			return sanZhengVO;
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}catch(Exception e){
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	/**
	 * 根据组织机构获取企业注册信息
	 * @author tangxin 2015-05-09
	 */
	private EnterpriseRegiste findEnterprise(Long organization){
		try{
			/**
			 * 根据组织结构获取企业注册时的id和企业id
			 * idMap 中的键值对：
			 * enterpriseId： 注册表中对应的企业id
			 * businessId: 企业表（business_unit）中的企业id
			 */
			Map<String, Long> idMap= getDAO().findEnterpriseIdAndBusIdByOrganization(organization);
			if(idMap != null){
				return enterpriseRegisteService.findById(idMap.get("enterpriseId"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 对已经排序和未排序的认证信息进行筛选
	 * @param sanZhengVO 已排序的证照信息集合，如果为空时表示用户未进行过排序操作，
	 * @author tangxin 2015-05-09
	 */
	@Override
	public List<SortFieldValueVO> filterSanZhengVO(List<SortFieldValueVO> sanZhengVO, Long organization) throws Exception{
		// 获取企业信息
		BusinessUnit busunit = businessUnitService.findByOrganization(organization);
		EnterpriseRegiste registBus = null;
		/**
		 * boolean 变量说明
		 * orgsort：组织机构排序的标记，值为true时，表示组织机构已经被选中排序
		 * licesort：营业执照证排序的标记，值为true时，表示营业执照证已经被选中排序
		 * taxsort：税务登记证排序的标记，值为true时，表示税务登记证已经被选中排序
		 */
		boolean orgsort = false,licesort = false,taxsort = false;
		/**
		 * String 变量说明
		 * qsNoStr：已排序的QS号集合，两个QS号直接使用符号‘;’分割，如：QS1111 1111 1111;QS2222 2222 2222
		 * busCertIdStr:已排序的认证信息id集合，两个id直接使用符号‘;’分割，如：1;2
		 */
		String qsNoStr = "",busCertIdStr = "";
		if(sanZhengVO == null){
			// 如果已排序的企业证照信息为null，则创建一个新的实体，避免空指针异常
			sanZhengVO = new ArrayList<SortFieldValueVO>();
		}
		// 遍历已排序的企业证照集合
		for(SortFieldValueVO vo : sanZhengVO){
			/**
			 * sortFieldId 排序信息所属的类型
			 * 1  排序的信息为组织机构信息
			 * 2 排序的信息为营业执照证
			 * 3 排序的信息为税务登记证
			 * 4 排序的信息为生产许可证
			 * 5 排序信息为企业认证信息（企业普通认证和荣誉认证） 
			 */
			Long fid = vo.getSortFieldId();
			if(fid == null) continue;
			if(fid.equals(1L)){
				// 组织机构证已排序，将标记设置为true
				orgsort = true;
			}else if(fid.equals(2L)){
				// 营业执照证已排序，将标记设置为true
				licesort = true;
			}else if(fid.equals(3L)){
				// 税务登记证已排序，将标记设置为true
				taxsort = true;
			}else if(fid.equals(4L)){
				// 生产许可证已排序，叠加QS号，当排序的信息为生产许可证时，name存储的就是QS号
				qsNoStr += vo.getName()+";";
			}else if(fid.equals(5L)){
				// 企业认证已排序，叠加认证id
				busCertIdStr += vo.getId()+";";
			}
		}
		// 如果组织机构信息还没排序，则查询未排序的组织机构信息并添加到返回结果集中
		if(!orgsort){
			if(registBus == null) registBus = findEnterprise(organization);
			// 从企业注册实体 EnterpriseRegiste 中获取组织机构信息，封装成 SortFieldValueVO
			SortFieldValueVO tvo = createSortFieldVOByOrg(registBus);
			if(tvo != null) {
				sanZhengVO.add(tvo);
			}
		}
		// 如果营业执照信息还没排序，则查询未营业执照信息并添加到返回结果集中
		if(!licesort){
			if(registBus == null) registBus = findEnterprise(organization);
			// 从企业注册实体 EnterpriseRegiste 中获取营业执照信息，封装成 SortFieldValueVO
			SortFieldValueVO tvo = createSortFieldVOByLice(registBus);
			if(tvo != null) {
				sanZhengVO.add(tvo);
			}
		}
		// 如果税务登记证信息还没排序，则查未排序的税务登记证信息并添加到返回结果集中
		if(!taxsort){
			if(registBus == null) registBus = findEnterprise(organization);
			// 从企业注册实体 EnterpriseRegiste 中获取营业执照信息，封装成 SortFieldValueVO
			SortFieldValueVO tvo = createSortFieldVOByTaxReg(registBus);
			if(tvo != null) {
				sanZhengVO.add(tvo);
			}
		}
		// 对未排序的qs信息重新封装并添加到返回结果集中 
		List<SortFieldValueVO> listQsVO = filterQsInfo(qsNoStr, busunit);
		if(listQsVO != null) sanZhengVO.addAll(listQsVO);
		// 对未排序的企业认证信息重新封装并添加到返回结果集中 
		List<SortFieldValueVO> listCertVO = filterCertInfo(busCertIdStr, busunit);
		if(listCertVO != null) sanZhengVO.addAll(listCertVO);
		return sanZhengVO;
	}
	
	/**
	 * 迭代资源，返回集合中id最大的资源对象
	 * 相册展示时，需要展示最新的图片资源
	 * @author tangxin 2015-05-15
	 */
	private Resource IteratorResRetLast(Set<Resource> reSet){
		if(reSet == null || reSet.size() < 1){
			return null;
		}
		// 定义一个返回的 Resource
		Resource result = null;
		// 定义个存储 最大资源id的变量
		Long maxId = 0L;
		// 遍历资源集合，找到最大的id的资源
		for(Resource res : reSet){
			if(res == null) continue;
			if(maxId < res.getId()){
				maxId = res.getId();
				result = res;
			}
		}
		// 返回id最大的资源
		return result;
	}
	
	/**
	 * 如果组织机构还没排序，重新封装并添加到返回结果集中
	 * @author tangxin 2015-05-09
	 */
	private SortFieldValueVO createSortFieldVOByOrg(EnterpriseRegiste registBus){
		if(registBus == null){
			return null;
		}
		// 获取最后上传的组织机构资源，即id最大的资源
		Resource res = IteratorResRetLast(registBus.getOrgAttachments());
		// 如果资源为空，则不进行封装
		if(res == null) {
			return null;
		}
		// 将组织机构信息封装成 SortFieldValueVO
		return new SortFieldValueVO(res.getId(),"组织机构证",1L,1L,registBus.getId(),-1,null,res.getUrl(), registBus.getOrganizationNo(),null,null);
	}
	
	/**
	 * 如果营业执照信息还没排序，重新封装并添加到返回结果集中
	 * @author tangxin 2015-05-09
	 */
	private SortFieldValueVO createSortFieldVOByLice(EnterpriseRegiste registBus){
		if(registBus == null){
			return null;
		}
		// 获取最后上传的营业执照资源，即id最大的资源
		Resource res = IteratorResRetLast(registBus.getLicAttachments());
		// 如果资源为空，则不进行封装
		if(res == null) {
			return null;
		}
		// 将组营业执照信息封装成 SortFieldValueVO
		return new SortFieldValueVO(res.getId(),"营业执照",2L,2L,registBus.getId(),-1,null,res.getUrl(), registBus.getLicenseNo(),null,null);
	}
	
	/**
	 * 如果税务登记证信息还没排序，重新封装并添加到返回结果集中
	 * @author tangxin 2015-05-09
	 */
	private SortFieldValueVO createSortFieldVOByTaxReg(EnterpriseRegiste registBus){
		if(registBus == null){
			return null;
		}
		// 获取最后上传的税务登记证资源，即id最大的资源
		Resource res = IteratorResRetLast(registBus.getTaxRegAttachments());
		// 如果资源为空，则不进行封装
		if(res == null) {
			return null;
		}
		// 将组税务登记证信息封装成 SortFieldValueVO
		return new SortFieldValueVO(res.getId(),"税务登记证",3L,3L,registBus.getId(),-1,null,res.getUrl(),null,null,null);
	}
	
	/**
	 * 对未排序的qs信息重新封装并添加到返回结果集中
	 * @author tangxin 2015-05-09
	 * @throws ServiceException 
	 */
	private List<SortFieldValueVO> filterQsInfo(String qsNoStr, BusinessUnit busunit) throws ServiceException{
		if(busunit == null){
			return null;
		}
		// 空指针处理
		qsNoStr = (qsNoStr == null ? "" : qsNoStr);

		/**
		 * 获取企业的详细的生产许可证信息
		 * @author ZhangHui 2015/6/3
		 */
		List<ProductionLicenseInfo> qsSet = productionLicenseService.getListByBusId(busunit.getId());
		if(qsSet == null || qsSet.size() < 1){
			return null;
		}
		// 创建一个排序的信息VO
		List<SortFieldValueVO> listVO = new ArrayList<SortFieldValueVO>();
		for(ProductionLicenseInfo qsIns : qsSet){
			/**
			 * qsNoStr 已排序的QS号集合,封装未排序的生产许可证信息时，应该不包含已排序的QS信息
			 */
			if(qsIns == null || qsNoStr.contains(qsIns.getQsNo())) continue;
			Set<Resource> reSet = qsIns.getQsAttachments();
			if(reSet == null || reSet.size() < 1) continue;
			Resource res = reSet.iterator().next();
			// 创建排序信息的VO，并添加到List集合中
			listVO.add(new SortFieldValueVO(res.getId(),qsIns.getQsNo(),4L,4L,busunit.getId(),-1,null,res.getUrl(), qsIns.getQsNo(),null,null));
		}
		return listVO;
	}
	
	/**
	 * 对未排序的企业认证信息重新封装并添加到返回结果集中
	 * @author tangxin 2015-05-09
	 */
	private List<SortFieldValueVO> filterCertInfo(String busCertIdStr, BusinessUnit busunit){
		if(busunit == null){
			return null;
		}
		// 空指针处理
		busCertIdStr = (busCertIdStr == null ? "" : busCertIdStr);
		// 企业认证信息
		List<BusinessCertification> listCerts = null;
		try {
			// 或企业认证信息集合
			listCerts = businessCertificationService.getListOfCertificationByBusinessId(busunit.getId());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		// 当前企业下没有认证信息，返回null
		if(listCerts == null || listCerts.size() < 1){
			return null;
		}
		// 定义并创建排序信息VO集合
		List<SortFieldValueVO> listVO = new ArrayList<SortFieldValueVO>();
		for(BusinessCertification cert : listCerts){
			Certification stdCert = cert.getCert();
			/**
			 * busCertIdStr 已排序的认证信息id,封装未排序的认证信息时，应不包含已排序的认证
			 */
			if(stdCert == null || busCertIdStr.contains(cert.getId().toString())) continue;
			Resource res = cert.getCertResource();
			if(stdCert == null || res == null) continue;
			// 创建排序信息的VO，并添加到List集合中
			listVO.add(new SortFieldValueVO(res.getId(),stdCert.getName(),5L,5L,cert.getId(),-1,null,res.getUrl(), null,null,null));
		}
		return listVO;
	}
	

	/**
	 * 根据fieldTypeId 获取指定的排序资料集合
	 * @author tangxin 2015-05-10
	 */
	public List<SalesDataSort> getListByFieldType(Long organizaton, Long fieldTypeId) throws ServiceException {
		try{
			return getDAO().getListByFieldType(organizaton, fieldTypeId);
		}catch(Exception e){
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	/**
	 * 封装产品排序的对象
	 * @author tangxin 2015-05-10
	 */
	private SalesDataSort createDataSortBySotFieldVO(SortFieldValueVO pvo, AuthenticateInfo info,
			Map<String,Long> ids, boolean isNew){
		if(pvo == null || info == null || ids == null){
			return null;
		}
		SalesDataSort ds = new SalesDataSort();
		ds.setObjectId(pvo.getObjectId());
		ds.setOrganization(info.getOrganization());
		ds.setNo(pvo.getNo());
		ds.setSort(pvo.getSort());
		ds.setBusinessId(ids.get("businessId"));
		ds.setUpdateTime(new Date());
		ds.setUpdateUser(info.getUserName());
		if(isNew){
			ds.setGuid(SalesUtil.createGUID());
			ds.setCreateTime(new Date());
			ds.setCreateUser(info.getUserName());
		}
		return ds;
	}
	
	/**
	 * 保存产品排序信息
	 * @author tangxin 2015-05-10
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultVO saveSortFieldInfo(List<SortFieldValueVO> listSortProductVO, 
			AuthenticateInfo info, Long fileType) throws ServiceException {
		try{
			ResultVO resultVO = new ResultVO();
			if(listSortProductVO == null || listSortProductVO.size() < 1){
				return resultVO;
			}
			Map<String, Long> idMap = getDAO().findEnterpriseIdAndBusIdByOrganization(info.getOrganization());
			if(idMap == null) {
				resultVO.setStatus("false");
				resultVO.setStatus("获取该企业注册信息时出现异常！");
				return resultVO;
			}
			SortField orig_proField = getDAO().findSortFieldById(fileType);
			/* getListByFieldType方法的参数  fieldTypeId,值为6的时候代码产品 */
			List<SalesDataSort> orig＿productSort = getListByFieldType(info.getOrganization(), fileType);
			List<Long> sortIds = new ArrayList<Long>();
			List<Integer> sortList = new ArrayList<Integer>();
			for(SortFieldValueVO pvo : listSortProductVO){
				if(pvo == null) continue;
				/* 如果sortid为空，并且sort>0,新增一个产品排序 */
				if(pvo.getSortId() == null && pvo.getSort() > 0){
					SalesDataSort sds = createDataSortBySotFieldVO(pvo, info, idMap, true);
					sds.setSortField(orig_proField);
					create(sds);
				} else if(pvo.getSortId() != null){
					sortIds.add(pvo.getSortId());
					sortList.add(pvo.getSort());
					if(pvo.getSort() < 1){
						pvo.setSortId(null);
					}
				}
			}
			 /* 跟新已经排序的产品信息 */
			if(orig＿productSort != null){
				for(SalesDataSort sds : orig＿productSort){
					int index = sortIds.indexOf(sds.getId());
					int sort = (index > -1 ? sortList.get(index) : -1);
					if(index > -1 && sort > 0){
						sds.setSort(sort);
						update(sds);
					}else{
						delete(sds);
					}
				}
			}
			return resultVO;
		}catch(Exception e){
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	/**
	 * 保存企业资料排序信息
	 * @author tangxin 2015-05-10
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public DataSortVO saveSortData(DataSortVO dataSortVO, AuthenticateInfo info) throws ServiceException{
		try{
			if(dataSortVO == null) return dataSortVO;
			/* 保存三证、生产许可证、荣誉认证排序信息 */
			saveCertSortInfo(dataSortVO.getListSortCertVO(), info);
			/* 产品排序vo */
			List<SortFieldValueVO> listSortProductVO = dataSortVO.getListSortProductVO();
			/* 保存产品排序信息 */
			saveSortFieldInfo(listSortProductVO, info, 6L);
			/* 企业掠影排序集合 */
			List<SalesResource> busAlbumListRes =  createSortResourceBySortVO(dataSortVO.getListSortAlbumVO());
			/* 保存企业掠影排序集合 */
			salesResourceService.updateSortResource(busAlbumListRes);
			/* 企业销售案例排序集合 */
			List<SalesCase> salesCaseList =  createSortCaseBySortVO(dataSortVO.getListSortSalesCaseVO());
			/* 保存企业销售案例排序集合 */
			salesCaseService.updateSortCase(salesCaseList);
			return dataSortVO;
		}catch(Exception e){
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	/**
	 * 通过List<SortFieldValueVO>集合构造一个List<SalesCase>
	 * @author tangxin 2015-05-10
	 */
	private List<SalesResource> createSortResourceBySortVO(List<SortFieldValueVO> listSortResourceVO){
		if(listSortResourceVO == null || listSortResourceVO.size() < 1){
			return null;
		}
		List<SalesResource> listSortRes = new ArrayList<SalesResource>();
		for(SortFieldValueVO sfvo : listSortResourceVO){
			if(sfvo == null) continue;
			SalesResource res = new SalesResource();
			res.setId(sfvo.getId());
			res.setSort(sfvo.getSort());
			listSortRes.add(res);
		}
		return listSortRes;
	}
	
	/**
	 * 通过List<SortFieldValueVO>集合构造一个List<SalesCase>
	 * @author tangxin 2015-05-10
	 */
	private List<SalesCase> createSortCaseBySortVO(List<SortFieldValueVO> listSortResourceVO){
		if(listSortResourceVO == null || listSortResourceVO.size() < 1){
			return null;
		}
		List<SalesCase> listSortCase = new ArrayList<SalesCase>();
		for(SortFieldValueVO sfvo : listSortResourceVO){
			if(sfvo == null) continue;
			SalesCase scase = new SalesCase();
			scase.setId(sfvo.getId());
			scase.setSort(sfvo.getSort());
			listSortCase.add(scase);
		}
		return listSortCase;
	}
	
	/**
	 * 保存三证、生产许可证、荣誉认证排序信息
	 * @author tangxin 2015-05-10 
	 */
	private ResultVO saveCertSortInfo(List<SortFieldValueVO> listCertSortVO, AuthenticateInfo info) throws ServiceException{
		ResultVO resultVO = new ResultVO();
		if(listCertSortVO == null || listCertSortVO.size() < 1){
			return resultVO;
		}
		List<SortFieldValueVO> listQSSortVO = new ArrayList<SortFieldValueVO>();
		List<SortFieldValueVO> listHonorCertSortVO = new ArrayList<SortFieldValueVO>();
		for(SortFieldValueVO sfvo : listCertSortVO){
			if(sfvo == null) continue;
			Long fileType = sfvo.getSortFieldId();
			if(fileType == null) continue;
			if(fileType.equals(1L) || fileType.equals(2L) || fileType.equals(3L)){
				List<SortFieldValueVO> origVO = new ArrayList<SortFieldValueVO>();
				origVO.add(sfvo);
				saveSortFieldInfo(origVO, info, fileType);
			}else if(fileType.equals(4L)){
				listQSSortVO.add(sfvo);
			}else if(fileType.equals(5L)){
				listHonorCertSortVO.add(sfvo);
			}
		}
		saveSortFieldInfo(listQSSortVO, info, 4L);
		saveSortFieldInfo(listHonorCertSortVO, info, 5L);
		return resultVO;
	}
	
	@Override
	public SalesDataSortDAO getDAO() {
		return salesDataSortDAO;
	}

}
