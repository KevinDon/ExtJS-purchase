package com.newaim.purchase.archives.flow.shipping.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.flow.shipping.dao.ServiceProviderQuotationPortDao;
import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotationPort;
import com.newaim.purchase.archives.flow.shipping.vo.ServiceProviderQuotationPortVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ServiceProviderQuotationPortService {

    @Autowired
    private ServiceProviderQuotationPortDao serviceProviderQuotationPortDao;


    /**
     * 根据询价单id查询所有港口报价信息
     * @param serviceProviderQuotationId
     * @return
     */
    public List<ServiceProviderQuotationPort> listPortsByServiceProviderQuotationId(String serviceProviderQuotationId){
        return serviceProviderQuotationPortDao.findByServiceProviderQuotationId(serviceProviderQuotationId);
    }

    /**
     * 根据询价单id查询所有港口报价信息
     * @param serviceProviderQuotationId
     * @return
     */
    public List<ServiceProviderQuotationPortVo> listPortVosByServiceProviderQuotationId(String serviceProviderQuotationId){
        return BeanMapper.mapList(listPortsByServiceProviderQuotationId(serviceProviderQuotationId), ServiceProviderQuotationPort.class, ServiceProviderQuotationPortVo.class);
    }

    public List<ServiceProviderQuotationPort> listPorts(String serviceProviderId, String type){
        return serviceProviderQuotationPortDao.listPorts(serviceProviderId, type);
    }

    public List<ServiceProviderQuotationPort> listPorts(String serviceProviderId, String type, String orderShippingPlanId){
        return serviceProviderQuotationPortDao.listPorts(serviceProviderId, type, orderShippingPlanId);
    }

   public   List<ServiceProviderQuotationPortVo> listPorts(String shippingPlanid) {
        return BeanMapper.mapList(serviceProviderQuotationPortDao.listports(shippingPlanid), ServiceProviderQuotationPort.class, ServiceProviderQuotationPortVo.class);
    }

}
