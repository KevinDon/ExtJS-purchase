package com.newaim.purchase.api.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.mapper.JsonMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.api.dto.*;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.entity.ProductCategory;
import com.newaim.purchase.archives.product.entity.ProductCombined;
import com.newaim.purchase.archives.product.entity.ProductCombinedDetail;
import com.newaim.purchase.archives.product.service.ProductCategoryService;
import com.newaim.purchase.archives.product.service.ProductCombinedDetailService;
import com.newaim.purchase.archives.product.service.ProductCombinedService;
import com.newaim.purchase.archives.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Mark on 2017/12/13.
 */
@Service
public class OmsApiService extends ServiceBase {

    @Autowired
    private TransferService transferService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductCombinedService productCombinedService;

    @Autowired
    private ProductCombinedDetailService productCombinedDetailService;

    /**
     * 添加产品
     * @param productId
     * @return
     * @throws Exception
     */
    public RestResult goodsAdd(String productId) throws Exception {
        Map<String, String> params = Maps.newHashMap();
        String method = "Goods.Add";
        params.put("method", method);
        Product product = productService.getProduct(productId);
        OmsGoodsAddDto dto = new OmsGoodsAddDto();
        dto.setOwnerCode("NEW AIM");
        dto.setBrandCode("NEW AIM");
        dto.setGoodsNo(product.getSku());
        dto.setGoodsName(product.getName());
        dto.setGoodsNameEnglish(product.getName());

        //加入隶属采购组
        Map<String, String> goodsAttr = Maps.newHashMap();
        goodsAttr.put("goods_attr_7", product.getDepartmentEnName());
        dto.setGoodsAttrs(goodsAttr);

        //产品分类
        ProductCategory category = productCategoryService.getProductCategory(product.getCategoryId());
        if (category != null){
            dto.setCategoryCode(category.getOmsCode());
        }
        dto.setMarketPrice(product.getProp().getTargetBinAud() != null ? product.getProp().getTargetBinAud(): new BigDecimal(0) );
        dto.setGoodsWeight(product.getProp().getInnerCartonNetWeight() != null &&  product.getProp().getInnerCartonNetWeight().doubleValue() > 0 ? product.getProp().getInnerCartonNetWeight(): product.getProp().getMasterCartonGrossWeight());
        dto.setVolume(product.getProp().getInnerCartonCbm() != null && product.getProp().getInnerCartonCbm().doubleValue() > 0 ? product.getProp().getInnerCartonCbm(): product.getProp().getMasterCartonCbm());
        dto.setLength(product.getProp().getInnerCartonL() != null && product.getProp().getInnerCartonL().doubleValue() > 0 ? product.getProp().getInnerCartonL(): product.getProp().getMasterCartonL());
        dto.setWidth(product.getProp().getInnerCartonW() != null && product.getProp().getInnerCartonW().doubleValue() > 0  ? product.getProp().getInnerCartonW(): product.getProp().getMasterCartonW());
        dto.setHeight(product.getProp().getInnerCartonH() != null && product.getProp().getInnerCartonH().doubleValue() > 0 ? product.getProp().getInnerCartonH(): product.getProp().getMasterCartonH() );
        List<OmsSkuDto> skuList = Lists.newArrayList();
        OmsSkuDto skuDto = new OmsSkuDto();
        skuDto.setSku(product.getSku());
        skuDto.setSkuAttrs(goodsAttr);
        skuDto.setMarketPrice(product.getProp().getTargetBinAud() != null ? product.getProp().getTargetBinAud(): new BigDecimal(0) );
        skuDto.setVolume(product.getProp().getInnerCartonCbm() != null && product.getProp().getInnerCartonCbm().doubleValue() > 0 ? product.getProp().getInnerCartonCbm(): product.getProp().getMasterCartonCbm());
        skuDto.setLength(product.getProp().getInnerCartonL() != null && product.getProp().getInnerCartonL().doubleValue() > 0 ? product.getProp().getInnerCartonL(): product.getProp().getMasterCartonL());
        skuDto.setWidth(product.getProp().getInnerCartonW() != null && product.getProp().getInnerCartonW().doubleValue() > 0 ? product.getProp().getInnerCartonW(): product.getProp().getMasterCartonW());
        skuDto.setHeight(product.getProp().getInnerCartonH() != null && product.getProp().getInnerCartonH().doubleValue() > 0 ? product.getProp().getInnerCartonH(): product.getProp().getMasterCartonH());
        skuDto.setEan(product.getEan());
        skuDto.setSlaveSkus(product.getBarcode());
        skuList.add(skuDto);
        dto.setSkuList(skuList);
        params.put("goods", JsonMapper.INSTANCE.toJson(dto));
        return transferService.transferForRestResult(method, params);
    }

    /**
     * 添加产品
     * @param productId
     * @return
     * @throws Exception
     */
    public RestResult goodsUpdate(String productId) throws Exception {
        Map<String, String> params = Maps.newHashMap();
        String method = "Goods.Add";
        params.put("method", method);
        Product product = productService.getProduct(productId);
        OmsGoodsAddDto dto = new OmsGoodsAddDto();
        dto.setOwnerCode("NEW AIM");
        dto.setBrandCode("NEW AIM");
        dto.setGoodsNo(product.getSku());
        dto.setGoodsName(product.getName());
        dto.setGoodsNameEnglish(product.getName());

        //加入隶属采购组
        Map<String, String> goodsAttr = Maps.newHashMap();
        goodsAttr.put("goods_attr_7", product.getDepartmentEnName());
        dto.setGoodsAttrs(goodsAttr);

        //产品分类
        ProductCategory category = productCategoryService.getProductCategory(product.getCategoryId());
        if (category != null){
            dto.setCategoryCode(category.getOmsCode());
        }
        dto.setMarketPrice(product.getProp().getTargetBinAud() != null ? product.getProp().getTargetBinAud(): new BigDecimal(0) );
        dto.setGoodsWeight(product.getProp().getInnerCartonNetWeight() != null && product.getProp().getInnerCartonNetWeight().doubleValue() > 0 ? product.getProp().getInnerCartonNetWeight(): product.getProp().getMasterCartonGrossWeight());
        dto.setVolume(product.getProp().getInnerCartonCbm() != null && product.getProp().getInnerCartonCbm().doubleValue() > 0 ? product.getProp().getInnerCartonCbm(): product.getProp().getMasterCartonCbm());
        dto.setLength(product.getProp().getInnerCartonL() != null && product.getProp().getInnerCartonL().doubleValue() > 0 ? product.getProp().getInnerCartonL(): product.getProp().getMasterCartonL());
        dto.setWidth(product.getProp().getInnerCartonW() != null && product.getProp().getInnerCartonW().doubleValue() > 0  ? product.getProp().getInnerCartonW(): product.getProp().getMasterCartonW());
        dto.setHeight(product.getProp().getInnerCartonH() != null && product.getProp().getInnerCartonH().doubleValue() > 0 ? product.getProp().getInnerCartonH(): product.getProp().getMasterCartonH() );
        List<OmsSkuDto> skuList = Lists.newArrayList();
        OmsSkuDto skuDto = new OmsSkuDto();
        skuDto.setSku(product.getSku());
        skuDto.setSkuAttrs(goodsAttr);
        skuDto.setMarketPrice(product.getProp().getTargetBinAud() != null ? product.getProp().getTargetBinAud(): new BigDecimal(0) );
        skuDto.setVolume(product.getProp().getInnerCartonCbm() != null && product.getProp().getInnerCartonCbm().doubleValue() > 0 ? product.getProp().getInnerCartonCbm(): product.getProp().getMasterCartonCbm());
        skuDto.setLength(product.getProp().getInnerCartonL() != null && product.getProp().getInnerCartonL().doubleValue() > 0 ? product.getProp().getInnerCartonL(): product.getProp().getMasterCartonL());
        skuDto.setWidth(product.getProp().getInnerCartonW() != null && product.getProp().getInnerCartonW().doubleValue() > 0 ? product.getProp().getInnerCartonW(): product.getProp().getMasterCartonW());
        skuDto.setHeight(product.getProp().getInnerCartonH() != null && product.getProp().getInnerCartonH().doubleValue() > 0 ? product.getProp().getInnerCartonH(): product.getProp().getMasterCartonH());
        skuList.add(skuDto);
        dto.setSkuList(skuList);
        params.put("goods", JsonMapper.INSTANCE.toJson(dto));
        return transferService.transferForRestResult(method, params);
    }

    /**
     * 添加PARENT产品
     * @param productId
     * @return
     * @throws Exception
     */
    public RestResult goodsAddForParent(String productId) throws Exception {
        Map<String, String> params = Maps.newHashMap();
        String method = "Goods.Add";
        params.put("method", method);
        ProductCombined product = productCombinedService.getProductCombined(productId);
        OmsGoodsAddDto dto = new OmsGoodsAddDto();
        dto.setOwnerCode("NEW AIM");
        dto.setBrandCode("NEW AIM");
        dto.setGoodsNo(product.getCombinedSku());
        dto.setGoodsName(product.getCombinedName());
        dto.setGoodsNameEnglish(product.getCombinedName());

        //加入隶属采购组
//        Map<String, String> goodsAttr = Maps.newHashMap();
//        goodsAttr.put("goods_attr_7", product.getDepartmentEnName());
//        dto.setGoodsAttrs(goodsAttr);

        //产品分类
        ProductCategory category = productCategoryService.getProductCategory(product.getCategoryId());
        if (category != null){
            dto.setCategoryCode(category.getOmsCode());
        }
        dto.setMarketPrice(null);
        dto.setGoodsWeight(null);
        dto.setVolume(null);
        dto.setLength(null);
        dto.setWidth(null);
        dto.setHeight(null);
        List<OmsSkuDto> skuList = Lists.newArrayList();
        OmsSkuDto skuDto = new OmsSkuDto();
        skuDto.setSku(product.getCombinedSku());
//        skuDto.setSkuAttrs(goodsAttr);
        skuDto.setMarketPrice(null);
        skuDto.setVolume(null);
        skuDto.setLength(null);
        skuDto.setWidth(null);
        skuDto.setHeight(null);
        skuDto.setEan(product.getEan());
        skuDto.setSlaveSkus(null);
        skuList.add(skuDto);
        dto.setSkuList(skuList);
        params.put("goods", JsonMapper.INSTANCE.toJson(dto));
        return transferService.transferForRestResult(method, params);
    }

    /**
     * 修改PARENT产品
     * @param productId
     * @return
     * @throws Exception
     */
    public RestResult goodsUpdateForParent(String productId) throws Exception {
        Map<String, String> params = Maps.newHashMap();
        String method = "Goods.Add";
        params.put("method", method);
        ProductCombined product = productCombinedService.getProductCombined(productId);
        OmsGoodsAddDto dto = new OmsGoodsAddDto();
        dto.setOwnerCode("NEW AIM");
        dto.setBrandCode("NEW AIM");
        dto.setGoodsNo(product.getCombinedSku());
        dto.setGoodsName(product.getCombinedName());
        dto.setGoodsNameEnglish(product.getCombinedName());

//        //加入隶属采购组
//        Map<String, String> goodsAttr = Maps.newHashMap();
//        goodsAttr.put("goods_attr_7", product.getDepartmentEnName());
//        dto.setGoodsAttrs(goodsAttr);

        //产品分类
        ProductCategory category = productCategoryService.getProductCategory(product.getCategoryId());
        if (category != null){
            dto.setCategoryCode(category.getOmsCode());
        }
        dto.setMarketPrice(null);
        dto.setGoodsWeight(null);
        dto.setVolume(null);
        dto.setLength(null);
        dto.setWidth(null);
        dto.setHeight(null);
        List<OmsSkuDto> skuList = Lists.newArrayList();
        OmsSkuDto skuDto = new OmsSkuDto();
        skuDto.setSku(product.getCombinedSku());
//        skuDto.setSkuAttrs(goodsAttr);
        skuDto.setMarketPrice(null);
        skuDto.setVolume(null);
        skuDto.setLength(null);
        skuDto.setWidth(null);
        skuDto.setHeight(null);
        skuList.add(skuDto);
        dto.setSkuList(skuList);
        params.put("goods", JsonMapper.INSTANCE.toJson(dto));
        return transferService.transferForRestResult(method, params);
    }

    /**
     * 添加产品
     * @param productId
     * @return
     * @throws Exception
     */
    public RestResult goodsUpdate2(String productId) throws Exception {
        Map<String, String> params = Maps.newHashMap();
        String method = "Goods.Update";
        params.put("method", method);
        Product product = productService.getProduct(productId);
        OmsGoodsUpdateDto dto = new OmsGoodsUpdateDto();
        dto.setOwnerCode("NEW AIM");
        dto.setGoodsNo(product.getSku());
        dto.setGoodsName(product.getName());
        dto.setGoodsNameEnglish(product.getName());
        dto.setGoodsWeight(product.getProp().getInnerCartonNetWeight() != null && product.getProp().getInnerCartonNetWeight().doubleValue() > 0 ? product.getProp().getInnerCartonNetWeight(): product.getProp().getMasterCartonGrossWeight());
        dto.setVolume(product.getProp().getInnerCartonCbm() != null && product.getProp().getInnerCartonCbm().doubleValue() > 0 ? product.getProp().getInnerCartonCbm(): product.getProp().getMasterCartonCbm());
        dto.setLength(product.getProp().getInnerCartonL() != null && product.getProp().getInnerCartonL().doubleValue() > 0 ? product.getProp().getInnerCartonL(): product.getProp().getMasterCartonL());
        dto.setWidth(product.getProp().getInnerCartonW() != null && product.getProp().getInnerCartonW().doubleValue() > 0  ? product.getProp().getInnerCartonW(): product.getProp().getMasterCartonW());
        dto.setHeight(product.getProp().getInnerCartonH() != null && product.getProp().getInnerCartonH().doubleValue() > 0 ? product.getProp().getInnerCartonH(): product.getProp().getMasterCartonH() );
        params.put("goods", JsonMapper.INSTANCE.toJson(dto));
        return transferService.transferForRestResult(method, params);
    }

    /**
     * 组合产品添加
     * @param packageId
     * @return
     * @throws Exception
     */
    public RestResult packageAdd(String packageId) throws Exception {
        Map<String, String> params = Maps.newHashMap();
        String method = "Goods.PackageAdd";
        params.put("method", method);
        ProductCombined productCombined = productCombinedService.getProductCombined(packageId);
        OmsPackageAddDto dto = new OmsPackageAddDto();
        dto.setOwnerCode("NEW AIM");
        dto.setBrandCode("NEW AIM");
        dto.setPackageSku(productCombined.getCombinedSku());
        dto.setPackageName(productCombined.getCombinedName());
        dto.setPackageEan(productCombined.getEan());
        //TODO 有效开始时间
        dto.setValidTimeStart(null);
        //TODO 有效结束时间
        dto.setValidTimeEnd(null);
        List<OmsPackageSkuDto> skus = Lists.newArrayList();
        List<ProductCombinedDetail> details = productCombinedDetailService.findDetailsByProductCombinedId(packageId);
        if(details != null && details.size() > 0){
            for (int i = 0; i < details.size(); i++) {
                ProductCombinedDetail detail = details.get(i);
                OmsPackageSkuDto sku = new OmsPackageSkuDto();
                sku.setSku(detail.getSku());
                sku.setNumber(detail.getQty()!=null && detail.getQty()>0? detail.getQty(): 0);
                sku.setPrice(detail.getPriceAud() != null ? detail.getPriceAud():  BigDecimal.valueOf(0) );
                skus.add(sku);
            }
        }
        dto.setSkus(skus);
        dto.setIsCainiao(0);
        params.put("goods", JsonMapper.INSTANCE.toJson(dto));
        return transferService.transferForRestResult(method, params);
    }
    /**
     * 组合产品修改
     * @param packageId
     * @return
     * @throws Exception
     */
    public RestResult packageUpdate(String packageId) throws Exception {
        RestResult result = new RestResult();
        Map<String, Object> params = Maps.newHashMap();
        String method = "products/updateComboSku";
        params.put("method", method);
        ProductCombined productCombined = productCombinedService.getProductCombined(packageId);

        params.put("owner_code", "NEW AIM");
        params.put("brand_code", "NEW AIM");
        params.put("package_sku", productCombined.getCombinedSku());
        params.put("package_ean", productCombined.getEan());
        params.put("package_name", productCombined.getCombinedName());
        params.put("valid_time_start", null);
        params.put("valid_time_end", null);
        List<Map<String, Object> > skus = Lists.newArrayList();
        List<ProductCombinedDetail> details = productCombinedDetailService.findDetailsByProductCombinedId(packageId);
        if(details != null && details.size() > 0){
            for (int i = 0; i < details.size(); i++) {
                ProductCombinedDetail detail = details.get(i);;
                Map<String, Object> sku = Maps.newHashMap();
                sku.put("sku", detail.getSku());
                sku.put("number", detail.getQty()!=null && detail.getQty()>0 ? detail.getQty(): 0 );
                sku.put("price", detail.getPriceAud() != null ? detail.getPriceAud():  BigDecimal.valueOf(0));
                skus.add(sku);
            }
        }
        params.put("skus", skus);
        params.put("is_cainiao", 0);

        String resultStr = transferService.transferOmsCustomObject(method, params);

        JsonMapper jsonMapper = new JsonMapper();
        OmsApiDto dto = jsonMapper.fromJson(resultStr, OmsApiDto.class);
        try {
            if (dto != null) {
                if (Long.valueOf(dto.getStatus()) > 0) {
                    result.setSuccess(true);
                } else {
                    result.setSuccess(false);
                }
                result.setMsg(dto.getMsg()).setData(dto.getData()).setCode(dto.getStatus());
            } else {
                result.setSuccess(false);
            }
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(e.getMessage());
        }

        return result;
    }

}
