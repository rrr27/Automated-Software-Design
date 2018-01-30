dbase(meta,[domain,arrow,path]).

table(domain,[id,"name","ext","conformExecutable",temp]).
domain(d0,'state','violet','',false).
domain(d1,'fsm','pl','Boot.MDELite.CategoryConform',true).
domain(d2,'java','','',false).

table(arrow,[id,"name","domainInputs","domainOutput","javaExecutable"]).
arrow(a0,'parse','state','fsm','Violett.StateParser').
arrow(a1,'NestSwitch','fsm','java','Boot.fsm.kevinFsm2java').
arrow(a2,'DesignPattern','fsm','java','Boot.fsm.sriramFsm2java').

table(path,[id,"name","path"]).
path(p0,'convertDP','parse;DesignPattern').
path(p1,'convertNS','parse;NestSwitch').
path(p2,'validate','parse').

