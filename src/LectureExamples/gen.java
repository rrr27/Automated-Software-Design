package LectureExamples;

import MDELite.Marquee2Arguments;
import PrologDB.Table;
import PrologDB.Tuple;

public class gen extends MDELite.RunningBear {

    /**
     * produce files from given database of type "ex"
     * @param args the command line arguments
     */
    public static void main(String... args) {
       // Step 1: standard marquee processing
        Marquee2Arguments mark = new Marquee2Arguments(gen.class, "data.pl", ".java", args);
        RBSetup(mark, args);
        
        // Step 2: get the tables from database db
        Table clss = db.getTable("class");
        Table attr = db.getTable("attribute");

        // Step 3: generate files
        for (Tuple c : clss.tuples()) {
            openOut(c.get("name") + ".java");
            l("class %s {", c.get("name"));
            Table attrOf = c.rightSemiJoin("cid", attr, "cid");
            for (Tuple a : attrOf.tuples()) {
                l("  %s %s;", a.get("type"), a.get("name"));
            }
            l("}");
            l();
            closeOut();
        }
    }
}
