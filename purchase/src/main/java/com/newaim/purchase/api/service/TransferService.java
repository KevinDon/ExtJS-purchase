package com.newaim.purchase.api.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.mapper.JsonMapper;
import com.newaim.core.utils.AES128Utils;
import com.newaim.core.utils.DateFormatUtil;
import com.newaim.core.utils.MD5Util;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.WebUtils;
import com.newaim.purchase.admin.system.service.DictionaryService;
import com.newaim.purchase.admin.system.vo.DictionaryCallVo;
import com.newaim.purchase.admin.system.vo.DictionaryValueVo;
import com.newaim.purchase.api.dto.OmsApiDto;
import com.newaim.purchase.api.dto.OmsOrderDetailDto;
import com.newaim.purchase.api.dto.OmsOrderDto;
import com.newaim.purchase.archives.product.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransferService {

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private ProductService productService;

    public String transfer(String method, Map<String, String> params) throws IOException {
        String env = getOmsApiEnv("api_oms");
        params.put("app_key", getOmsAppKey(env));
        params.put("app_secret", getOmsAppSecret(env));
        params.put("timestamp", DateFormatUtil.format(Calendar.getInstance().getTime(), "yyyy-MM-dd HH:mm:ss"));
        params.put("app_sign", getAppSign(params));
        String url = getOmsUrl(env) + "?method=" + method;
        return WebUtils.doPost(url, params, 30000, 30000);
    }

    public RestResult transferForRestResult(String method, Map<String, String> params) throws IOException {
        RestResult result = new RestResult();
        try {
            String resultStr = transfer(method, params);
            if(StringUtils.isNotBlank(resultStr)){
                JsonMapper jsonMapper = new JsonMapper();
                jsonMapper.getMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                OmsApiDto resultDto = jsonMapper.fromJson(resultStr, OmsApiDto.class);
                String status = resultDto.getStatus();
                Boolean success = false;
                if(StringUtils.isNotBlank(status) && Integer.valueOf(status) > 0){
                    success = true;
                }
                result.setSuccess(success).setMsg(resultDto.getMsg()).setData(resultDto.getData()).setCode(resultDto.getStatus());
            }else{
                result.setSuccess(false);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            result.setSuccess(false).setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 自定义OMS接口
     * @param method
     * @param params
     * @return
     * @throws IOException
     */
    public String transferOmsCustom(String method, Map<String, String> params) throws IOException {
        String env = getOmsApiEnv("api_oms_custom");
        long timestamp = Calendar.getInstance().getTimeInMillis();
        params.put("secret", getOmsCustomSecret(env, timestamp));
        params.put("timestamp", timestamp + "");
        String url = getOmsCustomUrl(env) + "/" + method;
        return WebUtils.doPost(url, params, 30000, 30000);
    }

    /**
     * 自定义OMS接口
     */
    public <T> RestResult transferOmsCustomForRestResult(String method, Map<String, String> params, Class<?>... classes) {
        RestResult result = new RestResult();
        try {
            String resultStr = transferOmsCustom(method, params);
            if(StringUtils.isNotBlank(resultStr)){
                JsonMapper jsonMapper = new JsonMapper();
                jsonMapper.getMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                OmsApiDto dto = jsonMapper.fromJson(resultStr, OmsApiDto.class, classes);
                if(dto != null){
                    List<OmsOrderDto> ood = (List<OmsOrderDto>) dto.getData();
                    if(ood.size()>0){
                        for (int i = 0; i < ood.size(); i++){
                            List<OmsOrderDetailDto> details = ood.get(i).getDetails();
                            for (int j = 0; j < details.size(); j++) {
                                OmsOrderDetailDto detail = details.get(j);
                                if(StringUtils.isNotBlank(detail.getSku())){
                                    detail.setProduct(productService.getBySku(detail.getSku()));
                                }
                            }
                        }
                    }

                    if(Long.valueOf(dto.getStatus()) > 0){
                        result.setSuccess(true);
                    }else{
                        result.setSuccess(false);
                    }
                    if(StringUtils.equals(dto.getHasNext(), "1")){
                        result.setHasMore(1);
                    }
                    result.setMsg(dto.getMsg()).setData(ood).setCode(dto.getStatus());
                }else{
                    result.setSuccess(false);
                }
            }else{
                result.setSuccess(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.setSuccess(false).setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 自定义OMS接口
     * @param method
     * @param params
     * @return
     * @throws IOException
     */
    public String transferOmsCustomObject(String method, Map<String, Object> params) throws IOException {
        String env = getOmsApiEnv("api_oms_custom");
        long timestamp = Calendar.getInstance().getTimeInMillis();
        params.put("secret", getOmsCustomSecret(env, timestamp));
        params.put("timestamp", timestamp + "");
        String url = getOmsCustomUrl(env) + "/" + method;
        Map<String, String> header = Maps.newHashMap();
        header.put("Content-Type", "application/json");
        return WebUtils.doPost(url, JsonMapper.INSTANCE.toJson(params), "utf-8", 30000, 30000, header);
    }

    /**
     * 自定义OMS接口
     */
    public <T> RestResult transferOmsCustomForRestResultObject(String method, Map<String, Object> params, Class<?>... classes) {
        RestResult result = new RestResult();
        try {
            String resultStr = transferOmsCustomObject(method, params);
            if(StringUtils.isNotBlank(resultStr)){
                JsonMapper jsonMapper = new JsonMapper();
                jsonMapper.getMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                OmsApiDto dto = jsonMapper.fromJson(resultStr, OmsApiDto.class, classes);
                if(dto != null){
                    List<OmsOrderDto> ood = (List<OmsOrderDto>) dto.getData();
                    if(ood.size()>0){
                        for (int i = 0; i < ood.size(); i++){
                            List<OmsOrderDetailDto> details = ood.get(i).getDetails();
                            for (int j = 0; j < details.size(); j++) {
                                OmsOrderDetailDto detail = details.get(j);
                                if(StringUtils.isNotBlank(detail.getSku())){
                                    detail.setProduct(productService.getBySku(detail.getSku()));
                                }
                            }
                        }
                    }

                    if(Long.valueOf(dto.getStatus()) > 0){
                        result.setSuccess(true);
                    }else{
                        result.setSuccess(false);
                    }
                    if(StringUtils.equals(dto.getHasNext(), "1")){
                        result.setHasMore(1);
                    }
                    result.setMsg(dto.getMsg()).setData(ood).setCode(dto.getStatus());
                }else{
                    result.setSuccess(false);
                }
            }else{
                result.setSuccess(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.setSuccess(false).setMsg(e.getMessage());
        }
        return result;
    }

    public String transferInbound(String method, Map<String, Object> params) throws IOException {
        String env = getOmsApiEnv("api_oms_custom");
        long timestamp = Calendar.getInstance().getTimeInMillis();
        params.put("secret", getOmsCustomSecret(env, timestamp));
        params.put("timestamp", timestamp + "");
        String url = getInboundUrl(env) + "/" + method;
        Map<String, String> header = Maps.newHashMap();
        header.put("Content-Type", "application/json");
        return WebUtils.doPost(url, JsonMapper.INSTANCE.toJson(params), "utf-8", 30000, 30000, header);
//        return WebUtils.doPost(url, params, 30000, 30000);
    }

    /**
     * 自定义OMS接口
     */
    public <T> RestResult transferInboundForRestResult(String method, LinkedHashMap<String, Object> params, Class<?>... classes) {
        RestResult result = new RestResult();
        try {
            String resultStr = transferInbound(method, params);
            if(StringUtils.isNotBlank(resultStr)){
                JsonMapper jsonMapper = new JsonMapper();
                jsonMapper.getMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                OmsApiDto dto = jsonMapper.fromJson(resultStr, OmsApiDto.class, classes);
                if(dto != null){
                    List<OmsOrderDto> ood = (List<OmsOrderDto>) dto.getData();

                    if(Long.valueOf(dto.getStatus()) > 0){
                        result.setSuccess(true);
                    }else{
                        result.setSuccess(false);
                    }
                    if(StringUtils.equals(dto.getHasNext(), "1")){
                        result.setHasMore(1);
                    }
                    result.setMsg(dto.getMsg()).setData(ood).setCode(dto.getStatus());
                }else{
                    result.setSuccess(false);
                }
            }else{
                result.setSuccess(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.setSuccess(false).setMsg(e.getMessage());
        }
        return result;
    }

    public String transferWarehouse(String method, LinkedHashMap<String, Object> params) throws IOException {
        String env = getOmsApiEnv("api_oms_custom");
        long timestamp = Calendar.getInstance().getTimeInMillis();
        params.put("secret", getOmsCustomSecret(env, timestamp));
        params.put("timestamp", timestamp + "");
        String url = getWarehouseUrl(env) + "/" + method;
        Map<String, String> header = Maps.newHashMap();
        header.put("Content-Type", "application/json");
        return WebUtils.doPost(url, JsonMapper.INSTANCE.toJson(params), "utf-8", 30000, 30000, header);
    }

    /**
     * 自定义OMS接口
     */
    public <T> RestResult transferWarehouseForRestResult(String method, LinkedHashMap<String, Object> params, Class<?>... classes) {
        RestResult result = new RestResult();
        try {
            String resultStr = transferWarehouse(method, params);
            if(StringUtils.isNotBlank(resultStr)){
                JsonMapper jsonMapper = new JsonMapper();
                jsonMapper.getMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                OmsApiDto dto = jsonMapper.fromJson(resultStr, OmsApiDto.class, classes);
                if(dto != null){
                    List<OmsOrderDto> ood = (List<OmsOrderDto>) dto.getData();
                    if(Long.valueOf(dto.getStatus()) > 0){
                        result.setSuccess(true);
                    }else{
                        result.setSuccess(false);
                    }
                    if(StringUtils.equals(dto.getHasNext(), "1")){
                        result.setHasMore(1);
                    }
                    result.setMsg(dto.getMsg()).setData(ood).setCode(dto.getStatus());
                }else{
                    result.setSuccess(false);
                }
            }else{
                result.setSuccess(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.setSuccess(false).setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 获取签名
     * @param params 参数
     * @return 签名
     */
    private String getAppSign(Map<String, String> params){
        List<String> data = Lists.newArrayList();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> entry = iterator.next();
            data.add(entry.getKey() + entry.getValue());
        }
        Collections.sort(data);
        String appSign = StringUtils.join(data.toArray(new String[]{}), "");
        appSign = params.get("app_secret") + appSign + params.get("app_secret");
        return MD5Util.md5(appSign).toUpperCase();
    }

    /**
     * 获取url
     * @return
     */
    public String getOmsUrl(String env){
        String url = null;
        String apiSystem = "api_oms";
        if(StringUtils.equals("1", env)){
            url = getOmsApiDictionaryValue(apiSystem, "url");
        }else if(StringUtils.equals("2", env)){
            url = getOmsApiDictionaryValue(apiSystem, "test_url");
        }
        return url;
    }

    /**
     * 获取app_key
     * @return
     */
    public String getOmsAppKey(String env){
        String appKey = null;
        String apiSystem = "api_oms";
        if(StringUtils.equals("1", env)){
            appKey = getOmsApiDictionaryValue(apiSystem, "app_key");
        }else if(StringUtils.equals("2", env)){
            appKey = getOmsApiDictionaryValue(apiSystem, "test_app_key");
        }
        return appKey;
    }

    /**
     * 获取app_secret
     * @return
     */
    public String getOmsAppSecret(String env){
        String appSecret = null;
        String apiSystem = "api_oms";
        if(StringUtils.equals("1", env)){
            appSecret = getOmsApiDictionaryValue(apiSystem, "app_secret");
        }else if(StringUtils.equals("2", env)){
            appSecret = getOmsApiDictionaryValue(apiSystem, "test_app_secret");
        }
        return appSecret;
    }

    /**
     * 获取api环境， 1： 正式 2： 测试
     * @return
     */
    public String getOmsApiEnv(String apiSystem){
        return getOmsApiDictionaryValue(apiSystem, "env");
    }

    /**
     * 获取OMS CUSTOM API url
     * @return
     */
    public String getOmsCustomUrl(String env){
        return getCustomBaseUrl(env) + "/omsapi";
    }

    /**
     * 获取inbound API url
     * @return
     */
    public String getInboundUrl(String env){
        return getCustomBaseUrl(env) + "/inbound";
    }

    /**
     * 获取warehouse api url
     */
    public String getWarehouseUrl(String env){
        return getCustomBaseUrl(env) + "/warehouse";
    }

    /**
     * 获取CUSTOM基础url
     */
    public String getCustomBaseUrl(String env){
        String url = null;
        String apiSystem = "api_oms_custom";
        if(StringUtils.equals("1", env)){
            url = getOmsApiDictionaryValue(apiSystem, "url");
        }else if(StringUtils.equals("2", env)){
            url = getOmsApiDictionaryValue(apiSystem, "test_url");
        }
        return url;
    }

    /**
     * 获取OMS CUSTOM API key
     * @return
     */
    public String getOmsCustomKey(String env){
        String url = null;
        String apiSystem = "api_oms_custom";
        if(StringUtils.equals("1", env)){
            url = getOmsApiDictionaryValue(apiSystem, "key");
        }else if(StringUtils.equals("2", env)){
            url = getOmsApiDictionaryValue(apiSystem, "test_key");
        }
        return url;
    }

    /**
     * 获取OMS CUSTOM API iv
     * @return
     */
    public String getOmsCustomIv(String env){
        String url = null;
        String apiSystem = "api_oms_custom";
        if(StringUtils.equals("1", env)){
            url = getOmsApiDictionaryValue(apiSystem, "iv");
        }else if(StringUtils.equals("2", env)){
            url = getOmsApiDictionaryValue(apiSystem, "test_iv");
        }
        return url;
    }

    /**
     * 获取OMS CUSTOM API account
     * @return
     */
    public String getOmsCustomAccount(String env){
        String url = null;
        String apiSystem = "api_oms_custom";
        if(StringUtils.equals("1", env)){
            url = getOmsApiDictionaryValue(apiSystem, "account");
        }else if(StringUtils.equals("2", env)){
            url = getOmsApiDictionaryValue(apiSystem, "test_account");
        }
        return url;
    }

    /**
     * 獲取密钥
     * @param env
     * @param timestamp
     * @return
     */
    public String getOmsCustomSecret(String env, long timestamp){
        String account = getOmsCustomAccount(env);
        String key = getOmsCustomKey(env);
        String iv = getOmsCustomIv(env);
        try {
            return AES128Utils.encrypt(account + ":" + timestamp, key, iv);
        } catch (Exception e) {
        }
        return null;
    }

    private String getOmsApiDictionaryValue(String apiSystem, String code){
        DictionaryCallVo callVos = dictionaryService.getByCodemainAndCodeSub(apiSystem, code).get(0);
        List<DictionaryValueVo> valueVos = callVos.getOptions();
        for (DictionaryValueVo vo : valueVos) {
            if(vo.getStatus() == 1){
                return vo.getValue();
            }
        }
        return null;
    }

}
