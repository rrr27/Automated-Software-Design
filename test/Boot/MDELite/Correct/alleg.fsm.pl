dbase(fsm,[node,edge]).

table(node,[nid,ntype,"text","color",xpos,ypos]).
node(StateNode0,state,'Class%','',360.0,195.0).
node(StateNode1,state,'Vpl','',567.0,200.0).
node(StateNode2,state,'Schema','',825.0,205.0).
node(StateNode3,state,'Java%%','',1073.0,217.0).
node(NoteNode0,note,'domains%domain=Class,ext=violet%domain=Vpl,ext=pl,conform=Boot.allegory.ClassConform%domain=Schema,ext=pl,conform=MDL.ReadSchema%domain=Java%','',360.0,312.0).
node(NoteNode1,note,'arrows%parse=Violett.ClassParser%toSchema=Boot.allegory.Vpl2Schema%toJava=Boot.allegory.VplSchema2Java','',762.0,326.0).
node(NoteNode2,note,'paths%validate=parse%onlySchema=parse;toSchema%full=parse;toSchema;toJava%fromSchema=toJava','',1029.0,346.0).

table(edge,[eid,etype,"label",startid,endid]).
edge(e0,arrow,'parse',StateNode0,StateNode1).
edge(e1,arrow,'toSchema',StateNode1,StateNode2).
edge(e2,arrow,'toJava',StateNode2,StateNode3).

