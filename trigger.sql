create or replace trigger trigger_add_student
after insert on students 
for each row
begin
insert into logs values (seq_logid.nextval,user,sysdate,'student','insert',:new.sid);
end;
/
create or replace trigger trigger_del_student
after delete on students
for each row
begin
delete enrollments where sid = :old.sid;
insert into logs values (seq_logid.nextval,user,sysdate,'student','delete',:old.sid);
end;
/
create or replace trigger trigger_stud_enroll 
after insert on enrollments
for each row
begin
update classes set class_size = class_size + 1 where classid = :new.classid;
insert into logs values (seq_logid.nextval,user,sysdate,'enrollments','insert',:new.classid);
end;
 /
create or replace trigger trigger_stud_del_enroll
after delete on enrollments
for each row
begin
update classes set class_size = class_size - 1 where classid = :old.classid;
insert into logs values (seq_logid.nextval,user,sysdate,'enrollments','delete',:old.classid);
end;
/
