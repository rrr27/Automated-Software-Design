dbase(ypl,[yumlBox,yumlAssociation]).

table(yumlBox,[id,type,"name","fields","methods"]).
yumlBox(b0,c,'Customer','-forname:string;surname:string','doShiz()').
yumlBox(b1,n,'Aggregate root','','').
yumlBox(b2,c,'Order','','').
yumlBox(b3,c,'LineItem','','').

table(yumlAssociation,[id,box1,"role1","end1","lineType",box2,"role2","end2"]).
yumlAssociation(a0,b0,'','<>','-',b2,'orders*','>').
yumlAssociation(a1,b2,'','++','-',b3,'0..*','>').
yumlAssociation(a2,b2,'','','-',b1,'','').

