dbase(ypl,[yumlBox,yumlAssociation]).

table(yumlBox,[id,type,"name","fields","methods"]).
yumlBox(b0,c,'connection','end1;end2;name1;name2;role1;role2;comma;quote;connectionS;','_connection(String String String String String String);_dump();').
yumlBox(b1,c,'klass','fields;id;methods;name;','_klass(String String String);_klass(String#);_toId(String);_dump();').
yumlBox(b2,c,'Main','','_Main();_checkNameStructure(String);_err(String);_main(String#);_printArray(String String#);').

table(yumlAssociation,[id,box1,"role1","end1","lineType",box2,"role2","end2"]).

