package com.newaim.purchase.desktop.reports.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.product.service.ProductService;
import com.newaim.purchase.desktop.reports.dao.ReportsProductDao;
import com.newaim.purchase.desktop.reports.entity.ReportsProduct;
import com.newaim.purchase.desktop.reports.vo.ReportsProductVo;
import com.newaim.purchase.desktop.reports.vo.ReportsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly=true, rollbackFor = Exception.class)
public class ReportsProductService extends ServiceBase {


	@Autowired
   	private ProductService productService;

    @Autowired
    private ReportsProductDao reportsProductDao;

    @Autowired
    private ReportsProductTroubleDetailService reportsProductTroubleDetailService;

	/**
	 * 标准的查询符合条件的记录
	 * @param params
	 * @param pageNumber
	 * @param pageSize
	 * @param sort
	 * @return
	 */
	public Page<ReportsProductVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
		Specification<ReportsProduct> spec = buildSpecification(params);
		Page<ReportsProduct> p = reportsProductDao.findAll(spec, pageRequest);
		Page<ReportsProductVo> page = p.map(new Converter<ReportsProduct, ReportsProductVo>() {
			@Override
			public ReportsProductVo convert(ReportsProduct dv) {
				return BeanMapper.map(dv, ReportsProductVo.class);
			}
		});

		return page;
	}

	//获取生效的报告
	public List<ReportsVo> listByProductId(String id) {

		List<ReportsVo> rp = listByProductId(id, 1);

		return rp;
	}

	/**
	 * 根据product id 获取相关报告
	 * @param id
	 * @param status  0为未生效; 1为生效的;　2为失效的; 3为删除的; 4为全部的
	 * @return
	 */
    public List<ReportsVo> listByProductId(String id, Integer status) {

	    List<ReportsVo> rp = reportsProductDao.listReportsByProductId(id, status);

        return rp;
    }

	/**
	 * 根据产品ID数组获取相关报告
	 * @param ids
	 * @param status  0为未生效; 1为生效的;　2为失效的; 3为删除的; 4为全部的
	 * @return
	 */
	public List<ReportsVo> listByProductIds(String[] ids, Integer status) {

	    List<ReportsVo> rp = reportsProductDao.listReportsByProductIds(ids, status);

        return rp;
    }


    /**
     * 根据关联业务ID查找报告
     * @param businessId
     * @param status
     * @return
     */
    public List<ReportsVo> listByBusinessId (String businessId, Integer status){

        List<ReportsVo> rp = reportsProductDao.listReportsByBusinessId(businessId, status);

        return rp;
    }

    /**
     * 根据供应商ID查找报告
     * @param vendorId
     * @param status
     * @return
     */
    public List<ReportsVo> listReportsByVendorId (String vendorId, Integer status){

        List<ReportsVo> rp = reportsProductDao.listReportsByVendorId(vendorId, status);

        return rp;
    }

    /**
     * 根据报告ID找相关产品
     * @param reportsId
     * @return
     */
    public List<ReportsProductVo> listByReportsId (String reportsId){

        List<ReportsProductVo> rp = BeanMapper.mapList(reportsProductDao.findDetailsByReportsId(reportsId), ReportsProduct.class, ReportsProductVo.class);

        return rp;
    }

	@Transactional(rollbackFor = Exception.class)
	public void save(ReportsProduct reportsProduct){
		reportsProductDao.save(reportsProduct);
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteDetailsByReportsId(String reportsId){
		if(StringUtils.isNotBlank(reportsId)){
			reportsProductDao.deleteDeltailsByReportsId(reportsId);
		}
	}

	/**
	 * 查找报告产品明细
	 * @param reportsId
	 * @return
	 */
	public List<ReportsProduct> findDetailsByReportsId(String reportsId){
		return reportsProductDao.findDetailsByReportsId(reportsId);
	}

	/**
	 * 根据reportsId查找报告产品明细（安检报告)
	 * @param reportsId
	 * @return
	 */
	public List<ReportsProductVo> findDetailsVoByReportsId(String reportsId){
		List<ReportsProduct> rps = reportsProductDao.findDetailsByReportsId(reportsId);

		if(null == rps) return null;

		List<ReportsProductVo> data = BeanMapper.mapList(rps, ReportsProduct.class, ReportsProductVo.class);
		if(data != null && data.size() > 0){
			for (int i = 0; i < data.size(); i++) {
				ReportsProductVo vo = data.get(i);
				vo.setProduct(productService.getBase(vo.getProductId()));
				vo.setTroubleDetails(reportsProductTroubleDetailService.findDetailsVoByReportsIdAndProductId(reportsId, vo.getProductId()));
			}
		}
		return data;
	}

	public ReportsProduct getReportsProduct(String id){
		return reportsProductDao.getOne(id);
	}

	public ReportsProductVo get(String id){
		return BeanMapper.map(reportsProductDao.getOne(id), ReportsProductVo.class);
	}


	private Specification<ReportsProduct> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<ReportsProduct> spec = DynamicSpecifications.bySearchFilter(filters.values(), ReportsProduct.class);
		return spec;
	}

}
