package com.newaim.purchase.admin.system.service;


import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserDepartmentVo;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.dao.BuyerInfoDao;
import com.newaim.purchase.admin.system.entity.BuyerInfo;
import com.newaim.purchase.admin.system.vo.BuyerInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class BuyerInfoService extends ServiceBase {

    @Autowired
    private BuyerInfoDao buyerInfoDao;

    public Page<BuyerInfoVo> listBuyerInfo(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<BuyerInfo> spec =buildSpecification(params);
        Page<BuyerInfo> p = buyerInfoDao.findAll(spec, pageRequest);
        Page<BuyerInfoVo> page = p.map(new Converter<BuyerInfo, BuyerInfoVo>() {
            @Override
            public BuyerInfoVo convert(BuyerInfo buyerInfo) {
                return convertToBuyerInfoVo(buyerInfo);
            }
        });

        return page;
    }

    public BuyerInfo getBuyerInfo(String id){
        return buyerInfoDao.findOne(id);
    }

    public BuyerInfoVo get(String id){
        BuyerInfoVo vo= convertToBuyerInfoVo(getBuyerInfo(id));
        return vo;
    }

    private BuyerInfoVo convertToBuyerInfoVo(BuyerInfo buyerInfo){
        BuyerInfoVo vo = BeanMapper.map(buyerInfo, BuyerInfoVo.class);
        return vo;
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<BuyerInfo> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<BuyerInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), BuyerInfo.class);
        return spec;
    }

    //新建
    @Transactional
    public void add(BuyerInfo buyerInfo){
        buyerInfoDao.clear();
        //设置买方创建人信息
        setBuyerCreatorInfo(buyerInfo);
        buyerInfoDao.save(buyerInfo);
    }

    //保存
    @Transactional
    public void save(BuyerInfo buyerInfo){
        if (StringUtils.isNotBlank(buyerInfo.getId())){
            //设置更新信息
            buyerInfo.setUpdatedAt(new Date());
        }else {
            //设置创建人信息
            setBuyerCreatorInfo(buyerInfo);
        }
        buyerInfoDao.save(buyerInfo);
    }

    //另存
    @Transactional
    public BuyerInfo saveAs(BuyerInfo buyerInfo){
            buyerInfoDao.clear();
            //设置买方创建人相关信息
            setBuyerCreatorInfo(buyerInfo);
            buyerInfoDao.save(buyerInfo);
            return buyerInfo;
    }

    /**
     * 设置买方创建人信息
     * @param buyerInfo
     */
    private void setBuyerCreatorInfo(BuyerInfo buyerInfo){
        UserVo user = SessionUtils.currentUserVo();
        if (StringUtils.isBlank(buyerInfo.getId())){
            buyerInfo.setCreatedAt(new Date());
            buyerInfo.setStatus(1);
        }else {
            buyerInfo.setUpdatedAt(new Date());
        }
        buyerInfo.setCreatorId(user.getId());
        buyerInfo.setCreatorCnName(user.getCnName());
        buyerInfo.setCreatorEnName(user.getEnName());
        UserDepartmentVo department = user.getDepartment();
        if(department != null){
            buyerInfo.setDepartmentId(department.getId());
            buyerInfo.setDepartmentCnName(department.getCnName());
            buyerInfo.setDepartmentEnName(department.getEnName());
        }
        buyerInfo.setId(user.getId());
        buyerInfo.setCnName(user.getCnName());
        buyerInfo.setEnName(user.getEnName());
    }


    /**
     * 确认
     * @param id
     */
    @Transactional
    public void confirmBuyerInfo(String id){
        BuyerInfo buyerInfo = buyerInfoDao.findOne(id);
        buyerInfo.setStatus(1);
        buyerInfo.setUpdatedAt(new Date());
        buyerInfoDao.save(buyerInfo);
    }

    @Transactional
    public void deleteBuyerInfo(String id){
        buyerInfoDao.delete(id);
    }

}
