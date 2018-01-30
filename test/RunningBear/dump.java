package RunningBear;

import MDELite.Marquee2Arguments;
import MDELite.RunningBear;
import static MDELite.RunningBear.RBSetup;
import PrologDB.Table;
import PrologDB.Tuple;

public class dump extends RunningBear {
    public static void main(String... args) {
        Marquee2Arguments mark = new Marquee2Arguments(dump.class,"", "", args);
        RBSetup(mark, args);
        
        l("dbase(nopos,[interfaceExtends,association,class,classImplements,entity,interface]).\n");
        
        l("table(interfaceExtends,[id,base,extends]).");
        Table ie = db.getTableEH("interfaceExtends");
        if (ie.count()==0) {
            l(":- dynamic interfaceExtends/3.");
        } else {
            for( Tuple i : ie.tuples()) {
                l("interfaceExtends(%s,%s,%s).",i.get("id"),i.get("base"),i.get("extends"));
            }
        }
        l();
        
        l("table(association,[id,\"role1\",arrow1,\"role2\",arrow2,cid1,cid2]).");
        Table assoc = db.getTableEH("association");
        if (assoc.count()==0) {
            l(":- dynamic association/7.");
        } else {
            for (Tuple a : assoc.tuples()) {
                l("association(%s,'%s',%s,'%s',%s,%s,%s).",
                        a.get("id"),a.get("role1"),a.get("arrow1"),
                        a.get("arrow2"),a.get("cid1"),a.get("cid2"));
            }
        }
        l();

        l("table(class,[id,\"name\",\"fields\",\"methods\",superid]).");
        Table cls = db.getTableEH("class");
        if (cls.count()==0) {
            l(":- dynamic class/5.");
        } else {
            for( Tuple c : cls.tuples()) {
                l("class(%s,'%s','%s','%s',%s).",
                        c.get("id"),c.get("name"),c.get("fields"),
                        c.get("methods"),c.get("superid"));
            }
        }
        l();
        
        l("table(classImplements,[id,cid,iid]).");
        Table ci = db.getTableEH("classImplements");
        if (ci.count()==0) {
            l(":- dynamic classImplements/3.");
        } else {
            for(Tuple c : ci.tuples()) {
               l("classImplements(%s,%s,%s).", c.get("id"),
                       c.get("cid"),c.get("iid"));
            }
        }
        l();
        
        l("table(entity,[id]).");
        Table et = db.getTableEH("entity");
        if (et.count()==0) {
            l(":- dynamic entity/1.");
        } else {
            for(Tuple e : et.tuples()) {
                l("entity(%s).",e.get("id"));
            }
        }
        l();
        
        l("table(interface,[id,\"name\",\"methods\"]).");
        Table i = db.getTableEH("interface");
        if (i.count()==0) {
            l(":- dynamic interface/3.");
        } else {
            for(Tuple ii : i.tuples()) {
                l("interface(%s,'%s','%s').", 
                        ii.get("id"), ii.get("name"),ii.get("methods"));
            }
        }
    }
    
//table(interface,[id,"name","methods"]).
//#set($done="")
//#foreach( $i in $interfaceS )
//  #set($done="1")
//interface(${i.id},'${i.name}','${i.methods}').
//#end
//
}
//#set($MARKER="//----")
//${MARKER}out.txt
//dbase(nopos,[interfaceExtends,association,class,classImplements,entity,interface]).
//
//table(interfaceExtends,[id,base,extends]).
//#set($done="")
//#foreach( $i in $interfaceExtendsS )
//  #set($done="1")
//interfaceExtends($i.id,$i.base,$i.extends).
//#end
//#if( $done != "1")
//:- dynamic interfaceExtends/3.
//#end
//
//table(association,[id,"role1",arrow1,"role2",arrow2,cid1,cid2]).
//#set($done="")
//#foreach( $a in $associationS)
//  #set($done="1")
//association($a.id,'$a.role1',$a.arrow1,'$a.role2,$a.arrow2,$a.cid1,$a.cid2).
//#end
//#if( $done != "1")
//:- dynamic association/7.
//#end
//
//table(class,[id,"name","fields","methods",superid]).
//#set($done="")
//#foreach( $c in $classS )
//  #set($done="1")
//class($c.id,'$c.name','${c.fields}','${c.methods}',${c.superid}).
//#end 
//#if( $done != "1")
//:- dynamic class/5.
//#end
//
//table(classImplements,[id,cid,iid]).
//#set($done="")
//#foreach( $c in $classImplementsS )
//  #set($done="1")
//classImplements($c.id,$c.cid,$c.iid).
//#end
//#if( $done != "1")
//:- dynamic classImplements/3.
//#end
//
//table(entity,[id]).
//#set($done="")
//#foreach( $c in $entityS )
//  #set($done="1")
//entity($c.id).
//#end 
//#if( $done != "1")
//:- dynamic entity/1.
//#end
//
//table(interface,[id,"name","methods"]).
//#set($done="")
//#foreach( $i in $interfaceS )
//  #set($done="1")
//interface(${i.id},'${i.name}','${i.methods}').
//#end
//
