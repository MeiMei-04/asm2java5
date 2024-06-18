-- Tạo cơ sở dữ liệu QLKH
CREATE DATABASE QLKH;
GO

-- Sử dụng cơ sở dữ liệu QLKH
USE QLKH;
GO

-- Tạo bảng user
CREATE TABLE [User] (
  id INT PRIMARY KEY IDENTITY(1,1),
  name VARCHAR(50) NOT NULL,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL
);
GO

-- Tạo bảng Customer
CREATE TABLE Customer (
  id INT PRIMARY KEY IDENTITY(1,1),
  name VARCHAR(50) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL
);
GO
-- Nhập dữ liệu vào bảng user
INSERT INTO [User] (name, username, password)
VALUES
  ('John Doe', 'johndoe', 'password123'),
  ('Jane Smith', 'janesmith', 'secret456'),
  ('Bob Johnson', 'bobjohnson', 'mypassword');
GO

-- Nhập dữ liệu vào bảng Customer
INSERT INTO Customer (name, email)
VALUES
  ('Alice Brown', 'alice.brown@example.com'),
  ('David Lee', 'david.lee@example.com'),
  ('Sarah Davis', 'sarah.davis@example.com'),
  ('Michael Chen', 'michael.chen@example.com');
GO
-- Create History table
CREATE TABLE History (
  id INT PRIMARY KEY IDENTITY(1,1),
  timestamp DATETIME DEFAULT GETDATE(),
  ipadress VARCHAR(100),
  Operation VARCHAR(100),
  [user] VARCHAR(100),
  Description TEXT
);
