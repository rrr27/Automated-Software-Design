package Parsing.DBPrimitives;

import Parsing.GeneralPrimitives.FixedSkip;
import Parsing.Parsers.LineToParse;

/** RightParen is ")" */
public class RightParen extends FixedSkip {

    /** Standard constructor */
    public RightParen() {
    }
    
    /** Standard constructor with line to parse
     * @param l -- line to parse
     */
    public RightParen(LineToParse l) {
        super(l);
    }
    
    @Override
    public RightParen klone(LineToParse l) {
        return new RightParen(l);
    }
    
    @Override
    public String getPattern() {
        return ")";
    }
    
    @Override
    public String getEmsg() { return "right paren"; }
}
