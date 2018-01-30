package Parsing.DBPrimitives;

import Parsing.GeneralPrimitives.FixedSkip;
import Parsing.Parsers.LineToParse;

/** a Comma is a "," token */
public class Comma extends FixedSkip {
    
    /** Standard constructor */
    public Comma() { }
    
    /** Standard constructor with line to parse
     * @param l -- line t parse 
     */
    public Comma(LineToParse l) {
        super(l);
    }

    @Override
    public String getPattern() {
        return ",";
    }
    
    @Override
    public String getEmsg() { return "comma"; }
    
    @Override
    public Comma klone(LineToParse l) {
        return new Comma(l);
    }
}
