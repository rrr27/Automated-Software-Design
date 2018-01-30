dbase(ypl,[yumlBox,yumlAssociation]).

% type = n (for note), c (for class), i (for interface)
table(yumlBox,[id,type,"name","fields","methods"]).
yumlBox(a,c,'a','','').
yumlBox(b,c,'b','','').
yumlBox(c,c,'c','','').
yumlBox(d,c,'d','','').
yumlBox(e,c,'e','','').
yumlBox(f,c,'f','','').

yumlBox(aa,i,'aa','','').
yumlBox(bb,i,'bb','','').
yumlBox(cc,i,'cc','','').
yumlBox(dd,i,'dd','','').
yumlBox(ee,i,'ee','','').
yumlBox(ff,i,'ff','','').

table(yumlAssociation,[id,box1,"role1","end1","lineType",box2,"role2","end2"]).
yumlAssociation(a1,a,'','^','',b,'','').
yumlAssociation(a2,a,'','^','',c,'','').
yumlAssociation(a3,a,'','^','',d,'','').
yumlAssociation(a4,e,'','','',d,'','^').
yumlAssociation(a5,e,'','^','',a,'','').

%yumlAssociation(i1,aa,'','^','',bb,'','').
%yumlAssociation(i2,aa,'','^','',cc,'','').
%yumlAssociation(i3,aa,'','^','',dd,'','').
%yumlAssociation(i4,ee,'','','',dd,'','^').
%yumlAssociation(i5,ee,'','^','',aa,'','').

yumlAssociation(i1,bb,'','','',aa,'','^').
yumlAssociation(i2,cc,'','','',aa,'','^').
yumlAssociation(i3,dd,'','','',aa,'','^').
yumlAssociation(i4,dd,'','^','',ee,'','').
yumlAssociation(i5,aa,'','','',ee,'','^').
