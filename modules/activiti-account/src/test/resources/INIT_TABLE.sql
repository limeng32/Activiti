drop table if exists account;

drop table if exists accountBucket;

drop table if exists accountRole;

drop table if exists loginlog;

drop table if exists resetPasswordLog;

drop table if exists role;

drop table if exists systemMonitor;

/*==============================================================*/
/* Table: account                                               */
/*==============================================================*/
create table account
(
   account_id           varchar(40) not null ,
   name                 varchar(20) ,
   email                varchar(320) ,
   password             varchar(64) ,
   activated            tinyint ,
   status               char(1) ,
   activateValue        varchar(8) ,
   opLock               int ,
   company              char(4) ,
   primary key (account_id)
);


/*==============================================================*/
/* Table: accountBucket                                         */
/*==============================================================*/
create table accountBucket
(
   accountBucket_id     varchar(40) not null ,
   account_id           varchar(40) ,
   nickname             varchar(20) ,
   originalPortrait     varchar(300) ,
   portraitModify       varchar(50) ,
   tempOriginalPortrait varchar(300) ,
   uploadedSize         bigint ,
   primary key (accountBucket_id)
);


/*==============================================================*/
/* Table: accountRole                                           */
/*==============================================================*/
create table accountRole
(
   accountRole_id       varchar(40) not null ,
   account_id           varchar(40) ,
   role_id              varchar(40) ,
   primary key (accountRole_id)
);


/*==============================================================*/
/* Table: loginlog                                              */
/*==============================================================*/
create table loginlog
(
   loginlog_id          varchar(40) not null ,
   loginIP              varchar(50) ,
   account_id           varchar(40) ,
   loginDate            date ,
   primary key (loginlog_id)
);


/*==============================================================*/
/* Table: resetPasswordLog                                      */
/*==============================================================*/
create table resetPasswordLog
(
   reset_pass_id        varchar(40) not null ,
   emailTime            datetime ,
   dueTime              datetime ,
   resetTime            datetime ,
   userIP               varchar(50) ,
   token                varchar(50) ,
   url                  varchar(200) ,
   available            boolean ,
   status               char ,
   tempAccount          varchar(320) ,
   tempPassword         varchar(64) ,
   account_id           varchar(40) ,
   primary key (reset_pass_id)
);

/*==============================================================*/
/* Table: role                                                  */
/*==============================================================*/
create table role
(
   role_id              varchar(40) not null ,
   name                 varchar(20) ,
   value                char(1) ,
   opLock               int ,
   primary key (role_id)
);
