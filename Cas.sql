create database CarRen;
use CarRen;

CREATE TABLE Vehicle (
    vehicleID INT PRIMARY KEY auto_increment,
    make VARCHAR(255),
    model VARCHAR(255),
    year INT,
    dailyRate DECIMAL(10, 2),
    status VARCHAR(15) CHECK (status IN ('available', 'notAvailable')),
    passengerCapacity INT,
    engineCapacity INT
);

-- Customer Table
CREATE TABLE Customer (
    customerID INT PRIMARY KEY auto_increment,
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    email VARCHAR(255),
    phoneNumber VARCHAR(20)
);

-- Lease Table
CREATE TABLE Lease (
    leaseID INT PRIMARY KEY auto_increment,
    vehicleID INT,
    customerID INT,
    startDate DATE,
    endDate DATE,
    type VARCHAR(15) CHECK (type IN ('DailyLease', 'MonthlyLease')),
    FOREIGN KEY (vehicleID) REFERENCES Vehicle(vehicleID),
    FOREIGN KEY (customerID) REFERENCES Customer(customerID)
);

-- Payment Table
CREATE TABLE Payment (
    paymentID INT PRIMARY KEY auto_increment,
    leaseID INT,
    paymentDate DATE,
    amount DECIMAL(10, 2),
    FOREIGN KEY (leaseID) REFERENCES Lease(leaseID)
);

show tables;

select * from customer;
select * from vehicle;
select * from lease;

CREATE TABLE LeaseTypeVehicleMapping (
    leaseType VARCHAR(50) PRIMARY KEY,
    vehicleID INT
);
INSERT INTO LeaseTypeVehicleMapping (leaseType, vehicleID) VALUES
('daily', 1),
('monthly', 2);

select * from vehicle;

select * from lease;

ALTER TABLE vehicle
ADD monthlyRate DECIMAL(10, 2);

select * from payment;