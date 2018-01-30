package Yuml;

import MDELite.Marquee4Conform;
import PrologDB.Constraints;
import static PrologDB.Constraints.isLegit;
import static PrologDB.Constraints.isUnique;
import PrologDB.DB;
import PrologDB.ErrorReport;
import PrologDB.Table;
import PrologDB.Tuple;
import java.util.function.Predicate;
import static PrologDB.Constraints.implies;

public class ClassConform {
    static Table Box, Assoc;
    
    public static void main(String... args) {
        String expl;
        
        // Step 1: Standard marquee processing
        Marquee4Conform mark = new Marquee4Conform(ClassConform.class, ".ypl.pl", args);
        String inputFileName = mark.getInputFileName();

        // Step 2: read database and create error handler
        DB db = DB.readDataBase(inputFileName);
        Box = db.getTableEH("yumlBox");
        Assoc = db.getTableEH("yumlAssociation");
        ErrorReport er = new ErrorReport();

        // Step 3: apply constraints
        // C1: all Box ids are unique
        // C2: all Assoc ids are unique
        isUnique(Box, "id", er);
        isUnique(Assoc, "id", er);
        isUnique(Box.filter(r->!r.is("type","n")),"name",er);

        // C3: All box1 references are legit
        // C4: All box2 references are legit
        isLegit(Assoc, "box1", Box, "id", er);
        isLegit(Assoc, "box2", Box, "id", er);

        // Box constraints 
        // % type = c(class),i(nterface),n(ote).  packages are skipped   
        // table(Box,[id,type,"name","fields","methods",x,y]).
        // C5: if type = i, then fields are empty
        // C6: if type = n, then fields and methods are empty
        Predicate<Tuple> typeisi = r->r.is("type","i");
        Predicate<Tuple> typeisn = r->r.is("type","n");
        Predicate<Tuple> fieldsIsEmpty = r->r.is("fields","");
        Predicate<Tuple> methodsIsEmpty = r->r.is("methods","");
        
        expl = "interfaces should have no fields";
        implies(Box, typeisi, fieldsIsEmpty, expl, er);
        
        expl = "notes should have no fields or methods";
        implies(Box, typeisn, fieldsIsEmpty.and(methodsIsEmpty),expl,er);

        Predicate<Tuple> e1isTriangle = t -> t.is("end1", "^");
        Predicate<Tuple> e2isTriangle = t -> t.is("end2", "^");
        Predicate<Tuple> e1isEmpty = t -> t.is("end1", "");
        Predicate<Tuple> e2isEmpty = t -> t.is("end2", "");
        Predicate<Tuple> r1isEmpty = t -> t.is("role1", "");
        Predicate<Tuple> r2isEmpty = t -> t.is("role2", "");

        expl = "end1=TRIANGLE implies (end2 is empty and role1 is empty and role2 is empty)";
        implies(Assoc, e1isTriangle, e2isEmpty.and(r1isEmpty.and(r2isEmpty)), expl, er);
        implies(Assoc, e2isTriangle, e1isEmpty.and(r1isEmpty.and(r2isEmpty)), expl, er);

        // C11 if end1==TRIANGLE and type1=c and type2=i, then lineStyle== dashed, symmetrically
        Table intf = Box.filter(r->r.is("type","i"));
        Table klasses = Box.filter(r->r.is("type","c"));
        Table a1 = Assoc.semiJoin("box1", intf, "id");
        Table a2 = a1.semiJoin("box2", klasses, "id");
       
        Predicate<Tuple> isDashed = r -> r.is("lineType", "-.-");
        expl = "linestyle should be dashed";
        implies(a2, e1isTriangle, isDashed, expl, er);
        
        // now symmetrical check
        a1 = Assoc.semiJoin("box1", klasses, "id");
        a2 = a1.semiJoin("box2", intf, "id");
        implies(a2, e2isTriangle, isDashed, expl, er);
        
        // cycle checking: table a2 contains associations only between classes
        // first check for extends cycles

        a1 = Assoc.semiJoin("box1",klasses,"id");
        a2 = a1.semiJoin("box2",klasses,"id");
        
        Table ct = Constraints.makeCycleTable();
        a2.filter(e1isTriangle).forEach(t->addTuple(t,ct,true));
        a2.filter(e2isTriangle).forEach(t->addTuple(t,ct,false));
        //ct.sort("par",true).print(System.err);
        Constraints.cycleCheck(ct,er);
        
        // Finally, see if there are implements cycles 

        a1 = Assoc.semiJoin("box1",intf,"id");
        a2 = a1.semiJoin("box2",intf,"id");
        
        Table ct2 = Constraints.makeCycleTable();
        a2.filter(e1isTriangle).forEach(t->addTuple(t,ct2,true));
        a2.filter(e2isTriangle).forEach(t->addTuple(t,ct2,false));
        //ct2.print(System.err);
        Constraints.cycleCheck(ct2, er);
        
        // End report all errors
        er.printReportEH(System.out);
    }
                
    private static void addTuple(Tuple t, Table ct, boolean direction) {
        Tuple cycle = new Tuple(ct);
        String box1 = Box.getFirstEH(g->g.is("id",t.get("box1"))).get("name");
        String box2 = Box.getFirstEH(g->g.is("id",t.get("box2"))).get("name");
        if (direction) {
            cycle.setValues(box1, box2);
        } else {
            cycle.setValues(box2, box1);
        }
        ct.add(cycle);
    }
}
