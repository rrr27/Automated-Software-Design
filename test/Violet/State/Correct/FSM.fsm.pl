dbase(fsm,[node,edge]).

table(node,[nid,ntype,"text","color",xpos,ypos]).
node(StateNode0,state,'VioletFMS;%domain=state;%ext=violet;%isTemp=false;','',268.0,293.0).
node(StateNode1,state,'PrologDB;%domain=prologdb;%ext=pl;%isTemp=true;','',613.0,315.0).
node(StateNode2,state,'JavaCode%domain=javacode;%ext=dir;%isTemp=false;','',978.0,334.0).
node(NoteNode0,note,'Paths%convert=parseXML.RBScript%','',411.0,384.0).
node(CircularInitialStateNode0,init,'','',329.0,161.0).
node(CircularFinalStateNode0,final,'','',1070.0,186.0).

table(edge,[eid,etype,"label",startid,endid]).
edge(e0,arrow,'parseXML=paarseXML.Main.java',StateNode0,StateNode1).
edge(e1,arrow,'RBScript = prologdb2java.Main.java',StateNode1,StateNode2).
edge(e2,arrow,'',CircularInitialStateNode0,CircularFinalStateNode0).
edge(e3,note,'',StateNode0,NoteNode0).
edge(e4,arrow,'',StateNode2,StateNode1).
edge(e5,arrow,'',StateNode2,StateNode2).
edge(e6,arrow,'',StateNode2,StateNode2).
edge(e7,arrow,'',StateNode1,StateNode1).
edge(e8,arrow,'',StateNode1,StateNode1).
edge(e9,arrow,'',StateNode1,StateNode1).
edge(e10,arrow,'',CircularInitialStateNode0,StateNode1).

