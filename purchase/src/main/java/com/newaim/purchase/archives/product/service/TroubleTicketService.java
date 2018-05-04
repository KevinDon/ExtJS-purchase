package com.newaim.purchase.archives.product.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.vo.UserDepartmentVo;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.product.dao.TroubleTicketDao;
import com.newaim.purchase.archives.product.dao.TroubleTicketProductDao;
import com.newaim.purchase.archives.product.dao.TroubleTicketProductDetailDao;
import com.newaim.purchase.archives.product.dao.TroubleTicketRemarkDao;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.entity.TroubleTicket;
import com.newaim.purchase.archives.product.entity.TroubleTicketProduct;
import com.newaim.purchase.archives.product.entity.TroubleTicketProductDetail;
import com.newaim.purchase.archives.product.entity.TroubleTicketRemark;
import com.newaim.purchase.archives.product.vo.TroubleTicketVo;
import com.newaim.purchase.constants.ConstantsAttachment;
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
import java.util.List;
import java.util.LinkedHashMap;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class TroubleTicketService extends ServiceBase{

    private static final String FILE_MODULE_NAME =  ConstantsAttachment.Status.ProductProblem.code;

    @Autowired
    private TroubleTicketDao troubleTicketDao;

    @Autowired
    private TroubleTicketProductDao troubleTicketProductDao;

    @Autowired
    private TroubleTicketProductDetailDao troubleTicketProductDetailDao;

    @Autowired
    private TroubleTicketProductService troubleTicketProductService;

    @Autowired
    private TroubleTicketRemarkService troubleTicketRemarkService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ProductService productService;

    public Page<TroubleTicketVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<TroubleTicket> spec = buildSpecification(params);
        Page<TroubleTicket> p = troubleTicketDao.findAll(spec, pageRequest);
        Page<TroubleTicketVo> page = p.map(new Converter<TroubleTicket, TroubleTicketVo>() {
            @Override
            public TroubleTicketVo convert(TroubleTicket troubleTicket) {
                return convertToTroubleTicketVo(troubleTicket);
            }
        });
        return page;
    }

    public TroubleTicket getTroubleTicket(String id){
        return troubleTicketDao.findOne(id);
    }

    /**
     * 单独获取产品证书Vo
     * @param id
     * @return
     */
    public TroubleTicketVo getTroubleTicketVo(String id){
        TroubleTicketVo vo =convertToTroubleTicketVo(getTroubleTicket(id));
        return vo;
    }

    /**
     * 获取证书Vo和对应的附件、供应商、明细信息
     * @param id
     * @return
     */
    public TroubleTicketVo get(String id){
        TroubleTicketVo vo =convertToTroubleTicketVo(getTroubleTicket(id));
        vo.setDetails(troubleTicketProductService.findDetailsVoByTroubleTicketId(id));
        vo.setComments(troubleTicketRemarkService.findRemarksVoByTroubleTicketId(id));
        vo.setAttachments(attachmentService.listByBusinessIdAndModuleName(id, FILE_MODULE_NAME));
        return vo;
    }

    private TroubleTicketVo convertToTroubleTicketVo(TroubleTicket troubleTicket){
        TroubleTicketVo vo = BeanMapper.map(troubleTicket, TroubleTicketVo.class);
        return vo;
    }

    /**
     * 添加
     * @param troubleTicket
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public TroubleTicket add(TroubleTicket troubleTicket, List<Attachment> attachments, List<TroubleTicketProduct> details){
        //设置创建人相关信息
        setCreatorInfo(troubleTicket);
        //草稿
        troubleTicket.setStatus(Constants.Status.DRAFT.code);
        troubleTicket = troubleTicketDao.save(troubleTicket);
        saveCommentInfo(troubleTicket);
        saveDetails(troubleTicket,details);
        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, troubleTicket.getId());
        return troubleTicket;
    }

    @Transactional(rollbackFor = Exception.class)
    public TroubleTicket update(TroubleTicket troubleTicket, List<Attachment> attachments, List<TroubleTicketProduct> details){
        //设置更新时间
        troubleTicket.setStatus(Constants.Status.DRAFT.code);
        troubleTicket.setUpdatedAt(new Date());
        saveCommentInfo(troubleTicket);
        //保存明细
        saveDetails(troubleTicket,details);
        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, troubleTicket.getId());
        return troubleTicketDao.save(troubleTicket);
    }

    /**
     * 复制另存
     * @param troubleTicket
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public TroubleTicket saveAs(TroubleTicket troubleTicket, List<Attachment> attachments, List<TroubleTicketProduct> details){
        troubleTicketDao.clear();
        //设置创建人相关信息
        setCreatorInfo(troubleTicket);
        troubleTicket.setId(null);
        troubleTicket = troubleTicketDao.save(troubleTicket);
        saveCommentInfo(troubleTicket);
        saveDetails(troubleTicket,details);
        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, troubleTicket.getId());
        return troubleTicket;
    }

    /**
     * 每次保存时新增
     * @param troubleTicket
     */
    private void saveCommentInfo(TroubleTicket troubleTicket){
        if(StringUtils.isNotBlank(troubleTicket.getComment())){
            TroubleTicketRemark comment = new TroubleTicketRemark();
            comment.setTroubleTicketId(troubleTicket.getId());
            comment.setCreatedAt(new Date());
            comment.setRemark(troubleTicket.getComment());
            UserVo user = SessionUtils.currentUserVo();
            comment.setCreatorId(user.getId());
            comment.setCreatorCnName(user.getCnName());
            comment.setCreatorEnName(user.getEnName());
            comment.setDepartmentId(user.getDepartmentId());
            comment.setDepartmentCnName(user.getDepartmentCnName());
            comment.setDepartmentEnName(user.getDepartmentEnName());
            troubleTicketRemarkService.save(comment);
        }
    }

    /**
     * 设置证书创建人信息，同时设置所属部门，用于新增时
     * @param troubleTicket
     */
    private void setCreatorInfo(TroubleTicket troubleTicket){
        UserVo user = SessionUtils.currentUserVo();
        troubleTicket.setCreatedAt(new Date());
        troubleTicket.setCreatorId(user.getId());
        troubleTicket.setCreatorCnName(user.getCnName());
        troubleTicket.setCreatorEnName(user.getEnName());
        UserDepartmentVo department = user.getDepartment();
        if(department != null){
            troubleTicket.setDepartmentId(department.getId());
            troubleTicket.setDepartmentCnName(department.getCnName());
            troubleTicket.setDepartmentEnName(department.getEnName());
        }
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(TroubleTicket troubleTicket, List<TroubleTicketProduct> details){
        String troubleTicketId = troubleTicket.getId();
        troubleTicketProductDao.deleteByTroubleTicketId(troubleTicketId);
        troubleTicketProductDetailDao.deleteByTroubleTicketId(troubleTicketId);
        if(details != null){
            for (int i = 0; i < details.size(); i++) {
                TroubleTicketProduct troubleTicketProduct = details.get(i);
                troubleTicketProduct.setTroubleTicketId(troubleTicketId);
                Product product = productService.findProductBySku(troubleTicketProduct.getSku());
                if(product != null){
                    troubleTicketProduct.setProductId(product.getId());
                    troubleTicketProduct.setProductName(product.getName());
                    troubleTicketProduct.setCombined(product.getCombined());
                    troubleTicketProduct.setCategoryId(product.getCategoryId());
                    troubleTicketProduct.setCategoryCnName(product.getCategoryCnName());
                    troubleTicketProduct.setCategoryEnName(product.getCategoryEnName());
                }
                troubleTicketProduct.setOrderId(troubleTicket.getOrderId());
                troubleTicketProduct.setOrderNumber(troubleTicket.getOrderNumber());
                troubleTicketProductDao.save(troubleTicketProduct);
                List<TroubleTicketProductDetail> troubleDetails = troubleTicketProduct.getTroubleDetails();
                if(troubleDetails != null && troubleDetails.size() > 0){
                    for (int j = 0; j < troubleDetails.size(); j++) {
                        TroubleTicketProductDetail troubleDetail = troubleDetails.get(j);
                        troubleDetail.setTroubleTicketId(troubleTicketId);
                        troubleDetail.setProductId(troubleTicketProduct.getProductId());
                        troubleDetail.setSku(troubleTicketProduct.getSku());
                        troubleDetail.setTroubleTicketProductId(troubleTicketProduct.getId());
                    }
                    troubleTicketProductDetailDao.save(troubleDetails);
                }
            }
        }
    }

    /**
     * 确认
     * @param id 产品id
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmTroubleTicket(String id){
        updateTroubleTicketStatus(id, Constants.Status.NORMAL.code);
    }

    /**
     * 标识为删除
     * @param id 产品ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteTroubleTicket(String id){
        updateTroubleTicketStatus(id, Constants.Status.DELETED.code);
    }

    /**
     * 更新产品状态
     * @param id
     * @param status
     */
    private void updateTroubleTicketStatus(String id, Integer status){
        TroubleTicket troubleTicket = getTroubleTicket(id);
        if(troubleTicket != null){
            troubleTicket.setStatus(status);
            troubleTicket.setUpdatedAt(new Date());
            troubleTicketDao.save(troubleTicket);
        }
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<TroubleTicket> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<TroubleTicket> spec = DynamicSpecifications.bySearchFilter(filters.values(), TroubleTicket.class);
        return spec;
    }

}
