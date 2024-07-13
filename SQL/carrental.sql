-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 13, 2024 at 05:26 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `carrental`
--

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

CREATE TABLE `booking` (
  `bookid` int(100) NOT NULL,
  `carId` varchar(50) NOT NULL,
  `userId` varchar(50) NOT NULL,
  `bookStart` datetime(6) DEFAULT current_timestamp(6),
  `bookEnd` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `booking`
--

INSERT INTO `booking` (`bookid`, `carId`, `userId`, `bookStart`, `bookEnd`) VALUES
(1, '1', '1', '2024-07-01 05:52:24.000000', '2024-07-01 17:52:24.000000'),
(2, '3', '1', '2024-07-13 00:01:35.000000', '2024-07-13 03:01:35.000000'),
(3, '1', '1', '2024-07-13 00:07:48.000000', '2024-07-13 03:07:48.000000'),
(4, '2', '1', '2024-07-13 00:07:51.000000', '2024-07-13 03:07:51.000000'),
(5, '4', '1', '2024-07-13 00:07:53.000000', '2024-07-13 03:07:53.000000'),
(6, '1', '1', '2024-07-13 22:17:00.000000', '2024-07-14 00:17:00.000000'),
(7, '4', '1', '2024-07-13 22:38:46.000000', '2024-07-14 01:38:46.000000');

-- --------------------------------------------------------

--
-- Table structure for table `ownercars`
--

CREATE TABLE `ownercars` (
  `carid` int(100) NOT NULL,
  `carbrand` varchar(90) NOT NULL,
  `sitType` varchar(50) NOT NULL,
  `platNo` varchar(11) NOT NULL,
  `pricePerHour` float NOT NULL,
  `Status` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ownercars`
--

INSERT INTO `ownercars` (`carid`, `carbrand`, `sitType`, `platNo`, `pricePerHour`, `Status`) VALUES
(1, 'Toyota Camry', '4 seater', 'VUY 2123', 15, 'Booked'),
(2, 'Proton Persona', '4 seater', 'MDJ 2343', 10, 'Available'),
(3, 'Toyota Inova', '6 seater', 'JGS 1432', 25, 'Booked'),
(4, 'Proton Exora', '6 seater', 'AKM 1423', 22, 'Booked'),
(5, 'Toyota Unser', '6 seater', 'JUV 3345', 78, 'Available'),
(6, 'Porcshe', '4 Seater', 'LOL 2345', 89, 'Available');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `usrname` varchar(100) NOT NULL,
  `pass` varchar(100) NOT NULL,
  `usertype` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `age` int(50) NOT NULL,
  `noIC` varchar(50) NOT NULL,
  `address` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `usrname`, `pass`, `usertype`, `name`, `age`, `noIC`, `address`) VALUES
(1, 'kaneemi', 'kaneemi', 1, 'Ahmad Kaneemi', 21, '030414-04-0233', 'Alor Gajah, Melaka'),
(3, 'akram', 'akram', 1, 'Akram Yusop', 21, '030124-04-0213', 'Durian Tunggal, Melaka'),
(4, 'akta', 'akta', 2, 'Muhd Akta', 25, '990514-04-0234', 'Bukit Katil, Melaka'),
(5, 'afwan', 'afwan', 2, 'Shazarif Afwan', 22, '020424-04-0235', 'Bukit Beruang, Melaka');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`bookid`);

--
-- Indexes for table `ownercars`
--
ALTER TABLE `ownercars`
  ADD PRIMARY KEY (`carid`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `booking`
--
ALTER TABLE `booking`
  MODIFY `bookid` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `ownercars`
--
ALTER TABLE `ownercars`
  MODIFY `carid` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
