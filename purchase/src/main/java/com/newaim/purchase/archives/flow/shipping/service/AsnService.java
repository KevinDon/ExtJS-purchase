package com.newaim.purchase.archives.flow.shipping.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.shipping.dao.AsnDao;
import com.newaim.purchase.archives.flow.shipping.entity.Asn;
import com.newaim.purchase.archives.flow.shipping.vo.AsnPackingDetailVo;
import com.newaim.purchase.archives.flow.shipping.vo.AsnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class AsnService extends ServiceBase {

    @Autowired
    private AsnDao asnDao;

    @Autowired
    private AsnPackingDetailService asnPackingDetailService;

    /**
     * 分页查询正式收货通知数据
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<AsnVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<Asn> spec = buildSpecification(params);
        Page<Asn> p = asnDao.findAll(spec, pageRequest);
        Page<AsnVo> page = p.map(asn -> {
            AsnVo vo = convertToAsnVo(asn);
            vo.setDetails(asnPackingDetailService.findPackingDetailsVoByAsnId(vo.getId()));
            return vo;
        });
        return page;
    }

    /**
     * 将entity转换为Vo
     * @param asn
     * @return
     */
    private AsnVo convertToAsnVo(Asn asn){
        AsnVo vo = BeanMapper.map(asn, AsnVo.class);
        return vo;
    }

    /**
     * 动态建立关系
     * @param searchParams
     * @return
     */
    private Specification<Asn> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Asn> spec = DynamicSpecifications.bySearchFilter(filters.values(), Asn.class);
        return spec;
    }

    /**
     * 根据ID获取收货通知
     * @param id
     * @return
     */
    public Asn getAsn(String id){
        return asnDao.findOne(id);
    }

    /**
     * 根据ID获取收货通知
     * @param id
     * @return
     */
    public AsnVo get(String id){
        AsnVo vo =convertToAsnVo(getAsn(id));
        vo.setDetails(asnPackingDetailService.findPackingDetailsVoByAsnId(vo.getId()));
        return vo;
    }
    

}
