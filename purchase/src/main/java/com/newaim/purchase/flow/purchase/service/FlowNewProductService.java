package com.newaim.purchase.flow.purchase.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.product.dao.ProductVendorPropDao;
import com.newaim.purchase.archives.product.entity.ProductVendorProp;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.flow.purchase.dao.FlowNewProductDao;
import com.newaim.purchase.flow.purchase.dao.FlowNewProductDetailDao;
import com.newaim.purchase.flow.purchase.entity.FlowNewProduct;
import com.newaim.purchase.flow.purchase.entity.FlowNewProductDetail;
import com.newaim.purchase.flow.purchase.vo.FlowNewProductVo;
import com.newaim.purchase.flow.workflow.entity.FlowOperatorHistory;
import com.newaim.purchase.flow.workflow.service.FlowServiceBase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.LinkedHashMap;

/**
 * @author Mark
 * @date 2017/9/18
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowNewProductService extends FlowServiceBase {

    private static final String FILE_MODULE_NAME =  ConstantsAttachment.Status.FlowNewProduct.code;

    @Autowired
    private FlowNewProductDao flowNewProductDao;

    @Autowired
    private FlowNewProductDetailDao flowNewProductDetailDao;

    @Autowired
    private FlowNewProductDetailService flowNewProductDetailService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private ProductVendorPropDao productVendorPropDao;

    @Autowired
    private AttachmentService attachmentService;

    public Page<FlowNewProductVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowNewProduct> spec = buildSpecification(params);
        Page<FlowNewProduct> p = flowNewProductDao.findAll(spec, pageRequest);
        Page<FlowNewProductVo> page = p.map(new Converter<FlowNewProduct, FlowNewProductVo>() {
		    @Override
		    public FlowNewProductVo convert(FlowNewProduct flowNewProduct) {
		        return convertToFlowNewProductVo(flowNewProduct);
		    }
		});
        return page;
    }

    private FlowNewProductVo convertToFlowNewProductVo(FlowNewProduct flowNewProduct){
        FlowNewProductVo vo = BeanMapper.map(flowNewProduct, FlowNewProductVo.class);
        return vo;
    }

    public FlowNewProduct getFlowNewProduct(String id){
        return flowNewProductDao.findOne(id);
    }

    public FlowNewProductVo get(String id){
    	FlowNewProductVo vo =convertToFlowNewProductVo(getFlowNewProduct(id));
        vo.setAttachments(attachmentService.listByBusinessId(id) );
        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));
        }
        vo.setDetails(flowNewProductDetailService.findDetailsVoByBusinessId(id));
        return vo;
    }
    
    @Transactional(rollbackFor = Exception.class)
	public FlowNewProduct add(FlowNewProduct flowNewProduct, List<FlowNewProductDetail> details, List<Attachment> attachments){
        //设置创建人信息
        setFlowCreatorInfo(flowNewProduct);
        //设置供应商信息
        setFlowVendorInfo(flowNewProduct);
        //设置价格信息
        setFlowNewProductPriceInfo(flowNewProduct, details);
        flowNewProduct = flowNewProductDao.save(flowNewProduct);
        saveDetails(flowNewProduct.getId(), details);
        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowNewProduct.getId());

    	return flowNewProduct;
	}

    @Transactional(rollbackFor = Exception.class)
    public FlowNewProduct update(FlowNewProduct flowNewProduct, List<FlowNewProductDetail> details, List<Attachment> attachments){
        //设置供应商信息
        setFlowVendorInfo(flowNewProduct);

        setFlowNewProductPriceInfo(flowNewProduct, details);
        //设置更新时间
        flowNewProduct.setUpdatedAt(new Date());

        //删除之前明细，重新绑定数据
        flowNewProductDetailDao.deleteByBusinessId(flowNewProduct.getId());
        saveDetails(flowNewProduct.getId(),details);
        //更新附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowNewProduct.getId());

        return flowNewProductDao.save(flowNewProduct);
    }

    /**
     * 复制另存
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowNewProduct saveAs(FlowNewProduct flowNewProduct, List<FlowNewProductDetail> details, List<Attachment> attachments){
        flowNewProductDao.clear();
        //设置创建人信息
        setFlowCreatorInfo(flowNewProduct);
        //设置供应商信息
        setFlowVendorInfo(flowNewProduct);
        //设置产品价格信息
        setFlowNewProductPriceInfo(flowNewProduct, details);
        //清理信息
        cleanInfoForSaveAs(flowNewProduct);
        //保存
        flowNewProduct = flowNewProductDao.save(flowNewProduct);
        saveDetails(flowNewProduct.getId(), details);

        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowNewProduct.getId());
    	return flowNewProduct;
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(String businessId, List<FlowNewProductDetail> details){
        if(details != null){
            for (int i = 0; i < details.size(); i++) {
                FlowNewProductDetail detail =  details.get(i);
                detail.setBusinessId(businessId);
                ProductVendorProp prop = productVendorPropDao.findProductVendorPropByProductId(detail.getProductId());
                prop.setCompetitorPriceAud(detail.getCompetitorPriceAud());
                prop.setCompetitorPriceRmb(detail.getCompetitorPriceRmb());
                prop.setCompetitorPriceUsd(detail.getCompetitorPriceUsd());
                prop.setCompetitorSaleRecord(detail.getCompetitorSaleRecord());
                prop.setEbayMonthlySalesAud(detail.getEbayMonthlySalesAud());
                prop.setEbayMonthlySalesRmb(detail.getEbayMonthlySalesRmb());
                prop.setEbayMonthlySalesUsd(detail.getEbayMonthlySalesUsd());
            }
            flowNewProductDetailDao.save(details);
        }
    }

    /**
     * 设置总价信息
     */
    private void setFlowNewProductPriceInfo(FlowNewProduct flowNewProduct, List<FlowNewProductDetail> details){
        BigDecimal totalPriceAud = BigDecimal.ZERO;
        BigDecimal totalPriceRmb = BigDecimal.ZERO;
        BigDecimal totalPriceUsd = BigDecimal.ZERO;
        Integer totalOrderQty = 0;
        BigDecimal totalOrderValueAud = BigDecimal.ZERO;
        BigDecimal totalOrderValueRmb = BigDecimal.ZERO;
        BigDecimal totalOrderValueUsd = BigDecimal.ZERO;
        if(details != null) {
            for(FlowNewProductDetail detail : details){
                if(detail.getPriceAud() != null && detail.getOrderQty()!= null){
                    totalPriceAud = totalPriceAud.add(detail.getPriceAud().multiply(BigDecimal.valueOf(detail.getOrderQty())));
                }
                if(detail.getPriceRmb() != null && detail.getOrderQty() != null){
                    totalPriceRmb = totalPriceRmb.add(detail.getPriceRmb().multiply(BigDecimal.valueOf(detail.getOrderQty())));
                }
                if(detail.getPriceUsd() != null && detail.getOrderQty() != null){
                    totalPriceUsd = totalPriceUsd.add(detail.getPriceUsd().multiply(BigDecimal.valueOf(detail.getOrderQty())));
                }
                if(detail.getOrderQty() != null){
                    totalOrderQty += detail.getOrderQty();
                }
                if(detail.getOrderValueAud() != null){
                    totalOrderValueAud = totalOrderValueAud.add(detail.getOrderValueAud());
                }
                if(detail.getOrderValueRmb() != null){
                    totalOrderValueRmb = totalOrderValueRmb.add(detail.getOrderValueRmb());
                }
                if(detail.getOrderValueUsd() != null){
                    totalOrderValueUsd = totalOrderValueUsd.add(detail.getOrderValueUsd());
                }
                if (detail.getCurrency() == null){
                    detail.setCurrency(flowNewProduct.getCurrency());
                }
            }
        }
        flowNewProduct.setTotalPriceAud(totalPriceAud);
        flowNewProduct.setTotalPriceRmb(totalPriceRmb);
        flowNewProduct.setTotalPriceUsd(totalPriceUsd);
        flowNewProduct.setTotalOrderQty(totalOrderQty);
        flowNewProduct.setTotalOrderValueAud(totalOrderValueAud);
        flowNewProduct.setTotalOrderValueRmb(totalOrderValueRmb);
        flowNewProduct.setTotalOrderValueUsd(totalOrderValueUsd);
    }
	
    @Transactional(rollbackFor = Exception.class)
	public void delete(FlowNewProduct flowNewProduct){
        flowNewProduct.setUpdatedAt(new Date());
        flowNewProduct.setStatus(Constants.Status.DELETED.code);
        flowNewProductDao.save(flowNewProduct);
	}

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
	public void hold(String flowId){
	    FlowNewProduct flow = flowNewProductDao.findOne(flowId);
	    flow.setHold(Constants.HoldStatus.HOLD.code);
	    flowNewProductDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowNewProduct flow = flowNewProductDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowNewProductDao.save(flow);
    }

    private Specification<FlowNewProduct> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowNewProduct> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowOperatorHistory.class);
        return spec;
    }

    public List<FlowNewProduct> findByProductQuotationBusinessId( String productQuotationBusinessId){
        return flowNewProductDao.findByProductQuotationBusinessId(productQuotationBusinessId);
    }
}
