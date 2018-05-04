package com.newaim.purchase.admin.system.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.account.dao.DepartmentDao;
import com.newaim.purchase.admin.account.entity.Department;
import com.newaim.purchase.admin.system.dao.AutoCodeDao;
import com.newaim.purchase.admin.system.entity.AutoCode;
import com.newaim.purchase.admin.system.vo.AutoCodeVo;
import com.newaim.purchase.flow.inspection.dao.FlowOrderQcDao;
import com.newaim.purchase.flow.inspection.entity.FlowOrderQc;
import com.newaim.purchase.flow.purchase.dao.FlowSampleDao;
import com.newaim.purchase.flow.purchase.entity.FlowSample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.LinkedHashMap;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class AutoCodeService extends ServiceBase {

    @Autowired
    private AutoCodeDao autoCodeeDao;

    @Autowired
    private FlowSampleDao flowSampleDao;

    @Autowired
    private FlowOrderQcDao flowOrderQcDao;

    @Autowired
    private DepartmentDao departmentDao;

    /**
     * 分页查询所有提醒信息
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<AutoCodeVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<AutoCode> spec = buildSpecification(params);
        Page<AutoCode> p = autoCodeeDao.findAll(spec, pageRequest);
        Page<AutoCodeVo> page = p.map(cronJob -> {
            AutoCodeVo vo = convertToVo(cronJob);
            return vo;
        });
        return page;
    }

    /**
     * entity转换为Vo
     * @param autoCodee
     * @return
     */
    private AutoCodeVo convertToVo(AutoCode autoCodee){
        AutoCodeVo vo = BeanMapper.map(autoCodee, AutoCodeVo.class);
        return vo;
    }

    public AutoCode getAutoCodee(String id){
        return autoCodeeDao.findOne(id);
    }

    public AutoCodeVo get(String id){
        AutoCodeVo o = convertToVo(getAutoCodee(id));
        return o;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(AutoCode autoCodee){
        autoCodee.setUpdatedAt(new Date());
        setDeparmentInfo(autoCodee);
        autoCodeeDao.save(autoCodee);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAs(AutoCode autoCodee){
        autoCodeeDao.clear();
        autoCodee.setUpdatedAt(new Date());
        setDeparmentInfo(autoCodee);
        autoCodeeDao.save(autoCodee);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(AutoCode autoCodee){
        autoCodee.setUpdatedAt(new Date());
        setDeparmentInfo(autoCodee);
        autoCodeeDao.save(autoCodee);
    }

    private void setDeparmentInfo(AutoCode autoCode){
        if(StringUtils.isNotBlank(autoCode.getDepartmentId())){
            Department department = departmentDao.findOne(autoCode.getDepartmentId());
            if(department != null){
                autoCode.setDepartmentCnName(department.getCnName());
                autoCode.setDepartmentEnName(department.getEnName());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String id){
        autoCodeeDao.delete(id);
    }

    public AutoCode findByCodeAndDepartmentId(String code, String departmentId){
        List<AutoCode> data = autoCodeeDao.findByCodeAndDepartmentId(code, departmentId);
        return data != null && data.size() > 0 ? data.get(0) : null;
    }

    public AutoCode findByCode(String code){
        List<AutoCode> data = autoCodeeDao.findByCode(code);
        return data != null && data.size() > 0 ? data.get(0) : null;
    }

    private synchronized String generateValue(AutoCode autoCode){
        String value = null;
        try {
            Template template = new Template(null, autoCode.getFormat(), new Configuration(Configuration.getVersion()));
            value = FreeMarkerTemplateUtils.processTemplateIntoString(template, autoCode);
            if(autoCode.getMainValue() != null){
                autoCode.setMainValue(autoCode.getMainValue() + 1);
            }
            if(autoCode.getSubValue() != null){
                autoCode.setSubValue(autoCode.getSubValue() + 1);
            }
            autoCode.setUpdatedAt(new Date());
            autoCode.setLastValue(value);
            autoCodeeDao.save(autoCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Transactional(rollbackFor = Exception.class)
    public synchronized String generateValue(String code, String departmentId){
        String value = null;
        AutoCode autoCode;
        if(StringUtils.isNotBlank(departmentId)){
            autoCode = findByCodeAndDepartmentId(code, departmentId);
        }else{
            autoCode = findByCode(code);
        }
        if(autoCode != null){
            value = generateValue(autoCode);
        }
        return value;
    }

    @Transactional(rollbackFor = Exception.class)
    public synchronized String generateValueByCodeAndBusinessId(String code, String businessId){
        String departmentId = null;
        if(StringUtils.equals(code, "sample_inspection")){
            //样品质检
            FlowSample flowSample = flowSampleDao.findOne(businessId);
            if(flowSample != null){
                departmentId = flowSample.getDepartmentId();
            }
        }else if(StringUtils.equals(code, "order_inspection")){
            //订单质检
            FlowOrderQc flowOrderQc = flowOrderQcDao.findOne(businessId);
            if(flowOrderQc != null){
                departmentId = flowOrderQc.getDepartmentId();
            }
        }
        return generateValue(code, departmentId);
    }

    private Specification<AutoCode> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<AutoCode> spec = DynamicSpecifications.bySearchFilter(filters.values(), AutoCode.class);
        return spec;
    }

}
