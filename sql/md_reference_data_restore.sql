
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

/*!40000 ALTER TABLE `md_steel_property` DISABLE KEYS */;
INSERT INTO `md_steel_property` (`property_id`, `steel_grade`, `state`, `yield_min`, `tensile_min`, `elongation_min`, `hv_min`, `hv_max`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1,'SUS301','ANN',205.00,520.00,40.00,NULL,218.00,'0','',NULL,'',NULL,NULL),(2,'SUS301','1/4H',470.00,780.00,25.00,250.00,310.00,'0','',NULL,'',NULL,NULL),(3,'SUS301','1/2H',510.00,930.00,18.00,310.00,370.00,'0','',NULL,'',NULL,NULL),(4,'SUS301','3/4H',745.00,1130.00,5.00,370.00,430.00,'0','',NULL,'',NULL,NULL),(5,'SUS301','FH',1030.00,1320.00,4.00,430.00,490.00,'0','',NULL,'',NULL,NULL),(6,'SUS301','EH',1275.00,1570.00,3.00,490.00,550.00,'0','',NULL,'',NULL,NULL),(7,'SUS301','SH',1550.00,1750.00,1.00,550.00,NULL,'0','',NULL,'',NULL,NULL),(8,'SUS304','ANN',205.00,520.00,40.00,NULL,200.00,'0','',NULL,'',NULL,NULL),(9,'SUS304','1/4H',450.00,700.00,20.00,220.00,250.00,'0','',NULL,'',NULL,NULL),(10,'SUS304','1/2H',700.00,850.00,8.00,250.00,310.00,'0','',NULL,'',NULL,NULL),(11,'SUS304','3/4H',880.00,1000.00,4.00,310.00,370.00,'0','',NULL,'',NULL,NULL),(12,'SUS304','FH',1020.00,1150.00,1.00,370.00,NULL,'0','',NULL,'',NULL,NULL),(13,'SUS430','ANN',205.00,420.00,22.00,NULL,200.00,'0','',NULL,'',NULL,NULL),(14,'SUS430','1/4H',500.00,550.00,5.00,160.00,200.00,'0','',NULL,'',NULL,NULL),(15,'SUS430','1/2H',600.00,650.00,3.00,200.00,240.00,'0','',NULL,'',NULL,NULL),(16,'SUS430','3/4H',680.00,750.00,2.00,240.00,280.00,'0','',NULL,'',NULL,NULL),(17,'SUS430','H',800.00,850.00,1.00,280.00,NULL,'0','',NULL,'',NULL,NULL);
/*!40000 ALTER TABLE `md_steel_property` ENABLE KEYS */;

/*!40000 ALTER TABLE `md_tolerance` DISABLE KEYS */;
INSERT INTO `md_tolerance` (`tolerance_id`, `standard`, `thickness_min`, `thickness_max`, `width_min`, `width_max`, `dev_lower`, `dev_upper`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1,'JIS G4305',0.000,0.600,10.00,80.00,-0.150,0.150,'0','',NULL,'',NULL,''),(2,'JIS G4305',0.600,1.000,10.00,80.00,-0.200,0.200,'0','',NULL,'',NULL,''),(3,'JIS G4305',0.000,0.600,80.00,160.00,-0.150,0.150,'0','',NULL,'',NULL,''),(4,'JIS G4305',0.600,1.000,80.00,160.00,-0.200,0.200,'0','',NULL,'',NULL,''),(5,'JIS G4305',0.000,0.600,160.00,260.00,-0.200,0.200,'0','',NULL,'',NULL,''),(6,'JIS G4305',0.600,1.000,160.00,260.00,-0.250,0.250,'0','',NULL,'',NULL,''),(7,'JIS G4305',0.000,0.600,260.00,400.00,-0.250,0.250,'0','',NULL,'',NULL,''),(8,'JIS G4305',0.600,1.000,260.00,400.00,-0.250,0.250,'0','',NULL,'',NULL,''),(9,'JIS G4305',0.000,0.600,400.00,600.00,-0.300,0.300,'0','',NULL,'',NULL,''),(10,'JIS G4305',0.600,1.000,400.00,600.00,-0.300,0.300,'0','',NULL,'',NULL,''),(11,'无锡宝浦',0.030,0.150,10.00,80.00,-0.100,0.100,'0','',NULL,'',NULL,''),(12,'无锡宝浦',0.150,0.500,10.00,80.00,-0.150,0.150,'0','',NULL,'',NULL,''),(13,'无锡宝浦',0.500,1.000,10.00,80.00,-0.200,0.200,'0','',NULL,'',NULL,''),(14,'无锡宝浦',0.030,0.150,80.00,160.00,-0.100,0.100,'0','',NULL,'',NULL,''),(15,'无锡宝浦',0.150,0.500,80.00,160.00,-0.150,0.150,'0','',NULL,'',NULL,''),(16,'无锡宝浦',0.500,1.000,80.00,160.00,-0.200,0.200,'0','',NULL,'',NULL,''),(17,'无锡宝浦',0.030,0.150,160.00,260.00,-0.100,0.100,'0','',NULL,'',NULL,''),(18,'无锡宝浦',0.150,0.500,160.00,260.00,-0.150,0.150,'0','',NULL,'',NULL,''),(19,'无锡宝浦',0.500,1.000,160.00,260.00,-0.200,0.200,'0','',NULL,'',NULL,''),(20,'无锡宝浦',0.030,0.150,260.00,400.00,-0.100,0.100,'0','',NULL,'',NULL,''),(21,'无锡宝浦',0.150,0.500,260.00,400.00,-0.150,0.150,'0','',NULL,'',NULL,''),(22,'无锡宝浦',0.500,1.000,260.00,400.00,-0.200,0.200,'0','',NULL,'',NULL,''),(23,'无锡宝浦',0.030,0.150,400.00,600.00,-0.100,0.100,'0','',NULL,'',NULL,''),(24,'无锡宝浦',0.150,0.500,400.00,600.00,-0.150,0.150,'0','',NULL,'',NULL,''),(25,'无锡宝浦',0.500,1.000,400.00,600.00,-0.200,0.200,'0','',NULL,'',NULL,'');
/*!40000 ALTER TABLE `md_tolerance` ENABLE KEYS */;

/*!40000 ALTER TABLE `md_process` DISABLE KEYS */;
INSERT INTO `md_process` (`process_id`, `process_no`, `process_name`, `is_qc`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1,10,'原材料检验','1','0','',NULL,'',NULL,'来料检验：物理性能、带材规格、材质、表面'),(2,20,'轧制','0','0','',NULL,'',NULL,'厚度、表面（Ra0.2-0.4）'),(3,30,'固溶处理','0','0','',NULL,'',NULL,'硬度HV≤165、延伸率≥55%、表面光亮无划伤'),(4,40,'清洗','0','0','',NULL,'',NULL,'无油污'),(5,50,'拉矫','0','0','',NULL,'',NULL,'不平度≤1.5mm、性能HV250-280、延伸率≥55%'),(6,60,'分剪','0','0','',NULL,'',NULL,'宽度213±0.10mm、毛刺高度≤0.03mm'),(7,70,'包装','0','0','',NULL,'',NULL,'卷重要求、内孔 c508mm'),(8,80,'总检查员抽检','1','0','',NULL,'',NULL,'各项尺寸和性能抽检'),(9,90,'成品仓库','0','0','',NULL,'',NULL,'储存条件：防止损伤和淋湿');
/*!40000 ALTER TABLE `md_process` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

