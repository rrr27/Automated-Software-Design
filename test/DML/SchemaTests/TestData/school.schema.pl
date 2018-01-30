dbase(school,[person,professor,department,student]).

table(person,[id,"name"]).
table(professor,[id,'name',deptid]).
table(department,[id,"name:xxx",building:yyy]).
table(student,[id,"name",utid]).

subtable(person,[professor,student]).
