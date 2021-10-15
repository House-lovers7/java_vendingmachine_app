DROP TABLE IF EXISTS ITEM;
CREATE TABLE ITEM
(
    ITEM_CD      VARCHAR(5) NOT NULL,
    ITEM_NAME    VARCHAR(20) NOT NULL UNIQUE,
    SELL_FLG     VARCHAR(1) NOT NULL DEFAULT '0' COMMENT '0:販売停止中\n1:販売中',
PRIMARY KEY (ITEM_CD)
)
ENGINE=InnoDB
;
INSERT INTO ITEM (ITEM_CD, ITEM_NAME, SELL_FLG)
VALUES ('10001', 'Cola', '0'),
       ('10002', 'Sprite', '0'),
       ('10003', 'Orange', '0'),
       ('10004', 'Apple', '0'),
       ('10005', 'Osiruko', '0')
;

DROP TABLE IF EXISTS RACK;
CREATE TABLE RACK
(
    RACK_NO        VARCHAR(2) NOT NULL,
    ITEM_CD        VARCHAR(5),
    ITEM_NAME      VARCHAR(10),
    ITEM_NUMBER    INT NOT NULL DEFAULT 0,
    CAPACITY       INT NOT NULL DEFAULT 10,
    PRICE          INT NOT NULL DEFAULT 120,
PRIMARY KEY (RACK_NO)
)
ENGINE=InnoDB
;
INSERT INTO RACK (RACK_NO, ITEM_CD, ITEM_NUMBER, CAPACITY, PRICE)
VALUES ('01', '10001', 5, 10, 120),
       ('02', '10001', 5, 10, 120),
       ('03', '10002', 5, 10, 120),
       ('04', '10003', 5, 10, 120),
       ('05', '10004', 5, 10, 120)
;