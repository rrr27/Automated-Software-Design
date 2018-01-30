dbase(meta,[domain,arrow,path]).

table(domain,[id,"name","ext","conformExecutable",temp]).
domain(d0,'state','violet','',false).
domain(d1,'fsm','pl','Boot.MDELite.CategoryConform',true).
domain(d2,'meta','pl','Boot.MDELite.MetaConform',false).
domain(d3,'java','','',false).

table(arrow,[id,"name","domainInputs","domainOutput","javaExecutable"]).
arrow(a0,'parse','state','fsm','Violett.StateParser').
arrow(a1,'m2m','fsm','meta','Boot.MDELite.fsm2meta').
arrow(a2,'m2t','meta','java','Boot.MDELite.meta2java').

table(path,[id,"name","path"]).
path(p0,'all','parse;m2m;m2t').
path(p1,'validateViolet','parse').
path(p2,'validateMeta','parse;m2m').
path(p3,'quick','m2t').

