package Violett;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import MDELite.Error;
import static MDELite.Error.*;
import MDELite.Marquee2Arguments;
import MDELite.Utils;
import PrologDB.DB;
import PrologDB.DBSchema;
import PrologDB.Table;
import PrologDB.Tuple;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/** parser for X.state.violet files into X.fsm.pl database */
public class StateParser {

    static final String StateDiagram = "com.horstmann.violet.StateDiagramGraph";
    static final String violetFSMSchema = Utils.MDELiteHome()+"libpl/fsm.schema.pl";
    static final String StateNode = "com.horstmann.violet.StateNode";
    static final String NoteNode = "com.horstmann.violet.NoteNode";
    static final String InitNode = "com.horstmann.violet.CircularStateNode";
    static final String FinalNode = "com.horstmann.violet.product.diagram.state.CircularFinalStateNode";
    static final String PointNode = "com.horstmann.violet.PointNode";
    static final String Edge = "com.horstmann.violet.StateTransitionEdge";
    static final String NoteEdge = "com.horstmann.violet.NoteEdge";
    static final String Double = "java.awt.geom.Point2D$Double";

    static final String ExpectedNodeType = "state|note|init|final|point";
    static final String ExpectedEdgeType = "arrow|note";

    static HashMap<String, String> Ntype, Etype;

    static {
        Ntype = new HashMap<>();
        Ntype.put(StateNode, "state");
        Ntype.put(NoteNode, "note");
        Ntype.put(InitNode, "init");
        Ntype.put(FinalNode, "final");
        Ntype.put(PointNode, "point");

        Etype = new HashMap<>();
        Etype.put(Edge, "arrow");
        Etype.put(NoteEdge, "note");
    }

    static String NtypeEH(String x) {
        String y = Ntype.get(x);
        if (y == null) {
            throw Error.toss(violetUnexpectedType, x, ExpectedNodeType);
        }
        return y;
    }

    static String EtypeEH(String x) {
        String y = Etype.get(x);
        if (y == null) {
            throw Error.toss(violetUnexpectedType, x, ExpectedEdgeType);
        }
        return y;
    }

    static Table nodeTable, edgeTable;
    static int eid, voidCtr;

    /** parser for X.state.violet files into X.fsm.pl database
     * @param args -- X.state.violet X.fsm.pl 
     */
    public static void main(String... args) {
        PrintStream out;
        String AppName, parseFileName;
        Document doc;

        // Step 0: standard marquee processing
        Marquee2Arguments mark = new Marquee2Arguments(StateParser.class, ".state.violet", ".fsm.pl", args);
        out = mark.getOutputPrintStream();
        parseFileName = mark.getInputFileName();
        File fXmlFile = mark.getInputFile();
        AppName = mark.getAppName(parseFileName);

        // Step 1: read the xml document
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
        } catch (IOException e) {
            throw Error.toss(fileNoExist, parseFileName);
        } catch (ParserConfigurationException | SAXException e) {
            throw Error.toss(violetParsingError, parseFileName, e.getMessage());
        }

        // Step 2: verify that class 
        // is "com.horstmann.violet.StateDiagramGraph", otherwise stop.
        Element mainObj = (Element) doc.getElementsByTagName("object").item(0);
        String diagramType = mainObj.getAttribute("class");
        sanity(StateDiagram, diagramType, violetWrongClass);

        // Step 3: read in violetFSM schema + create empty database
        DBSchema vFSMschema = DBSchema.readSchema(violetFSMSchema);
        DB FSMdb = new DB(AppName, vFSMschema);
        nodeTable = FSMdb.getTableEH("node");
        edgeTable = FSMdb.getTableEH("edge");
        eid = voidCtr = 0;

        // Step 4: get all void nodes -- there are lots of them
        // but we only process "addNode" and "connect" and ignore the rest.
        NodeList nList = mainObj.getElementsByTagName("void");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Element nNode = (Element) nList.item(temp);
            String method = nNode.getAttribute("method");
            switch (method) {
                case "addNode":
                    addNode(nNode);
                    break;
                case "connect":
                    addConnect(nNode);
                    break;
                default:
            }
        }
        FSMdb.print(out);
    }

    /**
     * addNode processes an "addNode" element e, from which we harvest a
     * nodeTable ID and the xpos and ypos location
     *
     */
    static void addNode(Element e) {
        NodeList voidList, nList;
        int cnt;
        String shouldBeText, textValue = "", ntype, color = "", xpos="", ypos="";
        Element first, second=null;

        // Step 1: get all "object" decls within element e
        NodeList allObjects = e.getElementsByTagName("object");

        // Step 2: there are at least two objects, among them 'first' and
        //         'second'. Harvest from first the id and text and if 
        //         StateNode or NoteNode, harvest test if the node type
        //         has it.  Do some sanity checking too to confirm that
        //         the xml structure is what we expect.
        first = (Element) allObjects.item(0);
        String cls = first.getAttribute("class");
        ntype = NtypeEH(cls);
        
        // Step 3: deal with the strange case of multiple init and final
        //         nodes not having identifiers
        String id = first.getAttribute("id");
        if (id.equals("")) {
            id = "Void"+voidCtr++;
        }
        
        if (cls.equals(StateNode) || cls.equals(NoteNode)) {
            voidList = first.getElementsByTagName("void");
            cnt = voidList.getLength();
            switch (cnt) {
                case 2: shouldBeText = ((Element) voidList.item(1)).getAttribute("property");
                        sanity("text", shouldBeText, violetPropertyUnexpected);
                        textValue = getTagValue("string", (Element) voidList.item(1), 0)
                                .replace("\n", "%").replace(" ", "");
                        break;
                case 3: shouldBeText = ((Element) voidList.item(2)).getAttribute("property");
                        sanity("text", shouldBeText, violetPropertyUnexpected);
                        textValue = getTagValue("string", (Element) voidList.item(2), 0)
                                .replace("\n", "%").replace(" ", "");
                        Element colorElement = (Element) voidList.item(0);
                        nList =  colorElement.getElementsByTagName("object");
                        for (int i=0; i<4; i++) {
                            color = color + getTagValue("int", (Element) nList.item(0), i)+ ":";
                        }
                        break;
                default: sanity(2, voidList, cls);
            }
        }

        // Step 3: harvest from "second" object the xpos, ypos values.  Note
        //         that 'second' for PointNodes is different from all other
        //         nodes, making this bit messier than necessary.
        for (int i=0; i<allObjects.getLength(); i++) {
           second = (Element) allObjects.item(i);
           String c = second.getAttribute("class");
           if (c.equals(Double)) {
               break;
           }
        }
        
        voidList = second.getElementsByTagName("void");
        for (int i=0; i<voidList.getLength(); i++) {
            Element ee = (Element) voidList.item(i);
            String m = ee.getAttribute("method");
            if (m.equals("setLocation")) {
                xpos = getTagValue("double", ee, 0);
                ypos = getTagValue("double", ee, 1);
                break;
            }
        }

        // Step 4: form tuple
        Tuple t = new Tuple(nodeTable);
        t.setValues(id, ntype, textValue, color, xpos, ypos);
        nodeTable.add(t);
    }

    private static String getTagValue(String sTag, Element eElement, int index) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(index).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if (nValue == null) {
            return "";
        }
        return nValue.getNodeValue(); //standardizeWhiteSpace(nValue.getNodeValue());
    }

    static void addConnect(Element e) {
        String etype;
        NodeList nList;
        String label = "";

        // Step 1: get 3 objects (that's all of them
        NodeList oList = e.getElementsByTagName("object");
        Element first = ((Element) oList.item(0));
        String cls = first.getAttribute("class");

        // Step 2: get the label, if it exists.  label = "" now.
        nList = first.getElementsByTagName("void");
        if (nList.getLength() == 1) {
            Element prop = ((Element) nList.item(0));
            label = getTagValue("string", prop, 0);
        }

        // Step 3: translate cls into appropriate etype
        etype = EtypeEH(cls);
        String from = ((Element) oList.item(1)).getAttribute("idref");
        String to = ((Element) oList.item(2)).getAttribute("idref");

        // Step 4: form tuple and add it to the edgeTable
        Tuple t = new Tuple(edgeTable);
        t.setValues("e" + eid++, etype, label, from, to);
        edgeTable.add(t);
    }

    static void sanity(String required, String found, Error e) {
        if (!required.equals(found)) {
            throw Error.toss(e, found, required);
        }
    }

    static void sanity(int required, NodeList nlist, String nodeName) {
        int found = nlist.getLength();
        if (required != found) {
            throw Error.toss(violetWrongNumberObjects, nodeName, ""+required, ""+found);
        }
    }
}
