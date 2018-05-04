package com.newaim.purchase.archives.flow.purchase.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.archives.flow.purchase.dao.PurchasePlanDetailViewDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchasePlan;
import com.newaim.purchase.archives.flow.purchase.entity.PurchasePlanDetailView;
import com.newaim.purchase.archives.flow.purchase.vo.PurchasePlanDetailViewVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.LinkedHashMap;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class PurchasePlanDetailViewService extends ServiceBase {

    @Autowired
    private PurchasePlanDetailViewDao purchasePlanDetailViewDao;

    @Autowired
    private PurchasePlanService purchasePlanService;

    public List<PurchasePlanDetailViewVo> listAllByVendor(String vendorId){
        UserVo user = SessionUtils.currentUserVo();
        List<PurchasePlanDetailView> data = purchasePlanDetailViewDao.findAllByVendor(vendorId, user.getId());
        List<PurchasePlanDetailViewVo> result = BeanMapper.mapList(data, PurchasePlanDetailView.class, PurchasePlanDetailViewVo.class);
        if(result != null && result.size() > 0){
            for (int i = 0; i < result.size(); i++) {
                PurchasePlanDetailViewVo vo = result.get(i);
                PurchasePlan purchasePlan = purchasePlanService.getPurchasePlan(vo.getPurchasePlanId());
                vo.setFlowPurchasePlanId(purchasePlan.getBusinessId());
            }
        }
        return result;
    }

    public Page<PurchasePlanDetailViewVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<PurchasePlanDetailView> spec = buildSpecification(params);
        Page<PurchasePlanDetailView> p = purchasePlanDetailViewDao.findAll(spec, pageRequest);
        Page<PurchasePlanDetailViewVo> page = p.map(purchasePlanDetailView -> {
            PurchasePlanDetailViewVo vo = convertToPurchasePlanDetailViewVo(purchasePlanDetailView);
            PurchasePlan purchasePlan = purchasePlanService.getPurchasePlan(vo.getPurchasePlanId());
            vo.setFlowPurchasePlanId(purchasePlan.getBusinessId());
            return vo;
        });
        return page;
    }

    private PurchasePlanDetailViewVo convertToPurchasePlanDetailViewVo(PurchasePlanDetailView purchasePlanDetailView){
        PurchasePlanDetailViewVo vo = BeanMapper.map(purchasePlanDetailView, PurchasePlanDetailViewVo.class);
        return vo;
    }

    private Specification<PurchasePlanDetailView> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<PurchasePlanDetailView> spec = DynamicSpecifications.bySearchFilter(filters.values(), PurchasePlanDetailView.class);
        return spec;
    }

    /**
     * 根据id获取采购计划信息
     * @param id
     * @return
     */
    public PurchasePlanDetailView getPurchasePlanDetailView(String id){
        return purchasePlanDetailViewDao.findOne(id);
    }

    /**
     * 根据id获取采购计划信息
     * @param id
     * @return
     */
    public PurchasePlanDetailViewVo get(String id){
        PurchasePlanDetailViewVo vo =convertToPurchasePlanDetailViewVo(getPurchasePlanDetailView(id));
        return vo;
    }




}
