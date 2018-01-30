package Parsing.YumlPrimitives;

import Parsing.GeneralPrimitives.Pattern;
import Parsing.Parsers.LineToParse;
import Parsing.GeneralPrimitives.Token;

/** Yuml Production : Barrier() BoxEntry() */
public class BarrierBoxEntry extends Pattern {
    static private Token[] sequence = { new Barrier(), new BoxEntry() };
    
    /** Standard constructor */
    public BarrierBoxEntry() {
        super(sequence);
    }
    
    /** Standard constructor with line to parse
     * @param l -- LineToParse
     */
    public BarrierBoxEntry(LineToParse l) {
        super(l);
    }
}
