package MDL;

import MDELite.ExeUtils;

/**
 * MDL program that invokes Violet
 */
public class Violet {

    /**
     * invokes violet via process spawning
     * 
     * @param args -- ignored
     * @throws Exception -- whatever ExeUtils complains about
     */
    public static void main(String... args) throws Exception {
        Process p = ExeUtils.spawnCommandLine("java", "com.horstmann.violet.UMLEditor");
        if (args.length != 0) {
            p.waitFor();
        }
    }
}
