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

/**
 * parser of X.class.violet files to produce X.vpl.pl database
 */
public class ClassParser {

    static final String ClassDiagram = "com.horstmann.violet.ClassDiagramGraph";
    static final String violetSchema = Utils.MDELiteHome() + "libpl/vpl.schema.pl";
    static final String ClassNode = "com.horstmann.violet.ClassNode";
    static final String InterfaceNode = "com.horstmann.violet.InterfaceNode";
    static final String NoteNode = "com.horstmann.violet.NoteNode";
    static final String Double = "java.awt.geom.Point2D$Double";
    static final String ClassEdge = "com.horstmann.violet.ClassRelationshipEdge";
    static final String BentStyle = "com.horstmann.violet.BentStyle";
    static final String ArrowHead = "com.horstmann.violet.ArrowHead";
    static final String LineStyle = "com.horstmann.violet.LineStyle";
    static final String StartLabel = "startLabel";
    static final String EndLabel = "endLabel";
    static final String MiddleLabel = "middleLabel";

    static Table vBox, vAssociation;
    static int vCntr, aCntr;

    /**
     * parser of X.class.violet files to produce X.vpl.pl database
     *
     * @param args X.class.violet X.vpl.pl
     */
    public static void main(String... args) {
        PrintStream out;
        String AppName, parseFileName;
        Document doc;

        // Step 0: standard marquee processing
        Marquee2Arguments mark = new Marquee2Arguments(ClassParser.class, ".class.violet", ".vpl.pl", args);
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

        // Step 2: verify that the class 
        // is "com.horstmann.violet.StateDiagramGraph", otherwise stop.
        Element mainObj = (Element) doc.getElementsByTagName("object").item(0);
        String diagramType = mainObj.getAttribute("class");
        sanity(ClassDiagram, diagramType, violetWrongClass);

        // Step 3: read in violetFSM schema + create empty database
        DBSchema vplSchema = DBSchema.readSchema(violetSchema);
        DB vplDB = new DB(AppName, vplSchema);
        vBox = vplDB.getTableEH("vBox");
        vAssociation = vplDB.getTableEH("vAssociation");
        vCntr = aCntr = 0;

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
        patch(); // fill in type1 and type2 fields of vAssociation
        vplDB.print(out);
    }

    /**
     * addNode processes an "addNode" element e, from which we harvest a
     * nodeTable ID and the xpos and ypos location
     *
     */
    static void addNode(Element e) {
        NodeList voidList;
        String fields = "", methods = "", name = "", /*ntype, color = "",*/ xpos = "", ypos = "", type;
        Element first, second;

        // Step 1: get all "object" decls within element e
        NodeList allObjects = e.getElementsByTagName("object");

        // Step 2: there are at least two objects, among them 'first' and
        //         'second'. Harvest from first the id and text and if 
        //         StateNode or NoteNode, harvest test if the node type
        //         has it.  Do some sanity checking too to confirm that
        //         the xml structure is what we expect.
        first = (Element) allObjects.item(0);
        second = (Element) allObjects.item(1);
        sanity(4, allObjects, e.getNodeName());

        String cls = second.getAttribute("class");
        sanity(cls, Double, violetUnexpectedType);

        cls = first.getAttribute("class");
        switch (cls) {
            case ClassNode:
                type = "c";
                break;
            case InterfaceNode:
                type = "i";
                break;
            case NoteNode:
                type = "n";
                break;
            default:
                return;
        }

        // Step 3: harvest rest of information from this <object/>
        String id = first.getAttribute("id");
        if (id.equals("")) {
            id = "Void" + vCntr++;
        }
        String shouldBe;
        int indx = 0;
        voidList = first.getElementsByTagName("void");
        if (type.equals("c") || type.equals("i")) {
            if (voidList.getLength() != 0) {
                Element elem = (Element) voidList.item(indx);
                shouldBe = elem.getAttribute("property");
                if (shouldBe.equals("attributes")) {
                    fields = getTagValue("string", (Element) voidList.item(++indx), 0).replace("\n", "%");
                    shouldBe = ((Element) voidList.item(++indx)).getAttribute("property");
                }
                if (shouldBe.equals("methods")) {
                    methods = getTagValue("string", (Element) voidList.item(++indx), 0).replace("\n", "%");
                    shouldBe = ((Element) voidList.item(++indx)).getAttribute("property");
                }
                if (shouldBe.equals("name") || shouldBe.equals("text")) {
                    name = getTagValue("string", (Element) voidList.item(++indx), 0).replace("\n", "").trim();
                    if (name.startsWith("«interface»")) {
                        name = name.substring(11);
                    }
                }
            }
        }
        if (type.equals("n")) {
            if (voidList.getLength() != 0) {
                name = getTagValue("string", (Element) voidList.item(1), 0).replace("\n", "").trim();
            }
        }

        voidList = second.getElementsByTagName("void");
        for (int i = 0; i < voidList.getLength(); i++) {
            Element ee = ((Element) voidList.item(i));
            shouldBe = ee.getAttribute("method");
            if (shouldBe.equals("setLocation")) {
                xpos = getTagValue("double", ee, 0).replace("\n", "%");
                ypos = getTagValue("double", ee, 1).replace("\n", "%");
                break;
            }
        }

        // Step 3: add tuple [id,"AppName","fields","methods",x,y]
        Tuple t = new Tuple(vBox);
        t.setValues(id, type, name, fields, methods, xpos, ypos);
        vBox.add(t);
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
        NodeList nList;
        String startId, role1 = "", arrow1 = "", type1 = "";
        String endId, role2 = "", arrow2 = "", type2 = "";
        String bentStyle = "", lineStyle = "", midLabel = "";

        // Step 1: get 3 objects: the first and the last 2
        NodeList oList = e.getElementsByTagName("object");
        int last = oList.getLength();
        Element relationship = ((Element) oList.item(0));
        Element start = ((Element) oList.item(last - 2));
        Element end = ((Element) oList.item(last - 1));

        // Step 2: sanity checks -- no non-existent pointers startId, endId
        String c1 = relationship.getAttribute("class");
        sanity(c1, ClassEdge, violetUnexpectedType);
        startId = start.getAttribute("idref");
        type1
                = endId = end.getAttribute("idref");
        if (startId.equals("") || endId.equals("")) {
            throw Error.toss(violetMissingAttribute, "idRef");
        }

        // Step 3: get the label, if it exists.  label = "" now.
        nList = relationship.getElementsByTagName("object");
        for (int i = 0; i < nList.getLength(); i++) {
            Element ee = ((Element) nList.item(i));
            String prop = ee.getAttribute("class");
            switch (prop) {
                case BentStyle:
                    bentStyle = ee.getAttribute("field");
                    break;
                case ArrowHead:
                    String field = ee.getAttribute("field");
                    Element ep = (Element) ee.getParentNode();
                    String head = ep.getAttribute("property");
                    if (head.equals("")) {
                        throw Error.toss(violetPropertyUnexpected, "", "property");
                    }
                    if (head.startsWith("start")) {
                        arrow1 = field;
                    } else if (head.startsWith("end")) {
                        arrow2 = field;
                    } else {
                        throw Error.toss(violetPropertyUnexpected, head, "startArrowHead|endArrowHead");
                    }
                    break;
                case LineStyle:
                    lineStyle = ee.getAttribute("field");
                    break;
                default:
                    throw Error.toss(violetPropertyUnexpected, prop, "startArrowHead|endArrowHead");
            }
        }

        // Step 3: get role labels
        nList = relationship.getElementsByTagName("void");
        for (int i = 0; i < nList.getLength(); i++) {
            Element ee = ((Element) nList.item(i));
            String prop = ee.getAttribute("property");
            switch (prop) {
                case StartLabel:
                    role1 = getTagValue("string", ee, 0);
                    continue;
                case EndLabel:
                    role2 = getTagValue("string", ee, 0);
                    continue;
                case MiddleLabel:
                    midLabel = getTagValue("string", ee, 0);
                default:
            }
        }

        // now create a vAssociation tuple
        //id,cid1,"role1","arrow1",type1,cid2,"role2","arrow2",type2,"bentStyle","lineStyle","middlelabel"
        Tuple t = new Tuple(vAssociation);
        t.setValues("A" + aCntr++, startId, type1, role1, arrow1,
                endId, type2, role2, arrow2,
                bentStyle, lineStyle, midLabel);
        vAssociation.add(t);
    }

    static void sanity(String required, String found, Error e) {
        if (!required.equals(found)) {
            throw Error.toss(e, found, required);
        }
    }

    static void sanity(int required, NodeList nlist, String nodeName) {
        int found = nlist.getLength();
        if (required != found) {
            throw Error.toss(violetWrongNumberObjects, nodeName, "" + required, "" + found);
        }
    }

    // type1 and type2 fields in vAssociation tuples were not set.  patch()
    // fixes this
    static void patch() {
        HashMap<String, String> hmap = new HashMap<>();
        vBox.stream().forEach(t -> hmap.put(t.get("id"), t.get("type")));
        for (Tuple a : vAssociation.tuples()) {
            a.set("type1", hmap.get(a.get("cid1")));
            a.set("type2", hmap.get(a.get("cid2")));
        }
    }
}
