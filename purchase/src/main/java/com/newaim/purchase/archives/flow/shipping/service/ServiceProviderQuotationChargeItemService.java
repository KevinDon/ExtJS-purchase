package com.newaim.purchase.archives.flow.shipping.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.flow.shipping.dao.ServiceProviderQuotationChargeItemDao;
import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotationChargeItem;
import com.newaim.purchase.archives.flow.shipping.vo.ServiceProviderQuotationChargeItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ServiceProviderQuotationChargeItemService {

    @Autowired
    private ServiceProviderQuotationChargeItemDao serviceProviderQuotationChargeItemDao;


    /**
     * 根据询价单id查询所有收费项目报价信息
     * @param serviceProviderQuotationId
     * @return
     */
    public List<ServiceProviderQuotationChargeItem> listChargeItemsByServiceProviderQuotationId(String serviceProviderQuotationId){
        return serviceProviderQuotationChargeItemDao.findByServiceProviderQuotationId(serviceProviderQuotationId);
    }

    /**
     * 根据询价单id查询所有收费项目报价信息
     * @param serviceProviderQuotationId
     * @return
     */
    public List<ServiceProviderQuotationChargeItemVo> listChargeItemsVosByServiceProviderQuotationId(String serviceProviderQuotationId){
        return BeanMapper.mapList(listChargeItemsByServiceProviderQuotationId(serviceProviderQuotationId), ServiceProviderQuotationChargeItem.class, ServiceProviderQuotationChargeItemVo.class);
    }

    /**
     * 通过服务商id和类型查找报价
     * @param serviceProviderId 服务商id
     * @param type 类型
     * @return 港口报价
     */
    public List<ServiceProviderQuotationChargeItem> listChargeItems(String serviceProviderId, String type){
        return serviceProviderQuotationChargeItemDao.listChargeItems(serviceProviderId, type);
    }

    /**
     * 通过服务商id和类型查找报价
     * @param serviceProviderId 服务商id
     * @param type 类型
     * @return 港口报价
     */
    public List<ServiceProviderQuotationChargeItem> listChargeItems(String serviceProviderId, String type, String orderShippingPlanId){
        return serviceProviderQuotationChargeItemDao.listChargeItems(serviceProviderId, type, orderShippingPlanId);
    }


}
