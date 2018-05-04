package com.newaim.purchase.flow.workflow.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.flow.workflow.dao.FlowOperatorHistoryDao;
import com.newaim.purchase.flow.workflow.entity.FlowOperatorHistory;
import org.apache.commons.lang3.StringUtils;
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
 * Created by Mark on 2017/10/10.
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowOperatorHistoryService extends ServiceBase {

    @Autowired
    private FlowOperatorHistoryDao flowOperatorHistoryDao;

    public Page<FlowOperatorHistory> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowOperatorHistory> spec = buildSpecification(params);
        Page<FlowOperatorHistory> page = flowOperatorHistoryDao.findAll(spec, pageRequest);
        return page;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveFlowOperatorHistory(FlowOperatorHistory entity){
        flowOperatorHistoryDao.save(entity);
    }

    public FlowOperatorHistory getFlowOperatorHistory(String id){
        return flowOperatorHistoryDao.findOne(id);
    }

    /**
     * 流程历史记录
     * @param businessId 业务Id
     * @param flowRemark 审批意见
     * @param data 需要记录的数据
     * @param approved
     */
    @Transactional(rollbackFor = Exception.class)
    public void newFlowOperatorHistory(String businessId, String flowRemark, String data, String approved, UserVo startUser){
        if(Constants.FlowAct.START.code.equals(approved)){
            //发起
            newFlowOperatorHistory(businessId, Constants.FlowHistoryStatus.START.code, flowRemark, data, startUser);
        }else if(Constants.FlowAct.ALLOW.code.equals(approved)){
            //同意
            newFlowOperatorHistory(businessId, Constants.FlowHistoryStatus.PASS.code, flowRemark, data, startUser);
        }else if(Constants.FlowAct.BACK.code.equals(approved)){
            //退回
            newFlowOperatorHistory(businessId, Constants.FlowHistoryStatus.BACK.code, flowRemark, data, startUser);
        }else if(Constants.FlowAct.REDO.code.equals(approved)){
            //反审
            newFlowOperatorHistory(businessId, Constants.FlowHistoryStatus.REDO.code, flowRemark, data, startUser);
        }else if(Constants.FlowAct.REFUSE.code.equals(approved)){
            //拒绝
            newFlowOperatorHistory(businessId, Constants.FlowHistoryStatus.REFUSE.code, flowRemark, data, startUser);
        }
    }

    private void newFlowOperatorHistory(String businessId, int type, String flowRemark, String data, UserVo startUser){
        UserVo user;
        if(startUser != null && StringUtils.isNotBlank(startUser.getId())){
            user = startUser;
        }else{
            user = SessionUtils.currentUserVo();
        }
        FlowOperatorHistory flowOperatorHistory = new FlowOperatorHistory();
        flowOperatorHistory.setBusinessId(businessId);
        flowOperatorHistory.setOperatorId(user.getId());
        flowOperatorHistory.setOperatorCnName(user.getCnName());
        flowOperatorHistory.setOperatorEnName(user.getEnName());
        flowOperatorHistory.setData(data);
        flowOperatorHistory.setType(type);
        flowOperatorHistory.setRemark(flowRemark);
        flowOperatorHistory.setCreatedAt(new Date());
        flowOperatorHistoryDao.save(flowOperatorHistory);
    }

    private Specification<FlowOperatorHistory> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowOperatorHistory> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowOperatorHistory.class);
        return spec;
    }
}
