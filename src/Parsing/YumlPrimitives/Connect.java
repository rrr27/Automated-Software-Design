package Parsing.YumlPrimitives;

import Parsing.GeneralPrimitives.Pattern;
import Parsing.Parsers.LineToParse;
import Parsing.GeneralPrimitives.Choose1;
import Parsing.GeneralPrimitives.Nothing;
import Parsing.GeneralPrimitives.Token;

/** Yuml Production : Connect = [End1] [name] Solid [name] {End2] Box */
public class Connect extends Pattern {
    static private Token[] array = { 
        new Choose1( new End1(), new Nothing() ), 
        new Choose1( new Role(), new Nothing() ), 
        new Choose1( new Dashed(), new Solid() ),
        new Choose1( new Role(), new Nothing() ), 
        new Choose1( new End2(), new Nothing() ),
        new Box() };
    
    /** Standard constructor */
    public Connect() {
        super(array);
    }
    
    /** Standard constructor with line to parse
     * @param l -- LineToParse
     */
    public Connect(LineToParse l) {
        super(l);
    }
}
