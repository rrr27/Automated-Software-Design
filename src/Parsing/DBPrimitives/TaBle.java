package Parsing.DBPrimitives;

import Parsing.GeneralPrimitives.FixedSkip;
import Parsing.Parsers.LineToParse;

/** table token "table" */
public class TaBle extends FixedSkip {
    
    /** Standard constructor */
    public TaBle() { }
    
    /** Standard constructor with line to parse
     * @param l -- LineToParse
     */
    public TaBle(LineToParse l) {
        super(l);
    }

    @Override
    public String getPattern() {
        return "table";
    }
    
    @Override
    public String getEmsg() { return "table"; }
    
    @Override
    public TaBle klone(LineToParse l) {
        return new TaBle(l);
    }
}
