package DML.SchemaTests;

import PrologDB.DBSchema;
import RegTest.Utility;
import java.io.File;
import java.io.PrintStream;
import org.junit.Assert;
import org.junit.Test;

public class SchemaCopyTest extends CommonTst {
    static final String testdata = "test/DML/SchemaTests/TestData/";
    static final String correct  = "test/DML/SchemaTests/Correct/";
    static final String outfile = "error.txt";
    static final String outfile2 = "error2.txt";
    
    /* this is composite test -- perhaps developed out of laziness.
       an ooschema is entered as input.  It is flattened and a true
       schema file is produced.  Then the flattened part of the schema is
       copied, and then the schema is produced in whole.
    */

    public SchemaCopyTest() {
    }

    void genericCopySchemaTest(String localFileName) {
        // localfilename <dbname>.ooschema.pl
        if (!localFileName.endsWith(".ooschema.pl")) {
            System.err.format("file %s does not end in '.ooschema.pl", localFileName);
            Assert.fail();
            return;
        }
        DBSchema result = null;
        DBSchema copy;
        try {
            File schemaFile =  new File(testdata + localFileName);   
            result = DBSchema.readSchema(schemaFile);
            result.finishAndPropagateAttributes();
            String flattenedSchema = localFileName.replace(".ooschema.pl", ".schema.pl");
            PrintStream ps = new PrintStream(outfile);  // new PrintStream(testdata + flattenedSchema);   
            result.print(ps);
            ps.println();
            copy = result.copy();
            String newName = copy.getName() + "C";
            copy.rename(newName);
            String fullName = copy.getFullName();
            PrintStream ps2 =  new PrintStream(outfile2);   
            copy.print(ps2);
            ps2.println();
            ps.close();
            ps2.close();
            Utility.validate(outfile, correct + flattenedSchema, false);
            Utility.validate(outfile2, correct + fullName, false);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void test1() {
        genericCopySchemaTest("violetdb.ooschema.pl");
    }

   @Test
    public void test2() {
        genericCopySchemaTest("inh.ooschema.pl");
    }
  
    @Test
    public void test3() {
        genericCopySchemaTest("complex.ooschema.pl");
    }
    
    @Test
    public void test4() {
        genericCopySchemaTest("hierarchy.ooschema.pl");
    }
}
