package com.newaim.purchase.desktop.systemtools.service;

import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserDepartmentVo;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.desktop.systemtools.dao.RateControlDao;
import com.newaim.purchase.desktop.systemtools.entity.RateControl;
import com.newaim.purchase.desktop.systemtools.vo.RateControlVo;
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
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class RateControlService extends ServiceBase{

    @Autowired
    private RateControlDao rateControlDao;

    /**
     * 分页查询
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<RateControlVo> listRateControl(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<RateControl> spec = buildSpecification(params);
        Page<RateControl> p = rateControlDao.findAll(spec, pageRequest);
        Page<RateControlVo> page = p.map(new Converter<RateControl, RateControlVo>() {
            @Override
            public RateControlVo convert(RateControl rateControl) {
                return convertToRateControlVo(rateControl);
            }
        });
        return page;
    }

    /**
     * 获取最新并且有效的汇率
     * @return
     */
    public RateControlVo listNewRate(){
        RateControlVo o = null;
        List<RateControl> rows = rateControlDao.listNewRate();
        if(rows.size()>0) {
            o = BeanMapper.map(rows.get(0), RateControlVo.class);
        }

        return o;
    }


    public RateControl getRateControl(String id){
        return rateControlDao.findOne(id);
    }

    /**
     * 根据ID获取汇率信息
     * @param id
     * @return
     */
    public RateControlVo get(String id){
        RateControlVo vo= convertToRateControlVo(getRateControl(id));
        return vo;
    }

    private RateControlVo convertToRateControlVo(RateControl rateControl){
        RateControlVo vo = BeanMapper.map(rateControl, RateControlVo.class);
        return vo;
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<RateControl> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<RateControl> spec = DynamicSpecifications.bySearchFilter(filters.values(), RateControl.class);
        return spec;
    }

    /**
     * 添加
     * @param rateControl
     */
    @Transactional
    public void add(RateControl rateControl){
        //设置创建人信息
        setRateContolCreatorInfo(rateControl);
        rateControlDao.save(rateControl);
    }

    /**
     * 保存
     * @param rateControl
     */
    @Transactional
    public void save(RateControl rateControl){
        if (StringUtils.isNotBlank(rateControl.getId())){
            //设置更新信息
            rateControl.setUpdatedAt(new Date());
        }else {
            //设置创建人信息
            setRateContolCreatorInfo(rateControl);
        }
        rateControlDao.save(rateControl);
        }

    /**
     * 另存
     * @param rateControl
     * @return
     */
    @Transactional
    public RateControl saveAs(RateControl rateControl){
        rateControlDao.clear();
        setRateContolCreatorInfo(rateControl);
        rateControl.setId(null);
        rateControl.setUpdatedAt(null);
        rateControlDao.save(rateControl);
        return rateControl;
    }

    /**
     * 设置汇率管理的创建人信息
     * @param rateControl
     */
    private void  setRateContolCreatorInfo(RateControl rateControl){
        UserVo user = SessionUtils.currentUserVo();
        if(StringUtils.isBlank(rateControl.getId())){
            rateControl.setCreatedAt(new Date());
            rateControl.setStatus(1);
        }else{
            rateControl.setUpdatedAt(new Date());
        }
        rateControl.setCreatorId(user.getId());
        rateControl.setCreatorCnName(user.getCnName());
        rateControl.setCreatorEnName(user.getEnName());
        UserDepartmentVo department = user.getDepartment();
        if(department != null){
            rateControl.setDepartmentId(department.getId());
            rateControl.setDepartmentCnName(department.getCnName());
            rateControl.setDepartmentEnName(department.getEnName());
        }
    }

    /**
     * 确认
     * @param id
     */
    @Transactional
    public void confirmVendor(String id){
        RateControl rateControl = rateControlDao.findOne(id);
        rateControl.setStatus(1);
        rateControl.setUpdatedAt(new Date());
        rateControlDao.save(rateControl);
    }

    /**
     * 删除
     * @param id
     */
    @Transactional
    public void deleteRateControl(String id){
        rateControlDao.delete(id);
    }


}
