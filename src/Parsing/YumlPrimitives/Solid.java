package Parsing.YumlPrimitives;

import Parsing.Parsers.LineToParse;
import Parsing.GeneralPrimitives.FixedSave;

/** Yuml token: Solid is "-" */
public class Solid extends FixedSave  {
    
    /** Standard constructor */
    public Solid() {} 
    
    /** Standard constructor with line to parse
     * @param l -- LineToParse
     */
    public Solid(LineToParse l) {
        super(l);
    }

    @Override
    public String getPattern() {
        return "-";
    }
    
    @Override
    public Solid klone(LineToParse l) {
        return new Solid(l);
    }
    
    @Override
    public String getEmsg() { return "-"; }
}
