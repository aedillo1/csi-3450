-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.6.5-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for library
CREATE DATABASE IF NOT EXISTS `library` /*!40100 DEFAULT CHARACTER SET utf8mb3 */;
USE `library`;

-- Dumping structure for table library.books
CREATE TABLE IF NOT EXISTS `books` (
  `BOOK_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `BOOK_TITLE` varchar(50) DEFAULT '0',
  `BOOK_AUTHOR` varchar(50) DEFAULT '0',
  `BOOK_STOCK` int(11) NOT NULL DEFAULT 1,
  `BOOK_ISBN` varchar(50) DEFAULT '0',
  PRIMARY KEY (`BOOK_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3 COMMENT='books table';

-- Dumping data for table library.books: ~10 rows (approximately)
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` (`BOOK_ID`, `BOOK_TITLE`, `BOOK_AUTHOR`, `BOOK_STOCK`, `BOOK_ISBN`) VALUES
	(1, 'THE EPIC OF GILGAMESH', 'SIN-LEQI-UNNINNI', 1, '978-0140441000'),
	(2, '1984', 'GEORGE ORWELL', 3, '978-0451524935'),
	(3, 'TO KILL A MOCKINGIBRD', 'HARPER LEE', 3, '978-0060935467'),
	(4, 'THE BOOK THIEF', 'MARKUS ZUSAK', 1, '978-0375842207'),
	(5, 'THE FAULT IN OUR STARS', 'JOHN GREEN', 3, '978-0142424179'),
	(6, 'PRIDE AND PREJUDICE', 'JANE AUSTEN', 2, '978-1676097709'),
	(7, 'THE HUNGER GAMES', 'SUZANNE COLLINS', 2, '978-0439023528'),
	(8, 'ANIMAL FARM', 'GEORGE ORWELL', 3, '978-0151002177'),
	(9, 'THE CATCHER IN THE RYE', 'J.D SALINGER', 3, '978-0316769174'),
	(10, 'THE GREAT GATSBY', 'F. SCOTT FITZGERALD', 2, '978-0743273565');
/*!40000 ALTER TABLE `books` ENABLE KEYS */;

-- Dumping structure for table library.employees
CREATE TABLE IF NOT EXISTS `employees` (
  `EMP_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `EMP_PASS` varchar(50) NOT NULL DEFAULT '0',
  `EMP_NAME` varchar(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`EMP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb3 COMMENT='employees table';

-- Dumping data for table library.employees: ~1 rows (approximately)
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` (`EMP_ID`, `EMP_PASS`, `EMP_NAME`) VALUES
	(1000, 'test', 'test');
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
