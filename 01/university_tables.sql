create table gruppa (
  groupid number(9) not null,
  groupname varchar2(99) not null,
  constraint gruppa_groupid_pk primary key (groupid)
);

create table student (
  studentid number(9) not null,
  studentname varchar2(99) not null,
  groupid number(9) not null,
  constraint student_studentid_pk primary key (studentid),
  constraint student_groupid_fk foreign key (groupid) references gruppa(groupid)
);
