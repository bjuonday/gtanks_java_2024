-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 14, 2025 at 05:19 PM
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
-- Database: `db_gtanks`
--

-- --------------------------------------------------------

--
-- Table structure for table `gt_groups`
--

CREATE TABLE `gt_groups` (
  `group_id` int(11) NOT NULL,
  `group_name` text NOT NULL,
  `group_rank` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `gt_users`
--

CREATE TABLE `gt_users` (
  `user_id` int(11) NOT NULL,
  `user_login` text NOT NULL,
  `user_password` text NOT NULL,
  `user_email` text DEFAULT NULL,
  `user_score` int(11) NOT NULL,
  `user_balance` int(11) NOT NULL,
  `user_group_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `gt_users`
--

INSERT INTO `gt_users` (`user_id`, `user_login`, `user_password`, `user_email`, `user_score`, `user_balance`, `user_group_id`) VALUES
(2, 'test', 'f5bb0c8de146c67b44babbf4e6584cc0', NULL, 0, 0, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `gt_users_bans`
--

CREATE TABLE `gt_users_bans` (
  `ban_id` int(11) NOT NULL,
  `ban_user` text NOT NULL,
  `ban_reason` text NOT NULL,
  `ban_admin` text NOT NULL,
  `ban_start` longtext NOT NULL,
  `ban_length` longtext NOT NULL,
  `ban_enable` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `gt_users_items`
--

CREATE TABLE `gt_users_items` (
  `garage_id` int(11) NOT NULL,
  `item_user` longtext DEFAULT NULL,
  `item_id` text NOT NULL,
  `item_count` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `gt_users_mount`
--

CREATE TABLE `gt_users_mount` (
  `mount_id` int(11) NOT NULL,
  `mount_user` longtext DEFAULT NULL,
  `mount_weapon` text NOT NULL,
  `mount_armor` text NOT NULL,
  `mount_color` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `gt_users`
--
ALTER TABLE `gt_users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `user_login` (`user_login`) USING HASH;

--
-- Indexes for table `gt_users_bans`
--
ALTER TABLE `gt_users_bans`
  ADD PRIMARY KEY (`ban_id`);

--
-- Indexes for table `gt_users_items`
--
ALTER TABLE `gt_users_items`
  ADD PRIMARY KEY (`garage_id`);

--
-- Indexes for table `gt_users_mount`
--
ALTER TABLE `gt_users_mount`
  ADD PRIMARY KEY (`mount_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `gt_users`
--
ALTER TABLE `gt_users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `gt_users_bans`
--
ALTER TABLE `gt_users_bans`
  MODIFY `ban_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `gt_users_items`
--
ALTER TABLE `gt_users_items`
  MODIFY `garage_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `gt_users_mount`
--
ALTER TABLE `gt_users_mount`
  MODIFY `mount_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
