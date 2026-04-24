-- Drop existing tables in reverse dependency order
DROP TABLE IF EXISTS Transactions CASCADE;
DROP TABLE IF EXISTS Accounts CASCADE;
DROP TABLE IF EXISTS Loans CASCADE;
DROP TABLE IF EXISTS Customers CASCADE;
DROP TABLE IF EXISTS login_roles CASCADE;
DROP TABLE IF EXISTS LoginInfo CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

-- Customers Table
CREATE TABLE Customers (
    CustomerID SERIAL PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    DateOfBirth DATE,
    SSN VARCHAR(11) UNIQUE,
    Address TEXT,
    City VARCHAR(100),
    State VARCHAR(50),
    ZipCode VARCHAR(20),
    Phone VARCHAR(15),
    Email VARCHAR(100) UNIQUE NOT NULL,
    RegistrationDate DATE DEFAULT CURRENT_DATE
);

-- Login Information (Authentication)
CREATE TABLE LoginInfo (
    LoginID SERIAL PRIMARY KEY,
    CustomerID INT UNIQUE NOT NULL,
    Username VARCHAR(50) UNIQUE NOT NULL,
    PasswordHash VARCHAR(255) NOT NULL,
    LastLogin TIMESTAMPTZ,
    FailedAttempts INT DEFAULT 0,
    IsLocked BOOLEAN DEFAULT FALSE,
    TwoFactorEnabled BOOLEAN DEFAULT FALSE,
    CreatedAt TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID) ON DELETE CASCADE
);

-- Accounts Table (One account belongs to exactly one customer)
CREATE TABLE Accounts (
    AccountID SERIAL PRIMARY KEY,
    AccountNumber VARCHAR(20) UNIQUE NOT NULL,
    CustomerID INT NOT NULL,
    AccountType TEXT NOT NULL 
        CHECK (AccountType IN ('Savings', 'Checking', 'Money Market', 'Certificate of Deposit')),
    Balance DECIMAL(15, 2) DEFAULT 0.00 CHECK (Balance >= 0),
    OpenDate DATE NOT NULL,
    Status TEXT DEFAULT 'Active' 
        CHECK (Status IN ('Active', 'Inactive', 'Closed', 'Frozen')),
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID) ON DELETE CASCADE
);

-- Transactions Table
CREATE TABLE Transactions (
    TransactionID SERIAL PRIMARY KEY,
    AccountID INT NOT NULL,
    TransactionDate TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    Amount DECIMAL(15, 2) NOT NULL,
    TransactionType TEXT NOT NULL 
        CHECK (TransactionType IN ('Deposit', 'Withdrawal', 'Transfer Out', 'Transfer In', 'Fee', 'Interest')),
    Description VARCHAR(255),
    ReferenceAccountID INT NULL,
    FOREIGN KEY (AccountID) REFERENCES Accounts(AccountID) ON DELETE RESTRICT,
    FOREIGN KEY (ReferenceAccountID) REFERENCES Accounts(AccountID) ON DELETE SET NULL
);

-- Loans Table
CREATE TABLE Loans (
    LoanID SERIAL PRIMARY KEY,
    CustomerID INT NOT NULL,
    LoanAmount DECIMAL(15, 2) NOT NULL,
    InterestRate DECIMAL(5,2) NOT NULL,
    TermMonths INT NOT NULL,
    StartDate DATE NOT NULL,
    Status TEXT DEFAULT 'Active' 
        CHECK (Status IN ('Pending', 'Active', 'Paid Off', 'Defaulted')),
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS roles (
    roleid   SERIAL PRIMARY KEY,
    rolename VARCHAR(64) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS login_roles (
    loginid     INT NOT NULL REFERENCES logininfo (loginid) ON DELETE CASCADE,
    roleid      INT NOT NULL REFERENCES roles (roleid) ON DELETE CASCADE,
    assigned_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (loginid, roleid)
);
CREATE INDEX IF NOT EXISTS idx_login_roles_loginid ON login_roles (loginid);