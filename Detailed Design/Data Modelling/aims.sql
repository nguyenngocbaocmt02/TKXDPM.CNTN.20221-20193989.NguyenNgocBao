CREATE SCHEMA IF NOT EXISTS "AIMS";
USE "AIMS" ;

CREATE TABLE IF NOT EXISTS "AIMS"."Book" (
  "id" INT(12) NOT NULL,
  "author" VARCHAR(50) NOT NULL,
  "coverType" VARCHAR(50) NOT NULL,
  "publisher" VARCHAR(50) NOT NULL,
  "publishDate" DATETIME NOT NULL,
  "numberOfPage" INT(12) NOT NULL,
  "language" VARCHAR(50) NOT NULL,
  "bookCategory" VARCHAR(50) NOT NULL,
  PRIMARY KEY ("id"),
  CONSTRAINT "fk_Book_Media1"
    FOREIGN KEY ("id")
    REFERENCES "AIMS"."Media" ("id"))

CREATE TABLE IF NOT EXISTS "AIMS"."CD" (
  "id" INT(12) NOT NULL,
  "artist" VARCHAR(50) NOT NULL,
  "label" VARCHAR(50) NOT NULL,
  "type" VARCHAR(50) NOT NULL,
  "releasedDate" DATE NULL DEFAULT NULL,
  PRIMARY KEY ("id"),
  CONSTRAINT "fk_CD_Media1"
    FOREIGN KEY ("id")
    REFERENCES "AIMS"."Media" ("id"))


CREATE TABLE IF NOT EXISTS "AIMS"."DVD" (
  "id" INT(12) NOT NULL,
  "type" VARCHAR(50) NOT NULL,
  "director" VARCHAR(50) NOT NULL,
  "runtime" INT(12) NOT NULL,
  "publisher" VARCHAR(50) NOT NULL,
  "subtitle" VARCHAR(50) NOT NULL,
  "releasedDate" DATETIME NULL DEFAULT NULL,
  PRIMARY KEY ("id"),
  CONSTRAINT "fk_DVD_Media1"
    FOREIGN KEY ("id")
    REFERENCES "AIMS"."Media" ("id"))

CREATE TABLE IF NOT EXISTS "AIMS"."Media" (
  "id" INT(12) NOT NULL AUTO_INCREMENT,
  "category" VARCHAR(50) NOT NULL,
  "price" INT(12) NOT NULL,
  "quantity" INT(12) NOT NULL,
  "title" VARCHAR(50) NOT NULL,
  "value" INT(12) NOT NULL,
  "imageUrl" VARCHAR(50) NOT NULL,
  "DVD_id" INT(12) NOT NULL,
  "CD_id" INT(12) NOT NULL,
  "Book_id" INT(12) NOT NULL,
  PRIMARY KEY ("id", "DVD_id", "CD_id", "Book_id"),
  CONSTRAINT "fk_Media_DVD1"
    FOREIGN KEY ("DVD_id")
    REFERENCES "AIMS"."DVD" ("id"),
  CONSTRAINT "fk_Media_CD1"
    FOREIGN KEY ("CD_id")
    REFERENCES "AIMS"."CD" ("id"),
  CONSTRAINT "fk_Media_Book1"
    FOREIGN KEY ("Book_id")
    REFERENCES "AIMS"."Book" ("id"))

CREATE TABLE IF NOT EXISTS "AIMS"."PaymentTransaction" (
  "id" INT(12) NOT NULL,
  "date" DATETIME NOT NULL,
  "content" VARCHAR(50) NOT NULL,
  "method" VARCHAR(50) NULL DEFAULT NULL,
  "cardId" INT(12) NOT NULL,
  "invoiceId" INT(12) NOT NULL,
  "Card_id" INT(12) NOT NULL,
  PRIMARY KEY ("id", "cardId", "invoiceId", "Card_id"),
  CONSTRAINT "fk_PaymentTransaction_Invoice1"
    FOREIGN KEY ("invoiceId")
    REFERENCES "AIMS"."invoice" ("id"),


CREATE TABLE IF NOT EXISTS "AIMS"."Invoice" (
  "id" INT(12) NOT NULL,
  "totalPrice" INT(12) NOT NULL,
  "orderId" INT(12) NOT NULL,
  "PaymentTransaction_id" INT(12) NOT NULL,
  "PaymentTransaction_invoiceId" INT(12) NOT NULL,
  PRIMARY KEY ("id", "PaymentTransaction_id", "PaymentTransaction_invoiceId"),
  CONSTRAINT "fk_Invoice_Order1"
    FOREIGN KEY ("orderId")
    REFERENCES "AIMS"."Order" ("id"),

CREATE TABLE IF NOT EXISTS "AIMS"."Order" (
  "id" INT(12) NOT NULL,
  "shippingFees" VARCHAR(50) NULL DEFAULT NULL,
  "DeliveryInfoId" INT(12) NOT NULL,
  "Invoice_id" INT(12) NOT NULL,
  PRIMARY KEY ("id", "DeliveryInfoId"),
  INDEX "fk_Order_DeliveryInfo1_idx" ("DeliveryInfoId" ASC) VISIBLE,
  INDEX "fk_Order_Invoice1_idx" ("Invoice_id" ASC) VISIBLE,
  CONSTRAINT "fk_Order_DeliveryInfo1"
    FOREIGN KEY ("DeliveryInfoId")
    REFERENCES "AIMS"."Deliveryinfo" ("id"),
  CONSTRAINT "fk_Order_Invoice1"
    FOREIGN KEY ("Invoice_id")
    REFERENCES "AIMS"."Invoice" ("id"))

CREATE TABLE IF NOT EXISTS "AIMS"."DeliveryInfo" (
  "id" INT(12) NOT NULL AUTO_INCREMENT,
  "name" VARCHAR(50) NULL DEFAULT NULL,
  "province" VARCHAR(50) NULL DEFAULT NULL,
  "instructions" VARCHAR(200) NULL DEFAULT NULL,
  "address" VARCHAR(100) NULL DEFAULT NULL,
  "Order_id" INT(12) NOT NULL,
  "Order_DeliveryInfoId" INT(12) NOT NULL,
  PRIMARY KEY ("id", "Order_id", "Order_DeliveryInfoId"),
  CONSTRAINT "fk_DeliveryInfo_Order1"
    FOREIGN KEY ("Order_id" , "Order_DeliveryInfoId")
    REFERENCES "AIMS"."Order" ("id" , "DeliveryInfoId"))


CREATE TABLE IF NOT EXISTS "AIMS"."OrderMedia" (
  "orderID" INT(12) NOT NULL,
  "price" INT(12) NOT NULL,
  "quantity" INT(12) NOT NULL,
  "mediaId" INT(12) NOT NULL,
  "Invoice_id" INT(12) NOT NULL,
  "Media_id" INT(12) NOT NULL,
  PRIMARY KEY ("orderID", "mediaId", "Invoice_id", "Media_id"),
  CONSTRAINT "fk_OrderMedia_Media1"
    FOREIGN KEY ("mediaId")
    REFERENCES "AIMS"."Media" ("id"),
  CONSTRAINT "fk_ordermedia_order"
    FOREIGN KEY ("orderID")
    REFERENCES "AIMS"."Order" ("id"),
  CONSTRAINT "fk_OrderMedia_Invoice1"
    FOREIGN KEY ("Invoice_id")
    REFERENCES "AIMS"."Invoice" ("id"),
  CONSTRAINT "fk_OrderMedia_Media2"
    FOREIGN KEY ("Media_id")
    REFERENCES "AIMS"."Media" ("id"))
