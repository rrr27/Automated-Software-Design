package Parsing.YumlPrimitives;

import Parsing.GeneralPrimitives.Nothing;
import Parsing.GeneralPrimitives.Choose1;
import Parsing.Parsers.LineToParse;
import Parsing.GeneralPrimitives.Token;

/** Yuml Token: choose1( diamond, plusplus, hat, gtr, nothing) */
public class End2 extends Choose1 {
    private static Token[] sequence = {new Diamond(), new PlusPlus(), new Hat(), new Gtr(), new Nothing()};
    
    /** Standard constructor */
    public End2() {
        super(sequence);
    }
    
    /** Standard constructor with line to parse
     * @param l -- LineToParse 
     */
    public End2(LineToParse l) {
        super(l);
    }
}
