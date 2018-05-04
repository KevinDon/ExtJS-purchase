package com.newaim.purchase.archives.service_provider.service;

import com.google.common.collect.Lists;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.service_provider.dao.ServiceProviderChargeItemDao;
import com.newaim.purchase.archives.service_provider.entity.ServiceProvider;
import com.newaim.purchase.archives.service_provider.entity.ServiceProviderChargeItem;
import com.newaim.purchase.archives.service_provider.vo.ServiceProviderChargeItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ServiceProviderChargeItemService {

    @Autowired
    private ServiceProviderChargeItemDao serviceProviderChargeItemDao;

    /**
     * 保存收费项目集合
     * @param chargeItems 港口
     * @param serviceProvider 对应服务商
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<ServiceProviderChargeItem> saveServiceProviderChargeItem(List<ServiceProviderChargeItem> chargeItems, ServiceProvider serviceProvider){

        //1. 删除此服务商信息下所有收费项目
        serviceProviderChargeItemDao.deleteByServiceProviderId(serviceProvider.getId());
        //2. 设置服务商信息
        if(chargeItems != null && chargeItems.size() > 0){
            List<ServiceProviderChargeItem> data = Lists.newArrayList();
            for (int i = 0; i < chargeItems.size(); i++) {
                ServiceProviderChargeItem chargeItem = BeanMapper.map(chargeItems.get(i), ServiceProviderChargeItem.class);
                chargeItem.setId(null);
                chargeItem.setServiceProviderId(serviceProvider.getId());
                chargeItem.setServiceProviderCnName(serviceProvider.getCnName());
                chargeItem.setServiceProviderEnName(serviceProvider.getEnName());
                chargeItem.setFeeType("1");
                data.add(chargeItem);
            }
            return serviceProviderChargeItemDao.save(data);
        }
        return null;
    }

    public ServiceProviderChargeItemVo get(String id) {
        ServiceProviderChargeItem row = getServiceProviderPort(id);
        ServiceProviderChargeItemVo o = BeanMapper.map(row, ServiceProviderChargeItemVo.class);

        return o;
    }

    public ServiceProviderChargeItem getServiceProviderPort(String id) {
        ServiceProviderChargeItem o = serviceProviderChargeItemDao.findServiceProviderChargeItemById(id);
        return o;
    }

    /**
     * 通过服务商id查找所有收费项目
     * @param serviceProviderId
     * @return
     */
    public List<ServiceProviderChargeItemVo> listChargeItemsByServiceProviderId(String serviceProviderId) {
        List<ServiceProviderChargeItem> data = serviceProviderChargeItemDao.findChargeItemsByServiceProviderId(serviceProviderId);
        return BeanMapper.mapList(data, ServiceProviderChargeItem.class, ServiceProviderChargeItemVo.class);
    }

    public List<ServiceProviderChargeItemVo> listChargeItemByChargeItemIds(List<ServiceProviderChargeItemVo> chargeItems){
        List<ServiceProviderChargeItemVo> rd = Lists.newArrayList();

        if (!ListUtils.isEmpty(chargeItems)) {
            for (int i = 0; i < chargeItems.size(); i++) {
                ServiceProviderChargeItemVo pv = new ServiceProviderChargeItemVo();
                BeanMapper.copyProperties(chargeItems.get(i), pv, true);
//                pv = get(pv.getItemId());
                rd.add(pv);
            }
        }

        return rd;
    }
}
