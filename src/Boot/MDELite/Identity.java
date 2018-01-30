package Boot.MDELite;

import MDELite.Marquee2Arguments;

/** simple identity transformation for meta files. */
public class Identity {
    
    /** do nothing meta transformation -- just check arguments
     * @param inputPattern -- pattern of both input and output file ex ".meta.pl"
     * @param args -- normal "X.meta.pl"; if 2nd argument must be "X.meta.pl" too
     */
    public static void main(String inputPattern, String... args) {
        // Step 1: standard marquee processing
        Marquee2Arguments mark = new Marquee2Arguments(Identity.class, inputPattern, inputPattern, args);
        String inputFileName = mark.getInputFileName();
        String outputFileName = mark.getOutputFileName();
        String AppName1 = mark.getAppName(inputFileName);
        String AppName2 = mark.getAppName(outputFileName);
        if (AppName1.equals(AppName2)) {
        } else {
            String msg = String.format("input and output files must be the same (%s,%s).",inputFileName, outputFileName);
            mark.displayMarquee(msg);
        }
    }
}
