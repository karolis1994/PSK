CREATE TABLE IF NOT EXISTS Principals(
  ID SERIAL PRIMARY KEY NOT NULL,
  Email VARCHAR (255) NOT NULL UNIQUE,
  Points INT NULL,
  FirstName VARCHAR (255) NOT NULL,
  LastName VARCHAR (255) NOT NULL,
  PasswordHash VARCHAR(255) NOT NULL,
  GroupNo INT NULL,
  IsAdmin BOOLEAN NULL,
  IsApproved BOOLEAN NULL,
  IsDeleted BOOLEAN NULL
);
CREATE TABLE IF NOT EXISTS Settings(
  ID SERIAL PRIMARY KEY NOT NULL,
  SettingName VARCHAR (255) NULL,
  SettingValue VARCHAR (255) NULL
);
CREATE TABLE IF NOT EXISTS Salts(
  ID SERIAL PRIMARY KEY NOT NULL,
  Salt INT NOT NULL
);
CREATE TABLE IF NOT EXISTS Invitations(
  RecieverID INT NULL REFERENCES Principals(ID),
  SenderID INT NOT NULL REFERENCES Principals(ID),
  PRIMARY KEY(RecieverID, SenderID),
  URLCode VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS Houses(
  ID SERIAL PRIMARY KEY NOT NULL,
  Title VARCHAR(255),
  Description VARCHAR(255),
  Address VARCHAR (255),
  IsClosed BOOLEAN,
  IsDeleted BOOLEAN
);
CREATE TABLE IF NOT EXISTS Extras(
  ID SERIAL PRIMARY KEY NOT NULL,
  HouseID INT REFERENCES Houses(ID),
  Title VARCHAR(255),
  Description VARCHAR(255),
  IsDeleted BOOLEAN
);
CREATE TABLE IF NOT EXISTS PaidServices(
  ID SERIAL PRIMARY KEY NOT NULL,
  HouseID INT NULL REFERENCES Houses(ID),
  ExtrasID INT NULL REFERENCES Extras(ID),
  Cost FLOAT NOT NULL,
  CHECK (HouseID IS NOT NULL OR ExtrasID IS NOT NULL)
);
CREATE TABLE IF NOT EXISTS Payments(
  ID SERIAL PRIMARY KEY NOT NULL,
  PaymentNo INT UNIQUE,
  PrincipalID INT NOT NULL REFERENCES Principals(ID),
  PaidServiceID INT NOT NULL REFERENCES PaidServices(ID),
  CreatedAt TIMESTAMP WITH TIME ZONE,
  PayedAt TIMESTAMP WITH TIME ZONE,
  Ammount FLOAT NOT NULL,
  IsPaid BOOLEAN
);
CREATE TABLE IF NOT EXISTS Reservations(
  ID SERIAL PRIMARY KEY NOT NULL,
  PrincipalID INT NOT NULL REFERENCES Principals(ID),
  HouseID INT NOT NULL REFERENCES Houses(ID),
  ReservedFrom TIMESTAMP WITH TIME ZONE NOT NULL,
  ReservedTo TIMESTAMP WITH TIME ZONE NOT NULL,
  IsCanceled BOOLEAN
);