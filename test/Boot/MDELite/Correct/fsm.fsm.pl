dbase(fsm,[node,edge]).

table(node,[nid,ntype,"text","color",xpos,ypos]).
node(StateNode0,state,'state','',328.0,275.0).
node(StateNode1,state,'fsm','',601.0,276.0).
node(StateNode2,state,'java%','',927.0,288.0).
node(NoteNode0,note,'arrows%parse=Violett.StateParser%NestSwitch=Boot.fsm.kevinFsm2java%DesignPattern=Boot.fsm.sriramFsm2java%%%','',676.0,422.0).
node(NoteNode1,note,'paths%convertDP=parse;DesignPattern%convertNS=parse;NestSwitch%validate=parse%%','',947.0,420.0).
node(NoteNode2,note,'domains%domain=state,ext=violet%domain=fsm,ext=pl,temp,conform=Boot.MDELite.CategoryConform%','',274.0,423.0).

table(edge,[eid,etype,"label",startid,endid]).
edge(e0,arrow,'parse',StateNode0,StateNode1).
edge(e1,arrow,'NestSwitch, DesignPattern',StateNode1,StateNode2).

