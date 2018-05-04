package com.newaim.purchase.archives.flow.purchase.service;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.flow.purchase.dao.CustomClearancePackingDao;
import com.newaim.purchase.archives.flow.purchase.dao.CustomClearancePackingDetailDao;
import com.newaim.purchase.archives.flow.purchase.entity.CustomClearancePacking;
import com.newaim.purchase.archives.flow.purchase.entity.CustomClearancePackingDetail;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearancePackingDetailVo;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseContractDetailVo;
import com.newaim.purchase.archives.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CustomClearancePackingDetailService {

    @Autowired
    private CustomClearancePackingDetailDao customClearancePackingDetailDao;

    @Autowired
    private CustomClearancePackingDao customClearancePackingDao;

    @Autowired
    private ProductService productService;

    public CustomClearancePackingDetail getCustomClearancePackingDetail(String id){
        return customClearancePackingDetailDao.findOne(id);
    }

    public CustomClearancePackingDetailVo get(String id){
        return BeanMapper.map(getCustomClearancePackingDetail(id), CustomClearancePackingDetailVo.class);
    }

    /**
     * 根据装柜单ID返回装柜单明细
     * @param packingId
     * @return
     */
    public List<CustomClearancePackingDetail> findPackingDetailsByPackingId(String packingId){
        return customClearancePackingDetailDao.findPackingDetailsByPackingId(packingId);
    }


    /**
     * 根据装柜单ID返回装柜单明细
     * @param packingId
     * @return
     */
    public List<CustomClearancePackingDetailVo> findPackingDetailsVoByPackingId(String packingId){
        List<CustomClearancePackingDetail> data = findPackingDetailsByPackingId(packingId);
        List<CustomClearancePackingDetailVo> result = BeanMapper.mapList(findPackingDetailsByPackingId(packingId), CustomClearancePackingDetail.class, CustomClearancePackingDetailVo.class);
        if (data != null){
            for (int i = 0; i< data.size();i++){
                CustomClearancePackingDetailVo vo = result.get(i);
                vo.setProduct(productService.get(vo.getProductId()));
            }
        }
        return result;
    }

    /**
     * 根据装柜ID查找合并后的装柜单明细（相同的SKU会做数量合并）
     * @param detailVos
     * @return
     */
    public List<CustomClearancePackingDetailVo> findMergeDetailVosByPackingId(List<CustomClearancePackingDetailVo> detailVos){
        List<CustomClearancePackingDetailVo> destDetails = Lists.newArrayList();
        Map<String, List<CustomClearancePackingDetailVo>> temp = Maps.newHashMap();
        List<CustomClearancePackingDetailVo> vos = detailVos;
        for (int i = 0; i<vos.size();i++){
            CustomClearancePackingDetailVo detailVo = vos.get(i);
            String productId = detailVo.getProductId();
            if (temp.containsKey(productId)) {
                temp.get(productId).add(detailVo);
            }else {
                List<CustomClearancePackingDetailVo> newDetails = Lists.newArrayList();
                newDetails.add(detailVo);
                temp.put(productId, newDetails);
                destDetails.add(detailVo);
            }
        }
        Map<String,CustomClearancePackingDetailVo> qtyMap = Maps.newHashMap();
        for (String productId : temp.keySet()){
            List<CustomClearancePackingDetailVo> details = temp.get(productId);
            if (details.size() > 1){
                BigDecimal totalOrderQty = BigDecimal.ZERO;
                BigDecimal totalCartons = BigDecimal.ZERO;
                BigDecimal totalPackingQty = BigDecimal.ZERO;
                BigDecimal totalPackingCartons = BigDecimal.ZERO;
                for (int i = 0; i < details.size(); i++) {
                    CustomClearancePackingDetailVo detail = details.get(i);
                    totalOrderQty = totalOrderQty.add(BigDecimal.valueOf(detail.getOrderQty()));
                    totalCartons = totalCartons.add(BigDecimal.valueOf(detail.getCartons()));
                    totalPackingQty = totalPackingQty.add(BigDecimal.valueOf(detail.getPackingQty()));
                    totalPackingCartons = totalPackingCartons.add(BigDecimal.valueOf(detail.getPackingCartons()));
                }
                CustomClearancePackingDetailVo packingDetailVo = new CustomClearancePackingDetailVo();
                packingDetailVo.setOrderQty(totalOrderQty.intValue());
                packingDetailVo.setCartons(totalCartons.intValue());
                packingDetailVo.setPackingQty(totalPackingQty.intValue());
                packingDetailVo.setPackingCartons(totalPackingCartons.intValue());
                qtyMap.put(productId, packingDetailVo);
            }
        }
        //回写合并后数量
        for (int i = 0; i < destDetails.size(); i++) {
            CustomClearancePackingDetailVo detail = destDetails.get(i);
            if(qtyMap.containsKey(detail.getProductId())){
                CustomClearancePackingDetailVo packingDetailVo = qtyMap.get(detail.getProductId());
                detail.setPackingQty(packingDetailVo.getPackingQty());
                detail.setPackingCartons(packingDetailVo.getPackingCartons());
                detail.setOrderQty(detail.getPackingQty());
                detail.setCartons(detail.getPackingCartons());
            }
        }
        return destDetails;
    }


    /**
     * 根据产品Id返回装柜单明细
     * @param productId
     * @return
     */
    public List<CustomClearancePackingDetail> findPackingDetailsByProductId(String productId){
        return customClearancePackingDetailDao.findPackingDetailsByProductId(productId);
    }

    /**
     * 根据产品Id返回装柜单明细
     * @param productId
     * @return
     */
    public List<CustomClearancePackingDetailVo> findPackingDetailsVoByProductId(String productId){
        return BeanMapper.mapList(findPackingDetailsByProductId(productId), CustomClearancePackingDetail.class, CustomClearancePackingDetailVo.class);
    }

    /**
     * 根据装柜单ID返回装柜单明细
     * @param packingIds
     * @return
     */
    public List<CustomClearancePackingDetail> findPackingDetailsByPackingIds(List<String> packingIds){
        return customClearancePackingDetailDao.findPackingDetailsByPackingIdIn(packingIds);
    }


    /**
     * 根据装柜单ID返回装柜单明细
     * @param packingIds
     * @return
     */
    public List<CustomClearancePackingDetailVo> findPackingDetailsVoByPackingIds(List<String> packingIds){
        return BeanMapper.mapList(findPackingDetailsByPackingIds(packingIds), CustomClearancePackingDetail.class, CustomClearancePackingDetailVo.class);
    }

    public CustomClearancePackingDetail findPackingDetailByPackingIdAndProductId(String packingId, String productId){
        return customClearancePackingDetailDao.findPackingDetailByPackingIdAndProductId(packingId, productId);
    }

    /**
     * 根据装柜编号返回装柜单明细
     * @param containerNumber
     * @return
     */
    public List<CustomClearancePackingDetail> findPackingDetailsByPackingNumber(String containerNumber){
        List<CustomClearancePackingDetail> result = Lists.newArrayList();
        List<CustomClearancePacking> packings = customClearancePackingDao.findPackingsByContainerNumber(containerNumber);
        List<String> packingIds = getPackingIdsByPackings(packings);
        if(packingIds != null && packingIds.size() > 0){
            result = findPackingDetailsByPackingIds(packingIds);
        }
        return result;
    }

    /**
     * 根据装柜编号返回装柜单明细
     * @param containerNumbers
     * @return
     */
    public List<CustomClearancePackingDetail> findPackingDetailsByPackingNumbers(List<String> containerNumbers){
        List<CustomClearancePackingDetail> result = Lists.newArrayList();
        List<CustomClearancePacking> packings = customClearancePackingDao.findPackingsByContainerNumberIn(containerNumbers);
        List<String> packingIds = getPackingIdsByPackings(packings);
        if(packingIds != null && packingIds.size() > 0){
            result = findPackingDetailsByPackingIds(packingIds);
        }
        return result;
    }

    private List<String> getPackingIdsByPackings(List<CustomClearancePacking> packings){
        List<String> packingIds = Lists.newArrayList();
        if(packings != null && packings.size() > 0){
            for (int i = 0; i < packings.size(); i++) {
                packingIds.add(packings.get(i).getId());
            }
        }
        return packingIds;
    }

    /**
     * 根据装柜编号返回装柜单明细
     * @param containerNumber
     * @return
     */
    public List<CustomClearancePackingDetailVo> findPackingDetailsVoByPackingNumber(String containerNumber){
        return BeanMapper.mapList(findPackingDetailsByPackingNumber(containerNumber), CustomClearancePackingDetail.class, CustomClearancePackingDetailVo.class);
    }



}
