insert into gruppa(groupid, groupname) values (1, '6225M');
insert into gruppa(groupid, groupname) values (2, '6226M');
insert into gruppa(groupid, groupname) values (3, '6227M');
insert into gruppa(groupid, groupname) values (4, '6228M');
insert into gruppa(groupid, groupname) values (5, '6229M');

insert into student(studentid, studentname, groupid)
  values (1, '������ ��������� ��������',
    (select groupid from gruppa where groupname = '6225M'));
insert into student(studentid, studentname, groupid)
  values (2, '��������� ��������� ����������',
    (select groupid from gruppa where groupname = '6226M'));
insert into student(studentid, studentname, groupid)
  values (3, '������� ������ ���������',
    (select groupid from gruppa where groupname = '6226M'));
insert into student(studentid, studentname, groupid)
  values (4, '������� ������ ���������',
    (select groupid from gruppa where groupname = '6227M'));
insert into student(studentid, studentname, groupid)
  values (5, '������ ���� ��������',
    (select groupid from gruppa where groupname = '6227M'));
insert into student(studentid, studentname, groupid)
  values (6, '�������� �������� ����������',
    (select groupid from gruppa where groupname = '6227M'));
insert into student(studentid, studentname, groupid)
  values (7, '������� ������� ���������',
    (select groupid from gruppa where groupname = '6228M'));
insert into student(studentid, studentname, groupid)
  values (8, '��������� ���� ����������',
    (select groupid from gruppa where groupname = '6229M'));
