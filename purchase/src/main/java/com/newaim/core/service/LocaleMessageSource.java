package com.newaim.core.service;

import com.newaim.purchase.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 国际化操作类，提供通用操作提示/list/add/update/delete/up/down/upgrade/downgrade
 * Created by Mark on 2017/9/18.
 */
@Component
public class LocaleMessageSource {

    /**通用操作提示*/
    public static final String MSG_UNAUTHORIZED = "msg_unauthorized";
    public static final String MSG_LIST_SUCCESS = "msg_list_success";
    public static final String MSG_LIST_FAILURE = "msg_list_failure";
    public static final String MSG_GET_SUCCESS = "msg_get_success";
    public static final String MSG_GET_FAILURE = "msg_get_failure";
    public static final String MSG_ADD_SUCCESS = "msg_add_success";
    public static final String MSG_ADD_FAILURE = "msg_add_failure";
    public static final String MSG_INS_SUCCESS = "msg_ins_success";
    public static final String MSG_INS_FAILURE = "msg_ins_failure";
    public static final String MSG_UPDATE_SUCCESS = "msg_update_success";
    public static final String MSG_UPDATE_FAILURE = "msg_update_failure";
    public static final String MSG_DELETE_SUCCESS = "msg_delete_success";
    public static final String MSG_DELETE_FAILURE = "msg_delete_failure";
    public static final String MSG_DEPLOY_SUCCESS = "msg_deploy_success";
    public static final String MSG_DEPLOY_FAILURE = "msg_deploy_failure";
    public static final String MSG_UP_SUCCESS = "msg_up_success";
    public static final String MSG_UP_FAILURE = "msg_up_failure";
    public static final String MSG_UP_FAILURE_TOP = "msg_up_failure_top";
    public static final String MSG_DOWN_SUCCESS = "msg_down_success";
    public static final String MSG_DOWN_FAILURE = "msg_down_failure";
    public static final String MSG_DOWN_FAILURE_BOTTOM = "msg_down_failure_bottom";
    public static final String MSG_UPGRADE_SUCCESS = "msg_upgrade_success";
    public static final String MSG_UPGRADE_FAILURE_TOP_LEVEL = "msg_upgrade_failure_top_level";
    public static final String MSG_DOWNGRADE_SUCCESS = "msg_downgrade_success";
    public static final String MSG_DOWNGRADE_FAILURE_BOTTOM_LEVEL = "msg_downgrade_failure_bottom_level";
    public static final String MSG_OPERATE_SUCCESS = "msg_operate_success";
    public static final String MSG_OPERATE_FAILURE = "msg_operate_failure";
    public static final String MSG_OPERATE_EXCEPTION = "msg_operate_exception";

    public static final String MSG_TASK_CANNOT_DELETE = "msg_task_cannot_delete";
    public static final String MSG_TASK_NOT_FOUND = "msg_task_not_found";
    public static final String MSG_TASK_NO_ASSIGNEE = "msg_task_no_assignee";

    public static final String MSG_TASK_MSG_TITLE = "msg_task_msg_title";
    public static final String MSG_TASK_MSG_CONTENT = "msg_task_msg_content";


    public static final String MSG_EMAIL_CONN_TEST_SUCCESS = "msg_email_conn_test_success";
    public static final String MSG_EMAIL_CONN_TEST_FAILURE = "msg_email_conn_test_failure";
    public static final String MSG_EMAIL_RECEIVE_SUCCESS = "msg_email_receive_success";
    public static final String MSG_EMAIL_RECEIVE_FAILURE = "msg_email_receive_failure";
    public static final String MSG_EMAIL_SEND_SUCCESS = "msg_email_send_success";
    public static final String MSG_EMAIL_SEND_FAILURE = "msg_email_send_failure";


    public static final String MSG_NEW_PRODUCT_CONVERT_TITLE = "msg_new_product_convert_title";
    public static final String MSG_NEW_PRODUCT_CONVERT_CONTENT = "msg_new_product_convert_content";

    public static final String MSG_LOGIN_USER_DISABLED="msg_login_user_disabled";

    //档案历史操作提示
    public static final String MSG_ARCHIVES_SUBMIT_SUCCESS="msg_archives_submit_success";
    public static final String MSG_ARCHIVES_SUBMIT_FAILURE="msg_archives_submit_failure";
    public static final String MSG_ARCHIVES_CONFIRM_SUCCESS="msg_archives_confirm_success";
    public static final String MSG_ARCHIVES_CONFIRM_FAILURE="msg_archives_confirm_failure";

    /**SKU*/
    public static final String MSG_PRODUCT_EXISTS_SKU="msg_product_exists_sku";
    /**barcode*/
    public static final String MSG_PRODUCT_EXISTS_BARCODE="msg_product_exists_barcode";
    /**ean*/
    public static final String MSG_PRODUCT_EXISTS_EAN="msg_product_exists_ean";
    /**asnNumber*/
    public static final String MSG_PRODUCT_EXISTS_ASN_NUMBER="msg_product_exists_asn_number";

    /**vendor code*/
    public static final String MSG_PRODUCT_EXISTS_VENDOR_CODE="msg_product_exists_vendor_code";

    /**Hold*/
    public static final String MSG_HOLD_ALREADY = "msg_hold_already";
    public static final String MSG_HOLD_SUCCESS = "msg_hold_success";
    public static final String MSG_UN_HOLD_SUCCESS = "msg_un_hold_success";
    public static final String MSG_CANCEL_SUCCESS = "msg_cancel_success";
    public static final String MSG_CANCEL_FAILURE = "msg_cancel_failure";

    /**安检不合格*/
    public static final String MSG_RISK_RATING_NOT_PASS = "msg_risk_rating_not_pass";
    
    /**档案审核消息*/
    public static final String MSG_ARCHIVES_EDIT_TITLE = "msg_archives_edit_title";
    public static final String MSG_ARCHIVES_EDIT_CONTENT = "msg_archives_edit_content";
    public static final String MSG_ARCHIVES_CONFIRM_TITLE = "msg_archives_confirm_title";
    public static final String MSG_ARCHIVES_CONFIRM_CONTENT= "msg_archives_confirm_content";
    


    @Autowired
    private MessageSource messageSource;

    public String getMsgLoginUserDisabled(){
        return this.getMessage(MSG_LOGIN_USER_DISABLED);
    }

    /**
     * 没有权限
     */
    public String getMsgUnauthorized(){
        return this.getMessage(MSG_UNAUTHORIZED);
    }

    /**
     *  通用list成功提示
     */
    public String getMsgListSuccess(){
        return this.getMessage(MSG_LIST_SUCCESS);
    }

    /**
     *  通用list失败提示
     */
    public String getMsgListFailure(Object... args){
        return this.getMessage(MSG_LIST_FAILURE, args);
    }

    /**
     * 获取成功
     */
    public String getMsgGetSuccess() {
        return this.getMessage(MSG_GET_SUCCESS);
    }

    /**
     * 获取失败
     * @param args 失败原因
     */
    public String getMsgGetFailure(Object... args) {
        return this.getMessage(MSG_GET_FAILURE, args);
    }

    /**
     * <pre>
     *  根據動作返回不同的成功提示
     *  edit/update: 更新
     *  add/create: 創建
     * </pre>
     * @param act 動作
     */
    public String getMsgSaveSuccess(String act){
        if ("add".equals(act) || "copy".equals(act) || "create".equals(act)){
            return getMsgAddSuccess();
        }else if("edit".equals(act) || "save".equals(act) || "update".equals(act)){
            return getMsgUpdateSuccess();
        }else{
            return getMsgOperateSuccess();
        }
    }

    /**
     * <pre>
     *  根據動作返回不同的失敗提示
     *  edit/update: 更新
     *  add/create: 創建
     * </pre>
     * @param act 動作
     */
    public String getMsgSaveFailure(String act, Object... args){
        if ("add".equals(act) || "copy".equals(act) || "create".equals(act)){
            return getMsgAddFailure(args);
        }else if("edit".equals(act) || "save".equals(act) || "update".equals(act)){
            return getMsgUpdateFailure(args);
        }else{
            return getMsgOperateException(args);
        }
    }

    /**
     * 创建成功
     */
    public String getMsgAddSuccess() {
        return this.getMessage(MSG_ADD_SUCCESS);
    }

    /**
     *  创建失败
     * @param args 失败
     */
    public String getMsgAddFailure(Object... args) {
        return this.getMessage(MSG_ADD_FAILURE, args);
    }

    /**
     * 插入成功
     */
    public String getMsgInsSuccess() {
        return this.getMessage(MSG_INS_SUCCESS);
    }

    /**
     * 插入失败
     * @param args 失败原因
     */
    public String getMsgInsFailure(Object... args) {
        return this.getMessage(MSG_INS_FAILURE, args);
    }

    /**
     * 更新成功
     */
    public String getMsgUpdateSuccess() {
        return this.getMessage(MSG_UPDATE_SUCCESS);
    }

    /**
     * 更新失败
     * @param args 更新失败原因
     */
    public String getMsgUpdateFailure(Object... args) {
        return this.getMessage(MSG_UPDATE_FAILURE, args);
    }

    /**
     * 删除成功
     */
    public String getMsgDeleteSuccess() {
        return this.getMessage(MSG_DELETE_SUCCESS);
    }

    /**
     * 删除失败
     * @param args 失败原因
     */
    public String getMsgDeleteFailure(Object... args) {
        return this.getMessage(MSG_DELETE_FAILURE, args);
    }
    /**
     * 删除成功
     */
    public String getMsgDeploySuccess() {
    	return this.getMessage(MSG_DEPLOY_SUCCESS);
    }

    /**
     * 删除失败
     * @param args 失败原因
     */
    public String getMsgDeployFailure(Object... args) {
    	return this.getMessage(MSG_DEPLOY_FAILURE, args);
    }

    /**
     * 上移成功
     */
    public String getMsgUpSuccess() {
        return this.getMessage(MSG_UP_SUCCESS);
    }

    /**
     * 上移失败
     * @param args 失败原因
     */
    public String getMsgUpFailure(Object... args) {
        return this.getMessage(MSG_UP_FAILURE, args);
    }

    /**
     * 上移失败，已到最上面
     */
    public String getMsgUpFailureTop() {
        return this.getMessage(MSG_UP_FAILURE_TOP);
    }

    /**
     * 下移成功
     */
    public String getMsgDownSuccess() {
        return this.getMessage(MSG_DOWN_SUCCESS);
    }

    /**
     * 下移失败
     * @param args 失败原因
     */
    public String getMsgDownFailure(Object... args) {
        return this.getMessage(MSG_DOWN_FAILURE, args);
    }

    /**
     *  下移失败，已到最下面
     */
    public String getMsgDownFailureBottom() {
        return this.getMessage(MSG_DOWN_FAILURE_BOTTOM);
    }

    /**
     * 升级成功
     */
    public String getMsgUpgradeSuccess() {
        return this.getMessage(MSG_UPGRADE_SUCCESS);
    }

    /**
     * 升级失败，已到最顶层
     */
    public String getMsgUpgradeFailureTopLevel() {
        return this.getMessage(MSG_UPGRADE_FAILURE_TOP_LEVEL);
    }

    /**
     * 降级成功
     */
    public String getMsgDowngradeSuccess() {
        return this.getMessage(MSG_DOWNGRADE_SUCCESS);
    }

    /**
     * 降级失败，已到最底层
     */
    public String getMsgDowngradeFailureBottomLevel() {
        return this.getMessage(MSG_DOWNGRADE_FAILURE_BOTTOM_LEVEL);
    }


    public String getMsgOperateSuccess() {
        return this.getMessage(MSG_OPERATE_SUCCESS);
    }

    /**
     * 操作失败
     */
    public String getMsgOperateFailure() {
        return this.getMessage(MSG_OPERATE_FAILURE);
    }

    /**
     * 操作异常
     * @param args 异常原因
     */
    public String getMsgOperateException(Object... args) {
        return this.getMessage(MSG_OPERATE_EXCEPTION, args);
    }

    /**
     * 邮箱测试通过
     * @return
     */
    public String getMsgEmailConnTestSuccess(){
        return this.getMessage(MSG_EMAIL_CONN_TEST_SUCCESS);
    }

    /**
     * 邮箱测试不通过
     * @return
     */
    public String getMsgEmailConnTestFailure(){
        return this.getMessage(MSG_EMAIL_CONN_TEST_FAILURE);
    }

    /**
     * 邮件接收成功
     * @return
     */
    public String getMsgEmailReceiveSuccess(Object... args){
        return this.getMessage(MSG_EMAIL_RECEIVE_SUCCESS, args);
    }

    /**
     * 邮件接收失败
     * @return
     */
    public String getMsgEmailReceiveFailure(){
        return this.getMessage(MSG_EMAIL_RECEIVE_FAILURE);
    }
    /**
     * 邮件发送成功
     * @return
     */
    public String getMsgEmailSendSuccess() {return this.getMessage(MSG_EMAIL_SEND_SUCCESS);}
    /**
     * 邮件发送失败
     * @return
     */
    public String getMsgEmailSendFailure() {return this.getMessage(MSG_EMAIL_SEND_FAILURE);}
    /**
     * 任务不能删除，已发起流程
     * @return
     */
    public String getMsgTaskCannotDelete(){
        return this.getMessage(MSG_TASK_CANNOT_DELETE);
    }

    /**
     * 找不到对应任务
     * @return 提示信息
     */
    public String getMsgTaskNotFound(){
        return this.getMessage(MSG_TASK_NOT_FOUND);
    }

    /**
     * 找不到任务签收人或签收人为多个
     * @return
     */
    public String getMsgTaskNoAssignee(){
        return this.getMessage(MSG_TASK_NO_ASSIGNEE);
    }

    /**
     * 流程消息標題
     * @param args
     * @return
     */
    public String getMsgTaskMsgTitle(Object... args){
        return this.getMessage(MSG_TASK_MSG_TITLE, args);
    }

    /**
     * 流程消息内容
     * @param args
     * @return
     */
    public String getMsgTaskMsgContent(Object... args){
        return this.getMessage(MSG_TASK_MSG_CONTENT, args);
    }

    /**
     * 新品转换提示标题
     * @param args
     * @return
     */
    public String getMsgNewProductConvertTitle(Object... args){
        return this.getMessage(MSG_NEW_PRODUCT_CONVERT_TITLE, args);
    }

    /**
     * 新品转换提示内容
     * @param args
     * @return
     */
    public String getMsgNewProductConvertContent(Object... args){
        return this.getMessage(MSG_NEW_PRODUCT_CONVERT_CONTENT, args);
    }

    /**
     * 档案修改提交成功
     * @return
     */
    public  String getMsgArchivesSubmitSuccess(){
        return this.getMessage(MSG_ARCHIVES_SUBMIT_SUCCESS);
    }

    public String getMsgArchivesSubmitFailure(){
        return this.getMessage(MSG_ARCHIVES_SUBMIT_FAILURE);
    }

    public String getMsgArchivesConfirmSuccess(){
        return this.getMessage(MSG_ARCHIVES_CONFIRM_SUCCESS);
    }

    public String getMsgArchivesConfirmFailure(){
        return this.getMessage(MSG_ARCHIVES_CONFIRM_FAILURE);
    }

    /**
     * 已存在sku
     * @return
     */
    public String getMsgProductExistsSku(String... args){
        return this.getMessage(MSG_PRODUCT_EXISTS_SKU, args);
    }

    /**
     * 已存在barcode
     * @return
     */
    public String getMsgProductExistsBarcode(String... args){
        return this.getMessage(MSG_PRODUCT_EXISTS_BARCODE, args);
    }

    /**
     * 已存在ean
     * @return
     */
    public String getMsgProductExistsEan(String... args){
        return this.getMessage(MSG_PRODUCT_EXISTS_EAN, args);
    }

    /**
     * 已存在asnNumber
     * @return
     */
    public String getMsgProductExistsAsnNumber(String... args){
        return this.getMessage(MSG_PRODUCT_EXISTS_ASN_NUMBER, args);
    }

    /**
     * 已存在供应商编码
     * @return
     */
    public String getMsgProductExistsVendorCode(String... args){
        return this.getMessage(MSG_PRODUCT_EXISTS_VENDOR_CODE, args);
    }

    /**
     * 流程已冻结
     * @param businessId
     * @return
     */
    public String getMsgHoldAlready(String businessId){
        return getMessage(MSG_HOLD_ALREADY, businessId);
    }

    /**
     * 冻结成功
     * @param businessId
     */
    public String getMsgHoldSuccess(String businessId){
        return this.getMessage(MSG_HOLD_SUCCESS, businessId);
    }

    /**
     * 解冻成功
     * @param businessId
     * @return
     */
    public String getMsgUnHoldSuccess(String businessId) {
        return this.getMessage(MSG_UN_HOLD_SUCCESS, businessId);
    }

    /**
     * 作废成功
     * @param businessId
     * @return
     */
    public String getMsgCancelSuccess(String businessId){
        return this.getMessage(MSG_CANCEL_SUCCESS, businessId);
    }

    /**
     * 作废失败
     * @return
     */
    public String getMsgCancelFailure(String businessId){
        return this.getMessage(MSG_CANCEL_FAILURE, businessId);
    }

    /**
     * 安检不合格
     * @return
     */
    public String getMsgRiskRatingNotPass(){
        return this.getMessage(MSG_RISK_RATING_NOT_PASS);
    }

    public String getMessage(String code){
        return this.getMessage(code, new Object[]{}, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, Object... args){
        return this.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, Object[] args, Locale locale){
        return messageSource.getMessage(code, args, locale);
    }


    /**
     * 档案修改消息標題
     * @param args
     * @return
     */
    public String getMsgArchivesChangeTitle(Object... args){
        return this.getMessage(MSG_ARCHIVES_EDIT_TITLE, args);
    }
    /**
     * 档案审核消息標題
     * @param args
     * @return
     */
    public String getMsgArchivesConfirmTitle(Object... args){
    	return this.getMessage(MSG_ARCHIVES_CONFIRM_TITLE, args);
    }
    /**
     * 档案审核消息修改提示内容
     * @param args
     * @return
     */
    public String getMsgArchivesEditContent(Object... args){
    	return this.getMessage(MSG_ARCHIVES_EDIT_CONTENT, args);
    }
    /**
     * 档案审核消息审核提示内容
     * @param args
     * @return
     */
    public String getMsgArchivesConfirmContent(Object... args){
    	return this.getMessage(MSG_ARCHIVES_CONFIRM_CONTENT, args);
    }
}
