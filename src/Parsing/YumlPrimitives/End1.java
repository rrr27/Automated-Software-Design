package Parsing.YumlPrimitives;

import Parsing.GeneralPrimitives.Nothing;
import Parsing.GeneralPrimitives.Choose1;
import Parsing.Parsers.LineToParse;
import Parsing.GeneralPrimitives.Token;

/**  Yuml token: choose1( diamond, plusplus, hat, lss, nothing ) */
public class End1 extends Choose1 {
    private static Token[] sequence = {new Diamond(), new PlusPlus(), new Hat(), new Lss(), new Nothing()};
    
    /** Standard constructor */
    public End1() {
        super(sequence);
    }
    
    /** Standard constructor with line to parse
     * @param l -- LineToParse
     */
    public End1(LineToParse l) {
        super(l);
    }
}
