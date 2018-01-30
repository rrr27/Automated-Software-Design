dbase(ypl,[yumlBox,yumlAssociation]).

table(yumlBox,[id,type,"name","fields","methods"]).
yumlBox(b0,i,'aint','','').
yumlBox(b1,i,'bint','','').
yumlBox(b2,c,'aclass','','').
yumlBox(b3,c,'bclass','','').
yumlBox(b4,c,'cclass','','').
yumlBox(b5,c,'dclass','','').

table(yumlAssociation,[id,box1,"role1","end1","lineType",box2,"role2","end2"]).
yumlAssociation(a0,b2,'bc','','-',b3,'a','>').
yumlAssociation(a1,b4,'x','<>','-',b5,'y','').
yumlAssociation(a2,b4,'z','','-',b2,'a','++').
yumlAssociation(a3,b0,'','^','-.-',b5,'','').
yumlAssociation(a4,b0,'','','-',b1,'','^').
yumlAssociation(a5,b3,'','^','-',b5,'','').

