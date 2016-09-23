/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     7/6/2013 3:21:42 PM                          */
/*==============================================================*/

drop table if exists product_instance_test_result;

drop table if exists test_property;

drop table if exists test_property_category;

drop table if exists test_lab_test;

drop table if exists test_lab;

drop table if exists fda_test;

drop table if exists fda_test_plan;

drop table if exists fda_statement;

drop table if exists fda;

drop table if exists business_unit_test;

drop table if exists test_result;

drop table if exists product_instance;

drop table if exists product;

drop table if exists fda_product_group;

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
   code                 varchar(20),
   name                 varchar(200),
   primary key (id)
);

/*==============================================================*/
/* Index: idx_code                                              */
/*==============================================================*/
create unique index idx_code on business_type
(
   code
);

/*==============================================================*/
/* Table: business_category                                     */
/*==============================================================*/
create table business_category
(
   id                   bigint not null auto_increment,
   code                 varchar(20),
   name                 varchar(200),
   primary key (id)
);

/*==============================================================*/
/* Index: idx_code                                              */
/*==============================================================*/
create unique index idx_code on business_category
(
   code
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
   type                 varchar(20),
   category             varchar(20),
   primary key (id),
   constraint fk_business_unit_business_type foreign key (type)
      references business_type (code),
   constraint fk_business_unit_business_category foreign key (category)
      references business_category (code)
);

/*==============================================================*/
/* Index: idx_name                                              */
/*==============================================================*/
create unique index idx_name on business_unit
(
   name
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
/* Table: fda_product_group                                     */
/*==============================================================*/
create table fda_product_group
(
   id                   bigint not null auto_increment,
   code                 varchar(20),
   name                 varchar(50),
   primary key (id)
);


/*==============================================================*/
/* Index: idx_code                                              */
/*==============================================================*/
create unique index idx_code on fda_product_group
(
   code
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
   fda_product_group    varchar(20),
   primary key (id),
   constraint fk_product_business_brand foreign key (business_brand_id)
      references business_brand (id) on delete restrict on update restrict,
   constraint fk_product_fda_product_group foreign key (fda_product_group)
      references fda_product_group (code) on delete restrict on update restrict
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
/* Table: product_instance                                      */
/*==============================================================*/
create table product_instance
(
   id                   bigint not null auto_increment,
   batch_serial_no      varchar(50),
   serial               varchar(50),
   production_date      datetime,
   expiration_date      datetime,
   product_id           bigint,
   original_id          bigint,
   primary key (id),
   constraint fk_product_instance_product_instance foreign key (original_id)
      references product_instance (id),
   constraint fk_product_instance_product foreign key (product_id)
      references product (id)
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
/* Table: test_result                                           */
/*==============================================================*/
create table test_result
(
   id                   bigint not null auto_increment,
   product_instance_id  bigint,
   comment              varchar(2000),
   pass                 bit,
   test_date            datetime,
   primary key (id),
   constraint fk_test_result_product_instance foreign key (product_instance_id)
      references product_instance (id)
);

/*==============================================================*/
/* Table: business_unit_test                                    */
/*==============================================================*/
create table business_unit_test
(
   id                   bigint not null auto_increment,
   business_unit_id     bigint,
   test_result_id       bigint,
   primary key (id),
   constraint fk_business_unit_test_business_unit foreign key (business_unit_id)
      references business_unit (id) on delete restrict on update restrict,
   constraint fk_business_unit_test_test_result foreign key (test_result_id)
      references test_result (id) on delete restrict on update restrict
);

/*==============================================================*/
/* Table: fda                                                   */
/*==============================================================*/
create table fda
(
   id                   bigint not null auto_increment,
   name                 varchar(200),
   address              varchar(200),
   supervisor           bigint,
   primary key (id),
   constraint fk_fda_fda foreign key (supervisor)
      references fda (id) on delete restrict on update restrict
);

/*==============================================================*/
/* Table: fda_statement                                         */
/*==============================================================*/
create table fda_statement
(
   id                   bigint not null auto_increment,
   publish_date         datetime,
   content              varchar(2000),
   product_instance_id  bigint,
   fda_id               bigint,
   primary key (id),
   constraint fk_fda_statement_product_instance foreign key (product_instance_id)
      references product_instance (id),
   constraint fk_fda_statement_fda foreign key (fda_id)
      references fda (id) on delete restrict on update restrict
);

/*==============================================================*/
/* Table: fda_test_plan                                         */
/*==============================================================*/
create table fda_test_plan
(
   id                   bigint not null auto_increment,
   comment              varchar(2000),
   start_date           date,
   end_date             date,
   product_group        varchar(20),
   fda_id               bigint,
   primary key (id),
   constraint fk_fda_test_plan_fda foreign key (fda_id)
      references fda (id) on delete restrict on update restrict
);

/*==============================================================*/
/* Table: fda_test                                              */
/*==============================================================*/
create table fda_test
(
   id                   bigint not null auto_increment,
   fda_id               bigint,
   test_result_id       bigint,
   fda_test_plan_id     bigint,
   primary key (id),
   constraint fk_fda_test_fda foreign key (fda_id)
      references fda (id) on delete restrict on update restrict,
   constraint fk_fda_test_test_result foreign key (test_result_id)
      references test_result (id) on delete restrict on update restrict,
   constraint fk_fda_test_fda_test_plan foreign key (fda_test_plan_id)
      references fda_test_plan (id) on delete restrict on update restrict
);

/*==============================================================*/
/* Table: test_lab                                              */
/*==============================================================*/
create table test_lab
(
   id                   bigint not null auto_increment,
   name                 varchar(200),
   address              varchar(200),
   primary key (id)
);

/*==============================================================*/
/* Table: test_lab_test                                         */
/*==============================================================*/
create table test_lab_test
(
   id                   bigint not null auto_increment,
   test_lab_id          bigint,
   test_result_id       bigint,
   primary key (id),
   constraint fk_test_lab_test_test_lab foreign key (test_lab_id)
      references test_lab (id) on delete restrict on update restrict,
   constraint fk_test_lab_test_result foreign key (test_result_id)
      references test_result (id) on delete restrict on update restrict
);

/*==============================================================*/
/* Table: test_property_category                                */
/*==============================================================*/
create table test_property_category
(
   id                   bigint not null auto_increment,
   code                 varchar(20),
   name                 varchar(50),
   primary key (id)
);

/*==============================================================*/
/* Index: idx_code                                              */
/*==============================================================*/
create unique index idx_code on test_property_category
(
   code
);

/*==============================================================*/
/* Table: test_property                                         */
/*==============================================================*/
create table test_property
(
   id                   bigint not null auto_increment,
   test_result_id       bigint,
   categroy             varchar(20),
   name                 varchar(200),
   value                varchar(50),
   primary key (id),
   constraint fk_test_property_test_result foreign key (test_result_id)
      references test_result (id) on delete restrict on update restrict,
   constraint fk_test_property_test_property_category foreign key (categroy)
      references test_property_category (code) on delete restrict on update restrict
);

