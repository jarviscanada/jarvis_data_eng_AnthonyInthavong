CREATE SEQUENCE hp_product_seq start with 10000;

CREATE TABLE product (
  product_id bigint NOT NULL DEFAULT nextval('hp_product_seq'),
  code varchar(50) DEFAULT NULL,
  name varchar(50) DEFAULT NULL,
  size int DEFAULT NULL,
  variety varchar(50) DEFAULT NULL,
  price numeric(10,2) DEFAULT NULL,
  status varchar(50) DEFAULT NULL,
  PRIMARY KEY (product_id)
);

ALTER SEQUENCE hp_product_seq OWNED BY product.product_id;

INSERT INTO product (product_id, code, name, size, variety, price, status)
VALUES
(1,'MWBLU20','Mineral Water',20,'Blueberry',1.79,'ACTIVE'),
(2,'MWBLU32','Mineral Water',32,'Blueberry',3.69,'ACTIVE'),
(3,'MWCRA20','Mineral Water',20,'Cranberry',1.79,'DISCONTINUED'),
(4,'MWCRA32','Mineral Water',32,'Cranberry',3.69,'DISCONTINUED'),
(5,'MWLEM20','Mineral Water',20,'Lemon-Lime',1.79,'ACTIVE'),
(6,'MWLEM32','Mineral Water',32,'Lemon-Lime',3.69,'ACTIVE'),
(7,'MWMAN20','Mineral Water',20,'Mango',1.79,'DISCONTINUED'),
(8,'MWMAN32','Mineral Water',32,'Mango',3.69,'DISCONTINUED'),
(9,'MWORG20','Mineral Water',20,'Orange',1.79,'ACTIVE'),
(10,'MWORG32','Mineral Water',32,'Orange',3.69,'ACTIVE'),
(11,'MWPEA20','Mineral Water',20,'Peach',1.79,'ACTIVE'),
(12,'MWPEA32','Mineral Water',32,'Peach',3.69,'ACTIVE'),
(13,'MWRAS20','Mineral Water',20,'Raspberry',1.79,'ACTIVE'),
(14,'MWRAS32','Mineral Water',32,'Raspberry',3.69,'ACTIVE'),
(15,'MWSTR20','Mineral Water',20,'Strawberry',1.79,'ACTIVE'),
(16,'MWSTR32','Mineral Water',32,'Strawberry',3.69,'ACTIVE');
