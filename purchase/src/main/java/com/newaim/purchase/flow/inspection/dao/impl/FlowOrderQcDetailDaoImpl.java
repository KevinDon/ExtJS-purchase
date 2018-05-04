package com.newaim.purchase.flow.inspection.dao.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.jpa.BaseDaoCustomImpl;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.vo.ProductVo;
import com.newaim.purchase.flow.inspection.dao.FlowOrderQcDetailDaoCustom;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class FlowOrderQcDetailDaoImpl  extends BaseDaoCustomImpl implements FlowOrderQcDetailDaoCustom {

    @Override
    public List<ProductVo> getProductsByOrderId(String orderId) {
        List<Product> list = Lists.newArrayList();
        if(StringUtils.isNotBlank(orderId)){
            Map<String, Object> params = Maps.newHashMap();
            StringBuilder hql = new StringBuilder("SELECT m FROM Product m, FlowPurchaseContractDetail t2 WHERE ");
            hql.append("m.id = t2.productId AND t2.businessId = :orderId ");

            params.put("orderId", orderId);

            list = list(hql.toString(), params);

            if(list.size()>0){
                return BeanMapper.mapList(list, Product.class, ProductVo.class);
            }
        }
        return null;
    }

}
