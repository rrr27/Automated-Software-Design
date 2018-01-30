package Boot.MDELite;

import MDELite.Marquee2Arguments;
import MDELite.Utils;
import PrologDB.DB;
import PrologDB.DBSchema;
import PrologDB.ErrorReport;
import PrologDB.Table;
import PrologDB.Tuple;
import java.util.HashMap;

public class fsm2meta {

    private static int catCount = 0;
    private static int arrCount = 0;
    private static int pthCount = 0;

    private static DB fsmDb, metaDb;
    private static DBSchema metaSch;
    private static Table fsmNode, fsmEdge, metaDomain, metaArrow, metaPath;
    private static ErrorReport er;

    public static void main(String... args) {
        // Step 0: initialize variables
        catCount = arrCount = pthCount = 0;
        
        // Step 1: standard marquee processing
        Marquee2Arguments mark = new Marquee2Arguments(fsm2meta.class, ".fsm.pl", ".meta.pl", args);
        String inputFileName = mark.getInputFileName();
        String outputFileName = mark.getOutputFileName();
        String AppName = mark.getAppName(inputFileName);

        // Step 2: open the database and initialize error report
        fsmDb = DB.readDataBase(inputFileName);
        fsmNode = fsmDb.getTableEH("node");
        fsmEdge = fsmDb.getTableEH("edge");
        metaSch = DBSchema.readSchema(Utils.MDELiteHome() + "libpl/meta.schema.pl");
        metaDb = new DB(AppName, metaSch);
        metaDomain = metaDb.getTableEH("domain");
        metaArrow = metaDb.getTableEH("arrow");
        metaPath = metaDb.getTableEH("path");
        er = new ErrorReport();
        initNameXlator();

        // Step 4: create all categories -- 3 fields are given default values
        for (Tuple n : fsmNode.tuples()) {
            if (n.is("ntype", "state")) {
                Tuple dom = new Tuple(metaDomain);
                dom.setValues("d" + catCount++, n.get("text").replace("%", ""), "", "", "false");
                metaDomain.add(dom);
            }
        }
        
        // Step 5: create all arrows -- 1 field is given default value
        for (Tuple e : fsmEdge.tuples()) {
            if (e.is("etype", "arrow")) {
                // handle case where a single arrow has multiple names -- this
                // means that there are multiple tuples
                String ename = e.get("label").replace("%", "").replace(" ", "");
                String[] enames = ename.split(",");
                for (String en : enames) {
                    Tuple a = new Tuple(metaArrow);
                    a.setValues("a" + arrCount++, en, id2name.get(e.get("startid")), id2name.get(e.get("endid")), "");
                    metaArrow.add(a);
                }
            }
        }

        // Step 6: harvest notes
        for (Tuple p : fsmNode.tuples()) {
            if (p.is("ntype", "note")) {
                String contents = p.get("text");
                String id = p.get("nid");
                if (contents.startsWith("paths")) {
                    paths(id, contents);
                    continue;
                }
                if (contents.startsWith("arrows")) {
                    arrows(id, contents);
                    continue;
                }
                if (contents.startsWith("domains")) {
                    domains(id, contents);
                    continue;
                }
                er.add("unrecognizable note with contents %s", contents);
            }
        }

        // Step 7: write out database, report errors and quit
        er.printReportEH(System.out);
        metaDb.print(outputFileName);

    }

    private static HashMap<String, String> id2name, name2id;

    private static void initNameXlator() {
        id2name = new HashMap<>();
        name2id = new HashMap<>();
        for (Tuple n : fsmNode.tuples()) {
            String id = n.get("nid");
            switch (n.get("ntype")) {
                case "state":
                    String cont = n.get("text").replace("%", "");
                    id2name.put(id, cont);
                    name2id.put(cont, id);
                    break;
                case "note":
                    break;
//                case "init":
//                    er.add("node(%s...) is an init node, which is disallowed", id);
//                    break;
//                case "final":
//                    er.add("node(%s...) is a final node, which is disallowed", id);
//                    break;
//                case "point":
//                    er.add("node(%s...) is a point node, which is disallowed", id);
//                    break;
                default:
                    er.add("node(%s...) is an unrecognizable node and is disallowed", id);
                    break;
            }
        }
    }

    private static void domains(String id, String contents) {
        // format: domain=<name> [,ext=<ext>] [,temp] [,conform=<prg>]
        // Step 1: primitive error checking
        String parse[] = contents.split("%");
        if (!parse[0].startsWith("domains")) {
            er.add("processing of domains note failed; contents = %s", contents);
            return;
        }

        // Step 2: harvest tuple fields
        for (int i = 1; i < parse.length; i++) {
            String lparse[] = parse[i].replace(" ", "").split(",");
            String dom = null, ext = "", temp = "false", conform = null;
            for (String tag : lparse) {
                if (tag.equals("temp")) {
                    temp = "true";
                    continue;
                }
                if (tag.startsWith("domain=")) {
                    if (dom!=null)
                        er.add("Multiple specifications for %s",tag);
                    dom = tag.replace("domain=", "");
                    continue;
                }
                if (tag.startsWith("ext=")) {
                    if (!ext.equals(""))
                        er.add("Multiple specifications for s",tag);
                    ext = tag.replace("ext=", "");
                    continue;
                }
                if (tag.startsWith("conform=")) {
                    if (conform!=null)
                        er.add("Multiple specifications for s",tag);
                    conform = tag.replace("conform=", "");
                    continue;
                }
                er.add("path(%s,...) has unrecognizable attribute %s", id, contents);
            }

            // Step 3 now add this infomation to an existing domain tuple
            if (dom == null) {
                er.add("path(%s...) no domain attribute specified in %s", id, contents);
                return;
            }

            String copy = dom;
            Tuple t = metaDomain.getFirst(g -> g.is("name", copy));
            if (t == null) {
                er.add("node(%s...) domain note references non existent domain %s", id, dom);
            }
            t.set("temp", temp);
            t.set("ext", ext);
            if (conform == null)
                t.set("conformExecutable","");
            else
                t.set("conformExecutable", conform);
            if (ext.equals("pl") && conform == null) {
                er.add("path(%s...) no conformance executable specified for domain %s ", id, dom);
            }
            // tuple is automatically updated
        }
    }

    private static void arrows(String id, String contents) {
        // Step 1: primitive error checking
        String parse[] = contents.split("%");
        if (!parse[0].equals("arrows")) {
            er.add("processing of arrows note failed; contents = %s", contents);
            return;
        }

        // Step 2: harvest tuple information
        //         arrowname = program

        for (int i = 1; i < parse.length; i++) {
            String lparse[] = parse[i].replace(" ", "").split(",");
            for (String line : lparse) {
                String[] parsed = line.split("=");
                if (parsed.length != 2) {
                    er.add("node(%s,...) arrow note has unrecognizable structure %s", parse[i]);
                    continue;
                }
                String arr = parsed[0];
                String exe = parsed[1];
                Tuple at = metaArrow.getFirst(g -> g.is("name", arr));
                if (at == null) {
                    er.add("node(%s...) arrow note references a non-existent arrow %s", id, arr);
                    return;
                }
                at.set("javaExecutable", exe);
                // tuple now updated
            }
        }
    }


    private static void paths(String id, String contents) {
        // Step 1: primitive error checking
        String parse[] = contents.split("%");
        if (!parse[0].equals("paths")) {
            er.add("processing of paths note failed; contents = %s", contents);
            return;
        }

        // Step 2: harvest contents
        String pname, psequence;
        for (int i = 1; i < parse.length; i++) {
            String lparse[] = parse[i].split("=");
            if (lparse.length != 2) {
                er.add("node(%s...) unrecognized path structure", id);
                return;
            }
            pname = lparse[0];
            psequence = lparse[1];
            Tuple p = new Tuple(metaPath);
            p.setValues("p" + pthCount++, pname, psequence);
            metaPath.add(p);
        }
    }

}
