package MDELite;

import static MDELite.Error.fileNoExist;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/** standard MDELite Marquee processing */
public class Marquee2Arguments extends MarqueeCommon {

    String outputFilePattern;

    /** standard usage:
        Marquee2Arguments mark = new Marquee2Arguments(THIS.class, ".a.b", ".c.d", args);
        String inputFileName = mark.getInputFileName();
        String outputFileName = mark.getOutputFileName();
        String appName = mark.getAppName(outputFileName);
     * @param cls -- class object of MDELite application
     * @param inputFilePattern -- one input pattern
     * @param outputFilePattern -- one output pattern
     * @param args -- command-line arguments from main(args)
     */
    public Marquee2Arguments(Class cls, String inputFilePattern, String outputFilePattern, String args[]) {
        super(cls, inputFilePattern, args);
        this.outputFilePattern = outputFilePattern;
    }

    /** create a marquee -- I think to passify a whiney compiler */
    public Marquee2Arguments() {
    }

    /**
     * display generic marquee
     * @param msg -- if there is a special error, provide it here; "" otherwise
     */
    public void displayMarquee(String msg) {
        if (msg != null) {
            System.out.format("\n---> %s\n", msg);
        }
        System.out.format("\nUsage: %s X%s [X%s]\n", cls.getName(), inputFilePattern, outputFilePattern);
        System.out.format(  "       translate X%s to X%s\n", inputFilePattern, outputFilePattern);
        System.out.format(  "       1st argument must conform to format X%s\n", inputFilePattern);
        System.out.format(  "       if 2nd argument is absent, output file is X%s\n", outputFilePattern);
        System.exit(0);
    }

    /**
     * @return string of input file name (the first argument)
     */
    @Override
    public String getInputFileName() {
        int len = args.length;
        if (len == 0) {
            displayMarquee(null);
        }
        if (!args[0].endsWith(inputFilePattern)) {
            displayMarquee(args[0] + " submitted as input");
        }
        return args[0];
    }

    /**
     * @return -- output file name (second argument)
     */
    public String getOutputFileName() {
        String result = "";
        int len = args.length;
        switch (len) {
            case 0 : displayMarquee(null); break;
            case 1 : result = Utils.shortFileName(getInputFileName().replace(inputFilePattern, outputFilePattern)); break;
            default : result = args[1];
        }
        return result;
    }

    /**
     * @param i -- index of command-line argument to return
     * @return argument # i
     */
    public String getArgument(int i) {
        switch (i) {
            case 0:
                return getInputFileName();
            case 1:
                return getOutputFileName();
            default:
                if (i < 0 || i >= args.length) {
                    displayMarquee("input index " + i + " requested and doesn't exist");
                } else {
                    return args[i];
                }
        }
        return null; // never reaches here; added to pacify whiney compiler
    }

    /**
     * @return PrintStream of outputFile
     */
    public PrintStream getOutputPrintStream() {
        PrintStream out = null;
        String outFile = getOutputFileName();
        try {
            out = new PrintStream(outFile);
        } catch (FileNotFoundException e) {
            throw Error.toss(fileNoExist, outFile);
        }
        return out;
    }
}
