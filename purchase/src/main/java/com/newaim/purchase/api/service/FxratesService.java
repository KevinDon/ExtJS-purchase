package com.newaim.purchase.api.service;

import com.google.common.collect.Maps;
import com.newaim.core.utils.HttpUtils;
import com.newaim.core.utils.WebUtils;
import com.newaim.purchase.admin.system.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class FxratesService {

    @Autowired
    private DictionaryService dictionaryService;

    public String transfer() throws IOException {
        String nabKeys = dictionaryService.getByCodemainAndCodeSub("api_finance", "nab_key").get(0).getOptions().get(0).getValue();
        String nabUrl = dictionaryService.getByCodemainAndCodeSub("api_finance", "nab_url_fxrates").get(0).getOptions().get(0).getValue();

        Map<String, String> headerMap = Maps.newHashMap();
        headerMap.put("x-nab-key", nabKeys);

        return HttpUtils.sendGet(nabUrl, null, headerMap);
    }

}
