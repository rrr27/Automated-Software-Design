package Violett;

import static MDELite.Error.violetPropertyUnexpected;
import PrologDB.Tuple;
import MDELite.Error;
import MDELite.Marquee2Arguments;
import static MDELite.RunningBear.RBSetup;

/** Unparser of X.vpl.pl files to X.class.violet */
public class ClassUnParser extends MDELite.RunningBear {

    static String NewLine = "%";
    static int numPoints;
    
    /** Unparser of X.vpl.pl files to X.class.violet
     * @param args -- X.vpl.pl X.class.violet
     */
    public static void main(String... args) {
        numPoints = 0;

        // Step 0: standard marquee processing
        Marquee2Arguments mark = new Marquee2Arguments(ClassUnParser.class, ".vpl.pl", ".class.violet", args);
        RBSetup(mark, args);

        // Step 1: output header
        l("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        l("<java version=\"1.8.0_92\" class=\"java.beans.XMLDecoder\">");
        l(" <object class=\"com.horstmann.violet.ClassDiagramGraph\">");

        // Step 3: output classes
        for (Tuple c : db.getTable("vBox").tuples()) {
            switch (c.get("type")) {
                case "c":
                    makeClass(c);
                    break;
                case "i":
                    makeInterface(c);
                    break;
                case "n":
                    makeNote(c);
                    break;
                default:
                    throw Error.toss(violetPropertyUnexpected, c.get("type"), "c|i|n");
            }
        }

        // Step 4: output associations
        for (Tuple a : db.getTable("vAssociation").tuples()) {
            makeAssociation(a);
        }

        l(" </object>");
        l("</java>");
    }

    static void makeClass(Tuple c) {
        l("  <void method=\"addNode\">");
        l("   <object class=\"com.horstmann.violet.ClassNode\" id=\"%s\">", c.get("id"));

        fields(c);
        methods(c);
        name(c, "      <string>%s</string>");
        l("   </object>");

        positions(c, numPoints);
        numPoints++;
    }

    static void makeInterface(Tuple i) {
        l("  <void method=\"addNode\">");
        l("   <object class=\"com.horstmann.violet.InterfaceNode\" id=\"%s\">", i.get("id"));

        methods(i);
        name(i, "      <string>Â«interfaceÂ»\n%s</string>");
        l("   </object>");

        positions(i, numPoints);
        numPoints++;
    }

    static void makeNote(Tuple n) {
        String id = n.get("id");
        l("  <void method=\"addNode\">");
        if (id.startsWith("Void")) {
            l("   <object class=\"com.horstmann.violet.NoteNode\"/>");
        } else {
            l("   <object class=\"com.horstmann.violet.NoteNode\" id=\"%s\">", id);
            note(n, "      <string>%s</string>");
            l("   </object>");
        }
        positions(n, numPoints);
        numPoints++;
    }

    static void makeAssociation(Tuple a) {
        l("  <void method=\"connect\">");
        l("   <object class=\"com.horstmann.violet.ClassRelationshipEdge\">");
        style(a, "BentStyle", "bentStyle");
        arrow(a, "arrow2", "end");
        property(a, "endLabel", "role2");
        style(a, "LineStyle", "lineStyle");
        property(a, "middleLabel", "middleLabel");
        arrow(a, "arrow1", "start");
        property(a, "startLabel", "role1");
        String cid1 = a.get("cid1");
        String cid2 = a.get("cid2");
        l("   </object>");
        l("   <object idref=\"%s\"/>", cid1);
        l("   <object idref=\"%s\"/>", cid2);
        l("  </void>");
    }

    static void property(Tuple a, String propName, String attr) {
        String prop = a.get(attr);
        if (prop.equals("") == false) {
            l("    <void property=\"%s\">", propName);
            l("     <string>%s</string>", prop.replace(";", ","));
            l("    </void>");
        }
    }

    static void style(Tuple a, String styleName, String attr) {
        String sty = a.get(attr);
        if (sty.equals("") == false) {
            l("    <void property=\"%s\">", attr);
            l("     <object class=\"com.horstmann.violet.%s\" field=\"%s\"/>", styleName, sty);
            l("    </void>");
        }
    }

    static void arrow(Tuple a, String arrow1or2, String startOrEnd) {
        String arr = a.get(arrow1or2);
        if (arr.equals("") == false) {
            l("    <void property=\"%sArrowHead\">", startOrEnd);
            l("     <object class=\"com.horstmann.violet.ArrowHead\" field=\"%s\"/>", arr);
            l("    </void>");
        }
    }

    static void methods(Tuple i) {
        String meth = i.get("methods");
        if (meth.equals("") == false) {
            l("    <void property=\"methods\">");
            l("     <void property=\"text\">");
            l("      <string>%s</string>", meth.replace(NewLine, "\n"));
            l("     </void>");
            l("    </void>");
        }
    }

    static void positions(Tuple i, int numPoints) {
        l("   <object class=\"java.awt.geom.Point2D$Double\" id=\"Point2D$Double%d\">", numPoints);
        l("    <void class=\"java.awt.geom.Point2D$Double\" method=\"getField\">");
        l("     <string>x</string>");
        l("     <void method=\"set\">");
        l("      <object idref=\"Point2D$Double%d\"/>", numPoints);
        l("      <double>%.1f</double>", i.getDouble("x"));
        l("     </void>");
        l("    </void>");
        l("    <void class=\"java.awt.geom.Point2D$Double\" method=\"getField\">");
        l("     <string>y</string>");
        l("     <void method=\"set\">");
        l("      <object idref=\"Point2D$Double%d\"/>", numPoints);
        l("      <double>%.1f</double>", i.getDouble("y"));
        l("     </void>");
        l("    </void>");
        l("    <void method=\"setLocation\">");
        l("     <double>%.1f</double>", i.getDouble("x"));
        l("     <double>%.1f</double>", i.getDouble("y"));
        l("    </void>");
        l("   </object>");
        l("  </void>");
    }

    static void name(Tuple i, String fmt) {
        nameLike("name", i, fmt);
    }

    static void note(Tuple i, String fmt) {
        nameLike("text", i, fmt);
    }

    static void nameLike(String title, Tuple i, String fmt) {
        String contents = i.get("name").replace(NewLine, "\n");
        if (!contents.equals("")) {
            l("    <void property=\"" + title + "\">");
            l("     <void property=\"text\">");
            l(fmt, i.get("name").replace(NewLine, "\n"));
            l("     </void>");
            l("    </void>");
        }
    }

    static void fields(Tuple c) {
        String fields = c.get("fields").replace(NewLine, "\n").replace("\"", "&quot;");
        if (fields.equals("") == false) {
            l("    <void property=\"attributes\">");
            l("     <void property=\"text\">");
            l("      <string>%s</string>", fields);
            l("     </void>");
            l("    </void>");
        }
    }
}