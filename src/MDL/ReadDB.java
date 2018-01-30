package MDL;

import MDELite.Marquee1Argument;
import PrologDB.DB;

/**
 * MDL program that reads a database file and reports errors (other than
 * constraint violations)
 */
public class ReadDB {

    /**
     * reads database X.S.pl and reports errors, if any;  Schema 
     * constraints not applied/evaluated.
     * @param args -- X.S.pl
     */
    public static void main(String... args) {
        // Step 1: standard marquee processing
        Marquee1Argument mark = new Marquee1Argument(ReadDB.class, ".pl", args);
        String inputFileName = mark.getInputFileName();

        // Step 2: do the work
        DB.readDataBase(inputFileName);
    }
}
