package com.newaim.purchase.flow.inspection.dao;

import com.newaim.purchase.archives.product.vo.ProductVo;

import java.util.List;

public interface FlowOrderQcDetailDaoCustom {

    List<ProductVo> getProductsByOrderId(String orderId);

}
