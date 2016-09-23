/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     6/21/2013 10:04:57 PM                        */
/*==============================================================*/


drop table if exists product_instance_test_result;

drop table if exists fda_test_plan;

drop table if exists fda_statement;

drop table if exists product_instance;

drop table if exists product;

drop table if exists business_brand;

drop table if exists business_unit;

drop table if exists business_category;

drop table if exists business_type;

/*==============================================================*/
/* Table: business_type                                         */
/*==============================================================*/
create table business_type
(
   id                   bigint not null auto_increment,
   name                 varchar(200),
   primary key (id)
);

/*==============================================================*/
/* Table: business_category                                     */
/*==============================================================*/
create table business_category
(
   id                   bigint not null auto_increment,
   name                 varchar(200),
   primary key (id)
);

/*==============================================================*/
/* Table: business_unit                                         */
/*==============================================================*/
create table business_unit
(
   id                   bigint not null auto_increment,
   name                 varchar(200),
   address              varchar(200),
   address2             varchar(200),
   logo                 varchar(50),
   website              varchar(50),
   type_id              bigint,
   category_id          bigint,
   primary key (id),
   constraint fk_business_unit_business_type foreign key (type_id)
      references business_type (id),
   constraint fk_business_unit_business_category foreign key (category_id)
      references business_category (id)
);

/*==============================================================*/
/* Table: business_brand                                        */
/*==============================================================*/
create table business_brand
(
   id                   bigint not null auto_increment,
   name                 varchar(200),
   identity             varchar(200),
   symbol               varchar(200),
   logo                 varchar(50),
   trademark            varchar(10),
   cobrand              bit,
   registration_date    varchar(20),
   business_unit_id     bigint,
   primary key (id),
   constraint fk_business_brand_business_unit foreign key (business_unit_id)
      references business_unit (id)
);

/*==============================================================*/
/* Index: idx_name                                              */
/*==============================================================*/
create unique index idx_name on business_brand
(
   name
);

/*==============================================================*/
/* Index: idx_identity                                          */
/*==============================================================*/
create unique index idx_identity on business_brand
(
   identity
);

/*==============================================================*/
/* Index: idx_name                                              */
/*==============================================================*/
create unique index idx_name on business_unit
(
   name
);

/*==============================================================*/
/* Table: product                                               */
/*==============================================================*/
create table product
(
   id                   bigint not null auto_increment,
   name                 varchar(200),
   status               varchar(50),
   format               varchar(200),
   regularity           varchar(200),
   barcode              varchar(50),
   note                 text,
   business_brand_id    bigint,
   primary key (id),
   constraint fk_product_business_brand foreign key (business_brand_id)
      references business_brand (id) on delete restrict on update restrict
);

/*==============================================================*/
/* Table: product_instance                                      */
/*==============================================================*/
create table product_instance
(
   id                   bigint not null auto_increment,
   batch_serial_no      varchar(50),
   serial               varchar(50),
   production_date      varchar(20),
   expiration_date       varchar(20),
   product_id           bigint,
   original_id          bigint,
   primary key (id),
   constraint fk_product_instance_product_instance foreign key (original_id)
      references product_instance (id),
   constraint fk_product_instance_product foreign key (product_id)
      references product (id)
);

/*==============================================================*/
/* Table: fda_statement                                         */
/*==============================================================*/
create table fda_statement
(
   id                   bigint not null auto_increment,
   date                 date,
   content              varchar(2000),
   product_instance_id  bigint,
   primary key (id),
   constraint fk_fda_statement_product_instance foreign key (product_instance_id)
      references product_instance (id)
);

/*==============================================================*/
/* Table: fda_test_plan                                         */
/*==============================================================*/
create table fda_test_plan
(
   id                   bigint not null auto_increment,
   name                 varchar(200),
   content              varchar(2000),
   product_instance_id  bigint,
   primary key (id),
   constraint fk_fda_test_plan_product_instance foreign key (id)
      references product_instance (id)
);

/*==============================================================*/
/* Index: idx_name                                              */
/*==============================================================*/
create index idx_name on product
(
   name
);

/*==============================================================*/
/* Index: idx_barcode                                           */
/*==============================================================*/
create unique index idx_barcode on product
(
   barcode
);

/*==============================================================*/
/* Index: idx_batch_serial_no_serial_product_id                 */
/*==============================================================*/
create unique index idx_batch_serial_no_serial_product_id on product_instance
(
   batch_serial_no,
   serial,
   product_id
);

/*==============================================================*/
/* Table: product_instance_test_result                          */
/*==============================================================*/
create table product_instance_test_result
(
   id                   bigint not null auto_increment,
   content              varchar(2000),
   product_instance_id  bigint,
   fda_test_plan_id     bigint,
   primary key (id),
   constraint fk_product_instance_test_result_fda_test_plan foreign key (fda_test_plan_id)
      references fda_test_plan (id),
   constraint fk_product_instance_test_result_product_instance foreign key (product_instance_id)
      references product_instance (id)
);

