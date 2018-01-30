dbase(petdb,[pet,cat,dog]).

table(pet,[did,"name"]).
table(dog,[did,"name","breed", color]).
table(cat,[did,"name","type"]).

subtable(pet,[dog,cat]).
