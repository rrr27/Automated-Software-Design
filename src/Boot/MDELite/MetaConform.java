package Boot.MDELite;

import MDELite.Marquee4Conform;
import PrologDB.Constraints;
import static PrologDB.Constraints.isUnique;
import PrologDB.DB;
import PrologDB.ErrorReport;
import PrologDB.Table;
import PrologDB.Tuple;
import java.util.HashSet;

public class MetaConform extends Constraints {

    static final String pathSeparator = ";";
    static final String domainSeparator = ",";

    private static Table domain, arrow, path;

    public static void main(String... args) {
        // Step 1: Standard marquee processing
        Marquee4Conform mark = new Marquee4Conform(MetaConform.class, ".meta.pl", args);
        String inputFileName = mark.getInputFileName();

        // Step 2: read database and create error handler
        DB db = DB.readDataBase(inputFileName);
        domain = db.getTableEH("domain");
        arrow = db.getTableEH("arrow");
        path = db.getTableEH("path");
        ErrorReport er = new ErrorReport();

        // Step 3: apply constraints
        // the ids of every table are unique
        UniqueId(domain, er);
        UniqueId(arrow, er);
        UniqueId(path, er);

        // the names of every table are unique
        isUnique(domain, "name", er);
        isUnique(arrow, "name", er);
        isUnique(path, "name", er);

        // names of arrows and paths must be unique among both tables
        HashSet<String> adef = new HashSet<>();
        HashSet<String> pdef = new HashSet<>();
        for (Tuple a : arrow.tuples()) {
            String aName = a.get("name");
            if (adef.contains(aName)) {
                er.add("multiple arrows have name %s", aName);
            } else {
                adef.add(aName);
            }
        }

        for (Tuple p : path.tuples()) {
            String pname = p.get("name");
            if (pdef.contains(pname)) {
                er.add("multiple paths have name %s", pname);
            } else {
                pdef.add(pname);
            }
            if (adef.contains(pname)) {
                er.add("path(%s...) has same name as arrow %s", p.get("id"), pname);
            }
        }

        // verify that each arrow's inputs and outputs belong to legalDomains
        HashSet<String> legalDomains = new HashSet<>();
        domain.forEach(d->legalDomains.add(d.get("name")));
        findIllegalDomainReferences("domainInputs", legalDomains, er);
        findIllegalDomainReferences("domainOutput", legalDomains, er);

        // now for each path, make sure that every step references a defined arrow
        // If people create a loop, that's their problem.
        HashSet<String> legalSteps = new HashSet<>();
        arrow.forEach(a -> legalSteps.add(a.get("name")));
        path.forEach(p->legalSteps.add(p.get("name")));

        for (Tuple pp : path.tuples()) {
            String ppname = pp.get("name");
            String[] steps = pp.get("path").split(pathSeparator);
            for (String s : steps) {
                if (legalSteps.contains(s)) {
                    continue;
                }
                er.add("path %s references nonexistent arrow %s", ppname, s);
            }
        }

        // every database must have a legal conform executable
        for (Tuple nn : domain.filter(t -> t.is("ext", "pl")).tuples()) {
            String err = String.format("domain(%s...)", nn.get("id"));
            isLegalClassName(err, nn.get("conformExecutable"), er);
        }
        // every arrow must have a legal executable
        for (Tuple aa : arrow.tuples()) {
            String err = String.format("arrow(%s...)", aa.get("id"));
            isLegalClassName(err, aa.get("javaExecutable"), er);
        }

        // End report all errors
        er.printReportEH(System.err);
    }

    private static void findIllegalDomainReferences(String fieldName, HashSet<String> legalDomains, ErrorReport er) {
        for (Tuple a : arrow.tuples()) {
            String ds[] = a.get(fieldName).split(domainSeparator);
            for (String d : ds) {
                if (!legalDomains.contains(d)) {
                    er.add("arrow(%s...) references nonexistent domain %s", a.get("id"), d);
                }
            }
        }
    }

    static void isLegalClassName(String err, String className, ErrorReport er) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            er.add("%s java executable '%s' does not exist or is not on classpath", err, className);
        }
    }
}
