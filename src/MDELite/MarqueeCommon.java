package MDELite;

import static MDELite.Error.fileNoExist;
import java.io.File;

/** general Marquee for all Conformance programs */
abstract class MarqueeCommon {
    Class cls;
    String inputFilePattern;
    String args[];

    /** pacify whiney compiler */
    public MarqueeCommon() { }
    
    /**
     * 
     * @param cls -- class object of MDELite application
     * @param inputFilePattern -- input file pattern as in ".vpl.pl"
     * @param args -- command line args from main(args) of MDELite applicatoin
     */
    public MarqueeCommon(Class cls, String inputFilePattern, String args[]) {
        this.cls = cls;
        this.inputFilePattern = inputFilePattern;
        this.args = args;
    }

    
    /** 
     * error method that insists that the number of command-line arguments is i
     * @param i -- number of command line arguments
     */
    public void insist(int i) {
        if (args.length != i) 
            displayMarquee(null);
    }

    /**
     * @return -- input FILE to be validated
     */
    public File getInputFile() {
        File result = null;
        String filename = getInputFileName();
        try {
            result = new File(getInputFileName());
        } catch (Exception e) {
            throw Error.toss(fileNoExist, filename);
        }
        return result;
    }
    
    public abstract String getInputFileName();
    
    public abstract void displayMarquee(String msg);

    /**
     * @param filename - input file names are dir/dir/dir/X.schemaname.pl
     * @return -- name X of application
     */
    public String getAppName(String filename) {
        String shorten = filename.replace(inputFilePattern, "");
        String[] arr = Utils.parseFileName(shorten);
        return arr[arr.length - 1];
    }

}
