package com.newaim.purchase.desktop.reports.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.desktop.reports.dao.ReportsProductTroubleDetailDao;
import com.newaim.purchase.desktop.reports.entity.ReportsProductTroubleDetail;
import com.newaim.purchase.desktop.reports.vo.ReportsProductTroubleDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly=true, rollbackFor = Exception.class)
public class ReportsProductTroubleDetailService extends ServiceBase {


    @Autowired
    private ReportsProductTroubleDetailDao reportsProductTroubleDetailDao;

	@Transactional(rollbackFor = Exception.class)
	public void save(ReportsProductTroubleDetail reportsProductTroubleDetail){
		reportsProductTroubleDetailDao.save(reportsProductTroubleDetail);
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteDetailsByReportsId(String reportsId){
		if(StringUtils.isNotBlank(reportsId)){
			reportsProductTroubleDetailDao.deleteDeltailsByReportsId(reportsId);
		}
	}

	/**
	 * 查找报告产品明细
	 * @param reportsProductId
	 * @return
	 */
	public List<ReportsProductTroubleDetail> findDetailsByReportsProductId(String reportsProductId){
		return reportsProductTroubleDetailDao.findAllByReportsProductId(reportsProductId);
	}

	/**
	 * 根据productId查找报告中的被选子项
	 * @param productId
	 * @return
	 */
	public List<ReportsProductTroubleDetailVo> findDetailsVoByProductId(String productId){
		List<ReportsProductTroubleDetailVo> data = BeanMapper.mapList(reportsProductTroubleDetailDao.findAllByProductId(productId), ReportsProductTroubleDetail.class, ReportsProductTroubleDetailVo.class);
		return data;
	}

	/**
	 * 根据productId查找报告中的被选子项
	 * @param productId
	 * @return
	 */
	public List<ReportsProductTroubleDetailVo> findDetailsVoByReportsIdAndProductId(String reportsId, String productId){
		List<ReportsProductTroubleDetail> rptd = reportsProductTroubleDetailDao.findAllByReportsIdAndProductId(reportsId, productId);
		if(null == rptd || rptd.size()==0) return null;
		List<ReportsProductTroubleDetailVo> data = BeanMapper.mapList(rptd, ReportsProductTroubleDetail.class, ReportsProductTroubleDetailVo.class);
		return data;
	}

	public ReportsProductTroubleDetail getReportsProductTroubleDetail(String id){
		return reportsProductTroubleDetailDao.getOne(id);
	}

	public ReportsProductTroubleDetailVo get(String id){
		return BeanMapper.map(reportsProductTroubleDetailDao.findOne(id), ReportsProductTroubleDetailVo.class);
	}

}
