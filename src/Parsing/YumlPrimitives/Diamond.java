/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parsing.YumlPrimitives;

import Parsing.GeneralPrimitives.FixedSave;
import Parsing.Parsers.LineToParse;

/** Yuml Token : Diamond is less-than-greater-than */
public class Diamond extends FixedSave {
    
    public final String tok = "<>";
    
    /** Standard constructor */
    public Diamond() {
        super();
    }
    
    /** Standard constructor with line to parse
     * @param l -- LineToParse
     */
    public Diamond(LineToParse l) {
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
    public Diamond klone(LineToParse l) {
       return new Diamond(l);
    }
}
