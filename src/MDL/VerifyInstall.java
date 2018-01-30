package MDL;

import MDELite.ExeUtils;

/**
 * program that sees if it can invoke Violet
 */
public class VerifyInstall {

    /**
     * invokes violet from the command line; reports to System.err errors
     * and System.out if things go well
     * 
     * @param args -- ignored
     */
    public static void main(String[] args) {

        try {
//            ExeUtils.executeCommandLine("java", "MDL.Violet");
              Violet.main("","");
        } catch (Exception e) {
            System.out.println("Error: "+ e.getMessage());
            System.out.println("cannot execute Violet.Main");
            System.out.println("check to see that MDELiteX.jar is on your CLASSPATH");
            return;
        }

        // Should be ready to go!
        System.out.println("Violet should be running now.");
        System.out.println("If not, something is wrong.");  
        System.out.println("Otherwise, please close Violet,");
        System.out.println("and MDELite Ready to Use!");
    }
}
