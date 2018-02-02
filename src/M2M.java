import PrologDB.DBSchema;
import PrologDB.DB;
import PrologDB.Tuple;
import PrologDB.Table;
import MDELite.Marquee2Arguments;
import PrologDB.ColumnCorrespondence;

public class M2M {

    public static void main(String... args) {

        // Step 1: standard marquee processing
        Marquee2Arguments mark = new Marquee2Arguments(M2M.class,
                ".families2.pl", ".families1.pl", args);
        String inputFileName =  mark.getInputFileName();
        String outputSchema =  "families1.schema.pl";

        // Step 2: read the families database and their tables
        DB in = DB.readDataBase(inputFileName);
        Table fam = in.getTableEH("family");
        Table mem = in.getTableEH("member");

        // Step 3: create an empty persons database with empty tables
        DBSchema outSchema = DBSchema.readSchema(outputSchema);
        DB inria = new DB(in.getName(), outSchema);
        Table family_new = inria.getTableEH("family");
        Table member_new = inria.getTableEH("member");

        // Step 4: fill in family tuples as it is
        fam.forEach(t -> family_new.addTuple(t.getId(), t.get("lastName"), t.get("fatherid"), t.get("motherid")));
        // Check if either is not null then fill that as fid and corresponding check for son
        // and daughter else if both null, check them in fatherid then in motherid

        fam.join("fatherid", mem, "mid").forEach(t -> helper(member_new, t, "true"));
        fam.join("motherid", mem, "mid").forEach(t -> helper(member_new, t, "false"));
        fam.join("id", mem, "sonOf").forEach(t -> helper(member_new, t, "true"));
        fam.join("id", mem, "daughterOf").forEach(t -> helper(member_new, t, "false"));

        member_new.sort("mid", true);

        // Step 5: print out database
        inria.print("inria.families1.pl");


    }

    static void helper(Table tab, Tuple t, String isMale) {
        String id = t.get("member.mid");
        String firstName = t.get("member.firstName");
        String fid = null;
        if(!t.get("member.sonOf").equalsIgnoreCase("null")) {
            fid = t.get("member.sonOf");
        } else if (!t.get("member.daughterOf").equalsIgnoreCase("null")){
            fid = t.get("member.daughterOf");
        }
        tab.addTuple(id, firstName, fid, isMale);
    }

}
