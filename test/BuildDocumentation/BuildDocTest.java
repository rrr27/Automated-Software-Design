package BuildDocumentation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map.Entry;
import org.junit.Test;

public class BuildDocTest {

    // this is a (marker, contents) hash map filled in by harvest()
    static HashMap<String, String> harvested = null;

    /**
     * create .html file outputHtml given a .htm BuildDocTest and the
     * sourceCodeFile (typically .java) from which to extract code. A marker is
     * " ---NAME--- " in an .htm template file A marker is "///NAME " in a
     * sourcecode file
     *
     * @param sourceCodeFileName -- file that has named begin+end markers --
     * doesn't have to be a .java file
     * @param templateFile --- .htm file with named markers (where source code
     * is to be inserted)
     * @param outputHtml -- output .html file
     */
    public void main(String sourceCodeFileName, String templateFile, String outputHtml) {
        if (!templateFile.endsWith(".htm")) {
            throw new RuntimeException("template file \'" + templateFile + "' does not end in .htm");
        }
        if (!(outputHtml.endsWith(".html") || outputHtml.endsWith(".htm"))) {
            throw new RuntimeException("output file \'" + outputHtml + "' does not end in .htm/.html");
        }

        harvestCode(sourceCodeFileName); // fills in harvest
        String htmlFile = inhale(templateFile); // fills in results
        for (Entry<String, String> e : harvested.entrySet()) {
            String key = "---" + e.getKey() + "---";
            if (htmlFile.contains(key)) {
                htmlFile = htmlFile.replace(key, e.getValue());
            }
        }
        exhale(htmlFile, outputHtml);
    }

    /**
     * scan sourceCodeFileName for ///Name ... ///Name parentheses whatever is
     * in between associate that with the contents of marker NAME and place into
     * HashMap harvested
     *
     * @param sourceCodeFileName -- name of source code file to scan
     */
    static void harvestCode(String sourceCodeFileName) {
        harvested = new HashMap<>();
        LineNumberReader br;
        String markerName = null;
        String accum = null;
        int lineno = -1;
        try {
            br = new LineNumberReader(new InputStreamReader(new FileInputStream(new File(sourceCodeFileName))));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("///")) {
                    // first time through, get marker name and init accum
                    if (markerName == null) {
                        markerName = line.replace("///", "").trim();
                        accum = "";
                        continue;
                    } else {
                        // second time through erase marker name and save accum
                        harvested.put(markerName, accum);
                        markerName = null;
                        accum = null;
                        continue;
                    }
                }
                lineno = br.getLineNumber();

                if (markerName != null) {
                    accum = accum + line + "<br>";
                }
            }
            br.close();
        } catch (Exception e) {
            throw new RuntimeException("unable to parse " + sourceCodeFileName + " around line " + lineno + "\n" + e.getMessage());
        }
    }

    /**
     * return the String that is the content of the template file
     *
     * @param templateFileName -- path to template file
     * @return -- string that is the contents of the Ht
     */
    public static String inhale(String templateFileName) {
        LineNumberReader reader = null;
        String accum = "", line, pattern;
        try {
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(new File(templateFileName))));
            while ((line = reader.readLine()) != null) {
                String[] toks = line.split(":::");
                boolean odd = true;
                for (String s : toks) {
                    if (odd) {
                        accum += s + " ";
                    } else {
                        String contents = readFile(s);
                        accum = accum.trim() + contents + " ";
                    }
                    odd = !odd;
                }
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("unable to read " + templateFileName + " at line " + reader.getLineNumber()
                    + "\n" + e.getMessage());
        }
        return accum;
    }

    static String readFile(String filename) {
        String accum = "", line;
        try {
            LineNumberReader br = new LineNumberReader(new InputStreamReader(new FileInputStream(new File(filename))));
            while ((line = br.readLine()) != null) {
                accum += line + "\n";
            }
            br.close();
        } catch (Exception e) {
            throw new RuntimeException("unable to read " + filename + "\n" + e.getMessage());
        }
        return accum.trim();
    }

    /**
     * write out string data to file HtmlFileName
     *
     * @param data -- string of file contents
     * @param HtmlFileName -- name of file to write
     */
    static void exhale(String data, String HtmlFileName) {
        try {
            PrintStream out = new PrintStream(HtmlFileName);
            out.println(data);
            out.close();
        } catch (Exception e) {
            throw new RuntimeException("unable to write " + HtmlFileName + "\n" + e.getMessage());
        }
    }

    @Test
    public void m2m() {
        main("test/DML/PrologDB/DocExamplesTest.java", "DocGen/M2MTemplate.htm", "Docs/M2MPrograms.html");
    }

    @Test
    public void constraints() {
        main("test/DML/PrologDB/DocConstraintExamplesTest.java", "DocGen/ConstraintTemplate.htm", "Docs/ConstraintPrograms.html");
    }

    @Test
    public void families() {
        main("src/LectureExamples/FamiliesM2M.java", "DocGen/FamiliesDemo.htm", "Docs/FamiliesDemo.html");
    }

    @Test
    public void allegories() {
        main("src/LectureExamples/allegory/Main.java", "DocGen/AllegoryDemo.htm", "DELETE.htm");
        main("src/LectureExamples/allegory/PDD.java", "DELETE.htm", "DELETE1.htm");
        main("test/LectureExamples/Correct/x.PDD.pl", "DELETE1.htm", "Docs/AllegoryDemo.html");
    }

    @Test
    public void violet() {
        violetSetup();
        main("test/DML/PrologDB/DocConstraintExamplesTest.java", "DocGen/VioletManualTemplate.htm", "Docs/VioletManual.html");
    }

    void violetSetup() {
        Violett.ClassParser.main("test/BuildDocumentation/Test/StudentCourse.class.violet", "outViolet.txt");
    }

    @Test
    public void yuml() {
        yumlSetup();
        main("test/BuildDocumentation/test/nothing.jav", "DocGen/YumlManualTemplate.htm", "Docs/YumlManual.html");
    }

    void yumlSetup() {
        Yuml.ClassParser.main("test/BuildDocumentation/Test/x.yuml.yuml", "outYuml1.txt");
        Yuml.ClassParser.main("test/BuildDocumentation/Test/y.yuml.yuml", "outYuml2.txt");
    }

    @Test
    public void manual() throws Exception {
        manualSetup();
        main("test/BuildDocumentation/test/nothing.jav", "DocGen/MDELiteOverviewTemplate.htm", "Docs/MDELiteOverview.html");
    }

    void manualSetup() throws Exception {
        PrintStream save = System.err;
        System.setErr(new PrintStream("outMan.txt"));
        schoolConform.main("test/BuildDocumentation/test/my.school.pl");
        System.setErr(save);
    }

    @Test
    public void rb() throws Exception {
        main("test/RunningBear/demo.java", "DocGen/M2TTemplate.htm", "Docs/M2TOverview.html");
    }

    @Test
    public void boot() throws Exception {
        main("test/BuildDocumentation/test/nothing.jav", "DocGen/BootStrappingTemplate.htm", "Docs/BootStrapping.html");
    }
    
    @Test
    public void index() throws Exception {
        main("test/BuildDocumentation/test/nothing.jav", "DocGen/indexTemplate.htm", "Docs/index.html");
    }

}
