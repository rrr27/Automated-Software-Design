package DML.SchemaTests;

import PrologDB.Column;
import PrologDB.DB;
import PrologDB.DBSchema;
import PrologDB.SubTableSchema;
import PrologDB.Table;
import PrologDB.TableSchema;
import PrologDB.Tuple;
import RegTest.Utility;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

public class CommonTst {
    
    static final String errorfile = "error.txt";

    void genericSchema2EmptyDB(String localFileName) {
        // localfilename <dbname>.<schemaname>.pl

        try {
            File schemaFile = new File("TestData/Schema/" + localFileName);
            DBSchema dbs = DBSchema.readSchema(schemaFile);
            DB dbe = new DB(dbs.getName() + "E", dbs);
            String fullName = dbe.getFullName();
            dbe.print(new PrintStream(errorfile));
            Utility.validate(errorfile, "Correct/EmptyDB/" + fullName, false);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            org.junit.Assert.fail();
        }
    }

    protected void genericExtend(String localdbfile) {
        DB db = DB.readDataBase("TestData/DB/" + localdbfile);
        DBSchema dbs = db.getSchema();
        String name = dbs.getName();
        
        // build schema from ground up
        // RULE: always create schema first before creating database.
        // NOt other way around.
        
        DBSchema newSchema = new DBSchema(name + "1");

        for (TableSchema ts : dbs.getTableSchemas()) {
            TableSchema nts = new TableSchema(ts.getName());
            for (Column c : ts.getColumns()) {
                nts.addColumn(new Column(c));
            }
            nts.addColumn(new Column("length")); // this is the extra attribute that defined "extension" in this test
            newSchema.addTableSchema(nts);
        }
        
        // now finishAndPropagateAttributes off with subtable declarations
        List<SubTableSchema> slist = db.getSubTableSchemas();
        for (SubTableSchema sts : slist) {
            newSchema.addSubTableSchema(sts.copy(newSchema));
        }
        
        // now at this point, we have literally copied the contents of dbs into new schema.
        // now we can instantiate tables -- because newSchema can be normalized

        DB db1 = new DB(db.getName() + "1", newSchema); // this creates empty tables
        for (Table t : db.getTables()) {
            Table nt = db1.getTableEH(t.getName());
            TableSchema newts = newSchema.getTableSchemaEH(t.getName());
            for (Tuple tpl : t.tuplesLocal()) {
                Tuple tuple = new Tuple(newts);
                String firstString = null;
                for (Column c : t.getColumns()) {
                    String colName = c.getName();
                    if (c.isQuoted() && firstString == null) {
                        firstString = tpl.get(colName);
                        String firstValue = firstString.length() + "";
                        tuple.set("length", firstValue);
                    }
                    tuple.set(colName, tpl.get(colName));
                }
                tuple.isComplete();
                nt.add(tuple);
            }
        }

        String db1filename = db1.getFullName();
        try {
            db1.print(new PrintStream(errorfile));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        Utility.validate(errorfile, "Correct/DBExtend/" + db1filename, false);
    }
}
