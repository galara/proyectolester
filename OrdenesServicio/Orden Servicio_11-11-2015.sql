-- MySQL dump 10.13  Distrib 5.6.10, for Win32 (x86)
--
-- Host: localhost    Database: tautomotriz
-- ------------------------------------------------------
-- Server version	5.6.10

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
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cliente` (
  `idCliente` int(11) NOT NULL AUTO_INCREMENT,
  `nit` varchar(45) DEFAULT NULL,
  `nombres` varchar(45) DEFAULT NULL,
  `apellidos` varchar(45) DEFAULT NULL,
  `direccion` varchar(45) DEFAULT NULL,
  `telefono` varchar(45) DEFAULT NULL,
  `correo` varchar(45) DEFAULT NULL,
  `sexo` varchar(45) DEFAULT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`idCliente`),
  UNIQUE KEY `Nit_UNIQUE` (`nit`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (1,'258369','Gustavo Adolfo','Lara','Guatemalaa','121212',NULL,'M',1);
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empleado`
--

DROP TABLE IF EXISTS `empleado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `empleado` (
  `idEmpleado` int(11) NOT NULL AUTO_INCREMENT,
  `dpi` varchar(45) DEFAULT NULL,
  `nombres` varchar(45) DEFAULT NULL,
  `apellidos` varchar(45) DEFAULT NULL,
  `Direccion` varchar(45) DEFAULT NULL,
  `Telefono` varchar(45) DEFAULT NULL,
  `Sexo` varchar(1) DEFAULT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  `Cargo` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idEmpleado`),
  UNIQUE KEY `dpi_UNIQUE` (`dpi`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='							';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empleado`
--

LOCK TABLES `empleado` WRITE;
/*!40000 ALTER TABLE `empleado` DISABLE KEYS */;
INSERT INTO `empleado` VALUES (1,'123456','Gustavo Adolfo','Lara Macario','Guatemala','5689','M',1,'Ayudante de Mecanico');
/*!40000 ALTER TABLE `empleado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `marca`
--

DROP TABLE IF EXISTS `marca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `marca` (
  `idmarca` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(45) DEFAULT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`idmarca`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `marca`
--

LOCK TABLES `marca` WRITE;
/*!40000 ALTER TABLE `marca` DISABLE KEYS */;
INSERT INTO `marca` VALUES (1,'TOYOTA',1),(2,'HONDA',1);
/*!40000 ALTER TABLE `marca` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu` (
  `idmenu` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `principal` varchar(45) DEFAULT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`idmenu`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'Marca','Principal',1),(2,'Cliente','Principal',1),(3,'Empleado','Principal',1),(4,'Usuario','Principal',1),(5,'Orden Servicio','Principal',1),(6,'Menu','Principal',1),(7,'Servicio','Principal',1),(8,'Vehiculo','Principal',1),(9,'Generar Backup Principal','Principal',1),(10,'Restaurar Backup Principal','Principal',1);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordenservicio`
--

DROP TABLE IF EXISTS `ordenservicio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ordenservicio` (
  `idOrdenServicio` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(200) DEFAULT NULL,
  `idVehiculo` int(11) NOT NULL,
  `idCliente` int(11) NOT NULL,
  `Fecha_Orden` date DEFAULT NULL,
  `Fecha_entrega` date DEFAULT NULL,
  `Estado` tinyint(1) DEFAULT NULL,
  `idEmpleado` int(11) NOT NULL,
  `Usuario_idusuario` int(11) NOT NULL,
  PRIMARY KEY (`idOrdenServicio`),
  KEY `fk_OrdenServicio_Vehiculo1_idx` (`idVehiculo`),
  KEY `fk_OrdenServicio_Cliente1_idx` (`idCliente`),
  KEY `fk_OrdenServicio_Empleado1_idx` (`idEmpleado`),
  KEY `fk_OrdenServicio_Usuario1_idx` (`Usuario_idusuario`),
  CONSTRAINT `fk_OrdenServicio_Cliente1` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_OrdenServicio_Empleado1` FOREIGN KEY (`idEmpleado`) REFERENCES `empleado` (`idEmpleado`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_OrdenServicio_Usuario1` FOREIGN KEY (`Usuario_idusuario`) REFERENCES `usuario` (`idusuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_OrdenServicio_Vehiculo1` FOREIGN KEY (`idVehiculo`) REFERENCES `vehiculos` (`idvehiculos`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordenservicio`
--

LOCK TABLES `ordenservicio` WRITE;
/*!40000 ALTER TABLE `ordenservicio` DISABLE KEYS */;
INSERT INTO `ordenservicio` VALUES (1,'',1,1,'2015-11-11','2015-11-11',1,1,1),(2,'prueba 2',1,1,'2015-11-11','2015-11-11',1,1,1),(3,'prueba 3',1,1,'2015-11-11','2015-11-12',1,1,1),(4,'pruebaaa 5',1,1,'2015-11-11','2015-11-15',1,1,1),(5,'Ajuste de frenos.',1,1,'2015-11-11','2015-11-16',1,1,1),(6,'sadfsadf',1,1,'2015-11-11','2015-11-29',1,1,1);
/*!40000 ALTER TABLE `ordenservicio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordenserviciodetalle`
--

DROP TABLE IF EXISTS `ordenserviciodetalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ordenserviciodetalle` (
  `idOrdenServicioDetalle` int(11) NOT NULL AUTO_INCREMENT,
  `idServicio` int(11) NOT NULL,
  `idOrdenServicio` int(11) NOT NULL,
  PRIMARY KEY (`idOrdenServicioDetalle`),
  KEY `fk_OrdenServicioDetalle_Servicio1_idx` (`idServicio`),
  KEY `fk_OrdenServicioDetalle_OrdenServicio1_idx` (`idOrdenServicio`),
  CONSTRAINT `fk_OrdenServicioDetalle_OrdenServicio1` FOREIGN KEY (`idOrdenServicio`) REFERENCES `ordenservicio` (`idOrdenServicio`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_OrdenServicioDetalle_Servicio1` FOREIGN KEY (`idServicio`) REFERENCES `servicio` (`idServicio`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordenserviciodetalle`
--

LOCK TABLES `ordenserviciodetalle` WRITE;
/*!40000 ALTER TABLE `ordenserviciodetalle` DISABLE KEYS */;
INSERT INTO `ordenserviciodetalle` VALUES (1,1,1),(2,1,2),(3,1,3),(4,1,4),(5,3,5),(6,2,5),(7,4,5),(8,3,6),(9,1,6),(10,2,6),(11,5,6),(12,4,6);
/*!40000 ALTER TABLE `ordenserviciodetalle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `perfilusuario`
--

DROP TABLE IF EXISTS `perfilusuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `perfilusuario` (
  `idperfilusuario` int(11) NOT NULL AUTO_INCREMENT,
  `estado` tinyint(1) DEFAULT NULL,
  `Usuario_idusuario` int(11) NOT NULL,
  `menu_idmenu` int(11) NOT NULL,
  PRIMARY KEY (`idperfilusuario`),
  KEY `fk_perfilusuario_Usuario1_idx` (`Usuario_idusuario`),
  KEY `fk_perfilusuario_menu1_idx` (`menu_idmenu`),
  CONSTRAINT `fk_perfilusuario_menu1` FOREIGN KEY (`menu_idmenu`) REFERENCES `menu` (`idmenu`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_perfilusuario_Usuario1` FOREIGN KEY (`Usuario_idusuario`) REFERENCES `usuario` (`idusuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perfilusuario`
--

LOCK TABLES `perfilusuario` WRITE;
/*!40000 ALTER TABLE `perfilusuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `perfilusuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servicio`
--

DROP TABLE IF EXISTS `servicio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `servicio` (
  `idServicio` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(45) DEFAULT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`idServicio`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servicio`
--

LOCK TABLES `servicio` WRITE;
/*!40000 ALTER TABLE `servicio` DISABLE KEYS */;
INSERT INTO `servicio` VALUES (1,'Cambio de aceite',1),(2,'Lineacion y balanceo',1),(3,'Ajuste de Frenos',1),(4,'Mantenimiento P',1),(5,'Mantenimeinto C',1);
/*!40000 ALTER TABLE `servicio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `idusuario` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  `usuario` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  `fechacreacion` date DEFAULT NULL,
  `fecnabaja` date DEFAULT NULL,
  PRIMARY KEY (`idusuario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'Gustavo','glara','123',1,'2015-11-10','2015-11-10'),(2,'Gustavo','glara2','123',1,'2015-11-10','2015-11-10');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehiculos`
--

DROP TABLE IF EXISTS `vehiculos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vehiculos` (
  `idvehiculos` int(11) NOT NULL AUTO_INCREMENT,
  `matricula` varchar(45) DEFAULT NULL,
  `tarjeta_circulacion` varchar(45) DEFAULT NULL,
  `modelo` int(11) DEFAULT NULL,
  `idmarca` int(11) NOT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`idvehiculos`),
  UNIQUE KEY `Placa_UNIQUE` (`matricula`),
  KEY `fk_Vehiculo_Marca1_idx` (`idmarca`),
  CONSTRAINT `fk_Vehiculo_Marca1` FOREIGN KEY (`idmarca`) REFERENCES `marca` (`idmarca`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehiculos`
--

LOCK TABLES `vehiculos` WRITE;
/*!40000 ALTER TABLE `vehiculos` DISABLE KEYS */;
INSERT INTO `vehiculos` VALUES (1,'122dfg','123456C',2015,2,1);
/*!40000 ALTER TABLE `vehiculos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-11 16:52:37
