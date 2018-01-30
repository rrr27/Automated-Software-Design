package Parsing.GeneralPrimitives;

import Parsing.Parsers.LineToParse;

/** name token (alpha (alpha|underscore|digit)* */
public class Name extends Token {

    /** Standard constructor */
    public Name() {
    }
    
    /** Standard constructor with line to parse
     * @param l -- line to parse
     */
    public Name(LineToParse l) {
        super(l);
    }
    
    @Override
    public boolean canParse() { // consume name, if canParse == true
        toBeParsed.AssertNotAtEnd(this);
        char c = toBeParsed.peek();
        return toBeParsed.isLetter(c);
    }
    
    @Override
    public void consumeToken() {
        int i;
        for (i = 1; i < toBeParsed.toParse.length(); i++) {
            if (!toBeParsed.isLetterOrDigitOrUnderScore(toBeParsed.toParse.charAt(i))) {
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
    public Name klone(LineToParse l) {
        return new Name(l);
    }
    
    @Override
    public String getEmsg() { return "name"; }
}
