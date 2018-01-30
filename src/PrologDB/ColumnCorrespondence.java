package PrologDB;

import java.util.LinkedList;
import java.util.function.Function;
import MDELite.Error;

public class ColumnCorrespondence {

    public static class Pair {
        boolean which;
        String left;
        String right;
        Function<Tuple,String> fun;

        public Pair(String left, String right) {
            which = true;
            this.left = left;
            this.right = right;
            // fun undefined
        }
        
        public Pair(String left, Function<Tuple,String> fun) {
            which = false;
            this.left = left;
            this.fun = fun;
            // right undefined
        }
    }
    
    LinkedList<Pair> correspondence;
    
    public ColumnCorrespondence() {
        correspondence = new LinkedList<>();
    }
    
    public ColumnCorrespondence add(String left, String right) {
        correspondence.add(new Pair(left,right));
        return this;
    }
    
    public ColumnCorrespondence add(String left, Function<Tuple,String> fun ) {
        correspondence.add(new Pair(left,fun));
        return this;
    }
    
    public ColumnCorrespondence replace(String left, String right) {
        remove(left);
        add(left, right);
        return this;
    }
    
    public ColumnCorrespondence replace(String left, Function<Tuple,String> fun ) {
        remove(left);
        add(left,fun);
        return this;
    }
    
    public void remove(String left) {
        for (int i = 0; i<correspondence.size(); i++) {
            Pair p = correspondence.get(i);
            if (p.left.equals(left)) {
                correspondence.remove(i);
                return;
            }
        }
        throw Error.toss(Error.correspondenceNotFound,left);
    }

}
