dbase(figure,[shape,box,square,octagon,circle]).

table(shape,[sid,name]).
table(box,[sid,name,lenx,leny]).
table(square,[sid,name,len]).
table(octagon,[sid,name,len,bold]).
table(circle,[sid,name,len,bold]).

subtable(shape,[box,square]).
subtable(square,[octagon]).
subtable(octagon,[circle]).
