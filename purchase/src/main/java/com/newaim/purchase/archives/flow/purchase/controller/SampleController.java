package com.newaim.purchase.archives.flow.purchase.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.archives.flow.purchase.entity.SampleDetail;
import com.newaim.purchase.archives.flow.purchase.service.SampleDetailService;
import com.newaim.purchase.archives.flow.purchase.service.SampleService;
import com.newaim.purchase.archives.flow.purchase.vo.SampleVo;
import com.newaim.purchase.archives.product.service.ProductService;
import com.newaim.purchase.archives.product.vo.ProductVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/archives/flow/purchase/sample")
public class SampleController extends ControllerBase {

    @Autowired
    private SampleService sampleService;

    @Autowired
    private SampleDetailService sampleDetailService;

    @Autowired
    private ProductService productService;

    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords,String vendorId,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
    ) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
                params.put("id-S-LK-OR", keywords);
                params.put("creatorCnName-S-LK-OR", keywords);
                params.put("creatorEnName-S-LK-OR", keywords);
                params.put("handlerDepartmentCnName-S-LK-OR", keywords);
                params.put("handlerDepartmentEnName-S-LK-OR", keywords);
                params.put("reviewerCnName-S-LK-OR", keywords);
                params.put("reviewerEnName-S-LK-OR", keywords);
                params.put("reviewerDepartmentCnName-S-LK-OR", keywords);
                params.put("reviewerDepartmentEnName-S-LK-OR", keywords);
                params.put("departmentCnName-S-LK-OR", keywords);
                params.put("departmentEnName-S-LK-OR", keywords);
            }else{
                params = ServletUtils.getParametersStartingWith(request);
                //搜索出指定部门以下的所有记录
                if(params.size()>0){
                    if(params.get("departmentId-S-EQ") != null && StringUtils.isNotBlank(params.get("departmentId-S-EQ").toString())){
                        String depIds = getDepartmentsByDepId(params.get("departmentId-S-EQ").toString());
                        params.remove("departmentId-S-EQ");
                        params.put("departmentId-S-IN", depIds);
                    }
                }
            }

            if (null!= vendorId && !"".equals(vendorId)){
                params.put("vendorId-S-EQ-ADD", vendorId);
            }


            Page<SampleVo> page = sampleService.list(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/listfordialog")
    public RestResult listForDialog(ServletRequest request, String sort, String keywords,String vendorId,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
    ){
        RestResult result = list(request,sort,keywords,vendorId, pageNumber, pageSize);

        List<SampleVo> samples= (List<SampleVo>) result.getData();

        List<ProductVo> products = Lists.newArrayList();

        if (samples!=null && samples.size()>0){
            for (int i = 0; i < samples.size(); i++) {
                SampleVo sample = samples.get(i);
                List<SampleDetail> sampleDetails = sampleDetailService.findDetailsBySampleId(sample.getId());
                if(sampleDetails != null && sampleDetails.size() > 0){
                    for (int j = 0; j < sampleDetails.size(); j++) {
                        products.add(productService.get(sampleDetails.get(j).getProductId()));
                    }
                }
            }
        }

        return result.setData(products);
    }





}
