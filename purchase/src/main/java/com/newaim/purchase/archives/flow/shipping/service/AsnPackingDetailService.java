package com.newaim.purchase.archives.flow.shipping.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseContractDetailService;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseContractService;
import com.newaim.purchase.archives.flow.shipping.dao.AsnPackingDetailDao;
import com.newaim.purchase.archives.flow.shipping.entity.Asn;
import com.newaim.purchase.archives.flow.shipping.entity.AsnPacking;
import com.newaim.purchase.archives.flow.shipping.entity.AsnPackingDetail;
import com.newaim.purchase.archives.flow.shipping.vo.AsnPackingDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class AsnPackingDetailService extends ServiceBase {

    @Autowired
    private AsnPackingDetailDao asnPackingDetailDao;

    @Autowired
    private AsnPackingService asnPackingService;

    @Autowired
    private AsnService asnService;

    @Autowired
    private PurchaseContractDetailService purchaseContractDetailService;

    @Autowired
    private PurchaseContractService purchaseContractService;

    /**
     * 根据asnPackingId返回所有明细
     * @param asnPackingId 业务ID
     * @return detail集合
     */
    public List<AsnPackingDetailVo> findPackingDetailsVoByAsnPackingId(String asnPackingId){
        List<AsnPackingDetail> list = findPackingDetailsByAsnPackingId(asnPackingId);
        try{
            List<AsnPackingDetailVo> voList = BeanMapper.mapList(list, AsnPackingDetail.class, AsnPackingDetailVo.class);
            return voList;
        } catch (Exception e){
            String msg = e.getMessage();
        }
        return null;

    }

    /**
     * 根据asnPackingId返回所有明细
     * @param asnPackingId 业务ID
     * @return detail集合
     */
    public List<AsnPackingDetail> findPackingDetailsByAsnPackingId(String asnPackingId){
        return asnPackingDetailDao.findPackingDetailsByAsnPackingId(asnPackingId);
    }

    /**
     * 根据AsnId找SKU明细
     * @return
     */
    public List<AsnPackingDetail> findPackingDetailsByAsnId(String asnId){
        return asnPackingDetailDao.findPackDetailsByAsnId(asnId);
    }

    /**
     * 根据AsnId找SKU明细VO
     * @return
     */
    public List<AsnPackingDetailVo> findPackingDetailsVoByAsnId(String asnId){
        return BeanMapper.mapList(findPackingDetailsByAsnId(asnId),AsnPackingDetail.class,AsnPackingDetailVo.class);
    }

    /**
     * 分页查询所有asn明细
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<AsnPackingDetailVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<AsnPackingDetail> spec = buildSpecification(params);
        Page<AsnPackingDetail> p = asnPackingDetailDao.findAll(spec, pageRequest);
        Page<AsnPackingDetailVo> page = p.map(asnPackingDetail ->{
            AsnPackingDetailVo vo = convertToAsnPackingDetailVo(asnPackingDetail);
            AsnPacking asnPacking =asnPackingService.getAsnPacking(vo.getAsnPackingId());
            if (asnPacking!=null){
                Asn asn = asnService.getAsn(asnPacking.getAsnId());
                vo.setAsnNumber(asn.getAsnNumber());
            }
            return vo;
        });
        return page;
    }

    List<AsnPackingDetailVo> findPackDetailsByOrderId(String orderId){
        return BeanMapper.mapList(asnPackingDetailDao.findPackDetailsByOrderId(orderId), AsnPackingDetail.class, AsnPackingDetailVo.class);
    }

    public List findMergeDetailVosByOrderId(String orderId){
        List<AsnPackingDetailVo> destDetails = Lists.newArrayList();
        List<AsnPackingDetailVo> asnPackingDetails = findPackDetailsByOrderId(orderId);
        Map<String, List<AsnPackingDetailVo>> temp = Maps.newHashMap();
        for (int i = 0; i < asnPackingDetails.size(); i++) {
            AsnPackingDetailVo asnPackingDetail = asnPackingDetails.get(i);
            String productId = asnPackingDetail.getProductId();
            if(temp.containsKey(productId)){
                temp.get(productId).add(asnPackingDetail);
            }else{
                List<AsnPackingDetailVo> newDetails = Lists.newArrayList();
                newDetails.add(asnPackingDetail);
                temp.put(productId, newDetails);
                destDetails.add(asnPackingDetail);
            }
        }
        //对有重复SKU做价格合并
        Map<String,AsnPackingDetailVo> priceMap = Maps.newHashMap();
        for (String productId : temp.keySet()) {
            List<AsnPackingDetailVo> details = temp.get(productId);
            if(details.size() > 1){
                BigDecimal totalExpectedQty = BigDecimal.ZERO;
                BigDecimal totalExpectedCartons = BigDecimal.ZERO;
                BigDecimal totalReceivedQty = BigDecimal.ZERO;
                BigDecimal totalReceivedCartons = BigDecimal.ZERO;
                List<String> asnIds = Lists.newArrayList();
                List<String> asnNumbers = Lists.newArrayList();
                for (int i = 0; i < details.size(); i++) {
                    AsnPackingDetailVo detail = details.get(i);
                    totalExpectedQty = totalExpectedQty.add(BigDecimal.valueOf(detail.getExpectedQty()));
                    totalExpectedCartons = totalExpectedCartons.add(BigDecimal.valueOf(detail.getExpectedCartons()));
                    totalReceivedQty = totalReceivedQty.add(BigDecimal.valueOf(detail.getReceivedQty()));
                    totalReceivedCartons = totalReceivedCartons.add(BigDecimal.valueOf(detail.getReceivedCartons()));
                    if(!asnIds.contains(detail.getAsnId())){
                        asnIds.add(detail.getAsnId());
                        asnNumbers.add(detail.getAsnNumber());
                    }
                }
                AsnPackingDetailVo priceDetail = new AsnPackingDetailVo();
                priceDetail.setAsnId(String.join(",", asnIds));
                priceDetail.setAsnNumber(String.join(",", asnNumbers));
                priceDetail.setExpectedQty(totalExpectedQty.intValue());
                priceDetail.setExpectedCartons(totalExpectedCartons.intValue());
                priceDetail.setReceivedQty(totalReceivedQty.intValue());
                priceDetail.setReceivedCartons(totalReceivedCartons.intValue());
                priceMap.put(productId, priceDetail);
            }
        }

        //回写合并后的价格和数量
        for (int i = 0; i < destDetails.size(); i++) {
            AsnPackingDetailVo detail = destDetails.get(i);
            if(priceMap.containsKey(detail.getProductId())){
                AsnPackingDetailVo priceDetail = priceMap.get(detail.getProductId());
                priceDetail.setExpectedQty(priceDetail.getReceivedQty());
                priceDetail.setExpectedCartons(priceDetail.getExpectedCartons());
                priceDetail.setReceivedQty(priceDetail.getExpectedQty());
                priceDetail.setReceivedCartons(priceDetail.getReceivedCartons());
            }
        }
        return destDetails;
    }

    /**
     * 将entity转换为Vo
     *
     * @param asnPackingDetail
     * @return
     */
    private AsnPackingDetailVo convertToAsnPackingDetailVo(AsnPackingDetail asnPackingDetail) {
        AsnPackingDetailVo vo = BeanMapper.map(asnPackingDetail, AsnPackingDetailVo.class);
        return vo;
    }

    private Specification<AsnPackingDetail> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<AsnPackingDetail> spec = DynamicSpecifications.bySearchFilter(filters.values(), AsnPackingDetail.class);
        return spec;
    }




}
