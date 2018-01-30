package Parsing.DBPrimitives;

import Parsing.GeneralPrimitives.FixedSkip;
import Parsing.Parsers.LineToParse;

/** DBase is "dbase" token */
public class DBase extends FixedSkip {
    
    /** Standard constructor */
    public DBase() { }
    
    /** Standard constructor with line to parse
     * @param l -- line t parse 
     */
    public DBase(LineToParse l) {
        super(l);
    }

    @Override
    public String getPattern() {
        return "dbase";
    }
    
    @Override
    public String getEmsg() { return "dbase"; }
    
    @Override
    public DBase klone(LineToParse l) {
        return new DBase(l);
    }
}
