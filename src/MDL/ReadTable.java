package MDL;

import PrologDB.Table;

/**
 * MDL program that reads a .pl or .csv file and reports errors (other than
 * constraint violations)
 */
public class ReadTable {

    /**
     * customized marquee for this program
     */
    static void marquee() {
        String className = ReadTable.class.getName();
        System.out.println();
        System.out.format("Usage: %s <x>.<y>\n",className);
        System.out.format("       <y> is .csv or .pl\n");
        System.out.format("       reads table <x> and reports errors\n");
        System.exit(1);
    }

    /**
     * reads table X.pl or X.csv and reports errors
     * no schema constraints applied
     * 
     * @param args -- X.pl or X.csv
     */
    public static void main(String... args) {
        // Step 1: almost standard marquee processing
        if (args.length != 1 || !(args[0].endsWith(".csv") || args[0].endsWith(".pl"))) {
            marquee();
        }
        
        // Step 2: do the work
        Table.readTable(args[0]);
    }
}
