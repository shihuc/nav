/*
Navicat MySQL Data Transfer

Source Server         : IOT-DEV-DB21
Source Server Version : 50724
Source Host           : 10.95.200.21:3306
Source Database       : iotres

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-12-31 10:01:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `dij_edge`
-- ----------------------------
DROP TABLE IF EXISTS `dij_edge`;
CREATE TABLE `dij_edge` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `point` int(11) NOT NULL COMMENT 'point表的主键ID',
  `neighbor` int(11) NOT NULL COMMENT '指定点的邻居节点在point表的主键ID',
  `weight` int(11) NOT NULL COMMENT '边的权重，这里主要是像素距离',
  PRIMARY KEY (`id`),
  UNIQUE KEY `POINT_NEIG_IDX` (`point`,`neighbor`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of dij_edge
-- ----------------------------
INSERT INTO `dij_edge` VALUES ('1', '1', '2', '186');
INSERT INTO `dij_edge` VALUES ('2', '1', '4', '273');
INSERT INTO `dij_edge` VALUES ('3', '1', '5', '390');
INSERT INTO `dij_edge` VALUES ('4', '2', '3', '184');
INSERT INTO `dij_edge` VALUES ('5', '3', '4', '308');
INSERT INTO `dij_edge` VALUES ('6', '3', '7', '425');
INSERT INTO `dij_edge` VALUES ('7', '5', '6', '353');
INSERT INTO `dij_edge` VALUES ('8', '5', '4', '205');
INSERT INTO `dij_edge` VALUES ('9', '4', '7', '343');
INSERT INTO `dij_edge` VALUES ('10', '4', '6', '407');
INSERT INTO `dij_edge` VALUES ('11', '6', '7', '271');
