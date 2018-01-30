package Parsing.YumlPrimitives;

import Parsing.GeneralPrimitives.Token;
import Parsing.Parsers.LineToParse;

/** name token (alpha (alpha|underscore|digit)* */
public class Role extends Token {

    /** Standard constructor */
    public Role() {
    }
    
    /** Standard constructor with line to parse
     * @param l -- line to parse
     */
    public Role(LineToParse l) {
        super(l);
    }
    
    @Override
    public boolean canParse() { // consume name, if canParse == true
        toBeParsed.AssertNotAtEnd(this);
        char c = toBeParsed.peek();
        return toBeParsed.isRole(c);
    }
    
    @Override
    public void consumeToken() {
        int i;
        for (i = 1; i < toBeParsed.toParse.length(); i++) {
            char c = toBeParsed.toParse.charAt(i);
            if (!toBeParsed.isRole(c)) {
                break;
            }
        }
        toBeParsed.returnSave(i);
    }
    
    @Override
    public void parse() {
        toBeParsed.parseSaveQualified(this);
    }
    
    @Override
    public Role klone(LineToParse l) {
        return new Role(l);
    }
    
    @Override
    public String getEmsg() { return "role"; }
}
