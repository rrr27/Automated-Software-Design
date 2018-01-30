package Parsing.YumlPrimitives;

import Parsing.GeneralPrimitives.FixedSave;
import Parsing.Parsers.LineToParse;

/** Yuml token ++ */
public class PlusPlus extends FixedSave {
    public static final String tok = "++";
    
    /** Standard constructor */
    public PlusPlus() {
        super();
    }
    
    /** Standard constructor with line to parse 
     * @param l -- LineToParse
     */
    public PlusPlus(LineToParse l) {
        super(l);
    }

    @Override
    public String getEmsg() {
        return tok;
    }

    @Override
    public String getPattern() {
        return tok;
    }

    @Override
    public PlusPlus klone(LineToParse l) {
       return new PlusPlus(l);
    }
}
