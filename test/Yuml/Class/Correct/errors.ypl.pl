dbase(ypl,[yumlBox,yumlAssociation]).

% type = n (for note), c (for class), i (for interface)
table(yumlBox,[id,type,"name","fields","methods"]).
yumlBox(b1,c,'don','','').
yumlBox(b1,c,'bat','','').

yumlBox(b5,i,'donb','nofields','a,b').
yumlBox(b6,n,'donn','nofields','a,b').
yumlBox(b7,c,'donb','','').
yumlBox(b8,n,'donn','','').


table(yumlAssociation,[id,box1,"role1","end1","lineType",box2,"role2","end2"]).
yumlAssociation(a1,b1,'','','',b2,'','').
yumlAssociation(a1,b3,'','','',b2,'','').
yumlAssociation(a5,b5,'','^','-.-',b1,'','').
yumlAssociation(a6,b5,'','^','',b1,'','').
yumlAssociation(a7,b7,'','','',b5,'','^').


