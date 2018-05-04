package com.newaim.purchase.desktop.reports.service;

import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.admin.system.vo.AttachmentVo;
import com.newaim.purchase.archives.vendor.entity.Vendor;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.desktop.reports.dao.ReportsDao;
import com.newaim.purchase.desktop.reports.dao.ReportsProductTroubleDetailDao;
import com.newaim.purchase.desktop.reports.entity.Reports;
import com.newaim.purchase.desktop.reports.entity.ReportsProduct;
import com.newaim.purchase.desktop.reports.entity.ReportsProductTroubleDetail;
import com.newaim.purchase.desktop.reports.vo.ReportsVo;
import org.apache.commons.lang3.ArrayUtils;
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
@Transactional(readOnly=true, rollbackFor = Exception.class)
public class ReportsService extends ServiceBase {

	@Autowired
	private ReportsDao reportsDao;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private ReportsProductService reportsProductService;

    @Autowired
    private ReportsProductTroubleDetailDao reportsProductTroubleDetailDao;


	/**
	 * 标准的查询符合条件的记录
	 * @param params
	 * @param pageNumber
	 * @param pageSize
	 * @param sort
	 * @return
	 */
	public Page<ReportsVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<Reports> spec = buildSpecification(params);
        Page<Reports> p = reportsDao.findAll(spec, pageRequest);
        Page<ReportsVo> page = p.map(new Converter<Reports, ReportsVo>() {
		    @Override
		    public ReportsVo convert(Reports dv) {
		        return BeanMapper.map(dv, ReportsVo.class);
		    }
		});
        
		return page;
	}

	/**
	 * 根据供应商ID获取所有相关报告
	 * @param vendorId
	 * @return
	 */
	public List<ReportsVo> listByVendorId(String vendorId) {
		List<ReportsVo> list = Lists.newArrayList();
		
		List<Reports> rows = reportsDao.findReportsByVendorId(vendorId);
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, Reports.class, ReportsVo.class);
		}
		
		return list;
	}
	
	
	public ReportsVo get(String id){
        ReportsVo vo = new ReportsVo();
        try {
            Reports row = reportsDao.findReportsById(id);
            if(null !=row ){
            	vo = BeanMapper.map(row, ReportsVo.class);
			}

        }finally {
            return vo;
        }
	}

	public ReportsVo get(String id, String[] params){
		ReportsVo vo = get(id);
		//获取供应商
		if(ArrayUtils.contains(params, "vendor") && null != vo.getVendorId()){
			vo.setVendor(vendorService.get(vo.getVendorId()));
			vo.setVendorName(vo.getVendor().getName());
		}
		//报告附件
		if(ArrayUtils.contains(params, "file")){
			vo.setReportFile(attachmentService.getByBusinessIdAndModuleName(id, ConstantsAttachment.Status.Reports_File.code));
		}
		//其它附件
		if(ArrayUtils.contains(params, "attachment")){
			vo.setAttachments(attachmentService.listByBusinessIdAndModuleName(id, ConstantsAttachment.Status.Reports.code));
		}
		//获取photo附件
		if(ArrayUtils.contains(params, "photo")){
			vo.setImagesDoc(attachmentService.listByBusinessIdAndModuleName(id, ConstantsAttachment.Status.Reports_Photo.code));
			if(vo.getImagesDoc() != null){
				List<String> list = Lists.newArrayList();
				for(AttachmentVo atta: vo.getImagesDoc()){
					list.add(atta.getDocumentId());
				}
				vo.setImages(StringUtils.join(list.toArray(), ","));
			}
		}

		vo.setDetails(reportsProductService.findDetailsVoByReportsId(id));

		return vo;
	}

	public Reports getReports(String id){
		Reports o = reportsDao.findReportsById(id);
		return o;
	}

	/**
	 * 新建
	 * @param o
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public Reports add(Reports o, List<ReportsProduct> reportsProducts){
		UserVo user = SessionUtils.currentUserVo();
		o.setCreatorId(user.getId());
		o.setCreatorCnName(user.getCnName());
		o.setCreatorEnName(user.getEnName());
		o.setDepartmentId(user.getDepartmentId());
		o.setDepartmentCnName(user.getDepartmentCnName());
		o.setDepartmentEnName(user.getDepartmentEnName());
		
		o.setId(null);
		o.setStatus(0);
		o.setCreatedAt(new Date());

		//保存供应商
		setVendor(o);

    	o = reportsDao.save(o);

    	//保存明细
    	saveDetails(o.getId(), reportsProducts);

		//保存图片附件
		setPhotosFromImages(o);
		//保存报告文件
		setReportFile(o);

		return o;
	}

    /**
     * 新建
     * @param o
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Reports saveAs(Reports o, List<ReportsProduct> reportsProducts){
        reportsDao.clear();
        o.setId(null);
        o.setUpdatedAt(null);
        o.setConfirmedCnName(null);
        o.setConfirmedId(null);
        o.setConfirmedAt(null);
        
        return add(o, reportsProducts);
    }

	/**
	 * 保存及修改
	 * @param o
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public Reports save(Reports o, List<ReportsProduct> reportsProducts){
		o.setUpdatedAt(new Date());

		//保存供应商
		setVendor(o);

		o = reportsDao.save(o);

		saveDetails(o.getId(), reportsProducts);

		//保存图片附件
		setPhotosFromImages(o);

		//保存报告文件
		setReportFile(o);

		return o;
	}

	private void saveDetails(String reportsId, List<ReportsProduct> reportsProducts){
        reportsProductTroubleDetailDao.deleteDeltailsByReportsId(reportsId);
		reportsProductService.deleteDetailsByReportsId(reportsId);
		if(reportsProducts != null && reportsProducts.size() > 0){
			for (int i = 0; i < reportsProducts.size(); i++) {
				ReportsProduct o = reportsProducts.get(i);
				o.setReportsId(reportsId);
				reportsProductService.save(o);
				List<ReportsProductTroubleDetail> troubleDetails = o.getTroubleDetails();
				if(troubleDetails != null && troubleDetails.size() > 0){
					for (int j = 0; j < troubleDetails.size(); j++) {
						ReportsProductTroubleDetail detail = troubleDetails.get(j);
						detail.setReportsProductId(o.getId());
						detail.setReportsId(reportsId);
						detail.setProductId(o.getProductId());
                        reportsProductTroubleDetailDao.save(detail);
					}
				}
			}
		}
	}

	/**
	 * 修改状态为“启用”
	 * @param o
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public Reports updateStatus(Reports o){
		o.setStatus(1);
		return reportsDao.save(o);
	}

    /**
     * 根据business id 保存所有报告的状态为“启用”
     * @param businessId
     */
	@Transactional(rollbackFor = Exception.class)
    public void saveEnabledByBusinessId(String businessId){
	    List<Reports> rs = reportsDao.findReportsByBusinessIdAndStatus(businessId, Constants.Status.DRAFT.code);
	    if(rs.size()>0){
	        for(Reports r : rs){
	            r.setStatus(Constants.Status.NORMAL.code);
                reportsDao.save(r);
            }
        }
    }

    public boolean checkReportsByBusinessId(String businessId){
		List<Reports> rs = reportsDao.findReportsByBusinessIdAndStatus(businessId, Constants.Status.NORMAL.code);
		return  rs == null || rs.isEmpty() ? false : true;
	}

    /**
     * 根据business id 保存所有报告的状态为“禁用”
     * @param businessId
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveDisabledByBusinessId(String businessId){
    	List<Integer> statusList = Lists.newArrayList();
    	statusList.add(Constants.Status.DRAFT.code);
    	statusList.add(Constants.Status.NORMAL.code);
        List<Reports> rs = reportsDao.findReportsByBusinessIdAndStatusIn(businessId, statusList);
        if(rs.size()>0){
            for(Reports r : rs){
                r.setStatus(Constants.Status.DISABLED.code);
                reportsDao.save(r);
            }
        }
    }

    /**
     * 根据business id 保存所有报告的状态为“草稿”
     * @param businessId
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveDraftByBusinessId(String businessId){
        List<Reports> rs = reportsDao.findReportsByBusinessIdAndStatus(businessId, Constants.Status.NORMAL.code);
        if(rs.size()>0){
            for(Reports r : rs){
                r.setStatus(Constants.Status.DRAFT.code);
                reportsDao.save(r);
            }
        }
    }

    /**
     * 根据business id 保存所有报告的确认人信息
     * @param businessId
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveConfirmedByBusinessId(String businessId){
        List<Reports> rs = reportsDao.findReportsByBusinessIdAndStatus(businessId, Constants.Status.NORMAL.code);
        if(rs.size()>0){
            UserVo user = SessionUtils.currentUserVo();
            for(Reports r : rs){
                r.setConfirmedAt(new Date());
                r.setConfirmedId(user.getId());
                r.setConfirmedCnName(user.getCnName());
                r.setConfirmedEnName(user.getEnName());
                reportsDao.save(r);
            }
        }
    }

	/**
	 * 根据ID做物理删除
	 * @param id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(String id){
		//删除全部关联附件
		attachmentService.deleteByBusinessId(id);

		reportsDao.delete(id);
	}

	/**
	 * 根据ID修改状态为“删除”
	 * @param id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void setDelete(String id){
		Reports o = getReports(id);
		o.setStatus(3);
		reportsDao.save(o);
	}

	/**
	 * 保存报告文件
	 * @param o
	 */
	public void setReportFile(Reports o){
		if( o.getReportFile() != null && StringUtils.isNotBlank(o.getReportFile())){
			Attachment atta = new Attachment();
			atta.setDocumentId(o.getReportFile());
			atta = attachmentService.add(atta,  ConstantsAttachment.Status.Reports_File.code, o.getId());
			//回写文件
			o.setFile(atta.getId());
		}
	}

	/**
	 * 写photos附件
	 * @param o
	 */
	public void setPhotosFromImages (Reports o){
		if(o.getImages() != null && StringUtils.isNotBlank(o.getImages())){
			List<Attachment> attachments = Lists.newArrayList();
			String[] images = o.getImages().split(",");
			for(String image : images){
				Attachment atta = new Attachment();
				atta.setDocumentId(image);
				attachments.add(atta);
			}
			attachments = attachmentService.save(attachments,  ConstantsAttachment.Status.Reports_Photo.code, o.getId());

			//回写photos
			if(attachments.size()>0){
				List<String> attaIds = Lists.newArrayList();
				for(Attachment atta: attachments){
					attaIds.add(atta.getId());
				}
				o.setPhotos(StringUtils.join(attaIds.toArray(), ","));
			}

		}else{
			attachmentService.deleteByBusinessId(o.getId(),  ConstantsAttachment.Status.Reports_Photo.code);
			o.setPhotos(null);
		}
	}

	/**
	 * 保存供应商名
	 * @param o
	 */
	public void setVendor(Reports o){
		if(null != o.getVendorId() && StringUtils.isNotBlank(o.getVendorId())){
			Vendor vd = vendorService.getVendor(o.getVendorId());
			o.setVendorCnName(vd.getCnName());
			o.setVendorEnName(vd.getEnName());
		}
	}

	private Specification<Reports> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Reports> spec = DynamicSpecifications.bySearchFilter(filters.values(), Reports.class);
        return spec;
    }

}
