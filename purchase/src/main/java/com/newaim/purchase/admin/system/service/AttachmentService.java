package com.newaim.purchase.admin.system.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.system.dao.AttachmentDao;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.entity.MyDocument;
import com.newaim.purchase.admin.system.vo.AttachmentVo;
import com.newaim.purchase.admin.system.vo.MyDocumentVo;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)

public class AttachmentService extends ServiceBase {

    @Autowired
    private AttachmentDao attachmentDao;

    @Autowired
    private MyDocumentService myDocumentService;


    public List<AttachmentVo> list(Sort sort) {
        List<AttachmentVo> list = Lists.newArrayList();

        List<Attachment> rows = attachmentDao.findAll(sort);

        if (rows.size() > 0) {
            list = BeanMapper.mapList(rows, Attachment.class, AttachmentVo.class);
        }

        return list;

    }

    public Page<AttachmentVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort) {

        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<Attachment> spec = buildSpecification(params);
        Page<Attachment> p = attachmentDao.findAll(spec, pageRequest);
        Page<AttachmentVo> page = p.map(new Converter<Attachment, AttachmentVo>() {
            @Override
            public AttachmentVo convert(Attachment dv) {
                return BeanMapper.map(dv, AttachmentVo.class);
            }
        });

        return page;
    }

    public Page<AttachmentVo> list(LinkedHashMap<String, Object> params, Pageable pageable) {

        Specification<Attachment> spec = buildSpecification(params);
        Page<Attachment> p = attachmentDao.findAll(spec, pageable);
        Page<AttachmentVo> page = p.map(new Converter<Attachment, AttachmentVo>() {
            @Override
            public AttachmentVo convert(Attachment dv) {
                return BeanMapper.map(dv, AttachmentVo.class);
            }
        });

        return page;
    }


    public List<AttachmentVo> listByBusinessId(String businessId) {
        List<AttachmentVo> list = Lists.newArrayList();

        List<Attachment> rows = attachmentDao.findAttachmentByBusinessId(businessId);
        if (rows.size() > 0) {
            list = BeanMapper.mapList(rows, Attachment.class, AttachmentVo.class);
        }

        return list;
    }

    /**
     * 获取多附件
     *
     * @param businessId
     * @param moduleName
     * @return
     */
    public List<AttachmentVo> listByBusinessIdAndModuleName(String businessId, String moduleName) {
        List<AttachmentVo> list = Lists.newArrayList();

        List<Attachment> rows = attachmentDao.findAttachmentByBusinessIdAndModuleName(businessId, moduleName);
        if (rows.size() > 0) {
            list = BeanMapper.mapList(rows, Attachment.class, AttachmentVo.class);
        }

        return list;
    }

    public AttachmentVo get(String id) {
        AttachmentVo o;
        Attachment row = attachmentDao.findAttachmentById(id);
        o = BeanMapper.map(row, AttachmentVo.class);

        return o;
    }

    /**
     * 获取单附件
     *
     * @param businessId
     * @param moduleName
     * @return
     */
    public AttachmentVo getByBusinessIdAndModuleName(String businessId, String moduleName) {
        List<AttachmentVo> list = Lists.newArrayList();
        AttachmentVo result = null;

        List<Attachment> rows = attachmentDao.findAttachmentByBusinessIdAndModuleName(businessId, moduleName);
        if (rows.size() > 0) {
            list = BeanMapper.mapList(rows, Attachment.class, AttachmentVo.class);
            result = list.get(0);
        }

        return result;
    }

    public Attachment getAttachment(String id) {
        Attachment o = attachmentDao.findAttachmentById(id);
        return o;
    }

    /**
     * 根据 document Id 和  business id 获取附件列表
     *
     * @param documentId
     * @param businessId
     * @return
     */
    public Attachment findAttachmentByDocumentIdAndBusinessId(String documentId, String businessId) {
        Attachment o = attachmentDao.findAttachmentByDocumentIdAndBusinessId(documentId, businessId);
        return o;
    }


    @Transactional
    public void add(Attachment o) {
        attachmentDao.clear();

        attachmentDao.save(o);
    }

    @Transactional
    public void save(Attachment o) {
        attachmentDao.save(o);
    }


    @Transactional
    public Attachment add(Attachment o, String moduleName, String businessId) {
        deleteByBusinessId(businessId, moduleName);
        if (StringUtils.isNotBlank(businessId) && !StringUtils.isEmpty(businessId)) {
            o.setBusinessId(businessId);
            o.setModuleName(moduleName);

            attachmentDao.save(o);
        }
        return o;
    }

    /**
     * 批量保存附件
     *
     * @param list
     * @param moduleName
     * @param businessId
     * @return
     */
    @Transactional
    public List<Attachment> save(List<Attachment> list, String moduleName, String businessId) {
        List<Attachment> o = Lists.newArrayList();
        deleteByBusinessId(businessId, moduleName);
        if (StringUtils.isNotBlank(businessId) && !StringUtils.isEmpty(businessId) && null != list) {
            for (int i = 0; i < list.size(); i++) {
                Attachment e = new Attachment();
                e.setBusinessId(businessId);
                e.setDocumentId(list.get(i).getDocumentId());
                e.setModuleName(moduleName);

                o.add(e);
            }
        }

        o = attachmentDao.save(o);
        return o;
    }

    @Transactional
    public void delete(String id) {
        try {
            attachmentDao.delete(id);
        } catch (Exception e) {

        }
    }

    /**
     * 根据业务单据ID删除指定字段里的附件
     *
     * @param bussinessId
     * @param moduleName
     */
    @Transactional
    public void deleteByBusinessId(String bussinessId, String moduleName) {
        try {
            attachmentDao.deleteByBusinessIdAndModuleName(bussinessId, moduleName);
        } catch (Exception e) {

        }
    }

    /**
     * 根据业务单据ID删除全部附件
     *
     * @param bussinessId
     */
    @Transactional
    public void deleteByBusinessId(String bussinessId) {
        try {
            attachmentDao.deleteByBusinessId(bussinessId);
        } catch (Exception e) {

        }
    }


    private Specification<Attachment> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Attachment> spec = DynamicSpecifications.bySearchFilter(filters.values(), Attachment.class);
        return spec;
    }

    /**
     * 根据 myDocumentId 获取图片信息List
     *
     * @param imageIds
     * @param moduleName
     * @param businessId
     * @return
     */
    public List<AttachmentVo> listByDocumentId(String imageIds, String moduleName, String businessId) {
        List<AttachmentVo> rd = Lists.newArrayList();
        LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
        if (StringUtils.isNotBlank(imageIds)) {
            params.put("id-S-IN-ADD", imageIds);
            params.put("status-N-EQ-ADD", "1");
            Page<MyDocumentVo> mdp = myDocumentService.list(params, 1, 100, null);
            if (mdp.getContent().size() > 0) {
                for (MyDocumentVo mdv : mdp.getContent()) {
                    AttachmentVo av = new AttachmentVo();
                    av.setDocument(mdv);
                    av.setDocumentId(mdv.getId());
                    av.setBusinessId(businessId);
                    av.setModuleName(moduleName);
                    rd.add(av);
                }
            }
        }
        return rd;
    }

    /**
     * @TODO 需要优化对象实例化
     * 根据myDocumentId 获取附件信息List
     *
     * @param attachmentVos
     * @param moduleName
     * @param businessId
     * @return
     */
    @Transactional
    public List<AttachmentVo> listFromAttachmentVos(List<AttachmentVo> attachmentVos, String moduleName, String businessId) {
        List<AttachmentVo> rd = Lists.newArrayList();

        if (!ListUtils.isEmpty(attachmentVos)) {
        	for(int i=0; i<attachmentVos.size(); i++) {
                AttachmentVo av = new AttachmentVo();
        		BeanMapper.copyProperties(attachmentVos.get(i), av, true);
                MyDocumentVo mdv = myDocumentService.get((String) av.getDocumentId());
                av.setDocument(mdv);
                av.setModuleName(moduleName);
                rd.add(av);
        	}
        }

        return rd;
    }
}
