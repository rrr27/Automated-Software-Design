package MDELite;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * a few utility routines used in tests, and error classes
 */
public class Utils {

    private static String MDELiteHome = null;   // set by first call to homeOfMDELite()

    /**
     * MDELite is distributed in a Jar, which is placed somewhere on a client's
     * disk. This method returns the directory in which the JAR file is stored,
     * which then permits access to files that are packaged in the same
     * directory with the JAR (like libpl/X.schema.pl files
     *
     * @return directory containing libpl/ directory
     */
    public static String MDELiteHome() {
        // Step 1: if this method was called before, return cached result
        if (MDELiteHome == null) {
            Class utils = Utils.class;
            String url = utils.getResource(utils.getSimpleName() + ".class").getPath();
            if (url.startsWith("file:/")) {
                // assume form is file:/C:/Java/JavaJars/SetDate.jar!/setdate/Main.class
                url = url.replace("file:/", ""); // remove file:/
                int dotJarPos = url.indexOf(".jar!");
                url = url.substring(0, dotJarPos);  // save everything including .jar!
                int lastSlash = url.lastIndexOf("/");
                url = url.substring(0, lastSlash + 1); // strip off everything after last slash
                int pastColon = url.indexOf(":") + 1;
                MDELiteHome = url.substring(pastColon); // save everyting past :/
            } else {
                // assume form is /C:/Users/don/Documents/NetBeansProjects/MDELite7/build/classes/PrologDB/Table.class
                int pastFirstColon = url.indexOf(':') + 1;
                url = url.substring(pastFirstColon);  // throw away ...: in url lead
                int MDELiteLoc = url.indexOf("MDELite");
                int pastFirstSlash = 0;
                for (int i = MDELiteLoc; i < MDELiteLoc + 12; i++) {
                    if (url.charAt(i) == '/') {
                        pastFirstSlash = i + 1;
                        break;
                    }
                }
                if (pastFirstSlash == 0) {
                    throw Error.toss(Error.noMDELiteHome);
                }
                MDELiteHome = url.substring(0, pastFirstSlash); // find MDELite.../ and throw away stuff to the right
            }
        }
        return MDELiteHome;
    }

    public static void main(String... args) {
        String home = MDELiteHome();
        System.out.format("Home = %s\n", home);
        File dhome = new File(home);
        for (File f : dhome.listFiles()) {
            System.out.println(f.getName());
        }

    }

    /**
     * converts explanation, where 2+char tokens that are UPPERCASE are replaced
     * with "%s"; the args fill in these as strings
     *
     * @param explanation -- Error of message in explanation form
     * @param args -- sequence of String arguments
     * @return Java formatted String
     */
    public static String makeString(String explanation, Object... args) {
        String[] tokens = explanation.split(" ");
        String format = "";

        for (String token : tokens) {
            if (token.length() != 1 && token.equals(token.toUpperCase())) {
                token = "%s";
            }
            format = format + token + " ";
        }
        String result = String.format(format.trim() + "\n", args);    //+"\n"
        return result;
    }

    /**
     * parameterize trims an explanation and then splits it into an array of
     * strings. If a string contains all capitals and it is 2 or more characters
     * long, this specified where an error parameter is to be substituted.
     *
     * @param explanation - is a parameterized message string
     * @return parameterized/tokenized explanation
     */
    public static String[] parameterize(String explanation) {
        String[] tokens = explanation.trim().split(" ");
        for (String token : tokens) {
            if (token.length() != 1 && token.equals(token.toUpperCase())) {
                token = null;
            }
        }
        return tokens;
    }

    /**
     * a filename is "x#y#z.q.r", where # denotes a windows (\) or unix (/)
     * directory marker, and "." denotes the usual "dot" in file names This
     * method returns a string array of a parsed file; in this case it would
     * return ["x","y","z","q","r"].
     *
     * see UtilsTest for examples
     *
     * @param filename -- name of file to examine
     * @return parsing of the filename into a string array
     */
    public static String[] parseFileName(String filename) {
        String newname = filename.replace('.', '@');
        newname = newname.replace('\\', '@');
        newname = newname.replace('/', '@');
        newname = newname.replace("@@", "@");
        String[] result = newname.split("@");
        return result;
    }

    /**
     *
     * @param longFileName -- a path to a file, with "/" separators
     * @return removes directories from longFileName
     */
    public static String shortFileName(String longFileName) {
        longFileName = longFileName.replace("\\", "/");
        int i = longFileName.lastIndexOf("/");
        if (i == -1) {
            return longFileName;
        } else {
            return longFileName.substring(i + 1);
        }
    }

    /**
     * given a parseFileName array ["x","y","z","q","r"], endsWith returns true
     * if its 2nd argument matches the postfix of the input array; ex args =
     * ["q","r"] will return true;
     *
     * see UtilsTest for examples
     *
     * @param result -- string array produced by parseFileName
     * @param args -- a list of strings to match with the end of result
     * @return true if args matches the latter part of result
     */
    public static boolean endsWith(String[] result, String... args) {
        int len = args.length;
        int len2 = result.length;
        for (int i = 0; i < len; i++) {
            String r = result[len2 - len + i];
            String s = args[i];
            if (!r.equals(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param lst -- list ending with name, schema, ext
     * @return -- name
     */
    public static String getName(String[] lst) {
        int len = lst.length;
        return lst[len - 3];
    }

    /**
     *
     * @param lst -- list ending with name, schema, ext
     * @return -- ext
     */
    public static String getExt(String[] lst) {
        int len = lst.length;
        return lst[len - 1];
    }

    /**
     *
     * @param lst -- list ending with name, schema, ext
     * @return -- schema
     */
    public static String getSchema(String[] lst) {
        int len = lst.length;
        return lst[len - 2];
    }

    public static void deleteFile(String name) {
        File f = new File(name);
        if (!f.delete()) {
            System.out.format("unable to delete file %s\n", name);
        }
    }
    
    public static void copyFile(String from, String to) {
        File source = new File(from);
        File dest = new File(to);
        try {
            Files.copy(source.toPath(),dest.toPath());
        } catch (IOException e) {
             throw Error.toss(Error.cantCopyFile, from, e.getMessage());
        }
    }
}
