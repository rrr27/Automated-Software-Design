package Yuml;

import MDELite.Error;
import MDELite.Marquee2Arguments;
import PrologDB.Table;
import PrologDB.Tuple;

/**
 * M2T program for unparsing a yuml specification
 */
public class ClassUnParser extends MDELite.RunningBear {

    /**
     * main prograqm
     *
     * @param args -- args
     */
    public static void main(String... args) {
        String bid, name, fields, methods, members;
        String end1, end2, box1, box2, role1, role2, dash,
                box1name, box2name;

        // Step 1: standard marquee processing
        Marquee2Arguments mark = new Marquee2Arguments(ClassUnParser.class, ".ypl.pl", ".yuml.yuml", args);
        RBSetup(mark, args);

        // Step 2: output classes
        Table box = db.getTableEH("yumlBox");
        for (Tuple c : box.tuples()) {
            if (!c.is("type","c")) {
                continue;  // not class
            }
            bid = c.get("id");
            name = c.get("name");
            fields = yx(c.get("fields"));
            methods = yx(c.get("methods"));
            members = fields + methods;

            // this is an optimization: do not display memberless classes if 
            // they appear in an association
            boolean print = true;
            if (members.equals("")) {
                for (Tuple a : db.getTable("yumlAssociation").tuples()) {
                    if (bid.equals(a.get("box1")) || bid.equals(a.get("box2"))) {
                        print = false;
                        break;
                    }
                }
                if (print) {
                    l("[%s]", name);
                }
            } else {
                l("[%s|%s|%s]", name, fields, methods);
            }
        }

        // Step 3: generate interfaces
        for (Tuple i : box.tuples()) {
            if (!i.is("type","i")) {
                continue;  // not interface
            }
            name = i.get("name");
            methods = yx(i.get("methods"));

            if (methods.equals("")) {
                l("[interface %s]", name);
            } else {
                l("[interface %s||%s]", name, methods);
            }
        }
        
        // Step 4: generate notes
        for (Tuple n : box.tuples()) {
            if (!n.is("type","n")) {
                continue;  // not note
            }
            l("[note: %s]",n.get("name"));
        }

        // Step 5: generate associations
        for (Tuple a : db.getTable("yumlAssociation").tuples()) {
            end1 = a.get("end1");
            end2 = a.get("end2");
            dash = a.get("lineType");
            box1 = a.get("box1");
            box2 = a.get("box2");
            role1 = yx(a.get("role1"));
            role2 = yx(a.get("role2"));

            l("[%s]%s%s%s%s%s[%s]", boxName(box1,box), end1, role1, dash, role2, end2, boxName(box2,box));
        }
    }
    
    private static String boxName(String bid, Table box) { 
        Tuple b = box.getFirstEH(t->t.is("id",bid));
        switch (b.get("type")) {
            case "c" : return b.get("name"); 
            case "i" : return "interface "+ b.get("name");
            case "n" : return "note: "+ b.get("name");
        }
        throw Error.toss(Error.yumlUnknownType, b.get("type"));
    }
    
    // Yuml doesn't like double quotes in any specification
    // qx removes double quotes (") and replaces them with single quotes (')
    // also, Yuml doesn't like commas in any specification
    // qx replaces (,) with ( ) (blank)
    private static final char squote = '\'';
    private static String yx(String x) {
        return x.replace('\"',squote).replace(',', ' ');
    }
}

////## translates yuml.pl database to .yuml
////##
////##
////#set($debug=false)
////#set($MARKER="//----")
////${MARKER}${OutputFileName}
////##
////### generate classes
////##
////#if ($debug)
//////classes
////#end
////#foreach($c in $yumlClassS)
////    #set($n=$c.name)
////    #set($fields=$c.fields )
////    #set($methods=$c.methods)
////    #set($members=$fields+$methods)
////    #if ($members=="")
////##
////## this is an optimization -- display classes if they 
////## do NOT appear in any association
////        #set($print = true)
////        #foreach($a in $yumlAssociationS)
////            #if ($c.name==$a.name1)
////                #set($print=false)
////            #end
////            #if ($c.name==$a.name2)
////                #set($print=false)
////            #end
////        #end
////        #if ($print==true)
////[$n]
////        #end
////        #else
////[$n|$fields|$methods]
////    #end
////#end
////## generate interfaces
////##
////#if ($debug)
////// interfaces
////#end
////#foreach($i in $yumlInterfaceS)
////##--debug $i
////    #set($iname=$i.name)
////    #set($methods=$i.methods)
////    #if ($methods=="")
////[<<interface>> $iname]
////    #else
////[<<interface>> $iname||$methods]
////    #end
////#end
////##
////## generate associations, compositions, and aggregations
////##
////#if ($debug)
////// associations, compositions, and aggregations
////#end
////#foreach($a in $yumlAssociationS)
////##-- debug $a
////    #set($arrow1=$a.end1)
////    #set($arrow2=$a.end2)
////    #set($cid1name=$a.name1)
////    #set($cid2name=$a.name2)
////    #set($role1=$a.role1)
////    #set($role2=$a.role2)
////## now compute dashed lines.  A dashed line is used iff
////## box1 is of a class and box2 is of an interface or vice versa
////    #set($isInterface1=false)
////    #set($isInterface2=false)
////    #foreach($i in $yumlInterfaceS)
////        #if ($i.name.equals($a.name1))
////            #set($isInterface1=true)
////            #set($cid1name= "<<interface>>;"+$cid1name)
////        #end
////        #if ($i.name.equals($a.name2))
////            #set($isInterface2=true)
////            #set($cid2name= "<<interface>>;"+$cid2name)
////        #end
////    #end
////    #set($dashed=($isInterface2 != $isInterface1))
////    #if ($dashed)
////[$cid1name]${end1}${role1}-.-${role2}${end2}[$cid2name]
////    #else
////[$cid1name]${end1}${role1}-${role2}${end2}[$cid2name]
////    #end
////#end
