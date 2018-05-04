package com.newaim.purchase.archives.flow.purchase.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.purchase.dao.SampleDao;
import com.newaim.purchase.archives.flow.purchase.dao.SampleDetailDao;
import com.newaim.purchase.archives.flow.purchase.dao.SampleOtherDetailDao;
import com.newaim.purchase.archives.flow.purchase.vo.SampleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import com.newaim.purchase.archives.flow.purchase.entity.Sample;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SampleService extends ServiceBase {

    @Autowired
    private SampleDao sampleDao;

    @Autowired
    private SampleDetailService sampleDetailService;

    @Autowired
    private SampleOtherDetailService sampleOtherDetailService;

    public Page<SampleVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<Sample> spec = buildSpecification(params);
        Page<Sample> p = sampleDao.findAll(spec, pageRequest);
        Page<SampleVo> page = p.map(new Converter<Sample, SampleVo>() {
            @Override
            public SampleVo convert(Sample sample) {
                return convertToSampleVo(sample);
            }
        });
        return page;
    }

    private SampleVo convertToSampleVo(Sample sample){
        SampleVo vo = BeanMapper.map(sample, SampleVo.class);
        return vo;
    }

    public Sample getSample(String id){
        return sampleDao.findOne(id);
    }

    public SampleVo get(String id){
        SampleVo vo =convertToSampleVo(getSample(id));
        vo.setDetails(sampleDetailService.findDetailVosBySampleId(id));
        vo.setOtherDetails(sampleOtherDetailService.findOtherDetailVosBySampleId(id));
        return vo;
    }

    private Specification<Sample> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Sample> spec = DynamicSpecifications.bySearchFilter(filters.values(), Sample.class);
        return spec;
    }

}
