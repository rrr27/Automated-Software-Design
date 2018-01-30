package PrologDB;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;


/** used as argument to stream collect operation-- to convert streams of tuples
 *  to streams of tables.  Must provide schema of table up front, as it is
 *  possible that an empty table will be produced.  Calling sequence
 *   .collect(new toTable(tableschema)
 */
public class toTable implements Collector<Tuple,TupleList,Table> {
    
    private TableSchema tschema;  // schema of table to produce
    
    /** create table accumulator for given schema
     * @param ts -- table schema
     */
    public toTable(TableSchema ts) {
        tschema = ts;
    }
    
    /** create table accumulator/collector with schema of given table
     * @param t -- table whose schema is to be used
     */
    public toTable(Table t) {
        this(t.schema);
    }
    
    // See http://blog.indrek.io/articles/creating-a-collector-in-java-8/
    
    @Override
    public BiConsumer<TupleList, Tuple> accumulator() {
        return (tl, t) -> tl.add(t);
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }

    @Override
    public BinaryOperator<TupleList> combiner() {
        return TupleList::addTuples;
    }

    @Override
    public Function<TupleList, Table> finisher() {
        return (tl) -> new Table(tschema,tl); 
        };

    @Override
    public Supplier<TupleList> supplier() {
        return TupleList::new;
    }
    
}
