/* userやgroupといった名前はSQLでは予約語で使えないため，userNameとしていることに注意 */
CREATE TABLE users (
    id IDENTITY,
    name VARCHAR NOT NULL
);
CREATE TABLE matches (
    id IDENTITY NOT NULL PRIMARY KEY,
    user1 INT,
    user2 INT,
    user1Hand VARCHAR NOT NULL,
    user2Hand VARCHAR NOT NULL
);
