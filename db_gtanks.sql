-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 10, 2024 at 12:00 PM
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
  `user_id` longtext NOT NULL,
  `user_login` text NOT NULL,
  `user_password` text NOT NULL,
  `user_email` text DEFAULT NULL,
  `user_score` int(11) NOT NULL,
  `user_balance` int(11) NOT NULL,
  `user_group_id` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `gt_users_bans`
--

CREATE TABLE `gt_users_bans` (
  `ban_id` longtext NOT NULL,
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
  `garage_id` longtext NOT NULL,
  `item_user` longtext NOT NULL,
  `item_id` text NOT NULL,
  `item_count` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `gt_users_mount`
--

CREATE TABLE `gt_users_mount` (
  `mount_id` longtext NOT NULL,
  `mount_user` longtext NOT NULL,
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
  ADD UNIQUE KEY `login` (`user_login`) USING HASH;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
