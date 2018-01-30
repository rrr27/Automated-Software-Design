dbase(school,[person,professor,department,student]).

table(person,[id,"name"]).
table(professor,[deptid]).
table(department,[id,"name","building"]).
table(student,[utid]).

subtable(person,[professor,student]).
