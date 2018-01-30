dbase(vpl,[vBox,vAssociation]).

table(vBox,[id,type,"name","fields","methods",x,y]).
vBox(ClassNode0,c,'violetClass','"name"%"fields"%"methods"%x%y','',107.0,47.0).
vBox(ClassNode1,c,'violetInterface','"name"%"methods"%x%y','',105.0,397.0).
vBox(ClassNode2,c,'violetAssociation','"role1"%arrow1%"type1"%"role2"%arrow2%"type2"%"lineStyle"','',641.0,34.0).
vBox(ClassNode3,c,'violetInterfaceExtends','','',86.0,661.0).
vBox(ClassNode4,c,'violetClassImplements','%','',362.0,308.0).
vBox(ClassNode5,c,'violetMiddleLabels','"label"','',639.0,257.0).
vBox(ClassNode6,c,'endPoint','','',366.0,80.0).

table(vAssociation,[id,cid1,type1,"role1","arrow1",cid2,type2,"role2","arrow2","bentStyle","lineStyle","middleLabel"]).
vAssociation(A0,ClassNode1,c,'idb,idx','',ClassNode3,c,'','','','','').
vAssociation(A1,ClassNode0,c,'cid','',ClassNode4,c,'','','','','').
vAssociation(A2,ClassNode1,c,'iid','',ClassNode4,c,'','','','','').
vAssociation(A3,ClassNode0,c,'','',ClassNode6,c,'','TRIANGLE','VHV','','').
vAssociation(A4,ClassNode1,c,'','',ClassNode6,c,'','TRIANGLE','VHV','','').
vAssociation(A5,ClassNode6,c,'cid1,cid2','',ClassNode2,c,'','','HVH','','').
vAssociation(A6,ClassNode5,c,'','',ClassNode6,c,'cid1,cid2','','','','').

