package Parsing.DBPrimitives;

import Parsing.GeneralPrimitives.FixedSkip;
import Parsing.Parsers.LineToParse;

/** a Dot is "." token */
public class Dot extends FixedSkip  {
    
    /** Standard constructor */
    public Dot() {} 
    
    /** Standard constructor with line to parse
     * @param l -- line to parse
     */
    public Dot(LineToParse l) {
        super(l);
    }

    @Override
    public String getPattern() {
        return ".";
    }
    
    @Override
    public Dot klone(LineToParse l) {
        return new Dot(l);
    }
    
    @Override
    public String getEmsg() { return "dot"; }
}
