/*
 Navicat Premium Dump SQL

 Source Server         : ZUQIU
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44)
 Source Host           : localhost:3306
 Source Schema         : zuqiujulebguanli

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44)
 File Encoding         : 65001

 Date: 07/05/2026 16:55:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置参数名称',
  `value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置参数值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, '首页轮播图1', 'upload/config1.jpg');
INSERT INTO `config` VALUES (2, '首页轮播图2', 'upload/config2.jpg');
INSERT INTO `config` VALUES (3, '首页轮播图3', 'upload/config3.jpg');

-- ----------------------------
-- Table structure for dictionary
-- ----------------------------
DROP TABLE IF EXISTS `dictionary`;
CREATE TABLE `dictionary`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dic_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典编码',
  `dic_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典名称',
  `code_index` int(11) NULL DEFAULT NULL COMMENT '编码',
  `index_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编码名称',
  `super_id` int(11) NULL DEFAULT NULL COMMENT '父级字段ID',
  `beizhu` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dictionary
-- ----------------------------
INSERT INTO `dictionary` VALUES (1, 'sex_types', '性别类型', 1, '男', NULL, NULL, '2026-03-21 09:00:00');
INSERT INTO `dictionary` VALUES (2, 'sex_types', '性别类型', 2, '女', NULL, NULL, '2026-03-21 09:00:00');
INSERT INTO `dictionary` VALUES (3, 'gonggao_types', '公告类型', 1, '俱乐部公告', NULL, NULL, '2026-03-21 09:00:00');
INSERT INTO `dictionary` VALUES (4, 'gonggao_types', '公告类型', 2, '赛事通知', NULL, NULL, '2026-03-21 09:00:00');
INSERT INTO `dictionary` VALUES (5, 'gonggao_types', '公告类型', 3, '转会动态', NULL, NULL, '2026-03-21 09:00:00');
INSERT INTO `dictionary` VALUES (6, 'gonggao_types', '公告类型', 4, '活动公告', NULL, NULL, '2026-03-21 09:00:00');
INSERT INTO `dictionary` VALUES (7, 'saishi_types', '赛事类型', 1, '中超联赛', NULL, NULL, '2026-03-21 09:00:00');
INSERT INTO `dictionary` VALUES (8, 'saishi_types', '赛事类型', 2, '足协杯', NULL, NULL, '2026-03-21 09:00:00');
INSERT INTO `dictionary` VALUES (9, 'saishi_types', '赛事类型', 3, '亚冠联赛', NULL, NULL, '2026-03-21 09:00:00');
INSERT INTO `dictionary` VALUES (10, 'saishi_types', '赛事类型', 4, '热身赛', NULL, NULL, '2026-03-21 09:00:00');
INSERT INTO `dictionary` VALUES (11, 'xunlian_types', '训练计划类型', 1, '体能训练', NULL, NULL, '2026-03-21 09:00:00');
INSERT INTO `dictionary` VALUES (12, 'xunlian_types', '训练计划类型', 2, '战术训练', NULL, NULL, '2026-03-21 09:00:00');
INSERT INTO `dictionary` VALUES (13, 'xunlian_types', '训练计划类型', 3, '技术训练', NULL, NULL, '2026-03-21 09:00:00');
INSERT INTO `dictionary` VALUES (14, 'xunlian_types', '训练计划类型', 4, '恢复训练', NULL, NULL, '2026-03-21 09:00:00');
INSERT INTO `dictionary` VALUES (15, 'shuju_types', '球员数据类型', 1, '前锋数据', NULL, NULL, '2026-03-21 09:00:00');
INSERT INTO `dictionary` VALUES (16, 'shuju_types', '球员数据类型', 2, '中场数据', NULL, NULL, '2026-03-21 09:00:00');

-- ----------------------------
-- Table structure for gonggao
-- ----------------------------
DROP TABLE IF EXISTS `gonggao`;
CREATE TABLE `gonggao`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gonggao_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告名称',
  `gonggao_photo` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告图片',
  `gonggao_types` int(11) NOT NULL COMMENT '公告类型',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '发布时间',
  `gonggao_content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '公告详情',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公告信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gonggao
-- ----------------------------
INSERT INTO `gonggao` VALUES (1, '2026赛季备战计划正式启动', 'upload/gonggao1.jpg', 1, '2026-03-10 09:00:00', '<p>为迎接2026赛季中超联赛，俱乐部将于3月15日起启动全面备战计划。主要内容包括：</p><ul><li>一线队集结，进行体能恢复测试</li><li>新援融入训练，战术体系磨合</li><li>热身赛安排：3月22日对阵山东泰山、3月25日对阵上海海港</li></ul><p>请全体球员、教练员按时归队，共同为新赛季做好准备。</p>', '2026-03-10 09:00:00');
INSERT INTO `gonggao` VALUES (2, '中超联赛第3轮主场赛事预告', 'upload/gonggao2.jpg', 2, '2026-03-18 14:00:00', '<p>我俱乐部将于3月29日19:35在主场迎战北京国安，这是中超联赛第3轮的关键战役。</p><p><strong>比赛信息：</strong></p><p>时间：2026年3月29日 19:35</p><p>地点：俱乐部主场体育场</p><p>转播：CCTV5、咪咕视频</p><p>欢迎广大球迷到场助威，共同见证精彩对决！</p>', '2026-03-18 14:00:00');
INSERT INTO `gonggao` VALUES (3, '官宣：巴西前锋马科斯正式加盟', 'upload/gonggao3.jpg', 3, '2026-03-08 10:00:00', '<p>经俱乐部与巴西弗拉门戈足球俱乐部友好协商，巴西前锋马科斯正式加盟我俱乐部，转会费1200万欧元，合同为期三年。</p><p><strong>球员简介：</strong></p><p><strong>马科斯，25岁，身高182cm，司职中锋。上赛季巴甲联赛出场32次，打入18球，荣获联赛银靴奖。其出色的门前嗅觉和身体对抗能力将极大增强我队锋线实力。</strong></p><p><strong>欢迎马科斯加入俱乐部大家庭！</strong></p>', '2026-03-08 10:00:00');
INSERT INTO `gonggao` VALUES (5, '关于客场远征军的组织通知', 'upload/gonggao5.jpg', 1, '2026-03-19 16:00:00', '<p>俱乐部将组织3月29日客场对阵北京国安的远征助威团。</p><p><strong>报名信息：</strong></p><p>名额：500人</p><p>费用：往返大巴+球票 380元/人</p><p>集合时间：3月29日 12:00</p><p>集合地点：俱乐部南门停车场</p><p>有意者请于3月25日前联系球迷协会报名。</p>', '2026-03-19 16:00:00');
INSERT INTO `gonggao` VALUES (6, 'U21梯队获得全国青年联赛冠军', 'upload/gonggao6.jpg', 1, '2026-03-15 20:00:00', '<p>喜讯！我俱乐部U21青年队在全国青年足球联赛决赛中2:1战胜广州队，成功夺冠！</p><p>这是俱乐部青训体系的重要成果，展现了俱乐部在人才培养方面的实力。祝贺全体教练员和球员！</p><p>多名青年队球员将在新赛季进入一线队大名单，期待他们的表现。</p>', '2026-03-15 20:00:00');
INSERT INTO `gonggao` VALUES (7, '队长张伟续约至2029年', 'upload/gonggao7.jpg', 3, '2026-03-12 11:00:00', '<p>俱乐部与队长张伟正式续约，新合同至2029年6月。</p><p>张伟，30岁，司职中场，自2020年加盟以来已为球队出场156次，打入28球，助攻45次，是球队中场核心。上赛季荣获中超最佳中场球员称号。</p><p>"这里是我的家，我会继续为冠军而战。"——张伟</p>', '2026-03-12 11:00:00');
INSERT INTO `gonggao` VALUES (8, '俱乐部新训练基地竣工', 'upload/gonggao8.jpg', 1, '2026-03-05 09:00:00', '<p>历时两年建设，俱乐部新训练基地正式竣工并投入使用。</p><p><strong>基地设施：</strong></p><ul><li>4块标准天然草坪训练场</li><li>1块室内人工草坪训练场</li><li>现代化体能训练中心</li><li>运动康复理疗中心</li><li>球员公寓及餐厅</li></ul><p>新基地将为球队提供世界一流的训练条件，助力俱乐部向更高目标迈进。</p>', '2026-03-05 09:00:00');

-- ----------------------------
-- Table structure for hetong
-- ----------------------------
DROP TABLE IF EXISTS `hetong`;
CREATE TABLE `hetong`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `yonghu_id` int(11) NULL DEFAULT NULL COMMENT '用户',
  `hetong_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同标题',
  `hetong_file` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传合同',
  `hetong_text` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  `hetong_delete` int(11) NULL DEFAULT NULL COMMENT '逻辑删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '合同' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hetong
-- ----------------------------
INSERT INTO `hetong` VALUES (1, 1, '张伟续约合同（2026-2029）', 'upload/file.rar', '队长张伟续约三年，年薪800万元，附带队长津贴及进球奖金条款。', 1, '2026-03-12 11:00:00');
INSERT INTO `hetong` VALUES (2, 2, '李强球员合同（2025-2028）', 'upload/file.rar', '门将李强合同，年薪450万元，附带零封奖金条款。', 1, '2025-07-15 10:00:00');
INSERT INTO `hetong` VALUES (3, 3, '王磊青训合同', 'upload/file.rar', '青年队球员王磊晋升一线队合同，为期两年。', 1, '2026-01-20 14:00:00');
INSERT INTO `hetong` VALUES (4, NULL, '某体育品牌赞助合同', 'upload/file.rar', '五年球衣赞助协议，总金额3亿元，包含装备赞助。', 2, '2026-01-01 09:00:00');
INSERT INTO `hetong` VALUES (5, NULL, '新训练基地建设合同', 'upload/file.rar', '训练基地建设工程合同，总造价1.2亿元。', 1, '2024-03-01 09:00:00');
INSERT INTO `hetong` VALUES (6, 1, '马科斯转会合同', 'upload/file.rar', '巴西前锋马科斯转会合同，转会费1200万欧元。', 1, '2026-03-08 10:00:00');

-- ----------------------------
-- Table structure for jiaolian
-- ----------------------------
DROP TABLE IF EXISTS `jiaolian`;
CREATE TABLE `jiaolian`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户',
  `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `jiaolian_uuid_number` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '教练编号',
  `jiaolian_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '教练姓名',
  `jiaolian_phone` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '教练手机号',
  `jiaolian_id_number` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '教练身份证号',
  `jiaolian_photo` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '教练头像',
  `sex_types` int(11) NULL DEFAULT NULL COMMENT '性别',
  `jiaolian_email` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '教练邮箱',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '教练' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jiaolian
-- ----------------------------
INSERT INTO `jiaolian` VALUES (1, 'coach_chen', '123456', '1710000000001', '陈志远', '13800138001', '110101197501011234', 'upload/jiaolian1.jpg', 1, 'chen@club.com', '2026-03-21 09:00:00');
INSERT INTO `jiaolian` VALUES (2, 'coach_wang', '123456', '1710000000002', '王大明', '13800138002', '110101198002022345', 'upload/jiaolian2.jpg', 1, 'wang@club.com', '2026-03-21 09:00:00');
INSERT INTO `jiaolian` VALUES (3, 'coach_li', '123456', '1710000000003', '李雪梅', '13800138003', '110101198503033456', 'upload/jiaolian3.jpg', 2, 'li@club.com', '2026-03-21 09:00:00');
INSERT INTO `jiaolian` VALUES (4, 'coach_zhang', '123456', '1710000000004', '张海涛', '13800138004', '110101197804044567', 'upload/jiaolian1.jpg', 1, 'zhang@club.com', '2026-03-21 09:00:00');

-- ----------------------------
-- Table structure for saishi
-- ----------------------------
DROP TABLE IF EXISTS `saishi`;
CREATE TABLE `saishi`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `saishi_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '赛事名称',
  `saishi_uuid_number` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '赛事编号',
  `saishi_photo` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '赛事照片',
  `saishi_address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '赛事地点',
  `saishi_video` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '赛事视频',
  `saishi_types` int(11) NULL DEFAULT NULL COMMENT '赛事类型',
  `saishi_content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '赛事介绍',
  `saishi_delete` int(11) NULL DEFAULT NULL COMMENT '逻辑删除',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '录入时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '赛事' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of saishi
-- ----------------------------
INSERT INTO `saishi` VALUES (1, '中超联赛第1轮：主场vs上海申花', '1710000000101', 'upload/saishi1.jpg', '俱乐部主场体育场', 'upload/video.mp4', 1, '<p>2026赛季中超联赛首轮，我俱乐部主场迎战上海申花。比赛时间：3月15日19:35。</p><p><strong>预计首发：</strong>4-3-3阵型</p><p>门将：李强</p><p>后卫：刘洋、赵鹏、孙铭、周伟</p><p>中场：张伟、马科斯、陈浩</p><p>前锋：马科斯、王磊、李明</p>', 1, '2026-03-10 09:00:00', '2026-03-10 09:00:00');
INSERT INTO `saishi` VALUES (2, '中超联赛第2轮：客场vs山东泰山', '1710000000102', 'upload/saishi2.jpg', '济南奥体中心', 'upload/video.mp4', 1, '<p>中超联赛第2轮，我俱乐部客场挑战山东泰山。比赛时间：3月22日19:35。</p><p>山东泰山是传统强队，主场战绩出色，我队需做好充分准备。</p>', 1, '2026-03-10 09:00:00', '2026-03-10 09:00:00');
INSERT INTO `saishi` VALUES (3, '中超联赛第3轮：主场vs北京国安', '1710000000103', 'upload/saishi3.jpg', '俱乐部主场体育场', 'upload/video.mp4', 1, '<p>中超联赛第3轮焦点战，我俱乐部主场迎战北京国安。比赛时间：3月29日19:35。</p><p>京沪对决历来精彩，本场关系到赛季初的积分排名走势。</p>', 1, '2026-03-15 09:00:00', '2026-03-15 09:00:00');
INSERT INTO `saishi` VALUES (4, '中超联赛第4轮：客场vs成都蓉城', '1710000000104', 'upload/saishi4.jpg', '成都凤凰山体育场', 'upload/video.mp4', 1, '<p>中超联赛第4轮，客场挑战成都蓉城。比赛时间：4月5日19:35。</p><p>成都蓉城主场氛围火爆，我队需要保持冷静，发挥技战术水平。</p>', 1, '2026-03-15 09:00:00', '2026-03-15 09:00:00');
INSERT INTO `saishi` VALUES (5, '中超联赛第5轮：主场vs广州队', '1710000000105', 'upload/saishi5.jpg', '俱乐部主场体育场', 'upload/video.mp4', 1, '<p>中超联赛第5轮，主场迎战广州队。比赛时间：4月12日19:35。</p><p>广州队本赛季阵容年轻化，技战术特点鲜明，需认真对待。</p>', 1, '2026-03-18 09:00:00', '2026-03-18 09:00:00');
INSERT INTO `saishi` VALUES (6, '足协杯第3轮：主场vs武汉三镇', '1710000000106', 'upload/saishi6.jpg', '俱乐部主场体育场', 'upload/video.mp4', 2, '<p>足协杯第3轮，主场迎战武汉三镇。比赛时间：4月16日19:00。</p><p>足协杯是争冠的重要战线，我队将全力出战。</p>', 1, '2026-03-18 09:00:00', '2026-03-18 09:00:00');
INSERT INTO `saishi` VALUES (7, '亚冠小组赛第1轮：主场vs韩国全北现代', '1710000000107', 'upload/saishi7.jpg', '俱乐部主场体育场', 'upload/video.mp4', 3, '<p>2026亚冠联赛小组赛首轮，主场迎战韩国K联赛冠军全北现代。比赛时间：4月20日20:00。</p><p>这是俱乐部时隔两年重返亚冠赛场，全队上下高度重视。</p>', 1, '2026-03-20 09:00:00', '2026-03-20 09:00:00');
INSERT INTO `saishi` VALUES (8, '亚冠小组赛第2轮：客场vs日本横滨水手', '1710000000108', 'upload/saishi8.jpg', '横滨国际综合竞技场', 'upload/video.mp4', 3, '<p>亚冠小组赛第2轮，客场挑战日本J联赛冠军横滨水手。比赛时间：4月27日18:00。</p><p>横滨水手技术细腻，传控能力强，我队需要发挥身体优势。</p>', 1, '2026-03-20 09:00:00', '2026-03-20 09:00:00');
INSERT INTO `saishi` VALUES (9, '热身赛：主场vs浙江队', '1710000000109', 'upload/saishi9.jpg', '俱乐部主场体育场', 'upload/video.mp4', 4, '<p>赛季前热身赛，主场对阵浙江队。比赛时间：3月8日15:00。</p><p>通过热身赛检验冬训成果，调整比赛状态。</p>', 1, '2026-03-01 09:00:00', '2026-03-01 09:00:00');
INSERT INTO `saishi` VALUES (10, '热身赛：中立场vs河南队', '1710000000110', 'upload/saishi10.jpg', '海口观澜湖足球基地', 'upload/video.mp4', 4, '<p>赛季前热身赛第2场，中立场对阵河南队。比赛时间：3月11日15:00。</p><p>继续磨合阵容，确定新赛季主力框架。</p>', 1, '2026-03-01 09:00:00', '2026-03-01 09:00:00');

-- ----------------------------
-- Table structure for shuju
-- ----------------------------
DROP TABLE IF EXISTS `shuju`;
CREATE TABLE `shuju`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `yonghu_id` int(11) NULL DEFAULT NULL COMMENT '用户',
  `shuju_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '球员数据名称',
  `shuju_uuid_number` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '球员数据编号',
  `shuju_photo` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '球员数据照片',
  `shuju_types` int(11) NULL DEFAULT NULL COMMENT '球员数据类型',
  `shuju_time` date NULL DEFAULT NULL COMMENT '日期',
  `shuju_content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '球员数据介绍',
  `shuju_delete` int(11) NULL DEFAULT NULL COMMENT '逻辑删除',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '录入时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '球员数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shuju
-- ----------------------------
INSERT INTO `shuju` VALUES (1, 1, '队长张伟 - 2025赛季数据', '1710000000201', 'upload/shuju1.jpg', 2, '2025-12-31', '<p><strong>张伟（队长/中场）</strong></p><p>出场：32次（首发30次）</p><p>进球：8球</p><p>助攻：12次</p><p>传球成功率：87%</p><p>关键传球：96次</p><p>抢断：78次</p><p>荣获2025赛季中超最佳中场球员</p>', 1, '2026-01-15 10:00:00', '2026-01-15 10:00:00');
INSERT INTO `shuju` VALUES (2, 1, '马科斯 - 2025赛季数据（巴西）', '1710000000202', 'upload/shuju2.jpg', 1, '2025-12-31', '<p><strong>马科斯（前锋/新援）</strong></p><p>出场：30次（首发28次）</p><p>进球：18球</p><p>助攻：5次</p><p>射门：98次</p><p>射正率：48%</p><p>过人成功率：62%</p><p>巴甲联赛银靴奖得主</p>', 1, '2026-03-08 10:00:00', '2026-03-08 10:00:00');
INSERT INTO `shuju` VALUES (3, 2, '李强 - 2025赛季数据', '1710000000203', 'upload/shuju3.jpg', 2, '2025-12-31', '<p><strong>李强（门将）</strong></p><p>出场：30次（首发30次）</p><p>扑救：112次</p><p>扑救成功率：78%</p><p>零封：12场</p><p>出击成功：23次</p><p>传球成功率：82%</p>', 1, '2026-01-15 10:00:00', '2026-01-15 10:00:00');
INSERT INTO `shuju` VALUES (4, 1, '王磊 - 2025赛季数据', '1710000000204', 'upload/shuju4.jpg', 1, '2025-12-31', '<p><strong>王磊（前锋）</strong></p><p>出场：28次（首发22次）</p><p>进球：12球</p><p>助攻：6次</p><p>射门：76次</p><p>射正率：42%</p><p>头球进球：4个</p>', 1, '2026-01-15 10:00:00', '2026-01-15 10:00:00');
INSERT INTO `shuju` VALUES (5, 3, '刘洋 - 2025赛季数据', '1710000000205', 'upload/shuju5.jpg', 2, '2025-12-31', '<p><strong>刘洋（边后卫）</strong></p><p>出场：31次（首发30次）</p><p>进球：2球</p><p>助攻：8次</p><p>抢断：102次</p><p>拦截：56次</p><p>传中成功率：35%</p>', 1, '2026-01-15 10:00:00', '2026-01-15 10:00:00');
INSERT INTO `shuju` VALUES (6, 1, '陈浩 - 2025赛季数据', '1710000000206', 'upload/shuju6.jpg', 2, '2025-12-31', '<p><strong>陈浩（中场）</strong></p><p>出场：29次（首发26次）</p><p>进球：5球</p><p>助攻：9次</p><p>传球成功率：85%</p><p>长传成功率：72%</p><p>远射进球：3个</p>', 1, '2026-01-15 10:00:00', '2026-01-15 10:00:00');

-- ----------------------------
-- Table structure for token
-- ----------------------------
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `tablename` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名',
  `role` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色',
  `token` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '令牌',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '令牌表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of token
-- ----------------------------
INSERT INTO `token` VALUES (1, 1, 'zhangwei', 'yonghu', '用户', 'sy262982zkm0x72rg5vetfexnsplhern', '2026-03-23 11:49:59', '2026-05-07 17:44:39');
INSERT INTO `token` VALUES (2, 2, 'manager', 'users', '管理员', 's2ev7aci13rdgy6wl22jh7wz9p1cvfdj', '2026-03-23 11:51:28', '2026-05-07 17:44:07');
INSERT INTO `token` VALUES (3, 1, 'coach_chen', 'jiaolian', '教练', 'jje7nkgkcft6rxc0yuhb0cd71vuqz1ib', '2026-03-25 18:33:38', '2026-03-25 22:00:29');
INSERT INTO `token` VALUES (4, 2, 'liqiang', 'yonghu', '用户', 'a74sh6c55ukzi7we3wb9fy00q6n96ybr', '2026-04-11 22:52:41', '2026-04-11 23:52:42');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `role` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '管理员' COMMENT '角色',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '管理员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (2, 'manager', 'manager', '管理员', '2026-03-21 09:00:00');

-- ----------------------------
-- Table structure for xunlian
-- ----------------------------
DROP TABLE IF EXISTS `xunlian`;
CREATE TABLE `xunlian`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `yonghu_id` int(11) NULL DEFAULT NULL COMMENT '用户',
  `xunlian_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '训练计划名称',
  `xunlian_uuid_number` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '训练计划编号',
  `xunlian_photo` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '训练计划照片',
  `xunlian_types` int(11) NULL DEFAULT NULL COMMENT '训练计划类型',
  `xunlian_kemu` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '训练科目',
  `xunlian_time` date NULL DEFAULT NULL COMMENT '日期',
  `xunlian_content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '训练计划介绍',
  `xunlian_delete` int(11) NULL DEFAULT NULL COMMENT '逻辑删除',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '录入时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '训练计划' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of xunlian
-- ----------------------------
INSERT INTO `xunlian` VALUES (1, NULL, '周一：体能强化训练', '1710000000301', 'upload/xunlian1.jpg', 1, '体能训练', '2026-03-17', '<p><strong>训练时间：</strong>09:00-11:30</p><p><strong>训练内容：</strong></p><ul><li>热身跑：15分钟</li><li>核心力量训练：45分钟</li><li>有氧耐力训练：30分钟</li><li>拉伸放松：20分钟</li></ul><p><strong>目标：</strong>提升球员基础体能储备，为赛季做好准备。</p>', 1, '2026-03-15 09:00:00', '2026-03-15 09:00:00');
INSERT INTO `xunlian` VALUES (2, NULL, '周二：战术配合训练', '1710000000302', 'upload/xunlian2.jpg', 2, '战术训练', '2026-03-18', '<p><strong>训练时间：</strong>09:00-11:30</p><p><strong>训练内容：</strong></p><ul><li>视频分析（对手分析）：30分钟</li><li>定位球演练：45分钟</li><li>进攻套路配合：45分钟</li><li>防守站位练习：30分钟</li></ul><p><strong>目标：</strong>强化球队整体战术执行力。</p>', 1, '2026-03-15 09:00:00', '2026-03-15 09:00:00');
INSERT INTO `xunlian` VALUES (3, NULL, '周三：技术专项训练', '1710000000303', 'upload/xunlian3.jpg', 3, '技术训练', '2026-03-19', '<p><strong>训练时间：</strong>09:00-11:30</p><p><strong>训练内容：</strong></p><ul><li>传球精度练习：30分钟</li><li>射门训练：45分钟</li><li>头球争顶训练：30分钟</li><li>小场地对抗赛：45分钟</li></ul><p><strong>目标：</strong>提升球员个人技术能力。</p>', 1, '2026-03-15 09:00:00', '2026-03-15 09:00:00');
INSERT INTO `xunlian` VALUES (4, NULL, '周四：恢复性训练', '1710000000304', 'upload/xunlian4.jpg', 4, '恢复训练', '2026-03-20', '<p><strong>训练时间：</strong>09:00-10:30</p><p><strong>训练内容：</strong></p><ul><li>轻松慢跑：20分钟</li><li>游泳放松：30分钟</li><li>按摩理疗：40分钟</li></ul><p><strong>目标：</strong>帮助球员恢复体能，预防伤病。</p>', 1, '2026-03-15 09:00:00', '2026-03-15 09:00:00');
INSERT INTO `xunlian` VALUES (5, NULL, '周五：赛前针对性训练', '1710000000305', 'upload/xunlian5.jpg', 2, '战术训练', '2026-03-21', '<p><strong>训练时间：</strong>09:00-11:00</p><p><strong>训练内容：</strong></p><ul><li>对手技战术分析：30分钟</li><li>针对性战术演练：60分钟</li><li>定位球攻防演练：30分钟</li></ul><p><strong>目标：</strong>为周末比赛做好战术准备。</p>', 1, '2026-03-15 09:00:00', '2026-03-15 09:00:00');
INSERT INTO `xunlian` VALUES (6, NULL, '周六：赛前适应训练', '1710000000306', 'upload/xunlian6.jpg', 4, '恢复训练', '2026-03-22', '<p><strong>训练时间：</strong>10:00-11:00</p><p><strong>训练内容：</strong></p><ul><li>场地适应：20分钟</li><li>传接球练习：20分钟</li><li>射门热身：20分钟</li></ul><p><strong>目标：</strong>保持比赛状态，调整心态。</p>', 1, '2026-03-15 09:00:00', '2026-03-15 09:00:00');
INSERT INTO `xunlian` VALUES (7, NULL, '守门员专项训练', '1710000000307', 'upload/xunlian7.jpg', 3, '技术训练', '2026-03-17', '<p><strong>训练时间：</strong>14:00-16:00</p><p><strong>参训人员：</strong>守门员组</p><p><strong>训练内容：</strong></p><ul><li>扑救反应训练：45分钟</li><li>出击时机训练：30分钟</li><li>门线技术训练：30分钟</li><li>大脚开球训练：15分钟</li></ul>', 1, '2026-03-15 09:00:00', '2026-03-15 09:00:00');
INSERT INTO `xunlian` VALUES (8, NULL, '青年队联合训练', '1710000000308', 'upload/xunlian8.jpg', 1, '体能训练', '2026-03-19', '<p><strong>训练时间：</strong>14:00-16:00</p><p><strong>参训人员：</strong>一线队替补 + U21青年队</p><p><strong>训练内容：</strong></p><ul><li>联合体能训练：45分钟</li><li>分组对抗赛：75分钟</li></ul><p><strong>目标：</strong>考察青年队球员，保持替补球员状态。</p>', 1, '2026-03-15 09:00:00', '2026-03-15 09:00:00');
INSERT INTO `xunlian` VALUES (9, NULL, '定位球专项训练', '1710000000309', 'upload/xunlian9.jpg', 3, '技术训练', '2026-03-20', '<p><strong>训练时间：</strong>15:00-16:30</p><p><strong>训练内容：</strong></p><ul><li>角球进攻套路：30分钟</li><li>任意球配合：30分钟</li><li>点球练习：15分钟</li><li>界外球战术：15分钟</li></ul><p><strong>目标：</strong>提升定位球得分效率。</p>', 1, '2026-03-18 09:00:00', '2026-03-18 09:00:00');
INSERT INTO `xunlian` VALUES (10, NULL, '伤病球员康复训练', '1710000000310', 'upload/xunlian10.jpg', 4, '恢复训练', '2026-03-21', '<p><strong>训练时间：</strong>09:00-10:30</p><p><strong>参训人员：</strong>伤病恢复期球员</p><p><strong>训练内容：</strong></p><ul><li>水疗放松：20分钟</li><li>核心稳定训练：25分钟</li><li>下肢力量恢复：25分钟</li><li>拉伸理疗：20分钟</li></ul>', 1, '2026-03-18 09:00:00', '2026-03-18 09:00:00');
INSERT INTO `xunlian` VALUES (11, 0, '体能训练', '1775918918239', 'upload/xunlian11.jpg', 4, '跑步', '2026-04-24', '1111111111111111', 1, '2026-04-11 22:49:09', '2026-04-11 22:49:09');
INSERT INTO `xunlian` VALUES (12, 1, '体能训练', '1778143499735', 'upload/xunlian12.jpg', 4, '跑步', '2026-05-13', '体能训练1小时', 1, '2026-05-07 16:45:26', '2026-05-07 16:45:26');

-- ----------------------------
-- Table structure for yonghu
-- ----------------------------
DROP TABLE IF EXISTS `yonghu`;
CREATE TABLE `yonghu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户',
  `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `yonghu_uuid_number` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户编号',
  `yonghu_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `yonghu_phone` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户手机号',
  `yonghu_id_number` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户身份证号',
  `yonghu_photo` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `sex_types` int(11) NULL DEFAULT NULL COMMENT '性别',
  `yonghu_email` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of yonghu
-- ----------------------------
INSERT INTO `yonghu` VALUES (1, 'zhangwei', '123456', '1710000000001', '张伟', '13900139001', '110101199501011234', 'upload/yonghu1.jpg', 1, 'zhangwei@club.com', '2026-03-21 09:00:00');
INSERT INTO `yonghu` VALUES (2, 'liqiang', '123456', '1710000000002', '李强', '13900139002', '110101199602022345', 'upload/yonghu2.jpg', 1, 'liqiang@club.com', '2026-03-21 09:00:00');
INSERT INTO `yonghu` VALUES (3, 'wanglei', '123456', '1710000000003', '王磊', '13900139003', '110101199903033456', 'upload/yonghu3.jpg', 1, 'wanglei@club.com', '2026-03-21 09:00:00');
INSERT INTO `yonghu` VALUES (4, 'liuyang', '123456', '1710000000004', '刘洋', '13900139004', '110101199704044567', 'upload/yonghu1.jpg', 1, 'liuyang@club.com', '2026-03-21 09:00:00');
INSERT INTO `yonghu` VALUES (5, 'chenhao', '123456', '1710000000005', '陈浩', '13900139005', '110101199805055678', 'upload/yonghu2.jpg', 1, 'chenhao@club.com', '2026-03-21 09:00:00');
INSERT INTO `yonghu` VALUES (6, 'marcos', '123456', '1710000000006', '马科斯', '13900139006', '110101199906066789', 'upload/yonghu3.jpg', 2, 'marcos@club.com', '2026-03-21 09:00:00');

SET FOREIGN_KEY_CHECKS = 1;


