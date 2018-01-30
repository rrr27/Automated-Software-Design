dbase(ypl,[yumlBox,yumlAssociation]).

table(yumlBox,[id,type,"name","fields","methods"]).
yumlBox(b0,c,'Batory','','').
yumlBox(b1,c,'Customer','','').
yumlBox(b2,c,'Order','','').
yumlBox(b3,c,'Billing Address','','').
yumlBox(b4,c,'Address','','').
yumlBox(b5,c,'Company','','').
yumlBox(b6,c,'Location','','').

table(yumlAssociation,[id,box1,"role1","end1","lineType",box2,"role2","end2"]).
yumlAssociation(a0,b1,'1','<>','-',b2,'*','>').
yumlAssociation(a1,b1,'','','-',b3,'','>').
yumlAssociation(a2,b1,'1','','-',b4,'0..*','').
yumlAssociation(a3,b2,'','','-',b4,'billing','>').
yumlAssociation(a4,b2,'','','-',b4,'shipping','>').
yumlAssociation(a5,b5,'','<>','-',b6,'1','>').

