package Parsing.GeneralPrimitives;

import Parsing.Parsers.LineToParse;

/** column name token (alpha (alpha|underscore|digit|colon)* */
public class ColumnName extends Token {

    /** Standard constructor */
    public ColumnName() {
    }
    
    /** Standard constructor with line to parse
     * @param l -- line to parse
     */
    public ColumnName(LineToParse l) {
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
            if (!toBeParsed.isColumnNameChar(toBeParsed.toParse.charAt(i))) {
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
    public ColumnName klone(LineToParse l) {
        return new ColumnName(l);
    }
    
    @Override
    public String getEmsg() { return "columnName"; }
}
