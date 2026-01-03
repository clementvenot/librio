-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jan 04, 2026 at 12:18 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `librio`
--

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `id` bigint(20) NOT NULL,
  `external_id` varchar(100) NOT NULL,
  `title` varchar(500) NOT NULL,
  `subtitle` varchar(500) DEFAULT NULL,
  `author` varchar(500) DEFAULT NULL,
  `publisher` varchar(255) DEFAULT NULL,
  `published_date` varchar(20) DEFAULT NULL,
  `categories` varchar(500) DEFAULT NULL,
  `page_count` int(11) DEFAULT NULL,
  `average_rating` bigint(20) DEFAULT NULL,
  `image_medium` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`id`, `external_id`, `title`, `subtitle`, `author`, `publisher`, `published_date`, `categories`, `page_count`, `average_rating`, `image_medium`) VALUES
(28, 'gbk_jacques_aranda_fay', 'Jacques Aranda', NULL, 'Gaël Faye', 'Grasset', '2024-01-01', 'Roman', 240, 4, NULL),
(29, 'gbk_la_peste_camus', 'La Peste', NULL, 'Albert Camus', 'Gallimard', '1947-06-10', 'Roman', 320, 4, NULL),
(31, 'gbk_mort_metier', 'La mort et mon métier', NULL, 'Robert Merle', 'Gallimard', '1952-01-01', 'Roman', 340, 4, NULL),
(32, 'gbk_maleville', 'Malevil', NULL, 'Robert Merle', 'Gallimard', '1972-01-01', 'Science-fiction', 640, 4, NULL),
(33, 'gbk_orgueil_prejuges', 'Orgueil et Préjugés', NULL, 'Jane Austen', 'Penguin Classics', '1813-01-28', 'Roman', 432, 5, NULL),
(34, 'gbk_cyrano', 'Cyrano de Bergerac', NULL, 'Edmond Rostand', 'Flammarion', '1897-12-28', 'Théâtre', 256, 4, NULL),
(35, 'gbk_malade_imaginaire', 'Le Malade imaginaire', NULL, 'Molière', 'Larousse', '1673-02-10', 'Théâtre', 160, 4, NULL),
(36, 'gbk_rouge_bresil', 'Rouge Brésil', NULL, 'Jean-Christophe Rufin', 'Gallimard', '2001-01-01', 'Roman', 560, 4, NULL),
(37, 'gbk_ile_esclaves', 'L’Île des esclaves', NULL, 'Marivaux', 'Hachette', '1725-01-01', 'Théâtre', 96, 4, NULL),
(38, 'gbk_germinal', 'Germinal', NULL, 'Émile Zola', 'Le Livre de Poche', '1885-03-01', 'Roman', 592, 4, NULL),
(39, 'gbk_noir_rouge', 'Le Rouge et le Noir', NULL, 'Stendhal', 'Gallimard', '1830-11-13', 'Roman', 576, 4, NULL),
(40, 'gbk_princesse_cleves', 'La Princesse de Clèves', NULL, 'Madame de La Fayette', 'Flammarion', '1678-01-01', 'Roman', 352, 4, NULL),
(41, 'gbk_sac_billes', 'Un sac de billes', NULL, 'Joseph Joffo', 'Pocket', '1973-01-01', 'Roman', 384, 4, NULL),
(42, 'gbk_petit_prince', 'Le Petit Prince', NULL, 'Antoine de Saint-Exupéry', 'Gallimard', '1943-04-06', 'Conte', 96, 5, NULL),
(43, 'gbk_liste_kersten', 'La Liste de Kersten', NULL, 'François Kersaudy', 'Tallandier', '2016-01-01', 'Essai', 480, 4, NULL),
(44, 'gbk_princesse_cleves_dup', 'La Princesse de Clèves', NULL, 'Madame de La Fayette', 'Gallimard', '1678-01-01', 'Roman', 352, 4, NULL),
(45, 'gbk_guerre_metaux_rares', 'La Guerre des métaux rares', NULL, 'Guillaume Pitron', 'Les Liens qui Libèrent', '2018-01-11', 'Essai', 296, 4, NULL),
(46, 'gbk_trois_contes', 'Trois Contes', NULL, 'Gustave Flaubert', 'Flammarion', '1877-01-01', 'Nouvelles', 192, 4, NULL),
(47, 'gbk_guerre_celine', 'Guerre', NULL, 'Louis-Ferdinand Céline', 'Gallimard', '2022-05-05', 'Roman', 176, 4, NULL),
(48, 'gbk_trois_mousquetaires', 'Les Trois Mousquetaires', NULL, 'Alexandre Dumas', 'Le Livre de Poche', '1844-03-01', 'Roman', 704, 5, NULL),
(49, 'gbk_romeo_juliette', 'Roméo et Juliette', NULL, 'William Shakespeare', 'Folio', '1597-01-01', 'Théâtre', 224, 4, NULL),
(51, 'gbk_montecristo', 'Le Comte de Monte-Cristo', NULL, 'Alexandre Dumas', 'Gallimard', '1845-01-01', 'Roman', 1248, 5, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `favorites`
--

CREATE TABLE `favorites` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `book_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `external_id` (`external_id`),
  ADD UNIQUE KEY `uk_books_external_id` (`external_id`),
  ADD UNIQUE KEY `ix_books_external_id` (`external_id`),
  ADD KEY `idx_books_external_id` (`external_id`);

--
-- Indexes for table `favorites`
--
ALTER TABLE `favorites`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_user_book` (`user_id`,`book_id`),
  ADD KEY `idx_favorites_user` (`user_id`),
  ADD KEY `idx_favorites_book` (`book_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `uk_users_email` (`email`),
  ADD UNIQUE KEY `ix_users_email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `books`
--
ALTER TABLE `books`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- AUTO_INCREMENT for table `favorites`
--
ALTER TABLE `favorites`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `favorites`
--
ALTER TABLE `favorites`
  ADD CONSTRAINT `fk_favorites_book` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_favorites_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
