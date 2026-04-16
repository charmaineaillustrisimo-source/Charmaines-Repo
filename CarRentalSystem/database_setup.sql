/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  macbookairm1grey
 * Created: Apr 16, 2026
 */

-- Run this in phpMyAdmin to set up your team environment
CREATE DATABASE IF NOT EXISTS car_rental_db;
USE car_rental_db;

-- 1. Users Table (Handles authentication and user tiers)
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100),
    role ENUM('ADMIN', 'USER') DEFAULT 'USER',
    user_tier ENUM('FREE', 'PRO') DEFAULT 'FREE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Cars Table (Handles vehicle listings)
CREATE TABLE IF NOT EXISTS cars (
    car_id INT AUTO_INCREMENT PRIMARY KEY,
    owner_id INT NOT NULL,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    price_per_day DECIMAL(10, 2) NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'RENTED') DEFAULT 'PENDING',
    description TEXT,
    FOREIGN KEY (owner_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 3. Default Admin Account (Password: admin123)
-- Using INSERT IGNORE so it doesn't duplicate if run multiple times
INSERT IGNORE INTO users (username, password, email, full_name, role, user_tier) 
VALUES ('admin', 'admin123', 'admin@carrental.com', 'System Admin', 'ADMIN', 'PRO');
