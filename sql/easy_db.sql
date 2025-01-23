/*
 Navicat Premium Data Transfer

 Source Server         : 201
 Source Server Type    : MySQL
 Source Server Version : 80403
 Source Host           : 192.168.2.201:3307
 Source Schema         : easy_db

 Target Server Type    : MySQL
 Target Server Version : 80403
 File Encoding         : 65001

 Date: 21/01/2025 23:06:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_code
-- ----------------------------
DROP TABLE IF EXISTS `t_code`;
CREATE TABLE `t_code`  (
  `c_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '激活码唯一标识',
  `c_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '激活码',
  `c_type` tinyint(0) NOT NULL COMMENT '激活码类型：1-注册码 2-激活码',
  `c_timetype` tinyint(0) NOT NULL COMMENT '激活码时长类型：0-小时卡 1-日卡 2-周卡 3-月卡 4-季卡 5-年卡',
  `c_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '注册码绑定用户，如果是激活码则是机器码',
  `c_createtime` datetime(0) NOT NULL COMMENT '激活码创建时间',
  `c_usetime` datetime(0) NULL DEFAULT NULL COMMENT '激活码使用时间',
  `c_expired` datetime(0) NULL DEFAULT NULL COMMENT '激活码到期时间',
  `c_score` int(0) NOT NULL COMMENT '激活码积分 -1则无限制 0为停用',
  `s_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属软件ID',
  PRIMARY KEY (`c_id`) USING BTREE,
  UNIQUE INDEX `c_code`(`c_code`, `s_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_developer
-- ----------------------------
DROP TABLE IF EXISTS `t_developer`;
CREATE TABLE `t_developer`  (
  `d_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '开发者唯一标识',
  `d_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '开发者名称',
  `d_mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开发者绑定邮箱',
  `d_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '开发者密码（SHA256）',
  PRIMARY KEY (`d_id`) USING BTREE,
  UNIQUE INDEX `d_name`(`d_name`) USING BTREE,
  UNIQUE INDEX `d_mail`(`d_mail`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_encrypt
-- ----------------------------
DROP TABLE IF EXISTS `t_encrypt`;
CREATE TABLE `t_encrypt`  (
  `e_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '加密算法唯一标识',
  `e_publickey` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '加密算法公钥',
  `e_privatekey` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '加密算法私钥',
  `e_updatetime` datetime(0) NULL DEFAULT NULL COMMENT '加密算法修改时间',
  `s_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '加密算法所属软件',
  `encryption` tinyint(0) NOT NULL DEFAULT 0 COMMENT '加密算法类型 0-无加密 1-RSA加密',
  PRIMARY KEY (`e_id`) USING BTREE,
  UNIQUE INDEX `s_id`(`s_id`, `encryption`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_interface
-- ----------------------------
DROP TABLE IF EXISTS `t_interface`;
CREATE TABLE `t_interface`  (
  `i_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口唯一标识',
  `i_function` tinyint(0) NOT NULL COMMENT '接口功能 0-注册 1-账号登录 2-激活码登录 3-获取版本 4-获取公告 5-获取指定用户到期时间 6-是否为当前最新版本 7-修改用户密码',
  `i_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口链接',
  `i_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口参数',
  `i_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口描述',
  `s_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '软件唯一标识',
  `encryption` tinyint(0) NULL DEFAULT NULL COMMENT '接口加密方式 0-无加密 1-RSA2048',
  PRIMARY KEY (`i_id`) USING BTREE,
  UNIQUE INDEX `i_url`(`i_url`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_request
-- ----------------------------
DROP TABLE IF EXISTS `t_request`;
CREATE TABLE `t_request`  (
  `r_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求的唯一主键',
  `r_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方法',
  `r_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求链接',
  `r_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数，通常get携带',
  `r_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求体，通常post携带',
  `r_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求用户',
  `r_role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求身份',
  `r_date` datetime(0) NOT NULL COMMENT '请求日期',
  `r_time` bigint(0) NOT NULL COMMENT '请求响应时间',
  `r_ipaddress` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求IP地址',
  `r_response` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求响应内容',
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_software
-- ----------------------------
DROP TABLE IF EXISTS `t_software`;
CREATE TABLE `t_software`  (
  `s_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '软件唯一标识',
  `s_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '软件名称',
  `s_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '软件密钥',
  `s_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '软件描述',
  `s_notice` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '软件公告',
  `s_version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '软件当前版本',
  `s_codetype` tinyint(0) NULL DEFAULT NULL COMMENT '软件登录类型：0-账户密码 1-账户密码+注册码 2-激活码',
  `s_verifymac` tinyint(0) NOT NULL DEFAULT 1 COMMENT '软件是否验证机器码：0-不验证 1-验证',
  `d_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开发者唯一标识',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '软件创建事件',
  `create_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '软件创建IP',
  PRIMARY KEY (`s_id`) USING BTREE,
  UNIQUE INDEX `s_key`(`s_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `u_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户唯一标识',
  `u_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `u_mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `u_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户密码（SHA256）',
  `s_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`u_id`) USING BTREE,
  UNIQUE INDEX `u_name`(`u_name`, `s_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- View structure for v_codeinfo
-- ----------------------------
DROP VIEW IF EXISTS `v_codeinfo`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_codeinfo` AS select `t_code`.`c_id` AS `c_id`,`t_code`.`c_code` AS `c_code`,`t_code`.`c_type` AS `c_type`,`t_code`.`c_timetype` AS `c_timetype`,`t_code`.`c_name` AS `c_name`,`t_code`.`c_usetime` AS `c_usetime`,`t_code`.`c_expired` AS `c_expired`,`t_code`.`c_score` AS `c_score`,`t_code`.`s_id` AS `s_id`,`t_software`.`s_name` AS `s_name`,`t_software`.`d_id` AS `d_id` from (`t_code` join `t_software` on((`t_code`.`s_id` = `t_software`.`s_id`)));

-- ----------------------------
-- View structure for v_encryptinfo
-- ----------------------------
DROP VIEW IF EXISTS `v_encryptinfo`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_encryptinfo` AS select `t_encrypt`.`e_id` AS `e_id`,`t_encrypt`.`e_publickey` AS `e_publickey`,`t_encrypt`.`e_privatekey` AS `e_privatekey`,`t_encrypt`.`e_updatetime` AS `e_updatetime`,`t_software`.`s_id` AS `s_id`,`t_software`.`s_name` AS `s_name`,`t_encrypt`.`encryption` AS `encryption`,`t_software`.`d_id` AS `d_id` from (`t_software` left join `t_encrypt` on((`t_encrypt`.`s_id` = `t_software`.`s_id`)));

-- ----------------------------
-- View structure for v_interfaceinfo
-- ----------------------------
DROP VIEW IF EXISTS `v_interfaceinfo`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_interfaceinfo` AS select `t_interface`.`i_id` AS `i_id`,`t_interface`.`i_function` AS `i_function`,`t_interface`.`i_url` AS `i_url`,`t_interface`.`i_desc` AS `i_desc`,`t_interface`.`encryption` AS `encryption`,`t_software`.`s_name` AS `s_name`,`t_software`.`s_verifymac` AS `s_verifymac`,`t_software`.`s_codetype` AS `s_codetype`,`t_software`.`d_id` AS `d_id`,`t_software`.`s_id` AS `s_id` from (`t_software` join `t_interface` on((`t_software`.`s_id` = `t_interface`.`s_id`)));

-- ----------------------------
-- View structure for v_requestlog
-- ----------------------------
DROP VIEW IF EXISTS `v_requestlog`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_requestlog` AS select `t_request`.`r_id` AS `r_id`,`t_request`.`r_method` AS `r_method`,`t_request`.`r_uri` AS `r_uri`,`t_request`.`r_params` AS `r_params`,`t_request`.`r_body` AS `r_body`,`t_request`.`r_username` AS `r_username`,`t_request`.`r_role` AS `r_role`,`t_request`.`r_date` AS `r_date`,`t_request`.`r_time` AS `r_time`,`t_request`.`r_ipaddress` AS `r_ipaddress`,`t_request`.`r_response` AS `r_response`,`t_interface`.`s_id` AS `s_id`,`t_software`.`s_name` AS `s_name`,`t_interface`.`i_function` AS `i_function`,`t_interface`.`encryption` AS `encryption`,`t_software`.`d_id` AS `d_id` from ((`t_request` join `t_interface`) join `t_software` on(((substr(`t_request`.`r_uri`,9) = `t_interface`.`i_url`) and (`t_interface`.`s_id` = `t_software`.`s_id`))));

-- ----------------------------
-- View structure for v_userinfo
-- ----------------------------
DROP VIEW IF EXISTS `v_userinfo`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_userinfo` AS select `t_user`.`u_id` AS `u_id`,`t_user`.`u_name` AS `u_name`,`t_user`.`u_mail` AS `u_mail`,`t_user`.`u_password` AS `u_password`,`t_software`.`s_id` AS `s_id`,`t_software`.`s_name` AS `s_name`,`t_software`.`s_verifymac` AS `s_verifymac`,`t_software`.`d_id` AS `d_id`,`t_code`.`c_code` AS `c_code`,`t_code`.`c_name` AS `c_name`,`t_code`.`c_expired` AS `c_expired`,`t_code`.`c_score` AS `c_score`,`t_code`.`c_type` AS `c_type` from ((`t_user` left join `t_software` on((`t_software`.`s_id` = `t_user`.`s_id`))) left join `t_code` on(((`t_user`.`u_name` = `t_code`.`c_name`) and (`t_user`.`s_id` = `t_code`.`s_id`))));

SET FOREIGN_KEY_CHECKS = 1;
