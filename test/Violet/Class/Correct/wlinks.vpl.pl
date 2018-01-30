dbase(vpl,[vBox,vAssociation]).

table(vBox,[id,type,"name","fields","methods",x,y]).
vBox(ClassNode0,c,'aclass','','',209.0,131.0).
vBox(ClassNode1,c,'bclass','','',666.0,130.0).
vBox(ClassNode2,c,'cclass','','',209.0,292.0).
vBox(ClassNode3,c,'dclass','','',670.0,308.0).
vBox(InterfaceNode0,i,'aint','','',665.0,483.0).
vBox(InterfaceNode1,i,'bint','','',229.0,473.0).

table(vAssociation,[id,cid1,type1,"role1","arrow1",cid2,type2,"role2","arrow2","bentStyle","lineStyle","middleLabel"]).
vAssociation(A0,ClassNode0,c,'b,c','',ClassNode1,c,'a','V','HVH','','').
vAssociation(A1,ClassNode3,c,'','',ClassNode1,c,'','TRIANGLE','','','').
vAssociation(A2,InterfaceNode0,i,'','',InterfaceNode1,i,'','TRIANGLE','VHV','','').
vAssociation(A3,ClassNode3,c,'','',InterfaceNode0,i,'','TRIANGLE','VHV','DOTTED','').
vAssociation(A4,ClassNode2,c,'x','DIAMOND',ClassNode3,c,'y','','HVH','','').
vAssociation(A5,ClassNode0,c,'','BLACK_DIAMOND',ClassNode2,c,'z','','HVH','','').

