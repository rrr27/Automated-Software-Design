package RunningBear;

///demo
import MDELite.Marquee2Arguments;
import MDELite.RunningBear;
import PrologDB.Table;
import PrologDB.Tuple;

public class demo extends RunningBear {
    static Table cls, fld;

    public static void main(String... args) {
        // Step 1: standard marquee processing
        Marquee2Arguments mark = new Marquee2Arguments(demo.class, ".classes.pl", ".txt", args);
        RBSetup(mark, args); // opens "X.classes.pl" database, as specified on the command line
                             // variable db is the opened database
                             // file "X.java" is opened (or whatever command-line output is specified
        
        // Step 2: open tables of database
        cls = db.getTableEH("class");
        fld = db.getTableEH("field");
        
        // Step 3: "generate" code ...
///demo
///rest
        cls.forEach(c->genClass(c,false));
        
        // Step 4: done
    }

    static void genClass(Tuple c, boolean separateFile) {
        // Step 1: print the class header
        String sup = c.get("superName");
        String xtends = (!sup.equals("null"))?"extends "+sup:"";
        if (separateFile) {
            openOut(c.get("name")+".java");
        }
        l("class %s %s {", c.get("name"), xtends);
        
        // Step 2: compute table of c's fields
        Table cfields = c.rightSemiJoin("cid",fld,"cid"); 
        
        // Step 3: declare all fields and their types
        cfields.forEach(f-> l("   %s %s;", f.get("type"), f.get("name")));
        
        // Step 4: print the class footer
        l("}");
        l(1);    
        if (separateFile) {
            closeOut();
        }
    }
///rest
}
