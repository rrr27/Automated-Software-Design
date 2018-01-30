package Parsing.GeneralPrimitives;

import Parsing.Parsers.LineToParse;


/** parse fixed string and save the result */
public abstract class FixedSave extends Token {
    /**
     * this is the string of a token that is expected
     */
    String pattern;
    
    /** this is the string to report in an error */
    String emsg;
    
    /** Standard constructor */
    public FixedSave() {
        pattern = getPattern();
        emsg = getEmsg();
    }
    
    /** Standard constructor with line to parse
     * 
     * @param l -- line to parse
     */
    public FixedSave(LineToParse l) {
        super(l);
        pattern = getPattern();
        emsg = getEmsg();
    }
    
    /** 
     * @return fixed string pattern to parse
     */
    public abstract String getPattern();
    
    @Override
    public boolean canParse() {
        return toBeParsed.startsWith(pattern);
    }

    @Override
    public void consumeToken() {
        toBeParsed.returnSave(pattern.length());
    }
    
    @Override
    public void parse() {
        toBeParsed.parseSaveQualified(this);
    }
    
    @Override
    public abstract FixedSave klone(LineToParse l);
}
