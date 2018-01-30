dbase(ypl,[yumlBox,yumlAssociation]).

table(yumlBox,[id,type,"name","fields","methods"]).
yumlBox(b0,c,'User','+Forename+;Surname;+HashedPassword;-Salt','+Login();+Logout()').

table(yumlAssociation,[id,box1,"role1","end1","lineType",box2,"role2","end2"]).

