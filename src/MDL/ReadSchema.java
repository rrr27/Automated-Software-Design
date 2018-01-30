package MDL;

import PrologDB.DBSchema;

/**
 * reads a schema or ooschema and reports errors
 */
public class ReadSchema {

    /**
     * standard marquee for an MDL program
     */
    static void marquee(String msg) {
        String className = ReadSchema.class.getName();
        System.out.format("\nUsage: %s <X>.<sch>.pl\n", className);
        System.out.format("       <sch> is 'ooschema' or 'schema'\n");
        System.out.format("       reads schema x and reports errors\n");
        if (msg != null) {
            System.out.format("       %s\n", msg);
        }
        System.exit(0);
    }

    /* reads database -- outputs errors */
    /**
     * reads schema from any ooschema or schema file and throws Error if errors
     * are found.
     *
     * @param args -- X.S.pl, where S = ooschema or schema
     */
    public static void main(String... args) {
        if (args.length == 0)
            marquee(null);
        if (args.length != 1 || !args[0].endsWith(".pl")) {
            marquee(args[0] + " submitted as input");
        }
        DBSchema s = DBSchema.readSchema(args[0]);
    }
}
