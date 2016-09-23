package com.gettec.fsnip.fsn.service.sales.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.sales.SalesCaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.sales.SalesCase;
import com.gettec.fsnip.fsn.model.sales.SalesResource;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.sales.PhotosAlbumsService;
import com.gettec.fsnip.fsn.service.sales.SalesCaseService;
import com.gettec.fsnip.fsn.service.sales.SalesResourceService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.util.sales.SalesUtil;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.AlbumVO;
import com.gettec.fsnip.fsn.vo.sales.DetailAlbumVO;
import com.gettec.fsnip.fsn.vo.sales.SalesCaseVO;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

@Service(value="salesCaseService")
public class SalesCaseServiceImpl extends BaseServiceImpl<SalesCase,SalesCaseDAO> implements SalesCaseService {

    @Autowired private SalesCaseDAO salesCaseDAO;
    @Autowired private BusinessUnitService businessUnitService;
    @Autowired private SalesResourceService salesResourceService;
    @Autowired private PhotosAlbumsService photosAlbumsService;

    @Override
    public SalesCaseDAO getDAO() {
        return salesCaseDAO;
    }

    /**
     * 拼接configure条件（纯sql语句）
     * Create Date 2015-05-04
     * @author HY
     */
    private String getConfigure(String configure) {
        String new_configure = "";
        if(configure == null) {
            return " WHERE 1=1 ";
        }
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
        new_configure = (new_configure.equals("") ? " WHERE 1=1 " : new_configure);
        return new_configure;
    }

    /**
     * 分割页面的过滤信息
     * Create Date 2015-05-04
     * @author HY
     */
    private String splitJointConfigure(String field, String mark, String value, boolean isIgnoreBrand) throws ServiceException {
        try {
            value = URLDecoder.decode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("【Service-Error】数据筛选时，利用utf-8解码筛选条件时出现异常！",e);
        }
        if(field.equals("id")){
            return FilterUtils.getConditionStr("salescase.id", mark, value);
        }
        if(field.equals("salesCaseName")){
            return FilterUtils.getConditionStr("salescase.name",mark,value);
        }
        if(field.equals("salesDetails")){
            return FilterUtils.getConditionStr("salescase.description",mark,value);
        }
        return null;
    }

    /**
     * 销售案例管理中的总数
     * Create Date 2015-05-04
     * @author HY
     */
    @Override
    public long countByConfigure(Long organization, String configure) throws ServiceException {
        try {
            return salesCaseDAO.countByConfigure(organization, getConfigure(configure));
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(), daoe.getException());
        }
    }

    /**
     * 查询相关企业的所有的销售案例
     * Create Date 20150-05-04
     * @author HY
     */
    @Override
    public List<SalesCaseVO> getListByOrganizationWithPage(Long organization, String configure, Integer page, Integer pageSize) throws ServiceException {
        try {
            return salesCaseDAO.getListByOrganizationWithPage(organization, getConfigure(configure), page, pageSize);
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(), daoe.getException());
        }
    }

    /**
     * 保持销售案例
     * Create Date 20150-05-04
     * @author HY
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SalesCaseVO save(SalesCaseVO salesCaseVO, AuthenticateInfo info, boolean isNew) throws ServiceException {
        try{
        	// 当前台传递的VO为null时，不保存，直接返回。
            if(salesCaseVO == null) {
                return salesCaseVO;
            }
            // 定义一个 销售案例 
            SalesCase salesCase = null;
            if(!isNew) { /* 如果是更新 */
                salesCase = getDAO().findById(salesCaseVO.getId());
            }
            // 将销售案例 VO 转换为 Entity
            salesCase = salesCaseVO.toEntity(salesCase);
            salesCase.setUpdateTime(new Date());
            salesCase.setUpdateByUser(info.getUserName());
            if(isNew) {/* 如果是新增 */
                Long busId = businessUnitService.findIdByOrg(info.getOrganization());
                // 为新增的 销售案例 赋状态值 
                salesCase.setCreateTime(new Date());
                salesCase.setCreateByUser(info.getUserName());
                // 系统全局唯一标识 GUID
                salesCase.setGuid(SalesUtil.createGUID());
                salesCase.setBusinessId(busId); //销售 案例 所属企业的id
                salesCase.setOrganization(info.getOrganization()); //销售 案例 所属企业的组织机构
                salesCase.setDelStatus(0); // delStatus 0 未删除 ，1 删除状态
                create(salesCase); // create 创建
                // 新增成功后 ，给销售案例 VO 赋id值 ，VO需要返回到前台。
                salesCaseVO.setId(salesCase.getId());
            }else{
            	// 更新状态时，更新销售案例信息
                update(salesCase);
            }
            // 销售案例 资源 集合
            List<SalesResource> salesResList = salesCaseVO.getResource();
            if(salesResList != null) {
            	// 将资源上传到FTP 并 保存持久化资源对象
                salesResourceService.saveSalesCaseResource(salesResList, salesCase.getGuid(), info);
            } else {
            	// 当前台未封装资源信息时，需要判断之前是否保存资源，如果有则需要删除。
                salesResourceService.removeResourceByGUID(salesCase.getGuid(), info);
            }
            // 将处理后的资源集合 赋值给VO （新增时资源会有id，前台需要使用）
            salesCaseVO.setResource(salesResList);
        }catch(Exception e){
            throw new ServiceException(e.getMessage(), e);
        }
        return salesCaseVO;
    }

    /**
     * 验证销售案例名称是否重复
     * @author HY
     * Created Date 2015-05-05
     */
    @Override
    public long countByName(String name, Long organization, Long id) throws ServiceException {
        try{
            return this.getDAO().countByName(name, organization, id);
        }catch(DaoException daoe){
            throw new ServiceException(daoe.getMessage(), daoe.getException());
        }
    }
    /**
     * 删除销售案例信息(假删除)
     * @author HY
     * CReated Date 2015-05-03
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultVO removeById(Long id, AuthenticateInfo info) throws ServiceException {
        ResultVO resultVO = new ResultVO();
        if(id == null) {
        	// 参数为空，删除失败，直接返回
            resultVO.setStatus("false");
            resultVO.setErrorMessage("删除失败,被删除销售案例的id为空！");
            return resultVO;
        }
        //　 获取需要删除的对象
        SalesCase orig_salesCase = findById(id);
        if(orig_salesCase != null) {
        	// 修改对象被删除的状态，delStatus: 1表示已经删除，0表示未删除
            orig_salesCase.setDelStatus(1);
            // 修改对象的更新日期和更新的用户
            orig_salesCase.setUpdateTime(new Date());
            orig_salesCase.setUpdateByUser(info.getUserName());
            // 通过销售案例的 GUID 图标删除当前销售案例的图片资源（假删除）
            resultVO = salesResourceService.removeResourceByGUID(orig_salesCase.getGuid(), info);
            // 更新被删除的实体
            update(orig_salesCase);
        } else {
        	// 被删除的对象不存在，删除失败
            resultVO.setStatus("false");
            resultVO.setErrorMessage("删除失败,被删除的销售案例信息不存在！");
        }
        return resultVO;
    }
    /**
     * 封装企业销售案例
     * author HY
     * Create Date 2015-05-05
     */
    @Override
    public List<SalesCaseVO> getListEntityByBusId(Long busId) throws ServiceException {
        try {
            return salesCaseDAO.getListEntityByBusId(busId);
        } catch (DaoException daoe) {
            throw new ServiceException(daoe.getMessage(), daoe.getException());
        }
    }

    /**
     * 销售案例相册信息
     * @author HY
     * CReated Date 2015-05-03
     */
    @Override
    public AlbumVO getSalesCaseAlbum(Long id, String cut) throws ServiceException {
        try {
        	// 通过id获取销售案例实体
            SalesCase orig_salesCase = salesCaseDAO.findById(id);
            AlbumVO albumVO = null; // 定义相册VO
            if(orig_salesCase !=null){
                albumVO = new AlbumVO();
                albumVO.setName(orig_salesCase.getName());
                albumVO.setDescribe(orig_salesCase.getDescription());
                // 获取销售案例下的图片资源，DetailAlbumVO 封装图片结构话信息。
                List<DetailAlbumVO> listAlbumVO = photosAlbumsService.getListAlbumsByBusId(orig_salesCase.getId(),cut);
                if(listAlbumVO != null && listAlbumVO.size() > 0){
                    albumVO.setDetailAlbums(listAlbumVO);
                    albumVO.setTotal(listAlbumVO.size());
                }
            } 
            return albumVO;
        } catch (JPAException jpae) {
            throw new ServiceException(jpae.getMessage(), jpae.getException());
        }
    }

    /**
	 * 批量更新销售案例排序顺序,电子资源排序时，对销售案例进行排序时，调用此方法。
	 * @author tangxin 2015-05-16
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultVO updateSortCase(List<SalesCase> listCase) throws ServiceException{
		try{
			ResultVO resultVO = new ResultVO();
			// 需要排序的销售案例信息为空，直接返回，不做失败处理。
			if(listCase == null || listCase.size() < 1){
				return resultVO;
			}
			for(SalesCase scase : listCase){
				if(scase == null) continue;
				SalesCase origCase = findById(scase.getId());
				if(origCase == null) continue;
				// 更改销售案例的排序序号
				origCase.setSort(scase.getSort());
				update(origCase);
			}
			return resultVO;
		}catch(Exception e){
			throw new ServiceException(e.getMessage(), e);
		}
	}
}
