package com.newaim.purchase.archives.product.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.api.service.OmsApiService;
import com.newaim.purchase.archives.product.dao.ProductCombinedDao;
import com.newaim.purchase.archives.product.dao.ProductCombinedDetailDao;
import com.newaim.purchase.archives.product.dao.ProductDao;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.entity.ProductCategory;
import com.newaim.purchase.archives.product.entity.ProductCombined;
import com.newaim.purchase.archives.product.entity.ProductCombinedDetail;
import com.newaim.purchase.archives.product.vo.ProductCombinedDetailVo;
import com.newaim.purchase.archives.product.vo.ProductCombinedVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductCombinedService extends ServiceBase {

    @Autowired
    private ProductCombinedDao productCombinedDao;

    @Autowired
    private ProductCombinedDetailDao productCombinedDetailDao;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private OmsApiService omsApiService;

    @Autowired
    private ProductCombinedDetailService productCombinedDetailService;


    @Autowired
    private ProductDao productDao;

    @Transactional(readOnly = true)
    public Page<ProductCombinedVo> listProductCombined(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<ProductCombined> spec = buildSpecification(params);
        Page<ProductCombined> p = productCombinedDao.findAll(spec, pageRequest);
        Page<ProductCombinedVo> page = p.map(new Converter<ProductCombined, ProductCombinedVo>() {
            @Override
            public ProductCombinedVo convert(ProductCombined product) {
                ProductCombinedVo vo = BeanMapper.map(product, ProductCombinedVo.class);
                return vo;
            }
        });
        return page;
    }

    /**
     * 根据组合产品ID获取组合产品
     * @param id
     * @return
     */
    public ProductCombined getProductCombined(String id){
        return productCombinedDao.findOne(id);
    }

    /**
     * 根据组合产品ID获取组合产品
     * @param id
     * @return
     */
    public ProductCombinedVo get(String id){
        ProductCombinedVo vo = convertToProductCombinedVo(getProductCombined(id));
        vo.setDetails(productCombinedDetailService.findDetailsVoByProductCombinedId(id));
        return vo;
    }

    /**
     * entity转换为VO
     * @param productCombined
     * @return
     */
    private ProductCombinedVo convertToProductCombinedVo(ProductCombined productCombined){
        ProductCombinedVo vo = BeanMapper.map(productCombined, ProductCombinedVo.class);
        return vo;
    }

    /**
     * 新建
     * @param productCombined
     * @param details
     * @return
     */
    @Transactional
    public ProductCombined add(ProductCombined productCombined, List<ProductCombinedDetail>details){
        try {
            //设置创建人信息
            setProductCombinedCreatorInfo(productCombined);

            //设置产品分类
            if(productCombined.getComboType() == 3) {
                setProductCategoryInfo(productCombined);
            }

            //加入同步状态（已新建未同步）
            productCombined.setFlagSyncStatus(Constants.ProductSyncFlag.ADD.code);
            productCombined = productCombinedDao.save(productCombined);
            saveDetails(productCombined, details);
            return productCombined;
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    /**
     * 修改，删除之前明细，重新绑定明细信息
     * @param productCombined
     * @param details
     * @return
     */
    @Transactional
    public ProductCombined update(ProductCombined productCombined, List<ProductCombinedDetail>details){
        try {
            //设置更新时间
            productCombined.setUpdatedAt(new Date());
            //设置产品分类
            if(productCombined.getComboType() == 3) {
                setProductCategoryInfo(productCombined);
            }
            //加入同步状态（已更新未同步）
            productCombined.setFlagSyncStatus(Constants.ProductSyncFlag.UPDATE.code);
            //删除之前明细，重新绑定数据
            productCombinedDetailDao.deleteByProductCombinedId(productCombined.getId());
            saveDetails(productCombined, details);
            return productCombinedDao.save(productCombined);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


    /**
     * 复制另存
     * @param productCombined
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductCombined saveAs(ProductCombined productCombined, List<ProductCombinedDetail>details){
        productCombinedDao.clear();
        //设置创建人信息
        setProductCombinedCreatorInfo(productCombined);
        //设置产品分类
        if(productCombined.getComboType() == 3) {
            setProductCategoryInfo(productCombined);
        }
        //清理信息
        productCombined.setId(null);
        productCombined.setUpdatedAt(null);
        productCombined = productCombinedDao.save(productCombined);
        saveDetails(productCombined,details);
        return productCombined;
    }
    /**
     * 序列化档案修改申请对象
     * @param o
     * @param details
     * @return
     */
    public ProductCombinedVo getApplyVoToSave(ProductCombined o, List<ProductCombinedDetail>details) {

        //设置产品分类
        if(o.getComboType() == 3) {
            setProductCategoryInfo(o);
        }

    	ProductCombinedVo vo = BeanMapper.map(o, ProductCombinedVo.class);

        //产品明细表
        if (details != null && details.size() > 0) {
            vo.setDetails(BeanMapper.mapList(details, ProductCombinedDetail.class, ProductCombinedDetailVo.class));
        } else {
            vo.setDetails(null);
        }

        return vo;
    }

    /**
	 * 反序列化档案修改申请对象
	 * 
	 * @param vo
	 * @return
	 */
	public ProductCombinedVo getApplyVoToDisplay(ProductCombinedVo vo) {

		// 产品明细
		if (null == vo.getDetails() || ListUtils.isEmpty(vo.getDetails())) {
			vo.setDetails(null);
		}
		return vo;
	}

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(ProductCombined productCombined, List<ProductCombinedDetail>details){
        if(details != null){
            boolean isNormal = Constants.Status.NORMAL.code.equals(productCombined.getStatus());
            for (int i = 0; i < details.size(); i++) {
                ProductCombinedDetail detail = details.get(i);
                detail.setProductCombinedId(productCombined.getId());
                if(isNormal){
                    //启动时，相关产品档案明细保存为组合产品
                    Product product = productDao.findOne(detail.getProductId());
                    if(product != null){
                        product.setCombined(1);
                        productDao.save(product);
                    }
                }
            }
            productCombinedDetailDao.save(details);
        }
    }


    /**
     * 设置创建人信息
     */
    public void setProductCombinedCreatorInfo(ProductCombined productCombined){
        UserVo user = SessionUtils.currentUserVo();
        productCombined.setCreatedAt(new Date());
//        productCombined.setStatus(Constants.Status.DRAFT.code);
        productCombined.setCreatorId(user.getId());
        productCombined.setCreatorCnName(user.getCnName());
        productCombined.setCreatorEnName(user.getEnName());
        productCombined.setDepartmentId(user.getDepartmentId());
        productCombined.setDepartmentCnName(user.getDepartmentCnName());
        productCombined.setDepartmentEnName(user.getDepartmentEnName());
    }


    /**
     * 确认
     * @param id
     */
    @Transactional
    public void confirmProductCombined(String id){
        ProductCombined productCombined = productCombinedDao.findOne(id);
        productCombined.setStatus(1);
        productCombinedDao.save(productCombined);
    }

    /**
     * 根据ID删除
     * @param id
     */
    @Transactional
    public void deleteProductCombined(String id){
        productCombinedDao.delete(id);
    }

    /**
     *  设置产品类别信息，用户新增或更新时
     * @param product
     */
    private void setProductCategoryInfo(ProductCombined product){
        if(StringUtils.isNotBlank(product.getCategoryId())){
            ProductCategory category = productCategoryService.getProductCategory(product.getCategoryId());
            product.setCategoryCnName(category.getCnName());
            product.setCategoryEnName(category.getEnName());
        }
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<ProductCombined> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<ProductCombined> spec = DynamicSpecifications.bySearchFilter(filters.values(), ProductCombined.class);
        return spec;
    }
}
