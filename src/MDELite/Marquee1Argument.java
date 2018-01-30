package MDELite;

/** general Marquee 1 argument programs */
public class Marquee1Argument extends MarqueeCommon {

    /** general Marquee for all Conformance programs 
     * standard usage:
        Marquee1Argument mark = new Marquee1Argument(THIS.class, ".a.b", args);
        String inputFileName = mark.getInputFileName();
        String AppName = mark.getAppName(inputFileName);
     * @param cls -- class object of the Conformance Program
     * @param inputFilePattern -- file pattern (".meta.pl") to match/process
     * @param args -- from main(args)
     */
    public Marquee1Argument(Class cls, String inputFilePattern, String args[]) {
        this.cls = cls;
        this.inputFilePattern = inputFilePattern;
        this.args = args;
    }
    
    /** to pacify whiney compiler */
    private Marquee1Argument() {}
   

    /**
     * display generic marquee for Conformance files
     * @param msg -- specialized error message to print, otherwise ""
     */
    public void displayMarquee(String msg) {
        System.out.format("\nUsage: %s X%s\n", cls.getName(), inputFilePattern);
        if(msg!=null) {
            System.out.format("\n       %s\n", msg);
        }
        System.exit(0);
    }

    /**
     * @return  name of the input file that is to be validated
     */
    @Override
    public String getInputFileName() {
        if (args.length == 0)
            displayMarquee(null);
        if ( args[0]!=null && !args[0].endsWith(inputFilePattern)) {
            displayMarquee(args[0] + " submitted as input");
        }
        return args[0];
    }

}
