CREATE TABLE IF NOT EXISTS Pictures(
  ID SERIAL PRIMARY KEY NOT NULL,
  ImageName VARCHAR(100) NOT NULL,
  Image BYTEA NOT NULL
);

CREATE TABLE IF NOT EXISTS Principals(
  ID SERIAL PRIMARY KEY NOT NULL,
  Email VARCHAR (255) NOT NULL UNIQUE,
  Points INT NULL,
  Firstname VARCHAR (255) NOT NULL,
  Lastname VARCHAR (255) NOT NULL,
  Address VARCHAR (255) NOT NULL,
  PhoneNumber VARCHAR (255) NOT NULL,
  Birthdate DATE NOT NULL,
  Picture INT NULL REFERENCES Pictures(ID),
  About VARCHAR (255) NULL,
  PasswordHash VARCHAR(255) NULL,
  Salt VARCHAR(255) NULL,
  GroupNo INT NULL,
  IsAdmin BOOLEAN NULL,
  IsApproved BOOLEAN NULL,
  IsDeleted BOOLEAN NULL,
  MembershipUntill DATE NULL,
  FacebookID VARCHAR(128) NULL UNIQUE,
  Version SERIAL
);
CREATE TABLE IF NOT EXISTS Settings(
  ID SERIAL PRIMARY KEY NOT NULL,
  SettingName VARCHAR (255) NULL,
  SettingValue VARCHAR (255) NULL,
  Version SERIAL
);
CREATE TABLE IF NOT EXISTS Invitations(
  ID SERIAL PRIMARY KEY NOT NULL,
  SenderID INT NOT NULL REFERENCES Principals(ID),
  URLCode VARCHAR(255) NOT NULL,
  IsActivated BOOLEAN NULL,
  Version SERIAL
);
CREATE TABLE IF NOT EXISTS Recommendations(
  ID SERIAL PRIMARY KEY NOT NULL,
  RecieverID INT NOT NULL REFERENCES Principals(ID),
  SenderID INT NOT NULL REFERENCES Principals(ID), 
  URLCode VARCHAR(255) NOT NULL,
  IsActivated BOOLEAN NULL,
  Version SERIAL
);
CREATE TABLE IF NOT EXISTS Houses(
  ID SERIAL PRIMARY KEY NOT NULL,
  Title VARCHAR(255),
  Description VARCHAR(255),
  Address VARCHAR (255),
  IsClosed BOOLEAN,
  IsDeleted BOOLEAN,
  Version SERIAL,
  Capacity INT,
  AvailableFrom INT NOT NULL,
  AvailableTo INT NOT NULL,
  FaceBookImageURL VARCHAR (255)
);
CREATE TABLE IF NOT EXISTS Extras(
  ID SERIAL PRIMARY KEY NOT NULL,
  HouseID INT REFERENCES Houses(ID),
  Title VARCHAR(255),
  Description VARCHAR(255),
  IsDeleted BOOLEAN,
  Version SERIAL
);
CREATE TABLE IF NOT EXISTS OtherServices(
  ID SERIAL PRIMARY KEY NOT NULL,
  Title VARCHAR(255),
  Description VARCHAR(255),
  IsDeleted BOOLEAN,
  Version SERIAL
);
CREATE TABLE IF NOT EXISTS PaidServices(
  ID SERIAL PRIMARY KEY NOT NULL,
  HouseID INT NULL REFERENCES Houses(ID),
  ExtrasID INT NULL REFERENCES Extras(ID),
  OtherServiceID INT NULL REFERENCES OtherServices(ID),
  Cost FLOAT NOT NULL,
  CostInPoints INT NOT NULL,
  Version SERIAL,
  CHECK (HouseID IS NOT NULL OR ExtrasID IS NOT NULL OR OtherServiceID IS NOT NULL)
);
CREATE TABLE IF NOT EXISTS Payments(
  ID SERIAL PRIMARY KEY NOT NULL,
  PaymentNo VARCHAR(128) UNIQUE,
  PrincipalID INT NOT NULL REFERENCES Principals(ID),
  PaidServiceID INT NOT NULL REFERENCES PaidServices(ID),
  CreatedAt TIMESTAMP WITH TIME ZONE,
  PayedAt TIMESTAMP WITH TIME ZONE,
  Ammount FLOAT NOT NULL,
  IsPaid BOOLEAN,
  PaidWithPoints BOOLEAN,
  BoughtItems INT NULL,
  Version SERIAL
);
CREATE TABLE IF NOT EXISTS Reservations(
  ID SERIAL PRIMARY KEY NOT NULL,
  PrincipalID INT NOT NULL REFERENCES Principals(ID),
  HouseID INT REFERENCES Houses(ID),
  ExtraID INT REFERENCES Extras(ID),
  PaymentID INT NOT NULL REFERENCES Payments(ID),
  ReservedFrom TIMESTAMP WITH TIME ZONE NOT NULL,
  ReservedTo TIMESTAMP WITH TIME ZONE NOT NULL,
  IsCanceled BOOLEAN,
  Version SERIAL
);
CREATE TABLE IF NOT EXISTS ReservationExtras(
  ID SERIAL PRIMARY KEY NOT NULL,
  ReservationID INT NOT NULL REFERENCES Reservations(ID),
  ExtraID INT NOT NULL REFERENCES Extras(ID),
  Version SERIAL
);
