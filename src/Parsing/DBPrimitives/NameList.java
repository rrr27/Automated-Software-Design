package Parsing.DBPrimitives;

import Parsing.GeneralPrimitives.NonEmptyList;
import Parsing.GeneralPrimitives.Name;
import Parsing.Parsers.LineToParse;

/** a NameList is Name(,Name)* */
public class NameList extends NonEmptyList { // comma separated names
    
    /** Standard constructor */
    public NameList() {
        super(new Name(), new Comma());
    }
    
    /** Standard constructor with line to parse
     * @param l -- line to parse
     */
    public NameList(LineToParse l) {
        super( new Name(), new Comma(), l);
    }

    @Override
    public NameList klone(LineToParse l) {
        return new NameList(l);
    }
}
