package Parsing.YumlPrimitives;

import Parsing.GeneralPrimitives.FixedSkip;
import Parsing.Parsers.LineToParse;

/** Yuml token: Barrier is "|" */
public class Barrier extends FixedSkip  {
    public static String tok = "|";
    
    /** Standard constructor */
    public Barrier() {} 
    
    /** Standard constructor with line to parse
     * @param l -- LineToParse
     */
    public Barrier(LineToParse l) {
        super(l);
    }

    @Override
    public String getPattern() {
        return tok;
    }
    
    @Override
    public Barrier klone(LineToParse l) {
        return new Barrier(l);
    }
    
    @Override
    public String getEmsg() { return tok; }
}
