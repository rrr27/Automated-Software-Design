package CorrectResultsAfterChainingErrorMsgs;

import MDELite.ErrInt;
import MDELite.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class UpdateCorrectFiles {

    public static void main(String... args) {
        // Step 1: get past command line
        
        if (args.length != 0) {
            System.err.println("No arguments");
            return;
        }
        JOptionPane.showMessageDialog( null, "copy-by-refactoring Error + ParsE to CorrectResults...; Copy test/ to testBackup/" );

        
        // Step 2: see if there is a backup directory.  If not, stop.
        File bkup = new File("testBackup");
        if (!(bkup.exists() && bkup.isDirectory())) {
            System.err.println("backup directory does not exist.  Update terminated");
            return;
        }
        
        String tallyho = JOptionPane.showInputDialog("password to proceed");
        if (!tallyho.equals("tallyho!")) {
            System.err.println("password incorrect");
            return;
        }
        
        // Step 3: get the list of "Correct" directories in the "test" dir
        LinkedList<File> correctDirs = findCorrectDirectories();
        
        // Step 4: get String array from Error and Error and create linked list 
        //         of tokenized strings, and then update Correct files
        HashMap<String,String> newMsg = getEnumMessages(MDELite.Error.consult,false);
        HashMap<String,String[]> oldMsg = getTokenizedEnumMessages(Error.consult,false);
        updateCorrectTests(newMsg, oldMsg, correctDirs);
        
        // Step 5: do the same with ParsE and ParsE
       
        //newMsg = ParsE.getEnumMessages();
        //oldMsg = ParsE.getTokenizedEnumMessages();
        newMsg = getEnumMessages(MDELite.ParsE.parseError, true);
        oldMsg = getTokenizedEnumMessages(ParsE.parseError, true);
        updateCorrectTests(newMsg, oldMsg, correctDirs);
    }

    // used by UpdateCorrectFiles
    /**
     * truncates explanation by trimming it, and throwing away everything
     * after the first new line
     * @param explanation -- is a parameterized message string
     * @return trimmed explanation
     */
    public static String truncate(String explanation) {
        explanation = explanation.trim();
        int i = explanation.indexOf('\n');
        if (i == -1) {
            return explanation.trim();
        }
        return explanation.substring(0, i - 1);
    }
    
    static void updateCorrectTests(HashMap<String,String> newMsg, HashMap<String,String[]> oldMsg, LinkedList<File> correctDirs) {
        // Step 1: march through each file in a "Correct" directory and update it
        for (File cdir : correctDirs) {
            for (File f : cdir.listFiles()) {
                repair(f, newMsg, oldMsg);
            }
        }
    }
        
    public static LinkedList<File> findCorrectDirectories() {
        LinkedList<File> dirList = new LinkedList<>();
        
        File test = new File("test");
        for (File testPackage : test.listFiles()) {
            if (!testPackage.isDirectory()) {
                continue;
            }
            for (File subdir : testPackage.listFiles()) {
                if (!subdir.isDirectory()) {
                    continue;
                }
                if (subdir.getName().equals("Correct")) {
                    dirList.add(subdir);
                }
            } // end subdir list
        } // end testPackage
        
        return dirList;
    }

    public static void repair(File f, HashMap<String,String> newMsg, HashMap<String,String[]> oldMsg) {
        // Step 1: read each line of file f as a string into a linked list
        LinkedList<String> lines = readFileIntoList(f);
        LinkedList<String> newLines = new LinkedList<>();
        
        // Step 2: for each line, repair it (or leave it unchanged).  Add this
        //         to the newLines list
        for (String line : lines) {
            String newLine = repairLine(line, newMsg, oldMsg);
            newLines.add(newLine);
        }
        
        // Step 3: write out the new version of the file.
        try {
            PrintStream out = new PrintStream(f);
            for (String l : newLines) {
                out.println(l);
            }
            out.close();
        } catch (FileNotFoundException ex) {
            throw Error.toss(Error.fileNoExist, f.getName());
        }
        // Step 4: Done!
    }
    
    static LinkedList<String> readFileIntoList(File f) {
        LinkedList<String> lines = new LinkedList<>();
        try {
            LineNumberReader br = new LineNumberReader(new InputStreamReader(new FileInputStream(f)));
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();
        } catch (FileNotFoundException ex) {
            throw Error.toss(Error.fileNoExist, f.getName());
        } catch (IOException ex) {
            throw Error.toss(Error.ioerror, f.getName(), ex.getMessage());
        }
        return lines;
    }
    
    public static String repairLine(String line, HashMap<String,String> newMsg, HashMap<String,String[]> oldMsg) {
        // Step 1: split the line into tokens
        
        String[] tokens = line.trim().split(" ");
       
        // Step 2: get the starting sequence of blanks + the ending sequence of blanks
        int start = line.indexOf(tokens[0]);
        String last = tokens[tokens.length-1];
        int end   = line.lastIndexOf(last)+last.length();
        String starting = line.substring(0,start);
        String ending   = line.substring(end);
        
        // Step 3: now see if we can match this array of tokens with the tokens 
        //         of any old message.  Literally consider every old message
        //         to see if it matches
        for (String ename : oldMsg.keySet()) {
            String[] oldPattern = oldMsg.get(ename);
            String[] args = match(oldPattern,tokens);
            if (args!=null) {
                String newPattern = newMsg.get(ename);
                switch(args.length) {
                    case 1 : return (starting + Utils.makeString(newPattern, args[0])+ ending).replace("\n",""); 
                    case 2 : return (starting + Utils.makeString(newPattern, args[0], args[1])+ ending).replace("\n","");
                    case 3 : return (starting + Utils.makeString(newPattern, args[0], args[1], args[2])+ ending).replace("\n","");
                    case 4 : return (starting + Utils.makeString(newPattern, args[0], args[1], args[2], args[3])+ ending).replace("\n","");
                    case 5 : return (starting + Utils.makeString(newPattern, args[0], args[1], args[2], args[3], args[4])+ ending).replace("\n","");
                    case 6 : return (starting + Utils.makeString(newPattern, args[0], args[1], args[2], args[3], args[4], args[5])+ ending).replace("\n","");
                    default : throw Error.toss(Error.tooManyArguments, newPattern);
                }
            }
        }
        return line;
    }
    
    public static String[] match(String[] oldPattern, String[] tokens) {
        // Step 1: we have to collect all arguments of the old pattern
        //         if there is a match -- an argument is LIKETHIS all
        //         upper case special care is needed for an argument 
        //         that terminates an oldPattern
        LinkedList<String> args = new LinkedList<>();
        
        // Step 2: look only as far as the shorter of the two patterns
        int oldPatternLen = oldPattern.length;
        int tokensLen     = tokens.length;
        if (oldPatternLen>tokensLen)
            return null;
        int min = Math.min(oldPatternLen, tokensLen);
        for (int i = 0 ; i<min; i++) {
            if (oldPattern[i].length()==1) {
                if (oldPattern[i].equals(tokens[i])) {
                    continue;  // must be the same in this case
                }
                return null;  // nothing changes
            }
            if (oldPattern[i].equals(oldPattern[i].toUpperCase())) {
                args.add(tokens[i]);
                continue;  // don't compare in case of %s
            }
            if (oldPattern[i].equals(tokens[i])) {
                continue;
            }
            return null;   // nothing changes
        }
        // Step 3: everything matches up to their common length, which is at
        //         least oldPatternLength.  For the remaining tokens, 
        //         just append them.
        String theLast = args.removeLast();
        for (int j = min; j<tokensLen; j++) {
            theLast = theLast + " " + tokens[j];
        }
        args.add(theLast);
        
        // Step 4: return arguments
        return args.toArray(new String[0]);
    }
    
    static String[] getArgs(String[] oldPattern) {
        LinkedList<String> result = new LinkedList<>();
        for (String s : oldPattern) {
            if (s.length()==1)
                continue;
            if (s.equals(s.toUpperCase())) {
                result.add(s);
            }
        }
        return result.toArray(new String[0]);
    }
         
    
    static <T> HashMap<String,String[]> getTokenizedEnumMessages(ErrInt<T> t, boolean atLine) {
        HashMap<String,String[]> map = new HashMap<>();
        for (ErrInt<T> e : t.vals()) {
            String[] x = truncate(e.getMsg()).split(" ");
            map.put(e.name(),x);
            if (atLine){
                x = truncate("at line XX " +e.getMsg()).split(" ");
                map.put(e.name()+"LINES",x);
            }
        }
        return map;
    }
    
    
   static HashMap<String,String> getEnumMessages(ErrInt t, boolean atLine) {
        HashMap<String,String> map = new HashMap<>();
        for (ErrInt e : t.vals()) {
            map.put(e.name(),truncate(e.getMsg()));
            if (atLine)
                map.put(e.name()+"LINES",truncate("at line XX " + e.getMsg()));
        }
        return map;
    }

}
