package PrologDB;

import MDELite.Utils;
import java.util.HashMap;

/**
 * Unique is a useful utility for evaluating the uniqueness of specific columns
 * of a stream of tuples.
 */
public class Unique {
    /**
     * hashmap of (col,id) pairs for tuples
     */
    HashMap<String,String> hs;
    
    /**
     * name of table
     */
    String tableName;
    
    /**
     * col is name of col column (to be verified for uniqueness)
     */
    String col;
    
    /**
     * id is name of identifier column of table
     */
    String id;
    
    /**
     * er is the error reporter, which accumulates error messages
     */
    ErrorReport er;
    
    /**
     * create a Unique object error reporter that received tuples from
     * table 'table' and verifies that the 'column' of each tuple is unique and
     * not null.  Null-valued and duplicate 'column' values are reported.
     * 
     * @param table -- table to examine
     * @param column -- name of column in which no duplicates should exist
     * @param erReport -- ErrorReport object for accumulating errors
     */
    public Unique(Table table, String column, ErrorReport erReport) {
        tableName = table.getName();
        col = column;
        id = table.getSchema().getIDName();
        er = erReport;
        hs = new HashMap<>();
    }
    
    public Unique(String column, ErrorReport erReport) {
        tableName = null;
        col = column;
        id = null;
        er = erReport;
        hs = new HashMap<>();
    }
    
    /**
     * register the content of a tuple: get its id col, column value,
     * and see if the column value has been seen before; if so, register
     * an error with the error report object.
     * 
     * @param t -- tuple whose contents are to be registered
     */
    public void add(Tuple t) {
        String colValue = t.get(col);
        add(t,colValue);
    }
    
    /**
     * add tuple t whose column is "computed" to be colValue
     * @param t -- tuple to add
     * @param colValue -- value of "computed" attribute
     */
    public void add(Tuple t, String colValue) {
        if (id == null) {
            id = t.getSchema().getIDName();
            tableName = t.getSchema().getName();
        }
        if (colValue==null && colValue.equals("null")) {
            String m = String.format("%s(%s...) has %s = null",
                     tableName,t.get(id),col);
            er.add(m);
            return;
        }
        if (hs.containsKey(colValue)) {
            String s = String.format("%s(%s...) and %s(%s...) both have %s = %s",
                    tableName,t.get(id),tableName,hs.get(colValue),col,colValue);
            er.add(s);
        } else {
            hs.put(colValue,t.get(id));
        }
    }
    
//    /**
//     * generate an error string that two tuples (t1id, t2id) that
//     * share the same value for column col
//     * 
//     * @param value
//     * @param t1id
//     * @param t2id
//     * @return error string to be given to ErrorReporter
//     */
//    
//    String errmsg(Tuple t,String colValue) {
//        return Utils.makeString(
//          "TABLE ( ID1 ...) and TABLE ( ID2 ...) both have COLUMN = VALUE",
////          "unique constraint violated: COLUMN = VALUE is replicated in TABLE tuples ID1 and ID2", 
////          col,colValue,tableName,t.get(id),hs.get(colValue));
//            tableName,t.get(id),tableName,hs.get(colValue),col,colValue);
//
//    }
}
