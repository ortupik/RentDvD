-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.1.21-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping structure for table dvd_rental_db.customers
CREATE TABLE IF NOT EXISTS `customers` (
  `customer_id` int(11) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(50) DEFAULT NULL,
  `lastname` varchar(50) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- Dumping data for table dvd_rental_db.customers: ~0 rows (approximately)
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` (`customer_id`, `firstname`, `lastname`, `phone`, `address`, `created_at`) VALUES
	(2, 'Jake', 'Warton', '07354353434', 'P.O BOX 433, Bradford', '2018-03-17 08:12:16');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;


-- Dumping structure for table dvd_rental_db.dvd_table
CREATE TABLE IF NOT EXISTS `dvd_table` (
  `dvd_id` int(11) NOT NULL AUTO_INCREMENT,
  `dvd_name` varchar(100) NOT NULL DEFAULT '',
  `genre` varchar(100) NOT NULL DEFAULT '',
  `dvd_description` text,
  `image_path` text,
  `rental_price` int(11) DEFAULT '0',
  `time_created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`dvd_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- Dumping data for table dvd_rental_db.dvd_table: ~2 rows (approximately)
/*!40000 ALTER TABLE `dvd_table` DISABLE KEYS */;
INSERT INTO `dvd_table` (`dvd_id`, `dvd_name`, `genre`, `dvd_description`, `image_path`, `rental_price`, `time_created`) VALUES
	(1, 'Tulip', 'Drama', 'Super Movie', 'Tulips.jpg', 20, '2018-03-16 20:04:26'),
	(2, 'Ostrich', 'Action', 'Animated movie', '7.jpg', 25, '2018-03-16 20:05:00'),
	(3, 'Return of Koal', 'Drama', 'Rise of the Apes', 'Koala.jpg', 20, '2018-03-19 12:46:51');
/*!40000 ALTER TABLE `dvd_table` ENABLE KEYS */;


-- Dumping structure for table dvd_rental_db.rent
CREATE TABLE IF NOT EXISTS `rent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) DEFAULT '0',
  `dvd_id` int(11) DEFAULT '0',
  `amount` int(11) DEFAULT '0',
  `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

-- Dumping data for table dvd_rental_db.rent: ~2 rows (approximately)
/*!40000 ALTER TABLE `rent` DISABLE KEYS */;
INSERT INTO `rent` (`id`, `customer_id`, `dvd_id`, `amount`, `time`) VALUES
	(9, 1, 2, 0, '2018-03-19 12:22:40');
/*!40000 ALTER TABLE `rent` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
