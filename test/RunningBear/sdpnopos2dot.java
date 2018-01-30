package RunningBear;

import MDELite.Marquee2Arguments;
import MDELite.RunningBear;
import PrologDB.Tuple;

public class sdpnopos2dot extends RunningBear {
    
    public static void main(String... args) throws Exception {
        Marquee2Arguments mark = new Marquee2Arguments(sdpnopos2dot.class,"","", args);
        RBSetup(mark, args);

        l("digraph {");
        l("   // classes");
        for( Tuple c : db.getTable("class").tuples()) {
           l("   %s;",c.get("id"));
        }
        
        l("\n   // interfaces");
        for(Tuple i : db.getTable("interface").tuples()) {
            l("   %s;", i.get("id"));
        }
        
        l("\n   // class Implements");
        for(Tuple c : db.getTable("classImplements").tuples()) {
            l("   %s->%s;",c.get("cid"),c.get("iid"));
        }
        
        l("\n   // interface Extends");
        for(Tuple i : db.getTable("interfaceExtends").tuples()) {
            l("${i.extends}->${i.base};",i.get("extends"), i.get("base"));
        }
        
        l("\n   // class Extends");
        for(Tuple c : db.getTable("class").tuples()) {
            if (c.is("superid","null")==false) {
                l("   %s->%s;",c.get("id"), c.get("superid"));
            }
        }

        l("\n   // associations");
        for(Tuple a : db.getTable("association").tuples())
            l("   %s->%s;",a.get("cid1"),a.get("cid2"));
        l("}");
    }
}
//#set($MARKER="//----")
//${MARKER}out.txt
//digraph {
//   // classes 
//#foreach( $c in $classS )
//   ${c.id};
//#end 
//
//   // interfaces 
//#foreach( $i in $interfaceS )
//   ${i.id};
//#end
//
//   // class Implements
//#foreach( $c in $classImplementsS )
//   ${c.cid}->${c.iid};
//#end
//
//   // interface Extends
//#foreach( $i in $interfaceExtendsS )
//   ${i.extends}->${i.base};
//#end
//
//   // class Extends
//#foreach( $c in $classS )
//    #if (($c.superid) !="null" )
//   ${c.id}->${c.superid};
//    #end 
//#end 
//
//   // associations 
//#foreach( $a in $associationS)
//   ${a.cid1}->${a.cid2};
//#end
//}
