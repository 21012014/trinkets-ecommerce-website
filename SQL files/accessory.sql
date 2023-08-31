-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: graded_assignment
-- ------------------------------------------------------
-- Server version	5.7.21-log

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
-- Table structure for table `accessory`
--

DROP TABLE IF EXISTS `accessory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accessory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `img_name` varchar(255) DEFAULT NULL,
  `material` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `quantity` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `sold` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK78k22tqvo8w0icau5am8064qr` (`category_id`),
  CONSTRAINT `FK78k22tqvo8w0icau5am8064qr` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accessory`
--

LOCK TABLES `accessory` WRITE;
/*!40000 ALTER TABLE `accessory` DISABLE KEYS */;
INSERT INTO `accessory` VALUES (1,'925 Silver Korean Fashion Crystal Zircon Adjustable Opening Ring For Women COCOJEWELRY','Silver_adjustable_opening_ring.png','S925 silver plated','Silver ring',1.82,5258,1,3),(2,'Butterfly Earrings Exquisite Zirconium Ear Cuff Leaf Ear Hook Clip On Cartilage Earrings','Butterfly ear cuff.png','Metal Coating, Others','Butterfly earrings',4.3,3549,2,5),(3,'Four Leaf Bracelet Women Silver Link Wrist Chain Bracelets Jewelry','silver bracelet with four leaf clovers.png','Silver','Silver Link Wrist Chain Bracelets',1.39,729,4,4),(4,'MIHAN Jewelry Flower Wrapped Quartz Pendant Wire Wrap Natural Stone Necklace Hexagonal Healing Crystal Necklace Rose Quartz','Quartz pendant crystal necklace.png','Others','Natural Stone Necklace',3.87,98,3,3),(5,'Headwear Flowers Pearl Rhinestone Crystal Hair Pin Bridal Headdress Prom Wedding Jewelry Hair Clip Hair Accessories','Headwear flower rhinestone hair pin.png','Alloy','Flower Pearl Rhinestone Crystal Hair Pin',4.8,5184,5,3),(6,'XILIANGFEIZI Elegance Tulip Brooch High-End Zircon & Opal Flower Brooches Pins Coat Fashion Accessories Corsage Ladies Gift','Elegant tulip brooch.png','Rhinestone, Others','Elegant Tulip Brooch Coat Pin',2.68,3678,5,3),(11,'Cute White & Black Ghost Alloy Couple Pendant','cartoon_ghost_alloy_pendant.png','Others','Cartoon Ghost Pendant',1.5,2998,3,2);
/*!40000 ALTER TABLE `accessory` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-17 22:43:59
