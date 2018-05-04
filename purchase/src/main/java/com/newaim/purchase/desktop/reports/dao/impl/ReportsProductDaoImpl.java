package com.newaim.purchase.desktop.reports.dao.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.jpa.BaseDaoCustomImpl;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.desktop.reports.dao.ReportsProductDaoCustom;
import com.newaim.purchase.desktop.reports.entity.Reports;
import com.newaim.purchase.desktop.reports.entity.ReportsProduct;
import com.newaim.purchase.desktop.reports.vo.ReportsVo;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportsProductDaoImpl extends BaseDaoCustomImpl implements ReportsProductDaoCustom {
    @Override
    public List<ReportsVo> listReportsByProductId(String productId, Integer status) {

        List<Reports> list = Lists.newArrayList();
        if(StringUtils.isNotBlank(productId)){
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            StringBuilder hql = new StringBuilder("SELECT m FROM Reports m, ReportsProduct m2 WHERE ");
            hql.append("m.id = m2.reportsId AND m.status=:status AND m2.productId=:productId ");

            params.put("productId", productId);
            params.put("status", status);
            hql.append("ORDER BY m.createdAt ASC");

            list = list(hql.toString(), params);

            if(list.size()>0){
                return BeanMapper.mapList(list, Reports.class, ReportsVo.class);
            }
        }
        return null;
    }

    @Override
    public List<ReportsVo> listReportsByProductIds(String[] productIds, Integer status) {
        List<Reports> list = Lists.newArrayList();
        if(null != productIds && productIds.length >0){
            String productIdStr =  StringUtils.join(productIds, "','");
            productIdStr = "'" + productIdStr + "'";

            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            StringBuilder hql = new StringBuilder("SELECT m FROM Reports m, ReportsProduct m2 WHERE ");
            hql.append("m.id = m2.reportsId AND m.status=:status AND m2.productId IN :productId ");

            params.put("productId", productIdStr);
            params.put("status", status);
            hql.append("ORDER BY m.createdAt ASC");

            list = list(hql.toString(), params);

            if(list.size()>0){
                return BeanMapper.mapList(list, Reports.class, ReportsVo.class);
            }
        }

        return null;
    }

    @Override
    public List<ReportsVo> listReportsByBusinessId(String businessId, Integer status) {
        List<Reports> list = Lists.newArrayList();
        if(StringUtils.isNotBlank(businessId)){
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            StringBuilder hql = new StringBuilder("SELECT m FROM Reports m WHERE ");

            params.put("businessId", businessId);
            hql.append(" m.businessId = :businessId ");

            if(status>-1) {
                params.put("status", status);
                hql.append("AND m.status=:status ");
            }else{
                hql.append("AND m.status>-1 AND m.status<3 ");
            }
            hql.append("ORDER BY m.createdAt ASC");

            list = list(hql.toString(), params);

            if(list.size()>0){
                return BeanMapper.mapList(list, Reports.class, ReportsVo.class);
            }
        }

        return null;
    }

    @Override
    public List<ReportsVo> listReportsByVendorId(String vendorId, Integer status) {
        List<Reports> list = Lists.newArrayList();
        if(StringUtils.isNotBlank(vendorId)){
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            StringBuilder hql = new StringBuilder("SELECT m FROM Reports m WHERE ");

            params.put("vendorId", vendorId);
            hql.append(" m.vendorId = :vendorId ");

            if(status>-1) {
                params.put("status", status);
                hql.append("AND m.status=:status ");
            }else{
                hql.append("AND m.status>-1 AND m.status<3 ");
            }
            hql.append("ORDER BY m.createdAt ASC");

            list = list(hql.toString(), params);

            if(list.size()>0){
                return BeanMapper.mapList(list, Reports.class, ReportsVo.class);
            }
        }

        return null;
    }

    @Override
    public ReportsProduct findByReportsIdAndProductId(String businessId, String productId) {
        if(StringUtils.isNotBlank(businessId) && StringUtils.isNotBlank(productId)){
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            StringBuilder hql = new StringBuilder("select t from ReportsProduct t where 1=1 ");
            hql.append(" and t.productId = :productId");
            params.put("productId", productId);
            hql.append(" and exists (select 1 from Reports r where r.id = t.reportsId and r.businessId = :businessId and r.status = 1)");
            params.put("businessId", businessId);
            List<ReportsProduct> data = list(hql.toString(), params);
            if(data != null && data.size() > 0){
                return data.get(0);
            }
        }
        return null;
    }
}
