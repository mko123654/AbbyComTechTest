CREATE DATABASE  IF NOT EXISTS `exam` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `exam`;
-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: exam
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `question_option`
--

DROP TABLE IF EXISTS `question_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question_option` (
  `question_option_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_bin NOT NULL COMMENT '題目選項代碼',
  `question_option_content` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8_bin NOT NULL COMMENT '題目選項內容',
  `question_option_description` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8_bin DEFAULT NULL COMMENT '題目選項描述',
  PRIMARY KEY (`question_option_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='題目選項資料表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_option`
--

LOCK TABLES `question_option` WRITE;
/*!40000 ALTER TABLE `question_option` DISABLE KEYS */;
INSERT INTO `question_option` VALUES ('0d8658c963e0428d81fe874c2e1d3f92','length、size()、length()',NULL),('1330a4f992d548f8894aad0029c595b4','蟒蛇',NULL),('1524eab3fb914c1498926bb49e1d8978','P4',NULL),('191d74a830214d369677ef7ccd43014a','屬於動態語言。',NULL),('2287b55aa0e84e0490773282b8939f34','正確',NULL),('303f8b05673d4b82ad3b25abaef029c5','錯誤',NULL),('33e0e87e115f447fa82e688807a86bfe','length()、length、length()',NULL),('3f77c684275649dfa345bef58d8071be','地鼠',NULL),('470ef63fbe61408aac6839e4e8fa73ec','Rob Pike',NULL),('4b31b7d30fc04238b4f64633bf396799','正確',NULL),('52d7f60170da4ebbbab2a31d230f15b8','Guido van Rossum',NULL),('536de341f62c4ffc94c919ccccdfa057','P3',NULL),('653db846341241dfb98649073d2d5189','正確',NULL),('6624b142676d472b8a16a0219f1a2a82','貓',NULL),('67aba4c85d324b188bd3fb994e2487c6','P1',NULL),('68d20f2f90174334a8fc486a4bcd0b0a','P2',NULL),('7983a63e5e3948f2bedb0168cc649977','byte',NULL),('7fc0c96caee44d9d9dd6327855523369','完善的錯誤處理機制，如JAVA的try...catch...',NULL),('889c8c2e5910419cb56bf68be299fae2','公貓',NULL),('906e0a4321634ad2b90feff444bbb0c8','正確',NULL),('9085147e65b4424b97382fde42481473','Bill Gates',NULL),('9906a5bf84ea452c81418afdc4c013ff','size()、length、length()',NULL),('99938cc925294954a6b189077b9622d3','效能跟java和C++相近 比python快30倍。',NULL),('9c92b5ae08034ed6924108ada29c36b0','Elon Marks',NULL),('b1b4d687974f487eac0d985c68142c51','go可以直接指定編譯成各種平台版本，像是window或debian，不用煩惱平台問題。',NULL),('b20b6378756946919dce4ac0ac302811','錯誤',NULL),('ba92665f4a1a42b3a006d16c7beded60','錯誤',NULL),('c31cc117b8784697974a345b3ca21215','錯誤',NULL),('c40ae15d83214c5cb71672e013530cf8','正確',NULL),('d0584eb77e554bf3afbbd034c2643fb9','有一顆痣',NULL),('d1be24597d4f4770a6a6db023557b30e','正確',NULL),('d6d23d190c0e4d60899b5ce5e268a689','double',NULL),('d9c80a0b0bc44af1958e16f46f6cfd50','short',NULL),('db5755515b61490bba992dc65a54e519','袋鼠',NULL),('de937cda0f2a4c8e9a9f5084949f2ea7','蟒蛇',NULL),('e555e386a29a4c2d831d3a27401693fb','賓士貓',NULL),('e6dd20c66c1d499fb412623ff89cafe3','正確',NULL),('e7ca60a47e794bd5b82e284909943d7e','boolean',NULL),('e8793abae2174a868c637c6ca323a010','錯誤',NULL),('ef40347b3f4a4533a5e27296bf12e532','會講人話',NULL),('fa081f05856f49e98169101e32bdde99','String',NULL),('fc4c4c28dce44911ad66afe944af8011','length、size()、size()',NULL),('fcfacd8198184732913058fb23c225bb','錯誤',NULL),('fe92e9de25f341daa121ccff41708d4e','錯誤',NULL),('ffc3a536b7c84277a58eb291ff1f5f6f','由於編譯完的binary可以直接執行，給予其他公司執行時可以防止程式碼外洩，而編譯完成後的binary也無法反編譯。',NULL);
/*!40000 ALTER TABLE `question_option` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-05-24 15:57:15
