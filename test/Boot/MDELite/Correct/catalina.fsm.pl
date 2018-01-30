dbase(fsm,[node,edge]).

table(node,[nid,ntype,"text","color",xpos,ypos]).
node(StateNode0,state,'state%%','',346.0,258.0).
node(StateNode1,state,'fsm%','',543.0,263.0).
node(StateNode2,state,'meta','',737.0,260.0).
node(StateNode3,state,'java%','',950.0,271.0).
node(NoteNode0,note,'arrows%parse=Violett.StateParser%m2m=Boot.MDELite.fsm2meta%m2t=Boot.MDELite.meta2java%','',680.0,387.0).
node(NoteNode1,note,'domains%domain=state,ext=violet%domain=fsm,ext=pl,conform=Boot.MDELite.CategoryConform,temp%domain=meta,ext=pl,conform=Boot.MDELite.MetaConform%domain=java','',259.0,387.0).
node(NoteNode2,note,'paths%all=parse;m2m;m2t%validateViolet=parse%validateMeta=parse;m2m%quick=m2t','',914.0,391.0).

table(edge,[eid,etype,"label",startid,endid]).
edge(e0,arrow,'parse',StateNode0,StateNode1).
edge(e1,arrow,'m2m',StateNode1,StateNode2).
edge(e2,arrow,'m2t',StateNode2,StateNode3).

