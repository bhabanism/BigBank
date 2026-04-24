-- 1. Insert Customers
INSERT INTO Customers (CustomerID, FirstName, LastName, DateOfBirth, SSN, Address, City, State, ZipCode, Phone, Email, RegistrationDate) VALUES
(1, 'John', 'Doe', '1985-03-15', '123-45-6789', '789 Maple Street', 'New York', 'NY', '10001', '(212) 555-1001', 'john.doe@example.com', '2023-01-10'),
(2, 'Jane', 'Smith', '1990-07-22', '987-65-4321', '101 Oak Lane', 'New York', 'NY', '10002', '(212) 555-1002', 'jane.smith@example.com', '2023-02-15'),
(3, 'Robert', 'Johnson', '1978-11-05', '456-78-9012', '202 Pine Road', 'New York', 'NY', '10003', '(212) 555-1003', 'robert.j@example.com', '2023-03-20'),
(4, 'Emily', 'Davis', '1995-01-30', '321-65-4987', '303 Birch Blvd', 'New York', 'NY', '10004', '(212) 555-1004', 'emily.davis@example.com', '2024-01-05');

-- 2. Insert Login Information
INSERT INTO LoginInfo (LoginID, CustomerID, Username, PasswordHash, LastLogin, FailedAttempts, IsLocked, TwoFactorEnabled) VALUES
(1, 1, 'johndoe', '$2b$12$examplehash1234567890abcdef1234567890abcdef1234567890', '2026-04-10 14:30:00'::timestamptz, 0, FALSE, TRUE),
(2, 2, 'janesmith', '$2b$12$examplehash1234567890abcdef1234567890abcdef1234567890', '2026-04-11 09:15:00'::timestamptz, 0, FALSE, TRUE),
(3, 3, 'robertj', '$2b$12$examplehash1234567890abcdef1234567890abcdef1234567890', NULL, 2, FALSE, FALSE),
(4, 4, 'emilydavis', '$2b$12$examplehash1234567890abcdef1234567890abcdef1234567890', '2026-04-12 08:45:00'::timestamptz, 0, FALSE, TRUE);

-- 3. Insert Accounts
INSERT INTO Accounts (AccountID, AccountNumber, CustomerID, AccountType, Balance, OpenDate, Status) VALUES
(1, 'ACC10001', 1, 'Savings', 5250.75, '2023-01-15', 'Active'),
(2, 'ACC10002', 2, 'Checking', 1340.00, '2023-02-20', 'Closed'),
(3, 'ACC10003', 1, 'Checking', 15500.50, '2024-05-10', 'Frozen'),
(4, 'ACC20001', 3, 'Savings', 8750.25, '2023-06-01', 'Active'),
(5, 'ACC20002', 4, 'Money Market', 15200.00, '2024-01-10', 'Inactive');

-- 4. Insert Loans
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, TermMonths, StartDate, Status) VALUES
(1, 1, 25000.00, 5.75, 48, '2024-02-01', 'Active'),
(2, 3, 75000.00, 4.25, 60, '2023-07-15', 'Active');

-- 5. Insert Transactions
INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType, Description, ReferenceAccountID) VALUES
(1, 1, '2023-01-15 09:00:00', 5000.00, 'Deposit', 'Initial deposit', NULL),
(2, 1, '2026-03-15 10:30:00', 250.75, 'Deposit', 'Salary credit', NULL),
(3, 2, '2023-02-20 10:15:00', 3000.00, 'Deposit', 'Opening deposit', NULL),
(4, 2, '2026-04-05 11:45:00', 150.00, 'Withdrawal', 'Online transfer to merchant', NULL),
(5, 3, '2024-05-10 08:00:00', 15000.00, 'Deposit', 'Account opening deposit', NULL),
(6, 1, '2026-04-01 00:00:00', 125.50, 'Interest', 'Monthly interest', NULL),
(7, 2, '2026-04-10 16:20:00', 500.00, 'Transfer Out', 'Transfer to savings', 1),
(8, 1, '2026-04-10 16:20:00', 500.00, 'Transfer In', 'Received from checking', 2),
(9, 4, '2023-06-01 10:00:00', 8000.00, 'Deposit', 'Initial deposit', NULL),
(10, 5, '2024-01-10 12:00:00', 15200.00, 'Deposit', 'Opening deposit', NULL);

INSERT INTO roles (rolename)
SELECT v
FROM (VALUES
    ('ROLE_CUSTOMER'),
    ('ROLE_MANAGER'),
    ('ROLE_ADMIN')
) AS t (v)
WHERE NOT EXISTS (SELECT 1 FROM roles r WHERE r.rolename = t.v);

-- One time - Every existing login gets ROLE_CUSTOMER
INSERT INTO login_roles (loginid, roleid)
SELECT l.loginid, r.roleid
FROM logininfo l
JOIN roles r ON r.rolename = 'ROLE_CUSTOMER'
WHERE NOT EXISTS (
    SELECT 1 FROM login_roles x
    WHERE x.loginid = l.loginid AND x.roleid = r.roleid
);

INSERT INTO customers (
    firstname, lastname, dateofbirth, ssn, address, city, state, zipcode, phone, email, registrationdate
)
SELECT
    'System',
    'Administrator',
    DATE '1980-01-01',
    '000-00-0001',
    '1 Bank Plaza',
    'New York',
    'NY',
    '10001',
    '(212) 555-0000',
    'admin@bigbank.local',
    CURRENT_DATE
WHERE NOT EXISTS (
    SELECT 1 FROM customers c WHERE c.email = 'admin@bigbank.local'
);
INSERT INTO logininfo (
    customerid, username, passwordhash, lastlogin, failedattempts, islocked, twofactorenabled
)
SELECT
    c.customerid,
    'admin',
    '$2b$12$examplehash1234567890abcdef1234567890abcdef1234567890',  -- e.g. BCrypt of "ChangeMe!Admin123"
    NULL,
    0,
    FALSE,
    FALSE
FROM customers c
WHERE c.email = 'admin@bigbank.local'
  AND NOT EXISTS (SELECT 1 FROM logininfo l WHERE l.username = 'admin');
INSERT INTO login_roles (loginid, roleid)
SELECT l.loginid, r.roleid
FROM logininfo l
JOIN customers c ON c.customerid = l.customerid AND c.email = 'admin@bigbank.local'
JOIN roles r ON r.rolename = 'ROLE_ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM login_roles x WHERE x.loginid = l.loginid AND x.roleid = r.roleid
);
-- Optional: give admin ROLE_MANAGER as well (remove block if you do not want it)
INSERT INTO login_roles (loginid, roleid)
SELECT l.loginid, r.roleid
FROM logininfo l
JOIN customers c ON c.customerid = l.customerid AND c.email = 'admin@bigbank.local'
JOIN roles r ON r.rolename = 'ROLE_MANAGER'
WHERE NOT EXISTS (
    SELECT 1 FROM login_roles x WHERE x.loginid = l.loginid AND x.roleid = r.roleid
);