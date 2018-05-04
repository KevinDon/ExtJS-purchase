package com.newaim.purchase.archives.flow.purchase.service;


import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.flow.purchase.dao.CustomClearanceDao;
import com.newaim.purchase.archives.flow.purchase.entity.CustomClearance;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearanceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CustomClearanceService {

    @Autowired
    private CustomClearanceDao customClearanceDao;

    /**
     * 根据业务ID查找清关信息
     * @param id
     * @return
     */
    public CustomClearance getCustomClearance(String id){
        return customClearanceDao.findOne(id);
    }

    /**
     * 根据业务ID查找清关信息
     * @param id
     * @return
     *
     */
    public CustomClearanceVo getCustomClearanceVo(String id){
        return BeanMapper.map(getCustomClearance(id), CustomClearanceVo.class);
    }

}
