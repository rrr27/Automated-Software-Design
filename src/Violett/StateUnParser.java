package Violett;

import static MDELite.Error.violetPropertyUnexpected;
import PrologDB.Tuple;
import MDELite.Error;
import MDELite.Marquee2Arguments;
import static MDELite.RunningBear.RBSetup;

/** Unparser of X.fsm.pl files to X.state.violet */
public class StateUnParser extends MDELite.RunningBear {

    static String NewLine = "%";
    static int numPoints;

    /** Unparser of X.vpl.pl files to X.class.violet
     * @param args -- X.vpl.pl X.class.violet
     */
    public static void main(String... args) {
        numPoints = 0;

        Marquee2Arguments mark = new Marquee2Arguments(StateUnParser.class, ".fsm.pl", ".state.violet", args);
        RBSetup(mark, args);

        // Step 1: output header
        l("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        l("<java version=\"1.8.0_92\" class=\"java.beans.XMLDecoder\">");
        l(" <object class=\"com.horstmann.violet.StateDiagramGraph\">");

        // Step 3: output classes
        for (Tuple n : db.getTable("node").tuples()) {
            switch (n.get("ntype")) {
                case "state":
                    makeState(n);
                    break;
                case "init":
                    makeSpecialState(n, true);
                    break;
                case "final":
                    makeSpecialState(n, false);
                    break;
                case "note":
                    makeNote(n);
                    break;
                case "point":
                    // ignore points
                    break;
                default:
                    throw Error.toss(violetPropertyUnexpected, n.get("type"), "c|i|n");
            }
        }

        // Step 4: output associations
        for (Tuple a : db.getTable("edge").tuples()) {
            makeEdge(a);
        }

        l(" </object>");
        l("</java>");
    }

    static void makeState(Tuple n) {
        l("  <void method=\"addNode\">");
        l("   <object class=\"com.horstmann.violet.StateNode\" id=\"%s\">", n.get("nid"));
        l("    <void property=\"name\">");
        l("     <void property=\"text\">");
        l("      <string>%s</string>", n.get("text").replace(NewLine, "\n"));
        l("     </void>");
        l("    </void>");
        l("   </object>");
        positions(n, numPoints++);
    }

    static void makeSpecialState(Tuple n, boolean init) {
        String cls = "com.horstmann.violet." + (init ? "CircularStateNode" : "product.diagram.state.CircularFinalStateNode");
        l("  <void method=\"addNode\">");
        String id = n.get("nid");
        if (id.startsWith("Void")) {
            l("   <object class=\"%s\"/>", cls);
        } else {
            l("   <object class=\"%s\" id=\"%s\"/>", cls, id);
        }
        positions(n, numPoints++);;
    }

    static void makeNote(Tuple n) {
        String id = n.get("nid");
        l("  <void method=\"addNode\">");
        boolean open = !n.is("text", "") || !n.is("color", "");
        p("   <object class=\"com.horstmann.violet.NoteNode\"");
        if (!id.startsWith("Void")) {
            p(" id=\"%s\"", id);
        }
        if (open) {
            l(">");
            color(n);
            note(n, "      <string>%s</string>");
            l("   </object>");
        } else {
            l("/>");
        }
        positions(n, numPoints++);
    }

    static void makeEdge(Tuple e) { //table(edge,[eid,etype,"label",startid,endid]).
        l("  <void method=\"connect\">");
        String et = e.get("etype");
        String cls = e.get("etype").equals("arrow") ? "com.horstmann.violet.StateTransitionEdge" : "com.horstmann.violet.NoteEdge";

        String prop = e.get("label");
        if (prop.equals("") == true) {
            l("   <object class=\"%s\"/>", cls);
        } else {
            l("   <object class=\"%s\">", cls);
            property(e, "label", "label");
            l("   </object>");
        }
        String startid = e.get("startid");
        String endid = e.get("endid");
        l("   <object idref=\"%s\"/>", startid);
        l("   <object idref=\"%s\"/>", endid);
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

    static void color(Tuple n) {
        String color = n.get("color");
        if (color.equals("") == false) {
            String[] colors = n.get("color").split(":");
            l("    <void property=\"color\">");
            l("     <object class=\"java.awt.Color\">");
            l("      <int>%s</int>", colors[0]);
            l("      <int>%s</int>", colors[1]);
            l("      <int>%s</int>", colors[2]);
            l("      <int>%s</int>", colors[3]);
            l("     </object>");
            l("    </void>");
        }
    }

    static void positions(Tuple i, int numPoints) {
        l("   <object class=\"java.awt.geom.Point2D$Double\" id=\"Point2D$Double%d\">", numPoints);
        l("    <void class=\"java.awt.geom.Point2D$Double\" method=\"getField\">");
        l("     <string>x</string>");
        l("     <void method=\"set\">");
        l("      <object idref=\"Point2D$Double%d\"/>", numPoints);
        l("      <double>%.1f</double>", i.getDouble("xpos"));
        l("     </void>");
        l("    </void>");
        l("    <void class=\"java.awt.geom.Point2D$Double\" method=\"getField\">");
        l("     <string>y</string>");
        l("     <void method=\"set\">");
        l("      <object idref=\"Point2D$Double%d\"/>", numPoints);
        l("      <double>%.1f</double>", i.getDouble("ypos"));
        l("     </void>");
        l("    </void>");
        l("    <void method=\"setLocation\">");
        l("     <double>%.1f</double>", i.getDouble("xpos"));
        l("     <double>%.1f</double>", i.getDouble("ypos"));
        l("    </void>");
        l("   </object>");
        l("  </void>");
    }

    static void name(Tuple i, String fmt) {
        nameLike("text", i, fmt);
    }

    static void note(Tuple i, String fmt) {
        nameLike("text", i, fmt);
    }

    static void nameLike(String title, Tuple i, String fmt) {
        String contents = i.get("text").replace(NewLine, "\n");
        if (!contents.equals("")) {
            l("    <void property=\"" + title + "\">");
            l("     <void property=\"text\">");
            l(fmt, i.get("text").replace(NewLine, "\n"));
            l("     </void>");
            l("    </void>");
        }
    }
}
