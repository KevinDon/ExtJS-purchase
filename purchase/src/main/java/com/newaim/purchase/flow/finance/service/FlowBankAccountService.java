package com.newaim.purchase.flow.finance.service;

import com.google.common.collect.Sets;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.finance.dao.BankAccountDao;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.desktop.message.helper.Msg;
import com.newaim.purchase.flow.finance.dao.FlowBankAccountDao;
import com.newaim.purchase.flow.finance.entity.FlowBankAccount;
import com.newaim.purchase.flow.finance.vo.FlowBankAccountVo;
import com.newaim.purchase.flow.workflow.dao.FlowOperatorHistoryDao;
import com.newaim.purchase.flow.workflow.entity.FlowOperatorHistory;
import com.newaim.purchase.flow.workflow.service.FlowServiceBase;
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
import java.util.Map;
import java.util.Set;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowBankAccountService extends FlowServiceBase {

    @Autowired
    private FlowBankAccountDao flowBankAccountDao;

    @Autowired
    private BankAccountDao bankAccountDao;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private FlowOperatorHistoryDao flowOperatorHistoryDao;

    /**
     * 分页查询所有
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FlowBankAccountVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowBankAccount> spec = buildSpecification(params);
        Page<FlowBankAccount> p = flowBankAccountDao.findAll(spec, pageRequest);
        Page<FlowBankAccountVo> page = p.map(new Converter<FlowBankAccount, FlowBankAccountVo>() {
            @Override
            public FlowBankAccountVo convert(FlowBankAccount flowBankAccount) {
                return convertToBankAccountVo(flowBankAccount);
            }
        });
        return page;
    }

    private FlowBankAccountVo convertToBankAccountVo(FlowBankAccount flowBankAccount){
        FlowBankAccountVo vo = BeanMapper.map(flowBankAccount, FlowBankAccountVo.class);
        return vo;
    }

    public FlowBankAccount getFlowBankAccount(String id){
        return flowBankAccountDao.findOne(id);
    }

    /**
     * 获取信息
     * @param id
     * @return
     */
    public FlowBankAccountVo get(String id){
        FlowBankAccountVo vo = convertToBankAccountVo(getFlowBankAccount(id));
        //供应商信息
        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));
        }
        //保函附件
        vo.setGuaranteeLetterFile(attachmentService.getByBusinessIdAndModuleName(id, ConstantsAttachment.Status.BankAccount_File.code));
        //合同保函附件
        vo.setContractGuaranteeLetterFile(attachmentService.getByBusinessIdAndModuleName(id, ConstantsAttachment.Status.BankAccount_ContractFile.code));
        return vo;
    }

    /**
     * 新建，
     * @param flowBankAccount
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowBankAccount add(FlowBankAccount flowBankAccount){
        //设置创建信息
        setFlowCreatorInfo(flowBankAccount);
        //设置供应商信息
        setFlowVendorInfo(flowBankAccount);
        //保存
        flowBankAccount = flowBankAccountDao.save(flowBankAccount);
        //保存保函文件
        setGuaranteeLetterFile(flowBankAccount);
        //保存合同担保函
        setContractGuaranteeLetterFile(flowBankAccount);
        return flowBankAccount;
    }

    /**
     * 另存
     * @param flowBankAccount
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowBankAccount saveAs(FlowBankAccount flowBankAccount){
        flowBankAccountDao.clear();
        //设置创建信息
        setFlowCreatorInfo(flowBankAccount);
        //设置供应商信息
        setFlowVendorInfo(flowBankAccount);
        //复制时清理信息
        cleanInfoForSaveAs(flowBankAccount);
        //保存主单数据
        flowBankAccount = flowBankAccountDao.save(flowBankAccount);
        //保存保函文件
        setGuaranteeLetterFile(flowBankAccount);
        //保存合同担保函
        setContractGuaranteeLetterFile(flowBankAccount);
        return flowBankAccount;
    }

    /**
     * 更新，设置更新时间，供应商信息，保函文件
     * @param flowBankAccount
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowBankAccount update(FlowBankAccount flowBankAccount){
        //设置更新时间
        flowBankAccount.setUpdatedAt(new Date());
        //设置供应商信息
        setFlowVendorInfo(flowBankAccount);
        flowBankAccount = flowBankAccountDao.save(flowBankAccount);
        //保存保函文件
        setGuaranteeLetterFile(flowBankAccount);
        //保存合同担保函
        setContractGuaranteeLetterFile(flowBankAccount);
        return flowBankAccount;
    }

    /**
     * 设置保函文件
     * @param o
     */
    public void setGuaranteeLetterFile(FlowBankAccount o){
        if(o.getGuaranteeLetterFile() != null && StringUtils.isNotBlank(o.getGuaranteeLetterFile())) {
            Attachment atta = new Attachment();
            atta.setDocumentId(o.getGuaranteeLetterFile());
            atta = attachmentService.add(atta, ConstantsAttachment.Status.BankAccount_File.code, o.getId());
            o.setGuaranteeLetter(atta.getId());
        }
    }

    /**
     * 设置合同履约担保函
     * @param o
     */
    public void setContractGuaranteeLetterFile(FlowBankAccount o){
        if(o.getContractGuaranteeLetterFile() != null && StringUtils.isNotBlank(o.getContractGuaranteeLetterFile())) {
            Attachment atta = new Attachment();
            atta.setDocumentId(o.getContractGuaranteeLetterFile());
            atta = attachmentService.add(atta, ConstantsAttachment.Status.BankAccount_ContractFile.code, o.getId());
            o.setContractGuaranteeLetter(atta.getId());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowBankAccount flowBankAccount){
        flowBankAccount.setUpdatedAt(new Date());
        flowBankAccount.setStatus(Constants.Status.DELETED.code);
        flowBankAccountDao.save(flowBankAccount);
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowBankAccount flow = flowBankAccountDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowBankAccountDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowBankAccount flow = flowBankAccountDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowBankAccountDao.save(flow);
    }

    /**
     * 动态建立关系
     * @param searchParams
     * @return
     */
    private Specification<FlowBankAccount> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowBankAccount> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowBankAccount.class);
        return spec;
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancel(String businessId){
        FlowBankAccount flowBankAccount = flowBankAccountDao.findOne(businessId);
        flowBankAccount.setStatus(Constants.Status.CANCELED.code);
        flowBankAccountDao.save(flowBankAccount);
        List<FlowOperatorHistory> histories = flowOperatorHistoryDao.findByBusinessId(businessId);
        Set<String> sets = Sets.newHashSet();
        if(histories != null && histories.size() > 0){
            for (int i = 0; i < histories.size(); i++) {
                String userId = histories.get(i).getOperatorId();
                if(!sets.contains(userId)){
                    sets.add(userId);
                    Msg.send(userId, "收款帐号变更作废", "收款帐号变更申请已经作废, 请及时检查" + flowBankAccount.getVendorCnName() + "的账号信息是否有误！");
                }
            }
            sets.clear();
        }
    }
}
