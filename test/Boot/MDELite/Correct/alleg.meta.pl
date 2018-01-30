dbase(meta,[domain,arrow,path]).

table(domain,[id,"name","ext","conformExecutable",temp]).
domain(d0,'Class','violet','',false).
domain(d1,'Vpl','pl','Boot.allegory.ClassConform',false).
domain(d2,'Schema','pl','MDL.ReadSchema',false).
domain(d3,'Java','','',false).

table(arrow,[id,"name","domainInputs","domainOutput","javaExecutable"]).
arrow(a0,'parse','Class','Vpl','Violett.ClassParser').
arrow(a1,'toSchema','Vpl','Schema','Boot.allegory.Vpl2Schema').
arrow(a2,'toJava','Schema','Java','Boot.allegory.VplSchema2Java').

table(path,[id,"name","path"]).
path(p0,'validate','parse').
path(p1,'onlySchema','parse;toSchema').
path(p2,'full','parse;toSchema;toJava').
path(p3,'fromSchema','toJava').

