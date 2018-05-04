package com.newaim.purchase;

/**
 * 定义通用常量
 */
public class Constants {

    /**
     * 档案状态
     */
    public enum Status{

        DRAFT(0, "草稿"), NORMAL(1, "正常"), DISABLED(2, "已禁用"), DELETED(3, "已删除"), CANCELED(4, "已作废");

        Status(Integer code, String name){

            this.code = code;
            this.name = name;
        }

        public final Integer code;

        public final String name;
    }

    /**
     * 档案状态
     */
    public enum ProductSyncFlag{

        NORMAL(3, "不处理"), ADD(1, "创建"), UPDATE(2, "更新");

        ProductSyncFlag(Integer code, String name){

            this.code = code;
            this.name = name;
        }

        public final Integer code;

        public final String name;
    }

    /**
     * 冻结状态
     */
    public enum HoldStatus{

        HOLD(1, "冻结"), UN_HOLD(2, "未冻结");

        HoldStatus(Integer code, String name){

            this.code = code;
            this.name = name;
        }

        public final Integer code;

        public final String name;
    }

    /**
     * 新品的开发、安检、质检状态定义
     */
    public enum NewProductStatus{
        DEV_STATUS_NOT_PASS(2, "新品开发未通过"), DEV_STATUS_PASS(1, "新品开发已通过"),
        COMPLIANCE_STATUS_NOT_PASS(2, "安检未通过"), COMPLIANCE_STATUS_PASS(1, "安检已通过"),
        QC_STATUS_NOT_PASS(2, "质检未通过"), QC_STATUS_PASS(1, "质检已通过");

        NewProductStatus(Integer code, String name){

            this.code = code;
            this.name = name;
        }

        public final Integer code;

        public final String name;
    }

    /**
     * 风控级别
     */
    public enum  RiskRating{
        GREEN(1, "绿（无检）"), YELLOW(2, "黄（五抽一）"), BLUE(3, "蓝（三抽一）"),
        ORANGE(4, "橙（全检）"), RED(5, "红（售完停售）"), BLACK(6, "黑（立即封存）");

        RiskRating(Integer code, String name){

            this.code = code;
            this.name = name;
        }

        public final Integer code;

        public final String name;

        /**
         * 判断是否通过安检
         * @param code
         * @return
         */
        public static boolean isPass(Integer code){
            if(code != null){
                if(GREEN.code.equals(code) || YELLOW.code.equals(code)
                        || BLUE.code.equals(code) || ORANGE.code.equals(code)){
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 供应商类别
     */
    public enum VendorType{
        VENDOR(1, "供应商"), SERVICE_PROVIDER(2, "服务商");

        VendorType(Integer code, String name){

            this.code = code;
            this.name = name;
        }

        public final Integer code;

        public final String name;
    }

    /**
     * 流程状态
     */
    public enum FlowStatus{

        ADJUST_APPLY(0, "调整申请", "adjust apply"), REVIEW(1, "审批中", "review"), PASS(2, "通过", "pass"),REJECTED(3, "拒绝", "rejected");

        FlowStatus(Integer code, String cnName, String enName){
            this.code = code;
            this.cnName = cnName;
            this.enName = enName;
        }

        public final Integer code;

        public final String cnName;

        public final String enName;

        public static String getCnName(Integer code){
            FlowStatus[] statuses = FlowStatus.values();
            for (FlowStatus status: statuses) {
                if(status.code.equals(code)){
                    return status.cnName;
                }
            }
            return null;
        }

        public static String getEnName(Integer code){
            FlowStatus[] statuses = FlowStatus.values();
            for (FlowStatus status: statuses) {
                if(status.code.equals(code)){
                    return status.enName;
                }
            }
            return null;
        }
    }

    /**
     * 流程历史状态
     */
    public enum FlowHistoryStatus{
        START(1, "发起"), BACK(2, "退回"),REDO(3, "返审"),PASS(4, "通过"),REFUSE(5, "拒绝");

        FlowHistoryStatus(Integer code, String name){
            this.code = code;
            this.name = name;
        }

        public final Integer code;

        public final String name;
    }

    /**
     * 流程处理动作定义
     */
    public enum FlowAct{

        /**发起*/
        START("99", "start"),
        /**通过*/
        ALLOW("0", "allow"),
        /**退回*/
        BACK("1", "back"),
        /**返审*/
        REDO("2", "redo"),
        /**拒绝*/
        REFUSE("3", "refuse");

        FlowAct(String code, String name){
            this.code = code;
            this.name = name;
        }

        public final String code;

        public final String name;

    }

    /**
     * 流程查找下一节点接收人方式
     */
    public enum FlowFindType{
        ROLE("role", "角色"), UP_ROLE("upRole", "上级或同级角色"),DOWN_ROLE("downRole", "下级或同级角色"), SAME_ROLE("sameRole", "同角色"),
        INITIATOR_ROLE("initiatorRole", "发起人角色"), INITIATOR_UP_ROLE("initiatorUpRole", "发起人上级角色");

        FlowFindType(String code, String name){
            this.code = code;
            this.name = name;
        }

        public final String code;

        public final String name;
    }

    /**
     * 费用支付状态
     */
    public enum FeePaymentStatus{


        UNPAID(1, "未支付"), PROCESSING(2, "支付中"), COMPLETE(3, "已支付");

        FeePaymentStatus(Integer code, String name){
            this.code = code;
            this.name = name;
        }

        public final Integer code;

        public final String name;
    }

    /**
     * 费用支付类型
     */
    public enum FeeType{


        VENDOR(1, "供应商"), SERVICE_PROVIDER(2, "服务商"), CONTRACT_BALANCE(3, "合同尾款"), VENDOR_DEPOSIT(4, "供应商押金"), ELECTRONIC_PROCESSING_FEE(5, "电放费");

        FeeType(Integer code, String name){
            this.code = code;
            this.name = name;
        }

        public final Integer code;

        public final String name;
    }

}
