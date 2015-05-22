set serveroutput on
create or replace package body StudRegSys as

/*query2*/

procedure show_students(c_showStud out proj2cursor) is
begin
      open c_showStud for select * from students;
end;

procedure show_courses(c_showCourses out proj2cursor) is
begin
open c_showCourses for select * from courses;
end;

procedure show_prerequisites(c_showPreReq out proj2cursor) is
begin
open c_showPreReq for select * from prerequisites;
end;

procedure show_classes(c_showCls out proj2cursor) is
begin
open  c_showCls for select * from classes;
end;

procedure show_enrollments(c_showEnroll out proj2cursor) is
begin
open c_showEnroll for select * from enrollments;
end;

procedure show_logs(c_showLogs out proj2cursor) is
begin
open c_showLogs for select * from logs;
end;


/*query-3*/
procedure add_student(s_sid in students.sid%type, s_firstname in students.firstname%type, s_lastname in students.lastname%type, s_status in students.status%type,s_gpa in 
students.gpa%type,s_email in students.email%type) as
begin
insert into students values (s_sid, s_firstname,s_lastname, s_status, s_gpa, s_email);
commit;
end;

/*query-4*/
procedure student_info(s_sid in students.sid%type,c_infoStud out proj2cursor,error_mes out varchar2)
IS
cnt number(5);
s_firstname students.firstname%type;
s_lastname students.lastname%type;
ex1 EXCEPTION;
ex2 EXCEPTION;
BEGIN
select count(*) into cnt from students where sid=s_sid;
                if(cnt=0) then
                RAISE ex1;
                end if;

select count(*) into cnt from students s,enrollments e,classes c  where e.sid=s.sid and s.sid=s_sid and e.classid=c.classid;
                if(cnt=0) then
                RAISE ex2;
                end if;

open c_infoStud FOR select courses.dept_code, courses.course#, courses.title from enrollments, classes, courses 
where enrollments.sid = s_sid and enrollments.classid = classes.classid 
and classes.course# = courses.course# and classes.dept_code = courses.dept_code;
EXCEPTION
 when ex1 then
      Raise_application_error(-20001,'The sid : '||s_sid||' is not valid');
  when ex2 then
      Raise_application_error(-20001,'The student has not taken any course');


end;

/*query-5*/
procedure course_info(c_dept_code in courses.dept_code%type,c_course# in courses.course#%type,c_courdet out proj2cursor,c_prereq out proj2cursor) is
begin

open c_courdet for select (dept_code || course#) as courseid,title
from courses
where dept_code=c_dept_code and course#=c_course#;

open c_prereq for
select (c.dept_code || c.course#) as courseid,c.title
from courses c
where c.dept_code in(select pre_dept_code from prerequisites where dept_code=c_dept_code and course#=c_course#) and c.course# in(select pre_course# from prerequisites where 
dept_code=c_dept_code and course#=c_course#);

end;


/*query-6*/
procedure class_info(v_classid in classes.classid%type,c_class_info out proj2cursor,err_msg out varchar2) is
v_title courses.title%type;
title varchar2(10); 
counter number(5);
 ex1 EXCEPTION;
 ex2 EXCEPTION;

 begin

 select title into v_title from classes where classid=v_classid;   
    select count(*) into counter from classes where classid=v_classid;
		if(counter=0) then
		RAISE ex1;
		end if;

select count(*) into counter from enrollments, courses, classes where enrollments.classid=classes.classid and classes.classid=v_classid;
		if(counter=0) then
		RAISE ex2;
		end if;

open c_class_info for select students.sid,students.firstname from students,courses,enrollments,classes where students.sid=enrollments.sid and enrollments.classid=classes.classid and classes.dept_code = courses.dept_code and classes.course#=courses.course# and classes.classid=v_classid;

exception

when ex1 then
Raise_application_error(-20001,'the class id: ' || v_classid || ' is invalid');
err_msg:='the class id' || v_classid || '  is invalid';

when ex2 then
Raise_application_error(-20001,'No students are enrolled in the class with classid :' || v_classid || '.');
err_msg:='No students are enrolled in the class with classid :' || v_classid || '!';
end;
 




/*query-7*/
procedure enroll_student(s_sid in students.sid%type,c_classid in classes.classid%type) is

	counter number(5);
	flag number(1);
	c_class_size classes.class_size%type;
	c_limit classes.limit%type;

	ex1 EXCEPTION;
	ex2 EXCEPTION;
	ex3 EXCEPTION;
	ex4 EXCEPTION;
	ex5 EXCEPTION;
	ex6 EXCEPTION;
	ex7 EXCEPTION;


	begin
		select count(*) into counter from classes where classid=c_classid;
		if(counter=0) then
		RAISE ex1;
		end if;
		
		select count(*) into counter from students where sid=s_sid;
		if(counter=0) then
		RAISE ex2;
		end if;

		select class_size,limit into c_class_size,c_limit from classes where classid=c_classid;
		if(c_class_size=c_limit) then
		RAISE ex3;
		end if;
		
		select count(*) into counter from students s,enrollments e where e.sid=s.sid and s.sid=s_sid and e.classid=c_classid;
		if(counter>0) then
		RAISE ex4;
		end if;
		
		select count(*) into counter from 		
		((select c1.classid from classes c1,classes c2 where c1.semester=c2.semester and c1.year=c2.year and c2.year=(select year from classes where classid=c_classid and 
		c2.semester=(select semester from classes where classid=c_classid))) intersect (select classid from enrollments where sid=s_sid));

		dbms_output.put_line(counter);
		if(counter>3) then
		RAISE ex5;
		end if;

	

		select count(*) into counter from
			((select x.pre_dept_code,x.pre_course# from prerequisites x,classes y where y.dept_code=x.dept_code and y.course#=x.course# and 
			y.classid=c_classid)
			minus
			(select c.dept_code,c.course# from classes c,enrollments e where e.sid=s_sid and e.classid=c.classid and e.lgrade is not null));


		if(counter>0) then
		RAISE ex6;
		end if;

		insert into enrollments values(s_sid,c_classid,null);

	        if(counter=3) then
                dbms_output.put_line('You are overloaded');
                RAISE ex7;   
                end if;
	

		EXCEPTION
		when ex1 then
			Raise_application_error(-20001,'The classid : '||c_classid||' is invalid');
		when ex2 then
			Raise_application_error(-20001,'The sid : '||s_sid||' is invalid');
		when ex3 then
			Raise_application_error(-20001,'The class with classid is : '||c_classid||' is closed.');
		when ex4 then
			Raise_application_error(-20001,'The student with sid : '||s_sid||' is already enrolled in class whose classid is : '||c_classid||' .');
		when ex5 then
			Raise_application_error(-20001,'The student with sid : '||s_sid||' cannot be enrolled in more than four classes.');
		when ex6 then
			Raise_application_error(-20001,'Pre-requisite courses have not been completed by the student with sid : '||s_sid||' .');
		when ex7 then 
			Raise_application_error(-20001,'You are overloaded. However, the student is still enrolled');		

	end;

/*query-8*/
procedure delete_enrollments(s_sid in students.sid%type,c_classid in classes.classid%type, str_out out logs.operation%type) is
	
	cnt number(5);
	flag1 number(1);
	flag2 number(1);
	s_firstname students.firstname%type;
        s_lastname students.lastname%type;
        c_class_size classes.class_size%type;
	c_limit classes.limit%type;
	str1 varchar(100);
	str2 varchar(100);
	ex1 EXCEPTION;
	ex2 EXCEPTION;
	ex3 EXCEPTION;
	ex4 EXCEPTION;
     begin
	flag1:=0;
	flag2:=0;
	select count(*) into cnt from classes where classid=c_classid;
	if(cnt=0) then
	RAISE ex1;
	end if;
		select count(*) into cnt from students where sid=s_sid;
		if(cnt=0) then
		RAISE ex2;
		end if;

		select count(*) into cnt from students s,enrollments e where e.sid=s.sid and s.sid=s_sid and e.classid=c_classid;
		if(cnt=0) then
		RAISE ex3;
		end if;
		
		select count(*) into cnt from students s,enrollments e where e.sid=s.sid and s.sid=s_sid and e.classid=c_classid and e.lgrade is not null;
		if(cnt=1) then
		RAISE ex4;
		end if;

		delete from enrollments where sid=s_sid and classid=c_classid;

		select count(*) into cnt from enrollments where sid=s_sid;
		if(cnt=0) then
			str1:=' This student is now not enrolled in any classes.';
			flag1:=1;
		end if;

		select count(*) into cnt from enrollments where classid=c_classid;
		if(cnt=0) then
			str2:=' The class now has no students.';
			flag2:=1;
		end if;

		if(flag1=1 and flag2=0) then
		str_out := str1;
		end if;

		if(flag1=0 and flag2=1) then
		str_out := str2;
		end if;

		if(flag1=1 and flag2=1) then
		str_out :=' This student is now not enrolled in any class AND The class has no students. ';
		end if;
		
		EXCEPTION
		when ex1 then
			Raise_application_error(-20001,'The classid : '||c_classid||' is not valid');
		when ex2 then
			Raise_application_error(-20001,'The sid : '||s_sid||' is not valid');
		when ex3 then
			Raise_application_error(-20001,'The student with sid : '||s_sid||' is not enrolled in class whose classid is : '||c_classid||' .');
		when ex4 then
			Raise_application_error(-20001,'The student with sid : '||s_sid||' has completed the class whose classid is : '||c_classid||', so cannot be 
deleted.');

	end;



/*query-9*/
procedure delete_student(s_sid in students.sid%type) is
cnt number(5);
ex1 EXCEPTION;
begin
        select count(*) into cnt from students where sid=s_sid;
        if(cnt=0) then
        RAISE ex1;
        end if;
        delete from students where sid=s_sid;
        EXCEPTION
        when ex1 then
        Raise_application_error(-20001,'The sid : '||s_sid||' is not valid');
        end;


end;
/
show errors




