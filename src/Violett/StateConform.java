package Violett;

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

/** Conformance program for .vpl.pl databases */
public class StateConform extends Constraints {
   
    /** Conformance program for .vpl.pl databases
     * @param args X.vpl.pl
     */
    public static void main(String... args) {
        String expl;
        
        // Step 1: Standard marquee processing
        Marquee4Conform mark = new Marquee4Conform(StateConform.class, ".fsm.pl", args);
        String inputFileName = mark.getInputFileName();

        // Step 2: read database and create error handler
        DB db = DB.readDataBase(inputFileName);
        Table node = db.getTableEH("node");
        Table edge = db.getTableEH("edge");
        ErrorReport er = new ErrorReport();

        // Step 3: apply constraints
        // C1: all node ids are unique
        // C2: all edge ids are unique
        isUnique(node, "nid", er);
        isUnique(edge, "eid", er);

        // C3: All cid1 references are legit
        // C4: All cid2 references are legit
        isLegit(edge, "startid", node, "nid", er);
        isLegit(edge, "endid", node, "nid", er);

        // node constraints 
        // no two state nodes have the same text
        isUnique(node.filter(r->r.is("ntype","state")), "text", er);
        // no two edges should have the same non-empty label
        isUnique(edge.filter(r->!r.is("label","")),"label",er);
        
        // no fsm should have ntype = point
        Predicate<Tuple> ntypeisp = r->r.is("ntype","point");

        expl = "no points declarations allowed";
        iftest(node, ntypeisp, expl,er);
        
        // every node, except start nodes, have an incoming edge
        Predicate<Tuple> notPointOrNote = r->!(r.is("ntype","point") || r.is("ntype","note"));
        Table needsEdge = node.filter(notPointOrNote);
        Table missingInComing = needsEdge.antiSemiJoin("nid", edge, "endid").filter(r->!r.is("ntype","init"));
        missingInComing.stream().forEach(r->er.add("node nid=%s does not have an incoming edge",r.get("nid")));
        
        // every node, except final nodes have an outgoing edge
        Table missingOutGoing = needsEdge.antiSemiJoin("nid",edge,"startid").filter(r->!r.is("ntype","final"));
        missingOutGoing.stream().forEach(r->er.add("node nid=%s does not have an outgoing edge",r.get("nid")));

        // End report all errors
        er.printReportEH(System.out);
    }
}
