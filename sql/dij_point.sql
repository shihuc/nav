/*
Navicat MySQL Data Transfer

Source Server         : IOT-DEV-DB21
Source Server Version : 50724
Source Host           : 10.95.200.21:3306
Source Database       : iotres

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-12-31 10:01:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `dij_point`
-- ----------------------------
DROP TABLE IF EXISTS `dij_point`;
CREATE TABLE `dij_point` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pointx` int(11) NOT NULL COMMENT '点的X坐标',
  `pointy` int(11) NOT NULL COMMENT '点的Y坐标',
  PRIMARY KEY (`id`),
  UNIQUE KEY `POINT_XY_IDX` (`pointx`,`pointy`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of dij_point
-- ----------------------------
INSERT INTO `dij_point` VALUES ('2', '70', '307');
INSERT INTO `dij_point` VALUES ('1', '154', '141');
INSERT INTO `dij_point` VALUES ('3', '158', '469');
INSERT INTO `dij_point` VALUES ('4', '394', '271');
INSERT INTO `dij_point` VALUES ('5', '544', '132');
INSERT INTO `dij_point` VALUES ('7', '572', '564');
INSERT INTO `dij_point` VALUES ('6', '782', '393');
