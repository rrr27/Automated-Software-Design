package Allegory;

import LectureExamples.allegory.PDD;
import MDELite.Marquee2Arguments;
import PrologDB.DB;
import PrologDB.DBSchema;
import PrologDB.Table;
import PrologDB.TableSchema;

public class VPLSchema2Java extends MDELite.RunningBear {
    static String q = "\"";
    static DBSchema dbs;
    //static Table dual;
    static String appName;

    public static void main(String... args) {
        // Step 1: standard marquee processing
        Marquee2Arguments mark = new Marquee2Arguments(Vpl2Schema.class, ".schema.pl", ".java", args);
        RBSetup(mark, args);

        String inputFileName = mark.getInputFileName();
        String outputFileName = mark.getOutputFileName();
        appName = mark.getAppName(mark.getInputFileName());
        openOut("src/Allegory/" + outputFileName);

        // Step 2: open tables of database and create empty schema
        //         and read dual file
        dbs = DBSchema.readSchema(inputFileName);

        //dual = Table.readTable(appName + ".dual.csv");

        // Step 3: generate file

        header();
        mainClass(dbs);
        common();
        classes(dbs);
        footer();
        closeOut();
    }

    private static void header(){
        //add package and imports
        //TODO handle the addition in correct package
        l("package Allegory;");
        l();
        l("import PrologDB.DB;\n" +
                "import PrologDB.Table;\n" +
                "import PrologDB.TableSchema;\n" +
                "import PrologDB.Tuple;\n" +
                "import java.util.function.Consumer;\n" +
                "import java.util.function.Predicate;");
        l();

    }

    private static void footer(){
        //end the outer class
        l("}");

    }

    private static void mainClass(DBSchema dbSchema){
        //produces the main class
        //start the outer class
        l("public class %s {", dbSchema.getName());
        l();
        l("  DB db;");
        dbSchema.getTableSchemas().forEach(t -> l("  public Table %s;", t.getName()));
        l();
        l("  public %s(String fileName) {", dbSchema.getName());
        l("     db = DB.readDataBase(fileName);");
        dbSchema.getTableSchemas().forEach(t ->l("     %s = db.getTableEH(\"%s\");", t.getName(), t.getName()));
        l("  }");

    }

    private static void common(){
        //produces the abstract common
        l();
        l("  abstract class common <T extends common> {\n" +
                "///commonT\n" +
                "       Table table;\n" +
                "\n" +
                "       protected common() { }\n" +
                "\n" +
                "       protected common(String tableName, Tuple t) {\n" +
                "          TableSchema ts = t.getSchema();\n" +
                "          if (!ts.getName().equals(tableName)) \n" +
                "            throw new RuntimeException(\"assigning non-\"+tableName+\" table to \"+tableName);\n" +
                "          table = new Table(ts).add(t);\n" +
                "       }\n" +
                "\n" +
                "       protected common(String tableName, Table tab) {\n" +
                "          if (!tab.getSchema().getName().equals(tableName)) {\n" +
                "             throw new RuntimeException(\"assigning non-\"+tableName+\" table to \"+tableName);\n" +
                "          }\n" +
                "          table = tab;\n" +
                "        }\n" +
                "\n" +
                "       public T select(Predicate<Tuple> p) {\n" +
                "          Table result = table.filter(p);\n" +
                "          return New(result);\n" +
                "        }\n" +
                "\n" +
                "       protected abstract T New(Table t);\n" +
                "\n" +
                "       public T id() { return New(table); }\n" +
                "\n" +
                "       public void print() { table.print(System.out); }\n" +
                "\n" +
                "       public void forEach(Consumer<Tuple> action) { table.stream().forEach(t -> action.accept(t)); }\n" +
                "\n" +
                "       public T intersect(T tab) { return New(this.table.intersect(tab.table)); }\n" +
                "\n" +
                "       public int size() { return this.table.count(); }\n" +
                "\n" +
                "       public boolean equals(T tab) { return this.table.equals(tab.table); }\n" +
                //TODO work out union part for Table
                /*"\n" +
                "       public boolean union(T tab) { return this.table.union(tab.table); }\n" +*/

                "  }");

    }

    private static void classes(DBSchema dbSchema){
        //produces each class in schema in this
        dbSchema.getTableSchemas().forEach(t -> addClass(t));

    }

    private static void addClass(TableSchema tableSchema){
        l();
        l("  public class %s extends common<%s> {", tableSchema.getName(), tableSchema.getName());
        l();
        l("       protected %s New(Table t) { return new %s(t); }\n", tableSchema.getName(), tableSchema.getName());
        l();
        l("       protected %s () { table = %s; }", tableSchema.getName(), tableSchema.getName());
        l();
        l("       protected %s (Table t) { super(\"%s\", t); }\n", tableSchema.getName(), tableSchema.getName());
        l();
        l("       protected %s (Tuple t) { super(\"%s\", t); }\n", tableSchema.getName(), tableSchema.getName());
        l("  }");
        l();

    }
}
