dbase(fsm,[node,edge]).

table(node,[nid,ntype,"text","color",xpos,ypos]).
node(CircularInitialStateNode0,init,'','',259.0,265.0).
node(StateNode0,state,'start','',430.0,270.0).
node(StateNode1,state,'drink','',590.0,175.0).
node(StateNode2,state,'eat','',610.0,378.0).
node(StateNode3,state,'Pig%','',818.0,256.0).
node(CircularFinalStateNode0,final,'','',1072.0,283.0).

table(edge,[eid,etype,"label",startid,endid]).
edge(e0,arrow,'',CircularInitialStateNode0,StateNode0).
edge(e1,arrow,'',StateNode0,StateNode2).
edge(e2,arrow,'',StateNode0,StateNode1).
edge(e3,arrow,'',StateNode1,StateNode3).
edge(e4,arrow,'',StateNode2,StateNode3).
edge(e5,arrow,'',StateNode1,StateNode2).
edge(e6,arrow,'',StateNode2,StateNode1).
edge(e7,arrow,'',StateNode3,CircularFinalStateNode0).

