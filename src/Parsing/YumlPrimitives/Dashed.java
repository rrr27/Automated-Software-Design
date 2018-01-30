package Parsing.YumlPrimitives;

import Parsing.Parsers.LineToParse;
import Parsing.GeneralPrimitives.FixedSave;

/** Yuml token: Solid is "-" */
public class Dashed extends FixedSave  {
    
    /** Standard constructor */
    public Dashed() {} 
    
    /** Standard constructor with line to parse
     * @param l -- LineToParse
     */
    public Dashed(LineToParse l) {
        super(l);
    }

    @Override
    public String getPattern() {
        return "-.-";
    }
    
    @Override
    public Dashed klone(LineToParse l) {
        return new Dashed(l);
    }
    
    @Override
    public String getEmsg() { return "-.-"; }
}
