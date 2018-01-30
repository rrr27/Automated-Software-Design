dbase(ypl,[yumlBox,yumlAssociation]).

table(yumlBox,[id,type,"name","fields","methods"]).
yumlBox(b0,c,'Fonts','','_Fonts();font();getCajb();getOkjb();').
yumlBox(b1,c,'Print','','_Print(Component);print(Graphics print.PageFormat int);_disableDoubleBuffering(Component);_enableDoubleBuffering(Component);_printComponent(Component);print();').
yumlBox(b2,c,'Main','','_Main();getLineWrap();getTextArea();_main(String#);').
yumlBox(b3,c,'RedoAction','','_RedoAction(Main);actionPerformed(ActionEvent);').
yumlBox(b4,c,'Actions','','_Actions(Main);abouT();copY();cuT();exiT();finD();findNexT();fonT();lineWraP();neW();opeN();open();pastE();prinT();savE();save();saveAs();selectALL();').
yumlBox(b5,c,'Center','','_Center(Fonts);_Center(Main);fCenter();nCenter();').
yumlBox(b6,c,'About','','_About();').
yumlBox(b7,c,'UndoAction','','_UndoAction(Main);actionPerformed(ActionEvent);').
yumlBox(b8,c,'ExampleFileFilter','','_ExampleFileFilter();_ExampleFileFilter(String);_ExampleFileFilter(String String);_ExampleFileFilter(String#);_ExampleFileFilter(String# String);getDescription();getExtension(File);accept(File);isExtensionListInDescription();addExtension(String);setDescription(String);setExtensionListInDescription(boolean);').
yumlBox(b9,c,'JPanel','','').
yumlBox(b10,c,'filechooser.FileFilter','','').
yumlBox(b11,c,'JDialog','','').
yumlBox(b12,c,'JFrame','','').
yumlBox(b13,c,'AbstractAction','','').

table(yumlAssociation,[id,box1,"role1","end1","lineType",box2,"role2","end2"]).
yumlAssociation(a0,b9,'','^','-',b6,'','').
yumlAssociation(a1,b4,'','','-',b0,'font','<>').
yumlAssociation(a2,b10,'','^','-',b8,'','').
yumlAssociation(a3,b11,'','^','-',b0,'','').
yumlAssociation(a4,b0,'','','-',b5,'center','<>').
yumlAssociation(a5,b12,'','^','-',b2,'','').
yumlAssociation(a6,b2,'','','-',b4,'actions','<>').
yumlAssociation(a7,b2,'','','-',b5,'center','<>').
yumlAssociation(a8,b13,'','^','-',b3,'','').
yumlAssociation(a9,b13,'','^','-',b7,'','').

