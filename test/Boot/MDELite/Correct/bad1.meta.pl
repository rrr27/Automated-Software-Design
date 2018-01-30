dbase(meta,[domain,arrow,path]).

table(domain,[id,"name","ext","conformExecutable",temp]).
domain(d0,'state','violet','',false).
domain(d1,'fsm','pl','Violett.StateConform',true).
domain(d2,'fsm','pl','Boot.MDELite.MetaConform',true).
domain(d3,'java','','',false).
domain(d1,'fsm','pl','Violet.StateConform',true).

table(arrow,[id,"name","domainInputs","domainOutput","javaExecutable"]).
arrow(a0,'parse','state','fsm','Violett.StateParser').
arrow(a1,'m2m','fsm','meta','Boot.MDELite.fsm2meta').
arrow(a2,'m2t','state','java','Boot.MDELite.meta2java').
arrow(a2,'m2t','state','java','Boot.MDELite.meta2java').

table(path,[id,"name","path"]).
path(p0,'convert','parse;m2m;m2t').
path(p1,'validateMeta','parse;m2m').
path(p2,'validateViolet','parse').
path(p3,'validateViolet','parse').
path(p5,'m2m','parse').
