package PrologDB;

import MDELite.Error;
import java.util.HashMap;


/**
 * currently unused Table implementation
 */
public class KeyedTable extends Table {

    /**
     * keyName of keyTable
     */
    final public String keyName;
    
    /**
     * here's the hashMap of (key,tuple) pairs
     */
    private final HashMap<String, Tuple> tuples;

    /**
     * returns KeyTable object that is a table of tableSchema instances
     * using a column whose name is keyName as the key column; 
     * Error is thrown if no such column exists
     * 
     * @param tableSchema -- table schema to be instantiated
     * @param keyName -- name of key column of table
     */
    public KeyedTable(TableSchema tableSchema, String keyName) {
        super(tableSchema);
        tableSchema.getColumnEH(keyName);  // make sure column exists
        this.keyName = keyName;
        tuples = new HashMap<>();
        throw Error.toss(Error.tableKeyed);
    }
    
    /**
     * returns the tuple with the given keyValue; null returned if no such
     * keyValue exists
     * 
     * @param keyValue -- search key
     * @return the tuple with the given keyValue; null returned if no such
     * keyValue exists
     */
    public Tuple getKey(String keyValue) {
        return tuples.get(keyValue);
    }

    /** 
     * Error Handling version of getKey; Error returned if no
     * such tuple exists
     * 
     * @param keyValue -- search key
     * @return tuple with given keyValue
     */
    public Tuple getKeyEH(String keyValue) {
        Tuple t = getKey(keyValue);
        if (t != null) {
            return t;
        }
        throw Error.toss(Error.tableNoTupleWithKey, schema.getName(),keyName,keyValue);
    }

    /** 
     * returns copy of key table
     * @return copy of key table
     */
    public KeyedTable copy() {
        throw Error.toss(Error.tableKeyed);
    }
}
