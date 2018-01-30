package PrologDB;

import java.util.LinkedList;

/**
 * a TupleList is literally a linked-list of Tuples  
 */
public class TupleList extends LinkedList<Tuple> {
    
   
    
    /**
     * copy tuple list by copying each tuple on the list and returning the
     * copied list.  Precondition: the implicit schema of the tuplelist 
     * must have an identical set of columns as tableSchema. 
     * Only the names of their schemas may be different
     *
     * @param tableSchema -- table schema of the list
     * @return copied tuple list
     */
    public TupleList copy(TableSchema tableSchema) {
        TupleList newtl = new TupleList();
        stream().forEach((t) -> {
            newtl.add(t.copy(tableSchema));
        });
        return newtl;
    }
    
    // Experimental 
    
    public TupleList() {}
    
    public TupleList addTuples(TupleList tl) {
        this.addAll(tl);
        return this;
    }
    
    public Table toTable(Table tab) {
        return new Table(tab, this);
    }
}
