/*
Navicat MySQL Data Transfer

Source Server         : localhost_mysql
Source Server Version : 50617
Source Host           : localhost:3306
Source Database       : auto_test_demo1

Target Server Type    : MYSQL
Target Server Version : 50617
File Encoding         : 65001

Date: 2018-10-07 22:04:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for na_account_department
-- ----------------------------
DROP TABLE IF EXISTS `na_account_department`;
CREATE TABLE `na_account_department` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `parent_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '父ID',
  `cn_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '中文名',
  `en_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '英文名',
  `level` smallint(6) DEFAULT NULL COMMENT '层级',
  `leaf` smallint(6) DEFAULT NULL COMMENT '叶子',
  `status` smallint(6) DEFAULT NULL COMMENT '状态',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `sort` bigint(20) DEFAULT NULL COMMENT '排序号',
  `path` text COLLATE utf8_unicode_ci COMMENT '路径',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_account_department_created_at_idx` (`created_at`) USING BTREE,
  KEY `na_account_department_en_name_idx` (`en_name`) USING BTREE,
  KEY `na_account_department_id_idx` (`id`) USING BTREE,
  KEY `na_account_department_leaf_idx` (`leaf`) USING BTREE,
  KEY `na_account_department_level_idx` (`level`) USING BTREE,
  KEY `na_account_department_status_idx` (`status`) USING BTREE,
  KEY `fkgbsyk8g04hxa4x5gf2db5op32` (`parent_id`) USING BTREE,
  CONSTRAINT `fkgbsyk8g04hxa4x5gf2db5op32` FOREIGN KEY (`parent_id`) REFERENCES `na_account_department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_account_department
-- ----------------------------
INSERT INTO `na_account_department` VALUES ('DEP201806231402474892534', 'DEP20180822000000110', '中国IT', 'CN IT', '0', '0', '1', '2018-07-23 14:02:47', '2', 'DEP20180822000000110');
INSERT INTO `na_account_department` VALUES ('DEP201806231402567686999', 'DEP201806231402474892534', '测试组', 'Test Team', '1', '0', '1', '2018-07-23 14:02:56', '1', 'DEP20180822000000110_DEP201806231402474892534');
INSERT INTO `na_account_department` VALUES ('DEP201806231409223387225', 'DEP20180822000000110', '澳洲IT', 'AI IT', '0', '0', '1', '2018-07-23 14:09:22', '3', 'DEP20180822000000110_DEP20180822000000110');
INSERT INTO `na_account_department` VALUES ('DEP201806231416249721161', 'DEP201806231402474892534', 'PHP组', 'PHP Team', '0', '0', '1', '2018-07-23 14:16:24', '2', 'DEP20180822000000110_DEP201806231402474892534');
INSERT INTO `na_account_department` VALUES ('DEP201806231416417957360', 'DEP201806231402474892534', 'JAVA组', 'JAVA Team', '0', '0', '1', '2018-07-23 14:16:41', '3', 'DEP20180822000000110_DEP201806231402474892534');
INSERT INTO `na_account_department` VALUES ('DEP201806231417136618464', 'DEP201806231402474892534', '系统组', 'System Team', '0', '0', '1', '2018-07-23 14:17:13', '4', 'DEP20180822000000110_DEP201806231402474892534');
INSERT INTO `na_account_department` VALUES ('DEP20180822000000110', null, 'Newaim', 'Newaim', '1', '0', '1', null, '1', null);

-- ----------------------------
-- Table structure for na_account_resource
-- ----------------------------
DROP TABLE IF EXISTS `na_account_resource`;
CREATE TABLE `na_account_resource` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `parent_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cn_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `en_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `leaf` smallint(6) DEFAULT NULL,
  `level` smallint(6) DEFAULT NULL,
  `type` smallint(6) DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL,
  `sort` smallint(6) DEFAULT NULL,
  `item_icon` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `function` longtext COLLATE utf8_unicode_ci,
  `path` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_account_resource_code_idx` (`code`) USING BTREE,
  KEY `na_account_resource_id_idx` (`id`) USING BTREE,
  KEY `na_account_resource_leaf_idx` (`leaf`) USING BTREE,
  KEY `na_account_resource_level_idx` (`level`) USING BTREE,
  KEY `na_account_resource_parent_id_idx` (`parent_id`) USING BTREE,
  KEY `na_account_resource_status_idx` (`status`) USING BTREE,
  KEY `na_account_resource_type_idx` (`type`) USING BTREE,
  CONSTRAINT `fk19td5hc7v3reptwhys5dtjk6j` FOREIGN KEY (`parent_id`) REFERENCES `na_account_resource` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_account_resource
-- ----------------------------
INSERT INTO `na_account_resource` VALUES ('MOD20170728155843430', 'MOD20170822101010114', '用户级权限', 'User & Role', '0', '2', '0', '', '1', '1', '', '2017-08-23 08:58:08', null, null, null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728155938744', 'MOD20170728155843430', '用户账号', 'User Account', '1', '1', '2', 'AccountUserView', '1', '1', null, '2017-08-23 08:58:08', 'User', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728160112776', 'MOD20170728155843430', '组织架构', 'Department', '1', '1', '2', 'AccountDepartmentView', '1', '6', null, '2017-08-23 08:58:08', 'Department', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728160137853', 'MOD20170728155843430', '角色管理', 'Role', '1', '1', '2', 'AccountRoleView', '1', '5', null, '2017-08-23 08:58:08', 'Role', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728160248143', 'MOD20170728155843430', '角色权限', 'Role Permission', '1', '1', '2', 'AccountPermissionView', '1', '4', null, '2017-08-23 08:58:08', 'RolePermission', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728160348129', 'MOD20170822101010114', '数据字典', 'Data Dictionary', '0', '0', '0', '', '1', '2', '', '2017-08-23 08:58:08', '', '', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728160507765', 'MOD20170728160348129', '字典分类', 'Dictionary Category', '1', '1', '2', 'DataDictionaryCategoryView', '1', '1', null, '2017-08-23 08:58:08', 'DictionaryCategory', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728160556818', 'MOD20170728160348129', '字典管理', 'Dictionary Item', '1', '1', '2', 'DataDictionaryManageView', '1', '2', null, '2017-08-23 08:58:08', 'Dictionary', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728160751128', 'MOD20170822101010114', '系统设置', 'System Setting', '0', '0', '0', '', '1', '4', '', '2017-08-23 08:58:08', '', '', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728160921443', 'MOD20170728160751128', '定时器设置', 'Alert Setting', '1', '1', '2', 'AlertSettingView', '1', '1', '', '2017-08-23 08:58:08', 'AlertSetting', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728161037934', 'MOD20170728160751128', '常用配置项目', 'Configurations', '1', '1', '2', 'CommonConfigurationItemsView', '2', '2', '', '2017-08-23 08:58:08', 'CommonConfiguration', '[[\"normal|list\",\"normal|edit\"],[],[\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728161313432', 'MOD20170822101010111', '文件管理', 'Files', '0', '2', '0', null, '1', '9', null, null, null, null, null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728162514871', 'MOD20170728161313432', '业务模板', 'Templates', '1', '1', '2', 'MyTemplateView', '2', '1', null, null, 'MyTemplate', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728162548115', 'MOD20170728161313432', '我的文件', 'Document', '1', '1', '2', 'MyDocumentView', '1', '2', null, null, 'MyDocument', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728162921619', 'MOD20170822101010113', '脚本管理', 'Script Mange', '0', '1', '0', '', '1', '1', '', null, '', null, null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728163627238', 'MOD20170728162921619', '项目实例', 'Project Instance', '1', '1', '2', 'ProjectsInstanceView', '1', '5', '', null, 'ProjectsInstance', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728164106927', 'MOD20170728162921619', '用例脚本', 'Case', '1', '0', '2', 'CaseView', '1', '2', '', null, 'Case', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728164136213', 'MOD20170728162921619', '用例数据', 'Case Data', '1', '0', '2', 'CaseDataView', '2', '3', '', null, 'Service', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728164358580', 'MOD20170822101010112', '执行脚本', 'Project Script', '0', '0', '0', '', '1', '3', '', null, '', null, null);
INSERT INTO `na_account_resource` VALUES ('MOD20170728170410976', 'MOD20170728164358580', '执行用例组', 'Run Case Group', '1', '1', '2', 'RunCaseGroupView', '1', '2', '', null, 'RunCaseGroup', '[[\"normal|list\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170806113105545', 'MOD20170728160348129', '字典值管理', 'Dictionary Value', '1', '1', '2', 'DataDictionaryValueView', '1', '3', null, '2017-08-23 08:58:08', 'DictionaryValue', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170822010350641', 'MOD20170728155843430', '角色分配', 'Role Assignment', '1', '1', '2', 'AccountRoleAssignView', '1', '3', null, '2017-09-22 01:03:50', 'RoleAssign', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170822101010111', null, '工作面板', 'Desktop', '0', '1', '0', null, '1', '1', 'fa fa-fw fa-desktop', null, null, null, null);
INSERT INTO `na_account_resource` VALUES ('MOD20170822101010112', null, '执行脚本', 'Script Run', '0', '1', '0', '', '1', '6', 'fa fa-fw fa-code-fork', null, '', null, null);
INSERT INTO `na_account_resource` VALUES ('MOD20170822101010113', null, '脚本管理', 'Script Info', '0', '1', '0', '', '1', '8', 'fa fa-fw fa-file-o', null, '', null, null);
INSERT INTO `na_account_resource` VALUES ('MOD20170822101010114', null, '系统配置', 'System', '0', '1', '0', null, '1', '10', 'fa fa-fw fa-cogs', '2017-08-23 08:58:08', null, '[[\"normal|normal\",\"normal|add\",\"normal|edit\",\"normal|del\",\"normal|back\"],[\"flow|start\",\"flow|allow\"],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170822101010115', 'MOD20170822101010114', '模块配置', 'Modules', '0', '2', '0', '', '1', '5', '', '2017-08-23 08:57:07', '', '', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170822101010116', 'MOD20170822101010115', '模块管理', 'Modules', '1', '3', '2', 'ModulesView', '1', '1', 'fa fa-fw fa-cogs', null, 'Modules', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170828084638684', 'MOD20170728155843430', '用户角色', 'User Roles', '1', '1', '2', 'AccountUserRoleView', '1', '2', null, '2017-09-28 08:46:39', 'UserRole', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20170912073810640', 'MOD20170728160348129', '附件文件分类', 'Attachment Category', '1', '1', '2', 'MyDocumentCategoryView', '1', '4', null, '2017-10-12 07:38:10', 'MyDocumentCategory', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD201709300903291469368', 'MOD201782891459211', '联系人', 'Contacts', '1', '1', '2', 'ContactsView', '1', '7', null, '2017-10-30 09:03:29', 'Contacts', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20178281233167803', 'MOD201782891459211', '已发件', 'Sent Box', '1', '3', '2', 'EmailSentBoxView', '1', '3', null, null, 'Email', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD201782891374047', 'MOD20170822101010111', '我的消息', 'Message', '0', '2', '0', null, '1', '2', null, null, null, null, null);
INSERT INTO `na_account_resource` VALUES ('MOD2017828914568504', 'MOD201782891459211', '收件箱', 'Inbox', '1', '3', '2', 'EmailInboxView', '1', '1', null, null, 'Email', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD201782891459211', 'MOD20170822101010111', '工作邮箱', 'Email', '0', '2', '0', null, '1', '4', null, null, null, null, null);
INSERT INTO `na_account_resource` VALUES ('MOD201782891621191', 'MOD201782891459211', '草稿箱', 'Drafts', '1', '3', '2', 'EmailDraftsView', '1', '4', null, null, 'Email', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD2017828918136327', 'MOD201782891459211', '垃圾箱', 'Trash', '1', '3', '2', 'EmailDustbinView', '1', '5', null, null, 'Email', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD2017828918585442', 'MOD201782891459211', '邮箱设置', 'Settings', '1', '3', '2', 'EmailSettingView', '1', '6', null, null, 'EmailSetting', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD201782892232638', 'MOD201782891374047', '消息列表', 'Message', '1', '3', '2', 'MessageView', '1', '1', null, null, 'Message', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD201782892546678', 'MOD201782891374047', '待办事项', 'Task', '1', '3', '2', 'EventsView', '1', '2', null, null, 'Events', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD20178289441700', 'MOD201782891374047', '日程提醒', 'Schedule Reminder', '1', '3', '2', 'ScheduleView', '2', '5', null, null, 'Schedule', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('MOD201801270551331501217', 'MOD20170728160348129', '系统帮助', 'System Help', '1', '1', '2', 'HelpView', '1', '5', null, '2018-02-27 05:51:34', 'Help', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('RES201806191542466545857', 'MOD20170728162921619', '用例模块', 'Case Category', '1', '1', '2', 'CaseCategoryView', '1', '4', '', '2018-07-19 15:42:46', 'CaseCategory', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', 'MOD20170728162921619');
INSERT INTO `na_account_resource` VALUES ('RES201806191628061418411', 'MOD20170728164358580', '执行日志', 'Run Logs', '1', '0', '2', 'RunLogsView', '1', '3', '', '2018-07-19 16:28:06', 'RunLogs', '[[\"normal|list\",\"normal|del\",\"normal|export\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('RES201806191629275174758', 'MOD20170728164358580', '执行报告', 'Run Report', '1', '0', '2', 'RunReportView', '2', '4', null, '2018-07-19 16:29:27', 'RunReport', '[[\"normal|list\",\"normal|export\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('RES201807051155433340411', 'MOD20170728162921619', '项目管理', 'Projects', '1', '1', '2', 'ProjectsView', '1', '6', null, '2018-08-05 11:55:43', 'Projects', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('RES201807051401071611668', 'MOD20170728162921619', '用例群组', 'Case Group', '1', '1', '2', 'CaseGroupView', '1', '1', null, '2018-08-05 14:01:07', 'CaseGroup', '[[\"normal|list\",\"normal|add\",\"normal|edit\",\"normal|del\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);
INSERT INTO `na_account_resource` VALUES ('RES201807051407255803339', 'MOD20170728164358580', '执行项目', 'Run Projects', '1', '1', '2', 'RunProjectsView', '1', '1', null, '2018-08-05 14:07:25', 'RunProjects', '[[\"normal|list\",\"normal|run\",\"normal|stop\"],[],[\"data|data\",\"data|1\",\"data|2\",\"data|3\",\"data|4\"]]', null);

-- ----------------------------
-- Table structure for na_account_role
-- ----------------------------
DROP TABLE IF EXISTS `na_account_role`;
CREATE TABLE `na_account_role` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `cn_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `en_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sort` smallint(6) DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_account_role_id_idx` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_account_role
-- ----------------------------
INSERT INTO `na_account_role` VALUES ('1', '系统管理员', 'System Admin', '100', '1', '2017-09-12 19:44:13', 'admin');
INSERT INTO `na_account_role` VALUES ('2', '测试组长', 'Test Leader', '5', '1', '2017-08-22 08:49:19', 'TestLeader');
INSERT INTO `na_account_role` VALUES ('3', '全局观察者', 'Global observer', '1', '1', '2018-01-05 15:08:23', 'Globalobserver');
INSERT INTO `na_account_role` VALUES ('4', '项目管理', 'Project Manager', '27', '1', '2017-10-31 03:01:44', 'ProjectManager');
INSERT INTO `na_account_role` VALUES ('5', '测试工程师', 'Tester', '2', '1', '2017-10-25 08:07:37', 'Tester');
INSERT INTO `na_account_role` VALUES ('6', '开发工程师', 'Developer', '3', '1', '2017-10-26 13:42:04', 'Developer');

-- ----------------------------
-- Table structure for na_account_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `na_account_role_resource`;
CREATE TABLE `na_account_role_resource` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `role_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `resource_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `action` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `data` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `model` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `path` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_account_role_resource_resource_id_idx` (`resource_id`) USING BTREE,
  KEY `na_account_role_resource_role_id_idx` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_account_role_resource
-- ----------------------------
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181022912', '2', 'RES201806191542466545857', null, '1', 'CaseCategory', 'MOD20170728162921619_MOD20170822101010113_RES201806191542466545857');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181198972', '2', 'MOD2017828918585442', 'normal:del', null, 'EmailSetting', 'MOD201782891459211_MOD20170822101010111_MOD2017828918585442');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181283956', '2', 'RES201807051401071611668', 'normal:del', null, 'CaseGroup', 'MOD20170728162921619_MOD20170822101010113_RES201807051401071611668');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181299616', '2', 'RES201807051155433340411', 'normal:edit', null, 'Projects', 'MOD20170728162921619_MOD20170822101010113_RES201807051155433340411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181378637', '2', 'MOD20170728164106927', 'normal:normal', null, 'Case', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164106927');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181503415', '2', 'RES201807051401071611668', 'normal:edit', null, 'CaseGroup', 'MOD20170728162921619_MOD20170822101010113_RES201807051401071611668');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181508364', '2', 'MOD201782891621191', null, '1', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD201782891621191');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181558383', '2', 'MOD20170728164136213', null, '2', 'Service', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164136213');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181671948', '2', 'MOD201782892232638', 'normal:del', null, 'Message', 'MOD201782891374047_MOD20170822101010111_MOD201782892232638');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181691163', '2', 'MOD20170728162548115', null, '1', 'MyDocument', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162548115');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181784543', '2', 'RES201806191629275174758', null, '2', 'RunReport', 'MOD20170728164358580_MOD20170822101010112_RES201806191629275174758');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181788230', '2', 'MOD2017828918585442', null, '1', 'EmailSetting', 'MOD201782891459211_MOD20170822101010111_MOD2017828918585442');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181825552', '2', 'MOD20170728170410976', null, '1', 'RunCaseGroup', 'MOD20170728164358580_MOD20170822101010112_MOD20170728170410976');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181846515', '2', 'MOD201782892232638', 'normal:add', null, 'Message', 'MOD201782891374047_MOD20170822101010111_MOD201782892232638');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181899785', '2', 'MOD2017828918585442', 'normal:list', null, 'EmailSetting', 'MOD201782891459211_MOD20170822101010111_MOD2017828918585442');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181903692', '2', 'MOD20178281233167803', 'normal:del', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD20178281233167803');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181916548', '2', 'MOD20170728164136213', 'normal:edit', null, 'Service', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164136213');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181921504', '2', 'MOD20178281233167803', 'normal:add', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD20178281233167803');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181923466', '2', 'RES201806191629275174758', 'normal:normal', null, 'RunReport', 'MOD20170728164358580_MOD20170822101010112_RES201806191629275174758');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181923597', '2', 'MOD201709300903291469368', 'normal:normal', null, 'Contacts', 'MOD201782891459211_MOD20170822101010111_MOD201709300903291469368');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409181970316', '2', 'MOD20170728162548115', 'normal:normal', null, 'MyDocument', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162548115');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409182031988', '2', 'MOD2017828914568504', 'normal:del', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828914568504');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409182032376', '2', 'MOD201782891621191', 'normal:add', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD201782891621191');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409182048926', '2', 'MOD2017828914568504', null, '1', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828914568504');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409182157239', '2', 'MOD201782891621191', 'normal:list', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD201782891621191');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409182288992', '2', 'MOD20170728162514871', 'normal:list', null, 'MyTemplate', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162514871');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409182337290', '2', 'MOD20170728162548115', null, '2', 'MyDocument', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162548115');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409182369274', '2', 'MOD20170728164106927', null, '2', 'Case', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164106927');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409182391917', '2', 'MOD20178281233167803', 'normal:edit', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD20178281233167803');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409182568861', '2', 'RES201807051155433340411', 'normal:add', null, 'Projects', 'MOD20170728162921619_MOD20170822101010113_RES201807051155433340411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409182617874', '2', 'MOD201782892232638', 'normal:edit', null, 'Message', 'MOD201782891374047_MOD20170822101010111_MOD201782892232638');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409182691609', '2', 'MOD20170728163627238', 'normal:edit', null, 'ProjectsInstance', 'MOD20170728162921619_MOD20170822101010113_MOD20170728163627238');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409182796888', '2', 'MOD20170728163627238', 'normal:list', null, 'ProjectsInstance', 'MOD20170728162921619_MOD20170822101010113_MOD20170728163627238');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409182869133', '2', 'MOD2017828918585442', 'normal:normal', null, 'EmailSetting', 'MOD201782891459211_MOD20170822101010111_MOD2017828918585442');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409182882397', '2', 'RES201807051401071611668', 'normal:normal', null, 'CaseGroup', 'MOD20170728162921619_MOD20170822101010113_RES201807051401071611668');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409182904666', '2', 'MOD201782891621191', 'normal:normal', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD201782891621191');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409182960506', '2', 'MOD201782891621191', 'normal:edit', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD201782891621191');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409183114189', '2', 'MOD20170728163627238', 'normal:normal', null, 'ProjectsInstance', 'MOD20170728162921619_MOD20170822101010113_MOD20170728163627238');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409183196635', '2', 'RES201806191628061418411', 'normal:normal', null, 'RunLogs', 'MOD20170728164358580_MOD20170822101010112_RES201806191628061418411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409183332066', '2', 'RES201806191629275174758', 'normal:list', null, 'RunReport', 'MOD20170728164358580_MOD20170822101010112_RES201806191629275174758');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409183424468', '2', 'MOD2017828918136327', 'normal:list', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828918136327');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409183531428', '2', 'MOD20170728163627238', 'normal:add', null, 'ProjectsInstance', 'MOD20170728162921619_MOD20170822101010113_MOD20170728163627238');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409183711683', '2', 'RES201807051155433340411', 'normal:list', null, 'Projects', 'MOD20170728162921619_MOD20170822101010113_RES201807051155433340411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409183801696', '2', 'MOD201709300903291469368', 'normal:edit', null, 'Contacts', 'MOD201782891459211_MOD20170822101010111_MOD201709300903291469368');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409183807307', '2', 'MOD2017828914568504', 'normal:normal', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828914568504');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409183957614', '2', 'RES201807051155433340411', 'normal:del', null, 'Projects', 'MOD20170728162921619_MOD20170822101010113_RES201807051155433340411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409183958253', '2', 'MOD20170728163627238', null, '1', 'ProjectsInstance', 'MOD20170728162921619_MOD20170822101010113_MOD20170728163627238');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409184032401', '2', 'MOD20170728164106927', 'normal:list', null, 'Case', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164106927');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409184189481', '2', 'MOD2017828914568504', 'normal:list', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828914568504');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409184223610', '2', 'MOD201782892232638', 'normal:normal', null, 'Message', 'MOD201782891374047_MOD20170822101010111_MOD201782892232638');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409184223957', '2', 'MOD201709300903291469368', 'normal:list', null, 'Contacts', 'MOD201782891459211_MOD20170822101010111_MOD201709300903291469368');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409184306006', '2', 'MOD201782891621191', 'normal:del', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD201782891621191');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409184373489', '2', 'MOD20170728164136213', null, '1', 'Service', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164136213');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409184569482', '2', 'RES201807051155433340411', null, '1', 'Projects', 'MOD20170728162921619_MOD20170822101010113_RES201807051155433340411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409184786376', '2', 'RES201807051155433340411', 'normal:normal', null, 'Projects', 'MOD20170728162921619_MOD20170822101010113_RES201807051155433340411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409184821767', '2', 'RES201806191629275174758', 'normal:export', null, 'RunReport', 'MOD20170728164358580_MOD20170822101010112_RES201806191629275174758');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185044192', '2', 'MOD20170728162548115', 'normal:edit', null, 'MyDocument', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162548115');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185048164', '2', 'MOD2017828918136327', null, '1', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828918136327');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185109166', '2', 'RES201807051401071611668', null, '2', 'CaseGroup', 'MOD20170728162921619_MOD20170822101010113_RES201807051401071611668');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185212657', '2', 'MOD20170728164136213', 'normal:list', null, 'Service', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164136213');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185307297', '2', 'MOD20170728164106927', 'normal:edit', null, 'Case', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164106927');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185316791', '2', 'MOD201709300903291469368', 'normal:add', null, 'Contacts', 'MOD201782891459211_MOD20170822101010111_MOD201709300903291469368');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185404945', '2', 'MOD20178281233167803', 'normal:normal', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD20178281233167803');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185476269', '2', 'RES201806191542466545857', 'normal:normal', null, 'CaseCategory', 'MOD20170728162921619_MOD20170822101010113_RES201806191542466545857');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185546183', '2', 'MOD20178281233167803', null, '1', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD20178281233167803');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185607937', '2', 'MOD20170728170410976', 'normal:normal', null, 'RunCaseGroup', 'MOD20170728164358580_MOD20170822101010112_MOD20170728170410976');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185642313', '2', 'MOD20170728162548115', 'normal:del', null, 'MyDocument', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162548115');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185659181', '2', 'MOD20170728162514871', 'normal:edit', null, 'MyTemplate', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162514871');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185691443', '2', 'RES201807051401071611668', 'normal:add', null, 'CaseGroup', 'MOD20170728162921619_MOD20170822101010113_RES201807051401071611668');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185753692', '2', 'RES201806191628061418411', null, '2', 'RunLogs', 'MOD20170728164358580_MOD20170822101010112_RES201806191628061418411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185772247', '2', 'MOD20170728164136213', 'normal:del', null, 'Service', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164136213');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185811301', '2', 'MOD2017828914568504', 'normal:edit', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828914568504');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185822548', '2', 'MOD20170728160112776', 'normal:list', null, 'Department', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160112776');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185911023', '2', 'RES201806191542466545857', 'normal:del', null, 'CaseCategory', 'MOD20170728162921619_MOD20170822101010113_RES201806191542466545857');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409185933528', '2', 'MOD201782892232638', 'normal:list', null, 'Message', 'MOD201782891374047_MOD20170822101010111_MOD201782892232638');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409186014242', '2', 'MOD20170728170410976', null, '2', 'RunCaseGroup', 'MOD20170728164358580_MOD20170822101010112_MOD20170728170410976');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409186188674', '2', 'RES201806191542466545857', 'normal:list', null, 'CaseCategory', 'MOD20170728162921619_MOD20170822101010113_RES201806191542466545857');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409186263266', '2', 'RES201806191628061418411', 'normal:list', null, 'RunLogs', 'MOD20170728164358580_MOD20170822101010112_RES201806191628061418411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409186271036', '2', 'MOD2017828918136327', 'normal:del', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828918136327');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409186282824', '2', 'MOD201709300903291469368', null, '1', 'Contacts', 'MOD201782891459211_MOD20170822101010111_MOD201709300903291469368');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409186283393', '2', 'MOD20170728162514871', null, '2', 'MyTemplate', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162514871');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409186586377', '2', 'MOD20178281233167803', 'normal:list', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD20178281233167803');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409186608281', '2', 'MOD20170728162514871', null, '1', 'MyTemplate', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162514871');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409186696019', '2', 'MOD20170728164136213', 'normal:add', null, 'Service', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164136213');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409186770121', '2', 'RES201807051401071611668', null, '1', 'CaseGroup', 'MOD20170728162921619_MOD20170822101010113_RES201807051401071611668');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409187016484', '2', 'RES201806191629275174758', null, '1', 'RunReport', 'MOD20170728164358580_MOD20170822101010112_RES201806191629275174758');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409187022078', '2', 'MOD20170728164106927', null, '1', 'Case', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164106927');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409187535504', '2', 'MOD20170728162548115', 'normal:add', null, 'MyDocument', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162548115');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409188139013', '2', 'MOD20170728162548115', 'normal:list', null, 'MyDocument', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162548115');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409188283905', '2', 'RES201806191542466545857', 'normal:add', null, 'CaseCategory', 'MOD20170728162921619_MOD20170822101010113_RES201806191542466545857');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409188310692', '2', 'MOD2017828918585442', 'normal:edit', null, 'EmailSetting', 'MOD201782891459211_MOD20170822101010111_MOD2017828918585442');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409188387822', '2', 'RES201807051155433340411', null, '2', 'Projects', 'MOD20170728162921619_MOD20170822101010113_RES201807051155433340411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409188428014', '2', 'MOD20170728162514871', 'normal:add', null, 'MyTemplate', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162514871');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409188434582', '2', 'MOD20170728164106927', 'normal:del', null, 'Case', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164106927');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409188545223', '2', 'MOD20170728162514871', 'normal:del', null, 'MyTemplate', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162514871');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409188592815', '2', 'MOD2017828914568504', 'normal:add', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828914568504');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409188750992', '2', 'MOD20170728164136213', 'normal:normal', null, 'Service', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164136213');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409188964185', '2', 'MOD20170728162514871', 'normal:normal', null, 'MyTemplate', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162514871');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189023330', '2', 'MOD2017828918585442', 'normal:add', null, 'EmailSetting', 'MOD201782891459211_MOD20170822101010111_MOD2017828918585442');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189064309', '2', 'RES201807051407255803339', 'normal:normal', null, 'RunProject', 'MOD20170728164358580_MOD20170822101010112_RES201807051407255803339');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189151327', '2', 'MOD20170728155938744', 'normal:list', null, 'User', 'MOD20170728155843430_MOD20170822101010114_MOD20170728155938744');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189165638', '2', 'RES201806191628061418411', null, '1', 'RunLogs', 'MOD20170728164358580_MOD20170822101010112_RES201806191628061418411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189183731', '2', 'RES201806191542466545857', null, '2', 'CaseCategory', 'MOD20170728162921619_MOD20170822101010113_RES201806191542466545857');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189260044', '2', 'RES201807051407255803339', null, '1', 'RunProject', 'MOD20170728164358580_MOD20170822101010112_RES201807051407255803339');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189303949', '2', 'MOD20170728170410976', 'normal:list', null, 'RunCaseGroup', 'MOD20170728164358580_MOD20170822101010112_MOD20170728170410976');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189371284', '2', 'MOD201782892232638', null, '1', 'Message', 'MOD201782891374047_MOD20170822101010111_MOD201782892232638');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189422605', '2', 'MOD20170728163627238', null, '2', 'ProjectsInstance', 'MOD20170728162921619_MOD20170822101010113_MOD20170728163627238');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189490268', '2', 'RES201806191542466545857', 'normal:edit', null, 'CaseCategory', 'MOD20170728162921619_MOD20170822101010113_RES201806191542466545857');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189507646', '2', 'MOD2017828918136327', 'normal:normal', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828918136327');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189535367', '2', 'MOD2017828918136327', 'normal:edit', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828918136327');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189594649', '2', 'MOD20170728164106927', 'normal:add', null, 'Case', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164106927');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189682692', '2', 'RES201806191628061418411', 'normal:export', null, 'RunLogs', 'MOD20170728164358580_MOD20170822101010112_RES201806191628061418411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189687364', '2', 'MOD2017828918136327', 'normal:add', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828918136327');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189715267', '2', 'RES201807051407255803339', null, '2', 'RunProject', 'MOD20170728164358580_MOD20170822101010112_RES201807051407255803339');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189736349', '2', 'MOD201709300903291469368', 'normal:del', null, 'Contacts', 'MOD201782891459211_MOD20170822101010111_MOD201709300903291469368');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189848064', '2', 'RES201807051407255803339', 'normal:list', null, 'RunProject', 'MOD20170728164358580_MOD20170822101010112_RES201807051407255803339');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189891400', '2', 'RES201807051401071611668', 'normal:list', null, 'CaseGroup', 'MOD20170728162921619_MOD20170822101010113_RES201807051401071611668');
INSERT INTO `na_account_role_resource` VALUES ('RRU201808080409189917155', '2', 'MOD20170728163627238', 'normal:del', null, 'ProjectsInstance', 'MOD20170728162921619_MOD20170822101010113_MOD20170728163627238');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381037813', '1', 'MOD20170822101010116', null, '3', 'Modules', 'MOD20170822101010115_MOD20170822101010114_MOD20170822101010116');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381055692', '1', 'RES201807051401071611668', null, '4', 'CaseGroup', 'MOD20170728162921619_MOD20170822101010113_RES201807051401071611668');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381063504', '1', 'MOD2017828918136327', 'normal:normal', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828918136327');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381073733', '1', 'MOD20170728170410976', null, '1', 'RunCaseGroup', 'MOD20170728164358580_MOD20170822101010112_MOD20170728170410976');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381081257', '1', 'MOD2017828918585442', 'normal:normal', null, 'EmailSetting', 'MOD201782891459211_MOD20170822101010111_MOD2017828918585442');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381082706', '1', 'MOD201801270551331501217', null, '2', 'Help', 'MOD20170728160348129_MOD20170822101010114_MOD201801270551331501217');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381097248', '1', 'MOD20170822010350641', null, '2', 'RoleAssign', 'MOD20170728155843430_MOD20170822101010114_MOD20170822010350641');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381102236', '1', 'MOD20170912073810640', 'normal:normal', null, 'MyDocumentCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170912073810640');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381103129', '1', 'MOD2017828914568504', 'normal:del', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828914568504');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381103764', '1', 'MOD201782891621191', 'normal:del', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD201782891621191');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381103847', '1', 'MOD20170728160137853', 'normal:normal', null, 'Role', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160137853');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381124455', '1', 'MOD201801270551331501217', 'normal:add', null, 'Help', 'MOD20170728160348129_MOD20170822101010114_MOD201801270551331501217');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381146074', '1', 'RES201806191629275174758', null, 'data', 'RunReport', 'MOD20170728164358580_MOD20170822101010112_RES201806191629275174758');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381147898', '1', 'MOD20170728164106927', null, '2', 'Case', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164106927');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381152550', '1', 'MOD201782891621191', 'normal:add', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD201782891621191');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381188136', '1', 'MOD20170822010350641', 'normal:add', null, 'RoleAssign', 'MOD20170728155843430_MOD20170822101010114_MOD20170822010350641');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381205902', '1', 'MOD20170728163627238', 'normal:normal', null, 'ProjectsInstance', 'MOD20170728162921619_MOD20170822101010113_MOD20170728163627238');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381236352', '1', 'MOD20170728160137853', null, '1', 'Role', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160137853');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381239855', '1', 'MOD201709300903291469368', null, '4', 'Contacts', 'MOD201782891459211_MOD20170822101010111_MOD201709300903291469368');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381264053', '1', 'MOD20170828084638684', null, 'data', 'UserRole', 'MOD20170728155843430_MOD20170822101010114_MOD20170828084638684');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381269584', '1', 'MOD2017828914568504', 'normal:edit', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828914568504');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381280860', '1', 'RES201807051401071611668', 'normal:list', null, 'CaseGroup', 'MOD20170728162921619_MOD20170822101010113_RES201807051401071611668');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381293611', '1', 'MOD20170728160248143', null, '2', 'RolePermission', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160248143');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381303020', '1', 'MOD201782892232638', null, '4', 'Message', 'MOD201782891374047_MOD20170822101010111_MOD201782892232638');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381315896', '1', 'MOD2017828914568504', null, 'data', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828914568504');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381335729', '1', 'MOD201709300903291469368', null, '2', 'Contacts', 'MOD201782891459211_MOD20170822101010111_MOD201709300903291469368');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381344895', '1', 'MOD20170728162548115', null, '3', 'MyDocument', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162548115');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381354285', '1', 'RES201807051407255803339', 'normal:stop', null, 'RunProjects', 'MOD20170728164358580_MOD20170822101010112_RES201807051407255803339');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381359635', '1', 'RES201807051155433340411', null, '2', 'Projects', 'MOD20170728162921619_MOD20170822101010113_RES201807051155433340411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381368777', '1', 'MOD201801270551331501217', 'normal:del', null, 'Help', 'MOD20170728160348129_MOD20170822101010114_MOD201801270551331501217');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381405425', '1', 'MOD20170912073810640', 'normal:edit', null, 'MyDocumentCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170912073810640');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381405755', '1', 'MOD20170806113105545', 'normal:normal', null, 'DictionaryValue', 'MOD20170728160348129_MOD20170822101010114_MOD20170806113105545');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381406562', '1', 'MOD20170728160507765', null, '1', 'DictionaryCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160507765');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381412611', '1', 'MOD2017828918136327', 'normal:list', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828918136327');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381459235', '1', 'RES201807051407255803339', null, 'data', 'RunProjects', 'MOD20170728164358580_MOD20170822101010112_RES201807051407255803339');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381462419', '1', 'MOD20170728160248143', null, 'data', 'RolePermission', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160248143');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381477152', '1', 'MOD20178289441700', 'normal:del', null, 'Schedule', 'MOD201782891374047_MOD20170822101010111_MOD20178289441700');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381482370', '1', 'MOD20170728162514871', null, '1', 'MyTemplate', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162514871');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381535977', '1', 'MOD2017828914568504', null, '1', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828914568504');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381543557', '1', 'MOD20170728160507765', null, '2', 'DictionaryCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160507765');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381548125', '1', 'RES201807051407255803339', null, '4', 'RunProjects', 'MOD20170728164358580_MOD20170822101010112_RES201807051407255803339');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381553397', '1', 'MOD20170828084638684', null, '2', 'UserRole', 'MOD20170728155843430_MOD20170822101010114_MOD20170828084638684');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381555900', '1', 'MOD20170728162548115', null, '2', 'MyDocument', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162548115');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381572278', '1', 'RES201806191628061418411', 'normal:del', null, 'RunLogs', 'MOD20170728164358580_MOD20170822101010112_RES201806191628061418411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381609893', '1', 'MOD201782891621191', null, '1', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD201782891621191');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381613944', '1', 'MOD20170728160112776', 'normal:edit', null, 'Department', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160112776');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381619109', '1', 'MOD2017828918585442', 'normal:del', null, 'EmailSetting', 'MOD201782891459211_MOD20170822101010111_MOD2017828918585442');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381630188', '1', 'MOD20170728161037934', 'normal:edit', null, 'CommonConfiguration', 'MOD20170728160751128_MOD20170822101010114_MOD20170728161037934');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381660421', '1', 'MOD201709300903291469368', 'normal:add', null, 'Contacts', 'MOD201782891459211_MOD20170822101010111_MOD201709300903291469368');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381661136', '1', 'MOD20170728170410976', null, '2', 'RunCaseGroup', 'MOD20170728164358580_MOD20170822101010112_MOD20170728170410976');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381677762', '1', 'MOD20170728160112776', null, '4', 'Department', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160112776');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381694380', '1', 'MOD20170728164106927', 'normal:edit', null, 'Case', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164106927');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381721097', '1', 'MOD20170728160507765', 'normal:edit', null, 'DictionaryCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160507765');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381742268', '1', 'MOD201782892232638', 'normal:normal', null, 'Message', 'MOD201782891374047_MOD20170822101010111_MOD201782892232638');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381779051', '1', 'MOD20170728162548115', 'normal:del', null, 'MyDocument', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162548115');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381780005', '1', 'RES201806191628061418411', 'normal:list', null, 'RunLogs', 'MOD20170728164358580_MOD20170822101010112_RES201806191628061418411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381787726', '1', 'MOD20170822101010116', null, '1', 'Modules', 'MOD20170822101010115_MOD20170822101010114_MOD20170822101010116');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381788731', '1', 'MOD20170728161037934', 'normal:list', null, 'CommonConfiguration', 'MOD20170728160751128_MOD20170822101010114_MOD20170728161037934');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381788932', '1', 'MOD20170728162514871', null, '4', 'MyTemplate', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162514871');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381790973', '1', 'MOD20170728164136213', 'normal:edit', null, 'Service', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164136213');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381798436', '1', 'MOD20170728163627238', null, '3', 'ProjectsInstance', 'MOD20170728162921619_MOD20170822101010113_MOD20170728163627238');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381808611', '1', 'MOD20170728160507765', 'normal:list', null, 'DictionaryCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160507765');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381810277', '1', 'MOD20170728164106927', 'normal:add', null, 'Case', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164106927');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381813660', '1', 'MOD20170728170410976', null, '4', 'RunCaseGroup', 'MOD20170728164358580_MOD20170822101010112_MOD20170728170410976');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381824942', '1', 'MOD20170728162514871', null, '3', 'MyTemplate', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162514871');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381834462', '1', 'RES201806191542466545857', null, 'data', 'CaseCategory', 'MOD20170728162921619_MOD20170822101010113_RES201806191542466545857');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381886856', '1', 'MOD20170728162548115', 'normal:list', null, 'MyDocument', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162548115');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381890275', '1', 'MOD201801270551331501217', null, '3', 'Help', 'MOD20170728160348129_MOD20170822101010114_MOD201801270551331501217');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381916650', '1', 'MOD20170728164136213', null, '3', 'Service', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164136213');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381922228', '1', 'RES201806191628061418411', null, 'data', 'RunLogs', 'MOD20170728164358580_MOD20170822101010112_RES201806191628061418411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381922758', '1', 'MOD20170728160921443', 'normal:edit', null, 'AlertSetting', 'MOD20170728160751128_MOD20170822101010114_MOD20170728160921443');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381944388', '1', 'RES201807051155433340411', 'normal:normal', null, 'Projects', 'MOD20170728162921619_MOD20170822101010113_RES201807051155433340411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381979512', '1', 'MOD20170728160248143', 'normal:del', null, 'RolePermission', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160248143');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381985089', '1', 'MOD201782891621191', 'normal:list', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD201782891621191');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342381990813', '1', 'MOD20170728160248143', null, '4', 'RolePermission', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160248143');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382034953', '1', 'MOD2017828914568504', null, '2', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828914568504');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382103102', '1', 'MOD2017828918136327', null, '4', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828918136327');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382119461', '1', 'MOD20170912073810640', 'normal:list', null, 'MyDocumentCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170912073810640');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382129504', '1', 'MOD20170728160248143', null, '1', 'RolePermission', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160248143');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382131914', '1', 'MOD20170806113105545', 'normal:edit', null, 'DictionaryValue', 'MOD20170728160348129_MOD20170822101010114_MOD20170806113105545');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382138593', '1', 'MOD20170728162548115', null, 'data', 'MyDocument', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162548115');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382145772', '1', 'MOD2017828918136327', null, '2', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828918136327');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382180904', '1', 'MOD2017828918136327', 'normal:edit', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828918136327');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382203689', '1', 'MOD20170828084638684', null, '3', 'UserRole', 'MOD20170728155843430_MOD20170822101010114_MOD20170828084638684');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382212314', '1', 'MOD20170728162514871', 'normal:del', null, 'MyTemplate', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162514871');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382212500', '1', 'RES201807051155433340411', null, 'data', 'Projects', 'MOD20170728162921619_MOD20170822101010113_RES201807051155433340411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382252462', '1', 'MOD20170728162548115', 'normal:edit', null, 'MyDocument', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162548115');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382253263', '1', 'MOD20178281233167803', 'normal:list', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD20178281233167803');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382262398', '1', 'MOD20170912073810640', 'normal:del', null, 'MyDocumentCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170912073810640');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382398171', '1', 'MOD2017828918585442', 'normal:list', null, 'EmailSetting', 'MOD201782891459211_MOD20170822101010111_MOD2017828918585442');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382473648', '1', 'MOD20170728163627238', null, '4', 'ProjectsInstance', 'MOD20170728162921619_MOD20170822101010113_MOD20170728163627238');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382536787', '1', 'MOD20170728160112776', 'normal:normal', null, 'Department', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160112776');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382557230', '1', 'RES201806191542466545857', 'normal:edit', null, 'CaseCategory', 'MOD20170728162921619_MOD20170822101010113_RES201806191542466545857');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382573227', '1', 'MOD20170728155938744', null, '3', 'User', 'MOD20170728155843430_MOD20170822101010114_MOD20170728155938744');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382584114', '1', 'MOD201782892232638', null, '1', 'Message', 'MOD201782891374047_MOD20170822101010111_MOD201782892232638');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382630539', '1', 'MOD201782892232638', 'normal:list', null, 'Message', 'MOD201782891374047_MOD20170822101010111_MOD201782892232638');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382710742', '1', 'MOD20170728160921443', 'normal:del', null, 'AlertSetting', 'MOD20170728160751128_MOD20170822101010114_MOD20170728160921443');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382720238', '1', 'RES201806191629275174758', 'normal:normal', null, 'RunReport', 'MOD20170728164358580_MOD20170822101010112_RES201806191629275174758');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382834428', '1', 'MOD20170822010350641', 'normal:list', null, 'RoleAssign', 'MOD20170728155843430_MOD20170822101010114_MOD20170822010350641');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382837125', '1', 'RES201806191628061418411', null, '1', 'RunLogs', 'MOD20170728164358580_MOD20170822101010112_RES201806191628061418411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342382854548', '1', 'RES201806191542466545857', null, '4', 'CaseCategory', 'MOD20170728162921619_MOD20170822101010113_RES201806191542466545857');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383008410', '1', 'RES201807051407255803339', 'normal:list', null, 'RunProjects', 'MOD20170728164358580_MOD20170822101010112_RES201807051407255803339');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383008773', '1', 'MOD201782892232638', 'normal:del', null, 'Message', 'MOD201782891374047_MOD20170822101010111_MOD201782892232638');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383127057', '1', 'RES201806191542466545857', 'normal:add', null, 'CaseCategory', 'MOD20170728162921619_MOD20170822101010113_RES201806191542466545857');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383135925', '1', 'MOD20170822101010116', null, '2', 'Modules', 'MOD20170822101010115_MOD20170822101010114_MOD20170822101010116');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383191526', '1', 'MOD20170728164136213', 'normal:list', null, 'Service', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164136213');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383196034', '1', 'MOD20170728155938744', 'normal:edit', null, 'User', 'MOD20170728155843430_MOD20170822101010114_MOD20170728155938744');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383228066', '1', 'MOD20170728160507765', null, 'data', 'DictionaryCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160507765');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383229986', '1', 'MOD20170822010350641', null, '4', 'RoleAssign', 'MOD20170728155843430_MOD20170822101010114_MOD20170822010350641');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383232185', '1', 'MOD20170728160248143', 'normal:normal', null, 'RolePermission', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160248143');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383279840', '1', 'MOD20170728160556818', 'normal:edit', null, 'Dictionary', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160556818');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383292676', '1', 'MOD20170822010350641', 'normal:del', null, 'RoleAssign', 'MOD20170728155843430_MOD20170822101010114_MOD20170822010350641');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383318706', '1', 'MOD201801270551331501217', 'normal:list', null, 'Help', 'MOD20170728160348129_MOD20170822101010114_MOD201801270551331501217');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383403854', '1', 'RES201806191628061418411', null, '2', 'RunLogs', 'MOD20170728164358580_MOD20170822101010112_RES201806191628061418411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383422370', '1', 'MOD20170728160921443', 'normal:add', null, 'AlertSetting', 'MOD20170728160751128_MOD20170822101010114_MOD20170728160921443');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383458039', '1', 'MOD20178289441700', null, '1', 'Schedule', 'MOD201782891374047_MOD20170822101010111_MOD20178289441700');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383473821', '1', 'MOD20170822101010116', null, 'data', 'Modules', 'MOD20170822101010115_MOD20170822101010114_MOD20170822101010116');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383619160', '1', 'MOD20178281233167803', 'normal:normal', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD20178281233167803');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383630202', '1', 'MOD20170828084638684', 'normal:add', null, 'UserRole', 'MOD20170728155843430_MOD20170822101010114_MOD20170828084638684');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383669078', '1', 'MOD20170806113105545', 'normal:list', null, 'DictionaryValue', 'MOD20170728160348129_MOD20170822101010114_MOD20170806113105545');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383710531', '1', 'MOD20170728160921443', null, '2', 'AlertSetting', 'MOD20170728160751128_MOD20170822101010114_MOD20170728160921443');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383746496', '1', 'RES201807051407255803339', null, '1', 'RunProjects', 'MOD20170728164358580_MOD20170822101010112_RES201807051407255803339');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383747893', '1', 'MOD20170912073810640', null, '4', 'MyDocumentCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170912073810640');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383753095', '1', 'MOD20170728160507765', 'normal:add', null, 'DictionaryCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160507765');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383800586', '1', 'MOD20170806113105545', 'normal:add', null, 'DictionaryValue', 'MOD20170728160348129_MOD20170822101010114_MOD20170806113105545');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383806643', '1', 'MOD201782892546678', 'normal:normal', null, 'Events', 'MOD201782891374047_MOD20170822101010111_MOD201782892546678');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383846129', '1', 'MOD20170728163627238', null, '2', 'ProjectsInstance', 'MOD20170728162921619_MOD20170822101010113_MOD20170728163627238');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383864206', '1', 'MOD20170728160556818', null, '2', 'Dictionary', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160556818');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383916241', '1', 'RES201807051155433340411', null, '1', 'Projects', 'MOD20170728162921619_MOD20170822101010113_RES201807051155433340411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383959417', '1', 'MOD2017828914568504', null, '4', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828914568504');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383984277', '1', 'RES201807051401071611668', 'normal:normal', null, 'CaseGroup', 'MOD20170728162921619_MOD20170822101010113_RES201807051401071611668');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383988455', '1', 'MOD20178289441700', null, '4', 'Schedule', 'MOD201782891374047_MOD20170822101010111_MOD20178289441700');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383991979', '1', 'MOD20170728160137853', 'normal:edit', null, 'Role', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160137853');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342383997049', '1', 'MOD20170728160137853', 'normal:list', null, 'Role', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160137853');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384078315', '1', 'MOD20170822010350641', null, '3', 'RoleAssign', 'MOD20170728155843430_MOD20170822101010114_MOD20170822010350641');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384110184', '1', 'MOD20170822101010116', 'normal:del', null, 'Modules', 'MOD20170822101010115_MOD20170822101010114_MOD20170822101010116');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384217918', '1', 'MOD20170728160248143', 'normal:add', null, 'RolePermission', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160248143');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384223354', '1', 'MOD20170728160921443', null, '4', 'AlertSetting', 'MOD20170728160751128_MOD20170822101010114_MOD20170728160921443');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384233842', '1', 'MOD201782892232638', null, '2', 'Message', 'MOD201782891374047_MOD20170822101010111_MOD201782892232638');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384298474', '1', 'MOD20178281233167803', 'normal:add', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD20178281233167803');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384349630', '1', 'MOD20170728161037934', 'normal:normal', null, 'CommonConfiguration', 'MOD20170728160751128_MOD20170822101010114_MOD20170728161037934');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384409565', '1', 'MOD20170728162548115', 'normal:normal', null, 'MyDocument', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162548115');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384432292', '1', 'MOD20170728160921443', null, '1', 'AlertSetting', 'MOD20170728160751128_MOD20170822101010114_MOD20170728160921443');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384470153', '1', 'MOD20170728163627238', null, 'data', 'ProjectsInstance', 'MOD20170728162921619_MOD20170822101010113_MOD20170728163627238');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384526843', '1', 'MOD20178289441700', 'normal:normal', null, 'Schedule', 'MOD201782891374047_MOD20170822101010111_MOD20178289441700');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384580126', '1', 'MOD20170728164106927', null, '3', 'Case', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164106927');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384611399', '1', 'MOD20170728163627238', null, '1', 'ProjectsInstance', 'MOD20170728162921619_MOD20170822101010113_MOD20170728163627238');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384675048', '1', 'MOD201801270551331501217', null, 'data', 'Help', 'MOD20170728160348129_MOD20170822101010114_MOD201801270551331501217');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384680715', '1', 'RES201806191628061418411', null, '3', 'RunLogs', 'MOD20170728164358580_MOD20170822101010112_RES201806191628061418411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384700354', '1', 'MOD20170728170410976', null, '3', 'RunCaseGroup', 'MOD20170728164358580_MOD20170822101010112_MOD20170728170410976');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384722406', '1', 'MOD201801270551331501217', 'normal:edit', null, 'Help', 'MOD20170728160348129_MOD20170822101010114_MOD201801270551331501217');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384724605', '1', 'MOD201782892546678', 'normal:edit', null, 'Events', 'MOD201782891374047_MOD20170822101010111_MOD201782892546678');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384757938', '1', 'MOD20170822010350641', 'normal:normal', null, 'RoleAssign', 'MOD20170728155843430_MOD20170822101010114_MOD20170822010350641');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384786325', '1', 'RES201807051407255803339', 'normal:normal', null, 'RunProjects', 'MOD20170728164358580_MOD20170822101010112_RES201807051407255803339');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384790601', '1', 'MOD201782892546678', null, '4', 'Events', 'MOD201782891374047_MOD20170822101010111_MOD201782892546678');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384808320', '1', 'MOD201782891621191', null, '3', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD201782891621191');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384826862', '1', 'MOD20170728164106927', null, '1', 'Case', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164106927');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384844167', '1', 'MOD20170728162548115', null, '4', 'MyDocument', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162548115');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384865259', '1', 'MOD201709300903291469368', null, '1', 'Contacts', 'MOD201782891459211_MOD20170822101010111_MOD201709300903291469368');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384896889', '1', 'MOD20170728160507765', 'normal:normal', null, 'DictionaryCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160507765');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384911235', '1', 'MOD20170728163627238', 'normal:edit', null, 'ProjectsInstance', 'MOD20170728162921619_MOD20170822101010113_MOD20170728163627238');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384923913', '1', 'RES201806191542466545857', null, '2', 'CaseCategory', 'MOD20170728162921619_MOD20170822101010113_RES201806191542466545857');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384964568', '1', 'MOD2017828918585442', 'normal:edit', null, 'EmailSetting', 'MOD201782891459211_MOD20170822101010111_MOD2017828918585442');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342384979143', '1', 'RES201807051155433340411', 'normal:edit', null, 'Projects', 'MOD20170728162921619_MOD20170822101010113_RES201807051155433340411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385001292', '1', 'MOD20170828084638684', null, '1', 'UserRole', 'MOD20170728155843430_MOD20170822101010114_MOD20170828084638684');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385030643', '1', 'MOD2017828914568504', 'normal:list', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828914568504');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385047813', '1', 'RES201807051401071611668', null, '3', 'CaseGroup', 'MOD20170728162921619_MOD20170822101010113_RES201807051401071611668');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385057770', '1', 'MOD20170728160556818', 'normal:del', null, 'Dictionary', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160556818');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385058840', '1', 'MOD20178281233167803', null, '3', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD20178281233167803');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385088335', '1', 'MOD20170806113105545', null, '4', 'DictionaryValue', 'MOD20170728160348129_MOD20170822101010114_MOD20170806113105545');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385097212', '1', 'MOD20170728161037934', null, '3', 'CommonConfiguration', 'MOD20170728160751128_MOD20170822101010114_MOD20170728161037934');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385099668', '1', 'MOD201709300903291469368', null, '3', 'Contacts', 'MOD201782891459211_MOD20170822101010111_MOD201709300903291469368');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385113966', '1', 'MOD20170728155938744', 'normal:add', null, 'User', 'MOD20170728155843430_MOD20170822101010114_MOD20170728155938744');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385152399', '1', 'MOD20170728162548115', null, '1', 'MyDocument', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162548115');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385162888', '1', 'MOD20178289441700', 'normal:add', null, 'Schedule', 'MOD201782891374047_MOD20170822101010111_MOD20178289441700');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385205588', '1', 'MOD201782892546678', null, '2', 'Events', 'MOD201782891374047_MOD20170822101010111_MOD201782892546678');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385299042', '1', 'MOD20170822101010116', 'normal:list', null, 'Modules', 'MOD20170822101010115_MOD20170822101010114_MOD20170822101010116');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385330027', '1', 'MOD20170728164106927', 'normal:del', null, 'Case', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164106927');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385385893', '1', 'MOD2017828918136327', null, '3', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828918136327');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385417920', '1', 'MOD20170828084638684', 'normal:list', null, 'UserRole', 'MOD20170728155843430_MOD20170822101010114_MOD20170828084638684');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385440151', '1', 'MOD20170728160556818', null, '1', 'Dictionary', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160556818');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385454178', '1', 'MOD20170822101010116', 'normal:add', null, 'Modules', 'MOD20170822101010115_MOD20170822101010114_MOD20170822101010116');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385471065', '1', 'MOD20170728162514871', 'normal:normal', null, 'MyTemplate', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162514871');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385473943', '1', 'MOD20170728155938744', null, '1', 'User', 'MOD20170728155843430_MOD20170822101010114_MOD20170728155938744');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385513519', '1', 'MOD20170728160112776', null, '1', 'Department', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160112776');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385531139', '1', 'MOD201782892546678', 'normal:del', null, 'Events', 'MOD201782891374047_MOD20170822101010111_MOD201782892546678');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385541420', '1', 'MOD20170806113105545', null, '2', 'DictionaryValue', 'MOD20170728160348129_MOD20170822101010114_MOD20170806113105545');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385593654', '1', 'RES201807051407255803339', null, '3', 'RunProjects', 'MOD20170728164358580_MOD20170822101010112_RES201807051407255803339');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385636575', '1', 'MOD20170728164136213', 'normal:add', null, 'Service', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164136213');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385677724', '1', 'MOD20170728170410976', 'normal:list', null, 'RunCaseGroup', 'MOD20170728164358580_MOD20170822101010112_MOD20170728170410976');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385698903', '1', 'MOD20170728162514871', 'normal:list', null, 'MyTemplate', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162514871');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385745056', '1', 'MOD20178281233167803', null, 'data', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD20178281233167803');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385803818', '1', 'MOD20170728160137853', 'normal:add', null, 'Role', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160137853');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385828635', '1', 'MOD201782891621191', null, 'data', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD201782891621191');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385835460', '1', 'MOD2017828918136327', null, '1', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828918136327');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385856537', '1', 'MOD20170728164106927', 'normal:list', null, 'Case', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164106927');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385869503', '1', 'MOD20170828084638684', 'normal:del', null, 'UserRole', 'MOD20170728155843430_MOD20170822101010114_MOD20170828084638684');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385890757', '1', 'MOD201782892546678', null, '3', 'Events', 'MOD201782891374047_MOD20170822101010111_MOD201782892546678');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385947291', '1', 'MOD201782892232638', 'normal:add', null, 'Message', 'MOD201782891374047_MOD20170822101010111_MOD201782892232638');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342385991264', '1', 'MOD20170828084638684', null, '4', 'UserRole', 'MOD20170728155843430_MOD20170822101010114_MOD20170828084638684');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386003293', '1', 'RES201807051401071611668', null, '1', 'CaseGroup', 'MOD20170728162921619_MOD20170822101010113_RES201807051401071611668');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386077483', '1', 'MOD20170728155938744', 'normal:del', null, 'User', 'MOD20170728155843430_MOD20170822101010114_MOD20170728155938744');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386101875', '1', 'MOD20170728161037934', null, 'data', 'CommonConfiguration', 'MOD20170728160751128_MOD20170822101010114_MOD20170728161037934');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386131244', '1', 'MOD20170728164136213', null, 'data', 'Service', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164136213');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386134462', '1', 'MOD20170828084638684', 'normal:edit', null, 'UserRole', 'MOD20170728155843430_MOD20170822101010114_MOD20170828084638684');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386187703', '1', 'MOD201709300903291469368', 'normal:list', null, 'Contacts', 'MOD201782891459211_MOD20170822101010111_MOD201709300903291469368');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386202920', '1', 'MOD20170728162514871', 'normal:edit', null, 'MyTemplate', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162514871');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386224540', '1', 'MOD20170728160556818', 'normal:list', null, 'Dictionary', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160556818');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386244581', '1', 'MOD20170728162514871', null, '2', 'MyTemplate', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162514871');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386249324', '1', 'MOD20170728163627238', 'normal:list', null, 'ProjectsInstance', 'MOD20170728162921619_MOD20170822101010113_MOD20170728163627238');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386286104', '1', 'MOD20170728161037934', null, '4', 'CommonConfiguration', 'MOD20170728160751128_MOD20170822101010114_MOD20170728161037934');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386305752', '1', 'MOD20170728155938744', null, 'data', 'User', 'MOD20170728155843430_MOD20170822101010114_MOD20170728155938744');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386322291', '1', 'MOD20170728164136213', null, '1', 'Service', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164136213');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386377473', '1', 'MOD2017828914568504', null, '3', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828914568504');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386379957', '1', 'MOD20170806113105545', null, 'data', 'DictionaryValue', 'MOD20170728160348129_MOD20170822101010114_MOD20170806113105545');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386424927', '1', 'MOD201801270551331501217', null, '1', 'Help', 'MOD20170728160348129_MOD20170822101010114_MOD201801270551331501217');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386483836', '1', 'MOD20170728160112776', 'normal:del', null, 'Department', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160112776');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386483986', '1', 'MOD201801270551331501217', null, '4', 'Help', 'MOD20170728160348129_MOD20170822101010114_MOD201801270551331501217');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386517121', '1', 'RES201807051407255803339', 'normal:run', null, 'RunProjects', 'MOD20170728164358580_MOD20170822101010112_RES201807051407255803339');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386552963', '1', 'MOD201801270551331501217', 'normal:normal', null, 'Help', 'MOD20170728160348129_MOD20170822101010114_MOD201801270551331501217');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386564558', '1', 'MOD20178289441700', null, 'data', 'Schedule', 'MOD201782891374047_MOD20170822101010111_MOD20178289441700');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386619476', '1', 'MOD20170728162548115', 'normal:add', null, 'MyDocument', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162548115');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386684502', '1', 'MOD2017828914568504', 'normal:add', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828914568504');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386705463', '1', 'RES201806191542466545857', null, '1', 'CaseCategory', 'MOD20170728162921619_MOD20170822101010113_RES201806191542466545857');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386712808', '1', 'MOD201782892546678', null, '1', 'Events', 'MOD201782891374047_MOD20170822101010111_MOD201782892546678');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386719271', '1', 'MOD20170728160248143', null, '3', 'RolePermission', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160248143');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386752319', '1', 'RES201806191629275174758', null, '4', 'RunReport', 'MOD20170728164358580_MOD20170822101010112_RES201806191629275174758');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386777022', '1', 'RES201806191628061418411', 'normal:export', null, 'RunLogs', 'MOD20170728164358580_MOD20170822101010112_RES201806191628061418411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386783646', '1', 'RES201806191629275174758', 'normal:list', null, 'RunReport', 'MOD20170728164358580_MOD20170822101010112_RES201806191629275174758');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386811121', '1', 'MOD20170728160137853', null, '4', 'Role', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160137853');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386845163', '1', 'RES201806191629275174758', 'normal:export', null, 'RunReport', 'MOD20170728164358580_MOD20170822101010112_RES201806191629275174758');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386846613', '1', 'MOD20170822101010116', 'normal:edit', null, 'Modules', 'MOD20170822101010115_MOD20170822101010114_MOD20170822101010116');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386856146', '1', 'MOD20170728155938744', null, '4', 'User', 'MOD20170728155843430_MOD20170822101010114_MOD20170728155938744');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386887717', '1', 'MOD201782892546678', 'normal:add', null, 'Events', 'MOD201782891374047_MOD20170822101010111_MOD201782892546678');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386905391', '1', 'MOD20170728160112776', 'normal:list', null, 'Department', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160112776');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386922785', '1', 'MOD20178281233167803', 'normal:edit', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD20178281233167803');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386932223', '1', 'MOD20170728160112776', 'normal:add', null, 'Department', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160112776');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386963277', '1', 'MOD2017828918136327', 'normal:add', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828918136327');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342386963838', '1', 'MOD20170728160248143', 'normal:list', null, 'RolePermission', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160248143');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387016921', '1', 'MOD20170728163627238', 'normal:del', null, 'ProjectsInstance', 'MOD20170728162921619_MOD20170822101010113_MOD20170728163627238');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387033647', '1', 'MOD2017828918585442', null, '4', 'EmailSetting', 'MOD201782891459211_MOD20170822101010111_MOD2017828918585442');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387039055', '1', 'RES201807051155433340411', null, '3', 'Projects', 'MOD20170728162921619_MOD20170822101010113_RES201807051155433340411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387047672', '1', 'MOD2017828918136327', 'normal:del', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828918136327');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387107617', '1', 'MOD2017828918585442', null, '2', 'EmailSetting', 'MOD201782891459211_MOD20170822101010111_MOD2017828918585442');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387145744', '1', 'MOD20170728170410976', 'normal:normal', null, 'RunCaseGroup', 'MOD20170728164358580_MOD20170822101010112_MOD20170728170410976');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387179137', '1', 'RES201806191542466545857', 'normal:normal', null, 'CaseCategory', 'MOD20170728162921619_MOD20170822101010113_RES201806191542466545857');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387224073', '1', 'RES201807051401071611668', null, '2', 'CaseGroup', 'MOD20170728162921619_MOD20170822101010113_RES201807051401071611668');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387303639', '1', 'MOD201709300903291469368', 'normal:edit', null, 'Contacts', 'MOD201782891459211_MOD20170822101010111_MOD201709300903291469368');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387324460', '1', 'MOD201709300903291469368', 'normal:normal', null, 'Contacts', 'MOD201782891459211_MOD20170822101010111_MOD201709300903291469368');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387326880', '1', 'MOD20170912073810640', null, '2', 'MyDocumentCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170912073810640');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387355528', '1', 'MOD201782891621191', 'normal:edit', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD201782891621191');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387379432', '1', 'MOD20170728164106927', null, '4', 'Case', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164106927');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387405627', '1', 'MOD20170728160507765', 'normal:del', null, 'DictionaryCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160507765');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387445098', '1', 'MOD20170728162514871', null, 'data', 'MyTemplate', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162514871');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387447830', '1', 'MOD20170728164136213', 'normal:del', null, 'Service', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164136213');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387495953', '1', 'MOD20170728160137853', null, '3', 'Role', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160137853');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387535107', '1', 'MOD20170822010350641', 'normal:edit', null, 'RoleAssign', 'MOD20170728155843430_MOD20170822101010114_MOD20170822010350641');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387552104', '1', 'MOD20170828084638684', 'normal:normal', null, 'UserRole', 'MOD20170728155843430_MOD20170822101010114_MOD20170828084638684');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387566877', '1', 'MOD20170912073810640', 'normal:add', null, 'MyDocumentCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170912073810640');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387584329', '1', 'MOD20170912073810640', null, '3', 'MyDocumentCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170912073810640');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387599239', '1', 'MOD2017828918136327', null, 'data', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828918136327');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387639518', '1', 'MOD20170728160137853', 'normal:del', null, 'Role', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160137853');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387642626', '1', 'MOD20170822010350641', null, '1', 'RoleAssign', 'MOD20170728155843430_MOD20170822101010114_MOD20170822010350641');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387688722', '1', 'MOD20170728162514871', 'normal:add', null, 'MyTemplate', 'MOD20170728161313432_MOD20170822101010111_MOD20170728162514871');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387707062', '1', 'MOD2017828918585442', null, '1', 'EmailSetting', 'MOD201782891459211_MOD20170822101010111_MOD2017828918585442');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387748561', '1', 'MOD20170728163627238', 'normal:add', null, 'ProjectsInstance', 'MOD20170728162921619_MOD20170822101010113_MOD20170728163627238');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387756692', '1', 'MOD20170728160112776', null, '3', 'Department', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160112776');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387782480', '1', 'MOD20170728160112776', null, '2', 'Department', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160112776');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387805543', '1', 'MOD20178289441700', 'normal:edit', null, 'Schedule', 'MOD201782891374047_MOD20170822101010111_MOD20178289441700');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387838586', '1', 'MOD20170728160137853', null, '2', 'Role', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160137853');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387859231', '1', 'MOD201782892232638', null, '3', 'Message', 'MOD201782891374047_MOD20170822101010111_MOD201782892232638');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387860913', '1', 'MOD2017828914568504', 'normal:normal', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD2017828914568504');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387880962', '1', 'MOD2017828918585442', null, '3', 'EmailSetting', 'MOD201782891459211_MOD20170822101010111_MOD2017828918585442');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387889803', '1', 'MOD20170728160112776', null, 'data', 'Department', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160112776');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387917181', '1', 'MOD201782891621191', null, '4', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD201782891621191');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387938943', '1', 'MOD2017828918585442', 'normal:add', null, 'EmailSetting', 'MOD201782891459211_MOD20170822101010111_MOD2017828918585442');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387963112', '1', 'MOD20170728155938744', 'normal:normal', null, 'User', 'MOD20170728155843430_MOD20170822101010114_MOD20170728155938744');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387963967', '1', 'MOD20170822101010116', null, '4', 'Modules', 'MOD20170822101010115_MOD20170822101010114_MOD20170822101010116');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387981223', '1', 'RES201807051155433340411', 'normal:add', null, 'Projects', 'MOD20170728162921619_MOD20170822101010113_RES201807051155433340411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342387993500', '1', 'MOD201782892546678', 'normal:list', null, 'Events', 'MOD201782891374047_MOD20170822101010111_MOD201782892546678');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388041268', '1', 'RES201806191629275174758', null, '3', 'RunReport', 'MOD20170728164358580_MOD20170822101010112_RES201806191629275174758');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388044134', '1', 'MOD20170728160556818', null, '3', 'Dictionary', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160556818');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388047573', '1', 'MOD20170728160921443', null, '3', 'AlertSetting', 'MOD20170728160751128_MOD20170822101010114_MOD20170728160921443');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388058953', '1', 'RES201806191542466545857', 'normal:del', null, 'CaseCategory', 'MOD20170728162921619_MOD20170822101010113_RES201806191542466545857');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388086524', '1', 'RES201807051401071611668', 'normal:del', null, 'CaseGroup', 'MOD20170728162921619_MOD20170822101010113_RES201807051401071611668');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388097096', '1', 'MOD20170728160556818', 'normal:add', null, 'Dictionary', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160556818');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388231760', '1', 'MOD20170806113105545', null, '3', 'DictionaryValue', 'MOD20170728160348129_MOD20170822101010114_MOD20170806113105545');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388238861', '1', 'RES201807051155433340411', null, '4', 'Projects', 'MOD20170728162921619_MOD20170822101010113_RES201807051155433340411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388278503', '1', 'MOD20178281233167803', null, '1', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD20178281233167803');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388299897', '1', 'MOD20170822101010116', 'normal:normal', null, 'Modules', 'MOD20170822101010115_MOD20170822101010114_MOD20170822101010116');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388369454', '1', 'MOD201782892232638', 'normal:edit', null, 'Message', 'MOD201782891374047_MOD20170822101010111_MOD201782892232638');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388417342', '1', 'MOD20170822010350641', null, 'data', 'RoleAssign', 'MOD20170728155843430_MOD20170822101010114_MOD20170822010350641');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388437021', '1', 'MOD20178289441700', null, '2', 'Schedule', 'MOD201782891374047_MOD20170822101010111_MOD20178289441700');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388446335', '1', 'RES201807051401071611668', null, 'data', 'CaseGroup', 'MOD20170728162921619_MOD20170822101010113_RES201807051401071611668');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388470274', '1', 'RES201806191542466545857', 'normal:list', null, 'CaseCategory', 'MOD20170728162921619_MOD20170822101010113_RES201806191542466545857');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388481519', '1', 'MOD20170728155938744', 'normal:list', null, 'User', 'MOD20170728155843430_MOD20170822101010114_MOD20170728155938744');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388521268', '1', 'MOD201782892546678', null, 'data', 'Events', 'MOD201782891374047_MOD20170822101010111_MOD201782892546678');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388575156', '1', 'RES201806191629275174758', null, '2', 'RunReport', 'MOD20170728164358580_MOD20170822101010112_RES201806191629275174758');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388578880', '1', 'MOD20170728160248143', 'normal:edit', null, 'RolePermission', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160248143');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388624572', '1', 'MOD201782891621191', null, '2', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD201782891621191');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388632871', '1', 'MOD20170728160137853', null, 'data', 'Role', 'MOD20170728155843430_MOD20170822101010114_MOD20170728160137853');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388696523', '1', 'RES201807051155433340411', 'normal:del', null, 'Projects', 'MOD20170728162921619_MOD20170822101010113_RES201807051155433340411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388699216', '1', 'MOD20170806113105545', null, '1', 'DictionaryValue', 'MOD20170728160348129_MOD20170822101010114_MOD20170806113105545');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388801886', '1', 'RES201807051155433340411', 'normal:list', null, 'Projects', 'MOD20170728162921619_MOD20170822101010113_RES201807051155433340411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388826267', '1', 'MOD20170728160556818', 'normal:normal', null, 'Dictionary', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160556818');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342388951213', '1', 'MOD20178281233167803', null, '4', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD20178281233167803');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389050292', '1', 'MOD20178289441700', null, '3', 'Schedule', 'MOD201782891374047_MOD20170822101010111_MOD20178289441700');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389142414', '1', 'MOD20170728164136213', null, '4', 'Service', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164136213');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389155186', '1', 'RES201806191629275174758', null, '1', 'RunReport', 'MOD20170728164358580_MOD20170822101010112_RES201806191629275174758');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389171139', '1', 'RES201807051407255803339', null, '2', 'RunProjects', 'MOD20170728164358580_MOD20170822101010112_RES201807051407255803339');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389191180', '1', 'MOD20178281233167803', null, '2', 'Email', 'MOD201782891459211_MOD20170822101010111_MOD20178281233167803');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389191563', '1', 'MOD201782892232638', null, 'data', 'Message', 'MOD201782891374047_MOD20170822101010111_MOD201782892232638');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389213131', '1', 'RES201806191542466545857', null, '3', 'CaseCategory', 'MOD20170728162921619_MOD20170822101010113_RES201806191542466545857');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389240811', '1', 'MOD20178281233167803', 'normal:del', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD20178281233167803');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389242557', '1', 'MOD2017828918585442', null, 'data', 'EmailSetting', 'MOD201782891459211_MOD20170822101010111_MOD2017828918585442');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389255608', '1', 'MOD20170728160507765', null, '3', 'DictionaryCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160507765');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389320570', '1', 'MOD201709300903291469368', 'normal:del', null, 'Contacts', 'MOD201782891459211_MOD20170822101010111_MOD201709300903291469368');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389322781', '1', 'MOD20170912073810640', null, '1', 'MyDocumentCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170912073810640');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389384542', '1', 'RES201806191628061418411', 'normal:normal', null, 'RunLogs', 'MOD20170728164358580_MOD20170822101010112_RES201806191628061418411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389455312', '1', 'MOD201709300903291469368', null, 'data', 'Contacts', 'MOD201782891459211_MOD20170822101010111_MOD201709300903291469368');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389511315', '1', 'MOD20170806113105545', 'normal:del', null, 'DictionaryValue', 'MOD20170728160348129_MOD20170822101010114_MOD20170806113105545');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389536143', '1', 'RES201807051401071611668', 'normal:edit', null, 'CaseGroup', 'MOD20170728162921619_MOD20170822101010113_RES201807051401071611668');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389540756', '1', 'RES201807051401071611668', 'normal:add', null, 'CaseGroup', 'MOD20170728162921619_MOD20170822101010113_RES201807051401071611668');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389540988', '1', 'MOD20170728160507765', null, '4', 'DictionaryCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160507765');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389609363', '1', 'MOD20170728164136213', null, '2', 'Service', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164136213');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389646865', '1', 'MOD20170728160921443', 'normal:list', null, 'AlertSetting', 'MOD20170728160751128_MOD20170822101010114_MOD20170728160921443');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389651018', '1', 'MOD20170728155938744', null, '2', 'User', 'MOD20170728155843430_MOD20170822101010114_MOD20170728155938744');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389671596', '1', 'MOD20170728170410976', null, 'data', 'RunCaseGroup', 'MOD20170728164358580_MOD20170822101010112_MOD20170728170410976');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389696476', '1', 'MOD20170728164106927', null, 'data', 'Case', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164106927');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389708886', '1', 'MOD20170728160556818', null, 'data', 'Dictionary', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160556818');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389743877', '1', 'MOD20170728160921443', 'normal:normal', null, 'AlertSetting', 'MOD20170728160751128_MOD20170822101010114_MOD20170728160921443');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389745875', '1', 'MOD20170912073810640', null, 'data', 'MyDocumentCategory', 'MOD20170728160348129_MOD20170822101010114_MOD20170912073810640');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389784581', '1', 'RES201806191628061418411', null, '4', 'RunLogs', 'MOD20170728164358580_MOD20170822101010112_RES201806191628061418411');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389826562', '1', 'MOD20170728164106927', 'normal:normal', null, 'Case', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164106927');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389830321', '1', 'MOD20178289441700', 'normal:list', null, 'Schedule', 'MOD201782891374047_MOD20170822101010111_MOD20178289441700');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389858980', '1', 'MOD20170728164136213', 'normal:normal', null, 'Service', 'MOD20170728162921619_MOD20170822101010113_MOD20170728164136213');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389865891', '1', 'MOD20170728160556818', null, '4', 'Dictionary', 'MOD20170728160348129_MOD20170822101010114_MOD20170728160556818');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389934868', '1', 'MOD20170728160921443', null, 'data', 'AlertSetting', 'MOD20170728160751128_MOD20170822101010114_MOD20170728160921443');
INSERT INTO `na_account_role_resource` VALUES ('RRU201809021342389980441', '1', 'MOD201782891621191', 'normal:normal', null, 'Email', 'MOD201782891459211_MOD20170822101010111_MOD201782891621191');

-- ----------------------------
-- Table structure for na_account_user
-- ----------------------------
DROP TABLE IF EXISTS `na_account_user`;
CREATE TABLE `na_account_user` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `account` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cn_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `en_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `extension` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `qq` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `salt` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `skype` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `joinus_at` datetime DEFAULT NULL,
  `set_list_rows` smallint(6) DEFAULT NULL,
  `email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gender` smallint(6) DEFAULT NULL,
  `wechat` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lang` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  `timezone` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `date_format` varchar(19) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_account_user_account_idx` (`account`) USING BTREE,
  KEY `na_account_user_id_idx` (`id`) USING BTREE,
  KEY `na_account_user_status_idx` (`status`) USING BTREE,
  KEY `na_account_user_department_id_fkey` (`department_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_account_user
-- ----------------------------
INSERT INTO `na_account_user` VALUES ('1', 'admin', '系统管理员', 'admin', '0209999999', '12345678901', 'admin', 'd6b16e3c85bdc45e', 'admin', 'DEP20180822000000110', '1', '2017-08-22 00:00:00', '2018-09-16 17:28:38', '2017-09-13 01:17:45', '50', 'hh_ping@163.com', '478e2011ebaeaa2c6b236be3fc3f19f14a56965e', '1', 'admin', 'zh_CN', 'GMT+8', 'd/m/Y H:i:s');
INSERT INTO `na_account_user` VALUES ('USER20170903010453523456', 'test1', '测试账号25', 'test', 'a', '13811111', 'aa', '4e86a3119ecb28c1', 'a', 'DEP201806231402567686999', '1', '2017-10-03 01:04:53', '2018-10-07 13:11:21', '2017-09-13 03:16:07', '30', '123@123.com', 'edcf27bc31ceb74c747ee1eed45bee1016a6228b', '2', 'aa', 'zh_CN', 'GMT+8', 'd/m/Y H:i:s');
INSERT INTO `na_account_user` VALUES ('USER20170925082403553832', 'tq', '质检经理', 'QC Manager', '0203333333', '11333333', 'QCManager', 'd6b16e3c85bdc45e', 'QCManager', 'DEP201806231402567686999', '1', '2017-10-25 08:24:03', '2018-09-08 03:35:13', '2017-10-25 06:23:56', '25', 'QCManager@163.com', '478e2011ebaeaa2c6b236be3fc3f19f14a56965e', '1', 'QCManager', 'en_AU', 'GMT+8', 'Y-m-d H:i:s');
INSERT INTO `na_account_user` VALUES ('USER20170931030313923501', 'tp', '项目员', 'project', '1', '11111111', '1', 'd6b16e3c85bdc45e', '1', 'DEP201806231402567686999', '2', '2017-10-31 03:03:14', '2018-09-08 03:36:39', '2017-10-31 01:03:09', '30', '123@123.com', '478e2011ebaeaa2c6b236be3fc3f19f14a56965e', '1', '1', 'zh_CN', 'GMT+8', 'Y-m-d H:i:s');
INSERT INTO `na_account_user` VALUES ('USER20171003140545679571', 'T-josie', 'josie.duan', 'josie.duan', '0207564333', '11111111', 'cgmanager', 'd6b16e3c85bdc45e', 'cgmanager', 'DEP201806231402567686999', '2', '2017-11-03 14:05:46', '2018-08-08 16:18:42', '2017-11-01 04:05:04', '30', 'jisie@163.com', '478e2011ebaeaa2c6b236be3fc3f19f14a56965e', '1', 'cgmanager', 'zh_CN', 'GMT+8', 'Y-m-d H:i:s');
INSERT INTO `na_account_user` VALUES ('USER20171011070658263608', 'T-cw', '船务员-测试', '船务员-测试', '123', '13811111', '12', 'd6b16e3c85bdc45e', '123', 'DEP201709261136175908932', '2', '2017-11-11 07:06:59', null, '2017-11-24 05:06:47', '25', '123@123.cok', '478e2011ebaeaa2c6b236be3fc3f19f14a56965e', '1', '3123', 'zh_CN', 'GMT+8', 'Y-m-d H:i:s');
INSERT INTO `na_account_user` VALUES ('USER20171014130825490718', 'T-cwleader', '船务组长（开发）', '船务组长（开发）', '1', '13811111', '1', 'd6b16e3c85bdc45e', '1', 'DEP201709261136175908932', '2', '2017-11-14 13:08:25', null, '2017-11-14 11:08:10', '25', '123@123.sss', '478e2011ebaeaa2c6b236be3fc3f19f14a56965e', '1', '1', 'zh_CN', 'GMT+8', 'Y-m-d H:i:s');
INSERT INTO `na_account_user` VALUES ('USER20171017061950145281', 'T-cgassistant', '采购员助理', '123', '6', '11111111', '1', 'd6b16e3c85bdc45e', '3', 'DEP201806231417136618464', '2', '2017-11-17 06:19:50', '2018-09-08 03:33:54', '2017-11-01 04:19:28', '25', '123@123.com', '478e2011ebaeaa2c6b236be3fc3f19f14a56965e', '1', '2', 'zh_CN', 'GMT+8', 'Y-m-d H:i:s');
INSERT INTO `na_account_user` VALUES ('USER20171017065946104655', 'T-gm', '公司总经理', 'GM', null, '15686587320', '1029496931', 'd6b16e3c85bdc45e', '1946sd.163.cn', 'DEP20180822000000110', '1', '2017-11-17 06:59:47', '2018-08-08 16:16:53', '2017-11-17 04:58:24', '25', '123@123.com', '478e2011ebaeaa2c6b236be3fc3f19f14a56965e', '1', 'fsdsdsddf451515', 'zh_CN', 'GMT+8', 'Y-m-d H:i:s');
INSERT INTO `na_account_user` VALUES ('USER20171017073317224321', 'T-Screeners1', '安检员1号', 'Screeners1', '120452', '15214512451', '17793634623', 'd6b16e3c85bdc45e', 'Screeners2@qq.com', 'DEP201806231416249721161', '1', '2017-11-17 07:33:17', '2018-08-08 16:07:24', '2017-11-11 07:35:18', '25', 'Screeners2@qq.com', '478e2011ebaeaa2c6b236be3fc3f19f14a56965e', '2', 'wewer@qq.com', 'zh_CN', 'GMT+8', 'Y-m-d H:i:s');
INSERT INTO `na_account_user` VALUES ('USER20171017073502130126', 'T-Screeners2', '安检员2号', 'Screeners2', '020-85296356', '15985265448', '15985263545', 'd6b16e3c85bdc45e', 'Screeners@qq.com', 'DEP201806231402567686999', '1', '2017-11-17 07:35:03', '2018-08-08 16:16:38', '2017-11-10 07:37:10', '30', 'Screeners@qq.com', '478e2011ebaeaa2c6b236be3fc3f19f14a56965e', '1', '85296356', 'zh_CN', 'GMT+8', 'Y-m-d H:i:s');
INSERT INTO `na_account_user` VALUES ('USER20171017085811380810', 'T-spleader', '船务组长', '12345', '2589', '15985244', 'shippingleader@qq.com', 'd6b16e3c85bdc45e', 'shippingleader@qq.com', 'DEP201806231417136618464', '2', '2017-11-17 08:58:11', '2018-08-12 08:04:00', '2017-11-10 09:00:31', '25', 'shippingleader@qq.com', '478e2011ebaeaa2c6b236be3fc3f19f14a56965e', '1', 'shippingleader@qq.com', 'zh_CN', 'GMT+8', 'Y-m-d H:i:s');
INSERT INTO `na_account_user` VALUES ('USER20171020085236742409', 'T-ap', '出纳', 'account payable', null, '13236456225', '1223', 'd6b16e3c85bdc45e', '1561562', 'DEP201806231416249721161', '2', '2017-11-20 08:52:36', '2018-09-08 03:34:05', '2017-11-20 06:52:04', '25', '123@123.com', '478e2011ebaeaa2c6b236be3fc3f19f14a56965e', '1', '59656454', 'zh_CN', 'GMT+8', 'Y-m-d H:i:s');
INSERT INTO `na_account_user` VALUES ('USER20171130190458587582', 'T-qcleader', '质检组长', 'qc leader', '1', '11111111', '1', 'd6b16e3c85bdc45e', '1', 'DEP201709250819205450587', '2', '2017-12-30 19:04:59', null, '2017-10-25 06:23:23', '25', '123@123.com', '478e2011ebaeaa2c6b236be3fc3f19f14a56965e', '1', '1', 'zh_CN', 'GMT+8', 'Y-m-d H:i:s');
INSERT INTO `na_account_user` VALUES ('USER20180712070708747425', 'T-autest1', '会计', '1', '1', '11111111', '1', 'd6b16e3c85bdc45e', '1', 'DEP201806231402567686999', '2', '2018-08-12 07:07:08', '2018-08-08 16:15:44', '2017-10-31 01:03:09', '30', '123@123.com', '478e2011ebaeaa2c6b236be3fc3f19f14a56965e', '1', '1', 'zh_CN', 'GMT+8', 'Y-m-d H:i:s');

-- ----------------------------
-- Table structure for na_account_user_online
-- ----------------------------
DROP TABLE IF EXISTS `na_account_user_online`;
CREATE TABLE `na_account_user_online` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `session_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'session id',
  `user_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户ID',
  `user_cn_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户中文名',
  `user_en_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户英文名',
  `department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门ID',
  `con_user_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '联接的用户ID',
  `created_at` datetime(6) DEFAULT NULL COMMENT '联接创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '最近活动时间',
  `ip` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '访问IP',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id` (`id`) USING BTREE,
  KEY `session_id` (`session_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `con_user_id` (`con_user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_account_user_online
-- ----------------------------

-- ----------------------------
-- Table structure for na_account_user_role
-- ----------------------------
DROP TABLE IF EXISTS `na_account_user_role`;
CREATE TABLE `na_account_user_role` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `user_id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `role_id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_account_user_role_id_idx` (`id`) USING BTREE,
  KEY `user_id` (`user_id`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_account_user_role
-- ----------------------------
INSERT INTO `na_account_user_role` VALUES ('URU201808080334462699301', 'USER20170903010453523456', '2');
INSERT INTO `na_account_user_role` VALUES ('URU201808080335531038004', 'USER20170925082403553832', '3');
INSERT INTO `na_account_user_role` VALUES ('URU201809021343146964944', '1', '1');

-- ----------------------------
-- Table structure for na_archives_history
-- ----------------------------
DROP TABLE IF EXISTS `na_archives_history`;
CREATE TABLE `na_archives_history` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `module_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模块名',
  `business_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模块记录ID',
  `old_content` longblob COMMENT '原内容',
  `new_content` longblob COMMENT '修改后内容',
  `status` smallint(6) DEFAULT NULL COMMENT '记录状态',
  `is_applied` smallint(6) DEFAULT NULL COMMENT '应用过：1是、２否',
  `ver` smallint(6) DEFAULT NULL COMMENT '版本',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `creator_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator_cn_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator_en_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_cn_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_en_name` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `assignee_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '变更确认人ID',
  `assignee_cn_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '变更确认人中文名',
  `assignee_en_name` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '变更确认人英文名',
  `role_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '可以处理角色ID',
  `role_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '可处理角色中文名',
  `role_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '可处理角色英文名',
  `assignee_at` datetime DEFAULT NULL COMMENT '变更确认时间',
  `role_code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '可处理角色编码',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_archive_history_business_id_idx` (`business_id`) USING BTREE,
  KEY `na_archive_history_created_at_idx` (`created_at`) USING BTREE,
  KEY `na_archive_history_id_idx` (`id`) USING BTREE,
  KEY `na_archive_history_is_applied_idx` (`is_applied`) USING BTREE,
  KEY `na_archive_history_module_name_idx` (`module_name`) USING BTREE,
  KEY `na_archive_history_status_idx` (`status`) USING BTREE,
  KEY `na_archive_history_ver_idx` (`ver`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_archives_history
-- ----------------------------

-- ----------------------------
-- Table structure for na_attachment
-- ----------------------------
DROP TABLE IF EXISTS `na_attachment`;
CREATE TABLE `na_attachment` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `business_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `document_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `module_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `category_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='附件表';

-- ----------------------------
-- Records of na_attachment
-- ----------------------------

-- ----------------------------
-- Table structure for na_auto_code
-- ----------------------------
DROP TABLE IF EXISTS `na_auto_code`;
CREATE TABLE `na_auto_code` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `title` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '标题',
  `code` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '规则类型',
  `format` longtext COLLATE utf8_unicode_ci COMMENT '格式： 可变字串型，用于编码格式的模板内容（需要能解析：固定字符串、年、月、日、部门名（前几位）、用户名（前几位）、供应商Code、主累加值、从累加值；可用Freemarker方式解析）',
  `main_value` int(11) DEFAULT NULL COMMENT '主累加值： 整型，用于编码格式中累加值部分的记录；',
  `main_value_clean_rule` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '主累加号清0条件：可变字串型，在生成规则中使主累加号从1开始的判断条件；',
  `sub_value` int(11) DEFAULT NULL COMMENT '从累加值： 整型，用于编码格式中累加值部分的记录（备用字段）',
  `sub_value_clean_rule` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '从累加号清0条件：可变字串型，在生成规则中使从累加号从1开始的判断条件',
  `department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门id',
  `department_cn_name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门中文名',
  `department_en_name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门英文名',
  `description` longtext COLLATE utf8_unicode_ci COMMENT '描述',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `group_no` smallint(6) DEFAULT NULL COMMENT '组编号',
  `sort` smallint(6) DEFAULT NULL COMMENT '排序',
  `last_value` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='编号记录表';

-- ----------------------------
-- Records of na_auto_code
-- ----------------------------

-- ----------------------------
-- Table structure for na_case
-- ----------------------------
DROP TABLE IF EXISTS `na_case`;
CREATE TABLE `na_case` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL COMMENT 'id',
  `cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '中文名',
  `en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '英文名',
  `code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '脚本Code',
  `project_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '项目ID',
  `document_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '脚本文件',
  `document_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '脚本文件名',
  `category_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '分类ID',
  `context` text COLLATE utf8_unicode_ci COMMENT '脚本说明',
  `sort` int(11) DEFAULT NULL COMMENT '显示排序',
  `status` int(2) DEFAULT NULL COMMENT '记录状态 : 0草稿；1正常；2禁用；3删除',
  `creator_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `creator_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人中文名',
  `creator_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人英文名',
  `department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门ID',
  `department_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门中文名',
  `department_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门英文名',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `updated_index` int(11) DEFAULT NULL COMMENT '更改次数',
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `sort` (`sort`),
  KEY `status` (`status`),
  KEY `document_id` (`document_id`),
  KEY `creator_id` (`creator_id`),
  KEY `cn_name` (`cn_name`),
  KEY `en_name` (`en_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of na_case
-- ----------------------------
INSERT INTO `na_case` VALUES ('CASE20180712144926455234', 'test_login.py', 'test_login.py', 'case1', 'PROJ20180729162013978305', 'MDOC20180716142344600844', 'test_login.py', 'CASEC2018080516525145627', 'ssssssssss', null, '1', '1', null, null, 'DEP20180822000000110', null, null, '2018-08-12 14:49:26', '2018-10-06 04:35:40', '13');
INSERT INTO `na_case` VALUES ('CASE20180712150538841440', 'test_vendorcategory.py', 'test_vendorcategory.py', 'case12', 'PROJ20180729162013978305', 'MDOC20180719092924730393', 'test_vendorcategory.py', 'CASEC2018071413594285765', 'ssssssssss', null, '1', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-08-12 15:05:38', '2018-10-06 04:35:54', '14');
INSERT INTO `na_case` VALUES ('CASE20180712150648626547', 'test_flow_product_quotation', 'test_flow_product_quotation', 'CASE-FPQ-01', 'PROJ20180708152734951932', 'MDOC20180906134313628337', 'test_flow_product_quotation.py', 'CASEC2018080516050614493', 'ssssssssss', null, '1', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-08-12 15:06:48', '2018-10-06 13:45:00', '4');
INSERT INTO `na_case` VALUES ('CASE20180712150726301887', 'test_flow_purchase_plan', 'test_flow_purchase_plan', 'CaseFPP-01', 'PROJ20180708152734951932', 'MDOC20180906134239511626', 'test_flow_purchase_plan.py', 'CASEC2018080516531427482', 'ssssssssss', null, '1', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-08-12 15:07:26', '2018-10-06 13:50:13', '8');

-- ----------------------------
-- Table structure for na_case_category
-- ----------------------------
DROP TABLE IF EXISTS `na_case_category`;
CREATE TABLE `na_case_category` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `parent_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '父ID',
  `project_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '项目ID',
  `cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '脚本中文名',
  `en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '脚本英文名',
  `level` smallint(6) DEFAULT NULL COMMENT '层级',
  `leaf` smallint(6) DEFAULT NULL COMMENT '叶子',
  `status` smallint(6) DEFAULT NULL COMMENT '状态',
  `sort` bigint(20) DEFAULT NULL COMMENT '排序号',
  `path` text COLLATE utf8_unicode_ci COMMENT '路径',
  `creator_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `creator_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人中文名',
  `creator_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人英文名',
  `department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门ID',
  `department_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门中文名',
  `department_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门英文名',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `parent_id` (`parent_id`),
  KEY `cn_name` (`cn_name`),
  KEY `en_name` (`en_name`),
  KEY `status` (`status`),
  KEY `sort` (`sort`),
  KEY `project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of na_case_category
-- ----------------------------
INSERT INTO `na_case_category` VALUES ('CASEC2018071216204075350', null, 'PROJ20180729162013978305', 'a', 'a', '1', '0', '1', '1', null, '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-08-12 16:20:40', null);
INSERT INTO `na_case_category` VALUES ('CASEC2018071216270372156', 'CASEC2018071216204075350', 'PROJ20180729162013978305', 'b1', 'b1', '1', '0', '1', '1', 'CASEC2018071216204075350', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-08-12 16:27:03', null);
INSERT INTO `na_case_category` VALUES ('CASEC2018071216312234828', 'CASEC2018071216270372156', 'PROJ20180729162013978305', 'cccccccccccccc', 'ccccccccdd', '1', '1', '0', '1', 'CASEC2018071216204075350_CASEC2018071216270372156', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-08-12 16:31:22', null);
INSERT INTO `na_case_category` VALUES ('CASEC2018071216313024589', 'CASEC2018071216270372156', 'PROJ20180729162013978305', 'cccccccccccccc1', 'cccccccc2', '1', '1', '1', '3', 'CASEC2018071216204075350_CASEC2018071216270372156', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-08-12 16:31:30', null);
INSERT INTO `na_case_category` VALUES ('CASEC2018071413594285765', 'CASEC2018071216204075350', 'PROJ20180729162013978305', 'b2', 'b2', '0', '0', '1', '2', 'CASEC2018071216204075350', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-08-14 13:59:42', null);
INSERT INTO `na_case_category` VALUES ('CASEC2018071514212116176', 'CASEC2018071216204075350', 'PROJ20180729162013978305', 'b3', 'b3', '0', '0', '1', '3', 'CASEC2018071216204075350', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-08-15 14:21:21', null);
INSERT INTO `na_case_category` VALUES ('CASEC2018072816293522977', null, 'PROJ20180729162013978305', 'b', 'b', '0', '0', '1', '3', null, '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-08-28 16:29:35', null);
INSERT INTO `na_case_category` VALUES ('CASEC2018072917235717461', null, 'PROJ20180708152734951932', 'ccccc', 'ccccc', '0', '0', '1', '2', null, '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-08-29 17:23:57', null);
INSERT INTO `na_case_category` VALUES ('CASEC2018080516050614493', 'CASEC2018072917235717461', 'PROJ20180708152734951932', 'ccccc2', 'ccccc23', '1', '0', '1', '1', 'CASEC2018072917235717461', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-09-05 16:05:06', null);
INSERT INTO `na_case_category` VALUES ('CASEC2018080516525145627', 'CASEC2018071216270372156', 'PROJ20180729162013978305', 'cccccccccccccc13', 'cccccccc23', '1', '1', '1', '2', 'CASEC2018071216204075350_CASEC2018071216270372156', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-09-05 16:52:51', null);
INSERT INTO `na_case_category` VALUES ('CASEC2018080516531427482', 'CASEC2018072917235717461', 'PROJ20180708152734951932', 'ccccc23', 'ccccc233', '0', '1', '1', '2', 'CASEC2018072917235717461', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-09-05 16:53:14', null);

-- ----------------------------
-- Table structure for na_case_group
-- ----------------------------
DROP TABLE IF EXISTS `na_case_group`;
CREATE TABLE `na_case_group` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '群组中文名',
  `en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '群组英文名',
  `sort` int(11) DEFAULT NULL COMMENT '显示排序',
  `status` int(2) DEFAULT NULL COMMENT '记录状态 : 0草稿；1正常；2禁用；3删除',
  `creator_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `creator_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人中文名',
  `creator_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人英文名',
  `department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门ID',
  `department_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门中文名',
  `department_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门英文名',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of na_case_group
-- ----------------------------
INSERT INTO `na_case_group` VALUES ('CASEG2018071415293346863', '测试项目-开发测试站-01', '测试项目-开发测试站-01', null, '1', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-08-14 15:29:33', '2018-10-06 13:46:37');
INSERT INTO `na_case_group` VALUES ('CASEG2018071415294896490', '测试项目一 + 测试项目二', '测试项目一 + 测试项目二', null, '1', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-08-14 15:29:48', '2018-10-06 13:48:54');

-- ----------------------------
-- Table structure for na_case_group_union
-- ----------------------------
DROP TABLE IF EXISTS `na_case_group_union`;
CREATE TABLE `na_case_group_union` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `group_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '群组ID',
  `case_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用例ID',
  `projects_inst_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '项目实例ID',
  `data_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据ID',
  `sort` int(11) DEFAULT NULL COMMENT '显示排序',
  `status` int(2) DEFAULT NULL COMMENT '记录状态 : 0草稿；1正常；2禁用；3删除',
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `group_id` (`group_id`),
  KEY `case_id` (`case_id`),
  KEY `data_id` (`data_id`),
  KEY `sort` (`sort`),
  KEY `status` (`status`),
  KEY `projects_inst_id` (`projects_inst_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of na_case_group_union
-- ----------------------------
INSERT INTO `na_case_group_union` VALUES ('CASEU2018090613463728081', 'CASEG2018071415293346863', 'CASE20180712144926455234', 'PROI20180712102705200465', null, '2', '1');
INSERT INTO `na_case_group_union` VALUES ('CASEU2018090613463796142', 'CASEG2018071415293346863', 'CASE20180712150538841440', 'PROI20180712102705200465', null, '1', '1');
INSERT INTO `na_case_group_union` VALUES ('CASEU2018090613485447880', 'CASEG2018071415294896490', 'CASE20180712150726301887', 'PROI20180906133942807355', null, '1', '1');
INSERT INTO `na_case_group_union` VALUES ('CASEU2018090613485464468', 'CASEG2018071415294896490', 'CASE20180712150538841440', 'PROI20180712102705200465', null, '2', '1');
INSERT INTO `na_case_group_union` VALUES ('CASEU2018090613485467692', 'CASEG2018071415294896490', 'CASE20180712150648626547', 'PROI20180906133942807355', null, '3', '1');

-- ----------------------------
-- Table structure for na_contacts
-- ----------------------------
DROP TABLE IF EXISTS `na_contacts`;
CREATE TABLE `na_contacts` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `vendor_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cn_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `en_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gender` smallint(6) DEFAULT NULL,
  `title` smallint(6) DEFAULT NULL,
  `department` smallint(6) DEFAULT NULL,
  `skype` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `qq` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `extension` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `wechat` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mobile` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL,
  `shared` smallint(6) DEFAULT NULL,
  `remark` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator_cn_name` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator_en_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_cn_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_en_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `agent_company` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '代理公司',
  `port` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '港口',
  `type` smallint(6) DEFAULT NULL,
  `vendor_cn_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `vendor_en_name` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fax` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '传真',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_contacts
-- ----------------------------

-- ----------------------------
-- Table structure for na_cronjob
-- ----------------------------
DROP TABLE IF EXISTS `na_cronjob`;
CREATE TABLE `na_cronjob` (
  `id` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'id',
  `code` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '编码',
  `name` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '名称',
  `type` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '类型',
  `year` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '年份（1970-2099）',
  `day_of_week` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '日（星期）(0-6)',
  `month` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '月(0-11)',
  `day_of_month` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '日(0-31)',
  `hour` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '时(0-23)',
  `minute` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '分(0-59)',
  `second` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '秒(0-59)',
  `day_of_week_unit` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '日（星期）单位',
  `month_unit` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '月单位',
  `day_of_month_unit` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '日单位',
  `hour_unit` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '时单位',
  `minute_unit` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '分单位',
  `remind_type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '提醒类型： 提前、滞后',
  `role_codes` text COLLATE utf8_unicode_ci COMMENT '角色编码',
  `is_system` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否系统级',
  `status` smallint(6) DEFAULT NULL COMMENT '状态',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `run_params` text COLLATE utf8_unicode_ci,
  `run_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最近运行时间',
  `run_status` smallint(6) DEFAULT NULL COMMENT '最近运行状态：1报错,2正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='任务配置表';

-- ----------------------------
-- Records of na_cronjob
-- ----------------------------
INSERT INTO `na_cronjob` VALUES ('1', 'test', 'test', '2', null, null, null, null, null, null, null, null, null, null, null, null, '1', '1', '1', '1', null, null, '2018-10-02 23:54:04', null);

-- ----------------------------
-- Table structure for na_cronjob_logs
-- ----------------------------
DROP TABLE IF EXISTS `na_cronjob_logs`;
CREATE TABLE `na_cronjob_logs` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `cronjob_code` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `run_time` int(11) DEFAULT NULL,
  `run_status` smallint(2) DEFAULT NULL,
  `run_date_start` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `run_date_end` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `run_result` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `cronjob_code` (`cronjob_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of na_cronjob_logs
-- ----------------------------

-- ----------------------------
-- Table structure for na_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `na_dictionary`;
CREATE TABLE `na_dictionary` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `code_main` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `code_sub` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `category_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sort` smallint(6) DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL,
  `custom` smallint(6) DEFAULT NULL,
  `type` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_dictionary_code_main_idx` (`code_main`) USING BTREE,
  KEY `na_dictionary_code_sub_idx` (`code_sub`) USING BTREE,
  KEY `na_dictionary_id_idx` (`id`) USING BTREE,
  KEY `na_dictionary_sort_idx` (`sort`) USING BTREE,
  KEY `na_dictionary_status_idx` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_dictionary
-- ----------------------------
INSERT INTO `na_dictionary` VALUES ('DD201708130552138462', 'module_action', 'normal', 'DDC20170813040923198', '1', '1', '2', '2');
INSERT INTO `na_dictionary` VALUES ('DD201708130723182306', 'module_action', 'flow', 'DDC20170813040923198', '2', '1', '2', '2');
INSERT INTO `na_dictionary` VALUES ('DD201708130729231202', 'module_action', 'data', 'DDC20170813040923198', '3', '1', '2', '2');
INSERT INTO `na_dictionary` VALUES ('DD201708281220335539', 'setting_email', 'smtp_service', 'DDC20170814062954183', '1', '1', '2', '3');
INSERT INTO `na_dictionary` VALUES ('DD201709020831219184', 'setting_email', 'smtp_port', 'DDC20170814062954183', '2', '1', '2', '3');
INSERT INTO `na_dictionary` VALUES ('DD201709020834021501', 'setting_local', 'language', 'DDC20170814062954183', '1', '1', '2', '1');
INSERT INTO `na_dictionary` VALUES ('DD201709020836242090', 'setting_email', 'smtp_account', 'DDC20170814062954183', '3', '1', '2', '3');
INSERT INTO `na_dictionary` VALUES ('DD201709020837057161', 'setting_email', 'smtp_password', 'DDC20170814062954183', '4', '1', '2', '3');
INSERT INTO `na_dictionary` VALUES ('DD201709100851072504', 'workflow', 'history_status', 'DDC20170813040923198', '3', '1', '2', '1');
INSERT INTO `na_dictionary` VALUES ('DD201709160404001893', 'document', 'shared', 'DDC20170813040923198', '6', '1', '2', '1');
INSERT INTO `na_dictionary` VALUES ('DD2017093103324276353719', 'contacts', 'department', 'DDC20170902081717767', '2', '1', '1', '1');
INSERT INTO `na_dictionary` VALUES ('DD2017093104383968750542', 'contacts', 'position', 'DDC20170902081717767', '2', '1', '1', '1');
INSERT INTO `na_dictionary` VALUES ('DD2017101300344310130147', 'options', 'yesno', 'DDC20170902081747501', '1', '1', '1', '1');
INSERT INTO `na_dictionary` VALUES ('DD2017102303025719877149', 'options', 'status', 'DDC20170902081717767', '2', '1', '1', '1');
INSERT INTO `na_dictionary` VALUES ('DD2017102303080343984824', 'project', 'type', 'DDC20170902081717767', '20', '1', '1', '1');
INSERT INTO `na_dictionary` VALUES ('DD2017102303132195251300', 'system', 'email_box_type', 'DDC20170813040923198', '10', '1', '2', '1');
INSERT INTO `na_dictionary` VALUES ('DD2017110707064286350808', 'cronjob', 'remind_type', 'DDC20170813040923198', '30', '1', '2', '1');
INSERT INTO `na_dictionary` VALUES ('DD2017110709124389522687', 'cronjob', 'unit', 'DDC20170813040923198', '30', '1', '2', '1');
INSERT INTO `na_dictionary` VALUES ('DD2018021214204744943937', 'purchase', 'settlement_type', 'DDC20170902081549227', '44', '1', '1', '1');
INSERT INTO `na_dictionary` VALUES ('DD2018030601415367562931', 'product', 'sync', 'DDC20170902081717767', '42', '1', '1', '1');
INSERT INTO `na_dictionary` VALUES ('DICT20180707151247314704', 'project', 'run_status', 'DDC20170902081717767', '19', '1', '1', '1');

-- ----------------------------
-- Table structure for na_dictionary_category
-- ----------------------------
DROP TABLE IF EXISTS `na_dictionary_category`;
CREATE TABLE `na_dictionary_category` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `status` smallint(6) DEFAULT NULL,
  `sort` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_dictionary_category_id_idx` (`id`) USING BTREE,
  KEY `na_dictionary_category_sort_idx` (`sort`) USING BTREE,
  KEY `na_dictionary_category_status_idx` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_dictionary_category
-- ----------------------------
INSERT INTO `na_dictionary_category` VALUES ('DDC20170813040923198', '1', '1');
INSERT INTO `na_dictionary_category` VALUES ('DDC20170814062954183', '1', '2');
INSERT INTO `na_dictionary_category` VALUES ('DDC20170902081549227', '1', '3');
INSERT INTO `na_dictionary_category` VALUES ('DDC20170902081717767', '1', '4');
INSERT INTO `na_dictionary_category` VALUES ('DDC20170902081747501', '1', '5');

-- ----------------------------
-- Table structure for na_dictionary_category_desc
-- ----------------------------
DROP TABLE IF EXISTS `na_dictionary_category_desc`;
CREATE TABLE `na_dictionary_category_desc` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `category_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `title` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lang` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_dictionary_category_desc_dict_id_idx` (`category_id`) USING BTREE,
  KEY `na_dictionary_category_desc_id_idx` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_dictionary_category_desc
-- ----------------------------
INSERT INTO `na_dictionary_category_desc` VALUES ('DDCD2017082901502230', 'DDC20170814062954183', '全局配置项', 'zh_CN');
INSERT INTO `na_dictionary_category_desc` VALUES ('DDCD2017082901502288', 'DDC20170814062954183', 'Public Setting', 'en_AU');
INSERT INTO `na_dictionary_category_desc` VALUES ('DDCD2017090208182841', 'DDC20170902081549227', '可维护字段（流程）', 'zh_CN');
INSERT INTO `na_dictionary_category_desc` VALUES ('DDCD2017090208182895', 'DDC20170902081549227', 'Flow Fields', 'en_AU');
INSERT INTO `na_dictionary_category_desc` VALUES ('DDCD2017090208184755', 'DDC20170902081717767', 'Archive Fields', 'en_AU');
INSERT INTO `na_dictionary_category_desc` VALUES ('DDCD2017090208184786', 'DDC20170902081717767', '可维护字段（档案）', 'zh_CN');
INSERT INTO `na_dictionary_category_desc` VALUES ('DDCD2017091210254722', 'DDC20170902081747501', 'Other Fields', 'en_AU');
INSERT INTO `na_dictionary_category_desc` VALUES ('DDCD2017091210254744', 'DDC20170902081747501', '可维护字段（其它）', 'zh_CN');
INSERT INTO `na_dictionary_category_desc` VALUES ('DICTCD201805101713002563', 'DDC20170813040923198', 'System Category', 'en_AU');
INSERT INTO `na_dictionary_category_desc` VALUES ('DICTCD201805101713005116', 'DDC20170813040923198', '系统相关类', 'zh_CN');
INSERT INTO `na_dictionary_category_desc` VALUES ('DICTCD201807120750381100', 'DICTC2018071207503871037', '可维护字段（流程）', 'zh_CN');
INSERT INTO `na_dictionary_category_desc` VALUES ('DICTCD201807120750386300', 'DICTC2018071207503871037', 'Flow Fields', 'en_AU');

-- ----------------------------
-- Table structure for na_dictionary_desc
-- ----------------------------
DROP TABLE IF EXISTS `na_dictionary_desc`;
CREATE TABLE `na_dictionary_desc` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `dict_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `context` longtext COLLATE utf8_unicode_ci,
  `lang` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_dictionary_desc_dict_id_idx` (`dict_id`) USING BTREE,
  KEY `na_dictionary_desc_id_idx` (`id`) USING BTREE,
  KEY `na_dictionary_desc_lang_idx` (`lang`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_dictionary_desc
-- ----------------------------
INSERT INTO `na_dictionary_desc` VALUES ('DDD20170910085436104', 'DD201709100851072504', 'Flow History Status', null, 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD20170910085436977', 'DD201709100851072504', '流程历史状态', null, 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD20170916040400510', 'DD201709160404001893', '分享范围', null, 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD20170916040400832', 'DD201709160404001893', 'Shared Region', null, 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201709310439132320333', 'DD2017093104383968750542', '职位', '联系人职位', 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201709310439134793533', 'DD2017093104383968750542', 'position', null, 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201709310512041627861', 'DD2017093103324276353719', 'deparment', 'null', 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201709310512049481357', 'DD2017093103324276353719', '部门', '联系人部门', 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710100035517635118', 'DD201708130723182306', 'Permissions List (Flow)', null, 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710100035519379385', 'DD201708130723182306', '审批权限', '用于模块定义时，可以备选的权限项目审批类，如：“通过”、“拒绝” .......', 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710100036115448566', 'DD201708130729231202', 'Permissions List (Data)', null, 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710100036117948458', 'DD201708130729231202', '数据权限', '用于模块定义时，可以备选的数据权限项目，如：“自己”、“部门内” .......', 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710100037543134277', 'DD201708130552138462', '常规权限', '用于模块定义时，可以备选的权限项目，如：“增”、“删”、“改”、查 .......', 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710100037548976235', 'DD201708130552138462', 'Permissions List (normal)', '<font face=\"tahoma, arial, verdana, sans-serif\">Permissions List (normal)</font>', 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710130034432046756', 'DD2017101300344310130147', '是非选项', null, 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710130034436259144', 'DD2017101300344310130147', 'Yes or No', null, 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710230303114961777', 'DD2017102303025719877149', '状态', null, 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710230303118841216', 'DD2017102303025719877149', 'Status', null, 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710230308086505673', 'DD2017102303080343984824', '项目类型', null, 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710230308089773502', 'DD2017102303080343984824', 'Project Type', null, 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710230313215419584', 'DD2017102303132195251300', '邮箱类型', '<font face=\"tahoma, arial, verdana, sans-serif\">系统设定的邮箱类型</font>', 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710230313218070439', 'DD2017102303132195251300', 'Email box type', null, 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710301338379300942', 'DD201709020834021501', '本地化配置', '系统默认的本地化配置', 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710301338379801901', 'DD201709020834021501', 'Local Configuration', 'Local configuration.', 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710301338491657748', 'DD201709020836242090', 'SMTP 登录账号', 'SMTP 登录账号', 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710301338496509530', 'DD201709020836242090', 'SMTP User Name', 'SMTP User Name', 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710301338551870242', 'DD201709020837057161', 'SMTP 登录密码', 'SMTP 登录密码', 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710301338556155984', 'DD201709020837057161', 'SMTP Password', 'SMTP Password', 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710301339081386468', 'DD201709020831219184', 'SMTP服务端口', 'SMTP（发送邮件服务）服务端口', 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710301339083381905', 'DD201709020831219184', 'SMTP Port', 'Port for SMTP&nbsp;Service.', 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710301339172384654', 'DD201708281220335539', 'SMTP Service', 'Email Service.', 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201710301339177943412', 'DD201708281220335539', 'SMTP服务地址', 'SMTP服务地址', 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201801071321532097471', 'DD2017110707064286350808', '提醒类型', null, 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201801071321535567713', 'DD2017110707064286350808', 'Remind type', '<br>', 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201801071322081108526', 'DD2017110709124389522687', '提醒单位', null, 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201801071322087166560', 'DD2017110709124389522687', 'Remind type', '<br>', 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201802121420477336332', 'DD2018021214204744943937', '结算类型', null, 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201802121420478225380', 'DD2018021214204744943937', 'Settlement Type', null, 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201803060141534199848', 'DD2018030601415367562931', '是否同步', null, 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DDD201803060141536397907', 'DD2018030601415367562931', 'Sync', null, 'en_AU');
INSERT INTO `na_dictionary_desc` VALUES ('DICTD2018070715124710413', 'DICT20180707151247314704', '项目状态', null, 'zh_CN');
INSERT INTO `na_dictionary_desc` VALUES ('DICTD2018070715124768080', 'DICT20180707151247314704', 'Project Run Status', null, 'en_AU');

-- ----------------------------
-- Table structure for na_dictionary_value
-- ----------------------------
DROP TABLE IF EXISTS `na_dictionary_value`;
CREATE TABLE `na_dictionary_value` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `dict_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `value` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sort` smallint(6) DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL,
  `is_default` smallint(6) DEFAULT NULL,
  `custom` smallint(6) DEFAULT NULL,
  `param1` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '额外参数１',
  `param2` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '额外参数２',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_dictionary_item_default_idx` (`is_default`) USING BTREE,
  KEY `na_dictionary_item_id_idx` (`id`) USING BTREE,
  KEY `na_dictionary_item_sort_idx` (`sort`) USING BTREE,
  KEY `na_dictionary_item_status_idx` (`status`) USING BTREE,
  KEY `na_dictionary_item_sub_code_idx` (`dict_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_dictionary_value
-- ----------------------------
INSERT INTO `na_dictionary_value` VALUES ('DDV20170813061243184', 'DD201708130552138462', 'add', '2', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170813061451935', 'DD201708130552138462', 'edit', '3', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170813061630166', 'DD201708130552138462', 'del', '4', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170813062237325', 'DD201708130723182306', 'start', '1', '1', '1', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170813062517517', 'DD201708130723182306', 'allow', '2', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170813065015976', 'DD201708130723182306', 'refuse', '3', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170813065202202', 'DD201708130723182306', 'redo', '4', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170813073043457', 'DD201708130729231202', '1', '1', '1', '1', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170813073701742', 'DD201708130729231202', '2', '2', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170813074134435', 'DD201708130729231202', '4', '4', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170813080528949', 'DD201708130729231202', '3', '3', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170825053910738', 'DD201708130552138462', 'list', '1', '1', '1', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170904003048932', 'DD201708130723182306', 'back', '5', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170910085148333', 'DD201709100851072504', '1', '1', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170910085304178', 'DD201709100851072504', '2', '2', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170910085347985', null, '3', '3', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170910085507703', 'DD201709100851072504', '3', '3', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170910085525178', 'DD201709100851072504', '4', '4', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170910085608183', 'DD201709100851072504', '5', '5', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170911075812973', null, '3', '3', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170912112410977', null, '2', '2', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170912112428144', 'DD201709121123171557', '2', '2', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170916040446466', 'DD201709160404001893', '1', '1', '1', '1', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170916040551125', 'DD201709160404001893', '2', '2', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170916040619334', 'DD201709160404001893', '3', '3', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170920025730591', 'DD201709200253301769', '1', '1', '1', '1', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170920030008147', 'DD201709200253301769', '2', '2', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV20170920030053177', null, '3', '3', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201709310443583940693', 'DD2017093103324276353719', '1', '1', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201709310444164852221', 'DD2017093103324276353719', '2', '2', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201709310445026239005', 'DD2017093103324276353719', '3', '3', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201709310445427228424', 'DD2017093104383968750542', '1', '5', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201709310446004649720', null, '2', '2', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201709310446314615360', null, '3', '3', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201709310447145360084', 'DD2017093104383968750542', '2', '2', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201709310447394971171', 'DD2017093104383968750542', '3', '3', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201709310448368512589', 'DD2017093104383968750542', '4', '4', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710020638443138714', null, '3', '3', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710040708106582053', null, '2', '2', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710040709541787350', null, '7', '7', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710040720067977736', null, '9', '9', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710040735365772591', null, '4', '4', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710040735502918957', null, '4', '4', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710040736321326566', null, '6', '6', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710040737117932072', null, '8', '8', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710040738461762456', null, '11', '11', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710040754036587984', null, '2', '2', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710040754129592778', 'DD2017100407311385860904', '2', '2', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710040754372937869', null, '3', '3', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710040754491771119', 'DD2017100407311385860904', '3', '3', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710080800357193654', null, '4', '4', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710110104327491527', 'DD2017100407311385860904', '4', '4', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710130035047600456', 'DD2017101300344310130147', '1', '1', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710130035241085922', 'DD2017101300344310130147', '2', '2', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710140513328385041', null, '另存为', '8', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710230303497441279', 'DD2017102303025719877149', '1', '1', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710230304513248494', 'DD2017102303025719877149', '2', '2', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710230403434363732', 'DD2017102303132195251300', '0', '1', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710230404057652509', 'DD2017102303132195251300', '1', '2', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710230404315994064', 'DD2017102303132195251300', '2', '3', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710230405147296000', 'DD2017102303132195251300', '3', '4', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710230537443698423', null, '7', '7', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201710270550491299066', 'DD2017100407311385860904', '5', '5', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711011242272799153', null, '2', '2', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711020944503780529', null, '2', '2', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711020945295185751', null, '3', '3', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711020945477443409', null, '4', '4', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711070328126301171', 'DD201708130552138462', 'confirm', '5', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711080615331234706', null, '2', '2', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711180024471345634', 'DD201708130552138462', 'export', '6', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711180025095346751', 'DD201708130552138462', 'email', '7', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711230821231767556', 'DD201708130723182306', 'hold', '6', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711281030101146043', null, '15', '1', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711281030429476984', null, '16', '1', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711281031411176910', null, '1', '1', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711281037457662202', null, '1', '1', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711300325107969252', null, '6', '6', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711300326179782855', 'DD201709100851072504', '6', '6', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711300327546887922', 'DD201709100851072504', '7', '7', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711300329184424793', 'DD201708130723182306', 'unhold', '7', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711300329232219648', 'DD201708130723182306', 'cancel', '8', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201711300332047658032', 'DD201709100851072504', '8', '8', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201800230631147948586', null, '9', '9', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201801061136402216963', 'DD2017110709124389522687', '2', '1', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201801061136402385820', 'DD2017110707064286350808', '2', '1', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201801070216455525847', 'DD2017093104383968750542', '5', '5', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201801070217145333325', 'DD2017093104383968750542', '6', '6', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201801070218152374885', null, '7', '7', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201801070219068176750', 'DD2017093104383968750542', '7', '7', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201801071330442814864', 'DD2017110709124389522687', '1', '1', '1', '1', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201801071333318457194', 'DD2017110707064286350808', '1', '1', '1', '1', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201801280223396734571', 'DD2017093103324276353719', '4', '4', '1', '1', '1', '4', '4');
INSERT INTO `na_dictionary_value` VALUES ('DDV201801280224323872043', 'DD2017093103324276353719', '5', '5', '1', '1', '1', '销售部门', null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201802121421162878626', 'DD2018021214204744943937', '1', '1', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201802121421383699043', 'DD2018021214204744943937', '2', '2', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201802121421582651455', 'DD2018021214204744943937', '3', '3', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201802190828566812126', 'DD2017102303080343984824', '2', '2', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201802190829179618290', 'DD2017102303080343984824', '1', '1', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201802280551411494305', 'DD201709020837057161', '3', '1', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201802280551411914943', 'DD201709020831219184', '35', '1', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201802280551415696619', 'DD201709020836242090', '2', '1', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201802280551416465244', 'DD201709020834021501', 'zh_CN', '1', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201802280551417554899', 'DD201708281220335539', '1.1.2.2', '1', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201803060142224010221', 'DD2018030601415367562931', '1', '1', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201803060142521970173', 'DD2018030601415367562931', '2', '2', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201803060143097984348', null, '3', '3', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DDV201803060143503965659', 'DD2018030601415367562931', '3', '3', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DICTV2018070715074099600', 'DD2017102303025719877149', '0', '0', '1', '1', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DICTV2018070715092992279', 'DD2017102303025719877149', '3', '3', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DICTV2018070715202199471', 'DICT20180707151247314704', '1', '1', '1', '1', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DICTV2018070715214136751', 'DICT20180707151247314704', '2', '2', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DICTV2018070715221477338', 'DICT20180707151247314704', '3', '3', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DICTV2018070715230041867', 'DICT20180707151247314704', '4', '4', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DICTV2018070715232943564', 'DICT20180707151247314704', '5', '5', '1', '2', '1', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DICTV2018080717094113392', 'DD201708130552138462', 'run', '8', '1', '2', '2', null, null);
INSERT INTO `na_dictionary_value` VALUES ('DICTV2018080717104274907', 'DD201708130552138462', 'stop', '9', '1', '2', '2', null, null);

-- ----------------------------
-- Table structure for na_dictionary_value_desc
-- ----------------------------
DROP TABLE IF EXISTS `na_dictionary_value_desc`;
CREATE TABLE `na_dictionary_value_desc` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `dict_value_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `text` longtext COLLATE utf8_unicode_ci,
  `lang` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_dictionary_value_desc
-- ----------------------------
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017081306263123', 'DDV20170813062517517', '通过', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017081306263184', 'DDV20170813062517517', 'Agree', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017081306501572', 'DDV20170813065015976', '拒绝', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017081306501574', 'DDV20170813065015976', 'Refuse', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017081306523426', 'DDV20170813065202202', 'Redo', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017081306523496', 'DDV20170813065202202', '返审', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017081307370144', 'DDV20170813073701742', '部门内', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017081307370181', 'DDV20170813073701742', 'In Department', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017081308025252', 'DDV20170813074134435', '含禁用', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017081308025267', 'DDV20170813074134435', 'All (Include Disabled)', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017081308052811', 'DDV20170813080528949', 'All', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017081308052877', 'DDV20170813080528949', '全部', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017081308093117', 'DDV20170813062237325', '发启', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017081308093130', 'DDV20170813062237325', 'Submit', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017082505393066', 'DDV20170813061243184', '新建', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017082505393084', 'DDV20170813061243184', 'Add', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017082505394416', 'DDV20170813061451935', 'Edit', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017082505394441', 'DDV20170813061451935', '修改', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017082505395152', 'DDV20170813061630166', 'Delete', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017082505395170', 'DDV20170813061630166', '删除', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017082901504144', 'DDV20170813073043457', 'Self', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017082901504161', 'DDV20170813073043457', '自身', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017090400331711', 'DDV20170904003048932', 'Go Back', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017090400331741', 'DDV20170904003048932', '退回', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017090900504348', 'DDV20170909005043299', 'review', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017090900504378', 'DDV20170909005043299', '审批中', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017090900513715', 'DDV20170909005137255', '通过', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017090900513796', 'DDV20170909005137255', 'pass', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017090900534466', 'DDV20170909005344444', '拒绝', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017090900534469', 'DDV20170909005344444', 'Rejected', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091008534785', 'DDV20170910085347985', '返审', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091008534795', 'DDV20170910085347985', 'Redo', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091008550735', 'DDV20170910085507703', '返审', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091008550782', 'DDV20170910085507703', 'Redo', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091008552516', 'DDV20170910085525178', 'Pass', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091008552569', 'DDV20170910085525178', '通过', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091008560813', 'DDV20170910085608183', 'Refuse', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091008560815', 'DDV20170910085608183', '拒绝', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091008561529', 'DDV20170910085304178', '退回', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091008561532', 'DDV20170910085304178', 'Back', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091008562218', 'DDV20170910085148333', 'start', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091008562291', 'DDV20170910085148333', '发启', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091107461013', 'DDV20170911074610131', 'Exhibition', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091107461025', 'DDV20170911074610131', '展会', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091107485926', 'DDV20170911074851903', 'Internet', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091107485958', 'DDV20170911074851903', '互联网', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091107501416', 'DDV20170911075014880', '自主联系', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091107501418', 'DDV20170911075014880', 'Independent Contact', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091107571435', 'DDV20170911075714118', 'AUD', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091107571447', 'DDV20170911075714118', 'AUD', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091107574182', 'DDV20170911075741548', 'RMB', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091107574199', 'DDV20170911075741548', 'RMB', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091107581211', 'DDV20170911075812973', 'USD', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091107581256', 'DDV20170911075812973', 'USD', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091107584410', 'DDV20170911075844952', 'USD', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091107584419', 'DDV20170911075844952', 'USD', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091109304415', 'DDV20170911093027295', 'Other<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091109304480', 'DDV20170911093027295', '其它', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091209393711', 'DDV20170912093937228', 'K=A', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091209393720', 'DDV20170912093937228', 'K=A', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091211185442', 'DDV20170912093817713', 'A=A', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091211185495', 'DDV20170912093817713', 'A=A', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091211241040', 'DDV20170912112410977', '180g', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091211241044', 'DDV20170912112410977', '180g', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091604055150', 'DDV20170916040551125', '部门内', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091604055181', 'DDV20170916040551125', 'In the department', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091604061914', 'DDV20170916040619334', '所有人', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017091604061917', 'DDV20170916040619334', 'To All', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017092002573085', 'DDV20170920025730591', '<font face=\"tahoma, arial, verdana, sans-serif\">All_returnable</font>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017092002573094', 'DDV20170920025730591', '全部可退', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017092003005331', 'DDV20170920030053177', 'Partially_returnable', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017092003225624', 'DDV20170912093956739', 'K=K', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017092003225687', 'DDV20170912093956739', 'K=K', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017092003230546', 'DDV20170912112428144', '180g', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017092003230567', 'DDV20170912112428144', '180g', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017092301570552', 'DDV20170920030008147', 'Partially_returnable', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD2017092301570566', 'DDV20170920030008147', '部分可退', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20170931044600527445', 'DDV201709310446004649720', null, 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20170931044600677894', 'DDV201709310446004649720', '采购组长<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20170931044631171512', 'DDV201709310446314615360', null, 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20170931044631449524', 'DDV201709310446314615360', '质检组长<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171002063844109641', 'DDV201710020638443138714', '到岸前', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171002063844592319', 'DDV201710020638443138714', '到岸前', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004070810192985', 'DDV201710040708106582053', '广州<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004070810303570', 'DDV201710040708106582053', 'GUANGZHOU', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004070954632429', 'DDV201710040709541787350', 'SHENZHEN<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004070954672384', 'DDV201710040709541787350', '深圳', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004072006457201', 'DDV201710040720067977736', '新港', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004072006523606', 'DDV201710040720067977736', 'XINGANG<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073354786811', 'DDV201710040733545789934', '海运费用', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073354853443', 'DDV201710040733545789934', 'Freight Charges<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073414183052', 'DDV201710040734147979943', '本地费用', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073414442104', 'DDV201710040734147979943', 'Destination Charges<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073513544786', 'DDV201710040735137086084', '澳大利亚港口费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073513545715', 'DDV201710040735137086084', 'Australian Port Charges', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073536767820', 'DDV201710040735365772591', '订单交货费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073536848158', 'DDV201710040735365772591', 'Delivery Order Fee<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073550320411', 'DDV201710040735502918957', '订单交货费<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073550906806', 'DDV201710040735502918957', 'Delivery Order Fee', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073605607535', 'DDV201710040736051642190', '订单交货费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073605863083', 'DDV201710040736051642190', 'Delivery Order Fee', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073618765281', 'DDV201710040736187399121', 'Sea Cargo Automation Fee<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073618905272', 'DDV201710040736187399121', '海运自动化费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073632154901', 'DDV201710040736321326566', 'CMR合规费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073632660723', 'DDV201710040736321326566', 'CMR Compliance Fee<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073646193240', 'DDV201710040736464130031', 'CMR合规费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073646574196', 'DDV201710040736464130031', 'CMR Compliance Fee', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073700720353', 'DDV201710040737007687069', '空集装箱通知费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073700956710', 'DDV201710040737007687069', 'Empty Container Notification Fee', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073711156054', 'DDV201710040737117932072', '车站预订费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073711178495', 'DDV201710040737117932072', 'Slot Booking Fee<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073725476668', 'DDV201710040737253663980', 'Slot Booking Fee', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073725715016', 'DDV201710040737253663980', '车站预订费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073745281044', 'DDV201710040737453565182', '码头基建费<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073745809750', 'DDV201710040737453565182', 'Wharf Infrastructure Fee<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073802157532', 'DDV201710040738021103744', '检疫费用', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073802978301', 'DDV201710040738021103744', 'Quarantine Assesment Fee', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073846875590', 'DDV201710040738461762456', '清关费（最多5行）<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004073846971377', 'DDV201710040738461762456', 'Customs Clearance Fee (up to 5 lines)', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004074045925953', 'DDV201710040739189215726', '清关费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004074045997369', 'DDV201710040739189215726', 'Customs Clearance Fee', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004075114182683', 'DDV201710040751149108715', '运输燃油附加费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004075114571260', 'DDV201710040751149108715', 'Transport Fuel Surcharge', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004075204118454', 'DDV201710040752043014982', '20\'GP', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004075204338665', 'DDV201710040752043014982', '20\'GP', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004075403185447', 'DDV201710040754036587984', 'per H/B<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004075403682781', 'DDV201710040754036587984', 'per H/B', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004075437160004', 'DDV201710040754372937869', '每次', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171004075437262919', 'DDV201710040754372937869', 'per entry<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171007084411574031', 'DDV201710040721035106389', '墨尔本', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171007084411734260', 'DDV201710040721035106389', 'MELBOURNE<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171008045945180248', 'DDV201710080459451507784', '合同报价', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171008045945540718', 'DDV201710080459451507784', 'Contract', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171008060648959641', 'DDV201710080606485998187', '特殊报价', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171008060648988287', 'DDV201710080606485998187', 'Special<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171008080035300483', 'DDV201710080800357193654', '其他', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171008080035970701', 'DDV201710080800357193654', '其他', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171009001241186614', 'DDV201710090012419217850', '其它', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171009001241822311', 'DDV201710090012419217850', 'Other', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171009001315132030', 'DDV201710090013153374332', '供应商联系人', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171009001315630379', 'DDV201710090013153374332', 'Vendor', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171009001411448726', 'DDV201710090014117811798', '服务商联系人', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171009001411553479', 'DDV201710090014117811798', 'Service', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171009084619209073', 'DDV201710090846196111622', '海运', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171009084619344378', 'DDV201710090846196111622', 'Sea', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171009104553150464', 'DDV201710080607041057506', 'Order Quote', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171009104553697138', 'DDV201710080607041057506', '订单报价', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171011005408692369', 'DDV201710040754491771119', 'per entry', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171011005408822276', 'DDV201710040754491771119', '每次清关', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171011011126422051', 'DDV201710040754129592778', 'per H/B', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171011011126489112', 'DDV201710040754129592778', '每个订单', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171011011158337885', 'DDV201710110104327491527', 'per Shpmt<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171011011158587463', 'DDV201710110104327491527', '每次发运', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171013003504369780', 'DDV201710130035047600456', '是', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171013003504927863', 'DDV201710130035047600456', 'YES', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171014051332250645', 'DDV201710140513328385041', null, 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171014051332299534', 'DDV201710140513328385041', null, 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171016030753251291', 'DDV201710160258098809932', '服务商<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171016030753280610', 'DDV201710160258098809932', '服务商', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171017060535420577', 'DDV20170825053910738', '查看', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171017060535577272', 'DDV20170825053910738', 'list data', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171018061421243133', 'DDV20170916040446466', 'No Share', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171018061421465990', 'DDV20170916040446466', '不分享', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171021024631510854', 'DDV201710210246319182555', 'Send Spare Parts', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171021024631530199', 'DDV201710210246319182555', '发零部件', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171021024717356245', 'DDV201710210247136719778', '发整箱', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171021024717747199', 'DDV201710210247136719778', 'Send The Whole Case', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023030349283878', 'DDV201710230303497441279', '正常', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023030349315735', 'DDV201710230303497441279', 'General', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023030451130252', 'DDV201710230304513248494', '禁用', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023030451849146', 'DDV201710230304513248494', 'Disabled', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023040343845077', 'DDV201710230403434363732', '草稿箱', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023040343868661', 'DDV201710230403434363732', 'Drafts Box', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023040405155826', 'DDV201710230404057652509', 'In Box', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023040405580636', 'DDV201710230404057652509', '收件箱', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023040431200098', 'DDV201710230404315994064', 'Sent Box', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023040431639479', 'DDV201710230404315994064', '发件箱', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023040514583645', 'DDV201710230405147296000', '垃圾箱', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023040514623166', 'DDV201710230405147296000', 'Dustbin Box', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023052711149867', 'DDV201710230527111892887', 'Item RTS due to courier issue/unknown reason(Count)', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023052711171260', 'DDV201710230527111892887', '产品退回', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023052829114341', 'DDV201710230528297118099', '产品丢失 (Australia Post)', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023052829455110', 'DDV201710230528297118099', 'Item lost in transit - Australia Post(Count)', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023052846472074', 'DDV201710230527392825726', 'Item lost in transit - Allied Express', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023052846591736', 'DDV201710230527392825726', '产品丢失 (Allied)', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023053302106952', 'DDV201710230533021188860', 'Item lost in transit - Fastway(Count)', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023053302647974', 'DDV201710230533021188860', '产品丢失 (Fastway)', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023053559253386', 'DDV201710230535591848218', 'Item delayed in transit', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023053559746559', 'DDV201710230535591848218', '派送延迟', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023053744299691', 'DDV201710230537443698423', ' 产品损坏', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023053744395159', 'DDV201710230537443698423', 'Item lost in transit - Direct', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023053831181050', 'DDV201710230538318093676', '取消订单', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023053831279025', 'DDV201710230538318093676', 'Order cancelled', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023053851336734', 'DDV201710230538519041281', 'RTS due to incorrect/insufficent address', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023053851994515', 'DDV201710230538519041281', '地址有误', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023053909464526', 'DDV201710230539091825075', '客人买错', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023053909863713', 'DDV201710230539091825075', 'Customer ordered incorrectly', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023053928517857', 'DDV201710230539288396400', '支付问题', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023053928778914', 'DDV201710230539288396400', 'Checkout and payment', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023053951589881', 'DDV201710230539516133686', 'Price difference/discount voucher', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023053951817264', 'DDV201710230539516133686', '价差', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054345295934', 'DDV201710230541309712468', 'Wrong item sent', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054345782357', 'DDV201710230541309712468', '产品发错', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054407461615', 'DDV201710230542237993545', ' 配件发错', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054407914595', 'DDV201710230542237993545', 'Wrong spare part sent', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054428118481', 'DDV201710230542423608395', 'Item not dispatched', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054428276411', 'DDV201710230542423608395', '产品未发', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054446154527', 'DDV201710230544467042173', '缺货', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054446171629', 'DDV201710230544467042173', 'Out of stock', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054504695607', 'DDV201710230545043605097', 'Item not as described/product information', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054504918647', 'DDV201710230545043605097', '描述不符', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054521142195', 'DDV201710230545212842913', '非配送区域', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054521713965', 'DDV201710230545212842913', 'No delivery area', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054548241409', 'DDV201710230545484630209', 'System error', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054548872957', 'DDV201710230545484630209', '系统错误', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054606166191', 'DDV201710230546064883577', '诈骗订单', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054606317113', 'DDV201710230546064883577', 'Fraudulent purchase', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054622113653', 'DDV201710230546224626564', '沟通不良', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054622889250', 'DDV201710230546224626564', 'Poor communication', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054742175089', 'DDV201710230547421273411', '配件丢失或损坏', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054742340752', 'DDV201710230547421273411', 'Missing or damaged parts', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054802142431', 'DDV201710230548027348373', '不能使用', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171023054802388082', 'DDV201710230548027348373', 'Item not functioning', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171024015143135377', 'DDV201710240151331748280', '待支付', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171024015143176461', 'DDV201710240151331748280', 'Unpaid', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171024015301270003', 'DDV201710240153011221989', '支付中', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171024015301364717', 'DDV201710240153011221989', 'Processing', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171024015330188471', 'DDV201710240153305085234', 'Complete', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171024015330677319', 'DDV201710240153305085234', '已支付', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171027055109412833', 'DDV201710270550491299066', 'per vendor<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171027055109543521', 'DDV201710270550491299066', '每供应商', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171028115239566728', 'DDV201710130035241085922', 'NO<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171028115239732986', 'DDV201710130035241085922', '否', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171029020915367094', 'DDV201710290208551149906', '拒绝', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171029020915378086', 'DDV201710290208551149906', 'Reject', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171101124215821419', 'DDV201711011242155307842', '室内', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171101124215838152', 'DDV201711011242155307842', 'Indoor<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171101124227362680', 'DDV201711011242272799153', '室外', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171101124227808154', 'DDV201711011242272799153', 'Outdoor<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171101124248109402', 'DDV201711011242481492073', '室外', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171101124248188697', 'DDV201711011242481492073', 'Outdoor', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171102094450448533', 'DDV201711020944503780529', 'EPARCEL', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171102094450818844', 'DDV201711020944503780529', 'EPARCEL<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171102094505190460', 'DDV201711020945058035864', 'EPARCEL', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171102094505672986', 'DDV201711020945058035864', 'EPARCEL', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171102094520457644', 'DDV201711020944201021881', 'C4LETTER', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171102094520810733', 'DDV201711020944201021881', 'C4LETTER', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171102094529265135', 'DDV201711020945295185751', 'FASTWAY', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171102094529845037', 'DDV201711020945295185751', 'FASTWAY<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171102094539163519', 'DDV201711020945391759028', 'FASTWAY', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171102094539707168', 'DDV201711020945391759028', 'FASTWAY', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171102094547165982', 'DDV201711020945477443409', 'ALLIED<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171102094547915124', 'DDV201711020945477443409', 'ALLIED', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171102094554147419', 'DDV201711020945546126398', 'ALLIED', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171102094554234182', 'DDV201711020945546126398', 'ALLIED', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171107032925160478', 'DDV201711070328126301171', '审核', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171107032925711071', 'DDV201711070328126301171', 'Confirm', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171108061533134359', 'DDV201711080615331234706', '此类产品在过去五年类是否有召回<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171108061533249861', 'DDV201711080615331234706', 'Has this item or any similar one recalled before in last 5 years?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171118002447118047', 'DDV201711180024471345634', '导出', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171118002447285415', 'DDV201711180024471345634', 'Export', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171118002509176186', 'DDV201711180025095346751', '邮件', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171118002509790668', 'DDV201711180025095346751', 'Email', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171119055436977393', 'DDV201711190554361601978', 'Issue A', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171119055436995394', 'DDV201711190554361601978', '问题A', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171119055515597332', 'DDV201711190555152287272', 'Issue C', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171119055515785299', 'DDV201711190555152287272', '问题C', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171119055522155851', 'DDV201711190554571606903', 'Issue B', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171119055522507810', 'DDV201711190554571606903', '问题B', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171119055541663601', 'DDV201711190555412007757', '问题D', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171119055541726797', 'DDV201711190555412007757', 'Issue D', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171119055600518222', 'DDV201711190556001796468', 'Issue E', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171119055600876301', 'DDV201711190556001796468', '问题E', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171119055620366802', 'DDV201711190556203349757', '问题F', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171119055620820801', 'DDV201711190556203349757', 'Issue F', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171119055641140762', 'DDV201711190556414126058', null, 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171119055641930349', 'DDV201711190556414126058', '问题G', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171121104957941815', 'DDV201710290208128343093', '待质检', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171121104957957471', 'DDV201710290208128343093', 'Pending', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171121105006284560', 'DDV201710290207263768754', '已质检', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171121105006754323', 'DDV201710290207263768754', 'Approve', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171123122329142111', 'DDV201711110022429674542', null, 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171123122329735694', 'DDV201711110022429674542', '熏蒸费用', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103010128595', 'DDV201711281030101146043', 'Xiamen', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103010497361', 'DDV201711281030101146043', '厦门', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103042196965', 'DDV201711281030429476984', '中山', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103042537014', 'DDV201711281030429476984', 'Zhongshan', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103141568164', 'DDV201711281031411176910', 'NA', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103141708919', 'DDV201711281031411176910', 'NA', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103745367913', 'DDV201711281037457662202', 'Nanjing', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103745521139', 'DDV201711281037457662202', '南京', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103811133954', 'DDV201710040720361157890', 'Ningbo', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103811252444', 'DDV201710040720361157890', '宁波', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103820658518', 'DDV201710040720214390314', 'Fuzhou', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103820857128', 'DDV201710040720214390314', '福州', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103826703541', 'DDV201710040719504522609', 'Guangzhou', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103826952397', 'DDV201710040719504522609', '广州', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103838245658', 'DDV201710040719347013439', 'Huangpu', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103838898292', 'DDV201710040719347013439', '黄埔', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103848236302', 'DDV201710040708368863187', '南昌', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103848611571', 'DDV201710040708368863187', 'Nanchang', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103858350713', 'DDV201710040707591689947', '南通', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128103858406219', 'DDV201710040707591689947', 'Nantong', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104035245111', 'DDV201710040709433817787', 'Qingdao', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104035857273', 'DDV201710040709433817787', '青岛', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104111165800', 'DDV201710040709025497171', '上海', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104111359709', 'DDV201710040709025497171', 'Shanghai', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104135173446', 'DDV201711281025388815846', '汕头', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104135840095', 'DDV201711281025388815846', 'shantou', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104142114706', 'DDV201711281026234232200', 'Shenzhen', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104142537976', 'DDV201711281026234232200', '深圳', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104254787303', 'DDV201711281029117919267', 'Shunde', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104254877552', 'DDV201711281029117919267', '顺德', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104312150422', 'DDV201711281029497528920', '天津', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104312286586', 'DDV201711281029497528920', 'Tianjing', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104415412976', 'DDV201711281044035435017', '厦门', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104415704018', 'DDV201711281044035435017', 'Xiamen', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104436381351', 'DDV201711281031135654822', '中山', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104436545738', 'DDV201711281031135654822', 'Zhongshan', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104446276367', 'DDV201711281032057772974', 'NA', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104446654782', 'DDV201711281032057772974', 'NA', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104502523080', 'DDV201711281032359396173', '江苏', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104502690126', 'DDV201711281032359396173', 'Jiangsu', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104518150515', 'DDV201711281032581899739', '佛山', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104518846305', 'DDV201711281032581899739', 'Foshan', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104533175598', 'DDV201711281033269884450', '江门', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104533273761', 'DDV201711281033269884450', 'Jiangmen', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104552353356', 'DDV201711281035351725170', '常州', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104552629787', 'DDV201711281035351725170', 'Changzhou', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104621530720', 'DDV201711281035535379265', 'Wuhan', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171128104621691433', 'DDV201711281035535379265', '武汉', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171130032510637473', 'DDV201711300325107969252', 'Frozen', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171130032510885718', 'DDV201711300325107969252', '冻结', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171130033021313153', 'DDV201711300329232219648', 'Cancellation', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171130033021764426', 'DDV201711300329232219648', '作废', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171130033204738679', 'DDV201711300332047658032', '作废', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171130033204979508', 'DDV201711300332047658032', 'Cancellation', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171130034457262509', 'DDV201711300329184424793', '解冻', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171130034457455716', 'DDV201711300329184424793', 'Unhold', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171130034511720124', 'DDV201711230821231767556', '冻结', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171130034511788315', 'DDV201711230821231767556', 'Hold', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171130034553124751', 'DDV201711300327546887922', 'Unhold', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171130034553268876', 'DDV201711300327546887922', '解冻', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171130034602200283', 'DDV201711300326179782855', 'Hold', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20171130034602589468', 'DDV201711300326179782855', '冻结', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180004004819293727', 'DDV201711281036246794578', 'LiangYuGang', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180004004819503339', 'DDV201711281036246794578', '连云港', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180004004830192543', 'DDV201711281036531060343', 'NanSha', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180004004830722405', 'DDV201711281036531060343', '南沙', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180004004851284555', 'DDV201710040709293127947', 'Port Klang, Malaysia', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180004004851366970', 'DDV201710040709293127947', '巴生港,马来西亚', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180004004908431593', 'DDV201711281037219504200', '芜湖', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180004004908438592', 'DDV201711281037219504200', 'Wuhu', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180004004916307421', 'DDV201711281047289746954', '南京', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180004004916425524', 'DDV201711281047289746954', 'Nanjing', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180005021252155470', 'DDV201710161336041409707', '供应商押金', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180005021252413836', 'DDV201710161336041409707', 'Bond', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180010011957655302', 'DDV201800100119578920185', 'Sample', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180010011957850518', 'DDV201800100119578920185', '样品采购费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180010012015229690', 'DDV201710160257386244804', 'Vendor', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180010012015706068', 'DDV201710160257386244804', '供应商', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180011122201142226', 'DDV201800111222015739817', 'Fixed', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180011122201358784', 'DDV201800111222015739817', '固定值', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180011122231183272', 'DDV201800111222317267811', '百分比', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180011122231520929', 'DDV201800111222317267811', 'Percent', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180016134318494518', 'DDV20170909004950569', '调整申请', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180016134318771030', 'DDV20170909004950569', 'Adjust Apply&nbsp;', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180017063359106633', 'DDV201710020637482827114', 'before Shipment', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180017063359477035', 'DDV201710020637482827114', '开船前', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180017063516587643', 'DDV201710020638165433488', 'against BL copy', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180017063516643822', 'DDV201710020638165433488', '获得提单COPY件', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180017063559226829', 'DDV201710020639026825649', 'after ETA', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180017063559599375', 'DDV201710020639026825649', '到岸后', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023061857492450', 'DDV201711080623246405997', '这个产品是否需要强制标准（纺织品的care label 除外）<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023061857513593', 'DDV201711080623246405997', 'Does this item have any mandatory standards (apart from fabric care label)?<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062043611846', 'DDV201711080624189554002', '这个产品是否已被禁止？<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062043828182', 'DDV201711080624189554002', 'Is this or has this item been banned?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062112294290', 'DDV201711080621165851348', 'Does this item need a fabric care label?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062112430759', 'DDV201711080621165851348', '这个产品是否需要纺织品的care label?', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062602581587', 'DDV201800230620212577654', '是否电器类产品？<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062602921744', 'DDV201800230620212577654', 'Is it an electrical equipment?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062607386244', 'DDV201800230621428789066', 'Is this item in prescribed electrical equipment list?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062607560014', 'DDV201800230621428789066', '这个产品是否在电气类强制认证名单中？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062615107450', 'DDV201800230622126950357', 'Does this item need Australian Gas Association (AGA) registration?<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062615184910', 'DDV201800230622126950357', '这个产品是否需要澳大利亚煤气协会（AGA）注册？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062634402401', 'DDV201800230622467161730', '这个产品是否需要水印注册？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062634610933', 'DDV201800230622467161730', 'Does this item need water mark registration?<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062638770942', 'DDV201800230623119729250', '这个产品是否需要水效能标签（WELS）？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062638855968', 'DDV201800230623119729250', 'Does this item need Water Efficiency Labelling and Standard (WELS)?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062644358233', 'DDV201800230623354749001', '这个产品是否需要最低效能标准标签？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062644596170', 'DDV201800230623354749001', 'Does this item need Minimum Energy Performance Standards (MEPS)?<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062655641821', 'DDV201800230624033098311', 'Does this item contain any refrigerant gas?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062655850625', 'DDV201800230624033098311', '这个产品是否含有冷媒？<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062659559534', 'DDV201800230624382242170', '这个产品是否需要任何其他政府登记？<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062659805487', 'DDV201800230624382242170', 'Does it need any other registration from government?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062706267506', 'DDV201800230625054434810', '这个产品在澳洲是否有专利？<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062706590617', 'DDV201800230625054434810', 'Does this item have patent in Australia <br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062710412441', 'DDV201800230625331489220', '这个产品是否需要SAA证书？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023062710588881', 'DDV201800230625331489220', 'Does it need SAA certificate', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063114130325', 'DDV201800230631147948586', 'Does this item contain any chemical?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063114598503', 'DDV201800230631147948586', '这个产品是否含有任何化学物质？<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063824412035', 'DDV201711080614531517180', 'Has this item or any similar one recalled before in last 5 years?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063824986738', 'DDV201711080614531517180', '这个产品的类似产品是否在过去5年中发生过产品召回？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063829154926', 'DDV201711080615553808816', 'Is it a product for babies (under 36 months)?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063829760511', 'DDV201711080615553808816', '这个产品是否婴儿产品（36个月以下）？<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063834240776', 'DDV201711080616495511650', '这个产品是否儿童产品（36个月以上，15岁以下）？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063834351194', 'DDV201711080616495511650', 'Is it a product for Kids (over 36 months, under age 15)?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063839262061', 'DDV201711080617447004706', '这个产品是否针对60岁以上使用者？<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063839852817', 'DDV201711080617447004706', 'Is it a product targeting age above 60?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063854417851', 'DDV201711080618491179383', '这个产品是否针对残疾人？<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063854779442', 'DDV201711080618491179383', 'Is it a product targeting for disability?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063858253719', 'DDV201711080620017374601', 'Is it a product related to any medical uses?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063858699409', 'DDV201711080620017374601', '这个产品是否与医疗相关？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063904522772', 'DDV201800230630471380294', 'Does this item aim for cook or storage foods?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063904865613', 'DDV201800230630471380294', '这个产品是否用于烹饪或储存食物？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063908116907', 'DDV201800230629565083185', '这个产品是否含有任何可燃气体/液体？<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063908373487', 'DDV201800230629565083185', 'Does this item contain any flammable gas/liquids?<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063911215710', 'DDV201800230632146056628', '这个产品是否含有任何化学物质？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063911305439', 'DDV201800230632146056628', 'Does this item contain any chemical?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063917265636', 'DDV201800230632441967624', 'Does this item use with gas?<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063917567619', 'DDV201800230632441967624', '这个产品是否需要使用液化石油汽？<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063921211964', 'DDV201800230633131766738', '这个产品是否用在水中？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063921558348', 'DDV201800230633131766738', 'Does this item use with water?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063929901840', 'DDV201800230633449514927', 'Does this item use with electric?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063929962260', 'DDV201800230633449514927', '这个产品是否用电？<br>', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063945542283', 'DDV201800230634246441624', 'Does this item use with any flammable gas/liquids?<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063945970863', 'DDV201800230634246441624', '这个产品是否使用任何可燃气体/液体？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063953467307', 'DDV201800230634491912123', 'Does this item have suggested weight capacity?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063953685697', 'DDV201800230634491912123', '这个产品是否有建议承重能力？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063957640713', 'DDV201800230635131141028', 'Is it a health related product?<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023063957697215', 'DDV201800230635131141028', '这是否是一个健康相关的产品？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023064001102502', 'DDV201800230635376018818', '这个产品是否声称或建议在工业或商业范围使用？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023064001162843', 'DDV201800230635376018818', 'Does this item claimed or recommended for industrial or commercial use?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023064007129813', 'DDV201800230636029289096', '这个产品是否超过35kg?', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023064007190335', 'DDV201800230636029289096', 'Does this item weight over 35 kg?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023064013236141', 'DDV201800230636381423935', '这个产品是否让使用者在路面上使用？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023064013941904', 'DDV201800230636381423935', 'Does this item let user to use it on the road?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023064020159057', 'DDV201800230637019997516', '这个产品是否含有木质部件？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023064020815137', 'DDV201800230637019997516', 'Does this item contain wooden parts?', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023064026109913', 'DDV201800230637347169690', 'Other commend from Compliance manager/officer?<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023064026556164', 'DDV201800230637347169690', '风控经理/风控员的其他评价？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023064034169013', 'DDV201800230637557380292', 'Does this item have patent in Overseas<br>', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023064034522520', 'DDV201800230637557380292', '这个产品在海外是否有专利？', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072026741790', 'DDV201800230720269088825', 'Communication', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072026772271', 'DDV201800230720269088825', '说服教育', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072049617512', 'DDV201710210245054005981', 'Full Refund', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072049916222', 'DDV201710210245054005981', '全额退款', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072416615973', 'DDV201800230724169899039', 'No return', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072416721096', 'DDV201800230724169899039', '没退货', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072523451921', 'DDV201800230725234808712', 'Allied pick-up', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072523702299', 'DDV201800230725234808712', 'Allied pick-up', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072544400770', 'DDV201800230725448015250', '6K label', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072544446392', 'DDV201800230725448015250', '6K label', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072601230119', 'DDV201800230726016228677', 'RTS', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072601982686', 'DDV201800230726016228677', 'RTS', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072627184858', 'DDV201800230726271013391', 'Customer\'s own', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072627535048', 'DDV201800230726271013391', 'Customer\'s own', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072704729353', 'DDV201800230727043017360', '没检验', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072704775928', 'DDV201800230727043017360', 'No inspection', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072724165660', 'DDV201800230727241047577', 'Disposal', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072724950581', 'DDV201800230727241047577', 'Disposal', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072750280784', 'DDV201800230727507314904', 'Restocked', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072750816410', 'DDV201800230727507314904', 'Restocked', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072811287069', 'DDV201800230728114123035', '2nd hand dealer', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072811931352', 'DDV201800230728114123035', '2nd hand dealer', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072833530433', 'DDV201800230728336130910', 'Report to Compliance', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023072833936575', 'DDV201800230728336130910', 'Report to Compliance', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023130321602782', 'DDV201800231303213192310', 'Spare part purchased', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180023130321613933', 'DDV201800231303213192310', 'Spare part purchased', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180029004503195411', 'DDV201710040752223958853', '40\'HQ', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180029004503934017', 'DDV201710040752223958853', '40\'HQ', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180029004513106903', 'DDV201710040752311389596', 'LCL', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180029004513188005', 'DDV201710040752311389596', 'LCL', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180029004605187157', 'DDV201710040752138630072', '40\'GP', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180029004605521439', 'DDV201710040752138630072', '40\'GP', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180029105653419243', 'DDV201709310447145360084', '采购组长', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180029105653923540', 'DDV201709310447145360084', 'Purchase Leader', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180029105750882295', 'DDV201709310448368512589', '财务', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180029105750930365', 'DDV201709310448368512589', 'Finance', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180029110302459935', 'DDV201709310447394971171', 'Quality Inspection Leader', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180029110302904516', 'DDV201709310447394971171', '质检组长', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180029110308494932', 'DDV201709310445427228424', '采购经理', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180029110308635309', 'DDV201709310445427228424', 'Purchase Manager', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180029110437331761', 'DDV201709310444164852221', '运输部门', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180029110437742890', 'DDV201709310444164852221', 'Transport Sector', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102024825187035', 'DDV201710080758054445623', 'Service', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102024825507540', 'DDV201710080758054445623', '服务商', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102024827829064', 'DDV201710080800114542256', 'Contract Balance Payment', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102024827960835', 'DDV201710080800114542256', '合同尾款', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025207145599', 'DDV201801020252071414049', ' Fumigation Charge', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025207658425', 'DDV201801020252071414049', '熏蒸费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025253288591', 'DDV201801020252538419401', 'Pre-paid Charge', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025253803416', 'DDV201801020252538419401', '代付费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025301807775', 'DDV201801020252377405962', 'LCL Charge', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025301967814', 'DDV201801020252377405962', '拼柜费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025328113604', 'DDV201801020253284890281', '样品费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025328499199', 'DDV201801020253284890281', 'Sample Charge', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025347145767', 'DDV201801020253477458731', null, 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025347564818', 'DDV201801020253477458731', '认证费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025400311356', 'DDV201801020254006166633', 'Spare Parts Fee', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025400978457', 'DDV201801020254006166633', '配件费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025514551389', 'DDV201801020255141490990', '额外的拖车费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025514554464', 'DDV201801020255141490990', 'Extra Trucking Fee', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025529102189', 'DDV201801020255291625689', '模具费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025529383145', 'DDV201801020255291625689', 'Mould Fee', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025541413315', 'DDV201801020255419144046', ' Dyeing Surcharge', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025541793129', 'DDV201801020255419144046', '小缸费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025605143262', 'DDV201801020256053547607', 'Photographic Charge', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025605945903', 'DDV201801020256053547607', '拍照费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025620397215', 'DDV201801020256203161991', '延误交期费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025620741459', 'DDV201801020256203161991', 'Production Delay Penalty', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025636532699', 'DDV201801020256367916914', '不良品扣款', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025636655883', 'DDV201801020256367916914', 'Defective Product Penalty', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025659535520', 'DDV201801020256591962830', '其他', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102025659674707', 'DDV201801020256591962830', 'Other', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102084517151717', 'DDV201710090846381699683', 'Air', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180102084517651872', 'DDV201710090846381699683', '空运', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180106092155416855', 'DDV20170923055234789', '黄（五抽一）', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180106092155523106', 'DDV20170923055234789', 'Yellow', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180106092224229664', 'DDV20170923055059910', 'Green', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180106092224619192', 'DDV20170923055059910', '绿（无检）', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180106092235141661', 'DDV20170923055326755', '蓝（三抽一）', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180106092235554539', 'DDV20170923055326755', 'Blue', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180106092304389487', 'DDV20170923055848476', 'Orange', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180106092304811229', 'DDV20170923055848476', '橙（全检）', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180106092311922201', 'DDV20170923055928756', '红（售完停售）', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180106092311969711', 'DDV20170923055928756', 'Red', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180106092318168855', 'DDV20170923055945671', '黑（立即封存）', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180106092318221470', 'DDV20170923055945671', 'Black', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107005005598428', 'DDV201711180947521115007', '订单最新批次', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107005005665229', 'DDV201711180947521115007', '订单最新批次', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107021645510500', 'DDV201801070216455525847', '销售员', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107021645602139', 'DDV201801070216455525847', 'Sales', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107021714378220', 'DDV201801070217145333325', 'Sales Manager', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107021714615425', 'DDV201801070217145333325', '销售经理', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107021815668040', 'DDV201801070218152374885', 'General Manager', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107021815776683', 'DDV201801070218152374885', '总经理', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107021906381210', 'DDV201801070219068176750', 'General Manager', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107021906619795', 'DDV201801070219068176750', '总经理', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107133044466444', 'DDV201801071330442814864', '固定', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107133044729543', 'DDV201801071330442814864', 'fixed', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107133105269897', 'DDV201801061136402216963', 'per', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107133105564235', 'DDV201801061136402216963', '每', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107133331147739', 'DDV201801071333318457194', '提前', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107133331865816', 'DDV201801071333318457194', 'Advance', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107133352204502', 'DDV201801061136402385820', '滞后', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107133352985460', 'DDV201801061136402385820', 'Postpone', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107134116139376', 'DDV201800090842321259659', '產品名細和紙箱明細按付件的ORDER PLAN 為準\n1) Pay term:30% for deposit,balance at sight of copy of BL\n2) Shipping mark on outer carton as per buyer\'s requirement.\n3) Barcode sticker on both the inner and outer package as buyer\'s requirement.\n4) Telex release after received Balance.\n5) Both outter and inner carton need to be sealed well with sticky tapes - to prevent the chance of small parts drop out during transition.\n6) Product inspection photo must be ready before delivery, or 3%-5% of total value will be deducted if the product quality and package not meet buyer\'s requirement.\n7) SAA plugs for Australia and RCM label will put on the product.', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180107134116326256', 'DDV201800090842321259659', '產品名細和紙箱明細按付件的ORDER PLAN 為準\n1) Pay term:30% for deposit,balance at sight of copy of BL\n2) Shipping mark on outer carton as per buyer\'s requirement.\n3) Barcode sticker on both the inner and outer package as buyer\'s requirement.\n4) Telex release after received Balance.\n5) Both outter and inner carton need to be sealed well with sticky tapes - to prevent the chance of small parts drop out during transition.\n6) Product inspection photo must be ready before delivery, or 3%-5% of total value will be deducted if the product quality and package not meet buyer\'s requirement.\n7) SAA plugs for Australia and RCM label will put on the product.', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180113013431127446', 'DDV201801130134311780511', '西墨尔本运送费 - 板挂车', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180113013431974144', 'DDV201801130134311780511', 'Cartage to West Melbourne via Drop Trailer', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180113015020208859', 'DDV201710040749479379047', '西墨尔本运送费 - 侧装车', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180113015020807168', 'DDV201710040749479379047', 'Cartage to West Melbourne via side-loader', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180113020117561785', 'DDV201801130201172401778', 'Electronic Processing Fee', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180113020117675909', 'DDV201801130201172401778', '电放费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180128020235528169', 'DDV201709310445026239005', 'Storage department', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180128020235796981', 'DDV201709310445026239005', '仓储部门', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180128022359370056', 'DDV201801280223396734571', '会计部门', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180128022359909229', 'DDV201801280223396734571', null, 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180128022432581059', 'DDV201801280224323872043', null, 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180128022432917878', 'DDV201801280224323872043', '销售部门', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180128022608443476', 'DDV201709310443583940693', 'Purchasing department', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180128022608647909', 'DDV201709310443583940693', '采购部门', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180212142116590514', 'DDV201802121421162878626', '合同订金内', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180212142116938664', 'DDV201802121421162878626', 'Under Contract Deposit', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180212142138344999', 'DDV201802121421383699043', 'Under Contract Balance', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180212142138617990', 'DDV201802121421383699043', '合同尾款内', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180212142158439089', 'DDV201802121421582651455', '订金率比分摊', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180212142158983771', 'DDV201802121421582651455', 'Under Deposit Rate', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180216095018733261', 'DDV201802160950183711000', 'Nternational Purchase', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180216095018904061', 'DDV201802160950183711000', '国际采购', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180216095038109287', 'DDV201802160950386855025', '本地采购', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180216095038272050', 'DDV201802160950386855025', 'Local Shopping', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180219082856784215', 'DDV201802190828566812126', '运维型', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180219082856794752', 'DDV201802190828566812126', 'Maintained', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180219082917789211', 'DDV201802190829179618290', '开发型', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180219082917990457', 'DDV201802190829179618290', 'Develop', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180219083047119667', 'DDV201710230534171085280', '产品损坏', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180219083047543394', 'DDV201710230534171085280', 'Product damage', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180219083427131577', 'DDV201710210245451243461', '部分退款', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180219083427389406', 'DDV201710210245451243461', 'Partial refund', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180222133649634188', 'DDV201802221336491135146', '快递费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180222133649684002', 'DDV201802221336491135146', 'Express Fee', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180222133706599637', 'DDV201802221337068192062', 'Other Fee', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180222133706781166', 'DDV201802221337068192062', '其他费用', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180226024918133705', 'DDV201802260249189311414', 'Electronic Processing Fee', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180226024918571488', 'DDV201802260249189311414', '电放费', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180306014222371909', 'DDV201803060142224010221', '未同步', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180306014222720643', 'DDV201803060142224010221', 'null', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180306014252372881', 'DDV201803060142521970173', '未同步', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180306014252861654', 'DDV201803060142521970173', 'Yse', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180306014309213339', 'DDV201803060143097984348', '已同步', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180306014309628085', 'DDV201803060143097984348', 'No', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180306014350148203', 'DDV201803060143503965659', 'No', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180306014350630184', 'DDV201803060143503965659', '已同步', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180320071030336660', 'DDV201710040709168786265', '巴西古当港，马来西亚', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180320071030703883', 'DDV201710040709168786265', 'Port Pasir Gudang, Malaysia', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180320071120137337', 'DDV201803200711201156669', 'Vietnam - ho chi minh port', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DDVD20180320071120974581', 'DDV201803200711201156669', '越南国家-胡志明港口', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201807071507405603', 'DICTV2018070715074099600', '草稿', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201807071507406911', 'DICTV2018070715074099600', 'Draft', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201807071509294727', 'DICTV2018070715092992279', '删除', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201807071509297571', 'DICTV2018070715092992279', 'Deleted', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201807071520211671', 'DICTV2018070715202199471', 'Research', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201807071520213387', 'DICTV2018070715202199471', '调研', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201807071521415854', 'DICTV2018070715214136751', 'Pass', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201807071521419238', 'DICTV2018070715214136751', '立项', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201807071522143522', 'DICTV2018070715221477338', '激活', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201807071522145271', 'DICTV2018070715221477338', 'Open', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201807071523001746', 'DICTV2018070715230041867', 'Completed', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201807071523009277', 'DICTV2018070715230041867', '完成', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201807071523293321', 'DICTV2018070715232943564', 'Closed', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201807071523294128', 'DICTV2018070715232943564', '关闭', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201808071709451670', 'DICTV2018080717094113392', '运行', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201808071709485383', 'DICTV2018080717094113392', 'Run', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201808071710421565', 'DICTV2018080717104274907', '终止', 'zh_CN');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201808071710421780', 'DICTV2018080717104274907', 'Stop', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201808071710491851', 'DICTV2018080717104971582', 'Stop', 'en_AU');
INSERT INTO `na_dictionary_value_desc` VALUES ('DICTVD201808071710495446', 'DICTV2018080717104971582', '终止', 'zh_CN');

-- ----------------------------
-- Table structure for na_email
-- ----------------------------
DROP TABLE IF EXISTS `na_email`;
CREATE TABLE `na_email` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `send_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '发件人ID',
  `send_email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `recipient_name` longtext COLLATE utf8_unicode_ci COMMENT '接收人ID',
  `recipient_email` longtext COLLATE utf8_unicode_ci,
  `box_type` smallint(6) DEFAULT NULL COMMENT '邮箱类型：０草稿箱、１收件箱、２发件箱、３垃圾箱',
  `title` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `content` longtext COLLATE utf8_unicode_ci,
  `is_html` smallint(6) DEFAULT NULL,
  `is_read` smallint(6) DEFAULT NULL,
  `is_replied` smallint(6) DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL,
  `attachment_count` smallint(6) DEFAULT NULL,
  `send_time` datetime DEFAULT NULL COMMENT '邮件发出时间',
  `recipient_time` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `creator_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator_cn_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator_en_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_cn_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_en_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cc_name` longtext COLLATE utf8_unicode_ci COMMENT '抄送人ID',
  `cc_email` longtext COLLATE utf8_unicode_ci COMMENT '抄送人邮箱',
  `email_setting_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '关联的邮箱配置',
  `email_template_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '选用的编辑模板',
  `bcc_name` longtext COLLATE utf8_unicode_ci COMMENT '暗抄人ID',
  `bcc_email` longtext COLLATE utf8_unicode_ci COMMENT '暗抄人邮箱',
  `color` smallint(6) DEFAULT NULL COMMENT '色彩标记',
  `uid` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '全球唯一标记',
  `only_head` smallint(6) DEFAULT NULL COMMENT '仅下载了邮件头信息',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_email_box_type_idx` (`box_type`) USING BTREE,
  KEY `na_email_email_setting_id_idx` (`email_setting_id`) USING BTREE,
  KEY `na_email_id_idx` (`id`) USING BTREE,
  KEY `na_email_only_head_idx` (`only_head`) USING BTREE,
  KEY `na_email_status_idx` (`status`) USING BTREE,
  KEY `na_email_uid_idx` (`uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_email
-- ----------------------------

-- ----------------------------
-- Table structure for na_email_box
-- ----------------------------
DROP TABLE IF EXISTS `na_email_box`;
CREATE TABLE `na_email_box` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `cn_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '邮箱中文名',
  `en_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '英文名',
  `number` smallint(6) DEFAULT NULL COMMENT '邮件总数',
  `no_read` smallint(6) DEFAULT NULL COMMENT '新邮件数量',
  `box_type` smallint(6) DEFAULT NULL COMMENT '邮箱类型：０草稿箱、１收件箱、２发件箱、３垃圾箱',
  `email_setting_id` varchar(26) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '邮箱配置ID',
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `creator_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator_cn_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator_en_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_cn_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_en_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL,
  `last_uid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '最后下载的message id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_email_box_box_type_email_setting_id_idx` (`box_type`,`email_setting_id`) USING BTREE,
  KEY `na_email_box_box_type_idx` (`box_type`) USING BTREE,
  KEY `na_email_box_created_at_idx` (`created_at`) USING BTREE,
  KEY `na_email_box_email_setting_id_idx` (`email_setting_id`) USING BTREE,
  KEY `na_email_box_id_idx` (`id`) USING BTREE,
  KEY `na_email_box_no_read_idx` (`no_read`) USING BTREE,
  KEY `na_email_box_number_idx` (`number`) USING BTREE,
  KEY `na_email_box_status_idx` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_email_box
-- ----------------------------

-- ----------------------------
-- Table structure for na_email_related
-- ----------------------------
DROP TABLE IF EXISTS `na_email_related`;
CREATE TABLE `na_email_related` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `email_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `businessId` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` smallint(6) DEFAULT NULL COMMENT '关联类型：１产品、２供应商、３服务商、４订单、5费用支付、6费用登记、7差额退款',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_email_related
-- ----------------------------

-- ----------------------------
-- Table structure for na_email_setting
-- ----------------------------
DROP TABLE IF EXISTS `na_email_setting`;
CREATE TABLE `na_email_setting` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '配置名称',
  `email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `shared` smallint(6) DEFAULT NULL COMMENT '共享范围：1私有、２部门内、３所有人',
  `service_pop` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `service_pop_port` varchar(6) COLLATE utf8_unicode_ci DEFAULT NULL,
  `service_pop_ssl` smallint(6) DEFAULT NULL,
  `pop_account` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pop_password` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `service_smtp` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `service_smtp_port` varchar(6) COLLATE utf8_unicode_ci DEFAULT NULL,
  `service_smtp_ssl` smallint(6) DEFAULT NULL,
  `smtp_account` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `smtp_password` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `service_imap` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `service_imap_port` varchar(6) COLLATE utf8_unicode_ci DEFAULT NULL,
  `service_imap_ssl` smallint(6) DEFAULT NULL,
  `imap_account` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `imap_password` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` smallint(6) DEFAULT NULL COMMENT '类型:1: POP3、SMTP，2:IMAP',
  `status` smallint(6) DEFAULT NULL COMMENT '启用状态',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `creator_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator_cn_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator_en_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_cn_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_en_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `send_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '发送者名字',
  `signature` longtext COLLATE utf8_unicode_ci COMMENT '签名',
  `service_smtp_validate` smallint(6) DEFAULT NULL COMMENT '是否需要登录验证',
  `only_head` smallint(6) DEFAULT NULL COMMENT '仅下载头部',
  `is_default` smallint(6) DEFAULT NULL COMMENT '默认的发件邮箱',
  `sort` smallint(6) DEFAULT NULL COMMENT '显示排序',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_email_setting_created_at_idx` (`created_at`) USING BTREE,
  KEY `na_email_setting_creator_id_idx` (`creator_id`) USING BTREE,
  KEY `na_email_setting_email_idx` (`email`) USING BTREE,
  KEY `na_email_setting_id_idx` (`id`) USING BTREE,
  KEY `na_email_setting_is_default_idx` (`is_default`) USING BTREE,
  KEY `na_email_setting_shared_idx` (`shared`) USING BTREE,
  KEY `na_email_setting_status_idx` (`status`) USING BTREE,
  KEY `na_email_setting_title_idx` (`title`) USING BTREE,
  KEY `na_email_setting_type_idx` (`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_email_setting
-- ----------------------------

-- ----------------------------
-- Table structure for na_files
-- ----------------------------
DROP TABLE IF EXISTS `na_files`;
CREATE TABLE `na_files` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `path` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `extension` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `bytes` int(11) DEFAULT NULL,
  `note` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `category_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL,
  `shared` smallint(6) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `creator_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator_cn_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator_en_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_cn_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_en_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` smallint(6) DEFAULT NULL,
  `email_cid` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用于邮件内的图片引用ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_files_category_id_idx` (`category_id`) USING BTREE,
  KEY `na_files_created_at_idx` (`created_at`) USING BTREE,
  KEY `na_files_creator_id_idx` (`creator_id`) USING BTREE,
  KEY `na_files_department_id_idx` (`department_id`) USING BTREE,
  KEY `na_files_shared_idx` (`shared`) USING BTREE,
  KEY `na_files_status_idx` (`status`) USING BTREE,
  CONSTRAINT `fkiagbv3267i6cfhjyuafpxl0cd` FOREIGN KEY (`category_id`) REFERENCES `na_files_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_files
-- ----------------------------
INSERT INTO `na_files` VALUES ('MDOC20180716142344600844', 'test_login.py', 'var/upload/1/cases/201810/89ae9d1f9aa88ff64e8d06f7d16434c6.py', 'py', '3004', 'test', 'MDCC20180622080139787149', '1', '1', '2018-08-16 14:23:44', '2018-10-07 09:43:27', '1', 'bb', 'bb', 'DEP20180822000000110', 'aaa', 'nnnnn', '6', null);
INSERT INTO `na_files` VALUES ('MDOC20180719092924730393', 'test_vendorcategory.py', 'var/upload/1/email/201810/dca81064cbd29ef887b6062762017c3e.py', 'py', '4078', 'ddddddddddddd', 'MDCC20180622080041985120', '1', '1', '2018-08-19 09:29:24', '2018-10-07 09:44:32', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '6', null);
INSERT INTO `na_files` VALUES ('MDOC20180906134239511626', 'test_flow_purchase_plan.py', 'var/upload/1/cases/201810/001a53989aa312d0d2f03d2f0fd5c950.py', 'py', '7775', '采购计划申请', 'MDCC20180622080139787149', '1', '1', '2018-10-06 13:42:39', '2018-10-07 07:33:56', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '6', null);
INSERT INTO `na_files` VALUES ('MDOC20180906134313628337', 'test_flow_product_quotation.py', 'var/upload/1/cases/201810/57ab98292fa62ef1b9954cbdcc3e2c53.py', 'py', '6223', '产品询价', 'MDCC20180622080139787149', '1', '1', '2018-10-06 13:43:13', '2018-10-07 07:33:44', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '6', null);

-- ----------------------------
-- Table structure for na_files_category
-- ----------------------------
DROP TABLE IF EXISTS `na_files_category`;
CREATE TABLE `na_files_category` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `status` smallint(6) DEFAULT NULL,
  `sort` smallint(6) DEFAULT NULL,
  `path` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `bytes` int(11) DEFAULT NULL,
  `category_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `creator_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `extension` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `note` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `shared` int(11) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_files_category_sort_idx` (`sort`) USING BTREE,
  KEY `na_files_category_status_idx` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_files_category
-- ----------------------------
INSERT INTO `na_files_category` VALUES ('MDCC20180622080041985120', '1', '1', 'email', null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `na_files_category` VALUES ('MDCC20180622080139787149', '1', '2', 'cases', null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `na_files_category` VALUES ('MDCC20180622080232358034', '1', '3', 'project', null, null, null, null, null, null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for na_files_category_desc
-- ----------------------------
DROP TABLE IF EXISTS `na_files_category_desc`;
CREATE TABLE `na_files_category_desc` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `category_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `title` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `context` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lang` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_files_category_desc_category_id_idx` (`category_id`) USING BTREE,
  CONSTRAINT `fkkp3k00eeu3nss9htdoo3m8vv0` FOREIGN KEY (`category_id`) REFERENCES `na_files_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_files_category_desc
-- ----------------------------
INSERT INTO `na_files_category_desc` VALUES ('MDCC20180622080041318581', 'MDCC20180622080041985120', '邮件附件', '邮件附件', 'zh_CN');
INSERT INTO `na_files_category_desc` VALUES ('MDCC20180622080041901947', 'MDCC20180622080041985120', 'Email Attachment', 'Email Attachment', 'en_AU');
INSERT INTO `na_files_category_desc` VALUES ('MDCC20180622080139803711', 'MDCC20180622080139787149', '用例附件', '用例附件', 'zh_CN');
INSERT INTO `na_files_category_desc` VALUES ('MDCC20180622080139889374', 'MDCC20180622080139787149', 'Case Attachment', 'Case Attachment', 'en_AU');
INSERT INTO `na_files_category_desc` VALUES ('MDCC20180622080232215504', 'MDCC20180622080232358034', '项目附件', '项目附件', 'zh_CN');
INSERT INTO `na_files_category_desc` VALUES ('MDCC20180622080232233501', 'MDCC20180622080232358034', 'Project Attachment', 'Project Attachment', 'en_AU');

-- ----------------------------
-- Table structure for na_files_template
-- ----------------------------
DROP TABLE IF EXISTS `na_files_template`;
CREATE TABLE `na_files_template` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `context` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` smallint(6) DEFAULT NULL COMMENT '1纯附件、２邮件类',
  `template_name` longtext COLLATE utf8_unicode_ci,
  `template_content` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL,
  `shared` smallint(6) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `creator_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator_cn_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creator_en_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_cn_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_en_name` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_files_template
-- ----------------------------

-- ----------------------------
-- Table structure for na_helps
-- ----------------------------
DROP TABLE IF EXISTS `na_helps`;
CREATE TABLE `na_helps` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '标题',
  `content` longtext COLLATE utf8_unicode_ci COMMENT '正文',
  `sort` int(11) DEFAULT NULL COMMENT '显示排序',
  `status` smallint(6) DEFAULT NULL COMMENT '显示状态',
  `relations` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '关联帮助[{id:'''', title:'''', sort:''''},{...}]',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `na_help_relation_idx` (`relations`) USING BTREE,
  KEY `na_help_sort_idx` (`sort`) USING BTREE,
  KEY `na_help_title_idx` (`title`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_helps
-- ----------------------------

-- ----------------------------
-- Table structure for na_message
-- ----------------------------
DROP TABLE IF EXISTS `na_message`;
CREATE TABLE `na_message` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `to_user_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `to_user_cn_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `to_user_en_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `from_user_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `from_user_cn_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `from_user_en_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `title` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `content` longtext COLLATE utf8_unicode_ci,
  `status` tinyint(6) DEFAULT NULL,
  `is_read` tinyint(6) DEFAULT NULL COMMENT '状态：1未读, 2已读',
  `type` tinyint(6) DEFAULT NULL COMMENT '信息类型： 1系统信息；2用户信息；３产品特性通知',
  `target` tinyint(6) DEFAULT NULL COMMENT '路径类型：1POPWIN；2Tab；3URL',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL COMMENT '阅读时间',
  `module_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模块通知专用',
  `business_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '工作流通知专用（业务ID）',
  `to_department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `to_department_cn_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `to_department_en_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `from_department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `from_department_cn_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `from_department_en_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  KEY `na_message_from_user_id_idx` (`from_user_id`) USING BTREE,
  KEY `na_message_status_idx` (`status`) USING BTREE,
  KEY `na_message_to_user_id_idx` (`to_user_id`) USING BTREE,
  KEY `na_message_title` (`title`) USING BTREE,
  KEY `na_message_created_at` (`created_at`) USING BTREE,
  KEY `na_message_type` (`type`) USING BTREE,
  KEY `na_message_is_read` (`is_read`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_message
-- ----------------------------
INSERT INTO `na_message` VALUES ('MSG201809071351126073395', 'USER20170903010453523456', '测试账号25', 'test', '1', '系统管理员', 'admin', 'conversation', 'dddddddddd', '1', '2', '2', null, '2018-10-07 13:51:02', '2018-10-07 14:02:31', null, null, 'DEP201806231402567686999', '测试组', 'Test Team', 'DEP20180822000000110', 'Newaim', 'Newaim');
INSERT INTO `na_message` VALUES ('MSG201809071352082701318', '1', '系统管理员', 'admin', 'USER20170903010453523456', '测试账号25', 'test', 'conversation', 'ddddddddddddddd', '1', '2', '2', null, '2018-10-07 13:52:08', '2018-10-07 13:55:35', null, null, 'DEP20180822000000110', 'Newaim', 'Newaim', 'DEP201806231402567686999', '测试组', 'Test Team');
INSERT INTO `na_message` VALUES ('MSG201809071352171924826', '1', '系统管理员', 'admin', 'USER20170903010453523456', '测试账号25', 'test', 'conversation', 'aaaaa', '1', '2', '2', null, '2018-10-07 13:52:17', '2018-10-07 13:55:30', null, null, 'DEP20180822000000110', 'Newaim', 'Newaim', 'DEP201806231402567686999', '测试组', 'Test Team');
INSERT INTO `na_message` VALUES ('MSG201809071352326940229', 'USER20170903010453523456', '测试账号25', 'test', '1', '系统管理员', 'admin', 'conversation', 'aaaaaaaaaa', '1', '2', '2', null, '2018-10-07 13:52:32', '2018-10-07 14:02:23', null, null, 'DEP201806231402567686999', '测试组', 'Test Team', 'DEP20180822000000110', 'Newaim', 'Newaim');
INSERT INTO `na_message` VALUES ('MSG201809071352593806063', 'USER20170903010453523456', '测试账号25', 'test', '1', '系统管理员', 'admin', 'conversation', 'ccccccc', '1', '2', '2', null, '2018-10-07 13:52:59', '2018-10-07 13:55:24', null, null, 'DEP201806231402567686999', '测试组', 'Test Team', 'DEP20180822000000110', 'Newaim', 'Newaim');
INSERT INTO `na_message` VALUES ('MSG201809071353167628367', '1', '系统管理员', 'admin', 'USER20170903010453523456', '测试账号25', 'test', 'conversation', '有点问题，不能直接使用', '1', '2', '2', null, '2018-10-07 13:53:16', '2018-10-07 13:55:31', null, null, 'DEP20180822000000110', 'Newaim', 'Newaim', 'DEP201806231402567686999', '测试组', 'Test Team');
INSERT INTO `na_message` VALUES ('MSG201809071356123507518', '1', '系统管理员', 'admin', 'USER20170903010453523456', '测试账号25', 'test', 'conversation', '什么时候开始测试', '1', '2', '2', null, '2018-10-07 13:56:12', null, null, null, 'DEP20180822000000110', 'Newaim', 'Newaim', 'DEP201806231402567686999', '测试组', 'Test Team');
INSERT INTO `na_message` VALUES ('MSG201809071357168038249', 'USER20170903010453523456', '测试账号25', 'test', '1', '系统管理员', 'admin', 'conversation', '再等15分钟', '1', '2', '2', null, '2018-10-07 13:57:16', null, null, null, 'DEP201806231402567686999', '测试组', 'Test Team', 'DEP20180822000000110', 'Newaim', 'Newaim');
INSERT INTO `na_message` VALUES ('MSG201809071358168778866', '1', '系统管理员', 'admin', 'USER20170903010453523456', '测试账号25', 'test', 'conversation', 'Hello, 在不啊?', '1', '2', '2', null, '2018-10-07 13:58:16', '2018-10-07 14:02:18', null, null, 'DEP20180822000000110', 'Newaim', 'Newaim', 'DEP201806231402567686999', '测试组', 'Test Team');
INSERT INTO `na_message` VALUES ('MSG201809071359441254201', '1', '系统管理员', 'admin', 'USER20170903010453523456', '测试账号25', 'test', 'conversation', '================<div><br></div><div>asdf</div><div><br></div><div>====================</div>', '1', '2', '2', null, '2018-10-07 13:59:44', '2018-10-07 14:02:17', null, null, 'DEP20180822000000110', 'Newaim', 'Newaim', 'DEP201806231402567686999', '测试组', 'Test Team');
INSERT INTO `na_message` VALUES ('MSG201809071402449430513', '1', '系统管理员', 'admin', 'USER20170903010453523456', '测试账号25', 'test', 'conversation', 'aaaaaaaaaaaaaa', '1', '2', '2', null, '2018-10-07 14:02:44', '2018-10-07 14:03:10', null, null, 'DEP20180822000000110', 'Newaim', 'Newaim', 'DEP201806231402567686999', '测试组', 'Test Team');

-- ----------------------------
-- Table structure for na_portal
-- ----------------------------
DROP TABLE IF EXISTS `na_portal`;
CREATE TABLE `na_portal` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `module_key` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `col_num` smallint(6) DEFAULT NULL,
  `row_num` smallint(6) DEFAULT NULL,
  `module_type` smallint(6) DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL,
  `creator_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `height` smallint(6) DEFAULT NULL,
  KEY `na_portal_creator_id_idx` (`creator_id`) USING BTREE,
  KEY `na_portal_id_idx` (`id`) USING BTREE,
  KEY `na_portal_module_key_idx` (`module_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of na_portal
-- ----------------------------

-- ----------------------------
-- Table structure for na_projects
-- ----------------------------
DROP TABLE IF EXISTS `na_projects`;
CREATE TABLE `na_projects` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '项目名中文名',
  `en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '项目名英文名',
  `code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '项目代号',
  `context` text COLLATE utf8_unicode_ci COMMENT '项目简介',
  `sort` int(11) DEFAULT NULL COMMENT '显示排序',
  `status` int(2) DEFAULT NULL COMMENT '记录状态 : 0草稿；1正常；2禁用；3删除',
  `run_status` int(2) DEFAULT NULL COMMENT '项目状态：1调研；2立项；3激活；4完成；5关闭',
  `type` int(2) DEFAULT NULL COMMENT '项目类型：1开发型；2运维型;',
  `updated_index` int(11) DEFAULT NULL COMMENT '变更次数',
  `creator_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `creator_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人中文名',
  `creator_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人英文名',
  `department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门ID',
  `department_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门中文名',
  `department_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门英文名',
  `owner_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '负责人',
  `owner_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '负责人中文名',
  `owner_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '负责人英文名',
  `tester_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '测试负责人',
  `tester_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '测试负责人中文名',
  `tester_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '测试负责人英文名',
  `technology_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '技术负责人',
  `technology_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '技术负责人中文名',
  `technology_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '技术负责人英文名',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id` (`id`) USING BTREE,
  KEY `cn_name` (`cn_name`),
  KEY `en_name` (`en_name`),
  KEY `sort` (`sort`),
  KEY `status` (`status`),
  KEY `type` (`type`),
  KEY `run_status` (`run_status`),
  KEY `creator_id` (`creator_id`),
  KEY `department_id` (`department_id`),
  KEY `created_at` (`created_at`),
  KEY `updated_at` (`updated_at`),
  KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='项目表';

-- ----------------------------
-- Records of na_projects
-- ----------------------------
INSERT INTO `na_projects` VALUES ('PROJ20180708152734951932', '测试项目二', 'test project 02', null, 'bbbbbbbbbbbbbbbbbb', null, '1', '3', '1', '11', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', 'USER20171017065946104655', '公司总经理', null, '1', '系统管理员', null, 'USER20171017073317224321', '安检员1号', null, '2018-08-08 15:27:34', '2018-10-06 13:38:45');
INSERT INTO `na_projects` VALUES ('PROJ20180729162013978305', '测试项目一', 'test project 01', null, 'bbbbbbbbbbbbbbbbbb', null, '1', '3', '1', '2', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', 'USER20171017065946104655', '公司总经理', null, '1', '系统管理员', null, 'USER20171017073317224321', '安检员1号', null, '2018-08-29 16:20:13', '2018-10-06 04:10:08');

-- ----------------------------
-- Table structure for na_projects_instance
-- ----------------------------
DROP TABLE IF EXISTS `na_projects_instance`;
CREATE TABLE `na_projects_instance` (
  `id` varchar(24) COLLATE utf8_unicode_ci NOT NULL COMMENT '项目实例',
  `cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '实例中文名',
  `en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '实例英文名',
  `project_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '项目ID',
  `context` text COLLATE utf8_unicode_ci COMMENT '实例说明',
  `target_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '测试目标URL',
  `config` text COLLATE utf8_unicode_ci COMMENT '实例配置内容（ini格式）',
  `sort` int(11) DEFAULT NULL COMMENT '显示排序',
  `status` int(2) DEFAULT NULL COMMENT '记录状态 : 0草稿；1正常；2禁用；3删除',
  `creator_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `creator_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人中文名',
  `creator_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人英文名',
  `department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门ID',
  `department_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门中文名',
  `department_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门英文名',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `project_id` (`project_id`),
  KEY `status` (`status`),
  KEY `creator_id` (`creator_id`),
  KEY `sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of na_projects_instance
-- ----------------------------
INSERT INTO `na_projects_instance` VALUES ('PROI20180712102705200465', '开发测试站', 'dev test site', 'PROJ20180729162013978305', 'dddddddddd', 'http://localhost:8080/testing', '# -*- coding: utf-8 -*-\n[projects]\nbase_url=http://localhost:8080\nproject_path=/testing/', null, '1', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-08-12 10:27:05', '2018-10-06 04:11:08');
INSERT INTO `na_projects_instance` VALUES ('PROI20180906133942807355', '测试测试站', 'test test site', 'PROJ20180708152734951932', 'ccccccccccccc', 'http://localhost:8080/testing', '# -*- coding: utf-8 -*-\n[projects]\nbase_url=http://localhost:8080\nproject_path=/testing/', null, '1', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-10-06 13:39:42', '2018-10-06 13:49:25');

-- ----------------------------
-- Table structure for na_script_result
-- ----------------------------
DROP TABLE IF EXISTS `na_script_result`;
CREATE TABLE `na_script_result` (
  `id` bigint(24) NOT NULL AUTO_INCREMENT,
  `script_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '脚本ID',
  `script_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '脚本名',
  `target_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '脚本URL',
  `status` int(2) DEFAULT NULL COMMENT '记录状态: 1正常；２有争议；３作废；４删除',
  `result_pass` int(2) DEFAULT NULL COMMENT '通过',
  `result_wrong` int(2) DEFAULT NULL COMMENT '警告',
  `result_failed` int(2) DEFAULT NULL COMMENT '失败',
  `result_error` int(2) DEFAULT NULL COMMENT '错误',
  `result_file` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '独立结果文件路径',
  `result_context` text COLLATE utf8_unicode_ci COMMENT '结果备注',
  `log_path` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Log路径',
  `run_time` double(10,4) DEFAULT NULL COMMENT '运行时间',
  `run_hostname` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '运行主机',
  `run_ip` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '运行IP',
  `creator_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '执行人ID',
  `creator_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '执行人中文名',
  `creator_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '执行人英文名',
  `department_id` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门ID',
  `department_cn_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门中文名',
  `department_en_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '隶属部门英文名',
  `created_at` datetime(6) DEFAULT NULL COMMENT '执行时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id` (`id`) USING BTREE,
  KEY `script_id` (`script_id`) USING BTREE,
  KEY `status` (`status`) USING BTREE,
  KEY `creator_id` (`creator_id`) USING BTREE,
  KEY `department_id` (`department_id`) USING BTREE,
  KEY `created_at` (`created_at`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='脚本执行记录表';

-- ----------------------------
-- Records of na_script_result
-- ----------------------------
INSERT INTO `na_script_result` VALUES ('94', 'test_create_vendor_categ', '新建供应商分类', 'http://localhost:8080/testing/', '1', '0', null, '0', '1', 'G:\\web2\\jsp\\newaim-testing-client/var/result/201810/07/2018-10-07 15_50_19_result.html', '\"Traceback (most recent call last):\\n  File \\\"G:\\\\web2\\\\jsp\\\\newaim-testing-client\\\\var\\\\script\\\\PROI20180712102705200465\\\\case\\\\test_vendorcategory.py\\\", line 56, in test_vendorcategory\\n    self.driver.find_element_by_xpath(\\\"//*[@id=\'VendorCategoryViewTreePanelId-body\']//span[contains(@class,\'node-text\')]\\\").click()#\\u5b9a\\u4f4d\\u5230\\u4f9b\\u5e94\\u5546\\u7c7b\\u522b\\u7b2c\\u4e00\\u6761\\n  File \\\"C:\\\\Program Files\\\\Python\\\\Python35\\\\lib\\\\site-packages\\\\selenium\\\\webdriver\\\\remote\\\\webdriver.py\\\", line 258, in find_element_by_xpath\\n    return self.find_element(by=By.XPATH, value=xpath)\\n  File \\\"C:\\\\Program Files\\\\Python\\\\Python35\\\\lib\\\\site-packages\\\\selenium\\\\webdriver\\\\remote\\\\webdriver.py\\\", line 712, in find_element\\n    {\'using\': by, \'value\': value})[\'value\']\\n  File \\\"C:\\\\Program Files\\\\Python\\\\Python35\\\\lib\\\\site-packages\\\\selenium\\\\webdriver\\\\remote\\\\webdriver.py\\\", line 201, in execute\\n    self.error_handler.check_response(response)\\n  File \\\"C:\\\\Program Files\\\\Python\\\\Python35\\\\lib\\\\site-packages\\\\selenium\\\\webdriver\\\\remote\\\\errorhandler.py\\\", line 181, in check_response\\n    raise exception_class(message, screen, stacktrace)\\nselenium.common.exceptions.NoSuchElementException: Message: Unable to locate element: {\\\"method\\\":\\\"xpath\\\",\\\"selector\\\":\\\"//*[@id=\'VendorCategoryViewTreePanelId-body\']//span[contains(@class,\'node-text\')]\\\"}\\nStacktrace:\\n    at FirefoxDriver.prototype.findElementInternal_ (file:///C:/Users/ADMINI~1/AppData/Local/Temp/tmpm7pfwdue/extensions/fxdriver@googlecode.com/components/driver-component.js:10659)\\n    at FirefoxDriver.prototype.findElement (file:///C:/Users/ADMINI~1/AppData/Local/Temp/tmpm7pfwdue/extensions/fxdriver@googlecode.com/components/driver-component.js:10668)\\n    at DelayedCommand.prototype.executeInternal_/h (file:///C:/Users/ADMINI~1/AppData/Local/Temp/tmpm7pfwdue/extensions/fxdriver@googlecode.com/components/command-processor.js:12534)\\n    at DelayedCommand.prototype.executeInternal_ (file:///C:/Users/ADMINI~1/AppData/Local/Temp/tmpm7pfwdue/extensions/fxdriver@googlecode.com/components/command-processor.js:12539)\\n    at DelayedCommand.prototype.execute/< (file:///C:/Users/ADMINI~1/AppData/Local/Temp/tmpm7pfwdue/extensions/fxdriver@googlecode.com/components/command-processor.js:12481)\\n\"', 'G:\\web2\\jsp\\newaim-testing-client/var/driverlog/webdriver-2018-10-07 15_50_19.log', '25.3700', 'PC-haydn', '192.168.2.105', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-10-07 15:50:44.000000');
INSERT INTO `na_script_result` VALUES ('95', 'test_flow_product_quotat', null, null, '1', '0', null, '0', '1', null, '\'ProductQuotation\' object has no attribute \'script_id\'', null, '60.8780', 'PC-haydn', '192.168.2.105', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-10-07 15:51:45.000000');
INSERT INTO `na_script_result` VALUES ('96', 'test_flow_purchase_plan.', null, null, '1', '0', null, '0', '1', null, '\'purchasePlan\' object has no attribute \'script_id\'', null, '43.9360', 'PC-haydn', '192.168.2.105', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-10-07 15:52:30.000000');
INSERT INTO `na_script_result` VALUES ('97', 'test_login', '用户登录检查', 'http://localhost:8080/testing/', '1', '1', null, '0', '0', 'G:\\web2\\jsp\\newaim-testing-client/var/result/201810/07/2018-10-07 15_56_25_result.html', null, 'G:\\web2\\jsp\\newaim-testing-client/var/runlogs/system_2018-10-07 15_56_25.log', '17.7270', 'PC-haydn', '192.168.2.105', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-10-07 15:56:42.000000');
INSERT INTO `na_script_result` VALUES ('98', 'test_create_vendor_categ', '新建供应商分类', 'http://localhost:8080/testing/', '1', '0', null, '0', '1', 'G:\\web2\\jsp\\newaim-testing-client/var/result/201810/07/2018-10-07 15_56_43_result.html', '\"Traceback (most recent call last):\\n  File \\\"G:\\\\web2\\\\jsp\\\\newaim-testing-client\\\\var\\\\script\\\\PROI20180712102705200465\\\\case\\\\test_vendorcategory.py\\\", line 56, in test_vendorcategory\\n    self.driver.find_element_by_xpath(\\\"//*[@id=\'VendorCategoryViewTreePanelId-body\']//span[contains(@class,\'node-text\')]\\\").click()#\\u5b9a\\u4f4d\\u5230\\u4f9b\\u5e94\\u5546\\u7c7b\\u522b\\u7b2c\\u4e00\\u6761\\n  File \\\"C:\\\\Program Files\\\\Python\\\\Python35\\\\lib\\\\site-packages\\\\selenium\\\\webdriver\\\\remote\\\\webdriver.py\\\", line 258, in find_element_by_xpath\\n    return self.find_element(by=By.XPATH, value=xpath)\\n  File \\\"C:\\\\Program Files\\\\Python\\\\Python35\\\\lib\\\\site-packages\\\\selenium\\\\webdriver\\\\remote\\\\webdriver.py\\\", line 712, in find_element\\n    {\'using\': by, \'value\': value})[\'value\']\\n  File \\\"C:\\\\Program Files\\\\Python\\\\Python35\\\\lib\\\\site-packages\\\\selenium\\\\webdriver\\\\remote\\\\webdriver.py\\\", line 201, in execute\\n    self.error_handler.check_response(response)\\n  File \\\"C:\\\\Program Files\\\\Python\\\\Python35\\\\lib\\\\site-packages\\\\selenium\\\\webdriver\\\\remote\\\\errorhandler.py\\\", line 181, in check_response\\n    raise exception_class(message, screen, stacktrace)\\nselenium.common.exceptions.NoSuchElementException: Message: Unable to locate element: {\\\"method\\\":\\\"xpath\\\",\\\"selector\\\":\\\"//*[@id=\'VendorCategoryViewTreePanelId-body\']//span[contains(@class,\'node-text\')]\\\"}\\nStacktrace:\\n    at FirefoxDriver.prototype.findElementInternal_ (file:///C:/Users/ADMINI~1/AppData/Local/Temp/tmp4vvcs6l9/extensions/fxdriver@googlecode.com/components/driver-component.js:10659)\\n    at FirefoxDriver.prototype.findElement (file:///C:/Users/ADMINI~1/AppData/Local/Temp/tmp4vvcs6l9/extensions/fxdriver@googlecode.com/components/driver-component.js:10668)\\n    at DelayedCommand.prototype.executeInternal_/h (file:///C:/Users/ADMINI~1/AppData/Local/Temp/tmp4vvcs6l9/extensions/fxdriver@googlecode.com/components/command-processor.js:12534)\\n    at DelayedCommand.prototype.executeInternal_ (file:///C:/Users/ADMINI~1/AppData/Local/Temp/tmp4vvcs6l9/extensions/fxdriver@googlecode.com/components/command-processor.js:12539)\\n    at DelayedCommand.prototype.execute/< (file:///C:/Users/ADMINI~1/AppData/Local/Temp/tmp4vvcs6l9/extensions/fxdriver@googlecode.com/components/command-processor.js:12481)\\n\"', 'G:\\web2\\jsp\\newaim-testing-client/var/driverlog/webdriver-2018-10-07 15_56_43.log', '19.6640', 'PC-haydn', '192.168.2.105', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-10-07 15:57:03.000000');
INSERT INTO `na_script_result` VALUES ('99', 'test_flow_purchase_plan.', null, null, '1', '0', null, '0', '1', null, '\'purchasePlan\' object has no attribute \'script_id\'', null, '42.4860', 'PC-haydn', '192.168.2.105', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-10-07 16:00:04.000000');
INSERT INTO `na_script_result` VALUES ('100', 'test_login', '用户登录检查', 'http://localhost:8080/testing/', '1', '1', null, '0', '0', 'G:\\web2\\jsp\\newaim-testing-client/var/result/201810/07/2018-10-07 17_44_57_result.html', null, 'G:\\web2\\jsp\\newaim-testing-client/var/runlogs/system_2018-10-07 17_44_57.log', '18.5980', 'PC-haydn', '192.168.2.105', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-10-07 17:45:16.000000');
INSERT INTO `na_script_result` VALUES ('101', 'test_create_vendor_categ', '新建供应商分类', 'http://localhost:8080/testing/', '1', '0', null, '0', '1', 'G:\\web2\\jsp\\newaim-testing-client/var/result/201810/07/2018-10-07 19_18_29_result.html', '\"Traceback (most recent call last):\\n  File \\\"G:\\\\web2\\\\jsp\\\\newaim-testing-client\\\\var\\\\script\\\\PROI20180712102705200465\\\\case\\\\test_vendorcategory.py\\\", line 56, in test_vendorcategory\\n    self.driver.find_element_by_xpath(\\\"//*[@id=\'VendorCategoryViewTreePanelId-body\']//span[contains(@class,\'node-text\')]\\\").click()#\\u5b9a\\u4f4d\\u5230\\u4f9b\\u5e94\\u5546\\u7c7b\\u522b\\u7b2c\\u4e00\\u6761\\n  File \\\"C:\\\\Program Files\\\\Python\\\\Python35\\\\lib\\\\site-packages\\\\selenium\\\\webdriver\\\\remote\\\\webdriver.py\\\", line 258, in find_element_by_xpath\\n    return self.find_element(by=By.XPATH, value=xpath)\\n  File \\\"C:\\\\Program Files\\\\Python\\\\Python35\\\\lib\\\\site-packages\\\\selenium\\\\webdriver\\\\remote\\\\webdriver.py\\\", line 712, in find_element\\n    {\'using\': by, \'value\': value})[\'value\']\\n  File \\\"C:\\\\Program Files\\\\Python\\\\Python35\\\\lib\\\\site-packages\\\\selenium\\\\webdriver\\\\remote\\\\webdriver.py\\\", line 201, in execute\\n    self.error_handler.check_response(response)\\n  File \\\"C:\\\\Program Files\\\\Python\\\\Python35\\\\lib\\\\site-packages\\\\selenium\\\\webdriver\\\\remote\\\\errorhandler.py\\\", line 181, in check_response\\n    raise exception_class(message, screen, stacktrace)\\nselenium.common.exceptions.NoSuchElementException: Message: Unable to locate element: {\\\"method\\\":\\\"xpath\\\",\\\"selector\\\":\\\"//*[@id=\'VendorCategoryViewTreePanelId-body\']//span[contains(@class,\'node-text\')]\\\"}\\nStacktrace:\\n    at FirefoxDriver.prototype.findElementInternal_ (file:///C:/Users/ADMINI~1/AppData/Local/Temp/tmpop32adnz/extensions/fxdriver@googlecode.com/components/driver-component.js:10659)\\n    at FirefoxDriver.prototype.findElement (file:///C:/Users/ADMINI~1/AppData/Local/Temp/tmpop32adnz/extensions/fxdriver@googlecode.com/components/driver-component.js:10668)\\n    at DelayedCommand.prototype.executeInternal_/h (file:///C:/Users/ADMINI~1/AppData/Local/Temp/tmpop32adnz/extensions/fxdriver@googlecode.com/components/command-processor.js:12534)\\n    at DelayedCommand.prototype.executeInternal_ (file:///C:/Users/ADMINI~1/AppData/Local/Temp/tmpop32adnz/extensions/fxdriver@googlecode.com/components/command-processor.js:12539)\\n    at DelayedCommand.prototype.execute/< (file:///C:/Users/ADMINI~1/AppData/Local/Temp/tmpop32adnz/extensions/fxdriver@googlecode.com/components/command-processor.js:12481)\\n\"', 'G:\\web2\\jsp\\newaim-testing-client/var/driverlog/webdriver-2018-10-07 19_18_29.log', '20.2000', 'PC-haydn', '192.168.2.105', '1', '系统管理员', 'admin', 'DEP20180822000000110', 'Newaim', 'Newaim', '2018-10-07 19:18:49.000000');
