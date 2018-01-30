dbase(ypl,[yumlBox,yumlAssociation]).

table(yumlBox,[id,type,"name","fields","methods"]).
yumlBox(b0,i,'ITask','','').
yumlBox(b1,i,'Session','','').
yumlBox(b2,c,'NightlyBillingTask','','').
yumlBox(b3,c,'Company','','').
yumlBox(b4,c,'Location','','').
yumlBox(b5,c,'Point','','').
yumlBox(b6,c,'Wages','','').
yumlBox(b7,c,'Contractor','','').
yumlBox(b8,c,'Salaried','','').

table(yumlAssociation,[id,box1,"role1","end1","lineType",box2,"role2","end2"]).
yumlAssociation(a0,b0,'','^','-.-',b2,'','').
yumlAssociation(a1,b3,'','++','-',b4,'1','>').
yumlAssociation(a2,b4,'','<>','-',b5,'','>').
yumlAssociation(a3,b6,'','^','-',b7,'','').
yumlAssociation(a4,b6,'','^','-',b8,'','').

