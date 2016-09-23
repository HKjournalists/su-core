/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     7/2/2013 10:02:41 PM                         */
/*==============================================================*/

drop table if exists product_instance_test_result;

drop table if exists fda_statement;

drop table if exists fda_test_plan;

drop table if exists test_lab;

drop table if exists fda;

/*==============================================================*/
/* Table: fda                                                   */
/*==============================================================*/
create table fda
(
   id                   bigint not null auto_increment,
   name                 varchar(200),
   address             varchar(200),
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
   date                 date,
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
   name                 varchar(200),
   content              varchar(2000),
   product_instance_id  bigint,
   fda_id               bigint,
   primary key (id),
   constraint fk_fda_test_plan_product_instance foreign key (product_instance_id)
      references product_instance (id),
   constraint fk_fda_test_plan_fda foreign key (fda_id)
      references fda (id) on delete restrict on update restrict
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
/* Table: product_instance_test_result                          */
/*==============================================================*/
create table product_instance_test_result
(
   id                   bigint not null auto_increment,
   content              varchar(2000),
   product_instance_id  bigint,
   business_unit_id     bigint,
   test_lab_id          bigint,
   primary key (id),
   constraint fk_product_instance_test_result_product_instance foreign key (product_instance_id)
      references product_instance (id),
   constraint fk_product_instance_test_result_test_lab foreign key (test_lab_id)
      references test_lab (id) on delete restrict on update restrict,
   constraint fk_product_instance_test_result_business_unit foreign key (business_unit_id)
      references business_unit (id) on delete restrict on update restrict
);

