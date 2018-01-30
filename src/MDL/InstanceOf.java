package MDL;

import PrologDB.DBSchema;
import PrologDB.DB;
import MDELite.Utils;

/**
 *
 * InstanceOf is a program that takes two inputs, a schema and a db,
 * and confirms (or refutes) that the db conforms to the schema (sans constraints)
 */
public class InstanceOf {

    /**
     * standard marquee for an MDL program
     */
    public static void marquee() {
        System.out.println();
        System.out.format("\nUsage: %s S.schema.pl Y.S.pl\n", InstanceOf.class.getName());
        System.out.format("       confirms that database Y is an instance of S\n");
        System.exit(0);
    }

    /**
     * takes two inputs S.schema.pl Y.S.pl, and confirms or refutes
     * that the database conforms to the schema, not including schema constraints
     * @param args -- S.schema.pl, Y.S.pl
     */
    public static void main(String... args) {
        // Step 1: nonstandard marquee processing.  This program requires
        // a wee bit of refection to get it right
        if (args.length != 2) {
            marquee();
        }
        if (!args[0].endsWith(".schema.pl"))
            marquee();
        String[] parsed1 = Utils.parseFileName(args[0]);
        int len = parsed1.length;
        String pattern = "."+parsed1[len-3] + ".pl";
        if (!args[1].endsWith(pattern)) {
            marquee();
        }

        // Step 2: now do the work
        DBSchema s = DBSchema.readSchema(args[0]);
        DB d = DB.readDataBase(args[1]);
        DBSchema s1 = d.getSchema();
        try {
            s.equalsEH(s1);
        }
        catch (Exception e) {
            System.err.print(e.getMessage());
            System.err.format("schemas %s and %s do not match\n", args[0], args[1]);
        }
    }
}
