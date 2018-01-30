dbase(ypl,[yumlBox,yumlAssociation]).

table(yumlBox,[id,type,"name","fields","methods"]).
yumlBox(b0,n,'You can stick notes on diagrams too!','','').
yumlBox(b1,c,'Customer','','').
yumlBox(b2,c,'Order','','').
yumlBox(b3,c,'LineItem','','').
yumlBox(b4,c,'DeliveryMethod','','').
yumlBox(b5,c,'Product','','').
yumlBox(b6,c,'Category','','').
yumlBox(b7,c,'National','','').
yumlBox(b8,c,'International','','').

table(yumlAssociation,[id,box1,"role1","end1","lineType",box2,"role2","end2"]).
yumlAssociation(a0,b1,'1','<>','-',b2,'orders 0..*','>').
yumlAssociation(a1,b2,'*','++','-',b3,'*','>').
yumlAssociation(a2,b2,'','','-',b4,'1','>').
yumlAssociation(a3,b2,'*','','-',b5,'*','>').
yumlAssociation(a4,b6,'','<','-',b5,'','>').
yumlAssociation(a5,b4,'','^','-',b7,'','').
yumlAssociation(a6,b4,'','^','-',b8,'','').

