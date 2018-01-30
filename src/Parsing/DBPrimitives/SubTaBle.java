package Parsing.DBPrimitives;

import Parsing.GeneralPrimitives.FixedSkip;
import Parsing.Parsers.LineToParse;

/** subtable token "subtable" */
public class SubTaBle extends FixedSkip {
    
    /** Standard constructor */
    public SubTaBle() { }
    
    /** Standard constructor with line to parse
     * @param l -- line t parse 
     */
    public SubTaBle(LineToParse l) {
        super(l);
    }

    @Override
    public String getPattern() {
        return "subtable";
    }
    
    @Override
    public String getEmsg() { return "subtable"; }
    
    @Override
    public SubTaBle klone(LineToParse l) {
        return new SubTaBle(l);
    }
}
