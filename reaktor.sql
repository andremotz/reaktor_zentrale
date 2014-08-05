-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 02, 2014 at 04:48 PM
-- Server version: 5.6.16
-- PHP Version: 5.5.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `reaktor`
--

-- --------------------------------------------------------

--
-- Table structure for table `globalData`
--

CREATE TABLE IF NOT EXISTS `globalData` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `comment` varchar(140) NOT NULL,
  `isRunning` tinyint(1) NOT NULL,
  `secondsCompleted` int(20) NOT NULL,
  `regressionS1multiplikator` float NOT NULL DEFAULT '7.1956',
  `regressionS2multiplikator` float NOT NULL DEFAULT '7.1956',
  `regressionS1offset` float NOT NULL,
  `regressionS2offset` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `globalData`
--

INSERT INTO `globalData` (`id`, `comment`, `isRunning`, `secondsCompleted`, `regressionS1multiplikator`, `regressionS2multiplikator`, `regressionS1offset`, `regressionS2offset`) VALUES
(1, 'Mysql Test', 1, 125, 7.3409, 7.3409, -302.28, -302.28);

-- --------------------------------------------------------

--
-- Table structure for table `messwerte`
--

CREATE TABLE IF NOT EXISTS `messwerte` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `timestamp` char(20) NOT NULL,
  `session_id` int(5) NOT NULL,
  `celsius1` int(4) NOT NULL,
  `celsius2` int(4) NOT NULL,
  `wasRunning` int(2) NOT NULL DEFAULT '0',
  `wasHeating` int(1) NOT NULL DEFAULT '0',
  `zielTemp` int(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=40 ;

--
-- Dumping data for table `messwerte`
--

INSERT INTO `messwerte` (`id`, `timestamp`, `session_id`, `celsius1`, `celsius2`, `wasRunning`, `wasHeating`, `zielTemp`) VALUES
(1, '2014-09-30-12:09:21', 1, 14, 42, 1, 1, 52),
(2, '2014-09-30-12:09:26', 1, 14, 42, 1, 1, 52),
(3, '2014-09-30-12:09:31', 1, 14, 42, 1, 1, 52),
(4, '2014-09-30-12:09:36', 1, 14, 42, 1, 1, 52),
(5, '2014-09-30-12:09:41', 1, 14, 42, 1, 1, 52),
(6, '2014-09-30-12:09:46', 1, 14, 42, 1, 1, 52),
(7, '2014-09-30-12:09:52', 1, 14, 42, 1, 1, 52),
(8, '2014-09-30-12:09:57', 1, 14, 42, 1, 1, 52),
(9, '2014-10-30-12:10:02', 1, 14, 42, 1, 1, 52),
(10, '2014-10-30-12:10:20', 1, 14, 42, 1, 1, 52),
(11, '2014-10-30-12:10:20', 1, 14, 42, 1, 1, 52),
(12, '2014-10-30-12:10:26', 1, 14, 42, 1, 1, 52),
(13, '2014-10-30-12:10:26', 1, 14, 42, 1, 1, 52),
(14, '2014-10-30-12:10:31', 1, 14, 42, 1, 1, 52),
(15, '2014-10-30-12:10:31', 1, 14, 42, 1, 1, 52),
(16, '2014-10-30-12:10:36', 1, 14, 42, 1, 1, 52),
(17, '2014-10-30-12:10:36', 1, 14, 42, 1, 1, 52),
(18, '2014-10-30-12:10:42', 1, 14, 42, 1, 1, 52),
(19, '2014-10-30-12:10:42', 1, 14, 42, 1, 1, 52),
(20, '2014-10-30-12:10:47', 1, 14, 42, 1, 1, 52),
(21, '2014-10-30-12:10:47', 1, 14, 42, 1, 1, 52),
(22, '2014-10-30-12:10:52', 1, 14, 42, 1, 1, 52),
(23, '2014-10-30-12:10:52', 1, 14, 42, 1, 1, 52),
(24, '2014-10-30-12:10:57', 1, 14, 42, 1, 1, 52),
(25, '2014-10-30-12:10:58', 1, 14, 42, 1, 1, 52),
(26, '2014-11-30-12:11:03', 1, 14, 42, 1, 1, 52),
(27, '2014-11-30-12:11:03', 1, 14, 42, 1, 1, 52),
(28, '2014-11-30-12:11:08', 1, 14, 42, 1, 1, 52),
(29, '2014-11-30-12:11:08', 1, 14, 42, 1, 1, 52),
(30, '2014-11-30-12:11:13', 1, 14, 42, 1, 1, 52),
(31, '2014-11-30-12:11:13', 1, 14, 42, 1, 1, 52),
(32, '2014-11-30-12:11:19', 1, 14, 42, 1, 1, 52),
(33, '2014-11-30-12:11:19', 1, 14, 42, 1, 1, 52),
(34, '2014-11-30-12:11:24', 1, 14, 42, 1, 1, 52),
(35, '2014-11-30-12:11:24', 1, 14, 42, 1, 1, 52),
(36, '2014-11-30-12:11:29', 1, 14, 42, 1, 1, 52),
(37, '2014-11-30-12:11:29', 1, 14, 42, 1, 1, 52),
(38, '2014-11-30-12:11:35', 1, 14, 42, 1, 1, 52),
(39, '2014-11-30-12:11:35', 1, 14, 42, 1, 1, 52);

-- --------------------------------------------------------

--
-- Table structure for table `zielwerte`
--

CREATE TABLE IF NOT EXISTS `zielwerte` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `session_id` int(6) NOT NULL,
  `celsius` int(4) NOT NULL,
  `seconds` int(9) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `zielwerte`
--

INSERT INTO `zielwerte` (`id`, `session_id`, `celsius`, `seconds`) VALUES
(1, 1, 6, 120),
(2, 1, 52, 2100),
(3, 1, 62, 1200),
(4, 1, 74, 1800),
(5, 1, 10, 1000);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
