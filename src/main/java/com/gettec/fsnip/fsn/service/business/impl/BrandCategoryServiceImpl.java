package com.gettec.fsnip.fsn.service.business.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.business.BrandCategoryDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BrandCategory;
import com.gettec.fsnip.fsn.model.business.BrandCategoryDetail;
import com.gettec.fsnip.fsn.service.business.BrandCategoryService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.vo.business.BrandCategoryTreeNode;

/**
 * BrandCategory service implementation
 * @author Hui Zhang
 */
@Repository(value="brandcategoryService")
public class BrandCategoryServiceImpl extends BaseServiceImpl<BrandCategory,BrandCategoryDAO>
		implements BrandCategoryService{
	@Autowired private BrandCategoryDAO brandcategoryDAO;
	
	@Override
	public BrandCategoryDAO getDAO() {
		return brandcategoryDAO;
	}

	/**
	 * 根据level&keyWords获取BrandCategoryDetail集合
	 * @param level
	 * @param keyword
	 * @param orgs
	 * @return List<BrandCategoryTreeNode>
	 * @throws ServiceException
	 * */
	@Override
	public List<BrandCategoryTreeNode> getTreeNodes(int level, String keyword) throws ServiceException {
		try {
			return buildProductTree(getDAO().getTreeNodes(level, keyword),
					level);
		} catch (DaoException de) {
			throw new ServiceException(de.getMessage(), de.getCause());
		} catch (Exception e) {
			throw new ServiceException(
					"Service中根据level&keyWords获取BrandCategoryDetail集合时 出现异常", e);
		}
	}
	
	/**
	 * build BrandCategoryDetail to BrandCategoryTreeNode
	 * @param details
	 * @param level
	 * */
	@SuppressWarnings("deprecation")
	private static List<BrandCategoryTreeNode> buildProductTree(
			List<BrandCategoryDetail> details, int level) {
		if (details.size()<1) {
			return new ArrayList<BrandCategoryTreeNode>(0);
		}

		List<BrandCategoryTreeNode> nodes = new ArrayList<BrandCategoryTreeNode>(
				details.size());
		for (BrandCategoryDetail detail : details) {
			BrandCategoryTreeNode node = new BrandCategoryTreeNode();
			try {
				node.setId(URLEncoder.encode(level + "_" + detail.getName(),
						"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				node.setId(URLEncoder.encode(level + "_" + detail.getName()));
			}

			int index = detail.getName().lastIndexOf('.');
			String realName = detail.getName();
			if (index > 0) {
				realName = detail.getName().substring(index + 1);
			}
			node.setName(realName);
			node.setType("folder");
			node.setHasChildren(detail.getChildrenNum() > 0 ? true : false);
			if (detail.getProductId() != -1L) {
				node.setLeafId(detail.getProductId());
			}
			nodes.add(node);
		}
		nodes.get(0).setExpanded(false);
		return nodes;
	}
}
