package PrologDB;

import java.util.HashSet;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * common constraints that arise in MDELite tables
 *
 * @author don
 */
public class Constraints {

    /**
     * verify that all rows in table t have column with unique values
     *
     * @param t table
     * @param column name of column that is unique
     * @param er error reporter
     */
    public static void isUnique(Table t, String column, ErrorReport er) {
        Unique u = new Unique(t, column, er);
        t.forEach(s -> u.add(s));
    }

    /**
     * verify that all rows in table t have unique Ids
     *
     * @param t table
     * @param er error reporter
     */
    public static void UniqueId(Table t, ErrorReport er) {
        HashSet<String> ids = new HashSet<>();
        t.forEach(r -> {
            String id = r.getId();
            if (ids.contains(id)) {
                er.add("multiple tuples with id='%s'", id);
            } else {
                ids.add(id);
            }
        });
    }

    /**
     * ensure that t.column2r points to a legit row in r.columnrid, er is error
     * reporter
     *
     * @param t table with foreign key
     * @param column2r column with foreign key
     * @param r table that is referenced
     * @param columnrid column that is referenced in r
     * @param er error reporter
     */
    public static void isLegit(Table t, String column2r, Table r, String columnrid, ErrorReport er) {
        Table rsmall = r.project(columnrid);
        Table rejects = t.antiSemiJoin(column2r, rsmall, columnrid);
        String tname = t.getName();
        rejects.stream().forEach(k -> er.add("%s(%s...) has %s='%s' : not a legal reference",
                tname, k.getId(), column2r, k.get(column2r)));
    }

    public static void isLegit(Table t, String column2r, Table r, String columnrid, ErrorReport er, Function<Tuple, String> emsg) {
        Table rsmall = r.project(columnrid);
        Table rejects = t.antiSemiJoin(column2r, rsmall, columnrid);
        rejects.stream().forEach(k -> er.add(emsg.apply(k)));
    }

    /**
     * implies: ifpred implies thenpred; violations are reported in error
     * reporter
     *
     * @param t table
     * @param ifpred "if" predicate true
     * @param thenpred "then" predicate that must be true (otherwise violation)
     * @param expl -- String explanation "%s(%s...) has ...violated" where
     * ...violated is filled in
     * @param er -- error reporter
     */
    public static void implies(Table t, Predicate<Tuple> ifpred, Predicate<Tuple> thenpred,
            String expl, ErrorReport er) {
        String tname = t.getName();
        String fmt = "%s(%s...) has " + expl;
        for (Tuple r : t.tuples()) {
            if (ifpred.test(r)) {
                if (thenpred.negate().test(r)) {
                    String rid = r.getId();
                    er.add(fmt, tname, rid);
                }
            }
        }
    }

    /**
     * ifpred -- then error
     *
     * @param t table
     * @param ifpred "if" predicate true (then violation)
     * @param expl -- String explanation "tuple id=%s in %s: ... violated" where
     * ... is filled in
     * @param er -- error reporter
     */
    public static void iftest(Table t, Predicate<Tuple> ifpred,
            String expl, ErrorReport er) {
        implies(t, ifpred, r -> false, expl, er);
    }
    
    /**
     * findBad -- find all tuples in table t that do NOT satisfy ifpred
     *
     * @param t table
     * @param ifpred "if" predicate is false then violation
     * @param expl -- String explanation "tuple id=%s in %s: ... violated" where
     * ... is filled in
     * @param er -- error reporter
     */
    public static void findBad(Table t, Predicate<Tuple> ifpred,
            String expl, ErrorReport er) {
        implies(t, ifpred.negate(), r -> false, expl, er);
    }

    static final String par = "par";
    static final String chd = "chd";

    /**
     * @return empty cycle table (par,chd)
     */
    public static Table makeCycleTable() {
        TableSchema ts = new TableSchema("cycleTable");
        ts.addColumns(par, chd);
        Table ct = new Table(ts);
        return ct;
    }

    /**
     * determines if there are cycles in a cycle table (par,chd), where par =
     * parent id, and chd = child id
     *
     * @param t (par,chd) table
     * @param er error reporter to collect errors
     */
    public static void cycleCheck(Table t, ErrorReport er) {
        for (Tuple r : t.tuples()) {
            Stack<String> stack = new Stack<>();
            String start = r.get(par);
            stack.add(start);
            NodeNameInLoop = null;
            if (cycleCheck(t, start, stack)) {
                String result = NodeNameInLoop;
                while (true) {
                    String top = stack.pop();
                    result = top + "," + result;
                    if (top.equals(NodeNameInLoop)) {
                        break;
                    }
                }

                er.add("Cycle found: [%s]", result);
                return;
            }
        }
    }

    static String NodeNameInLoop;

    private static boolean cycleCheck(Table t, String startingNode, Stack<String> stack) {
        // smaller table is the transitions (startingNode,X) -- the startingNode step in the transition
        // stack contains the set of visited nodes
        Table smaller = t.filter(row -> row.is(par, startingNode));
        for (Tuple s : smaller.tuples()) {
            String lookAhead = s.get(chd);
            if (stack.search(lookAhead) != -1) {
                // we have seen this node before!! found a cycle
                NodeNameInLoop = lookAhead;
                return true;
            } else {
                stack.push(lookAhead);
                if (!cycleCheck(t, lookAhead, stack)) {
                    stack.pop();
                } else {
                    return true;
                }
            }
        }
        return false;
    }
}
