dbase(ypl,[yumlBox,yumlAssociation]).

table(yumlBox,[id,type,"name","fields","methods"]).
yumlBox(b0,c,'User','+Forename+;Surname;+HashedPassword;-Salt','+Login();+Logout()').
yumlBox(b1,c,'Customer','','').
yumlBox(b2,c,'Order','','').
yumlBox(b3,c,'LineItem','','').
yumlBox(b4,c,'Root','','').

table(yumlAssociation,[id,box1,"role1","end1","lineType",box2,"role2","end2"]).
yumlAssociation(a0,b0,'','^','-',b1,'','').
yumlAssociation(a1,b1,'','<>','-',b2,'orders*','>').
yumlAssociation(a2,b2,'','++','-',b3,'0..*','>').
yumlAssociation(a3,b2,'','','-',b4,'','').

