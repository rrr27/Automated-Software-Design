package DML.PrologDB;

import org.junit.Test;


public class ComplexDBExtendTest extends CommonTst {
    
    // a "extend" test is a programmatic creation of a DB schema
    // from an existing DB schema.  (This schema MUST be flattened).
    // It also is an attempt to programmatically extend a DB.
    // So in all of the examples below, a DB (and its schema)
    // is extended.

    public ComplexDBExtendTest() {
    }

    // complex because it has inheritance declaration
    
    @Test
    public void test1() {
        genericExtend("c.complex.pl");
    }
    
    @Test
    public void test2() {
        genericExtend("h.hierarchy.pl");
    }
    
    @Test
    public void test3() {
        genericExtend("i.inh.pl");
    }
}
