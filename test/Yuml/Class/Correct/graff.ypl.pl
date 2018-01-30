dbase(ypl,[yumlBox,yumlAssociation]).

table(yumlBox,[id,type,"name","fields","methods"]).
yumlBox(b0,c,'CycleWorkSpace','AnyCycles;isDirected;counter;BLACK;GRAY;WHITE;','_CycleWorkSpace(boolean);checkNeighborAction(Vertex Vertex);init_vertex(Vertex);postVisitAction(Vertex);preVisitAction(Vertex);').
yumlBox(b1,c,'Edge','weight;','_Edge();EdgeConstructor(Vertex Vertex);EdgeConstructor(Vertex Vertex int);adjustAdorns(Edge);display();').
yumlBox(b2,c,'Graph','inFile;edges;vertices;isDirected;ch;','_Graph();findsVertex(String);CycleCheck();readNumber();_endProfile();_resumeProfile();_startProfile();_stopProfile();GraphSearch(WorkSpace);NumberVertices();addAnEdge(Vertex Vertex int);addEdge(Edge);addOnlyEdge(Edge);addVertex(Vertex);display();run(Vertex);runBenchmark(String);stopBenchmark();').
yumlBox(b3,c,'Main','','_Main();_main(String#);').
yumlBox(b4,c,'Neighbor','','_Neighbor();_Neighbor(Vertex Edge);').
yumlBox(b5,c,'NumberWorkSpace','','_NumberWorkSpace();preVisitAction(Vertex);').
yumlBox(b6,c,'Vertex','visited;VertexColor;VertexCycle;VertexNumber;name;neighbors;','_Vertex();assignName(String);VertexConstructor();addNeighbor(Neighbor);dftNodeSearch(WorkSpace);display();init_vertex(WorkSpace);').
yumlBox(b7,c,'WorkSpace','','_WorkSpace();checkNeighborAction(Vertex Vertex);init_vertex(Vertex);nextRegionAction(Vertex);postVisitAction(Vertex);preVisitAction(Vertex);').

table(yumlAssociation,[id,box1,"role1","end1","lineType",box2,"role2","end2"]).
yumlAssociation(a0,b7,'','^','-',b0,'','').
yumlAssociation(a1,b4,'','^','-',b1,'','').
yumlAssociation(a2,b1,'','','-',b6,'start','<>').
yumlAssociation(a3,b4,'','','-',b1,'edge','<>').
yumlAssociation(a4,b4,'','','-',b6,'end','<>').
yumlAssociation(a5,b7,'','^','-',b5,'','').

