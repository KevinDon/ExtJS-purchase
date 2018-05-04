package com.newaim.purchase.archives.service_provider.service;

import com.google.common.collect.Lists;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.service_provider.dao.ServiceProviderPortDao;
import com.newaim.purchase.archives.service_provider.entity.ServiceProvider;
import com.newaim.purchase.archives.service_provider.entity.ServiceProviderPort;
import com.newaim.purchase.archives.service_provider.vo.ServiceProviderPortVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ServiceProviderPortService {

    @Autowired
    private ServiceProviderPortDao serviceProviderPortDao;

    /**
     * 保存港口集合
     *
     * @param ports           港口
     * @param serviceProvider 对应服务商
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<ServiceProviderPort> saveServiceProviderPort(List<ServiceProviderPort> ports, ServiceProvider serviceProvider) {

        //1. 删除此服务商信息下所有港口
        serviceProviderPortDao.deletePortByServiceProviderId(serviceProvider.getId());
        //2. 设置服务商信息
        if (ports != null && ports.size() > 0) {
            List<ServiceProviderPort> data = Lists.newArrayList();
            for (int i = 0; i < ports.size(); i++) {
                ServiceProviderPort port = BeanMapper.map(ports.get(i), ServiceProviderPort.class);
                port.setId(null);
                port.setServiceProviderId(serviceProvider.getId());
                port.setServiceProviderCnName(serviceProvider.getCnName());
                port.setServiceProviderEnName(serviceProvider.getEnName());
                port.setDestinationPortId("1");
                data.add(port);
            }
            return serviceProviderPortDao.save(data);
        }
        return null;
    }

    public ServiceProviderPortVo get(String id) {
        ServiceProviderPort row = getServiceProviderPort(id);
        ServiceProviderPortVo o = BeanMapper.map(row, ServiceProviderPortVo.class);

        return o;
    }

    public ServiceProviderPort getServiceProviderPort(String id) {
        ServiceProviderPort o = serviceProviderPortDao.findServiceProviderPortById(id);
        return o;
    }

    /**
     * 通过服务商id查找所有港口
     *
     * @param serviceProviderId 服务商id
     * @return
     */
    public List<ServiceProviderPortVo> listPortsByServiceProviderId(String serviceProviderId) {
        List<ServiceProviderPort> data = serviceProviderPortDao.findPortsByServiceProviderId(serviceProviderId);
        return BeanMapper.mapList(data, ServiceProviderPort.class, ServiceProviderPortVo.class);
    }

    /**
     * 根据ports Id 获取所有港口
     * @param ports
     * @return
     */
    public List<ServiceProviderPortVo> listPortsByPortId(List<ServiceProviderPortVo> ports) {
        List<ServiceProviderPortVo> rd = Lists.newArrayList();

        if (!ListUtils.isEmpty(ports)) {
            for (int i = 0; i < ports.size(); i++) {
                ServiceProviderPortVo pv = new ServiceProviderPortVo();
                BeanMapper.copyProperties(ports.get(i), pv, true);
//                pv = get(pv.getOriginPortId());
                rd.add(pv);
            }

        }

        return rd;
    }
}
