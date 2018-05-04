package com.newaim.purchase.archives.product.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.vo.UserDepartmentVo;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.archives.product.dao.TariffDao;
import com.newaim.purchase.archives.product.entity.Tariff;
import com.newaim.purchase.archives.product.vo.TariffVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created by Mark on 2017/9/18.
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class TariffService extends ServiceBase {

    @Autowired
    private TariffDao tariffDao;

    @Autowired
    private ProductService productService;

    public Page<TariffVo> listTariff(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<Tariff> spec = buildSpecification(params);
        Page<Tariff> p = tariffDao.findAll(spec, pageRequest);
        Page<TariffVo> page = p.map(tariff -> convertToTariffVo(tariff));
        return page;
    }

    public Tariff getTariff(String id){
        return tariffDao.findOne(id);
    }

    public TariffVo get(String id){
        TariffVo vo = convertToTariffVo(getTariff(id));
        return vo;
    }

    private TariffVo convertToTariffVo(Tariff tariff){
        TariffVo vo = BeanMapper.map(tariff, TariffVo.class);
        vo.setProducts(productService.listProductsByHsCode(vo.getItemCode()));
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAs(Tariff tariff){
        tariffDao.clear();

        //设置创建人相关信息
        setCreatorInfo(tariff);
        //草稿
        tariff.setStatus(Constants.Status.DRAFT.code);

        tariffDao.save(tariff);

    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Tariff tariff){
        //设置更新信息
        tariff.setUpdatedAt(new Date());
        tariffDao.save(tariff);
    }



    @Transactional(rollbackFor = Exception.class)
    public void add(Tariff tariff){
        setCreatorInfo(tariff);
        //草稿
        tariff.setStatus(Constants.Status.DRAFT.code);
        tariffDao.save(tariff);

    }

    /**
     * 设置产品创建人信息，同时设置所属部门，用于新增时
     * @param tariff
     */
    private void setCreatorInfo(Tariff tariff){
        UserVo user = SessionUtils.currentUserVo();
        tariff.setCreatedAt(new Date());
        tariff.setCreatorId(user.getId());
        tariff.setCreatorCnName(user.getCnName());
        tariff.setCreatorEnName(user.getEnName());
        UserDepartmentVo department = user.getDepartment();
        if(department != null){
            tariff.setDepartmentId(department.getId());
            tariff.setDepartmentCnName(department.getCnName());
            tariff.setDepartmentEnName(department.getEnName());
        }
    }
    /**
     * 序列化档案修改申请对象
     * @param o
     * @return
     */
    public TariffVo getApplyVoToSave(Tariff o) {
    	

  /*        setProductCombinedCreatorInfo(o);
          saveDetails(o.getId(),details);*/


    	TariffVo vo = BeanMapper.map(o, TariffVo.class);

      /*  //产品明细表
        if (details.size() > 0) {
            vo.setDetails(BeanMapper.mapList(details, ProductCombinedDetail.class, ProductCombinedDetailVo.class));
        } else {
            vo.setDetails(null);
        }*/


        return vo;

    }

    /**
     * 反序列化档案修改申请对象
     *
     * @param serviceProvider
     * @return
     */
    public TariffVo getApplyVoToDisplay(TariffVo vo) {

        


        return vo;
    }

    /**
     * 确认
     * @param id 产品id
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmTariff(String id){
        updateTariffStatus(id, Constants.Status.NORMAL.code);
    }

    /**
     * 标识为删除
     * @param id 产品ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteTariff(String id){
        updateTariffStatus(id, Constants.Status.DELETED.code);
    }

    /**
     * 更新产品状态
     * @param id
     * @param status
     */
    private void updateTariffStatus(String id, Integer status){
        Tariff tariff = getTariff(id);
        if(tariff != null){
            tariff.setStatus(status);
            tariff.setUpdatedAt(new Date());
            tariffDao.save(tariff);
        }
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Tariff> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Tariff> spec = DynamicSpecifications.bySearchFilter(filters.values(), Tariff.class);
        return spec;
    }
}
