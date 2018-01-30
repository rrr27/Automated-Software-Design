package DML.PrologDB;

///imports
import MDELite.Marquee2Arguments;
import PrologDB.Column;
import PrologDB.ColumnCorrespondence;
import PrologDB.DB;
import PrologDB.DBSchema;
import PrologDB.ErrorReport;
import PrologDB.SubTableSchema;
import PrologDB.Table;
import PrologDB.toTable;
import PrologDB.TableSchema;
import PrologDB.Tuple;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
///imports

import java.util.function.Predicate;
import org.junit.Test;

///tmplate
public class DocExamplesTest {

    public static void main(String... args) throws Exception {
        // Step 1: boilerplate initialization
        Marquee2Arguments mark = new Marquee2Arguments(DocExamplesTest.class, ".do.pl", ".pet.pl", args);
        String inputFileName = mark.getInputFileName();
        String outputFileName = mark.getOutputFileName();
        String appName = mark.getAppName(outputFileName);

        // Step 2: read database
        DB db = DB.readDataBase(inputFileName);
        Table dog = db.getTableEH("dog");
        Table own = db.getTableEH("owner");
        //...

        // Step 3: read schema
        DBSchema dbs = DBSchema.readSchema("pet.schema.pl");
        DB petdb = new DB(appName, dbs);
        Table pet = petdb.getTableEH("pet");
        Table owner = petdb.getTableEH("owner");
        //...

        // Step 4:now translate databasses
        //...
        // Finally, write out pet database
        petdb.print(outputFileName);
    }
///tmplate

    static final String testdata = "test/DML/PrologDB/TestData/";
    static final String correct = "test/DML/PrologDB/Correct/";
    static final String errorfile = "error.txt";
    static final String testdir = "test/DML/SchemaTests/TestData/";

    public DocExamplesTest() {
    }

    @Test
    public void toTableTest() {
        RegTest.Utility.redirectStdOut(errorfile);
        toTable();
        RegTest.Utility.validate(errorfile, correct + "toTable.txt", false);
    }

///toTable
    void toTable() {
        DB db = DB.readDataBase(testdata + "dogOwner.do.pl");
        Table dog = db.getTableEH("dog");

        // Step 1: stream dog, and collect into a new dog table 
        Table dog2 = dog.stream().collect(new toTable(dog));
        System.out.format("dog2 %s dog", dog2.equals(dog) ? "=" : "!=");
    }
///toTable

    @Test
    public void GroupByTest() {
        RegTest.Utility.redirectStdOut(errorfile);
        GroupBy();
        RegTest.Utility.validate(errorfile, correct + "groupby.txt", false);
    }

///GroupBy
    void GroupBy() {
        DB db = DB.readDataBase(testdata + "dogOwner.do.pl");
        Table dog = db.getTableEH("dog");

        // Step 1: one way -- print directly to standard out
        dog.groupBy("breed").forEach(tb -> System.out.format("%d %s\n", tb.count(), tb.getFirst(p -> true).get("breed")));

        // Step 2: collect into a separate table and print that table
        Table result = new Table(new TableSchema("result").addColumns("count", "breed"));
        dog.groupBy("breed")
                .forEach(tb -> result.addTuple(tb.count() + "", tb.getFirst(p -> true).get("breed")));
        result.print(System.out);
    }
///GroupBy

    @Test
    public void testprintSchema() throws Exception {
        RegTest.Utility.redirectStdOut(errorfile);
        printSchema();
        RegTest.Utility.validate(errorfile, correct + "printSchema.txt", false);
    }

///printSchema
    void printSchema() throws Exception {
        // Step 1: read in ooschema and propagate attributes to convert it
        //         into a .schema file, so that it can be instantiated
        DBSchema s = DBSchema.readSchema(testdir + "starTrek.ooschema.pl");
        s.finishAndPropagateAttributes();

        // Step 2: easy way to print a schema
        s.print(System.out);

        // Step 3: harder, but customized
        System.out.format("\n=========\n\n");
        System.out.format("database %s has \n", s.getName());
        for (TableSchema t : s.getTableSchemas()) {
            System.out.format("   table %10s with columns ", t.getName());
            for (Column c : t.getColumns()) {
                String quote = c.isQuoted() ? "\'" : "";
                System.out.format("%s%s%s ", quote, c.getName(), quote);
            }
            System.out.format("\n");
        }
        System.out.format("\n");
        for (SubTableSchema st : s.getSubTableSchemas()) {
            TableSchema supr = st.getSuper();
            System.out.format("   table %10s has subtables ", supr.getName());
            for (TableSchema chld : st.getSubTableSchemas()) {
                System.out.format("%s ", chld.getName());
            }
            System.out.format("\n");
        }
    }
///printSchema

///schemaBuild
    DBSchema schemaBuild() throws Exception {
        // Step 1: when a schema is initially created, and until it is
        //         "finished", it is an .ooschema
        DBSchema trekSchema = new DBSchema("starTrek");

        //  Step 2: add tables
        TableSchema crewman = new TableSchema("crewman");
        crewman.addColumns("cid", "fname", "lname");
        TableSchema commander = new TableSchema("commander").addColumn("rank");
        TableSchema lieutenant = new TableSchema("lieutenant").addColumn("specialty");

        trekSchema.addTableSchemas(crewman, commander, lieutenant);

        //  Step 3: create the lone subtable declaration, and add to schema
        SubTableSchema sts = new SubTableSchema(crewman);
        sts.addSubTableSchemas(commander, lieutenant);
        trekSchema.addSubTableSchema(sts);

        // Step 4: seal or "finish" the schema so that no further editing of it 
        //         is possible; propagate supertable attributes to subtables.
        trekSchema.finishAndPropagateAttributes();

        // return the DBSchema "starTrek.schema.pl"
        return trekSchema;
    }
///schemaBuild

    @Test
    public void testSchemaBuild() throws Exception {
        RegTest.Utility.redirectStdOut(errorfile);
        System.out.format("\n\n START schema build \n\n");
        DBSchema trekSchema = schemaBuild();
        trekSchema.print(System.out);
        System.out.format("\n\n END schema build \n\n");
        RegTest.Utility.validate(errorfile, correct + "schemaBuild.txt", false);
    }

    @Test
    public void testDBread() throws Exception {
        RegTest.Utility.redirectStdOut(errorfile);
        DBread();
        RegTest.Utility.validate(errorfile, correct + "dbread.txt", false);
    }

///DBread
    void DBread() throws Exception {
        // Step 1: read a database given its file
        DB db = DB.readDataBase(testdata + "/enterprise.starTrek.pl");

        // Step 2: easy way to print a database
        db.print(System.out);

        // Step 3: a more customized way
        System.out.format("\n\n ===== \n\n");
        System.out.format("database %s contains:\n", db.getName());

        for (Table t : db.getTables()) {
            System.out.format("Table %s\n", t.getName());
            t.stream().forEach(tup -> tup.print(System.out));
            System.out.format("\n");
        }
    }
///DBread

    @Test
    public void testDBbuild() throws Exception {
        RegTest.Utility.redirectStdOut(errorfile);
        DBBuild();
        RegTest.Utility.validate(errorfile, correct + "dbBuild.txt", false);
    }

///DBBuild
    void DBBuild() throws Exception {
        // Step 1: build the starTrek schema and then instantiate it with 
        //         an empty database
        DBSchema trekSchema = schemaBuild();  // see schemaBuild() defn above
        DB enterprise = new DB("enterprise", trekSchema);

        // Step 2: create the spock tuple
        Table crewman = enterprise.getTableEH("crewman");
        Tuple spock = new Tuple(crewman);
        spock.setValues("c1", "mr", "spock");
        crewman.add(spock);

        //  Step 3: create the sulu tuple
        Table lieutenant = enterprise.getTableEH("lieutenant");
        Tuple sulu = new Tuple(lieutenant).setValues("c3", "hikaru", "sulo", "navigation");
        lieutenant.add(sulu);

        //  Step 4: create the kirk tuple
        Table commander = enterprise.getTableEH("commander");
        commander.add(new Tuple(commander).setValues("c2", "james", "kirk", "captain"));

        //  Step 5: now print database
        enterprise.print(System.out);
    }
///DBBuild

    @Test
    public void testTableRetrieve() {
        RegTest.Utility.redirectStdOut(errorfile);
        TableRetrieve();
        RegTest.Utility.validate(errorfile, correct + "tblRetrieve.txt", false);
    }

///TableRetrieve
    void TableRetrieve() {
        // Step 1: read the database containing desired table, and get the table
        DB db = DB.readDataBase(testdata + "/dogOwner.do.pl");
        Table dog = db.getTable("dog");

        // Step 2: retrieve all aussies -- version 1 tuple streams
        dog.stream().filter(t -> t.is("breed", "aussie"))
                .forEach(t -> t.print(System.out));
        System.out.println();

        // Step 3: retrieve all aussies -- version 2 tables
        dog.filter(t -> t.is("breed", "aussie")).print(System.out);

        // Step 4: retrieve all aussies that are blacktri's -- version 1 
        Stream<Tuple> aussies = dog.stream().filter(t -> t.is("breed", "aussie"));
        Stream<Tuple> cuteAussies = aussies.filter(t -> t.is("color", "blacktri"));
        cuteAussies.forEach(t -> t.print(System.out));
        System.out.println();

        // Step 5: another version, this time using Tables -- version 2
        dog.filter(t -> t.is("breed", "aussie") && t.is("color", "blacktri"))
                .print(System.out);
    }
///TableRetrieve

    @Test
    public void InheritanceRetrieval() {
        RegTest.Utility.redirectStdOut(errorfile);
        InheritanceRetrieve();
        RegTest.Utility.validate(errorfile, correct + "inhRetrieve.txt", false);
    }

///InheritanceRetrieve
    void InheritanceRetrieve() {
        // Step 1 : read the database and get the pet table
        DB db = DB.readDataBase(testdata + "/pets.petdb.pl");
        Table pet = db.getTableEH("pet");

        // Step 1: easy way: print all pets in pet table and subtables
        pet.print(System.out);

        // Step 2: a more verbose way:
        pet.getSchema().print(System.out);
        pet.tuples().forEach(t -> t.print(System.out));

        // Step 3: all pets whose name begins with "l" -- streaming version
        System.out.println("\nlist of all pets whose name starts with 'l'");
        pet.filter(t -> t.get("name").startsWith("l")).print(System.out);

        // Step 4: all pets whose name begins with "l" -- table version
        System.out.println("\nversion 2");
        Table shorterPet = pet.filter(t -> t.get("name").startsWith("l"));
        shorterPet.print(System.out);
    }
///InheritanceRetrieve

    @Test
    public void crossSchemaTest() {
        RegTest.Utility.redirectStdOut(errorfile);
        System.out.format("BEGIN cross schema tests \n\n");
        crossSchema();
        System.out.format("\nEND cross schema tests");

        RegTest.Utility.validate(errorfile, correct + "cross.txt", false);
    }

///crossSchema
    public void crossSchema() {
        // Step 1: although this is a database file, you can still use it
        //         to read its schema.  Then read and print the dog and 
        //         owner table schemas
        DBSchema dbs = DBSchema.readSchema(testdata + "/dogOwner.do.pl");
        TableSchema dog = dbs.getTableSchema("dog");
        TableSchema owner = dbs.getTableSchema("owner");

        dog.print(System.out);
        owner.print(System.out);

        // Step 2: here is where the cross-product of two schemas is taken
        TableSchema dXoSchema = dog.crossProduct(owner);
        dXoSchema.print(System.out);

        // Step 3: take cross product of all 3 schemas, 2 at a time
        TableSchema when = dbs.getTableSchema("when");
        when.print(System.out);
        TableSchema dogXwhen = dog.crossProduct(when);
        dogXwhen.print(System.out);
        TableSchema dogXwhenXowner = dogXwhen.crossProduct(owner);
        dogXwhenXowner.print(System.out);
    }
///crossSchema

    @Test
    public void JoinTest() {
        RegTest.Utility.redirectStdOut(errorfile);
        TableJoins();
        RegTest.Utility.validate(errorfile, correct + "join.txt", false);
    }

///TableJoins
    void TableJoins() {
        // Step 1: read the dog-owner database and get its tables to join
        DB db = DB.readDataBase(testdata + "/dogOwner.do.pl");
        Table dog = db.getTable("dog");
        Table when = db.getTable("when");
        Table owner = db.getTable("owner");

        // Step 2: join dog with when (over fields named "did"). Note: the 
        //         fields of dogXwhen table are renamed to when.did, when.oid, 
        //         when.date, dog.did, dog.name, dog.breed, and dog.color
        Table dogXwhen = dog.join("did", when, "did");
        dogXwhen.print(System.out);

        // Step 3: join dogXwhen with the ownere table
        Table dogXwhenXowner = dogXwhen.join("when.oid", owner, "oid");
        dogXwhenXowner.print(System.out);
    }
///TableJoins

    @Test
    public void selfJoinTest() {
        RegTest.Utility.redirectStdOut(errorfile);
        selfJoin();
        RegTest.Utility.validate(errorfile, correct + "join2.txt", false);
    }

///SelfJoins
    void selfJoin() {
        //  Step 1: as usual, read a database and get the dog table and
        //          copy it.
        DB db = DB.readDataBase(testdata + "/dogOwner.do.pl");
        Table dog = db.getTable("dog");
        Table dog2 = dog.copyForSelfJoins("dog2");

        // Step 2: join tables.  The resulting table has 8 fields:
        //         dog.did, dog.name, dog.breed, dog.color and
        //         dog2.did, dog2.name, dog2.breed, and dog2.color
        Table dogXdog2 = dog.join("did", dog2, "did");
        dogXdog2.print(System.out);
    }
///SelfJoins

    @Test
    public void filterTest() {
        RegTest.Utility.redirectStdOut(errorfile);
        filter();
        RegTest.Utility.validate(errorfile, correct + "ftest.txt", false);
    }

///filter
    public void filter() {
        DB db = DB.readDataBase(testdata + "/enterprise.starTrek.pl");
        Table crew = db.getTableEH("crewman");

        // Step 1: easy way using tables
        Predicate<Tuple> p = t -> t.get("fname").startsWith("h");
        crew.filter(p).print(System.out);

        // Step 2: more manual via loops
        crew.getSchema().print(System.out);
        for (Tuple t : crew.tuples()) {
            if (t.get("fname").startsWith("h")) {
                t.print(System.out);
            }
        }
        System.out.println();

        // Step 3: another way using streams
        crew.getSchema().print(System.out);
        crew.stream()
                .filter(t -> t.get("fname").startsWith("h"))
                .forEach(t -> t.print(System.out));
    }
///filter

    @Test
    public void conversion() {
        RegTest.Utility.redirectStdOut(errorfile);
        DB db = DB.readDataBase(testdata + "/enterprise.starTrek.pl");
        Table crew = db.getTableEH("crewman");

        Stream<Tuple> crewStream = crew.stream();

        List<Tuple> crewList = crewStream.collect(Collectors.toList());

        for (Tuple t : crewList) {
            t.print(System.out);
        }

        crewList.stream().forEach(t -> t.print(System.out));
        RegTest.Utility.validate(errorfile, correct + "cnv.txt", false);
    }

    @Test
    public void conformTest() {
        RegTest.Utility.redirectStdOut(errorfile);
        try {
            conform();
        } catch (Exception e) {
        }
        RegTest.Utility.validate(errorfile, correct + "cnf.txt", false);
    }

///conform
    public void conform() {
        // Step 1: initialize
        DB db = DB.readDataBase(testdata + "/ex.con.pl");
        Table contract = db.getTableEH("contract");
        ErrorReport er = new ErrorReport();

        // Step 2: evaluate the Both constraint, collecting offenses using tables
        contract.filter(t -> ((t.isNull("pid") && t.isNull("cid"))
                || (!t.isNull("pid") && !t.isNull("cid"))))
                .forEach(t -> er.add("Both constraint violated " + t.get("kid")));

        // Step 3: evaluate the Company constraint, collecting offenses using tables
        contract.filter(t -> (!t.isNull("cid") && t.getInt("value") < 500))
                .forEach(t -> er.add("company constraint violated " + t.get("kid")));

        // Step 4: evaluate the Person constraint, collecting offenses using streams
        contract.stream()
                .filter(t -> !t.isNull("pid") && t.getInt("value") > 100)
                .forEach(t -> er.add("person constraint violated " + t.get("kid")));

        // Step 5: finally, print all offenses; if there are any, the 
        //         errors are printed to System.out and a Error is thrown
        er.printReportEH(System.out);
    }
///conform

    @Test
    public void nestedLoop() {
        RegTest.Utility.redirectStdOut(errorfile);
        DB db = DB.readDataBase(testdata + "/dogOwner.do.pl");

        Table dog = db.getTableEH("dog");
        ErrorReport er = new ErrorReport();

        dog.stream()
                .filter(t -> dog.stream()
                .filter(x -> x.get("name").
                equals(t.get("name")))
                .count() > 1)
                .forEach(t -> er.add("name duplicate " + t.get("name")));

        try {
            er.printReportEH(System.out);
        } catch (RuntimeException e) {
            System.out.println("errors found");
        }
        System.out.println("DONE");
        RegTest.Utility.validate(errorfile, correct + "nl.txt", false);
    }

    @Test
    public void csvTest() {
        csvfile();
        RegTest.Utility.validate("simple.pl", correct + "simple.pl", false);
        RegTest.Utility.validate("simple.csv", "test/DML/PrologDB/TestData/simple.csv", false);
    }
///csvfile

    public void csvfile() {
        Table csv = Table.readTable("test/DML/PrologDB/TestData/simple.csv");
        csv.writeTable("simple.pl");
        csv.writeTable("simple.csv");
    }
///csvfile

    @Test
    public void CorresSortTest() {
        RegTest.Utility.redirectStdOut(errorfile);
        try {
            corresSort();
        } catch (Exception e) {
        }
        RegTest.Utility.validate(errorfile, correct + "corresSort.txt", false);
    }

    void corresSort() {
        TableSchema bucks = TableSchema.readSchema("buckeyes", "table(buckeyes,[id,fname,lname,position]).");
        TableSchema longs = TableSchema.readSchema("longhorns", "table(longhorns,[lid,'name',pos]).");
        Table buckeyes = new Table(bucks)
                .addTuple("b2", "Dwayne", "Haskins", "qb")
                .addTuple("b1", "Denzel", "Ward", "cb")
                .addTuple("b3", "Nick", "Bosa", "dl");
        Table longhorns = new Table(longs);
///corresp
        boolean debug = true;
        // Step 1: manual way
        for (Tuple b : buckeyes.tuples()) {
            Tuple l = new Tuple(longhorns);
            l.set("lid", b.get("id").replace("b", "l"));
            l.set("name", b.get("fname") + " " + b.get("lname"));
            l.set("pos", b.get("position"));
            if (debug) {
                l.isComplete();
            }
            longhorns.add(l);
        }
        longhorns.print();

        // Step 2: using correspondences
        longhorns.deleteAll();
        ColumnCorrespondence c = new ColumnCorrespondence()
                .add("lid", b -> b.get("id").replace("b", "l"))
                .add("name", b -> b.get("fname") + " " + b.get("lname"))
                .add("pos", "position");
        longhorns.addTuples(buckeyes, c);
        longhorns.print();
///corresp
///sort
        // sort the longhorn table (in place)
        longhorns.sort("lid", true);
///sort
        longhorns.print();
    }

}
