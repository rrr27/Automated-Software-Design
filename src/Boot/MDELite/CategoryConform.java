package Boot.MDELite;

import MDELite.Marquee4Conform;
import PrologDB.Constraints;
import static PrologDB.Constraints.iftest;
import static PrologDB.Constraints.isLegit;
import static PrologDB.Constraints.isUnique;
import PrologDB.DB;
import PrologDB.ErrorReport;
import PrologDB.Table;
import PrologDB.Tuple;
import java.util.function.Predicate;

public class CategoryConform extends Constraints {
    
    static ErrorReport er;
    
    public static void main(String... args) {
        String expl;
        
        // Step 1: Standard marquee processing
        Marquee4Conform mark = new Marquee4Conform(CategoryConform.class, ".fsm.pl", args);
        String inputFileName = mark.getInputFileName();

        // Step 2: read database and create error handler
        DB db = DB.readDataBase(inputFileName);
        Table node = db.getTableEH("node");
        Table edge = db.getTableEH("edge");
        er = new ErrorReport();

        // Step 3: apply constraints
        // C1: all node ids are unique
        // C2: all edge ids are unique
        
        UniqueId(node, er);
        UniqueId(edge, er);

        // C3: All cid1 references are legit
        // C4: All cid2 references are legit
        isLegit(edge, "startid", node, "nid", er);
        isLegit(edge, "endid", node, "nid", er);
        
        // All nodes & edges must have a non blank name
        Constraints.iftest(node, g->g.is("text",""), "a blank text field", er);
        Constraints.iftest(edge,g->g.is("label",""), "a blank label field", er);


        // node constraints 
        // no two state nodes have the same text
        isUnique(node.filter(r->r.is("ntype","state")), "text", er);
        // no two edges should have the same non-empty label
        isUnique(edge.filter(r->!r.is("label","")),"label",er);
        
        // no fsm should have ntype = point
        Predicate<Tuple> ntypeisp = r->r.is("ntype","point");
        
        // only one note that starts with paths, arrows, domains

        Table notes = node.filter(n->n.is("ntype","note"));
        eReporter(notes.filter(t->t.get("text").startsWith("paths")).count(),"paths");
        eReporter(notes.filter(t->t.get("text").startsWith("arrows")).count(),"arrows");
        eReporter(notes.filter(t->t.get("text").startsWith("arrows")).count(),"arrows");

        expl = "no points declarations allowed";
        iftest(node, ntypeisp, expl,er);
        
        // no init, final or point nodes
        for (Tuple t : node.tuples()) {
            String type = t.get("ntype");
            if (type.equals("state") || type.equals("note"))
                continue;
            er.add("node(%s...) %s node not permitted in a category diagram",t.get("nid"),type);
        }

        // End report all errors
        er.printReportEH(System.out);
    }
    
    static void eReporter(int cnt, String type) {
        switch(cnt) {
            case 0 :  er.add("no note for %s", type); 
            case 1 :  return;
            default : er.add("too many notes for %s only one allowed", type);
        }
    }
}
