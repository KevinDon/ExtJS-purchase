//package com.newaim.purchase.api.service;
//
//import com.google.common.collect.Maps;
//import com.newaim.core.mapper.JsonMapper;
//import com.newaim.core.utils.RestResult;
//import com.newaim.core.utils.WebUtils;
//import com.newaim.purchase.admin.system.service.DictionaryService;
//import com.newaim.purchase.admin.system.vo.DictionaryCallVo;
//import com.newaim.purchase.admin.system.vo.DictionaryValueVo;
//import com.newaim.purchase.api.dto.WmsApiDto;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.LinkedHashMap;
//
//@Service
//public class WmsTransferService {
//
//    @Autowired
//    private DictionaryService dictionaryService;
//
//    public String transfer(String method, String jsonParams) throws IOException {
//        String env = getWmsApiEnv();
//        String url = getWmsUrl(env) + "/" + method;
//        Map<String, String> header = Maps.newHashMap();
//        header.put("Content-Type", "application/json");
//        return WebUtils.doPost(url, jsonParams, "utf-8", 30000, 30000, header);
//    }
//
//    public RestResult transferForRestResult(String method, String jsonParams) throws IOException {
//        RestResult result = new RestResult();
//        try {
//            String resultStr = transfer(method, jsonParams);
//            if(StringUtils.isNotBlank(resultStr)){
//                WmsApiDto dto = JsonMapper.INSTANCE.fromJson(resultStr, WmsApiDto.class);
//                if(dto != null){
//                    if(Long.valueOf(dto.getStatus()) > 0){
//                        result.setSuccess(true);
//                    }else{
//                        result.setSuccess(false);
//                    }
//                    if(StringUtils.equals(dto.getHasNext(), "1")){
//                        result.setHasMore(1);
//                    }
//                    result.setMsg(dto.getMsg()).setData(dto.getData()).setCode(dto.getStatus());
//                }else{
//                    result.setSuccess(false);
//                }
//            }else{
//                result.setSuccess(false);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            result.setSuccess(false).setMsg(e.getMessage());
//        }
//        return result;
//    }
//
//    /**
//     * 获取url
//     * @return
//     */
//    public String getWmsUrl(String env){
//        String url = null;
//        if(StringUtils.equals("1", env)){
//            url = getWmsApiDictionaryValue("api_wms", "url");
//        }else if(StringUtils.equals("2", env)){
//            url = getWmsApiDictionaryValue("api_wms", "test_url");
//        }
//        return url;
//    }
//
//
//    /**
//     * 获取api环境， 1： 正式 2： 测试
//     * @return
//     */
//    public String getWmsApiEnv(){
//        return getWmsApiDictionaryValue("api_wms", "env");
//    }
//
//    private String getWmsApiDictionaryValue(String apiSystem, String code){
//        DictionaryCallVo callVos = dictionaryService.getByCodemainAndCodeSub(apiSystem, code).get(0);
//        List<DictionaryValueVo> valueVos = callVos.getOptions();
//        for (DictionaryValueVo vo : valueVos) {
//            if(vo.getStatus() == 1){
//                return vo.getValue();
//            }
//        }
//        return null;
//    }
//
//}
