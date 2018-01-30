dbase(fsm,[node,edge]).

table(node,[nid,ntype,"text","color",xpos,ypos]).
node(StateNode0,state,'1','',351.0,149.0).
node(StateNode1,state,'2','',576.0,163.0).
node(StateNode2,state,'3','',793.0,167.0).
node(CircularInitialStateNode0,init,'','',395.0,267.0).
node(CircularInitialStateNode1,init,'','',625.0,266.0).
node(CircularInitialStateNode2,init,'','',835.0,277.0).
node(CircularFinalStateNode0,final,'','',405.0,348.0).
node(CircularFinalStateNode1,final,'','',632.0,356.0).
node(CircularFinalStateNode2,final,'','',865.0,367.0).
node(NoteNode0,note,'3','',403.0,417.0).
node(NoteNode1,note,'4','',631.0,435.0).
node(NoteNode2,note,'5','255:140:0:255:',877.0,440.0).
node(PointNode0,point,'','',417.0,350.0).
node(PointNode1,point,'','',634.0,372.0).
node(PointNode2,point,'','',874.0,374.0).

table(edge,[eid,etype,"label",startid,endid]).
edge(e0,arrow,'',CircularInitialStateNode0,StateNode0).
edge(e1,arrow,'',CircularInitialStateNode0,CircularFinalStateNode0).
edge(e2,arrow,'',CircularInitialStateNode1,StateNode1).
edge(e3,arrow,'',CircularInitialStateNode1,CircularFinalStateNode1).
edge(e4,arrow,'',CircularInitialStateNode2,StateNode2).
edge(e5,arrow,'',CircularInitialStateNode2,CircularFinalStateNode2).
edge(e6,note,'',NoteNode0,PointNode0).
edge(e7,note,'',NoteNode1,PointNode1).
edge(e8,note,'',NoteNode2,PointNode2).

