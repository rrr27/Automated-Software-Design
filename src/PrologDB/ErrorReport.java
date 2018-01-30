package PrologDB;

import MDELite.Error;
import java.io.PrintStream;

/**
 * ErrorReport is a standard object to collect errors in evaluating database
 * constraints; it remembers a list of errors, and when its report is printed,
 * it throws Error if errors are reported
 *
 */
public class ErrorReport {

    /**
     * this is the string accumulator of errors; initially it is empty.
     */
    private String errorReport = "";
    private int errorCount = 0;

    /**
     * returns a fresh ErrorReport object
     */
    public ErrorReport() {
    }

    /**
     * add errorMsg to report string
     *
     * @param errorMsg -- errorMsg to report string
     */
    public void add(String errorMsg) {
        errorReport += errorMsg.replace("\n","") + "\n";
        errorCount++;
    }

    /**
     * add error message (fmt,args) to report string
     *
     * @param fmt -- format string
     * @param args -- arguments of format string
     */
    public void add(String fmt, Object... args) {
        errorReport += String.format(fmt + "\n", args);
        errorCount++;
    }
    /** for debugging -- add barrier in report without adding an error
     */
    public void addBarrier() {
        errorReport += "--------\n";
    }

    /**
     * print report to PrintStream out; if no errors are reported, printReport
     * returns; otherwise a Error is thrown
     *
     * @param out -- printstream to output report
     */
    public void printReportEH(PrintStream out) {
        if (printReport(out)) {
            throw Error.toss(Error.errorReport, errorCount + "");
        }
    }
    
    /**
     * print report to PrintStream out; if no errors are reported, printReport
     * returns; otherwise a Error is thrown
     *
     * @param out -- printstream to output report
     * @return true if there are errors
     */
    public boolean printReport(PrintStream out) {
        if (errorCount == 0) {
            return false;
        }
        out.println(errorReport);
        out.flush();
        return true;
    }
}
