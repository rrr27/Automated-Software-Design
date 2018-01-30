package RegTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.LinkedList;
import org.junit.Assert;
import MDELite.Error;

/**
 * this is a set of methods that allow you to collect the output (system.out,
 * system.err) of a program run, and compare this output to that which is known
 * correct -- ie a regression test.
 */
public class Utility {

    /**
     * autopsy = line number at which files differ
     */
    public static int autopsy = -1;  


    /**
     * sort a file with name inFileName; sorted file output to file with
     * outFileName; eliminate is an array of patterns that are to be removed
     * from inFileName -- to eliminate irrelevant variances/differences
     * 
     * @param inFileName -- name of file to sort
     * @param outFileName -- name of sorted output file to be produced
     * @param eliminate -- array of patterns (regular expressions) to eliminate
     */
    private static void sortFile(String inFileName, String outFileName, String[] eliminate) {
        BufferedReader br;
        LinkedList<String> lines = new LinkedList<>();
        String line;

        try {
            FileReader in = new FileReader(inFileName);
            br = new BufferedReader(in);
            while (true) {
                line = br.readLine();
                if (line == null) {
                    break;
                }
                if (eliminate != null) {
                    for (String pattern : eliminate) {
                        line = line.replaceAll(pattern, "");
                    }
                }
                lines.add(line);
            }
            br.close();
        } catch(IOException e) {
            throw Error.toss(Error.ioerror,inFileName, e.getMessage());
        }
        try {
            Collections.sort(lines);
            PrintStream ps = new PrintStream(new File(outFileName));
            for (String myline : lines) {
                ps.println(myline);
            }
            ps.close();
        } catch (IOException e) {
            throw Error.toss(Error.ioerror, e.getMessage());
        }
    }

    /**
     * compare two files, named fileA and fileB, line by line.  eliminate is
     * an array of regular expression patterns to remove from each line before
     * comparing lines.  If all lines match, compareFiles returns silently.
     * otherwise Error is thrown with an explanation
     * 
     * @param fileA -- name of first file
     * @param fileB -- name of second file
     * @param fileMsg -- message reported when files are not the same
     * @param eliminate -- array of patterns (regular expressions) that are removed
     * from both files prior to comparing
     */
    private static void compareFiles(String fileA, String fileB, String fileMsg, String[] eliminate) {
        int lineNum = 0;
        String lineA = null, lineB = null;
        
        if (!new File(fileA).exists())
            throw Error.toss(Error.fileNoExist, fileA);
        
        if (!new File(fileB).exists())
            throw Error.toss(Error.fileNoExist, fileB);
        
        try {
            BufferedReader A = new BufferedReader(new FileReader(fileA));
            BufferedReader B = new BufferedReader(new FileReader(fileB));
            lineA = "";
            lineB = "";
            while (lineA.equals(lineB)) {
                lineNum++;
                lineA = A.readLine();
                lineB = B.readLine();
                if (lineA == null && lineB == null) {
                    return;
                }
                if ( (lineA == null && lineB != null) || (lineA != null && lineB == null) )
                    break;
                if (eliminate != null) {
                    for (String pattern : eliminate) {
                        lineA = lineA.replaceAll(pattern, "");
                        lineB = lineB.replaceAll(pattern, "");
                    }
                }
            }
            autopsy = lineNum;
        } catch (IOException e) {
            if (originalErr != null) {
                System.setErr(originalErr);
            }
            throw Error.toss(Error.ioerror,fileA + " or " +fileB, e.getMessage());
        }
        System.err.println();
        System.err.println(" output>" + lineA);
        System.err.println("correct>" + lineB);
        System.err.flush();
        Assert.fail("on line " + lineNum + ": " +fileMsg);
    }
    
    // usage: suppose a program outputs files A and B and produces text to StdOut.
    // if this program changes, we want to know if it still produces the correct version of these files
    // and has the same StdOut text.  This can be accomplished by the calls:
    //    regTest.redirectStdOut("stdout.txt");  (place output in file "stdout.txt"
    //    {run program};
    //    regTest.validate("stdout.txt", "correctStdOut.txt", false);  (false means don't sort before comparing files
    //    regTest.validate("A.txt", "correctA.txt", true);   (true means sort both files before comparing)
    //    regTest.validate("B.txt", "correctB.txt", true);
    // sorting before comparison is needed when output ordering may be different and non-consequential
    private static PrintStream originalOut = null;
    private static PrintStream originalErr = null;
    private static PrintStream outfile = null;
    private static PrintStream errfile = null;

    /**
     * init initializes Utility -- should be called but often is not
     */
    public static void init() {
        originalOut = null;
        originalErr = null;
        outfile = null;
        errfile = null;
    }

    /**
     * redirects standard output to file with name outputFile
     * @param outputFile -- name of file to which System.out is to be directed
     */
    public static void redirectStdOut(String outputFile) {
        originalOut = System.out;
        try {
            outfile = new PrintStream(outputFile);
            System.setOut(outfile);
        } catch (IOException e) {
            throw Error.toss(Error.ioerror,outfile,e.getMessage());
        }
    }

    /**
     * redirects standard error to file with name outputFile
     * @param outputFile -- name of file to which System.err is to be directed
     */
    public static void redirectStdErr(String outputFile) {
        originalErr = System.err;
        try {
            errfile = new PrintStream(outputFile);
            System.setErr(errfile);
        } catch (IOException e) {
            throw Error.toss(Error.ioerror,outputFile,e.getMessage());
        }
    }
    
    
    private final static String sortedOut = "sortedOut.txt";
    private final static String sortedCorrect = "sortedCor.txt";

    /**
     * done performs standard clean-up by closing files and
     * restoring standard out and standard err
     */
    public static void done() {
        if (outfile != null) {
            outfile.flush();
            outfile.close();
            outfile = null;
        }
        if (errfile != null) {
            errfile.flush();
            errfile.close();
            errfile = null;
        }
        if (originalOut != null) {
            System.out.flush();
            System.out.close();
            System.setOut(originalOut);
            originalOut = null;
        }
        if (originalErr != null) {
            System.err.flush();
            System.err.close();
            System.setErr(originalErr);
            originalErr = null;
        }
    }

    /**
     * verify that two files, whose names are outputFile and correctFile, by optionally 
     * sorting them, are identical; silent output means both files are identical
     * 
     * @param outputFile -- file that was produced
     * @param correctFile -- file that is known to be correct
     * @param sortedTest -- are files to be sorted prior to comparison?
     */
    public static void validate(String outputFile, String correctFile, boolean sortedTest) {
        validate(outputFile, correctFile, sortedTest, null);
    }

    /**
     * verify that two files, whose names are outputFile and correctFile, by optionally 
     * sorting them, and eliminating an array of regular expressions, are identical; 
     * silent output means both files are identical
     * 
     * @param outputFile -- file that was produced
     * @param correctFile -- file that is known to be correct
     * @param sortedTest -- are files to be sorted prior to comparison?
     * @param eliminate -- array of regular expressions to remove from both files prior to comparing them
     */
    public static void validate(String outputFile, String correctFile, boolean sortedTest, String[] eliminate) {

        String fileMsg = outputFile + " differs from " + correctFile;

        if (sortedTest) {
            sortFile(outputFile, sortedOut, eliminate);
            sortFile(correctFile, sortedCorrect, eliminate);
            outputFile = sortedOut;
            correctFile = sortedCorrect;
            fileMsg = fileMsg + "(compare sortedOut.txt with sortedCor.txt)";
            compareFiles(outputFile, correctFile, fileMsg, null);
            return;
        }
        compareFiles(outputFile, correctFile, fileMsg, eliminate);
    }
}
