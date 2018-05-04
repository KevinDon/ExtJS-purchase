package com.newaim.purchase.archives.flow.purchase.service;


import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.flow.purchase.dao.PurchasePlanDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchasePlan;
import com.newaim.purchase.archives.flow.purchase.vo.PurchasePlanVo;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.flow.purchase.dao.FlowPurchasePlanDao;
import com.newaim.purchase.flow.purchase.dao.FlowPurchasePlanDetailDao;
import com.newaim.purchase.flow.purchase.entity.FlowPurchasePlan;
import com.newaim.purchase.flow.purchase.entity.FlowPurchasePlanDetail;
import com.newaim.purchase.flow.purchase.service.FlowPurchasePlanDetailService;
import com.newaim.purchase.flow.purchase.vo.FlowPurchasePlanVo;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class PurchasePlanService extends FlowServiceBase {

    @Autowired
    private PurchasePlanDao purchasePlanDao;


    @Autowired
    private FlowPurchasePlanDetailService flowPurchasePlanDetailService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private AttachmentService attachmentService;

    /**
     * 分页查询List
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<PurchasePlanVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<PurchasePlan> spec = buildSpecification(params);
        Page<PurchasePlan> p = purchasePlanDao.findAll(spec, pageRequest);
        Page<PurchasePlanVo> page = p.map(new Converter<PurchasePlan, PurchasePlanVo>() {
            @Override
            public PurchasePlanVo convert(PurchasePlan purchasePlan) {
                return convertToFlowPurchasePlanVo(purchasePlan);
            }
        });
        return page;
    }

    /**
     * 将entity转换成Vo
     * @param purchasePlan
     * @return
     */
    private PurchasePlanVo convertToFlowPurchasePlanVo(PurchasePlan purchasePlan){
        PurchasePlanVo vo = BeanMapper.map(purchasePlan, PurchasePlanVo.class);
        return vo;
    }

    public PurchasePlan getPurchasePlan(String id){
        return purchasePlanDao.findOne(id);
    }

    /**
     * 根据ID获得附件、供应商、detail信息
     * @param id
     * @return
     */
    public PurchasePlanVo get(String id){
        PurchasePlanVo vo = convertToFlowPurchasePlanVo(getPurchasePlan(id));
        vo.setAttachments(attachmentService.listByBusinessId(id));
        if (StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));
        }
        vo.setDetails(flowPurchasePlanDetailService.findDetailsVoByBusinessId(id));
        return vo;
    }


    private Specification<PurchasePlan> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<PurchasePlan> spec = DynamicSpecifications.bySearchFilter(filters.values(), PurchasePlan.class);
        return spec;
    }

}
