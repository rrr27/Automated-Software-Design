dbase(school,[student,course,enroll]).

table(student,[sid,name,age]).
table(course,[cid,name,prof]).
table(enroll,[eid,sid,cid]).

student(s1,A,15).
student(s2,B,16).
student(s3,C,15).
student(s4,D,16).
student(s5,E,17).
student(s6,F,18).

course(c1,X,xp).
course(c2,Y,xp).
course(c3,Z,yp).
course(c4,W,zp).
course(c5,Q,zp).

enroll(e1,s1,c1).
enroll(e2,s1,c5).
enroll(e3,s2,c3).
enroll(e4,s2,c4).
enroll(e5,s3,c5).
enroll(e6,s5,c2).
enroll(e7,s5,c3).
enroll(e8,s6,c1).
