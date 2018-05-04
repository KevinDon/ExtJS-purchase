/*
Navicat PGSQL Data Transfer

Source Server         : pgsql_localhost
Source Server Version : 90603
Source Host           : localhost:5432
Source Database       : newaim_purchase
Source Schema         : public

Target Server Type    : PGSQL
Target Server Version : 90603
File Encoding         : 65001

Date: 2017-09-01 18:31:17
*/


-- ----------------------------
-- Sequence structure for na_account_department_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."na_account_department_id_seq";
CREATE SEQUENCE "public"."na_account_department_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;
SELECT setval('"public"."na_account_department_id_seq"', 1, true);

-- ----------------------------
-- Sequence structure for na_account_resource_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."na_account_resource_id_seq";
CREATE SEQUENCE "public"."na_account_resource_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for na_account_role_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."na_account_role_id_seq";
CREATE SEQUENCE "public"."na_account_role_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for na_account_user_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."na_account_user_id_seq";
CREATE SEQUENCE "public"."na_account_user_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;
SELECT setval('"public"."na_account_user_id_seq"', 1, true);

-- ----------------------------
-- Table structure for na_account_department
-- ----------------------------
DROP TABLE IF EXISTS "public"."na_account_department";
CREATE TABLE "public"."na_account_department" (
"id" varchar(20) COLLATE "default" DEFAULT nextval('na_account_department_id_seq'::regclass) NOT NULL,
"parent_id" varchar(20) COLLATE "default",
"cn_name" varchar(50) COLLATE "default",
"en_name" varchar(50) COLLATE "default",
"level" int2,
"leaf" int2,
"status" int2,
"created_at" timestamp(6),
"path" varchar(255) COLLATE "default",
"sort" int8
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of na_account_department
-- ----------------------------
INSERT INTO "public"."na_account_department" VALUES ('DEP20180822000000111', '', '公司', 'Company', '1', '0', '1', '2017-08-22 15:32:15', '', '1');

-- ----------------------------
-- Table structure for na_account_resource
-- ----------------------------
DROP TABLE IF EXISTS "public"."na_account_resource";
CREATE TABLE "public"."na_account_resource" (
"id" varchar(20) COLLATE "default" DEFAULT nextval('na_account_resource_id_seq'::regclass) NOT NULL,
"parent_id" varchar(20) COLLATE "default",
"cn_name" varchar(50) COLLATE "default",
"en_name" varchar(50) COLLATE "default",
"leaf" int2,
"level" int2,
"type" int2,
"url" varchar(255) COLLATE "default",
"status" int2,
"sort" int2,
"path" varchar(255) COLLATE "default",
"item_icon" varchar(100) COLLATE "default",
"created_at" timestamp(6),
"code" varchar(25) COLLATE "default",
"function" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of na_account_resource
-- ----------------------------
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728154148625', 'MOD20170822101010111', '统计分析', 'Statistic Analysis', '0', '2', '0', '', '1', '4', 'MOD20170822101010111', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728155255294', 'MOD20170728154148625', '订单统计表', 'Order Statistics', '1', '1', '2', 'StatisticsOrderView', '1', '1', 'MOD20170822101010111_MOD20170728154148625', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728155347776', 'MOD20170728154148625', '质量问题统计表', 'Problem Statistics', '1', '1', '2', 'StatisticsProblemView', '1', '2', 'MOD20170822101010111_MOD20170728154148625', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728155450682', 'MOD20170728154148625', '成本统计表', 'Cost Statistics', '1', '1', '2', 'StatisticsCostView', '1', '3', 'MOD20170822101010111_MOD20170728154148625', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728155544769', 'MOD20170728154148625', '质检统计表', 'Quality Statistics', '1', '1', '2', 'StatisticsQualityView', '1', '4', 'MOD20170822101010111_MOD20170728154148625', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728155654889', 'MOD20170728154148625', '订单周期统计表', 'Orders Cycle Summary', '1', '1', '2', 'StatisticsOrdersCycleView', '1', '5', 'MOD20170822101010111_MOD20170728154148625', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728155843430', 'MOD20170822101010114', '用户级权限', 'User & Role', '0', '2', '0', '', '1', '1', 'MOD20170822101010114', '', '2017-08-23 08:58:08', null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728155938744', 'MOD20170728155843430', '用户账号', 'User Account', '1', '1', '2', 'AccountUserView', '1', '1', 'MOD20170822101010114_MOD20170728155843430', '', '2017-08-23 08:58:08', null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728160112776', 'MOD20170728155843430', '组织架构', 'Department Manage', '1', '1', '2', 'AccountDepartmentView', '1', '2', 'MOD20170822101010114_MOD20170728155843430', '', '2017-08-23 08:58:08', null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728160137853', 'MOD20170728155843430', '用户角色', 'Role Manage', '1', '1', '2', 'AccountRoleView', '1', '3', 'MOD20170822101010114_MOD20170728155843430', '', '2017-08-23 08:58:08', null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728160248143', 'MOD20170728155843430', '角色权限', 'Role Permission', '1', '1', '2', 'AccountPermissionView', '1', '4', 'MOD20170822101010114_MOD20170728155843430', '', '2017-08-23 08:58:08', null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728160348129', 'MOD20170822101010114', '数据字典', 'Data Dictionary', '0', '0', '0', '', '1', '2', 'MOD20170822101010114', '', '2017-08-23 08:58:08', null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728160507765', 'MOD20170728160348129', '字典分类', 'Data Category', '1', '1', '2', 'DataDictionaryCategoryView', '1', '1', 'MOD20170822101010114_MOD20170728160348129', '', '2017-08-23 08:58:08', '', '');
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728160556818', 'MOD20170728160348129', '字典管理', 'Data Dictionary Manage', '1', '1', '2', 'DataDictionaryManageView', '1', '2', 'MOD20170822101010114_MOD20170728160348129', '', '2017-08-23 08:58:08', null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728160751128', 'MOD20170822101010114', '系统设置', 'System Setting', '0', '0', '0', '', '1', '5', 'MOD20170822101010114', '', '2017-08-23 08:58:08', null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728160921443', 'MOD20170728160751128', '提醒时长设置', 'Alert Setting', '1', '1', '2', 'AlertSettingView', '1', '1', 'MOD20170822101010114_MOD20170728160751128', '', '2017-08-23 08:58:08', null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728161037934', 'MOD20170728160751128', '常用配置项目', 'Common Configuration Items', '1', '1', '2', 'CommonConfigurationItemsView', '1', '2', 'MOD20170822101010114_MOD20170728160751128', '', '2017-08-23 08:58:08', null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728161255717', 'MOD20170822101010111', '相关报告', 'Related Reports', '0', '0', '0', '', '1', '5', 'MOD20170822101010111', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728161313432', 'MOD20170822101010111', '文件管理', 'Files Manage', '0', '2', '0', '', '1', '8', 'MOD20170822101010111', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728161348600', 'MOD20170822101010111', '财务功能', 'Finance Function', '0', '0', '0', '', '1', '7', 'MOD20170822101010111', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728161428168', 'MOD20170822101010111', '系统工具', 'System Tools', '0', '0', '0', '', '1', '9', 'MOD20170822101010111', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728161527108', 'MOD20170728161255717', '供应商调查报告', 'Supplier Investigation Report', '1', '1', '2', 'ReportSupplierInvestigationView', '1', '1', 'MOD20170822101010111_MOD20170728161255717', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728161611385', 'MOD20170728161255717', '产品分析报告', 'Product Analysis Report', '1', '1', '2', 'ReportProductAnalysisView', '1', '2', 'MOD20170822101010111_MOD20170728161255717', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728161650122', 'MOD20170728161255717', '产品预测报告', 'Product Forecast Report', '1', '1', '2', 'ReportProductForecastView', '1', '3', 'MOD20170822101010111_MOD20170728161255717', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728161733155', 'MOD20170728161255717', '产品汇总报告', 'Product Summary Report', '1', '1', '2', 'ReportProductSummaryView', '1', '4', 'MOD20170822101010111_MOD20170728161255717', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728161912614', 'MOD20170728161255717', '样品检验报告', 'Sample Inspection report', '1', '1', '2', 'ReportSampleInspectionView', '1', '5', 'MOD20170822101010111_MOD20170728161255717', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728162259521', 'MOD20170728161255717', '产品安检报告', 'Product Compliance Report', '1', '1', '2', 'ReportProductComplianceView', '1', '6', 'MOD20170822101010111_MOD20170728161255717', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728162359145', 'MOD20170728161255717', '订单检验报告', 'Order Inspection Report', '1', '1', '2', 'ReportOrderInspectionView', '1', '7', 'MOD20170822101010111_MOD20170728161255717', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728162514871', 'MOD20170728161313432', '业务模板', 'Business Service Template', '1', '1', '2', 'TemplateBusinessServiceView', '1', '1', 'MOD20170822101010111_MOD20170728161313432', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728162548115', 'MOD20170728161313432', '我的文件', 'My Document', '1', '1', '2', 'MyDocumentView', '1', '2', 'MOD20170822101010111_MOD20170728161313432', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728162641310', 'MOD20170728161348600', '汇率发布', 'Exchange Rate Manage', '1', '1', '2', 'ExchangeRateView', '1', '1', 'MOD20170822101010111_MOD20170728161348600', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728162753766', 'MOD20170728161428168', '汇率计算器', 'ExchangeCalculator', '1', '1', '2', 'ToolExchangeCalculatorView', '1', '1', 'MOD20170822101010111_MOD20170728161428168', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728162921619', 'MOD20170822101010113', '供应商', 'Vendor Manage', '0', '1', '0', '', '1', '1', 'MOD20170822101010113', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728163007109', 'MOD20170822101010113', '服务商', 'Service Provider', '0', '0', '0', '', '1', '2', 'MOD20170822101010113', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728163518494', 'MOD20170822101010113', '产品资料', 'Product Manage', '0', '2', '0', '', '1', '3', 'MOD20170822101010113', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728163627238', 'MOD20170728162921619', '供应商分类', 'Vendor Category Manage', '1', '1', '2', 'VendorCategoryView', '1', '1', 'MOD20170822101010113_MOD20170728162921619', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728163712156', 'MOD20170728162921619', '供应商档案', 'Vendor Document', '1', '1', '2', 'VendorDocumentView', '1', '2', 'MOD20170822101010113_MOD20170728162921619', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728163801269', 'MOD20170728163518494', '产品分类', 'Product Category Manage', '1', '1', '2', 'ProductCategoryView', '1', '1', 'MOD20170822101010113_MOD20170728163518494', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728163831887', 'MOD20170728163518494', '产品档案', 'Product Document', '1', '1', '2', 'ProductDocumentView', '1', '2', 'MOD20170822101010113_MOD20170728163518494', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728163939932', 'MOD20170728163518494', '产品组合关系', 'Product Combination', '1', '1', '2', 'ProductCombinationView', '1', '3', 'MOD20170822101010113_MOD20170728163518494', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728164026724', 'MOD20170728163518494', '产品问题记录', 'Product Problem', '1', '1', '2', 'ProductProblemView', '1', '4', 'MOD20170822101010113_MOD20170728163518494', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728164106927', 'MOD20170728163007109', '服务商分类', 'Service Provider Category', '1', '1', '2', 'ServiceProviderCategoryView', '1', '1', 'MOD20170822101010113_MOD20170728163007109', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728164136213', 'MOD20170728163007109', '服务商分类', 'Service Provider Document', '1', '1', '2', 'ServiceProviderDocumentView', '1', '2', 'MOD20170822101010113_MOD20170728163007109', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728164235774', 'MOD20170822101010112', '采购类', 'Purchasing Class', '0', '1', '0', '', '1', '1', 'MOD20170822101010112', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728164308465', 'MOD20170822101010112', '检验类', 'Inspection Class', '0', '0', '0', '', '1', '2', 'MOD20170822101010112', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728164358580', 'MOD20170822101010112', '船务类', 'Shipping Class', '0', '0', '0', '', '1', '3', 'MOD20170822101010112', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728164508555', 'MOD20170822101010112', '财务类', 'Finance Class', '0', '0', '0', '', '1', '4', 'MOD20170822101010112', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728164737880', 'MOD20170728164235774', '新品开发申请', 'New Product Application', '1', '1', '2', 'FlowNewProductView', '1', '1', 'MOD20170822101010112_MOD20170728164235774', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728164840980', 'MOD20170728164235774', '样品申请', 'Sample Application', '1', '1', '2', 'FlowSampleView', '1', '2', 'MOD20170822101010112_MOD20170728164235774', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728165118487', 'MOD20170728164235774', '采购询价申请', 'Product Quotation Application', '1', '1', '2', 'FlowProductQuotationView', '1', '3', 'MOD20170822101010112_MOD20170728164235774', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728165327264', 'MOD20170728164235774', '采购计划申请', 'Purchase Plan Application', '1', '1', '2', 'FlowPurchasePlanView', '1', '4', 'MOD20170822101010112_MOD20170728164235774', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728165420768', 'MOD20170728164235774', '采购合同申请', 'Purchase Contract Application', '1', '1', '2', 'FlowPurchaseContractView', '1', '5', 'MOD20170822101010112_MOD20170728164235774', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728165526382', 'MOD20170728164235774', '清关申请', 'Custom Clearance Application', '1', '1', '2', 'FlowCustomClearanceView', '1', '6', 'MOD20170822101010112_MOD20170728164235774', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728165631190', 'MOD20170728164235774', '供应商评级', 'Vendor Rating Application', '1', '1', '2', 'FlowVendorRatingView', '1', '7', 'MOD20170822101010112_MOD20170728164235774', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728165845110', 'MOD20170728164308465', '安检申请', 'Compliance Arrangement Application', '1', '1', '2', 'FlowComplianceArrangementView', '1', '1', 'MOD20170822101010112_MOD20170728164308465', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728165955200', 'MOD20170728164308465', '样品检验申请', 'Sample Inspection Application', '1', '1', '2', 'FlowSampleView', '1', '2', 'MOD20170822101010112_MOD20170728164308465', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728170210511', 'MOD20170728164308465', '订单质检申请', 'Order Quality Inspection', '1', '1', '2', 'FlowOrderQualityInspectionView', '1', '3', 'MOD20170822101010112_MOD20170728164308465', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728170324625', 'MOD20170728164308465', '产品认证申请', 'Product Certification application', '1', '1', '2', 'FlowProductCertificationView', '1', '4', 'MOD20170822101010112_MOD20170728164308465', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728170410976', 'MOD20170728164358580', '服务商询价', 'Service Inquiry', '1', '1', '2', 'FlowServiceInquiryView', '1', '1', 'MOD20170822101010112_MOD20170728164358580', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728170515877', 'MOD20170728164358580', '订单发货计划', 'Order Shipping Plan', '1', '1', '2', 'FlowOrderShippingPlanView', '1', '2', 'MOD20170822101010112_MOD20170728164358580', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728170605590', 'MOD20170728164358580', '订单发货确认', 'Order Shipping Confirmation', '1', '1', '2', 'FlowOrderShippingConfirmationView', '1', '3', 'MOD20170822101010112_MOD20170728164358580', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728170734273', 'MOD20170728164358580', '送仓计划', 'Warehouse Planning', '1', '1', '2', 'FlowWarehousePlanningView', '1', '4', 'MOD20170822101010112_MOD20170728164358580', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728170842711', 'MOD20170728164358580', '收货通知', 'Order Receiving Notice', '1', '1', '2', 'FlowOrderReceivingNoticeView', '1', '5', 'MOD20170822101010112_MOD20170728164358580', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728170946114', 'MOD20170728164508555', '样品付款申请', 'Sample Payment Application', '1', '1', '2', 'FlowSamplePaymentView', '1', '1', 'MOD20170822101010112_MOD20170728164508555', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728171054806', 'MOD20170728164508555', '合同订金申请', 'Deposit Contract Application', '1', '1', '2', 'FlowDepositContractView', '1', '2', 'MOD20170822101010112_MOD20170728164508555', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728171158161', 'MOD20170728164508555', '费用登记', 'Fee Registration Application', '1', '1', '2', 'FlowFeeRegistrationView', '1', '3', 'MOD20170822101010112_MOD20170728164508555', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728171245144', 'MOD20170728164508555', '费用支付申请', 'Fee Payment Application', '1', '1', '2', 'FlowFeePaymentView', '1', '4', 'MOD20170822101010112_MOD20170728164508555', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728171404882', 'MOD20170728164508555', '差额退款申请', 'Balance Refund Application', '1', '1', '2', 'FlowBalanceRefundView', '1', '5', 'MOD20170822101010112_MOD20170728164508555', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170728171536197', 'MOD20170728164508555', '收款账号变更', 'Account Change Application', '1', '1', '2', 'FlowAccountChangeView', '1', '6', 'MOD20170822101010112_MOD20170728164508555', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170731130442925', 'MOD20170731130503171', '工作流程a n', 'Workflows', '0', '1', '0', '', '1', '2', 'MOD20170731130503171', 'fa fa-fw fa-code-fork', null, '', null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170731130503171', '', '工作流程a1a', 'Workflows', '0', '0', '0', '', '1', '11', '', 'fa fa-fw fa-code-fork', null, '', null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170731135358288', 'MOD20170731130503171', '工作流程a1a', 'Workflows', '0', '0', '0', '', '1', '3', 'MOD20170731130503171', 'fa fa-fw fa-code-fork', null, '', null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170731135405565', 'MOD20170731130442925', '工作流程a n', 'Workflows', '0', '1', '0', '', '1', '1', 'MOD20170731130503171_MOD20170731130442925', 'fa fa-fw fa-code-fork', null, '', null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170822101010111', '', '我的桌面', 'My Desktop', '0', '1', '0', '', '1', '1', '', 'fa fa-fw fa-desktop', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170822101010112', '', '工作流程', 'Workflows', '0', '1', '0', '', '1', '6', '', 'fa fa-fw fa-code-fork', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170822101010113', '', '资料档案', 'Archives', '0', '1', '0', '', '1', '8', '', 'fa fa-fw fa-file-o', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170822101010114', '', '系统配置', 'System', '0', '1', '0', '', '1', '10', '', 'fa fa-fw fa-cogs', '2017-08-23 08:58:08', null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170822101010115', 'MOD20170822101010114', '模块配置', 'Modules', '0', '2', '0', '', '1', '4', 'MOD20170822101010114', '', '2017-08-23 08:57:07', null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20170822101010116', 'MOD20170822101010115', '模块管理', 'Modules-m', '1', '3', '2', 'ModulesView', '1', '1', 'MOD20170822101010114_MOD20170822101010115', 'fa fa-fw fa-cogs', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20178281233167803', 'MOD201782891459211', '发件箱', 'Sent Box', '1', '3', '2', 'EmailSentBoxView', '1', '4', 'MOD20170822101010111_MOD201782891459211', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD2017828911188948', 'MOD201782891374047', '产品特性通知', 'Product Speciality Notified', '1', '3', '2', 'ProductSpecialityNotifiedView', '1', '4', 'MOD20170822101010111_MOD201782891374047', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD201782891374047', 'MOD20170822101010111', '我的消息', 'My Message', '0', '2', '0', '', '1', '2', 'MOD20170822101010111', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD2017828914568504', 'MOD201782891459211', '收件箱', 'Inbox', '1', '3', '2', 'EmailInboxView', '1', '2', 'MOD20170822101010111_MOD201782891459211', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD201782891459211', 'MOD20170822101010111', '工作邮箱', 'My Job''s Email', '0', '2', '0', '', '1', '3', 'MOD20170822101010111', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD201782891621191', 'MOD201782891459211', '草稿箱', 'Drafts', '1', '3', '2', 'EmailDraftsView', '1', '1', 'MOD20170822101010111_MOD201782891459211', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD2017828918136327', 'MOD201782891459211', '垃圾箱', 'Dustbin', '1', '3', '2', 'EmailDustbinView', '1', '3', 'MOD20170822101010111_MOD201782891459211', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD2017828918585442', 'MOD201782891459211', '邮箱设置', 'Email Setting', '1', '3', '2', 'EmailSettingForm', '1', '5', 'MOD20170822101010111_MOD201782891459211', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD201782892232638', 'MOD201782891374047', '消息列表', 'My Message', '1', '3', '2', 'MessageView', '1', '1', 'MOD20170822101010111_MOD201782891374047', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD201782892546678', 'MOD201782891374047', '待办事项', 'My Events', '1', '3', '2', 'EventsView', '1', '2', 'MOD20170822101010111_MOD201782891374047', '', null, null, null);
INSERT INTO "public"."na_account_resource" VALUES ('MOD20178289441700', 'MOD201782891374047', '日程提醒', 'Schedule Reminder', '1', '3', '2', 'ScheduleView', '1', '3', 'MOD20170822101010111_MOD201782891374047', '', null, null, null);

-- ----------------------------
-- Table structure for na_account_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."na_account_role";
CREATE TABLE "public"."na_account_role" (
"id" varchar(20) COLLATE "default" DEFAULT nextval('na_account_role_id_seq'::regclass) NOT NULL,
"cn_name" varchar(50) COLLATE "default",
"en_name" varchar(50) COLLATE "default",
"sort" int2,
"status" int2,
"created_at" timestamp(6)
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of na_account_role
-- ----------------------------
INSERT INTO "public"."na_account_role" VALUES ('1', '系统管理员', 'System Admin', '1', '1', '2017-08-22 08:49:19');

-- ----------------------------
-- Table structure for na_account_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."na_account_user";
CREATE TABLE "public"."na_account_user" (
"id" varchar(20) COLLATE "default" DEFAULT nextval('na_account_user_id_seq'::regclass) NOT NULL,
"account" varchar(60) COLLATE "default",
"cn_name" varchar(12) COLLATE "default",
"en_name" varchar(50) COLLATE "default",
"extension" varchar(20) COLLATE "default",
"phone" varchar(100) COLLATE "default",
"qq" varchar(50) COLLATE "default",
"salt" varchar(20) COLLATE "default",
"skype" varchar(60) COLLATE "default",
"department_id" varchar(20) COLLATE "default",
"status" int2,
"created_at" timestamp(6),
"joinus_at" timestamp(6),
"language" char(3) COLLATE "default",
"set_list_rows" int2,
"email" varchar(100) COLLATE "default",
"password" varchar(60) COLLATE "default",
"gender" int2,
"wechat" varchar(100) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of na_account_user
-- ----------------------------
INSERT INTO "public"."na_account_user" VALUES ('1', 'admin', '系统管理员', 'admin', '2222', null, null, '7efbd59d9741d34f', null, 'DEP20180822000000111', '1', '2017-08-22 00:00:00', '2017-08-01 00:00:00', '   ', null, null, '691b14d79bf0fa2215f155235df5e670b64394cc', null, null);

-- ----------------------------
-- Table structure for na_account_user_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."na_account_user_role";
CREATE TABLE "public"."na_account_user_role" (
"user_id" varchar(20) COLLATE "default" NOT NULL,
"role_id" varchar(20) COLLATE "default" NOT NULL,
"id" varchar(20) COLLATE "default" NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of na_account_user_role
-- ----------------------------

-- ----------------------------
-- Table structure for na_dictionary
-- ----------------------------
DROP TABLE IF EXISTS "public"."na_dictionary";
CREATE TABLE "public"."na_dictionary" (
"id" varchar(20) COLLATE "default" NOT NULL,
"code_main" varchar(64) COLLATE "default",
"code_sub" varchar(64) COLLATE "default",
"category_id" varchar(20) COLLATE "default",
"sort" int2,
"status" int2,
"custom" int2
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of na_dictionary
-- ----------------------------

-- ----------------------------
-- Table structure for na_dictionary_category
-- ----------------------------
DROP TABLE IF EXISTS "public"."na_dictionary_category";
CREATE TABLE "public"."na_dictionary_category" (
"id" varchar(20) COLLATE "default" NOT NULL,
"status" int2,
"sort" int2
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of na_dictionary_category
-- ----------------------------

-- ----------------------------
-- Table structure for na_dictionary_category_desc
-- ----------------------------
DROP TABLE IF EXISTS "public"."na_dictionary_category_desc";
CREATE TABLE "public"."na_dictionary_category_desc" (
"id" varchar(20) COLLATE "default" NOT NULL,
"category_id" varchar(20) COLLATE "default",
"title" varchar(100) COLLATE "default",
"lang" varchar(5) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of na_dictionary_category_desc
-- ----------------------------

-- ----------------------------
-- Table structure for na_dictionary_desc
-- ----------------------------
DROP TABLE IF EXISTS "public"."na_dictionary_desc";
CREATE TABLE "public"."na_dictionary_desc" (
"id" varchar(20) COLLATE "default" NOT NULL,
"dict_id" varchar(20) COLLATE "default",
"name" varchar(100) COLLATE "default",
"context" text COLLATE "default",
"lang" varchar(5) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of na_dictionary_desc
-- ----------------------------

-- ----------------------------
-- Table structure for na_dictionary_value
-- ----------------------------
DROP TABLE IF EXISTS "public"."na_dictionary_value";
CREATE TABLE "public"."na_dictionary_value" (
"id" varchar(20) COLLATE "default" NOT NULL,
"dict_id" varchar(20) COLLATE "default",
"value" varchar(64) COLLATE "default",
"sort" int2,
"status" int2,
"is_default" int2,
"custom" int2
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of na_dictionary_value
-- ----------------------------

-- ----------------------------
-- Table structure for na_dictionary_value_desc
-- ----------------------------
DROP TABLE IF EXISTS "public"."na_dictionary_value_desc";
CREATE TABLE "public"."na_dictionary_value_desc" (
"id" varchar(20) COLLATE "default" NOT NULL,
"dict_value_id" varchar(20) COLLATE "default",
"text" text COLLATE "default",
"lang" varchar(5) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of na_dictionary_value_desc
-- ----------------------------

-- ----------------------------
-- View structure for na_view_dictionary_category
-- ----------------------------
CREATE OR REPLACE VIEW "public"."na_view_dictionary_category" AS 
 SELECT na_dictionary_category.id,
    na_dictionary_category.status,
    na_dictionary_category.sort,
    na_dictionary_category_desc.title,
    na_dictionary_category_desc.lang
   FROM na_dictionary_category,
    na_dictionary_category_desc
  WHERE ((na_dictionary_category.id)::text = (na_dictionary_category_desc.category_id)::text);

-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------
ALTER SEQUENCE "public"."na_account_department_id_seq" OWNED BY "na_account_department"."id";
ALTER SEQUENCE "public"."na_account_resource_id_seq" OWNED BY "na_account_resource"."id";
ALTER SEQUENCE "public"."na_account_role_id_seq" OWNED BY "na_account_role"."id";
ALTER SEQUENCE "public"."na_account_user_id_seq" OWNED BY "na_account_user"."id";

-- ----------------------------
-- Indexes structure for table na_account_department
-- ----------------------------
CREATE INDEX "na_account_department_level_idx" ON "public"."na_account_department" USING btree ("level");
CREATE INDEX "na_account_department_leaf_idx" ON "public"."na_account_department" USING btree ("leaf");
CREATE INDEX "na_account_department_status_idx" ON "public"."na_account_department" USING btree ("status");
CREATE INDEX "na_account_department_created_at_idx" ON "public"."na_account_department" USING btree ("created_at");
CREATE INDEX "na_account_department_en_name_idx" ON "public"."na_account_department" USING btree ("en_name");
CREATE INDEX "na_account_department_id_idx" ON "public"."na_account_department" USING btree ("id");

-- ----------------------------
-- Uniques structure for table na_account_department
-- ----------------------------
ALTER TABLE "public"."na_account_department" ADD UNIQUE ("id");

-- ----------------------------
-- Primary Key structure for table na_account_department
-- ----------------------------
ALTER TABLE "public"."na_account_department" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table na_account_resource
-- ----------------------------
CREATE INDEX "na_account_resource_leaf_idx" ON "public"."na_account_resource" USING btree ("leaf");
CREATE INDEX "na_account_resource_level_idx" ON "public"."na_account_resource" USING btree ("level");
CREATE INDEX "na_account_resource_type_idx" ON "public"."na_account_resource" USING btree ("type");
CREATE INDEX "na_account_resource_status_idx" ON "public"."na_account_resource" USING btree ("status");
CREATE UNIQUE INDEX "na_account_resource_id_idx" ON "public"."na_account_resource" USING btree ("id");

-- ----------------------------
-- Uniques structure for table na_account_resource
-- ----------------------------
ALTER TABLE "public"."na_account_resource" ADD UNIQUE ("id");

-- ----------------------------
-- Primary Key structure for table na_account_resource
-- ----------------------------
ALTER TABLE "public"."na_account_resource" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table na_account_role
-- ----------------------------
CREATE UNIQUE INDEX "na_account_role_id_idx" ON "public"."na_account_role" USING btree ("id");

-- ----------------------------
-- Uniques structure for table na_account_role
-- ----------------------------
ALTER TABLE "public"."na_account_role" ADD UNIQUE ("id");

-- ----------------------------
-- Primary Key structure for table na_account_role
-- ----------------------------
ALTER TABLE "public"."na_account_role" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table na_account_user
-- ----------------------------
CREATE INDEX "na_account_user_status_idx" ON "public"."na_account_user" USING btree ("status");
CREATE UNIQUE INDEX "na_account_user_id_idx" ON "public"."na_account_user" USING btree ("id");
CREATE INDEX "na_account_user_account_idx" ON "public"."na_account_user" USING btree ("account");

-- ----------------------------
-- Uniques structure for table na_account_user
-- ----------------------------
ALTER TABLE "public"."na_account_user" ADD UNIQUE ("id");

-- ----------------------------
-- Primary Key structure for table na_account_user
-- ----------------------------
ALTER TABLE "public"."na_account_user" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table na_account_user_role
-- ----------------------------
CREATE UNIQUE INDEX "na_account_user_role_id_idx" ON "public"."na_account_user_role" USING btree ("id");

-- ----------------------------
-- Primary Key structure for table na_account_user_role
-- ----------------------------
ALTER TABLE "public"."na_account_user_role" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table na_dictionary
-- ----------------------------
CREATE UNIQUE INDEX "na_dictionary_id_idx" ON "public"."na_dictionary" USING btree ("id");
CREATE INDEX "na_dictionary_sort_idx" ON "public"."na_dictionary" USING btree ("sort");
CREATE INDEX "na_dictionary_status_idx" ON "public"."na_dictionary" USING btree ("status");
CREATE INDEX "na_dictionary_is_custom_idx" ON "public"."na_dictionary" USING btree ("custom");
CREATE INDEX "na_dictionary_code_main_idx" ON "public"."na_dictionary" USING btree ("code_main");

-- ----------------------------
-- Primary Key structure for table na_dictionary
-- ----------------------------
ALTER TABLE "public"."na_dictionary" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table na_dictionary_category
-- ----------------------------
CREATE INDEX "na_dictionary_category_id_idx" ON "public"."na_dictionary_category" USING btree ("id");
CREATE INDEX "na_dictionary_category_sort_idx" ON "public"."na_dictionary_category" USING btree ("sort");
CREATE INDEX "na_dictionary_category_status_idx" ON "public"."na_dictionary_category" USING btree ("status");

-- ----------------------------
-- Primary Key structure for table na_dictionary_category
-- ----------------------------
ALTER TABLE "public"."na_dictionary_category" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table na_dictionary_category_desc
-- ----------------------------
CREATE INDEX "na_dictionary_category_desc_id_idx" ON "public"."na_dictionary_category_desc" USING btree ("id");
CREATE INDEX "na_dictionary_category_desc_dict_id_idx" ON "public"."na_dictionary_category_desc" USING btree ("category_id");

-- ----------------------------
-- Primary Key structure for table na_dictionary_category_desc
-- ----------------------------
ALTER TABLE "public"."na_dictionary_category_desc" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table na_dictionary_desc
-- ----------------------------
CREATE INDEX "na_dictionary_desc_id_idx" ON "public"."na_dictionary_desc" USING btree ("id");
CREATE INDEX "na_dictionary_desc_dict_id_idx" ON "public"."na_dictionary_desc" USING btree ("dict_id");
CREATE INDEX "na_dictionary_desc_lang_idx" ON "public"."na_dictionary_desc" USING btree ("lang");

-- ----------------------------
-- Primary Key structure for table na_dictionary_desc
-- ----------------------------
ALTER TABLE "public"."na_dictionary_desc" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table na_dictionary_value
-- ----------------------------
CREATE INDEX "na_dictionary_item_id_idx" ON "public"."na_dictionary_value" USING btree ("id");
CREATE INDEX "na_dictionary_item_sort_idx" ON "public"."na_dictionary_value" USING btree ("sort");
CREATE INDEX "na_dictionary_item_status_idx" ON "public"."na_dictionary_value" USING btree ("status");
CREATE INDEX "na_dictionary_item_default_idx" ON "public"."na_dictionary_value" USING btree ("is_default");
CREATE INDEX "na_dictionary_item_sub_code_idx" ON "public"."na_dictionary_value" USING btree ("dict_id");

-- ----------------------------
-- Primary Key structure for table na_dictionary_value
-- ----------------------------
ALTER TABLE "public"."na_dictionary_value" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table na_dictionary_value_desc
-- ----------------------------
ALTER TABLE "public"."na_dictionary_value_desc" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Key structure for table "public"."na_account_user"
-- ----------------------------
ALTER TABLE "public"."na_account_user" ADD FOREIGN KEY ("department_id") REFERENCES "public"."na_account_department" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- ----------------------------
-- Foreign Key structure for table "public"."na_account_user_role"
-- ----------------------------
ALTER TABLE "public"."na_account_user_role" ADD FOREIGN KEY ("role_id") REFERENCES "public"."na_account_role" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "public"."na_account_user_role" ADD FOREIGN KEY ("user_id") REFERENCES "public"."na_account_user" ("id") ON DELETE RESTRICT ON UPDATE CASCADE;
