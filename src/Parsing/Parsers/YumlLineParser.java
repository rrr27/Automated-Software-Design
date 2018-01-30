package Parsing.Parsers;

import MDELite.ParsE;
import Parsing.GeneralPrimitives.Nothing;
import Parsing.YumlPrimitives.YumlLine;
import PrologDB.DB;
import PrologDB.Table;
import PrologDB.Tuple;
import java.util.LinkedList;

/**
 * this is a simple line-parser for database and schema specifications Generally
 * this class is internal to PrologDB package and should not be called
 * externally
 */
public class YumlLineParser {

    /**
     * turn Debug on if you want to see stack trace on Errors
     */
    static boolean Debug = false;

    static int boxCounter = -1, assocCounter = -1;
    
    public static void init() { 
        boxCounter = 0;
        assocCounter = 0;
    }

    public static DB parseYumlLine(String line, int lineno, DB db) {
        // Step 0: precondition -- YumlLineParser must be initialized
        if (boxCounter < 0 || assocCounter < 0) {
            throw ParsE.toss(ParsE.yumlLineParser,-1,(Object) null);
        }

        // Step 1: parse the line
        LineToParse l = new LineToParse(line, lineno);
        YumlLine st = new YumlLine(l);
        st.parse();

        // Step 2: unpack parsing (schemaName, list-of-tables)
        LinkedList<String> list = l.parseList;
        Table box = db.getTableEH("yumlBox");
        Tuple c1 = ParseBox(l.getLineNo(), list, box);

        // Step 3: do we proceed to interpret Connect?
        if (!list.isEmpty()) {
            Table assoc = db.getTableEH("yumlAssociation");
            Tuple a = ParseAssociation(lineno, list, assoc);
            Tuple c2 = ParseBox(lineno, list, box);
            a.set("box1", c1.get("id"));
            a.set("box2", c2.get("id"));
            assoc.add(a);
        }
        return db;
    }

    public static String helper(LinkedList<String> lst) {
        String head = lst.removeFirst();
        if (head.equals(Nothing.tok)) {
            return "";
        }
        return head;
    }

    public static Tuple ParseBox(int lineno, LinkedList<String> list, Table box) {
        // Step 1: harvest contents
        String boxName = helper(list);
        String fields = helper(list);
        String methods = helper(list);

        // Step 2: classify tuple according to its name
        String type = "c";
        if (boxName.startsWith("interface")) {
            boxName = boxName.replace("interface", "").trim();
            type = "i";
        } else if (boxName.startsWith("note:")) {
            boxName = boxName.replace("note:", "").trim();
            type = "n";
        }
        String bn = boxName;  // bn is final
        
        // Step 3: now add the tuple's contents to the table
        Tuple c = box.getFirst(r -> r.is("name", bn));
        if (c == null) {
            // first time to define box
            c = new Tuple(box);

            c.setValues("b" + boxCounter++, type, boxName, fields, methods);
            box.add(c);
        } else {
            // seen before -- if so, if methods = fields = "" do nothing
            if (methods.equals("") && fields.equals("")) {
                return c;
            }
            // seen before -- but methods or fields is non-empty; that means
            // both fields were empty before
            if (c.is("methods", "") && c.is("fields", "")) {
                // fill them in
                c.set("methods", methods);
                c.set("fields", fields);
            } else {
                throw ParsE.toss(ParsE.yumlDuplicateBoxDecl, lineno, boxName);
            }
        }
        return c;
    }

    public static Tuple ParseAssociation(int lineno, LinkedList<String> list, Table assoc) {
        // Step 1: harvest connect (Connect = [End1] [name] Dash [name] {End2] Box End)
        String end1 = helper(list);
        String role1 = helper(list);
        String lineType = list.removeFirst();
        String role2 = helper(list);
        String end2 = helper(list);

        // Step 2: add association tuple
        Tuple a = new Tuple(assoc);
        a.set("id", "a"+assocCounter++);
        a.set("end1", end1);
        a.set("role1", role1);
        a.set("lineType", lineType);
        a.set("role2", role2);
        a.set("end2", end2);
        // Step 3: tuple a is incomplete -- must add box names
        return a;
    }
}
