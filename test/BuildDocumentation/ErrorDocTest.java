package BuildDocumentation;

import MDELite.Error;
import MDELite.ParsE;
import org.junit.Test;


public class ErrorDocTest {
    static String l1;
    static String l2;
    static String contents;
    
    public static void main(String templateFile, String outputHtmlFile) {
        if (!templateFile.endsWith(".htm") || !templateFile.startsWith("DocGen/")) {
           throw new RuntimeException("template file \'" + templateFile + "' does not end in .htm or starts with DocGen/");
        }
        if (!outputHtmlFile.endsWith(".html")|| !outputHtmlFile.startsWith("Docs/")) {
            throw new RuntimeException("output file \'" + outputHtmlFile + "' does not end in .htm or starts with Docs/");
        }
        harvestTemplate(templateFile);
        replaceMarker("Errors",errorEnum());
        replaceMarker("PErrors",parseErrorEnum());
        
        BuildDocTest.exhale(contents, outputHtmlFile);
    }
    
    static String findit = "monospace; width: ";
    public static void harvestTemplate(String filename) {
        contents = BuildDocTest.inhale(filename);
        int loc1 = contents.indexOf(findit) + findit.length();
        l1 = contents.substring(loc1, loc1+3);

        String c1 = contents.substring(loc1+4);
        
        int loc2 = c1.indexOf(findit)+ findit.length();
        l2 = c1.substring(loc2,loc2+3);
    }
    
    static void replaceMarker(String Marker, String replacement) {
        String mkr = "<!---"+Marker+"--->";
        contents = contents.replace(mkr, replacement);
    }
    
    
    static String errorEnum() {
        String accum = "";
        for(Error e : Error.values()) {
            String ename = e.toString();
            String emsg  = e.getMsg();
            accum = accum + newRow(ename,emsg);
        }
        return accum;
    }
    
    static String optional = "[at LINENUMBER ] ";
    static String parseErrorEnum() { 
        String accum = "";
        for(ParsE e : ParsE.values()) {
            String ename = e.toString();
            String emsg  = optional + e.getMsg();
            accum = accum + newRow(ename,emsg);
        }
        return accum;
    }
    
    static String newRow(String ename, String emsg) {
        String accum = "";
        accum = String.format("    <tr>\n");
        accum += String.format("      <td style=\"font-family: Courier New,Courier,monospace; width: %spx; font-weight: bold;\">%s</td>\n",l1,ename);
        accum += String.format("      <td style=\"font-family: Courier New,Courier,monospace; width: %spx; font-weight: bold;\">%s</td>\n",l2,emsg);
        accum += String.format("    </tr>\n");
        return accum;
    }
    
    @Test
    public void test1() {
        main("DocGen/ErrorTemplate.htm", "Docs/ErrorListings.html");
    }

}
