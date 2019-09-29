insert into gruppa(groupid, groupname) values (1, '6225M');
insert into gruppa(groupid, groupname) values (2, '6226M');
insert into gruppa(groupid, groupname) values (3, '6227M');
insert into gruppa(groupid, groupname) values (4, '6228M');
insert into gruppa(groupid, groupname) values (5, '6229M');

insert into student(studentid, studentname, groupid)
  values (1, 'Егоров Станислав Иванович',
    (select groupid from gruppa where groupname = '6225M'));
insert into student(studentid, studentname, groupid)
  values (2, 'Закаулкин Станислав Викторович',
    (select groupid from gruppa where groupname = '6226M'));
insert into student(studentid, studentname, groupid)
  values (3, 'Юзакиев Руслан Романович',
    (select groupid from gruppa where groupname = '6226M'));
insert into student(studentid, studentname, groupid)
  values (4, 'Замуров Рустам Фатихович',
    (select groupid from gruppa where groupname = '6227M'));
insert into student(studentid, studentname, groupid)
  values (5, 'Иванов Иван Иванович',
    (select groupid from gruppa where groupname = '6227M'));
insert into student(studentid, studentname, groupid)
  values (6, 'Коржиков Валентин Эдуардович',
    (select groupid from gruppa where groupname = '6227M'));
insert into student(studentid, studentname, groupid)
  values (7, 'Петрова Надежда Сергеевна',
    (select groupid from gruppa where groupname = '6228M'));
insert into student(studentid, studentname, groupid)
  values (8, 'Кузнецова Анна Викторовна',
    (select groupid from gruppa where groupname = '6229M'));
