dbase(meta,[domain,arrow,path]).

table(domain,[id,"name","ext","conformExecutable",temp]).
domain(d0,'state','violet','',false).
domain(d1,'fsm','pl','Violett.StateConform',true).
domain(d2,'java','','',false).

table(arrow,[id,"name","domainInputs","domainOutput","javaExecutable"]).
arrow(a0,'parse','state','fsm','Violett.StateParser').
arrow(a1,'NestSwitch','fsm','java','BootFSM.kevinFsm2java').
arrow(a2,'DesignPattern','fsm','java','BootFSM.sriramFsm2java').

table(path,[id,"name","path"]).
path(p0,'convertDP','validater;DesignPattern').
path(p1,'convertNS','parse;NestSwitch').
path(p2,'validate','parse').
path(p3,'bogus','parser;skip').
