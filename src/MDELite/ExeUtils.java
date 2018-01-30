package MDELite;

import java.io.*;

/**
 * a set of Java utilities to run command-line arguments
 * 
 */
public class ExeUtils {
    static final String errorFile = "error.txt";

    /** 
     * spans a process for the given command line, and doesn't wait to finish
     * @param cmdarray-- command line, parsed as a string array
     * @return Process object of spawned process
     * @throws Exception -- whatever ProcessBuilder complains about
     */
    public static Process spawnCommandLine(String... cmdarray) throws Exception {
        return new ProcessBuilder(cmdarray).start();
    }

    // original design of execute
    /**
     * spawns a process for the given command line and waits for it to finish
     * @param cmdarray -- command line, parsed as a string array
     * @throws Exception -- whatever ProcessBuilder or FileIO complains about
     */
    public static void executeCommandLine(String... cmdarray) throws Exception {
        String line;

        // Step 1: execute the command array, get handles on standard error, standard out, and wait until process is finished.
        //         er is a reader to the process error output
        //         in is a reader to the process standard output
        //        Process p = new ProcessBuilder(cmdarray).start();
        Process p = spawnCommandLine(cmdarray);
        BufferedReader er = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        p.waitFor();

        // Step 2: relay all process standard output to sdtout
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        in.close();

        // Step 3: drain process standard error into Globals.errorFile, provided that there is something to output
        boolean error = false;
        if ((line = er.readLine()) != null) {
            PrintStream ps = new PrintStream(errorFile);
            do {
                ps.format("%s\n", line);
            } while ((line = er.readLine()) != null);
            ps.flush();
            ps.close();
            error = true;
        }
        er.close();

        // Step 4: destroy the process
        p.destroy();

        // Step 5: if there was error output, throw an exception.
        if (error) {
            throw Error.toss(Error.consult,errorFile);
        }
    }
}
