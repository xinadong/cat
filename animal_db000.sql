/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : animal_db

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 23/11/2025 19:20:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ai_knowledge
-- ----------------------------
DROP TABLE IF EXISTS `ai_knowledge`;
CREATE TABLE `ai_knowledge`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '知识标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '知识内容',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类',
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签（逗号分隔）',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态(0禁用1启用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category`(`category`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  FULLTEXT INDEX `idx_content`(`title`, `content`)
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI知识库表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_knowledge
-- ----------------------------
INSERT INTO `ai_knowledge` VALUES (1, '大熊猫的基本特征', '大熊猫（学名：Ailuropoda melanoleuca）是中国特有物种，被誉为\"活化石\"和\"中国国宝\"。体型肥硕似熊，头圆尾短，黑白相间的毛色是其显著特征。眼周、耳朵、四肢及肩胛部为黑色，其余为白色。成年大熊猫体长1.2-1.8米，体重80-120公斤。', '哺乳动物', '大熊猫,特征,国宝', 1, '2025-11-23 17:51:27', '2025-11-23 17:51:27');
INSERT INTO `ai_knowledge` VALUES (2, '大熊猫的饮食习惯', '大熊猫虽属于食肉目，但食谱却非常特化，99%的食物都是竹子。一只成年大熊猫每天要吃掉12-38公斤的竹子。由于竹子的营养价值较低，大熊猫需要长时间进食来满足能量需求。它们偶尔也会吃一些小动物、鸟蛋等。', '哺乳动物', '大熊猫,饮食,竹子', 1, '2025-11-23 17:51:27', '2025-11-23 17:51:27');
INSERT INTO `ai_knowledge` VALUES (3, '大熊猫的繁殖特点', '大熊猫的繁殖能力很低，雌性每年只有一次发情期，且发情期仅2-3天。孕期为83-200天，通常每胎产1-2仔。幼崽出生时非常脆弱，体重仅90-130克，需要母亲悉心照料。', '哺乳动物', '大熊猫,繁殖,幼崽', 1, '2025-11-23 17:51:27', '2025-11-23 17:51:27');
INSERT INTO `ai_knowledge` VALUES (4, '东北虎的基本信息', '东北虎（学名：Panthera tigris altaica）是现存体型最大的虎亚种。成年雄性体重可达250-300公斤，体长可达3米。毛色较淡，黄色或棕黄色，带有黑色条纹。东北虎是独居动物，领地意识强，主要在晨昏活动。', '哺乳动物', '东北虎,老虎,猫科', 1, '2025-11-23 17:51:27', '2025-11-23 17:51:27');
INSERT INTO `ai_knowledge` VALUES (5, '东北虎的狩猎习性', '东北虎是顶级捕食者，主要捕食鹿、野猪等大型哺乳动物。它们善于伏击，通常在黄昏或夜间活动。东北虎的领地范围很大，雄性可达800-1000平方公里，雌性为300-500平方公里。', '哺乳动物', '东北虎,狩猎,习性', 1, '2025-11-23 17:51:27', '2025-11-23 17:51:27');
INSERT INTO `ai_knowledge` VALUES (6, '丹顶鹤的外形特征', '丹顶鹤（学名：Grus japonensis）体羽洁白，头顶裸露皮肤鲜红色，这是其名字的由来。颈部和翅膀尾端为黑色，体态优雅。身高可达1.5米，翼展可达2.4米，是大型涉禽。', '鸟类', '丹顶鹤,鹤,特征', 1, '2025-11-23 17:51:27', '2025-11-23 17:51:27');
INSERT INTO `ai_knowledge` VALUES (7, '丹顶鹤的栖息环境', '丹顶鹤喜欢在湿地、沼泽地带生活，这些地方食物丰富且便于观察天敌。它们以鱼类、昆虫和植物为食，性情机警，鸣声高亢。丹顶鹤在中国文化中象征长寿和吉祥。', '鸟类', '丹顶鹤,栖息地,湿地', 1, '2025-11-23 17:51:27', '2025-11-23 17:51:27');
INSERT INTO `ai_knowledge` VALUES (8, '扬子鳄的生活习性', '扬子鳄（学名：Alligator sinensis）是中国特有的鳄鱼，栖息在淡水中。体型较小，成年鳄鱼体长2米左右。性情较温和，以鱼类、螺类、水鸟等为食。冬季会冬眠，在洞穴中度过寒冷季节。', '爬行动物', '扬子鳄,鳄鱼,习性', 1, '2025-11-23 17:51:27', '2025-11-23 17:51:27');
INSERT INTO `ai_knowledge` VALUES (9, '中华鲟的洄游特性', '中华鲟（学名：Acipenser sinensis）是溯河洄游性鱼类，在海洋中生长，繁殖期会游回长江产卵。体型较大，最大可达5米，体重可达600公斤。中华鲟被称为\"水中活化石\"，已存在1.4亿年。', '鱼类', '中华鲟,洄游,鱼类', 1, '2025-11-23 17:51:27', '2025-11-23 17:51:27');
INSERT INTO `ai_knowledge` VALUES (10, '金丝猴的社会结构', '金丝猴（学名：Rhinopithecus roxellana）是群居动物，通常以家族群为单位生活。一个群体可达数十只甚至上百只。它们主要栖息在高海拔森林中，以植物的叶、芽、果实等为食。金丝猴面部天蓝色，体毛金黄色，非常美丽。', '哺乳动物', '金丝猴,猴子,社会性', 1, '2025-11-23 17:51:27', '2025-11-23 17:51:27');
INSERT INTO `ai_knowledge` VALUES (11, '朱鹮的保护历史', '朱鹮（学名：Nipponia nippon）曾一度被认为野外灭绝。1981年在陕西洋县发现7只野生朱鹮后，经过40多年的保护，数量已恢复到5000多只。这是中国野生动物保护的成功典范。', '鸟类', '朱鹮,保护,濒危', 1, '2025-11-23 17:51:27', '2025-11-23 17:51:27');
INSERT INTO `ai_knowledge` VALUES (12, '穿山甲的防御机制', '穿山甲（学名：Manis pentadactyla）全身覆盖角质鳞片，这是它们的主要防御武器。遇到危险时会蜷缩成球状，鳞片竖起。穿山甲是独居动物，昼伏夜出，主要以白蚁和蚂蚁为食，用锋利的爪子挖开蚁穴。', '哺乳动物', '穿山甲,防御,习性', 1, '2025-11-23 17:51:27', '2025-11-23 17:51:27');
INSERT INTO `ai_knowledge` VALUES (13, '动物保护等级说明', '中国将珍稀濒危野生动物分为国家一级保护动物和国家二级保护动物。一级保护动物如大熊猫、东北虎等，数量极少且濒临灭绝。二级保护动物数量较少，需要加强保护。保护野生动物人人有责。', '保护知识', '保护等级,法律,濒危', 1, '2025-11-23 17:51:27', '2025-11-23 17:51:27');
INSERT INTO `ai_knowledge` VALUES (14, '如何区分东北虎和华南虎', '东北虎和华南虎的主要区别：1.体型：东北虎更大，体重可达300公斤；华南虎较小，约150-200公斤。2.毛色：东北虎毛色较淡，黄色偏白；华南虎颜色较深。3.条纹：东北虎条纹较稀疏；华南虎条纹较密集。4.分布：东北虎在东北和俄罗斯；华南虎原分布在中国南方。', '哺乳动物', '老虎,区别,对比', 1, '2025-11-23 17:51:27', '2025-11-23 17:51:27');
INSERT INTO `ai_knowledge` VALUES (15, '湿地对鸟类的重要性', '湿地是许多鸟类的重要栖息地，为它们提供食物、饮水和繁殖场所。丹顶鹤、朱鹮等珍稀鸟类都依赖湿地生存。保护湿地就是保护这些珍贵的鸟类。中国有多个重要湿地保护区，如鄱阳湖、洞庭湖等。', '保护知识', '湿地,鸟类,栖息地', 1, '2025-11-23 17:51:27', '2025-11-23 17:51:27');

-- ----------------------------
-- Table structure for animal
-- ----------------------------
DROP TABLE IF EXISTS `animal`;
CREATE TABLE `animal`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '动物名称',
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `cover_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'https://pic57.photophoto.cn/20201125/0012024436462829_b.jpg' COMMENT '封面图',
  `images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '图片集(JSON格式)',
  `scientific_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学名',
  `appearance` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '外形特征',
  `habit` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '生活习性',
  `distribution` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分布地区',
  `protection_level` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '保护等级',
  `view_count` int(11) NULL DEFAULT 0 COMMENT '浏览量',
  `collect_count` int(11) NULL DEFAULT 0 COMMENT '收藏量',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态(0下架1上架)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_view_count`(`view_count`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '动物表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of animal
-- ----------------------------
INSERT INTO `animal` VALUES (1, '1234', 1, '/uploads/d946aa0cf1dd464390a540a93091239a.png', '[\"/uploads/49f887fc7a894d1f89c87c0e0fbb5ecc.png\",\"/uploads/eb47946ae6be4634ac0595b3bc0ae207.png\",\"/uploads/4a007949add44c0cbb00b2cea87817f8.png\"]', 'tangqiu', '体型肥硕似熊，头圆尾短，黑白相间的毛色是其显著特征。眼周、耳朵、四肢及肩胛部为黑色，其余为白色。', '喜欢独居，每只大熊猫有单独的活动区域。主食竹子，偶尔也吃一些动物尸体或其他植物。性情温顺但警觉性很高。', '中国四川、陕西、甘肃等地的山区', '国家一级保护动物', 1276, 89, 1, '2025-11-22 00:08:16', '2025-11-22 00:08:16');
INSERT INTO `animal` VALUES (2, '东北虎', 1, '/uploads/2d38ed7ffc6446c6844edecbbb5a4738.jpg', '[\"/uploads/ca30467d6e594c888a6442b35eea38fe.jpg\"]', 'Panthera tigris altaica', '体型硕大，毛色较淡，黄色或棕黄色，带有黑色条纹。体重可达300公斤以上。', '独居动物，善于游泳，领地意识强。主要在晨昏活动，捕食鹿、野猪等大型哺乳动物。', '中国东北、俄罗斯远东地区', '国家一级保护动物', 982, 65, 1, '2025-06-22 00:08:16', '2025-11-22 09:39:29');
INSERT INTO `animal` VALUES (3, '丹顶鹤', 2, '/uploads/f4df5b50f8814541b9937c4b1e133301.jpg', '[\"/uploads/20661b11d77b4c0fa910c9ad351a023c.jpg\"]', 'Grus japonensis', '体羽洁白，头顶裸露皮肤鲜红色，颈部和翅膀尾端为黑色，体态优雅。身高可达1.5米。', '喜欢在湿地、沼泽地带生活，以鱼类、昆虫和植物为食。性情机警，鸣声高亢。', '中国东北、日本、朝鲜等地', '国家一级保护动物', 757, 42, 1, '2025-11-22 00:08:16', '2025-11-22 00:08:16');
INSERT INTO `animal` VALUES (4, '扬子鳄', 3, '/uploads/5c1099cb40d0433ca2889c7a5b50bf19.jpg', '[\"/uploads/a04985a9271d4b2999abbfafda8ed944.jpg\"]', 'Alligator sinensis', '体型较小，成年鳄鱼体长2米左右，背部有坚硬的鳞甲，呈灰黑色或黄褐色。', '栖息在淡水中，性情较温和。以鱼类、螺类、水鸟等为食。冬季会冬眠。', '中国长江中下游地区', '国家一级保护动物', 523, 31, 1, '2025-06-22 00:08:16', '2025-11-22 09:39:29');
INSERT INTO `animal` VALUES (5, '中华鲟', 5, '/uploads/d50b446545a64069814cb10356bfd59e.jpg', '[\"/uploads/ed8401f1207d454f9ed0c40ba3e5681a.jpg\"]', 'Acipenser sinensis', '体型较大，身体呈梭形，体表有五行骨板，口位于腹面。体色背部灰褐色，腹部白色。', '为溯河洄游性鱼类，生活在长江、东海等水域。以底栖动物为食。', '长江流域、东海', '国家一级保护动物', 412, 28, 1, '2025-11-22 00:08:16', '2025-11-22 00:08:16');
INSERT INTO `animal` VALUES (6, '金丝猴', 1, '/uploads/930427d5eb8b4247ba80b29a9ffc61fd.jpg', '[\"/uploads/3ffe8e61182440efaadccb530e9d4fba.jpg\"]', 'Rhinopithecus roxellana', '体毛金黄色，面部天蓝色，鼻孔上翘。四肢较长，尾巴细长。', '群居动物，主要栖息在高海拔森林中。以植物的叶、芽、果实等为食。', '中国四川、甘肃、陕西、湖北等地', '国家一级保护动物', 893, 56, 1, '2025-09-22 00:08:16', '2025-11-22 09:39:29');
INSERT INTO `animal` VALUES (7, '朱鹮', 2, '/uploads/6dd29f438f7243d3bb45489eb14cc782.jpg', '[\"/uploads/8ecec06894e44636af438dee23e53e2f.jpg\"]', 'Nipponia nippon', '体羽白色，繁殖期头部、颈部和背部呈粉红色。头顶有长羽冠，嘴细长向下弯曲。', '栖息在水田、沼泽等湿地环境。以鱼类、泥鳅、青蛙等为食。', '中国陕西、日本等地', '国家一级保护动物', 634, 39, 1, '2025-11-22 00:08:16', '2025-11-22 00:08:16');
INSERT INTO `animal` VALUES (8, '穿山甲', 1, '/uploads/93213e8b7fad4970812dfe9288fb296d.jpg', '[\"/uploads/150a33eb8d084a1e841a774016f18bfe.jpg\"]', 'Manis pentadactyla', '全身覆盖角质鳞片，无牙齿，舌头细长。四肢粗短，爪子锋利。', '独居动物，昼伏夜出。主要以白蚁和蚂蚁为食，用爪子挖开蚁穴。', '中国南方各省', '国家一级保护动物', 447, 27, 1, '2025-11-22 00:08:16', '2025-11-22 00:08:16');

-- ----------------------------
-- Table structure for answer
-- ----------------------------
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `question_id` bigint(20) NOT NULL COMMENT '问题ID',
  `user_id` bigint(20) NOT NULL COMMENT '回答用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '回答内容',
  `is_accepted` tinyint(4) NULL DEFAULT 0 COMMENT '是否被采纳(0否1是)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_question_id`(`question_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '回答表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of answer
-- ----------------------------
INSERT INTO `answer` VALUES (1, 1, 2, '大熊猫转变为以竹子为主食是长期进化的结果。大约在200-700万年前，大熊猫的祖先开始食用竹子。竹子在大熊猫栖息地广泛分布且稳定，这使得大熊猫能够避免与其他食肉动物的竞争。虽然大熊猫的消化系统仍保留着食肉动物的特征，但它们已经适应了竹子的低营养密度，通过大量进食来满足能量需求。', 1, '2025-11-22 00:08:16');
INSERT INTO `answer` VALUES (2, 1, 1, '补充一点：大熊猫虽然主要吃竹子，但它们偶尔也会吃一些小动物、鸟蛋等，这说明它们还保留着一些食肉动物的习性。', 0, '2025-11-22 00:08:16');
INSERT INTO `answer` VALUES (3, 2, 1, '东北虎和华南虎的主要区别：\n1. 体型：东北虎更大，体重可达300公斤；华南虎较小，约150-200公斤。\n2. 毛色：东北虎毛色较淡，黄色偏白；华南虎颜色较深，橙黄色更明显。\n3. 条纹：东北虎条纹较稀疏；华南虎条纹较密集。\n4. 分布：东北虎在东北和俄罗斯；华南虎原分布在中国南方，现野外可能已灭绝。', 1, '2025-11-22 00:08:16');
INSERT INTO `answer` VALUES (4, 3, 2, '朱鹮珍贵的原因：\n1. 数量稀少：20世纪80年代一度只剩7只野生个体。\n2. 分布狭窄：主要分布在中国和日本。\n3. 繁殖困难：对栖息环境要求高，繁殖成功率低。\n4. 文化价值：在东亚文化中被视为吉祥鸟，象征幸福美满。\n经过几十年的保护，现在数量已恢复到5000多只。', 1, '2025-11-22 00:08:16');
INSERT INTO `answer` VALUES (5, 4, 1, '9999999999', 1, '2025-11-23 17:12:05');
INSERT INTO `answer` VALUES (6, 4, 3, '123', 0, '2025-11-23 17:12:39');

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `cover_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'https://pic57.photophoto.cn/20201125/0012024436462829_b.jpg' COMMENT '封面图',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章内容',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文章分类',
  `view_count` int(11) NULL DEFAULT 0 COMMENT '浏览量',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态(0草稿1发布)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '科普文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES (1, '大熊猫的生活习性详解', '/uploads/b66a1fae0b9b432890c6854b95e738c7.png', '大熊猫（学名：Ailuropoda melanoleuca）是中国特有物种，被誉为\"活化石\"和\"中国国宝\"。大熊猫已在地球上生存了至少800万年...\n\n【饮食习惯】\n大熊猫虽属于食肉目，但食谱却非常特化，99%的食物都是竹子。一只成年大熊猫每天要吃掉12-38公斤的竹子。\n\n【生活习性】\n大熊猫喜欢独居，除了发情期和育幼期外，常年单独生活。它们的活动区域一般在3-7平方公里。\n\n【繁殖特点】\n大熊猫的繁殖能力很低，雌性每年只有一次发情期，且发情期仅2-3天。孕期为83-200天，通常每胎产1-2仔。', '哺乳动物', 357, 1, '2025-11-22 00:08:16', '2025-11-22 00:08:16');
INSERT INTO `article` VALUES (2, '中国珍稀鸟类保护现状', '/uploads/b30ff277823f444c81b89f3a8398ed99.jpg', '中国是世界上鸟类资源最丰富的国家之一，拥有鸟类1400多种。其中许多是中国特有种，如朱鹮、丹顶鹤等...\n\n【朱鹮的复兴】\n朱鹮曾一度被认为野外灭绝，1981年在陕西洋县发现7只野生朱鹮后，经过40多年的保护，数量已恢复到5000多只。\n\n【丹顶鹤的保护】\n丹顶鹤是大型涉禽，主要栖息在沼泽和湿地。目前全球仅存2000-3000只，中国占据了重要的繁殖地和越冬地。', '鸟类', 245, 1, '2025-08-22 00:08:16', '2025-11-22 09:39:40');
INSERT INTO `article` VALUES (3, '探秘两栖动物的奇妙世界', '/uploads/57fadb3181a244a89c750912a1924af6.jpg', '两栖动物是最早登上陆地的脊椎动物，它们在进化史上具有重要地位...\n\n【两栖动物的特点】\n幼体生活在水中，用鳃呼吸；成体既能生活在水中也能生活在陆地，用肺和皮肤呼吸。\n\n【中国的两栖动物】\n中国已知两栖动物约400种，其中特有种占60%以上，如大鲵（娃娃鱼）、虎纹蛙等。', '两栖动物', 191, 1, '2025-11-22 00:08:16', '2025-11-22 00:08:16');

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片地址',
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '跳转链接',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态(0禁用1启用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sort`(`sort`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '轮播图表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of banner
-- ----------------------------
INSERT INTO `banner` VALUES (1, '探索神奇的动物世界', '/uploads/a0173efc6aeb458fbb2d125045fef4e6.png', '/animal/list', 1, 1, '2025-11-22 00:08:16');
INSERT INTO `banner` VALUES (2, '保护珍稀野生动物', '/uploads/ade6581d802a415cb7ecf57fc9cfe032.png', '/article/list', 2, 1, '2025-11-22 00:08:16');
INSERT INTO `banner` VALUES (3, '一起学习动物知识', '/uploads/9ea1b4f34eaa4f26817400a6a1f15654.png', '/question/list', 3, 1, '2025-11-22 00:08:16');

-- ----------------------------
-- Table structure for browse_history
-- ----------------------------
DROP TABLE IF EXISTS `browse_history`;
CREATE TABLE `browse_history`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `animal_id` bigint(20) NOT NULL COMMENT '动物ID',
  `browse_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_animal_id`(`animal_id`) USING BTREE,
  INDEX `idx_browse_time`(`browse_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '浏览历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of browse_history
-- ----------------------------
INSERT INTO `browse_history` VALUES (1, 1, 1, '2025-11-21 23:08:16');
INSERT INTO `browse_history` VALUES (2, 1, 2, '2025-11-21 22:08:16');
INSERT INTO `browse_history` VALUES (3, 1, 3, '2025-11-21 21:08:16');
INSERT INTO `browse_history` VALUES (4, 2, 1, '2025-11-21 23:38:16');
INSERT INTO `browse_history` VALUES (5, 2, 4, '2025-11-21 00:08:16');
INSERT INTO `browse_history` VALUES (6, 2, 5, '2025-11-20 00:08:16');
INSERT INTO `browse_history` VALUES (25, 3, 1, '2025-11-23 19:17:39');
INSERT INTO `browse_history` VALUES (26, 3, 6, '2025-11-23 17:27:26');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类描述',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类图标',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '动物分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '哺乳动物', '哺乳纲动物，用乳腺哺育幼仔', '/uploads/8e69c6a348fa4d63b1f09c45f43ea124.png', 1, '2025-11-22 00:08:16');
INSERT INTO `category` VALUES (2, '鸟类', '鸟纲动物，有羽毛、喙和翅膀', '/uploads/83ee2f642d854e11a99169cf97601350.png', 2, '2025-11-22 00:08:16');
INSERT INTO `category` VALUES (3, '爬行动物', '爬行纲动物，体表被鳞片或甲', '/uploads/ef57d9fc430e4bf18feac93a043cfcae.png', 3, '2025-11-22 00:08:16');
INSERT INTO `category` VALUES (4, '两栖动物', '两栖纲动物，幼体水生、成体陆生', '/uploads/7c32292b48ed4bf78b223e20b52ec011.png', 4, '2025-11-22 00:08:16');
INSERT INTO `category` VALUES (5, '鱼类', '鱼纲动物，水生脊椎动物', '/uploads/38d3a45f90a4466587b1c98c59806e3c.png', 5, '2025-11-22 00:08:16');
INSERT INTO `category` VALUES (6, '昆虫', '昆虫纲动物，身体分头、胸、腹三部分', '/uploads/afbd1524a2254f208e3f88e0554777e5.png', 6, '2025-11-22 00:08:16');

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `animal_id` bigint(20) NOT NULL COMMENT '动物ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_animal`(`user_id`, `animal_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_animal_id`(`animal_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collect
-- ----------------------------
INSERT INTO `collect` VALUES (1, 1, 1, '2025-11-22 00:08:16');
INSERT INTO `collect` VALUES (2, 1, 2, '2025-11-22 00:08:16');
INSERT INTO `collect` VALUES (3, 1, 3, '2025-11-22 00:08:16');
INSERT INTO `collect` VALUES (4, 2, 1, '2025-11-22 00:08:16');
INSERT INTO `collect` VALUES (5, 2, 6, '2025-11-22 00:08:16');
INSERT INTO `collect` VALUES (6, 2, 7, '2025-11-22 00:08:16');
INSERT INTO `collect` VALUES (8, 3, 1, '2025-11-23 17:31:29');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `animal_id` bigint(20) NOT NULL COMMENT '动物ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `like_count` int(11) NULL DEFAULT 0 COMMENT '点赞数',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态(0待审核1通过2拒绝)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_animal_id`(`animal_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, 1, 1, '大熊猫真的太可爱了！国宝就是国宝！', 15, 1, '2025-11-22 00:08:16');
INSERT INTO `comment` VALUES (2, 2, 1, '希望大熊猫的数量能越来越多，保护好我们的国宝。', 9, 1, '2025-11-22 00:08:16');
INSERT INTO `comment` VALUES (3, 1, 2, '东北虎威武霸气，真正的百兽之王！', 12, 1, '2025-11-22 00:08:16');
INSERT INTO `comment` VALUES (4, 2, 3, '丹顶鹤姿态优雅，象征着长寿和吉祥。', 6, 1, '2025-11-22 00:08:16');
INSERT INTO `comment` VALUES (5, 1, 1, '我太喜欢大熊满了', 4, 1, '2025-11-23 17:13:40');

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '提问用户ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '问题标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '问题内容',
  `answer_count` int(11) NULL DEFAULT 0 COMMENT '回答数',
  `is_top` tinyint(4) NULL DEFAULT 0 COMMENT '是否置顶(0否1是)',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态(0待审核1通过)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_is_top`(`is_top`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '问答表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES (1, 1, '大熊猫为什么只吃竹子？', '大熊猫明明是食肉动物，为什么现在主要以竹子为食呢？这个进化过程是怎样的？', 2, 1, 1, '2025-11-22 00:08:16');
INSERT INTO `question` VALUES (2, 2, '如何区分东北虎和华南虎？', '东北虎和华南虎都是老虎的亚种，它们之间有什么明显的区别吗？', 1, 0, 1, '2025-11-22 00:08:16');
INSERT INTO `question` VALUES (3, 1, '朱鹮为什么那么珍贵？', '朱鹮被称为东方宝石，为什么数量这么稀少？', 1, 0, 1, '2025-11-22 00:08:16');
INSERT INTO `question` VALUES (4, 3, '为什么大象不吃肉', '大象为什么不吃肉，123456', 2, 0, 1, '2025-11-23 17:11:11');

-- ----------------------------
-- Table structure for sys_admin
-- ----------------------------
DROP TABLE IF EXISTS `sys_admin`;
CREATE TABLE `sys_admin`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码（加密）',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'https://pic57.photophoto.cn/20201125/0012024436462829_b.jpg' COMMENT '头像',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态(0禁用1正常)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_admin
-- ----------------------------
INSERT INTO `sys_admin` VALUES (1, 'admin', '$2a$10$a/mbGWmpfGmKQdhsjKYB9eARtBI6hWiBpPwI932OJNYauQlWQEcAa', '系统管理员', '/uploads/215b0e4b6e41471db5d173525ccbbb9c.jpg', 'admin@animal.com', '13800138000', 1, '2025-11-22 00:08:16', '2025-11-22 10:43:00');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码（加密）',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'https://pic57.photophoto.cn/20201125/0012024436462829_b.jpg' COMMENT '头像',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态(0禁用1正常)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '$2a$10$oH7AzG4jUC1ZD83hcAsp3.RdBFP6XuU3n/DEBkfmTaWPnDBctxhtu', '动物爱好者', 'https://pic57.photophoto.cn/20201125/0012024436462829_b.jpg', 'user01@example.com', NULL, 1, '2025-11-22 00:08:16', '2025-11-22 09:17:55');
INSERT INTO `user` VALUES (2, 'user02', '$2a$10$oH7AzG4jUC1ZD83hcAsp3.RdBFP6XuU3n/DEBkfmTaWPnDBctxhtu', '科普达人', 'https://pic57.photophoto.cn/20201125/0012024436462829_b.jpg', 'user02@example.com', NULL, 1, '2025-07-22 00:08:16', '2025-11-22 09:39:49');
INSERT INTO `user` VALUES (3, 'zhx', '$2a$10$oH7AzG4jUC1ZD83hcAsp3.RdBFP6XuU3n/DEBkfmTaWPnDBctxhtu', '小松鼠', '/uploads/76217cff824149af862331c15a6165ea.jpg', '123456@qq.com', '19509521888', 1, '2025-11-22 09:17:33', '2025-11-22 11:02:48');

SET FOREIGN_KEY_CHECKS = 1;
