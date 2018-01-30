dbase(school,[person,professor,department,student]).

table(person,[id,"name"]).

table(professor,[id,"name",deptid]).
professor(p1,'don',d1).
professor(p2,'Robert',d1).
professor(p3,'Lorenzo',d2).
professor(p4,'kelly',d3).

table(department,[id,"name","building"]).
department(d1,'computer science','gates dell complex').
department(d2,'computer science','gates hall').
department(d3,'computer science','Bahen Centre').

table(student,[id,"name",utid]).
student(s1,'zeke','zh333').
student(s2,'Brenda','UTgreat').
student(s3,'Thomas','astronaut201').

subtable(person,[professor,student]).
