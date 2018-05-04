package com.newaim.purchase.flow.purchase.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.utils.DateFormatUtil;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.dao.DictionaryValueDao;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.entity.BuyerInfo;
import com.newaim.purchase.admin.system.entity.DictionaryValue;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.admin.system.service.BuyerInfoService;
import com.newaim.purchase.admin.system.service.DictionaryService;
import com.newaim.purchase.admin.system.vo.DictionaryValueVo;
import com.newaim.purchase.archives.finance.entity.BankAccount;
import com.newaim.purchase.archives.finance.service.BankAccountService;
import com.newaim.purchase.archives.flow.finance.dao.PurchaseContractDepositDao;
import com.newaim.purchase.archives.flow.finance.entity.PurchaseContractDeposit;
import com.newaim.purchase.archives.flow.inspection.dao.OrderQcDao;
import com.newaim.purchase.archives.flow.inspection.entity.OrderQc;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseBalanceRefundUnionDao;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.dao.PurchasePlanDetailDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseBalanceRefundUnion;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.purchase.entity.PurchasePlanDetail;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseBalanceRefundUnionService;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingPlanDao;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlan;
import com.newaim.purchase.archives.product.entity.ProductVendorProp;
import com.newaim.purchase.archives.product.service.ProductVendorPropService;
import com.newaim.purchase.archives.vendor.entity.Vendor;
import com.newaim.purchase.archives.vendor.entity.VendorProductCategoryUnion;
import com.newaim.purchase.archives.vendor.service.VendorProductCategoryUnionService;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.archives.vendor.vo.VendorProductCategoryUnionVo;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.desktop.email.service.ContactsService;
import com.newaim.purchase.desktop.email.vo.ContactsVo;
import com.newaim.purchase.flow.finance.dao.FlowPurchaseContractDepositDao;
import com.newaim.purchase.flow.finance.entity.FlowPurchaseContractDeposit;
import com.newaim.purchase.flow.inspection.dao.FlowOrderQcDao;
import com.newaim.purchase.flow.inspection.entity.FlowOrderQc;
import com.newaim.purchase.flow.purchase.dao.FlowPurchaseContractDao;
import com.newaim.purchase.flow.purchase.dao.FlowPurchaseContractDetailDao;
import com.newaim.purchase.flow.purchase.dao.FlowPurchaseContractOtherDetailDao;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContract;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContractDetail;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContractOtherDetail;
import com.newaim.purchase.flow.purchase.vo.FlowPurchaseContractDetailVo;
import com.newaim.purchase.flow.purchase.vo.FlowPurchaseContractVo;
import com.newaim.purchase.flow.shipping.dao.FlowOrderShippingApplyDao;
import com.newaim.purchase.flow.shipping.dao.FlowOrderShippingPlanDao;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingApply;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingPlan;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.LinkedHashMap;

/**
 * 采购合同业务操作类
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowPurchaseContractService extends FlowServiceBase {

    public static final String FILE_MODULE_NAME = ConstantsAttachment.Status.FlowPurchaseContract.code;

    @Autowired
    private FlowPurchaseContractDao flowPurchaseContractDao;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private FlowPurchaseContractDetailDao flowPurchaseContractDetailDao;

    @Autowired
    private FlowPurchaseContractOtherDetailDao flowPurchaseContractOtherDetailDao;

    @Autowired
    private FlowPurchaseContractDetailService flowPurchaseContractDetailService;

    @Autowired
    private FlowPurchaseContractOtherDetailService flowPurchaseContractOtherDetailService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private BuyerInfoService buyerInfoService;
    
    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private VendorProductCategoryUnionService vendorProductCategoryUnionService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private DictionaryValueDao dictionaryValueDao;

    @Autowired
    private ContactsService contactsService;

    @Autowired
    private VendorProductCategoryUnionService unionService;

    @Autowired
    private ProductVendorPropService productVendorPropService;

    @Autowired
    private FlowOrderQcDao  flowOrderQcDao;

    @Autowired
    private OrderQcDao orderQcDao;

    @Autowired
    private FlowPurchaseContractDepositDao flowPurchaseContractDepositDao;

    @Autowired
    private FlowOrderShippingPlanDao flowOrderShippingPlanDao;

    @Autowired
    private OrderShippingPlanDao orderShippingPlanDao;

    @Autowired
    private FlowOrderShippingApplyDao flowOrderShippingApplyDao;

    @Autowired
    private PurchaseBalanceRefundUnionDao purchaseBalanceRefundUnionDao;

    @Autowired
    private PurchaseBalanceRefundUnionService purchaseBalanceRefundUnionService;

    @Autowired
    private PurchasePlanDetailDao purchasePlanDetailDao;

    /**
     * 根据产品id获取对应采购合同
     * @param productId 产品id
     */
    public List<FlowPurchaseContractVo> listByProductId(String productId){
        List<FlowPurchaseContract> p = flowPurchaseContractDao.findByProductId(productId);
        return BeanMapper.mapList(p, FlowPurchaseContract.class, FlowPurchaseContractVo.class);
    }

    /**
     * 分页查询所有信息
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FlowPurchaseContractVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowPurchaseContract> spec = buildSpecification(params);
        Page<FlowPurchaseContract> p = flowPurchaseContractDao.findAll(spec, pageRequest);
        Page<FlowPurchaseContractVo> page = p.map(new Converter<FlowPurchaseContract, FlowPurchaseContractVo>() {
		    @Override
		    public FlowPurchaseContractVo convert(FlowPurchaseContract flowPurchaseContract) {
		        return convertToFlowPurchaseContractVo(flowPurchaseContract);
		    }
		});
        return page;
    }

    /**
     * 将entity转换为Vo
     * @param flowPurchaseContract
     * @return
     */
    private FlowPurchaseContractVo convertToFlowPurchaseContractVo(FlowPurchaseContract flowPurchaseContract){
        FlowPurchaseContractVo vo = BeanMapper.map(flowPurchaseContract, FlowPurchaseContractVo.class);
        return vo;
    }

    public FlowPurchaseContract getFlowPurchaseContract(String id){
        return flowPurchaseContractDao.findOne(id);
    }

    /**
     * 根据ID获得附件、供应商、details信息
     * @param id
     * @return
     */
    public FlowPurchaseContractVo get(String id){
    	FlowPurchaseContractVo vo =convertToFlowPurchaseContractVo(getFlowPurchaseContract(id));
        vo.setAttachments(attachmentService.listByBusinessId(id) );
        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));
        }
        if (StringUtils.isNotBlank(vo.getBuyerInfoId())){
            vo.setBuyerInfo(buyerInfoService.get(vo.getBuyerInfoId()));
        }
        if (StringUtils.isNotBlank(vo.getSellerContactId())){
            vo.setContacts(contactsService.get(vo.getSellerContactId()));
        }
        if (StringUtils.isNotBlank(vo.getVendorProductCategoryId())){
            vo.setCategoryUnion(unionService.get(vo.getVendorProductCategoryId()));
        }
        vo.setDetails(flowPurchaseContractDetailService.findDetailsVoByBusinessId(id));
        vo.setOtherDetails(flowPurchaseContractOtherDetailService.findOtherDetailsVoByBusinessId(id));
        setDetailsAvailableQty(vo);
        vo.setPurchaseBalanceRefundUnions(purchaseBalanceRefundUnionService.findVoByPurchaseContractBusinessId(id));
        return vo;
    }

    /**
     * 根据ID获得合同信息,合并相同sku价格和数量信息
     * @param id
     * @return
     */
    public FlowPurchaseContractVo getForMerge(String id){
        FlowPurchaseContractVo vo =convertToFlowPurchaseContractVo(getFlowPurchaseContract(id));

        if (StringUtils.isNotBlank(vo.getBuyerInfoId())){
            vo.setBuyerInfo(buyerInfoService.get(vo.getBuyerInfoId()));
        }
        if (StringUtils.isNotBlank(vo.getSellerContactId())){
            vo.setContacts(contactsService.get(vo.getSellerContactId()));
        }
        if (StringUtils.isNotBlank(vo.getVendorProductCategoryId())){
            vo.setCategoryUnion(unionService.get(vo.getVendorProductCategoryId()));
        }

        vo.setVendor(vendorService.get(vo.getVendorId()));

        vo.setDetails(flowPurchaseContractDetailService.findMergeDetailsVoByOrderId(id));
        setDetailsAvailableQty(vo);
        vo.setPurchaseBalanceRefundUnions(purchaseBalanceRefundUnionService.findVoByPurchaseContractBusinessId(id));
        return vo;
    }

    private void setDetailsAvailableQty(FlowPurchaseContractVo flowPurchaseContractVo){
        List<FlowPurchaseContractDetailVo> details = flowPurchaseContractVo.getDetails();
        if(details != null && details.size() > 0){
            for (int i = 0; i < details.size(); i++) {
                FlowPurchaseContractDetailVo detail = details.get(i);
                if(StringUtils.isNotBlank(detail.getPurchasePlanDetailId())){
                    PurchasePlanDetail purchasePlanDetail = purchasePlanDetailDao.findOne(detail.getPurchasePlanDetailId());
                    Integer alreadyOrderQty = purchasePlanDetail.getAlreadyOrderQty();
                    if(alreadyOrderQty == null){
                        alreadyOrderQty = 0;
                    }
                    detail.setSrcOrderQty(detail.getOrderQty());
                    int planAvailableQty = purchasePlanDetail.getOrderQty() - alreadyOrderQty;
                    if(planAvailableQty <= 0){
                        planAvailableQty = 0;
                    }
                    detail.setPlanAvailableQty(planAvailableQty);
                    detail.setAvailableQty(purchasePlanDetail.getOrderQty() - alreadyOrderQty + detail.getOrderQty());
                }
            }
        }
    }

    /**
     *添加、需要设置创建人、供应商、价格、附件信息
     */
    @Transactional(rollbackFor = Exception.class)
	public FlowPurchaseContract add(FlowPurchaseContract flowPurchaseContract,
                                    List<FlowPurchaseContractDetail> details,
                                    List<FlowPurchaseContractOtherDetail> otherDetails,
                                    List<PurchaseBalanceRefundUnion> purchaseBalanceRefundUnions, List<Attachment> attachments){
        setFlowCreatorInfo(flowPurchaseContract);
        setVendorInfo(flowPurchaseContract);
        //设置定金率
        setDepositRateInfo(flowPurchaseContract);
        //设置供应商产品类别名称
        setVendorProductCategoryInfo(flowPurchaseContract);
        //设置卖方联系人信息
        setSellerContactInfo(flowPurchaseContract);
        setFlowPurchaseContractBuyerInfo(flowPurchaseContract);
        //生成订单编号
        flowPurchaseContract.setOrderNumber(generateOrderNumber());
        //生成订单标题
        flowPurchaseContract.setOrderTitle(generateOrderTitle(flowPurchaseContract));
        //设置orderIndex
        setOrderIndex(flowPurchaseContract);

        //默认设置reta和retd
        flowPurchaseContract.setReta(flowPurchaseContract.getEta());
        flowPurchaseContract.setRetd(flowPurchaseContract.getEtd());

        flowPurchaseContract = flowPurchaseContractDao.save(flowPurchaseContract);

        setAlreadyOrderQty(details);

        saveDetails(flowPurchaseContract.getId(), details, otherDetails);
        //新增差额关联数据
        savePurchaseBalanceRefundUnionsInfo(flowPurchaseContract, purchaseBalanceRefundUnions);

        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowPurchaseContract.getId());

    	return flowPurchaseContract;
	}

	private void setAlreadyOrderQty(List<FlowPurchaseContractDetail> details){
	    if(details != null && details.size() > 0){
            for (int i = 0; i < details.size(); i++) {
                FlowPurchaseContractDetail detail = details.get(i);
                PurchasePlanDetail purchasePlanDetail = purchasePlanDetailDao.findOne(detail.getPurchasePlanDetailId());
                Integer alreadyOrderQty = purchasePlanDetail.getAlreadyOrderQty();
                if(alreadyOrderQty == null){
                    alreadyOrderQty = 0;
                }
                Integer orderQty = detail.getOrderQty();
                if(orderQty == null){
                    orderQty = 0;
                }
                purchasePlanDetail.setAlreadyOrderQty(alreadyOrderQty + orderQty);
                purchasePlanDetailDao.save(purchasePlanDetail);
            }
        }
    }

    private void setAlreadyOrderQtyForUpdate(List<FlowPurchaseContractDetail> details){
        if(details != null && details.size() > 0){
            for (int i = 0; i < details.size(); i++) {
                FlowPurchaseContractDetail detail = details.get(i);
                PurchasePlanDetail purchasePlanDetail = purchasePlanDetailDao.findOne(detail.getPurchasePlanDetailId());
                Integer alreadyOrderQty = purchasePlanDetail.getAlreadyOrderQty();
                if(alreadyOrderQty == null){
                    alreadyOrderQty = 0;
                }
                Integer orderQty = detail.getOrderQty();
                if(orderQty == null){
                    orderQty = 0;
                }
                Integer srcOrderQty = detail.getSrcOrderQty();
                if(srcOrderQty == null){
                    srcOrderQty = 0;
                }
                purchasePlanDetail.setAlreadyOrderQty(alreadyOrderQty + orderQty - srcOrderQty);
                purchasePlanDetailDao.save(purchasePlanDetail);
            }
        }
    }

    private void resetAlreadyOrderQty(String businessId){
        List<FlowPurchaseContractDetail> details = flowPurchaseContractDetailDao.findDetailsByBusinessId(businessId);
        if(details != null && details.size() > 0){
            for (int i = 0; i < details.size(); i++) {
                FlowPurchaseContractDetail detail = details.get(i);
                PurchasePlanDetail purchasePlanDetail = purchasePlanDetailDao.findOne(detail.getPurchasePlanDetailId());
                Integer alreadyOrderQty = purchasePlanDetail.getAlreadyOrderQty();
                if(alreadyOrderQty == null || alreadyOrderQty <= 0){
                    purchasePlanDetail.setAlreadyOrderQty(0);
                }else{
                    Integer orderQty = detail.getOrderQty();
                    if(orderQty == null || orderQty <= 0){
                        orderQty = 0;
                    }
                    int qty = alreadyOrderQty - orderQty;
                    purchasePlanDetail.setAlreadyOrderQty(qty > 0 ? qty : 0);
                }
                purchasePlanDetailDao.save(purchasePlanDetail);
            }
        }
    }

    private void savePurchaseBalanceRefundUnionsInfo(FlowPurchaseContract flowPurchaseContract, List<PurchaseBalanceRefundUnion> purchaseBalanceRefundUnions){

        if(purchaseBalanceRefundUnions != null && purchaseBalanceRefundUnions.size() > 0){
            for (int i = 0; i < purchaseBalanceRefundUnions.size(); i++) {
                purchaseBalanceRefundUnions.get(i).setPurchaseContractBusinessId(flowPurchaseContract.getId());
            }
            purchaseBalanceRefundUnionDao.save(purchaseBalanceRefundUnions);
        }
    }

    /**
     * 设置orderIndex
     * @param flowPurchaseContract
     */
    private synchronized void setOrderIndex(FlowPurchaseContract flowPurchaseContract){
        VendorProductCategoryUnion categoryUnion = vendorProductCategoryUnionService.getVendorProductCategoryUnion(flowPurchaseContract.getVendorProductCategoryId());
        Integer orderIndex = categoryUnion.getOrderIndex();
        categoryUnion.setOrderIndex(orderIndex != null ? orderIndex + 1 : 1);
        flowPurchaseContract.setOrderIndex(categoryUnion.getOrderIndex() + "");
        vendorProductCategoryUnionService.save(categoryUnion);
    }

    /**
     * 生成订单标题
     * @param flowPurchaseContract
     * @return
     */
    public synchronized String generateOrderTitle(FlowPurchaseContract flowPurchaseContract){
        //别名
        StringBuilder orderTitle = new StringBuilder();
        VendorProductCategoryUnion categoryUnion = vendorProductCategoryUnionService.getVendorProductCategoryUnion(flowPurchaseContract.getVendorProductCategoryId());
        if(categoryUnion != null){
            String alias = categoryUnion.getAlias();
            if(StringUtils.isBlank(alias)){
                //别名为空时，取供应商产品分类名
                orderTitle.append(categoryUnion.getProductCategory().getEnName());
            }else{
                orderTitle.append(alias);
            }
            orderTitle.append(" - Order ");
            Integer orderIndex = categoryUnion.getOrderIndex();
            orderTitle.append(orderIndex != null ? orderIndex + 1 : 1);
        }
        return orderTitle.toString();
    }

    /**
     * 生成订单编号，并将定订单编号记录在数据字典下
     * @return
     */
    public synchronized String generateOrderNumber(){
        DictionaryValueVo dictionaryValueVo = dictionaryService.getByCodemainAndCodeSub("generate_code", "job_no").get(0).getOptions().get(0);
        String oldValue = dictionaryValueVo.getValue();
        String orderNumberPattern = dictionaryService.getByCodemainAndCodeSub("rule", "order_number").get(0).getOptions().get(0).getValue();
        String oldYear = StringUtils.substring(oldValue, StringUtils.indexOf(oldValue, "-") - 2, StringUtils.indexOf(oldValue, "-"));
        String yearPattern = StringUtils.substring(orderNumberPattern, StringUtils.indexOf(orderNumberPattern, "{") + 1, StringUtils.indexOf(orderNumberPattern, "}"));
        String newYear = DateFormatUtil.format(new Date(), yearPattern);
        String orderNumber;
        Integer index = 0;
        if(StringUtils.equals(oldYear, newYear)){
            // +1
            String indexStr = StringUtils.substring(oldValue, StringUtils.indexOf(oldValue, "-") + 1);
            index = Integer.valueOf(indexStr);
        }else{
            index = 0;
        }
        //自增1
        index += 1;
        String indexStr = StringUtils.leftPad(index.toString(), 4, "0");
        orderNumber = StringUtils.replace(orderNumberPattern, "{" + yearPattern + "}", newYear).replace("{index}", indexStr);
        DictionaryValue dictionaryValue = dictionaryValueDao.findOne(dictionaryValueVo.getId());
        dictionaryValue.setValue(orderNumber);
        dictionaryValueDao.save(dictionaryValue);
        return orderNumber;
    }

    /**
     *更新
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowPurchaseContract update(FlowPurchaseContract flowPurchaseContract,
                                       List<FlowPurchaseContractDetail> details,
                                       List<FlowPurchaseContractOtherDetail> otherDetails,
                                       List<PurchaseBalanceRefundUnion> purchaseBalanceRefundUnions, List<Attachment> attachments){
        //设置供应商信息
        setVendorInfo(flowPurchaseContract);
        setDepositRateInfo(flowPurchaseContract);
        //设置供应商产品类别名称
        setVendorProductCategoryInfo(flowPurchaseContract);
        //设置卖方联系人信息
        setSellerContactInfo(flowPurchaseContract);
        //设置买方信息
        setFlowPurchaseContractBuyerInfo(flowPurchaseContract);
        //设置更新时间
        flowPurchaseContract.setUpdatedAt(new Date());
        //重置采购计划已用数量
        resetAlreadyOrderQty(flowPurchaseContract.getId());
        //删除之前明细，重新绑定数据
        flowPurchaseContractDetailDao.deleteByBusinessId(flowPurchaseContract.getId());
        flowPurchaseContractOtherDetailDao.deleteByBusinessId(flowPurchaseContract.getId());

        setAlreadyOrderQty(details);

        saveDetails(flowPurchaseContract.getId(), details, otherDetails);

        //新增差额关联数据
        List<PurchaseBalanceRefundUnion> oldUnions = purchaseBalanceRefundUnionDao.findByPurchaseContractBusinessId(flowPurchaseContract.getId());
        //取消旧关联数据
        if(oldUnions != null && oldUnions.size() > 0){
            for (int i = 0; i < oldUnions.size(); i++) {
                oldUnions.get(i).setPurchaseContractBusinessId(null);
            }
            purchaseBalanceRefundUnionDao.save(oldUnions);
        }
        savePurchaseBalanceRefundUnionsInfo(flowPurchaseContract, purchaseBalanceRefundUnions);
        //更新附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowPurchaseContract.getId());

        return flowPurchaseContractDao.save(flowPurchaseContract);
    }

    /**
     * 复制另存
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowPurchaseContract saveAs(FlowPurchaseContract flowPurchaseContract,
                                       List<FlowPurchaseContractDetail> details,
                                       List<FlowPurchaseContractOtherDetail> otherDetails,
                                       List<PurchaseBalanceRefundUnion> purchaseBalanceRefundUnions, List<Attachment> attachments){
        if(details != null){
            for (int i = 0; i < details.size(); i++) {
                details.get(i).setId(null);
                details.get(i).setBusinessId(null);
            }
        }
        if(otherDetails != null){
            for (int i = 0; i < otherDetails.size(); i++) {
                otherDetails.get(i).setId(null);
                otherDetails.get(i).setBusinessId(null);
            }
        }
        if(attachments != null){
            for (int i = 0; i < attachments.size(); i++) {
                attachments.get(i).setId(null);
                attachments.get(i).setBusinessId(null);
            }
        }
        flowPurchaseContract.setIsNeededQc(null);
        flowPurchaseContractDao.clear();
        setFlowCreatorInfo(flowPurchaseContract);
        setVendorInfo(flowPurchaseContract);
        setDepositRateInfo(flowPurchaseContract);
        //设置供应商产品类别名称
        setVendorProductCategoryInfo(flowPurchaseContract);
        //设置卖方联系人信息
        setSellerContactInfo(flowPurchaseContract);
        setFlowPurchaseContractBuyerInfo(flowPurchaseContract);
        //清理信息
        cleanInfoForSaveAs(flowPurchaseContract);
        //生成订单编号
        String srcOrderNumber = flowPurchaseContract.getOrderNumber();
        if(StringUtils.isBlank(srcOrderNumber)){
            //新生成
            flowPurchaseContract.setOrderNumber(generateOrderNumber());
        }else {
            int flagIndex = StringUtils.indexOf(srcOrderNumber,"-", StringUtils.indexOf(srcOrderNumber, "-") + 1);
            String orderNumber = srcOrderNumber;
            if(flagIndex > 0){
                orderNumber = StringUtils.substring(srcOrderNumber, 0, flagIndex);
            }
            String maxOrderNumber = flowPurchaseContractDao.findMaxOrderNumber(orderNumber);

            if(StringUtils.countMatches(maxOrderNumber, "-") == 2){
                String orderNumberPrefix = StringUtils.substringBeforeLast(maxOrderNumber, "-");
                String newOrderNumberIndex = StringUtils.substringAfterLast(maxOrderNumber, "-");
                Integer index = 1;
                try {
                    index = Integer.valueOf(newOrderNumberIndex);
                }catch (Exception e){
                }
                String newOrderNumber = orderNumberPrefix + "-" + (index +1);
                flowPurchaseContract.setOrderNumber(newOrderNumber);
            }else{
                flowPurchaseContract.setOrderNumber(maxOrderNumber + "-1");
            }
        }

        //默认设置reta和retd
        flowPurchaseContract.setReta(flowPurchaseContract.getEta());
        flowPurchaseContract.setRetd(flowPurchaseContract.getEtd());

        flowPurchaseContract = flowPurchaseContractDao.save(flowPurchaseContract);
        setAlreadyOrderQty(details);
        saveDetails(flowPurchaseContract.getId(), details, otherDetails);

        //新增差额关联数据
        savePurchaseBalanceRefundUnionsInfo(flowPurchaseContract, purchaseBalanceRefundUnions);

        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowPurchaseContract.getId());
    	return flowPurchaseContract;
    }

    /**
     * 设置定金率信息
     */
    private void setDepositRateInfo(FlowPurchaseContract flowPurchaseContract){
        BankAccount bankAccount = bankAccountService.getBankAccountByVendorId(flowPurchaseContract.getVendorId());
        if(bankAccount != null){
            flowPurchaseContract.setDepositRate(bankAccount.getDepositRate());
            flowPurchaseContract.setDepositType(bankAccount.getDepositType());
        }
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(String businessId, List<FlowPurchaseContractDetail> details, List<FlowPurchaseContractOtherDetail> otherDetails){
        if(details != null && details.size() > 0){
            for (int i = 0; i < details.size(); i++) {
                FlowPurchaseContractDetail detail = details.get(i);
                detail.setBusinessId(businessId);
                //设置工厂编号
                ProductVendorProp prop = productVendorPropService.getProductVendorPropByProductId(detail.getProductId());
                detail.setFactoryCode(prop.getFactoryCode());
            }
            flowPurchaseContractDetailDao.save(details);
        }

        if(otherDetails != null && otherDetails.size() > 0){
            Date now = new Date();
            for (int i = 0; i < otherDetails.size(); i++) {
                FlowPurchaseContractOtherDetail otherDetail = otherDetails.get(i);
                otherDetail.setBusinessId(businessId);
                otherDetail.setCreatedAt(now);
            }
            flowPurchaseContractOtherDetailDao.save(otherDetails);
        }
    }

    /**
     * 设置买方信息
     * @param flowPurchaseContract
     */
    private void setFlowPurchaseContractBuyerInfo(FlowPurchaseContract flowPurchaseContract){
        if (StringUtils.isNotBlank(flowPurchaseContract.getBuyerInfoId())){
            BuyerInfo buyerInfo = buyerInfoService.getBuyerInfo(flowPurchaseContract.getBuyerInfoId());
            if (buyerInfo!=null){
                flowPurchaseContract.setBuyerInfoCnName(buyerInfo.getCnName());
                flowPurchaseContract.setBuyerInfoEnName(buyerInfo.getEnName());
            }
        }
    }

    /**
     * 设置供应商信息
     * @param flowPurchaseContract
     */
    private void setVendorInfo(FlowPurchaseContract flowPurchaseContract){
        if (StringUtils.isNotBlank(flowPurchaseContract.getVendorId())){
            Vendor vendor = vendorService.getVendor(flowPurchaseContract.getVendorId());
            if (vendor!=null){
                flowPurchaseContract.setVendorCnName(vendor.getCnName());
                flowPurchaseContract.setVendorEnName(vendor.getEnName());
                flowPurchaseContract.setSellerCnName(vendor.getCnName());
                flowPurchaseContract.setSellerEnName(vendor.getEnName());
                flowPurchaseContract.setSellerCnAddress(vendor.getAddress());
                flowPurchaseContract.setSellerEnAddress(vendor.getAddress());
                flowPurchaseContract.setCurrency(vendor.getCurrency());
            }
        }
    }

    /**
     * 设置卖方联系人信息
     * @param flowPurchaseContract
     */
    private void setSellerContactInfo(FlowPurchaseContract flowPurchaseContract){
        if (StringUtils.isNotBlank(flowPurchaseContract.getSellerContactId())){
            ContactsVo contactsVo = contactsService.get(flowPurchaseContract.getSellerContactId());
            if (contactsVo!=null){
                flowPurchaseContract.setSellerContactCnName(contactsVo.getCnName());
                flowPurchaseContract.setSellerContactEnName(contactsVo.getEnName());
            }
        }
    }

    /**
     * 设置供应商分类别名
     * @param flowPurchaseContract
     */
    private void setVendorProductCategoryInfo(FlowPurchaseContract flowPurchaseContract){
        if (StringUtils.isNotBlank(flowPurchaseContract.getVendorProductCategoryId())){
            VendorProductCategoryUnionVo categoryUnion =vendorProductCategoryUnionService.get(flowPurchaseContract.getVendorProductCategoryId());
            if (categoryUnion!=null) {
                flowPurchaseContract.setVendorProductCategoryAlias(categoryUnion.getAlias());
            }
        }
    }


    /**
     * 设置总价信息
     */
    private void setFlowPurchaseContractPriceInfo(FlowPurchaseContract flowPurchaseContract, List<FlowPurchaseContractDetail> details){
        BigDecimal totalPriceAud = BigDecimal.ZERO;
        BigDecimal totalPriceRmb = BigDecimal.ZERO;
        BigDecimal totalPriceUsd = BigDecimal.ZERO;
        Integer totalOrderQty = 0;
        if(details != null) {
            for(FlowPurchaseContractDetail detail : details){
                if(detail.getPriceAud() != null && detail.getOrderQty() != null){
                    totalPriceAud = totalPriceAud.add(detail.getPriceAud().multiply(BigDecimal.valueOf(detail.getOrderQty())));
                }
                if(detail.getPriceRmb() != null && detail.getOrderQty() != null){
                    totalPriceRmb = totalPriceRmb.add(detail.getPriceRmb().multiply(BigDecimal.valueOf(detail.getOrderQty())));
                }
                if(detail.getPriceUsd() != null&& detail.getOrderQty() != null ){
                    totalPriceUsd = totalPriceUsd.add(detail.getPriceUsd().multiply(BigDecimal.valueOf(detail.getOrderQty())));
                }
                if(detail.getOrderQty() != null){
                    totalOrderQty += detail.getOrderQty();
                }
                if (detail.getCurrency() == null){
                    detail.setCurrency(flowPurchaseContract.getCurrency());
                }
                //设置工厂编号
                ProductVendorProp prop = productVendorPropService.getProductVendorPropByProductId(detail.getProductId());
                detail.setFactoryCode(prop.getFactoryCode());
            }
        }
        flowPurchaseContract.setTotalPriceAud(totalPriceAud);
        flowPurchaseContract.setTotalPriceRmb(totalPriceRmb);
        flowPurchaseContract.setTotalPriceUsd(totalPriceUsd);
        flowPurchaseContract.setTotalOrderQty(totalOrderQty);
    }
	
    @Transactional(rollbackFor = Exception.class)
	public void delete(FlowPurchaseContract flowPurchaseContract){
        flowPurchaseContract.setUpdatedAt(new Date());
        flowPurchaseContract.setStatus(Constants.Status.DELETED.code);
        flowPurchaseContractDao.save(flowPurchaseContract);
        restorePurchasePlan(flowPurchaseContract.getId());
	}

    /**
     * 还原采购合同对应的采购计划
     * @param orderBusinessId
     */
    @Transactional(rollbackFor = Exception.class)
	public void restorePurchasePlan(String orderBusinessId){
        //对应采购计划数量回写
        List<FlowPurchaseContractDetail> details = flowPurchaseContractDetailDao.findDetailsByBusinessId(orderBusinessId);
        if(details != null){
            for (int i = 0; i < details.size(); i++) {
                FlowPurchaseContractDetail detail = details.get(i);
                if(StringUtils.isNotBlank(detail.getPurchasePlanDetailId())){
                    PurchasePlanDetail purchasePlanDetail = purchasePlanDetailDao.findOne(detail.getPurchasePlanDetailId());
                    if(purchasePlanDetail != null){
                        purchasePlanDetail.setAlreadyOrderQty(purchasePlanDetail.getAlreadyOrderQty() - detail.getOrderQty());
                        purchasePlanDetailDao.save(purchasePlanDetail);
                    }
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancel(FlowPurchaseContract flowOrder, PurchaseContract order){

        Date now = new Date();
        if(flowOrder.getStatus() == 0 || flowOrder.getStatus() == 1){
            // 1. 作废合同单
            flowOrder.setStatus(Constants.Status.CANCELED.code);
            flowOrder.setUpdatedAt(now);
            flowPurchaseContractDao.save(flowOrder);
            if(order != null) {
                //1.1.2 作废实际订单
                order.setStatus(Constants.Status.CANCELED.code);
                order.setUpdatedAt(now);
                purchaseContractDao.save(order);

                //1.2.1 作废订单质检
                List<OrderQc> orderQcs = orderQcDao.findByOrderId(order.getId());
                if(orderQcs != null && orderQcs.size() > 0){
                    for (int i = 0; i < orderQcs.size(); i++) {
                        OrderQc orderQc = orderQcs.get(i);
                        orderQc.setStatus(Constants.Status.CANCELED.code);
                        orderQc.setUpdatedAt(now);
                    }
                    orderQcDao.save(orderQcs);
                }
                //1.2.1 作废订单质检流
                List<FlowOrderQc> flowOrderQcs = flowOrderQcDao.findByOrderId(order.getId());
                if(flowOrderQcs != null && flowOrderQcs.size() > 0){
                    for (int i = 0; i < flowOrderQcs.size(); i++) {
                        FlowOrderQc flowOrderQc = flowOrderQcs.get(i);
                        flowOrderQc.setStatus(Constants.Status.CANCELED.code);
                        flowOrderQc.setUpdatedAt(now);
                    }
                    flowOrderQcDao.save(flowOrderQcs);
                }
                //1.3.1 作废发货计划流
                List<FlowOrderShippingPlan> flowOrderShippingPlans = flowOrderShippingPlanDao.findByOrderId(order.getId());
                if(flowOrderShippingPlans != null && flowOrderShippingPlans.size() > 0){
                    for (int i = 0; i < flowOrderShippingPlans.size(); i++) {
                        FlowOrderShippingPlan flowOrderShippingPlan = flowOrderShippingPlans.get(i);
                        flowOrderShippingPlan.setStatus(Constants.Status.CANCELED.code);
                        flowOrderShippingPlan.setUpdatedAt(now);
                    }
                    flowOrderShippingPlanDao.save(flowOrderShippingPlans);
                }
                //1.3.2 作废发货计划
                List<OrderShippingPlan> orderShippingPlans = orderShippingPlanDao.findByOrderId(order.getId());
                if(orderShippingPlans != null & orderShippingPlans.size() > 0){
                    for (int i = 0; i < orderShippingPlans.size(); i++) {
                        OrderShippingPlan orderShippingPlan = orderShippingPlans.get(i);
                        orderShippingPlan.setStatus(Constants.Status.CANCELED.code);
                        orderShippingPlan.setUpdatedAt(now);
                    }
                    orderShippingPlanDao.save(orderShippingPlans);
                }
                //1.4 作废发货确认单
                List<FlowOrderShippingApply> flowOrderShippingApplies = flowOrderShippingApplyDao.findByOrderId(order.getId());
                if(flowOrderShippingApplies != null && flowOrderShippingApplies.size() > 0){
                    for (int i = 0; i < flowOrderShippingApplies.size(); i++) {
                        FlowOrderShippingApply flowOrderShippingApply = flowOrderShippingApplies.get(i);
                        flowOrderShippingApply.setStatus(Constants.Status.CANCELED.code);
                        flowOrderShippingApply.setUpdatedAt(new Date());
                    }
                    flowOrderShippingApplyDao.save(flowOrderShippingApplies);
                }
                //1.5 作废定金
                List<FlowPurchaseContractDeposit> flowDeposits = flowPurchaseContractDepositDao.findDepositByOrderId(order.getId());
                if(flowDeposits != null && flowDeposits.size() > 0){
                    for (int i = 0; i < flowDeposits.size(); i++) {
                        FlowPurchaseContractDeposit deposit = flowDeposits.get(i);
                        deposit.setStatus(Constants.Status.CANCELED.code);
                        deposit.setUpdatedAt(now);
                    }
                    flowPurchaseContractDepositDao.save(flowDeposits);
                }
            }
            restorePurchasePlan(flowOrder.getId());
        }
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowPurchaseContract flow = flowPurchaseContractDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowPurchaseContractDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowPurchaseContract flow = flowPurchaseContractDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowPurchaseContractDao.save(flow);
    }

    public List<FlowPurchaseContract> findByProductPurchasePlanBusinessId(String purchasePlanBusinessId){
        return flowPurchaseContractDao.findByProductPurchasePlanBusinessId(purchasePlanBusinessId);
    }

    private Specification<FlowPurchaseContract> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowPurchaseContract> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowPurchaseContract.class);
        return spec;
    }
}
