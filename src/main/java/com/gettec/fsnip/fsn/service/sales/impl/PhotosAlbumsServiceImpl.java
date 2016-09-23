package com.gettec.fsnip.fsn.service.sales.impl;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_SALES_ALBUMS_PATH;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.sales.PhotosAlbumsDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.Certification;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.EnterpriseRegiste;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;
import com.gettec.fsnip.fsn.model.business.OrganizingInstitution;
import com.gettec.fsnip.fsn.model.business.TaxRegisterInfo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.BusinessCertification;
import com.gettec.fsnip.fsn.model.sales.PhotosAlbums;
import com.gettec.fsnip.fsn.model.sales.SalesResource;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.business.EnterpriseRegisteService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.BusinessCertificationService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.service.sales.PhotosAlbumsService;
import com.gettec.fsnip.fsn.service.sales.SalesResourceService;
import com.gettec.fsnip.fsn.service.test.TestPropertyService;
import com.gettec.fsnip.fsn.util.ImgUtils;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.sales.SalesUtil;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.AlbumVO;
import com.gettec.fsnip.fsn.vo.sales.DetailAlbumVO;
import com.gettec.fsnip.fsn.vo.sales.PhotoFieldVO;
import com.gettec.fsnip.fsn.vo.sales.PhotosAlbumVO;
import com.gettec.fsnip.fsn.vo.sales.ReportAppVO;
import com.gettec.fsnip.fsn.vo.sales.SalesProductVO;
import com.gettec.fsnip.fsn.vo.sales.ViewAlbumVO;
import com.gettec.fsnip.fsn.vo.sales.ViewReportVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 相册service层
 * @author tangxin 2015/04/24
 * 
 */
@Service(value = "photosAlbumsService")
public class PhotosAlbumsServiceImpl extends BaseServiceImpl<PhotosAlbums, PhotosAlbumsDAO> implements
		PhotosAlbumsService {

	@Autowired private PhotosAlbumsDAO photosAlbumsDAO;
	@Autowired private EnterpriseRegisteService enterpriseRegisteService;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private BusinessCertificationService businessCertificationService;
	@Autowired private ProductService productService;
	@Autowired private SalesResourceService salesResourceService;
	@Autowired private TestPropertyService testPropertyService;

	/**
	 * 获取认证相册
	 * @author tangxin 2015-05-04
	 */
	private ViewAlbumVO getCertificationAlbumVO(Long organization, int type, String sysType)
			throws ServiceException {
		try {
			if (organization == null) {
				return null;
			}
			//默认图片
			String defaultImgUrl = SalesUtil.DEFAULT_IMG_URL;
			ViewAlbumVO albumVO = new ViewAlbumVO();
			Long busId = businessUnitService.findIdByOrg(organization);
			long count = 0l;
			if(SalesUtil.CERTIFICATION_TYPE_SLF == type) {
				/* 荣誉认证 */
				count = businessCertificationService.countByBusIdAndType(busId, type);
				albumVO.setAlbumID(SalesUtil.HONOR_ALBUM_ID);
				albumVO.setAlbumName(SalesUtil.HONOR_ALBUM_NAME);
				if("WEB".equals(sysType)) {
					List<BusinessCertification> listBusCert = businessCertificationService.getListOfCertificationByBusIdAndType(busId, type, 1, 1);
					if(listBusCert != null && listBusCert.size() == 1) {
						Resource res = listBusCert.get(0).getCertResource();
						albumVO.setCoverPhoto(res != null ? res.getUrl(): defaultImgUrl);
					} else {
						albumVO.setCoverPhoto(defaultImgUrl);
					}
				} else if("APP".equals(sysType)) {
					albumVO.setCoverPhoto(SalesUtil.HONOR_COVER_IMG);
				}
				
			} else if(SalesUtil.CERTIFICATION_TYPE_STD == type){
				/* 其他认证 */
				count = getDAO().countCertification(busId);
				
				albumVO.setAlbumID(SalesUtil.CERTIFICATION_ALBUM_ID);
				albumVO.setAlbumName(SalesUtil.CERTIFICATION_ALBUM_NAME);
				if("WEB".equals(sysType)) {
					List<DetailAlbumVO> albumvo = getListCertification(busId,1,1,null);
					if(albumvo != null && albumvo.size() == 1) {
						DetailAlbumVO vo = albumvo.get(0);
						albumVO.setCoverPhoto(vo != null ? vo.getImgUrl(): defaultImgUrl);
					} else {
						albumVO.setCoverPhoto(defaultImgUrl);
					}
				} else if("APP".equals(sysType)) {
					albumVO.setCoverPhoto(SalesUtil.CERTIFICATION_COVER_IMG);
				}
				
			}
			albumVO.setPhotoNo(count);
			return albumVO;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	/**
	 * 获取企业认证相册详细
	 * @author tangxin 2015-05-04
	 */
	private List<DetailAlbumVO> getCertificationDetailAlbumVO(Long organization,Long busId,String cut,int type,int page,int pageSize)
			throws ServiceException {
		try {
			if (organization == null || busId == null) {
				return null;
			}
			List<BusinessCertification> listBusCert = null;
			listBusCert = businessCertificationService.getListOfCertificationByBusIdAndType(busId, type, page, pageSize);
			List<DetailAlbumVO> detailAlbum = new ArrayList<DetailAlbumVO>();
			if(listBusCert != null && listBusCert.size() > 0) {
				Map<String,Integer> cutMap = SalesUtil.getCutWidthAndHeight(cut);
				int fileType = SalesUtil.PHOTO_FIELD_TYPE_CERTIFICATE;
				for(BusinessCertification cert : listBusCert){
					Resource res = cert.getCertResource();
					Certification stdCert = cert.getCert();
					DetailAlbumVO vo = createDetailAlbumVO(cert.getId(), (stdCert != null ? stdCert.getName() : res.getFileName()),
							res.getUrl(), cutMap);
					if(cert.getCert() != null) {
						PhotoFieldVO fieldVO = new PhotoFieldVO(null,null,null,null,cert.getEndDate(),null,null,cert.getCert().getName(),
								null,null,0l,0l,fileType,null);
						vo.setField(fieldVO);
					}
					vo.setProductId(0L);
					detailAlbum.add(vo);
				}
			}
			return detailAlbum;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	/**
	 * 获取企业三证相册
	 * @author tangxin 2015-05-04
	 */
	private ViewAlbumVO getSanZhengAlbumVO(Long organization, String sysType)
			throws ServiceException {
		try {
			if (organization == null) {
				return null;
			}
			ViewAlbumVO albumVO = new ViewAlbumVO();
			
			//BusinessUnit bus = businessUnitService.findByOrganization(organization);
			String strName = businessUnitService.findNameByOrganization(organization);
			
			EnterpriseRegiste enterbus = null;
			long count = 0;
			if (strName != null) {
				enterbus = enterpriseRegisteService.findbyEnterpriteName(strName);
			}
			albumVO.setAlbumID(SalesUtil.SANZHENG_ALBUM_ID);
			albumVO.setAlbumName(SalesUtil.SANZHENG_ALBUM_NAME);
			if("APP".equals(sysType)) {
				albumVO.setCoverPhoto(SalesUtil.SANZHENG_COVER_IMG);
			} else if("WEB".equals(sysType)) {
				albumVO.setCoverPhoto(getCoverPhoto(enterbus));
			}
			if(enterbus != null) {
				count = countByEnterpriseRegiste(enterbus);
			}
			albumVO.setPhotoNo(count);
			return albumVO;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	/**
	 * 获取三证封面图片
	 * @author tangxin 2015-05-04
	 */
	private String getCoverPhoto(EnterpriseRegiste enterbus){
		//默认图片
		String defaultImgUrl = SalesUtil.DEFAULT_IMG_URL;
		if(enterbus == null) {
			return defaultImgUrl;
		}
		Set<Resource> setRes = enterbus.getOrgAttachments();
		setRes = (setRes == null ? enterbus.getLicAttachments() : setRes);
		setRes = (setRes == null ? enterbus.getTaxRegAttachments() : setRes);
		Resource res = IteratorResRetLast(setRes);
		return (res != null ? res.getUrl() : defaultImgUrl);
	}
	
	/**
	 * 获取产品相册
	 * @author tangxin 2015-05-04
	 */
	private ViewAlbumVO getProductAlbum(Long organization, String sysType) throws ServiceException {
		try{
			if(organization == null) {
				return null;
			}
			//默认图片
			String defaultImgUrl = SalesUtil.DEFAULT_IMG_URL;
			ViewAlbumVO albumVO = new ViewAlbumVO();
			long count = productService.count(organization, null);
			albumVO.setAlbumID(SalesUtil.PRODUCT_ALBUM_ID);
			albumVO.setAlbumName(SalesUtil.PRODUCT_ALBUM_NAME);
			albumVO.setPhotoNo(count);
			if("WEB".equals(sysType)) {
				List<DetailAlbumVO> listProduct = productService.getProductAlbums(organization, 1, 1, null);
				if(listProduct != null && listProduct.size() == 1) {
					String imgUrl = listProduct.get(0).getImgUrl();
					imgUrl = ((imgUrl == null || "".equals(imgUrl)) ? defaultImgUrl : imgUrl);
					albumVO.setCoverPhoto(imgUrl);
				} else {
					albumVO.setCoverPhoto(defaultImgUrl);
				}
			} else if("APP".equals(sysType)) {
				albumVO.setCoverPhoto(SalesUtil.PRODUCTS_COVER_IMG);
			}
			
			return albumVO;
		}catch(Exception e){
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * 获取企业掠影相册
	 * @author tangxin 2015-05-04
	 */
	private ViewAlbumVO getBusPhotosAlbum(Long organization, String name, String sysType) throws ServiceException {
		try{
			if(organization == null || name == null) {
				return null;
			}
			//默认图片
			String defaultImgUrl = SalesUtil.DEFAULT_IMG_URL;
			ViewAlbumVO albumVO = new ViewAlbumVO();
			long count = getDAO().countByName(organization, name);
			albumVO.setAlbumID(SalesUtil.BUSPHOTO_ALBUM_ID);
			albumVO.setAlbumName(SalesUtil.BUSPHOTO_ALBUM_NAME);
			albumVO.setPhotoNo(count);
			if("WEB".equals(sysType)) {
				List<DetailAlbumVO> listBusPhotos = photosAlbumsDAO.getListAlbumsByPage(organization, name, 1, 1, null);
				if(listBusPhotos != null && listBusPhotos.size() == 1) {
					String imgUrl = listBusPhotos.get(0).getImgUrl();
					imgUrl = ((imgUrl == null || "".equals(imgUrl)) ? defaultImgUrl : imgUrl);
					albumVO.setCoverPhoto(imgUrl);
				} else {
					albumVO.setCoverPhoto(defaultImgUrl);
				}
			} else if("APP".equals(sysType)) {
				albumVO.setCoverPhoto(SalesUtil.BUS_ALBUM_COVER_IMG);
			}
			
			return albumVO;
		}catch(Exception e){
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	/**
	 * 获取企业所有的相册(从数据库里面查询指定相册的第一张图片)
	 * @author tangxin 2015-05-05
	 */
	@Override
	public List<ViewAlbumVO> getAlbumsByOrgId(Long organization, String cut, String sysType) throws ServiceException{
		try{
			List<ViewAlbumVO> resultVO = new ArrayList<ViewAlbumVO>();
			// 相册图片的切割参数
			Map<String, Integer> cutMap = SalesUtil.getCutWidthAndHeight(cut);
			// 获取三证相册 VO
			ViewAlbumVO sanZhengVO = getSanZhengAlbumVO(organization, sysType);
			if(sanZhengVO != null){
				if(cutMap != null) {
					// 相册封面图片
					sanZhengVO.setCoverPhoto(ImgUtils.getImgPath(sanZhengVO.getCoverPhoto(), cutMap.get("width"), cutMap.get("height")));
				}
				resultVO.add(sanZhengVO);
			}
			// 获取企业荣誉认证相册
			ViewAlbumVO certSlfVO = getCertificationAlbumVO(organization, SalesUtil.CERTIFICATION_TYPE_SLF, sysType);
			if(certSlfVO != null){
				if(cutMap != null) {
					// 相册封面图片
					certSlfVO.setCoverPhoto(ImgUtils.getImgPath(certSlfVO.getCoverPhoto(), cutMap.get("width"), cutMap.get("height")));
				}
				resultVO.add(certSlfVO);
			}
			// 获取企业其他认证相册，包含QS认证
			ViewAlbumVO certStdVO = getCertificationAlbumVO(organization, SalesUtil.CERTIFICATION_TYPE_STD, sysType);
			if(certStdVO != null){
				if(cutMap != null) {
					// 相册封面图片
					certStdVO.setCoverPhoto(ImgUtils.getImgPath(certStdVO.getCoverPhoto(), cutMap.get("width"), cutMap.get("height")));
				}
				resultVO.add(certStdVO);
			}
			// 获取企业产品相册集合
			ViewAlbumVO productVO = getProductAlbum(organization, sysType);
			if(productVO != null){
				if(cutMap != null) {
					// 相册封面图片
					productVO.setCoverPhoto(ImgUtils.getImgPath(productVO.getCoverPhoto(), cutMap.get("width"), cutMap.get("height")));
				}
				resultVO.add(productVO);
			}
			// 获取企业掠影相册
			ViewAlbumVO busPhtotsVO =  getBusPhotosAlbum(organization, SalesUtil.BUSPHOTO_ALBUM_NAME, sysType);
			if(busPhtotsVO != null){
				if(cutMap != null) {
					// 相册封面图片
					busPhtotsVO.setCoverPhoto(ImgUtils.getImgPath(busPhtotsVO.getCoverPhoto(), cutMap.get("width"), cutMap.get("height")));
				}
				resultVO.add(busPhtotsVO);
			}
			// 获取所有的相册
			return resultVO;
		}catch(Exception e){
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	/**
	 * 封装DetailAlbumVO
	 *  @author tangxin 2015-05-05
	 */
	private DetailAlbumVO createDetailAlbumVO(Long albumVOId,String name,String url,Map<String,Integer> cutMap) {
		if(url == null || name == null) {
			return null;
		}
		//默认图片
		String defaultImgUrl = SalesUtil.DEFAULT_IMG_URL;
		url = (url == null || url.equals("")) ? defaultImgUrl : url;
		DetailAlbumVO vo = new DetailAlbumVO();
		vo.setId(albumVOId);
		vo.setImgName(name);
		vo.setImgUrl(url);
		vo.setProductId(0L);
		if(cutMap != null) {
			vo.setThumbnailUrl(ImgUtils.getImgPath(url, cutMap.get("width"), cutMap.get("height")));
		}else{
			vo.setThumbnailUrl(url);
		}
		return vo;
	}
	
	/**
	 * 统计 EnterpriseRegiste 中 三证的数量
	 * @author tangxin 2015-05-05
	 */
	private long countByEnterpriseRegiste(EnterpriseRegiste enterprise){
		long count = 0;
		if(enterprise != null) {
			count = (enterprise.getOrgAttachments() != null && enterprise.getOrgAttachments().size() > 0) ? count+1 : count;
			count = (enterprise.getLicAttachments() != null && enterprise.getLicAttachments().size() > 0) ? count+1 : count;
			count = (enterprise.getTaxRegAttachments() != null && enterprise.getTaxRegAttachments().size() > 0) ? count+1 : count;
		}
		return count;
	}
	
	/**
	 * 根据企业信息创建相册图片的结构话字段
	 * @author tangxin 2015-05-05
	 */
	private PhotoFieldVO createPhotoFieldVO(BusinessUnit busunit,String type) {
		if(busunit == null) return null;
		PhotoFieldVO fieldVO = null;
		if("ORG".equals(type)){
			OrganizingInstitution orig = busunit.getOrgInstitution();
			if(orig != null){
				int fieldType = SalesUtil.PHOTO_FIELD_TYPE_ORGANIZATION;
				fieldVO = new PhotoFieldVO(null,orig.getOrgCode(),orig.getOrgName(),orig.getStartTime(),
						orig.getEndTime(),null,null,null,null,null,0l,0l,fieldType,null);
			}
		}else if("LICE".equals(type)){
			LicenseInfo lice = busunit.getLicense();
			if(lice != null){
				int fieldType = SalesUtil.PHOTO_FIELD_TYPE_LICENCE;
				fieldVO = new PhotoFieldVO(null,lice.getLicenseNo(),lice.getLicensename(),lice.getStartTime(),
						lice.getEndTime(),lice.getLegalName(),lice.getRegistrationTime(),null,null,null,0l,0l,fieldType,null);
			}
		}else if("TAXT".equals(type)){
			TaxRegisterInfo taxt = busunit.getTaxRegister();
			if(taxt != null){
				int fieldType = SalesUtil.PHOTO_FIELD_TYPE_TAXREGISTER;
				fieldVO = new PhotoFieldVO(taxt.getId(),null,taxt.getTaxerName(),null,null,null,null,null,null,null,0l,0l,fieldType,null);
			}
		}
		return fieldVO;
	}
	
	/**
	 * 迭代资源，返回集合最后一个资源对象
	 * @author tangxin 2015-05-15
	 */
	private Resource IteratorResRetLast(Set<Resource> reSet){
		if(reSet == null || reSet.size() < 1){
			return null;
		}
		Resource result = null;
		Long maxId = 0L;
		for(Resource res : reSet){
			if(res == null) continue;
			if(maxId < res.getId()){
				maxId = res.getId();
				result = res;
			}
		}
		return result;
	}
	
	/**
	 * 获取企业三证相册详细
	 * @author tangxin 2015-05-04
	 */
	private AlbumVO getSanZhengAlbum(Long organization,String cut) throws ServiceException {
		try {
			if (organization == null) {
				return null;
			}
			long count = 0;
			Map<String, Integer> cutMap = SalesUtil.getCutWidthAndHeight(cut);
			AlbumVO albumVO = new AlbumVO();
			List<DetailAlbumVO> detailAlbumVO = new ArrayList<DetailAlbumVO>();
			//BusinessUnit bus = businessUnitService.findByOrganization(organization);
			BusinessUnit bus = businessUnitService.findUnitNameSanZhengInfo(organization);
			EnterpriseRegiste enterbus = null;
			if (bus != null) {
				enterbus = enterpriseRegisteService.findbyEnterpriteName(bus.getName());
			}
			if(enterbus != null) {
				if(enterbus.getOrgAttachments() != null && enterbus.getOrgAttachments().size() > 0){
					Resource res = IteratorResRetLast(enterbus.getOrgAttachments());
					DetailAlbumVO vo = createDetailAlbumVO(enterbus.getId(),"组织机构代码证",res.getUrl(),cutMap);
					vo.setDescrib("各类组织机构在社会经济活动中的通行证。组织机构代码是对中华人民共和国境内依法注册、依法登记的机关、企、事业单位、社会团体和民办非企业单位颁发一个在全国范围内唯一的、始终不变的代码标识。");
					vo.setField(createPhotoFieldVO(bus,"ORG"));
					if(vo != null) {
						detailAlbumVO.add(vo);
					}
				}
				if(enterbus.getLicAttachments() != null && enterbus.getLicAttachments().size() > 0){
					Resource res = IteratorResRetLast(enterbus.getLicAttachments());
					DetailAlbumVO vo = createDetailAlbumVO(enterbus.getId(),"营业执照",res.getUrl(),cutMap);
					vo.setField(createPhotoFieldVO(bus,"LICE"));
					vo.setDescrib("营业执照是企业或组织合法经营权的凭证。营业执照分正本和副本，二者具有相同的法律效力。");
					if(vo != null) {
						detailAlbumVO.add(vo);
					}
				}
				if(enterbus.getTaxRegAttachments() != null && enterbus.getTaxRegAttachments().size() > 0){
					Resource res = IteratorResRetLast(enterbus.getTaxRegAttachments());
					DetailAlbumVO vo = createDetailAlbumVO(enterbus.getId(),"税务登记证",res.getUrl(),cutMap);
					vo.setDescrib("是从事生产、经营的纳税人向生产、经营地或者纳税义务发生地的主管税务机关申报办理税务登记时，所颁发的登记凭证。");
					vo.setField(createPhotoFieldVO(bus,"TAXT"));
					if(vo != null) {
						detailAlbumVO.add(vo);
					}
				}
				count = countByEnterpriseRegiste(enterbus);
			}
			albumVO.setDetailAlbums(detailAlbumVO);
			albumVO.setTotal(count);
			return albumVO;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	/**
	 * 分页查询企业其他认证相册数量，包括qs证
	 * @author tangxin 2015-05-21
	 */
	private List<DetailAlbumVO> getListCertification(Long busId, int page, int pageSize, String cut) throws DaoException{
		// 获取企业其他认证信息，包含QS认证
		List<Object[]> listObjs = getDAO().getListCertification(busId, page, pageSize);
		// 详细相册 VO 集合
		List<DetailAlbumVO> lsitAlbum = null;
		List<String> exisQSNo = new ArrayList<String>();
		// 日期处理
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		int fileType = SalesUtil.PHOTO_FIELD_TYPE_CERTIFICATE;
		// 图片切割参数
		Map<String,Integer> cutMap = SalesUtil.getCutWidthAndHeight(cut);
		if(listObjs != null && listObjs.size() > 0){
			lsitAlbum = new ArrayList<DetailAlbumVO>();
			for(Object[] obj : listObjs){
				if(obj == null) continue;
				String no = obj[0] != null ? obj[0].toString() : "";
				/* 针对qs证号，如果重复，则跳过封装 */
				if(!"".equals(no) && exisQSNo.contains(no)) continue;
				if(!"".equals(no)) exisQSNo.add(no);
				String name = obj[1] != null ? obj[1].toString() : "";
				Date endDte = null;
				try {
					endDte = obj[2] != null ?  sf.parse(obj[2].toString()) : null;
				} catch (ParseException e) {
					e.printStackTrace();
				}
				String url = obj[4] != null ? obj[4].toString() : "";
				DetailAlbumVO vo = createDetailAlbumVO(null, ("".equals(no) ? name : no), url, cutMap);
				vo.setDescrib(obj[5] != null ? obj[5].toString() : "");
				
				PhotoFieldVO fieldVO = new PhotoFieldVO(null,no,null,null,endDte,null,null,name,null,null,0l,0l,fileType,null);
				vo.setField(fieldVO);
				vo.setProductId(0L);
				lsitAlbum.add(vo);
			}
		}
		return lsitAlbum;
	}
	
	/**
	 * 获取单个相册详细信息
	 * @author tangxin 2015-05-05
	 */
	@Override
	public AlbumVO getDetailAblumByPage(Long organization,String albumID,String cut,
			int page,int pageSize) throws ServiceException{
		AlbumVO albumVO = new AlbumVO();
		List<DetailAlbumVO> listAlbumVO = null;
		try{
			if(organization == null || albumID == null) {
				return null;
			}
			if(albumID.equals(SalesUtil.SANZHENG_ALBUM_ID)){
				albumVO = getSanZhengAlbum(organization,cut);
				listAlbumVO = albumVO.getDetailAlbums();
			}else if(albumID.equals(SalesUtil.HONOR_ALBUM_ID)){
				Long busId = businessUnitService.findIdByOrg(organization);
				albumVO.setTotal(businessCertificationService.countByBusIdAndType(busId, SalesUtil.CERTIFICATION_TYPE_SLF));
				listAlbumVO = getCertificationDetailAlbumVO(organization,busId,cut,SalesUtil.CERTIFICATION_TYPE_SLF,page,pageSize);
			}else if(albumID.equals(SalesUtil.CERTIFICATION_ALBUM_ID)){
				Long busId = businessUnitService.findIdByOrg(organization);
				albumVO.setTotal(getDAO().countCertification(busId));
				listAlbumVO = getListCertification(busId, page, pageSize, cut);
			}else if(albumID.equals(SalesUtil.PRODUCT_ALBUM_ID)){
				albumVO.setTotal(productService.count(organization, null));
				listAlbumVO = productService.getProductAlbums(organization, page, pageSize, cut);
			}else if(albumID.equals(SalesUtil.BUSPHOTO_ALBUM_ID)){
				albumVO.setTotal(getDAO().countByName(organization, SalesUtil.BUSPHOTO_ALBUM_NAME));
				listAlbumVO = getDAO().getListAlbumsByPage(organization, SalesUtil.BUSPHOTO_ALBUM_NAME, page, pageSize, cut);
			}
			albumVO.setDetailAlbums(listAlbumVO);
		}catch(ServiceException sex) {
			throw new ServiceException(sex.getMessage(), sex);
		}catch(DaoException dao) {
			throw new ServiceException(dao.getMessage(), dao);
		}
		return albumVO;
	}
	
	/**
	 * 根据产品id获取产品最新的送检报告以及 各类型的报告数量
	 * @author tangxin 2015-05-06
	 */
	@Override
	public ViewReportVO getViewReport(Long productId) throws ServiceException{
		try{
			return getDAO().getViewReport(productId);
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}
	
	/**
	 * 根据产品id获取产品最新的自检报告信息 企业APP接口
	 * @author tangxin 2015-05-07
	 */
	@Override
	public ReportAppVO findReportByBusAPP(Long productId) throws ServiceException{
		try{
			ViewReportVO reportVO = getDAO().getViewReport(productId);
			ReportAppVO appVO = null;
			if(reportVO != null) {
				appVO = new ReportAppVO();
				appVO.create(reportVO);
				SalesProductVO productVO = new SalesProductVO(productId, reportVO.getProductName(), reportVO.getProductFormat(), reportVO.getProductDesc());
				appVO.setProduct(productVO);
				List<TestProperty> items = testPropertyService.findByReportId(reportVO.getReportId());
				appVO.createTestItem(items);
			}
			return appVO;
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}catch(ServiceException sex){
			throw new ServiceException(sex.getMessage(), sex);
		}
	}
	
	/**
	 * 根据产品id获取产品最新的自检报告信息 企业APP接口
	 * @author liuwx 2015-10-13
	 */
	@Override
	public ReportAppVO findReportByBusAPP(Long productId,int type) throws ServiceException{
		try{
			ViewReportVO reportVO = getDAO().getViewReport(productId,type);
			ReportAppVO appVO = null;
			if(reportVO != null) {
				appVO = new ReportAppVO();
				appVO.create(reportVO);
				SalesProductVO productVO = new SalesProductVO(productId, reportVO.getProductName(), reportVO.getProductFormat(), reportVO.getProductDesc());
				appVO.setProduct(productVO);
				List<TestProperty> items = testPropertyService.findByReportId(reportVO.getReportId());
				appVO.createTestItem(items);
			}
			return appVO;
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}catch(ServiceException sex){
			throw new ServiceException(sex.getMessage(), sex);
		}
	}
	
	
	/**
	 * 保存企业掠影信息
	 * @author tangxin 2015-05-06
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PhotosAlbumVO save(PhotosAlbumVO albumVO, AuthenticateInfo info, boolean isNew) throws ServiceException{
		ResultVO resultVO = new ResultVO();
		try{
			if(albumVO == null){
				return null;
			}
			PhotosAlbums album = null;
			if(!isNew) {
				album = getDAO().findById(albumVO.getId());
			}
			album = albumVO.toEntity(album);
			album.setUpdateTime(new Date());
			album.setUpdateUser(info.getUserName());
			if(isNew) {
				Long busId = businessUnitService.findIdByOrg(info.getOrganization());
				album.setCreateTime(new Date());
				album.setCreateUser(info.getUserName());
				album.setGuid(SalesUtil.createGUID());
				album.setBusinessId(busId);
				album.setOrganization(info.getOrganization());
				album.setDelStatus(0);
				create(album);
				albumVO.setId(album.getId());
			}else{
				update(album);
			}
			List<SalesResource> salesResList = albumVO.getResource();
			if(salesResList != null) {
				String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_SALES_ALBUMS_PATH);
				resultVO = salesResourceService.saveListResourceByFtppath(salesResList,album.getGuid(),info,ftpPath);
			} else {
				salesResourceService.removeResourceByGUID(album.getGuid(), info);
			}
			albumVO.setResource(salesResList);
			if(resultVO.getStatus().equals("false")){
				throw new ServiceException(resultVO.getErrorMessage(),null);
			}
			return albumVO;
		}catch(ServiceException sex){
			throw sex;
		} catch (JPAException jpae) {
			throw new ServiceException(jpae.getMessage(),jpae);
		}
	}
	
	/**
	 * 根据企业组织机构和相册名称查询
	 * @author tangxin 2015-04-30
	 */
	@Override
	public PhotosAlbumVO findByOrganizationAndName(Long organization, String name) throws ServiceException{
		try{
			PhotosAlbumVO albumVO = getDAO().findByOrganizationAndName(organization, name);
			if(albumVO != null) {
				/* 获取相册图片 */
				albumVO.setResource(salesResourceService.getListResourceByGUID(albumVO.getGuid()));
			}
			return albumVO;
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(),daoe);
		}
	}

	/**
	 * 根据企业id获取企业的销售案例的相册
	 * @author HY
	 * Create Date 2015-05-07
	 */
	@Override
	public List<DetailAlbumVO> getListAlbumsByBusId(Long id,String cut) throws ServiceException {
		try {
			return getDAO().getListAlbumsByBusId(id, cut);
		} catch (DaoException daoe) {
			throw new ServiceException(daoe.getMessage(),daoe);
		}
	}

	@Override
	public PhotosAlbumsDAO getDAO() {
		return photosAlbumsDAO;
	}

}
