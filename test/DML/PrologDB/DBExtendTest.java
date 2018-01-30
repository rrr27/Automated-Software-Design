package DML.PrologDB;

import org.junit.Test;

public class DBExtendTest extends CommonTst {
    
    // a "extend" test is a programmatic creation of a DB schema
    // from an existing DB schema.  (This schema MUST be flattened).
    // It also is an attempt to programmatically extend a DB.
    // So in all of the examples below, a DB (and its schema)
    // is extended.
    
    // these tests do NOT involve inheritance

    public DBExtendTest() {
    }

    @Test
    public void testNoLinks() {
        genericExtend("noLinks.violetdb.pl");
    }
    
    @Test
    public void testSchool() {
        genericExtend("school.violetdb.pl");
    }
    
    @Test
    public void testviolet() {
        genericExtend("violet.violetdb.pl");
    }
    
    @Test
    public void testwlinks() {
        genericExtend("wlinks.violetdb.pl");
    }
}
