-- Create the sport table for storing data from Rakuten API
CREATE TABLE IF NOT EXISTS sport (
    id IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);