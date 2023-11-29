CREATE DATABASE VCRTS;

USE VCRTS;

CREATE TABLE Vehicle (
  vehicleOwner VARCHAR(100),
  vehicleMake VARCHAR(100),
  vehicleModel VARCHAR(100),
  vehiclePlate VARCHAR(100) UNIQUE,
  residencyTime INT,
  arrivalTime DATETIME
);

CREATE TABLE Job (
  jobOwner VARCHAR(100),
  jobTitle VARCHAR(100),
  jobDescription VARCHAR(250),
  jobDeadline VARCHAR(20),
  jobDuration INT,
  completed VARCHAR(5),
  dateRequested DATETIME
);

CREATE TABLE User (
  username VARCHAR(100) UNIQUE,
  userPassword VARCHAR(100),
  isOwner VARCHAR(5) NOT NULL DEFAULT 'false',
  isClient VARCHAR(5) NOT NULL DEFAULT 'false',
  dateCreated DATETIME,
  lastSignIn DATETIME
);