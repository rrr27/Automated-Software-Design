dbase(con,[company,contract,person]).
table(company,[cid,"name"]).
table(contract,[kid,value,cid,pid]).
table(person,[pid,"name",nickname]).

person(p1,'Batory', itsme).
person(p2,'Lin',good).
person(p3,'Browne',old).

contract(k1,740,c1,null).
contract(k2,220,null,p1).
contract(k3,330,c2,null).
contract(k4,47,null,p2).
contract(k5,400,c1,p2).

company(c1,'Ace Plumbing').
company(c2,'Rudys BBQ').
company(c4,'Os Cafeteria').
