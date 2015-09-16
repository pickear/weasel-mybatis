/*
Navicat PGSQL Data Transfer

Source Server         : postgresql
Source Server Version : 90404
Source Host           : 192.168.3.133:5432
Source Database       : test
Source Schema         : public

Target Server Type    : PGSQL
Target Server Version : 90404
File Encoding         : 65001

Date: 2015-09-16 17:20:18
*/


-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS "public"."users";
CREATE TABLE "public"."users" (
"username" varchar(20) COLLATE "default" NOT NULL,
"password" varchar(20) COLLATE "default" NOT NULL,
"id" int4 NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table users
-- ----------------------------
ALTER TABLE "public"."users" ADD PRIMARY KEY ("id");
