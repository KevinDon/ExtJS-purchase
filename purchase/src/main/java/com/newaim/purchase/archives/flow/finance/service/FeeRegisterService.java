package com.newaim.purchase.archives.flow.finance.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.flow.finance.dao.FeeRegisterDao;
import com.newaim.purchase.archives.flow.finance.entity.FeeRegister;
import com.newaim.purchase.archives.flow.finance.vo.FeeRegisterVo;
import com.newaim.purchase.flow.finance.service.FlowFeeRegisterDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FeeRegisterService extends ServiceBase{

    @Autowired
    private FeeRegisterDao feeRegisterDao;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private FeeRegisterDetailService feeRegisterDetailService;

    /**
     * 分页查询正式费用登记数据
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FeeRegisterVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FeeRegister> spec = buildSpecification(params);
        Page<FeeRegister> p = feeRegisterDao.findAll(spec, pageRequest);
        Page<FeeRegisterVo> page = p.map(feeRegister -> {
            FeeRegisterVo vo = convertToFeeRegisterVo(feeRegister);
            vo.setAttachments(attachmentService.listByBusinessId(vo.getBusinessId()));
            vo.setDetails(feeRegisterDetailService.findDetailsVoByFeeRegisterIdAndType(vo.getId(), 1));
            vo.setOtherDetails(feeRegisterDetailService.findDetailsVoByFeeRegisterIdAndType(vo.getId(), 2));
            return vo;
        });
        return page;
    }

    /**
     * 将entity转换为Vo
     * @param feeRegister
     * @return
     */
    private FeeRegisterVo convertToFeeRegisterVo(FeeRegister feeRegister){
        FeeRegisterVo vo = BeanMapper.map(feeRegister, FeeRegisterVo.class);
        return vo;
    }

    /**
     * 动态建立关系
     * @param searchParams
     * @return
     */
    private Specification<FeeRegister> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FeeRegister> spec = DynamicSpecifications.bySearchFilter(filters.values(), FeeRegister.class);
        return spec;
    }

    /**
     * 根据ID获取费用登记
     * @param id
     * @return
     */
    public FeeRegister getFeeRegister(String id){
        return feeRegisterDao.findOne(id);
    }

    /**
     * 根据ID获取费用登记
     * @param id
     * @return
     */
    public FeeRegisterVo get(String id){
        FeeRegisterVo vo =convertToFeeRegisterVo(getFeeRegister(id));
        vo.setDetails(feeRegisterDetailService.findDetailsVoByFeeRegisterIdAndType(id, 1));
        vo.setOtherDetails(feeRegisterDetailService.findDetailsVoByFeeRegisterIdAndType(id, 2));
        vo.setAttachments(attachmentService.listByBusinessId(vo.getBusinessId()));
        return vo;
    }

}
