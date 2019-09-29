create table logrecord (
  logrecordid number(9) not null,
  entitytype varchar2(99) not null,
  entityid number(9) not null,
  entityname varchar2(99) not null,
  logrecordtime number(10) not null,
  constraint logrecord_logrecordid_pk primary key (logrecordid)
);
