package PrologDB;

import MDELite.Error;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * a SubTableSchema object is an ordered pair (table, [list-of-subtables]),
 * which encodes one level of a table inheritance hierarchy.
 */
public class SubTableSchema {

    /**
     * supr is the schema of dbSchema table subs is a list of schemas that are
     * children of the dbSchema table
     */
    private TableSchema supr;
    private final List<TableSchema> subs;
    DBSchema dbSchema = null; // added by DBSchema.addSubtableSchema()

    // ACCESSORS and INFO
    /**
     * @return subtable schemas of a SubTableSchema declaration
     */
    public List<TableSchema> getSubTableSchemas() {
        return subs;
    }

    /**
     * return SubTableSchema of super Table
     *
     * @return SubTableSchema of super Table
     */
    public String getName() {
        return supr.getName();
    }

    /**
     * @return return the TableSchema of super table
     */
    public TableSchema getSuper() {
        return supr;
    }

    /**
     * does this subtable schema include TableSchema s1 as subtable?
     *
     * @param s TableSchema of interest
     * @return true if s1 is a child schema of 'this'
     */
    public boolean contains(TableSchema s) {
        return subs.contains(s);
    }

    // CONSTRUCTORS
    /**
     * create new SubTable object for dbSchema schema supr
     *
     * @param supr is Table object of dbSchema schema
     */
    public SubTableSchema(TableSchema supr) {
        this.supr = supr;
        subs = new LinkedList<>();
    }

    /**
     * create subTableSchema declaration for dbSchema TableSchema supr with
 subtable Schemas subs
     *
     * @param supr TableSchema of dbSchema table
     * @param subs list of TableSchemas of children tables
     */
    public SubTableSchema(TableSchema supr, List<TableSchema> subs) {
        this.supr = supr;
        this.subs = subs;
    }

    private SubTableSchema() {
        supr = null;
        subs = new LinkedList<>();
    }

    // CONSTRUCTOR UTILITIES
    /**
     * adds SubTableSchema sub
     *
     * @param sub -- SubTableSchema to add
     */
    public void addSubTableSchema(TableSchema sub) {
        subs.add(sub);
    }

    /**
     * adding table schemas in bulk as subtables
     *
     * @param tschemas -- list of table schemas that are subtables of 'this'
     */
    public void addSubTableSchemas(TableSchema... tschemas) {
        for (TableSchema ts : tschemas) {
            subs.add(ts);
        }
    }

    // UTILITIES
    /**
     * return table schema from a list of schemas with the given SubTableSchema
     *
     * @param name -- SubTableSchema of table schema to locate
     * @param list -- in a given list
     * @param schema -- name of the DBSchema 
     * @return -- tableschema with 'SubTableSchema'
     */
    static TableSchema getTableSchema(String name, List<TableSchema> list, DBSchema schema) {
        for (TableSchema ts : list) {
            if (ts.getName().equals(name)) {
                return ts;
            }
        }
        return null;
    }

    /** same as TableSchema, except if nothing is found, throw an Error
     * 
     * @param name == 
     * @param list
     * @param schema
     * @return 
     */
    static TableSchema getTableSchemaEH(String name, List<TableSchema> list, DBSchema schema) {
        TableSchema result = getTableSchema(name, list, schema);
        if (result == null) {
            //String dbname = dbSchema==null?"unknown":dbSchema.getName();
            throw Error.toss(Error.subTableNoFind, name, schema.getName());
        }
        return result;
    }

    /**
     * return tableschema with given SubTableSchema from 'this'
     *
     * @param name -- SubTableSchema of table schema
     * @return -- tableschema with given 'SubTableSchema'
     */
    public TableSchema getSubTableSchema(String name) {
        for (TableSchema s : getSubTableSchemas()) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }

    /**
     * copy SubTableSchema by creating a new, pristine version that literally
     * replicates the data, but is specific to a new database schema.
     *
     * @param newSchema a DBSchema for cloning a subtable schema decl
     * @return copy of a SubTableSchema w.r.t. newSchema
     */
    public SubTableSchema copy(DBSchema newSchema) {
        // here's1 the idea --  we can't just copy a subtableschema -- we must
        // translate its supr and subs into corresponding objects of the new schema.

        // Step 1: create an empty copy and get the list of new subschemas
        SubTableSchema copy = new SubTableSchema();
        List<TableSchema> os = newSchema.getTableSchemas();

        // Step 2: translate supr into supr of new TableSchema
        copy.supr = getTableSchemaEH(supr.getName(), os, dbSchema);

        // Step 3: now translate each subschema to the new TableSchema
        for (TableSchema ts : subs) {
            copy.subs.add(getTableSchemaEH(ts.getName(), os, dbSchema));
        }

        // Step 4: return true copy
        return copy;
    }

    /**
     * push columns of dbSchema into subs
     */
    void flatten() {
        List<Column> columns = supr.getColumns();
        for (TableSchema t : subs) {
            Column[] reverseColumns = columns.toArray(new Column[columns.size()]);
            t.addColumnsFirst(reverseColumns);
        }
    }

    /**
     * two SubTableSchema objects are equal iff they both reference the same
     * superTable, and this.subtables \subsetof ts.subtables, and vice versa
     *
     * @param ts -- SubTableSchema to compare with
     * @return true if both reference identical sets of tables
     */
    public boolean equals(SubTableSchema ts) {
        // Step 1: must reference same dbSchema
        if (!ts.getSuper().equals(getSuper())) {
            return false; // dont have the same super
        }

        // Step 2:  ts must reference only subtables in this
        for (TableSchema s1 : ts.getSubTableSchemas()) {
            TableSchema result = getSubTableSchema(s1.getName());
            if (result == null) {
                return false;
            }
        }

        // Step 3: this must reference only subtables in ts
        for (TableSchema s2 : getSubTableSchemas()) {
            TableSchema result = ts.getSubTableSchema(s2.getName());
            if (result == null) {
                return false;
            }
        }

        // if we get this far, the SubTableSchemas are equal
        return true;  // same super SubTableSchema and same subclasses
    }

    /**
     * two SubTableSchema objects are equal iff they both reference the same
     * superTable, and this.subtables \subsetof ts.subtables, and vice versa if
     * unequal, Error is thrown with a reason
     *
     * @param ts -- SubTableSchema to compare with
     */
    public void equalsEH(SubTableSchema ts) {
        // Step 1: must reference same dbSchema
        if (!ts.getSuper().equals(getSuper())) {
            String name1 = ts.getSuper().getName();
            String name2 = getSuper().getName();
            if (name1.equals(name2)) {
                throw Error.toss(Error.subTableNoSameSuperSameNames, name1);
            } else {
                throw Error.toss(Error.subTableNoSameSuperDifferentNames, name1, name2);
            }
        }

        // Step 2:  ts must reference only subtables in this
        for (TableSchema s1 : ts.getSubTableSchemas()) {
            TableSchema result = getSubTableSchema(s1.getName());
            if (result == null) {
                String dbname = dbSchema==null?"unknown":dbSchema.getName();
                throw Error.toss(Error.tableNoExist, s1.getName(), dbname);
            }
        }

        // Step 3: this must reference only subtables in ts
        for (TableSchema s2 : getSubTableSchemas()) {
            TableSchema result = ts.getSubTableSchema(s2.getName());
            if (result == null) {
                String dbname = ts.dbSchema==null?"unknown":ts.dbSchema.getName();
                throw Error.toss(Error.tableNoExist, s2.getName(), dbname);
            }
        }
        // if we get this far, the SubTableSchemas are equal,return
    }

    // PRINT
    /**
     * produce standard string representation of a SubTableSchema declaration
     *
     * @return standard string representation of a SubTableSchema declaration
     */
    public String toString() {
        String result;
        String comma = "";

        result = String.format("subtable(%s,[", supr.getName());
        for (TableSchema s : subs) {
            result = result + String.format("%s%s", comma, s.getName());
            comma = ",";
        }
        result = result + String.format("]).\n");
        return result;
    }

    /**
     * print standard string declaration of SubTableSchema to PrintStream out
     *
     * @param out is output PrintStream
     */
    public void print(PrintStream out) {
        out.print(this);
    }
    
    /** print to Standard out
     */
    public void print() {
        print(System.out);
    }
}
