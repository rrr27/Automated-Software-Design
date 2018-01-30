dbase(sc,[course,student,takes]).

table(course,[cid,"name"]).
course(c1,'software design').
course(c2,'probability').
course(c3,'wireless networks').
course(c4,'databases').
course(c5,'compilers').
%nonunique id, name
course(c3,'software design').


table(student,[sid,'name']).
student(s1,"name1").
student(s2,"name2").
student(s3,"name3").
student(s4,"name4").
student(s5,"name5").
student(s6,"name6").
student(s7,"name7").
student(s8,"name1").

table(takes,[tid,cid,sid]).
takes(t1,c1,s1).
takes(t2,c1,s3).
takes(t3,c1,s4).
takes(t4,c1,s7).

takes(t5,c2,s3).
takes(t6,c2,s6).

takes(t7,c4,s3).
%duplicate
takes(t8,c4,s3).  
%illegal student
takes(t9,c4,s8).
%various nulls
takes(t10,null,s3).
takes(t11,c4,null).
takes(t12,null,null).

takes(t13,c5,s1).
takes(t14,c6,s2).
takes(t16,c4,s7).
%illegal student
takes(t17,c4,s8).
%illegal course
takes(t19,c0,s8).

