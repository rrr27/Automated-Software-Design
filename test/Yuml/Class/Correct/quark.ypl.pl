dbase(ypl,[yumlBox,yumlAssociation]).

table(yumlBox,[id,type,"name","fields","methods"]).
yumlBox(b0,c,'G','','apply(A);apply(I);').
yumlBox(b1,c,'intsum','','eval(String);toString();apply(A);apply(G);apply(H);apply(I);').
yumlBox(b2,c,'advsum','','eval(String);toString();apply(A);').
yumlBox(b3,c,'hoasum','','eval(String);toString();apply(H);').
yumlBox(b4,c,'A','','apply(A);apply(G);apply(H);apply(I);').
yumlBox(b5,c,'hoaadv','','eval(String);toString();apply(H);').
yumlBox(b6,c,'hoa','','eval(String);toString();apply(H);').
yumlBox(b7,c,'H','','apply(A);apply(G);apply(I);').
yumlBox(b8,c,'intro','','eval(String);toString();apply(A);apply(G);apply(H);apply(I);').
yumlBox(b9,c,'Main','initChoices;','_Main();_Main(String);_main(String#);applicationExit();initAtoms();initConstants();initContentPane();initLayout();initListeners();updateQuarkPanel();').
yumlBox(b10,c,'tree','','eval(String);toString();apply(A);apply(G);apply(H);apply(I);print();').
yumlBox(b11,c,'ghoaadv','','eval(String);toString();apply(G);apply(H);').
yumlBox(b12,c,'gadvprog','','eval(String);toString();apply(A);apply(G);apply(H);apply(I);').
yumlBox(b13,c,'advprog','','eval(String);toString();apply(A);apply(G);apply(H);apply(I);').
yumlBox(b14,c,'gadvice','','eval(String);toString();apply(G);apply(H);').
yumlBox(b15,c,'SwingApp','ContentPane;','_SwingApp();_SwingApp(String);applicationExit();init();initAtoms();initConstants();initContentPane();initLayout();initListeners();').
yumlBox(b16,c,'advice','','eval(String);toString();').
yumlBox(b17,c,'gadvsum','','eval(String);toString();apply(A);apply(G);apply(H);').
yumlBox(b18,c,'I','','').
yumlBox(b19,c,'JFrame','','').

table(yumlAssociation,[id,box1,"role1","end1","lineType",box2,"role2","end2"]).
yumlAssociation(a0,b10,'','^','-',b4,'','').
yumlAssociation(a1,b4,'','^','-',b16,'','').
yumlAssociation(a2,b18,'','^','-',b13,'','').
yumlAssociation(a3,b13,'','','-',b4,'left','<>').
yumlAssociation(a4,b13,'','','-',b18,'right','<>').
yumlAssociation(a5,b4,'','^','-',b2,'','').
yumlAssociation(a6,b2,'','','-',b4,'left','<>').
yumlAssociation(a7,b2,'','','-',b4,'right','<>').
yumlAssociation(a8,b10,'','^','-',b0,'','').
yumlAssociation(a9,b0,'','^','-',b14,'','').
yumlAssociation(a10,b18,'','^','-',b12,'','').
yumlAssociation(a11,b12,'','','-',b0,'left','<>').
yumlAssociation(a12,b12,'','','-',b18,'right','<>').
yumlAssociation(a13,b0,'','^','-',b17,'','').
yumlAssociation(a14,b17,'','','-',b0,'left','<>').
yumlAssociation(a15,b17,'','','-',b0,'right','<>').
yumlAssociation(a16,b0,'','^','-',b11,'','').
yumlAssociation(a17,b11,'','','-',b0,'right','<>').
yumlAssociation(a18,b11,'','','-',b7,'left','<>').
yumlAssociation(a19,b10,'','^','-',b7,'','').
yumlAssociation(a20,b7,'','^','-',b6,'','').
yumlAssociation(a21,b4,'','^','-',b5,'','').
yumlAssociation(a22,b5,'','','-',b4,'right','<>').
yumlAssociation(a23,b5,'','','-',b7,'left','<>').
yumlAssociation(a24,b7,'','^','-',b3,'','').
yumlAssociation(a25,b3,'','','-',b7,'left','<>').
yumlAssociation(a26,b3,'','','-',b7,'right','<>').
yumlAssociation(a27,b10,'','^','-',b18,'','').
yumlAssociation(a28,b18,'','^','-',b8,'','').
yumlAssociation(a29,b18,'','^','-',b1,'','').
yumlAssociation(a30,b1,'','','-',b18,'left','<>').
yumlAssociation(a31,b1,'','','-',b18,'right','<>').
yumlAssociation(a32,b15,'','^','-',b9,'','').
yumlAssociation(a33,b9,'','','-',b10,'t','<>').
yumlAssociation(a34,b19,'','^','-',b15,'','').

