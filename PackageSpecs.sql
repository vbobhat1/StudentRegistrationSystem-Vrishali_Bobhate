create or replace package StudRegSys as
TYPE proj2cursor IS REF CURSOR;

/*query-2*/
procedure show_students(c_showStud out proj2cursor);
procedure show_courses(c_showCourses out proj2cursor);
procedure show_prerequisites(c_showPreReq out proj2cursor);
procedure show_classes(c_showCls out proj2cursor);
procedure show_enrollments(c_showEnroll out proj2cursor) ;
procedure show_logs(c_showLogs out proj2cursor);

/*query-3*/
procedure add_student(s_sid in students.sid%type, s_firstname in students.firstname%type, s_lastname in students.lastname%type,  s_status in students.status%type,s_gpa in 
students.gpa%type,s_email in students.email%type);

/*query-4*/
procedure student_info(s_sid in students.sid%type,c_infoStud out proj2cursor,error_mes out varchar2);

/*query-5*/
procedure course_info(c_dept_code in courses.dept_code%type,c_course# in courses.course#%type,c_courdet out proj2cursor,c_prereq out proj2cursor);


/*query-6*/
procedure class_info(v_classid in classes.classid%type,c_class_info out proj2cursor,err_msg out varchar2);
/*procedure  class_info(classid in classes.classid%type, msg out varchar);*/

/*query-7*/

procedure enroll_student(s_sid in students.sid%type,c_classid in classes.classid%type);

/*query-8*/
procedure delete_enrollments(s_sid in students.sid%type,c_classid in classes.classid%type, str_out out logs.operation%type); 

/*query-9*/
procedure delete_student(s_sid in students.sid%type);


end;
/
show errors
