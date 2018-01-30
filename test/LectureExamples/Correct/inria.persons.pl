dbase(persons,[person,male,female]).

table(person,[id,"fullName"]).

table(male,[id,"fullName"]).
male(m1,'Jim March').
male(m5,'Peter Sailor').
male(m3,'Brandon March').
male(m7,'David Sailor').
male(m8,'Dylan Sailor').

table(female,[id,"fullName"]).
female(m2,'Cindy March').
female(m6,'Jackie Sailor').
female(m4,'Brenda March').
female(m9,'Kelly Sailor').

subtable(person,[male,female]).
dbase(persons,[person,male,female]).

table(person,[id,"fullName"]).

table(male,[id,"fullName"]).
male(m1,'Jim March').
male(m5,'Peter Sailor').
male(m3,'Brandon March').
male(m7,'David Sailor').
male(m8,'Dylan Sailor').

table(female,[id,"fullName"]).
female(m2,'Cindy March').
female(m6,'Jackie Sailor').
female(m4,'Brenda March').
female(m9,'Kelly Sailor').

subtable(person,[male,female]).
