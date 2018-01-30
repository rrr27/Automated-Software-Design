package MDL;

import PrologDB.DBSchema;
import java.io.PrintStream;
import MDELite.Error;
import MDELite.Marquee2Arguments;

/**
 * OO2Schema translates S.ooschema.pl to S.schema.pl
 * that is, it propagates supertable attributes to subtable attributes
 */
public class OO2schema {
    
    /**
     * reads X.ooschema.pl and outputs X.schema.pl by flattening -- propagating
     * columns of supertables to subtables
     * @param args -- X.ooschema.pl
     */
    public static void main(String... args) {
        
        // Step 1: standard marquee processing
        Marquee2Arguments mark = new Marquee2Arguments(OO2schema.class, ".ooschema.pl", ".schema.pl", args);
        //mark.insist(1);
        String inputFileName = mark.getInputFileName();
        String outputFileName = mark.getOutputFileName();

        // Step 2: do the work now
        DBSchema dbs = DBSchema.readSchema(inputFileName);
        dbs.finishAndPropagateAttributes();
        dbs.print(outputFileName);
    }
}
