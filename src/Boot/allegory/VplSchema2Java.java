package Boot.allegory;

import MDELite.Marquee2Arguments;
import MDELite.RunningBear;
import PrologDB.DBSchema;
import PrologDB.Table;

///start
public class VplSchema2Java extends RunningBear {

    static String q = "\"";
    static DBSchema dbs;
    static Table dual;
    static String appName;

    public static void main(String... args) {
        // Step 1: standard marquee processing
        Marquee2Arguments mark = new Marquee2Arguments(Vpl2Schema.class, ".schema.pl", ".java", args);
        String inputFileName = mark.getInputFileName();
        String outputFileName = mark.getOutputFileName();
        appName = mark.getAppName(mark.getInputFileName());
        openOut(outputFileName);
        appName = mark.getAppName(mark.getInputFileName());

        // Step 2: open tables of database and create empty schema
        //         and read dual file
        dbs = DBSchema.readSchema(inputFileName);
        dual = Table.readTable(appName + ".dual.csv");

        // Step 3: generate file
        header();
        mainClass();
        common();
        classes();
        footer();
    }
///start

    static void header() {
        System.out.println("program is stubbed -- real program not included in MDELite");
    }

    static void mainClass() {
        System.out.println("program is stubbed -- real program not included in MDELite");
    }

    static void common() {
        System.out.println("program is stubbed -- real program not included in MDELite");
    }

    static void classes() {
        System.out.println("program is stubbed -- real program not included in MDELite");
    }

    static void createAssociationMethod(String kind, String assocClassName, String tsIDName, String leftName, String rightName) {
        System.out.println("program is stubbed -- real program not included in MDELite");
    }

    static void createLoopMethod(String toClass, String methodName, String assocClassName, String thisClassIdName, String leftName, String rightName) {
        System.out.println("program is stubbed -- real program not included in MDELite");
    }

    public static void createArrow(String joinfield, String toClass, String arrowName) {
        System.out.println("program is stubbed -- real program not included in MDELite");
    }

    public static void createDual(String joinfield, String className, String arrowName, String dualName) {
        System.out.println("program is stubbed -- real program not included in MDELite");
    }

    static void footer() {
        System.out.println("program is stubbed -- real program not included in MDELite");
    }
}
