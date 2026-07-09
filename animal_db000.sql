-- MySQL dump 10.13  Distrib 8.0.37, for Win64 (x86_64)
--
-- Host: localhost    Database: animal_db
-- ------------------------------------------------------
-- Server version	8.0.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ai_knowledge`
--

DROP TABLE IF EXISTS `ai_knowledge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_knowledge` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '知识标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '知识内容',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '分类',
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标签（逗号分隔）',
  `status` tinyint DEFAULT '1' COMMENT '状态(0禁用1启用)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_category` (`category`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  FULLTEXT KEY `idx_content` (`title`,`content`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='AI知识库表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_knowledge`
--

LOCK TABLES `ai_knowledge` WRITE;
/*!40000 ALTER TABLE `ai_knowledge` DISABLE KEYS */;

INSERT INTO `ai_knowledge` VALUES (1, '麻糍/糖球', '麻糍（别名：糖球）是一只生活在食堂门口的校园猫咪，目前已毕业。它自称命里有贵人，从小到大都有好心的姨姨帮助。糖球对未来的猫生充满期待，表示会像糖球一样甜，是校园一级保护动物。', '校园猫咪', '麻糍,糖球,已毕业,食堂', 1, '2026-07-08 16:55:26', '2026-07-08 16:55:26');
INSERT INTO `ai_knowledge` VALUES (2, '小猪', '小猪是一只未毕业的校园一级保护动物，经常流连于下天桥至大超路上，喜欢在路边等待下课的姐姐们并讨要好吃的。它性格较为傲娇，喜欢被拍屁股，有时喜欢贴贴，有时表现得淡淡的，并提醒大家下次不准说它胖。', '校园猫咪', '小猪,未毕业,傲娇,路边', 1, '2026-07-08 16:24:21', '2026-07-08 16:24:21');
INSERT INTO `ai_knowledge` VALUES (3, '小三花', '小三花是一只已毕业的校园一级保护动物，生前常待在1舍楼下。它性格温和，特别喜欢宿管阿姨，深受师生喜爱。', '校园猫咪', '小三花,已毕业,1舍,宿管阿姨', 1, '2026-07-08 16:20:20', '2026-07-08 16:20:20');
INSERT INTO `ai_knowledge` VALUES (4, '麻团', '麻团是一只未毕业的校园一级保护动物。去年秋天它的六个孩子都被好心人领养，目前它独自留在校园里流浪。麻团发出了渴望家庭的呼唤，希望能像孩子们一样拥有一个温暖的家。', '校园猫咪', '麻团,未毕业,流浪,求领养', 1, '2026-07-08 16:20:20', '2026-07-08 16:20:20');
INSERT INTO `ai_knowledge` VALUES (5, '奥利奥', '奥利奥是一只已毕业的校园一级保护动物，自称为绝世无双美男子，堪称奶牛猫中集美貌与猫德一体的模范。它曾生活在校园中，并对绝育后被叫作“莉莉”的传言表示强烈抗议。', '校园猫咪', '奥利奥,已毕业,奶牛猫,美男子', 1, '2026-07-08 16:20:20', '2026-07-08 16:20:20');
INSERT INTO `ai_knowledge` VALUES (6, '八嘎', '八嘎是一只已毕业的校园一级保护动物。前半生在校园风光无限，渴饮饮水机，困睡大沙发，出行靠电梯，坐拥一整座图书馆。现已隐居在两脚兽家中，过上了抢狗饭、手动开门、偷小鱼干的幸福居家生活。', '校园猫咪', '八嘎,已毕业,图书馆,被领养', 1, '2026-07-08 16:20:20', '2026-07-08 16:20:20');
INSERT INTO `ai_knowledge` VALUES (7, '大痣', '大痣是一只未毕业的校园一级保护动物。它性格十分怕人，虽然会朝着人叫，但不允许人类靠近。近期它开始尝试接受人类投喂的食物，正在慢慢建立对人类的信任。', '校园猫咪', '大痣,未毕业,怕人,警惕', 1, '2026-07-08 16:20:20', '2026-07-08 16:20:20');
INSERT INTO `ai_knowledge` VALUES (8, 'dsm', 'dsm（大傻猫）是一只未毕业的校园一级保护动物，常驻实验室，是z老师的白月光。它生活在实验室区域，深受特定师生的喜爱与照顾。', '校园猫咪', 'dsm,大傻猫,未毕业,实验室', 1, '2026-07-08 16:24:26', '2026-07-08 16:24:26');
INSERT INTO `ai_knowledge` VALUES (9, '窦娥/豆娥', '窦娥（现名豆娥）是一只已毕业的校园一级保护动物。它喜欢躺在1舍门前休憩，性格带有“小夹子”属性，擅长媚人欺负猫。它曾澄清一窝小猫并非自己的亲生崽，自称是被冤枉的。', '校园猫咪', '窦娥,豆娥,已毕业,1舍,夹子音', 1, '2026-07-08 16:24:10', '2026-07-08 16:24:10');
INSERT INTO `ai_knowledge` VALUES (10, '张豆包', '张豆包是一只已毕业的校园一级保护动物。它自称是“战五渣”，曾被几只“母老虎”驱逐出领地跑到湖边，但因祸得福被更多两脚兽注意到，最终成功被领养，再也不用打架了。', '校园猫咪', '张豆包,已毕业,被领养,湖边', 1, '2026-07-08 16:24:01', '2026-07-08 16:24:01');
INSERT INTO `ai_knowledge` VALUES (11, '少爷', '少爷是一只未毕业的校园一级保护动物，常出现于图书馆前。它性格多变，有时见人喜欢翻身亲昵，有时又很高冷地端坐于马路上，自带高贵气质。', '校园猫咪', '少爷,未毕业,图书馆,高冷', 1, '2026-07-08 17:51:20', '2026-07-08 17:51:20');
INSERT INTO `ai_knowledge` VALUES (12, '66', '66是一只银渐层，已毕业的校园一级保护动物。它从两个月大被猫妈妈抛弃，经历了两次退养和无数坎坷，最终在好心哥哥姐姐们的帮助下拥有了家，现在过得非常开心。', '校园猫咪', '66,银渐层,已毕业,坎坷,被领养', 1, '2026-07-08 17:51:20', '2026-07-08 17:51:20');
INSERT INTO `ai_knowledge` VALUES (13, '咖喱', '咖喱是一只已毕业的校园一级保护动物。它对自己的颜值非常自信，自称“被自己美到”，是校园里备受关注的猫咪之一。', '校园猫咪', '咖喱,已毕业,颜值,自信', 1, '2026-07-08 17:51:20', '2026-07-08 17:51:20');
/*!40000 ALTER TABLE `ai_knowledge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `animal`
--

DROP TABLE IF EXISTS `animal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `animal` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `cover_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '/uploads/morenzaimiwang.jpg' COMMENT '封面图',
  `images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '图片集(JSON格式)',
  `scientific_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学名',
  `selfintro` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '自我介绍',
  `habit` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '生活习性',
  `over` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '毕业情况',
  `protection_level` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '保护等级',
  `view_count` int DEFAULT '0' COMMENT '浏览量',
  `collect_count` int DEFAULT '0' COMMENT '收藏量',
  `status` tinyint DEFAULT '1' COMMENT '状态(0下架1上架)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_category_id` (`category_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_view_count` (`view_count`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='动物表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `animal`
--

LOCK TABLES `animal` WRITE;
/*!40000 ALTER TABLE `animal` DISABLE KEYS */;
INSERT INTO `animal` VALUES (1, '麻糍/糖球', 1, '/uploads/maci1.jpg', '[\"/uploads/maci2.jpg\"]', 'tangqiu', '我的命里有贵人，从小到大都有好心的姨姨帮我。请姨姨们放心，我接下来的猫生会像糖球一样甜！', '生活在食堂门口', '已毕业', '校园一级保护动物', 1283, 89, 1, '2025-11-22 00:08:16', '2026-07-08 16:55:26');
INSERT INTO `animal` VALUES (2, '小猪', 1, '/uploads/xiaozhu1.jpg', '[\"/uploads/xiaozhu2.jpg\",\"/uploads/xiaozhu3.jpg\",\"/uploads/xiaozhu4.jpg\"]', 'xiaozhu', '我最喜欢待在路边等待姐姐们下课啦，姐姐们对我可好啦，经常给我带好吃的，但是下次不准说我胖啦', 'm，喜欢被拍屁股较为傲娇，有时喜欢贴贴，有时表现得淡淡的，经常流连于下天桥至大超路上', '未毕业', '校园一级保护动物', 988, 65, 1, '2025-06-22 00:08:16', '2026-07-08 16:24:21');
INSERT INTO `animal` VALUES (3, '小三花', 1, '/uploads/hua1.jpg', '[\"/uploads/hua2.jpg\",\"/uploads/hua3.jpg\",\"/uploads/hua4.jpg\",\"/uploads/hua5.jpg\"]', 'xiaosanhua', '我喜欢宿管阿姨', '常待在1舍楼下', '已毕业', '校园一级保护动物', 757, 42, 1, '2025-11-22 00:08:16', '2026-07-08 16:20:20');
INSERT INTO `animal` VALUES (4, '麻团', 1, '/uploads/mt1.jpg', '[\"/uploads/mt2.jpg\",\"/uploads/mt3.jpg\"]', 'matuan', '我去年秋天我的六个孩子都被好心姨姨领养了，只有我在校园里流浪，我还可以有一个家吗？', '欢迎大家补充', '未毕业', '校园一级保护动物', 523, 31, 1, '2025-06-22 00:08:16', '2026-07-08 16:20:20');
INSERT INTO `animal` VALUES (5, '奥利奥', 2, '/uploads/ala1.jpg', '[\"/uploads/ala2.jpg\",\"/uploads/ala3.jpg\",\"/uploads/ala4.jpg\",\"/uploads/ala5.jpg\",\"/uploads/ala7.jpg\"]', 'aoliao', '我奥利奥是绝世无双美男子,堪称奶牛猫中集美貌与猫德一体的模范猫猫。到底是谁谠我绝育后叫莉莉的，快停止这个危险的想法啊喂!', '被自个帅醒', '已毕业', '校园一级保护动物', 2052, 168, 1, '2025-11-22 00:08:16', '2026-07-08 16:20:20');
INSERT INTO `animal` VALUES (6, '八嘎', 2, '/uploads/baga.jpg', NULL, 'baga', '我大八嘎前半生风光无限，渴饮饮水机，困睡大沙发,出行靠电梯,坐拥一整座图书馆!现在隐居两脚兽家,也是抢狗饭、手动开门、偷小鱼干!', '八嘎', '已毕业', '校园一级保护动物', 893, 56, 1, '2025-09-22 00:08:16', '2026-07-08 16:20:20');
INSERT INTO `animal` VALUES (7, '大痣', 2, '/uploads/dz1.jpg', '[\"/uploads/dz2.jpg\",\"/uploads/dz3.jpg\",\"/uploads/dz4.jpg\"]', 'dazhi', '...', '十分怕人，但会朝着人叫，但不允许人类靠近，近期开始接受人类食物', '未毕业', '校园一级保护动物', 634, 39, 1, '2025-11-22 00:08:16', '2026-07-08 16:20:20');
INSERT INTO `animal` VALUES (8, 'dsm', 2, '/uploads/dsm1.jpg', '[\"/uploads/dsm2.jpg\",\"/uploads/dsm3.jpg\",\"/uploads/dsm4.jpg\"]', 'dashamao', '欢迎大家补充~', 'z老师的白月光，实验室居民', '未毕业', '校园一级保护动物', 448, 27, 1, '2025-11-22 00:08:16', '2026-07-08 16:24:26');
INSERT INTO `animal` VALUES (9, '窦娥/豆娥', 3, '/uploads/doue1.jpg', '[\"/uploads/doue2.jpg\",\"/uploads/doue3.jpg\",\"/uploads/doue4.jpg\"]', 'doue', '大家好,我叫豆娥,其实原名是窦娥，但是我真的是被冤枉的!那窝小猫不是我的崽啊QAQ', '喜欢躺在1舍门前休憩，小夹子，媚人欺负猫', '已毕业', '校园一级保护动物', 589, 68, 1, '2026-07-08 16:24:01', '2026-07-08 16:24:10');
INSERT INTO `animal` VALUES (10, '张豆包', 3, '/uploads/zhangdoubao.jpg', NULL, 'doubao zhang', '我是个战五渣,被几只\"母老虎\"驱逐出了领地，跑到了湖边。但是因祸得福,我被更多两脚兽注意到，现在我有了家，再也不用打架啦', '欢迎大家补充~', '已毕业', '校园一级保护动物', 147, 15, 1, '2026-07-08 16:24:01', '2026-07-08 16:24:01');
INSERT INTO `animal` VALUES (11, '少爷', 3, '/uploads/shaoye1.jpg', '[\"/uploads/shaoye2.jpg\",\"/uploads/shaoye3.jpg\",\"/uploads/shaoye4.jpg\",\"/uploads/shaoye5.jpg\"]', 'shaoye', '听得出来我很高贵不', '常出现于图书馆前，有时见人喜欢翻身亲昵，有时又很高冷，端坐于马路上', '未毕业', '校园一级保护动物', 666, 66, 1, '2026-07-08 17:51:20', '2026-07-08 17:51:20');
INSERT INTO `animal` VALUES (12, '66', 4, '/uploads/661.jpg', '[\"/uploads/662.jpg\",\"/uploads/663.jpg\",\"/uploads/664.jpg\"]', 'six six', '我是一只银渐层~。从两个月大被猫妈妈抛弃到被两次退养，经历了无数坎坷的我终于有了家,感谢这一路上的好心哥哥姐姐们,我现在过得很开心哦', '数据组干peach呢', '已毕业', '校园一级保护动物', 568, 25, 1, '2026-07-08 17:51:20', '2026-07-08 17:51:20');
INSERT INTO `animal` VALUES (13, '咖喱', 4, '/uploads/gl1.jpg', '[\"/uploads/gl2.jpg\",\"/uploads/gl3.jpg\",\"/uploads/gl4.jpg\",\"/uploads/gl5.jpg\"]', 'gali', '欢迎大家补充~', '被自己美到', '已毕业','校园一级保护动物', 520, 52, 1, '2026-07-08 17:51:20', '2026-07-08 17:51:20');
/*!40000 ALTER TABLE `animal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `answer`
--

DROP TABLE IF EXISTS `answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answer` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `question_id` bigint NOT NULL COMMENT '问题ID',
  `user_id` bigint NOT NULL COMMENT '回答用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '回答内容',
  `is_accepted` tinyint DEFAULT '0' COMMENT '是否被采纳(0否1是)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_question_id` (`question_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='回答表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer`
--

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;
INSERT INTO `answer` VALUES (1, 1, 2, '流浪猫的饮食以无盐、无添加的肉类和猫粮为主最安全：优先选择成猫粮、无盐主食罐头，或是清水煮熟的鸡胸肉、去刺鱼肉、牛肉，这些食物高蛋白易消化，不会给猫咪肠胃和肾脏带来负担；也可以偶尔喂少量无盐白米饭、无糖酸奶或蛋黄应急，但不能长期作为主食。一定要避开洋葱、大蒜、巧克力、葡萄等有毒食物，也不要喂高盐的剩菜剩饭、火腿肠、生鱼生肉，避免猫咪中毒或生病。', 1, '2025-11-22 00:08:16');
INSERT INTO `answer` VALUES (2, 2, 1, '补会怕冷的，它们一般躲在车底、楼道避风处晒太阳取暖，熬过寒冷天气。。', 0, '2025-11-22 00:08:16');
INSERT INTO `answer` VALUES (3, 2, 1, '当然怕冷，天冷就缩成一团抱团取暖，有温暖小窝会舒服很多', 1, '2025-11-22 00:08:16');
INSERT INTO `answer` VALUES (4, 3, 2, '轻声慢走别追赶，远远投喂食物，慢慢熟悉之后再轻轻抚摸就好', 1, '2025-11-22 00:08:16');
INSERT INTO answer VALUES (5, 3, 1, '不要突然伸手吓唬猫咪，保持温和态度，时间久了它就不怕人了', 1, '2025-11-23 17:12:05');
INSERT INTO answer VALUES (6, 4, 3, '偏爱安静人少的地方，草丛、树荫底下都是它们常待的小地盘。', 0, '2025-11-23 17:12:39');
/*!40000 ALTER TABLE `answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `cover_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '/uploads/morenzaimiwang.jpg'  COMMENT '封面图',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章内容',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文章分类',
  `view_count` int DEFAULT '0' COMMENT '浏览量',
  `status` tinyint DEFAULT '1' COMMENT '状态(0草稿1发布)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='科普文章表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
INSERT INTO `article` VALUES (1, '大熊猫的生活习性详解', '/uploads/b66a1fae0b9b432890c6854b95e738c7.png', '大熊猫（学名：Ailuropoda melanoleuca）是中国特有物种，被誉为\"活化石\"和\"中国国宝\"。大熊猫已在地球上生存了至少800万年...\n\n【饮食习惯】\n大熊猫虽属于食肉目，但食谱却非常特化，99%的食物都是竹子。一只成年大熊猫每天要吃掉12-38公斤的竹子。\n\n【生活习性】\n大熊猫喜欢独居，除了发情期和育幼期外，常年单独生活。它们的活动区域一般在3-7平方公里。\n\n【繁殖特点】\n大熊猫的繁殖能力很低，雌性每年只有一次发情期，且发情期仅2-3天。孕期为83-200天，通常每胎产1-2仔。', '哺乳动物', 357, 1, '2025-11-22 00:08:16', '2025-11-22 00:08:16');
INSERT INTO `article` VALUES (2, '中国珍稀鸟类保护现状', '/uploads/b30ff277823f444c81b89f3a8398ed99.jpg', '中国是世界上鸟类资源最丰富的国家之一，拥有鸟类1400多种。其中许多是中国特有种，如朱鹮、丹顶鹤等...\n\n【朱鹮的复兴】\n朱鹮曾一度被认为野外灭绝，1981年在陕西洋县发现7只野生朱鹮后，经过40多年的保护，数量已恢复到5000多只。\n\n【丹顶鹤的保护】\n丹顶鹤是大型涉禽，主要栖息在沼泽和湿地。目前全球仅存2000-3000只，中国占据了重要的繁殖地和越冬地。', '鸟类', 245, 1, '2025-08-22 00:08:16', '2025-11-22 09:39:40');
INSERT INTO `article` VALUES (3, '探秘两栖动物的奇妙世界', '/uploads/57fadb3181a244a89c750912a1924af6.jpg', '两栖动物是最早登上陆地的脊椎动物，它们在进化史上具有重要地位...\n\n【两栖动物的特点】\n幼体生活在水中，用鳃呼吸；成体既能生活在水中也能生活在陆地，用肺和皮肤呼吸。\n\n【中国的两栖动物】\n中国已知两栖动物约400种，其中特有种占60%以上，如大鲵（娃娃鱼）、虎纹蛙等。', '两栖动物', 191, 1, '2025-11-22 00:08:16', '2025-11-22 00:08:16');
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `banner`
--

DROP TABLE IF EXISTS `banner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `banner` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片地址',
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '跳转链接',
  `sort` int DEFAULT '0' COMMENT '排序',
  `status` tinyint DEFAULT '1' COMMENT '状态(0禁用1启用)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_sort` (`sort`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='轮播图表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `banner`
--

LOCK TABLES `banner` WRITE;
/*!40000 ALTER TABLE `banner` DISABLE KEYS */;
INSERT INTO `banner` VALUES (1, '你想了解的小咪信息都在这里 ˋ3ˊ', '/uploads/banner1.png', '/animal/list', 1, 1, '2025-11-22 00:08:16');
INSERT INTO `banner` VALUES (2, '保护珍稀野生动物', '/uploads/ade6581d802a415cb7ecf57fc9cfe032.png', '/article/list', 2, 1, '2025-11-22 00:08:16');
INSERT INTO `banner` VALUES (3, 'pick你的心上咪 >0<', '/uploads/banner3.png', '/question/list', 3, 1, '2025-11-22 00:08:16');
/*!40000 ALTER TABLE `banner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `browse_history`
--

DROP TABLE IF EXISTS `browse_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `browse_history` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `animal_id` bigint NOT NULL COMMENT '动物ID',
  `browse_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_animal_id` (`animal_id`) USING BTREE,
  KEY `idx_browse_time` (`browse_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='浏览历史表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `browse_history`
--

LOCK TABLES `browse_history` WRITE;
/*!40000 ALTER TABLE `browse_history` DISABLE KEYS */;
INSERT INTO `browse_history` VALUES (1, 1, 1, '2025-11-21 23:08:16');
INSERT INTO `browse_history` VALUES (2, 1, 2, '2025-11-21 22:08:16');
INSERT INTO `browse_history` VALUES (3, 1, 3, '2025-11-21 21:08:16');
INSERT INTO `browse_history` VALUES (4, 2, 1, '2025-11-21 23:38:16');
INSERT INTO `browse_history` VALUES (5, 2, 4, '2025-11-21 00:08:16');
INSERT INTO `browse_history` VALUES (6, 2, 5, '2025-11-20 00:08:16');
INSERT INTO `browse_history` VALUES (25, 3, 1, '2025-11-23 19:17:39');
INSERT INTO `browse_history` VALUES (26, 3, 6, '2025-11-23 17:27:26');
/*!40000 ALTER TABLE `browse_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '分类描述',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '分类图标',
  `sort` int DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='动物分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1, '三花', '自带三色 buff，傲娇又温柔的小可爱', '/uploads/1.png', 1, '2025-11-22 00:08:16');
INSERT INTO `category` VALUES (2, '奶牛', '猫界 “二哈”，黑白反差萌，精力满满', '/uploads/2.png', 2, '2025-11-22 00:08:16');
INSERT INTO `category` VALUES (3, '橘猫', '温顺慵懒软萌，慵懒暖心治愈十足', '/uploads/3.png', 3, '2025-11-22 00:08:16');
INSERT INTO `category` VALUES (4, '个体喵', '我们的种类在学校比较少，高手总是独行的喵~', '/uploads/4.png', 4, '2025-11-22 00:08:16');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collect`
--

DROP TABLE IF EXISTS `collect`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collect` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `animal_id` bigint NOT NULL COMMENT '动物ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_animal` (`user_id`,`animal_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_animal_id` (`animal_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collect`
--

LOCK TABLES `collect` WRITE;
/*!40000 ALTER TABLE `collect` DISABLE KEYS */;
INSERT INTO `collect` VALUES (1, 1, 1, '2025-11-22 00:08:16');
INSERT INTO `collect` VALUES (2, 1, 2, '2025-11-22 00:08:16');
INSERT INTO `collect` VALUES (3, 1, 3, '2025-11-22 00:08:16');
INSERT INTO `collect` VALUES (4, 2, 1, '2025-11-22 00:08:16');
INSERT INTO `collect` VALUES (5, 2, 6, '2025-11-22 00:08:16');
INSERT INTO `collect` VALUES (6, 2, 7, '2025-11-22 00:08:16');
INSERT INTO `collect` VALUES (8, 3, 1, '2025-11-23 17:31:29');
/*!40000 ALTER TABLE `collect` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `animal_id` bigint NOT NULL COMMENT '动物ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `status` tinyint DEFAULT '1' COMMENT '状态(0待审核1通过2拒绝)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_animal_id` (`animal_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1, 1, 4, '宝宝你是一只小丑猫', 15, 1, '2025-11-22 00:08:16');
INSERT INTO `comment` VALUES (2, 2, 4, '恶语伤咪心!。', 9, 1, '2025-11-22 00:08:16');
INSERT INTO `comment` VALUES (3, 1, 5, '好！帅!好!俊！', 12, 1, '2025-11-22 00:08:16');
INSERT INTO `comment` VALUES (4, 2, 11, '你也俊！', 6, 1, '2025-11-22 00:08:16');
INSERT INTO `comment` VALUES (5, 1, 1, '宝宝！', 4, 1, '2025-11-23 17:13:40');
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '提问用户ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '问题标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '问题内容',
  `answer_count` int DEFAULT '0' COMMENT '回答数',
  `is_top` tinyint DEFAULT '0' COMMENT '是否置顶(0否1是)',
  `status` tinyint DEFAULT '1' COMMENT '状态(0待审核1通过)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_is_top` (`is_top`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='问答表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1, 1, '流浪猫能吃什么?', '流浪小猫可以吃什么啊，想喂但是不知道喂什么都不敢喂', 1, 0, 1, '2025-09-08 18:18:16');
INSERT INTO `question` VALUES (2, 2, '校园猫咪冬天会怕冷吗', '感觉东北比我家冷，那小猫是不是会更冷啊？', 2, 0, 1, '2025-11-22 00:08:16');
INSERT INTO `question` VALUES (3, 1, '怎么和猫咪友好相处？', '有点怕和它相处不好伤害它呀？', 2, 1, 1, '2025-07-06 20:08:16');
INSERT INTO `question` VALUES (4, 3, '流浪猫喜欢待在校园哪里 ', '感觉平时有一些小猫去活动范围都看不到呀？', 1, 0, 1, '2026-11-23 17:11:11');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_admin`
--

DROP TABLE IF EXISTS `sys_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_admin` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码（加密）',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'moren.jpg' COMMENT '头像',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
  `status` tinyint DEFAULT '1' COMMENT '状态(0禁用1正常)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='管理员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_admin`
--

LOCK TABLES `sys_admin` WRITE;
/*!40000 ALTER TABLE `sys_admin` DISABLE KEYS */;
INSERT INTO `sys_admin` VALUES (1, 'admin', '$2a$10$oH7AzG4jUC1ZD83hcAsp3.RdBFP6XuU3n/DEBkfmTaWPnDBctxhtu', '系统管理员', '/uploads/admin.jpg', 'admin@animal.com', '13800138000', 1, '2025-11-22 00:08:16', '2025-11-22 10:43:00');
/*!40000 ALTER TABLE `sys_admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码（加密）',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'moren.jpg' COMMENT '头像',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
  `status` tinyint DEFAULT '1' COMMENT '状态(0禁用1正常)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1, 'user01', '$2a$10$oH7AzG4jUC1ZD83hcAsp3.RdBFP6XuU3n/DEBkfmTaWPnDBctxhtu', '动物爱好者', '/uploads/moren.jpg', 'user01@example.com', NULL, 1, '2025-11-22 00:08:16', '2025-11-22 09:17:55');
INSERT INTO `user` VALUES (2, 'user02', '$2a$10$oH7AzG4jUC1ZD83hcAsp3.RdBFP6XuU3n/DEBkfmTaWPnDBctxhtu', '科普达人', '/uploads/moren.jpg', 'user02@example.com', NULL, 1, '2025-07-22 00:08:16', '2025-11-22 09:39:49');
INSERT INTO `user` VALUES (3, '205', '$2a$10$oH7AzG4jUC1ZD83hcAsp3.RdBFP6XuU3n/DEBkfmTaWPnDBctxhtu', '小松鼠', '/uploads/moren.jpg', '123456@qq.com', '19509521888', 1, '2025-11-22 09:17:33', '2025-11-22 11:02:48');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-08 18:18:36
