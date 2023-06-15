/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : pinche_db

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2018-02-04 22:58:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL default '',
  `password` varchar(32) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for `t_carinfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_carinfo`;
CREATE TABLE `t_carinfo` (
  `carNo` varchar(30) NOT NULL COMMENT 'carNo',
  `chexing` varchar(20) NOT NULL COMMENT '车型',
  `carPhoto` varchar(60) NOT NULL COMMENT '车辆照片',
  `buyYear` int(11) NOT NULL COMMENT '购买年份',
  `jiaShiZheng` varchar(60) NOT NULL COMMENT '驾驶证',
  `xueShenZheng` varchar(60) NOT NULL COMMENT '学生证',
  `carDesc` varchar(2000) default NULL COMMENT '车辆描述',
  `userObj` varchar(20) NOT NULL COMMENT '所属学生',
  `shenHeState` varchar(20) NOT NULL COMMENT '审核状态',
  `addTime` varchar(20) default NULL COMMENT '提交时间',
  PRIMARY KEY  (`carNo`),
  KEY `userObj` (`userObj`),
  CONSTRAINT `t_carinfo_ibfk_1` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_carinfo
-- ----------------------------
INSERT INTO `t_carinfo` VALUES ('川A-2888', '宝马5系 2017款手动型', 'upload/22790a53-7259-440f-bff7-cefc5aad48fa.jpg', '2017', 'upload/c0eb7a6f-961c-4a63-8794-cc719461aadc.jpg', 'upload/2d6543c0-9313-498d-a147-4c643a7ff9b3.jpg', '双鱼林大神开宝马带你飞,检测车辆左前部轻微碰撞，左前大灯更换，骨架没有受损，无水淹火烧等痕迹，车辆发动机舱干净整洁，变速箱工作正常，车辆外观有局部钣金做漆，车身外观有伤痕，内饰正常使用，车内设备工作正常。', '20171108', '已审核', '2017-11-08 03:39:16');
INSERT INTO `t_carinfo` VALUES ('川A-3666', '奥迪自动挡', 'upload/551a0707-5e8d-4c43-b23f-3e91fa21fac5.jpg', '2016', 'upload/c0eb7a6f-961c-4a63-8794-cc719461aadc.jpg', 'upload/2d6543c0-9313-498d-a147-4c643a7ff9b3.jpg', '很好的车子哦', '20171108', '已审核', '2017-11-08 19:40:24');

-- ----------------------------
-- Table structure for `t_pinche`
-- ----------------------------
DROP TABLE IF EXISTS `t_pinche`;
CREATE TABLE `t_pinche` (
  `pincheId` int(11) NOT NULL auto_increment COMMENT '记录id',
  `carObj` varchar(30) NOT NULL COMMENT '车辆信息',
  `pinChePhoto` varchar(60) NOT NULL COMMENT '拼车图片',
  `startTime` varchar(20) default NULL COMMENT '出发时间',
  `startPlace` varchar(80) NOT NULL COMMENT '车出发位置',
  `endPlace` varchar(80) NOT NULL COMMENT '目的地',
  `price` float NOT NULL COMMENT '价格',
  `totalNum` int(11) NOT NULL COMMENT '可拼人数',
  `haveNum` int(11) NOT NULL COMMENT '已拼人数',
  PRIMARY KEY  (`pincheId`),
  KEY `carObj` (`carObj`),
  CONSTRAINT `t_pinche_ibfk_1` FOREIGN KEY (`carObj`) REFERENCES `t_carinfo` (`carNo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_pinche
-- ----------------------------
INSERT INTO `t_pinche` VALUES ('1', '川A-2888', 'upload/23f683e3-19e4-43cd-a3a3-29176b063c64.jpg', '2017-11-29 09:00', '成都万年场', '成都洛带古镇', '20', '5', '3');
INSERT INTO `t_pinche` VALUES ('2', '川A-3666', 'upload/a3d2ddac-ecec-48a0-b520-c58ecf8bd756.jpg', '2017-11-17 09:10:00', '成都红星路', '渠县望江广场', '68', '4', '0');
INSERT INTO `t_pinche` VALUES ('3', '川A-3666', 'upload/a3d2ddac-ecec-48a0-b520-c58ecf8bd756.jpg', '2017-11-29 13:13:00', '电子科技大学', '四川大学', '10', '5', '2');

-- ----------------------------
-- Table structure for `t_pincheorder`
-- ----------------------------
DROP TABLE IF EXISTS `t_pincheorder`;
CREATE TABLE `t_pincheorder` (
  `orderId` int(11) NOT NULL auto_increment COMMENT '订单id',
  `pinCheObj` int(11) NOT NULL COMMENT '拼车id',
  `userObj` varchar(20) NOT NULL COMMENT '用户',
  `personNum` int(11) NOT NULL COMMENT '拼车人数',
  `orderTime` varchar(20) default NULL COMMENT '下单时间',
  PRIMARY KEY  (`orderId`),
  KEY `pinCheObj` (`pinCheObj`),
  KEY `userObj` (`userObj`),
  CONSTRAINT `t_pincheorder_ibfk_1` FOREIGN KEY (`pinCheObj`) REFERENCES `t_pinche` (`pincheId`),
  CONSTRAINT `t_pincheorder_ibfk_2` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_pincheorder
-- ----------------------------
INSERT INTO `t_pincheorder` VALUES ('5', '1', '20171108', '2', '2017-11-08 23:40:03');
INSERT INTO `t_pincheorder` VALUES ('6', '1', '20171109', '1', '2017-11-08 23:42:12');
INSERT INTO `t_pincheorder` VALUES ('7', '3', '20171109', '2', '2017-11-26 00:42:49');

-- ----------------------------
-- Table structure for `t_userinfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_userinfo`;
CREATE TABLE `t_userinfo` (
  `user_name` varchar(20) NOT NULL COMMENT 'user_name',
  `password` varchar(20) NOT NULL COMMENT '登录密码',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `sex` varchar(4) NOT NULL COMMENT '性别',
  `birthday` varchar(20) default NULL COMMENT '出生日期',
  `userPhoto` varchar(60) NOT NULL COMMENT '用户照片',
  PRIMARY KEY  (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_userinfo
-- ----------------------------
INSERT INTO `t_userinfo` VALUES ('20171108', '123', '双鱼林', '男', '2017-11-06', 'upload/a74f6daf-f096-469f-8e30-104717098076.jpg');
INSERT INTO `t_userinfo` VALUES ('20171109', '123', '张小倩', '女', '2017-11-02', 'upload/4ff0966d-465c-408f-9b1f-f3189c1e4b35.jpg');
