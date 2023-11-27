create database VCRTS;

use VCRTS;

CREATE TABLE Vehicle (
	vehicleOwner VARCHAR(100),
    vehicleMake VARCHAR(100),
    vehicleModel VARCHAR(100),
    vehiclePlate VARCHAR(100),
    residencyTime INTEGER(100),
    timeStamp DATETIME
);
    
CREATE TABLE Job	(
	jobOwner VARCHAR(100),
    jobTitle VARCHAR(100),
    jobDescription VARCHAR(250),
    jobDeadline VARCHAR(20),
    jobDuration INTEGER(100),
    timeStamp DATETIME
);
    
    
    
    
    
