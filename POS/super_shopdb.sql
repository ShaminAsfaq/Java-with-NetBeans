-- MySQL dump 10.13  Distrib 5.5.46, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: super_shopdb
-- ------------------------------------------------------
-- Server version	5.5.46-0ubuntu0.14.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `liveTransaction`
--

DROP TABLE IF EXISTS `liveTransaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `liveTransaction` (
  `time` varchar(30) DEFAULT NULL,
  `customerName` varchar(30) DEFAULT NULL,
  `totalBill` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `liveTransaction`
--

LOCK TABLES `liveTransaction` WRITE;
/*!40000 ALTER TABLE `liveTransaction` DISABLE KEYS */;
INSERT INTO `liveTransaction` VALUES ('27 December,2015 10:14 PM','Md Ashraf Uddin',100);
/*!40000 ALTER TABLE `liveTransaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `productName` varchar(30) DEFAULT NULL,
  `productCategory` varchar(30) DEFAULT NULL,
  `unitPrice` double DEFAULT NULL,
  `totalUnit` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES ('Minicate Rice','Grocery',50,98),('El Paso','Cosmetics',750,34),('Chopstick','Accessories',150,81),('Onion','Grocery',29,36),('Garlick','Grocery',60,47),('Potatoes','Grocery',24,48),('Dinner Set','Crocaries',2500,17),('Coffee Mug','Crocaries',80,16),('Toy Train','Stationary',1200,14),('Stone Lighter','Accessories',345,18),('Teddy Bear','Stationary',1390,10),('Catberry Silk Chocolate','Stationary',350,20),('Catberry Bournville','Stationary',350,18.75),('Apple Juice','Grocery',25,100),('Heart Diary','Stationary',250,30),('Gillette Shaving Cream','Accessories',250,50),('Gillette Mach 3 Razor','Stationary',500,20),('Aarong Milk','Dairy',90,27.5),('Milk Vita Milk','Dairy',89,25),('Matador Gripper Gel','Stationary',8,30),('Matador Elegant','Stationary',8,30),('A4TECH Key Board','Accessories',490,17),('Logitech Game Controller','Accessories',990,14),('Walton Primo RX2','Accessories',11952,10),('Chicken Eggs(Deshi)','Dairy',52,100),('Chicken Eggs(Poltry)','Dairy',38,100),('Tuna Fish(Quarter Pack)','Dairy',580,49),('Beaf(Half Kg Pack)','Dairy',210,20),('Chicken(Poltry 1 Kg)','Dairy',160,15),('Coca-Cola 1 Litre','Grocery',60,49),('Coca-Cola 500 mL','Grocery',32,50),('Coca-Cola 250 mL','Grocery',22,100),('Coca-Cola 125 mL','Grocery',16,80),('Pepsi 1 Litre','Grocery',60,50),('Pepsi 500 mL','Grocery',32,50),('Pepsi 250 mL','Grocery',22,100),('Pepsi 125 mL','Grocery',15,100),('Sprite 1 Litre','Grocery',60,100),('Sprite 500 mL','Grocery',32,50),('Sprite 250 mL','Grocery',22,80),('Sprite 125 mL','Grocery',15,100),('7-Up 1 Litre','Grocery',60,98),('7-Up 500 mL','Grocery',32,50),('7-Up 250 mL','Grocery',22,50),('Mojo 1 Litre','Grocery',60,50),('Mojo 500 mL','Grocery',32,50),('Mojo 250 mL','Grocery',22,50),('Frutika 1 Litre','Grocery',60,50),('Frutika 500 mL','Grocery',40,50),('Frutika 250 mL','Grocery',22,100),('Pran Frooto 1 Litre','Grocery',60,50),('Pran Frooto 500 mL','Grocery',40,50),('Pran Frooto 250 mL','Grocery',22,100),('Mum Water 1 Litre','Grocery',25,100),('Mum Water 500 mL','Grocery',15,100),('Aquafina Water 1 Litre','Grocery',28,98),('Aquafina Water 500 mL','Grocery',18,100),('Acme Water 1 Litre','Grocery',25,100),('Acme Water 500 mL','Grocery',15,100),('Nokia Mobile','Accessories',4500,10),('Mobile','Accessories',3000,8),('Rayban Sunglass','Accessories',1400,20),('Lather Jacket','Accessories',12000,5),('iPhone 6','Accessories',72000,12),('Meril Vaselin','Cosmetics',15,100),('Ice cream','Food',20,100);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salesMan`
--

DROP TABLE IF EXISTS `salesMan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `salesMan` (
  `id` varchar(30) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salesMan`
--

LOCK TABLES `salesMan` WRITE;
/*!40000 ALTER TABLE `salesMan` DISABLE KEYS */;
INSERT INTO `salesMan` VALUES ('intiser','121'),('rabi','666');
/*!40000 ALTER TABLE `salesMan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shopping_details`
--

DROP TABLE IF EXISTS `shopping_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shopping_details` (
  `date` varchar(12) DEFAULT NULL,
  `time` varchar(10) DEFAULT NULL,
  `customerName` varchar(30) DEFAULT NULL,
  `bill` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shopping_details`
--

LOCK TABLES `shopping_details` WRITE;
/*!40000 ALTER TABLE `shopping_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `shopping_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transaction` (
  `customerUserName` varchar(30) DEFAULT NULL,
  `shoppingDate` varchar(25) DEFAULT NULL,
  `customerProduct` varchar(30) DEFAULT NULL,
  `totalUnits` double DEFAULT NULL,
  `totalPrice` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
INSERT INTO `transaction` VALUES ('asu','27-12-2015 10:14 PM','Minicate Rice',2,100);
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userInfo`
--

DROP TABLE IF EXISTS `userInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userInfo` (
  `customerName` varchar(30) DEFAULT NULL,
  `customerUserName` varchar(30) DEFAULT NULL,
  `userPassword` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userInfo`
--

LOCK TABLES `userInfo` WRITE;
/*!40000 ALTER TABLE `userInfo` DISABLE KEYS */;
INSERT INTO `userInfo` VALUES ('Md Ashraf Uddin','ASU','321');
/*!40000 ALTER TABLE `userInfo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-27 22:45:21
