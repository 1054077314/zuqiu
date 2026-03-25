/*
SQLyog Ultimate v11.3 (64 bit)
MySQL - 5.7.32-log : Database - zuqiujulebguanli
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`zuqiujulebguanli` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `zuqiujulebguanli`;

/*Table structure for table `config` */

DROP TABLE IF EXISTS `config`;

CREATE TABLE `config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '涓婚敭',
  `name` varchar(100) NOT NULL COMMENT '閰嶇疆鍙傛暟鍚嶇О',
  `value` varchar(100) DEFAULT NULL COMMENT '閰嶇疆鍙傛暟鍊?,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='绯荤粺閰嶇疆';

/*Data for the table `config` */

insert  into `config`(`id`,`name`,`value`) values 
(1,'棣栭〉杞挱鍥?','https://img-blog.csdnimg.cn/8989654e0199472782e09575990c1234.jpeg'),
(2,'棣栭〉杞挱鍥?','https://img-blog.csdnimg.cn/1234567890abcdef1234567890abcdef.jpeg'),
(3,'棣栭〉杞挱鍥?','https://img-blog.csdnimg.cn/abcdef1234567890abcdef1234567890.jpeg');

/*Table structure for table `dictionary` */

DROP TABLE IF EXISTS `dictionary`;

CREATE TABLE `dictionary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '涓婚敭',
  `dic_code` varchar(200) DEFAULT NULL COMMENT '瀛楀吀缂栫爜',
  `dic_name` varchar(200) DEFAULT NULL COMMENT '瀛楀吀鍚嶇О',
  `code_index` int(11) DEFAULT NULL COMMENT '缂栫爜',
  `index_name` varchar(200) DEFAULT NULL COMMENT '缂栫爜鍚嶇О',
  `super_id` int(11) DEFAULT NULL COMMENT '鐖剁骇瀛楁ID',
  `beizhu` varchar(200) DEFAULT NULL COMMENT '澶囨敞',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='瀛楀吀';

/*Data for the table `dictionary` */

insert  into `dictionary`(`id`,`dic_code`,`dic_name`,`code_index`,`index_name`,`super_id`,`beizhu`,`create_time`) values 
(1,'sex_types','鎬у埆绫诲瀷',1,'鐢?,NULL,NULL,'2026-03-21 09:00:00'),
(2,'sex_types','鎬у埆绫诲瀷',2,'濂?,NULL,NULL,'2026-03-21 09:00:00'),
(3,'gonggao_types','鍏憡绫诲瀷',1,'淇变箰閮ㄥ叕鍛?,NULL,NULL,'2026-03-21 09:00:00'),
(4,'gonggao_types','鍏憡绫诲瀷',2,'璧涗簨閫氱煡',NULL,NULL,'2026-03-21 09:00:00'),
(5,'gonggao_types','鍏憡绫诲瀷',3,'杞細鍔ㄦ€?,NULL,NULL,'2026-03-21 09:00:00'),
(6,'gonggao_types','鍏憡绫诲瀷',4,'娲诲姩鍏憡',NULL,NULL,'2026-03-21 09:00:00'),
(7,'saishi_types','璧涗簨绫诲瀷',1,'涓秴鑱旇禌',NULL,NULL,'2026-03-21 09:00:00'),
(8,'saishi_types','璧涗簨绫诲瀷',2,'瓒冲崗鏉?,NULL,NULL,'2026-03-21 09:00:00'),
(9,'saishi_types','璧涗簨绫诲瀷',3,'浜氬啝鑱旇禌',NULL,NULL,'2026-03-21 09:00:00'),
(10,'saishi_types','璧涗簨绫诲瀷',4,'鐑韩璧?,NULL,NULL,'2026-03-21 09:00:00'),
(11,'xunlian_types','璁粌璁″垝绫诲瀷',1,'浣撹兘璁粌',NULL,NULL,'2026-03-21 09:00:00'),
(12,'xunlian_types','璁粌璁″垝绫诲瀷',2,'鎴樻湳璁粌',NULL,NULL,'2026-03-21 09:00:00'),
(13,'xunlian_types','璁粌璁″垝绫诲瀷',3,'鎶€鏈缁?,NULL,NULL,'2026-03-21 09:00:00'),
(14,'xunlian_types','璁粌璁″垝绫诲瀷',4,'鎭㈠璁粌',NULL,NULL,'2026-03-21 09:00:00'),
(15,'shuju_types','鐞冨憳鏁版嵁绫诲瀷',1,'鍓嶉攱鏁版嵁',NULL,NULL,'2026-03-21 09:00:00'),
(16,'shuju_types','鐞冨憳鏁版嵁绫诲瀷',2,'涓満鏁版嵁',NULL,NULL,'2026-03-21 09:00:00');

/*Table structure for table `gonggao` */

DROP TABLE IF EXISTS `gonggao`;

CREATE TABLE `gonggao` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '涓婚敭',
  `gonggao_name` varchar(200) DEFAULT NULL COMMENT '鍏憡鍚嶇О',
  `gonggao_photo` varchar(200) DEFAULT NULL COMMENT '鍏憡鍥剧墖',
  `gonggao_types` int(11) NOT NULL COMMENT '鍏憡绫诲瀷',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '鍙戝竷鏃堕棿',
  `gonggao_content` longtext COMMENT '鍏憡璇︽儏',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='鍏憡淇℃伅';

/*Data for the table `gonggao` */

insert  into `gonggao`(`id`,`gonggao_name`,`gonggao_photo`,`gonggao_types`,`insert_time`,`gonggao_content`,`create_time`) values 
(1,'2026璧涘澶囨垬璁″垝姝ｅ紡鍚姩','https://img-blog.csdnimg.cn/20240101000001.jpg',1,'2026-03-10 09:00:00','<p>涓鸿繋鎺?026璧涘涓秴鑱旇禌锛屼勘涔愰儴灏嗕簬3鏈?5鏃ヨ捣鍚姩鍏ㄩ潰澶囨垬璁″垝銆備富瑕佸唴瀹瑰寘鎷細</p><ul><li>涓€绾块槦闆嗙粨锛岃繘琛屼綋鑳芥仮澶嶆祴璇?/li><li>鏂版彺铻嶅叆璁粌锛屾垬鏈綋绯荤（鍚?/li><li>鐑韩璧涘畨鎺掞細3鏈?2鏃ュ闃靛北涓滄嘲灞便€?鏈?5鏃ュ闃典笂娴锋捣娓?/li></ul><p>璇峰叏浣撶悆鍛樸€佹暀缁冨憳鎸夋椂褰掗槦锛屽叡鍚屼负鏂拌禌瀛ｅ仛濂藉噯澶囥€?/p>','2026-03-10 09:00:00'),
(2,'涓秴鑱旇禌绗?杞富鍦鸿禌浜嬮鍛?,'https://img-blog.csdnimg.cn/20240101000002.jpg',2,'2026-03-18 14:00:00','<p>鎴戜勘涔愰儴灏嗕簬3鏈?9鏃?9:35鍦ㄤ富鍦鸿繋鎴樺寳浜浗瀹夛紝杩欐槸涓秴鑱旇禌绗?杞殑鍏抽敭鎴樺焦銆?/p><p><strong>姣旇禌淇℃伅锛?/strong></p><p>鏃堕棿锛?026骞?鏈?9鏃?19:35</p><p>鍦扮偣锛氫勘涔愰儴涓诲満浣撹偛鍦?/p><p>杞挱锛欳CTV5銆佸挭鍜曡棰?/p><p>娆㈣繋骞垮ぇ鐞冭糠鍒板満鍔╁▉锛屽叡鍚岃璇佺簿褰╁鍐筹紒</p>','2026-03-18 14:00:00'),
(3,'瀹樺锛氬反瑗垮墠閿嬮┈绉戞柉姝ｅ紡鍔犵洘','https://img-blog.csdnimg.cn/20240101000003.jpg',3,'2026-03-08 10:00:00','<p>缁忎勘涔愰儴涓庡反瑗垮紬鎷夐棬鎴堣冻鐞冧勘涔愰儴鍙嬪ソ鍗忓晢锛屽反瑗垮墠閿嬮┈绉戞柉姝ｅ紡鍔犵洘鎴戜勘涔愰儴锛岃浆浼氳垂1200涓囨鍏冿紝鍚堝悓涓烘湡涓夊勾銆?/p><p><strong>鐞冨憳绠€浠嬶細</strong></p><p>椹鏂紝25宀侊紝韬珮182cm锛屽徃鑱屼腑閿嬨€備笂璧涘宸寸敳鑱旇禌鍑哄満32娆★紝鎵撳叆18鐞冿紝鑽ｈ幏鑱旇禌閾堕澊濂栥€傚叾鍑鸿壊鐨勯棬鍓嶅梾瑙夊拰韬綋瀵规姉鑳藉姏灏嗘瀬澶у寮烘垜闃熼攱绾垮疄鍔涖€?/p><p>娆㈣繋椹鏂姞鍏ヤ勘涔愰儴澶у搴紒</p>','2026-03-08 10:00:00'),
(4,'鐞冭糠寮€鏀炬棩娲诲姩閫氱煡','https://img-blog.csdnimg.cn/20240101000004.jpg',4,'2026-03-20 08:00:00','<p>涓哄洖棣堝箍澶х悆杩风殑鏀寔锛屼勘涔愰儴灏嗕簬4鏈?鏃ヤ妇鍔?026骞村害鐞冭糠寮€鏀炬棩娲诲姩銆?/p><p><strong>娲诲姩瀹夋帓锛?/strong></p><ul><li>10:00-12:00 鐞冨憳绛惧悕鍚堝奖浼?/li><li>13:00-14:30 璁粌鍦哄叕寮€璁粌瑙傛懇</li><li>14:30-15:30 淇变箰閮ㄨ崳瑾夊鍙傝</li><li>15:30-17:00 鐞冭糠浜掑姩娓告垙鍙婃娊濂?/li></ul><p>鎶ュ悕鏂瑰紡锛氬叧娉ㄤ勘涔愰儴瀹樻柟鍏紬鍙凤紝鐐瑰嚮鑿滃崟鏍?娲诲姩鎶ュ悕"銆?/p>','2026-03-20 08:00:00'),
(5,'鍏充簬瀹㈠満杩滃緛鍐涚殑缁勭粐閫氱煡','https://img-blog.csdnimg.cn/20240101000005.jpg',1,'2026-03-19 16:00:00','<p>淇变箰閮ㄥ皢缁勭粐3鏈?9鏃ュ鍦哄闃靛寳浜浗瀹夌殑杩滃緛鍔╁▉鍥€?/p><p><strong>鎶ュ悕淇℃伅锛?/strong></p><p>鍚嶉锛?00浜?/p><p>璐圭敤锛氬線杩斿ぇ宸?鐞冪エ 380鍏?浜?/p><p>闆嗗悎鏃堕棿锛?鏈?9鏃?12:00</p><p>闆嗗悎鍦扮偣锛氫勘涔愰儴鍗楅棬鍋滆溅鍦?/p><p>鏈夋剰鑰呰浜?鏈?5鏃ュ墠鑱旂郴鐞冭糠鍗忎細鎶ュ悕銆?/p>','2026-03-19 16:00:00'),
(6,'U21姊槦鑾峰緱鍏ㄥ浗闈掑勾鑱旇禌鍐犲啗','https://img-blog.csdnimg.cn/20240101000006.jpg',1,'2026-03-15 20:00:00','<p>鍠滆锛佹垜淇变箰閮║21闈掑勾闃熷湪鍏ㄥ浗闈掑勾瓒崇悆鑱旇禌鍐宠禌涓?:1鎴樿儨骞垮窞闃燂紝鎴愬姛澶哄啝锛?/p><p>杩欐槸淇变箰閮ㄩ潚璁綋绯荤殑閲嶈鎴愭灉锛屽睍鐜颁簡淇变箰閮ㄥ湪浜烘墠鍩瑰吇鏂归潰鐨勫疄鍔涖€傜璐哄叏浣撴暀缁冨憳鍜岀悆鍛橈紒</p><p>澶氬悕闈掑勾闃熺悆鍛樺皢鍦ㄦ柊璧涘杩涘叆涓€绾块槦澶у悕鍗曪紝鏈熷緟浠栦滑鐨勮〃鐜般€?/p>','2026-03-15 20:00:00'),
(7,'闃熼暱寮犱紵缁害鑷?029骞?,'https://img-blog.csdnimg.cn/20240101000007.jpg',3,'2026-03-12 11:00:00','<p>淇变箰閮ㄤ笌闃熼暱寮犱紵姝ｅ紡缁害锛屾柊鍚堝悓鑷?029骞?鏈堛€?/p><p>寮犱紵锛?0宀侊紝鍙歌亴涓満锛岃嚜2020骞村姞鐩熶互鏉ュ凡涓虹悆闃熷嚭鍦?56娆★紝鎵撳叆28鐞冿紝鍔╂敾45娆★紝鏄悆闃熶腑鍦烘牳蹇冦€備笂璧涘鑽ｈ幏涓秴鏈€浣充腑鍦虹悆鍛樼О鍙枫€?/p><p>"杩欓噷鏄垜鐨勫锛屾垜浼氱户缁负鍐犲啗鑰屾垬銆?鈥斺€斿紶浼?/p>','2026-03-12 11:00:00'),
(8,'淇变箰閮ㄦ柊璁粌鍩哄湴绔ｅ伐','https://img-blog.csdnimg.cn/20240101000008.jpg',1,'2026-03-05 09:00:00','<p>鍘嗘椂涓ゅ勾寤鸿锛屼勘涔愰儴鏂拌缁冨熀鍦版寮忕宸ュ苟鎶曞叆浣跨敤銆?/p><p><strong>鍩哄湴璁炬柦锛?/strong></p><ul><li>4鍧楁爣鍑嗗ぉ鐒惰崏鍧缁冨満</li><li>1鍧楀鍐呬汉宸ヨ崏鍧缁冨満</li><li>鐜颁唬鍖栦綋鑳借缁冧腑蹇?/li><li>杩愬姩搴峰鐞嗙枟涓績</li><li>鐞冨憳鍏瘬鍙婇鍘?/li></ul><p>鏂板熀鍦板皢涓虹悆闃熸彁渚涗笘鐣屼竴娴佺殑璁粌鏉′欢锛屽姪鍔涗勘涔愰儴鍚戞洿楂樼洰鏍囪繄杩涖€?/p>','2026-03-05 09:00:00');

/*Table structure for table `hetong` */

DROP TABLE IF EXISTS `hetong`;

CREATE TABLE `hetong` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '涓婚敭',
  `yonghu_id` int(11) DEFAULT NULL COMMENT '鐢ㄦ埛',
  `hetong_name` varchar(200) DEFAULT NULL COMMENT '鍚堝悓鏍囬',
  `hetong_file` varchar(200) DEFAULT NULL COMMENT '涓婁紶鍚堝悓',
  `hetong_text` text COMMENT '澶囨敞',
  `hetong_delete` int(11) DEFAULT NULL COMMENT '閫昏緫鍒犻櫎',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='鍚堝悓';

/*Data for the table `hetong` */

insert  into `hetong`(`id`,`yonghu_id`,`hetong_name`,`hetong_file`,`hetong_text`,`hetong_delete`,`create_time`) values 
(1,1,'寮犱紵缁害鍚堝悓锛?026-2029锛?,'https://download.csdn.net/file/202401/hetong1.pdf','闃熼暱寮犱紵缁害涓夊勾锛屽勾钖?00涓囧厓锛岄檮甯﹂槦闀挎触璐村強杩涚悆濂栭噾鏉℃銆?,1,'2026-03-12 11:00:00'),
(2,2,'鏉庡己鐞冨憳鍚堝悓锛?025-2028锛?,'https://download.csdn.net/file/202401/hetong2.pdf','闂ㄥ皢鏉庡己鍚堝悓锛屽勾钖?50涓囧厓锛岄檮甯﹂浂灏佸閲戞潯娆俱€?,1,'2025-07-15 10:00:00'),
(3,3,'鐜嬬闈掕鍚堝悓','https://download.csdn.net/file/202401/hetong3.pdf','闈掑勾闃熺悆鍛樼帇纾婃檵鍗囦竴绾块槦鍚堝悓锛屼负鏈熶袱骞淬€?,1,'2026-01-20 14:00:00'),
(4,NULL,'鏌愪綋鑲插搧鐗岃禐鍔╁悎鍚?,'https://download.csdn.net/file/202401/hetong4.pdf','浜斿勾鐞冭。璧炲姪鍗忚锛屾€婚噾棰?浜垮厓锛屽寘鍚澶囪禐鍔┿€?,1,'2026-01-01 09:00:00'),
(5,NULL,'鏂拌缁冨熀鍦板缓璁惧悎鍚?,'https://download.csdn.net/file/202401/hetong5.pdf','璁粌鍩哄湴寤鸿宸ョ▼鍚堝悓锛屾€婚€犱环1.2浜垮厓銆?,1,'2024-03-01 09:00:00'),
(6,1,'椹鏂浆浼氬悎鍚?,'https://download.csdn.net/file/202401/hetong6.pdf','宸磋タ鍓嶉攱椹鏂浆浼氬悎鍚岋紝杞細璐?200涓囨鍏冦€?,1,'2026-03-08 10:00:00');

/*Table structure for table `jiaolian` */

DROP TABLE IF EXISTS `jiaolian`;

CREATE TABLE `jiaolian` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '涓婚敭',
  `username` varchar(200) DEFAULT NULL COMMENT '璐︽埛',
  `password` varchar(200) DEFAULT NULL COMMENT '瀵嗙爜',
  `jiaolian_uuid_number` varchar(200) DEFAULT NULL COMMENT '鏁欑粌缂栧彿',
  `jiaolian_name` varchar(200) DEFAULT NULL COMMENT '鏁欑粌濮撳悕',
  `jiaolian_phone` varchar(200) DEFAULT NULL COMMENT '鏁欑粌鎵嬫満鍙?,
  `jiaolian_id_number` varchar(200) DEFAULT NULL COMMENT '鏁欑粌韬唤璇佸彿',
  `jiaolian_photo` varchar(200) DEFAULT NULL COMMENT '鏁欑粌澶村儚',
  `sex_types` int(11) DEFAULT NULL COMMENT '鎬у埆',
  `jiaolian_email` varchar(200) DEFAULT NULL COMMENT '鏁欑粌閭',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='鏁欑粌';

/*Data for the table `jiaolian` */

insert  into `jiaolian`(`id`,`username`,`password`,`jiaolian_uuid_number`,`jiaolian_name`,`jiaolian_phone`,`jiaolian_id_number`,`jiaolian_photo`,`sex_types`,`jiaolian_email`,`create_time`) values 
(1,'coach_chen','123456','1710000000001','闄堝織杩?,'13800138001','110101197501011234','https://img-blog.csdnimg.cn/20240101000101.jpg',1,'chen@club.com','2026-03-21 09:00:00'),
(2,'coach_wang','123456','1710000000002','鐜嬪ぇ鏄?,'13800138002','110101198002022345','https://img-blog.csdnimg.cn/20240101000102.jpg',1,'wang@club.com','2026-03-21 09:00:00'),
(3,'coach_li','123456','1710000000003','鏉庨洩姊?,'13800138003','110101198503033456','https://img-blog.csdnimg.cn/20240101000103.jpg',2,'li@club.com','2026-03-21 09:00:00'),
(4,'coach_zhang','123456','1710000000004','寮犳捣娑?,'13800138004','110101197804044567','https://img-blog.csdnimg.cn/20240101000104.jpg',1,'zhang@club.com','2026-03-21 09:00:00');

/*Table structure for table `saishi` */

DROP TABLE IF EXISTS `saishi`;

CREATE TABLE `saishi` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '涓婚敭',
  `saishi_name` varchar(200) DEFAULT NULL COMMENT '璧涗簨鍚嶇О',
  `saishi_uuid_number` varchar(200) DEFAULT NULL COMMENT '璧涗簨缂栧彿',
  `saishi_photo` varchar(200) DEFAULT NULL COMMENT '璧涗簨鐓х墖',
  `saishi_address` varchar(200) DEFAULT NULL COMMENT '璧涗簨鍦扮偣',
  `saishi_types` int(11) DEFAULT NULL COMMENT '璧涗簨绫诲瀷',
  `saishi_content` longtext COMMENT '璧涗簨浠嬬粛',
  `saishi_delete` int(11) DEFAULT NULL COMMENT '閫昏緫鍒犻櫎',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '褰曞叆鏃堕棿',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='璧涗簨';

/*Data for the table `saishi` */

(1,'涓秴鑱旇禌绗?杞細涓诲満vs涓婃捣鐢宠姳','1710000000101','https://img-blog.csdnimg.cn/20240101000201.jpg','淇变箰閮ㄤ富鍦轰綋鑲插満',1,'<p>2026璧涘涓秴鑱旇禌棣栬疆锛屾垜淇变箰閮ㄤ富鍦鸿繋鎴樹笂娴风敵鑺便€傛瘮璧涙椂闂达細3鏈?5鏃?9:35銆?/p><p><strong>棰勮棣栧彂锛?/strong>4-3-3闃靛瀷</p><p>闂ㄥ皢锛氭潕寮?/p><p>鍚庡崼锛氬垬娲嬨€佽档楣忋€佸瓩閾€佸懆浼?/p><p>涓満锛氬紶浼熴€侀┈绉戞柉銆侀檲娴?/p><p>鍓嶉攱锛氶┈绉戞柉銆佺帇纾娿€佹潕鏄?/p>',1,'2026-03-10 09:00:00','2026-03-10 09:00:00'),
(2,'涓秴鑱旇禌绗?杞細瀹㈠満vs灞变笢娉板北','1710000000102','https://img-blog.csdnimg.cn/20240101000202.jpg','娴庡崡濂ヤ綋涓績',1,'<p>涓秴鑱旇禌绗?杞紝鎴戜勘涔愰儴瀹㈠満鎸戞垬灞变笢娉板北銆傛瘮璧涙椂闂达細3鏈?2鏃?9:35銆?/p><p>灞变笢娉板北鏄紶缁熷己闃燂紝涓诲満鎴樼哗鍑鸿壊锛屾垜闃熼渶鍋氬ソ鍏呭垎鍑嗗銆?/p>',1,'2026-03-10 09:00:00','2026-03-10 09:00:00'),
(3,'涓秴鑱旇禌绗?杞細涓诲満vs鍖椾含鍥藉畨','1710000000103','https://img-blog.csdnimg.cn/20240101000203.jpg','淇变箰閮ㄤ富鍦轰綋鑲插満',1,'<p>涓秴鑱旇禌绗?杞劍鐐规垬锛屾垜淇变箰閮ㄤ富鍦鸿繋鎴樺寳浜浗瀹夈€傛瘮璧涙椂闂达細3鏈?9鏃?9:35銆?/p><p>浜勃瀵瑰喅鍘嗘潵绮惧僵锛屾湰鍦哄叧绯诲埌璧涘鍒濈殑绉垎鎺掑悕璧板娍銆?/p>',1,'2026-03-15 09:00:00','2026-03-15 09:00:00'),
(4,'涓秴鑱旇禌绗?杞細瀹㈠満vs鎴愰兘钃夊煄','1710000000104','https://img-blog.csdnimg.cn/20240101000204.jpg','鎴愰兘鍑ゅ嚢灞变綋鑲插満',1,'<p>涓秴鑱旇禌绗?杞紝瀹㈠満鎸戞垬鎴愰兘钃夊煄銆傛瘮璧涙椂闂达細4鏈?鏃?9:35銆?/p><p>鎴愰兘钃夊煄涓诲満姘涘洿鐏垎锛屾垜闃熼渶瑕佷繚鎸佸喎闈欙紝鍙戞尌鎶€鎴樻湳姘村钩銆?/p>',1,'2026-03-15 09:00:00','2026-03-15 09:00:00'),
(5,'涓秴鑱旇禌绗?杞細涓诲満vs骞垮窞闃?,'1710000000105','https://img-blog.csdnimg.cn/20240101000205.jpg','淇变箰閮ㄤ富鍦轰綋鑲插満',1,'<p>涓秴鑱旇禌绗?杞紝涓诲満杩庢垬骞垮窞闃熴€傛瘮璧涙椂闂达細4鏈?2鏃?9:35銆?/p><p>骞垮窞闃熸湰璧涘闃靛骞磋交鍖栵紝鎶€鎴樻湳鐗圭偣椴滄槑锛岄渶璁ょ湡瀵瑰緟銆?/p>',1,'2026-03-18 09:00:00','2026-03-18 09:00:00'),
(6,'瓒冲崗鏉3杞細涓诲満vs姝︽眽涓夐晣','1710000000106','https://img-blog.csdnimg.cn/20240101000206.jpg','淇变箰閮ㄤ富鍦轰綋鑲插満',2,'<p>瓒冲崗鏉3杞紝涓诲満杩庢垬姝︽眽涓夐晣銆傛瘮璧涙椂闂达細4鏈?6鏃?9:00銆?/p><p>瓒冲崗鏉槸浜夊啝鐨勯噸瑕佹垬绾匡紝鎴戦槦灏嗗叏鍔涘嚭鎴樸€?/p>',1,'2026-03-18 09:00:00','2026-03-18 09:00:00'),
(7,'浜氬啝灏忕粍璧涚1杞細涓诲満vs闊╁浗鍏ㄥ寳鐜颁唬','1710000000107','https://img-blog.csdnimg.cn/20240101000207.jpg','淇变箰閮ㄤ富鍦轰綋鑲插満',3,'<p>2026浜氬啝鑱旇禌灏忕粍璧涢杞紝涓诲満杩庢垬闊╁浗K鑱旇禌鍐犲啗鍏ㄥ寳鐜颁唬銆傛瘮璧涙椂闂达細4鏈?0鏃?0:00銆?/p><p>杩欐槸淇变箰閮ㄦ椂闅斾袱骞撮噸杩斾簹鍐犺禌鍦猴紝鍏ㄩ槦涓婁笅楂樺害閲嶈銆?/p>',1,'2026-03-20 09:00:00','2026-03-20 09:00:00'),
(8,'浜氬啝灏忕粍璧涚2杞細瀹㈠満vs鏃ユ湰妯花姘存墜','1710000000108','https://img-blog.csdnimg.cn/20240101000208.jpg','妯花鍥介檯缁煎悎绔炴妧鍦?,3,'<p>浜氬啝灏忕粍璧涚2杞紝瀹㈠満鎸戞垬鏃ユ湰J鑱旇禌鍐犲啗妯花姘存墜銆傛瘮璧涙椂闂达細4鏈?7鏃?8:00銆?/p><p>妯花姘存墜鎶€鏈粏鑵伙紝浼犳帶鑳藉姏寮猴紝鎴戦槦闇€瑕佸彂鎸ヨ韩浣撲紭鍔裤€?/p>',1,'2026-03-20 09:00:00','2026-03-20 09:00:00'),
(9,'鐑韩璧涳細涓诲満vs娴欐睙闃?,'1710000000109','https://img-blog.csdnimg.cn/20240101000209.jpg','淇变箰閮ㄤ富鍦轰綋鑲插満',4,'<p>璧涘鍓嶇儹韬禌锛屼富鍦哄闃垫禉姹熼槦銆傛瘮璧涙椂闂达細3鏈?鏃?5:00銆?/p><p>閫氳繃鐑韩璧涙楠屽啲璁垚鏋滐紝璋冩暣姣旇禌鐘舵€併€?/p>',1,'2026-03-01 09:00:00','2026-03-01 09:00:00'),
(10,'鐑韩璧涳細涓珛鍦簐s娌冲崡闃?,'1710000000110','https://img-blog.csdnimg.cn/20240101000210.jpg','娴峰彛瑙傛緶婀栬冻鐞冨熀鍦?,4,'<p>璧涘鍓嶇儹韬禌绗?鍦猴紝涓珛鍦哄闃垫渤鍗楅槦銆傛瘮璧涙椂闂达細3鏈?1鏃?5:00銆?/p><p>缁х画纾ㄥ悎闃靛锛岀‘瀹氭柊璧涘涓诲姏妗嗘灦銆?/p>',1,'2026-03-01 09:00:00','2026-03-01 09:00:00');

/*Table structure for table `shuju` */

DROP TABLE IF EXISTS `shuju`;

CREATE TABLE `shuju` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '涓婚敭',
  `yonghu_id` int(11) DEFAULT NULL COMMENT '鐢ㄦ埛',
  `shuju_name` varchar(200) DEFAULT NULL COMMENT '鐞冨憳鏁版嵁鍚嶇О',
  `shuju_uuid_number` varchar(200) DEFAULT NULL COMMENT '鐞冨憳鏁版嵁缂栧彿',
  `shuju_photo` varchar(200) DEFAULT NULL COMMENT '鐞冨憳鏁版嵁鐓х墖',
  `shuju_types` int(11) DEFAULT NULL COMMENT '鐞冨憳鏁版嵁绫诲瀷',
  `shuju_time` date DEFAULT NULL COMMENT '鏃ユ湡',
  `shuju_content` longtext COMMENT '鐞冨憳鏁版嵁浠嬬粛',
  `shuju_delete` int(11) DEFAULT NULL COMMENT '閫昏緫鍒犻櫎',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '褰曞叆鏃堕棿',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='鐞冨憳鏁版嵁';

/*Data for the table `shuju` */

insert  into `shuju`(`id`,`yonghu_id`,`shuju_name`,`shuju_uuid_number`,`shuju_photo`,`shuju_types`,`shuju_time`,`shuju_content`,`shuju_delete`,`insert_time`,`create_time`) values 
(1,1,'闃熼暱寮犱紵 - 2025璧涘鏁版嵁','1710000000201','https://img-blog.csdnimg.cn/20240101000301.jpg',2,'2025-12-31','<p><strong>寮犱紵锛堥槦闀?涓満锛?/strong></p><p>鍑哄満锛?2娆★紙棣栧彂30娆★級</p><p>杩涚悆锛?鐞?/p><p>鍔╂敾锛?2娆?/p><p>浼犵悆鎴愬姛鐜囷細87%</p><p>鍏抽敭浼犵悆锛?6娆?/p><p>鎶㈡柇锛?8娆?/p><p>鑽ｈ幏2025璧涘涓秴鏈€浣充腑鍦虹悆鍛?/p>',1,'2026-01-15 10:00:00','2026-01-15 10:00:00'),
(2,1,'椹鏂?- 2025璧涘鏁版嵁锛堝反瑗匡級','1710000000202','https://img-blog.csdnimg.cn/20240101000302.jpg',1,'2025-12-31','<p><strong>椹鏂紙鍓嶉攱/鏂版彺锛?/strong></p><p>鍑哄満锛?0娆★紙棣栧彂28娆★級</p><p>杩涚悆锛?8鐞?/p><p>鍔╂敾锛?娆?/p><p>灏勯棬锛?8娆?/p><p>灏勬鐜囷細48%</p><p>杩囦汉鎴愬姛鐜囷細62%</p><p>宸寸敳鑱旇禌閾堕澊濂栧緱涓?/p>',1,'2026-03-08 10:00:00','2026-03-08 10:00:00'),
(3,2,'鏉庡己 - 2025璧涘鏁版嵁','1710000000203','https://img-blog.csdnimg.cn/20240101000303.jpg',2,'2025-12-31','<p><strong>鏉庡己锛堥棬灏嗭級</strong></p><p>鍑哄満锛?0娆★紙棣栧彂30娆★級</p><p>鎵戞晳锛?12娆?/p><p>鎵戞晳鎴愬姛鐜囷細78%</p><p>闆跺皝锛?2鍦?/p><p>鍑哄嚮鎴愬姛锛?3娆?/p><p>浼犵悆鎴愬姛鐜囷細82%</p>',1,'2026-01-15 10:00:00','2026-01-15 10:00:00'),
(4,1,'鐜嬬 - 2025璧涘鏁版嵁','1710000000204','https://img-blog.csdnimg.cn/20240101000304.jpg',1,'2025-12-31','<p><strong>鐜嬬锛堝墠閿嬶級</strong></p><p>鍑哄満锛?8娆★紙棣栧彂22娆★級</p><p>杩涚悆锛?2鐞?/p><p>鍔╂敾锛?娆?/p><p>灏勯棬锛?6娆?/p><p>灏勬鐜囷細42%</p><p>澶寸悆杩涚悆锛?涓?/p>',1,'2026-01-15 10:00:00','2026-01-15 10:00:00'),
(5,3,'鍒樻磱 - 2025璧涘鏁版嵁','1710000000205','https://img-blog.csdnimg.cn/20240101000305.jpg',2,'2025-12-31','<p><strong>鍒樻磱锛堣竟鍚庡崼锛?/strong></p><p>鍑哄満锛?1娆★紙棣栧彂30娆★級</p><p>杩涚悆锛?鐞?/p><p>鍔╂敾锛?娆?/p><p>鎶㈡柇锛?02娆?/p><p>鎷︽埅锛?6娆?/p><p>浼犱腑鎴愬姛鐜囷細35%</p>',1,'2026-01-15 10:00:00','2026-01-15 10:00:00'),
(6,1,'闄堟旦 - 2025璧涘鏁版嵁','1710000000206','https://img-blog.csdnimg.cn/20240101000306.jpg',2,'2025-12-31','<p><strong>闄堟旦锛堜腑鍦猴級</strong></p><p>鍑哄満锛?9娆★紙棣栧彂26娆★級</p><p>杩涚悆锛?鐞?/p><p>鍔╂敾锛?娆?/p><p>浼犵悆鎴愬姛鐜囷細85%</p><p>闀夸紶鎴愬姛鐜囷細72%</p><p>杩滃皠杩涚悆锛?涓?/p>',1,'2026-01-15 10:00:00','2026-01-15 10:00:00');

/*Table structure for table `token` */

DROP TABLE IF EXISTS `token`;

CREATE TABLE `token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '涓婚敭',
  `userid` bigint(20) NOT NULL COMMENT '鐢ㄦ埛ID',
  `username` varchar(100) NOT NULL COMMENT '鐢ㄦ埛鍚?,
  `tablename` varchar(100) DEFAULT NULL COMMENT '琛ㄥ悕',
  `role` varchar(100) DEFAULT NULL COMMENT '瑙掕壊',
  `token` varchar(200) NOT NULL COMMENT '浠ょ墝',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '鏂板鏃堕棿',
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '杩囨湡鏃堕棿',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='浠ょ墝琛?;

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '涓婚敭',
  `username` varchar(100) NOT NULL COMMENT '鐢ㄦ埛鍚?,
  `password` varchar(100) NOT NULL COMMENT '瀵嗙爜',
  `role` varchar(100) DEFAULT '绠＄悊鍛? COMMENT '瑙掕壊',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '鏂板鏃堕棿',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='绠＄悊鍛?;

/*Data for the table `users` */

insert  into `users`(`id`,`username`,`password`,`role`,`addtime`) values 
(1,'admin','admin','绠＄悊鍛?,'2026-03-21 09:00:00'),
(2,'manager','manager','绠＄悊鍛?,'2026-03-21 09:00:00');

/*Table structure for table `xunlian` */

DROP TABLE IF EXISTS `xunlian`;

CREATE TABLE `xunlian` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '涓婚敭',
  `yonghu_id` int(11) DEFAULT NULL COMMENT '鐢ㄦ埛',
  `xunlian_name` varchar(200) DEFAULT NULL COMMENT '璁粌璁″垝鍚嶇О',
  `xunlian_uuid_number` varchar(200) DEFAULT NULL COMMENT '璁粌璁″垝缂栧彿',
  `xunlian_photo` varchar(200) DEFAULT NULL COMMENT '璁粌璁″垝鐓х墖',
  `xunlian_types` int(11) DEFAULT NULL COMMENT '璁粌璁″垝绫诲瀷',
  `xunlian_kemu` varchar(200) DEFAULT NULL COMMENT '璁粌绉戠洰',
  `xunlian_time` date DEFAULT NULL COMMENT '鏃ユ湡',
  `xunlian_content` longtext COMMENT '璁粌璁″垝浠嬬粛',
  `xunlian_delete` int(11) DEFAULT NULL COMMENT '閫昏緫鍒犻櫎',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '褰曞叆鏃堕棿',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='璁粌璁″垝';

/*Data for the table `xunlian` */

insert  into `xunlian`(`id`,`yonghu_id`,`xunlian_name`,`xunlian_uuid_number`,`xunlian_photo`,`xunlian_types`,`xunlian_kemu`,`xunlian_time`,`xunlian_content`,`xunlian_delete`,`insert_time`,`create_time`) values 
(1,NULL,'鍛ㄤ竴锛氫綋鑳藉己鍖栬缁?,'1710000000301','https://img-blog.csdnimg.cn/20240101000401.jpg',1,'浣撹兘璁粌','2026-03-17','<p><strong>璁粌鏃堕棿锛?/strong>09:00-11:30</p><p><strong>璁粌鍐呭锛?/strong></p><ul><li>鐑韩璺戯細15鍒嗛挓</li><li>鏍稿績鍔涢噺璁粌锛?5鍒嗛挓</li><li>鏈夋哀鑰愬姏璁粌锛?0鍒嗛挓</li><li>鎷変几鏀炬澗锛?0鍒嗛挓</li></ul><p><strong>鐩爣锛?/strong>鎻愬崌鐞冨憳鍩虹浣撹兘鍌ㄥ锛屼负璧涘鍋氬ソ鍑嗗銆?/p>',1,'2026-03-15 09:00:00','2026-03-15 09:00:00'),
(2,NULL,'鍛ㄤ簩锛氭垬鏈厤鍚堣缁?,'1710000000302','https://img-blog.csdnimg.cn/20240101000402.jpg',2,'鎴樻湳璁粌','2026-03-18','<p><strong>璁粌鏃堕棿锛?/strong>09:00-11:30</p><p><strong>璁粌鍐呭锛?/strong></p><ul><li>瑙嗛鍒嗘瀽锛堝鎵嬪垎鏋愶級锛?0鍒嗛挓</li><li>瀹氫綅鐞冩紨缁冿細45鍒嗛挓</li><li>杩涙敾濂楄矾閰嶅悎锛?5鍒嗛挓</li><li>闃插畧绔欎綅缁冧範锛?0鍒嗛挓</li></ul><p><strong>鐩爣锛?/strong>寮哄寲鐞冮槦鏁翠綋鎴樻湳鎵ц鍔涖€?/p>',1,'2026-03-15 09:00:00','2026-03-15 09:00:00'),
(3,NULL,'鍛ㄤ笁锛氭妧鏈笓椤硅缁?,'1710000000303','https://img-blog.csdnimg.cn/20240101000403.jpg',3,'鎶€鏈缁?,'2026-03-19','<p><strong>璁粌鏃堕棿锛?/strong>09:00-11:30</p><p><strong>璁粌鍐呭锛?/strong></p><ul><li>浼犵悆绮惧害缁冧範锛?0鍒嗛挓</li><li>灏勯棬璁粌锛?5鍒嗛挓</li><li>澶寸悆浜夐《璁粌锛?0鍒嗛挓</li><li>灏忓満鍦板鎶楄禌锛?5鍒嗛挓</li></ul><p><strong>鐩爣锛?/strong>鎻愬崌鐞冨憳涓汉鎶€鏈兘鍔涖€?/p>',1,'2026-03-15 09:00:00','2026-03-15 09:00:00'),
(4,NULL,'鍛ㄥ洓锛氭仮澶嶆€ц缁?,'1710000000304','https://img-blog.csdnimg.cn/20240101000404.jpg',4,'鎭㈠璁粌','2026-03-20','<p><strong>璁粌鏃堕棿锛?/strong>09:00-10:30</p><p><strong>璁粌鍐呭锛?/strong></p><ul><li>杞绘澗鎱㈣窇锛?0鍒嗛挓</li><li>娓告吵鏀炬澗锛?0鍒嗛挓</li><li>鎸夋懇鐞嗙枟锛?0鍒嗛挓</li></ul><p><strong>鐩爣锛?/strong>甯姪鐞冨憳鎭㈠浣撹兘锛岄闃蹭激鐥呫€?/p>',1,'2026-03-15 09:00:00','2026-03-15 09:00:00'),
(5,NULL,'鍛ㄤ簲锛氳禌鍓嶉拡瀵规€ц缁?,'1710000000305','https://img-blog.csdnimg.cn/20240101000405.jpg',2,'鎴樻湳璁粌','2026-03-21','<p><strong>璁粌鏃堕棿锛?/strong>09:00-11:00</p><p><strong>璁粌鍐呭锛?/strong></p><ul><li>瀵规墜鎶€鎴樻湳鍒嗘瀽锛?0鍒嗛挓</li><li>閽堝鎬ф垬鏈紨缁冿細60鍒嗛挓</li><li>瀹氫綅鐞冩敾闃叉紨缁冿細30鍒嗛挓</li></ul><p><strong>鐩爣锛?/strong>涓哄懆鏈瘮璧涘仛濂芥垬鏈噯澶囥€?/p>',1,'2026-03-15 09:00:00','2026-03-15 09:00:00'),
(6,NULL,'鍛ㄥ叚锛氳禌鍓嶉€傚簲璁粌','1710000000306','https://img-blog.csdnimg.cn/20240101000406.jpg',4,'鎭㈠璁粌','2026-03-22','<p><strong>璁粌鏃堕棿锛?/strong>10:00-11:00</p><p><strong>璁粌鍐呭锛?/strong></p><ul><li>鍦哄湴閫傚簲锛?0鍒嗛挓</li><li>浼犳帴鐞冪粌涔狅細20鍒嗛挓</li><li>灏勯棬鐑韩锛?0鍒嗛挓</li></ul><p><strong>鐩爣锛?/strong>淇濇寔姣旇禌鐘舵€侊紝璋冩暣蹇冩€併€?/p>',1,'2026-03-15 09:00:00','2026-03-15 09:00:00'),
(7,NULL,'瀹堥棬鍛樹笓椤硅缁?,'1710000000307','https://img-blog.csdnimg.cn/20240101000407.jpg',3,'鎶€鏈缁?,'2026-03-17','<p><strong>璁粌鏃堕棿锛?/strong>14:00-16:00</p><p><strong>鍙傝浜哄憳锛?/strong>瀹堥棬鍛樼粍</p><p><strong>璁粌鍐呭锛?/strong></p><ul><li>鎵戞晳鍙嶅簲璁粌锛?5鍒嗛挓</li><li>鍑哄嚮鏃舵満璁粌锛?0鍒嗛挓</li><li>闂ㄧ嚎鎶€鏈缁冿細30鍒嗛挓</li><li>澶ц剼寮€鐞冭缁冿細15鍒嗛挓</li></ul>',1,'2026-03-15 09:00:00','2026-03-15 09:00:00'),
(8,NULL,'闈掑勾闃熻仈鍚堣缁?,'1710000000308','https://img-blog.csdnimg.cn/20240101000408.jpg',1,'浣撹兘璁粌','2026-03-19','<p><strong>璁粌鏃堕棿锛?/strong>14:00-16:00</p><p><strong>鍙傝浜哄憳锛?/strong>涓€绾块槦鏇胯ˉ + U21闈掑勾闃?/p><p><strong>璁粌鍐呭锛?/strong></p><ul><li>鑱斿悎浣撹兘璁粌锛?5鍒嗛挓</li><li>鍒嗙粍瀵规姉璧涳細75鍒嗛挓</li></ul><p><strong>鐩爣锛?/strong>鑰冨療闈掑勾闃熺悆鍛橈紝淇濇寔鏇胯ˉ鐞冨憳鐘舵€併€?/p>',1,'2026-03-15 09:00:00','2026-03-15 09:00:00'),
(9,NULL,'瀹氫綅鐞冧笓椤硅缁?,'1710000000309','https://img-blog.csdnimg.cn/20240101000409.jpg',3,'鎶€鏈缁?,'2026-03-20','<p><strong>璁粌鏃堕棿锛?/strong>15:00-16:30</p><p><strong>璁粌鍐呭锛?/strong></p><ul><li>瑙掔悆杩涙敾濂楄矾锛?0鍒嗛挓</li><li>浠绘剰鐞冮厤鍚堬細30鍒嗛挓</li><li>鐐圭悆缁冧範锛?5鍒嗛挓</li><li>鐣屽鐞冩垬鏈細15鍒嗛挓</li></ul><p><strong>鐩爣锛?/strong>鎻愬崌瀹氫綅鐞冨緱鍒嗘晥鐜囥€?/p>',1,'2026-03-18 09:00:00','2026-03-18 09:00:00'),
(10,NULL,'浼ょ梾鐞冨憳搴峰璁粌','1710000000310','https://img-blog.csdnimg.cn/20240101000410.jpg',4,'鎭㈠璁粌','2026-03-21','<p><strong>璁粌鏃堕棿锛?/strong>09:00-10:30</p><p><strong>鍙傝浜哄憳锛?/strong>浼ょ梾鎭㈠鏈熺悆鍛?/p><p><strong>璁粌鍐呭锛?/strong></p><ul><li>姘寸枟鏀炬澗锛?0鍒嗛挓</li><li>鏍稿績绋冲畾璁粌锛?5鍒嗛挓</li><li>涓嬭偄鍔涢噺鎭㈠锛?5鍒嗛挓</li><li>鎷変几鐞嗙枟锛?0鍒嗛挓</li></ul>',1,'2026-03-18 09:00:00','2026-03-18 09:00:00');

/*Table structure for table `yonghu` */

DROP TABLE IF EXISTS `yonghu`;

CREATE TABLE `yonghu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '涓婚敭',
  `username` varchar(200) DEFAULT NULL COMMENT '璐︽埛',
  `password` varchar(200) DEFAULT NULL COMMENT '瀵嗙爜',
  `yonghu_uuid_number` varchar(200) DEFAULT NULL COMMENT '鐢ㄦ埛缂栧彿',
  `yonghu_name` varchar(200) DEFAULT NULL COMMENT '鐢ㄦ埛濮撳悕',
  `yonghu_phone` varchar(200) DEFAULT NULL COMMENT '鐢ㄦ埛鎵嬫満鍙?,
  `yonghu_id_number` varchar(200) DEFAULT NULL COMMENT '鐢ㄦ埛韬唤璇佸彿',
  `yonghu_photo` varchar(200) DEFAULT NULL COMMENT '鐢ㄦ埛澶村儚',
  `sex_types` int(11) DEFAULT NULL COMMENT '鎬у埆',
  `yonghu_email` varchar(200) DEFAULT NULL COMMENT '鐢ㄦ埛閭',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='鐢ㄦ埛';

/*Data for the table `yonghu` */

insert  into `yonghu`(`id`,`username`,`password`,`yonghu_uuid_number`,`yonghu_name`,`yonghu_phone`,`yonghu_id_number`,`yonghu_photo`,`sex_types`,`yonghu_email`,`create_time`) values 
(1,'zhangwei','123456','1710000000001','寮犱紵','13900139001','110101199501011234','https://img-blog.csdnimg.cn/20240101000501.jpg',1,'zhangwei@club.com','2026-03-21 09:00:00'),
(2,'liqiang','123456','1710000000002','鏉庡己','13900139002','110101199602022345','https://img-blog.csdnimg.cn/20240101000502.jpg',1,'liqiang@club.com','2026-03-21 09:00:00'),
(3,'wanglei','123456','1710000000003','鐜嬬','13900139003','110101199903033456','https://img-blog.csdnimg.cn/20240101000503.jpg',1,'wanglei@club.com','2026-03-21 09:00:00'),
(4,'liuyang','123456','1710000000004','鍒樻磱','13900139004','110101199704044567','https://img-blog.csdnimg.cn/20240101000504.jpg',1,'liuyang@club.com','2026-03-21 09:00:00'),
(5,'chenhao','123456','1710000000005','闄堟旦','13900139005','110101199805055678','https://img-blog.csdnimg.cn/20240101000505.jpg',1,'chenhao@club.com','2026-03-21 09:00:00'),
(6,'marcos','123456','1710000000006','椹鏂?,'13900139006','110101199906066789','https://img-blog.csdnimg.cn/20240101000506.jpg',1,'marcos@club.com','2026-03-21 09:00:00');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
