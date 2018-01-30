package MDELite;

import PrologDB.DB;
import java.io.PrintStream;

/**
 * RunningBear is a pure Java Model-to-Text tool to replace Velocity
 */
public abstract class RunningBear {

    /**
     * db is the current MDELite7 database db = DB.read(database);
     */
    public static DB db;
    private static String database;
    private static PrintStream out = System.out;
    private static Marquee2Arguments mark;

    /**
     * opens database and creates PrintStream for RunningBear output
     *
     * @param mark -- a Marquee object
     * @param args -- command-line arguments from main(args)
     */
    public static void RBSetup(Marquee2Arguments mark, String[] args) {

        // Step 2: open database, if necessary
        database = mark.getInputFileName();
        if (database != null && database.equals("null")) {
            database = null;
        }

        if (database != null) {
            try {
                db = DB.readDataBase(database);
            } catch (Exception e) {
                throw Error.toss(Error.ioerror, database, e.getMessage());
            }
        }

        // Step 3: open output file, if necessary
        openOut(mark.getOutputFileName());
    }

    /**
     * @return the name of the current database file
     */
    public static String getDatabase() {
        return database;
    }

    /**
     * @return the name of the output file
     */
    public static String getOutputFileName() {
        return mark.getOutputFileName();
    }

    /**
     * printline(format, arguments)
     *
     * @param fmt -- format
     * @param args -- argument list
     */
    public static void l(String fmt, Object... args) {
        out.format(fmt + "\n", args);
    }

    /**
     * print empty line
     */
    public static void l() {
        out.format("\n");
    }
    
    /** print n empty lines
     * @param n -- number of empty lines to print
     */
    public static void l(int n) {
        for (int i=1; i<=n; i++)
            l();
    }

    /**
     * print(format, arguments) -- no line eject
     *
     * @param fmt -- format
     * @param args -- argument list
     */
    public static void p(String fmt, Object... args) {
        out.format(fmt, args);
    }

    /**
     * send all further output to outputFile closeOut the current file (unless
     * it is System.out)
     *
     * @param outputFile -- the name of the output file for RunningBear
     */
    public static void openOut(String outputFile) {
        if (out != System.out) {
            closeOut();
        }
        try {
            out = new PrintStream(outputFile);
        } catch (Exception e) {
            throw Error.toss(Error.ioerror, outputFile, e.getMessage());
        }
    }
    
    /**
     * send all further output to outputFile closeOut the current file (unless
     * it is System.out)
     *
     * @param o -- new output stream for RB
     */
    public static void openOut(PrintStream o) {
        if (o != System.out) {
            closeOut();
        }
        out = o;
    }

    /**
     * closeOut the current output file (unless it is System.out)
     */
    public static void closeOut() {
        if (out != System.out) {
            out.close();
            out = System.out;
        }
    }
    
    /** return output printstream
     * 
     * @return output printstream
     */
    public static PrintStream getOut() {
        return out;
    }
}
