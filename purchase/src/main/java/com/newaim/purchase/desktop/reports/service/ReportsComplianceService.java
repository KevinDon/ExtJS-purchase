package com.newaim.purchase.desktop.reports.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.desktop.reports.dao.ReportsComplianceDao;
import com.newaim.purchase.desktop.reports.entity.ReportsCompliance;
import com.newaim.purchase.desktop.reports.vo.ReportsComplianceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ReportsComplianceService extends ServiceBase {

    @Autowired
    private ReportsComplianceDao reportsComplianceDao;

    /**
     * 获取详情
     * @param id
     * @return
     */
    public ReportsComplianceVo get(String id) {

        ReportsComplianceVo vo = BeanMapper.map(getReportsCompliance(id), ReportsComplianceVo.class);

        return vo;
    }

    public ReportsCompliance getReportsCompliance(String id) {
        ReportsCompliance o = reportsComplianceDao.findReportsComplianceById(id);

        return o;
    }

    /**
     * 根据reportsId获取记录
     * @param reportsId
     * @return
     */
    public ReportsComplianceVo getByReportsId(String reportsId){
        ReportsComplianceVo o = new ReportsComplianceVo();
        try {
            ReportsCompliance rc = reportsComplianceDao.findReportsComplianceByReportsId(reportsId);
            o = BeanMapper.map(rc, ReportsComplianceVo.class);

        }catch (Exception e){

        }finally {
            return o;
        }
    }
    /**
     * 新建
     *
     * @param o
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ReportsCompliance add(String reportsId, ReportsCompliance o) {

        deleteAllByReportsId(reportsId);

        o.setId(null);
        o.setReportsId(reportsId);
        o = reportsComplianceDao.save(o);

        return o;
    }

    /**
     * 保存及修改
     *
     * @param o
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ReportsCompliance save(String reportsId, ReportsCompliance o) {

        deleteAllByReportsId(reportsId);

        o.setReportsId(reportsId);
        o = reportsComplianceDao.save(o);

        return o;
    }

    /**
     * 根据ID做物理删除
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        reportsComplianceDao.delete(id);
    }

    /**
     * 根据reportsId删除相关明细
     * @param reportsId
     */
    public void deleteAllByReportsId(String reportsId){
        reportsComplianceDao.deleteAllByReportsId(reportsId);
    }

}
