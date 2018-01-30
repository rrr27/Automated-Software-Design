dbase(figure,[shape,box,square,octagon,circle]).

table(shape,[sid,name]).
table(box,[lenx,leny]).
table(square,[len]).
table(octagon,[bold]).
table(circle,[]).

subtable(shape,[box,square]).
subtable(square,[octagon]).
subtable(octagon,[circle]).
